<template>
  <AppLayout>
    <section class="life-record-page">
      <div class="page-panel">
        <div class="section-header">
          <div>
            <h2>生活记录</h2>
            <p>记录每天的生活状态与感受，让日常更有痕迹。</p>
          </div>
          <el-button type="primary" @click="openCreateDialog">新建生活记录</el-button>
        </div>

        <el-form :model="queryForm" inline class="filter-form">
          <el-form-item label="日期">
            <el-date-picker
              v-model="queryForm.recordDate"
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

          <el-form-item label="心情">
            <el-select v-model="queryForm.moodStatus" placeholder="全部心情" clearable>
              <el-option v-for="item in moodOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="page-panel">
        <div class="life-record-card-list" v-loading="loading">
          <el-empty v-if="!loading && !cardList.length" description="今天还没有生活记录，去写下第一条吧。" />

          <article v-for="item in cardList" :key="item.id" class="life-record-card">
            <div class="life-record-card-header">
              <div>
                <h3>{{ item.recordDate }}</h3>
                <div class="life-record-meta">
                  <el-tag :type="moodTagType(item.moodStatus)" effect="plain">
                    {{ item.moodStatus || '未记录心情' }}
                  </el-tag>
                  <span>{{ sleepText(item.sleepHours) }}</span>
                </div>
              </div>
              <div class="life-record-actions">
                <el-button link type="primary" @click="openDetailDialog(item.id)">查看</el-button>
                <el-button link type="primary" @click="openEditDialog(item.id)">编辑</el-button>
                <el-button link type="danger" @click="handleDelete(item.id)">删除</el-button>
              </div>
            </div>

            <p class="life-record-summary">{{ summaryText(item.diaryContent) }}</p>

            <div class="life-record-extra">
              <div class="life-record-extra-item">
                <span>饮食</span>
                <strong>{{ item.dietNote || '暂无记录' }}</strong>
              </div>
              <div class="life-record-extra-item">
                <span>运动</span>
                <strong>{{ item.exerciseNote || '暂无记录' }}</strong>
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
        :title="dialogMode === 'create' ? '新建生活记录' : '编辑生活记录'"
        width="720px"
        destroy-on-close
      >
        <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
          <el-form-item label="记录日期" prop="recordDate">
            <el-date-picker
              v-model="editForm.recordDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
            />
          </el-form-item>
          <el-form-item label="日记正文" prop="diaryContent">
            <el-input
              v-model="editForm.diaryContent"
              type="textarea"
              :rows="5"
              maxlength="3000"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="心情状态" prop="moodStatus">
            <el-select v-model="editForm.moodStatus" placeholder="选择心情" clearable>
              <el-option v-for="item in moodOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="睡眠时长" prop="sleepHours">
            <el-input-number
              v-model="editForm.sleepHours"
              :min="0"
              :max="24"
              :step="0.5"
              :precision="1"
              controls-position="right"
              placeholder="小时"
            />
          </el-form-item>
          <el-form-item label="饮食简述" prop="dietNote">
            <el-input v-model="editForm.dietNote" type="textarea" :rows="2" maxlength="500" show-word-limit />
          </el-form-item>
          <el-form-item label="运动简述" prop="exerciseNote">
            <el-input v-model="editForm.exerciseNote" type="textarea" :rows="2" maxlength="500" show-word-limit />
          </el-form-item>
        </el-form>

        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            {{ dialogMode === 'create' ? '创建' : '保存' }}
          </el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="detailDialogVisible" title="生活记录详情" width="640px" destroy-on-close>
        <div v-if="detailLoading" class="dialog-loading">正在加载详情...</div>
        <div v-else class="detail-grid">
          <div class="detail-item">
            <span>记录日期</span>
            <strong>{{ detailData.recordDate || '-' }}</strong>
          </div>
          <div class="detail-item">
            <span>心情状态</span>
            <strong>{{ detailData.moodStatus || '未记录' }}</strong>
          </div>
          <div class="detail-item">
            <span>睡眠时长</span>
            <strong>{{ sleepText(detailData.sleepHours) }}</strong>
          </div>
          <div class="detail-item">
            <span>创建时间</span>
            <strong>{{ formatDateTime(detailData.createdAt) }}</strong>
          </div>
          <div class="detail-item detail-item-full">
            <span>日记正文</span>
            <p>{{ detailData.diaryContent || '暂无内容' }}</p>
          </div>
          <div class="detail-item detail-item-full">
            <span>饮食简述</span>
            <p>{{ detailData.dietNote || '暂无记录' }}</p>
          </div>
          <div class="detail-item detail-item-full">
            <span>运动简述</span>
            <p>{{ detailData.exerciseNote || '暂无记录' }}</p>
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
  createLifeRecordApi,
  deleteLifeRecordApi,
  getLifeRecordDetailApi,
  getLifeRecordListApi,
  updateLifeRecordApi
} from '@/api/lifeRecord'

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
  recordDate: '',
  startDate: '',
  endDate: '',
  moodStatus: ''
})

const editForm = reactive({
  recordDate: '',
  diaryContent: '',
  moodStatus: '',
  sleepHours: null,
  dietNote: '',
  exerciseNote: ''
})

const moodOptions = [
  { label: '开心', value: '开心' },
  { label: '平静', value: '平静' },
  { label: '充实', value: '充实' },
  { label: '一般', value: '一般' },
  { label: '疲惫', value: '疲惫' }
]

const editRules = computed(() => ({
  recordDate: [{ required: true, message: '请选择记录日期', trigger: 'change' }],
  diaryContent: [{ required: true, message: '请输入日记正文', trigger: 'blur' }],
  moodStatus: [{ max: 20, message: '心情状态不能超过 20 个字符', trigger: 'blur' }],
  sleepHours: [
    {
      validator: (_, value, callback) => {
        if (value === null || value === undefined || value === '') {
          callback()
          return
        }
        if (value < 0 || value > 24) {
          callback(new Error('睡眠时长需在 0.0 到 24.0 之间'))
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
    if (queryForm.recordDate) params.recordDate = queryForm.recordDate
    if (queryForm.startDate) params.startDate = queryForm.startDate
    if (queryForm.endDate) params.endDate = queryForm.endDate
    if (queryForm.moodStatus) params.moodStatus = queryForm.moodStatus

    const response = await getLifeRecordListApi(params)
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
  queryForm.recordDate = ''
  queryForm.startDate = ''
  queryForm.endDate = ''
  queryForm.moodStatus = ''
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
  const response = await getLifeRecordDetailApi(id)
  Object.assign(editForm, {
    recordDate: response.data.recordDate || '',
    diaryContent: response.data.diaryContent || '',
    moodStatus: response.data.moodStatus || '',
    sleepHours: response.data.sleepHours ?? null,
    dietNote: response.data.dietNote || '',
    exerciseNote: response.data.exerciseNote || ''
  })
  editDialogVisible.value = true
}

async function openDetailDialog(id) {
  detailDialogVisible.value = true
  detailLoading.value = true
  try {
    const response = await getLifeRecordDetailApi(id)
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
      recordDate: editForm.recordDate,
      diaryContent: editForm.diaryContent,
      moodStatus: editForm.moodStatus || null,
      sleepHours: editForm.sleepHours === null || editForm.sleepHours === '' ? null : Number(editForm.sleepHours),
      dietNote: editForm.dietNote || null,
      exerciseNote: editForm.exerciseNote || null
    }

    if (dialogMode.value === 'create') {
      await createLifeRecordApi(payload)
      ElMessage.success('生活记录创建成功')
    } else {
      await updateLifeRecordApi(currentEditId.value, payload)
      ElMessage.success('生活记录修改成功')
    }

    editDialogVisible.value = false
    loadList()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('是否确认删除该条生活记录？', '删除确认', {
      type: 'warning'
    })
    await deleteLifeRecordApi(id)
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
    recordDate: '',
    diaryContent: '',
    moodStatus: '',
    sleepHours: null,
    dietNote: '',
    exerciseNote: ''
  })
}

function summaryText(text) {
  if (!text) {
    return '今天还没有补充日记内容。'
  }
  return text.length > 120 ? `${text.slice(0, 120)}...` : text
}

function sleepText(value) {
  if (value === null || value === undefined || value === '') {
    return '睡眠未记录'
  }
  return `睡眠 ${value}h`
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

function formatDateTime(value) {
  if (!value) {
    return '-'
  }
  return value.replace('T', ' ')
}
</script>
