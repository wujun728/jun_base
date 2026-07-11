<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="92px">
      <el-form-item label="标题" prop="title" label-width="68px">
        <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="启用状态" prop="enableFlag">
        <el-select v-model="queryParams.enableFlag" placeholder="请选择" clearable>
          <el-option v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['serial:config:add']">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" :search="false" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号名称" align="center" prop="title" />
      <el-table-column label="最新流水号" align="center" prop="currentSeq" width="160" />
      <el-table-column label="启用状态" align="center" prop="enableFlag" width="160">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enableFlag" active-value="1" inactive-value="0" @change="handleEnableFlagChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="220" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['serial:config:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['serial:config:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改编号配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="128px">
        <!-- 基础信息部分 -->
        <el-form-item label="编号名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="启用状态" prop="enableFlag">
              <el-select v-model="form.enableFlag" placeholder="请选择">
                <el-option v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <!-- 编号规则部分 -->
        <el-card class="box-card">
          <div slot="header" class="card-header">
            <span class="header-title">编号规则</span>
            <el-button type="primary" size="mini" @click="addRule">新增规则</el-button>
          </div>
          <div v-for="(rule, index) in form.rules" :key="rule.key" class="rule-item">
            <el-form-item
              label-width="68px"
              :label="'规则' + (index + 1)"
              :prop="'rules.' + index + '.type'"
              :rules="{ required: true, message: '请选择类型', trigger: 'change' }"
            >
              <el-select v-model="rule.type" placeholder="类型">
                <el-option v-for="item in ruleTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item label-width="12px" :prop="'rules.' + index + '.value'" :rules="{ required: true, message: '请输入值', trigger: 'blur' }">
              <span>
                <el-tooltip :content="getTooltipText(rule.type)" placement="top">
                  <i class="el-icon-question mr10"></i>
                </el-tooltip>
              </span>
              <el-input v-model="rule.value" @input="handleRuleValue" :placeholder="getPlaceholder(rule.type)" :style="getWidth(rule.type)" />
              <span v-if="rule.type == '2'" class="ml10">
                <el-checkbox v-model="rule.padZero" @change="changePadZero(rule.padZero)">是否补0</el-checkbox>
                <el-tooltip content="勾选后，不足位的补0，例如：流水号长度填写5，当前序号为1，则补0结果为：00001" placement="top">
                  <i class="el-icon-question ml5"></i>
                </el-tooltip>
              </span>
              <el-select v-if="rule.type == '2'" v-model="rule.seqResetType" placeholder="重置方式" class="ml10" style="width: 100px;">
                <el-option v-for="item in resetTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <span v-if="rule.type == '2'">
                <el-tooltip content="流水号重置方式，不重置：流水号一直递增；按天：每天都从1开始递增；按周：每周一从1开始递增，以此类推" placement="top">
                  <i class="el-icon-question ml5"></i>
                </el-tooltip>
              </span>
            </el-form-item>
            <el-button
              v-if="form.rules.length > 1"
              type="danger"
              icon="el-icon-delete"
              circle
              size="mini"
              class="rule-remove"
              @click="removeRule(index)"
            />
          </div>
          <div class="card-header">
            <span class="example">效果示例：{{ example }}</span>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listConfig, getConfig, delConfig, addConfig, updateConfig, changeEnableFlag } from "@/api/serial/config";

export default {
  name: "SerialConfig",
  dicts: ["sys_yes_or_no"],
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
      // 编号配置表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 更新人时间范围
      daterangeCreateTime: [],
      // 输入提示
      placeholderMap: {
        0: "请输入内容",
        1: "格式：yyyyMMddHHmmss",
        2: "流水号长度",
      },
      // 提示内容
      tooltipMap: {
        0: "固定值，直接拼接，不会进行额外处理",
        1: "yyyy：年，MM：月，dd：日，HH：时，mm：分，ss：秒，例如：20250101，则填写：yyyyMMdd",
        2: "设置流水号长度，最大不超过10位，例如：输入5，则最大为99999，最小为1",
      },
      // 规则示例
      example: "",
      // 规则类型
      ruleTypes: [
        { label: "固定值", value: "0" },
        { label: "日期", value: "1" },
        { label: "流水号", value: "2" },
      ],
      // 重置方式
      resetTypes: [
        { label: "不重置", value: "0" },
        { label: "按天", value: "1" },
        { label: "按周", value: "2" },
        { label: "按月", value: "3" },
        { label: "按年", value: "4" },
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        enableFlag: null,
        createTime: null,
      },
      // 表单参数
      form: {
        id: null,
        title: null,
        enableFlag: "0",
        remark: null,
        rules: [
          {
            type: "",
            value: "",
            key: Date.now(),
          },
        ],
      },
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        enableFlag: [{ required: true, message: "请选择状态", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 新增规则
    addRule() {
      this.form.rules.push({
        type: "",
        value: "",
        key: Date.now(),
      });
    },
    // 删除规则
    removeRule(index) {
      this.form.rules.splice(index, 1);
    },
    // 处理示例
    handleRuleValue(value) {
      let exampleDesc = "";
      this.form.rules.map((item) => {
        if (item.type == "0") {
          // 固定值
          exampleDesc += item.value;
        } else if (item.type == "1") {
          // 日期
          let dateDesc = "";
          if (item.value === "yyyy") {
            dateDesc = "2025";
          } else if (item.value === "yyyyMM") {
            dateDesc = "202501";
          } else if (item.value === "yyyyMMdd") {
            dateDesc = "20250101";
          } else if (item.value === "yyyyMMddHH") {
            dateDesc = "2025010101";
          } else if (item.value === "yyyyMMddHHmm") {
            dateDesc = "202501010101";
          } else if (item.value === "yyyyMMddHHmmss") {
            dateDesc = "20250101010101";
          }
          exampleDesc += dateDesc;
        } else if (item.type == "2") {
          if (item.value) {
            // 流水号
            let seqLen = parseInt(item.value);
            if (seqLen > 10) {
              this.$modal.msgError("流水号长度超过限制！");
              return;
            }
            if (item.padZero) {
              exampleDesc += this.generateZeroString(item.value) + "1";
            } else {
              exampleDesc += "1";
            }
          }
        }
      });
      this.example = exampleDesc;
    },
    getWidth(type) {
      let width = "330px;";
      if (type == "2") {
        width = "100px;";
      }
      return "width:" + width;
    },
    // 提示内容
    getPlaceholder(type) {
      return this.placeholderMap[type] || "请输入";
    },
    // 提示内容
    getTooltipText(type) {
      return this.tooltipMap[type] || "选择类型后，出现对应提示内容";
    },
    // 补0事件
    changePadZero(val) {
      this.handleRuleValue();
    },
    // 补0
    generateZeroString(length) {
      const validLength = Math.max(0, parseInt(length - 1) || 0);
      if (validLength > 10) {
        return "0".repeat(10);
      }
      return "0".repeat(validLength);
    },
    /** 查询编号配置列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && "" != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listConfig(this.queryParams).then((response) => {
        this.configList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 状态切换
    handleEnableFlagChange(row) {
      let text = row.enableFlag === "0" ? "停用" : "启用";
      this.$modal
        .confirm('确认要"' + text + '"当前配置吗？')
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
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        enableFlag: "0",
        remark: null,
        rules: [
          {
            type: "",
            value: "",
            padZero: false,
            seqResetType: "",
            key: Date.now(),
          },
        ],
      };
      this.example = "";
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
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
      this.title = "添加编号配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getConfig(id).then((response) => {
        this.form = response.data;
        if (response.data.rules) {
          this.form.rules = response.data.rules.map((item) => ({
            type: item.ruleType,
            value: item.ruleValue,
            padZero: item.padZero == "1" ? true : false,
            seqResetType: item.seqResetType,
            key: Date.now() + Math.floor(Math.random() * 100),
          }));
          this.handleRuleValue();
        }
        this.open = true;
        this.title = "修改编号配置";
        this.loading = false;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // 处理提交数据
          const submitData = {
            ...this.form,
            // 转换规则数据结构（根据后端需求）
            rules: this.form.rules.map((rule) => ({
              ruleType: rule.type,
              ruleValue: rule.value,
              padZero: rule.padZero == true ? "1" : "0",
              seqResetType: rule.seqResetType,
            })),
          };
          if (submitData.id != null) {
            updateConfig(submitData).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(submitData).then((response) => {
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
        .confirm("是否确认删除此编号规则？")
        .then(function () {
          return delConfig(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  },
};
</script>
<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-title {
  font-size: 14px;
  font-weight: 600;
}
.rule-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding-top: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
.rule-remove {
  margin-left: 12px;
  margin-bottom: 22px;
}
.example {
  color: #697a9c;
  font-size: 14px;
  font-weight: 600;
}
</style>