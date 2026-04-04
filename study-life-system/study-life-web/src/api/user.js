import request from '@/utils/request'

export function getProfileApi() {
  return request({
    url: '/user/profile',
    method: 'get'
  })
}
