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
          <strong>{{ overview.todayPlanCount }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">今日已完成</span>
          <strong>{{ overview.todayCompletedPlanCount }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">今日完成率</span>
          <strong>{{ percentText(overview.todayCompletionRate) }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">本周完成率</span>
          <strong>{{ percentText(overview.thisWeekCompletionRate) }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">本月生活记录</span>
          <strong>{{ overview.thisMonthLifeRecordDays }}</strong>
        </div>
        <div class="info-card">
          <span class="info-title">本月复盘天数</span>
          <strong>{{ overview.thisMonthReviewDays }}</strong>
        </div>
      </div>

      <div class="placeholder-card">
        <div class="section-header">
          <div>
            <h3>今日记录提醒</h3>
            <p>及时补上今天的生活记录，方便后续回看与复盘。</p>
          </div>
          <el-button
            :type="todayLifeRecord ? 'success' : 'primary'"
            plain
            @click="goLifeRecord"
          >
            {{ todayLifeRecord ? '已记录' : '去记录' }}
          </el-button>
        </div>

        <div class="record-reminder-card" :class="{ filled: !!todayLifeRecord }">
          <template v-if="todayLifeRecord">
            <div class="record-reminder-title">
              <el-tag :type="moodTagType(todayLifeRecord.moodStatus)" effect="plain">
                {{ todayLifeRecord.moodStatus || '未记录心情' }}
              </el-tag>
              <strong>{{ sleepText(todayLifeRecord.sleepHours) }}</strong>
            </div>
            <p>{{ summaryText(todayLifeRecord.diaryContent) }}</p>
          </template>
          <template v-else>
            <strong>今天还没有填写生活记录</strong>
            <p>去写下一天里的心情、睡眠、饮食和运动，让首页提醒变成“已记录”。</p>
          </template>
        </div>
      </div>

      <div class="placeholder-card">
        <div class="section-header">
          <div>
            <h3>今日复盘提醒</h3>
            <p>当天的总结和明日计划写下来，第二天会更有方向感。</p>
          </div>
          <el-button
            :type="todayDailyReview ? 'success' : 'primary'"
            plain
            @click="goDailyReview"
          >
            {{ todayDailyReview ? '已复盘' : '去复盘' }}
          </el-button>
        </div>

        <div class="record-reminder-card" :class="{ filled: !!todayDailyReview }">
          <template v-if="todayDailyReview">
            <div class="record-reminder-title">
              <el-tag :type="scoreTagType(todayDailyReview.reviewScore)" effect="dark">
                {{ reviewScoreText(todayDailyReview.reviewScore) }}
              </el-tag>
            </div>
            <p>{{ reviewSummaryText(todayDailyReview.overallEvaluation || todayDailyReview.completedItems) }}</p>
          </template>
          <template v-else>
            <strong>今天还没有填写每日复盘</strong>
            <p>去总结今天的完成情况、反思未完成原因，并把明天的计划先写下来。</p>
          </template>
        </div>
      </div>

      <div class="placeholder-card">
        <div class="section-header">
          <div>
            <h3>统计概览</h3>
            <p>查看本周任务完成情况，以及本月记录与复盘天数。</p>
          </div>
          <el-button type="primary" plain @click="goStatistics">进入统计分析页</el-button>
        </div>

        <div class="dashboard-overview-list">
          <div class="overview-row">
            <span>本周计划总数</span>
            <strong>{{ overview.thisWeekPlanCount }}</strong>
          </div>
          <div class="overview-row">
            <span>本周已完成任务</span>
            <strong>{{ overview.thisWeekCompletedPlanCount }}</strong>
          </div>
          <div class="overview-row">
            <span>本月生活记录天数</span>
            <strong>{{ overview.thisMonthLifeRecordDays }}</strong>
          </div>
          <div class="overview-row">
            <span>本月复盘天数</span>
            <strong>{{ overview.thisMonthReviewDays }}</strong>
          </div>
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
import { getLifeRecordListApi } from '@/api/lifeRecord'
import { getDailyReviewListApi } from '@/api/dailyReview'
import { getStatisticsOverviewApi } from '@/api/statistics'
import { getUserInfo } from '@/utils/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const todayPlans = ref([])
const todayLifeRecord = ref(null)
const todayDailyReview = ref(null)
const overview = ref({
  todayPlanCount: 0,
  todayCompletedPlanCount: 0,
  todayCompletionRate: 0,
  thisWeekPlanCount: 0,
  thisWeekCompletedPlanCount: 0,
  thisWeekCompletionRate: 0,
  thisMonthLifeRecordDays: 0,
  thisMonthReviewDays: 0
})

const profile = computed(() => authStore.userInfo || getUserInfo() || {})
const nickname = computed(() => profile.value.nickname || profile.value.username || '同学')

onMounted(async () => {
  if (!authStore.userInfo) {
    await authStore.fetchProfile()
  }
  await Promise.all([loadOverview(), loadTodayPlans(), loadTodayLifeRecord(), loadTodayDailyReview()])
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
  await Promise.all([loadTodayPlans(), loadOverview()])
}

async function loadOverview() {
  const response = await getStatisticsOverviewApi()
  overview.value = response.data || overview.value
}

async function loadTodayLifeRecord() {
  const response = await getLifeRecordListApi({
    pageNum: 1,
    pageSize: 1,
    recordDate: formatDate(new Date())
  })
  todayLifeRecord.value = response.data.list?.[0] || null
}

async function loadTodayDailyReview() {
  const response = await getDailyReviewListApi({
    pageNum: 1,
    pageSize: 1,
    reviewDate: formatDate(new Date())
  })
  todayDailyReview.value = response.data.list?.[0] || null
}

function goStudyPlan() {
  router.push('/study-life/study-plan')
}

function goLifeRecord() {
  router.push('/study-life/life-record')
}

function goDailyReview() {
  router.push('/study-life/daily-review')
}

function goStatistics() {
  router.push('/study-life/statistics')
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

function sleepText(value) {
  if (value === null || value === undefined || value === '') {
    return '睡眠未记录'
  }
  return `睡眠 ${value}h`
}

function summaryText(text) {
  if (!text) {
    return '今天还没有补充生活摘要。'
  }
  return text.length > 90 ? `${text.slice(0, 90)}...` : text
}

function moodTagType(value) {
  return (
    {
      开心: 'success',
      平静: 'primary',
      充实: 'warning',
      一般: 'info',
      疲惫: 'danger'
    }[value] || 'info'
  )
}

function reviewScoreText(value) {
  if (value === null || value === undefined || value === '') {
    return '未评分'
  }
  return `评分 ${value} 分`
}

function reviewSummaryText(text) {
  if (!text) {
    return '今天已完成复盘，可以随时进入页面补充更完整的总结。'
  }
  return text.length > 90 ? `${text.slice(0, 90)}...` : text
}

function scoreTagType(value) {
  if (value === null || value === undefined) {
    return 'info'
  }
  if (value >= 8) {
    return 'success'
  }
  if (value >= 6) {
    return 'warning'
  }
  return 'danger'
}

function percentText(value) {
  return `${Number(value || 0).toFixed(2)}%`
}
</script>
