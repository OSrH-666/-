package com.example.unionsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void initDatabase() {
        String dbPath = datasourceUrl.replace("jdbc:sqlite:", "");
        File dbFile = new File(dbPath);
        File dataDir = dbFile.getParentFile();
        
        if (dataDir != null && !dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        if (!dbFile.exists()) {
            try (Connection conn = DriverManager.getConnection(datasourceUrl);
                 Statement stmt = conn.createStatement()) {
                createTables(stmt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createTables(Statement stmt) throws Exception {
        stmt.execute("CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "real_name VARCHAR(50)," +
                "student_id VARCHAR(20)," +
                "email VARCHAR(100)," +
                "phone VARCHAR(20)," +
                "role VARCHAR(20) DEFAULT 'STUDENT'," +
                "status INTEGER DEFAULT 1," +
                "grade VARCHAR(10)," +
                "major VARCHAR(100)," +
                "class_name VARCHAR(50)," +
                "avatar_url VARCHAR(500)," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        
        stmt.execute("CREATE TABLE IF NOT EXISTS role (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "code VARCHAR(50) NOT NULL UNIQUE," +
                "description VARCHAR(200))");
        
        stmt.execute("CREATE TABLE IF NOT EXISTS permission (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "code VARCHAR(50) NOT NULL UNIQUE," +
                "description VARCHAR(200))");
        
        stmt.execute("CREATE TABLE IF NOT EXISTS role_permission (" +
                "role_id INTEGER," +
                "permission_id INTEGER," +
                "PRIMARY KEY (role_id, permission_id))");
        
        stmt.execute("CREATE TABLE IF NOT EXISTS user_role (" +
                "user_id INTEGER," +
                "role_id INTEGER," +
                "PRIMARY KEY (user_id, role_id))");
        
        stmt.execute("INSERT OR IGNORE INTO role (id, name, code, description) VALUES " +
                "(1, '系统管理员', 'ADMIN', '系统最高权限管理员')," +
                "(2, '社团负责人', 'CLUB_MANAGER', '社团/学生会负责人')," +
                "(3, '普通用户', 'STUDENT', '普通学生用户')");
        
        stmt.execute("INSERT OR IGNORE INTO permission (id, name, code, description) VALUES " +
                "(1, '用户管理', 'user:manage', '管理所有用户')," +
                "(2, '社团管理', 'club:manage', '管理社团信息')," +
                "(3, '活动管理', 'activity:manage', '管理活动信息')," +
                "(4, '报名管理', 'registration:manage', '管理报名信息')," +
                "(5, '签到管理', 'checkin:manage', '管理签到信息')," +
                "(6, '总结管理', 'summary:manage', '管理活动总结')," +
                "(7, '查看用户', 'user:view', '查看用户信息')," +
                "(8, '查看社团', 'club:view', '查看社团信息')," +
                "(9, '查看活动', 'activity:view', '查看活动信息')," +
                "(10, '报名活动', 'activity:register', '报名活动')," +
                "(11, '签到', 'checkin:do', '进行签到')");
        
        stmt.execute("INSERT OR IGNORE INTO role_permission (role_id, permission_id) VALUES " +
                "(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11)," +
                "(2,2),(2,3),(2,4),(2,5),(2,6),(2,8),(2,9),(2,10),(2,11)," +
                "(3,7),(3,8),(3,9),(3,10),(3,11)");
        
        String encodedPassword = passwordEncoder.encode("123456");
        stmt.execute("INSERT OR IGNORE INTO user (id, username, password, real_name, student_id, email, phone, role, status) VALUES " +
                "(1, 'admin', '" + encodedPassword + "', '系统管理员', '202400000000', 'admin@example.com', '13800138000', 'ADMIN', 1)," +
                "(2, 'yangsiqin', '" + encodedPassword + "', '杨思勤', '202405567014', 'yangsiqin@example.com', '13800138001', 'CLUB_MANAGER', 1)," +
                "(3, 'guhua', '" + encodedPassword + "', '古华', '202405567034', 'guhua@example.com', '13800138002', 'CLUB_MANAGER', 1)," +
                "(4, 'helinhan', '" + encodedPassword + "', '何林晗', '202405567008', 'helinhan@example.com', '13800138003', 'STUDENT', 1)," +
                "(5, 'jiangnanzhu', '" + encodedPassword + "', '蒋南柱', '202405567007', 'jiangnanzhu@example.com', '13800138004', 'STUDENT', 1)," +
                "(6, 'pengzihao', '" + encodedPassword + "', '彭子皓', '202405567019', 'pengzihao@example.com', '13800138005', 'STUDENT', 1)," +
                "(7, 'zhangsan', '" + encodedPassword + "', '张三', '202405567020', 'zhangsan@example.com', '13800138006', 'STUDENT', 1)," +
                "(8, 'lisi', '" + encodedPassword + "', '李四', '202405567021', 'lisi@example.com', '13800138007', 'STUDENT', 1)");
        
        stmt.execute("INSERT OR IGNORE INTO user_role (user_id, role_id) VALUES " +
                "(1,1),(2,2),(3,2),(4,3),(5,3),(6,3),(7,3),(8,3)");

        stmt.execute("CREATE TABLE IF NOT EXISTS club (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT NULL," +
                "type INTEGER DEFAULT 0," +
                "description TEXT," +
                "logo_url VARCHAR(500)," +
                "status INTEGER DEFAULT 0," +
                "founder_id INTEGER," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS club_member (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "club_id INTEGER," +
                "user_id INTEGER," +
                "role INTEGER DEFAULT 0," +
                "status INTEGER DEFAULT 1," +
                "joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS activity (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "club_id INTEGER," +
                "name VARCHAR(100) NOT NULL," +
                "description TEXT," +
                "start_time TIMESTAMP," +
                "end_time TIMESTAMP," +
                "location VARCHAR(200)," +
                "quota INTEGER DEFAULT 50," +
                "status INTEGER DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS registration (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "activity_id INTEGER," +
                "user_id INTEGER," +
                "status INTEGER DEFAULT 1," +
                "queue_position INTEGER DEFAULT 0," +
                "registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "confirmed_at TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS check_in (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "registration_id INTEGER," +
                "check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "method INTEGER DEFAULT 0," +
                "latitude DOUBLE," +
                "longitude DOUBLE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS summary (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "activity_id INTEGER," +
                "content TEXT," +
                "attachment_url VARCHAR(500)," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        
        stmt.execute("INSERT OR IGNORE INTO club (id, name, type, description, logo_url, status, founder_id) VALUES " +
                "(1, '计算机科技协会', 0, '致力于推广计算机技术，组织技术交流活动', 'https://example.com/logo/ct.png', 1, 2)," +
                "(2, '文艺表演社团', 0, '组织各类文艺演出，培养艺术人才', 'https://example.com/logo/art.png', 1, 3)," +
                "(3, '志愿者协会', 0, '组织各类志愿服务活动', 'https://example.com/logo/volunteer.png', 1, 2)," +
                "(4, '学生会', 1, '学生自治组织，服务全体学生', 'https://example.com/logo/student.png', 1, 2)");
        
        stmt.execute("INSERT OR IGNORE INTO club_member (id, club_id, user_id, role, status, joined_at) VALUES " +
                "(1, 1, 2, 1, 1, '2024-09-01 08:00:00')," +
                "(2, 1, 4, 0, 1, '2024-09-05 10:00:00')," +
                "(3, 1, 5, 0, 1, '2024-09-06 14:00:00')," +
                "(4, 2, 3, 1, 1, '2024-09-01 09:00:00')," +
                "(5, 2, 6, 0, 1, '2024-09-08 11:00:00')," +
                "(6, 3, 2, 1, 1, '2024-09-01 10:00:00')," +
                "(7, 3, 7, 0, 1, '2024-09-10 09:00:00')," +
                "(8, 4, 2, 2, 1, '2024-09-01 11:00:00')");
        
        stmt.execute("INSERT OR IGNORE INTO activity (id, club_id, name, description, start_time, end_time, location, quota, status) VALUES " +
                "(1, 1, 'AI技术分享会', '邀请业界专家分享AI最新技术发展', '2024-12-20 14:00:00', '2024-12-20 17:00:00', '学术报告厅A', 100, 1)," +
                "(2, 1, '编程大赛', '年度编程竞赛，展示编程技能', '2024-12-25 09:00:00', '2024-12-25 17:00:00', '计算机学院实验室', 50, 0)," +
                "(3, 2, '迎新晚会', '欢迎新生加入的文艺晚会', '2024-10-01 19:00:00', '2024-10-01 22:00:00', '大学生活动中心', 500, 2)," +
                "(4, 3, '社区志愿服务', '关爱孤寡老人志愿服务活动', '2024-11-15 08:00:00', '2024-11-15 12:00:00', '阳光社区', 30, 1)," +
                "(5, 4, '学生代表大会', '年度学生代表大会', '2024-12-10 14:00:00', '2024-12-10 18:00:00', '大礼堂', 200, 1)");
        
        stmt.execute("INSERT OR IGNORE INTO registration (id, activity_id, user_id, status, queue_position, registered_at, confirmed_at) VALUES " +
                "(1, 1, 4, 1, 0, '2024-12-01 10:00:00', '2024-12-01 10:00:00')," +
                "(2, 1, 5, 1, 0, '2024-12-01 11:00:00', '2024-12-01 11:00:00')," +
                "(3, 1, 6, 1, 0, '2024-12-02 09:00:00', '2024-12-02 09:00:00')," +
                "(4, 1, 7, 1, 0, '2024-12-02 14:00:00', '2024-12-02 14:00:00')," +
                "(5, 3, 4, 1, 0, '2024-09-15 08:00:00', '2024-09-15 08:00:00')," +
                "(6, 3, 5, 1, 0, '2024-09-15 09:00:00', '2024-09-15 09:00:00')," +
                "(7, 4, 7, 1, 0, '2024-11-01 10:00:00', '2024-11-01 10:00:00')," +
                "(8, 4, 8, 1, 0, '2024-11-02 11:00:00', '2024-11-02 11:00:00')," +
                "(9, 5, 4, 1, 0, '2024-12-05 14:00:00', '2024-12-05 14:00:00')," +
                "(10, 5, 7, 1, 0, '2024-12-05 15:00:00', '2024-12-05 15:00:00')");
        
        stmt.execute("INSERT OR IGNORE INTO check_in (id, registration_id, check_in_time, method, latitude, longitude) VALUES " +
                "(1, 5, '2024-10-01 18:30:00', 0, 30.5728, 104.0668)," +
                "(2, 6, '2024-10-01 18:45:00', 0, 30.5729, 104.0669)," +
                "(3, 7, '2024-11-15 08:15:00', 1, 30.5680, 104.0580)," +
                "(4, 8, '2024-11-15 08:20:00', 1, 30.5681, 104.0581)");
        
        stmt.execute("INSERT OR IGNORE INTO summary (id, activity_id, content, attachment_url) VALUES " +
                "(1, 3, '本次迎新晚会圆满成功！共有500余名观众到场观看，节目形式多样，包括歌曲、舞蹈、小品等15个节目。晚会不仅展示了新生的才艺，也增强了同学们之间的交流与团结。感谢所有参与演出的同学和工作人员的辛勤付出！', 'https://example.com/summary/20240901.pdf')");
    }
}
