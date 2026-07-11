<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工工号" prop="usercode">
        <el-input
          v-model="queryParams.usercode"
          placeholder="请输入员工工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="员工姓名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入员工姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门" prop="deptname">
        <el-input
          v-model="queryParams.deptname"
          placeholder="请输入部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="岗位" prop="postname">
        <el-input
          v-model="queryParams.postname"
          placeholder="请输入岗位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="试用期开始时间" prop="starttime1">
        <el-date-picker clearable
          v-model="queryParams.starttime1"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择试用期开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="试用期结束时间" prop="endtime1">
        <el-date-picker clearable
          v-model="queryParams.endtime1"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择试用期结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际转正时间" prop="acttime2">
        <el-date-picker clearable
          v-model="queryParams.acttime2"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择实际转正时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="转正结论" prop="dictBecomeMember">
        <el-input
          v-model="queryParams.dictBecomeMember"
          placeholder="请输入转正结论"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
      <el-form-item label="娴佺▼ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
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
          v-hasPermi="['system:member:add']"
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
          v-hasPermi="['system:member:edit']"
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
          v-hasPermi="['system:member:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:member:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="员工工号" align="center" prop="usercode" />
      <el-table-column label="员工姓名" align="center" prop="username" />
      <el-table-column label="部门" align="center" prop="deptname" />
      <el-table-column label="岗位" align="center" prop="postname" />
      <el-table-column label="试用期开始时间" align="center" prop="starttime1" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.starttime1, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="试用期结束时间" align="center" prop="endtime1" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endtime1, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际转正时间" align="center" prop="acttime2" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.acttime2, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="试用期导师评价" align="center" prop="techDesc" />
      <el-table-column label="试用期领导评价" align="center" prop="leaderDesc" />
      <el-table-column label="转正评价" align="center" prop="becomeMemDesc" />
      <el-table-column label="转正结论" align="center" prop="dictBecomeMember" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:member:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:member:remove']"
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

    <!-- 添加或修改转正对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="员工工号" prop="usercode">
          <el-input v-model="form.usercode" placeholder="请输入员工工号" />
        </el-form-item>
        <el-form-item label="员工姓名" prop="username">
          <el-input v-model="form.username" placeholder="请输入员工姓名" />
        </el-form-item>
        <el-form-item label="部门" prop="deptname">
          <el-input v-model="form.deptname" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="岗位" prop="postname">
          <el-input v-model="form.postname" placeholder="请输入岗位" />
        </el-form-item>
        <el-form-item label="试用期开始时间" prop="starttime1">
          <el-date-picker clearable
            v-model="form.starttime1"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择试用期开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="试用期结束时间" prop="endtime1">
          <el-date-picker clearable
            v-model="form.endtime1"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择试用期结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实际转正时间" prop="acttime2">
          <el-date-picker clearable
            v-model="form.acttime2"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择实际转正时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="试用期导师评价" prop="techDesc">
          <el-input v-model="form.techDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="试用期领导评价" prop="leaderDesc">
          <el-input v-model="form.leaderDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="转正评价" prop="becomeMemDesc">
          <el-input v-model="form.becomeMemDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="转正结论" prop="dictBecomeMember">
          <el-input v-model="form.dictBecomeMember" placeholder="请输入转正结论" />
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
        <el-form-item label="娴佺▼ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入${comment}" />
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
import { listMember, getMember, delMember, addMember, updateMember } from "@/api/qixing/member";

export default {
  name: "Member",
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
      // 转正表格数据
      memberList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        usercode: null,
        username: null,
        deptname: null,
        postname: null,
        starttime1: null,
        endtime1: null,
        acttime2: null,
        techDesc: null,
        leaderDesc: null,
        becomeMemDesc: null,
        dictBecomeMember: null,
        createId: null,
        updateId: null,
        orderId: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: "员工姓名不能为空", trigger: "blur" }
        ],
        deptname: [
          { required: true, message: "部门不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询转正列表 */
    getList() {
      this.loading = true;
      listMember(this.queryParams).then(response => {
        this.memberList = response.rows;
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
        usercode: null,
        username: null,
        deptname: null,
        postname: null,
        starttime1: null,
        endtime1: null,
        acttime2: null,
        techDesc: null,
        leaderDesc: null,
        becomeMemDesc: null,
        dictBecomeMember: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null,
        orderId: null,
        orderStatus: null
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
      this.title = "添加转正";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMember(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改转正";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMember(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMember(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除转正编号为"' + ids + '"的数据项？').then(function() {
        return delMember(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/member/export', {
        ...this.queryParams
      }, `member_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
