<template>
  <div>
    <div class="card-box">
      <el-card :body-style="{ padding: '20px 20px 0' }" class="box-card">
        <div class="setting-container">
          <el-row>
            <el-col v-bind="grid1">&nbsp;</el-col>
            <el-col v-bind="grid2">
              <el-form ref="elForm" :rules="rules" label-width="280px" size="medium">
                <div class="card-list">
                  <form-create
                    v-if="fromData"
                    :option="fromData.rule.options"
                    :rule="fromData.rule"
                    @submit="onSubmit"
                  />
                </div>
              </el-form>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script>
import request from '@/api/request'
import formCreate from '@form-create/element-ui'
export default {
  name: 'FollowRules',
  props: {
    ruleForm: {
      default: () => {},
      type: Object
    },
    fromData: {
      default: () => {},
      type: Object
    }
  },
  components: {
    formCreate: formCreate.$form()
  },
  data() {
    return {
      grid1: {
        xl: 2,
        lg: 4,
        md: 2,
        sm: 24,
        xs: 24
      },
      grid2: {
        xl: 20,
        lg: 18,
        md: 20,
        sm: 24,
        xs: 24
      },
      rules: {}
    }
  },
  methods: {
    onSubmit(formData) {
      request[this.fromData.method.toLowerCase()](this.fromData.action, formData)
    }
  }
}
</script>
<style lang="scss" scoped>
.card-box {
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;

  .box-card {
    .setting-container {
      // padding-bottom: 20px;

      .card-list {
        //border-spacing: 2px;
        .info {
          font-size: 12px;
          color: #909399;
          margin-left: 8px;
        }
      }
      .dash-line {
        // width: 100%;
        // height: 1px;
        // background-image: linear-gradient(to right, #dcdfe6 0%, #dcdfe6 50%, transparent 50%);
        // background-size: 12px 0.5px; //第一个值（20px）越大线条越长间隙越大
        // background-repeat: repeat-x;
        // margin-top: 20px;
      }
    }
  }
}
::v-deep.el-card.is-always-shadow {
  border: none;
}
::v-deep.el-select {
  width: 85%;
}
::v-deep.el-input--medium .el-input__inner {
  font-size: 13px;
}
</style>
