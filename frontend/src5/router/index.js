import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Layout from '../components/Layout/index.vue'

// 学生端页面
import StudentDashboard from '../views/StudentDashboard.vue'
import StudentClubList from '../views/StudentClubList.vue'
import StudentClubDetail from '../views/StudentClubDetail.vue'
import StudentActivityList from '../views/StudentActivityList.vue'
import StudentActivityDetail from '../views/StudentActivityDetail.vue'
import StudentRegistrationList from '../views/StudentRegistrationList.vue'
import StudentProfile from '../views/StudentProfile.vue'
import StudentProfileEdit from '../views/StudentProfileEdit.vue'
import StudentChangePassword from '../views/StudentChangePassword.vue'

// 负责人端页面
import LeaderDashboard from '../views/LeaderDashboard.vue'
import LeaderClub from '../views/LeaderClub.vue'
import LeaderMembers from '../views/LeaderMembers.vue'
import LeaderActivities from '../views/LeaderActivities.vue'
import LeaderActivityDetail from '../views/LeaderActivityDetail.vue'
import ActivityForm from '../views/ActivityForm.vue'
import RegistrationList from '../views/RegistrationList.vue'
import LeaderCheckin from '../views/LeaderCheckin.vue'
import LeaderSummaries from '../views/LeaderSummaries.vue'
import LeaderSummaryForm from '../views/LeaderSummaryForm.vue'
import LeaderSummaryDetail from '../views/LeaderSummaryDetail.vue'

// 管理员端页面
import AdminDashboard from '../views/AdminDashboard.vue'
import AdminUsers from '../views/AdminUsers.vue'
import AdminClubs from '../views/AdminClubs.vue'
import AdminClubDetail from '../views/AdminClubDetail.vue'
import UserForm from '../views/UserForm.vue'
import AdminStatistics from '../views/AdminStatistics.vue'
import AdminSettings from '../views/AdminSettings.vue'

const routes = [
  { 
    path: '/login', 
    name: 'Login', 
    component: Login, 
    meta: { requiresAuth: false, title: '登录' } 
  },
  { 
    path: '/register', 
    name: 'Register', 
    component: Register, 
    meta: { requiresAuth: false, title: '注册' } 
  },
  {
    path: '/student',
    component: Layout,
    meta: { requiresAuth: true, roles: ['student'], title: '学生首页' },
    children: [
      { path: '', name: 'StudentDashboard', component: StudentDashboard, meta: { title: '学生首页' } },
      { path: 'clubs', name: 'StudentClubList', component: StudentClubList, meta: { title: '组织浏览' } },
      { path: 'clubs/detail/:id', name: 'StudentClubDetail', component: StudentClubDetail, meta: { title: '组织详情' } },
      { path: 'activities', name: 'StudentActivityList', component: StudentActivityList, meta: { title: '活动列表' } },
      { path: 'activities/detail/:id', name: 'StudentActivityDetail', component: StudentActivityDetail, meta: { title: '活动详情' } },
      { path: 'registrations', name: 'StudentRegistrationList', component: StudentRegistrationList, meta: { title: '我的报名' } },
      { path: 'profile', name: 'StudentProfile', component: StudentProfile, meta: { title: '个人中心' } },
      { path: 'profile/edit', name: 'StudentProfileEdit', component: StudentProfileEdit, meta: { title: '修改资料' } },
      { path: 'profile/change-password', name: 'StudentChangePassword', component: StudentChangePassword, meta: { title: '修改密码' } }
    ]
  },
  {
    path: '/leader',
    component: Layout,
    meta: { requiresAuth: true, roles: ['leader'], title: '负责人首页' },
    children: [
      { path: '', name: 'LeaderDashboard', component: LeaderDashboard, meta: { title: '负责人首页' } },
      { path: 'club', name: 'LeaderClub', component: LeaderClub, meta: { title: '组织信息' } },
      { path: 'members', name: 'LeaderMembers', component: LeaderMembers, meta: { title: '成员审核' } },
      { path: 'activities', name: 'LeaderActivities', component: LeaderActivities, meta: { title: '活动管理' } },
      { path: 'activities/add', name: 'LeaderActivityAdd', component: ActivityForm, meta: { title: '发布活动' } },
      { path: 'activities/edit/:id', name: 'LeaderActivityEdit', component: ActivityForm, meta: { title: '编辑活动' } },
      { path: 'activities/detail/:id', name: 'LeaderActivityDetail', component: LeaderActivityDetail, meta: { title: '活动详情' } },
      { path: 'activities/registrations/:id', name: 'LeaderActivityRegistrations', component: RegistrationList, meta: { title: '报名管理' } },
      { path: 'checkin', name: 'LeaderCheckin', component: LeaderCheckin, meta: { title: '签到管理' } },
      { path: 'summaries', name: 'LeaderSummaries', component: LeaderSummaries, meta: { title: '活动总结' } },
      { path: 'summaries/add', name: 'LeaderSummaryAdd', component: LeaderSummaryForm, meta: { title: '撰写总结' } },
      { path: 'summaries/edit/:id', name: 'LeaderSummaryEdit', component: LeaderSummaryForm, meta: { title: '编辑总结' } },
      { path: 'summaries/detail/:id', name: 'LeaderSummaryDetail', component: LeaderSummaryDetail, meta: { title: '总结详情' } }
    ]
  },
  {
    path: '/admin',
    component: Layout,
    meta: { requiresAuth: true, roles: ['admin'], title: '管理员首页' },
    children: [
      { path: '', name: 'AdminDashboard', component: AdminDashboard, meta: { title: '管理员首页' } },
      { path: 'users', name: 'AdminUsers', component: AdminUsers, meta: { title: '用户管理' } },
      { path: 'users/add', name: 'AdminUserAdd', component: UserForm, meta: { title: '添加用户' } },
      { path: 'users/edit/:id', name: 'AdminUserEdit', component: UserForm, meta: { title: '编辑用户' } },
      { path: 'clubs', name: 'AdminClubs', component: AdminClubs, meta: { title: '组织审核' } },
      { path: 'clubs/detail/:id', name: 'AdminClubDetail', component: AdminClubDetail, meta: { title: '组织详情' } },
      { path: 'statistics', name: 'AdminStatistics', component: AdminStatistics, meta: { title: '系统统计' } },
      { path: 'settings', name: 'AdminSettings', component: AdminSettings, meta: { title: '系统配置' } }
    ]
  },
  { path: '/', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
  const userRole = user?.role

  if (to.meta.requiresAuth) {
    if (!token) {
      next('/login')
      return
    }

    if (to.meta.roles && !to.meta.roles.includes(userRole)) {
      const rolePath = {
        student: '/student',
        leader: '/leader',
        admin: '/admin'
      }
      next(rolePath[userRole] || '/login')
      return
    }

    next()
  } else {
    if (token && (to.path === '/login' || to.path === '/register')) {
      const rolePath = {
        student: '/student',
        leader: '/leader',
        admin: '/admin'
      }
      next(rolePath[userRole] || '/login')
    } else {
      next()
    }
  }
})

export default router
