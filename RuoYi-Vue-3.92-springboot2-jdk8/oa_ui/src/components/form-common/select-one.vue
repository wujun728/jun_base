<!-- @FileDescription: 下拉选择一对一组件 -->
<template>
  <el-popover placement="bottom-start" trigger="manual" v-model="showPopover" popper-class="popover" ref="treePopover">
    <isFullScreen @call-parent-method="handlePopoverHide">
      <div class="tree-box">
        <el-input
          v-model="where.keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入关键字搜索"
          clearable
          style="width: 100%"
          @change="getList"
          @keyup.native.stop.prevent.enter="getList"
          class="input"
        ></el-input>

        <div class="table-box mt20">
          <el-table :data="tableData" style="width: 100%" height="390px">
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
                  <el-popover
                    placement="top-start"
                    width="350"
                    trigger="hover"
                    :content="scope.row[item.field_name_en]"
                  >
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
        </div>
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
    </isFullScreen>
    <!-- 人员数据 -->
    <template slot="reference">
      <slot name="custom"></slot>
      <div class="select plan-footer-one mr10" ref="select" v-if="!isSlots" @click="handlePopoverShow">
        <span v-if="!oneData.name">{{ placeholder }}</span>
        <div class="flex-box" v-else>
          <span> {{ oneData.name }}</span>
          <span class="el-icon-circle-close" @click.stop="deleteFn"></span>
        </div>
      </div>
    </template>
  </el-popover>
</template>
<script>
import { savecrudCateApi, dataModulerListApi, dataModulerFieldApi } from '@/api/develop'
import { PopupManager } from 'element-ui/src/utils/popup'
import isFullScreen from '@/components/isFullScreen/index'

export default {
  name: '',
  components: { isFullScreen },
  props: {
    // 选中的数据
    value: {
      type: Array,
      default: () => {
        return []
      }
    },
    id: {},
    placeholder: {
      type: String,
      default: '请选择一对一'
    },
    // 只能单选一个
    onlyOne: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      rowId: 0,
      header: [],
      isSlots: false,
      total: 0,
      showPopover: false,
      where: { keyword: '', page: 1, limit: 15 },
      tableData: [],
      index_field_name: '',
      oneData: {}
    }
  },
  computed: {},
  watch: {
    value(newVal, oldValue) {
      this.oneData = newVal
    }
  },
  mounted() {
    this.maskZindex = PopupManager.nextZIndex()
    document.addEventListener('click', this.handleGlobalClick)
    this.rowId = this.id
    if (this.$slots.custom) {
      this.isSlots = true
    }
    if (this.rowId) {
      this.getHeader(this.id)
      this.getList(this.id)
    }

    if (this.value.name) {
      this.oneData = this.value
    }
  },
  methods: {
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
    getValue(val, type) {
      let str = ''
      if (val == '') {
        str = '--'
      } else if (type === 'input_select') {
        str = val.name + val.type ? '（' + val.type + '）' : ''
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
    deleteFn() {
      this.oneData = {
        id: '',
        name: ''
      }
      this.$emit('getSelection', this.oneData)
    },
    selectClick(row) {
      let data = {
        id: row.id,
        name: row[this.index_field_name]
      }
      this.oneData = data
      this.$emit('getSelection', data)
      this.$refs.treePopover.doClose()
    },
    handleGlobalClick(event) {
      if (!this.$refs.treePopover.$el.contains(event.target)) {
        this.showPopover = false
      }
    },
    handlePopoverShow() {
      this.showPopover = true
    },
    handlePopoverHide() {
      this.showPopover = false
    }
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleGlobalClick)
  }
}
</script>
<style scoped lang="scss">
.tree-box {
  min-width: 242px;
  min-height: 150px;
  position: sticky;
  padding: 24px 12px;
  z-index: 9999;
  background: #fff;
  min-width: 150px;
  border-radius: 4px;
  border: 1px solid #e6ebf5;
}
.img {
  cursor: pointer;
  display: block;
  width: 50px;
  height: 50px;
  margin-right: 4px;
  margin-bottom: 4px;
}

.plan-footer-one {
  cursor: pointer;
  -webkit-appearance: none;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  color: #c0c4cc;
  display: inline-block;
  font-size: inherit;
  min-height: 32px;
  line-height: 32px;
  outline: none;
  font-size: 13px;
  padding: 0 15px;
  -webkit-transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  width: 100%;

  .el-tag.el-tag--info {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #303133;
  }
}

.flex-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #303133;

  .el-icon-circle-close {
    color: #c0c4cc;
  }
}
.input {
  ::v-deep.el-input__validateIcon {
    display: none;
  }
}
.isChecked {
  color: #1890ff !important;
}

::v-deep .el-popper {
  margin-top: 5px;
}
</style>
<style>
.popover {
  padding: 0px !important;
}
</style>
