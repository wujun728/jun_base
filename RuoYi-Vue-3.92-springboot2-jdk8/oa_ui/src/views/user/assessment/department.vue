<template>
  <!-- 部门考核 -->
  <div class="divBox">
    <div>
      <el-card v-if="checkBtn" class="employees-card-bottom">
        <div>
          <div class="plan-tabs-content mb20">
            <el-tabs v-model="tabCur" @tab-click="tapClick">
              <el-tab-pane label="考核记录" name="4" />
              <el-tab-pane label="待处理" name="3" />
              <el-tab-pane label="考核统计" name="5" />
            </el-tabs>
            <el-button
              v-if="tabCur == 3 || tabCur == 4"
              class="btn-create"
              icon="el-icon-plus"
              size="small"
              type="primary"
              @click="addFinance"
              >{{ $t('access.createassessment') }}</el-button
            >
          </div>
          <!-- 待处理与考核记录  -->
          <lower-level
            v-if="tabCur == 3 || tabCur == 4"
            ref="lower"
            :handle="handle"
            :type="1"
            @lowerHandleCheck="lowerHandleCheck"
          />
          <!-- 考核统计   -->
          <assess-statistics v-if="tabCur == 5" ref="lowerStatistics" :type="1" />
        </div>
      </el-card>
    </div>

    <audit-process
      v-if="!checkBtn"
      :id="id"
      ref="auditProcess"
      :is-tips="isTips"
      :isShowProps="isShow"
      :process-index="status"
      :rowData="rowData"
      :strType="strType"
      @auditProcess="auditProcess"
      @saveOk="saveOk"
    />
  </div>
</template>
<script>
export default {
  name: 'DepartmentAssessment',
  components: {
    auditProcess: () => import('./components/auditProcess'),
    lowerLevel: () => import('./components/lowerLevel'),
    assessStatistics: () => import('@/views/user/workStatistics/components/assessStatistics')
  },

  data() {
    return {
      tabCur: '4',
      id: 0,
      checkBtn: true,
      isTips: true,
      status: 0,
      isShow: 0,
      strType: '',
      handle: '', // 待处理1其他''
      rowData: {},
      tabButton: false
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    addFinance() {
      this.$refs.lower.addFinance()
    },
    saveOk() {
      this.checkBtn = true
      setTimeout(() => {
        this.$refs.lower.getTableData()
      }, 300)
    },
    lowerHandleCheck(res, row, str) {
      if (row) this.rowData = row;
      this.strType = str;
      this.checkBtn = false;
      this.isShow = res.is_show;
      this.status = res.status == 5 ? 0 : res.status;
      this.id = res.id;
      this.isTips = res.addBtn <= 1;
    },
    tapClick(e) {
      if (this.tabCur == 3) {
        this.handle = 1
        setTimeout(() => {
          this.$refs.lower?.getTableData()
        }, 300)
      } else {
        this.handle = ''
        setTimeout(() => {
          this.$refs.lower?.getTableData()
        }, 300)
      }
      this.getList()
    },
    auditProcess(data) {
      if (data.return == 1) {
        this.checkBtn = true
        this.tabButton = false
        this.getList()
      }
    },
    getList() {
      // 使用常量定义延迟时间，提高代码可读性
      const DELAY_TIME = 300;
      setTimeout(() => {
        const tabCur = parseInt(this.tabCur);
        // 使用对象映射替代多个条件判断，提高代码可维护性
        const actions = {
          3: () => {
            this.$refs.lower.handle = 1;
            this.$refs.lower.getFormBoxFrame();
            if (!this.tabButton) {
              this.$refs.lower.getTableData();
            }
            this.tabButton = true;
          },
          4: () => {
            this.$refs.lower.handle = '';
            this.$refs.lower.getFormBoxFrame();
            if (!this.tabButton) {
              this.$refs.lower.getTableData();
            }
            this.tabButton = true;
          },
          default: () => {
            this.tabButton = false;
            this.$refs.lowerStatistics.getTableData();
          }
        };
        // 根据tabCur的值执行相应的操作
        (actions[tabCur] || actions.default)();
      }, DELAY_TIME);
    }
  }
}
</script>

<style lang="scss" scoped>
.btn-create {
  margin-top: -15px;
}
.plan-tabs-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e6ebf5;
  ::v-deep .el-tabs__header {
    margin-bottom: 0;
    .el-tabs__item {
      font-size: 15px;
    }
    .el-tabs__nav-wrap::after {
      height: 1px;
      background-color: #eee;
    }
  }
}
</style>
