<template>
  <!-- 个人档案弹窗：新增：initData.type=add 编辑：initData.type=edit -->
  <div class="userDetails">
    <el-drawer
      :direction="direction"
      :title="initData.type !== 'edit' ? '个人档案' : ''"
      :visible.sync="drawerIsShow"
      size="65%"
      @close="handleClose"
    >
      <slot v-if="initData.type === 'add'" slot="title">
        <div class="tabsEdit">
          <div class="tabs">员工档案</div>
        </div>
      </slot>
      <!--------- 添加的页面 ----------->
      <div v-if="initData.type === 'add'" class="drawer-body">
        <!-- 左边菜单组件 -->
        <div class="left">
          <LeftNavigation
            :componentStatus="initData"
            :imageUrl="photo"
            :tabtypes="tabtypes"
            @getImageUrl="getImageUrl"
            @pageJump="pageJump"
          ></LeftNavigation>
        </div>

        <!-- 右边内容 -->
        <div ref="top" class="right">
          <formItemDataList
            ref="addData"
            :componentStatus="initData"
            :photo="photo"
            :tabtypes="tabtypes"
            @submitOk="submitOk"
          />
        </div>

        <!-- 底部保存 -->
        <div class="button from-foot-btn fix btn-shadow">
          <el-button size="small" @click="cancel">取消</el-button>
          <el-button size="small" type="primary" @click="protect">保存</el-button>
        </div>
      </div>

      <!--------- 编辑的页面 ----------->
      <slot v-if="initData.type === 'edit'" slot="title">
        <div class="tabsEdit">
          <div class="tabs">
            <el-tabs v-model="tabsName" class="cr-header-tabs" @tab-click="handleClick">
              <el-tab-pane label="个人档案" name="personalFile" />
              <el-tab-pane label="个人经历" name="personalExperience" />
              <el-tab-pane v-if="tabtypes !== 0" label="人事异动" name="personnelChange" />
              <el-tab-pane v-if="tabtypes !== 0" label="调薪记录" name="salaryAdjustmentRecord" />
            </el-tabs>
          </div>

          <div v-if="tabtypes == 1 && userInfo.uid !== ''" class="invitationUrl" @click="perfectFn">
            <el-link :underline="false"> <i class="iconfont icongerenjianli-yaoqing"></i> 邀请完善档案</el-link>
          </div>
        </div>
      </slot>
      <div v-if="initData.type === 'edit'" class="drawer-body">
        <!-- tabs: 个人档案 -->
        <template v-if="tabsName === 'personalFile'">
          <div class="left">
            <LeftNavigation
              :componentStatus="initData"
              :imageUrl="photo"
              :tabtypes="tabtypes"
              :userInfo="userInfo"
              @getImageUrl="getImageUrl"
              @pageJump="pageJump"
            ></LeftNavigation>
          </div>

          <div class="right">
            <formItemDataList
              :id="id"
              ref="editData"
              :componentStatus="initData"
              :photo="photo"
              :tabtypes="tabtypes"
              @getPhoto="getPhoto"
              @userInfoFn="userInfoFn"
            />
          </div>

          <!-- 编辑底部保存按钮 -->
          <div v-if="actionType" class="button from-foot-btn fix btn-shadow">
            <el-button size="small" @click="cancel">取消</el-button>
            <el-button :loading="isLoading" size="small" type="primary" @click="inDuction(actionType)">{{
              actionType == '入职' ? '入职' : '重新入职'
            }}</el-button>
          </div>
        </template>

        <!-- tabs: 个人经历 -->
        <template v-if="tabsName === 'personalExperience'">
          <!-- 调form组件只渲染 ['工作经历', '教育经历'] 内容 -->
          <div class="personalExperience">
            <formItemDataList :id="id" ref="editWork" :componentStatus="{ ...initData, personalExperience: true }" />
          </div>
        </template>

        <!-- tabs: 人事异动 -->
        <template v-if="tabsName === 'personnelChange'">
          <personnelChange :list="changeList" :tabsName="tabsName" />
        </template>

        <!-- tabs: 调薪记录 -->
        <template v-if="tabsName === 'salaryAdjustmentRecord'">
          <div style="padding: 20px; width: 100%">
            <personnelChange
              v-if="list && list.lenght !== 0"
              :list="list"
              :tabsName="tabsName"
              @deleteSalaryList="deleteSalaryList"
              @getSalaryContent="getSalaryContent"
            />

            <salaryAdjustmentRecord :id="id" ref="salaryAdjustmentRecord" @getSalaryList="getSalaryList" />
          </div>
        </template>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { getSalaryList, deleteSalaryList, changeCard, entryCard, perfectCard } from '@/api/enterprise'
import formOptions from '../mixins/index.js'
export default {
  name: 'userDetails',
  mixins: [formOptions],
  props: {
    tabtypes: {
      type: [String, Number],
      default: 1
    },
    actionType: {
      type: [String, Number],
      default: ''
    }
  },
  data() {
    return {
      drawerIsShow: false,
      tabsName: 'personalFile',
      direction: 'rtl',
      positionConfig: {},
      initData: {},
      id: '',
      photo: '',
      list: [],
      userInfo: {},
      changeList: [],
      isLoading: false
    }
  },
  watch: {
    height(val) {
      this.height = val
    }
  },
  mounted() {
    window.onresize = () => {
      //写在mounted中,onresize事件会在页面大小被调整时触发
      return (() => {
        window.screenHeight = document.body.clientHeight
        this.height = window.screenHeight
      })()
    }
  },
  methods: {
    // 点击左侧菜单，右边内容跳转
    pageJump(id, index) {
      if (this.tabtypes === 0 && this.initData.type === 'add') {
        document.getElementById(index).scrollIntoView({ behavior: 'smooth' })
      } else {
        document.getElementById(id).scrollIntoView({ behavior: 'smooth' })
      }
    },

    // 获取：个人简历头像
    getImageUrl(data) {
      this.photo = data
    },

    //  获取：档案新增/编辑头像
    getPhoto(data) {
      this.photo = data
    },

    // 新增：取消按钮
    cancel() {
      this.drawerIsShow = false
    },

    // 新增：保存按钮
    protect() {
      this.$refs.addData.saveCard()
    },

    // 新增： 保存刷新
    submitOk() {
      this.drawerIsShow = false
    },

    // 编辑：切换Tabs
    handleClick(tab) {
      if (tab.label == '个人经历') {
        this.getlist()
      } else if (tab.label == '调薪记录') {
        this.getSalaryList()
      } else if (tab.label == '人事异动') {
        this.changeCard()
      } else {
        setTimeout(() => {
          this.$refs.editData.enterpriseCardId()
        }, 100)
      }
    },

    // 编辑: 工作经历
    getlist() {
      setTimeout(() => {
        this.$refs.editWork.enterpriseCardId()
      }, 100)
    },

    userInfoFn(data) {
      this.userInfo = data
    },

    // 编辑：人事异动
    async changeCard() {
      let data = {
        card_id: this.id
      }
      const result = await changeCard(data)
      let list = result.data.list
      list.forEach((item) => {
        if (item.types == 0) {
          const isPart = {
            0: '全职',
            1: '兼职',
            2: '实习',
            3: '劳务派遣',
            4: '退休返聘',
            5: '劳务外包'
          }
          item.is_part = isPart[item.types] || '其他'
        }
      })
      this.changeList = list
    },

    // 编辑: 调薪弹窗
    changeSalary() {
      let data = {
        title: '调薪弹窗'
      }
      this.$refs.salaryAdjustmentRecord.changeSalary(data)
    },
    // 定薪
    onSalary() {
      this.$refs.salaryAdjustmentRecord.dialogVisible = true
      this.$refs.salaryAdjustmentRecord.title = '定薪'
      this.$refs.salaryAdjustmentRecord.status = 'add'
    },

    // 编辑: 获取调薪记录
    async getSalaryList() {
      let data = {
        id: this.id
      }
      const result = await getSalaryList(data)
      this.list = result.data
    },

    // 编辑: 编辑调薪记录
    getSalaryContent(id) {
      let data = {
        title: '编辑薪资',
        id: id
      }
      this.$refs.salaryAdjustmentRecord.editId(data)
    },

    // 编辑: 删除调薪记录
    deleteSalaryList(id) {
      this.$modalSure('确定永久删除该记录吗').then(() => {
        deleteSalaryList(id).then((res) => {
          this.getSalaryList()
        })
      })
    },

    // 编辑： 入职/重新入职
    inDuction(data) {
      this.isLoading = true
      entryCard(this.id)
        .then((res) => {
          this.drawerIsShow = false
          this.isLoading = false
        })
        .then((err) => {
          this.isLoading = false
        })
    },

    // 编辑： 完善个人档案
    async perfectFn() {
      await perfectCard(this.id)
    },

    // 打开弹窗的操作
    init(data) {
      this.drawerIsShow = true
      this.initData = data

      if (this.initData.type == 'add') {
        setTimeout(() => {
          this.$refs.addData.defaultFn()
          this.photo = ''
        }, 300)
      }
      if (this.initData.type == 'edit') {
        setTimeout(() => {
          this.$refs.editData.enterpriseCardId()
          this.$refs.editData.defaultFn()
        }, 100)
      }
    },

    // 关闭弹窗的回调
    handleClose() {
      let set = this
      set.tabsName = 'personalFile'
      if (this.initData.type === 'add') {
        this.photo = ''
      }

      if (set.initData.type == 'add') {
        setTimeout(function () {
          set.$refs.addData.resetForm()
        }, 100)
      }
      if (set.initData.type == 'edit') {
        setTimeout(() => {
          set.$refs.editData.resetForm()
        }, 100)
      }
      this.$emit('getList')
    }
  },

  components: {
    // 左边菜单
    LeftNavigation: () => import('./LeftNavigation.vue'),
    // 工作经历/教育经历
    tableBox: () => import('./tableBox.vue'),
    // 上传图片
    UploadImage: () => import('../components/UploadImage.vue'),
    // 右边表单
    formItemDataList: () => import('./formItemDataList.vue'),
    // 人事异动
    personnelChange: () => import('./personnelChange.vue'),
    // 调薪记录
    salaryAdjustmentRecord: () => import('./record.vue')
  }
}
</script>
<style lang="scss" scoped>
.left {
  // padding-top: 20px;
  width: 180px;
  height: calc(100vh - 80px);
}
.personalExperience {
  height: 600px;
  overflow-y: auto;
  width: 100%;
  margin-bottom: 200px;
  padding: 0 20px;
  margin-top: 10px;
}

.personalExperience::-webkit-scrollbar {
  height: 0;
  width: 0;
}

::v-deep .el-drawer__body {
  overflow-y: hidden;
}
// ::v-deep .el-drawer {
//   padding: 0px 0px 20px 0;
// }

::v-deep .el-drawer__header {
  height: 56px;
  line-height: 56px;

  // padding: 0 20px;
}
::v-deep .el-tabs__item {
  font-weight: 700;
  z-index: 9999;
}
.main {
  margin: 20px;
  overflow-y: hidden;
}

.table-box {
  height: 600px;
  overflow-y: auto;
  padding: 0 13px;
  .table-item {
    padding-bottom: 29px;
    .title {
      margin: 20px 0 14px;
      padding-left: 10px;
      border-left: 2px solid #1890ff;
      font-size: 14px;
    }
  }
}
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
.add-text {
  font-size: 14px;
  font-weight: 500;
  color: #1890ff;
  margin: 20px 20px 0 0;
}

.drawer-body {
  display: flex;
}

.right {
  margin: 0 20px;
  width: calc(100% - 180px);
  height: calc(100vh - 80px);

  overflow-x: hidden;
  padding-bottom: 300px;
  padding-top: 20px;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
  overflow-y: auto;
}

.from-item-title {
  display: flex;
  justify-content: space-between;
  height: 20px;
  border-left: 2px solid #1890ff;

  .editBtn {
    margin-right: 20px;
  }

  span {
    padding-left: 10px;
    font-weight: 500;
    font-size: 13px;
  }
}

.form-box {
  display: flex;
  flex-wrap: wrap;
  margin: 0 20px;
  justify-content: space-between;

  .form-item {
    width: 48%;

    ::v-deep .el-form-item__content {
      width: calc(100% - 90px);
    }

    ::v-deep .el-input {
      width: 95%;
    }

    ::v-deep .el-select {
      width: 100%;
    }

    ::v-deep .el-form-item {
      margin-bottom: 24px;
    }

    ::v-deep .el-textarea__inner {
      resize: none;
    }
  }
}

::v-deep .head-portrait .avatar-uploader-icon {
  width: 90px !important;
  height: 110px !important;
  line-height: 110px !important;
}
::v-deep .avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

::v-deep .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
}

::v-deep .el-upload--picture-card {
  width: 160px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  line-height: 100px;
  position: relative;
  overflow: hidden;
}

.plan-footer-one {
  height: 36px;
  line-height: 36px;

  .placeholder {
    font-size: 14px;
    color: #ccc;
  }

  span {
    margin-right: 6px;
  }
}

.from-text {
  margin: 20px 0;
  font-size: 11px;
  color: gray;
}

::v-deep .avatar {
  width: 160px;
  height: 100px;
  display: block;
}
.tabsEdit {
  display: flex;
  justify-content: space-between;
  .invitationUrl {
    margin-right: 20px;
    height: 56px;
  }
}
::v-deep.el-drawer__close-btn {
  display: flex;
  justify-content: space-between;
  color: #c0c4cc;
}
</style>
