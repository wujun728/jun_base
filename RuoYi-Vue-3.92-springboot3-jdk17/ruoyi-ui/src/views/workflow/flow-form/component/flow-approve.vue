<template>
  <!-- 审批过程 -->
  <div class="mt10">
    <div class="header">
      <div v-if="showType === 0" class="search-form">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label prop="actId">
            <el-select
              v-model="queryParams.actId"
              placeholder="筛选办理环节"
              size="small"
              clearable
              style="width:300px"
              @change="handleQuery"
              @keyup.enter.native="handleQuery"
            >
              <el-option v-for="item in actFilterList" :key="item.actId" :label="item.actName" :value="item.actId" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div class="tab">
        <el-tooltip content="列表风格">
          <span @click="changeShowType(0)">
            <i class="el-icon-s-grid model-icon" :class="{active: showType == 0}"></i>
          </span>
        </el-tooltip>
        <el-tooltip content="时间线风格">
          <span @click="changeShowType(1)">
            <i class="el-icon-s-operation model-icon" :class="{active: showType == 1}"></i>
          </span>
        </el-tooltip>
      </div>
    </div>
    <!-- 列表模式 -->
    <div v-if="showType == 0">
      <el-table
        ref="approve"
        stripe
        :data="flowRecordList"
        :span-method="handleSpan"
        row-key="`assigneeId` + `actId`"
        key="approve"
        v-loading="loading"
        element-loading-text="正在加载中..."
        element-loading-spinner="el-icon-loading"
      >
        <el-table-column label="流程环节" align="left" prop="taskName" width="200" show-overflow-tooltip />
        <el-table-column label="部门" align="left" prop="deptName" width="200" show-overflow-tooltip />
        <el-table-column label="办理人" align="left" prop="assigneeName" width="200" show-overflow-tooltip />
        <el-table-column label="办理时间" align="left" prop="finishTime" width="200">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.finishTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="接收时间" align="left" prop="createTime" width="200">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="left" prop="status" show-overflow-tooltip>
          <template slot-scope="scope">
            <span :class="scope.row.status === '0' ? 'status-no' : 'status-yes'">{{ handleStatus(scope.row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === '0' && userId && scope.row.assigneeId != userId" type="text" @click="urge(scope.row)">催办</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
    <!-- 时间线模式 -->
    <div v-else>
      <el-timeline>
        <el-timeline-item v-for="(item,index ) in flowRecordList" :key="index" :icon="setIcon(item.finishTime)" :color="setColor(item.finishTime)">
          <p style="font-weight: 600">{{ item.taskName }}</p>
          <el-card :body-style="{ padding: '10px' }">
            <el-descriptions class="margin-top" :column="1" size="small">
              <el-descriptions-item label-class-name="my-label" content-class-name="my-content">
                <template slot="label">
                  <i class="el-icon-user pr5"></i>处理人
                </template>
                <span class="ml10">{{ item.assigneeName }}</span>
                <el-tag class="ml5" size="mini">{{ item.deptName }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label-class-name="my-label">
                <template slot="label">
                  <i class="el-icon-date pr5"></i>接收时间
                </template>
                {{ item.createTime }}
              </el-descriptions-item>
              <el-descriptions-item label-class-name="my-label">
                <template slot="label">
                  <i class="el-icon-date pr5"></i>处理时间
                </template>
                {{ item.finishTime }}
              </el-descriptions-item>
              <el-descriptions-item label-class-name="my-label">
                <template slot="label">
                  <i class="el-icon-tickets pr5"></i>状态
                </template>
                <span class="ml24" :class="item.status === '0' ? 'status-no' : 'status-yes'">{{ handleStatus(item.status)}}</span>
              </el-descriptions-item>
              <el-descriptions-item v-if="item.duration" label-class-name="my-label">
                <template slot="label">
                  <i class="el-icon-time pr5"></i>耗时
                </template>
                <span class="ml24">{{ item.duration }}</span>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script>
import { flowRecord, urge } from "@/api/workflow/task";

export default {
  name: "FlowApprove",
  data() {
    return {
      loading: true,
      showSearch: true,
      // 显示模式，0-列表，1-时间线
      showType: localStorage.getItem("flow_approve_show_type") || 0,
      // 展示方式（存储在localstore中，如果没有就默认0）
      flowRecordList: [], // 流程流转数据
      // 办理环节筛选
      actFilterList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        actId: null,
      },
      total: 0,
      spanArr: [], // 存储合并信息的数组
    };
  },
  props: {
    procInsId: {
      type: String,
      default: null,
    },
    userId: String,
  },
  computed: {
    // 审批状态
    handleStatus() {
      return (val) => {
        return val === "1" ? "已提交" : "未处理";
      };
    },
  },
  created() {},
  methods: {
    /** 切换展示方式 */
    changeShowType(val) {
      this.showType = val;
      localStorage.setItem("flow_approve_show_type", val);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 流程流转记录 */
    async getList() {
      this.loading = true;
      const params = { ...this.queryParams, procInsId: this.procInsId };
      const res = await flowRecord(params);
      if (res.code === 200) {
        this.flowRecordList = res.data.flowList;
        this.actFilterList = res.data.actFilters;
        this.total = res.data.total;
        this.calcSpan();
        this.loading = false;
        this.$emit("change-flag");
      }
    },
    /** 催办 */
    urge(row) {
      this.$confirm("是否提醒对方加快办理？", "提示", { confirmButtonText: "确 认", cancelButtonText: "取 消" }).then(() => {
        this.loading = true;
        urge({ taskId: row.taskId }).then((res) => {
          if (res.code === 200) {
            this.$modal.msgSuccess("催办成功！");
            this.loading = false;
          }
        });
      });
    },
    // 计算合并规则（在获取数据后调用）
    calcSpan() {
      this.spanArr = [];
      let count = 1; // 连续相同actId计数
      let startIndex = 0; // 合并起始位置
      this.flowRecordList.forEach((item, index) => {
        if (index === 0) {
          this.spanArr.push(1);
          return;
        }
        // 当前actId与前一项相同
        if (item.taskId === this.flowRecordList[index - 1].taskId) {
          count++;
          this.spanArr[startIndex] = count;
          this.spanArr.push(0);
        } else {
          count = 1;
          startIndex = index;
          this.spanArr.push(1);
        }
      });
    },
    // 合并单元格方法
    handleSpan({ rowIndex, columnIndex }) {
      // 指定需要合并的列（根据实际需求修改索引）
      const mergeColumns = [0]; // 合并第1列（索引从0开始）
      if (mergeColumns.includes(columnIndex)) {
        const span = this.spanArr[rowIndex];
        return {
          rowspan: span,
          colspan: span > 0 ? 1 : 0,
        };
      }
      return { rowspan: 1, colspan: 1 };
    },
    setIcon(val) {
      if (val) {
        return "el-icon-check";
      } else {
        return "el-icon-time";
      }
    },
    setColor(val) {
      if (val) {
        return "#2bc418";
      } else {
        return "#b3bdbb";
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  justify-content: space-between;
  width: 100%;

  .search-form {
    flex: 1;
    margin-right: 20px;
  }

  .tab {
    margin-left: auto;
  }
}
.status-no {
  color: rgb(230, 57, 57);
}
.status-yes {
  color: rgb(43, 196, 24);
}

.model-icon {
  margin-left: 8px;
  &:first-of-type {
    margin-right: 0;
  }
}

.model-icon.active {
  color: #409eff;
}
.ml24 {
  margin-left: 24px;
}
</style>
<style>
.my-label {
  color: #999;
}
.my-content {
  color: #333;
}
</style>