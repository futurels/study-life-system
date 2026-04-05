import request from '@/utils/request'

export function getLifeRecordListApi(params) {
  return request({
    url: '/life-record/list',
    method: 'get',
    params
  })
}

export function getLifeRecordDetailApi(id) {
  return request({
    url: `/life-record/${id}`,
    method: 'get'
  })
}

export function createLifeRecordApi(data) {
  return request({
    url: '/life-record',
    method: 'post',
    data
  })
}

export function updateLifeRecordApi(id, data) {
  return request({
    url: `/life-record/${id}`,
    method: 'put',
    data
  })
}

export function deleteLifeRecordApi(id) {
  return request({
    url: `/life-record/${id}`,
    method: 'delete'
  })
}
