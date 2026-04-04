<template>
  <AppLayout>
    <section class="dashboard-shell">
      <div class="hero-card">
        <p class="hero-label">Welcome</p>
        <h2>{{ nickname }}，认证闭环已经打通</h2>
        <p>
          当前首页只保留第一阶段必需内容：登录结果展示、当前用户信息展示，以及后续模块入口占位。
        </p>
      </div>

      <div class="info-grid">
        <div class="info-card">
          <span class="info-title">当前用户名</span>
          <strong>{{ profile.username || '-' }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">昵称</span>
          <strong>{{ profile.nickname || '-' }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">邮箱</span>
          <strong>{{ profile.email || '未设置' }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">注册时间</span>
          <strong>{{ profile.createdAt || '-' }}</strong>
        </div>
      </div>

      <div class="placeholder-card">
        <h3>后续模块入口占位</h3>
        <p>学习计划、生活记录、每日复盘、统计分析将在后续阶段逐步接入。</p>
      </div>
    </section>
  </AppLayout>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import AppLayout from '@/components/layout/AppLayout.vue'
import { useAuthStore } from '@/stores/auth'
import { getUserInfo } from '@/utils/auth'

const authStore = useAuthStore()

const profile = computed(() => authStore.userInfo || getUserInfo() || {})
const nickname = computed(() => profile.value.nickname || profile.value.username || '同学')

onMounted(async () => {
  if (!authStore.userInfo) {
    await authStore.fetchProfile()
  }
})
</script>
