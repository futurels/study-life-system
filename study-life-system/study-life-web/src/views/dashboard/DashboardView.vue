<template>
  <AppLayout>
    <section class="dashboard-shell">
      <div class="hero-card">
        <p class="hero-label">Today</p>
        <h2>{{ nickname }}，今天的学习节奏已经准备好了</h2>
        <p>
          首页优先展示“今日学习计划概览”，帮助你快速看到今天要做什么、已经完成了什么。
        </p>
      </div>

      <div class="info-grid dashboard-metrics">
        <div class="info-card">
          <span class="info-title">今日任务数</span>
          <strong>{{ todayPlans.length }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">今日已完成</span>
          <strong>{{ completedCount }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">进行中</span>
          <strong>{{ processingCount }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">未开始</span>
          <strong>{{ pendingCount }}</strong>
        </div>
      </div>

      <div class="placeholder-card">
        <div class="section-header">
          <div>
            <h3>今日学习计划</h3>
            <p>展示今天的学习任务，并支持直接完成。</p>
          </div>
          <el-button type="primary" plain @click="goStudyPlan">进入学习计划页</el-button>
        </div>

        <el-empty v-if="!loading && !todayPlans.length" description="今天还没有学习计划，先去创建第一条任务吧。" />

        <div v-else class="today-plan-list">
          <div v-for="item in todayPlans" :key="item.id" class="today-plan-item">
            <div>
              <div class="today-plan-top">
                <el-tag :type="priorityTagType(item.priorityLevel)" effect="dark">
                  {{ priorityText(item.priorityLevel) }}
                </el-tag>
                <strong>{{ item.planTitle }}</strong>
              </div>
              <div class="today-plan-meta">
                <span>{{ formatTimeRange(item.startTime, item.endTime) }}</span>
                <el-tag :type="statusTagType(item.planStatus)" effect="plain">
                  {{ statusText(item.planStatus) }}
                </el-tag>
              </div>
            </div>
            <el-button
              v-if="item.planStatus !== 2"
              type="success"
              plain
              @click="handleQuickComplete(item)"
            >
              完成
            </el-button>
          </div>
        </div>
      </div>
    </section>
  </AppLayout>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppLayout from '@/components/layout/AppLayout.vue'
import { useAuthStore } from '@/stores/auth'
import { getStudyPlanListApi, updateStudyPlanStatusApi } from '@/api/studyPlan'
import { getUserInfo } from '@/utils/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const todayPlans = ref([])

const profile = computed(() => authStore.userInfo || getUserInfo() || {})
const nickname = computed(() => profile.value.nickname || profile.value.username || '同学')

const completedCount = computed(() => todayPlans.value.filter((item) => item.planStatus === 2).length)
const processingCount = computed(() => todayPlans.value.filter((item) => item.planStatus === 1).length)
const pendingCount = computed(() => todayPlans.value.filter((item) => item.planStatus === 0).length)

onMounted(async () => {
  if (!authStore.userInfo) {
    await authStore.fetchProfile()
  }
  await loadTodayPlans()
})

async function loadTodayPlans() {
  loading.value = true
  try {
    const response = await getStudyPlanListApi({
      pageNum: 1,
      pageSize: 6,
      planDate: formatDate(new Date())
    })
    todayPlans.value = response.data.list || []
  } finally {
    loading.value = false
  }
}

async function handleQuickComplete(item) {
  await updateStudyPlanStatusApi(item.id, { planStatus: 2 })
  ElMessage.success('任务已标记为完成')
  await loadTodayPlans()
}

function goStudyPlan() {
  router.push('/study-life/study-plan')
}

function formatDate(date) {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

function formatTimeRange(startTime, endTime) {
  if (!startTime && !endTime) {
    return '未设置时间'
  }
  if (startTime && endTime) {
    return `${startTime} - ${endTime}`
  }
  return startTime || endTime
}

function priorityText(value) {
  return ({ 1: '高', 2: '中', 3: '低' }[value] || '中')
}

function statusText(value) {
  return ({ 0: '未开始', 1: '进行中', 2: '已完成', 3: '已取消' }[value] || '未知')
}

function priorityTagType(value) {
  return ({ 1: 'danger', 2: 'warning', 3: 'info' }[value] || 'warning')
}

function statusTagType(value) {
  return ({ 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }[value] || 'info')
}
</script>
