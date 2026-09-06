<!--
 * 表单构建器 - 右侧属性面板
 * 两个 Tab：组件属性（动态编辑选中组件）、表单属性（全局配置）。
 -->
<template>
  <div
    class="flex h-full w-[360px] min-w-[360px] flex-col overflow-hidden border-l border-gray-200 bg-white">
    <el-tabs v-model="activeTab" class="h-full pt-3 pl-4">
      <el-tab-pane label="组件属性" name="props" class="h-full">
        <el-scrollbar height="100%">
          <div class="h-full overflow-y-auto px-4 pb-4">
            <PropertyPanel :node="selectedNode" />
          </div>
        </el-scrollbar>
      </el-tab-pane>
      <el-tab-pane label="表单属性" name="form" class="h-full">
        <el-scrollbar height="100%">
          <div class="h-full overflow-y-auto px-4 pb-4">
            <div class="space-y-3">
              <div
                class="text-xs font-medium tracking-wider text-gray-500 uppercase">
                表单配置
              </div>
              <el-form label-position="top">
                <el-form-item label="表单 Ref 名称">
                  <el-input
                    v-model="formConfig.formRefName"
                    placeholder="formRef" />
                </el-form-item>
                <el-form-item label="表单 Model 名称">
                  <el-input
                    v-model="formConfig.formModelName"
                    placeholder="formModel" />
                </el-form-item>
                <el-form-item label="校验 Rules 名称">
                  <el-input
                    v-model="formConfig.rulesName"
                    placeholder="rules" />
                </el-form-item>
                <el-form-item label="标签宽度">
                  <el-input-number
                    v-model="formConfig.labelWidth"
                    :min="0"
                    :max="200"
                    style="width: 100%" />
                </el-form-item>
                <el-form-item label="标签位置">
                  <el-select
                    v-model="formConfig.labelPosition"
                    style="width: 100%">
                    <el-option label="左侧" value="left" />
                    <el-option label="右侧" value="right" />
                    <el-option label="顶部" value="top" />
                  </el-select>
                </el-form-item>
                <el-form-item label="尺寸">
                  <el-select v-model="formConfig.size" style="width: 100%">
                    <el-option label="大" value="large" />
                    <el-option label="默认" value="default" />
                    <el-option label="小" value="small" />
                  </el-select>
                </el-form-item>
                <el-form-item label="行内模式">
                  <el-switch v-model="formConfig.inline" />
                </el-form-item>
                <el-form-item label="全局禁用">
                  <el-switch v-model="formConfig.disabled" />
                </el-form-item>
                <el-form-item label="隐藏必填标记">
                  <el-switch v-model="formConfig.hideRequiredAsterisk" />
                </el-form-item>
                <el-form-item label="校验状态图标">
                  <el-switch v-model="formConfig.statusIcon" />
                </el-form-item>
                <el-form-item label="显示提交/重置按钮">
                  <el-switch v-model="formConfig.showFormButton" />
                </el-form-item>
              </el-form>
            </div>
          </div>
        </el-scrollbar>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import { useDesigner } from '../composables/useDesigner'
import PropertyPanel from './PropertyPanel.vue'

const activeTab = ref('props')
const { selectedNode, formConfig } = useDesigner()
</script>
