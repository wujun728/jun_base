<template>
  <div class="app-container">
    <el-form ref="attachmentInfoForm" :model="info" :rules="rules" label-width="128px">
      <el-form-item label="是否需要附件" prop="attachFlag">
        <el-radio v-model="info.attachFlag" label="0">否</el-radio>
        <el-radio v-model="info.attachFlag" label="1">是</el-radio>
      </el-form-item>
      <div v-if="info.attachFlag && info.attachFlag === '1'">
        <el-form-item label="附件名称" prop="name">
          <el-input placeholder="请输入附件名称，用于详情页面中的附件页签下显示" v-model="info.name" style="width: 400px;" />
        </el-form-item>
        <el-form-item label="限制大小" prop="limitSize">
          <el-input placeholder="请输入数字，单位Mb" v-model="info.limitSize" type="number" style="width: 400px;">
            <template slot="append">Mb</template>
          </el-input>
        </el-form-item>
        <el-form-item label="限制文件类型" prop="limitType">
          <el-input placeholder="请输入文件后缀，多个用英文逗号隔开，不填默认全部文件类型，例：.docx, .ppt, .pdf" v-model="info.limitType" />
        </el-form-item>
        <el-form-item label="提示内容" prop="tipContent">
          <el-input placeholder="请输入提示内容" v-model="info.tipContent" />
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  props: {
    info: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      rules: {
        name: [{ required: true, message: "请填写附件名称", trigger: "blur" }],
        fileType: [{ required: true, message: "请填写附件类型", trigger: "blur" }],
        attachFlag: [{ required: true, message: "请选择是否需要附件", trigger: "blur" }],
      },
    };
  },
};
</script>
