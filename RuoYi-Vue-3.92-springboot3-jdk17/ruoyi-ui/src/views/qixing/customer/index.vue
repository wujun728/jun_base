<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户编码" prop="customerCode">
        <el-input
          v-model="queryParams.customerCode"
          placeholder="请输入客户编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入公司电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财务经理" prop="financialManager">
        <el-input
          v-model="queryParams.financialManager"
          placeholder="请输入财务经理"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财务经理电话" prop="fmPhone">
        <el-input
          v-model="queryParams.fmPhone"
          placeholder="请输入财务经理电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="营业执照" prop="businessLicense">
        <el-input
          v-model="queryParams.businessLicense"
          placeholder="请输入营业执照"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="营业执照编码" prop="businessLicenseNo">
        <el-input
          v-model="queryParams.businessLicenseNo"
          placeholder="请输入营业执照编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="法定代表人" prop="legalRepresentative">
        <el-input
          v-model="queryParams.legalRepresentative"
          placeholder="请输入法定代表人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="注册资本" prop="registeredCapital">
        <el-input
          v-model="queryParams.registeredCapital"
          placeholder="请输入注册资本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户地址" prop="customerAddress">
        <el-input
          v-model="queryParams.customerAddress"
          placeholder="请输入客户地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司始建于" prop="companyBegin">
        <el-date-picker clearable
          v-model="queryParams.companyBegin"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择公司始建于">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实收资本" prop="paiclupCapital">
        <el-input
          v-model="queryParams.paiclupCapital"
          placeholder="请输入实收资本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经营范围" prop="businessScope">
        <el-input
          v-model="queryParams.businessScope"
          placeholder="请输入经营范围"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="营业期限开始" prop="businessTermStart">
        <el-date-picker clearable
          v-model="queryParams.businessTermStart"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择营业期限开始">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="营业期限结束" prop="businessTermEnd">
        <el-date-picker clearable
          v-model="queryParams.businessTermEnd"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择营业期限结束">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="登记机关" prop="registrationAuthority">
        <el-input
          v-model="queryParams.registrationAuthority"
          placeholder="请输入登记机关"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执照发放日" prop="licensingDay">
        <el-date-picker clearable
          v-model="queryParams.licensingDay"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择执照发放日">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="纳税人识别号" prop="taxRegNumber">
        <el-input
          v-model="queryParams.taxRegNumber"
          placeholder="请输入纳税人识别号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="税号" prop="dutyParagraph">
        <el-input
          v-model="queryParams.dutyParagraph"
          placeholder="请输入税号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开户行" prop="openingBank">
        <el-input
          v-model="queryParams.openingBank"
          placeholder="请输入开户行"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号" prop="accountNumber">
        <el-input
          v-model="queryParams.accountNumber"
          placeholder="请输入账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话" prop="telephone">
        <el-input
          v-model="queryParams.telephone"
          placeholder="请输入电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司地址" prop="companyAddress">
        <el-input
          v-model="queryParams.companyAddress"
          placeholder="请输入公司地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户对接责任人" prop="customerRespPerson">
        <el-input
          v-model="queryParams.customerRespPerson"
          placeholder="请输入客户对接责任人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户关系维护责任人" prop="customerRelationPerson">
        <el-input
          v-model="queryParams.customerRelationPerson"
          placeholder="请输入客户关系维护责任人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="鍒涘缓浜? prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="createId">
        <el-input
          v-model="queryParams.createId"
          placeholder="请输入创建人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新人" prop="updateId">
        <el-input
          v-model="queryParams.updateId"
          placeholder="请输入更新人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否弃用" prop="deleted">
        <el-input
          v-model="queryParams.deleted"
          placeholder="请输入是否弃用"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财务经理1" prop="financialManager1">
        <el-input
          v-model="queryParams.financialManager1"
          placeholder="请输入财务经理1"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财务经理电话1" prop="fmPhone1">
        <el-input
          v-model="queryParams.fmPhone1"
          placeholder="请输入财务经理电话1"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财务经理2" prop="financialManager2">
        <el-input
          v-model="queryParams.financialManager2"
          placeholder="请输入财务经理2"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财务经理电话2" prop="fmPhone2">
        <el-input
          v-model="queryParams.fmPhone2"
          placeholder="请输入财务经理电话2"
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
          v-hasPermi="['system:customer:add']"
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
          v-hasPermi="['system:customer:edit']"
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
          v-hasPermi="['system:customer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:customer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户ID" align="center" prop="id" />
      <el-table-column label="客户名称" align="center" prop="customerName" />
      <el-table-column label="客户编码" align="center" prop="customerCode" />
      <el-table-column label="客户类型" align="center" prop="dictCustomerType" />
      <el-table-column label="公司电话" align="center" prop="phone" />
      <el-table-column label="财务经理" align="center" prop="financialManager" />
      <el-table-column label="财务经理电话" align="center" prop="fmPhone" />
      <el-table-column label="客户联系信息" align="center" prop="customerLinks" />
      <el-table-column label="客户营业信息" align="center" prop="customerBusiness" />
      <el-table-column label="营业执照" align="center" prop="businessLicense" />
      <el-table-column label="公司全称" align="center" prop="conpanyName" />
      <el-table-column label="营业执照编码" align="center" prop="businessLicenseNo" />
      <el-table-column label="法定代表人" align="center" prop="legalRepresentative" />
      <el-table-column label="注册资本" align="center" prop="registeredCapital" />
      <el-table-column label="客户地址" align="center" prop="customerAddress" />
      <el-table-column label="公司类型" align="center" prop="dictCompanyType" />
      <el-table-column label="公司始建于" align="center" prop="companyBegin" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.companyBegin, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实收资本" align="center" prop="paiclupCapital" />
      <el-table-column label="经营范围" align="center" prop="businessScope" />
      <el-table-column label="营业期限开始" align="center" prop="businessTermStart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.businessTermStart, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="营业期限结束" align="center" prop="businessTermEnd" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.businessTermEnd, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登记机关" align="center" prop="registrationAuthority" />
      <el-table-column label="执照发放日" align="center" prop="licensingDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.licensingDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="客户开票信息" align="center" prop="customerBillingInformation" />
      <el-table-column label="纳税人识别号" align="center" prop="taxRegNumber" />
      <el-table-column label="税号" align="center" prop="dutyParagraph" />
      <el-table-column label="开户行" align="center" prop="openingBank" />
      <el-table-column label="账号" align="center" prop="accountNumber" />
      <el-table-column label="电话" align="center" prop="telephone" />
      <el-table-column label="公司地址" align="center" prop="companyAddress" />
      <el-table-column label="是否老客户" align="center" prop="dictIsOldCustomer" />
      <el-table-column label="客户对接责任人" align="center" prop="customerRespPerson" />
      <el-table-column label="客户关系维护责任人" align="center" prop="customerRelationPerson" />
      <el-table-column label="备注信息" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="creator" />
      <el-table-column label="创建人" align="center" prop="createId" />
      <el-table-column label="更新人" align="center" prop="updateId" />
      <el-table-column label="是否弃用" align="center" prop="deleted" />
      <el-table-column label="财务经理1" align="center" prop="financialManager1" />
      <el-table-column label="财务经理电话1" align="center" prop="fmPhone1" />
      <el-table-column label="财务经理2" align="center" prop="financialManager2" />
      <el-table-column label="财务经理电话2" align="center" prop="fmPhone2" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="编辑权限人员" align="center" prop="editor" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:customer:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:customer:remove']"
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

    <!-- 添加或修改客户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="客户编码" prop="customerCode">
          <el-input v-model="form.customerCode" placeholder="请输入客户编码" />
        </el-form-item>
        <el-form-item label="公司电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入公司电话" />
        </el-form-item>
        <el-form-item label="财务经理" prop="financialManager">
          <el-input v-model="form.financialManager" placeholder="请输入财务经理" />
        </el-form-item>
        <el-form-item label="财务经理电话" prop="fmPhone">
          <el-input v-model="form.fmPhone" placeholder="请输入财务经理电话" />
        </el-form-item>
        <el-form-item label="客户联系信息" prop="customerLinks">
          <el-input v-model="form.customerLinks" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="客户营业信息" prop="customerBusiness">
          <el-input v-model="form.customerBusiness" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="营业执照" prop="businessLicense">
          <el-input v-model="form.businessLicense" placeholder="请输入营业执照" />
        </el-form-item>
        <el-form-item label="公司全称" prop="conpanyName">
          <el-input v-model="form.conpanyName" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="营业执照编码" prop="businessLicenseNo">
          <el-input v-model="form.businessLicenseNo" placeholder="请输入营业执照编码" />
        </el-form-item>
        <el-form-item label="法定代表人" prop="legalRepresentative">
          <el-input v-model="form.legalRepresentative" placeholder="请输入法定代表人" />
        </el-form-item>
        <el-form-item label="注册资本" prop="registeredCapital">
          <el-input v-model="form.registeredCapital" placeholder="请输入注册资本" />
        </el-form-item>
        <el-form-item label="客户地址" prop="customerAddress">
          <el-input v-model="form.customerAddress" placeholder="请输入客户地址" />
        </el-form-item>
        <el-form-item label="公司始建于" prop="companyBegin">
          <el-date-picker clearable
            v-model="form.companyBegin"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择公司始建于">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实收资本" prop="paiclupCapital">
          <el-input v-model="form.paiclupCapital" placeholder="请输入实收资本" />
        </el-form-item>
        <el-form-item label="经营范围" prop="businessScope">
          <el-input v-model="form.businessScope" placeholder="请输入经营范围" />
        </el-form-item>
        <el-form-item label="营业期限开始" prop="businessTermStart">
          <el-date-picker clearable
            v-model="form.businessTermStart"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择营业期限开始">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="营业期限结束" prop="businessTermEnd">
          <el-date-picker clearable
            v-model="form.businessTermEnd"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择营业期限结束">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="登记机关" prop="registrationAuthority">
          <el-input v-model="form.registrationAuthority" placeholder="请输入登记机关" />
        </el-form-item>
        <el-form-item label="执照发放日" prop="licensingDay">
          <el-date-picker clearable
            v-model="form.licensingDay"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择执照发放日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="客户开票信息" prop="customerBillingInformation">
          <el-input v-model="form.customerBillingInformation" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="纳税人识别号" prop="taxRegNumber">
          <el-input v-model="form.taxRegNumber" placeholder="请输入纳税人识别号" />
        </el-form-item>
        <el-form-item label="税号" prop="dutyParagraph">
          <el-input v-model="form.dutyParagraph" placeholder="请输入税号" />
        </el-form-item>
        <el-form-item label="开户行" prop="openingBank">
          <el-input v-model="form.openingBank" placeholder="请输入开户行" />
        </el-form-item>
        <el-form-item label="账号" prop="accountNumber">
          <el-input v-model="form.accountNumber" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="电话" prop="telephone">
          <el-input v-model="form.telephone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="公司地址" prop="companyAddress">
          <el-input v-model="form.companyAddress" placeholder="请输入公司地址" />
        </el-form-item>
        <el-form-item label="是否老客户" prop="dictIsOldCustomer">
          <el-input v-model="form.dictIsOldCustomer" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="客户对接责任人" prop="customerRespPerson">
          <el-input v-model="form.customerRespPerson" placeholder="请输入客户对接责任人" />
        </el-form-item>
        <el-form-item label="客户关系维护责任人" prop="customerRelationPerson">
          <el-input v-model="form.customerRelationPerson" placeholder="请输入客户关系维护责任人" />
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜? prop="creator">
          <el-input v-model="form.creator" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="创建人" prop="createId">
          <el-input v-model="form.createId" placeholder="请输入创建人" />
        </el-form-item>
        <el-form-item label="更新人" prop="updateId">
          <el-input v-model="form.updateId" placeholder="请输入更新人" />
        </el-form-item>
        <el-form-item label="是否弃用" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入是否弃用" />
        </el-form-item>
        <el-form-item label="财务经理1" prop="financialManager1">
          <el-input v-model="form.financialManager1" placeholder="请输入财务经理1" />
        </el-form-item>
        <el-form-item label="财务经理电话1" prop="fmPhone1">
          <el-input v-model="form.fmPhone1" placeholder="请输入财务经理电话1" />
        </el-form-item>
        <el-form-item label="财务经理2" prop="financialManager2">
          <el-input v-model="form.financialManager2" placeholder="请输入财务经理2" />
        </el-form-item>
        <el-form-item label="财务经理电话2" prop="fmPhone2">
          <el-input v-model="form.fmPhone2" placeholder="请输入财务经理电话2" />
        </el-form-item>
        <el-form-item label="娴佺▼ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="编辑权限人员" prop="editor">
          <el-input v-model="form.editor" type="textarea" placeholder="请输入内容" />
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
import { listCustomer, getCustomer, delCustomer, addCustomer, updateCustomer } from "@/api/qixing/customer";

export default {
  name: "Customer",
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
      // 客户信息表格数据
      customerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerName: null,
        customerCode: null,
        dictCustomerType: null,
        phone: null,
        financialManager: null,
        fmPhone: null,
        customerLinks: null,
        customerBusiness: null,
        businessLicense: null,
        conpanyName: null,
        businessLicenseNo: null,
        legalRepresentative: null,
        registeredCapital: null,
        customerAddress: null,
        dictCompanyType: null,
        companyBegin: null,
        paiclupCapital: null,
        businessScope: null,
        businessTermStart: null,
        businessTermEnd: null,
        registrationAuthority: null,
        licensingDay: null,
        customerBillingInformation: null,
        taxRegNumber: null,
        dutyParagraph: null,
        openingBank: null,
        accountNumber: null,
        telephone: null,
        companyAddress: null,
        dictIsOldCustomer: null,
        customerRespPerson: null,
        customerRelationPerson: null,
        creator: null,
        createId: null,
        updateId: null,
        deleted: null,
        financialManager1: null,
        fmPhone1: null,
        financialManager2: null,
        fmPhone2: null,
        orderId: null,
        orderStatus: null,
        editor: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerName: [
          { required: true, message: "客户名称不能为空", trigger: "blur" }
        ],
        customerCode: [
          { required: true, message: "客户编码不能为空", trigger: "blur" }
        ],
        dictCustomerType: [
          { required: true, message: "客户类型不能为空", trigger: "change" }
        ],
        phone: [
          { required: true, message: "公司电话不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询客户信息列表 */
    getList() {
      this.loading = true;
      listCustomer(this.queryParams).then(response => {
        this.customerList = response.rows;
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
        customerName: null,
        customerCode: null,
        dictCustomerType: null,
        phone: null,
        financialManager: null,
        fmPhone: null,
        customerLinks: null,
        customerBusiness: null,
        businessLicense: null,
        conpanyName: null,
        businessLicenseNo: null,
        legalRepresentative: null,
        registeredCapital: null,
        customerAddress: null,
        dictCompanyType: null,
        companyBegin: null,
        paiclupCapital: null,
        businessScope: null,
        businessTermStart: null,
        businessTermEnd: null,
        registrationAuthority: null,
        licensingDay: null,
        customerBillingInformation: null,
        taxRegNumber: null,
        dutyParagraph: null,
        openingBank: null,
        accountNumber: null,
        telephone: null,
        companyAddress: null,
        dictIsOldCustomer: null,
        customerRespPerson: null,
        customerRelationPerson: null,
        remark: null,
        creator: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null,
        deleted: null,
        financialManager1: null,
        fmPhone1: null,
        financialManager2: null,
        fmPhone2: null,
        orderId: null,
        orderStatus: null,
        editor: null
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
      this.title = "添加客户信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCustomer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改客户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCustomer(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCustomer(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除客户信息编号为"' + ids + '"的数据项？').then(function() {
        return delCustomer(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/customer/export', {
        ...this.queryParams
      }, `customer_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
