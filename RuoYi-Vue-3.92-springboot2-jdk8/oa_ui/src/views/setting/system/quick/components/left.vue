<template>
  <div class="assess-left">
    <div class="el-card__header">
      <span class="">分类类型</span>
      <el-tooltip effect="dark" content="添加分类" placement="top">
        <span @click="addCategory" class="iconfont icontianjia color-doc pointer"></span>
      </el-tooltip>
    </div>
    <div class="assess-left-con">
      <el-scrollbar style="height: 100%">
        <ul class="assess-left-ul">
          <li
            v-for="(item, index) in department"
            :key="index"
            :class="index == tabIndex ? 'active' : ''"
            @click="clickDepart(index, item.id)"
          >
            <span>{{ item.cate_name }}</span>
            <el-popover
              :ref="`pop-${item.id}`"
              placement="bottom-end"
              trigger="click"
              :offset="10"
              @after-enter="handleShow(item.id)"
              @hide="handleHide"
            >
              <div class="right-item-list">
                <div class="right-item" @click.stop="addDivsion(item.id)">{{ $t('public.edit') }}</div>
                <div class="right-item" @click.stop="handleDelete(item.id)">{{ $t('public.delete') }}</div>
              </div>
              <i slot="reference" class="icon iconfont icongengduo pointer rank-icon"></i>
            </el-popover>
          </li>
        </ul>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
import {
  configQuickCateApi,
  configQuickCateDeleteApi,
  configQuickCateEditApi,
  configQuickCateCreateApi
} from '@/api/setting'

export default {
  name: 'DepartmentNot',
  data() {
    return {
      department: [],
      tabIndex: 0,
      activeValue: '',
      optionValue: {
        id: 0
      }
    }
  },
  mounted() {
    this.getTargetCate()
  },
  methods: {
    clickDepart(index, id) {
      this.tabIndex = index
      this.optionValue.id = id
      this.handleEmit()
    },
    handleEmit() {
      this.$emit('eventOptionData', this.optionValue)
    },
    // 编辑窗口显示
    handleShow(value) {
      this.activeValue = value
    },
    // 编辑窗口隐藏
    handleHide() {
      this.activeValue = ''
    },
    getTargetCate() {
      configQuickCateApi({ page: 0, limit: 0 }).then((res) => {
        this.department = res.data.list ? res.data.list : []
        if (this.department.length > 0) {
          this.optionValue.id = this.department[0].id
          this.clickDepart(0, this.optionValue.id)
        }
        this.handleEmit()
      })
    },
    addCategory() {
      this.$modalForm(configQuickCateCreateApi()).then(({ message }) => {
        this.getTargetCate()
      })
    },
    addDivsion(id) {
      this.$modalForm(configQuickCateEditApi(id)).then(({ message }) => {
        this.getTargetCate()
      })
    },
    async handleDelete(id) {
      await this.$modalSure('你确定要删除这条分类吗')
      await configQuickCateDeleteApi(id)
      await this.getTargetCate()
    }
  }
}
</script>

<style lang="scss" scoped>
.pull-left {
  font-size: 14px;
  font-weight: 500;
}
.icontianjia {
  font-size: 14px;
  color: #1890ff;
}
.assess-left {
  height: calc(100vh - 95px);
  ::v-deep .el-card__header {
    border-bottom: none;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-bottom: 15px;
    button {
      justify-content: flex-end;
    }
  }
  .assess-left-con {
    height: calc(100% - 50px);
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
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
      padding-left: 20px;
      padding-right: 15px;
      color: #303133;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      cursor: pointer;
      border-right: 2px solid transparent;
      display: flex;
      justify-content: space-between;
      &.active {
        background: rgba(24, 144, 255, 0.08);
        color: #1890ff;
        font-weight: 600;
        border-color: #1890ff;
      }
      // &.active {
      //   background-color: rgba(24, 144, 255, 0.08);
      //   border-right: 2px solid #1890ff;
      //   color: #1890ff;
      //   .assess-left-more {
      //     color: #1890ff;
      //   }
      // }
      .rank-icon {
        font-size: 14px;
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
</style>
