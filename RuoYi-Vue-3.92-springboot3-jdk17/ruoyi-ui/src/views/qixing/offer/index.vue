<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入职人员名称" prop="refJobUsername">
        <el-input
          v-model="queryParams.refJobUsername"
          placeholder="请输入入职人员名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入工作地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门" prop="refDeptname">
        <el-input
          v-model="queryParams.refDeptname"
          placeholder="请输入部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="招聘专员" prop="refUsername1">
        <el-input
          v-model="queryParams.refUsername1"
          placeholder="请输入招聘专员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Offer邮件发送" prop="dictYesNo">
        <el-input
          v-model="queryParams.dictYesNo"
          placeholder="请输入Offer邮件发送"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入职时间" prop="jobInTime">
        <el-date-picker clearable
          v-model="queryParams.jobInTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入职时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="鍒涘缓浜篒D" prop="createId">
        <el-input
          v-model="queryParams.createId"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="鏇存柊浜篒D" prop="updateId">
        <el-input
          v-model="queryParams.updateId"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:offer:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:offer:edit']"
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
          v-hasPermi="['system:offer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:offer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="offerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="入职人员名称" align="center" prop="refJobUsername" />
      <el-table-column label="工作地点" align="center" prop="location" />
      <el-table-column label="部门" align="center" prop="refDeptname" />
      <el-table-column label="招聘专员" align="center" prop="refUsername1" />
      <el-table-column label="Offer状态" align="center" prop="dictOfferStatus" />
      <el-table-column label="Offer邮件发送" align="center" prop="dictYesNo" />
      <el-table-column label="入职时间" align="center" prop="jobInTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.jobInTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:offer:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:offer:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改Offer发放对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="入职人员名称" prop="refJobUsername">
          <el-input v-model="form.refJobUsername" placeholder="请输入入职人员名称" />
        </el-form-item>
        <el-form-item label="工作地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入工作地点" />
        </el-form-item>
        <el-form-item label="部门" prop="refDeptname">
          <el-input v-model="form.refDeptname" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="招聘专员" prop="refUsername1">
          <el-input v-model="form.refUsername1" placeholder="请输入招聘专员" />
        </el-form-item>
        <el-form-item label="Offer邮件发送" prop="dictYesNo">
          <el-input v-model="form.dictYesNo" placeholder="请输入Offer邮件发送" />
        </el-form-item>
        <el-form-item label="入职时间" prop="jobInTime">
          <el-date-picker clearable
            v-model="form.jobInTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入职时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜篒D" prop="createId">
          <el-input v-model="form.createId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="鏇存柊浜篒D" prop="updateId">
          <el-input v-model="form.updateId" placeholder="请输入${comment}" />
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
import { listOffer, getOffer, delOffer, addOffer, updateOffer } from "@/api/qixing/offer";

export default {
  name: "Offer",
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
      // Offer发放表格数据
      offerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refJobUsername: null,
        location: null,
        refDeptname: null,
        refUsername1: null,
        dictOfferStatus: null,
        dictYesNo: null,
        jobInTime: null,
        createId: null,
        updateId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refJobUsername: [
          { required: true, message: "入职人员名称不能为空", trigger: "blur" }
        ],
        location: [
          { required: true, message: "工作地点不能为空", trigger: "blur" }
        ],
        refDeptname: [
          { required: true, message: "部门不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询Offer发放列表 */
    getList() {
      this.loading = true;
      listOffer(this.queryParams).then(response => {
        this.offerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        refJobUsername: null,
        location: null,
        refDeptname: null,
        refUsername1: null,
        dictOfferStatus: null,
        dictYesNo: null,
        jobInTime: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加Offer发放";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOffer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改Offer发放";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOffer(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOffer(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除Offer发放编号为"' + ids + '"的数据项？').then(function() {
        return delOffer(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/offer/export', {
        ...this.queryParams
      }, `offer_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
