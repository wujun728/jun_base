<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="92px">
      <el-form-item label="被委托人" prop="beEntrustName" label-width="68px">
        <el-input v-model="queryParams.beEntrustName" placeholder="请输入被委托人" clearable @clear="handleQuery" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="委托日期" prop="entrustDates">
        <el-date-picker
          v-model="queryParams.entrustDates"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          @change="handleQuery"
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['worksetting:entrust:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['worksetting:entrust:remove']"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="entrustList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="被委托人" align="left" prop="beEntrustName" width="200">
        <template slot-scope="scope">
          <div class="be-entrust">
            <image-preview :src="scope.row.beEntrust.avatar" :width="30" :height="30" />
            <span class="ml10">{{ scope.row.beEntrust.nickName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="委托日期" align="center" prop="entrustDate" />
      <el-table-column label="委托方式" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_workflow_entrust_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="启用状态" align="center" prop="enableFlag">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enableFlag" active-value="1" inactive-value="0" @change="handleEnableFlagChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['worksetting:entrust:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['worksetting:entrust:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改流程办理委托对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="被委托人" prop="beEntrust">
          <form-user-select v-model="form.beEntrust" :checkedUsers="selectedUsers" :multiple="false" :placeholder="'请选择被委托人'" @input="confimUser" />
        </el-form-item>
        <el-form-item label="委托日期" prop="entrustDates">
          <el-date-picker
            v-model="form.entrustDates"
            style="width: 300px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="启用状态" prop="enableFlag">
          <el-select v-model="form.enableFlag" placeholder="请选择启用状态">
            <el-option v-for="dict in dict.type.t_workflow_enable_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="委托方式" prop="type">
          <el-select v-model="form.type" placeholder="请选择委托方式">
            <el-option v-for="dict in dict.type.t_workflow_entrust_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="适用模板" prop="templateIds" v-show="form.type === '1'">
          <el-select v-model="form.templateIdList" size="medium" multiple filterable style="width:100%" placeholder="请选择模板">
            <el-option v-for="item in templateList" :key="item.templateId" :label="item.templateName" :value="item.templateId"></el-option>
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
import { listEntrust, getEntrust, delEntrust, addEntrust, updateEntrust, changeEnableFlag } from "@/api/setting/entrust";
import { getSelectTemplateList } from "@/api/workflow/template";

export default {
  name: "Entrust",
  components: { FormUserSelect },
  dicts: ["t_workflow_entrust_type", "t_workflow_enable_type"],
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
      // 流程办理委托表格数据
      entrustList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        beEntrustName: null,
        entrustDates: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        beEntrust: [{ required: true, message: "请选择被委托人", trigger: "change" }],
        type: [{ required: true, message: "请选择委托方式", trigger: "change" }],
        entrustDates: [{ required: true, message: "请选择委托日期", trigger: "change" }],
        enableFlag: [{ required: true, message: "请选择状态", trigger: "change" }],
      },
      // 当前选择人
      selectedUsers: [],
      // 模板列表
      templateList: [],
    };
  },
  created() {
    this.getTemplateList();
    this.getList();
  },
  methods: {
    /** 选人确认 */
    confimUser(selection) {
      this.openSelect = false;
      this.selectedUsers = selection || [];
      this.form.beEntrust = selection && selection.length > 0 ? selection[0] : null;
    },
    // 获取所有模板
    async getTemplateList() {
      getSelectTemplateList().then((res) => {
        this.templateList = this.handleTree(res.data, "templateId");
      });
    },
    /** 查询流程办理委托列表 */
    getList() {
      this.loading = true;
      listEntrust(this.queryParams).then((response) => {
        this.entrustList = response.rows;
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
        beEntrust: null,
        entrustDates: [],
        type: "0",
        templateIds: null,
        enableFlag: "1",
      };
      this.resetForm("form");
      this.selectedUsers = null;
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
      this.title = "添加委托替办";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getEntrust(id).then((res) => {
        if (res.code === 200) {
          this.form = res.data;
          this.open = true;
          this.title = "修改委托替办";
          this.$nextTick(() => {
            this.selectedUsers = [res.data.beEntrust];
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateEntrust(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEntrust(this.form).then((response) => {
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
        .confirm("是否确认删除？")
        .then(function () {
          return delEntrust(ids);
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

<style lang="scss" scoped>
.be-entrust {
  display: flex;
  align-items: center;
}
</style>