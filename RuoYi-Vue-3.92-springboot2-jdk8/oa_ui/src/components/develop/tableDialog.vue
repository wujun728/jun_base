<!-- @FileDescription: 低代码-选择一对一弹窗-->
<template>
  <div class="oa-dialog">
    <el-dialog
      top="10%"
      :visible.sync="show"
      width="700px"
      append-to-body
      :show-close="false"
      :close-on-click-modal="false"
    >
      <div slot="title" class="header flex-between">
        <span class="title">选择引用内容</span>
        <span class="el-icon-close" @click="handleClose"></span>
      </div>

      <div class="flex mb20">
        <div class="inTotal">共 {{ total }} 项</div>
        <div class="ml14">
          <el-input
            v-model="where.keyword"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入关键字搜索"
            clearable
            style="width: 250px"
            @change="getList"
            @keyup.native.stop.prevent.enter="getList"
            class="input"
          ></el-input>
        </div>
      </div>

      <div class="content table-box">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column
            v-for="(item, index) in header"
            :prop="item.field_name_en"
            :label="item.field_name"
            :key="index"
          >
            <template slot-scope="scope">
              <div v-if="item.form_value === 'image'" class="img-box">
                <img
                  v-for="(val, index) in scope.row[item.field_name_en]"
                  :key="index"
                  :src="val.url"
                  alt=""
                  class="img"
                  @click="lookViewer(val.url, val.name)"
                />
                <span v-if="!scope.row[item.field_name_en] || scope.row[item.field_name_en].length == 0">--</span>
              </div>
              <div v-else-if="item.form_value === 'input_percentage'">
                <el-progress
                  :percentage="scope.row[item.field_name_en] ? scope.row[item.field_name_en] : 0"
                ></el-progress>
              </div>
              <div v-else-if="item.form_value === 'tag' || item.form_value === 'checkbox'">
                <el-popover v-if="scope.row[item.field_name_en].length > 2" placement="top-start" trigger="hover">
                  <template>
                    <div class="flex_box">
                      <div class="tips" v-for="(val, index) in scope.row[item.field_name_en]" :key="index">
                        <el-tag size="small">
                          {{ val }}
                        </el-tag>
                      </div>
                    </div>
                  </template>
                  <div slot="reference">
                    <div class="flex_box">
                      <div class="tips" v-for="(val, index) in scope.row[item.field_name_en]" :key="index">
                        <el-tag size="small" v-if="index < 2">
                          {{ val }}
                        </el-tag>
                      </div>
                      <el-tag v-if="scope.row[item.field_name_en].length > 2" size="small">...</el-tag>
                    </div>
                  </div>
                </el-popover>
                <template v-else>
                  <div class="flex_box">
                    <div class="tips" v-for="(val, index) in scope.row[item.field_name_en]" :key="index">
                      <el-tag size="small" v-if="index < 2">
                        {{ val }}
                      </el-tag>
                    </div>
                    <el-tag v-if="scope.row[item.field_name_en].length > 2" size="small">...</el-tag>
                  </div>
                </template>
                <span v-if="scope.row[item.field_name_en] && scope.row[item.field_name_en].length == 0">--</span>
              </div>
              <div v-else-if="item.form_value === 'switch'">
                <el-switch
                  disabled
                  v-model="scope.row[item.field_name_en]"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="开启"
                  inactive-text="关闭"
                >
                </el-switch>
              </div>

              <div v-else-if="item.form_value === 'textarea'">
                <el-popover placement="top-start" width="350" trigger="hover" :content="scope.row[item.field_name_en]">
                  <div class="over-text" slot="reference" v-if="scope.row[item.field_name_en].length > 11">
                    {{ scope.row[item.field_name_en] }}
                  </div>
                </el-popover>
                <span v-if="scope.row[item.field_name_en].length <= 11"> {{ scope.row[item.field_name_en] }} </span>
                <span v-if="!scope.row[item.field_name_en]">--</span>
              </div>
              <div v-else-if="['input_number', 'input_float', 'input_price'].includes(item.form_value)">
                {{ scope.row[item.field_name_en] }}
              </div>
              <div v-else>
                {{ getValue(scope.row[item.field_name_en], item.form_value) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="text" @click="selectClick(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="flex-end mt14">
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
    </el-dialog>
  </div>
</template>

<script>
import { savecrudCateApi, dataModulerListApi, dataModulerFieldApi } from '@/api/develop'
export default {
  name: 'CrmebOaEntApplicatDialog',
  props: {},
  data() {
    return {
      rowId: 0,
      show: false,
      header: [],
      total: 0,
      where: { keyword: '', page: 1, limit: 15 },
      tableData: [],
      index_field_name: ''
    }
  },

  methods: {
    handleClose() {
      this.show = false
    },
    openBox(id) {
      this.rowId = id
      this.getHeader(id)
      this.getList(id)
      this.show = true
    },
    // 获取一对一关联字段
    getHeader(id) {
      dataModulerFieldApi(this.rowId).then((res) => {
        this.header = res.data.association_field
        this.index_field_name = res.data.index_field_name
      })
    },

    // 获取一对一关联列表
    getList(id) {
      dataModulerListApi(this.rowId, this.where).then((res) => {
        this.total = res.data.count
        this.tableData = res.data.list || []
      })
    },
    // 数组转成字符串
    getValue(val) {
      let str = ''
      if (val == '') {
        str = '--'
      } else if (type === 'input_select') {
        str = val.name
      } else if (Array.isArray(val)) {
        str = val.toString()
      } else {
        str = val
      }
      return str || '--'
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    pageChange(val) {
      this.where.page = val
      this.getList()
    },
    selectClick(row) {
      let data = {
        id: row.id,
        name: row[this.index_field_name]
      }
      this.$emit('getSelection', data)
      this.handleClose()
    },

    submit() {
      this.fromItem.forEach((item, index) => {
        item.sort = this.fromItem.length - index
      })
      const data = {
        cate: this.fromItem
      }
      savecrudCateApi(data)
        .then((res) => {
          this.handleClose()
          this.$emit('getList')
        })
        .catch((err) => {
          this.$message.error(err.message)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.flex {
  display: flex;
  align-items: center;
}
.oa-dialog {
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .el-icon-close {
      color: #c0c4cc;
      font-weight: 500;
      font-size: 14px;
    }
  }

  .content {
    max-height: calc(100vh - 420px);
    overflow-y: auto;
  }
  .content::-webkit-scrollbar {
    height: 0;
    width: 0;
  }
  .content:first-child {
    padding: 0 20px;
  }

  .vertical {
    display: flex;
    flex-direction: column;
  }
  .add-type {
    display: flex;
    justify-content: flex-start;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
  }
  ::v-deep.el-dialog {
    border-radius: 6px;
  }
  ::v-deep .el-dialog__body {
    margin-bottom: 0;
    padding: 0;
  }

  ::v-deep .el-button--medium {
    padding: 10px 15px;
  }
}
.el-icon-remove {
  margin-left: 5px;
  color: red;
}
.icontuodong {
  color: #cacdd2;
  font-size: 14px;
}
</style>
