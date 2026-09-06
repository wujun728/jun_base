<script setup lang="ts">
import { listTable, getGenTable } from "@/api/tool/gen";
import { ref, watch } from 'vue';
import { GenJoin, genTableState } from ".";
const info = genTableState().info;
const tables = genTableState().tables;
const tableDict = genTableState().tableDict;
const joins = genTableState().joins;
const selectTables = ref<number[]>([])
const loading = ref(false);
const options = ref<{ value: number; label: string }[]>([])
const remoteMethod = (query: string) => {
  loading.value = true;
  listTable({ tableName: query }).then((response) => {
    loading.value = false;
    options.value = response.rows.map((item) => ({ value: item.tableId, label: item.tableName }));
  });
}
watch(tables, () => {
  tables.value?.forEach(item => tableDict.value[item.tableId] = item);
  options.value = tables.value?.map(item => ({ value: item.tableId, label: item.tableName }));
  selectTables.value = tables.value.map(item => item.tableId);
})

// 添加关联关系
const addJoin = () => {
  const newJoin = new GenJoin()
  newJoin.tableId = info.value.tableId;
  newJoin.leftTableId = info.value.tableId;
  newJoin.leftTableAlias = info.value.tableAlias;
  newJoin.joinColumns = []
  joins.value.push(newJoin);
};

// 获取表信息
async function getTable(tableId: number) {
  if (tableDict.value[tableId]) return tableDict.value[tableId]
  else {
    const table = await getGenTable(tableId).then(res => res.data.table);
    tableDict.value[tableId] = table;
    return table;
  }
}
// 处理关联表选择变化
const handleLeftTableChange = async (tableId: number, index: number) => {
  if (joins.value[index].newTableId === joins.value[index].leftTableId) {
    joins.value[index].joinColumns = [];
  }
  const table = await getTable(tableId);
  joins.value[index].tableId = info.value.tableId;
  joins.value[index].leftTableAlias = table.tableAlias;
  joins.value[index].leftTableId = table.tableId;
};
// 处理关联表选择变化
const handleRightTableChange = async (tableId: number, index: number) => {
  if (joins.value[index].newTableId === joins.value[index].rightTableId) {
    joins.value[index].joinColumns = [];
  }
  const table = await getTable(tableId);
  joins.value[index].tableId = info.value.tableId;
  joins.value[index].rightTableAlias = table.tableAlias;
  joins.value[index].rightTableId = table.tableId;
};
// 设置新表
const setNewTable = (join: GenJoin, tableId: number) => {
  join.newTableId = tableId;
  join.joinColumns = [];
};
// 删除关联关系
const removeJoin = (index: number) => joins.value.splice(index, 1);
</script>
<template>
  <el-form label-width="150px">
    <el-row>
      <el-col :span="16">
        <el-form-item label="添加可选择关联表">
          <el-select v-model="selectTables" multiple placeholder="" clearable filterable remote
            :remote-method="remoteMethod" :loading="loading">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item>
          <el-button type="primary" @click="addJoin">添加关联关系</el-button>
        </el-form-item>
      </el-col>
    </el-row>
    <div class="join-card">
      <el-form v-for="(join, index) in joins" :key="index" label-width="85px" class="join-form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="关联顺序">
              <el-input v-model="join.orderNum" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联类型">
              <el-select v-model="join.joinType" placeholder="选择关联类型">
                <el-option v-for="type in ['left', 'right', 'inner']" :key="type" :label="type" :value="type" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="左表">
              <el-select v-model="join.leftTableId" @change="(val: number) => handleLeftTableChange(val, index)"
                placeholder="选择关联表">
                <el-option v-for="tableId in selectTables" :key="tableId" :label="tableDict[tableId]?.tableName"
                  :value="tableId" />
              </el-select>
            </el-form-item>
            <el-form-item label="左表别名">
              <div style="display: flex; gap: 10px;">
                <el-input v-model="join.leftTableAlias" placeholder="请输入左表别名" />
                <el-button :type="join.newTableId === join.leftTableId ? 'success' : 'info'"
                  @click="setNewTable(join, join.leftTableId)">设为新表</el-button>
              </div>
            </el-form-item>
            <el-form-item label="左表关联键">
              <el-select v-model="join.leftTableFk" placeholder="选择左表关联键">
                <el-option v-for="column in tableDict[join.leftTableId]?.columns || []" :key="column.columnId"
                  :label="column.columnName" :value="column.columnId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="右表">
              <el-select v-model="join.rightTableId" @change="(val: number) => handleRightTableChange(val, index)"
                placeholder="选择右表">
                <el-option v-for="tableId in selectTables" :key="tableId" :label="tableDict[tableId]?.tableName"
                  :value="tableId" />
              </el-select>
            </el-form-item>
            <el-form-item label="右表别名">
              <div style="display: flex;gap: 10px;">
                <el-input v-model="join.rightTableAlias" placeholder="请输入右表别名" />
                <el-button :type="join.newTableId === join.rightTableId ? 'success' : 'info'"
                  @click="setNewTable(join, join.rightTableId)">设为新表</el-button>
              </div>
            </el-form-item>
            <el-form-item label="右表关联键">
              <el-select v-model="join.rightTableFk" placeholder="选择右表关联键">
                <el-option v-for="column in tableDict[join.rightTableId]?.columns || []" :key="column.columnId"
                  :label="column.columnName" :value="column.columnId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="添加字段">
              <el-select v-model="join.joinColumns" multiple value-key="tableId" placeholder="">
                <el-option v-for="column in tableDict[join.newTableId]?.columns || []" :key="column.columnId"
                  :label="column.columnName" :value="column.columnName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-button style="width: 100%;" type="danger" @click="() => removeJoin(index)">删除</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </el-form>
</template>
<style scoped lang="scss">
.join-card {
  display: flex;
  flex-wrap: wrap;

  .join-form {
    margin: 1px;
    padding: 12px;
    border: 1px solid #dcdfe6;
    width: 550px;
    border-radius: 12px;
  }
}
</style>