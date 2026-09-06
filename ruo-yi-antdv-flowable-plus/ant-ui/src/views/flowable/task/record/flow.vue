<template>
  <div>
    <bpmn-modeler
      ref="refNode"
      :xml="xmlData"
      :is-view="true"
      :taskList="taskData"
    />
  </div>
</template>
<script>
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import bpmnModeler from '@/components/Process/index'
export default {
  name: 'Flow',
  components: {
    bpmnModeler
  },
  props: {
    xmlData: {
      type: String,
      default: ''
    },
    taskData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {}
  }
}
</script>
