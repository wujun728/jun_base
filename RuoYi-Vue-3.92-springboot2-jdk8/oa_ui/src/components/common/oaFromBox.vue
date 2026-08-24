<!-- 
  @FileDescription: 全局筛选组件
  功能：提供统一的筛选、排序、新增等功能
-->
<template>
  <div>
    <!-- 头部区域 -->
    <div v-if="showHeader" class="header-16 mb20">
      <div class="title-16" @click="backFn">
        <span v-if="isBack" class="el-icon-arrow-left pointer"></span> 
        {{ title }}
        <!-- 提示信息 -->
        <el-popover placement="right" popper-class="monitor-yt-popover" trigger="hover">
          <div class="prompt-bag">{{ alert }}</div>
          <i v-if="alert" slot="reference" class="el-icon-question"></i>
        </el-popover>
        <slot name="title"></slot>
      </div>
      
      <!-- 右侧按钮区域 -->
      <div class="flex lh-center">
        <slot name="rightBtn"></slot>
        
        <!-- 新增/导出按钮 -->
        <el-button
          v-if="isAddBtn && btnType !== 'default'"
          :icon="btnIcon ? 'el-icon-plus' : ''"
          :loading="loading"
          :type="btnText == '导出' ? '' : 'primary'"
          class="h32"
          size="small"
          @click="addDataFn()"
        >
          {{ btnText }}
        </el-button>
        
        <!-- 默认按钮 -->
        <el-button 
          v-if="btnType === 'default'" 
          class="h32" 
          size="small" 
          @click="addDataFn()"
        >
          {{ btnText }}
        </el-button>
        
        <!-- 下拉菜单 -->
        <div v-if="dropdownList.length > 0">
          <el-dropdown>
            <span class="iconfont icongengduo2 pointer ml10"></span>
            <el-dropdown-menu style="text-align: left">
              <el-dropdown-item 
                v-for="item in dropdownList" 
                :key="item.value" 
                @click.native="dropdownSearch(item)"
              >
                <span v-if="item.label !== '设置标签'">{{ item.label }}</span>
                <select-label
                  v-else
                  ref="selectLabel"
                  :ids="ids"
                  :props="{ children: 'children', label: 'name' }"
                  :slotType="`customer`"
                  @handleLabelConf="handleLabelConf($event, item)"
                />
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search flex lh-center">
      <div class="flex lh-center">
        <!-- 树形选择器 -->
        <el-select
          v-if="treeData.length"
          v-model="treeValue"
          class="grey-bga mr10"
          placeholder="请选择"
          popper-class="tree-select"
          size="small"
          style="width: 120px"
          @change="treeChange"
        >
          <el-option-group 
            v-for="group in treeData" 
            :key="group.label || group.id" 
            :label="group.label"
          >
            <el-option 
              v-for="item in group.options" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value"
            />
          </el-option-group>
        </el-select>
        
        <!-- 总数显示 -->
        <div v-if="isTotal" class="total-16">共 {{ total }} 条</div>
        
        <!-- 表单筛选 -->
        <formList
          v-if="seniorSearch.length > 0"
          ref="formList"
          :isTimeArray="false"
          :list="seniorSearch"
          :timeValue="timeVal"
          :type="type"
          @handleEmit="handleEmit"
          @resetSearch="resetSearch"
        />
      </div>
      
      <!-- 右侧操作区域 -->
      <div class="right">
        <!-- 高级筛选 -->
        <el-popover
          v-model="$store.state.business.conditionDialog"
          placement="bottom-start"
          popper-class="condition-popover"
          trigger="manual"
          width="750"
        >
          <div class="condition-box">
            <div class="flex-between">
              <div class="title">筛选条件</div>
              <div 
                class="el-icon-close pointer" 
                @click="$store.state.business.conditionDialog = false"
              />
            </div>
            <condition-dialog
              v-if="$store.state.business.conditionDialog"
              :eventStr="`event`"
              :formArray="viewSearch"
              :max="9"
              :noRule="false"
              @saveCondition="saveCondition"
            />
          </div>
          <div 
            v-if="isViewSearch" 
            slot="reference" 
            class="pointer text-16 el-dropdown-link" 
            @click="onShow"
          >
            筛选&nbsp;<span class="iconfont icona-bianzu8"></span>
            <span v-if="additional_search.length > 0" class="yuan">
              {{ additional_search ? additional_search.length : 0 }}
            </span>
          </div>
        </el-popover>

        <!-- 时间排序 -->
        <el-popover 
          ref="popover" 
          placement="bottom" 
          popper-class="time-popover" 
          trigger="click" 
          width="140"
        >
          <div class="field-box">
            <div
              v-for="(item, index) in timeSearch"
              :key="index"
              :class="activeIndex == item.value ? 'field-bga' : ''"
              class="field-text"
              @click="handleClick(item, index)"
            >
              <span v-if="activeIndex == item.value" class="el-icon-check"></span>
              <span class="over-text">{{ item.name }}</span>
            </div>
          </div>
          <div class="field-box">
            <div
              v-for="(item, index) in sortList"
              :key="index"
              :class="sortIndex == item.value ? 'field-bga' : ''"
              class="field-text"
              @click="sortFn(item, index)"
            >
              <span v-if="sortIndex == item.value" class="el-icon-check"></span> 
              {{ item.name }}
            </div>
          </div>

          <div 
            v-if="sortSearch" 
            slot="reference" 
            class="text-16 paixuBox pointer"
          >
            <span class="iconfont iconpaixu4"></span>
          </div>
        </el-popover>
      </div>
    </div>
  </div>
</template>

<script>
import formList from '@/views/develop/module/components/formList'
import conditionDialog from '@/components/develop/conditionDialog'
import selectLabel from '@/components/form-common/select-label'

export default {
  name: 'OaFromBox',
  components: { 
    formList, 
    conditionDialog, 
    selectLabel 
  },
  
  props: {
    // 组件类型
    type: {
      type: String,
      default: ''
    },
    
    // 是否显示返回按钮
    isBack: {
      type: Boolean,
      default: false
    },
    
    // 树形数据
    treeData: {
      type: Array,
      default: () => []
    },
    
    // 树形默认值
    treeDefault: {
      type: [String, Number],
      default: ''
    },
    
    // 下拉菜单列表
    dropdownList: {
      type: Array,
      default: () => []
    },
    
    // 总数
    total: {
      type: Number,
      default: 0
    },
    
    // 搜索条件
    search: {
      type: Array,
      default: () => []
    },
    
    // 多选ID集合
    ids: {
      type: Array,
      default: () => []
    },
    
    // 高级筛选条件
    viewSearch: {
      type: Array,
      default: () => []
    },
    
    // 时间区间
    timeVal: {
      type: [Array, String],
      default: () => []
    },
    
    // 是否显示高级筛选
    isViewSearch: {
      type: Boolean,
      default: true
    },
    
    // 是否显示排序
    sortSearch: {
      type: Boolean,
      default: true
    },
    
    // 是否显示新增按钮
    isAddBtn: {
      type: Boolean,
      default: true
    },
    
    // 标题
    title: {
      type: String,
      default: ''
    },
    
    // 按钮文字
    btnText: {
      type: String,
      default: '新增'
    },
    
    // 是否显示按钮图标
    btnIcon: {
      type: Boolean,
      default: true
    },
    
    // 按钮类型
    btnType: {
      type: String,
      default: ''
    },
    
    // 提示信息
    alert: {
      type: String,
      default: ''
    },
    
    // 是否显示总数
    isTotal: {
      type: Boolean,
      default: true
    }
  },
  
  data() {
    return {
      // 筛选条件
      where: {
        sort_field: 'created_at',
        sort_value: ''
      },
      
      loading: false,          // 加载状态
      seniorSearch: this.search, // 高级搜索条件
      additional_search: [],     // 附加搜索条件
      filterData: [],           // 筛选数据
      
      // 排序选项
      sortList: [
        { name: '升序', value: 'asc' },
        { name: '降序', value: 'desc' },
        { name: '默认排序', value: '' }
      ],
      
      // 时间搜索选项
      timeSearch: [
        { name: '创建时间', value: 'created_at' },
        { name: '修改时间', value: 'updated_at' }
      ],
      
      activeIndex: 'created_at', // 当前激活的时间索引
      sortIndex: '',            // 当前排序索引
      treeValue: ''            // 树形选择值
    }
  },
  
  watch: {
    // 监听搜索条件变化
    search(val) {
      this.seniorSearch = val
    },
    
    // 监听树形默认值变化
    treeDefault: {
      handler(val) {
        this.treeValue = val
      },
      immediate: true
    }
  },
  
  computed: {
    // 是否显示头部
    showHeader() {
      return this.title !== '' || this.isAddBtn || this.dropdownList.length > 0
    }
  },
  
  methods: {
    /**
     * 处理表单提交
     * @param {Object} data - 表单数据
     */
    handleEmit(data) {
      this.where = { ...data }
      this.$emit('confirmData', this.where)
    },
    
    /**
     * 显示高级筛选
     */
    onShow() {
      this.$store.state.business.conditionDialog = true
    },
    
    /**
     * 返回操作
     */
    backFn() {
      if (this.isBack) {
        this.$emit('backFn')
      }
    },
    
    /**
     * 重置搜索
     */
    resetSearch() {
      this.treeValue = this.treeDefault
      this.where = {}
      this.additional_search = []
      
      let data = {
        list: [],
        type: '',
        additional_search_boolean: '1'
      }
      
      this.$store.commit('uadatefieldOptions', data)
      this.$emit('confirmData', 'reset')
    },
    
    /**
     * 新增数据
     */
    addDataFn() {
      this.$emit('addDataFn')
    },
    
    /**
     * 下拉菜单搜索
     * @param {Object} item - 菜单项
     */
    dropdownSearch(item) {
      this.$emit('dropdownFn', item)
    },
    
    /**
     * 处理标签配置
     * @param {Object} val - 标签值
     * @param {Object} item - 菜单项
     */
    handleLabelConf(val, item) {
      this.$emit('dropdownFn', item, val)
    },
    
    /**
     * 处理时间点击
     * @param {Object} item - 时间项
     * @param {Number} index - 索引
     */
    handleClick(item, index) {
      this.where.sort_field = item.value
      this.activeIndex = item.value
      this.$emit('confirmData', this.where)
    },
    
    /**
     * 排序操作
     * @param {Object} item - 排序项
     * @param {Number} index - 索引
     */
    sortFn(item, index) {
      this.where.sort_value = item.value
      this.sortIndex = item.value
      this.$emit('confirmData', this.where)
    },
    
    /**
     * 保存筛选条件
     * @param {Object} data - 筛选数据
     */
    saveCondition(data) {
      this.additional_search = this.$store.state.business.fieldOptions.list
      let obj = {}
      
      if (this.additional_search == 0) {
        this.$store.state.business.fieldOptions.resetList.map((item) => {
          if (item.type === 'date_picker') {
            obj[item.field] = ''
          } else {
            obj[item.field] = ''
          }
        })
      } else {
        this.additional_search.map((item) => {
          if (item.type === 'date_picker') {
            obj[item.field] = item.option[0] + '-' + item.option[1]
          } else {
            obj[item.field] = item.option
          }
        })
      }
      
      this.$emit('confirmData', { ...this.where, ...obj })
    },
    
    /**
     * 树形选择变化
     * @param {String} value - 选择的值
     */
    treeChange(value) {
      this.$emit('treeChange', { value })
    }
  }
}
</script>

<style lang="scss" scoped>
.grey-bga {
  ::v-deep .el-input__inner {
    border: none;
    background: #f7f7f7;
  }
}
.search {
  //margin-top: 11px;
  display: flex;
  justify-content: space-between;
}
.title-16 {
  height: 32px !important;
  line-height: 32px;
}
.total-16 {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #909399;
  min-width: 50px;
  white-space: nowrap;
}
.right {
  display: flex;
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
    // margin-top: 4px;
  }
  .el-dropdown-link {
    height: 33px;
    padding: 0 10px;
    line-height: 33px;
    min-width: 64px;
    border: 1px solid #fff;
  }
}
.el-icon-question {
  cursor: pointer;
  color: #1890ff;
  font-size: 15px;
}
.el-dropdown-link:hover {
  // border: 1px solid #1890ff;
  background: #f7f7f7;
}
.field-box {
  margin-top: 8px;
  border-bottom: 1px solid #f5f5f5;
  margin-bottom: 8px;
}
.paixuBox {
  width: 25px;
  height: 33px;
  line-height: 33px;
  display: flex;
  justify-content: center;
  border: 1px solid #fff;
  // align-items: center;
}
.paixuBox:hover {
  background: #f7f7f7;
  // border: 1px solid #1890ff;
}
.field-text {
  cursor: pointer;
  height: 32px;
  // background-color: pink;
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
.ml29 {
  margin-left: 29px;
}
.ml3 {
  margin-left: 3px;
}
.field-bga {
  color: #1890ff;
  background: rgba(24, 144, 255, 0.07);
}
.el-icon-check {
  position: absolute;
  left: 14px;
  top: 11px;
}
.prompt-bag {
  background-color: #edf5ff;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  line-height: 12px;
  color: #606266;
  margin-bottom: 6px;
}
.condition-box {
  padding-top: 5px;
  max-height: 350px !important;
  overflow-y: auto !important;
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
.icongengduo2 {
  font-size: 32px !important;
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
</style>
