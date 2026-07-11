<template>
  <div>
    <template slot="header">
      <div class="card-header">
        <span>{{ translateNodeName(elementType) }}</span>
      </div>
    </template>
    <el-collapse v-model="activeName" accordion>
      <!--   常规信息     -->
      <el-collapse-item name="common">
        <template slot="title">
          <i class="el-icon-info mr5"></i>
          <span style="font-weight: 600;">基本信息</span>
          <span class="require ml5">*</span>
        </template>
        <common-panel :id="elementId" />
      </el-collapse-item>

      <!--   环节信息     -->
      <el-collapse-item name="Task" v-if="elementType.indexOf('Task') !== -1">
        <template slot="title">
          <i class="el-icon-user-solid mr5"></i>
          <span style="font-weight: 600;">流程参与者</span>
          <span class="require ml5">*</span>
        </template>
        <user-task-panel :id="elementId" @flowUserConfirm="flowUserConfirm" />
      </el-collapse-item>

      <!--   定时器事件信息     -->
      <el-collapse-item name="timerEvent" v-if="['BoundaryEvent','StartEvent','IntermediateCatchEvent'].includes(elementType) ">
        <template slot="title">
          <i class="el-icon-user-solid mr5"></i>
          <span style="font-weight: 600;">定时器</span>
        </template>
        <event-panel :id="elementId" />
      </el-collapse-item>

      <!--   多实例     -->
      <el-collapse-item name="multiInstance" v-if="elementType.indexOf('Task') !== -1 && multipleConfig">
        <template slot="title">
          <i class="el-icon-s-grid mr5"></i>
          <span style="font-weight: 600;">多实例</span>
          <el-tooltip content="多人同时审批，完成条件时，才会触发生成下一审批环节" placement="top">
            <i class="el-icon-question ml5"></i>
          </el-tooltip>
        </template>
        <multi-instance :id="elementId" />
      </el-collapse-item>

      <!--   执行监听器     -->
      <el-collapse-item name="executionListener">
        <template slot="title">
          <i class="el-icon-s-promotion mr5"></i>
          <span style="font-weight: 600;">执行监听器</span>
          <el-badge :value="executionListenerCount" class="item" type="primary" />
        </template>
        <execution-listener :id="elementId" @getExecutionListenerCount="getExecutionListenerCount" />
      </el-collapse-item>

      <!--   任务监听器     -->
      <el-collapse-item name="taskListener" v-if="elementType === 'UserTask'">
        <template slot="title">
          <i class="el-icon-s-flag mr5"></i>
          <span style="font-weight: 600;">任务监听器</span>
          <el-badge :value="taskListenerCount" class="item" type="primary" />
        </template>
        <task-listener :id="elementId" @getTaskListenerCount="getTaskListenerCount" />
      </el-collapse-item>

      <!--   流转条件     -->
      <el-collapse-item name="condition" v-if="conditionVisible">
        <template slot="title">
          <i class="el-icon-share mr5"></i>
          <span style="font-weight: 600;">流转条件</span>
        </template>
        <condition-panel :id="elementId" />
      </el-collapse-item>

      <!--   扩展属性     -->
      <el-collapse-item name="properties">
        <template slot="title">
          <i class="el-icon-circle-plus mr5"></i>
          <span style="font-weight: 600;">扩展属性</span>
        </template>
        <properties-panel :id="elementId" />
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
import ExecutionListener from "./panel/executionListener";
import TaskListener from "./panel/taskListener";
import MultiInstance from "./panel/multiInstance";
import CommonPanel from "./panel/commonPanel";
import UserTaskPanel from "./panel/taskPanel";
import ConditionPanel from "./panel/conditionPanel";
import OtherPanel from "./panel/otherPanel";
import PropertiesPanel from "./panel/PropertiesPanel";
import EventPanel from "./panel/eventPanel";

import { translateNodeName } from "./common/bpmnUtils";
import FlowUser from "@/components/flow/User/index.vue";
import FlowRole from "@/components/flow/Role/index.vue";
import FlowExp from "@/components/flow/Expression/index.vue";
export default {
  name: "Designer",
  components: {
    ExecutionListener,
    TaskListener,
    MultiInstance,
    CommonPanel,
    UserTaskPanel,
    ConditionPanel,
    OtherPanel,
    PropertiesPanel,
    FlowUser,
    FlowRole,
    FlowExp,
    EventPanel,
  },
  data() {
    return {
      activeName: "common",
      executionListenerCount: 0,
      taskListenerCount: 0,
      elementId: "",
      elementType: "",
      conditionVisible: false, // 流转条件设置
      multipleConfig: false, // 多实例配置
      rules: {
        id: [{ required: true, message: "节点Id 不能为空", trigger: "blur" }],
        name: [{ required: true, message: "节点名称不能为空", trigger: "blur" }],
      },
    };
  },

  /** 传值监听 */
  watch: {
    elementId: {
      handler() {
        this.activeName = "common";
      },
    },
  },
  created() {
    this.initModels();
  },
  methods: {
    // 初始化流程设计器
    initModels() {
      this.getActiveElement();
    },

    // 注册节点事件
    getActiveElement() {
      // 初始第一个选中元素 bpmn:Process
      this.initFormOnChanged(null);
      this.modelerStore.modeler.on("import.done", (e) => {
        this.initFormOnChanged(null);
      });
      // 监听选择事件，修改当前激活的元素以及表单
      this.modelerStore.modeler.on("selection.changed", ({ newSelection }) => {
        this.initFormOnChanged(newSelection[0] || null);
      });
      this.modelerStore.modeler.on("element.changed", ({ element }) => {
        // 保证 修改 "默认流转路径" 类似需要修改多个元素的事件发生的时候，更新表单的元素与原选中元素不一致。
        if (element && element.id === this.elementId) {
          this.initFormOnChanged(element);
        }
      });
    },

    // 初始化数据
    initFormOnChanged(element) {
      let activatedElement = element;
      if (!activatedElement) {
        activatedElement =
          this.modelerStore.elRegistry.find((el) => el.type === "bpmn:Process") ??
          this.modelerStore.elRegistry.find((el) => el.type === "bpmn:Collaboration");
      }
      if (!activatedElement) return;
      this.modelerStore.element = activatedElement;
      this.elementId = activatedElement.id;
      this.elementType = activatedElement.type.split(":")[1] || "";
      this.conditionVisible = !!(
        this.elementType === "SequenceFlow" &&
        activatedElement.source &&
        activatedElement.source.type.indexOf("StartEvent") === -1
      );
    },

    /** 获取执行监听器数量 */
    getExecutionListenerCount(value) {
      this.executionListenerCount = value;
    },
    /** 获取任务监听器数量 */
    getTaskListenerCount(value) {
      this.taskListenerCount = value;
    },
    translateNodeName(val) {
      return translateNodeName(val);
    },
    /** 流程参与者配置确认后调用，控制多实例配置 */
    flowUserConfirm(val) {
      this.multipleConfig = val;
    },
  },
};
</script>

<style lang="scss">
.require {
  font-weight: 600;
  color: red;
}
</style>
