<template>
  <AppLayout>
    <section class="study-plan-page">
      <div class="page-panel">
        <div class="section-header">
          <div>
            <h2>学习计划</h2>
            <p>管理每日学习任务与完成状态。</p>
          </div>
          <el-button type="primary" @click="openCreateDialog">新建学习计划</el-button>
        </div>

        <el-form :model="queryForm" inline class="filter-form">
          <el-form-item label="日期">
            <el-date-picker
              v-model="queryForm.planDate"
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

          <el-form-item label="状态">
            <el-select v-model="queryForm.planStatus" placeholder="全部状态" clearable>
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item label="优先级">
            <el-select v-model="queryForm.priorityLevel" placeholder="全部优先级" clearable>
              <el-option v-for="item in priorityOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="page-panel">
        <el-table :data="tableData" v-loading="loading" class="study-plan-table">
          <el-table-column prop="planTitle" label="计划标题" min-width="180" show-overflow-tooltip />
          <el-table-column prop="planDate" label="计划日期" width="120" />
          <el-table-column label="时间" width="190">
            <template #default="{ row }">
              {{ formatTimeRange(row.startTime, row.endTime) }}
            </template>
          </el-table-column>
          <el-table-column label="优先级" width="100">
            <template #default="{ row }">
              <el-tag :type="priorityTagType(row.priorityLevel)" effect="dark">
                {{ priorityText(row.priorityLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="110">
            <template #default="{ row }">
              <el-tag :type="statusTagType(row.planStatus)" effect="plain">
                {{ statusText(row.planStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
          <el-table-column label="操作" width="300" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="openDetailDialog(row.id)">查看</el-button>
              <el-button link type="primary" @click="openEditDialog(row.id)">编辑</el-button>
              <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
              <el-button
                v-if="row.planStatus !== 2"
                link
                type="success"
                @click="handleComplete(row.id)"
              >
                完成
              </el-button>
            </template>
          </el-table-column>
        </el-table>

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
        :title="dialogMode === 'create' ? '新建学习计划' : '编辑学习计划'"
        width="640px"
        destroy-on-close
      >
        <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
          <el-form-item label="计划标题" prop="planTitle">
            <el-input v-model="editForm.planTitle" maxlength="100" show-word-limit />
          </el-form-item>
          <el-form-item label="计划内容" prop="planContent">
            <el-input v-model="editForm.planContent" type="textarea" :rows="4" />
          </el-form-item>
          <el-form-item label="所属日期" prop="planDate">
            <el-date-picker
              v-model="editForm.planDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
            />
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-time-picker
              v-model="editForm.startTime"
              value-format="HH:mm:ss"
              placeholder="开始时间"
              clearable
            />
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-time-picker
              v-model="editForm.endTime"
              value-format="HH:mm:ss"
              placeholder="结束时间"
              clearable
            />
          </el-form-item>
          <el-form-item label="优先级" prop="priorityLevel">
            <el-select v-model="editForm.priorityLevel">
              <el-option v-for="item in priorityOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="editForm.remark" type="textarea" :rows="3" maxlength="500" show-word-limit />
          </el-form-item>
        </el-form>

        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            {{ dialogMode === 'create' ? '创建' : '保存' }}
          </el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="detailDialogVisible" title="学习计划详情" width="560px" destroy-on-close>
        <div v-if="detailLoading" class="dialog-loading">正在加载详情...</div>
        <div v-else class="detail-grid">
          <div class="detail-item">
            <span>计划标题</span>
            <strong>{{ detailData.planTitle || '-' }}</strong>
          </div>
          <div class="detail-item">
            <span>计划日期</span>
            <strong>{{ detailData.planDate || '-' }}</strong>
          </div>
          <div class="detail-item">
            <span>开始时间</span>
            <strong>{{ detailData.startTime || '未设置' }}</strong>
          </div>
          <div class="detail-item">
            <span>结束时间</span>
            <strong>{{ detailData.endTime || '未设置' }}</strong>
          </div>
          <div class="detail-item">
            <span>优先级</span>
            <strong>{{ priorityText(detailData.priorityLevel) }}</strong>
          </div>
          <div class="detail-item">
            <span>状态</span>
            <strong>{{ statusText(detailData.planStatus) }}</strong>
          </div>
          <div class="detail-item detail-item-full">
            <span>计划内容</span>
            <p>{{ detailData.planContent || '暂无内容' }}</p>
          </div>
          <div class="detail-item detail-item-full">
            <span>备注</span>
            <p>{{ detailData.remark || '暂无备注' }}</p>
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
  createStudyPlanApi,
  deleteStudyPlanApi,
  getStudyPlanDetailApi,
  getStudyPlanListApi,
  updateStudyPlanApi,
  updateStudyPlanStatusApi
} from '@/api/studyPlan'

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
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
  planDate: '',
  startDate: '',
  endDate: '',
  planStatus: undefined,
  priorityLevel: undefined
})

const editForm = reactive({
  planTitle: '',
  planContent: '',
  planDate: '',
  startTime: '',
  endTime: '',
  priorityLevel: 2,
  remark: ''
})

const statusOptions = [
  { label: '未开始', value: 0 },
  { label: '进行中', value: 1 },
  { label: '已完成', value: 2 },
  { label: '已取消', value: 3 }
]

const priorityOptions = [
  { label: '高', value: 1 },
  { label: '中', value: 2 },
  { label: '低', value: 3 }
]

const editRules = computed(() => ({
  planTitle: [{ required: true, message: '请输入计划标题', trigger: 'blur' }],
  planDate: [{ required: true, message: '请选择所属日期', trigger: 'change' }],
  priorityLevel: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  endTime: [
    {
      validator: (_, value, callback) => {
        if (value && editForm.startTime && value < editForm.startTime) {
          callback(new Error('结束时间不能早于开始时间'))
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
    if (queryForm.planDate) params.planDate = queryForm.planDate
    if (queryForm.startDate) params.startDate = queryForm.startDate
    if (queryForm.endDate) params.endDate = queryForm.endDate
    if (queryForm.planStatus !== undefined) params.planStatus = queryForm.planStatus
    if (queryForm.priorityLevel !== undefined) params.priorityLevel = queryForm.priorityLevel

    const response = await getStudyPlanListApi(params)
    tableData.value = response.data.list || []
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
  queryForm.planDate = ''
  queryForm.startDate = ''
  queryForm.endDate = ''
  queryForm.planStatus = undefined
  queryForm.priorityLevel = undefined
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
  const response = await getStudyPlanDetailApi(id)
  Object.assign(editForm, {
    planTitle: response.data.planTitle || '',
    planContent: response.data.planContent || '',
    planDate: response.data.planDate || '',
    startTime: response.data.startTime || '',
    endTime: response.data.endTime || '',
    priorityLevel: response.data.priorityLevel ?? 2,
    remark: response.data.remark || ''
  })
  editDialogVisible.value = true
}

async function openDetailDialog(id) {
  detailDialogVisible.value = true
  detailLoading.value = true
  try {
    const response = await getStudyPlanDetailApi(id)
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
      planTitle: editForm.planTitle,
      planContent: editForm.planContent || '',
      planDate: editForm.planDate,
      startTime: editForm.startTime || null,
      endTime: editForm.endTime || null,
      priorityLevel: editForm.priorityLevel,
      remark: editForm.remark || ''
    }

    if (dialogMode.value === 'create') {
      await createStudyPlanApi(payload)
      ElMessage.success('学习计划创建成功')
    } else {
      await updateStudyPlanApi(currentEditId.value, payload)
      ElMessage.success('学习计划修改成功')
    }

    editDialogVisible.value = false
    loadList()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('是否确认删除该条学习计划？', '删除确认', {
      type: 'warning'
    })
    await deleteStudyPlanApi(id)
    ElMessage.success('删除成功')
    if (tableData.value.length === 1 && queryForm.pageNum > 1) {
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

async function handleComplete(id) {
  await updateStudyPlanStatusApi(id, { planStatus: 2 })
  ElMessage.success('已标记为完成')
  loadList()
}

function resetEditForm() {
  Object.assign(editForm, {
    planTitle: '',
    planContent: '',
    planDate: '',
    startTime: '',
    endTime: '',
    priorityLevel: 2,
    remark: ''
  })
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

function formatTimeRange(startTime, endTime) {
  if (!startTime && !endTime) {
    return '未设置时间'
  }
  if (startTime && endTime) {
    return `${startTime} - ${endTime}`
  }
  return startTime || endTime
}
</script>
