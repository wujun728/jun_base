<template>
  <div class="app-container">
    <el-form ref="basicInfoForm" :model="info" :rules="rules" label-width="128px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="模板名称" prop="name">
            <el-input placeholder="请输入模板名称" v-model="info.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板分类" prop="type">
            <template>
              <el-select v-model="info.type" clearable filterable placeholder="请选择">
                <el-option v-for="templateType in typeOptions" :key="templateType.id" :label="templateType.name" :value="templateType.id">
                  <span style="float: left">{{ templateType.name }}</span>
                </el-option>
              </el-select>
            </template>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联流程" prop="defKey">
            <template>
              <el-select v-model="info.defKey" clearable filterable placeholder="请选择">
                <el-option v-for="flow in flowOptions" :key="flow.procDefKey" :label="flow.procDefName" :value="flow.procDefKey">
                  <span style="float: left">{{ flow.procDefName }}</span>
                </el-option>
              </el-select>
            </template>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序号" prop="sort">
            <el-input placeholder="请输入数字，值越小越靠前" v-model="info.sort" type="number" style="width: 240px;" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { getFlowOptionSelect } from "@/api/workflow/flowable/definition";
import { listEnable } from "@/api/workflow/template";

export default {
  props: {
    info: {
      type: Object,
      default: null,
    },
  },
  created() {
    /** 查询流程下拉列表 */
    getFlowOptionSelect().then((response) => {
      this.flowOptions = response.data;
    });
    /** 查询模板分类 */
    listEnable().then((res) => {
      this.typeOptions = res.data;
    });
  },
  data() {
    return {
      rules: {
        name: [{ required: true, message: "请输入模板名称", trigger: "blur" }],
        type: [{ required: true, message: "请选择模板类型", trigger: "blur" }],
        defKey: [{ required: true, message: "请关联流程", trigger: "blur" }],
        sort: [{ required: true, message: "请输入排序号", trigger: "blur" }],
      },
      flowOptions: [],
      typeOptions: [], // 模板分类
    };
  },
};
</script>
