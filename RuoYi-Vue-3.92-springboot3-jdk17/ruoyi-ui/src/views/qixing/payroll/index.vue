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
      <el-form-item label="部门" prop="deptname">
        <el-input
          v-model="queryParams.deptname"
          placeholder="请输入部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="岗位" prop="workrole">
        <el-input
          v-model="queryParams.workrole"
          placeholder="请输入岗位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属月份" prop="month">
        <el-input
          v-model="queryParams.month"
          placeholder="请输入所属月份"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实发工资" prop="payrollAct">
        <el-input
          v-model="queryParams.payrollAct"
          placeholder="请输入实发工资"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工资期间" prop="payrollDatatimeSe">
        <el-input
          v-model="queryParams.payrollDatatimeSe"
          placeholder="请输入工资期间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标准月薪" prop="payrollBase">
        <el-input
          v-model="queryParams.payrollBase"
          placeholder="请输入标准月薪"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本月月薪" prop="payrollCurMouth">
        <el-input
          v-model="queryParams.payrollCurMouth"
          placeholder="请输入本月月薪"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="税前补发" prop="taxBeforeBufa">
        <el-input
          v-model="queryParams.taxBeforeBufa"
          placeholder="请输入税前补发"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="税前扣款" prop="taxBeforeKoukuan">
        <el-input
          v-model="queryParams.taxBeforeKoukuan"
          placeholder="请输入税前扣款"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请假小时数" prop="hoursQingjia">
        <el-input
          v-model="queryParams.hoursQingjia"
          placeholder="请输入请假小时数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="缺勤小时数" prop="hoursQueqing">
        <el-input
          v-model="queryParams.hoursQueqing"
          placeholder="请输入缺勤小时数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收入" prop="moneyAllIn">
        <el-input
          v-model="queryParams.moneyAllIn"
          placeholder="请输入收入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="社保扣款" prop="sbKoukuan">
        <el-input
          v-model="queryParams.sbKoukuan"
          placeholder="请输入社保扣款"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公积金扣款" prop="gjjKoukuan">
        <el-input
          v-model="queryParams.gjjKoukuan"
          placeholder="请输入公积金扣款"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="个税" prop="geshui">
        <el-input
          v-model="queryParams.geshui"
          placeholder="请输入个税"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="税后补发" prop="taxAfterBufa">
        <el-input
          v-model="queryParams.taxAfterBufa"
          placeholder="请输入税后补发"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="税后扣款" prop="taxAfterKoukuan">
        <el-input
          v-model="queryParams.taxAfterKoukuan"
          placeholder="请输入税后扣款"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际出勤天数" prop="workDayCount">
        <el-input
          v-model="queryParams.workDayCount"
          placeholder="请输入实际出勤天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当月应出勤天数" prop="sholdbeWorkDayCount">
        <el-input
          v-model="queryParams.sholdbeWorkDayCount"
          placeholder="请输入当月应出勤天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作时长(小时)" prop="workTotalHours">
        <el-input
          v-model="queryParams.workTotalHours"
          placeholder="请输入工作时长(小时)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="应出勤时长(小时)" prop="shoulbeWorkHours">
        <el-input
          v-model="queryParams.shoulbeWorkHours"
          placeholder="请输入应出勤时长(小时)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工资审核状态" prop="payrollState">
        <el-input
          v-model="queryParams.payrollState"
          placeholder="请输入工资审核状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工资审核意见" prop="payrollStateMsg">
        <el-input
          v-model="queryParams.payrollStateMsg"
          placeholder="请输入工资审核意见"
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
      <el-form-item label="${comment}" prop="deleted">
        <el-input
          v-model="queryParams.deleted"
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
          v-hasPermi="['system:payroll:add']"
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
          v-hasPermi="['system:payroll:edit']"
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
          v-hasPermi="['system:payroll:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:payroll:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payrollList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="${comment}" align="center" prop="id" />
      <el-table-column label="工号" align="center" prop="usercode" />
      <el-table-column label="用户名称" align="center" prop="refUsername" />
      <el-table-column label="部门" align="center" prop="deptname" />
      <el-table-column label="岗位" align="center" prop="workrole" />
      <el-table-column label="所属月份" align="center" prop="month" />
      <el-table-column label="实发工资" align="center" prop="payrollAct" />
      <el-table-column label="工资期间" align="center" prop="payrollDatatimeSe" />
      <el-table-column label="标准月薪" align="center" prop="payrollBase" />
      <el-table-column label="本月月薪" align="center" prop="payrollCurMouth" />
      <el-table-column label="税前补发" align="center" prop="taxBeforeBufa" />
      <el-table-column label="税前扣款" align="center" prop="taxBeforeKoukuan" />
      <el-table-column label="请假小时数" align="center" prop="hoursQingjia" />
      <el-table-column label="缺勤小时数" align="center" prop="hoursQueqing" />
      <el-table-column label="收入" align="center" prop="moneyAllIn" />
      <el-table-column label="社保扣款" align="center" prop="sbKoukuan" />
      <el-table-column label="公积金扣款" align="center" prop="gjjKoukuan" />
      <el-table-column label="个税" align="center" prop="geshui" />
      <el-table-column label="税后补发" align="center" prop="taxAfterBufa" />
      <el-table-column label="税后扣款" align="center" prop="taxAfterKoukuan" />
      <el-table-column label="实际出勤天数" align="center" prop="workDayCount" />
      <el-table-column label="当月应出勤天数" align="center" prop="sholdbeWorkDayCount" />
      <el-table-column label="工作时长(小时)" align="center" prop="workTotalHours" />
      <el-table-column label="应出勤时长(小时)" align="center" prop="shoulbeWorkHours" />
      <el-table-column label="工资审核状态" align="center" prop="payrollState" />
      <el-table-column label="工资审核意见" align="center" prop="payrollStateMsg" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="deleted" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:payroll:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:payroll:remove']"
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

    <!-- 添加或修改工资审核发放对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工号" prop="usercode">
          <el-input v-model="form.usercode" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="用户名称" prop="refUsername">
          <el-input v-model="form.refUsername" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="部门" prop="deptname">
          <el-input v-model="form.deptname" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="岗位" prop="workrole">
          <el-input v-model="form.workrole" placeholder="请输入岗位" />
        </el-form-item>
        <el-form-item label="所属月份" prop="month">
          <el-input v-model="form.month" placeholder="请输入所属月份" />
        </el-form-item>
        <el-form-item label="实发工资" prop="payrollAct">
          <el-input v-model="form.payrollAct" placeholder="请输入实发工资" />
        </el-form-item>
        <el-form-item label="工资期间" prop="payrollDatatimeSe">
          <el-input v-model="form.payrollDatatimeSe" placeholder="请输入工资期间" />
        </el-form-item>
        <el-form-item label="标准月薪" prop="payrollBase">
          <el-input v-model="form.payrollBase" placeholder="请输入标准月薪" />
        </el-form-item>
        <el-form-item label="本月月薪" prop="payrollCurMouth">
          <el-input v-model="form.payrollCurMouth" placeholder="请输入本月月薪" />
        </el-form-item>
        <el-form-item label="税前补发" prop="taxBeforeBufa">
          <el-input v-model="form.taxBeforeBufa" placeholder="请输入税前补发" />
        </el-form-item>
        <el-form-item label="税前扣款" prop="taxBeforeKoukuan">
          <el-input v-model="form.taxBeforeKoukuan" placeholder="请输入税前扣款" />
        </el-form-item>
        <el-form-item label="请假小时数" prop="hoursQingjia">
          <el-input v-model="form.hoursQingjia" placeholder="请输入请假小时数" />
        </el-form-item>
        <el-form-item label="缺勤小时数" prop="hoursQueqing">
          <el-input v-model="form.hoursQueqing" placeholder="请输入缺勤小时数" />
        </el-form-item>
        <el-form-item label="收入" prop="moneyAllIn">
          <el-input v-model="form.moneyAllIn" placeholder="请输入收入" />
        </el-form-item>
        <el-form-item label="社保扣款" prop="sbKoukuan">
          <el-input v-model="form.sbKoukuan" placeholder="请输入社保扣款" />
        </el-form-item>
        <el-form-item label="公积金扣款" prop="gjjKoukuan">
          <el-input v-model="form.gjjKoukuan" placeholder="请输入公积金扣款" />
        </el-form-item>
        <el-form-item label="个税" prop="geshui">
          <el-input v-model="form.geshui" placeholder="请输入个税" />
        </el-form-item>
        <el-form-item label="税后补发" prop="taxAfterBufa">
          <el-input v-model="form.taxAfterBufa" placeholder="请输入税后补发" />
        </el-form-item>
        <el-form-item label="税后扣款" prop="taxAfterKoukuan">
          <el-input v-model="form.taxAfterKoukuan" placeholder="请输入税后扣款" />
        </el-form-item>
        <el-form-item label="实际出勤天数" prop="workDayCount">
          <el-input v-model="form.workDayCount" placeholder="请输入实际出勤天数" />
        </el-form-item>
        <el-form-item label="当月应出勤天数" prop="sholdbeWorkDayCount">
          <el-input v-model="form.sholdbeWorkDayCount" placeholder="请输入当月应出勤天数" />
        </el-form-item>
        <el-form-item label="工作时长(小时)" prop="workTotalHours">
          <el-input v-model="form.workTotalHours" placeholder="请输入工作时长(小时)" />
        </el-form-item>
        <el-form-item label="应出勤时长(小时)" prop="shoulbeWorkHours">
          <el-input v-model="form.shoulbeWorkHours" placeholder="请输入应出勤时长(小时)" />
        </el-form-item>
        <el-form-item label="工资审核状态" prop="payrollState">
          <el-input v-model="form.payrollState" placeholder="请输入工资审核状态" />
        </el-form-item>
        <el-form-item label="工资审核意见" prop="payrollStateMsg">
          <el-input v-model="form.payrollStateMsg" placeholder="请输入工资审核意见" />
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
        <el-form-item label="${comment}" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入${comment}" />
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
import { listPayroll, getPayroll, delPayroll, addPayroll, updatePayroll } from "@/api/qixing/payroll";

export default {
  name: "Payroll",
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
      // 工资审核发放表格数据
      payrollList: [],
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
        deptname: null,
        workrole: null,
        month: null,
        payrollAct: null,
        payrollDatatimeSe: null,
        payrollBase: null,
        payrollCurMouth: null,
        taxBeforeBufa: null,
        taxBeforeKoukuan: null,
        hoursQingjia: null,
        hoursQueqing: null,
        moneyAllIn: null,
        sbKoukuan: null,
        gjjKoukuan: null,
        geshui: null,
        taxAfterBufa: null,
        taxAfterKoukuan: null,
        workDayCount: null,
        sholdbeWorkDayCount: null,
        workTotalHours: null,
        shoulbeWorkHours: null,
        payrollState: null,
        payrollStateMsg: null,
        createId: null,
        updateId: null,
        deleted: null,
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
        refUsername: [
          { required: true, message: "用户名称不能为空", trigger: "blur" }
        ],
        month: [
          { required: true, message: "所属月份不能为空", trigger: "blur" }
        ],
        payrollAct: [
          { required: true, message: "实发工资不能为空", trigger: "blur" }
        ],
        workDayCount: [
          { required: true, message: "实际出勤天数不能为空", trigger: "blur" }
        ],
        sholdbeWorkDayCount: [
          { required: true, message: "当月应出勤天数不能为空", trigger: "blur" }
        ],
        workTotalHours: [
          { required: true, message: "工作时长(小时)不能为空", trigger: "blur" }
        ],
        shoulbeWorkHours: [
          { required: true, message: "应出勤时长(小时)不能为空", trigger: "blur" }
        ],
        payrollState: [
          { required: true, message: "工资审核状态不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询工资审核发放列表 */
    getList() {
      this.loading = true;
      listPayroll(this.queryParams).then(response => {
        this.payrollList = response.rows;
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
        deptname: null,
        workrole: null,
        month: null,
        payrollAct: null,
        payrollDatatimeSe: null,
        payrollBase: null,
        payrollCurMouth: null,
        taxBeforeBufa: null,
        taxBeforeKoukuan: null,
        hoursQingjia: null,
        hoursQueqing: null,
        moneyAllIn: null,
        sbKoukuan: null,
        gjjKoukuan: null,
        geshui: null,
        taxAfterBufa: null,
        taxAfterKoukuan: null,
        workDayCount: null,
        sholdbeWorkDayCount: null,
        workTotalHours: null,
        shoulbeWorkHours: null,
        payrollState: null,
        payrollStateMsg: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null,
        deleted: null,
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
      this.title = "添加工资审核发放";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPayroll(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工资审核发放";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePayroll(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPayroll(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除工资审核发放编号为"' + ids + '"的数据项？').then(function() {
        return delPayroll(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/payroll/export', {
        ...this.queryParams
      }, `payroll_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
