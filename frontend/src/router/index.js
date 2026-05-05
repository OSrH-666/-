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
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/', name: 'Dashboard', component: Dashboard },
  { path: '/clubs', name: 'ClubList', component: ClubList },
  { path: '/clubs/add', name: 'ClubAdd', component: ClubForm },
  { path: '/clubs/edit/:id', name: 'ClubEdit', component: ClubForm },
  { path: '/activities', name: 'ActivityList', component: ActivityList },
  { path: '/activities/add', name: 'ActivityAdd', component: ActivityForm },
  { path: '/activities/edit/:id', name: 'ActivityEdit', component: ActivityForm },
  { path: '/activities/detail/:id', name: 'ActivityDetail', component: ActivityDetail },
  { path: '/registrations', name: 'RegistrationList', component: RegistrationList },
  { path: '/summaries', name: 'SummaryList', component: SummaryList }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (token) {
    next()
  } else {
    next()
  }
})

export default router