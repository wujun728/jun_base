<template>
  <el-dialog
    :title="personConfig.title"
    :visible.sync="dialogFormVisible"
    width="500px"
    :modal="false"
    :close-on-click-modal="false"
    custom-class="person"
    :before-close="handleClose"
  >
    <div v-if="personConfig.type == 2">
      <div class="updata">
        <div class="name mb15">{{ excelFail.name }}</div>
        <el-upload
          class="upload-demo mb10"
          :action="fileUrl"
          :on-success="handleSuccess"
          :headers="myHeaders"
          :show-file-list="false"
          :data="uploadData"
          multiple
          accept=".xls,.xlsx"
          :before-upload="beforeUpload"
        >
          <el-button slot="trigger" size="small" type="primary">{{ $t('public.selectfile') }}</el-button>
        </el-upload>
        <a
          ><el-button slot="trigger" size="small" type="text" @click="handleDownLoad">{{
            $t('public.down')
          }}</el-button></a
        >
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close">{{ $t('public.cancel') }}</el-button>
        <el-button type="primary" @click="determine">{{ $t('public.ok') }}</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { getToken } from '@/utils/auth';
import SettingMer from '@/libs/settingMer';
import { downloadUrlApi } from '@/api/public';
import { userBatchCreate } from '@/api/user';

export default {
  name: 'UpdataXls',
  props: {
    frameId: {
      type: [String, Number],
      default: 0,
    },
    personConfig: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      dialogFormVisible: false,
      myHeaders: {
        authorization: 'Bearer ' + getToken(),
      },
      uploadData: {},
      excelFail: {},
      option: {
        form: {
          labelWidth: '75px',
        },
        submitBtn: true,
        global: {
          frame: {
            props: {
              closeBtn: false,
              okBtn: false,
              onLoad: (e) => {
                e.fApi = this.$refs.fc.$f;
              },
            },
          },
        },
        onSubmit: (formData) => {
          alert(JSON.stringify(formData));
        },
      }, // 表单配置
      FromData: null,
      rules: [
        {
          type: 'input',
          field: 'field1',
          title: 'field1',
          props: {
            placeholder: 'asdasd',
          },
          value: '',
          validate: [{ type: 'string', required: true, message: this.$t('public.tipstext') }],
        },
        {
          type: 'div',
          class: 'asdasd',
          children: ['asdasd'],
        },
      ],
    };
  },
  computed: {
    fileUrl() {
      return `${SettingMer.https}/common/upload`;
    },
  },
  methods: {
    async determine() {
      if (!this.excelFail.url) return this.$message.error(this.$t('public.tipstext1'));
      const data = {
        frame_id: this.frameId,
        file: this.excelFail.url,
      };
      await userBatchCreate(data)
      this.dialogFormVisible = false;
    },
    open() {
      this.dialogFormVisible = true;
    },
    handleClose(done) {
      this.excelFail = {};
      if (this.personConfig.type === 1) this.$refs.ruleForm.resetFields();
      done();
    },
    close() {
      this.dialogFormVisible = false;
    },
    // 上传成功
    handleSuccess(response) {
      if (response.status === 200) {
        this.$message.success(this.$t('public.tipstext2'));
        this.excelFail = response.data;
      } else {
        this.$message.error(response.message);
      }
    },
    beforeUpload(file) {
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1);
      const extension = testmsg === 'xls';
      const extension2 = testmsg === 'xlsx';
      if (!extension && !extension2) {
        this.$message({
          message: this.$t('public.tipstext3'),
          type: 'warning',
        });
      }
      return extension || extension2;
    },
    // 下载
    async handleDownLoad() {
      await downloadUrlApi({
        type: 'apply',
      })
      this.dialogFormVisible = false;
      await window.open(res.data.download_url, '_blank');
    },
  },
};
</script>

<style lang="scss" scoped>
.model-title {
  border-bottom: 1px solid #dfe6ec;
}
.form-box {
  margin-top: 20px;
}
.tips {
  margin-top: 10px;
  font-size: 14px;
  color: #999;
  line-height: 1.5;
}

.updata {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 150px;
}
</style>
