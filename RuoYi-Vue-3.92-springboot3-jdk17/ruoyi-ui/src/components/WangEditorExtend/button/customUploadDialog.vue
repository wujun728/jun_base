<template>
  <el-dialog title="上传文件" :visible.sync="visible" width="500px" append-to-body>
    <el-upload
      ref="comUploader"
      :data="optData"
      :file-list="tableList"
      :headers="headers"
      :show-file-list="true"
      :action="uploadUrl"
      :auto-upload="true"
      :on-success="onUploadSuccess"
      :on-remove="handleRemove"
      :on-exceed="handleExceed"
      :on-progress="handlerProgress"
      :accept="limitType"
      :limit="1"
    >
      <el-button size="mini" type="primary" icon="el-icon-upload">选取文件</el-button>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submit">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
  
<script>
import { getToken } from "@/utils/auth";

export default {
  props: ["editor"],
  data() {
    return {
      visible: true,
      file: null,
      // 文件列表数据
      tableList: [],
      // 请求头，必须携带，否则会被拦截并返回401
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      // 上传时附带的参数
      optData: {
        chunkFlag: false, // 是否分片，固定值：false
        chunkNumber: 1, // 分片数，默认1，不分片
        totalChunks: 1, // 总分片数，默认1，不分片
      },
      // 上传状态，0-未上传，1-上传中，2-已上传
      uploadStatus: "0",
      limitType: "",
      uploadUrl: process.env.VUE_APP_BASE_API + "/file/operate/uploadfile", // 上传接口地址
      downloadUrl: process.env.VUE_APP_BASE_API + "/file/operate/downloadfile/v1?fileId=", //下载接口地址
    };
  },
  methods: {
    // 确定上传
    submit() {
      if (this.uploadStatus === "0") {
        this.$modal.msgError(`请先上传文件！`);
        return;
      } else if (this.uploadStatus === "1") {
        this.$modal.msgError(`文件正在上传处理中，请稍后...`);
        return;
      }
      // 实现上传逻辑
      this.$emit("confirm", { editor: this.editor, downloadUrl: this.downloadUrl, fileList: this.tableList });
    },
    // 取消上传
    cancel() {
      this.visible = false;
      this.$emit("close");
    },
    /** 上传成功后回调 */
    onUploadSuccess(res, file, fileList) {
      fileList.map((item) => {
        if (item.response) {
          const { fileId, identifier, sort } = item.response.data[0];
          this.tableList.push({
            uid: item.uid,
            name: item.name,
            size: item.size,
            fileId: fileId,
            identifier: identifier,
            sort: sort,
          });
          this.uploadStatus = "2";
        }
      });
    },
    /** 删除 */
    handleRemove(file, fileList) {
      this.tableList.splice(0, 1);
    },
    /** 限制个数提示 */
    handleExceed() {
      this.$modal.msgError(`请先删除原文件再上传!`);
    },
    handlerProgress() {
      this.uploadStatus = "1";
    },
  },
};
</script>  