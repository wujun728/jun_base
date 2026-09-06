<template>
  <div>
    <el-form label-width="80px" size="small" @submit.native.prevent>
      <el-form-item label="流程表单">
        <el-select v-model="bpmnFormData.formKey" clearable class="m-2" placeholder="挂载节点表单"
          @change="updateElementFormKey">
          <el-option v-for="item in formList" :key="item.value" :label="item.formName" :value="item.formId" />
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import { listAllForm } from '@modules/flowable/api/form'
import { StrUtil } from "@/utils/StrUtil";
import modelerStore from '@modules/flowable/components/Process/common/global'
export default {
  name: "FormPanel",
  /** 组件传值  */
  props: {
    id: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      formList: [], // 表单数据
      bpmnFormData: {}
    }
  },

  /** 传值监听 */
  watch: {
    id: {
      handler(newVal) {
        if (StrUtil.isNotBlank(newVal)) {
          // 加载表单列表
          this.getListForm();
          this.resetFlowForm();
        }
      },
      immediate: true, // 立即生效
    },
  },
  created() {

  },
  methods: {

    // 方法区
    resetFlowForm() {
      this.bpmnFormData.formKey = modelerStore.element.businessObject.formKey;
    },

    updateElementFormKey(val) {
      if (StrUtil.isBlank(val)) {
        delete modelerStore.element.businessObject[`formKey`]
      } else {
        modelerStore.modeling.updateProperties(modelerStore.element, { 'formKey': val });
      }
    },

    // 获取表单信息
    getListForm() {
      listAllForm().then(res => {
        res.rows.forEach(item => {
          item.formId = item.formId.toString();
        })
        this.formList = res.rows;
      })
    }
  }
}


</script>
