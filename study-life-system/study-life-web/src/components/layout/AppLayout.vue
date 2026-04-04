<template>
  <div class="app-layout">
    <aside class="app-sidebar">
      <div class="brand-block">
        <div class="brand-title">Study Life</div>
        <div class="brand-subtitle">第一阶段最小运行版</div>
      </div>

      <nav class="nav-list">
        <router-link class="nav-item active" to="/study-life/dashboard">首页</router-link>
      </nav>
    </aside>

    <div class="app-main">
      <header class="app-header">
        <div>
          <div class="page-title">学习计划与日常生活记录系统</div>
          <div class="page-subtitle">登录成功后的首页框架页</div>
        </div>
        <div class="header-user">
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
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getUserInfo } from '@/utils/auth'

const router = useRouter()
const authStore = useAuthStore()

const displayName = computed(() => {
  const storeUser = authStore.userInfo
  const localUser = getUserInfo()
  return storeUser?.nickname || localUser?.nickname || storeUser?.username || localUser?.username || '未登录用户'
})

function handleLogout() {
  authStore.logout()
  router.push('/study-life/login')
}
</script>
