<template>
  <!-- 实体管理页面 -->
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <div class="head flex-between">
        <div class="flex-tab">
          <el-tabs v-model="activeName" @tab-click="handleTabClick">
            <el-tab-pane
              v-for="(item, index) in applicationTabData"
              :key="item.id"
              :label="item.name"
              :name="item.id"
            ></el-tab-pane>
          </el-tabs>
        </div>

        <div class="right lh-center">
          <el-button type="primary" icon="el-icon-plus" class="mb10" size="small" @click="createEntity('', 'add')">
            新建实体</el-button
          >
          <div class="sheBox mb10" @click="openBox">
            <span class="el-icon-setting"></span>
          </div>
        </div>
      </div>
      <div class="splitLine"></div>
      <!-- 表格 -->
      <div class="flex h32">
        <div class="inTotal">共 {{ total }} 条</div>
        <div class="ml20">
          <el-input
            v-model="where.table_name"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入关键字"
            clearable
            style="width: 250px"
            @change="getList(1)"
            @keyup.native.stop.prevent.enter="getList(1)"
            class="input"
          ></el-input>
        </div>
      </div>
      <div class="table-box">
        <el-table
          v-loading="loading"
          row-key="id"
          :data="tableData"
          :height="height"
          style="width: 100%"
          :tree-props="{ children: 'children' }"
        >
          <el-table-column prop="table_name_en" label="实体名称" min-width="180">
            <template slot-scope="scope">
              <span class="color-doc pointer" @click="designFn(scope.row)">{{ scope.row.table_name_en }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="table_name" label="显示名称" min-width="150">
            <template slot-scope="scope">
              {{ scope.row.table_name }}
              <span :class="scope.row.crud_id == 0 ? 'zhu' : 'cong'">{{
                scope.row.crud_id == 0 ? '(主)' : '(从)'
              }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="info" label="关联应用" min-width="250" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.cate && scope.row.cate.length > 0">{{ scope.row.cate.join('、') }}</span>
              <span v-else>--</span>
            </template>
          </el-table-column>

          <el-table-column prop="info" label="实体说明" min-width="250" show-overflow-tooltip>
            <template slot-scope="scope">{{ scope.row.info || '--' }}</template>
          </el-table-column>
          <el-table-column prop="user.name" label="创建人" width="230"> </el-table-column>
          <el-table-column prop="created_at" label="创建时间" width="230"> </el-table-column>
          <el-table-column prop="address" label="操作" fixed="right" width="150">
            <template slot-scope="scope">
              <el-button type="text" @click="designFn(scope.row)">实体设计</el-button>
              <el-dropdown>
                <span class="el-dropdown-link el-button--text el-button more">
                  更多
                  <i class="el-icon-arrow-down" />
                </span>
                <el-dropdown-menu style="text-align: left">
                  <!-- <el-dropdown-item @click.native="createEntity(scope.row, 'copy')"> 复制实体 </el-dropdown-item> -->
                  <el-dropdown-item @click.native="designFn(scope.row)"> 实体属性 </el-dropdown-item>
                  <el-dropdown-item @click.native="designFn(scope.row, 2)"> 字段设计 </el-dropdown-item>
                  <template v-if="scope.row.crud_id == 0">
                    <el-dropdown-item @click.native="designFn(scope.row, 3)"> 表单设计 </el-dropdown-item>
                    <el-dropdown-item @click.native="designFn(scope.row, 4)"> 列表设计 </el-dropdown-item>
                    <el-dropdown-item @click.native="designFn(scope.row, 5)"> 流程设计 </el-dropdown-item>
                    <el-dropdown-item @click.native="designFn(scope.row, 6)"> 触发器设计 </el-dropdown-item>
                  </template>
                  <el-dropdown-item
                    divided
                    v-if="scope.row.children && scope.row.children.length == 0 && scope.row.crud_id == 0"
                    @click.native="createEntity(scope.row)"
                  >
                    添加从实体
                  </el-dropdown-item>
                  <el-dropdown-item divided @click.native="deleteEntity(scope.row)"> 删除实体 </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="page-fixed">
        <el-pagination
          :page-size="where.limit"
          :current-page="where.page"
          :page-sizes="[15, 20, 30]"
          layout="total,sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </el-card>

    <!-- 应用管理弹窗 -->
    <applicat-dialog ref="applicat" :list="applicationTabData" @getList="getCrudAllType"></applicat-dialog>
    <!-- 新建实体弹窗 -->
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
import { roterPre } from '@/settings'
import oaDialog from '@/components/form-common/dialog-form'
import Commnt from '@/components/develop/commonData'
import applicatDialog from '@/components/develop/applicatDialog'
import {
  getcrudCateListApi,
  databaseSaveApi,
  databaseListApi,
  databaseDelApi,
  databaseCopyApi,
  getDatabaseApi
} from '@/api/develop'
export default {
  name: 'CrmebOaEntIndex',
  components: {
    applicatDialog,
    oaDialog
  },
  data() {
    return {
      fromData: {
        width: '600px',
        title: '新建实体',
        btnText: '确定',
        labelWidth: '100px',
        type: ''
      },
      where: {
        table_name: '',
        page: 1,
        limit: 15,
        cate_id: ''
      },
      height: window.innerHeight - 300 + 'px',
      id: 0,
      total: 0,
      tableData: [],
      loading: false,
      formDataInit: Commnt.formDataInit,
      formRules: Commnt.formRules,
      formConfig: Commnt.formConfig,
      applicationTabData: [],
      activeName: 0
    }
  },
  watch: {
    // 监听路由参数的变化，更新activeName
    '$route.query': {
      immediate: true,
      handler(query) {
        this.activeName = query.tab || ''
      }
    }
  },
  created() {
    this.getCrudAllType()
    this.getList(this.activeName)
  },

  methods: {
    // 获取应用分类
    async getCrudAllType() {
      const data = await getcrudCateListApi()
      this.applicationTabData = [{ name: '全部实体', id: '' }, ...data.data.list]
      this.applicationTabData.forEach((item) => {
        item.id = item.id + ''
        item.sort = item.sort || 0
      })
    },
    // 点击应用分类
    handleTabClick(tab) {
      // 点击标签时，更新路由参数
      let tabid = tab.name ? tab.name : '0'
      this.$router.push({ query: { tab: tabid } })
      this.where.page = 1
      this.getList(tabid)
    },
    // 获取实体列表
    async getList(val) {
      if (this.where.cate_id == '自定义') {
        return false
      }
      this.where.cate_id = this.activeName
      this.loading = true
      const data = await databaseListApi(this.where)
      this.total = data.data.count
      this.tableData = data.data.list
      this.loading = false
    },

    // 打开应用管理弹窗
    openBox() {
      const oldList = this.applicationTabData
      const list = oldList.slice(1, this.applicationTabData.length)
      this.$refs.applicat.openBox(list)
    },

    // 新建实体
    async createEntity(row, type) {
      this.formDataInit = {
        table_name: '',
        table_name_en: '',
        crud_id: '',
        cate_ids: [],
        crud_type: '0',
        info: '',
        show_log: '1',
        show_comment: 1
      }

      if (!row && this.where.cate_id > 0) {
        this.formDataInit.cate_ids.push(this.where.cate_id)
      }

      if (row.id) {
        this.id = row.id
        this.formDataInit.crud_id = row.id
      }
      if (type == 'copy') {
        this.fromData.title = '复制实体'
        this.fromData.type = 'copy'
        this.formDataInit.info = row.info
        this.formDataInit.cate_ids = row.cate_ids
        this.formDataInit.info = row.info
      } else {
        this.fromData.type = ''
      }
      const data = await getDatabaseApi()
      this.formConfig[3].options = data.data
      const oldList = this.applicationTabData
      const list = oldList.slice(1, this.applicationTabData.length)
      this.formConfig[7].options = list
      this.$refs.oaDialog.openBox()
    },

    // 新建实体回调
    submit(data, type) {
      if (data.crud_id) {
        data.crud_id = data.crud_id[1]
      }

      if (type == 'copy') {
        databaseCopyApi(this.id, data)
          .then((res) => {
            if (res.status == 200) {
              this.$refs.oaDialog.handleClose()
              this.getList()
            }
          })
          .catch((err) => {
            this.$message.error(err.message)
          })
      } else {
        databaseSaveApi(data)
          .then((res) => {
            if (res.status == 200) {
              this.$refs.oaDialog.handleClose()
              this.getList()
            }
          })
          .catch((err) => {
            this.$message.error(err.message)
          })
      }
    },

    // 删除实体
    async deleteEntity(row) {
      await this.$modalSure('你确定要删除这条实体吗')
      await databaseDelApi(row.id)
      const totalPage = Math.ceil((this.total - 1) / this.where.limit)
      this.where.page = this.where.page > totalPage ? totalPage : this.where.page
      this.where.page = this.where.page < 1 ? 1 : this.where.page
      await this.getList()
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },

    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    designFn(row, tabIndex) {
      this.$router.push({
        path: `${roterPre}/develop/crud/design`,
        query: {
          id: row.id,
          tabIndex: tabIndex,
          tab: this.activeName
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.head {
  display: flex;
  width: 100%;

  ::v-deep .el-tabs__header {
    margin-bottom: 0;
  }

  ::v-deep .el-tabs__nav-wrap::after {
    height: 0;
  }
  ::v-deep .el-tabs__item {
    margin-bottom: 5px;
    line-height: 32px;
  }
  .flex-tab {
    width: calc(100% - 200px);
    display: flex;
    align-items: center;
    ::v-deep .el-tabs {
      max-width: 100%;
    }

    .el-icon-setting {
      cursor: pointer;
      margin-bottom: 12px;
      margin-left: 10px;
      color: #909399;
    }
  }
}
.flex {
  margin-top: 20px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}
.cong {
  color: #909399;
}
.zhu {
  color: #1890ff;
}
::v-deep .el-input--small .el-input__inner {
  font-size: 13px;
}
::v-deep .el-button--small {
  font-size: 13px;
}
::v-deep.el-card__body {
  padding-top: 14px;
}
.sheBox {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  line-height: 32px;
  text-align: center;
  margin-left: 10px;
  cursor: pointer;
}
</style>
