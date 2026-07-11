<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="92px">
      <el-form-item label="领导" prop="leaderName" label-width="68px">
        <el-input v-model="queryParams.leaderName" placeholder="请输入领导名称" clearable @clear="handleQuery" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="秘书" prop="secretaryName">
        <el-input v-model="queryParams.secretaryName" placeholder="请输入秘书名称" clearable @clear="handleQuery" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="启用状态" prop="enableFlag">
        <el-select v-model="queryParams.enableFlag" placeholder="请选择启用状态" clearable @change="handleQuery">
          <el-option v-for="dict in dict.type.t_workflow_enable_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['worksetting:secretary:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['worksetting:secretary:remove']"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="secretaryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="领导" align="center" prop="leaderName" />
      <el-table-column label="秘书" align="center" prop="secretaryName" />
      <el-table-column label="启用状态" align="center" prop="enableFlag">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enableFlag" active-value="1" inactive-value="0" @change="handleEnableFlagChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['worksetting:secretary:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['worksetting:secretary:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改流程办理秘书对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="领导" prop="leader">
          <form-user-select v-model="form.leader" :checkedUsers="selectedLeader" :multiple="false" :placeholder="'请选择领导'" @input="confimLeader" />
        </el-form-item>
        <el-form-item label="秘书" prop="secretary">
          <form-user-select
            v-model="form.secretary"
            :checkedUsers="selectedSecretary"
            :multiple="false"
            :placeholder="'请选择秘书'"
            @input="confimSecretary"
          />
        </el-form-item>
        <el-form-item label="启用状态" prop="enableFlag">
          <el-select v-model="form.enableFlag" placeholder="请选择启用状态" clearable>
            <el-option v-for="dict in dict.type.t_workflow_enable_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
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
import FormUserSelect from "@/components/form/FormUserSelect.vue";
import { listSecretary, getSecretary, delSecretary, addSecretary, updateSecretary, changeEnableFlag } from "@/api/setting/secretary";

export default {
  name: "Secretary",
  components: { FormUserSelect },
  dicts: ["t_workflow_enable_type"],
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
      // 流程办理秘书表格数据
      secretaryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        leaderName: null,
        secretaryName: null,
        enableFlag: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        leader: [{ required: true, message: "请选择领导", trigger: "change" }],
        secretary: [{ required: true, message: "请选择秘书", trigger: "change" }],
        enableFlag: [{ required: true, message: "请选择状态", trigger: "change" }],
      },
      // 选人弹框
      openSelect: false,
      // 当前已选委托人
      selectedLeader: [],
      // 当前已选被秘书
      selectedSecretary: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 选人确认 */
    confimLeader(selection) {
      this.openSelect = false;
      this.selectedLeader = selection || [];
      this.form.leader = selection && selection.length > 0 ? selection[0] : null;
    },
    confimSecretary(selection) {
      this.openSelect = false;
      this.selectedSecretary = selection || [];
      this.form.secretary = selection && selection.length > 0 ? selection[0] : null;
    },
    /** 查询流程办理秘书列表 */
    getList() {
      this.loading = true;
      listSecretary(this.queryParams).then((response) => {
        this.secretaryList = response.rows;
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
        leader: null,
        secretary: null,
        enableFlag: "1",
      };
      this.resetForm("form");
      this.selectedLeader = [];
      this.selectedSecretary = [];
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加办理秘书";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getSecretary(id).then((res) => {
        if (res.code === 200) {
          this.form = res.data;
          this.open = true;
          this.title = "修改办理秘书";
          this.$nextTick(() => {
            this.selectedLeader = [res.data.leader];
            this.selectedSecretary = [res.data.secretary];
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateSecretary(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSecretary(this.form).then((response) => {
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
        .confirm("是否确认删除此配置？")
        .then(function () {
          return delSecretary(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    // 状态切换
    handleEnableFlagChange(row) {
      let text = row.enableFlag === "0" ? "停用" : "启用";
      this.$modal
        .confirm("确认要" + text + "当前配置吗？")
        .then(function () {
          return changeEnableFlag(row.id, row.enableFlag);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
          this.getList();
        })
        .catch(function () {
          row.enableFlag = row.enableFlag === "0" ? "1" : "0";
        });
    },
  },
};
</script>
