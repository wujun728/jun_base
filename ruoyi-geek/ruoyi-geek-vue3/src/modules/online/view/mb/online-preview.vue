<script setup>
import request from '@/utils/request';
import { computed } from 'vue';

const porp = defineProps(["path", "tag", "sqlText", "parameterType", "resultMap", "tagId", "method"])
const generated = reactive({
  path: "",
  codes: [
    {
      title: "mapper.xml",
      code: "",
      generated: () => {
        let code = "<" + porp.tag + " "
        if (!!porp.tagId) {
          code += "id=\"" + porp.tagId + "\" "
        }
        if (!!porp.parameterType) {
          code += "parameterType=\"" + porp.parameterType + "\" "
        }
        if (!!porp.resultMap) {
          code += "resultMap=\"" + porp.resultMap + "\" "
        }
        code += ">\n"
        code += porp.sqlText
        code += "\n</" + porp.tag + ">"
        return code
      },
    },
    {
      title: "api.js",
      code: "",
      generated: () => {
        let code = "import request from '@/utils/request'\n"
        code += "export function " + porp.tagId;
        if (porp.method === 'get' || porp.method === 'GET') {
          code += "(params){\n"
        } else {
          code += "(data){\n"
        }
        code += "    return request({\n"
        code += "      url: '/online/api" + porp.path + "',\n"
        code += "      method: '" + porp.method + "',\n"
        if (porp.method === 'get' || porp.method === 'GET') {
          code += "      params\n"
        } else {
          code += "      data\n"
        }
        code += "    })\n"
        code += "  }\n"
        code += "}"
        return code
      }
    },
  ]
})
const tabsName = ref('mapper.xml')
function tabHandleClick(tab, event) {

}
const { proxy } = getCurrentInstance();
/** 复制代码成功 */
function copyTextSuccess() {
  proxy.$modal.msgSuccess("复制成功");
}
const testData = ref({
  data: "",
  returnData: {},
})
const rules = ref({
  data: [
    { required: true, message: '请输入测试数据', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        try {
          JSON.parse(value)
          callback()
        } catch (error) {
          callback(new Error('错误的JSON字符串'))
        }
      }
    }
  ]
})
function jsonToTree(jsonString) {
  let json = {}
  try {
    json = JSON.parse(jsonString);
  } catch (e) {
    return ""
  }

  function objectToTree(o) {
    const tree = [];
    for (var key in o) {
      if (typeof o[key] === 'object') {
        tree.push({
          label: key,
          children: objectToTree(o[key])
        })
      } else {
        tree.push({ label: key + ":" + o[key] })
      }
    }
    return tree;
  }
  return objectToTree(json);
}
const returnTree = computed(() => {
  return jsonToTree(testData.value.returnData)
})
function testClick() {
  const requestObject = {
    url: '/online/api' + porp.path,
    method: porp.method,
  }
  if (porp.method === "GET" || porp.method === "get") {
    requestObject.params = JSON.parse(testData.value.data)
  } else {
    requestObject.data = JSON.parse(testData.value.data)
  }
  request(requestObject).then(res => {
    proxy.$modal.msgSuccess("请求成功")
    testData.value.returnData = JSON.stringify(res, null, 2)
  })
}
</script>
<template>
  <el-dialog :title="generated.path" width="800" append-to-body>
    <el-tabs v-model="tabsName" class="demo-tabs" @tab-click="tabHandleClick">
      <el-tab-pane v-for="code in generated.codes" :label="code.title" :name="code.title">
        <div>
          <div style="width: 100%;display: flex;justify-content: space-between;">
            <el-button link icon="DocumentCopy" @click="testClick">&nbsp;测试</el-button>
            <el-button link icon="DocumentCopy" v-copyText="code.generated()"
              v-copyText:callback="copyTextSuccess">&nbsp;复制</el-button>
          </div>
          <div style="display: flex;justify-content: center;align-content: center;">
            <div class="code-container">
              <pre v-text="code.generated()"></pre>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-form :rules="rules" :model="testData" hide-required-asterisk>
      <el-form-item prop="data" label="参数">
        <el-input v-model="testData.data" placeholder="请输入测试数据"></el-input>
      </el-form-item>
      <el-form-item label="响应">
        <div class="code-container">
          <el-tree style="width: 100%;" :data="returnTree" :props="{ children: 'children', label: 'label', }" />
        </div>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<style scoped lang="scss">
.code-container {
  width: 100%;
  height: 200px;
  overflow: auto;
  border: 1px solid #ebeef5;
}
</style>