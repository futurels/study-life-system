import request from '@/utils/request'

export function getStudyPlanListApi(params) {
  return request({
    url: '/study-plan/list',
    method: 'get',
    params
  })
}

export function createStudyPlanApi(data) {
  return request({
    url: '/study-plan',
    method: 'post',
    data
  })
}

export function getStudyPlanDetailApi(id) {
  return request({
    url: `/study-plan/${id}`,
    method: 'get'
  })
}

export function updateStudyPlanApi(id, data) {
  return request({
    url: `/study-plan/${id}`,
    method: 'put',
    data
  })
}

export function deleteStudyPlanApi(id) {
  return request({
    url: `/study-plan/${id}`,
    method: 'delete'
  })
}

export function updateStudyPlanStatusApi(id, data) {
  return request({
    url: `/study-plan/${id}/status`,
    method: 'put',
    data
  })
}
