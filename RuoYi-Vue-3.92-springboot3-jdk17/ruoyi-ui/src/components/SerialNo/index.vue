<template>
  <!-- 编号 -->
  <div>
    <div class="serial-select-trigger">
      <div v-if="calSerialNO.length > 0" class="selected-serial">
        <span class="serial-chip">
          <span class="ml5">{{ calSerialNO }}</span>
        </span>
        <el-button class="action-button" type="primary" plain size="mini" @click="openSelect">重新生成</el-button>
      </div>
      <span v-else class="placeholder">
        {{ placeholder }}
        <el-button class="action-button" type="primary" size="mini" @click="openSelect">生成编号</el-button>
      </span>
    </div>
    <!-- 选择编号 -->
    <el-dialog title="生成编号" :visible.sync="open" width="600px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="编号规则" prop="id">
          <template>
            <el-select v-model="form.confId" clearable filterable placeholder="请选择">
              <el-option v-for="conf in serialConfOptions" :key="conf.id" :label="conf.title" :value="conf.id">
                <span style="float: left">{{ conf.title }}</span>
              </el-option>
            </el-select>
          </template>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getSerialNo">生成编号</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { genSerialNo, listAllOptions } from "@/api/serial/config.js";

export default {
  name: "DesignSerialNo",
  props: {
    // 绑定的值
    value: String,
    // 配置的属性--start
    multiple: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    placeholder: {
      type: String,
      default: null,
    },
    // 配置的属性--end
  },
  data() {
    return {
      // 组件开关
      open: false,
      serialNo: "",
      // 编号配置选项
      serialConfOptions: [],
      form: {},
      rules: {
        businessType: [{ required: true, message: "请选择编号业务", trigger: "change" }],
      },
    };
  },
  computed: {
    calSerialNO() {
      return this.value || "";
    },
  },
  watch: {
    value: {
      handler(val) {
        this.serialNo = val || "";
      },
    },
    immediate: true,
  },
  created() {
    this.getSerialConfOpts();
  },
  methods: {
    /** 打开组件 */
    openSelect() {
      this.open = true;
    },
    /** 获取编号所有配置 */
    getSerialConfOpts() {
      listAllOptions().then((res) => {
        if (res.code === 200) {
          this.serialConfOptions = res.data;
        }
      });
    },
    /** 生成编号 */
    getSerialNo() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          genSerialNo(this.form.confId).then((res) => {
            if (res.code === 200) {
              this.open = false;
              this.serialNo = res.data;
              this.$emit("input", this.serialNo);
            }
          });
        }
      });
    },
    cancel() {
      this.open = false;
    },
  },
};
</script>
<style scoped lang="scss">
.serial-select-trigger {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 4px 6px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;

  :hover {
    border-color: #409eff;
  }

  .selected-serial {
    display: flex;
    align-items: center;
    flex-grow: 1;
  }
  .serial-chip {
    display: inline-block;
    align-items: center;
    font-size: 14px;
    height: 24px;
    line-height: 24px;
  }
}

.placeholder {
  color: #c0c4cc;
  padding-left: 5px;
  height: 26px;
  line-height: 26px;
  display: flex;
  align-items: center;
  flex-grow: 1;
}

.action-button {
  margin-left: auto;
  font-size: 12px;
  padding: 6px;
}

::v-deep .el-dialog__body {
  height: 200px;
  overflow-y: auto;
}
</style>