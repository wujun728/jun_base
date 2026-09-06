<!--
 * 表单构建器 - 预览渲染项
 * 递归渲染所有组件类型的预览效果。
 * 支持基础组件、布局组件、按钮组件。
 -->
<template>
  <template v-if="node.type === 'row'">
    <el-row :gutter="(node.props.gutter as number) || 0">
      <template v-for="col in node.children" :key="col.id">
        <el-col :span="(col.props.span as number) || 24">
          <PreviewItem
            v-for="child in col.children"
            :key="child.id"
            :node="child"
            :form-model="formModel" />
        </el-col>
      </template>
    </el-row>
  </template>

  <template v-else-if="node.type === 'col'">
    <el-col :span="(node.props.span as number) || 24">
      <PreviewItem
        v-for="child in node.children || []"
        :key="child.id"
        :node="child"
        :form-model="formModel" />
    </el-col>
  </template>

  <template v-else-if="node.type === 'button'">
    <el-button
      :type="(node.props.type as any) || 'primary'"
      :size="node.props.size as any"
      :icon="node.props.icon || undefined"
      :loading="!!node.props.loading"
      :plain="!!node.props.plain"
      :round="!!node.props.round"
      :circle="!!node.props.circle"
      :disabled="!!node.props.disabled">
      {{ (node.props.text as string) || '按钮' }}
    </el-button>
  </template>

  <template v-else>
    <el-form-item
      :label="node.label"
      :prop="node.field || node.id"
      :required="!!node.props.required">
      <el-input
        v-if="node.type === 'input'"
        v-model="formModel[node.field || node.id]"
        :placeholder="node.props.placeholder as string"
        :disabled="!!node.props.disabled"
        :clearable="!!node.props.clearable"
        :maxlength="node.props.maxlength as number"
        :show-word-limit="!!node.props.showWordLimit"
        :prefix-icon="node.props.prefix as string"
        :suffix-icon="node.props.suffix as string" />
      <el-input
        v-else-if="node.type === 'textarea'"
        v-model="formModel[node.field || node.id]"
        type="textarea"
        :rows="(node.props.rows as number) || 3"
        :placeholder="node.props.placeholder as string"
        :maxlength="node.props.maxlength as number"
        :show-word-limit="!!node.props.showWordLimit" />
      <el-input-number
        v-else-if="node.type === 'input-number'"
        v-model="formModel[node.field || node.id]"
        :min="node.props.min as number"
        :max="node.props.max as number"
        :step="(node.props.step as number) || 1"
        :placeholder="node.props.placeholder as string"
        :precision="node.props.precision as number"
        :controls="node.props.controls === false ? false : true" />
      <el-input
        v-else-if="node.type === 'password'"
        v-model="formModel[node.field || node.id]"
        type="password"
        :show-password="node.props.showPassword !== false"
        :placeholder="node.props.placeholder as string" />
      <el-select
        v-else-if="node.type === 'select' || node.type === 'multi-select'"
        v-model="formModel[node.field || node.id]"
        :multiple="node.type === 'multi-select'"
        :filterable="!!node.props.filterable"
        :clearable="!!node.props.clearable"
        :placeholder="node.props.placeholder as string"
        :collapse-tags="!!node.props.collapseTags"
        :allow-create="!!node.props.allowCreate"
        style="width: 100%">
        <el-option
          v-for="opt in (node.props.options as any[]) || []"
          :key="opt.value"
          :label="opt.label"
          :value="opt.value" />
      </el-select>
      <el-radio-group
        v-else-if="node.type === 'radio'"
        v-model="formModel[node.field || node.id]">
        <el-radio
          v-for="opt in (node.props.options as any[]) || []"
          :key="opt.value"
          :value="opt.value">
          {{ opt.label }}
        </el-radio>
      </el-radio-group>
      <el-checkbox-group
        v-else-if="node.type === 'checkbox'"
        v-model="formModel[node.field || node.id]">
        <el-checkbox
          v-for="opt in (node.props.options as any[]) || []"
          :key="opt.value"
          :label="opt.value">
          {{ opt.label }}
        </el-checkbox>
      </el-checkbox-group>
      <el-switch
        v-else-if="node.type === 'switch'"
        v-model="formModel[node.field || node.id]"
        :active-text="node.props.activeText as string"
        :inactive-text="node.props.inactiveText as string"
        :active-value="node.props.activeValue as any"
        :inactive-value="node.props.inactiveValue as any" />
      <el-date-picker
        v-else-if="node.type === 'date'"
        v-model="formModel[node.field || node.id]"
        type="date"
        :placeholder="node.props.placeholder as string"
        :format="node.props.format as string"
        :value-format="node.props.valueFormat as string" />
      <el-date-picker
        v-else-if="node.type === 'daterange'"
        v-model="formModel[node.field || node.id]"
        type="daterange"
        :start-placeholder="
          (node.props.startPlaceholder as string) || '开始日期'
        "
        :end-placeholder="(node.props.endPlaceholder as string) || '结束日期'"
        :range-separator="(node.props.rangeSeparator as string) || '至'"
        :format="node.props.format as string"
        :value-format="node.props.valueFormat as string" />
      <el-time-picker
        v-else-if="node.type === 'time'"
        v-model="formModel[node.field || node.id]"
        :placeholder="node.props.placeholder as string"
        :format="node.props.format as string"
        :value-format="node.props.valueFormat as string" />
      <el-date-picker
        v-else-if="node.type === 'datetime'"
        v-model="formModel[node.field || node.id]"
        type="datetime"
        :placeholder="node.props.placeholder as string"
        :format="node.props.format as string"
        :value-format="node.props.valueFormat as string" />
      <el-cascader
        v-else-if="node.type === 'cascader'"
        v-model="formModel[node.field || node.id]"
        :options="(node.props.options as any[]) || []"
        :placeholder="node.props.placeholder as string"
        :clearable="!!node.props.clearable"
        style="width: 100%" />
      <el-tree-select
        v-else-if="node.type === 'tree-select'"
        v-model="formModel[node.field || node.id]"
        :data="(node.props.data as any[]) || []"
        :placeholder="node.props.placeholder as string"
        :clearable="!!node.props.clearable"
        style="width: 100%" />
      <el-slider
        v-else-if="node.type === 'slider'"
        v-model="formModel[node.field || node.id]"
        :min="(node.props.min as number) || 0"
        :max="(node.props.max as number) || 100"
        :step="(node.props.step as number) || 1"
        :show-input="!!node.props.showInput"
        :show-stops="!!node.props.showStops"
        :range="!!node.props.range" />
      <el-rate
        v-else-if="node.type === 'rate'"
        v-model="formModel[node.field || node.id]"
        :max="(node.props.max as number) || 5"
        :allow-half="!!node.props.allowHalf"
        :show-text="!!node.props.showText"
        :show-score="!!node.props.showScore" />
      <el-color-picker
        v-else-if="node.type === 'color-picker'"
        v-model="formModel[node.field || node.id]"
        :show-alpha="!!node.props.showAlpha"
        :color-format="node.props.colorFormat as any" />
      <el-upload
        v-else-if="node.type === 'upload'"
        v-model="formModel[node.field || node.id]"
        :action="(node.props.action as string) || '#'"
        :multiple="!!node.props.multiple"
        :limit="node.props.limit as number"
        :accept="node.props.accept as string"
        :list-type="node.props.listType as any"
        :auto-upload="node.props.autoUpload !== false">
        <el-button type="primary">点击上传</el-button>
      </el-upload>
    </el-form-item>
  </template>
</template>

<script setup lang="ts">
import type { FormNode } from '../types/form'

defineProps<{
  node: FormNode
  formModel: Record<string, any>
}>()
</script>
