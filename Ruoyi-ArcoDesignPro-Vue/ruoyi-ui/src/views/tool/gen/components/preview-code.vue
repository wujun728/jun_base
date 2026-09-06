<template>
  <a-modal :visible="open" :title="state.title" :footer="false" fullscreen width="50vw" @cancel="handleCancel">
    <div class="code-content">
      <a-spin class="spin-wrap" :loading="loading" tip="加载中">
        <a-tabs>
          <a-tab-pane
              v-for="(value, key) in state.data"
              :key="key"
              :title="(key as string).substring((key as string).lastIndexOf('/')+1, (key as string).indexOf('.vm'))"
          >
            <div class="code-content">
              <pre v-highlight class="highlight-wrap"><code v-text="value"></code></pre>
            </div>
          </a-tab-pane>
        </a-tabs>
      </a-spin>
    </div>
  </a-modal>

</template>

<script setup lang="ts">
import {computed, reactive, watch} from "vue";
import {previewTable} from "@/api/tool/gen";
import useLoading from "@/hooks/loading";

const {loading, setLoading} = useLoading(true);

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  tableId: {
    type: [Number, String],
    default: undefined,
  },
})

const open = computed(() => {
  return !!props.visible;
})

const state = reactive({
  title: '代码预览',
  data: {},
})

const reset = () => {
  state.data = {}
}

const initPreviewCodeData = async (value: string | number) => {
  setLoading(true)
  const { data } = await previewTable(value)
  state.data = {...data}
  setLoading(false)
}

const emits = defineEmits(['update:visible']);
const handleCancel = () => {
  emits('update:visible', false)
}

watch(
    () => props.tableId,
    (value) => {
      if (value) {
        initPreviewCodeData(value);
      } else {
        reset();
      }
    }
)

</script>

<style scoped lang="less">

.spin-wrap {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.highlight-wrap {
  box-sizing: border-box;
  margin: 0;
  padding: 28px 28px;
  background-color: #1e1e1e;
  border-radius: 4px;
}

.highlight-wrap:before {

}
</style>