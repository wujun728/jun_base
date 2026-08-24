<template>
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <el-row>
        <!-- 左边结构 -->
        <el-col :span="5" class="left">
          <div class="title">假期类型</div>
          <div class="tree-box">
            <el-tree
              class="mt14"
              ref="tree"
              :data="treeData"
              :props="defaultProps"
              node-key="value"
              current-node-key="value"
              :default-expand-all="true"
              :highlight-current="true"
              :expand-on-click-node="false"
              :default-checked-keys="defaultCheckedKeys"
            >
              <div slot-scope="{ node, data }" class="custom-tree-node">
                <span class="flex-box">
                  {{ node.label }}
                </span>
              </div>
            </el-tree>
          </div>
        </el-col>
        <!-- 右边结构 -->
        <el-col :span="19" class="right">
          <div>
            <!-- <form-box ref="formBox" /> -->
          </div>
          <div class="inTotal">共有 {{ total }} 条</div>
          <div class="mt20 pl14 pr14">
            <div v-loading="loading">
              <el-table ref="table" :data="tableData" style="width: 100%" row-key="id" key="tab" default-expand-all>
                <el-table-column label="姓名" prop="created_at" min-width="80" />
                <el-table-column label="部门" prop="created_at" min-width="100" />
                <el-table-column label="参加工作时间" prop="created_at" min-width="120" />
                <el-table-column label="入职时间" prop="created_at" min-width="120" />
                <el-table-column label="年假基数（天）" prop="created_at" min-width="150" />
                <el-table-column label="历年承载数（天）" prop="created_at" min-width="150" />
                <el-table-column label="已释放年假（天）" prop="created_at" min-width="150" />
                <el-table-column label="已休年假（天）" prop="created_at" min-width="150" />
                <el-table-column label="剩余年假（天）" prop="created_at" min-width="150" />
                <!-- <el-table-column label="创建时间" prop="created_at" width="100">
                <template slot-scope="scope">
                  <div style="width: 76px;">
                  </div>
                </template>
              </el-table-column> -->
              </el-table>
            </div>
            <div class="paginationClass">
              <el-pagination
                :page-size="where.limit"
                :current-page="where.page"
                :page-sizes="[10, 15, 20]"
                layout="total, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="pageChange"
              />
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
export default {
  name: '',
  components: {
    // formBox: () => import('../components/formBox')
  },
  data() {
    return {
      treeData: [
        {
          label: '年假',
          value: '',
          num: 0
        },
        {
          label: '事假',
          value: 0,
          num: 0
        },
        {
          label: '病假',
          value: 1,
          num: 0
        },
        {
          label: '调休假',
          value: 2,
          num: 0
        },
        {
          label: '婚假',
          value: 3,
          num: 0
        },
        {
          label: '产假',
          value: 4,
          num: 0
        },
        {
          label: '陪产假',
          value: 5,
          num: 0
        },
        {
          label: '丧假',
          value: 6,
          num: 0
        }
      ],
      total: 0,
      defaultCheckedKeys: [],
      loading: false,
      tableData: [],
      where: {
        page: 1,
        limit: 15,
        types: 1,
        cid: '',
        name: '',
        label: [],
        follows: '',
        time: '',
        salesman_id: ''
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.where.limit = val
      this.where.page = 1
    },
    pageChange(page) {
      this.where.page = page
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-card__body {
  padding: 0;
}
.m14 {
  padding: 14px;
}
.pl14 {
  padding-left: 14px;
}
.pr14 {
  padding-right: 14px;
}
::v-deep .el-tree-node__content:hover {
  background: rgba(240, 250, 254, 0.6);
  border-color: #1890ff;
  color: #1890ff;
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
  font-weight: 800;
}

.left {
  padding: 0;
  .title {
    padding-left: 25px;
    padding-top: 26px;
    padding-bottom: 25px;
    font-size: 14px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 600;
    color: #303133;
    border-bottom: 1px solid #eeeeee;
  }
}
.right {
  border-left: 1px solid #eeeeee;
  padding-top: 14px;
}
.inTotal {
  padding-left: 14px;
  padding-right: 14px;
}
</style>
