<template>
  <!-- 管理员或有权限的角色公告列表（发布） -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公告标题" prop="noticeTitle">
        <el-input v-model="queryParams.noticeTitle" placeholder="请输入公告标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="noticeType">
        <el-select v-model="queryParams.noticeType" placeholder="公告类型" clearable>
          <el-option v-for="dict in dict.type.sys_notice_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:notice:add']">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="公告标题" align="left" prop="noticeTitle" :show-overflow-tooltip="true" />
      <el-table-column label="公告类型" align="center" prop="noticeType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_notice_type" :value="scope.row.noticeType" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_notice_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="goDetail(scope.row)">预览</el-button>
          <el-button
            v-if="scope.row.status == '0'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handlePublic(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '2'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handlePublic(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >再次发布</el-button>
          <el-button
            v-if="scope.row.status == '1'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleCanclePublic(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >下架</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:notice:edit']">修改</el-button>
          <el-button
            v-if="scope.row.status !== '1'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:notice:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-form-item label="公告标题" prop="noticeTitle">
          <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" />
        </el-form-item>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="公告类型" prop="noticeType">
              <el-radio-group v-model="form.noticeType">
                <el-radio v-for="dict in dict.type.sys_notice_type" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.noticeType === '1'">
            <el-form-item label="选择部门" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请选择部门" readonly class="user-select-input" @focus="openDeptSelector()">
                <template #suffix>
                  <svg-icon icon-class="tree" />
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否长期有效" prop="validDate">
          <el-radio-group v-model="form.validDate">
            <el-radio v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="开始生效日期" prop="validStartTime">
              <el-date-picker clearable v-model="form.validStartTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择开始使用日期"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.validDate === '0'">
            <el-form-item label="结束日期" prop="validEndTime">
              <el-date-picker clearable v-model="form.validEndTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择开始使用日期"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="内容">
          <editor v-model="form.noticeContent" :min-height="192" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <TreeSelect :open.sync="openSelect" :type="selectType" :selectedDepts="selectedDepts" @confimDept="confimDept" />
  </div>
</template>

<script>
import { listNotice, getNotice, delNotice, addNotice, updateNotice, changeStatus } from "@/api/system/notice";
import TreeSelect from "@/components/org/TreeSelect/index.vue";

export default {
  name: "Notice",
  dicts: ["sys_notice_status", "sys_notice_type", "sys_yes_or_no"],
  components: { TreeSelect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选人弹框
      openSelect: false,
      selectType: "single",
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
      // 公告表格数据
      noticeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        noticeTitle: [{ required: true, message: "公告标题不能为空", trigger: "blur" }],
        noticeType: [{ required: true, message: "公告类型不能为空", trigger: "change" }],
        validDate: [{ required: true, message: "请选择有效期类型", trigger: "change" }],
        validStartTime: [{ required: true, message: "请选择开始有效日期", trigger: "change" }],
        validEndTime: [{ required: true, message: "请选择结束日期", trigger: "change" }],
        deptName: [{ required: true, message: "请选择部门", trigger: "change" }],
      },
      // 部门选项
      deptOptions: [],
      //已选择部门
      selectedDepts: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 选部门 */
    openDeptSelector() {
      this.openSelect = true;
      this.selectedDepts = [];
    },
    /** 选人确认 */
    confimDept(selection) {
      if (!selection || selection.length <= 0) {
        this.$message.error("请选择部门！");
        return;
      }
      const dept = selection[0];
      this.$set(this.form, "deptId", dept.id);
      this.$set(this.form, "deptName", dept.label);
      this.openSelect = false;
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      listNotice(this.queryParams).then((response) => {
        this.noticeList = response.rows;
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
        noticeId: undefined,
        noticeTitle: undefined,
        noticeContent: undefined,
        noticeType: "0",
        validDate: "1",
        deptId: null,
        deptName: null,
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
      this.ids = selection.map((item) => item.noticeId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加公告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const noticeId = row.noticeId || this.ids;
      getNotice(noticeId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改公告";
      });
    },
    /** 发布 */
    handlePublic(row) {
      this.$confirm("确认发布此公告吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      })
        .then(() => {
          this.loading = true;
          changeStatus(row.noticeId, "1").then((res) => {
            row.status = "1";
            this.loading = false;
            this.$modal.msgSuccess("发布成功");
          });
        })
        .catch(() => {});
    },
    /** 下架 */
    handleCanclePublic(row) {
      this.$confirm("确认下架此公告吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      })
        .then(() => {
          this.loading = true;
          changeStatus(row.noticeId, "2").then((res) => {
            row.status = "2";
            this.loading = false;
            this.$modal.msgSuccess("下架成功");
          });
        })
        .catch(() => {});
    },
    /** 预览 */
    goDetail(row) {
      let params = { id: row.noticeId };
      this.$tab.openPage(row.noticeTitle, "/news/notice/n/detail/:t" + new Date().getTime(), params);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.noticeId != undefined) {
            updateNotice(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNotice(this.form).then((response) => {
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
      const noticeIds = row.noticeId || this.ids;
      this.$modal
        .confirm("是否确认删除此公告编？")
        .then(function () {
          return delNotice(noticeIds);
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
<style lang="scss" scoped>
.user-select-input {
  ::v-deep .el-input__inner {
    cursor: pointer;
    border: 1px solid #ebeef5;
    &:hover {
      border-color: #409eff;
    }
  }
}
</style>