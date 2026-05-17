import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import StudentDashboard from '../views/student/StudentDashboard.vue'
import LeaderDashboard from '../views/leader/LeaderDashboard.vue'
import AdminDashboard from '../views/admin/AdminDashboard.vue'
import ClubList from '../views/ClubList.vue'
import ClubForm from '../views/ClubForm.vue'
import ActivityList from '../views/ActivityList.vue'
import ActivityForm from '../views/ActivityForm.vue'
import ActivityDetail from '../views/ActivityDetail.vue'
import RegistrationList from '../views/RegistrationList.vue'
import SummaryList from '../views/SummaryList.vue'
import Profile from '../views/Profile.vue'
import ProfileEdit from '../views/ProfileEdit.vue'
import ProfileChangePassword from '../views/ProfileChangePassword.vue'
import UserManagement from '../views/UserManagement.vue'
import UnionInfo from '../views/UnionInfo.vue'
import UnionMember from '../views/UnionMember.vue'
import UnionNews from '../views/UnionNews.vue'
import MyRegistrations from '../views/student/MyRegistrations.vue'

const getRoleType = (role) => {
  if (role === 'ADMIN' || role === 'admin') {
    return 'admin'
  } else if (role === 'CLUB_MANAGER' || role === 'leader') {
    return 'leader'
  } else {
    return 'student'
  }
}

const routes = [
  { path: '/login', name: 'Login', component: Login, meta: { requiresAuth: false } },
  { path: '/register', name: 'Register', component: Register, meta: { requiresAuth: false } },
  { path: '/student', name: 'StudentDashboard', component: StudentDashboard, meta: { requiresAuth: true, roles: ['student'] } },
  { path: '/leader', name: 'LeaderDashboard', component: LeaderDashboard, meta: { requiresAuth: true, roles: ['leader'] } },
  { path: '/admin', name: 'AdminDashboard', component: AdminDashboard, meta: { requiresAuth: true, roles: ['admin'] } },
  { path: '/', name: 'Home', redirect: '/login' },
  { path: '/my-registrations', name: 'MyRegistrations', component: MyRegistrations, meta: { requiresAuth: true, roles: ['student'] } },
  { path: '/union/info', name: 'UnionInfo', component: UnionInfo, meta: { requiresAuth: true, roles: ['leader', 'admin'] } },
  { path: '/union/members', name: 'UnionMember', component: UnionMember, meta: { requiresAuth: true, roles: ['leader', 'admin'] } },
  { path: '/union/news', name: 'UnionNews', component: UnionNews, meta: { requiresAuth: true, roles: ['leader', 'admin'] } },
  { path: '/clubs', name: 'ClubList', component: ClubList, meta: { requiresAuth: true } },
  { path: '/clubs/add', name: 'ClubAdd', component: ClubForm, meta: { requiresAuth: true, roles: ['admin'] } },
  { path: '/clubs/edit/:id', name: 'ClubEdit', component: ClubForm, meta: { requiresAuth: true, roles: ['admin'] } },
  { path: '/activities', name: 'ActivityList', component: ActivityList, meta: { requiresAuth: true } },
  { path: '/activities/add', name: 'ActivityAdd', component: ActivityForm, meta: { requiresAuth: true, roles: ['leader', 'admin'] } },
  { path: '/activities/edit/:id', name: 'ActivityEdit', component: ActivityForm, meta: { requiresAuth: true, roles: ['leader', 'admin'] } },
  { path: '/activities/detail/:id', name: 'ActivityDetail', component: ActivityDetail, meta: { requiresAuth: true } },
  { path: '/registrations', name: 'RegistrationList', component: RegistrationList, meta: { requiresAuth: true, roles: ['leader', 'admin'] } },
  { path: '/summaries', name: 'SummaryList', component: SummaryList, meta: { requiresAuth: true, roles: ['leader', 'admin'] } },
  { path: '/profile', name: 'Profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/profile/edit', name: 'ProfileEdit', component: ProfileEdit, meta: { requiresAuth: true } },
  { path: '/profile/change-password', name: 'ProfileChangePassword', component: ProfileChangePassword, meta: { requiresAuth: true } },
  { path: '/users', name: 'UserManagement', component: UserManagement, meta: { requiresAuth: true, roles: ['admin'] } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth) {
    if (token) {
      const user = localStorage.getItem('user')
      if (user) {
        const userInfo = JSON.parse(user)
        const roleType = getRoleType(userInfo.role)
        
        if (to.meta.roles && to.meta.roles.length > 0) {
          if (to.meta.roles.includes(roleType)) {
            next()
          } else {
            next('/')
          }
        } else {
          next()
        }
      } else {
        next('/login')
      }
    } else {
      next('/login')
    }
  } else {
    if (token && (to.path === '/login' || to.path === '/register')) {
      const user = localStorage.getItem('user')
      if (user) {
        const userInfo = JSON.parse(user)
        const roleType = getRoleType(userInfo.role)
        if (roleType === 'admin') {
          next('/admin')
        } else if (roleType === 'leader') {
          next('/leader')
        } else {
          next('/student')
        }
        return
      }
    }
    next()
  }
})

export default router
