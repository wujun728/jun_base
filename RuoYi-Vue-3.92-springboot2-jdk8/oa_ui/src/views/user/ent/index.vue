<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '0px 20px 20px 20px' }" class="normal-page">
      <div>
        <el-row>
          <el-col class="mt20" v-bind="gridl">
            <menu-tree ref="menuTree" :is-show-user-count="false" :tree-data="treeData" @frameId="getFrameId" />
            <div v-if="treeData.length == 0">
              <default-page :index="14" :min-height="400"></default-page>
            </div>
          </el-col>
          <el-col v-bind="gridr">
            <div class="table-container ml20">
              <div>
                <div class="header-16">
                  <div class="title-16">企业通讯录</div>
                </div>
                <div class="seach-box">
                  <div class="inTotal">共 {{ total }} 条</div>

                  <el-input
                    v-model="where.search"
                    clearable
                    placeholder="请输入姓名/手机号码"
                    prefix-icon="el-icon-search"
                    size="small"
                    style="width: 210px"
                    @change="getUserAddBookeList(1)"
                    @keyup.native.stop.prevent.enter="getUserAddBookeList(1)"
                  >
                  </el-input>
                </div>
                <div>
                  <oa-table
                    :tableData="tableData"
                    :tableOptions="tableOptions"
                    :height="tableHeight"
                    :isShowPagination="false"
                    :loading="false"
                  >
                    <template #frames="{ row }">
                      <div v-for="(item, index) in row.frames" :key="index">
                        <span class="icon-h"
                          >{{ item.name }}
                          <span v-show="item.is_mastart === 1 && row.frames.length > 1" title="主部门">(主)</span>
                        </span>
                      </div>
                    </template>
                  </oa-table>
                </div>
              </div>
            </div>
            <div class="page-fixed">
              <el-pagination
                :current-page="where.page"
                :page-size="where.limit"
                :page-sizes="[15, 20, 30]"
                :total="total"
                layout="total, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="pageChange"
              />
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
// 引入用户通讯录树和列表的API
import { userAddBookTree, userAddBookeList } from '@/api/user';
// 引入菜单树组件
import menuTree from './components/menuTree';
// 引入默认页面组件
import defaultPage from '@/components/common/defaultPage';
// 引入自定义表格组件
import oaTable from '@/components/form-common/oa-table';
export default {
  name: 'Index',
  components: {
    menuTree,
    defaultPage,
    oaTable
  },
  data() {
    return {
      treeData: [],
      where: {
        page: 1,
        limit: 15,
        search: '',
        frame_id: '',
        field: ''
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
      tableOptions: [
        {
          label: '姓名',
          prop: 'name'
        },
        {
          label: '职位',
          render: (row) => {
            return <span>{row.job ? row.job.name : '--'}</span>
          }
        },
        {
          label: '部门',
          type: 'slot',
          name: 'frames'
        },
        {
          label: '联系方式',
          prop: 'phone'
        },
        {
          label: '邮箱',
          render: (row) => {
            return <span>{row.info && row.info.email ? row.info.email : '--'}</span>
          }
        }
      ],

      total: 0,
      tableData: []
    }
  },
  mounted() {
    this.getUserAddBookTree()
  },
  methods: {

    async getUserAddBookTree() {
      try {
        const result = await userAddBookTree();
        this.treeData = result.data;
        this.getUserAddBookeList();
      } catch (error) {
        console.error('获取用户通讯录树数据失败:', error);
      }
    },

    async getUserAddBookeList(num) {
      this.where.page = num || this.where.page;
      try {
        const result = await userAddBookeList(this.where);
        this.tableData = result.data.list;
        this.total = result.data.count;
      } catch (error) {
        console.error('获取用户通讯录列表数据失败:', error);
      }
    },

    pageChange(page) {
      this.where.page = page
      this.getUserAddBookeList()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getUserAddBookeList()
    },
    confirmData(data) {
      this.where = Object.assign(this.where, data)
      this.getUserAddBookeList(1)
    },
   
  
    getFrameId(data) {
      this.where.frame_id = data
      this.where.page = 1
      this.getUserAddBookeList()
    },
  
  }
}
</script>

<style lang="scss" scoped>
.iconzhuyaobumen {
  color: #ff9900;
}

.seach-box {
  margin-top: 8px;
  display: flex;
  align-items: center;
  .inTotal {
    margin-right: 15px;
  }
}
::v-deep .el-input__inner {
  display: flex;
  justify-content: start;
  align-items: end;
  line-height: 32px;
}

.table-container {
  padding-top: 20px;
  ::v-deep .el-form-item {
    margin-bottom: 0;
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
::v-deep .seach-box .el-input__clear {
  position: absolute;
  height: 100%;
  right: 0;
  top: -4px;
}
::v-deep .el-input__suffix {
  position: absolute;
  top: 5px;
}
</style>
