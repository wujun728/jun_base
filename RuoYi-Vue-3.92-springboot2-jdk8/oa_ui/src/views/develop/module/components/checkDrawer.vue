<template>
  <div>
    <el-drawer
      :visible.sync="drawer"
      direction="rtl"
      :show-close="false"
      :wrapper-closable="true"
      :append-to-body="true"
      :wrapperClosable="type == 'edit' ? false : true"
      @close="handleClose"
      :size="`55%`"
    >
      <div class="title flex-between" slot="title">
        <div class="fz15">{{ name || '查看' }}</div>
        <div class="title-right">
          <!-- <el-dropdown v-if="dropdownList && dropdownList.length > 0">
            <el-button type="primary" size="small" icon="el-icon-plus" class="mr10">新增相关</el-button>
            <el-dropdown-menu style="text-align: left">
              <el-dropdown-item v-for="item in dropdownList" :key="item.id" @click.native="dropdownSearch(item)">
                新建{{ item.table_name }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown> -->
          <el-dropdown v-if="tabIndex == 1">
            <div class="close-box mr10"><span class="el-icon-more"></span></div>
            <el-dropdown-menu style="text-align: left">
              <!-- <el-dropdown-item @click.native="editRow"> 编辑 </el-dropdown-item> -->
              <el-dropdown-item @click.native="deleteRow"> 删除 </el-dropdown-item>
              <!-- <el-dropdown-item :divided="tabIndex == 1 ? true : false" @click.native="openCheck()">
                配置新建项
              </el-dropdown-item> -->
              <!-- <el-dropdown-item @click.native="openCheck('tab')"> 配置tab显示项 </el-dropdown-item> -->
            </el-dropdown-menu>
          </el-dropdown>
          <div class="close-box no-border" @click="handleClose"><span class="el-icon-close"></span></div>
        </div>
      </div>

      <div class="titleTab" style="position: relative">
        <el-tabs v-model="tabIndex" type="border-card" tab-position="top" @tab-click="handleClick">
          <el-tab-pane label="详情" name="1"></el-tab-pane>
          <el-tab-pane
            v-for="item in tabData"
            :key="item.table_name_en"
            :label="item.table_name"
            :name="item.table_name_en"
          ></el-tab-pane>
        </el-tabs>
      </div>

      <!-- 详情 -->
      <div class="form-box" ref="formBox">
        <div class="mt50" v-if="tabIndex == 1">
          <VFormRender
            v-if="drawer"
            ref="preForm"
            :form-json="importTemplate"
            :form-data="allocation.module_info"
            :developData="developData"
            :preview-state="true"
          >
            <!--动态表格 -->
          </VFormRender>
          <!-- <div class="button from-foot-btn fix btn-shadow mt30" v-if="type === 'edit'">
          <el-button size="small" class="el-btn" @click="setForm">取消</el-button>
          <el-button size="small" type="primary" @click="saveFn()">保存</el-button>
        </div> -->
          <!-- 评论和操作日志 -->
          <div class="comment-tab" :class="info.crudInfo.show_log && info.crudInfo.show_comment ? 'border-top' : ''">
            <el-tabs v-model="activeName" @tab-click="handleCommentClick">
              <el-tab-pane
                :label="`${info.crudInfo.comment_title || '评论'}(${commentCount})`"
                name="1"
                v-if="info.crudInfo.show_comment"
              >
                <div class="comment-box">
                  <oa-comment
                    type="develop"
                    ref="oaComment"
                    :list="commentList"
                    @sendComment="sendComment"
                    @deleteReply="deleteReply"
                  ></oa-comment>
                </div>
              </el-tab-pane>
              <el-tab-pane :label="`操作日志(${logCount})`" name="2" v-if="info.crudInfo.show_log">
                <div class="comment-box">
                  <oa-log type="develop" :list="logList" :keyName="name"></oa-log>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>

        <!-- 表格 -->
        <div class="mt50 table-box" v-if="tabIndex != 1">
          <div class="flex-between mb10">
            <div class="search">
              <div class="inTotal">共 {{ total }} 项</div>
              <el-input
                v-model="where.keyword_default"
                prefix-icon="el-icon-search"
                size="small"
                placeholder="请输入关键字"
                clearable
                class="input"
                @change="getList"
              ></el-input>
            </div>
            <el-button size="small" icon="el-icon-plus" type="primary" @click="addData">新建</el-button>
          </div>

          <el-table :data="tableData" style="width: 100%" row-key="id" :key="keyVal" :height="height">
            <el-table-column
              v-for="(item, index) in tabInfo.showField"
              :prop="item.field_name_en"
              :label="item.field_name"
              :key="index"
              width="auto"
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
                <div v-else-if="item.form_value === 'tag'">
                  <el-popover
                    v-if="scope.row[item.field_name_en] && scope.row[item.field_name_en].length > 2"
                    placement="top-start"
                    trigger="hover"
                  >
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
                        <el-tag
                          v-if="scope.row[item.field_name_en] && scope.row[item.field_name_en].length > 2"
                          size="small"
                          >...</el-tag
                        >
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
                      <el-tag
                        v-if="scope.row[item.field_name_en] && scope.row[item.field_name_en].length > 2"
                        size="small"
                        >...</el-tag
                      >
                    </div>
                  </template>
                  <span v-if="!scope.row[item.field_name_en] || scope.row[item.field_name_en].length == 0">--</span>
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
                    <div
                      class="over-text"
                      slot="reference"
                      v-if="scope.row[item.field_name_en] && scope.row[item.field_name_en].length > 11"
                    >
                      {{ scope.row[item.field_name_en] }}
                    </div>
                  </el-popover>
                  <span v-if="scope.row[item.field_name_en] && scope.row[item.field_name_en].length <= 11">
                    {{ scope.row[item.field_name_en] }}
                  </span>
                  <span v-if="!scope.row[item.field_name_en]">--</span>
                </div>
                <div v-else-if="['input_number', 'input_float', 'input_price'].includes(item.form_value)">
                  {{ scope.row[item.field_name_en] }}
                </div>
                <!-- 多选 -->
                <div v-else-if="item.form_value == 'checkbox'">
                  <div
                    v-for="(val, index) in scope.row[item.field_name_en]"
                    class="dictionaries-tag over-text mr10"
                    :style="{
                      color: val.color ? val.color : '#1890ff',
                      background: val.color ? getColorFn(val.color, '0.1') : getColorFn('#1890ff', '0.1')
                    }"
                  >
                    {{ val.name }}
                  </div>
                  <div v-if="scope.row[item.field_name_en].length == 0">--</div>
                </div>
                <!-- 关联字典颜色 -->
                <div
                  v-else-if="
                    scope.row[item.field_name_en] &&
                    Object.prototype.hasOwnProperty.call(scope.row[item.field_name_en], 'color')
                  "
                  class="dictionaries-tag over-text"
                  :style="{
                    color: scope.row[item.field_name_en].color ? scope.row[item.field_name_en].color : '#1890ff',
                    background: scope.row[item.field_name_en].color
                      ? getColorFn(scope.row[item.field_name_en].color, '0.1')
                      : getColorFn('#1890ff', '0.1')
                  }"
                >
                  {{ scope.row[item.field_name_en].name }}
                </div>
                <div v-else>
                  {{ getValue(scope.row[item.field_name_en], item.form_value) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="address" label="操作" fixed="right" width="130">
              <template slot-scope="scope">
                <el-button type="text" @click="editTabsRow(scope.row)">编辑</el-button>
                <el-button type="text" @click="deleteTabsRow(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="page-fixed">
            <el-pagination
              :page-size="where.limit"
              :current-page="where.page"
              @current-change="pageChange"
              layout="total, prev, pager, next, jumper"
              :total="total"
            />
          </div>
        </div>
      </div>
    </el-drawer>

    <!-- 新增 -->
    <add-drawer ref="addDrawer" :keyName="tabKey" :crud="crud" @getList="getList"></add-drawer>
    <!-- 配置按钮/配置tab -->
    <checkbox-dialog
      ref="checkboxDialog"
      :title="title"
      :type="`field`"
      :name="`所有关联实体`"
      :showName="`已展示实体`"
      @getData="getData"
    ></checkbox-dialog>
  </div>
</template>
<script>
import { getColor } from '@/utils/format'
import VFormRender from '@/components/form-render/index'
import oaLog from '@/components/form-common/oa-log'
import oaComment from '@/components/form-common/oa-comment'
import addDrawer from '../components/addDrawer'
import checkboxDialog from '@/components/develop/checkboxDialog'
import {
  crudModuleCreateApi,
  crudModuleUpdateApi,
  crudModuleDelApi,
  crudModuleSaveApi,
  crudModuleInfoApi,
  crudModuleListApi,
  crudModuleFindApi,
  crudViewSaveApi,
  getModuleLogApi,
  getModuleCommentApi,
  saveModuleCommentApi,
  putModuleCommentApi,
  deleteModuleCommentApi
} from '@/api/develop'
export default {
  components: { VFormRender, checkboxDialog, addDrawer, oaLog, oaComment },
  props: {
    keyName: {
      type: String,
      default: ''
    },
    crud_id: {
      type: Number,
      default: 0
    },
    info: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      drawer: false,
      tabIndex: '1',
      height: `calc(100vh - 230px)`,
      activeName: '1',
      importTemplate: {
        formConfig: {},
        widgetList: []
      },
      developData: {
        data_id: 0,
        crud_name: '',
        show: '1'
      },
      name: '',
      tabKey: '',
      tabInfo: '',
      tabSave: 'tab',
      title: '',
      type: 'check',
      allocation: {
        association_table: []
      },

      crud: {
        crud_id: 0,
        crud_value: 0
      },
      logList: [],
      logCount: 0,
      commentCount: 0,
      commentList: [],
      logWhere: {
        page: 1,
        limit: 10
      },
      typelist: ['grid', 'card', 'grid-col', 'tab'],
      where: {
        page: 1,
        limit: 15,
        crud_value: 0,
        crud_id: 0,
        keyword_default: ''
      },
      total: 0,
      keyVal: 0,
      id: 0, // 当前列表的id值
      dropdownList: [],
      tabData: [],
      tableData: []
    }
  },
  created() {
    this.createForm()
  },
  mounted() {
    window.addEventListener('scroll', this.lazyLoading, true)
  },
  destroyed() {
    window.removeEventListener('scroll', this.lazyLoading)
  },
  computed: {
    getName() {
      let str = ''
      this.tabData.map((item) => {
        if (this.tabIndex == item.table_name_en) {
          str = item.table_name
        }
      })
      return str || ''
    }
  },

  watch: {
    info: {
      handler(val, oldVal) {
        if (val.crudInfo && val.crudInfo.id) {
          this.crud.crud_id = val.crudInfo.id
          this.where.crud_id = val.crudInfo.id
        }
        if (val.userOptions) {
          this.dropdownList = val.userOptions.options ? val.userOptions.options.create : []
          this.tabData = val.userOptions.options ? val.userOptions.options.tab : []
        }
      },
      deep: true
    }
  },
  methods: {
    // 滚动加载
    lazyLoading(e) {
      let document = this.$refs.formBox
      if (document) {
        let scrollTop = document.scrollTop || document.body.scrollTop
        let clientHeight = document.clientHeight
        let scrollHeight = document.scrollHeight

        if (parseInt(scrollTop + clientHeight) + 1 >= scrollHeight) {
          if (this.logCount / 10 > this.logWhere.page && this.activeName == 2) {
            this.logWhere.page = this.logWhere.page + 1
            this.getLogList()
          } else if (this.commentCount / 10 > this.logWhere.page && this.activeName == 1) {
            this.logWhere.page = this.logWhere.page + 1
            this.getCommentList()
          }
        }
      }
    },

    getColorFn(color, opacity) {
      return getColor(color, opacity)
    },

    handleCommentClick(e) {
      this.logWhere.page = 1
      if (this.activeName == 1) {
        this.commentList = []
        this.getCommentList()
      } else {
        this.logList = []
        this.getLogList()
      }
      this.$refs.oaComment.cancelComment()
    },
    // 保存评论
    sendComment(val, replyId, pid) {
      let obj = {
        pid: pid,
        comment: val
      }
      // 编辑
      if (replyId) {
        putModuleCommentApi(this.keyName, replyId, { comment: val }).then((res) => {
          this.commentList = []
          this.logWhere.page = 1
          this.getCommentList()
        })
      } else {
        saveModuleCommentApi(this.keyName, this.id, obj).then((res) => {
          this.commentList = []
          this.logWhere.page = 1
          this.getCommentList()
        })
      }
      this.$refs.oaComment.cancelComment()
    },
    // 删除评论
    deleteReply(item) {
      this.$modalSure('你确定要删除此评论吗').then(() => {
        deleteModuleCommentApi(this.keyName, item.id).then((res) => {
          this.commentList = []
          this.getCommentList()
        })
      })
    },

    addData() {
      this.$refs.addDrawer.openBox()
    },
    // 获取操作日志列表
    getLogList() {
      if (this.info.crudInfo.show_log) {
        getModuleLogApi(this.keyName, this.id, this.logWhere).then((res) => {
          this.logList = [...this.logList, ...res.data.list]
          this.logCount = res.data.count
        })
      }
    },

    // 获取评论列表
    getCommentList() {
      if (this.info.crudInfo.show_comment) {
        getModuleCommentApi(this.keyName, this.id, this.logWhere).then((res) => {
          this.$nextTick(() => {
            this.commentList = [...this.commentList, ...res.data.list]

            this.commentCount = res.data.count
          })
        })
      }
    },

    pageChange(val) {
      this.where.page = val
      this.getList()
    },

    // 数组转成字符串
    getValue(val, type) {
      let str = ''
      if (val == '') {
        str = '--'
      } else if (Array.isArray(val)) {
        str = val.toString()
      } else {
        str = val
      }
      return str || '--'
    },

    dropdownSearch(item) {
      this.tabKey = item.table_name_en
      setTimeout(() => {
        this.$refs.addDrawer.openBox()
      }, 300)
    },
    handleClick(e) {
      if (e.index != 0) {
        let index = e.index - 1
        this.tabKey = this.tabIndex
        this.getInfo()
        this.getList()
      }
      if (this.tabIndex == 1) {
        this.type = 'add'
        this.$nextTick(() => {
          this.$refs.preForm.setReadMode(true)
        })
      }
    },

    getInfo() {
      crudModuleInfoApi(this.tabKey, 0).then((res) => {
        this.tabInfo = res.data
      })
    },

    getList() {
      this.where.crud_value = this.crud.crud_value
      crudModuleListApi(this.tabKey, this.where).then((res) => {
        this.total = res.data.count
        this.tableData = res.data.list
        this.keyVal = this.keyVal + 1
      })
    },

    openCheck(type) {
      let selectList = []
      let ids = []
      if (type === 'tab') {
        this.tabSave = 'tab'
        this.title = '配置tab显示项'
        if (this.tabData && this.tabData.length > 0) {
          selectList = this.tabData
          this.tabData.map((item) => {
            ids.push(item.id)
          })
        } else {
          ids = []
        }
      } else {
        this.tabSave = 'create'
        this.title = '配置新增按钮'
        if (this.dropdownList && this.dropdownList.length > 0) {
          selectList = this.dropdownList
          this.dropdownList.map((item) => {
            ids.push(item.id)
          })
        } else {
          ids = []
        }
      }

      this.allocation.association_table.forEach((item) => {
        item.field_name = item.table_name
      })

      let obj = {
        list: this.allocation.association_table,
        ids,
        selectList
      }
      this.$refs.checkboxDialog.openBox(obj, ids)
    },

    openBox(row, data, info, name) {
      this.name = name

      if (info) {
        if (info.crudInfo && info.crudInfo.id) {
          this.crud.crud_id = info.crudInfo.id
          this.where.crud_id = info.crudInfo.id
        }

        if (info.userOptions) {
          this.dropdownList = info.userOptions.options ? info.userOptions.options.create : []
          this.tabData = info.userOptions.options ? info.userOptions.options.tab : []
        }
      }
      this.developData = {
        data_id: row.id,
        crud_name: this.keyName,
        show: '1'
      }

      this.id = row.id

      this.createForm(this.id)
      this.where.crud_value = row.id
      this.crud.crud_value = this.id
      if (data) {
        this.allocation = data
      }
      this.drawer = true
      setTimeout(() => {
        this.setForm()
      }, 300)

      this.getCommentList()
      this.getLogList()
    },

    async createForm(id) {
      let obj = {
        id: id
      }
      const res = await crudModuleCreateApi(this.keyName, obj)
      res.data.form_info.options.forEach((item) => {
        if (!this.typelist.includes(item.type)) {
          item.isShow = false
        } else if (item.type == 'grid') {
          item.cols.forEach((grid) => {
            grid.widgetList.forEach((gridItem) => {
              gridItem.isShow = false
            })
          })
        } else if (item.type == 'card') {
          item.widgetList.forEach((gridItem) => {
            gridItem.isShow = false
          })
        } else if (item.type == 'tab') {
          item.tabs.forEach((tab) => {
            tab.widgetList.forEach((gridItem) => {
              gridItem.isShow = false
            })
          })
        } else if (item.type == 'grid-col') {
          item.widgetList.forEach((gridItem) => {
            gridItem.isShow = false
          })
        }
      })

      this.$nextTick(() => {
        if (res.data.form_info.global_options) {
          this.$set(this.importTemplate, 'formConfig', res.data.form_info.global_options)
        }
        this.$set(this.importTemplate, 'widgetList', res.data.form_info.options || [])
      })
    },

    handleClose() {
      this.logWhere.page = 1
      this.logList = []
      this.commentList = []
      this.tabIndex = '1'
      this.drawer = false
      this.developData.show = '0'
      this.$emit('getList')
    },

    getData(data) {
      let obj = {}
      if (this.tabSave === 'create') {
        obj.options = {
          create: data.selectList
        }
      } else {
        obj.options = {
          tab: data.selectList
        }
      }
      if (this.crud_id > 0) {
        crudViewSaveApi(this.crud_id, obj).then((res) => {
          this.$emit('getInfo')
        })
      } else {
        crudModuleSaveApi(this.keyName, obj).then((res) => {
          this.$emit('getInfo')
        })
      }
    },

    async deleteRow() {
      await this.$modalSure('您确认要删除吗')
      await crudModuleDelApi(this.keyName, this.id)
      await this.$emit('getList')
      await this.handleClose()
    },

    async editRow() {
      this.tabIndex = '1'
      this.$refs.preForm.setReadMode(false)
      this.type = 'edit'
    },

    async editTabsRow(item) {
      const data = await crudModuleFindApi(this.tabKey, item.id)
      await this.$refs.addDrawer.openBox(item.id, data.data)
    },
    async deleteTabsRow(item) {
      await this.$modalSure('您确认要删除吗')
      await crudModuleDelApi(this.tabKey, item.id)
      await this.getList()
    },

    saveFn() {
      this.$refs['preForm']
        .getFormData()
        .then((formData) => {
          if (this.id > 0) {
            this.upladData(this.id, formData)
          }
        })
        .catch((error) => {
          this.$message.error(error)
        })
    },

    upladData(id, data) {
      this.loading = true
      crudModuleUpdateApi(this.keyName, id, data)
        .then((res) => {
          this.loading = false

          this.type = 'check'
          this.setForm()
        })
        .catch((err) => {
          this.loading = false
        })
    },

    setForm() {
      this.type = 'check'
      this.$refs.preForm.setReadMode(true)
    }
  }
}
</script>
<style scoped lang="scss">
.form-box {
  height: calc(100vh - 100px);
  overflow-y: auto;
}
.img {
  cursor: pointer;
  display: block;
  width: 50px;
  height: 50px;
  margin-right: 4px;
  margin-bottom: 4px;
}
.fz15 {
  font-size: 15px;
}
.fz30 {
  font-size: 30px;
  margin-left: 10px;
  color: #909399;
}
.title-right {
  display: flex;
  align-items: center;
  .icongengduo2 {
    font-weight: 400;
    margin: 0 10px;
  }
}
.titleTab {
  ::v-deep .el-tabs__item.is-active {
    border-right-color: transparent !important;
    border-left-color: transparent !important;
    &::after {
      content: '';
      height: 2px;
      width: 100%;
      background-color: #1890ff;
      position: absolute;
      left: 0;
      top: 0;
    }
  }
  ::v-deep .el-tabs__item {
    line-height: 40px !important;
    font-size: 14px;
  }
  ::v-deep .el-tabs__header {
    background-color: #f7fbff;
    border-bottom: none;
  }
  ::v-deep .el-tabs__nav-wrap::after {
    height: 0;
  }
  ::v-deep .el-tabs__active-bar {
    top: 0;
  }
  .el-tabs--border-card {
    height: 39px;
    position: fixed;
    top: 56px;
    width: 100%;
    z-index: 4;
    background-color: transparent;
    border: none;
    box-shadow: none;
  }
}
::v-deep .el-drawer__header {
  border-bottom: none !important;
}
.border-top {
  border-top: 1px solid #f2f6fc;
}

.comment-tab {
  ::v-deep .el-tabs__header {
    height: 54px;

    line-height: 54px;
    font-family: PingFang SC, PingFang SC;
    font-weight: 500;
    font-size: 14px;
    color: #303133;
    .el-tabs__item {
      line-height: none;
    }
  }
  .comment-box {
    min-height: 300px;
    margin-top: 20px;
  }
}
.mt50 {
  margin-top: 62px;
  padding: 0 20px;
}

.el-icon-setting {
  cursor: pointer;
  position: absolute;
  right: 30px;
  top: 10px;
  z-index: 100;
  color: #909399;
}
.search {
  display: flex;
  align-items: center;
  .inTotal {
    margin: 0;
    margin-right: 10px;
    white-space: nowrap;
  }
}
.flex-end {
  display: flex;
  justify-content: flex-end;
}
.close-box {
  cursor: pointer;
  border-radius: 4px;
  width: 30px;
  height: 30px;
  border: 1px solid #909399;
  text-align: center;
  line-height: 30px;
  color: #909399;
}
.no-border {
  border: none !important;
}
.dictionaries-tag {
  max-width: 100px;
  display: inline-block;
  margin: 0;
  box-sizing: border-box;
  height: 24px;
  padding: 0 8px;
  text-align: center;
  line-height: 24px;
  font-size: 12px;
  border-radius: 3px;
}
</style>
