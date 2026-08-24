<template>
  <div class="add-node-btn-box">
    <div class="add-node-btn">
      <el-popover v-model="visible" placement="right-start">
        <div class="add-node-popover-body">
          <a class="add-node-popover-item approver" @click="addType(1)">
            <div class="item-wrapper">
              <i class="iconfont iconshenpi"></i>
            </div>
            <p>审批人</p>
          </a>
          <a class="add-node-popover-item notifier" @click="addType(2)">
            <div class="item-wrapper">
              <span class="iconfont iconchaosong"></span>
            </div>
            <p>抄送人</p>
          </a>
          <a class="add-node-popover-item condition" @click="addType(4)">
            <div class="item-wrapper">
              <span class="iconfont icontiaojian"></span>
            </div>
            <p>条件</p>
          </a>
        </div>
        <button slot="reference" class="btn" type="button">
          <i class="iconfont iconxinjian"></i>
        </button>
      </el-popover>
    </div>
  </div>
</template>
<script>
import func from '@/utils/preload';

export default {
  props: {
    childNodeP: {
      type: Object | String,
      default: '',
    },
  },
  data() {
    return {
      visible: false,
    };
  },
  beforeCreate() {
    this.$vue.prototype.$func = func;
  },
  methods: {
    addType(type) {
      this.visible = false;
      if (type != 4) {
        var data;
        if (type == 1) {
          data = {
            nodeName: '审核人',
            error: false,
            type: '1',
            onlyValue: this.$func.onlyValue(),
            settype: '2',
            selectMode: '0',
            selectRange: '0',
            directorLevel: '1',
            directorOrder: '1',
            examineMode: '3',
            noHanderAction: '1',
            examineEndDirectorLevel: '0',
            childNode: this.childNodeP,
            nodeUserList: [],
          };
          this.$emit('update:childNodeP', data);
        } else if (type == 2) {
          data = {
            nodeName: '抄送人',
            type: 2,
            onlyValue: this.$func.onlyValue(),
            ccSelfSelectFlag: '1',
            departmentHead: [],
            childNode: this.childNodeP,
            nodeUserList: [],
          };
        }
        this.$emit('update:childNodeP', data);
      } else {
        this.$emit('update:childNodeP', {
          nodeName: '路由',
          type: 4,
          onlyValue: this.$func.onlyValue(),
          childNode: null,
          conditionNodes: [
            {
              nodeName: '条件1',
              error: true,
              type: 3,
              onlyValue: this.$func.onlyValue(),
              priorityLevel: '1',
              conditionList: [],
              isDefault: false,
              nodeUserList: [],
              childNode: this.childNodeP,
            },
            {
              nodeName: '默认条件',
              type: 3,
              onlyValue: this.$func.onlyValue(),
              priorityLevel: '2',
              conditionList: [],
              isDefault: true,
              nodeUserList: [],
              childNode: null,
            },
          ],
        });
      }
    },
  },
};
</script>
<style scoped lang="scss">
.add-node-btn-box {
  width: 240px;
  display: -webkit-inline-box;
  display: -ms-inline-flexbox;
  display: inline-flex;
  -ms-flex-negative: 0;
  flex-shrink: 0;
  -webkit-box-flex: 1;
  -ms-flex-positive: 1;
  position: relative;
  &:before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
    margin: auto;
    width: 2px;
    height: 100%;
    background-color: #cacaca;
  }
  .add-node-btn {
    user-select: none;
    width: 240px;
    padding: 20px 0 32px;
    display: flex;
    -webkit-box-pack: center;
    justify-content: center;
    flex-shrink: 0;
    -webkit-box-flex: 1;
    flex-grow: 1;
    .btn {
      outline: none;
      box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
      width: 30px;
      height: 30px;
      background: #3296fa;
      border-radius: 50%;
      position: relative;
      border: none;
      display: flex;
      justify-content: center;
      align-items: center;
      -webkit-transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
      i {
        display: block;
        width: 17px !important;
        color: #fff;
        font-size: 16px;
      }
      &:hover {
        transform: scale(1.3);
        box-shadow: 0 13px 27px 0 rgba(0, 0, 0, 0.1);
      }
      &:active {
        transform: none;
        background: #1e83e9;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
      }
    }
  }
}
.add-node-popover-body {
  display: flex;
  padding: 0 15px;
  .add-node-popover-item {
    margin-right: 20px;
    cursor: pointer;
    text-align: center;
    color: #191f25 !important;
    .item-wrapper {
      user-select: none;
      width: 40px;
      height: 40px;
      margin-bottom: 5px;
      background: #fff;
      border: 1px solid #e2e2e2;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
      .iconfont {
        display: block;
        width: 24px !important;
        font-size: 20px;
        color: #fff;
      }
    }
    p {
      font-size: 13px;
    }
    &.approver {
      .item-wrapper {
        background-color: #ff943e;
      }
    }
    &.notifier {
      .item-wrapper {
        background-color: #3296fa;
      }
    }
    &.condition {
      .item-wrapper {
        background-color: #15bc83;
      }
    }
    &:hover {
      .item-wrapper {
        box-shadow: 0 10px 20px 0 rgba(50, 150, 250, 0.4);
      }
      .iconfont {
        color: #fff;
      }
    }
    &:active {
      .item-wrapper {
        box-shadow: none;
        background: #eaeaea;
      }
      .iconfont {
        color: #fff;
      }
    }
    &:last-of-type {
      margin-right: 0;
    }
  }
}
</style>
