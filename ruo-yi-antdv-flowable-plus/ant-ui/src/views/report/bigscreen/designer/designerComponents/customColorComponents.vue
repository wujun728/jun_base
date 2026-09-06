<template>
  <div>
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-plus"
      plain
      @click="handleAddClick"
    >新增</el-button
    >
    <el-table :data="formData" style="width: 100%">
      <el-table-column prop="color" label="颜色" align="center">
        <template slot-scope="scope">
          <span class="color-box" :style="{ background: scope.row.color }" />
        </template>
      </el-table-column>
      <el-table-column label="位置" align="center">
        <template slot-scope="scope">
          <span
            class="editor"
            @click="handleEditorClick(scope.$index, scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <span
            class="editor"
            @click="handleDeleteClick(scope.$index, scope.row)"
          >
            <i class="el-icon-delete" /> 删除
          </span>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="新增"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-form>
        <el-form-item label="颜色">
          <el-input
            v-model="colorValue"
            style="width: 200px"
            placeholder="请输入颜色"
            size="mini"
          >
            <template slot="append">
              <el-color-picker
                v-model="colorValue"
                :predefine="predefineColors"
                size="mini"
              />
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button
          size="mini"
          type="primary"
          @click="handleSaveClick"
        >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'CustomColorComponents',
  model: {
    prop: 'formData',
    event: 'input'
  },
  props: {
    formData: Array
  },
  data() {
    return {
      predefineColors: [
        '#ff4500',
        '#ff8c00',
        '#ffd700',
        '#90ee90',
        '#00ced1',
        '#1e90ff',
        '#c71585'
      ],
      colorValue: '',
      dialogVisible: false,
      flag: true, // true 新增， false 编辑
      indexEditor: -1 // 编辑第几个数据
    }
  },
  mounted() {},
  methods: {
    // 弹出框关闭
    handleClose() {
      this.dialogVisible = false
      this.colorValue = ''
    },
    // 新增
    handleAddClick() {
      this.colorValue = ''
      this.flag = true
      this.dialogVisible = true
    },
    // 确定
    handleSaveClick() {
      if (this.flag) {
        // 新增
        const obj = {
          color: this.colorValue
        }
        this.formData.push(obj)
        this.dialogVisible = false
      } else {
        // 编辑
        this.formData[this.indexEditor].color = this.colorValue
        this.dialogVisible = false
      }
      this.$emit('input', this.formData)
      this.$emit('change', this.formData)
    },
    // 编辑
    handleEditorClick(index, row) {
      this.flag = false
      this.colorValue = row.color
      this.dialogVisible = true
      this.indexEditor = index
    },
    // 删除
    handleDeleteClick(index) {
      this.formData.splice(index, 1)
      this.$emit('input', this.formData)
      this.$emit('change', this.formData)
    }
  }
}
</script>
<style lang="scss" scoped>
.color-box {
  .title {
    display: flex;
    flex-direction: row;
  }
}
/deep/.el-table,
/deep/.el-table__expanded-cell,
/deep/.el-table th,
/deep/.el-table tr {
  background-color: transparent !important;
  color: #859094 !important;
}
/deep/.el-table td,
/deep/.el-table th.is-leaf {
  border-bottom: none;
  line-height: 26px;
}
/deep/.el-table tbody tr:hover > td {
  background-color: #263445 !important;
}
/deep/.el-table::before {
  height: 0;
}
/deep/.el-color-picker--mini,
/deep/.el-color-picker--mini .el-color-picker__trigger {
  width: 23px;
  height: 23px;
}
/deep/.el-dialog {
  background: #1b1e25;
  .el-dialog__title {
    color: #fff;
  }
}
.color-box {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 5px;
}
.editor {
  color: #409eff;
  cursor: pointer;
}
</style>
