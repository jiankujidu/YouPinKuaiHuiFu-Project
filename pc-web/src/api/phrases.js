import request from './request'

export const getPhrases = (params) => {
  return request.get('/phrases', { params })
}

export const createPhrase = (data) => {
  return request.post('/phrases', data)
}

export const updatePhrase = (id, data) => {
  return request.put(`/phrases/${id}`, data)
}

export const deletePhrase = (id) => {
  return request.delete(`/phrases/${id}`)
}
