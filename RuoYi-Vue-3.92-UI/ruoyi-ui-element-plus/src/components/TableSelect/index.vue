<template>
  <div ref="tableSelectRef" :style="'width:' + width">
    <el-popover
      :visible="popoverVisible"
      :width="popoverWidth"
      placement="bottom-end"
      v-bind="selectConfig.popover"
      @show="handleShow"
    >
      <template #reference>
        <div @click="popoverVisible = !popoverVisible">
          <slot>
            <el-input
              class="reference"
              :model-value="text"
              :readonly="true"
              :placeholder="placeholder"
            >
              <template #suffix>
                <el-icon
                  :style="{
                    transform: popoverVisible ? 'rotate(180deg)' : 'rotate(0)',
                    transition: 'transform .5s',
                  }"
                >
                  <ArrowDown />
                </el-icon>
              </template>
            </el-input>
          </slot>
        </div>
      </template>
      <!-- 弹出框内容 -->
      <div ref="popoverContentRef">
        <!-- 表单 -->
        <el-form ref="formRef" :model="queryParams" :inline="true">
          <template v-for="item in selectConfig.formItems" :key="item.prop">
            <el-form-item :label="item.label" :prop="item.prop">
              <!-- Input 输入框 -->
              <template v-if="item.type === 'input'">
                <template v-if="item.attrs?.type === 'number'">
                  <el-input
                    v-model.number="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
                <template v-else>
                  <el-input
                    v-model="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
              </template>
              <!-- Select 选择器 -->
              <template v-else-if="item.type === 'select'">
                <el-select v-model="queryParams[item.prop]" v-bind="item.attrs">
                  <template v-for="option in item.options" :key="option.value">
                    <el-option :label="option.label" :value="option.value" />
                  </template>
                </el-select>
              </template>
              <!-- TreeSelect 树形选择 -->
              <template v-else-if="item.type === 'tree-select'">
                <el-tree-select v-model="queryParams[item.prop]" v-bind="item.attrs" />
              </template>
              <!-- DatePicker 日期选择器 -->
              <template v-else-if="item.type === 'date-picker'">
                <el-date-picker v-model="queryParams[item.prop]" v-bind="item.attrs" />
              </template>
              <!-- Input 输入框 -->
              <template v-else>
                <template v-if="item.attrs?.type === 'number'">
                  <el-input
                    v-model.number="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
                <template v-else>
                  <el-input
                    v-model="queryParams[item.prop]"
                    v-bind="item.attrs"
                    @keyup.enter="handleQuery"
                  />
                </template>
              </template>
            </el-form-item>
          </template>
          <el-form-item>
            <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
            <el-button icon="refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 列表 -->
        <el-table
          ref="tableRef"
          v-loading="loading"
          :data="pageData"
          :border="true"
          :max-height="250"
          :row-key="pk"
          :highlight-current-row="true"
          :class="{ radio: !isMultiple }"
          @select="handleSelect"
          @select-all="handleSelectAll"
        >
          <template v-for="col in selectConfig.tableColumns" :key="col.prop">
            <!-- 自定义 -->
            <template v-if="col.templet === 'custom'">
              <el-table-column v-bind="col">
                <template #default="scope">
                  <slot :name="col.slotName ?? col.prop" :prop="col.prop" v-bind="scope" />
                </template>
              </el-table-column>
            </template>
            <!-- 其他 -->
            <template v-else>
              <el-table-column v-bind="col" />
            </template>
          </template>
        </el-table>
        <!-- 分页 -->
        <pagination
          v-if="total > 0"
          v-model:total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="handlePagination"
        />
        <div class="feedback">
          <el-button type="primary" size="small" @click="handleConfirm">
            {{ confirmText }}
          </el-button>
          <el-button size="small" @click="handleClear">清 空</el-button>
          <el-button size="small" @click="handleClose">关 闭</el-button>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script setup>
import { useResizeObserver } from "@vueuse/core";

// 定义接收的属性
const props = defineProps({
  selectConfig: {
    type: Object,
    required: false,
    default: () => ({
      width: "100%",
      placeholder: "请选择",
      popover: {},
      indexAction: () => Promise.resolve(),
      pk: "id",
      multiple: false,
      formItems: [],
      tableColumns: [],
    }),
  },
  text: {
    type: String,
    default: "",
  },
});

// 自定义事件
const emit = defineEmits(["confirmClick"]);

// 主键
const pk = props.selectConfig.pk ?? "id";
// 是否多选
const isMultiple = props.selectConfig.multiple === true;
// 宽度
const width = props.selectConfig.width ?? "100%";
// 占位符
const placeholder = props.selectConfig.placeholder ?? "请选择";
// 是否显示弹出框
const popoverVisible = ref(false);
// 加载状态
const loading = ref(false);
// 数据总数
const total = ref(0);
// 列表数据
const pageData = ref([]);
// 每页条数
const pageSize = 10;
// 搜索参数
const queryParams = reactive({
  pageNum: 1,
  pageSize,
});

// 计算popover的宽度
const tableSelectRef = ref();
const popoverWidth = ref(width);
useResizeObserver(tableSelectRef, (entries) => {
  popoverWidth.value = `${entries[0].contentRect.width}px`;
});

// 表单操作
const formRef = ref();
// 初始化搜索条件
for (const item of props.selectConfig.formItems) {
  queryParams[item.prop] = item.initialValue ?? "";
}
// 重置操作
function handleReset() {
  formRef.value?.resetFields();
  fetchPageData(true);
}
// 查询操作
function handleQuery() {
  fetchPageData(true);
}
// 获取列表数据
async function fetchPageData(resetPage = false) {
  if (resetPage) {
    queryParams.pageNum = 1;
  }
  loading.value = true;
  try {
    const data = await props.selectConfig.indexAction(queryParams);
    pageData.value = data?.list ?? [];
    total.value = data?.total ?? 0;
  } finally {
    loading.value = false;
  }
}
// 表格操作
const tableRef = ref();
// 选中的行（用于存储选中状态）
const SELECTION = ref([]);
// 选择行
function handleSelect(_selection, row) {
  if (!isMultiple) {
    tableRef.value.clearSelection();
    tableRef.value.toggleRowSelection(row, true);
  }
}
// 全选
function handleSelectAll(_selection) {
  if (!isMultiple) {
    tableRef.value.clearSelection();
  }
}
// 确认按钮文本
const confirmText = computed(() => {
  return isMultiple ? "确 定" : "选 择";
});
// 确认选择
function handleConfirm() {
  const selectedRows = tableRef.value.getSelectionRows();
  emit("confirmClick", selectedRows);
  handleClose();
}
// 清空选择
function handleClear() {
  tableRef.value.clearSelection();
  emit("confirmClick", []);
}
// 关闭弹出框
function handleClose() {
  popoverVisible.value = false;
}
// 显示弹出框
function handleShow() {
  fetchPageData();
}
// 分页操作
function handlePagination() {
  fetchPageData();
}
</script>

<style scoped lang="scss">
.reference {
  cursor: pointer;
}

.feedback {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}

:deep(.radio) {
  .el-table__header-wrapper {
    .el-checkbox {
      display: none;
    }
  }
}
</style>
