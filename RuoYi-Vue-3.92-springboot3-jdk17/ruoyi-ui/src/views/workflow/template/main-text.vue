<template>
  <!-- 正文 -->
  <div class="app-container">
    <el-form ref="mainInfoForm" :model="info" :rules="rules" label-width="128px">
      <el-form-item label="是否需要正文" prop="mainTextFlag">
        <el-radio v-model="info.mainTextFlag" label="0">否</el-radio>
        <el-radio v-model="info.mainTextFlag" label="1">是</el-radio>
      </el-form-item>
      <div v-if="info.mainTextFlag && info.mainTextFlag === '1'">
        <el-form-item label="启用方式" prop="type">
          <div>
            <el-radio-group v-model="info.type" @change="changeRadio">
              <el-radio :label="'0'">用户直接上传</el-radio>
              <el-radio :label="'1'">动态模板替换</el-radio>
            </el-radio-group>
          </div>
          <div class="desc">{{ descText }}</div>
        </el-form-item>
        <el-row v-if="info.type === '0'">
          <el-form-item label="限制大小" prop="limitSize">
            <el-input placeholder="请输入数字，单位Mb" v-model="info.limitSize" type="number" style="width: 300px">
              <template slot="append">Mb</template>
            </el-input>
          </el-form-item>
          <el-form-item label="限制文件类型" prop="limitType">
            <el-input placeholder="请输入文件后缀，多个用英文逗号隔开，不填默认全部文件类型，例：.docx, .ppt, .pdf" v-model="info.limitType" />
          </el-form-item>
        </el-row>
        <el-form-item v-if="info.type === '1'" label="上传模板" prop="fileId">
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
            :accept="limitType"
            :limit="1"
          >
            <el-button size="mini" type="primary" icon="el-icon-upload">选取文件</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="提示内容" prop="tipContent">
          <el-input placeholder="请输入提示内容，显示在正文页签的头部区域" v-model="info.tipContent" />
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";

export default {
  name: "MainText",
  props: {
    info: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      // 文件列表数据
      tableList: [],
      // 提示内容
      descText: "说明：正文内容由用户在起草时，直接选择文件上传，系统不做任何处理。",
      // 请求头，必须携带，否则会被拦截并返回401
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      // 上传时附带的参数
      optData: {
        chunkFlag: false, // 是否分片
        chunkNumber: 1, // 分片数，默认1，不分片
        totalChunks: 1, // 总分片数，默认1，不分片
      },
      limitType: ".doc, .docx",
      uploadUrl: process.env.VUE_APP_BASE_API + "/file/operate/uploadfile", // 上传接口地址
      rules: {
        limitSize: [{ required: true, message: "请填写正文限制大小", trigger: "blur" }],
        mainTextFlag: [{ required: true, message: "请选择是否需要正文", trigger: "blur" }],
        type: [{ required: true, message: "请选择启用方式", trigger: "blur" }],
        fileId: [{ required: true, message: "请上传正文模板", trigger: "change" }],
      },
    };
  },
  watch: {
    info: {
      handler(newVal, oldVal) {
        if (Object.keys(newVal).length > 0) {
          if (newVal.type === "1" && newVal.fileId) {
            const exists = this.tableList.some((item) => item.fileId === newVal.fileId);
            if (!exists) {
              this.tableList.push({
                uid: newVal.fileId,
                name: newVal.fileName + "." + newVal.extendName,
                fileId: newVal.fileId,
              });
            }
          }
        }
      },
      immediate: true,
    },
  },
  methods: {
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
          this.$set(this.info, "fileId", fileId);
          this.$nextTick(() => {
            this.$refs.mainInfoForm.clearValidate("fileId");
          });
        }
      });
    },
    /** 删除 */
    handleRemove(file, fileList) {
      this.tableList.splice(0, 1);
      this.info.fileId = null;
      this.$nextTick(() => {
        this.$refs.mainInfoForm.clearValidate("fileId");
      });
    },
    /** 限制个数提示 */
    handleExceed() {
      this.$modal.msgError(`请先删除原文件再上传!`);
    },
    /** 切换选择时改变提示内容 */
    changeRadio(val) {
      if (val === "0") {
        this.descText = "说明：正文内容由用户在起草时，直接选择文件上传，系统不做任何处理。";
      } else {
        this.descText = "说明：正文内容根据模板设置，固定或动态替换字段内容生成。";
      }
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