<template>
  <div class="searchBox">
    <div class="flex-between mt20">
      <div class="title-16">{{ info.crudInfo ? info.crudInfo.table_name : '设计' }}列表 </div>
      <div class="lh-center">
        <el-button class="h32" icon="el-icon-plus" size="small" type="primary" @click="addData">新增</el-button>
        <div>
          <el-dropdown>
            <span class="iconfont icongengduo2 pointer"></span>
            <el-dropdown-menu style="text-align: left">
              <div v-for="(item, index) in dropdownList" :key="index" :class="index === 3 ? '' : 'border-bottom'">
                <el-dropdown-item v-for="val in item.children" @click.native="dropdownSearch(val)">
                  {{ val.label }}
                </el-dropdown-item>
              </div>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div class="flex-box mt20">
      <div class="left">
        <div>
          <!-- 视图管理 -->
          <el-popover ref="popoverType" placement="bottom" popper-class="time-popover" trigger="click" width="140">
            <div class="field-box mb0">
              <div
                v-for="(item, index) in searchTypeOptions"
                :key="index"
                :class="viewIndex == item.value ? 'field-color' : ''"
                class="view-text"
                @click="typeClick(item, index)"
              >
                <span class="over-text">{{ item.label }}</span>
                <span class="tips">系统</span>
              </div>
              <div
                v-for="(item, indexT) in viewList"
                :key="indexT + 'a'"
                :class="viewIndex == item.index ? 'field-color' : ''"
                class="view-text"
                @click="viewClick(item)"
              >
                <span class="over-text">{{ item.senior_title }}</span>
                <span class="tips">{{ item.senior_type == 0 ? '个人' : '公共' }}</span>
              </div>
            </div>
            <div class="view-text" @click="openViewBox"><span class="iconfont iconshituguanli"></span>视图管理</div>

            <div slot="reference" class="view-box">
              <span class="over-text1">{{ viewText }}</span>
              <span class="el-icon-arrow-down"></span>
            </div>
          </el-popover>
        </div>
        <div class="inTotal">共 {{ total }} 项</div>
      </div>
      <!-- 动态筛选条件 -->
      <div class="center flex">
        <formList
          ref="formList"
          :is_develp="true"
          :list="info.seniorSearch"
          :type="where.show_search_type"
          :scopeFrames="scopeFrames"
          @handleEmit="handleEmit"
          @resetSearch="resetSearch"
        ></formList>
      </div>

      <div class="right">
        <el-popover placement="bottom" trigger="click" width="117">
          <div>
            <div v-if="!['0', '1', '2'].includes(viewIndex)" class="view-item" @click="updateView">更新当前视图</div>
            <div :class="!['0', '1', '2'].includes(viewIndex) ? 'mt14' : ''" class="view-item" @click="addViewFn">
              存为新视图
            </div>
          </div>

          <div v-show="additional_search.length > 0" slot="reference" class="shitu">
            保存视图&nbsp;<span class="el-icon-arrow-down"></span>
          </div>
        </el-popover>

        <el-popover
          v-model="$store.state.business.conditionDialog"
          placement="bottom-start"
          trigger="manual"
          width="750"
        >
          <!-- 高级筛选 -->
          <div class="condition-box">
            <div class="flex-between">
              <div class="title">筛选条件</div>
              <div class="el-icon-close pointer" @click="$store.state.business.conditionDialog = false"></div>
            </div>
            <condition-dialog
              v-if="info.viewSearch && $store.state.business.conditionDialog"
              :additionalBoolean="where.view_search_boolean"
              :eventStr="`event`"
              :formArray="info.viewSearch"
              :max="9"
              @saveCondition="saveCondition"
            ></condition-dialog>
          </div>
          <div slot="reference" class="pointer mr10 text-16 el-dropdown-link" @click="onShow">
            筛选&nbsp;<span class="iconfont icona-bianzu8"></span>
            <span v-if="additional_search.length > 0" class="yuan">{{
              additional_search ? additional_search.length : 0
            }}</span>
          </div>
        </el-popover>

        <!-- 时间排序 -->
        <el-popover ref="popover" placement="bottom" popper-class="time-popover" trigger="click" width="140">
          <div class="field-box">
            <div
              v-for="(item, indexl) in filterData"
              :key="indexl + 'b'"
              :class="activeIndex == item.value ? 'field-bga' : ''"
              class="field-text"
              @click="handleClick(item)"
            >
              <span v-if="activeIndex == item.value" class="el-icon-check"></span>
              <span class="over-text">{{ item.label }}</span>
            </div>
          </div>
          <div v-for="(item, indexJ) in sortList" :key="indexJ + 'c'" class="field-text" @click="sortFn(item)">
            <span v-if="sortIndex == item.value" class="el-icon-check"></span> {{ item.name }}
          </div>
          <div slot="reference" class="text-16 paixuBox pointer">
            <span class="iconfont iconpaixu4"></span>
          </div>
        </el-popover>
      </div>

      <!-- 条件筛选弹窗/ tab设置 -->
      <checkbox-dialog
        ref="checkboxDialog"
        :max="max"
        :min="min"
        :name="name"
        :showName="showName"
        :title="title"
        :type="types"
        @getData="getData"
        @close="close"
      ></checkbox-dialog>
      <!-- 视图管理 -->
      <view-management
        ref="viewManagement"
        :keyName="keyName"
        :list="viewList"
        :search_boolean="where.view_search_boolean"
        :senior_search="additional_search"
        @getViewList="getViewList"
      ></view-management>
      <!-- 新建视图 -->
      <oa-dialog
        ref="oaDialog"
        :formConfig="formConfig"
        :formDataInit="formDataInit"
        :formRules="formRules"
        :fromData="fromData"
        @submit="submit"
      ></oa-dialog>
      <!-- 导出 -->
      <export-excel
        ref="exportExcel"
        :export-data="exportData"
        :merges="merges"
        :save-name="saveName"
        :template="true"
      />
    </div>
  </div>
</template>
<script>
import formList from './formList'
import {
  crudModuleSaveApi,
  crudViewSaveApi,
  crudSeniorListApi,
  crudModuleListApi,
  crudSeniorSaveApi,
  crudModuleInfoApi,
  moduleFramerApi
} from '@/api/develop'
import oaDialog from '@/components/form-common/dialog-form'
import Commnt from '@/components/develop/commonData'
import exportExcel from '@/components/common/exportExcel'
import viewManagement from '@/components/develop/viewManagement'
import checkboxDialog from '@/components/develop/checkboxDialog'
import conditionDialog from '@/components/develop/conditionDialog'

export default {
  name: '',
  components: {
    formList,
    oaDialog,
    checkboxDialog,
    conditionDialog,
    viewManagement,
    exportExcel
  },
  props: {
    total: {
      type: Number,
      default: 0
    },
    id: {
      type: Number,
      default: 0
    },
    info: {
      type: Object,
      default: () => {}
    },
    type: {
      type: String,
      default: ''
    },
    keyName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      name: '所有字段',
      showName: '已展示字段',
      isShow: false,
      viewShow: false,
      types:this.type,
      searchTypeOptions: Commnt.searchTypeOptions,
      title: '',
      max: 0,
      min: 0,
      sortIndex: '',
      viewText: '我查看的',
      viewIndex: '0',
      saveName: '',
      exportData: {
        data: [],
        cols: [{ wpx: 70 }, { wpx: 70 }, { wpx: 120 }, { wpx: 140 }, { wpx: 120 }]
      },
      merges: [{ s: { r: 0, c: 0 }, e: { r: 0, c: 10 } }],
      fromData: {
        width: '500px',
        title: '新建视图',
        btnText: '确定',
        labelWidth: '100px',
        type: ''
      },
      formDataInit: {
        senior_title: '',
        senior_type: '0'
      },
      formConfig: [
        {
          type: 'input',
          label: '视图名称：',
          placeholder: '请输入视图名称(10个字以内)',
          key: 'senior_title'
        },
        {
          type: 'radio',
          label: '视图类型：',
          placeholder: '请选择视图类型',
          key: 'senior_type',
          options: [
            {
              value: '个人',
              label: '0'
            },
            {
              value: '公共',
              label: '1'
            }
          ]
        }
      ],
      formRules: {
        senior_title: [
          {
            required: true,
            message: '请输入视图名称',
            trigger: 'blur'
          },
          { min: 0, max: 10, message: '最多输入10个字', trigger: 'blur' }
        ],
        senior_type: [
          {
            required: true,
            message: '请选择视图类型',
            trigger: 'change'
          }
        ]
      },
      sortList: [
        {
          name: '升序',
          value: '1'
        },
        {
          name: '降序',
          value: '0'
        },
        {
          name: '默认排序',
          value: ''
        }
      ],
      dropdownList: [
        {
          label: '操作',
          children: [
            {
              label: '批量共享协作',
              value: 7
            },
            {
              label: '批量移交',
              value: 8
            },
            {
              label: '批量删除',
              value: 1
            }
          ]
        },
        {
          children: [
            {
              label: '邀请填写',
              value: 9
            },
            {
              label: '邀请链接记录',
              value: 10
            }
          ]
        },
        {
          children: [
            {
              label: '数据导入',
              value: 5
            },
            {
              label: '数据导出',
              value: 4
            }
          ]
        },
        {
          children: [
            {
              label: '筛选条件设置',
              value: 2
            },
            {
              label: '表头显示设置',
              value: 3
            },
            {
              label: '详情tab设置',
              value: 6
            }
          ]
        }
      ],
      scopeFrames: [],
      where: {
        show_search_type: '0',
        view_search: [],
        order_by: {},
        scope_frame: 'all',
        view_search_boolean: ''
      },
      viewList: [],
      viewItem: {},
      activeIds: [],
      activeIndex: 0,
      searchType: 0,
      filterData: [],
      additional_search: [],
      additional_search_boolean: ''
    }
  },

  watch: {
    info: {
      handler(val) {
        if (val && val.orderByField) {
          this.saveName = '导出' + val.crudInfo.table_name + '.xlsx'
          this.filterData = val.orderByField.map((data) => {
            return {
              label: data.field_name,
              value: data.field_name_en,
              id: data.id || null,
              sortBy: 0,
              isChecked: false
            }
          })
        }
      },
      deep: true
    },
    // 给条件设置组装数据
    additional_search(val) {
      this.where.view_search = []
      val.map((item) => {
        let obj = {
          operator: item.value,
          form_field_uniqid: item.field,
          value: item.option,
          obj: item
        }
        if (!item.option && item.category === 2) {
          obj.value = []
          item.options.userList.map((i) => {
            obj.value.push(i.value)
          })
        } else if (!item.option && item.category === 1) {
          obj.value = []
          item.options.depList.map((i) => {
            obj.value.push(i.id)
          })
        }
        if (obj.operator === 'between') {
          if (item.type == 'date_time_picker' || item.type == 'date_picker') {
            let data = item.option[0] + '-' + item.option[1]
            obj.value = data
          } else {
            let data = {
              min: item.min,
              max: item.max
            }
            obj.value = data
          }
        }
        this.where.view_search.push(obj)
      })
      this.$emit('confirmData', this.where)
    },
    additional_search_boolean(val) {
      this.where.view_search_boolean = val
    }
  },

  mounted() {
    this.getScopeFrame()
    this.getViewList()
    this.confirmData()
  },

  methods: {
    addViewFn() {
      this.$refs.oaDialog.openBox()
      this.viewShow = false
    },
    // 管理范围数据
    getScopeFrame() {
      moduleFramerApi(this.keyName).then((res) => {
        this.scopeFrames = res.data
      })
    },

    // 新建视图
    submit(data) {
      data.sort = 0
      data.search_boolean = this.where.view_search_boolean
      data.senior_search = this.additional_search
      crudSeniorSaveApi(this.keyName, data).then((res) => {
        if (res.status == 200) {
          this.getViewList()
          this.$refs.oaDialog.handleClose()
        }
      })
    },

    // 获取实体管理数据
    getViewList(title) {
      let data = {
        title: title || ''
      }
      crudSeniorListApi(this.keyName, data).then((res) => {
        this.viewList = res.data
        this.viewList.forEach((item, index) => {
          item.index = index + 'view'
        })
      })
    },

    updateView() {
      let obj = {
        sort: 0,
        search_boolean: this.where.view_search_boolean,
        senior_search: this.additional_search,
        senior_title: this.viewItem.senior_title,
        senior_type: this.viewItem.senior_type,
        id: this.viewItem.id
      }
      crudSeniorSaveApi(this.keyName, obj).then((res) => {
        this.getViewList()
        this.viewShow = false
      })
    },

    openViewBox() {
      this.$refs.viewManagement.openBox()
    },

    viewClick(item) {
      this.viewIndex = item.index
      this.viewItem = item
      this.viewText = item.senior_title
      this.where.view_search_boolean = item.search_boolean
      this.additional_search = item.senior_search
      this.$refs.popoverType.doClose()
    },

    typeClick(item) {
      this.viewIndex = item.value
      this.viewText = item.label
      this.where.show_search_type = item.value
      this.confirmData()
      this.additional_search = []

      this.$refs.popoverType.doClose()
    },

    onShow() {
      let list = []
      if (this.where.view_search && this.where.view_search.length > 0) {
        this.where.view_search.map((item) => {
          list.push(item.obj)
        })
      } else {
        list = []
      }
      let data = {
        list,
        type: '',
        additional_search_boolean: this.where.view_search_boolean
      }
      if (this.where.view_search_boolean == '') {
        this.where.view_search_boolean = '1'
      }

      this.$store.commit('uadatefieldOptions', data)
      this.$store.commit('updateConditionDialog', true)
    },

    saveCondition() {
      this.additional_search = this.$store.state.business.fieldOptions.list
      this.where.view_search_boolean = this.$store.state.business.fieldOptions.additional_search_boolean
    },

    // 新增
    addData() {
      this.$emit('addData')
    },

    dropdownSearch(val) {
  
      if (val.value === 2) {
    
        this.max = 2
        this.min = 0
        this.title = '搜索显示设置'
        this.searchType = 2
        if (this.info.crudInfo.customField) {
          this.info.crudInfo.customField = this.info.crudInfo.customField.filter((item) => {
            return !['file', 'image'].includes(item.form_value)
          })
        }

        this.$refs.checkboxDialog.openBox(this.info.crudInfo, this.info.seniorSearch)
      } else if (val.value === 3) {
       
        this.max = 0
        this.min = 5
        this.title = '表头显示设置'
        this.searchType = 3
        this.info.crudInfo.customField = this.info.crudInfo.customField.filter((item) => {
          return !['file', 'rich_text'].includes(item.form_value)
        })
        this.$refs.checkboxDialog.openBox(this.info.crudInfo, this.info.showField)
      } else if (val.value === 1) {
        this.$emit('handleDelete')
      } else if (val.value == 4) {
        // 导出
        if (this.total > 1000) {
          return this.$message.error('超出限制，最大支持导出1000条数据')
        }
        this.where.page = 0
        this.where.limit = 1000
        this.where.is_field_all = 1
        let list = []
        let aoaData = []
        let arr = []
        let headerArr = []
        let objData = {
          is_field_all: 1
        }
        this.exportData.rows = [{ hpt: 30 }, { hpx: 30 }]
        let info = {}
        crudModuleInfoApi(this.keyName, 0, objData).then((res) => {
          info = res.data
          info.showField.map((item) => {
            arr.push(item.field_name)
            headerArr.push(item.field_name_en)
          })
          aoaData[0] = [
            `注意：富文本及图片不支持导入，省市区这种级联使用/隔开;
例如：陕西省/西安市/西咸新区；复选字段，多个选项之间用英文逗号隔开;
例如：重点客户,A级;
日期之间使用连接线隔开，时间之间用冒号隔开，例如：2024-08-10 13:14:16`
          ]
          aoaData[1] = headerArr
          aoaData[2] = arr
          crudModuleListApi(this.keyName, this.where).then((res) => {
            list = res.data.list

            if (list.length > 0) {
              list.forEach((el) => {
                let values = []
                headerArr.forEach((item) => {
                  values.push(el[item])
                })
                let elData = this.getVal(values)
                aoaData.push(elData)
              })
            }

            this.exportData.data = aoaData
            this.$refs.exportExcel.exportExcel()
          })
        })
      } else if (val.value == 5) {
        this.$emit('confirmData', 'import')
      } else if (val.value == 6) {
        // 详情tab设置
        this.types = 'field'
        this.name = '所有关联实体'
        this.showName = '已展示实体'
        this.min = 0
        let selectList = []
        let ids = []
        this.title = '配置tab显示项'
        let tabData = this.info.userOptions.options
        if (tabData && tabData.tab && tabData.tab.length > 0) {
          selectList = tabData.tab
          tabData.tab.map((item) => {
            ids.push(item.id)
          })
        } else {
          ids = []
        }
        this.info.associationTable.forEach((item) => {
          item.field_name = item.table_name
        })
        let obj = {
          list: this.info.associationTable,
          ids,
          selectList
        }

        setTimeout(() => {
          this.$refs.checkboxDialog.openBox(obj, ids)
        }, 300)
      } else if (val.value == 7) {
        // 移交
        this.$emit('handleDropdown', 'share')
      } else if (val.value == 8) {
        // 移交
        this.$emit('handleDropdown', 'transfer')
      } else if (val.value == 9) {
        // 邀请填写
        this.$emit('handleDropdown', 'fillIn')
      } else if (val.value == 10) {
        // 邀请填写
        this.$emit('handleDropdown', 'record')
      }
    },

    getVal(list) {
      let str = []
      list.map((val) => {
        if (val == '') {
          val = '--'
        } else if (Array.isArray(val) && typeof val[0] === 'object' && val[0] !== null) {
          let arr = []
          val.map((item) => {
            arr.push(item.name)
          })
          val = arr.join('、')
          val = val.toString()
        } else if (Array.isArray(val) && !val[0].name) {
          val = val.toString()
        } else if (typeof val === 'object' && val !== null) {
          val = val.name
        } else {
          val = val || '--'
        }
        str.push(val)
      })
      return str
    },

    handleEmit(data) {
      this.where = { ...this.where, ...data }
      this.$emit('confirmData', this.where, this.keyName)
    },

    confirmData() {
      if (this.where.show_search_type != 0) {
        this.where.scope_frame = 'all'
      }
      this.$emit('confirmData', this.where)
    },

    // 点击排序
    handleClick(data) {
      this.activeIndex = data.value
      this.where.order_by = {}
      if (this.sortIndex == '') {
        this.where.order_by = {}
      } else {
        this.where.order_by[data.value] = this.sortIndex
      }
      this.$emit('confirmData', this.where)
    },
    sortFn(item) {
      this.sortIndex = item.value
      this.where.order_by = {}
      if (item.value == '') {
        this.where.order_by = {}
      } else {
        this.where.order_by[this.activeIndex] = item.value
      }
      this.$emit('confirmData', this.where)
    },
    close(){
      this.types =this.type
    },

    getData(data) {
      let obj = {}
      if (this.searchType == 2) {
        obj.senior_search = data.ids
      } else if (this.searchType == 3) {
        obj.show_field = data.ids
      }
      if (this.type === 'view') {
        crudViewSaveApi(this.id, obj).then((res) => {
          this.$emit('getInfo')
          this.$emit('getList')
        })
      } else if (this.type == 'field') {
        obj.options = {
          tab: data.selectList
        }
        if (this.id) {
          crudViewSaveApi(this.id, obj).then((res) => {
            this.$emit('getInfo')
          })
        } else {
          crudModuleSaveApi(this.keyName, obj).then((res) => {
            this.$emit('getInfo')
          })
        }
      } else {
        crudModuleSaveApi(this.keyName, obj).then((res) => {
          this.$emit('getInfo')
          this.$emit('getList')
        })
      }
      this.types =this.type
    },

    resetSearch() {
      this.where = {
        show_search_type: '0',
        view_search: [],
        order_by: {},
        scope_frame: 'all',
        view_search_boolean: '',
        keyword_default: ''
      }
      this.viewText = '我查看的'
      this.additional_search = []
      let data = {
        list: [],
        type: '',
        additional_search_boolean: '1'
      }
      this.$store.commit('uadatefieldOptions', data)
      if (this.info.seniorSearch.length == 0) {
        this.$emit('confirmData', this.where)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.title {
  font-size: 16px;
  font-weight: 500;
  display: flex;
}

.fz30 {
  font-size: 30px;
  margin-left: 10px;
  color: #909399;
}
.border-bottom {
  margin-bottom: 10px;
  padding-bottom: 10px;
  font-size: 14px;
  border-bottom: 1px solid #dfe1e7;
}

.icongengduo2 {
  margin-left: 10px;
  color: #909399;
  font-size: 32px !important;
}
.ml14 {
  margin-left: 14px;
}
.inTotal {
  margin: 0 10px 0 10px;
}
.field-box {
  margin-top: 8px;
  border-bottom: 1px solid #f5f5f5;
  margin-bottom: 8px;
}
.flex-box {
  width: 100%;
  display: flex;
  .left {
    display: flex;
    align-items: baseline;
    min-width: 170px;
  }
  .right {
    display: flex;

    .shitu {
      cursor: pointer;
      width: 85px;
      height: 33px;
      text-align: center;
      line-height: 33px;
      border-radius: 4px;
      border: 1px solid #1890ff;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 13px;
      color: #1890ff;
    }

    .yuan {
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 12px;
      color: #909399;
    }
    .icona-bianzu8 {
      color: #999999;
      font-size: 13px;
    }
    .iconpaixu4 {
      color: #999999;
      font-size: 13px;
      margin-top: 4px;
    }
    .el-dropdown-link {
      height: 33px;
      padding: 0 10px;
      line-height: 33px;
      border: 1px solid #fff;
    }
  }
  .el-dropdown-link:hover {
    background: #f3f3f3;
    // border: 1px solid #1890ff;
  }
  .paixuBox {
    width: 25px;
    height: 33px;
    line-height: 33px;
    border: 1px solid #fff;
  }
  .paixuBox:hover {
    background: #f3f3f3;
    // border: 1px solid #1890ff;
  }
  .center {
    flex: 1;
  }
}
.grey-bga {
  ::v-deep .el-input__inner {
    border: none;
    background: #f0f2f5;
  }
}

.paixuBox {
  width: 25px;
  height: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.paixuBox:hover {
  background: #f7f7f7;
}
.field-text {
  cursor: pointer;
  height: 32px;
  width: 100%;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #303133;
  line-height: 32px;
  padding-right: 15px;
  padding-left: 29px;
  position: relative;
}
.field-text:hover {
  background-color: #f2f3f5;
}
.view-text {
  cursor: pointer;
  height: 32px;
  width: 100%;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #303133;
  line-height: 32px;
  padding-right: 16px;
  padding-left: 12px;
  position: relative;
  display: flex;
  align-items: center;
  .tips {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 12px;
    color: #909399;
  }
  .iconshituguanli {
    font-size: 12px;
    color: #909399;
    margin-right: 4px;
  }
}
.view-text:hover {
  background-color: #f2f3f5;
}
.mb0 {
  margin-bottom: 0;
}

.field-bga {
  color: #1890ff;
  background: rgba(24, 144, 255, 0.07);
}
.field-color {
  color: #1890ff !important;
}
.el-icon-check {
  position: absolute;
  left: 10px;
  top: 11px;
}
.prompt-bag {
  background-color: #edf5ff;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #606266;
}

.condition-box {
  padding: 8px;
  max-height: 500px;
  overflow-y: auto;
  .flex-between {
    display: flex;
    // border-bottom: 1px solid hsl(223, 13%, 89%);
    padding-bottom: 15px;
  }
  .title {
    font-size: 14px;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 500;
    color: #333333;
  }
}
.condition-box::-webkit-scrollbar {
  height: 0;
  width: 0;
}
.over-text {
  display: inline-block;
  width: 90px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.view-item {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #303133;
}
</style>
<style>
.time-popover {
  padding: 0;
}
.monitor-yt-popover {
  background: #edf5ff;
  border: 1px solid #97c3ff;
  padding: 11px 15px 0px 15px;
}
.view-box {
  cursor: pointer;
  padding: 0 10px;
  width: 100px;
  height: 32px;
  background: #f7f7f7;
  border-radius: 4px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #1e2128;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.el-icon-arrow-down {
  /* margin-left: 6px; */
}
</style>
