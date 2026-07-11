<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="微信" prop="wechat">
        <el-input
          v-model="queryParams.wechat"
          placeholder="请输入微信"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="telephone">
        <el-input
          v-model="queryParams.telephone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="国籍" prop="cuntroy">
        <el-input
          v-model="queryParams.cuntroy"
          placeholder="请输入国籍"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="婚姻状态" prop="dictMariage">
        <el-input
          v-model="queryParams.dictMariage"
          placeholder="请输入婚姻状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名族" prop="dictNation">
        <el-input
          v-model="queryParams.dictNation"
          placeholder="请输入名族"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生育状况" prop="dictIsborn">
        <el-input
          v-model="queryParams.dictIsborn"
          placeholder="请输入生育状况"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="家庭住址" prop="homeAdress">
        <el-input
          v-model="queryParams.homeAdress"
          placeholder="请输入家庭住址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证件号" prop="idNo">
        <el-input
          v-model="queryParams.idNo"
          placeholder="请输入证件号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="紧急联系人" prop="urgencyLinkMan">
        <el-input
          v-model="queryParams.urgencyLinkMan"
          placeholder="请输入紧急联系人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="紧急联系电话" prop="urgencyLinkPhone">
        <el-input
          v-model="queryParams.urgencyLinkPhone"
          placeholder="请输入紧急联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="紧急联系人关系" prop="urgencyLinkRelation">
        <el-input
          v-model="queryParams.urgencyLinkRelation"
          placeholder="请输入紧急联系人关系"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="QQ号码" prop="qqNo">
        <el-input
          v-model="queryParams.qqNo"
          placeholder="请输入QQ号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="第一学历" prop="dictFirestDegree">
        <el-input
          v-model="queryParams.dictFirestDegree"
          placeholder="请输入第一学历"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学位" prop="dictDegredd">
        <el-input
          v-model="queryParams.dictDegredd"
          placeholder="请输入学位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="专业" prop="professional">
        <el-input
          v-model="queryParams.professional"
          placeholder="请输入专业"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="毕业院校" prop="whichColledge">
        <el-input
          v-model="queryParams.whichColledge"
          placeholder="请输入毕业院校"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="院校类型" prop="dictColledge">
        <el-input
          v-model="queryParams.dictColledge"
          placeholder="请输入院校类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="毕业时间" prop="graduationDate">
        <el-date-picker clearable
          v-model="queryParams.graduationDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择毕业时间">
        </el-date-picker>
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
          v-hasPermi="['system:resume:add']"
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
          v-hasPermi="['system:resume:edit']"
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
          v-hasPermi="['system:resume:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:resume:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="resumeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="电话" align="center" prop="phone" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="微信" align="center" prop="wechat" />
      <el-table-column label="手机号" align="center" prop="telephone" />
      <el-table-column label="性别" align="center" prop="dictSex" />
      <el-table-column label="国籍" align="center" prop="cuntroy" />
      <el-table-column label="婚姻状态" align="center" prop="dictMariage" />
      <el-table-column label="名族" align="center" prop="dictNation" />
      <el-table-column label="生育状况" align="center" prop="dictIsborn" />
      <el-table-column label="家庭住址" align="center" prop="homeAdress" />
      <el-table-column label="政治面貌" align="center" prop="dictPoliticsStatus" />
      <el-table-column label="证件类型" align="center" prop="dictIdType" />
      <el-table-column label="证件号" align="center" prop="idNo" />
      <el-table-column label="紧急联系人" align="center" prop="urgencyLinkMan" />
      <el-table-column label="紧急联系电话" align="center" prop="urgencyLinkPhone" />
      <el-table-column label="紧急联系人关系" align="center" prop="urgencyLinkRelation" />
      <el-table-column label="QQ号码" align="center" prop="qqNo" />
      <el-table-column label="第一学历" align="center" prop="dictFirestDegree" />
      <el-table-column label="学位" align="center" prop="dictDegredd" />
      <el-table-column label="专业" align="center" prop="professional" />
      <el-table-column label="教育类型" align="center" prop="dictEduType" />
      <el-table-column label="毕业院校" align="center" prop="whichColledge" />
      <el-table-column label="院校类型" align="center" prop="dictColledge" />
      <el-table-column label="毕业时间" align="center" prop="graduationDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.graduationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:resume:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:resume:remove']"
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

    <!-- 添加或修改面试候选人对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="微信" prop="wechat">
          <el-input v-model="form.wechat" placeholder="请输入微信" />
        </el-form-item>
        <el-form-item label="手机号" prop="telephone">
          <el-input v-model="form.telephone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="国籍" prop="cuntroy">
          <el-input v-model="form.cuntroy" placeholder="请输入国籍" />
        </el-form-item>
        <el-form-item label="婚姻状态" prop="dictMariage">
          <el-input v-model="form.dictMariage" placeholder="请输入婚姻状态" />
        </el-form-item>
        <el-form-item label="名族" prop="dictNation">
          <el-input v-model="form.dictNation" placeholder="请输入名族" />
        </el-form-item>
        <el-form-item label="生育状况" prop="dictIsborn">
          <el-input v-model="form.dictIsborn" placeholder="请输入生育状况" />
        </el-form-item>
        <el-form-item label="家庭住址" prop="homeAdress">
          <el-input v-model="form.homeAdress" placeholder="请输入家庭住址" />
        </el-form-item>
        <el-form-item label="证件号" prop="idNo">
          <el-input v-model="form.idNo" placeholder="请输入证件号" />
        </el-form-item>
        <el-form-item label="紧急联系人" prop="urgencyLinkMan">
          <el-input v-model="form.urgencyLinkMan" placeholder="请输入紧急联系人" />
        </el-form-item>
        <el-form-item label="紧急联系电话" prop="urgencyLinkPhone">
          <el-input v-model="form.urgencyLinkPhone" placeholder="请输入紧急联系电话" />
        </el-form-item>
        <el-form-item label="紧急联系人关系" prop="urgencyLinkRelation">
          <el-input v-model="form.urgencyLinkRelation" placeholder="请输入紧急联系人关系" />
        </el-form-item>
        <el-form-item label="QQ号码" prop="qqNo">
          <el-input v-model="form.qqNo" placeholder="请输入QQ号码" />
        </el-form-item>
        <el-form-item label="第一学历" prop="dictFirestDegree">
          <el-input v-model="form.dictFirestDegree" placeholder="请输入第一学历" />
        </el-form-item>
        <el-form-item label="学位" prop="dictDegredd">
          <el-input v-model="form.dictDegredd" placeholder="请输入学位" />
        </el-form-item>
        <el-form-item label="专业" prop="professional">
          <el-input v-model="form.professional" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="毕业院校" prop="whichColledge">
          <el-input v-model="form.whichColledge" placeholder="请输入毕业院校" />
        </el-form-item>
        <el-form-item label="院校类型" prop="dictColledge">
          <el-input v-model="form.dictColledge" placeholder="请输入院校类型" />
        </el-form-item>
        <el-form-item label="毕业时间" prop="graduationDate">
          <el-date-picker clearable
            v-model="form.graduationDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择毕业时间">
          </el-date-picker>
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listResume, getResume, delResume, addResume, updateResume } from "@/api/qixing/resume";

export default {
  name: "Resume",
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
      // 面试候选人表格数据
      resumeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        phone: null,
        email: null,
        wechat: null,
        telephone: null,
        dictSex: null,
        cuntroy: null,
        dictMariage: null,
        dictNation: null,
        dictIsborn: null,
        homeAdress: null,
        dictPoliticsStatus: null,
        dictIdType: null,
        idNo: null,
        urgencyLinkMan: null,
        urgencyLinkPhone: null,
        urgencyLinkRelation: null,
        qqNo: null,
        dictFirestDegree: null,
        dictDegredd: null,
        professional: null,
        dictEduType: null,
        whichColledge: null,
        dictColledge: null,
        graduationDate: null,
        createId: null,
        updateId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "电话不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱不能为空", trigger: "blur" }
        ],
        wechat: [
          { required: true, message: "微信不能为空", trigger: "blur" }
        ],
        telephone: [
          { required: true, message: "手机号不能为空", trigger: "blur" }
        ],
        dictSex: [
          { required: true, message: "性别不能为空", trigger: "change" }
        ],
        cuntroy: [
          { required: true, message: "国籍不能为空", trigger: "blur" }
        ],
        dictMariage: [
          { required: true, message: "婚姻状态不能为空", trigger: "blur" }
        ],
        dictNation: [
          { required: true, message: "名族不能为空", trigger: "blur" }
        ],
        dictIsborn: [
          { required: true, message: "生育状况不能为空", trigger: "blur" }
        ],
        homeAdress: [
          { required: true, message: "家庭住址不能为空", trigger: "blur" }
        ],
        dictPoliticsStatus: [
          { required: true, message: "政治面貌不能为空", trigger: "change" }
        ],
        dictIdType: [
          { required: true, message: "证件类型不能为空", trigger: "change" }
        ],
        idNo: [
          { required: true, message: "证件号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询面试候选人列表 */
    getList() {
      this.loading = true;
      listResume(this.queryParams).then(response => {
        this.resumeList = response.rows;
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
        phone: null,
        email: null,
        wechat: null,
        telephone: null,
        dictSex: null,
        cuntroy: null,
        dictMariage: null,
        dictNation: null,
        dictIsborn: null,
        homeAdress: null,
        dictPoliticsStatus: null,
        dictIdType: null,
        idNo: null,
        urgencyLinkMan: null,
        urgencyLinkPhone: null,
        urgencyLinkRelation: null,
        qqNo: null,
        dictFirestDegree: null,
        dictDegredd: null,
        professional: null,
        dictEduType: null,
        whichColledge: null,
        dictColledge: null,
        graduationDate: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null
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
      this.title = "添加面试候选人";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getResume(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改面试候选人";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateResume(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addResume(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除面试候选人编号为"' + ids + '"的数据项？').then(function() {
        return delResume(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/resume/export', {
        ...this.queryParams
      }, `resume_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
