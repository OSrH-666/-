package com.example.unionsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

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
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS club (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT NULL," +
                "description TEXT," +
                "type VARCHAR(20) DEFAULT 'CLUB'," +
                "status VARCHAR(20) DEFAULT 'PENDING'," +
                "manager_id INTEGER," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS activity (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT NULL," +
                "description TEXT," +
                "club_id INTEGER," +
                "start_time TIMESTAMP," +
                "end_time TIMESTAMP," +
                "location VARCHAR(200)," +
                "max_participants INTEGER DEFAULT 50," +
                "status VARCHAR(20) DEFAULT 'DRAFT'," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS registration (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "activity_id INTEGER," +
                "user_id INTEGER," +
                "status VARCHAR(20) DEFAULT 'REGISTERED'," +
                "queue_position INTEGER DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.execute("CREATE TABLE IF NOT EXISTS check_in (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "registration_id INTEGER," +
                "check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "method VARCHAR(20) DEFAULT 'MANUAL')");

        stmt.execute("CREATE TABLE IF NOT EXISTS summary (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "activity_id INTEGER," +
                "content TEXT," +
                "attachment_url VARCHAR(500)," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }
}