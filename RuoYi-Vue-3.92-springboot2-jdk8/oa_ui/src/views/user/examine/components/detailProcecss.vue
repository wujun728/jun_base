<template>
  <!-- 审批进程 -->
  <div class="stepsBox">
    <div class="acea-row mb25">
      <div class="shu mr10"></div>
      <span class="title">审批进程</span>
    </div>
    <el-steps :active="indexCheck" direction="vertical">
      <el-step v-for="(item, index) in examineList" :key="index">
        <div slot="title" class="caption" @click="itemIsShow(item)">
          {{ item.title }}
          <el-tag
            v-if="item.types == 1 && item.settype != 5 && item.examine_mode > 0 && item.users.length > 1"
            effect="plain"
            size="mini"
          >
            {{ getExamineText(item.examine_mode, item) }}
          </el-tag>
          <i :class="!hiddenKey.includes(item.uniqued) ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"></i>
        </div>
        <div slot="icon" class="liuchen">
          <i :class="item.types == 1 ? 'el-icon-s-check' : 'el-icon-s-promotion'"></i>
        </div>
        <div slot="description">
          <div v-show="!hiddenKey.includes(item.uniqued)">
            <div v-for="(items, indexs) in item.users" :key="indexs" class="manList">
              <div class="flex-between">
                <div class="manList-avatar">
                  <div class="manList-icon flex">
                    <span v-if="items.is_transfer > 1" class="iconfont iconzhuanshenzhixiang"></span>
                    <img
                      v-if="judge(items)"
                      :src="items.card ? items.card.avatar : items.info.card.avatar"
                      class="mb10"
                    />

                    <img v-else src="../../../../assets/images/portrait.png" />

                    <!-- 通过 -->
                    <div
                      v-if="items.status === 1 && item.types !== 2 && items.is_sign != 1"
                      class="acea-icon acea-success"
                    >
                      <i class="el-icon-check jingshi"></i>
                    </div>

                    <!-- 加签 -->
                    <div v-if="items.is_sign == 1" class="acea-icon acea-sign">
                      <i class="iconfont iconjiaqiantubiao jingshi"></i>
                    </div>

                    <!-- 转审 -->
                    <div v-if="items.is_transfer !== 2 && items.is_transfer" class="acea-icon acea-trans">
                      <i class="iconfont iconzhuanshentubiao jingshi"></i>
                    </div>

                    <div v-if="items.status === 2 && item.types !== 2" class="acea-icon el-icon-warning"></div>
                  </div>
                  <span class="card-name">{{ items.card ? items.card.name : items.info.card.name }}</span>
                </div>

                <div class="name">
                  <span> {{ getDetailText(item.types, items.status, items) }} · </span>
                  <span v-if="items.status > 0 || items.is_transfer === 3 || items.is_transfer === 1">
                    {{ $moment(items.updated_at).format('YYYY/M/DD HH:mm') }}
                  </span>
                </div>
              </div>

              <div v-if="items.content" class="sign-bag flex">
                <span class="name">{{ items.is_transfer !== 2 && items.is_transfer ? '转审' : ' 加签' }}意见：</span
                >{{ items.content }}
              </div>
            </div>
          </div>
        </div>
      </el-step>
    </el-steps>
  </div>
</template>

<script>
export default {
  name: 'DetailProcess',
  props: {
    examineData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      openStatus: false,
      onlyPerson: false,
      selectPerson: false,
      title: '选择成员',
      checkedList: [],
      examineList: [],
      activeDepartment: {},
      index: -1,
      isAppoint: false,
      appointList: [],
      indexCheck: undefined,
      hiddenKey: []
    }
  },
  watch: {
    examineData: {
      handler(nVal) {
        this.examineList = nVal.users
        this.getChecked(nVal)
      },
      immediate: true
    },
    lang() {
      this.setOptions()
    }
  },
  methods: {
    getChecked(list) {
      if (list.users && list.users.length > 0) {
        this.indexCheck = list.users.findIndex((v) => v.uniqued === list.node_id)
      }
    },
    getExamineText(id, arr) {
      arr = arr.users.filter(function (obj) {
        return obj.status >= 0
      })
      let str = ''
      if (id == 1) {
        str = '或签'
      } else if (id == 2) {
        str = '会签'
      } else if (id == 3) {
        str = '依次审批'
      }
      if (!arr.length) {
        str = ''
      }
      return str
    },

    // 判断头像是否能显示
    judge(row) {
      const card = row?.card;
      // 如果 card 存在且其 avatar 属性包含 'http' 或 'https'，则返回 true，否则返回 false
      return card && (card.avatar?.includes('https') || card.avatar?.includes('http'));
    },
    // 选择部门关闭
    departmentClose() {
      this.openStatus = false
    },
    deleteUsersItem(row, index) {
      row.splice(index, 1)
    },
    getDetailText(type, status, item, row) {
      let str = ''
      if (type == 1 && status == 1 && item.is_sign != 1) {
        str = '已通过 '
      } else if (type == 1 && status == 2) {
        str = '已拒绝 '
      } else if (type == 2 && status > 0) {
        str = '已抄送 '
      } else if (type == 1 && item.is_sign == 1) {
        str = '已加签'
      } else if (item.is_sign == 1) {
        str = '已加签'
      } else if (item.is_transfer === 1 || item.is_transfer === 3) {
        str = '已转审 '
      }
      return str
    },
    itemIsShow(row) {
      var keys = this.hiddenKey.indexOf(row.uniqued)
      if (keys > -1) {
        this.hiddenKey.splice(keys, 1)
      } else {
        this.hiddenKey.push(row.uniqued)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.title {
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 14px;
  color: #303133;
}
.el-icon-arrow-right {
  line-height: 44px;
  font-size: 18px;
  color: rgba(0, 0, 0, 0.5);
}
.card-name {
  margin-left: 10px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #303133;
}
.iconzhuanshenzhixiang {
  color: #c0c4cc;
  font-size: 14px;
  margin-right: 12px;
  line-height: 44px;
}
.sign-bag {
  margin-top: 10px;
  padding: 12px;
  background: #f5f5f5;
  color: #606266;
  font-family: Source Han Sans, Source Han Sans;
  font-weight: 400;
  font-size: 13px;
  border-radius: 4px 4px 4px 4px;
}

.center {
  display: flex;
  align-items: center;
  justify-content: center;
}
.caption {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 13px;
  color: #303133;
  i {
    font-size: 14px;
    color: #c0c4cc;
  }
}

.shu {
  width: 3px;
  height: 16px;
  background: #1890ff;
  display: inline-block;
}
.manList {
  margin-bottom: 12px;
  &:last-of-type {
    margin-bottom: 0;
  }
  .manList-avatar {
    display: flex;
    align-items: center;
  }
  .manList-icon {
    position: relative;
  }
  img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
    display: block;
  }

  .acea-icon {
    position: absolute;
    right: -4px;
    top: -4px;
    width: 14px;
    height: 14px;

    border-radius: 50% !important;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .acea-sign {
    background: #f6bb19;
  }
  .acea-trans {
    background: #cccccc;
  }
  .acea-success {
    background: #1890ff;
  }
  .jingshi {
    color: #fff;
    font-size: 11px;
  }
  .el-icon-warning {
    color: #f56c6c;
    font-size: 15px;
  }

  .acea-arrow-right {
    position: absolute;
    right: -12px;
  }
  .time {
    text-align: right;
    font-size: 13px;
    color: #666666;
  }
}

.stepsBox {
  margin-top: 30px;
  ::v-deep.el-step__head.is-finish > .el-step__icon {
    background-color: #1890ff;
    color: #fff;
    border-color: #1890ff;
  }
  ::v-deep.el-step__line > .el-step__line-inner {
    border-width: 0 !important;
  }
  ::v-deep.el-step__head.is-finish > .el-step__line {
    background-color: #1890ff;
    color: #fff;
    border-color: #1890ff;
    width: 1px;
    left: 12px;
  }
  ::v-deep.el-step.is-vertical .el-step__line {
    width: 1px;
  }
  ::v-deep.el-step__head.is-process > .el-step__icon {
    background-color: #1890ff;
    color: #fff;
    border-color: #1890ff;
  }
  ::v-deep.el-step__description {
    margin-top: 17px;
    margin-bottom: 30px;
    padding-right: 0;
  }
  ::v-deep.el-step__title {
    color: #000;
    font-size: 14px;
    font-weight: 600;
  }
  .tit1 {
    font-style: normal;
    font-size: 13px;
    color: #999999;
    font-weight: normal;
  }
  ::v-deep.el-step__icon {
    background: #ccc;
  }
  .liuchen {
    i {
      color: #fff;
    }
  }
}

.upload {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: #eeeeee;
  font-size: 16px;
  margin-left: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  i {
    font-size: 22px;
  }
}
.name {
  display: flex;
  align-items: center;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #909399;
}
</style>
