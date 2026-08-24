<template>
  <div class="divBox">
    <el-card>
      <el-row>
        <el-col v-bind="gridl">
          <div class="assess-left">
            <div class="el-card__header">
              <span class="pull-left">晋升表</span>
            </div>
            <div class="v-height-flag">
              <div v-height>
                <el-scrollbar style="height: 100%">
                  <ul class="assess-left-ul">
                    <li
                      v-for="(item, index) in department"
                      v-if="item.status == 1"
                      :key="index"
                      :class="index == tabIndex ? 'active' : ''"
                      @click="clickDepart(index, item)"
                    >
                      <span>{{ item.name }}</span>
                    </li>
                  </ul>
                </el-scrollbar>
              </div>
            </div>
          </div>
        </el-col>
        <!-- 右边晋升表格 -->
        <el-col class="assess-right" v-bind="gridr">
          <div class="title">{{ title }}</div>
          <el-table ref="table" :data="list" class="table" row-key="id" style="width: 100%">
            <el-table-column label="职级" min-width="200" prop="position">
              <template #default="{ row }">
                <div>
                  <span>{{ getText(row.ranks) }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="固定工资">
              <el-table-column label="基本工资" min-width="100" prop="benefit.basicSalary"> </el-table-column>
              <el-table-column label="绩效工资" min-width="100" prop="benefit.performance"> </el-table-column>
            </el-table-column>
            <el-table-column label="合计" min-width="100" prop="total">
              <template slot-scope="scope">
                <span>{{ getTotal(scope.row.benefit) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="效益工资">
              <el-table-column label="提成工资" min-width="100" prop="benefit.commission"> </el-table-column>
              <el-table-column label="团队奖" min-width="100" prop="benefit.teamAward"> </el-table-column>
              <el-table-column label="分红" min-width="100" prop="benefit.dividends"> </el-table-column>
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="180">
              <template slot-scope="scope">
                <el-button type="text" @click="handleCheck(scope.row)">晋升标准</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-card>

    <!-- 晋升标准侧滑 -->
    <el-drawer :before-close="handleClose" :visible.sync="editDrawer" direction="rtl" size="61%" title="晋升标准">
      <div class="check-box">
        <div class="user-name mb20">
          <span>职位：</span>
          <span class="text">{{ positions.map((obj) => obj.name).join('、') }}</span>
        </div>
        <ueditorFrom
          ref="ueditorFrom"
          :border="true"
          :disabled="true"
          :content="content"
          type="notepad"
          :height="height"
          @input="ueditorEdit"
        />
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { promotionDataApi, promotionListApi } from '@/api/config.js'
export default {
  name: '',
  components: {
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor')
  },
  data() {
    return {
      title: '',
      tabIndex: 0,
      editDrawer: false,
      content: null,
      height: 'calc(100vh - 200px)',
      list: [],
      positions: [],
      department: [],
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
      promotion_id: '' // 晋升表id
    }
  },

  mounted() {
    this.getData(1)
  },

  methods: {
    // 切换表格
    clickDepart(index, item) {
      this.tabIndex = index
      this.promotion_id = item.id
      this.title = item.name
      this.getList(item.id)
    },
    ueditorEdit(e) {
      this.content = e
    },
    getText(row) {
      let str = ''
      let arr = []
      if (row.length > 0) {
        row.map((item) => {
          let text = item.name + '(' + item.alias + ')'
          arr.push(text)
        })
        str = arr.join('、')
      } else {
        str = '--'
      }
      return str
    },
    async getData(val) {
      const result = await promotionListApi()
      this.department = result.data.list
      if (this.department.length) this.title = this.department[0].name
      if (val == 1) {
        this.department.map((item) => {
          if (item.status == 1) {
            return this.getList(item.id)
          }
        })
      }
    },
    // 获取表格数据
    async getList(id) {
      const result = await promotionDataApi({ promotion_id: id })
      this.list = result.data
    },
    getTotal(row) {
      return Number(row.basicSalary) + Number(row.performance)
    },

    // 打开晋升标准
    handleCheck(row) {
      this.content = row.standard
      this.positions = row.positions
      this.editDrawer = true
    },
    // 关闭晋升标准
    handleClose() {
      this.editDrawer = false
    }
  }
}
</script>
<style lang="scss" scoped>
.pull-left {
  font-weight: 500;
}
.icontianjia {
  font-size: 14px;
  color: #1890ff;
}
.assess-left {
  height: 100%;
  margin: -15px 0 0px -20px;
  padding: 14px 0 40px 0;
  ::v-deep .el-card__header {
    border-bottom: none;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 0;
    padding-bottom: 15px;
    button {
      justify-content: flex-end;
    }
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  overflow: auto;
  .v-height-flag {
    border-right: 1px solid #eeeeee;
  }
  .assess-left-ul {
    list-style: none;
    margin: 0;
    padding: 0;
    position: relative;

    li {
      font-size: 14px;
      padding: 14px 10px 14px 20px;
      cursor: pointer;
      font-family: PingFangSC-Regular, PingFang SC;
      font-size: 14px;
      font-weight: 400;
      color: #303133;
      height: 100%;
      .assess-left-more {
        color: #333333;
        text-align: right;
        position: absolute;
        right: 10px;
        transform: rotate(90deg);
      }
      &.active {
        background-color: rgba(24, 144, 255, 0.08);
        border-right: 2px solid #1890ff;
        color: #1890ff;
        font-weight: 600;
        .assess-left-more {
          color: #1890ff;
        }
      }
    }
  }
}
.assess-left::-webkit-scrollbar {
  width: 5px;
  height: 1px;
}
.assess-left::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #f5f5f5;
}
.assess-left::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  background: #f0f2f5;
}
.right-item {
  line-height: 25px;
  cursor: pointer;
  margin-top: 10px;
  &:first-child {
    margin-top: 0;
  }
}
.assess-right {
  padding-left: 14px;
  ::v-deep .el-table thead.is-group th {
    background-color: rgba(247, 251, 255, 1);
    border-color: #fff;
  }
  ::v-deep .el-table td {
    border: none;
  }
  .el-table {
    border: none;
  }
  .title {
    margin-top: 10px;
    font-size: 18px;
    font-weight: 500;
    color: #303133;
    font-family: PingFangSC-Semibold, PingFang SC;
    margin-bottom: 20px;
  }
}
.check-box {
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  color: #666666;
  padding: 20px;
  padding-bottom: 0;

  .content {
    ::v-deep p {
      text-indent: 2em;
      font-size: 14px;
      line-height: 1.5;
    }
    ::v-deep td {
      border: 1px solid;
    }
  }
}
</style>
