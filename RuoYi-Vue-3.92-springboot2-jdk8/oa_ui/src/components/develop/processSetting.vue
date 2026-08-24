<!-- @FileDescription: 低代码-流程设置组件-->
<template>
  <div class="processData">
    <div class="fd-nav-content">
      <section class="dingflow-design">
        <div class="zoom">
          <div :class="'zoom-out' + (nowVal == 50 ? ' disabled' : '')" @click="zoomSize(1)"></div>
          <span>{{ nowVal }}%</span>
          <div :class="'zoom-in' + (nowVal == 300 ? ' disabled' : '')" @click="zoomSize(2)"></div>
        </div>
        <div
          id="box-scale"
          class="box-scale"
          :style="'transform: scale(' + nowVal / 100 + '); transform-origin: 50% 0px 0px;'"
        >
          <nodeWrap
            :node-config.sync="nodeConfig"
            :flow-permission.sync="flowPermission"
            :is-tried.sync="isTried"
            :table-id="tableId"
            :type-str="lowCode"
          ></nodeWrap>
          <div class="end-node">
            <div class="end-node-circle"></div>
            <div class="end-node-text">流程结束</div>
          </div>
        </div>
      </section>
    </div>
    <errorDialog :visible.sync="tipVisible" :list="tipList" />
    <promoter-drawer />
    <approver-drawer :type-str="lowCode" :director-max-level="directorMaxLevel" />
    <copyer-drawer :type-str="lowCode" :director-max-level="directorMaxLevel" />
    <!-- 条件设置 -->
    <el-drawer
      :append-to-body="true"
      title="条件设置"
      :visible.sync="$store.state.business.conditionDialog"
      :wrapperClosable="false"
      direction="rtl"
      class="condition_copyer"
      size="650px"
    >
      <condition-dialog :id="curd_id" v-if="$store.state.business.conditionDialog" />
    </el-drawer>
  </div>
</template>

<script>
import func from '@/utils/preload'
export default {
  name: 'ProcessData',
  components: {
    promoterDrawer: () => import('@/components/workFlow/drawer/promoterDrawer'),
    approverDrawer: () => import('@/components/workFlow/drawer/approverDrawer'),
    copyerDrawer: () => import('@/components/workFlow/drawer/copyerDrawer'),
    conditionDialog: () => import('@/components/develop/conditionDialog'),
    errorDialog: () => import('@/components/workFlow/dialog/errorDialog')
  },
  props: {
    tabName: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      default: ''
    },
    conf: {
      type: Object,
      default: () => {
        return null
      }
    }
  },
  data() {
    return {
      tipList: [],
      curd_id: '', // 实体id
      tipVisible: false,
      nowVal: 100,
      lowCode: 'lowCode',
      directorMaxLevel: 10,
      nodeConfig: {
        nodeName: '申请人',
        type: 0,
        onlyValue: this.$func.onlyValue(),
        priorityLevel: '',
        settype: '',
        selectMode: '',
        selectRange: '',
        directorLevel: '',
        examineMode: '',
        noHanderAction: '',
        examineEndDirectorLevel: '',
        ccSelfSelectFlag: '',
        conditionList: [],
        nodeUserList: {
          depList: [],
          userList: []
        },
        childNode: {
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
          noHanderAction: '2',
          examineEndDirectorLevel: '0',
          childNode: {
            nodeName: '抄送人',
            type: 2,
            onlyValue: this.$func.onlyValue(),
            ccSelfSelectFlag: '1',
            childNode: null,
            departmentHead: [],
            nodeUserList: [],
            error: false
          },
          nodeUserList: []
        },
        conditionNodes: []
      },
      flowPermission: {
        depList: [],
        userList: []
      },
      processConfig: {},
      isTried: false,
      tableId: '999'
    }
  },
  beforeCreate() {
    this.$vue.prototype.$func = func
  },

  watch: {
    conf: {
      handler(val) {
        this.nodeConfig = val
        this.flowPermission = this.nodeConfig.nodeUserList
        this.getData()
      },
      deep: true
    },
    id: {
      handler(val) {
        if (val) {
          this.curd_id = val
        }
      }
    }
  },
  methods: {
    reErr({ childNode }) {
      if (childNode) {
        const { type, error, nodeName, conditionNodes } = childNode
        if (type == 1 || type == 2) {
          if (error) {
            this.tipList.push({ name: nodeName, types: 1, type: ['', '审核人', '抄送人'][type] })
          }
          this.reErr(childNode)
        } else if (type == 3) {
          this.reErr(childNode)
        } else if (type == 4) {
          this.reErr(childNode)
          for (var i = 0; i < conditionNodes.length; i++) {
            if (conditionNodes[i].error) {
              this.tipList.push({ name: conditionNodes[i].nodeName, types: 1, type: ' 条件' })
            }
            if (!conditionNodes[i].childNode) {
              this.tipList.push({ name: conditionNodes[i].nodeName, types: 2, type: ' 审批人或抄送人' })
            }
            this.reErr(conditionNodes[i])
          }
        }
      } else {
        childNode = null
      }
    },
    getData() {
      return new Promise((resolve, reject) => {
        this.isTried = true
        this.tipList = []
        if (!this.nodeConfig.childNode) {
          this.tipList.push({ name: '', types: 2, type: ' 审批人或抄送人' })
        }
        this.reErr(this.nodeConfig)
        this.nodeConfig.nodeUserList = this.flowPermission

        resolve(this.nodeConfig)
      })
    },
    zoomSize(type) {
      if (type == 1) {
        if (this.nowVal === 50) {
          return
        }
        this.nowVal -= 10
      } else {
        if (this.nowVal === 300) {
          return
        }
        this.nowVal += 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../../styles/workflow.css';
.processData {
  height: 100%;
  .fd-nav-content {
    height: calc(100vh - 130px);
    overflow: auto;

    &::-webkit-scrollbar-thumb {
      -webkit-box-shadow: inset 0 0 6px #ccc;
    }
    &::-webkit-scrollbar {
      width: 4px !important; /*对垂直流动条有效*/
    }
  }
}
.condition_copyer {
  .el-drawer__body {
    .priority_level {
      background: rgba(255, 255, 255, 1);
      border-radius: 4px;
      border: 1px solid rgba(217, 217, 217, 1);
    }
  }
  .condition_content {
    padding: 20px 20px 0;
    ::v-deep .el-input-number--medium,
    ::v-deep .el-select--medium {
      width: 100%;
    }
    ::v-deep .el-input__inner {
      text-align: left;
    }
  }
}
.box-scale {
  padding-top: 30px;
}
</style>
