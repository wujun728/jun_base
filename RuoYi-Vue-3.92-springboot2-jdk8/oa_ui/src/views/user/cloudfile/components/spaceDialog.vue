<template>
  <!--  新建企业空间-->
  <div>
    <el-dialog :before-close="handleClose" :title="fromData.title" :visible.sync="dialogVisible" width="680px">
      <div class="body">
        <div class="add-button">
          <p>空间名称</p>
          <el-input
            v-model="name"
            :disabled="fromData.edit === 2 && !spaceButton"
            :placeholder="$t('file.placeholder03')"
            class="search-input"
            maxlength="20"
            size="small"
          />
        </div>
        <div class="content">
          <ul class="content-title">
            <li>
              <p class="text-left">
                <select-member ref="selectMember" :value="userList || []" @getSelectList="getSelectList($event, 1)">
                  <template v-slot:custom>
                    <el-button
                      :disabled="fromData.edit === 2 && !spaceButton"
                      icon="el-icon-plus"
                      type="text"
                      @click.stop="openDepartment(1)"
                    >
                      添加成员</el-button
                    >
                  </template>
                </select-member>
              </p>
              <p>
                <el-checkbox v-model="lookCheck" :disabled="true" @change="handleChange(1)" />
                {{ $t('public.check') }}
              </p>

              <p>
                <el-checkbox
                  v-model="createCheck"
                  :disabled="userList.length <= 0 || (fromData.edit === 2 && !spaceButton)"
                  @change="handleChange(2)"
                />{{ $t('public.establish') }}
              </p>
              <p>
                <el-checkbox
                  v-model="editCheck"
                  :disabled="userList.length <= 0 || (fromData.edit === 2 && !spaceButton)"
                  @change="handleChange(3)"
                />
                {{ $t('public.edit') }}
              </p>
              <p>
                <el-checkbox
                  v-model="downloadCheck"
                  :disabled="userList.length <= 0 || (fromData.edit === 2 && !spaceButton)"
                  @change="handleChange(4)"
                />
                {{ $t('public.download') }}
              </p>
              <p>
                <el-checkbox
                  v-model="deleteCheck"
                  :disabled="userList.length <= 0 || (fromData.edit === 2 && !spaceButton)"
                  @change="handleChange(5)"
                />
                {{ $t('public.delete') }}
              </p>
              <p>
                {{ $t('public.operation') }}
              </p>
            </li>
          </ul>
          <el-scrollbar style="height: 40vh">
            <ul class="content-body">
              <li v-for="(item, index) in userList" :key="index">
                <p class="username">
                  <img :src="item.card ? item.card.avatar : item.avatar" alt="" />
                  <span>{{ item.card ? item.card.name : item.name }}</span>
                </p>
                <p><el-checkbox v-model="item.lookCheck" :disabled="true" /></p>
                <template v-if="index === 0">
                  <p>
                    <el-checkbox v-model="item.createCheck" :disabled="fromData.edit === 2" :label="item.createCheck" />
                  </p>
                  <p>
                    <el-checkbox v-model="item.editCheck" :disabled="fromData.edit === 2" :label="item.editCheck" />
                  </p>
                  <p>
                    <el-checkbox
                      v-model="item.downloadCheck"
                      :disabled="fromData.edit === 2"
                      :label="item.downloadCheck"
                    />
                  </p>
                  <p>
                    <el-checkbox v-model="item.deleteCheck" :disabled="fromData.edit === 2" :label="item.deleteCheck" />
                  </p>
                </template>

                <template v-else>
                  <p>
                    <el-checkbox
                      v-model="item.createCheck"
                      :disabled="fromData.edit === 2 && !spaceButton"
                      :label="item.createCheck"
                    />
                  </p>
                  <p>
                    <el-checkbox
                      v-model="item.editCheck"
                      :disabled="fromData.edit === 2 && !spaceButton"
                      :label="item.editCheck"
                    />
                  </p>
                  <p>
                    <el-checkbox
                      v-model="item.downloadCheck"
                      :disabled="fromData.edit === 2 && !spaceButton"
                      :label="item.downloadCheck"
                    />
                  </p>
                  <p>
                    <el-checkbox
                      v-model="item.deleteCheck"
                      :disabled="fromData.edit === 2 && !spaceButton"
                      :label="item.deleteCheck"
                    />
                  </p>
                </template>

                <p>
                  <template v-if="fromData.edit === 2 && index === 0">
                    <select-member
                      v-show="spaceButton"
                      :only-one="true"
                      ref="member"
                      :value="[userList[0]] || []"
                      @getSelectList="getSelectList($event, 2)"
                    >
                      <template v-slot:custom>
                        <el-button type="text" @click="openDepartment(2)">转让管理员</el-button>
                      </template>
                    </select-member>
                    <span v-if="!spaceButton" type="text">管理员</span>
                  </template>
                  <template v-else>
                    <el-button v-if="spaceButton" type="text" @click="deleteShare(index)">{{
                      $t('public.delete')
                    }}</el-button>
                  </template>
                </p>
              </li>
            </ul>
          </el-scrollbar>
        </div>
      </div>
      <div slot="footer" class="dialog-footer text-right">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="handleAdd">{{ $t('public.ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  folderSpaceAddApi,
  folderSpaceEntTransferApi,
  folderSpaceRenameApi,
  folderSpaceShareEditApi,
  folderSpaceShareRuleApi
} from '@/api/cloud'
export default {
  name: 'SpaceDialog',
  components: {
    selectMember: () => import('@/components/form-common/select-member')
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
      lookCheck: true,
      downloadCheck: false,
      editCheck: false,
      createCheck: false,
      deleteCheck: false,
      dialogData: [],
      userList: [],
      name: '',
      spaceButton: false,
      onlyPerson: false,
      openType: 1,
      transferUser: [],
      where: {
        page: 1,
        limit: 15
      }
    }
  },
  watch: {
    fromData: {
      handler(nVal) {
        if (nVal.edit === 2) {
          this.name = nVal.data.name
          this.spaceButton = nVal.data.uid === this.$store.state.user.userInfo.uid
          this.getFolderShareRule(nVal.data.id)
        }
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.userList = []
      this.resetCheck()
      this.dialogVisible = false
    },

    // 选择成员完成回调
    getSelectList(data, type) {
      if (type === 1) {
        this.userList = data
        this.shareArray = []
        if (this.userList.length > 0) {
          this.userList.map((value) => {
            if (value.isCheck) {
              this.$set(value, 'lookCheck', true)
              this.$set(value, 'createCheck', false)
              this.$set(value, 'downloadCheck', false)
              this.$set(value, 'editCheck', false)
              this.$set(value, 'deleteCheck', false)
            }
          })
        }
      } else {
        this.transferUser = data
        if (this.transferUser.length > 0) {
          const data = {
            to_uid: this.transferUser[0].uid
          }
          this.setTransfer(this.fromData.data.id, data)
        }
      }
      this.openStatus = false
    },
    // 打开选择部门成员
    openDepartment(type) {
      let that = this
      if (type == 1) {
        that.$refs.selectMember.handlePopoverShow()
      } else {
        that.$refs.member[0].handlePopoverShow()
      }
    },
    handleAdd() {
      if (this.name == '') {
        this.$message.error(this.$t('file.placeholder03'))
      } else {
        var list = []
        var data = {}
        this.userList.forEach((value) => {
          list.push({
            value: value.value,
            uid: value.uid,
            read: value.lookCheck ? 1 : 0,
            create: value.createCheck ? 1 : 0,
            download: value.downloadCheck ? 1 : 0,
            update: value.editCheck ? 1 : 0,
            delete: value.deleteCheck ? 1 : 0
          })
        })
        data = {
          name: this.name,
          rule: list
        }
        if (this.fromData.edit === 1) {
          this.getFolderShare(data)
        } else {
          data.folder = 1
          this.getFolderRenameEdit(this.fromData.data.id, data)
        }
      }
    },
    handleChange(type) {
      if (type === 1) {
        this.lookCheck ? this.checkAll('lookCheck') : this.checkNone('lookCheck')
      } else if (type === 2) {
        this.createCheck ? this.checkAll('createCheck') : this.checkNone('createCheck')
      } else if (type === 3) {
        this.editCheck ? this.checkAll('editCheck') : this.checkNone('editCheck')
      } else if (type === 4) {
        this.downloadCheck ? this.checkAll('downloadCheck') : this.checkNone('downloadCheck')
      } else {
        this.deleteCheck ? this.checkAll('deleteCheck') : this.checkNone('deleteCheck')
      }
    },
    // 全选
    checkAll(items) {
      this.userList.forEach((item, index) => {
        if (this.fromData.edit === 2 && item.id === this.$store.state.user.userInfo.uid) {
        } else {
          item[items] = true
        }
      })
    },
    // 反选
    checkNone(items) {
      this.userList.forEach((item) => {
        if (this.fromData.edit === 2 && item.id === this.$store.state.user.userInfo.uid) {
        } else {
          item[items] = false
        }
      })
    },
    resetCheck() {
      this.lookCheck = false
      this.downloadCheck = false
      this.editCheck = false
      this.createCheck = false
      this.deleteCheck = false
      this.name = ''
    },
    deleteShare(index) {
      this.userList.splice(index, 1)
      if (this.userList.length <= 0) {
        this.resetCheck()
      }
    },
    // 添加
    async getFolderShare(data) {
      await folderSpaceAddApi(data)
      await this.handleClose()
      this.$emit('isOk')
    },
    // 修改
    getFolderRename(id, data) {
      folderSpaceRenameApi(id, data).then((res) => {
        this.handleClose()
        this.$emit('isOk')
      })
    },
    getFolderRenameEdit(id, data) {
      folderSpaceShareEditApi(id, data).then((res) => {
        if (res.status == 200) {
          this.handleClose()
          this.$emit('isOk')
        }
      })
    },
    // 转让空间
    setTransfer(id, data) {
      folderSpaceEntTransferApi(id, data).then((res) => {
        if (res.status == 200) {
          this.handleClose()
          this.$emit('isOk')
        }
      })
    },
    // 权限列表
    getFolderShareRule(id) {
      const data = {
        page: this.where.page,
        limit: this.where.limit
      }
      folderSpaceShareRuleApi(id, data).then((res) => {
        this.userList = []
        if (res.data.list.length > 0) {
          res.data.list.forEach((value) => {
            if (value.to_uid !== this.fromData.data.uid) {
              const data = {
                card: value.user,
                id: value.to_uid,
                uid: value.user.uid,
                id: value.user.id,
                isCheck: false,
                name: value.user.name,
                value: value.user.id || 0,
                lookCheck: value.auth.read === 1,
                createCheck: value.auth.create === 1,
                downloadCheck: value.auth.download === 1,
                editCheck: value.auth.update === 1,
                deleteCheck: value.auth.delete === 1
              }
              value.auth.read === 1 ? (this.createCheck = true) : (this.createCheck = false)
              value.auth.download === 1 ? (this.downloadCheck = true) : (this.downloadCheck = false)
              value.auth.update === 1 ? (this.editCheck = true) : (this.editCheck = false)
              value.auth.delete === 1 ? (this.deleteCheck = true) : (this.deleteCheck = false)

              this.userList.push(data)
            }
          })
        }
        // 生成第一个为管理的数据
        let card_id = JSON.parse(localStorage.getItem('userInfo'))
        const m = {
          card: this.fromData.data.user,
          isDelete: true,
          id: this.fromData.data.user.uid,
          uid: this.fromData.data.user.uid,
          value: card_id.id,
          isCheck: false,
          lookCheck: true,
          createCheck: true,
          downloadCheck: true,
          editCheck: true,
          deleteCheck: true
        }
        this.userList.unshift(m)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.body {
  .add-button {
    padding-bottom: 16px;
    border-bottom: 1px solid #f2f2f2;
  }
  .content {
    ::v-deep .el-scrollbar__wrap {
      overflow-x: hidden;
    }
    margin-right: -24px;
    ul,
    p {
      margin: 0;
      padding: 0;
    }
    ul {
      list-style: none;
      padding-right: 24px;
      li {
        padding: 10px 0;
        display: flex;
        justify-items: center;
        align-items: center;
        text-align: left;
        p {
          width: calc(100% / 7);
          padding-left: 20px;
          &:first-of-type {
            padding-left: 0;
          }
        }
      }
    }
    .text-left {
      text-align: left;
    }
    .content-title {
      ::v-deep .el-checkbox {
        margin-right: 4px;
      }
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
