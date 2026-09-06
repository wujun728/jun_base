<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="消息标题" prop="messageTitle">
          <el-input v-model="queryParams.messageTitle" placeholder="请输入消息标题" clearable style="width: 240px"
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="消息状态" prop="messageStatus">
          <el-select v-model="queryParams.messageStatus" placeholder="请选择消息状态" clearable style="width: 240px">
            <el-option v-for="dict in message_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
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
          <el-button type="primary" plain icon="Plus" @click="handleAdd"
            v-hasPermi="['modelMessage:messageSystem:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="messageSystemList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="消息标题" align="center" prop="messageTitle" :show-overflow-tooltip="true" />
        <el-table-column label="收件人" align="center" prop="messageRecipient" :show-overflow-tooltip="true" />
        <el-table-column label="消息状态" align="center" prop="messageStatus">
          <template #default="scope">
            <dict-tag :options="message_status" :value="scope.row.messageStatus" />
          </template>
        </el-table-column>
        <el-table-column label="发送时间" align="center" prop="createTime">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="查看" placement="top">
              <el-button link type="primary" icon="View" @click="openDrawer(scope.row.messageId)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 添加或修改消息管理对话框 -->
    <AddMessage ref="sendMessageRef" @success="handleSendSuccess" />

    <!-- 消息详情对话框 -->
    <el-dialog :title="title" v-model="drawerVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="消息标题" :span="2">{{ detailForm.messageTitle }}</el-descriptions-item>
        <el-descriptions-item label="消息内容" :span="2">{{ detailForm.messageContent }}</el-descriptions-item>
        <el-descriptions-item label="收件人">{{ detailForm.messageRecipient }}</el-descriptions-item>
        <el-descriptions-item label="发件人">{{ detailForm.createBy }}</el-descriptions-item>
        <el-descriptions-item label="发送方式">
          <dict-tag :options="send_mode" :value="detailForm.sendMode" />
        </el-descriptions-item>
        <el-descriptions-item label="消息类型">
          <dict-tag :options="message_type" :value="detailForm.messageType" />
        </el-descriptions-item>
        <el-descriptions-item label="消息状态">
          <dict-tag :options="message_status" :value="detailForm.messageStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="发送时间">{{ parseTime(detailForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" v-if="detailForm.remark" :span="2">
          {{ detailForm.remark }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="MessageSystem">
import { ref, reactive, toRefs, getCurrentInstance } from 'vue';
import { delMessageSystem, listMessageSystem, getMessageSystem, getUpdate } from "@modules/message/api/messageSystem";
import AddMessage from './components/addMessage.vue';

const { proxy } = getCurrentInstance();
const { message_status, message_type, send_mode } = proxy.useDict("message_status", "message_type", "send_mode");
const { parseTime } = proxy;

const messageSystemList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const multiple = ref(true);
const title = ref('');
const total = ref(0);
const sendMessageRef = ref(false);
const drawerVisible = ref(false);
const detailForm = ref({});

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    messageTitle: null,
    messageStatus: null,
  }
});

const { queryParams } = toRefs(data);

// 查询消息管理列表
function getList() {
  loading.value = true;
  listMessageSystem(queryParams.value).then(response => {
    messageSystemList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 搜索按钮操作
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

// 重置按钮操作
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.messageId);
  multiple.value = !selection.length;
}

// 发送信息操作
function handleAdd() {
  sendMessageRef.value.open();
  sendMessageRef.value.getTemplate();
}

// 删除按钮操作
function handleDelete(row) {
  const _messageIds = row.messageId || ids.value;
  proxy.$modal.confirm('是否确认删除消息管理编号为"' + _messageIds + '"的数据项？').then(function () {
    return delMessageSystem(_messageIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除信息成功！");
  }).catch(() => { });
}

function handleSendSuccess() {
  getList();
}

// 打开抽屉并加载详细信息
function openDrawer(messageId) {
  getMessageSystem(messageId).then(response => {
    detailForm.value = response.data;
    title.value = '信息详情';
    drawerVisible.value = true;
    getUpdate(messageId).then(response => { });
  });
}

getList();
</script>