<template>
  <div v-loading="loading" element-loading-text="正在加载中..." element-loading-spinner="el-icon-loading">
    <MainUpload
      v-if="isUploadText"
      :templateId="templateId"
      :businessId="businessId"
      :fileInfo="fileInfo"
      @showLoading="showLoading"
      @refreshText="showPreview"
    />
    <MainPreview v-if="isPreview" :fileInfo="fileInfo" :showDownLoad="!isUploadText" />
    <MainStamp v-if="isStamp" :title="title" :fileId="fileId" :businessId="businessId" @showPreview="showPreview" @submitLoading="showLoading" />
    <el-empty v-if="empty" :image-size="100"></el-empty>
  </div>
</template>
  
<script>
import MainStamp from "./main-stamp.vue";
import MainPreview from "./main-preview.vue";
import MainUpload from "./main-upload.vue";
import { getMainInfo } from "@/api/workflow/mainText";
import { getToken } from "@/utils/auth";
import { StrUtil } from "@/utils/StrUtil";
import Base from "@/utils/base64";

export default {
  name: "FlowMainText",
  components: { MainStamp, MainPreview, MainUpload },
  data() {
    return {
      loading: true,
      // 正文是否用户上传
      isUploadText: false,
      // 是否预览
      isPreview: false,
      // 是否盖章
      isStamp: false,
      // 是否空
      empty: false,
      // 业务ID
      businessId: null,
      // 表单标题
      title: null,
      // 模板ID
      templateId: null,
      // 文件id
      fileId: null,
      // 正文文件信息
      fileInfo: {},
      // 访问url地址
      appUrl: process.env.VUE_APP_URL,
      // 预览地址
      preViewUrl: process.env.VUE_APP_PREVIEW_URL,
    };
  },
  props: {
    isDetail: {
      type: Boolean,
      default: true,
    },
  },
  methods: {
    /** 获取正文信息 */
    getMainText(businessId, templateId) {
      this.businessId = businessId;
      this.templateId = templateId;
      this.loading = true;
      let param = {
        businessId: businessId,
        templateId: templateId,
      };
      getMainInfo(param).then((res) => {
        if (res.code === 200) {
          if (res.data.stampStatus === "1") {
            this.$emit("handleButton");
          }
          this.fileId = res.data.fileId;
          const fileName = res.data.fileName;
          // 上传正文
          if (res.data.mainTextType === "0") {
            // 表单非编辑状态，不提供正文编辑操作
            this.isUploadText = this.isDetail ? false : true;
          }
          if (StrUtil.isEmpty(this.fileId)) {
            this.empty = true;
            this.isPreview = false;
            this.loading = false;
            return;
          }
          this.empty = false;
          this.isPreview = true;
          let token = "Bearer " + getToken();
          var base = new Base();
          var url = this.appUrl + "/file/operate/preview?Authorization=" + token + "&fileId=" + this.fileId + "&fullfilename=" + fileName; //要预览文件的访问地址
          let mainTextUrl = this.preViewUrl + encodeURIComponent(base.encode(url));
          this.fileInfo = { fileId: this.fileId, fileName: fileName, mainTextUrl: mainTextUrl };
        }
        this.loading = false;
      });
    },
    /** 显示盖章 */
    showStamp(title) {
      this.title = title;
      this.isPreview = false;
      this.isStamp = true;
    },
    /** 显示预览 */
    showPreview() {
      this.isPreview = true;
      this.isStamp = false;
      this.getMainText(this.businessId, this.templateId);
    },
    /** 加载loading状态 */
    showLoading(val) {
      this.loading = val;
    },
  },
};
</script>