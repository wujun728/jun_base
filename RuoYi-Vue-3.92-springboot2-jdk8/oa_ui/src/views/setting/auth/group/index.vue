<template>
  <div class="divBox">
    <el-card class="normal-page">
      <el-row>
        <!-- 左边结构 -->
        <el-col v-bind="gridl">
          <div class="tree-box">
            <div class="clearfix el-card__header">
              <el-input
                v-model="filterText"
                size="small"
                prefix-icon="el-icon-search"
                clearable
                placeholder="搜索部门"
              />
            </div>
            <el-tree
              class="mt14"
              ref="tree"
              :data="treeData"
              :props="defaultProps"
              :highlight-current="true"
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              :default-expanded-keys="defaultFrame"
              node-key="value"
              @node-click="handleNodeClick"
            >
              <div slot-scope="{ node, data }" class="custom-tree-node">
                <span class="flex-box">
                  <i class="tree-icon iconfont iconwenjianjia" />
                  {{ node.label }}
                </span>
              </div>
            </el-tree>
          </div>
        </el-col>
        <!-- 右边结构 -->
        <el-col v-bind="gridr" class="right">
          <div class="box">
            <oaFromBox
              :search="searchData"
              :title="$route.meta.title"
              :total="total"
              :isAddBtn="false"
              :isViewSearch="false"
              :sortSearch="false"
              @confirmData="confirmData"
            ></oaFromBox>
          </div>
          <div class="v-height-flag">
            <div class="v-height-flag table-box">
              <el-table
                ref="multipleTable"
                :data="tableData"
                :height="tableHeight"
                tooltip-effect="dark"
                style="width: 100%"
              >
                <el-table-column type="index" width="50"> </el-table-column>
                <el-table-column prop="name" :label="$t('toptable.name')" min-width="100" />
                <el-table-column prop="job.name" :label="$t('toptable.post')" min-width="120" />
                <el-table-column prop="frame.name" label="部门" min-width="160" show-overflow-tooltip>
                  <template slot-scope="scope">
                    <div v-for="(item, index) in scope.row.frames" :key="index">
                      <span class="icon-h"
                        >{{ item.name }}
                        <span v-if="item.is_mastart === 1 && scope.row.frames.length > 1" title="主部门">(主)</span>
                      </span>
                    </div>
                  </template>
                </el-table-column>

                <el-table-column prop="phone" :label="$t('toptable.phone')" min-width="110" show-overflow-tooltip />
                <el-table-column prop="join_time" :label="$t('toptable.jointime')" min-width="100" />
                <el-table-column :label="$t('public.operation')" width="180">
                  <template slot-scope="scope">
                    <el-button type="text" @click="onPassword(scope.row)" v-hasPermi="['auth:group:password']">
                      修改密码
                    </el-button>

                    <el-button type="text" @click="onEdit(scope.row.id)" v-hasPermi="['auth:group:edit']">
                      {{ $t('public.edit') }}
                    </el-button>

                    <el-button
                      type="text"
                      @click="onDelete(scope.row.id, scope.$index)"
                      v-hasPermi="['auth:group:delete']"
                    >
                      {{ $t('public.delete') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="page-fixed">
                <el-pagination
                  :page-size="formData.limit"
                  :current-page="formData.page"
                  :page-sizes="[15, 20, 30]"
                  layout="total, sizes,prev, pager, next, jumper"
                  :total="total"
                  @size-change="handleSizeChange"
                  @current-change="pageChange"
                />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!--  编辑用户  -->
    <editUser ref="editUser" :user-id="userId" />
    <!-- 修改密码 -->
    <oa-dialog
      ref="oaDialog"
      :fromData="fromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="submit"
    ></oa-dialog>
  </div>
</template>

<script>
import { configFrameApi } from '@/api/setting'
import { loginRegex } from '@/utils/format'
import { userListApi } from '@/api/user'
import { enterpriseCardDeleteApi, userPwdPut } from '@/api/enterprise'

export default {
  name: 'Index',
  components: {
    oaDialog: () => import('@/components/form-common/dialog-form'),
    editUser: () => import('./components/editUser'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    var sitedata = JSON.parse(localStorage.getItem('sitedata'))
    var { val, text } = loginRegex(sitedata.password_type, Number(sitedata.password_length))
    var uPattern = new RegExp(val)

    return {
      fromData: {
        with: '400px',
        title: '修改密码',
        btnText: '确定',
        labelWidth: '90px',
        type: ''
      },
      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      },
      formDataInit: {
        password: '',
        password_confirm: ''
      },
      formConfig: [
        {
          type: 'password',
          label: '密码：',
          placeholder: '请输入密码',
          key: 'password'
        },
        {
          type: 'password',
          label: '确认密码：',
          placeholder: '请确认密码',
          key: 'password_confirm'
        }
      ],
      formRules: {
        password: {
          pattern: uPattern,
          message: text,
          trigger: 'blur'
        },
        password_confirm: { pattern: uPattern, message: text, trigger: 'blur' }
      },
      treeData: [],
      tableData: [],
      filterText: '',
      defaultFrame: [],
      frame_id: 0,
      personConfig: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      total: 0,
      formData: {
        page: 1,
        limit: 15,
        name: ''
      },
      rowUid: '',
      userId: '', // 用户id

      indexType: null,
      searchData: [
        {
          field_name: '姓名、电话号码',
          field_name_en: 'name',
          form_value: 'input'
        }
      ]
    }
  },
  beforeDestroy() {
    this.$bus.$off()
  },

  created() {
    this.getList()
  },
  mounted() {
    this.$bus.$on('getList', () => {
      this.getList()
    })
  },

  watch: {
    treeData: {
      handler(nVal, oVal) {
        this.$nextTick(() => {
          const value = nVal[0].value
          this.$refs.tree.setCurrentKey(this.frame_id ? this.frame_id : value)
          this.defaultFrame = [this.treeData[0].value]
        })
      },
      deep: true
    },
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    handleNodeClick(data) {
      this.frame_id = data.value
      this.getList()
    },

    async submit(data) {
      const obj = {
        password: data.password,
        password_confirm: data.password_confirm,
        uid: this.rowUid
      }
      await userPwdPut(obj)
      await this.$refs.oaDialog.handleClose()
    },
    onPassword(row) {
      this.rowUid = row.uid
      this.$refs.oaDialog.openBox()
    },

    // 获取权限列表
    getList() {
      configFrameApi().then(async (res) => {
        this.treeData = res.data
        await this.getTableList()
      })
    },

    // tree搜索过滤
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },

    // 操作关闭
    handleClose() {
      this.isOpen = false
    },

    reset() {
      this.formData.name = ''
      this.formData.page = 1
      this.frame_id = ''
      this.getTableList()
    },

    // 企业成员
    getTableList() {
      userListApi({
        pid: this.frame_id,
        page: this.formData.page,
        limit: this.formData.limit,
        name: this.formData.name
      }).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },

    handleInvitees() {
      this.handleSearch()
    },

    handleSearch() {
      this.formData.page = 1
      this.getTableList()
    },

    // 分页
    pageChange(page) {
      this.formData.page = page
      this.getTableList()
    },

    handleSizeChange(size) {
      this.formData.limit = size
      this.getTableList()
    },
    // 编辑用户
    onEdit(id) {
      this.userId = id
      this.$refs.editUser.open()
    },

    close() {
      this.openStatus = false
    },

    //  删除人员
    async onDelete(id, index) {
      await this.$modalSure(this.$t('setting.group.deletitle'))
      await enterpriseCardDeleteApi(id)
      this.tableData.splice(index, 1)
    },
    confirmData(data) {
      if (data == 'reset') {
        this.formData = {
          page: 1,
          limit: 15,
          name: ''
        }
        this.getTableList()
      } else {
        this.formData = { ...this.formData, ...data }
        this.formData.page = 1
        this.getTableList()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  height: 100%;
}
::v-deep .el-card__body {
  padding: 0;
}
.icon-h {
  position: relative;
  & > span {
    color: #1890ff;
  }
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: rgba(240, 250, 254, 0.6);
  border-color: #1890ff;
  .flex-box {
    color: #1890ff;
    font-weight: 600;
  }
}

::v-deep.el-tree-node__content {
  height: 40px;
  padding-right: 15px;
  border-right: 2px solid transparent;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node,
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .right-icon {
  display: inline-block;
  color: #1890ff;
  font-weight: 600;
}
::v-deep.el-tree-node > .el-tree-node__children {
  overflow: inherit;
}
.right {
  padding: 20px;
  // padding-top: 7px;
  min-height: calc(100vh - 77px);
  border-left: 1px solid #eeeeee;
  .box {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 10px;
  }
}
.tree-box {
  width: 100%;
  background: #fff;
}
.tree-icon {
  margin-right: 5px;
  font-size: 15px;
  color: #ffca28;
}
.iconzhuyaobumen {
  color: #ff9900;
}
::v-deep .el-tag.el-tag--info {
  margin-top: 3px;
}
.department ::v-deep .el-input__inner {
  color: #c0c4cc;
}

.table-box {
  .btn-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
.table-container {
  margin-top: 15px;
}
.flex-box {
  display: block;
  width: 98%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
.right-btn {
  ::v-deep .el-input-group__append {
    top: 0;
    button {
      color: #fff;
      background-color: #1890ff;
      border-radius: 0 5px 5px 0;
    }
  }
}
.icon-h {
  position: relative;
  & > span {
    color: #1890ff;
  }
}
.icon {
  position: absolute;
  top: 0;
  right: -15px;
  display: inline-block;
  width: 13px;
  height: 13px;
  font-size: 10px;
  font-weight: 500;
  text-align: center;
  line-height: 13px;
  color: #fff;
  border-radius: 50%;
  background-color: #ff9900;
}
.custom-tree-node {
  position: relative;
  width: 100%;
  .right-icon {
    display: none;
    position: absolute;
    right: -10px;
    top: 50%;
    transform: translateY(-50%);
  }

  .edit-box {
    z-index: 200;
    position: absolute;
    right: 0;
    bottom: -124px;
    width: 120px;
    padding: 7px 0;
    box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.06);
    border-radius: 6px;
    color: #000000;
    background: #fff;
    .edit-item {
      height: 40px;
      line-height: 40px;
      padding-left: 19px;
      font-size: 13px;
      &:active {
        background: #f5f5f5;
      }
    }
  }
}
</style>
