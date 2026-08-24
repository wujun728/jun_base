<template>
  <div class="divBox">
    <el-card class="work-card">
      <div>
        <el-row :gutter="14">
          <el-col v-bind="gridl">
            <div class="assess-left">
              <ul class="assess-left-ul">
                <li
                  v-for="(item, index) in department"
                  :key="index"
                  :class="index == tabIndex ? 'active' : ''"
                  @click="clickDepart(index, item.id)"
                >
                  <span>{{ item.name }}</span>
                </li>
              </ul>
            </div>
          </el-col>
          <el-col v-bind="gridr">
            <div class="assess-right v-height-flag">
              <div class="mb20">{{ tabName }}</div>
              <div>
                <div v-height v-if="tabId !== 3">
                  <el-scrollbar style="height: 100%">
                    <draggable
                      v-model="dataArray"
                      chosen-class="chosen"
                      force-fallback="true"
                      group="people"
                      animation="1000"
                      @start="onStart"
                      @end="onEnd"
                    >
                      <transition-group>
                        <div v-for="(item, index) in dataArray" :key="index" class="item-list">
                          <i class="icon iconfont icontuodong item-drag"></i>
                          <el-input
                            v-model="item.value.title"
                            clearables
                            :minlength="2"
                            :maxlength="16"
                            show-word-limit
                            :placeholder="$t('customer.placeholder06')"
                          />
                          <i class="el-icon-remove item-remove" @click="handleDelete(index, item.id)"></i>
                        </div>
                      </transition-group>
                    </draggable>
                    <el-button class="add-type mt14" type="text" @click="handleAddType"
                      >{{ $t('customer.addmodel') }} <i class="el-icon-plus"></i
                    ></el-button>
                  </el-scrollbar>
                </div>

                <!--发票 -->
                <div class="v-height-flag" v-if="tabName == '发票类目'">
                  <el-button type="primary" class="mb14" size="small" @click="addFinance">{{
                    $t('customer.addtype')
                  }}</el-button>
                  <div class="v-height-flag table-box">
                    <div v-height>
                      <el-table
                        :data="tableData"
                        style="width: 100%"
                        row-key="id"
                        default-expand-all
                        :tree-props="{ children: 'children' }"
                      >
                        <el-table-column prop="name" :label="$t('customer.typename')" min-width="220">
                        </el-table-column>
                        <el-table-column prop="sort" :label="$t('toptable.sort')" min-width="100" />
                        <el-table-column prop="address" :label="$t('public.operation')" width="200">
                          <template slot-scope="scope">
                            <el-button type="text" @click="handleEdit(scope.row)">{{ $t('public.edit') }}</el-button>

                            <el-button type="text" @click="deleteFn(scope.row)">{{ $t('public.delete') }}</el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
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
              </div>
            </div>
          </el-col>
        </el-row>
        <div class="cr-bottom-button">
          <el-button
            type="primary"
            size="small"
            v-hasPermi="['setup:work:add']"
            :loading="loading"
            @click="handleConfirm"
            >{{ $t('public.ok') }}</el-button
          >
        </div>
      </div>
    </el-card>
    <dialog-form ref="repeatDialog" :repeat-data="repeatData" @isOk="getTableData()" />
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import dialogForm from './type/components/addDialog'
import { clientConfigListApi, clientConfigSaveApi, invoiceCategoryList, deleteInvoiceCategory } from '@/api/enterprise'
export default {
  name: 'SetupWork',
  components: {
    draggable,
    dialogForm
  },
  data() {
    return {
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
      drawer: false,
      tableData: [],
      repeatData: {},
      where: {
        page: 1,
        limit: 15
      },
      total: 0,
      department: [
        { id: 1, name: this.$t('customer.customersource') },
        { id: 2, name: this.$t('customer.renewaltype') }
      ],
      tabIndex: 0,
      tabId: 1,
      tabName: this.$t('customer.customersource'),
      drag: false,
      dataArray: [],
      deleteArray: [],
      loading: false
    }
  },
  watch: {
    lang() {
      this.setOptions()
    }
  },
  created() {
    this.getClientList()
  },
  methods: {
    setOptions() {
      this.department = [
        { id: 1, name: this.$t('customer.customersource') },
        { id: 2, name: this.$t('customer.renewaltype') }
      ]
    },

    // 发票类目列表
    invoiceCategoryList() {
      let data = {
        page: this.where.page,
        limit: this.where.limit
      }
      invoiceCategoryList(data).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },

    getTableData() {
      this.invoiceCategoryList()
    },

    // 编辑分类
    async handleEdit(item) {
      this.repeatData = {
        title: this.$t('customer.edittype'),
        width: '480px',
        label: 3,
        type: 2,
        data: item
      }
      this.$refs.repeatDialog.dialogVisible = true
    },

    // 删除发票分类
    async deleteFn(item) {
      await this.$modalSure(this.$t('customer.message01'))
      await deleteInvoiceCategory(item.id)

      this.getTableData()
    },

    // 列表
    getClientList() {
      const key = this.tabId === 1 ? 'way' : 'renew'
      clientConfigListApi({ key }).then((res) => {
        this.dataArray = res.data === undefined ? [] : res.data
      })
    },
    // 添加分类
    async addFinance() {
      this.repeatData = {
        title: this.$t('customer.addtype'),
        width: '480px',
        label: 3,
        type: 1,
        data: []
      }
      this.$refs.repeatDialog.dialogVisible = true
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    clickDepart(index, id) {
      this.tabIndex = index
      this.tabId = id
      if (this.tabId == 1) {
        this.tabName = this.$t('customer.customersource')
      } else if (this.tabId == 2) {
        this.tabName = this.$t('customer.renewaltype')
      } else {
        this.tabName = '发票类目'
        this.invoiceCategoryList()
      }
      this.getClientList()
    },
    onStart() {
      this.drag = true
    },
    onEnd() {
      this.drag = false
    },
    handleConfirm() {
      var data = []
      if (this.dataArray.length <= 0) {
        this.$message.warning(this.$t('customer.message05'))
      } else {
        const status = this.dataArray.some((el, index) => {
          return el.value.title === ''
        })
        if (status) {
          this.$message.warning(this.$t('customer.message05'))
        } else {
          const len = this.dataArray.length
          this.dataArray.map((value, index) => {
            data.push({ title: value.value.title, id: value.id, sort: len - index + 1 })
          })

          const key = this.tabId === 1 ? 'way' : 'renew'
          this.clientConfigSave({ delete: this.deleteArray, data, key })
        }
      }
    },
    handleAddType() {
      if (this.dataArray.length > 0) {
        const status = this.dataArray.some((el, index) => {
          return el.value.title === ''
        })

        if (status) {
          this.$message.warning(this.$t('customer.message05'))
        } else {
          this.dataArray.push({ sort: '', id: 0, value: { title: '' } })
        }
      } else {
        this.dataArray.push({ sort: '', id: 0, value: { title: '' } })
      }
    },
    handleDelete(index, id) {
      if (id !== 0) {
        this.deleteArray.push(id)
      }
      this.dataArray.splice(index, 1)
    },
    // 保存企业设置
    clientConfigSave(data) {
      this.loading = true
      clientConfigSaveApi(data)
        .then((res) => {
          this.getClientList()
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  margin-bottom: 0;
  padding-bottom: 0;
}
.work-card {
  height: calc(100vh - 14px);
  position: relative;

  .cr-bottom-button {
    position: fixed;
    left: -20px;
    right: 0;
    bottom: 0;
    width: calc(100% + 220px);
  }
}
.el-row {
  height: 100%;
  .el-col {
    height: 100%;
  }
}
.assess-left {
  height: 800px;
  border-right: 1px solid #eeeeee;
  overflow: auto;
  .assess-left-ul {
    list-style: none;
    margin: 0;
    padding: 0;
    position: relative;
    li {
      font-size: 14px;
      height: 40px;
      line-height: 40px;
      padding-left: 24px;
      padding-right: 15px;

      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #303133;
      cursor: pointer;
      .assess-left-icon {
        color: #aaaaaa;
        font-size: 13px;
      }
      .assess-left-more {
        color: #333333;
        text-align: right;
        position: absolute;
        right: 10px;
        transform: rotate(90deg);
      }
    }
    li.active {
      width: 100%;
      background-color: #f0fafe;
      border-right: 3px solid #1890ff;
      color: #1890ff;
      .assess-left-icon {
        color: #1890ff;
      }
    }
  }
}
.assess-right {
  position: relative;
  height: 100%;
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .tab-share {
    margin: -10px 0px 0 -14px;
    ::v-deep .el-tabs__nav {
      padding: 0 20px;
    }
    ::v-deep .el-tabs__nav-wrap::after {
      height: 0;
    }
  }
  .add-type {
    margin-left: 28px;
  }
  .item-list {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    i {
      font-size: 18px;
    }
    width: 40%;
    .item-drag {
      padding-right: 10px;
      color: #dddddd;
    }
    .item-remove {
      color: #f5222d;
      padding-left: 10px;
    }
  }
  .item-list:last-of-type {
    margin-bottom: 0;
  }
  .fix {
    left: -20px;
    right: -20px;
    bottom: -20px;
    width: calc(100% + 40px);
  }
  .from-foot-btn button {
    height: auto;
  }
}
::v-deep .el-card__body {
  padding-left: 0;
}
</style>
