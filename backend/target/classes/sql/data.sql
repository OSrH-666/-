-- 大学生社团学生会信息管理系统 - 初始化数据

-- 插入角色数据
INSERT INTO role (name, code, description) VALUES 
('系统管理员', 'ADMIN', '系统最高权限管理员'),
('社团负责人', 'CLUB_MANAGER', '社团/学生会负责人'),
('普通用户', 'STUDENT', '普通学生用户');

-- 插入权限数据
INSERT INTO permission (name, code, description) VALUES 
('用户管理', 'user:manage', '管理所有用户'),
('社团管理', 'club:manage', '管理社团信息'),
('活动管理', 'activity:manage', '管理活动信息'),
('报名管理', 'registration:manage', '管理报名信息'),
('签到管理', 'checkin:manage', '管理签到信息'),
('总结管理', 'summary:manage', '管理活动总结'),
('查看用户', 'user:view', '查看用户信息'),
('查看社团', 'club:view', '查看社团信息'),
('查看活动', 'activity:view', '查看活动信息'),
('报名活动', 'activity:register', '报名活动'),
('签到', 'checkin:do', '进行签到');

-- 插入角色权限关联数据
-- 管理员权限
INSERT INTO role_permission (role_id, permission_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11);

-- 社团负责人权限
INSERT INTO role_permission (role_id, permission_id) VALUES 
(2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 8), (2, 9), (2, 10), (2, 11);

-- 普通用户权限
INSERT INTO role_permission (role_id, permission_id) VALUES 
(3, 7), (3, 8), (3, 9), (3, 10), (3, 11);

-- 插入用户数据 (密码为 BCrypt 加密的 '123456')
INSERT INTO user (username, password, real_name, student_id, email, phone, role, status) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '系统管理员', '202400000000', 'admin@example.com', '13800138000', 'ADMIN', 1),
('yangsiqin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '杨思勤', '202405567014', 'yangsiqin@example.com', '13800138001', 'CLUB_MANAGER', 1),
('guhua', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '古华', '202405567034', 'guhua@example.com', '13800138002', 'CLUB_MANAGER', 1),
('helinhan', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '何林晗', '202405567008', 'helinhan@example.com', '13800138003', 'STUDENT', 1),
('jiangnanzhu', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '蒋南柱', '202405567007', 'jiangnanzhu@example.com', '13800138004', 'STUDENT', 1),
('pengzihao', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '彭子皓', '202405567019', 'pengzihao@example.com', '13800138005', 'STUDENT', 1),
('zhangsan', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '张三', '202405567020', 'zhangsan@example.com', '13800138006', 'STUDENT', 1),
('lisi', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '李四', '202405567021', 'lisi@example.com', '13800138007', 'STUDENT', 1);

-- 插入用户角色关联数据
INSERT INTO user_role (user_id, role_id) VALUES 
(1, 1),  -- admin 是系统管理员
(2, 2),  -- 杨思勤 是社团负责人
(3, 2),  -- 古华 是社团负责人
(4, 3),  -- 何林晗 是普通用户
(5, 3),  -- 蒋南柱 是普通用户
(6, 3),  -- 彭子皓 是普通用户
(7, 3),  -- 张三 是普通用户
(8, 3);  -- 李四 是普通用户

-- 插入社团数据
INSERT INTO club (name, type, description, logo_url, status, founder_id) VALUES 
('计算机科技协会', 0, '致力于推广计算机技术，组织技术交流活动', 'https://example.com/logo/ct.png', 1, 2),
('文艺表演社团', 0, '组织各类文艺演出，培养艺术人才', 'https://example.com/logo/art.png', 1, 3),
('志愿者协会', 0, '组织各类志愿服务活动', 'https://example.com/logo/volunteer.png', 1, 2),
('学生会', 1, '学生自治组织，服务全体学生', 'https://example.com/logo/student.png', 1, 2);

-- 插入社团成员数据
INSERT INTO club_member (club_id, user_id, role, status, joined_at) VALUES 
(1, 2, 1, 1, '2024-09-01 08:00:00'),  -- 杨思勤是计算机科技协会负责人
(1, 4, 0, 1, '2024-09-05 10:00:00'),  -- 何林晗是成员
(1, 5, 0, 1, '2024-09-06 14:00:00'),  -- 蒋南柱是成员
(2, 3, 1, 1, '2024-09-01 09:00:00'),  -- 古华是文艺表演社团负责人
(2, 6, 0, 1, '2024-09-08 11:00:00'),  -- 彭子皓是成员
(3, 2, 1, 1, '2024-09-01 10:00:00'),  -- 杨思勤是志愿者协会负责人
(3, 7, 0, 1, '2024-09-10 09:00:00'),  -- 张三是成员
(4, 2, 2, 1, '2024-09-01 11:00:00');  -- 杨思勤是学生会管理员

-- 插入活动数据
INSERT INTO activity (club_id, name, description, start_time, end_time, location, quota, status) VALUES 
(1, 'AI技术分享会', '邀请业界专家分享AI最新技术发展', '2024-12-20 14:00:00', '2024-12-20 17:00:00', '学术报告厅A', 100, 1),
(1, '编程大赛', '年度编程竞赛，展示编程技能', '2024-12-25 09:00:00', '2024-12-25 17:00:00', '计算机学院实验室', 50, 0),
(2, '迎新晚会', '欢迎新生加入的文艺晚会', '2024-10-01 19:00:00', '2024-10-01 22:00:00', '大学生活动中心', 500, 2),
(3, '社区志愿服务', '关爱孤寡老人志愿服务活动', '2024-11-15 08:00:00', '2024-11-15 12:00:00', '阳光社区', 30, 1),
(4, '学生代表大会', '年度学生代表大会', '2024-12-10 14:00:00', '2024-12-10 18:00:00', '大礼堂', 200, 1);

-- 插入报名数据
INSERT INTO registration (activity_id, user_id, status, queue_position, registered_at, confirmed_at) VALUES 
(1, 4, 1, 0, '2024-12-01 10:00:00', '2024-12-01 10:00:00'),   -- 何林晗报名AI分享会
(1, 5, 1, 0, '2024-12-01 11:00:00', '2024-12-01 11:00:00'),   -- 蒋南柱报名AI分享会
(1, 6, 1, 0, '2024-12-02 09:00:00', '2024-12-02 09:00:00'),   -- 彭子皓报名AI分享会
(1, 7, 1, 0, '2024-12-02 14:00:00', '2024-12-02 14:00:00'),   -- 张三报名AI分享会
(3, 4, 1, 0, '2024-09-15 08:00:00', '2024-09-15 08:00:00'),   -- 何林晗报名迎新晚会
(3, 5, 1, 0, '2024-09-15 09:00:00', '2024-09-15 09:00:00'),   -- 蒋南柱报名迎新晚会
(4, 7, 1, 0, '2024-11-01 10:00:00', '2024-11-01 10:00:00'),   -- 张三报名志愿服务
(4, 8, 1, 0, '2024-11-02 11:00:00', '2024-11-02 11:00:00'),   -- 李四报名志愿服务
(5, 4, 1, 0, '2024-12-05 14:00:00', '2024-12-05 14:00:00'),   -- 何林晗报名学生代表大会
(5, 7, 1, 0, '2024-12-05 15:00:00', '2024-12-05 15:00:00');   -- 张三报名学生代表大会

-- 插入签到数据
INSERT INTO check_in (registration_id, check_in_time, method, latitude, longitude) VALUES 
(5, '2024-10-01 18:30:00', 0, 30.5728, 104.0668),   -- 何林晗扫码签到迎新晚会
(6, '2024-10-01 18:45:00', 0, 30.5729, 104.0669),   -- 蒋南柱扫码签到迎新晚会
(7, '2024-11-15 08:15:00', 1, 30.5680, 104.0580),   -- 张三定位签到志愿服务
(8, '2024-11-15 08:20:00', 1, 30.5681, 104.0581);   -- 李四定位签到志愿服务

-- 插入活动总结数据
INSERT INTO summary (activity_id, content, attachment_url) VALUES 
(3, '本次迎新晚会圆满成功！共有500余名观众到场观看，节目形式多样，包括歌曲、舞蹈、小品等15个节目。晚会不仅展示了新生的才艺，也增强了同学们之间的交流与团结。感谢所有参与演出的同学和工作人员的辛勤付出！', 'https://example.com/summary/20240901.pdf');
