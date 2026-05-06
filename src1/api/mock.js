export const mockUsers = [
  { id: 1, username: 'student', password: '123456', name: '学生用户', role: 'student' },
  { id: 2, username: 'leader', password: '123456', name: '负责人用户', role: 'leader' },
  { id: 3, username: 'admin', password: '123456', name: '管理员用户', role: 'admin' }
]

export const mockClubs = [
  { id: 1, name: '计算机协会', description: '专注于计算机技术交流', logo: '', status: 'approved', createdAt: '2024-01-15' },
  { id: 2, name: '篮球俱乐部', description: '热爱篮球运动', logo: '', status: 'approved', createdAt: '2024-02-20' },
  { id: 3, name: '音乐社', description: '音乐爱好者聚集地', logo: '', status: 'pending', createdAt: '2024-03-10' },
  { id: 4, name: '志愿者协会', description: '参与公益活动', logo: '', status: 'approved', createdAt: '2024-01-05' },
  { id: 5, name: '摄影协会', description: '记录美好瞬间', logo: '', status: 'pending', createdAt: '2024-03-15' }
]

export const mockActivities = [
  { id: 1, title: '编程大赛', clubId: 1, description: '年度编程竞赛', startTime: '2024-04-01', endTime: '2024-04-02', status: 'active', participants: 120 },
  { id: 2, title: '篮球友谊赛', clubId: 2, description: '校内篮球比赛', startTime: '2024-04-10', endTime: '2024-04-10', status: 'active', participants: 40 },
  { id: 3, title: '音乐会', clubId: 3, description: '春季音乐会', startTime: '2024-05-01', endTime: '2024-05-01', status: 'pending', participants: 0 },
  { id: 4, title: '志愿者日', clubId: 4, description: '社区志愿服务', startTime: '2024-04-15', endTime: '2024-04-15', status: 'completed', participants: 50 },
  { id: 5, title: '摄影展', clubId: 5, description: '校园摄影作品展', startTime: '2024-05-15', endTime: '2024-05-20', status: 'pending', participants: 0 }
]

export const mockRegistrations = [
  { id: 1, activityId: 1, userId: 1, userName: '学生A', status: 'approved', registeredAt: '2024-03-20' },
  { id: 2, activityId: 1, userId: 2, userName: '学生B', status: 'approved', registeredAt: '2024-03-21' },
  { id: 3, activityId: 2, userId: 1, userName: '学生A', status: 'pending', registeredAt: '2024-03-25' },
  { id: 4, activityId: 4, userId: 3, userName: '学生C', status: 'completed', registeredAt: '2024-04-10' },
  { id: 5, activityId: 1, userId: 3, userName: '学生C', status: 'approved', registeredAt: '2024-03-22' }
]

export const mockSummaries = [
  { id: 1, activityId: 4, content: '活动圆满成功，参与人数50人', images: [], createdAt: '2024-04-16' },
  { id: 2, activityId: 2, content: '比赛顺利结束，决出前三名', images: [], createdAt: '2024-04-11' }
]