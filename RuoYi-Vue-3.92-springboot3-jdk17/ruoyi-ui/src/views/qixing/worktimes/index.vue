<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="工号" prop="usercode">
        <el-input v-model="queryParams.usercode" placeholder="请输入工号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="用户名称" prop="refUsername">
        <el-input v-model="queryParams.refUsername" placeholder="请输入用户名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:worktimes:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:worktimes:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:worktimes:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:worktimes:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="worktimesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工号" align="center" prop="usercode" />
      <el-table-column label="用户名称" align="center" prop="refUsername" />
      <el-table-column label="日期" align="center" prop="workDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工作时长" align="center" prop="workTotalTime" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:worktimes:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:worktimes:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工号" prop="usercode">
          <el-input v-model="form.usercode" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="用户名称" prop="refUsername">
          <el-input v-model="form.refUsername" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="日期" prop="workDay">
          <el-date-picker clearable v-model="form.workDay" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" />
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTime">
          <el-date-picker clearable v-model="form.beginTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable v-model="form.endTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择结束时间" />
        </el-form-item>
        <el-form-item label="工作时长" prop="workTotalTime">
          <el-input v-model="form.workTotalTime" placeholder="请输入工作时长" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listWorktimes, getWorktimes, delWorktimes, addWorktimes, updateWorktimes } from "@/api/qixing/worktimes";

export default {
  name: "Worktimes",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      worktimesList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        usercode: null,
        refUsername: null
      },
      form: {},
      rules: {
        usercode: [{ required: true, message: "工号不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listWorktimes(this.queryParams).then(response => {
        this.worktimesList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        usercode: null,
        refUsername: null,
        workDay: null,
        beginTime: null,
        endTime: null,
        workTotalTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加工时记录";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWorktimes(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工时记录";
      });
    },
    submitForm() {
      this.["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWorktimes(this.form).then(response => {
              this..msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWorktimes(this.form).then(response => {
              this..msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this..confirm('是否确认删除工时编号为"' + ids + '"的数据项？').then(function() {
        return delWorktimes(ids);
      }).then(() => {
        this.getList();
        this..msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/worktimes/export', {
        ...this.queryParams
      }, worktimes_.xlsx)
    }
  }
};
</script>