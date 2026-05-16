import { mockUsers, mockClubs, mockActivities, mockRegistrations, mockSummaries } from './mock.js'

const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms))

export const login = async (userData) => {
  await delay(500)
  const user = mockUsers.find(u => u.username === userData.username && u.password === userData.password)
  if (user) {
    return {
      code: 200,
      message: '登录成功',
      data: {
        token: 'mock-token-' + user.id,
        username: user.username,
        realName: user.name,
        role: user.role
      }
    }
  }
  return { code: 401, message: '用户名或密码错误' }
}

export const register = async (userData) => {
  await delay(500)
  const exists = mockUsers.find(u => u.username === userData.username)
  if (exists) {
    return { code: 400, message: '用户名已存在' }
  }
  const newUser = {
    id: mockUsers.length + 1,
    ...userData,
    password: undefined
  }
  mockUsers.push({ ...userData })
  return { code: 200, message: '注册成功', data: newUser }
}

export const authApi = {
  login,
  register
}

export const getClubs = async (params = {}) => {
  await delay(300)
  let data = [...mockClubs]
  if (params.status) {
    data = data.filter(c => c.status === params.status)
  }
  return { code: 200, message: 'success', data }
}

export const getClubById = async (id) => {
  await delay(200)
  const club = mockClubs.find(c => c.id === parseInt(id))
  if (club) {
    return { code: 200, message: 'success', data: club }
  }
  return { code: 404, message: '组织不存在' }
}

export const createClub = async (clubData) => {
  await delay(300)
  const newClub = {
    id: mockClubs.length + 1,
    ...clubData,
    status: 'pending',
    createdAt: new Date().toISOString().split('T')[0]
  }
  mockClubs.push(newClub)
  return { code: 200, message: '创建成功', data: newClub }
}

export const updateClub = async (id, clubData) => {
  await delay(300)
  const index = mockClubs.findIndex(c => c.id === parseInt(id))
  if (index !== -1) {
    mockClubs[index] = { ...mockClubs[index], ...clubData }
    return { code: 200, message: '更新成功', data: mockClubs[index] }
  }
  return { code: 404, message: '组织不存在' }
}

export const deleteClub = async (id) => {
  await delay(300)
  const index = mockClubs.findIndex(c => c.id === parseInt(id))
  if (index !== -1) {
    mockClubs.splice(index, 1)
    return { code: 200, message: '删除成功' }
  }
  return { code: 404, message: '组织不存在' }
}

export const getActivities = async (params = {}) => {
  await delay(300)
  let data = [...mockActivities]
  if (params.clubId) {
    data = data.filter(a => a.clubId === parseInt(params.clubId))
  }
  if (params.status) {
    data = data.filter(a => a.status === params.status)
  }
  return { code: 200, message: 'success', data }
}

export const getActivityById = async (id) => {
  await delay(200)
  const activity = mockActivities.find(a => a.id === parseInt(id))
  if (activity) {
    return { code: 200, message: 'success', data: activity }
  }
  return { code: 404, message: '活动不存在' }
}

export const createActivity = async (activityData) => {
  await delay(300)
  const newActivity = {
    id: mockActivities.length + 1,
    ...activityData,
    status: 'pending',
    participants: 0
  }
  mockActivities.push(newActivity)
  return { code: 200, message: '创建成功', data: newActivity }
}

export const updateActivity = async (id, activityData) => {
  await delay(300)
  const index = mockActivities.findIndex(a => a.id === parseInt(id))
  if (index !== -1) {
    mockActivities[index] = { ...mockActivities[index], ...activityData }
    return { code: 200, message: '更新成功', data: mockActivities[index] }
  }
  return { code: 404, message: '活动不存在' }
}

export const deleteActivity = async (id) => {
  await delay(300)
  const index = mockActivities.findIndex(a => a.id === parseInt(id))
  if (index !== -1) {
    mockActivities.splice(index, 1)
    return { code: 200, message: '删除成功' }
  }
  return { code: 404, message: '活动不存在' }
}

export const getRegistrations = async (params = {}) => {
  await delay(300)
  let data = [...mockRegistrations]
  if (params.activityId) {
    data = data.filter(r => r.activityId === parseInt(params.activityId))
  }
  if (params.status) {
    data = data.filter(r => r.status === params.status)
  }
  return { code: 200, message: 'success', data }
}

export const createRegistration = async (registrationData) => {
  await delay(300)
  const exists = mockRegistrations.find(
    r => r.activityId === registrationData.activityId && r.userId === registrationData.userId
  )
  if (exists) {
    return { code: 400, message: '已报名此活动' }
  }
  const newRegistration = {
    id: mockRegistrations.length + 1,
    ...registrationData,
    status: 'pending',
    registeredAt: new Date().toISOString().split('T')[0]
  }
  mockRegistrations.push(newRegistration)
  return { code: 200, message: '报名成功', data: newRegistration }
}

export const updateRegistration = async (id, status) => {
  await delay(300)
  const index = mockRegistrations.findIndex(r => r.id === parseInt(id))
  if (index !== -1) {
    mockRegistrations[index].status = status
    return { code: 200, message: '更新成功', data: mockRegistrations[index] }
  }
  return { code: 404, message: '报名记录不存在' }
}

export const deleteRegistration = async (id) => {
  await delay(300)
  const index = mockRegistrations.findIndex(r => r.id === parseInt(id))
  if (index !== -1) {
    mockRegistrations.splice(index, 1)
    return { code: 200, message: '取消报名成功' }
  }
  return { code: 404, message: '报名记录不存在' }
}

export const getSummaries = async () => {
  await delay(300)
  return { code: 200, message: 'success', data: mockSummaries }
}

export const getSummaryById = async (id) => {
  await delay(200)
  const summary = mockSummaries.find(s => s.id === parseInt(id))
  if (summary) {
    return { code: 200, message: 'success', data: summary }
  }
  return { code: 404, message: '总结不存在' }
}

export const createSummary = async (summaryData) => {
  await delay(300)
  const newSummary = {
    id: mockSummaries.length + 1,
    ...summaryData,
    createdAt: new Date().toISOString().split('T')[0]
  }
  mockSummaries.push(newSummary)
  return { code: 200, message: '创建成功', data: newSummary }
}

export const updateSummary = async (id, summaryData) => {
  await delay(300)
  const index = mockSummaries.findIndex(s => s.id === parseInt(id))
  if (index !== -1) {
    mockSummaries[index] = { ...mockSummaries[index], ...summaryData }
    return { code: 200, message: '更新成功', data: mockSummaries[index] }
  }
  return { code: 404, message: '总结不存在' }
}

export const deleteSummary = async (id) => {
  await delay(300)
  const index = mockSummaries.findIndex(s => s.id === parseInt(id))
  if (index !== -1) {
    mockSummaries.splice(index, 1)
    return { code: 200, message: '删除成功' }
  }
  return { code: 404, message: '总结不存在' }
}

export const clubApi = {
  list: getClubs,
  get: getClubById,
  create: createClub,
  update: updateClub,
  delete: deleteClub
}

export const activityApi = {
  list: getActivities,
  get: getActivityById,
  create: createActivity,
  update: updateActivity,
  delete: deleteActivity
}

export const getRegistrationsByActivity = async (activityId) => {
  await delay(300)
  const data = mockRegistrations.filter(r => r.activityId === parseInt(activityId))
  return { code: 200, message: 'success', data }
}

export const manualCheckIn = async (registrationId) => {
  await delay(200)
  const index = mockRegistrations.findIndex(r => r.id === parseInt(registrationId))
  if (index !== -1) {
    mockRegistrations[index].status = 'checked_in'
    return { code: 200, message: '签到成功', data: mockRegistrations[index] }
  }
  return { code: 404, message: '报名记录不存在' }
}

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

export const summaryApi = {
  list: getSummaries,
  get: getSummaryById,
  create: createSummary,
  update: updateSummary,
  delete: deleteSummary
}