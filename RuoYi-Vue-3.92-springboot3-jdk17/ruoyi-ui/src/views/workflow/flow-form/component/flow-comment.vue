<template>
  <!-- 办理意见 -->
  <div class="mt10">
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
    <el-table
      ref="cmt"
      stripe
      :data="cmtList"
      :span-method="handleSpan"
      row-key="taskId"
      key="cmt"
      v-loading="loading"
      element-loading-text="正在加载中..."
      element-loading-spinner="el-icon-loading"
    >
      <el-table-column label="流程环节" align="left" prop="taskName" width="200" show-overflow-tooltip />
      <el-table-column label="部门" align="left" prop="deptName" width="200" show-overflow-tooltip />
      <el-table-column label="办理人" align="left" prop="assigneeName" width="200" />
      <el-table-column label="办理时间" align="left" prop="finishTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.finishTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="办理意见" align="left" prop="comment">
        <template slot-scope="scope">
          <span>{{ comments(scope.row.comment) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { flowCmts } from "@/api/workflow/task";

export default {
  name: "FlowComment",
  data() {
    return {
      loading: true,
      showSearch: true,
      // 意见数据
      cmtList: [],
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
  },
  computed: {
    // 意见
    comments() {
      return (cmt) => {
        if (!cmt) return "";
        return cmt.comment;
      };
    },
    // 审批状态
    handleStatus() {
      return (val) => {
        return val === "1" ? "已提交" : "未处理";
      };
    },
  },
  created() {},
  methods: {
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
      const res = await flowCmts(params);
      this.loading = false;

      if (res.code === 200 && res.data.flowList) {
        this.cmtList = res.data.flowList || [];
        this.actFilterList = res.data.actFilters;
        this.total = res.data.total || 0;
        this.calcSpan();
        this.$emit("change-flag");
      }
    },
    // 计算合并规则（在获取数据后调用）
    calcSpan() {
      this.spanArr = [];
      let count = 1; // 连续相同actId计数
      let startIndex = 0; // 合并起始位置
      this.cmtList.forEach((item, index) => {
        if (index === 0) {
          this.spanArr.push(1);
          return;
        }
        // 当前actId与前一项相同
        if (item.actId === this.cmtList[index - 1].actId) {
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
  },
};
</script>

<style lang="scss" scoped>
</style>