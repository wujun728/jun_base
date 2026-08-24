<template>
  <el-drawer
    :append-to-body="true"
    title="审批人设置"
    :visible.sync="$store.state.business.approverDrawer"
    direction="rtl"
    class="set_promoter"
    size="550px"
    :before-close="closeDrawer"
  >
    <div class="demo-drawer__content">
      <div class="drawer_content">
        <div class="approver_content">
          <el-radio-group v-model="approverConfig.settype" class="clear" @change="changeType">
            <el-radio label="1">指定成员</el-radio>
            <el-radio label="2">指定上级</el-radio>
            <el-radio label="7">连续多级上级</el-radio>
            <el-radio label="5">申请人本人</el-radio>
            <el-radio label="4" v-if="typeStr !== 'lowCode'">申请人自选</el-radio>
          </el-radio-group>
        </div>
        <div class="approver_node_user" v-if="approverConfig.settype == 1">
          <p class="title mt15">
            指定成员
            <span>不得超过100人</span>
          </p>
          <select-member
            :value="approverConfig.nodeUserList || []"
            @getSelectList="getSelectList"
            style="width: 100%"
          ></select-member>

          <div v-if="approverConfig.nodeUserList.length > 1">
            <p class="title mt15">多人审批方式</p>
            <el-radio-group v-model="approverConfig.examineMode" class="more-content" style="width: 100%">
              <el-radio label="1">或签（ 一名成员同意即可 ）</el-radio>
              <el-radio label="2">会签（ 须所有成员同意 ）</el-radio>
              <el-radio label="3">依次审批（按顺序依次审批）</el-radio>
            </el-radio-group>
          </div>
        </div>
        <div class="approver_manager" v-if="approverConfig.settype == 2">
          <p class="title">指定层级</p>
          <el-row>
            <el-col :span="11">
              <el-select v-model="approverConfig.directorOrder">
                <el-option value="0" label="从上至下"></el-option>
                <el-option value="1" label="从下至上"></el-option>
              </el-select>
            </el-col>
            <el-col :span="11" class="pull-right">
              <el-select v-model="approverConfig.directorLevel">
                <el-option
                  v-for="item in directorMaxLevel"
                  :label="item == 1 ? '直属上级' : '第' + item + '级上级'"
                  :value="item.toString()"
                  :key="item"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="approver_manager" v-if="approverConfig.settype == 7">
          <p class="title">
            指定终点
            <span>将从申请人的直属上级依次审批到此上级为止</span>
          </p>
          <el-row>
            <el-col :span="11">
              <el-select v-model="approverConfig.directorOrder">
                <el-option value="0" label="从上至下"></el-option>
                <el-option value="1" label="从下至上"></el-option>
              </el-select>
            </el-col>
            <el-col :span="11" class="pull-right">
              <el-select v-model="approverConfig.directorLevel">
                <el-option
                  v-for="item in directorMaxLevel"
                  :label="item === 1 ? '直属上级' : '第' + item + '级上级'"
                  :value="item.toString()"
                  :key="item"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="approver_self" v-if="approverConfig.settype == 5">
          <p>该审批节点设置“发起人自己”后，审批人默认为发起人</p>
        </div>
        <div class="approver_self_select" v-show="approverConfig.settype == 4">
          <h3>可选范围</h3>
          <el-radio-group v-model="approverConfig.selectRange" style="width: 100%" @change="changeRange">
            <el-radio label="1">不限范围</el-radio>
            <el-radio label="2">指定成员</el-radio>
          </el-radio-group>
          <select-member
            v-if="approverConfig.selectRange == 2"
            :value="approverConfig.nodeUserList || []"
            @getSelectList="getSelectList"
            style="width: 100%"
          ></select-member>

          <h3>选人方式</h3>
          <el-radio-group v-model="approverConfig.selectMode" style="width: 100%">
            <el-radio label="1">单选</el-radio>
            <el-radio label="2">多选</el-radio>
          </el-radio-group>
          <div v-if="approverConfig.selectMode == 2">
            <h3>多人审批方式</h3>
            <el-radio-group v-model="approverConfig.examineMode" class="more-content" style="width: 100%">
              <el-radio label="1">或签（ 一名成员同意即可 ）</el-radio>
              <el-radio label="2">会签（ 须所有成员同意 ）</el-radio>
              <el-radio label="3">依次审批（按顺序依次审批）</el-radio>
            </el-radio-group>
          </div>
        </div>
        <div class="approver_some" v-if="approverConfig.settype == 2 || approverConfig.settype == 7">
          <p class="title">当前层级无部门负责人时</p>
          <el-radio-group class="person" v-model="approverConfig.noHanderAction">
            <el-radio label="1">由上一级部门负责人审批</el-radio>
            <el-radio label="2">此审批节点为空时直接跳过，不视为异常</el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button size="small" @click="closeDrawer">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="saveApprover">{{ $t('public.ok') }}</el-button>
      </div>
    </div>
  </el-drawer>
</template>
<script>
export default {
  props: {
    directorMaxLevel: {
      type: Number,
      default: 5
    },
    typeStr: {
      // 判断是低代码还是审批流程
      type: String,
      default: ''
    }
  },
  components: {
    selectMember: () => import('@/components/form-common/select-member')
  },
  data() {
    return {
      approverConfig: {},
      type: 1,
      checkedList: []
    }
  },
  computed: {
    approverConfig1() {
      return this.$store.state.business.approverConfig.value
    }
  },
  watch: {
    approverConfig1(val) {
      this.approverConfig = val
    }
  },
  methods: {
    changeRange() {
      this.approverConfig.nodeUserList = []
      this.checkedList = []
    },
    changeType(val) {
      this.approverConfig.nodeUserList = []
      this.checkedList = []
      this.approverConfig.examineMode = '3'
      this.approverConfig.noHanderAction = '2'
      if (val == 2) {
        this.approverConfig.directorLevel = '1'
      } else if (val == 4) {
        this.approverConfig.selectMode = '1'
        this.approverConfig.selectRange = '1'
      } else if (val == 7) {
        this.approverConfig.directorLevel = '1'
      }
    },
    saveApprover() {
      if (
        this.approverConfig.settype == 1 ||
        (this.approverConfig.settype == 4 && this.approverConfig.selectRange == 2)
      ) {
        if (this.approverConfig.nodeUserList.length <= 0) {
          this.$message.warning('至少选择一个指定成员')
          return false
        }
      }
      this.approverConfig.error = !this.$func.setApproverStr(this.approverConfig)
      this.$store.commit('updateApproverConfig', {
        value: this.approverConfig,
        flag: true,
        id: this.$store.state.business.approverConfig.id
      })
      this.$emit('update:nodeConfig', this.approverConfig)
      this.closeDrawer()
    },
    closeDrawer() {
      this.$store.commit('updateApprover', false)
    },
    // 选择成员完成回调
    getSelectList(data) {
      this.approverConfig.nodeUserList = data
    }
  }
}
</script>
<style lang="scss">
.set_promoter {
  .approver_content {
    padding-bottom: 10px;
    font-size: 13px;
    border-bottom: 1px solid #f2f2f2;
  }
  .approver_self_select .el-button,
  .approver_content .el-button {
    margin-bottom: 20px;
  }
  .approver_content .el-radio,
  .approver_some .el-radio,
  .approver_self_select .el-radio {
    margin-bottom: 20px;
  }
  .approver_node_user {
    padding: 0 20px;
    .title {
      font-size: 14px;
      font-weight: bold;
      span {
        font-size: 13px;
        color: #999999;
        font-weight: normal;
      }
    }
  }
  .approver_manager {
    p {
      line-height: 32px;
    }
    .title {
      font-size: 14px;
      font-weight: bold;
      span {
        font-size: 13px;
        color: #999999;
        font-weight: normal;
      }
    }
  }
  .approver_self {
    padding: 28px 20px;
    p {
      font-size: 14px;
    }
  }
  .approver_self_select,
  .approver_manager,
  .approver_content,
  .approver_some {
    padding: 20px 20px 0;
    .title {
      font-size: 14px;
      font-weight: bold;
    }
    .person {
      .el-radio {
        margin-bottom: 15px;
        display: block;
      }
    }
  }
  .approver_manager p:first-of-type,
  .approver_some p {
    line-height: 19px;
    font-size: 14px;
    margin-bottom: 14px;
  }
  .approver_self_select {
    h3 {
      margin: 5px 0 20px;
      font-size: 14px;
      font-weight: bold;
      line-height: 19px;
    }
  }
  .more-content {
    .el-radio {
      display: block;
      margin-bottom: 15px;
    }
  }
}
.plan-footer-one {
  height: auto;
}
</style>
