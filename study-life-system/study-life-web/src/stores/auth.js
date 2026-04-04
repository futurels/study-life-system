import { defineStore } from 'pinia'
import { loginApi } from '@/api/auth'
import { getProfileApi } from '@/api/user'
import { clearAuth, getToken, setAuth } from '@/utils/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: getToken(),
    userInfo: null
  }),
  actions: {
    async login(payload) {
      const response = await loginApi(payload)
      setAuth(response.data.accessToken, response.data.userInfo)
      this.token = response.data.accessToken
      this.userInfo = response.data.userInfo
      return response
    },
    async fetchProfile() {
      const response = await getProfileApi()
      this.userInfo = response.data
      setAuth(this.token, response.data)
      return response
    },
    logout() {
      clearAuth()
      this.token = ''
      this.userInfo = null
    }
  }
})
