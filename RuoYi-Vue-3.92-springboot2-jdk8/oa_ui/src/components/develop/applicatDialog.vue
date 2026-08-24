<!-- 
  @FileDescription: 低代码应用管理弹窗组件
  功能：提供应用分类的增删改查和拖拽排序功能
-->
<template>
  <div class="oa-dialog">
    <!-- 弹窗主体 -->
    <el-dialog 
      top="13%" 
      :visible.sync="show" 
      :width="`500px`" 
      :show-close="false" 
      :close-on-click-modal="false"
    >
      <!-- 弹窗标题 -->
      <div slot="title" class="header">
        <span class="title">应用管理</span>
        <span class="el-icon-close" @click="handleClose"></span>
      </div>

      <!-- 内容区域 -->
      <div 
        class="content mt20" 
        ref="scrollTarget" 
        :class="fromItem.length > 7 ? 'box-shadow' : ''"
      >
        <div>
          <!-- 可拖拽列表 -->
          <draggable
            v-model="fromItem"
            chosen-class="chosen"  
            force-fallback="true" 
            group="people"  
            animation="1000"  
            @start="onStart"  
            @end="onEnd"  
          >
            <transition-group>
              <!-- 遍历应用列表 -->
              <div v-for="(item, index) in fromItem" :key="index">
                <div class="flex-between mb14">
                  <!-- 拖拽图标 -->
                  <i class="icon iconfont icontuodong item-drag"></i>
                  
                  <!-- 应用名称输入框 -->
                  <el-input 
                    v-model="item.name" 
                    clearable 
                    show-word-limit 
                    placeholder="请输入应用名称" 
                  />
                  
                  <!-- 删除按钮 -->
                  <i 
                    class="el-icon-remove item-remove" 
                    @click="handleDelete(item, index)"
                  ></i>
                </div>
              </div>
            </transition-group>
          </draggable>
        </div>
      </div>

      <!-- 底部操作按钮 -->
      <div slot="footer">
        <!-- 添加应用按钮 -->
        <el-button 
          class="add-type" 
          size="small" 
          type="text" 
          @click="handleAddType()"
        >
          <i class="el-icon-plus"></i> 添加应用
        </el-button>
        
        <!-- 确认取消按钮 -->
        <div class="dialog-footer">
          <el-button @click="handleClose">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import { savecrudCateApi, delcrudCateApi } from '@/api/develop'

export default {
  name: 'ApplicatDialog',
  components: {
    draggable
  },
  
  props: {
    // 应用分类列表
    list: {
      type: Array,
      default: () => []
    }
  },
  
  data() {
    return {
      show: false,      // 控制弹窗显示
      fromItem: []     // 表单数据
    }
  },

  methods: {
    /**
     * 关闭弹窗
     */
    handleClose() {
      this.show = false
    },
    
    /**
     * 打开弹窗并初始化数据
     * @param {Array} list - 应用分类列表
     */
    openBox(list) {
      if (list.length === 0) {
        // 默认添加一个空项
        this.fromItem.push({
          name: '',
          id: 0,
          sort: 0
        })
      } else {
        this.fromItem = [...list]
      }
      this.show = true
    },
    
    /**
     * 添加新的应用分类
     */
    handleAddType() {
      this.fromItem.push({
        name: '',
        id: 0,
        sort: 0
      })
      
      // 滚动到底部
      this.$nextTick(() => {
        const scrollTarget = this.$refs.scrollTarget
        scrollTarget.scrollTo({
          top: scrollTarget.scrollHeight,
          behavior: 'smooth'
        })
      })
    },
    
    /**
     * 提交表单数据
     */
    submit() {
      // 设置排序值
      this.fromItem.forEach((item, index) => {
        item.sort = this.fromItem.length - index
      })
      
      const data = {
        cate: this.fromItem
      }
      
      savecrudCateApi(data)
        .then(() => {
          this.handleClose()
          this.$emit('getList')
        })
        .catch(err => {
          this.$message.error(err.message)
        })
    },
    
    /**
     * 拖拽开始事件
     */
    onStart() {
      // 可添加拖拽开始时的逻辑
    },
    
    /**
     * 拖拽结束事件
     */
    onEnd() {
      // 可添加拖拽结束时的逻辑
    },
    
    /**
     * 删除应用分类
     * @param {Object} item - 当前项
     * @param {Number} index - 索引
     */
    handleDelete(item, index) {
      if (item.name === '') {
        this.fromItem.splice(index, 1)
        return
      }
      
      this.$modalSure('您确定要删除此应用分类吗？').then(() => {
        if (item.id > 0) {
          // 已存在的分类调用接口删除
          delcrudCateApi(item.id).then(() => {
            this.fromItem.splice(index, 1)
            this.$emit('getList')
          })
        } else {
          // 新增未保存的分类直接删除
          this.fromItem.splice(index, 1)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.oa-dialog {
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
  .content {
    max-height: calc(100vh - 520px);
    overflow-y: scroll;
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
.item-drag {
  cursor: move;
  font-size: 13px;
  margin-right: 4px;
  color: #909399;
}
.el-icon-remove {
  margin-left: 5px;
  color: red;
  cursor: pointer;
  font-size: 18px;
}
.icontuodong {
  color: #cacdd2;
  font-size: 14px;
}
.box-shadow {
  box-shadow: inset 0 -4px 4px -2px rgba(0, 0, 0, 0.1);
}
</style>
