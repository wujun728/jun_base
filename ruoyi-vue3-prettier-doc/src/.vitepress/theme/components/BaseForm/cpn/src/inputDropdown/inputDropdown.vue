<script setup>
import { ref, watch } from 'vue'
const props = defineProps({
  data: {
    required: true,
  },
  options: {
    type: Array,
    default: () => {
      return []
    },
  },
  config: {
    type: Object,
    default: () => {
      return {}
    },
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  inputEventFunction: {
    type: Object,
    default: () => {
      return {}
    },
  },
})

const dropdown = ref(null)

const labelInfo = ref('')
const inputFocus = () => {
  if (dropList.value.length > 0) {
    dropdown.value?.handleOpen()
  }
}
const handleValueChange = (value) => {
  labelInfo.value = value
  emit('update:data', value)
}
const checkInfo = (item) => {
  labelInfo.value = item.label
  emit('update:data', item.value)
}

const dropList = ref([])
watch(
  () => props.options,
  () => {
    dropList.value = props.options
  },
  {
    immediate: true,
  }
)
watch(
  () => props.data,
  () => {
    labelInfo.value = props.data
  },
  {
    immediate: true,
  }
)
let originArr = void 0
const search = (value) => {
  if (!originArr || originArr.length != props.options.length) {
    originArr = props.options
  }
  if (value) {
    const arr = props.options.filter((item) => {
      return item.value.includes(value)
    })
    dropList.value = arr
    if (dropList.value.length === 0) {
      dropdown.value?.handleClose()
    } else {
      inputFocus()
    }
  } else {
    dropList.value = originArr
    inputFocus()
  }
}
const emit = defineEmits(['update:data'])
</script>

<template>
  <div class="dropdownCpn">
    <el-dropdown
      ref="dropdown"
      trigger="contextmenu"
      popper-class="inputDropDown"
      :teleported="true"
      :max-height="300"
    >
      <el-input
        class="input"
        @update:modelValue="handleValueChange($event)"
        :model-value="labelInfo"
        @click="inputFocus"
        :disabled="disabled"
        @input="search"
        v-on="inputEventFunction || {}"
      >
      </el-input>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
            v-for="item in dropList"
            @click="checkInfo(item)"
            :key="item.value"
          >
            {{ item.label }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<style scoped lang="scss">
.dropdownCpn {
  width: 100%;
  position: relative;
  :deep(.el-dropdown) {
    width: 100%;
  }
}
.input {
  width: 100%;
}
</style>
<style lang="scss">
.inputDropDown {
  max-width: 200px;
  .el-popper__arrow {
    width: 100% !important;
  }
  .el-popper__arrow::before {
    left: calc(50% - 5px) !important;
  }
}
</style>
