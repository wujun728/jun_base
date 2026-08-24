<template>
  <!--  分享弹窗 -->
  <div>
    <el-dialog :before-close="handleClose" :title="fromData.title" :visible.sync="dialogVisible" width="480px">
      <div class="body">
        <div class="add-button">
          <el-button type="text" @click="openDepartment">{{ $t('file.addsharedmembers') }}</el-button>
        </div>
        <div class="content">
          <ul class="content-title">
            <li>
              <p class="text-left">{{ $t('file.spacemember') }}</p>
              <p>{{ $t('public.download') }}</p>
              <p>{{ $t('public.edit') }}</p>
              <p>{{ $t('public.operation') }}</p>
            </li>
            <li>
              <p class="text-left">{{ $t('file.batchsettings') }}</p>
              <p><el-checkbox v-model="downloadCheck" :disabled="userList.length <= 0" @change="downloadChange" /></p>
              <p><el-checkbox v-model="editCheck" :disabled="userList.length <= 0" @change="editChange" /></p>
              <p />
            </li>
          </ul>
          <ul class="content-body">
            <li v-for="(item, index) in userList" :key="index">
              <p class="username">
                <img :src="item.card.avatar" alt="" />
                <span>{{ item.card.name }}</span>
              </p>
              <p><el-checkbox :key="index" v-model="item.downloadCheck" :label="item.downloadCheck" /></p>
              <p><el-checkbox :key="index" v-model="item.editCheck" :label="item.editCheck" /></p>
              <p>
                <el-button type="text" @click="deleteShare(index)">{{ $t('public.delete') }}</el-button>
              </p>
            </li>
          </ul>
        </div>
      </div>
      <div slot="footer" class="dialog-footer text-center">
        <el-button @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button type="primary" @click="handleAdd">{{ $t('public.ok') }}</el-button>
      </div>
    </el-dialog>
    <!-- <department
      ref="department"
      :open-status="openStatus"
      :title="fromData.name"
      :is-site="false"
      :show-person="true"
      :active-department="activeDepartment"
      :user-list="userList"
      @adminClose="departmentClose"
      @changeMastart="changeMastart"
    /> -->
  </div>
</template>

<script>
import { folderShareApi, folderShareUserApi } from '@/api/user'
export default {
  name: 'ShareDialog',
  components: {
    // department: () => import('@/components/departmentTree')
  },
  props: {
    fromData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      lookCheck: false,
      downloadCheck: false,
      editCheck: false,
      dialogData: [],
      openStatus: false,
      userList: [],
      activeDepartment: {},
      where: {
        page: 1,
        limit: 15
      }
    }
  },
  mounted() {},
  methods: {
    handleClose() {
      this.resetCheck()
      this.dialogVisible = false
    },
    handleOpen() {
      this.dialogVisible = true
    },
    // 选择部门关闭
    departmentClose() {
      this.openStatus = false
    },
    // 选择成员完成回调
    changeMastart(data) {
      this.userList = data.userList
      this.shareArray = []
      if (this.userList.length > 0) {
        this.userList.map((value) => {
          if (value.isCheck) {
            this.$set(value, 'downloadCheck', false)
            this.$set(value, 'editCheck', false)
          }
        })
      }
      this.openStatus = false
    },
    // 打开选择部门成员
    openDepartment() {
      this.$refs.department.reseatData()
      this.openStatus = true
    },
    handleAdd() {
      if (this.userList.length <= 0) {
        this.$message.error(this.$t('file.placeholder04'))
      } else {
        var list = []
        this.userList.forEach((value) => {
          list.push({
            uid: value.card.uid,
            download: value.downloadCheck ? 1 : 0,
            update: value.editCheck ? 1 : 0
          })
        })
        this.getFolderShare(this.fromData.id, { rule: list })
      }
    },
    lookChange() {
      this.lookCheck ? this.checkAll('lookCheck') : this.checkNone('lookCheck')
    },
    downloadChange() {
      this.downloadCheck ? this.checkAll('downloadCheck') : this.checkNone('downloadCheck')
    },
    editChange() {
      this.editCheck ? this.checkAll('editCheck') : this.checkNone('editCheck')
    },
    // 全选
    checkAll(items) {
      this.userList.forEach((item) => {
        item[items] = true
      })
    },
    // 反选
    checkNone(items) {
      this.userList.forEach((item) => {
        item[items] = false
      })
    },
    resetCheck() {
      this.lookCheck = false
      this.downloadCheck = false
      this.editCheck = false
    },
    deleteShare(index) {
      this.userList.splice(index, 1)
      if (this.userList.length <= 0) {
        this.resetCheck()
      }
    },
    // 分享
    getFolderShare(id, data) {
      folderShareApi(id, data).then((res) => {
        this.handleClose()
        this.$emit('isOk')
        this.userList = []
      })
    },
    getRule() {
      this.getFolderShareRule(this.fromData.id)
    },
    // 权限列表
    getFolderShareRule(id) {
      const data = {
        page: this.where.page,
        limit: this.where.limit
      }
      folderShareUserApi(id, data).then((res) => {
        this.userList = []
        if (res.data.list.length > 0) {
          res.data.list.forEach((value) => {
            var data = {
              card: value.user,
              id: value.to_uid,
              isCheck: false,
              lookCheck: value.auth.read === 1,
              downloadCheck: value.auth.download === 1,
              editCheck: value.auth.update === 1
            }
            value.auth.download === 1 ? (this.downloadCheck = true) : (this.downloadCheck = false)
            value.auth.update === 1 ? (this.editCheck = true) : (this.editCheck = false)
            this.userList.push(data)
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.body {
  .add-button {
    border-bottom: 1px solid #d8d8d8;
  }
  .content {
    ul,
    p {
      margin: 0;
      padding: 0;
    }
    ul {
      list-style: none;
      li {
        padding: 10px 0;
        display: flex;
        justify-items: center;
        align-items: center;
        text-align: center;
        p {
          width: 25%;
        }
      }
    }
    .text-left {
      text-align: left;
    }
    .content-body {
      ::v-deep .el-checkbox__label {
        display: none;
      }
      .username {
        text-align: left;
        display: flex;
        align-items: center;
        img {
          height: 24px;
          width: 24px;
          border-radius: 50%;
        }
        span {
          padding-left: 6px;
        }
      }
    }
  }
}
</style>
