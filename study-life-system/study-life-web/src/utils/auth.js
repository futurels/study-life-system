const TOKEN_KEY = 'study-life-token'
const USER_INFO_KEY = 'study-life-user-info'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || ''
}

export function getUserInfo() {
  const raw = localStorage.getItem(USER_INFO_KEY)
  return raw ? JSON.parse(raw) : null
}

export function setAuth(token, userInfo) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
  }
  if (userInfo) {
    localStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo))
  }
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_INFO_KEY)
}
