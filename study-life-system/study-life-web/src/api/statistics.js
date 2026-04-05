import request from '@/utils/request'

export function getStatisticsOverviewApi() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

export function getStatisticsWeeklyApi(params) {
  return request({
    url: '/statistics/weekly',
    method: 'get',
    params
  })
}

export function getStatisticsMonthlyApi(params) {
  return request({
    url: '/statistics/monthly',
    method: 'get',
    params
  })
}
