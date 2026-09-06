<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="供应商ID" prop="supplierId">
        <el-input
          v-model="queryParams.supplierId"
          placeholder="请输入供应商ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职务" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入职务"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别 M/F" prop="gender">
        <el-input
          v-model="queryParams.gender"
          placeholder="请输入性别 M/F"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合作开始日期" prop="startCoopDate">
        <el-date-picker clearable
          v-model="queryParams.startCoopDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择合作开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="电话1" prop="phone1">
        <el-input
          v-model="queryParams.phone1"
          placeholder="请输入电话1"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话2" prop="phone2">
        <el-input
          v-model="queryParams.phone2"
          placeholder="请输入电话2"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="移动电话" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入移动电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电子邮件" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入电子邮件"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="传真" prop="fax">
        <el-input
          v-model="queryParams.fax"
          placeholder="请输入传真"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出生日期" prop="birthday">
        <el-date-picker clearable
          v-model="queryParams.birthday"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择出生日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="爱好" prop="hobby">
        <el-input
          v-model="queryParams.hobby"
          placeholder="请输入爱好"
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
          v-hasPermi="['system:supplierContact:add']"
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
          v-hasPermi="['system:supplierContact:edit']"
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
          v-hasPermi="['system:supplierContact:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:supplierContact:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="supplierContactList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="供应商ID" align="center" prop="supplierId" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="职务" align="center" prop="title" />
      <el-table-column label="性别 M/F" align="center" prop="gender" />
      <el-table-column label="合作开始日期" align="center" prop="startCoopDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startCoopDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电话1" align="center" prop="phone1" />
      <el-table-column label="电话2" align="center" prop="phone2" />
      <el-table-column label="移动电话" align="center" prop="mobile" />
      <el-table-column label="电子邮件" align="center" prop="email" />
      <el-table-column label="传真" align="center" prop="fax" />
      <el-table-column label="出生日期" align="center" prop="birthday" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.birthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="爱好" align="center" prop="hobby" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:supplierContact:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:supplierContact:remove']"
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

    <!-- 添加或修改供货商联系人对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="供应商ID" prop="supplierId">
          <el-input v-model="form.supplierId" placeholder="请输入供应商ID" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="职务" prop="title">
          <el-input v-model="form.title" placeholder="请输入职务" />
        </el-form-item>
        <el-form-item label="性别 M/F" prop="gender">
          <el-input v-model="form.gender" placeholder="请输入性别 M/F" />
        </el-form-item>
        <el-form-item label="合作开始日期" prop="startCoopDate">
          <el-date-picker clearable
            v-model="form.startCoopDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择合作开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="电话1" prop="phone1">
          <el-input v-model="form.phone1" placeholder="请输入电话1" />
        </el-form-item>
        <el-form-item label="电话2" prop="phone2">
          <el-input v-model="form.phone2" placeholder="请输入电话2" />
        </el-form-item>
        <el-form-item label="移动电话" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入移动电话" />
        </el-form-item>
        <el-form-item label="电子邮件" prop="email">
          <el-input v-model="form.email" placeholder="请输入电子邮件" />
        </el-form-item>
        <el-form-item label="传真" prop="fax">
          <el-input v-model="form.fax" placeholder="请输入传真" />
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker clearable
            v-model="form.birthday"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择出生日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="爱好" prop="hobby">
          <el-input v-model="form.hobby" placeholder="请输入爱好" />
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
import { listSupplierContact, getSupplierContact, delSupplierContact, addSupplierContact, updateSupplierContact } from "@/api/system/supplierContact";

export default {
  name: "SupplierContact",
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
      // 供货商联系人表格数据
      supplierContactList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        supplierId: null,
        name: null,
        title: null,
        gender: null,
        startCoopDate: null,
        phone1: null,
        phone2: null,
        mobile: null,
        email: null,
        fax: null,
        birthday: null,
        hobby: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        supplierId: [
          { required: true, message: "供应商ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询供货商联系人列表 */
    getList() {
      this.loading = true;
      listSupplierContact(this.queryParams).then(response => {
        this.supplierContactList = response.rows;
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
        supplierId: null,
        name: null,
        title: null,
        gender: null,
        startCoopDate: null,
        phone1: null,
        phone2: null,
        mobile: null,
        email: null,
        fax: null,
        birthday: null,
        hobby: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加供货商联系人";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSupplierContact(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改供货商联系人";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSupplierContact(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSupplierContact(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除供货商联系人编号为"' + ids + '"的数据项？').then(function() {
        return delSupplierContact(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/supplierContact/export', {
        ...this.queryParams
      }, `supplierContact_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
