<template>
  <div>
    <el-dialog
      append-to-body
      :title="dialogOption.title || '弹窗操作'"
      :width="dialogOption.width || '50%'"
      :top="dialogOption.top || '50px' "
      :visible="showDialogProps"
      width="60%" @close='closeDialog'>
      <avue-form ref="form" :value.sync="objectData" :option="formOption" @reset-change="resetForm" @submit="submit">
        <template
          slot-scope="{column,value,size,disabled,type}" v-for="item in formOption.registerFieldComponents"
          :slot="item">
          <!-- 引入新的组件  json -->
          <span v-if="column.type === 'json'">
              <b-code-editor :name="column.prop" :value="objectData[column.prop] || ''" :auto-format="true"
                             :smart-indent="true"
                             :theme="column.theme || 'idea'"
                             :indent-unit="4" :lint="true" :line-wrap="false" ref="editor"
                             @on-blur="jsonFormat(column.prop)"
              >
              </b-code-editor>
          </span>
        </template>
      </avue-form>
    </el-dialog>
  </div>
</template>

<script>
import CodeEditor from 'bin-code-editor';
import crudUtil from "@/utils/server-crud"

export default {
  name: "avue-dialog",
  props: {
    // 是否显示弹层
    showDialogProps: {
      type: Boolean,
      required: false
    },
    // 展示默认值数据
    objectData: {
      type: Object,
      required: false,
      default: {}
    },
    // form的option参数
    formOption: {
      type: Object,
      required: true,
    },
    dialogOption: {
      default: {}
    }
  },
  data() {
    return {
      sizeValue: 'small'
    }
  },
  components: {
    'avue-json': CodeEditor
  },
  watch: {
    // 当关闭弹窗的时候，清空里面的值
    showDialogProps(val) {
      if (!val) {
        this.$refs.form.resetForm();
      }
    }
  },
  computed: {},
  mounted() {

  },
  methods: {
    resetForm() {
      this.$emit("resetForm");
    },
    submit(row, hide) {
      debugger;
      this.$emit("submit", row, hide);
    }, closeDialog() {
      this.$emit("closeDialog");
    }, getJsonString(value) {
      return crudUtil.getJsonString(value);
    }, jsonFormat(name) {
      crudUtil.jsonFormat(this, name);
    }, getSlotFormName(item) {
      return crudUtil.getSlotFormName(item);
    }
  }
}
</script>

<style scoped>

</style>
