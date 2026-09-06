<script setup lang="ts">
import { ref, watch } from 'vue'
import BaseForm from '@/BaseComponent/BaseForm'
import businessStore from '@/store/business/businessStore'
import to from '@/utils/to'
import { getElementTotalSize, getDialogMaxHeight } from '@/utils/hsj/utils'
import type { DialogProps } from './types/types'
const {
  // 用于form反显
  infoInit = {},
  // 编辑和新建需要额外传入的参数，优先级低于用户输入的formData
  otherInfo = {},
  // 页面名称与pageContent和PageSearch的一致，每个页面必须唯一
  pageName,
  // dialog的宽度
  width = '600px',
  // dialog距离顶部的高度
  top = '7vh',
  // el-dialog的配置
  dialogConfig,
  // 用于新建时的默认值
  defaultData = {},
  // 发送修改的请求时id需要从哪个字段取值
  // 示例 row: { name: 'lmw', userId: 1 } idKey: 'userId'; 不传时默认会读取 pageName+'Id'
  idKey,
  // 发送请求时我需要给后端的id叫什么
  // 示例 sendData: { name: 'yahaha', adminId: 1 } sendIdKey: 'adminId'; 不传时默认会读取 pageName+'Id'
  // 本人遇到读id的键和发送请求时给后端的id不同的情况所以才设置成两个参数
  sendIdKey,
  // 是否为多选编辑
  isEditMore,
  // form的最大高度
  maxHeight,
  // 保存成功后重新搜索列表的函数
  search,
  // 新增和删除时的接口，规则 requestBaseUrl+interceptor(pageName)
  // 大部分情况下都可以适配，如果某个界面特殊可以多加一个参数，editUrl，不过建议和后端协商让后端改。
  requestBaseUrl = '/',
  beforeSaveFun,
} = defineProps<DialogProps>()
const emits = defineEmits(['closed', 'editNext', 'beforeSave'])
const slots = defineSlots()
const dialogVisible = ref(false)
const formData = ref<any>({})
const title = ref('')
const formRef = useTemplateRef('formRef')
const store = businessStore()
const loading = ref(false)
const tableSelected = ref<any[]>([])
const dialogClass = computed(() => {
  const totalPageName = pageName.replaceAll('/', '\\/')
  return `${totalPageName}Dialog`
})
// 判断是编辑还是新建
const isEdit = computed(() => {
  return Object.keys(infoInit).length !== 0
})
// 初始化form表单数据
watch(
  () => infoInit,
  (newValue) => {
    if (isEdit.value) {
      for (const item of dialogConfig?.formItems ?? []) {
        formData.value[`${item.field}`] = newValue[`${item.field}`]
      }
    } else {
      for (const item of dialogConfig?.formItems ?? []) {
        formData.value[`${item.field}`] = defaultData[`${item.field}`]
      }
    }
  }
)
const send = () => {
  return new Promise(async (resolve) => {
    emits('beforeSave')
    beforeSaveFun && (await beforeSaveFun())
    if (isEdit.value) {
      //编辑
      nextTick(() => {
        const promise = store.editDataAction({
          pageName: pageName,
          editInfo: {
            ...otherInfo,
            ...formData.value,
          },
          id:
            infoInit[`${idKey}`] ?? infoInit[pageName + 'Id'] ?? infoInit['id'],
          sendIdKey: sendIdKey,
          requestBaseUrl: requestBaseUrl,
        })
        resolve(promise)
      })
    } else {
      //新建
      nextTick(() => {
        const promise = store.createDataAction({
          pageName: pageName,
          newData: {
            ...otherInfo,
            ...formData.value,
          },
          requestBaseUrl,
        })
        resolve(promise)
      })
    }
  })
}
// 保存按钮
const commitClick = async () => {
  // 表单校验
  const validate = await formRef.value?.getFormValidate()
  if (validate) {
    loading.value = true
    const [res] = await to(send())
    if (res) {
      search && search()
      // 判断是否为多选后的编辑
      if (isEditMore && tableSelected.value.length > 0) {
        const current = tableSelected.value.shift()
        emits('editNext', current)
      } else {
        dialogVisible.value = false
      }
    }
    loading.value = false
  }
}
// dialog的底部的padding,主要用于表单最右边和footer的最右边保持对齐
const footerPaddingRight = ref('0px')
const getFooterPaddingRight = () => {
  const dialogEl = document.querySelector(`.${dialogClass.value}`)
  if (!dialogEl) {
    return
  }
  const formItem = dialogEl.querySelector('.el-form-item')
  const dialogBody = dialogEl.querySelector('.el-dialog__body')
  const { paddingRight: baseFromPaddingRigth } = getElementTotalSize(
    formRef.value?.$el!
  )
  const { paddingRight: formItemPaddingRight } = getElementTotalSize(formItem!)
  const { paddingRight: dialogBodyPaddingRight } = getElementTotalSize(
    dialogBody!
  )
  footerPaddingRight.value =
    baseFromPaddingRigth + formItemPaddingRight + dialogBodyPaddingRight + 'px'
}
const dialogClosed = () => {
  emits('closed')
}
// 对外暴露的方法，用于修改formData的值
const setFormData = (key: string, value: any) => {
  formData.value[key] = value
}

const changeSelected = (newValue: any[]) => {
  tableSelected.value = newValue
}
// 判断使用系统的设备是否为小屏
const isSmall = window.isSmallScreen
// 表单的最大高度
const dialogMaxHeight = ref<StrNum>()
// 计算表单的最大高度
const getMaxHeight = () => {
  // 默认取传入的maxHeight
  if (maxHeight) {
    dialogMaxHeight.value = maxHeight
  } else {
    dialogMaxHeight.value = getDialogMaxHeight(`.${dialogClass.value}`)
  }
}
const handleOpened = () => {
  getMaxHeight()
  getFooterPaddingRight()
}
defineExpose({
  dialogVisible,
  title,
  formData,
  setFormData,
  changeSelected,
  formRef,
})
</script>
<template>
  <div class="page-dialog">
    <el-dialog
      class="pageDialog"
      :class="dialogClass"
      v-model="dialogVisible"
      :title="title"
      :top="isSmall ? '0vh' : top"
      :width="getWidth(width)"
      :close-on-click-modal="false"
      draggable
      destroy-on-close
      :fullscreen="isSmall"
      @closed="dialogClosed"
      @open="handleOpened"
    >
      <el-scrollbar
        class="ba-table-form-scrollbar"
        :max-height="dialogMaxHeight"
      >
        <BaseForm
          class="baseForm"
          :data="formData"
          v-bind="dialogConfig"
          ref="formRef"
        >
          <template
            v-for="(_value, slotName) in slots"
            #[slotName]="{ backData }"
          >
            <slot :name="slotName" :backData="backData"> </slot>
          </template>
        </BaseForm>
        <slot></slot>
      </el-scrollbar>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" :loading="loading">
            取消
          </el-button>

          <el-button type="primary" @click="commitClick" :loading="loading">
            <span v-if="tableSelected.length > 0 && isEditMore">
              保存并编辑下一项
            </span>
            <span v-else>保存</span>
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.page-dialog {
  :deep(.el-dialog__header) {
    text-align: left;
    padding-bottom: 16px;
    border-bottom: 1px solid var(--ba-bg-color);
  }
  :deep(.el-dialog__footer) {
    text-align: right;
    padding-right: v-bind(footerPaddingRight) !important;
  }
  :deep(.el-form-item) {
    margin: 0px 0 18px 0px;
  }
  .baseForm {
    padding: 0 15px;
  }
  :deep(.pageDialog) {
    .el-radio-group {
      width: 100%;
      .el-radio {
        margin-right: 16px;
        &:last-child {
          margin-right: 0;
        }
      }
    }
  }
}
@media screen and (max-width: 768px) {
  :deep(.pageDialog) {
    .baseForm {
      padding: 0 6px;
    }
    .el-radio-group {
      .el-radio {
        margin-right: 12px;
        &:last-child {
          margin-right: 0;
        }
      }
      .el-radio.is-bordered {
        padding: 0px 8px;
      }
    }
  }
}
</style>
