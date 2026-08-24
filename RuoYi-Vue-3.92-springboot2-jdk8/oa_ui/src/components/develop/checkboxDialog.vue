<!-- 
  @FileDescription: 低代码配置弹窗组件
  功能：提供字段选择、排序和筛选功能
-->
<template>
  <!-- 弹窗容器 -->
  <div>
    <div class="flex">
      <!-- 主弹窗 -->
      <el-dialog
        :visible.sync="dialogShow"
        width="760px"
        :before-close="handleClose"
        :modal="true"
        :show-close="false"
        :append-to-body="true"
        :close-on-click-modal="false"
      >
        <!-- 弹窗标题 -->
        <div slot="title" class="header">
          <span class="title">{{ title }}</span>
          <span class="el-icon-close pointer" @click="handleClose"></span>
        </div>

        <!-- 实体选择区域 -->
        <div class="flex-between mb14" v-if="showCrud">
          <div style="width: 90px">引用实体：</div>
          <el-select
            v-model="id"
            :disabled="edit"
            placeholder="请选择"
            size="small"
            style="width: 100%"
            filterable
            @change="getlistData"
          >
            <el-option 
              v-for="item in options" 
              :key="item.id" 
              :label="item.table_name" 
              :value="item.id"
            >
              <span>{{ item.table_name }}({{ item.table_name_en }})</span>
            </el-option>
          </el-select>
        </div>

        <!-- 内容区域 -->
        <div class="container">
          <!-- 左侧字段列表 -->
          <el-col :span="24" class="search-p">
            <!-- 普通字段模式 -->
            <div class="tree-box" v-if="this.type !== 'module' && this.type !== 'view'">
              <div class="title-num">{{ name }}（{{ list ? list.length : 0 }}）</div>
              <div class="mt14 box" v-if="list && list.length > 0">
                <div
                  v-for="(item, index) in list"
                  :key="index"
                  class="item mb10 over-text"
                  @click="handleClick(item, 'id')"
                >
                  <span
                    class="iconfont"
                    :class="ids.includes(item.id) ? 'icontongyong-gouxuanxuanzhongtubiao' : ' iconweigouxuan'"
                  ></span>
                  {{ item.field_name }}
                  <span v-if="showCrud">({{ item.field_name_en }})</span>
                </div>
              </div>
              <div v-else>
                <default-page :index="14" :min-height="220" />
              </div>
            </div>

            <!-- 模块/视图模式 -->
            <div class="molude-box" v-else>
              <div class="title-num">{{ name }}（{{ totalNumber }}）</div>
              <div class="num mt14 mb10">系统字段</div>
              <div class="flex-wrap">
                <div
                  v-for="(item, index) in min == 5 ? crudInfo.systemListField : crudInfo.systemField"
                  :key="index"
                  class="item mb10 over-text"
                  @click="handleClick(item, 'field_name_en')"
                >
                  <span
                    class="iconfont"
                    :class="ids.includes(item.field_name_en) ? 'icontongyong-gouxuanxuanzhongtubiao' : ' iconweigouxuan'"
                  ></span>
                  {{ item.field_name }}<span v-if="type !== 'module'">({{ item.field_name_en }})</span>
                </div>
              </div>

              <div class="num mt14 mb10">自定义字段</div>
              <div class="flex-wrap">
                <div
                  v-for="(item, index) in min == 5 ? crudInfo.customListField : crudInfo.customField"
                  :key="index"
                  class="item mb10 over-text"
                  @click="handleClick(item, 'field_name_en')"
                >
                  <span
                    class="iconfont"
                    :class="ids.includes(item.field_name_en) ? 'icontongyong-gouxuanxuanzhongtubiao' : ' iconweigouxuan'"
                  ></span>
                  {{ item.field_name }} <span v-if="type !== 'module'">({{ item.field_name_en }})</span>
                </div>
              </div>
            </div>
          </el-col>

          <!-- 右侧已选字段区域 -->
          <div class="select-box">
            <div class="title flex-between">
              <span class="title-num">
                已展示字段（<span class="doc">{{ selectList ? selectList.length : 0 }}</span>
                <span v-if="max > 0">/{{ max }}</span>）
              </span>
              <span class="empty" @click="restFn">清空</span>
            </div>

            <!-- 可拖拽的已选字段列表 -->
            <div v-if="selectList" class="list-box">
              <draggable
                v-model="selectList"
                chosen-class="chosen"
                force-fallback="true"
                group="people"
                animation="1000"
              >
                <div v-for="(item, index) in selectList" :key="index" class="select-item">
                  <div class="left over-text">
                    <i class="icon iconfont icontuodong item-drag"></i>
                    <span>
                      {{ item.field_name }}
                      <span v-if="item.field_name_en && type !== 'module'"> ({{ item.field_name_en }})</span>
                    </span>
                  </div>
                  <div class="right-box">
                    <span
                      v-if="showCrud && item.field_name_en !== 'id'"
                      class="el-icon-close"
                      @click="handleDelete(item, type == 'module' || type == 'view' ? 'field_name_en' : 'id')"
                    />
                  </div>
                </div>
              </draggable>
            </div>
          </div>
        </div>

        <!-- 底部按钮 -->
        <span slot="footer" class="dialog-footer">
          <el-button size="small" @click="handleClose(1)">{{ $t('public.cancel') }}</el-button>
          <el-button size="small" type="primary" @click="handleConfirm">{{ $t('public.ok') }}</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import defaultPage from '@/components/common/defaultPage'
import draggable from 'vuedraggable'
import { databaseListApi, dataFieldListApi } from '@/api/develop'

export default {
  name: 'CheckboxDialog',
  components: {
    draggable,
    defaultPage
  },
  
  props: {
    // 组件类型：field(字段)、module(模块)、view(视图)
    type: {
      type: String,
      default: ''
    },
    
    // 左侧标题
    name: {
      type: String,
      default: '所有字段'
    },
    
    // 右侧标题
    showName: {
      type: String,
      default: '已选字段'
    },
    
    // 是否显示实体选择
    showCrud: {
      type: Boolean,
      default: false
    },
    
    // 英文显示
    ShowEn: {
      type: String,
      default: ''
    },
    
    // 最大选择数量
    max: {
      type: Number,
      default: 0
    },
    
    // 最小选择数量
    min: {
      type: Number,
      default: 0
    },
    
    // 弹窗标题
    title: {
      type: String,
      default: '引用实体设置'
    }
  },
  
  data() {
    return {
      edit: false,          // 是否编辑模式
      dialogShow: false,    // 控制弹窗显示
      options: [],         // 实体数据列表
      value: '',           // 选择的值
      id: 0,               // 当前实体ID
      ids: [],             // 选中字段ID集合
      selectList: [],      // 已选字段列表
      list: [],            // 全部字段列表
      crudInfo: {}         // 模块/视图信息
    }
  },
  
  computed: {
    // 计算总字段数
    totalNumber() {
      if (this.crudInfo.customListField && this.crudInfo.systemListField) {
        return this.crudInfo.systemListField.length + this.crudInfo.customListField.length
      }
      return 0
    }
  },
  
  mounted() {
    // 初始化获取实体数据
    if (this.type === 'field' && this.showCrud) {
      this.getList()
    }
  },
  
  methods: {
    /**
     * 打开弹窗并初始化数据
     * @param {Object} data - 传入的数据
     * @param {Array} ids - 已选ID数组
     */
    openBox(data, ids) {
      this.selectList = []
      this.ids = []
      
      // 字段模式处理
      if (this.type == 'field') {
        this.edit = data && data.type == 'edit'
        
        if (data && data.id) {
          this.id = data.id
          this.getlistData()
        }
        
        if (data && data.list) {
          this.list = data.list
        }
        
        if (data && data.ids) {
          this.ids = data.ids.map(Number)
          this.selectList = data.selectList
        }
      } 
      // 模块/视图模式处理
      else if ((this.type == 'module' || this.type == 'view') && data) {
        if (ids) {
          this.selectList = ids
        }
        
        this.crudInfo = data
        if (this.selectList.length > 0) {
          this.selectList.forEach(item => {
            this.ids.push(item.field_name_en)
          })
        }
      }
      
      // 默认选中ID字段
      if (this.showCrud && this.ids.length == 0 && !this.edit) {
        this.list.forEach(item => {
          if (item.is_default == 1 && item.field_name_en == 'id') {
            this.ids.push(item.id)
            this.selectList.push(item)
          }
        })
      }
      
      this.dialogShow = true
    },
    
    /**
     * 处理字段选择/取消选择
     * @param {Object} val - 当前字段对象
     * @param {String} id - 字段ID属性名
     */
    handleClick(val, id) {
      // 禁止取消ID字段
      if (this.showCrud && !this.edit && val.field_name_en == 'id') {
        return false
      }
      
      // 已选中则取消
      if (this.ids.includes(val[id])) {
        this.ids = this.ids.filter(item => item != val[id])
        this.selectList = this.selectList.filter(item => item[id] != val[id])
      } 
      // 未选中则添加
      else {
        // 检查最大限制
        if (this.max > 0 && this.selectList.length >= this.max) {
          return this.$message.error(`最多只能选择${this.max}个字段`)
        }
        
        this.ids.push(val[id])
        this.selectList.push(val)
      }
    },
    
    /**
     * 删除已选字段
     * @param {Object} val - 字段对象
     * @param {String} str - ID属性名
     */
    handleDelete(val, str) {
      this.ids = this.ids.filter(item => item !== val[str])
      this.selectList = this.selectList.filter(item => item[str] !== val[str])
    },
    
    /**
     * 获取实体列表数据
     */
    async getList() {
      const where = { cate_id: '' }
      const data = await databaseListApi(where)
      this.options = data.data.list
      
      // 默认选中第一个实体
      if (!this.edit) {
        this.id = this.options[0].id
      }
      
      this.getlistData()
    },
    
    /**
     * 获取字段列表数据
     */
    async getlistData() {
      const data = await dataFieldListApi(this.id)
      this.list = data.data
      
      // 默认选中ID字段
      if (this.showCrud && !this.edit) {
        this.ids = []
        this.selectList = []
        this.list.forEach(item => {
          if (item.is_default == 1 && item.field_name_en == 'id') {
            this.ids.push(item.id)
            this.selectList.push(item)
          }
        })
      }
    },
    
    /**
     * 关闭弹窗
     * @param {Number} val - 关闭标识
     */
    handleClose(val) {
      this.dialogShow = false
      this.edit = false
      this.ids = []
      this.selectList = []
      this.$emit('close') // 触发关闭事件
    },
    
    /**
     * 清空已选
     */
    restFn() {
      this.selectList = []
      this.ids = []
    },
    
    /**
     * 确认选择
     */
    handleConfirm() {
      // 收集选中ID
      let ids = []
      this.selectList.forEach(item => {
        if (this.type !== 'module' && this.type !== 'view') {
          ids.push(item.id)
        } else {
          ids.push(item.field_name_en)
        }
      })
      
      // 准备返回数据
      let data = {
        selectList: this.selectList,
        ids,
        id: this.id
      }
      
      // 验证选择数量
      if ((this.type === 'module' || this.type === 'view') && this.max > 0 && this.selectList.length > this.max) {
        return this.$message.error(`最多只能选择${this.max}个字段`)
      }
      
      if (this.min > 0 && this.selectList.length < this.min) {
        return this.$message.error(`至少选择${this.min}个字段`)
      }
      
      // 触发确认事件
      this.$emit('getData', data)
      this.handleClose()
    }
  }
}
</script>

<!-- 样式部分保持不变 -->
<style lang="scss" scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .title {
    font-size: 15px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 600;
    color: #303133;
  }
  .el-icon-close {
    color: #c0c4cc;
    font-weight: 500;
    font-size: 14px;
  }
}

.search-p {
  width: 70%;
  padding: 20px 20px 0 20px;
  ::v-deep .el-input-group__append {
    top: 0;
    background-color: transparent;
    border-left: none;
    text-align: center;
  }
}
.p20 {
  padding: 20px;
}
::-webkit-scrollbar-thumb {
  -webkit-box-shadow: inset 0 0 6px #ddd;
  display: none;
}

::-webkit-scrollbar {
  width: 3px !important; /*对垂直流动条有效*/
}
::v-deep.el-dialog__body {
  height: max-content;
}

.container {
  border: 1px solid #ececec;
  display: flex;
  min-height: 300px;

  .title {
    display: flex;
    justify-content: space-between;
    font-family: PingFang SC, PingFang SC;
    font-size: 14px;
    font-weight: 400;
    // margin-bottom: 35px;

    .num {
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 13px;
      color: #1e2128 !important;
    }
  }
  .select-box {
    width: 340px;
    padding: 20px 20px 0 20px;
    overflow-y: auto;
    height: initial;
    border-left: 1px solid #eeeeee;

    .list-box {
      .select-item {
        display: flex;
        justify-content: space-between;
        margin-top: 18px;
        cursor: pointer;
        .left {
          display: flex;
          font-size: 12px;
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          color: #303133;
        }
      }
    }
  }
}

.item {
  width: 50%;
  cursor: pointer;
}
.icontongyong-gouxuanxuanzhongtubiao {
  color: #1890ff;
  font-size: 17px;
}
.iconweigouxuan {
  color: #cccccc;
}
.dialog-footer {
  padding-top: 68px;
}
.item-drag {
  font-size: 13px;
  margin-right: 4px;
  color: #909399;
}
.tree-box {
  height: 100%;
  width: 100%;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
  overflow-y: auto;
  .box {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
  }
}

.molude-box {
  width: 100%;
  overflow-y: auto;
  height: 100%;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */

  .flex-wrap {
    width: 100%;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
  }
}
::v-deep .el-dialog__footer {
  padding-top: 30px;
}
.title-num {
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 14px;
  color: #1e2128;
}
.empty {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #1890ff;
  cursor: pointer;
}
</style>
