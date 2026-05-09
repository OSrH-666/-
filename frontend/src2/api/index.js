import request from './request.js'

export const login = async (userData) => {
  const response = await request.post('/auth/login', userData)
  return response
}

export const register = async (userData) => {
  const response = await request.post('/auth/register', userData)
  return response
}

export const authApi = {
  login,
  register
}

export const getClubs = async (params = {}) => {
  const response = await request.get('/club', { params })
  return response
}

export const getClubById = async (id) => {
  const response = await request.get(`/club/${id}`)
  return response
}

export const createClub = async (clubData) => {
  const response = await request.post('/club', clubData)
  return response
}

export const updateClub = async (id, clubData) => {
  const response = await request.put(`/club/${id}`, clubData)
  return response
}

export const deleteClub = async (id) => {
  const response = await request.delete(`/club/${id}`)
  return response
}

export const auditClub = async (id, status) => {
  const response = await request.put(`/club/${id}/audit`, null, { params: { status } })
  return response
}

export const getActivities = async (params = {}) => {
  const response = await request.get('/activity', { params })
  return response
}

export const getActivityById = async (id) => {
  const response = await request.get(`/activity/${id}`)
  return response
}

export const createActivity = async (activityData) => {
  const response = await request.post('/activity', activityData)
  return response
}

export const updateActivity = async (id, activityData) => {
  const response = await request.put(`/activity/${id}`, activityData)
  return response
}

export const deleteActivity = async (id) => {
  const response = await request.delete(`/activity/${id}`)
  return response
}

export const publishActivity = async (id) => {
  const response = await request.put(`/activity/${id}/publish`)
  return response
}

export const endActivity = async (id) => {
  const response = await request.put(`/activity/${id}/end`)
  return response
}

export const registerActivity = async (activityId, userId) => {
  const response = await request.post('/registration', null, { params: { activityId, userId } })
  return response
}

export const cancelRegistration = async (activityId, userId) => {
  const response = await request.delete('/registration', { params: { activityId, userId } })
  return response
}

export const getRegistrationsByActivity = async (activityId) => {
  const response = await request.get(`/registration/activity/${activityId}`)
  return response
}

export const getRegistrationsByUser = async (userId) => {
  const response = await request.get(`/registration/user/${userId}`)
  return response
}

export const getRegistrationCount = async (activityId) => {
  const response = await request.get(`/registration/activity/${activityId}/count`)
  return response
}

export const getSummaries = async (params = {}) => {
  const response = await request.get('/summary', { params })
  return response
}

export const getSummaryById = async (id) => {
  const response = await request.get(`/summary/${id}`)
  return response
}

export const getSummaryByActivity = async (activityId) => {
  const response = await request.get(`/summary/activity/${activityId}`)
  return response
}

export const createSummary = async (activityId, content, attachmentUrl) => {
  const response = await request.post('/summary', null, { params: { activityId, content, attachmentUrl } })
  return response
}

export const updateSummary = async (id, content, attachmentUrl) => {
  const response = await request.put(`/summary/${id}`, null, { params: { content, attachmentUrl } })
  return response
}

export const deleteSummary = async (id) => {
  const response = await request.delete(`/summary/${id}`)
  return response
}

export const checkIn = async (registrationId, method, latitude, longitude) => {
  const response = await request.post('/checkin', null, { 
    params: { registrationId, method, latitude, longitude } 
  })
  return response
}

export const manualCheckIn = async (registrationId) => {
  const response = await request.post('/checkin/manual', null, { 
    params: { registrationId } 
  })
  return response
}

export const getCheckInByRegistration = async (registrationId) => {
  const response = await request.get(`/checkin/registration/${registrationId}`)
  return response
}

export const clubApi = {
  list: getClubs,
  get: getClubById,
  create: createClub,
  update: updateClub,
  delete: deleteClub,
  audit: auditClub
}

export const activityApi = {
  list: getActivities,
  get: getActivityById,
  create: createActivity,
  update: updateActivity,
  delete: deleteActivity,
  publish: publishActivity,
  end: endActivity
}

export const registrationApi = {
  register: registerActivity,
  cancel: cancelRegistration,
  listByActivity: getRegistrationsByActivity,
  listByUser: getRegistrationsByUser,
  getCount: getRegistrationCount
}

export const checkInApi = {
  checkIn,
  manualCheckIn,
  getByRegistration: getCheckInByRegistration
}

export const summaryApi = {
  list: getSummaries,
  get: getSummaryById,
  getByActivity: getSummaryByActivity,
  create: createSummary,
  update: updateSummary,
  delete: deleteSummary
}
