<script setup lang="ts">
import { genTableState } from ".";
import { onMounted, ref, useTemplateRef } from "vue";
import basicInfoForm from "./basicInfoForm.vue";
import genInfoForm from "./genInfoForm.vue";
import joinTableForm from "./joinTableForm.vue";
import editTableForm from "./editTableForm.vue";
import { useRoute } from "vue-router";
import { modal, tab } from "@/plugins";
import { FormInstance } from "element-plus";
const route = useRoute();
const activeName = ref("columnInfo");
const info = genTableState().info;
const tables = genTableState().tables;
const basicInfo = useTemplateRef("basicInfo");
const genInfo = useTemplateRef("genInfo");
/** 提交按钮 */
function submitForm() {
  Promise.all([
    basicInfo.value!.$refs.basicInfoForm as FormInstance,
    genInfo.value!.$refs.genInfoForm as FormInstance
  ].map(form => new Promise((resolve, reject) => {
    form.validate((res, error) => {
      if (error) { reject(error); }
      else { resolve(res); }
    });
  }))).then(res => {
    const validateResult = res.every(item => !!item);
    if (validateResult) {
      genTableState().updateGenTableVo();
    } else {
      modal.msgError("表单校验未通过，请重新检查提交内容");
    }
  }).catch(error => {
    for (const errKey in error) {
      for (const err in error[errKey]) {
        modal.msgError(error[errKey][err].message);
      }
    }
  });
}

onMounted(() => {
  const tableId = route.params && route.params.tableId as string;
  if (tableId) genTableState().initGenTableVo(tableId);
})

function close() {
  const obj = { path: "/tool/gen", query: { t: Date.now(), pageNum: route.query.pageNum } };
  tab.closeOpenPage(obj);
}
</script>
<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <basic-info-form ref="basicInfo" :info="info" :tables="tables" />
      </el-tab-pane>
      <el-tab-pane label="关联表" name="joinTable">
        <join-table-form ref="joinTable" />
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="columnInfo">
        <edit-table-form ref="editTable" />
      </el-tab-pane>
      <el-tab-pane label="生成信息" name="genInfo">
        <gen-info-form ref="genInfo" />
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="100px">
      <div style="text-align: center;margin-left:-100px;margin-top:10px;">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </div>
    </el-form>
  </el-card>
</template>