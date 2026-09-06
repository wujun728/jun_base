<script setup name="GenEdit" lang="ts">
import { onMounted, ref } from 'vue';
import { optionselect as getDictOptionselect } from "@/api/system/dict/type";
import { GenColumn, genTableState } from '.';
const tables = genTableState().tables;
const info = genTableState().info;
const columns = genTableState().columns;
const tableHeight = ref(document.documentElement.scrollHeight - 245 + "px");

function setSubTableColumns(value: string) {
  for (const item in tables.value) {
    const name = tables.value[item].tableName;
    if (value === name) {
      return tables.value[item].columns;
    }
  }
}

const handleSubColumnNameChange = (column: GenColumn, val: string | undefined) => {
  column.subColumnJavaField = val?.replace(/_(\w)/g, (_, c) => c.toUpperCase()) ?? '';
  column.subColumnJavaType = setSubTableColumns(column.subColumnTableName)?.find(item => item.columnName === column.subColumnName)?.javaType ?? ''
};

const dictOptions = ref<Dict[]>([]);
onMounted(() => { getDictOptionselect().then(response => { dictOptions.value = response.data; }); })
</script>
<template>
  <div>
    <el-switch v-model="info.haveSubColumn" active-value="1" inactive-value="0" active-text="开启字段关联"
      inactive-text="关闭字段关联" />
    <el-table ref="dragTable" :data="columns" row-key="columnId" :max-height="tableHeight" style="width: 100%">
      <el-table-column label="序号" type="index" min-width="5%" fixed />
      <el-table-column fixed label="字段列名" prop="columnName" min-width="10%" :show-overflow-tooltip="true" />
      <el-table-column label="字段描述" min-width="10%">
        <template #default="scope">
          <el-input v-model="scope.row.columnComment" />
        </template>
      </el-table-column>
      <el-table-column label="物理类型" prop="columnType" min-width="10%" :show-overflow-tooltip="true" />
      <el-table-column label="Java类型" min-width="11%">
        <template #default="scope">
          <el-select v-model="scope.row.javaType">
            <el-option label="Long" value="Long" />
            <el-option label="String" value="String" />
            <el-option label="Integer" value="Integer" />
            <el-option label="Double" value="Double" />
            <el-option label="BigDecimal" value="BigDecimal" />
            <el-option label="Date" value="Date" />
            <el-option label="Boolean" value="Boolean" />
            <el-option label="LocalDate" value="LocalDate" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="java属性" min-width="10%">
        <template #default="scope">
          <el-input v-model="scope.row.javaField" />
        </template>
      </el-table-column>
      <el-table-column label="插入" min-width="5%">
        <template #default="scope">
          <el-checkbox true-value="1" false-value="0" v-model="scope.row.isInsert" />
        </template>
      </el-table-column>
      <el-table-column label="编辑" min-width="5%">
        <template #default="scope">
          <el-checkbox true-value="1" false-value="0" v-model="scope.row.isEdit" />
        </template>
      </el-table-column>
      <el-table-column label="列表" min-width="5%">
        <template #default="scope">
          <el-checkbox true-value="1" false-value="0" v-model="scope.row.isList" />
        </template>
      </el-table-column>
      <el-table-column label="查询" min-width="5%">
        <template #default="scope">
          <el-checkbox true-value="1" false-value="0" v-model="scope.row.isQuery" />
        </template>
      </el-table-column>
      <el-table-column label="查询方式" min-width="10%">
        <template #default="scope">
          <el-select v-model="scope.row.queryType">
            <el-option label="=" value="EQ" />
            <el-option label="!=" value="NE" />
            <el-option label=">" value="GT" />
            <el-option label=">=" value="GTE" />
            <el-option label="<" value="LT" />
            <el-option label="<=" value="LTE" />
            <el-option label="LIKE" value="LIKE" />
            <el-option label="BETWEEN" value="BETWEEN" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="必填" min-width="5%">
        <template #default="scope">
          <el-checkbox true-value="1" false-value="0" v-model="scope.row.isRequired"></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column label="显示类型" min-width="12%">
        <template #default="scope">
          <el-select v-model="scope.row.htmlType">
            <el-option label="文本框" value="input" />
            <el-option label="文本域" value="textarea" />
            <el-option label="下拉框" value="select" />
            <el-option label="单选框" value="radio" />
            <el-option label="复选框" value="checkbox" />
            <el-option label="日期控件" value="datetime" />
            <el-option label="图片上传" value="imageUpload" />
            <el-option label="文件上传" value="fileUpload" />
            <el-option label="富文本控件" value="editor" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="字典类型" min-width="12%">
        <template #default="scope">
          <el-select v-model="scope.row.dictType" clearable filterable placeholder="请选择">
            <el-option v-for="dict in dictOptions" :key="dict.dictType" :label="dict.dictName" :value="dict.dictType">
              <span style="float: left">{{ dict.dictName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.dictType }}</span>
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column type="expand">
        <template #default="scope">
          <div style="width: 90%;">
            <el-form :disabled="parseInt(info.haveSubColumn) != 1" label-width="150px">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="关联表">
                    <el-select v-model="scope.row.subColumnTableName" clearable placeholder="请选择">
                      <el-option v-for="(table, index) in tables" :key="index"
                        :label="table.tableName + '：' + table.tableComment" :value="table.tableName" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="关联字段">
                    <el-select v-model="scope.row.subColumnFkName" clearable placeholder="请选择">
                      <el-option v-for="(column, index) in setSubTableColumns(scope.row.subColumnTableName)"
                        :key="index" :label="column.columnName + '：' + column.columnComment"
                        :value="column.columnName" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="映射字段">
                    <el-select v-model="scope.row.subColumnName" clearable placeholder="请选择"
                      @change="(val: string) => handleSubColumnNameChange(scope.row, val)">
                      <el-option v-for="(column, index) in setSubTableColumns(scope.row.subColumnTableName)"
                        :key="index" :label="column.columnName + '：' + column.columnComment"
                        :value="column.columnName" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="java属性">
                    <el-input v-model="scope.row.subColumnJavaField" clearable></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="映射字段Java类型">
                    <el-select v-model="scope.row.subColumnJavaType" clearable>
                      <el-option label="Long" value="Long" />
                      <el-option label="String" value="String" />
                      <el-option label="Integer" value="Integer" />
                      <el-option label="Double" value="Double" />
                      <el-option label="BigDecimal" value="BigDecimal" />
                      <el-option label="Date" value="Date" />
                      <el-option label="Boolean" value="Boolean" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>