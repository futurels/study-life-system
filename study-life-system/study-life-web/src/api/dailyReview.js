import request from '@/utils/request'

export function getDailyReviewListApi(params) {
  return request({
    url: '/daily-review/list',
    method: 'get',
    params
  })
}

export function getDailyReviewDetailApi(id) {
  return request({
    url: `/daily-review/${id}`,
    method: 'get'
  })
}

export function createDailyReviewApi(data) {
  return request({
    url: '/daily-review',
    method: 'post',
    data
  })
}

export function updateDailyReviewApi(id, data) {
  return request({
    url: `/daily-review/${id}`,
    method: 'put',
    data
  })
}

export function deleteDailyReviewApi(id) {
  return request({
    url: `/daily-review/${id}`,
    method: 'delete'
  })
}
