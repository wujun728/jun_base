<template>
  <div class="icon-box">
    <el-input
      ref="inputRef"
      v-model="valueIcon"
      v-bind="$attrs"
      :placeholder="placeholder"
      :clearable="clearable"
      @clear="clearIcon"
      @click="openDialog"
    >
      <template #append>
        <el-icon v-if="!iconValue" class="el-input__icon"><Search /></el-icon>
        <SvgIcon v-else :name="iconValue"></SvgIcon>
      </template>
    </el-input>
    <el-dialog v-model="dialogVisible" :title="placeholder" top="50px" width="66%">
      <el-input ref="dialogInput" v-model="inputValue" placeholder="搜索图标" size="large" />
      <template #prefix>
        <el-icon class="el-input__icon"><Search /></el-icon>
      </template>
      <el-scrollbar v-if="Object.keys(iconsList).length">
        <div class="icon-list">
          <div v-for="item in iconsList" :key="item" class="icon-item" @click="selectIcon(item)">
            <SvgIcon :name="item"></SvgIcon>
            <span style="font-size: 12px">{{ item }}</span>
          </div>
        </div>
      </el-scrollbar>
      <el-empty v-else description="未搜索到您要找的图标~" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="SelectIcon">
import { ref, computed, nextTick } from "vue";
import SvgIcon from "@/components/SvgIcon/index.vue";
import icons from "./requireIcons";
import { Search } from "@element-plus/icons-vue";

interface SelectIconProps {
  iconValue: string;
  title?: string;
  clearable?: boolean;
  placeholder?: string;
}

const props = withDefaults(defineProps<SelectIconProps>(), {
  iconValue: "",
  title: "请选择图标",
  clearable: true,
  placeholder: "请选择图标"
});

// 重新接收一下，防止打包后 clearable 报错
const valueIcon = ref(props.iconValue);

// open Dialog
const dialogVisible = ref(false);
const dialogInput = ref();
const openDialog = () => {
  dialogVisible.value = true;
  nextTick(() => {
    const timer = setTimeout(() => {
      dialogInput.value.focus();
      clearTimeout(timer);
    });
  });
};

// 选择图标(触发更新父组件数据)
const emit = defineEmits<{
  "update:iconValue": [value: string];
}>();
const selectIcon = (item: any) => {
  dialogVisible.value = false;
  valueIcon.value = item;
  emit("update:iconValue", item);
  setTimeout(() => inputRef.value.blur(), 0);
};

// 清空图标
const inputRef = ref();
const clearIcon = () => {
  valueIcon.value = "";
  emit("update:iconValue", "");
  setTimeout(() => inputRef.value.blur(), 0);
};

// 监听搜索框值
const inputValue = ref("");
const customIcons: { [key: string]: any } = icons;
const iconsList = computed((): { [key: string]: any } => {
  if (!inputValue.value) return icons;
  let result: { [key: string]: any } = {};
  for (const key in customIcons) {
    if (customIcons[key].toLowerCase().indexOf(inputValue.value.toLowerCase()) > -1) result[key] = customIcons[key];
  }
  return result;
});
</script>

<style scoped lang="scss">
@import "./index.scss";
</style>
