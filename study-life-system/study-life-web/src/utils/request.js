import axios from 'axios'
import { ElMessage } from 'element-plus'
import { clearAuth, getToken } from '@/utils/auth'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    const result = response.data
    if (result.code !== 200) {
      const message = normalizeBusinessMessage(result)
      ElMessage.error(message)
      if (result.code === 401) {
        clearAuth()
        window.location.href = '/study-life/login'
      }
      return Promise.reject(result)
    }
    return result
  },
  (error) => {
    ElMessage.error(normalizeHttpErrorMessage(error))
    return Promise.reject(error)
  }
)

function normalizeBusinessMessage(result) {
  if (result?.code === 401) {
    return result.message || '登录状态已失效，请重新登录'
  }
  if (result?.code === 500) {
    return result.message || '系统开小差了，请稍后重试'
  }
  return result?.message || '请求失败，请检查输入后重试'
}

function normalizeHttpErrorMessage(error) {
  if (error.code === 'ECONNABORTED') {
    return '请求超时，请检查网络或稍后重试'
  }
  if (!error.response) {
    return '无法连接到服务器，请确认后端服务已启动'
  }
  const status = error.response.status
  if (status === 401) {
    return error.response.data?.message || '登录状态已失效，请重新登录'
  }
  if (status >= 500) {
    return error.response.data?.message || '服务器暂时不可用，请稍后再试'
  }
  return error.response.data?.message || '请求失败，请检查输入后重试'
}

export default request
