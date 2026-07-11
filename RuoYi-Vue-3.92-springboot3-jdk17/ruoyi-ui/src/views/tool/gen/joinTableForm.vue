<template>
  <div class="join-table-form">
    <el-row>
      <el-col :span="24">
        <el-form-item label="选择关联表">
          <el-select
            v-model="selectedTableId"
            placeholder="请选择要关联的表"
            filterable
            clearable
            @change="addJoinTable"
            style="width:300px"
          >
            <el-option
              v-for="table in availableTables"
              :key="table.tableId || table.tableName"
              :label="table.tableName + '：' + table.tableComment"
              :value="table.tableId || table.tableName"
            />
          </el-select>
          <el-button type="primary" plain size="small" icon="el-icon-plus" @click="addJoinRelation" style="margin-left:10px">添加关联关系</el-button>
        </el-form-item>
      </el-col>
    </el-row>

    <el-card v-for="(join, index) in joins" :key="index" class="join-card" shadow="never">
      <div slot="header" class="join-card-header">
        <span>关联关系 {{ join.orderNum || (index + 1) }}</span>
        <el-button type="text" icon="el-icon-delete" style="color:#F56C6C;float:right" @click="removeJoin(index)">删除</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="关联顺序">
            <el-input-number v-model="join.orderNum" :min="1" size="small" controls-position="right" style="width:100%"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="关联类型">
            <el-select v-model="join.joinType" size="small" style="width:100%">
              <el-option label="LEFT JOIN" value="left"/>
              <el-option label="RIGHT JOIN" value="right"/>
              <el-option label="INNER JOIN" value="inner"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="10">
          <el-card shadow="never" :class="['sub-card', { 'new-table-selected': join.newTableId === join.leftTableId }]">
            <div slot="header">
              <span>左表</span>
              <el-button type="text" size="mini" style="float:right" @click="setNewTable(join, join.leftTableId)">设为新表</el-button>
            </div>
            <el-form-item label="左表名" label-width="70px">
              <el-select v-model="join.leftTableId" size="small" @change="val => onLeftTableChange(val, index)" style="width:100%">
                <el-option v-for="t in allTables" :key="t.tableName" :label="t.tableName + ' (' + t.tableComment + ')'" :value="t.tableId || t.tableName"/>
              </el-select>
            </el-form-item>
            <el-form-item label="别名" label-width="70px">
              <el-input v-model="join.leftTableAlias" size="small" placeholder="如 t" />
            </el-form-item>
            <el-form-item label="关联键" label-width="70px">
              <el-select v-model="join.leftTableFk" size="small" filterable style="width:100%" placeholder="选择关联字段">
                <el-option v-for="col in getTableColumns(join.leftTableId)" :key="col.columnId || col.columnName" :label="col.columnName + ' (' + col.columnComment + ')'" :value="col.columnId || col.columnName"/>
              </el-select>
            </el-form-item>
          </el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="never" :class="['sub-card', { 'new-table-selected': join.newTableId === join.rightTableId }]">
            <div slot="header">
              <span>右表</span>
              <el-button type="text" size="mini" style="float:right" @click="setNewTable(join, join.rightTableId)">设为新表</el-button>
            </div>
            <el-form-item label="右表名" label-width="70px">
              <el-select v-model="join.rightTableId" size="small" @change="val => onRightTableChange(val, index)" style="width:100%">
                <el-option v-for="t in allTables" :key="t.tableName" :label="t.tableName + ' (' + t.tableComment + ')'" :value="t.tableId || t.tableName"/>
              </el-select>
            </el-form-item>
            <el-form-item label="别名" label-width="70px">
              <el-input v-model="join.rightTableAlias" size="small" placeholder="如 t1" />
            </el-form-item>
            <el-form-item label="关联键" label-width="70px">
              <el-select v-model="join.rightTableFk" size="small" filterable style="width:100%" placeholder="选择关联字段">
                <el-option v-for="col in getTableColumns(join.rightTableId)" :key="col.columnId || col.columnName" :label="col.columnName + ' (' + col.columnComment + ')'" :value="col.columnId || col.columnName"/>
              </el-select>
            </el-form-item>
          </el-card>
        </el-col>
        <el-col :span="4" v-if="join.newTableId">
          <div class="column-selector">
            <el-form-item label="选择新表字段" label-width="90px">
              <el-checkbox-group v-model="join.joinColumns">
                <el-checkbox
                  v-for="col in getTableColumns(join.newTableId)"
                  :key="col.columnId || col.columnName"
                  :label="col.columnId || col.columnName"
                  :value="col.columnId || col.columnName"
                >{{ col.columnName }}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "JoinTableForm",
  props: {
    tables: { type: Array, default: () => [] },
    mainTable: { type: Object, default: () => ({}) },
    joinsProp: { type: Array, default: () => [] }
  },
  data() {
    return {
      joins: [],
      selectedTableId: null,
      tableColumnsCache: {}
    }
  },
  computed: {
    allTables() {
      const list = [...this.tables]
      if (this.mainTable && this.mainTable.tableName) {
        if (!list.find(t => (t.tableName || t.tableId) === (this.mainTable.tableName || this.mainTable.tableId))) {
          list.unshift(this.mainTable)
        }
      }
      return list
    },
    availableTables() {
      return this.tables.filter(t => {
        const key = t.tableName || t.tableId
        return !this.joins.some(j =>
          (j.leftTableId === key || j.rightTableId === key ||
           j.newTableId === key) && j.leftTableId && j.rightTableId
        )
      })
    }
  },
  watch: {
    joinsProp: {
      immediate: true,
      handler(val) {
        if (val && val.length > 0) {
          this.joins = JSON.parse(JSON.stringify(val))
        }
      }
    }
  },
  mounted() {
    if (this.joins.length === 0) {
      this.addJoinRelation()
    }
  },
  methods: {
    getTableColumns(tableId) {
      if (!tableId) return []
      const table = this.allTables.find(t => (t.tableId || t.tableName) === tableId)
      return table ? (table.columns || []) : []
    },
    addJoinTable() {
      if (this.selectedTableId) {
        this.joins.push({
          tableId: this.mainTable.tableId || this.mainTable.tableName,
          leftTableId: this.mainTable.tableId || this.mainTable.tableName,
          rightTableId: this.selectedTableId,
          newTableId: this.selectedTableId,
          leftTableAlias: '',
          rightTableAlias: '',
          leftTableFk: null,
          rightTableFk: null,
          joinType: 'left',
          joinColumns: [],
          orderNum: this.joins.length + 1
        })
        this.selectedTableId = null
      }
    },
    addJoinRelation() {
      this.joins.push({
        tableId: this.mainTable.tableId || this.mainTable.tableName,
        leftTableId: this.mainTable.tableId || this.mainTable.tableName,
        rightTableId: null,
        newTableId: null,
        leftTableAlias: '',
        rightTableAlias: '',
        leftTableFk: null,
        rightTableFk: null,
        joinType: 'left',
        joinColumns: [],
        orderNum: this.joins.length + 1
      })
    },
    removeJoin(index) {
      this.joins.splice(index, 1)
    },
    setNewTable(join, tableId) {
      if (join.newTableId === tableId) {
        join.newTableId = null
        join.joinColumns = []
      } else {
        join.newTableId = tableId
      }
    },
    onLeftTableChange(val, index) {
      const table = this.allTables.find(t => (t.tableId || t.tableName) === val)
      if (table) {
        this.joins[index].leftTableAlias = table.tableName ? table.tableName.substring(0, 3) : ''
      }
    },
    onRightTableChange(val, index) {
      const table = this.allTables.find(t => (t.tableId || t.tableName) === val)
      if (table) {
        this.joins[index].rightTableAlias = table.tableName ? table.tableName.substring(0, 3) : ''
      }
    },
    getJoins() {
      return this.joins
    }
  }
}
</script>

<style scoped>
.join-card {
  margin-bottom: 15px;
}
.join-card-header {
  font-weight: bold;
}
.sub-card {
  border: 1px solid #dcdfe6;
}
.new-table-selected {
  border-color: #409EFF;
  background: #ecf5ff;
}
.column-selector {
  max-height: 250px;
  overflow-y: auto;
}
</style>
