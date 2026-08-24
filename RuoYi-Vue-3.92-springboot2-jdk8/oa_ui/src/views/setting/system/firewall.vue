<template>
  <div class="divBox firewall-box">
    <el-card class="card-head normal-page" shadow="never" :body-style="{ padding: '20px 20px 0 20px' }">
      <el-form :model="formData" :inline="true" class="form-box" size="small" label-width="84px">
        <el-form-item label="开关：">
          <el-radio-group v-model="formData.status">
            <el-radio :label="0" :value="0">关闭</el-radio>
            <el-radio :label="1" :value="1">拦截</el-radio>
            <el-radio :label="2" :value="2">过滤</el-radio>
          </el-radio-group>
          <div class="form-item-tips">
            关闭：不验证请求数据；拦截：若非发请求则返回错误；过滤：过滤掉非法参数，程序继续执行
          </div>
        </el-form-item>
        <el-form-item label="规则设置：">
          <div class="firewall-rule-item" v-for="(item, index) of formData.ruleList" :key="index">
            <el-input v-model="formData.ruleList[index]" placeholder="请输入规则" clearable />

            <div class="delete-btn-box" v-if="index">
              <el-button type="text" class="delete-btn" @click="handleDeleteRule(index)">删除</el-button>
            </div>
          </div>
          <el-button type="text" icon="el-icon-plus" @click="handleAddRule" style="margin: 10px 0">添加规则</el-button>
          <div class="firewall-rule-item">
            <el-button type="primary" @click="handleSaveRule">保存</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getFirewallConfigApi, saveFirewallConfigApi } from '@/api/setting'

export default {
  data() {
    return {
      formData: {
        status: 0, // 0 -> 关闭，1 -> 拦截, 2 -> 过滤
        ruleList: ['']
      }
    }
  },
  created() {
    this.getFirewallConfig()
  },
  methods: {
    async getFirewallConfig() {
      const res = await getFirewallConfigApi()
      const { firewall_switch, firewall_content } = res.data
      this.formData = {
        status: firewall_switch,
        ruleList: firewall_content?.length ? firewall_content : ['']
      }
    },
    handleAddRule() {
      this.formData.ruleList.push('')
    },
    handleSaveRule() {
      const data = {
        firewall_switch: this.formData.status,
        firewall_content: this.formData.ruleList
      }
      saveFirewallConfigApi(data)
    },
    handleDeleteRule(index) {
      this.formData.ruleList.splice(index, 1)
    }
  }
}
</script>

<style scoped lang="scss">
.form-box {
  width: 650px;
  margin: 20px auto 0;
}

.form-item-tips {
  font-size: 13px;
  color: #909399;
}

.firewall-box {
  ::v-deep.el-form-item {
    width: 100%;
    display: flex;
  }

  ::v-deep.el-form-item__content {
    flex: 1;
  }

  ::v-deep.el-icon-circle-close::before {
    content: '\e79d';
  }

  ::v-deep.el-form-item__label {
    color: #606266;
  }

  .firewall-rule-item {
    position: relative;

    & + .firewall-rule-item {
      margin-top: 20px;
    }

    .delete-btn-box {
      position: absolute;
      left: 100%;
      padding-left: 10px;
      top: 0;
      font-size: 14px;
      display: none;
      .delete-btn {
        color: red;
      }
    }

    &:hover {
      .delete-btn-box {
        display: block;
      }
    }
  }
}
</style>
