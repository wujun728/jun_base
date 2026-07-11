<template>
  <div>
    <flow-view :flowData="flowData" />
  </div>
</template>
<script>
import FlowView from "./flow-view";

export default {
  name: "Flow",
  components: {
    FlowView,
  },
  props: {
    flowData: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {};
  },
};
</script>
