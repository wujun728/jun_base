<template>
  <div class="stepsBox">
    <div class="acea-row mb25">
      <div class="shu mr10"></div>
      <span class="title">
        审批流程
        <i class="tit1" v-if="approverDelete || copyerDelete">（已由管理员预设不可{{ title01.substring(1) }}）</i>
      </span>
    </div>
    <el-steps direction="vertical">
      <el-step v-for="(item, index) in examineList" :key="index">
        <div slot="title" class="caption">
          {{ item.title }}
          <el-tag
            v-if="item.types == 1 && item.settype != 5 && item.examine_mode > 0 && item.users.length > 1"
            effect="plain"
            size="mini"
          >
            {{ getExamineText(item.examine_mode) }}
          </el-tag>

          <span class="color-tab tab-icon" v-if="item.types == 1 && item.settype === 4">*</span>
          <span class="tip-title color-tab" v-if="judgeNodeText(item)">{{ judgeNodeText(item) }}</span>
        </div>
        <div slot="icon" class="liuchen">
          <i :class="item.types == 1 ? 'el-icon-s-check' : 'el-icon-s-promotion'"></i>
        </div>
        <div slot="description">
          <div class="acea-row">
            <div v-for="(items, indexs) in item.users" :key="indexs" class="manList acea-row row-between-wrapper">
              <img
                v-if="items.card || items.avatar"
                class="mb10"
                :src="items.card ? items.card.avatar : items.avatar"
              />

              <img v-else class="mb10" src="../../../../assets/images/portrait.png" />

              <span>{{ items.card ? items.card.name : items.name }}</span>
              <span v-if="!items.isDelete" class="acea-icon" @click="deleteUsersItem(item.users, indexs)">
                <i class="el-icon-close"></i>
              </span>
              <div
                v-if="item.types == 1 && item.examine_mode == 3 && indexs !== item.users.length - 1"
                class="acea-arrow-right"
              >
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
            <select-member
              ref="selectMember"
              :only-one="item.select_mode == 1 ? true : false"
              :value="item.users || []"
              @getSelectList="getSelectList($event, item)"
            >
              <template v-slot:custom>
                <div
                  v-if="item.types == 1 && (item.options || editUser)"
                  class="upload"
                  @click="openDepartment(item, index)"
                >
                  <i class="el-icon-plus"></i>
                </div>
                <div
                  v-if="item.types == 2 && item.self_select == 1"
                  class="upload"
                  @click="openDepartment(item, index)"
                >
                  <i class="el-icon-plus"></i>
                </div>
              </template>
            </select-member>
          </div>
        </div>
      </el-step>
    </el-steps>
  </div>
</template>

<script>
export default {
  name: 'Process',
  components: {
    selectMember: () => import('@/components/form-common/select-member')
  },
  props: {
    examineData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    editUser: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      checkedList: [],
      examineList: [],
      examineRules: {},
      index: -1,
      isAppoint: false,
      appointList: [],
      approverDelete: true,
      copyerDelete: true,
      title01: '',
      memberShow: false
    }
  },
  watch: {
    examineData: {
      handler(nVal) {
        this.examineList = nVal.list
        this.examineRules = nVal.rules
        this.title01 = ''
        this.$nextTick(() => {
          if (this.examineRules && this.examineRules.edit) {
            let editText = ''
            editText = this.examineRules.edit + ''
            this.approverDelete = editText.includes('1')
            this.copyerDelete = editText.includes('2')
            this.title01 += this.approverDelete ? '和修改审批人' : ''
            this.title01 += this.copyerDelete ? '和删除抄送人' : ''
          }
        })
      },
      immediate: true
    },
    lang() {
      this.setOptions()
    }
  },
  methods: {
    getExamineText(id) {
      let str = ''
      if (id == 1) {
        str = '或签'
      } else if (id == 2) {
        str = '会签'
      } else if (id == 3) {
        str = '依次审批'
      }
      return str
    },

    // 选择成员完成回调
    getSelectList(data, item) {
      this.checkedList = data
      item.users = data
      this.index = -1
    },
    // 打开选择部门成员
    openDepartment(row, index) {
      this.index = index

      row.users.forEach((item) => {
        item.value = item.id
      })
      this.$refs.selectMember[index].handlePopoverShow()
    },
    deleteUsersItem(row, index) {
      row.splice(index, 1)
    },
    judgeNodeText(row) {
      let str = ''
      if (
        this.approverDelete &&
        row.types === 1 &&
        (row.settype === 2 || row.settype === 7) &&
        row.no_hander === 2 &&
        row.users.length <= 0
      ) {
        str = '直接跳过节点，不视为异常'
      }
      if (
        this.approverDelete &&
        row.types === 1 &&
        (row.settype === 2 || row.settype === 7) &&
        row.no_hander === 1 &&
        row.users.length <= 0
      ) {
        str = this.examineRules.abnormal <= 0 ? '自动同意' : '转交给指定人员处理'
      }
      return str
    }
  }
}
</script>

<style scoped lang="scss">
.el-icon-arrow-right {
  line-height: 44px;
  font-size: 18px;
  color: rgba(0, 0, 0, 0.5);
}
.manList {
  flex-direction: column;
  position: relative;
  img {
    width: 42px;
    height: 42px;
    border-radius: 50%;
    overflow: hidden;
    display: block;
  }
  span {
    font-size: 13px;
    font-weight: 400;
    color: rgba(102, 102, 102, 0.85);
    line-height: 13px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 75px;
    text-align: center;
  }
  .acea-icon {
    position: absolute;
    right: 14px;
    top: 0;
    width: 14px;
    height: 14px;
    background-color: #ccc;
    border-radius: 50%;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    i {
      font-size: 13px;
      color: #fff;
    }
  }
  .acea-arrow-right {
    position: absolute;
    right: -12px;
  }
}
.caption {
  .tab-icon {
    font-size: 16px;
  }
  .tip-title {
    font-size: 13px;
    font-weight: normal;
  }
}
.stepsBox {
  ::v-deep.el-step__description {
    margin-top: 17px;
    margin-bottom: 30px;
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
.shu {
  width: 3px;
  height: 16px;
  background: #1890ff;
  display: inline-block;
}
.title {
  font-size: 14px;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.85);
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
</style>
