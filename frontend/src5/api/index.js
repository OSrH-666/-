import request from './request'

export const authApi = {
  login: (data) => request.post('/auth/login', data),
  register: (data) => request.post('/auth/register', data)
}

export const getClubs = (params) => request.get('/club', { params })
export const getClubById = (id) => request.get(`/club/${id}`)
export const createClub = (data) => request.post('/club', data)
export const updateClub = (id, data) => request.put(`/club/${id}`, data)
export const deleteClub = (id) => request.delete(`/club/${id}`)

export const clubApi = {
  list: getClubs,
  get: getClubById,
  create: createClub,
  update: updateClub,
  delete: deleteClub
}

export const getActivities = (params) => request.get('/activity', { params })
export const getActivityById = (id) => request.get(`/activity/${id}`)
export const createActivity = (data) => request.post('/activity', data)
export const updateActivity = (id, data) => request.put(`/activity/${id}`, data)
export const deleteActivity = (id) => request.delete(`/activity/${id}`)

export const activityApi = {
  list: getActivities,
  get: getActivityById,
  create: createActivity,
  update: updateActivity,
  delete: deleteActivity
}

export const getRegistrations = (params) => request.get('/registration', { params })
export const createRegistration = (data) => request.post('/registration', data)
export const updateRegistration = (id, data) => request.put(`/registration/${id}`, data)
export const deleteRegistration = (id) => request.delete(`/registration/${id}`)

export const getRegistrationsByActivity = (activityId) => request.get(`/registration/activity/${activityId}`)

export const manualCheckIn = (registrationId) => request.post(`/registration/${registrationId}/check-in`)

export const registrationApi = {
  list: getRegistrations,
  listByActivity: getRegistrationsByActivity,
  create: createRegistration,
  update: updateRegistration,
  delete: deleteRegistration
}

export const checkInApi = {
  manualCheckIn
}

export const getSummaries = () => request.get('/summary')
export const getSummaryById = (id) => request.get(`/summary/${id}`)
export const createSummary = (data) => request.post('/summary', data)
export const updateSummary = (id, data) => request.put(`/summary/${id}`, data)
export const deleteSummary = (id) => request.delete(`/summary/${id}`)

export const summaryApi = {
  list: getSummaries,
  get: getSummaryById,
  create: createSummary,
  update: updateSummary,
  delete: deleteSummary
}

export const getUsers = () => request.get('/user')
export const getUserById = (id) => request.get(`/user/${id}`)
export const updateUser = (id, data) => request.put(`/user/${id}`, data)

export const userManagementApi = {
  list: getUsers,
  get: getUserById,
  update: updateUser
}
