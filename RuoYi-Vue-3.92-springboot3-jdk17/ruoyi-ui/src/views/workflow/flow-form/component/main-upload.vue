<template>
  <!-- 正文上传 -->
  <div class="upload-container">
    <div class="upload-tips">
      <i class="el-icon-warning-outline"></i>
      <span class="desc">注意：</span>
      <span>1、只能上传一份文件，如需多份，请先行压缩；2、不支持文件在线编辑；3、如需盖章，请上传Word、PDF类型文件（非压缩包）。</span>
    </div>
    <div class="upload-button-wrapper">
      <el-upload
        ref="mainTextUploader"
        :data="optData"
        :file-list="tableList"
        :headers="headers"
        :show-file-list="false"
        :action="uploadUrl"
        :auto-upload="true"
        :before-upload="onUnploadBefore"
        :on-success="onUploadSuccess"
      >
        <el-button size="mini" type="primary" icon="el-icon-upload">上传</el-button>
      </el-upload>
      <el-button size="mini" plain icon="el-icon-delete" @click="handleRemove">删除</el-button>
    </div>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import { uploadMainText, remove } from "@/api/workflow/mainText";

export default {
  name: "MainTextUpload",
  data() {
    return {
      // 请求头，必须携带
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      tableList: [],
      // 上传时附带的参数
      optData: {
        chunkFlag: false, // 是否分片
        chunkNumber: 1, // 分片数，默认1，不分片
        totalChunks: 1, // 总分片数，默认1，不分片
      },
      // 上传接口地址
      uploadUrl: process.env.VUE_APP_BASE_API + "/file/operate/uploadfile",
    };
  },
  props: {
    businessId: {
      type: String,
      default: null,
    },
    templateId: {
      type: String,
      default: null,
    },
    fileInfo: {
      type: Object,
      default: null,
    },
  },
  methods: {
    /** 上传前校验 */
    onUnploadBefore(file) {
      if (this.tableList.length > 0) {
        this.$message.error("只能上传一份文件，请先删除再上传！");
        return false;
      }
      let isRightSize = file.size / 1024 / 1024 < 500;
      if (!isRightSize) {
        this.$message.error("文件大小不能超过500MB");
        return false;
      }
      this.$emit("showLoading", true);
      return true;
    },
    /** 上传成功后回调 */
    onUploadSuccess(res, file, fileList) {
      fileList.map((item) => {
        if (item.response) {
          const { fileId } = item.response.data[0];
          this.tableList.push({
            uid: item.uid,
            name: item.name,
            fileId: fileId,
          });
          let param = {
            businessId: this.businessId,
            templateId: this.templateId,
            fileId: fileId,
          };
          uploadMainText(param).then((res) => {
            if (res.code === 200) {
              this.$modal.msgSuccess("上传成功");
              this.$emit("refreshText");
            }
          });
        }
      });
    },
    /** 删除 */
    handleRemove() {
      if (this.tableList.length <= 0 && !this.fileInfo.fileId) {
        this.$message.warning("没有可删除文件！");
        return;
      }
      this.$confirm("确认删除正文文件吗？删除后不可恢复，需重新上传！", "提示", { confirmButtonText: "确 认", cancelButtonText: "取 消" })
        .then(() => {
          this.$emit("showLoading", true);
          if (this.tableList.length > 0) {
            this.tableList.splice(0, 1);
          }
          remove(this.businessId).then((res) => {
            if (res.code === 200) {
              this.$emit("refreshText");
            }
          });
        })
        .catch(() => console.info("操作取消"));
    },
  },
};
</script>

<style scoped>
.upload-container {
  padding: 10px;
  background: #f5f9ff;
  border-radius: 1px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-tips {
  flex: 1;
  margin-right: 20px;
  color: #d28616;
  font-size: 14px;
}
.desc {
  font-weight: 600;
}

.upload-tips i {
  margin-right: 3px;
  flex-shrink: 0;
}

.upload-button-wrapper {
  display: flex;
  gap: 8px;
  align-items: center;
}
</style>