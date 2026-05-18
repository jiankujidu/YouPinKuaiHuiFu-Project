import request from './request'

export const getTeamMembers = () => {
  return request.get('/teams/members')
}

export const createTeam = (data) => {
  return request.post('/teams', data)
}
