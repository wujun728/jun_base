<template>
  <div>
    <div v-if="buttonArray.length > 0" class="current-con">
      <el-row class="current-btn">
        <el-col :span="18">
          <el-button
            v-for="(item, index) in buttonArray"
            :key="index"
            :class="index == btnIndex ? 'is_active' : ''"
            round
            @click="handleButton(index, item.id)"
            >{{ item.name }}</el-button
          >
        </el-col>
      </el-row>

      <div v-if="processIndex !== null" class="">
        <goalSetting v-if="processIndex == 0" :id="id" />
        <execution
          v-if="processIndex == 1 && !loading"
          :id="id"
          ref="execution"
          :processIndex="processIndex"
          :reply="reply"
          :current-button="true"
          @handleExecution="handleExecution"
        />
      </div>
    </div>
    <default-page v-else :index="0" />
  </div>
</template>
<script>
import Common from '@/components/user/accessCommon'
import { userAssessExplain, userAssessSubord } from '@/api/user'
export default {
  name: 'Current',
  components: {
    goalSetting: () => import('./goalSetting'),
    execution: () => import('./execution'),
    defaultPage: () => import('@/components/common/defaultPage')
  },
  data() {
    return {
      buttonArray: [],
      btnIndex: 0,
      processIndex: 0,
      id: 0,
      reply: [],
      tabButton: false,
      loading: false
    }
  },
  created() {
    this.getAssessMine()
  },
  methods: {
    handleButton(index, id) {
      this.btnIndex = index
      this.tabButton = true
      this.id = id
      this.getAssessMine()
    },
    getAssessMine() {
      this.loading = true
      let data = {
        handle: 1,
        time: '',
        status: 1
      }
      userAssessSubord(data).then((res) => {
        this.buttonArray = res.data.list ? res.data.list : []
        if (this.buttonArray.length > 0) {
          this.id = this.buttonArray[this.btnIndex].id
          this.getAssessExplain()
          this.loading = false
          if (this.tabButton) {
            if (this.processIndex === 1) {
              this.$refs.execution.getTableData()
            }
          }
          this.processIndex = null
          this.processIndex = this.buttonArray[this.btnIndex].status
        }
      })
    },
    getPeriodText(id) {
      const txt = Common.getPeriodText(id)
      return txt
    },
    getStatusText(id) {
      const txt = Common.getStatusText(id)
      return txt
    },
    async getAssessExplain() {
      const result = await userAssessExplain(this.id)
      this.tipsBoxContent = result.data.explain
      this.reply = result.data.reply
    },
    handleExecution() {
      this.btnIndex = 0
      this.getAssessMine()
    }
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  background-color: #ffffff;
}
.current-con {
  position: relative;
  .current-btn {
    position: absolute;
    left: 0;
    top: 3px;
    width: 100%;
    z-index: 4;
    button {
      padding: 6px 16px;
      font-size: 13px;
      margin-right: 6px;
    }
    button:last-of-type {
      margin-right: 0;
    }
    button.is_active {
      background-color: #1890ff;
      color: #ffffff;
      border-color: transparent;
    }
  }
}
.current-list {
  .current-table {
    margin: 15px 0;
    ::v-deep .current-table-title {
      padding: 5px 0;
      background-color: #f7f8fa;
      span {
        font-size: 13px;
      }
      span:first-of-type {
        padding-left: 10px;
        font-size: 13px;
        font-weight: bold;
        color: #000000;
      }
    }
    ::v-deep .el-table th > .cell {
      font-weight: normal;
      font-size: 13px;
    }
    ::v-deep .el-table--medium td {
      padding: 6px 0;
    }
    ::v-deep .el-input__inner {
      height: 28px;
      line-height: 28px;
    }
    .current-task {
      display: flex;
      ::v-deep .el-input__inner {
        width: 30px;
        border: none;
        background-color: transparent;
        padding: 0 0 0 3px;
      }
    }
  }
}
</style>
