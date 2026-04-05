import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  {
    path: '/',
    redirect: '/study-life/dashboard'
  },
  {
    path: '/study-life/login',
    name: 'login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/study-life/dashboard',
    name: 'dashboard',
    component: () => import('@/views/dashboard/DashboardView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/study-life/study-plan',
    name: 'study-plan',
    component: () => import('@/views/study-plan/StudyPlanView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/study-life/life-record',
    name: 'life-record',
    component: () => import('@/views/life-record/LifeRecordView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/study-life/daily-review',
    name: 'daily-review',
    component: () => import('@/views/daily-review/DailyReviewView.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const token = getToken()
  if (to.meta.requiresAuth && !token) {
    return '/study-life/login'
  }
  if (to.path === '/study-life/login' && token) {
    return '/study-life/dashboard'
  }
  return true
})

export default router
