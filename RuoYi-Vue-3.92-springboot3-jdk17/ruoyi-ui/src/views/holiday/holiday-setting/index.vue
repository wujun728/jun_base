<template>
  <div class="tab">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="92px">
      <el-form-item label="年度" prop="year" label-width="68px">
        <el-date-picker v-model="queryParams.year" type="year" placeholder="选择年" value-format="yyyy" format="yyyy" @keyup.enter.native="handleQuery"></el-date-picker>
      </el-form-item>
      <el-form-item label="假期名称" prop="holidayName">
        <el-input v-model="queryParams.holidayName" placeholder="请输入假期名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="开始日期">
        <el-date-picker
          v-model="daterangeStartDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:setting:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:setting:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:setting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:setting:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="settingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="年度" align="center" prop="year" width="120" />
      <el-table-column label="假期名称" align="left" prop="holidayName" width="200" />
      <el-table-column label="开始日期" align="center" prop="startDate" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="endDate" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="left" prop="remark" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:setting:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:setting:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改假期设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="年度" prop="year">
          <el-select v-model="form.year">
            <el-option v-for="year in yearList" :key="year" :value="year" :label="year" />
          </el-select>
        </el-form-item>
        <el-form-item label="假期名称" prop="holidayName">
          <el-input v-model="form.holidayName" placeholder="请输入假期名称" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker clearable v-model="form.startDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择开始日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker clearable v-model="form.endDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSetting, getSetting, delSetting, addSetting, updateSetting } from "@/api/system/holidaySetting";

export default {
  name: "HolidaySetting",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 假期设置表格数据
      settingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 更新人时间范围
      daterangeStartDate: [],
      // 更新人时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        year: "",
        holidayName: null,
        startDate: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        year: [{ required: true, message: "年度不能为空", trigger: "blur" }],
        holidayName: [{ required: true, message: "假期名称不能为空", trigger: "blur" }],
        startDate: [{ required: true, message: "开始日期不能为空", trigger: "blur" }],
        endDate: [{ required: true, message: "结束日期不能为空", trigger: "blur" }],
      },
      yearList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询假期设置列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeStartDate && "" != this.daterangeStartDate) {
        this.queryParams.params["beginStartDate"] = this.daterangeStartDate[0];
        this.queryParams.params["endStartDate"] = this.daterangeStartDate[1];
      }
      if (null != this.daterangeCreateTime && "" != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listSetting(this.queryParams).then((response) => {
        this.settingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /**
     * 生成年份列表：上一年度、当前年度、下一年度
     */
    generateYearList() {
      const currentYear = new Date().getFullYear();
      this.yearList = [
        currentYear - 1, // 上一年度
        currentYear, // 当前年度
        currentYear + 1, // 下一年度
      ];
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        year: new Date().getFullYear(),
        holidayName: null,
        startDate: null,
        endDate: null,
        remark: null,
      };
      this.resetForm("form");
      this.generateYearList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeStartDate = [];
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加假期设置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getSetting(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改假期设置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateSetting(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSetting(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除假期设置编号为"' + ids + '"的数据项？')
        .then(function () {
          return delSetting(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/setting/export",
        {
          ...this.queryParams,
        },
        `setting_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>

<style scoped lang="scss">
.tab {
  margin-top: 12px;
}
</style>