<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>{{ translateNodeName(elementType) }}</span>
      </div>
    </template>
    <el-collapse v-model="activeName">
      <!--   常规信息     -->
      <el-collapse-item name="common">
        <template #title><i class="el-icon-info"></i> 常规信息</template>
        <common-panel :id="elementId" />
      </el-collapse-item>

      <!--   任务信息     -->
      <el-collapse-item name="Task" v-if="elementType.indexOf('Task') !== -1">
        <template #title><i class="el-icon-s-claim"></i> 任务配置</template>
        <user-task-panel :id="elementId" />
      </el-collapse-item>

      <!--   表单     -->
      <el-collapse-item name="form" v-if="formVisible">
        <template #title><i class="el-icon-s-order"></i> 表单配置</template>
        <form-panel :id="elementId" />
      </el-collapse-item>

      <!--   执行监听器     -->
      <el-collapse-item name="executionListener">
        <template #title><i class="el-icon-s-promotion"></i> 执行监听器
          <el-badge :value="executionListenerCount" class="item" type="primary" />
        </template>
        <execution-listener :id="elementId" @getExecutionListenerCount="getExecutionListenerCount" />
      </el-collapse-item>

      <!--   任务监听器     -->
      <el-collapse-item name="taskListener" v-if="elementType === 'UserTask'">
        <template #title><i class="el-icon-s-flag"></i> 任务监听器
          <el-badge :value="taskListenerCount" class="item" type="primary" />
        </template>
        <task-listener :id="elementId" @getTaskListenerCount="getTaskListenerCount" />
      </el-collapse-item>

      <!--   多实例     -->
      <el-collapse-item name="multiInstance" v-if="elementType.indexOf('Task') !== -1">
        <template #title><i class="el-icon-s-grid"></i> 多实例</template>
        <multi-instance :id="elementId" />
      </el-collapse-item>
      <!--   流转条件     -->
      <el-collapse-item name="condition" v-if="conditionVisible">
        <template #title><i class="el-icon-share"></i> 流转条件</template>
        <condition-panel :id="elementId" />
      </el-collapse-item>

      <!--   扩展属性     -->
      <el-collapse-item name="properties">
        <template #title><i class="el-icon-circle-plus"></i> 扩展属性</template>
        <properties-panel :id="elementId" />
      </el-collapse-item>

      <!--   其他     -->
      <el-collapse-item name="other">
        <template #title><i class="el-icon-circle-plus"></i> 其他</template>
        <other-panel :id="elementId" />
      </el-collapse-item>

    </el-collapse>
  </el-card>
</template>

<script setup>
import ExecutionListener from './panel/executionListener'
import TaskListener from './panel/taskListener'
import MultiInstance from './panel/multiInstance'
import CommonPanel from './panel/commonPanel'
import UserTaskPanel from './panel/taskPanel'
import ConditionPanel from './panel/conditionPanel'
import FormPanel from './panel/formPanel'
import PropertiesPanel from './panel/PropertiesPanel'
import OtherPanel from './panel/otherPanel'
import { translateNodeName } from "./common/bpmnUtils";
import modelerStore from '@modules/flowable/components/Process/common/global'
import FlowUser from "@modules/flowable/components/Flow/User/index.vue";
import FlowRole from "@modules/flowable/components/Flow/Role/index.vue";
import FlowExp from "@modules/flowable/components/Flow/Expression/index.vue";
import { onMounted, ref, watch } from 'vue'

defineOptions({ name: 'Designer' })

const activeName = ref('common');
const executionListenerCount = ref(0);
const taskListenerCount = ref(0);
const elementId = ref('');
const elementType = ref('');
const conditionVisible = ref(false); // 流转条件设置
const formVisible = ref(false); // 表单配置
const rules = ref({
  id: [
    { required: true, message: '节点Id 不能为空', trigger: 'blur' },
  ],
  name: [
    { required: true, message: '节点名称不能为空', trigger: 'blur' },
  ],
});
watch(elementId, () => {
  activeName.value = "common";
});
// 初始化数据
function initFormOnChanged(element) {
  let activatedElement = element;
  if (!activatedElement) {
    activatedElement =
      modelerStore.elRegistry.find(el => el.type === "bpmn:Process") ??
      modelerStore.elRegistry.find(el => el.type === "bpmn:Collaboration");
  }
  if (!activatedElement) return;
  modelerStore.element = activatedElement;
  elementId.value = activatedElement.id;
  elementType.value = activatedElement.type.split(":")[1] || "";
  conditionVisible.value = !!(
    elementType.value === "SequenceFlow" &&
    activatedElement.source &&
    activatedElement.source.type.indexOf("StartEvent") === -1
  );
  formVisible.value = elementType.value === "UserTask" || elementType.value === "StartEvent";
}
// 注册节点事件
function getActiveElement() {
  // 初始第一个选中元素 bpmn:Process
  initFormOnChanged(null);
  modelerStore.modeler.on("import.done", e => {
    initFormOnChanged(null);
  });
  // 监听选择事件，修改当前激活的元素以及表单
  modelerStore.modeler.on("selection.changed", ({ newSelection }) => {
    initFormOnChanged(newSelection[0] || null);
  });
  modelerStore.modeler.on("element.changed", ({ element }) => {
    // 保证 修改 "默认流转路径" 类似需要修改多个元素的事件发生的时候，更新表单的元素与原选中元素不一致。
    if (element && element.id === elementId.value) {
      initFormOnChanged(element);
    }
  });
}
function getExecutionListenerCount(value) {
  executionListenerCount.value = value;
}
function getTaskListenerCount(value) {
  taskListenerCount.value = value;
}
onMounted(() => {
  getActiveElement();
});
</script>
