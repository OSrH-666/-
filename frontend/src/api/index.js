import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

api.interceptors.response.use(response => {
  return response.data
}, error => {
  if (error.response.status === 401) {
    localStorage.removeItem('token')
    location.href = '/login'
  }
  return Promise.reject(error)
})

export const authApi = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data)
}

export const clubApi = {
  list: (status) => api.get('/club', { params: { status } }),
  get: (id) => api.get(`/club/${id}`),
  create: (data) => api.post('/club', data),
  update: (id, data) => api.put(`/club/${id}`, data),
  delete: (id) => api.delete(`/club/${id}`),
  audit: (id, status) => api.put(`/club/${id}/audit`, { params: { status } })
}

export const activityApi = {
  list: (clubId, status) => api.get('/activity', { params: { clubId, status } }),
  get: (id) => api.get(`/activity/${id}`),
  create: (data) => api.post('/activity', data),
  update: (id, data) => api.put(`/activity/${id}`, data),
  delete: (id) => api.delete(`/activity/${id}`),
  publish: (id) => api.put(`/activity/${id}/publish`),
  end: (id) => api.put(`/activity/${id}/end`)
}

export const registrationApi = {
  register: (activityId, userId) => api.post('/registration', { params: { activityId, userId } }),
  cancel: (activityId, userId) => api.delete('/registration', { params: { activityId, userId } }),
  listByActivity: (activityId) => api.get(`/registration/activity/${activityId}`),
  listByUser: (userId) => api.get(`/registration/user/${userId}`),
  count: (activityId) => api.get(`/registration/activity/${activityId}/count`)
}

export const checkInApi = {
  checkIn: (registrationId, method, latitude, longitude) => 
    api.post('/checkin', { params: { registrationId, method, latitude, longitude } }),
  manualCheckIn: (registrationId) => api.post('/checkin/manual', { params: { registrationId } }),
  getByRegistration: (registrationId) => api.get(`/checkin/registration/${registrationId}`)
}

export const summaryApi = {
  create: (activityId, content, attachmentUrl) => 
    api.post('/summary', { params: { activityId, content, attachmentUrl } }),
  update: (id, content, attachmentUrl) => 
    api.put(`/summary/${id}`, { params: { content, attachmentUrl } }),
  getByActivity: (activityId) => api.get(`/summary/activity/${activityId}`),
  get: (id) => api.get(`/summary/${id}`),
  delete: (id) => api.delete(`/summary/${id}`)
}

export default api