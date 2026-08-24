<template>
  <!-- 申请审批-选择申请人弹窗 -->
  <el-drawer
    :append-to-body="true"
    :title="title"
    :visible.sync="$store.state.business.promoterDrawer"
    direction="rtl"
    class="set_promoter"
    size="550px"
    :before-close="closeDrawer"
  >
    <div class="demo-drawer__content">
      <div class="promoter_content drawer_content">
        <p class="title1">可提交申请的成员</p>
        <p class="title2">模板范围内的成员可提交，修改后，模板可见范围将被同步修改</p>
        <select-member
          :value="flowPermission.userList || []"
          @getSelectList="getSelectList"
          style="width: 100%"
        ></select-member>
      </div>
    </div>
  </el-drawer>
</template>
<script>
export default {
  components: {
    selectMember: () => import('@/components/form-common/select-member')
  },
  data() {
    return {
      flowPermission: {
        depList: [],
        userList: []
      },
      userList: [],
      depList: [],
      title: '申请人'
    }
  },
  computed: {
    flowPermission1() {
      return this.$store.state.business.flowPermission.value
    }
  },
  watch: {
    flowPermission1(val) {
      this.flowPermission = val
    }
  },
  methods: {
    savePromoter() {
      var arr1 = []
      if (this.flowPermission.depList.length > 0) {
        this.flowPermission.depList.map((value) => {
          arr1.push({
            id: value.id,
            is_mastart: value.is_mastart,
            name: value.name
          })
        })
      }
      this.flowPermission.depList = arr1
      this.$store.commit('updateFlowPermission', {
        value: this.flowPermission,
        flag: true,
        id: this.$store.state.business.flowPermission.id
      })
      this.closeDrawer()
    },
    // 选择成员完成回调
    getSelectList(data) {
      this.flowPermission.depList = []
      this.flowPermission.userList = data
    },

    cardTag(type, index) {
      if (type === 1) {
        this.flowPermission.userList.splice(index, 1)
      } else {
        this.flowPermission.depList.splice(index, 1)
      }
    },
    closeDrawer() {
      this.$store.commit('updatePromoter', false)
    }
  }
}
</script>
<style lang="scss" scoped>
.set_promoter {
  .promoter_content {
    padding: 0 20px;
    .el-button {
      margin-bottom: 20px;
    }
    .title1 {
      padding-top: 18px;
      font-size: 14px;
      line-height: 20px;
      color: rgba(0, 0, 0, 0.85);
      font-weight: 600;
    }
    .title2 {
      font-size: 13px;
      color: rgba(153, 153, 153, 0.85);
    }
  }
}
::v-deep .plan-footer-one {
  height: auto;
  line-height: 32px;
}
.from-foot-btn button {
  width: auto;
  height: auto;
}
</style>
