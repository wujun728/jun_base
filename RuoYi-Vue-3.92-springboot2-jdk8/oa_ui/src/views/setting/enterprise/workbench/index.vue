<template>
  <div class="divBox">
    <el-card>
      <div class="v-height-flag">
        <div v-height>
          <el-col :span="18" :offset="3">
            <div class="table-box">
              <form-create v-if="FromData" :option="option" :rule="FromData.rule" @submit="onSubmit" />
            </div>
          </el-col>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import formCreate from '@form-create/element-ui'
import request from '@/api/request'
import { getConfigWorkbenchApi } from '@/api/setting'
export default {
  name: 'Job',
  components: { formCreate: formCreate.$form() },
  data() {
    return {
      FromData: null,
      option: {
        form: {
          labelWidth: '150px'
        },
        global: {
          upload: {
            props: {
              onSuccess(rep, file) {
                if (rep.status === 200) {
                  file.url = rep.data.src
                }
              }
            }
          }
        }
      }
    }
  },
  created() {
    this.getConfigFrom()
  },
  methods: {
    getConfigFrom() {
      getConfigWorkbenchApi()
        .then(async (res) => {
          this.FromData = res.data
        })
        .catch((res) => {})
    },
    onSubmit(formData) {
      request[this.FromData.method.toLowerCase()](this.FromData.action.slice(4), formData)
        .then((res) => {
          // this.$message.success(res.message || '提交成功');
        })
        .catch((err) => {
          // this.$message.error(err.message || '提交失败');
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.table-box {
  min-height: 620px;
}
::v-deep .fc-files {
  width: 752px;
  height: 90px;
}
</style>
