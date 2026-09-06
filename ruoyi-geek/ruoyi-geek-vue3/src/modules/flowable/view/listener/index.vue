<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="监听类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="请选择监听类型" clearable>
            <el-option v-for="dict in sys_listener_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
          <el-button icon="refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt10">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="plus" @click="handleAdd"
            v-hasPermi="['system:listener:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="edit" :disabled="single" @click="handleUpdate"
            v-hasPermi="['system:listener:edit']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="delete" :disabled="multiple" @click="handleDelete"
            v-hasPermi="['system:listener:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="download" @click="handleExport"
            v-hasPermi="['system:listener:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-alert title="流程监听使用说明" type="success">
        <div>流程监听：</div>
        <div>1.任务监听与用户任务关联，主要用于监听用户任务生命周期中的各种事件，包括任务的创建（create）、完成（complete）、分配（assignment）、删除（delete）。</div>
        <div>2.执行监听关注整个流程实例的执行过程，包括进入（start）、离开（end）、取回（take）。</div>
        <div>值类型：</div>
        <div>1.表达式可以使用常量或通过${}使用流程中的变量和SpEL表达式。</div>
        <div>2.代理表达式内容填写实现了表达式接口（org.flowable.engine.delegate.JavaDelegate）的Bean的名称，并且用${}包裹起来。</div>
        <div>
          3.JAVA类写实现了执行监听器接口（org.flowable.engine.delegate.ExecutionListener）或任务监听器接口（org.flowable.engine.delegate.TaskListener）的类全路径。
        </div>
      </el-alert>
      <el-table v-loading="loading" :data="listenerList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="名称" align="center" prop="name" />
        <el-table-column label="监听类型" align="center" prop="type">
          <template v-slot="scope">
            <dict-tag :options="sys_listener_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column label="事件类型" align="center" prop="eventType" />
        <el-table-column label="值类型" align="center" prop="valueType">
          <template v-slot="scope">
            <dict-tag :options="sys_listener_value_type" :value="scope.row.valueType" />
          </template>
        </el-table-column>
        <el-table-column label="执行内容" align="center" prop="value" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template v-slot="scope">
            <el-button link type="primary" icon="edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['system:listener:edit']">修改</el-button>
            <el-button link type="primary" icon="delete" @click="handleDelete(scope.row)"
              v-hasPermi="['system:listener:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 添加或修改流程监听对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="监听类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择监听类型">
            <el-option v-for="dict in sys_listener_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="eventType" v-if="form.type === '1'">
          <el-select v-model="form.eventType" placeholder="请选择事件类型">
            <el-option v-for="dict in taskListenerEventList" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="eventType" v-else>
          <el-select v-model="form.eventType" placeholder="请选择事件类型">
            <el-option v-for="dict in executionListenerEventList" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="值类型" prop="valueType">
          <el-radio-group v-model="form.valueType">
            <el-radio v-for="dict in sys_listener_value_type" :key="dict.value" :value="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="执行内容" prop="value">
          <el-input v-model="form.value" placeholder="请输入执行内容" />
        </el-form-item>
      </el-form>
      <template #footer class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listListener, getListener, delListener, addListener, updateListener } from "@modules/flowable/api/listener";

export default {
  name: "Listener",
  setup() {
    const { proxy } = getCurrentInstance();
    const { sys_listener_value_type, sys_listener_type, sys_listener_event_type } = proxy.useDict("sys_listener_value_type", "sys_listener_type", "sys_listener_event_type");
    return {
      sys_listener_value_type,
      sys_listener_type,
      sys_listener_event_type
    }
  },
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
      // 流程监听表格数据
      listenerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        eventType: null,
        valueType: null,
        value: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      taskListenerEventList: [
        { label: 'create', value: 'create' },
        { label: 'assignment', value: 'assignment' },
        { label: 'complete', value: 'complete' },
        { label: 'delete', value: 'delete' },
      ],
      executionListenerEventList: [
        { label: 'start', value: 'start' },
        { label: 'end', value: 'end' },
        { label: 'take', value: 'take' },
      ],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程监听列表 */
    getList() {
      this.loading = true;
      listListener(this.queryParams).then(response => {
        this.listenerList = response.rows;
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
        name: null,
        type: null,
        eventType: null,
        valueType: null,
        value: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null,
        status: null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加流程监听";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getListener(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改流程监听";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateListener(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addListener(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除流程监听编号为"' + ids + '"的数据项？').then(function () {
        return delListener(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/listener/export', {
        ...this.queryParams
      }, `listener_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
