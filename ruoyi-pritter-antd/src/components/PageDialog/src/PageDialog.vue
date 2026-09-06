<script setup>
import BaseForm from '@/BaseComponent/BaseForm'
import businessStore from '@/store/business/businessStore'
import to from '@/utils/to'
import {
  getElementTotalSize,
  getDialogMaxHeight,
  antiShake,
} from '@/utils/utils'

const props = defineProps({
  infoInit: {
    type: Object,
    default: () => ({}),
  },
  otherInfo: {
    type: Object,
    default: () => ({}),
  },
  pageName: {
    type: String,
    required: true,
  },
  width: {
    type: [String, Number],
    default: '600px',
  },
  top: {
    type: String,
    default: '7vh',
  },
  dialogConfig: {
    type: Object,
    required: true,
  },
  defaultData: {
    type: Object,
    default: () => ({}),
  },
  idKey: {
    type: String,
    default: '',
  },
  sendIdKey: {
    type: String,
    default: '',
  },
  maxHeight: {
    type: [String, Number],
    default: '',
  },
  search: {
    type: Function,
    default: null,
  },
  requestBaseUrl: {
    type: String,
    default: '/',
  },
  beforeSaveFun: {
    type: Function,
    default: null,
  },
})

const emit = defineEmits(['closed', 'beforeSave'])
const slots = useSlots()
const dialogVisible = ref(false)
const formData = ref({})
const title = ref('')
const formRef = useTemplateRef('formRef')
const store = businessStore()
const loading = ref(false)

const dialogClass = computed(() => {
  const totalPageName = props.pageName.replaceAll('/', '\\/')
  return `${totalPageName}Dialog`
})

const isEdit = computed(() => {
  return Object.keys(props.infoInit).length !== 0
})

watch(
  () => props.infoInit,
  () => {
    if (isEdit.value) {
      for (const item of props.dialogConfig?.formItems ?? []) {
        formData.value[item.field] = props.infoInit[item.field]
      }
    } else {
      for (const item of props.dialogConfig?.formItems ?? []) {
        formData.value[item.field] = props.defaultData[item.field]
      }
    }
  }
)

const send = () => {
  return new Promise(async (resolve) => {
    emit('beforeSave')
    if (props.beforeSaveFun) {
      await props.beforeSaveFun()
    }
    if (isEdit.value) {
      nextTick(() => {
        resolve(
          store.editDataAction({
            pageName: props.pageName,
            editInfo: {
              ...props.otherInfo,
              ...formData.value,
            },
            id:
              props.infoInit[props.idKey] ??
              props.infoInit[props.pageName + 'Id'] ??
              props.infoInit.id,
            sendIdKey: props.sendIdKey,
            requestBaseUrl: props.requestBaseUrl,
          })
        )
      })
    } else {
      nextTick(() => {
        resolve(
          store.createDataAction({
            pageName: props.pageName,
            newData: {
              ...props.otherInfo,
              ...formData.value,
            },
            requestBaseUrl: props.requestBaseUrl,
          })
        )
      })
    }
  })
}

const commitClick = async () => {
  try {
    await formRef.value?.getFormValidate()
  } catch {
    return
  }
  loading.value = true
  const [res] = await to(send())
  if (res) {
    props.search && props.search()
    dialogVisible.value = false
  }
  loading.value = false
}

const footerPaddingRight = ref('0px')

const getFooterPaddingRight = () => {
  const dialogEl = document.querySelector(`.${dialogClass.value}`)
  if (!dialogEl) {
    return
  }
  const formItem = dialogEl.querySelector('.ant-form-item')
  const dialogBody = dialogEl.querySelector('.ant-modal-body')
  const { paddingRight: baseFromPaddingRight } = getElementTotalSize(
    formRef.value?.$el
  )
  const { paddingRight: formItemPaddingRight } = getElementTotalSize(formItem)
  const { paddingRight: dialogBodyPaddingRight } =
    getElementTotalSize(dialogBody)
  footerPaddingRight.value =
    baseFromPaddingRight + formItemPaddingRight + dialogBodyPaddingRight + 'px'
}

const setFormData = (key, value) => {
  formData.value[key] = value
}

const isSmall = window.isSmallScreen
const dialogMaxHeight = ref()

const getMaxHeight = () => {
  if (props.maxHeight) {
    dialogMaxHeight.value = props.maxHeight
  } else {
    dialogMaxHeight.value = getDialogMaxHeight(`.${dialogClass.value}`)
  }
}

const getMaxHeightAntiShake = antiShake(getMaxHeight, 100)

const handleOpened = () => {
  nextTick(() => {
    getMaxHeight()
    getFooterPaddingRight()
  })
  window.addEventListener('resize', getMaxHeightAntiShake)
}

const dialogClosed = () => {
  emit('closed')
  window.removeEventListener('resize', getMaxHeightAntiShake)
}

const modalWrapClass = computed(() => {
  if (isSmall) {
    console.log('full-modal')
    return 'full-modal'
  }
  return ['pageDialog', dialogClass.value].filter(Boolean).join(' ')
})
const openModal = () => {
  dialogVisible.value = true
  handleOpened()
}
const setTitle = (value) => {
  title.value = value
}
defineExpose({
  dialogVisible,
  openModal,
  setTitle,
  formData,
  setFormData,
  formRef,
})
</script>

<template>
  <div class="page-dialog">
    <a-modal
      v-model:open="dialogVisible"
      class="pageDialog"
      :wrap-class-name="modalWrapClass"
      :title="title"
      :width="getWidth(width)"
      :centered="false"
      :style="isSmall ? { top: '0vh' } : { top }"
      :mask-closable="false"
      :destroy-on-close="true"
      @afterClose="dialogClosed"
    >
      <div
        class="ba-table-form-scrollbar"
        :style="{
          maxHeight: dialogMaxHeight ? `${dialogMaxHeight}px` : undefined,
          overflowY: 'auto',
        }"
      >
        <BaseForm
          ref="formRef"
          class="baseForm"
          :data="formData"
          v-bind="dialogConfig"
        >
          <template
            v-for="(_value, slotName) in slots"
            #[slotName]="{ backData }"
          >
            <slot :name="slotName" :back-data="backData" />
          </template>
        </BaseForm>
        <slot />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <a-button :loading="loading" @click="dialogVisible = false">
            取消
          </a-button>
          <a-button type="primary" :loading="loading" @click="commitClick">
            保存
          </a-button>
        </div>
      </template>
    </a-modal>
  </div>
</template>

<style scoped lang="scss"></style>
