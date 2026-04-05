<template>
  <div class="app-layout">
    <aside class="app-sidebar">
      <div class="brand-block">
        <div class="brand-title">Study Life</div>
        <div class="brand-subtitle">记录计划、生活与成长</div>
      </div>

      <nav class="nav-list">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          class="nav-item"
          :class="{ active: route.path === item.path }"
          :to="item.path"
        >
          {{ item.label }}
        </router-link>
      </nav>
    </aside>

    <div class="app-main">
      <header class="app-header">
        <div>
          <div class="page-title">学习计划与日常生活记录系统</div>
          <div class="page-subtitle">今天也要认真生活，按计划稳步推进</div>
        </div>
        <div class="header-user">
          <div class="header-date">{{ currentDateText }}</div>
          <div class="user-name">{{ displayName }}</div>
          <el-button link type="primary" @click="handleLogout">退出登录</el-button>
        </div>
      </header>

      <main class="app-content">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getUserInfo } from '@/utils/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const navItems = [
  { path: '/study-life/dashboard', label: '首页' },
  { path: '/study-life/study-plan', label: '学习计划' },
  { path: '/study-life/life-record', label: '生活记录' },
  { path: '/study-life/daily-review', label: '每日复盘' },
  { path: '/study-life/statistics', label: '统计分析' }
]

const displayName = computed(() => {
  const storeUser = authStore.userInfo
  const localUser = getUserInfo()
  return storeUser?.nickname || localUser?.nickname || storeUser?.username || localUser?.username || '未登录用户'
})

const currentDateText = computed(() => {
  const now = new Date()
  return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`
})

function handleLogout() {
  authStore.logout()
  router.push('/study-life/login')
}
</script>
