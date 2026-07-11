<template>
  <!-- 消息通知 -->
  <div class="app-container">
    <el-form ref="messageNoticeInfoForm" :model="info" :rules="rules" label-width="140px">
      <el-form-item label="是否需要消息通知" prop="messageNoticeFlag">
        <el-radio v-model="info.messageNoticeFlag" label="0">否</el-radio>
        <el-radio v-model="info.messageNoticeFlag" label="1">是</el-radio>
      </el-form-item>
      <div v-if="info.messageNoticeFlag && info.messageNoticeFlag === '1'">
        <el-form-item label="消息类型" prop="type">
          <el-select v-model="info.type" placeholder="消息类型" clearable style="width: 240px">
            <el-option v-for="dict in dict.type.workflow_message_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="info.type === '1'" label="消息模板" prop="msgTemplate">
          <el-select v-model="info.msgTemplate" placeholder="请选择消息模板" clearable style="width: 240px">
            <el-option v-for="dict in dict.type.workflow_message_template_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "MessageNotice",
  props: {
    info: {
      type: Object,
      default: null,
    },
  },
  dicts: ["workflow_message_type", "workflow_message_template_type"],
  data() {
    return {
      rules: {
        messageNoticeFlag: [{ required: true, message: "请选择是否需要消息通知", trigger: "blur" }],
        type: [{ required: true, message: "请选择消息类型", trigger: "blur" }],
        msgTemplate: [{ required: true, message: "请选择消息模板或输入消息模板内容", trigger: "blur" }],
      },
    };
  },
  watch: {
    info: {
      handler(newVal) {
        if (Object.keys(newVal).length > 0) {
          if (newVal.type === "1") {
          }
        }
      },
      immediate: true,
    },
  },
  methods: {
    changeMessageType() {
      // this.info.msgTemplate = null;
    },
  },
};
</script>

<style scoped>
.desc {
  margin-top: 0;
  line-height: 20px;
  font-size: 12px;
  color: #909399;
}
</style>