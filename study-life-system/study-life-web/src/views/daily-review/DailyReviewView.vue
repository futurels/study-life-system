<template>
  <AppLayout>
    <section class="daily-review-page">
      <div class="page-panel">
        <div class="section-header">
          <div>
            <h2>每日复盘</h2>
            <p>总结今天，规划明天，把一天的得失沉淀下来。</p>
          </div>
          <el-button type="primary" @click="openCreateDialog">新建每日复盘</el-button>
        </div>

        <el-form :model="queryForm" inline class="filter-form">
          <el-form-item label="日期">
            <el-date-picker
              v-model="queryForm.reviewDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
              clearable
            />
          </el-form-item>

          <el-form-item label="日期范围">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              value-format="YYYY-MM-DD"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              range-separator="至"
              clearable
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="page-panel">
        <div class="daily-review-card-list" v-loading="loading">
          <el-empty v-if="!loading && !cardList.length" description="今天还没有复盘，去写下第一份总结吧。" />

          <article v-for="item in cardList" :key="item.id" class="daily-review-card">
            <div class="daily-review-card-header">
              <div>
                <h3>{{ item.reviewDate }}</h3>
                <div class="daily-review-score">
                  <el-tag :type="scoreTagType(item.reviewScore)" effect="dark">
                    {{ scoreText(item.reviewScore) }}
                  </el-tag>
                </div>
              </div>
              <div class="daily-review-actions">
                <el-button link type="primary" @click="openDetailDialog(item.id)">查看</el-button>
                <el-button link type="primary" @click="openEditDialog(item.id)">编辑</el-button>
                <el-button link type="danger" @click="handleDelete(item.id)">删除</el-button>
              </div>
            </div>

            <div class="daily-review-content-block">
              <div class="daily-review-line">
                <span>已完成</span>
                <p>{{ summaryText(item.completedItems) }}</p>
              </div>
              <div class="daily-review-line">
                <span>未完成</span>
                <p>{{ summaryText(item.unfinishedItems) }}</p>
              </div>
              <div class="daily-review-line">
                <span>整体评价</span>
                <p>{{ summaryText(item.overallEvaluation) }}</p>
              </div>
            </div>
          </article>
        </div>

        <div class="pagination-bar">
          <el-pagination
            background
            layout="total, sizes, prev, pager, next"
            :current-page="queryForm.pageNum"
            :page-size="queryForm.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>

      <el-dialog
        v-model="editDialogVisible"
        :title="dialogMode === 'create' ? '新建每日复盘' : '编辑每日复盘'"
        width="760px"
        destroy-on-close
      >
        <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
          <el-form-item label="复盘日期" prop="reviewDate">
            <el-date-picker
              v-model="editForm.reviewDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
            />
          </el-form-item>
          <el-form-item label="今日完成" prop="completedItems">
            <el-input v-model="editForm.completedItems" type="textarea" :rows="3" />
          </el-form-item>
          <el-form-item label="未完成项" prop="unfinishedItems">
            <el-input v-model="editForm.unfinishedItems" type="textarea" :rows="3" />
          </el-form-item>
          <el-form-item label="未完成原因" prop="unfinishedReason">
            <el-input v-model="editForm.unfinishedReason" type="textarea" :rows="3" />
          </el-form-item>
          <el-form-item label="整体评价" prop="overallEvaluation">
            <el-input
              v-model="editForm.overallEvaluation"
              type="textarea"
              :rows="3"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="明日计划" prop="tomorrowPlan">
            <el-input v-model="editForm.tomorrowPlan" type="textarea" :rows="3" />
          </el-form-item>
          <el-form-item label="今日评分" prop="reviewScore">
            <el-input-number
              v-model="editForm.reviewScore"
              :min="1"
              :max="10"
              :step="1"
              controls-position="right"
              placeholder="1 到 10"
            />
          </el-form-item>
        </el-form>

        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            {{ dialogMode === 'create' ? '创建' : '保存' }}
          </el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="detailDialogVisible" title="每日复盘详情" width="680px" destroy-on-close>
        <div v-if="detailLoading" class="dialog-loading">正在加载详情...</div>
        <div v-else class="detail-grid">
          <div class="detail-item">
            <span>复盘日期</span>
            <strong>{{ detailData.reviewDate || '-' }}</strong>
          </div>
          <div class="detail-item">
            <span>今日评分</span>
            <strong>{{ scoreText(detailData.reviewScore) }}</strong>
          </div>
          <div class="detail-item detail-item-full">
            <span>今日完成事项</span>
            <p>{{ detailData.completedItems || '暂无内容' }}</p>
          </div>
          <div class="detail-item detail-item-full">
            <span>今日未完成事项</span>
            <p>{{ detailData.unfinishedItems || '暂无内容' }}</p>
          </div>
          <div class="detail-item detail-item-full">
            <span>未完成原因</span>
            <p>{{ detailData.unfinishedReason || '暂无内容' }}</p>
          </div>
          <div class="detail-item detail-item-full">
            <span>整体评价</span>
            <p>{{ detailData.overallEvaluation || '暂无内容' }}</p>
          </div>
          <div class="detail-item detail-item-full">
            <span>明日计划</span>
            <p>{{ detailData.tomorrowPlan || '暂无内容' }}</p>
          </div>
        </div>
      </el-dialog>
    </section>
  </AppLayout>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppLayout from '@/components/layout/AppLayout.vue'
import {
  createDailyReviewApi,
  deleteDailyReviewApi,
  getDailyReviewDetailApi,
  getDailyReviewListApi,
  updateDailyReviewApi
} from '@/api/dailyReview'

const loading = ref(false)
const total = ref(0)
const cardList = ref([])
const dateRange = ref([])

const detailDialogVisible = ref(false)
const detailLoading = ref(false)
const detailData = ref({})

const editDialogVisible = ref(false)
const editFormRef = ref()
const submitLoading = ref(false)
const dialogMode = ref('create')
const currentEditId = ref(null)

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  reviewDate: '',
  startDate: '',
  endDate: ''
})

const editForm = reactive({
  reviewDate: '',
  completedItems: '',
  unfinishedItems: '',
  unfinishedReason: '',
  overallEvaluation: '',
  tomorrowPlan: '',
  reviewScore: null
})

const editRules = computed(() => ({
  reviewDate: [{ required: true, message: '请选择复盘日期', trigger: 'change' }],
  overallEvaluation: [{ max: 500, message: '整体评价不能超过 500 个字符', trigger: 'blur' }],
  reviewScore: [
    {
      validator: (_, value, callback) => {
        if (value === null || value === undefined || value === '') {
          callback()
          return
        }
        if (value < 1 || value > 10) {
          callback(new Error('今日评分需在 1 到 10 之间'))
          return
        }
        callback()
      },
      trigger: 'change'
    }
  ]
}))

watch(dateRange, (value) => {
  queryForm.startDate = value?.[0] || ''
  queryForm.endDate = value?.[1] || ''
})

loadList()

async function loadList() {
  loading.value = true
  try {
    const params = {
      pageNum: queryForm.pageNum,
      pageSize: queryForm.pageSize
    }
    if (queryForm.reviewDate) params.reviewDate = queryForm.reviewDate
    if (queryForm.startDate) params.startDate = queryForm.startDate
    if (queryForm.endDate) params.endDate = queryForm.endDate

    const response = await getDailyReviewListApi(params)
    cardList.value = response.data.list || []
    total.value = response.data.total || 0
  } finally {
    loading.value = false
  }
}

function handleQuery() {
  queryForm.pageNum = 1
  loadList()
}

function handleReset() {
  queryForm.pageNum = 1
  queryForm.pageSize = 10
  queryForm.reviewDate = ''
  queryForm.startDate = ''
  queryForm.endDate = ''
  dateRange.value = []
  loadList()
}

function handlePageChange(page) {
  queryForm.pageNum = page
  loadList()
}

function handleSizeChange(size) {
  queryForm.pageSize = size
  queryForm.pageNum = 1
  loadList()
}

function openCreateDialog() {
  dialogMode.value = 'create'
  currentEditId.value = null
  resetEditForm()
  editDialogVisible.value = true
}

async function openEditDialog(id) {
  dialogMode.value = 'edit'
  currentEditId.value = id
  resetEditForm()
  const response = await getDailyReviewDetailApi(id)
  Object.assign(editForm, {
    reviewDate: response.data.reviewDate || '',
    completedItems: response.data.completedItems || '',
    unfinishedItems: response.data.unfinishedItems || '',
    unfinishedReason: response.data.unfinishedReason || '',
    overallEvaluation: response.data.overallEvaluation || '',
    tomorrowPlan: response.data.tomorrowPlan || '',
    reviewScore: response.data.reviewScore ?? null
  })
  editDialogVisible.value = true
}

async function openDetailDialog(id) {
  detailDialogVisible.value = true
  detailLoading.value = true
  try {
    const response = await getDailyReviewDetailApi(id)
    detailData.value = response.data || {}
  } finally {
    detailLoading.value = false
  }
}

async function handleSubmit() {
  const valid = await editFormRef.value.validate().catch(() => false)
  if (!valid) {
    return
  }

  submitLoading.value = true
  try {
    const payload = {
      reviewDate: editForm.reviewDate,
      completedItems: editForm.completedItems || null,
      unfinishedItems: editForm.unfinishedItems || null,
      unfinishedReason: editForm.unfinishedReason || null,
      overallEvaluation: editForm.overallEvaluation || null,
      tomorrowPlan: editForm.tomorrowPlan || null,
      reviewScore: editForm.reviewScore === null || editForm.reviewScore === '' ? null : Number(editForm.reviewScore)
    }

    if (dialogMode.value === 'create') {
      await createDailyReviewApi(payload)
      ElMessage.success('每日复盘创建成功')
    } else {
      await updateDailyReviewApi(currentEditId.value, payload)
      ElMessage.success('每日复盘修改成功')
    }

    editDialogVisible.value = false
    loadList()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('是否确认删除该条每日复盘？', '删除确认', {
      type: 'warning'
    })
    await deleteDailyReviewApi(id)
    ElMessage.success('删除成功')
    if (cardList.value.length === 1 && queryForm.pageNum > 1) {
      queryForm.pageNum -= 1
    }
    loadList()
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      return
    }
    throw error
  }
}

function resetEditForm() {
  Object.assign(editForm, {
    reviewDate: '',
    completedItems: '',
    unfinishedItems: '',
    unfinishedReason: '',
    overallEvaluation: '',
    tomorrowPlan: '',
    reviewScore: null
  })
}

function summaryText(text) {
  if (!text) {
    return '暂无内容'
  }
  return text.length > 120 ? `${text.slice(0, 120)}...` : text
}

function scoreText(value) {
  if (value === null || value === undefined || value === '') {
    return '未评分'
  }
  return `今日评分：${value}分`
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
</script>
