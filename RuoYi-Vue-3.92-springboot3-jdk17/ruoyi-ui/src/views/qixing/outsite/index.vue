<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工号" prop="usercode">
        <el-input
          v-model="queryParams.usercode"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="refUsername">
        <el-input
          v-model="queryParams.refUsername"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="外出日期" prop="workDay">
        <el-date-picker clearable
          v-model="queryParams.workDay"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择外出日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="开始时间" prop="beginTime">
        <el-date-picker clearable
          v-model="queryParams.beginTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="外出时长" prop="workTotalTime">
        <el-input
          v-model="queryParams.workTotalTime"
          placeholder="请输入外出时长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批状态" prop="dictWfstateOutsite">
        <el-input
          v-model="queryParams.dictWfstateOutsite"
          placeholder="请输入审批状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前处理人" prop="currTodo">
        <el-input
          v-model="queryParams.currTodo"
          placeholder="请输入当前处理人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="填报人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入填报人"
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
      <el-form-item label="鍒涘缓浜篒D" prop="createId">
        <el-input
          v-model="queryParams.createId"
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
          v-hasPermi="['system:outsite:add']"
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
          v-hasPermi="['system:outsite:edit']"
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
          v-hasPermi="['system:outsite:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:outsite:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outsiteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工号" align="center" prop="usercode" />
      <el-table-column label="用户名称" align="center" prop="refUsername" />
      <el-table-column label="外出日期" align="center" prop="workDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="外出事由(出差、拜访客户)" align="center" prop="outsiteDesc" />
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
      <el-table-column label="外出时长" align="center" prop="workTotalTime" />
      <el-table-column label="审批状态" align="center" prop="dictWfstateOutsite" />
      <el-table-column label="当前处理人" align="center" prop="currTodo" />
      <el-table-column label="填报人" align="center" prop="creator" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:outsite:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:outsite:remove']"
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

    <!-- 添加或修改外出信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="id">
          <el-input v-model="form.id" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="工号" prop="usercode">
          <el-input v-model="form.usercode" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="用户名称" prop="refUsername">
          <el-input v-model="form.refUsername" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="外出日期" prop="workDay">
          <el-date-picker clearable
            v-model="form.workDay"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择外出日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="外出事由(出差、拜访客户)" prop="outsiteDesc">
          <el-input v-model="form.outsiteDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTime">
          <el-date-picker clearable
            v-model="form.beginTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="外出时长" prop="workTotalTime">
          <el-input v-model="form.workTotalTime" placeholder="请输入外出时长" />
        </el-form-item>
        <el-form-item label="审批状态" prop="dictWfstateOutsite">
          <el-input v-model="form.dictWfstateOutsite" placeholder="请输入审批状态" />
        </el-form-item>
        <el-form-item label="当前处理人" prop="currTodo">
          <el-input v-model="form.currTodo" placeholder="请输入当前处理人" />
        </el-form-item>
        <el-form-item label="填报人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入填报人" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="鏇存柊浜篒D" prop="updateId">
          <el-input v-model="form.updateId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="娴佺▼ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜篒D" prop="createId">
          <el-input v-model="form.createId" placeholder="请输入${comment}" />
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
import { listOutsite, getOutsite, delOutsite, addOutsite, updateOutsite } from "@/api/qixing/outsite";

export default {
  name: "Outsite",
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
      // 外出信息表格数据
      outsiteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        usercode: null,
        refUsername: null,
        workDay: null,
        outsiteDesc: null,
        beginTime: null,
        endTime: null,
        workTotalTime: null,
        dictWfstateOutsite: null,
        currTodo: null,
        creator: null,
        updateId: null,
        orderId: null,
        orderStatus: null,
        createId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        usercode: [
          { required: true, message: "工号不能为空", trigger: "blur" }
        ],
        refUsername: [
          { required: true, message: "用户名称不能为空", trigger: "blur" }
        ],
        workDay: [
          { required: true, message: "外出日期不能为空", trigger: "blur" }
        ],
        outsiteDesc: [
          { required: true, message: "外出事由(出差、拜访客户)不能为空", trigger: "blur" }
        ],
        beginTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        workTotalTime: [
          { required: true, message: "外出时长不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询外出信息列表 */
    getList() {
      this.loading = true;
      listOutsite(this.queryParams).then(response => {
        this.outsiteList = response.rows;
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
        refUsername: null,
        workDay: null,
        outsiteDesc: null,
        beginTime: null,
        endTime: null,
        workTotalTime: null,
        dictWfstateOutsite: null,
        currTodo: null,
        creator: null,
        remark: null,
        createTime: null,
        updateTime: null,
        updateId: null,
        orderId: null,
        orderStatus: null,
        createId: null
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
      this.title = "添加外出信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOutsite(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改外出信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOutsite(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOutsite(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除外出信息编号为"' + ids + '"的数据项？').then(function() {
        return delOutsite(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/outsite/export', {
        ...this.queryParams
      }, `outsite_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
