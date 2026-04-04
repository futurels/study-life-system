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
      ElMessage.error(result.message || '请求失败')
      if (result.code === 401) {
        clearAuth()
        window.location.href = '/study-life/login'
      }
      return Promise.reject(result)
    }
    return result
  },
  (error) => {
    ElMessage.error(error.response?.data?.message || '服务异常，请稍后再试')
    return Promise.reject(error)
  }
)

export default request
