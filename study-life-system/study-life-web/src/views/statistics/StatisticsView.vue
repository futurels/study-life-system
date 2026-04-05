<template>
  <AppLayout>
    <section class="statistics-page">
      <div class="page-panel">
        <div class="section-header">
          <div>
            <h2>统计分析</h2>
            <p>查看近期任务、生活记录和复盘的变化趋势。</p>
          </div>
        </div>

        <div class="statistics-range-group">
          <button
            v-for="item in rangeOptions"
            :key="item.value"
            type="button"
            class="range-chip"
            :class="{ active: activeRange === item.value }"
            @click="handleRangeChange(item.value)"
          >
            {{ item.label }}
          </button>
        </div>
      </div>

      <div class="info-grid statistics-metrics">
        <div v-for="item in metricCards" :key="item.label" class="info-card">
          <span class="info-title">{{ item.label }}</span>
          <strong>{{ item.value }}</strong>
          <p>{{ item.tip }}</p>
        </div>
      </div>

      <div class="statistics-chart-grid">
        <div class="page-panel chart-panel">
          <div class="section-header">
            <div>
              <h3>{{ trendTitle }}</h3>
              <p>计划总数与已完成任务数按天变化。</p>
            </div>
          </div>

          <el-empty v-if="!hasTrendData" description="当前范围暂无任务趋势数据" />

          <template v-else>
            <div class="chart-legend">
              <span><i class="legend-dot total"></i>计划总数</span>
              <span><i class="legend-dot done"></i>已完成</span>
            </div>
            <svg class="line-chart" viewBox="0 0 640 260" preserveAspectRatio="none">
              <g>
                <line
                  v-for="tick in 5"
                  :key="tick"
                  x1="52"
                  :y1="20 + (tick - 1) * 50"
                  x2="612"
                  :y2="20 + (tick - 1) * 50"
                  class="chart-grid-line"
                />
              </g>
              <polyline :points="planLinePoints" class="chart-line total" />
              <polyline :points="completedLinePoints" class="chart-line done" />
              <g v-for="point in trendPlotPoints" :key="point.date">
                <circle :cx="point.x" :cy="point.planY" r="4" class="chart-point total" />
                <circle :cx="point.x" :cy="point.completedY" r="4" class="chart-point done" />
                <text :x="point.x" y="244" text-anchor="middle" class="chart-label">
                  {{ shortDate(point.date) }}
                </text>
              </g>
            </svg>
          </template>
        </div>

        <div class="page-panel chart-panel">
          <div class="section-header">
            <div>
              <h3>{{ distributionTitle }}</h3>
              <p>按任务状态查看当前范围内的分布情况。</p>
            </div>
          </div>

          <el-empty v-if="!hasDistributionData" description="当前范围暂无任务状态分布数据" />

          <div v-else class="distribution-list">
            <div v-for="item in distributionRows" :key="item.planStatus" class="distribution-row">
              <div class="distribution-meta">
                <span>{{ statusText(item.planStatus) }}</span>
                <strong>{{ item.count }}</strong>
              </div>
              <div class="distribution-track">
                <div
                  class="distribution-fill"
                  :class="statusClass(item.planStatus)"
                  :style="{ width: `${item.percent}%` }"
                ></div>
              </div>
            </div>
          </div>
        </div>

        <div class="page-panel chart-panel">
          <div class="section-header">
            <div>
              <h3>本月记录情况</h3>
              <p>生活记录天数、复盘天数与当月计划完成趋势。</p>
            </div>
          </div>

          <el-empty v-if="!hasMonthlyInsight" description="当前月份暂无统计数据" />

          <template v-else>
            <div class="monthly-bars">
              <div v-for="item in monthlyBars" :key="item.label" class="monthly-bar-item">
                <div class="monthly-bar-track">
                  <div
                    class="monthly-bar-fill"
                    :class="item.className"
                    :style="{ height: `${item.height}%` }"
                  ></div>
                </div>
                <strong>{{ item.value }}</strong>
                <span>{{ item.label }}</span>
              </div>
            </div>

            <div class="mini-trend">
              <div class="mini-trend-header">本月计划完成趋势</div>
              <div class="mini-trend-list">
                <div v-for="item in monthlyTrendBars" :key="item.date" class="mini-trend-item">
                  <div class="mini-trend-track">
                    <div class="mini-trend-fill" :style="{ height: `${item.height}%` }"></div>
                  </div>
                  <span>{{ dayLabel(item.date) }}</span>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>
    </section>
  </AppLayout>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import AppLayout from '@/components/layout/AppLayout.vue'
import { getStatisticsMonthlyApi, getStatisticsWeeklyApi } from '@/api/statistics'

const activeRange = ref('week')
const rangeWeeklyData = ref(createWeeklyFallback())
const monthlyData = ref(createMonthlyFallback())
const loading = ref(false)

const rangeOptions = [
  { label: '本周', value: 'week' },
  { label: '本月', value: 'month' },
  { label: '最近7天', value: 'recent7' },
  { label: '最近30天', value: 'recent30' }
]

onMounted(async () => {
  await Promise.all([loadRangeStatistics(), loadMonthlyStatistics()])
})

const metricCards = computed(() => {
  const data = rangeWeeklyData.value
  return [
    { label: '任务总数', value: data.planCount ?? 0, tip: `${rangeLabel.value}内共安排的学习任务` },
    { label: '已完成任务数', value: data.completedPlanCount ?? 0, tip: '已标记为完成的任务数量' },
    { label: '完成率', value: `${formatPercent(data.completionRate)}%`, tip: '已完成任务占计划总数的比例' },
    { label: '生活记录天数', value: data.lifeRecordDays ?? 0, tip: `${rangeLabel.value}内填写生活记录的天数` },
    { label: '复盘天数', value: data.reviewDays ?? 0, tip: `${rangeLabel.value}内填写每日复盘的天数` }
  ]
})

const rangeLabel = computed(() => {
  return rangeOptions.find((item) => item.value === activeRange.value)?.label || '当前范围'
})

const trendTitle = computed(() => `${rangeLabel.value}任务完成趋势`)
const distributionTitle = computed(() => `${rangeLabel.value}任务状态分布`)

const hasTrendData = computed(() => rangeWeeklyData.value.dailyTrend?.some((item) => item.planCount || item.completedPlanCount))
const hasDistributionData = computed(() => distributionRows.value.some((item) => item.count > 0))
const hasMonthlyInsight = computed(() => {
  return (monthlyData.value.lifeRecordDays || 0) > 0
    || (monthlyData.value.reviewDays || 0) > 0
    || monthlyData.value.planCompletionTrend?.some((item) => item.completedPlanCount)
})

const trendPlotPoints = computed(() => {
  const list = rangeWeeklyData.value.dailyTrend || []
  if (!list.length) {
    return []
  }

  const maxValue = Math.max(
    1,
    ...list.flatMap((item) => [Number(item.planCount || 0), Number(item.completedPlanCount || 0)])
  )

  return list.map((item, index) => {
    const x = list.length === 1 ? 332 : 60 + (index * 540) / (list.length - 1)
    return {
      date: item.date,
      x,
      planY: calcChartY(Number(item.planCount || 0), maxValue),
      completedY: calcChartY(Number(item.completedPlanCount || 0), maxValue)
    }
  })
})

const planLinePoints = computed(() => trendPlotPoints.value.map((item) => `${item.x},${item.planY}`).join(' '))
const completedLinePoints = computed(() => trendPlotPoints.value.map((item) => `${item.x},${item.completedY}`).join(' '))

const distributionRows = computed(() => {
  const list = rangeWeeklyData.value.statusDistribution || []
  const total = list.reduce((sum, item) => sum + Number(item.count || 0), 0)
  return list.map((item) => ({
    ...item,
    percent: total ? Number(((Number(item.count || 0) / total) * 100).toFixed(2)) : 0
  }))
})

const monthlyBars = computed(() => {
  const max = Math.max(1, Number(monthlyData.value.lifeRecordDays || 0), Number(monthlyData.value.reviewDays || 0))
  return [
    {
      label: '生活记录',
      value: monthlyData.value.lifeRecordDays || 0,
      height: calcBarHeight(monthlyData.value.lifeRecordDays || 0, max),
      className: 'life'
    },
    {
      label: '每日复盘',
      value: monthlyData.value.reviewDays || 0,
      height: calcBarHeight(monthlyData.value.reviewDays || 0, max),
      className: 'review'
    }
  ]
})

const monthlyTrendBars = computed(() => {
  const list = monthlyData.value.planCompletionTrend || []
  const max = Math.max(1, ...list.map((item) => Number(item.completedPlanCount || 0)))
  return list.map((item) => ({
    date: item.date,
    height: calcBarHeight(item.completedPlanCount || 0, max)
  }))
})

async function handleRangeChange(value) {
  activeRange.value = value
  await loadRangeStatistics()
}

async function loadRangeStatistics() {
  loading.value = true
  try {
    const params = buildRangeParams(activeRange.value)
    const response = await getStatisticsWeeklyApi(params)
    rangeWeeklyData.value = response.data || createWeeklyFallback()
  } finally {
    loading.value = false
  }
}

async function loadMonthlyStatistics() {
  const response = await getStatisticsMonthlyApi({
    month: formatMonth(new Date())
  })
  monthlyData.value = response.data || createMonthlyFallback()
}

function buildRangeParams(rangeType) {
  const today = new Date()
  const end = toDateString(today)

  if (rangeType === 'week') {
    const monday = getStartOfWeek(today)
    const sunday = new Date(monday)
    sunday.setDate(monday.getDate() + 6)
    return {
      weekStartDate: toDateString(monday),
      weekEndDate: toDateString(sunday)
    }
  }

  if (rangeType === 'month') {
    const monthStart = new Date(today.getFullYear(), today.getMonth(), 1)
    const monthEnd = new Date(today.getFullYear(), today.getMonth() + 1, 0)
    return {
      weekStartDate: toDateString(monthStart),
      weekEndDate: toDateString(monthEnd)
    }
  }

  const span = rangeType === 'recent30' ? 29 : 6
  const start = new Date(today)
  start.setDate(today.getDate() - span)
  return {
    weekStartDate: toDateString(start),
    weekEndDate: end
  }
}

function getStartOfWeek(date) {
  const copy = new Date(date)
  const day = copy.getDay() || 7
  copy.setDate(copy.getDate() - day + 1)
  return copy
}

function toDateString(date) {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

function formatMonth(date) {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`
}

function calcChartY(value, max) {
  return 220 - (value / max) * 180
}

function calcBarHeight(value, max) {
  return max ? Number(((Number(value || 0) / max) * 100).toFixed(2)) : 0
}

function formatPercent(value) {
  return Number(value || 0).toFixed(2)
}

function shortDate(value) {
  if (!value) {
    return ''
  }
  return value.slice(5)
}

function dayLabel(value) {
  if (!value) {
    return ''
  }
  return value.slice(-2)
}

function statusText(value) {
  return ({ 0: '未开始', 1: '进行中', 2: '已完成', 3: '已取消' }[value] || '未知')
}

function statusClass(value) {
  return ({
    0: 'pending',
    1: 'processing',
    2: 'completed',
    3: 'canceled'
  }[value] || 'pending')
}

function createWeeklyFallback() {
  return {
    planCount: 0,
    completedPlanCount: 0,
    completionRate: 0,
    lifeRecordDays: 0,
    reviewDays: 0,
    dailyTrend: [],
    statusDistribution: []
  }
}

function createMonthlyFallback() {
  return {
    lifeRecordDays: 0,
    reviewDays: 0,
    planCompletionTrend: []
  }
}
</script>
