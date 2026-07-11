<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="描述" prop="desc1">
        <el-input v-model="queryParams.desc1" placeholder="请输入描述" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:mail:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:mail:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:mail:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:mail:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bizMailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="名称" align="center" prop="title" />
      <el-table-column label="描述" align="center" prop="desc1" />
      <el-table-column label="标记" align="center" prop="key1" />
      <el-table-column label="标记值" align="center" prop="value1" />
      <el-table-column label="日期" align="center" prop="date1" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date1, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:mail:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:mail:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="描述" prop="desc1">
          <el-input v-model="form.desc1" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="标记" prop="key1">
          <el-input v-model="form.key1" placeholder="请输入标记" />
        </el-form-item>
        <el-form-item label="标记值" prop="value1">
          <el-input v-model="form.value1" placeholder="请输入标记值" />
        </el-form-item>
        <el-form-item label="日期" prop="date1">
          <el-date-picker clearable v-model="form.date1" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" />
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
import { listBizMail, getBizMail, delBizMail, addBizMail, updateBizMail } from "@/api/qixing/bizmail";

export default {
  name: "BizMail",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      bizMailList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        desc1: null
      },
      form: {},
      rules: {
        title: [{ required: true, message: "名称不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listBizMail(this.queryParams).then(response => {
        this.bizMailList = response.rows;
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
        title: null,
        desc1: null,
        key1: null,
        value1: null,
        date1: null,
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
      this.title = "添加邮件信息";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBizMail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改邮件信息";
      });
    },
    submitForm() {
      this.["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBizMail(this.form).then(response => {
              this..msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBizMail(this.form).then(response => {
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
      this..confirm('是否确认删除邮件编号为"' + ids + '"的数据项？').then(function() {
        return delBizMail(ids);
      }).then(() => {
        this.getList();
        this..msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/mail/export', {
        ...this.queryParams
      }, mail_.xlsx)
    }
  }
};
</script>