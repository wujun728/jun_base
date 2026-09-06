<script setup>
import { deletData } from '@/api/business/main/index'
import { interceptor } from '@/store/business/businessStore'
import to from '@/utils/to'

const props = defineProps({
  tabelSelection: {
    type: Array,
    default: () => [],
  },
  keyDescribe: {
    type: String,
  },
  idKey: {
    type: String,
  },
  pageName: {
    type: String,
  },
  name: {
    type: String,
  },
})
const delLoading = ref(false)
const delDialog = ref(false)
const openDialog = () => {
  delDialog.value = true
}
const closeDialog = () => {
  delDialog.value = false
}
const confirm = async () => {
  delLoading.value = true
  const ids = props.tabelSelection.map((item) => {
    return item[props.idKey]
  })
  const url = interceptor(props.pageName)
  const [res] = to(await deletData(`${url}/${ids.toString()}`))
  delLoading.value = false
  if (res) {
    emit('delSuccess')
    closeDialog()
  }
}
const emit = defineEmits(['delSuccess'])
defineExpose({
  openDialog,
  closeDialog,
})
</script>

<template>
  <div class="delDialog">
    <el-dialog v-model="delDialog" title="批量删除" :width="getWidth('400px')">
      <div class="content">
        <span>是否批量删除{{ name }}为</span>
        <span v-for="item in tabelSelection" :key="item[idKey]" class="red">
          &nbsp;{{ item[keyDescribe] }}&nbsp;
        </span>
        <span>的信息</span>
      </div>
      <template #footer>
        <div>
          <el-button @click="delDialog = false" :loading="delLoading">
            取消
          </el-button>
          <el-button @click="confirm" :loading="delLoading" type="danger"
            >确认</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.content {
  font-size: 16px;
  .red {
    color: red;
    font-weight: 700;
  }
}
</style>
