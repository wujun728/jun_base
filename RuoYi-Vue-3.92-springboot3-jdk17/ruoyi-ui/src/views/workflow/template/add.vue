<template>
  <div class="app-container">
    <div>
      <div class="mb10 text-success">
        <i class="el-icon-edit-outline mr5"></i>基本信息
      </div>
      <el-divider />
      <BasicInfo ref="basicInfoForm" :info="baseInfo" />
    </div>
    <div>
      <div class="mb10 text-success">
        <i class="el-icon-tickets mr5"></i>表单配置
      </div>
      <el-divider />
      <BusinessFormInfo ref="businessForm" :info="formInfo" :formType="formInfo.formType" :dynamicForm="dynamicForm" />
    </div>
    <div>
      <div class="mb10 text-success">
        <i class="el-icon-document mr5"></i>正文配置
      </div>
      <el-divider />
      <MainText ref="mainText" :info="mainTextInfo" />
    </div>
    <div>
      <div class="mb10 text-success">
        <i class="el-icon-paperclip mr5"></i>附件配置
      </div>
      <el-divider />
      <AttachmentInfo ref="attachmentInfoForm" :info="attachmentInfo" />
    </div>
    <div>
      <div class="mb10 text-success">
        <i class="el-icon-document mr5"></i>消息通知配置
      </div>
      <el-divider />
      <MessageNotice ref="messageNotice" :info="messageNoticeInfo" />
    </div>
    <el-form label-width="100px">
      <el-form-item style="text-align: center; margin-left: -100px; margin-top: 10px">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import BasicInfo from "./basic-info";
import BusinessFormInfo from "./business-form-info";
import AttachmentInfo from "./attachment-info";
import MainText from "./main-text";
import MessageNotice from "./message-notice.vue";
import { addTemplate, getTemplate, updateTemplate } from "@/api/workflow/template";

export default {
  name: "addTemplate",
  components: {
    BasicInfo,
    BusinessFormInfo,
    AttachmentInfo,
    MainText,
    MessageNotice,
  },
  data() {
    return {
      activeNames: ["1", "2", "3"],
      activeName: "basic",
      // 基本信息
      baseInfo: {},
      // 表单信息
      formInfo: {},
      // 附件信息
      attachmentInfo: { attachFlag: "1", limitSize: 500 },
      // 动态表单信息
      dynamicForm: {},
      // 正文信息
      mainTextInfo: { mainTextFlag: "0", mainTextWay: "0", limitSize: 200 },
      // 消息通知信息
      messageNoticeInfo: { messageNoticeFlag: "0", type: "1", msgTemplate: "" },
    };
  },
  created() {
    if (this.$route.query && this.$route.query.id) {
      getTemplate(this.$route.query.id).then((res) => {
        if (res.code === 200 && res.data) {
          this.baseInfo = res.data;
          this.formInfo = res.data;
          this.mainTextInfo = {
            ...res.data.mainText,
            mainTextFlag: res.data.mainTextFlag,
          };
          this.attachmentInfo = {
            ...res.data.attachment,
            attachFlag: res.data.attachFlag,
          };
          this.messageNoticeInfo = {
            ...res.data.messageNotice,
            messageNoticeFlag: res.data.messageNoticeFlag,
          };
          this.dynamicForm = res.data.dynamicForm;
        }
      });
    }
  },
  methods: {
    /** 保存 */
    submitForm() {
      const basicForm = this.$refs.basicInfoForm.$refs.basicInfoForm;
      const businessForm = this.$refs.businessForm.$refs.businessForm;
      const attachmentForm = this.$refs.attachmentInfoForm.$refs.attachmentInfoForm;
      const mainInfoForm = this.$refs.mainText.$refs.mainInfoForm;
      const messageNoticeForm = this.$refs.messageNotice.$refs.messageNoticeInfoForm;
      Promise.all([basicForm, businessForm, mainInfoForm, attachmentForm, messageNoticeForm].map(this.getFormPromise)).then((res) => {
        const validateResult = res.every((item) => !!item);
        if (validateResult) {
          const templateTable = Object.assign({}, basicForm.model, businessForm.model);
          // 正文处理
          const mainTextFlag = mainInfoForm.model.mainTextFlag;
          templateTable.mainTextFlag = mainTextFlag;
          templateTable.mainText = mainTextFlag === "1" ? mainInfoForm.model : null;
          // 附件处理
          const attachFlag = attachmentForm.model.attachFlag;
          templateTable.attachFlag = attachFlag;
          templateTable.attachment = attachFlag === "1" ? attachmentForm.model : null;
          // 消息通知
          const messageNoticeFlag = messageNoticeForm.model.messageNoticeFlag;
          templateTable.messageNoticeFlag = messageNoticeFlag;
          templateTable.messageNotice = messageNoticeFlag === "1" ? messageNoticeForm.model : null;
          if (!templateTable.id) {
            addTemplate(templateTable).then((res) => {
              this.$modal.msgSuccess(res.msg);
              if (res.code === 200) {
                this.close();
              }
            });
          } else {
            updateTemplate(templateTable).then((res) => {
              this.$modal.msgSuccess(res.msg);
              if (res.code === 200) {
                this.close();
              }
            });
          }
        } else {
          this.$modal.msgError("请完善必填字段！");
        }
      });
    },
    /** 表单校验 */
    getFormPromise(form) {
      return new Promise((resolve) => {
        form.validate((res) => {
          resolve(res);
        });
      });
    },
    // 关闭当前标签页并返回上个页面
    close() {
      const obj = {
        path: "/workflow/template/form/config",
        query: { t: Date.now() },
      };
      this.$tab.closeOpenPage(obj);
    },
  },
};
</script>

<style scoped></style>
