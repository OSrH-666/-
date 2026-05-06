import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import ClubList from '../views/ClubList.vue'
import ClubForm from '../views/ClubForm.vue'
import ActivityList from '../views/ActivityList.vue'
import ActivityForm from '../views/ActivityForm.vue'
import ActivityDetail from '../views/ActivityDetail.vue'
import RegistrationList from '../views/RegistrationList.vue'
import SummaryList from '../views/SummaryList.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login, meta: { requiresAuth: false } },
  { path: '/register', name: 'Register', component: Register, meta: { requiresAuth: false } },
  { path: '/', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/clubs', name: 'ClubList', component: ClubList, meta: { requiresAuth: true } },
  { path: '/clubs/add', name: 'ClubAdd', component: ClubForm, meta: { requiresAuth: true } },
  { path: '/clubs/edit/:id', name: 'ClubEdit', component: ClubForm, meta: { requiresAuth: true } },
  { path: '/activities', name: 'ActivityList', component: ActivityList, meta: { requiresAuth: true } },
  { path: '/activities/add', name: 'ActivityAdd', component: ActivityForm, meta: { requiresAuth: true } },
  { path: '/activities/edit/:id', name: 'ActivityEdit', component: ActivityForm, meta: { requiresAuth: true } },
  { path: '/activities/detail/:id', name: 'ActivityDetail', component: ActivityDetail, meta: { requiresAuth: true } },
  { path: '/registrations', name: 'RegistrationList', component: RegistrationList, meta: { requiresAuth: true } },
  { path: '/summaries', name: 'SummaryList', component: SummaryList, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth) {
    if (token) {
      next()
    } else {
      next('/login')
    }
  } else {
    if (token && (to.path === '/login' || to.path === '/register')) {
      next('/')
    } else {
      next()
    }
  }
})

export default router