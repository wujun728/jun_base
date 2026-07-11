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
      <el-form-item label="姓名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入姓名"
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
      <el-form-item label="职务" prop="postname">
        <el-input
          v-model="queryParams.postname"
          placeholder="请输入职务"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入职日期" prop="entryTime">
        <el-date-picker clearable
          v-model="queryParams.entryTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入职日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="合同结束日期" prop="contractEndTime">
        <el-date-picker clearable
          v-model="queryParams.contractEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择合同结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="项目组" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目组"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作地点" prop="workLocation">
        <el-input
          v-model="queryParams.workLocation"
          placeholder="请输入工作地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="离职原因详细" prop="leanveReasonDetail">
        <el-input
          v-model="queryParams.leanveReasonDetail"
          placeholder="请输入离职原因详细"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否需要离职证明" prop="isNeedLeaveProve">
        <el-input
          v-model="queryParams.isNeedLeaveProve"
          placeholder="请输入是否需要离职证明"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="离职证明领取方式" prop="proveGetWay">
        <el-input
          v-model="queryParams.proveGetWay"
          placeholder="请输入离职证明领取方式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="离职证明邮寄地址" prop="proveGetAdress">
        <el-input
          v-model="queryParams.proveGetAdress"
          placeholder="请输入离职证明邮寄地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收件人" prop="proveGetPeople">
        <el-input
          v-model="queryParams.proveGetPeople"
          placeholder="请输入收件人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收件电话" prop="proveGetPhone">
        <el-input
          v-model="queryParams.proveGetPhone"
          placeholder="请输入收件电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="直接上级替换人工号" prop="leaderUsercode">
        <el-input
          v-model="queryParams.leaderUsercode"
          placeholder="请输入直接上级替换人工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="直接上级替换人姓名" prop="leaderUsername">
        <el-input
          v-model="queryParams.leaderUsername"
          placeholder="请输入直接上级替换人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后工作日" prop="lastWorkDay">
        <el-date-picker clearable
          v-model="queryParams.lastWorkDay"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择最后工作日">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="薪资结算日" prop="payOnLastDay">
        <el-date-picker clearable
          v-model="queryParams.payOnLastDay"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择薪资结算日">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="是否涉及工作交接" prop="isNeedJobHandover">
        <el-input
          v-model="queryParams.isNeedJobHandover"
          placeholder="请输入是否涉及工作交接"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作交接是否完成" prop="isHandoverFilish">
        <el-input
          v-model="queryParams.isHandoverFilish"
          placeholder="请输入工作交接是否完成"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程状态" prop="wfsate">
        <el-input
          v-model="queryParams.wfsate"
          placeholder="请输入流程状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前节点名称" prop="currNodename">
        <el-input
          v-model="queryParams.currNodename"
          placeholder="请输入当前节点名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前节点审批人" prop="currNodeUsername">
        <el-input
          v-model="queryParams.currNodeUsername"
          placeholder="请输入当前节点审批人"
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
          v-hasPermi="['system:dimission:add']"
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
          v-hasPermi="['system:dimission:edit']"
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
          v-hasPermi="['system:dimission:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dimission:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dimissionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="工号" align="center" prop="usercode" />
      <el-table-column label="姓名" align="center" prop="username" />
      <el-table-column label="部门" align="center" prop="deptname" />
      <el-table-column label="职务" align="center" prop="postname" />
      <el-table-column label="员工类型" align="center" prop="usertype" />
      <el-table-column label="入职日期" align="center" prop="entryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.entryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同结束日期" align="center" prop="contractEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目组" align="center" prop="projectName" />
      <el-table-column label="工作地点" align="center" prop="workLocation" />
      <el-table-column label="离职原因分类" align="center" prop="leanveReasonType" />
      <el-table-column label="离职原因详细" align="center" prop="leanveReasonDetail" />
      <el-table-column label="是否需要离职证明" align="center" prop="isNeedLeaveProve" />
      <el-table-column label="离职证明领取方式" align="center" prop="proveGetWay" />
      <el-table-column label="离职证明邮寄地址" align="center" prop="proveGetAdress" />
      <el-table-column label="收件人" align="center" prop="proveGetPeople" />
      <el-table-column label="收件电话" align="center" prop="proveGetPhone" />
      <el-table-column label="直接上级替换人工号" align="center" prop="leaderUsercode" />
      <el-table-column label="直接上级替换人姓名" align="center" prop="leaderUsername" />
      <el-table-column label="最后工作日" align="center" prop="lastWorkDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastWorkDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="薪资结算日" align="center" prop="payOnLastDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payOnLastDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否涉及工作交接" align="center" prop="isNeedJobHandover" />
      <el-table-column label="工作交接是否完成" align="center" prop="isHandoverFilish" />
      <el-table-column label="流程状态" align="center" prop="wfsate" />
      <el-table-column label="当前节点名称" align="center" prop="currNodename" />
      <el-table-column label="当前节点审批人" align="center" prop="currNodeUsername" />
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
            v-hasPermi="['system:dimission:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dimission:remove']"
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

    <!-- 添加或修改离职对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工号" prop="usercode">
          <el-input v-model="form.usercode" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="username">
          <el-input v-model="form.username" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="部门" prop="deptname">
          <el-input v-model="form.deptname" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="职务" prop="postname">
          <el-input v-model="form.postname" placeholder="请输入职务" />
        </el-form-item>
        <el-form-item label="入职日期" prop="entryTime">
          <el-date-picker clearable
            v-model="form.entryTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入职日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="合同结束日期" prop="contractEndTime">
          <el-date-picker clearable
            v-model="form.contractEndTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择合同结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目组" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目组" />
        </el-form-item>
        <el-form-item label="工作地点" prop="workLocation">
          <el-input v-model="form.workLocation" placeholder="请输入工作地点" />
        </el-form-item>
        <el-form-item label="离职原因详细" prop="leanveReasonDetail">
          <el-input v-model="form.leanveReasonDetail" placeholder="请输入离职原因详细" />
        </el-form-item>
        <el-form-item label="是否需要离职证明" prop="isNeedLeaveProve">
          <el-input v-model="form.isNeedLeaveProve" placeholder="请输入是否需要离职证明" />
        </el-form-item>
        <el-form-item label="离职证明领取方式" prop="proveGetWay">
          <el-input v-model="form.proveGetWay" placeholder="请输入离职证明领取方式" />
        </el-form-item>
        <el-form-item label="离职证明邮寄地址" prop="proveGetAdress">
          <el-input v-model="form.proveGetAdress" placeholder="请输入离职证明邮寄地址" />
        </el-form-item>
        <el-form-item label="收件人" prop="proveGetPeople">
          <el-input v-model="form.proveGetPeople" placeholder="请输入收件人" />
        </el-form-item>
        <el-form-item label="收件电话" prop="proveGetPhone">
          <el-input v-model="form.proveGetPhone" placeholder="请输入收件电话" />
        </el-form-item>
        <el-form-item label="直接上级替换人工号" prop="leaderUsercode">
          <el-input v-model="form.leaderUsercode" placeholder="请输入直接上级替换人工号" />
        </el-form-item>
        <el-form-item label="直接上级替换人姓名" prop="leaderUsername">
          <el-input v-model="form.leaderUsername" placeholder="请输入直接上级替换人姓名" />
        </el-form-item>
        <el-form-item label="最后工作日" prop="lastWorkDay">
          <el-date-picker clearable
            v-model="form.lastWorkDay"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最后工作日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="薪资结算日" prop="payOnLastDay">
          <el-date-picker clearable
            v-model="form.payOnLastDay"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择薪资结算日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否涉及工作交接" prop="isNeedJobHandover">
          <el-input v-model="form.isNeedJobHandover" placeholder="请输入是否涉及工作交接" />
        </el-form-item>
        <el-form-item label="工作交接是否完成" prop="isHandoverFilish">
          <el-input v-model="form.isHandoverFilish" placeholder="请输入工作交接是否完成" />
        </el-form-item>
        <el-form-item label="流程状态" prop="wfsate">
          <el-input v-model="form.wfsate" placeholder="请输入流程状态" />
        </el-form-item>
        <el-form-item label="当前节点名称" prop="currNodename">
          <el-input v-model="form.currNodename" placeholder="请输入当前节点名称" />
        </el-form-item>
        <el-form-item label="当前节点审批人" prop="currNodeUsername">
          <el-input v-model="form.currNodeUsername" placeholder="请输入当前节点审批人" />
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
import { listDimission, getDimission, delDimission, addDimission, updateDimission } from "@/api/qixing/dimission";

export default {
  name: "Dimission",
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
      // 离职表格数据
      dimissionList: [],
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
        usertype: null,
        entryTime: null,
        contractEndTime: null,
        projectName: null,
        workLocation: null,
        leanveReasonType: null,
        leanveReasonDetail: null,
        isNeedLeaveProve: null,
        proveGetWay: null,
        proveGetAdress: null,
        proveGetPeople: null,
        proveGetPhone: null,
        leaderUsercode: null,
        leaderUsername: null,
        lastWorkDay: null,
        payOnLastDay: null,
        isNeedJobHandover: null,
        isHandoverFilish: null,
        wfsate: null,
        currNodename: null,
        currNodeUsername: null,
        createId: null,
        updateId: null,
        orderId: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        usercode: [
          { required: true, message: "工号不能为空", trigger: "blur" }
        ],
        username: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        deptname: [
          { required: true, message: "部门不能为空", trigger: "blur" }
        ],
        postname: [
          { required: true, message: "职务不能为空", trigger: "blur" }
        ],
        usertype: [
          { required: true, message: "员工类型不能为空", trigger: "change" }
        ],
        entryTime: [
          { required: true, message: "入职日期不能为空", trigger: "blur" }
        ],
        contractEndTime: [
          { required: true, message: "合同结束日期不能为空", trigger: "blur" }
        ],
        projectName: [
          { required: true, message: "项目组不能为空", trigger: "blur" }
        ],
        workLocation: [
          { required: true, message: "工作地点不能为空", trigger: "blur" }
        ],
        leaderUsercode: [
          { required: true, message: "直接上级替换人工号不能为空", trigger: "blur" }
        ],
        leaderUsername: [
          { required: true, message: "直接上级替换人姓名不能为空", trigger: "blur" }
        ],
        lastWorkDay: [
          { required: true, message: "最后工作日不能为空", trigger: "blur" }
        ],
        payOnLastDay: [
          { required: true, message: "薪资结算日不能为空", trigger: "blur" }
        ],
        isNeedJobHandover: [
          { required: true, message: "是否涉及工作交接不能为空", trigger: "blur" }
        ],
        isHandoverFilish: [
          { required: true, message: "工作交接是否完成不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询离职列表 */
    getList() {
      this.loading = true;
      listDimission(this.queryParams).then(response => {
        this.dimissionList = response.rows;
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
        usertype: null,
        entryTime: null,
        contractEndTime: null,
        projectName: null,
        workLocation: null,
        leanveReasonType: null,
        leanveReasonDetail: null,
        isNeedLeaveProve: null,
        proveGetWay: null,
        proveGetAdress: null,
        proveGetPeople: null,
        proveGetPhone: null,
        leaderUsercode: null,
        leaderUsername: null,
        lastWorkDay: null,
        payOnLastDay: null,
        isNeedJobHandover: null,
        isHandoverFilish: null,
        createTime: null,
        wfsate: null,
        currNodename: null,
        currNodeUsername: null,
        remark: null,
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
      this.title = "添加离职";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDimission(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改离职";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDimission(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDimission(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除离职编号为"' + ids + '"的数据项？').then(function() {
        return delDimission(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dimission/export', {
        ...this.queryParams
      }, `dimission_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
