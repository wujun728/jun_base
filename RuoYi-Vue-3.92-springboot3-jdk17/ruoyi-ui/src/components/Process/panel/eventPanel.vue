<template>
  <div>
    <el-form label-width="82px" size="small" :rules="rules">
      <el-form-item label="定时类型" prop="timeType">
        <el-select v-model="bpmnFormData.timeType">
          <el-option label="开始时间" value="timeDate" />
          <el-option label="持续时间" value="timeDuration" />
          <el-option label="循环周期" value="timeCycle" />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" v-if="bpmnFormData.timeType === 'timeDate'" key="timeDate">
        <el-input v-model="bpmnFormData.timeDate" @change="updateElementEvent('timeDate')" />
      </el-form-item>
      <el-form-item label="持续时间" v-if="bpmnFormData.timeType === 'timeDuration'" key="timeDuration">
        <el-input v-model="bpmnFormData.timeDuration" @change="updateElementEvent('timeDuration')" />
      </el-form-item>
      <el-form-item label="循环周期" v-if="bpmnFormData.timeType === 'timeCycle'" key="timeCycle">
        <el-input v-model="bpmnFormData.timeCycle" @change="updateElementEvent('timeCycle')" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { StrUtil } from "@/utils/StrUtil";

export default {
  name: "eventPanel",
  components: {},
  /** 组件传值  */
  props: {
    id: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      bpmnFormData: {
        timeDate: "",
        timeDuration: "",
        timeCycle: "",
      },
      rules: {
        timeDate: [{ required: true, message: "开始时间不能为空", trigger: "blur" }],
        timeDuration: [{ required: true, message: "持续时间不能为空", trigger: "blur" }],
        timeCycle: [{ required: true, message: "循环周期不能为空", trigger: "blur" }],
      },
    };
  },

  /** 传值监听 */
  watch: {
    id: {
      handler(newVal) {
        if (StrUtil.isNotBlank(newVal)) {
          this.resetEventForm();
        }
      },
      immediate: true, // 立即生效
    },
  },
  created() {},
  methods: {
    // 初始化表单
    resetEventForm() {
      // 初始化设为空值
      this.bpmnFormData = {
        timeDate: "",
        timeDuration: "",
        timeCycle: "",
      };
      // 流程节点信息上取值
      for (let key in this.bpmnFormData) {
        const value = this.modelerStore.element.businessObject?.eventDefinitions
          ? this.modelerStore.element.businessObject?.eventDefinitions[0][key]
          : this.bpmnFormData[key];
        if (value) {
          this.bpmnFormData.timeType = key;
          this.$set(this.bpmnFormData, key, value.body);
        }
      }
    },

    // 更新节点信息
    updateElementEvent(key) {
      this.updateTimerEventDefinition(key, this.bpmnFormData[key]);
    },

    // 删除节点
    deleteFlowAttar() {
      delete this.modelerStore.element.businessObject?.eventDefinitions[0][`timeDate`];
      delete this.modelerStore.element.businessObject?.eventDefinitions[0][`timeDuration`];
      delete this.modelerStore.element.businessObject?.eventDefinitions[0][`timeCycle`];
    },

    // 更新定时器信息
    updateTimerEventDefinition(key, value) {
      const timerEventDefinition = this.modelerStore.moddle.create("bpmn:TimerEventDefinition");

      const formalExpression = this.modelerStore.moddle.create("bpmn:FormalExpression", {
        body: value,
      });
      timerEventDefinition.set(key, formalExpression);

      this.modelerStore.modeling.updateProperties(this.modelerStore.element, {
        eventDefinitions: [timerEventDefinition],
      });
    },
  },
};
</script>

<style scoped>
::v-deep .el-form-item__label {
  font-weight: normal;
}
</style>