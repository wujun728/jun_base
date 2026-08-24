<!--人事-职位管理-工作分析表 -->
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
                clearable
                placeholder="搜索部门"
                prefix-icon="el-icon-search"
                size="small"
              />
            </div>
            <el-tree
              ref="tree"
              :data="treeData"
              :default-expanded-keys="defaultFrame"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              :highlight-current="true"
              :props="defaultProps"
              node-key="value"
              @node-click="handleNodeClick"
            >
              <div slot-scope="{ node, data }" class="custom-tree-node">
                <div class="flex-box">
                  <i class="tree-icon iconfont iconwenjianjia" />
                  <div class="over-text">{{ node.label }}</div>
                </div>
              </div>
            </el-tree>
          </div>
        </el-col>
        <!-- 右边结构 -->
        <el-col class="right" v-bind="gridr">
          <oaFromBox
            v-if="search.length > 0"
            :isAddBtn="false"
            :isViewSearch="false"
            :search="search"
            :title="`工作分析表`"
            :total="totalSubmit"
            @confirmData="confirmData"
          ></oaFromBox>

          <div v-loading="loading" class="mt10">
            <el-table :data="tableData" :height="tableHeight" class="table" style="width: 100%">
              <el-table-column type="index" width="50"> </el-table-column>
              <el-table-column label="人员姓名" prop="name"> </el-table-column>
              <el-table-column label="职位" prop="job.name"> </el-table-column>
              <el-table-column label="部门" prop="frame.name">
                <template slot-scope="scope">
                  <div v-for="(item, index) in scope.row.frames" :key="index" class="frame-name over-text">
                    <span class="icon-h">
                      {{ item.name
                      }}<span v-show="item.is_mastart === 1 && scope.row.frames.length > 1" title="主部门">(主)</span>
                    </span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="更新时间" prop="updated_at"> </el-table-column>
              <el-table-column fixed="right" label="操作" width="130">
                <template slot-scope="scope">
                  <el-button
                    v-hasPermi="['hr:enterprise:analysis:check']"
                    size="small"
                    type="text"
                    @click="onCheck(scope.row)"
                    >查看</el-button
                  >
                  <el-button
                    v-hasPermi="['hr:enterprise:analysis:edit']"
                    size="small"
                    type="text"
                    @click="onEdit(scope.row)"
                    >编辑</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
      <div class="page-fixed">
        <el-pagination
          :current-page="where.page"
          :page-size="where.limit"
          :page-sizes="[15, 20, 30]"
          :total="totalSubmit"
          layout="total,sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看 -->
    <el-drawer :before-close="handleClose" :visible.sync="checkDrawer" direction="rtl" size="60%" title="工作分析表">
      <div v-if="detailData" class="check-box" >
        <div class="content mt14">
          <ueditorFrom :content="detailData" readOnly height="88vh" type="notepad" border />
        </div>
      </div>
      <div v-else>
        <default-page v-height :index="14" :min-height="580" />
      </div>
    </el-drawer>

    <!-- 编辑 -->
    <el-drawer
      :before-close="handleClose"
      :visible.sync="editDrawer"
      :wrapperClosable="false"
      direction="rtl"
      size="61%"
      title="编辑工作分析表"
    >
      <div class="check-box">
        <div class="user-name">
          <span>人员姓名：</span>
          <span class="text">{{ userName }}</span>
        </div>

        <component :is="loadEdit" ref="ueditorFrom" type="notepad" :border="true" :content="content" :height="height" />
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button size="small" @click="editDrawer = false">{{ $t('public.cancel') }}</el-button>
        <el-button :loading="saveLoading" size="small" type="primary" @click="handleConfirm">保存</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { configFrameApi } from '@/api/setting'
import { jobSelectApi } from '@/api/config.js'
import { jobAnalysis, jobDetails, putAnalysis } from '@/api/user'
import ueditorFrom from '@/components/form-common/oa-wangeditor'
import oaFromBox from '@/components/common/oaFromBox'
export default {
  name: 'CrmebOaEntAnalysis',
  components: {
    defaultPage: () => import('@/components/common/defaultPage'),
    oaFromBox,
    ueditorFrom
  },

  data() {
    return {
      treeData: [],
      height: 'calc(100vh - 180px)',
      defaultFrame: [],
      totalSubmit: 0,
      checkDrawer: false,
      editDrawer: false,
      saveLoading: false,
      detailData: '',
      filterText: '',
      userName: '',
      content: '',

      defaultProps: {
        children: 'children',
        label: 'label'
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

      where: {
        page: 1,
        limit: 15,
        frame_id: ''
      },
      search: [],
      loadEdit: '',
      loading: false,
      tableData: [],
      options: []
    }
  },
  watch: {
    treeData: {
      handler(nVal, oVal) {
        this.$nextTick(() => {
          const value = nVal[0].value
          this.$refs.tree.setCurrentKey(this.where.frame_id ? this.where.frame_id : value)
        })
      },
      deep: true
    },
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },

  mounted() {
    this.getList()
    this.getJobSelect()
  },

  methods: {
    // 获取权限列表
    async getList() {
      const result = await configFrameApi()
      this.treeData = result.data
      this.$set(this.where, 'frame_id', this.treeData[0].value)
      this.defaultFrame = [this.treeData[0].value]
      await this.getAnalysis()
    },
    // 获取职位下拉数据
    async getJobSelect() {
      const result = await jobSelectApi()
      this.options = result.data
      this.search = [
        {
          field_name: '姓名',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '请选择职位',
          field_name_en: 'job_id',
          form_value: 'select',
          data_dict: this.options
        }
      ]
    },

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          frame_id: ''
        }
        this.$set(this.where, 'frame_id', this.treeData[0].value)
        this.getAnalysis()
      } else {
        this.where = { ...this.where, ...data }
        this.getAnalysis(1)
      }
    },
    // tree搜索过滤
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },

    // 获取列表
    async getAnalysis(val) {
      this.loading = true
      if (val == 1) {
        this.where.page = 1
      }
      const result = await jobAnalysis(this.where)
      this.tableData = result.data.list
      this.loading = false
      this.totalSubmit = result.data.count
    },

    ueditorEdit(e) {},
    // 查看下级工作分析
    onCheck(data) {
      this.getCheck(data.id)
      this.checkDrawer = true
    },

    // 列表工作分析内容
    getCheck(uid) {
      return jobDetails(uid)
        .then((res) => {
          this.detailData = res.data.data
          this.loadEdit = ueditorFrom
        })
        .catch((err) => {
          this.detailData = ''
          this.loadEdit = ueditorFrom
        })
    },

    // 编辑下级工作分析
    onEdit(data) {
      this.userName = data.name
      this.uid = data.id

      this.getCheck(data.id)
        .then(() => {
          this.content = this.detailData || ''
        });
      this.editDrawer = true
    },

    // 保存编辑内容
    handleConfirm() {
      this.saveLoading = true
      this.content = this.$refs.ueditorFrom.getValue()
      let data = {
        data: this.content
      }
      putAnalysis(this.uid, data)
        .then((res) => {
          this.editDrawer = false
          this.saveLoading = false
          this.getAnalysis()
        })
        .catch((err) => {
          this.saveLoading = false
        })
    },

    handleNodeClick(data) {
      this.where.frame_id = data.value
      this.getAnalysis()
    },

    handleSizeChange(val) {
      this.where.page = 1
      this.where.limit = val
      this.getAnalysis()
    },
    handleCurrentChange(page) {
      this.where.page = page
      this.getAnalysis()
    },

    handleClose() {
      this.checkDrawer = false
      this.editDrawer = false
    }
  }
}
</script>

<style lang="scss" scoped>
.over-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.tree-box {
  background: #fff;
}
.tree-icon {
  margin-right: 5px;
  font-size: 15px;
  color: #ffca28;
}
.flex-box {
  display: flex;
  width: 95%;
  font-weight: 400px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-size: 14px;
  color: #303133;
  .text {
    display: inline-block;
    width: 60px !important;
  }
}
.inTotal {
  margin: 0;
}
.right {
  padding: 0 14px;
  border-left: 1px solid #eeeeee;
  padding-top: 20px;
}
.p20 {
  padding: 20px 0;
}
.user-name {
  margin-top: 20px;
  margin-bottom: 20px;
}
.user-name > span {
  font-size: 13px;
  color: #909399;
}
.user-name .text {
  display: inline-block;
  font-size: 13px;
  color: #303133;
}
.table {
  margin-top: 5px;
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

.check-box {
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  color: #666666;
  padding: 0px 20px 20px 24px;
  padding-top: 0px;

  .content {
    ::v-deep p {
      text-indent: 2em;
      font-size: 14px;
      line-height: 1.5;
    }
    ::v-deep td {
      border: 1px solid;
    }
    ::v-deep table {
      border: 1px solid #ccc;
    }

    ::v-deep table th {
      border: 1px solid #ccc;
    }
    ::v-deep table td {
      padding: 10px 5px;
      border: 1px solid #ccc;
    }

    ::v-deep p img {
      max-width: 800px;
    }
  }
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
}
</style>
