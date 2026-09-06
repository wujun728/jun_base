<script setup name="Order" lang="ts">
import { refundOrder, updateOrderStatus } from "@/modules/pay/api/order";
import { ref } from "vue";
import { PayOrder } from "@/entity/pay/PayOrder";
import { usePage } from "@/hook";
import { modal } from "@/plugins";
import { parseTime } from "@/utils/ruoyi";
import { getSchemaName, getSchemas } from "@/annotation/Schema";
import PayTest from "@modules/pay/view/paytest/index.vue";
const {
  queryParams,
  handleQuery,
  handleUpdate,
  handleAdd,
  resetForm,
  updateForm,
  resetQuery,
  handleDelete,
  handleExport,
  list,
  total,
  loading,
  form
} = usePage(PayOrder)
const open = ref(false);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const title = ref("");

// 取消按钮
function cancel() {
  open.value = false;
  resetForm();
}

// 多选框选中数据
function handleSelectionChange(selection: PayOrder[]) {
  ids.value = selection.map(item => item.orderId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAddClick() {
  resetForm();
  open.value = true;
  title.value = "添加订单";
}

/** 修改按钮操作 */
function handleUpdateClick(row: PayOrder) {
  updateForm(row.orderId || ids.value[0]).then(() => {
    open.value = true;
    title.value = "修改订单";
  });
}

/** 提交按钮 */
function submitForm() {
  if (form.value.orderId != null) {
    handleUpdate().then(response => {
      modal.msgSuccess("修改成功");
      open.value = false;
    });
  } else {
    handleAdd().then(response => {
      modal.msgSuccess("新增成功");
      open.value = false;
    });
  }
}

/** 删除按钮操作 */
function handleDeleteClick(row: PayOrder) {
  const _orderIds = row.orderId || ids.value;
  modal.confirm(`是否确认删除订单编号为"${_orderIds}"的数据项？`).then(function () {
    return handleDelete(_orderIds);
  }).then(() => {
    modal.msgSuccess("删除成功");
  }).catch(() => { });
}

// 新增：退款操作
function handleRefund(row: PayOrder) {
  const orderNumber = row.orderNumber;
  modal.confirm(`是否确认对订单号为"${orderNumber}"的订单进行退款？`).then(function () {
    return refundOrder(orderNumber);
  }).then(() => {
    handleQuery();
    modal.msgSuccess("退款成功");
  }).catch(() => { });
}

// 新增：更新订单状态操作
async function handleUpdateStatus(row: PayOrder) {
  const orderNumber = row.orderNumber;
  await updateOrderStatus(orderNumber);
  handleQuery();
  modal.msgSuccess("订单状态更新成功");
}

const payOrderNumber = ref()
const payOpen = ref(false);
async function handlePayTest(row: PayOrder) {
  payOrderNumber.value = row.orderNumber;
  payOpen.value = true;
}

handleQuery();
</script>
<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="订单号" prop="orderNumber">
          <el-input v-model="queryParams.orderNumber" placeholder="请输入订单号" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt10">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="handleAddClick"
            v-hasPermi="['pay:order:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdateClick"
            v-hasPermi="['pay:order:edit']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDeleteClick"
            v-hasPermi="['pay:order:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="Download" @click="handleExport"
            v-hasPermi="['pay:order:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="handleQuery"></right-toolbar>
      </el-row>
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column :label="getSchemaName(PayOrder, 'orderId')" align="center" prop="orderId" />
        <el-table-column v-for="item in getSchemas(PayOrder, 'orderNumber', 'thirdNumber')" :label="item.name"
          align="center" :prop="item.attr" width="180" />
        <el-table-column
          v-for="item in getSchemas(PayOrder, 'orderStatus', 'payType', 'totalAmount', 'actualAmount', 'createBy')"
          :label="item.name" align="center" :prop="item.attr" />
        <el-table-column v-for="item in getSchemas(PayOrder, 'createTime', 'updateTime')" :label="item.name"
          align="center" :prop="item.attr" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row[item.attr], '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="400">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdateClick(scope.row)"
              v-hasPermi="['pay:order:edit']">修改</el-button>
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
              v-hasPermi="['pay:order:remove']">删除</el-button>
            <el-button link type="warning" icon="RefreshLeft" @click="handleRefund(scope.row)"
              v-hasPermi="['pay:order:refund']">退款</el-button>
            <el-button link type="info" icon="Refresh" @click="handleUpdateStatus(scope.row)"
              v-hasPermi="['pay:order:updateStatus']">更新状态</el-button>
            <el-button link type="info" icon="Position" @click="handlePayTest(scope.row)"
              v-hasPermi="['pay:order:updateStatus']">测试支付</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="handleQuery" />
    </el-card>

    <!-- 添加或修改订单对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="orderRef" :model="form" label-width="120px">
        <el-form-item v-for="item in getSchemas(PayOrder, 'totalAmount', 'orderContent', 'orderMessage', 'remark')"
          :label="item.name" :prop="item.attr">
          <el-input v-model="form[item.attr]" :placeholder="`请输入${item.name}`" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 添加或修改订单对话框 -->
    <el-dialog v-model="payOpen" width="500px" append-to-body>
      <PayTest :orderNumber="payOrderNumber" />
    </el-dialog>
  </div>
</template>