<!-- 人事-考勤管理-白名单设置 -->
<template>
  <div class="divBox">
    <el-card class="employees-card">
      <div class="main">
        <div class="title-16 mb20">白名单设置</div>

        <!-- 白名单 -->
        <el-form>
          <div class="form-box">
            <div class="form-item">
              <el-form-item>
                <span slot="label">白名单人员:</span>

                <select-member
                  :value="departmentObj.userList || []"
                  :placeholder="`请选择白名单人员`"
                  @getSelectList="getSelectList($event, 1)"
                  style="width: 100%"
                ></select-member>
              </el-form-item>
              <el-form-item>
                <span slot="label">超级管理员:</span>
                <select-member
                  :value="departmentObj.adminList || []"
                  :placeholder="`请选择考勤超级管理员`"
                  @getSelectList="getSelectList($event, 2)"
                  style="width: 100%"
                ></select-member>

                <span class="tips">超级管理员支持管理所有考勤组，及全公司所有人的考勤统计数据</span>
              </el-form-item>
              <el-form-item>
                <el-button size="small" class="ml70" :loading="loading" type="primary" @click="submit">保存</el-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
<script>
import { putWhitelistApi, getWhitelistApi } from '@/api/config'
export default {
  name: '',
  components: { selectMember: () => import('@/components/form-common/select-member') },
  props: {},
  data() {
    return {
      loading: false,
      title: '选择白名单人员',
      departmentObj: {
        userList: [], // 选择成员
        adminList: []
      },
      value: 1
    }
  },

  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      const result = await getWhitelistApi()
      result.data.members.map((item) => {
        let obj = {
          id: item.id,
          value: item.card ? item.card.id : '',
          name: item.card ? item.card.name : '',
          avatar: item.card ? item.card.avatar : ''
        }
        this.departmentObj.userList.push(obj)
      })
      result.data.admins.map((el) => {
        let obj = {
          value: el.card ? el.card.id : '',
          name: el.card ? el.card.name : '',
          avatar: el.card ? el.card.avatar : ''
        }
        this.departmentObj.adminList.push(obj)
      })
    },
    // 保存数据
    submit() {
      let { userList, adminList } = this.departmentObj
      let members = []
      let admins = []
      userList.map((item) => {
        members.push(item.value)
      })
      adminList.map((item) => {
        admins.push(item.value)
      })
      let data = {
        members,
        admins
      }
      this.loading = true
      putWhitelistApi(data)
        .then((res) => {
          this.loading = false
        })
        .catch((err) => {
          this.loading = false
        })
    },

    // 选择成员完成回调
    getSelectList(data, type) {
      if (type == 1) {
        this.departmentObj.userList = data
      }
      if (type == 2) {
        this.departmentObj.adminList = data
      }
    }
  }
}
</script>
<style scoped lang="scss">
.flex-box {
  cursor: pointer;
  span {
    margin-right: 6px;
  }
  span:last-of-type {
    margin-left: 0;
  }
}
.fz13 {
  font-size: 12px;
  color: #909399;
}
.ml70 {
  margin-left: 80px;
}
.main {
  width: 800px;
}
.ml90 {
  margin-left: 0px;
}
.alert {
  width: 100%;
  padding: 20px 0;
  padding-top: 0;
}
::v-deep .el-alert {
  padding-left: 30px;
  border: 1px solid #1890ff;
  color: #1890ff;
  font-size: 13px;
  background-color: #edf7ff;
  line-height: 1;
  margin-bottom: 10px;
}
::v-deep .el-alert--info .el-alert__description {
  color: #303133;
  font-size: 13px;
  font-weight: 500;
}
::v-deep .el-alert__icon.is-big {
  font-size: 16px;
  width: 15px;
}
.tips {
  font-size: 12px;
  color: #909399;
  font-size: 400 !important;
}
.plan-footer-one {
  cursor: pointer;
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  color: #606266;

  font-size: inherit;
  min-height: 32px;
  line-height: 32px;
  padding: 0 15px;
}
</style>
