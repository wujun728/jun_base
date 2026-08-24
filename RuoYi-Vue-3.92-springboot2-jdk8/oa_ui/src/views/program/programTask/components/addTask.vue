<!-- 新增任务侧滑弹窗  -->
<template>
  <div class="station">
    <el-drawer
      title="新建任务"
      :visible.sync="drawer"
      :modal="true"
      :wrapper-closable="false"
      :before-close="handleClose"
      :append-to-body="true"
      size="1120px"
    >
      <el-form ref="formData" :model="formData" label-width="110px" :rules="rule">
        <div class="left-card">
          <el-form-item label="任务名称：" prop="name">
            <el-input
              v-model="formData.name"
              maxlength="100"
              show-word-limit
              class="input-item"
              placeholder="请输入任务名称"
            />
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="关联项目：" prop="program_id">
                <el-select
                  v-model="formData.program_id"
                  size="small"
                  clearable
                  placeholder="请选择关联项目"
                  filterable
                  @change="handleContract(formData.program_id, formData)"
                >
                  <el-option v-for="item in projectList" :key="item.value" :label="item.label" :value="item.value">
                    <div>
                      <span>{{ item.label }}</span>
                      <span
                        v-if="item.status == 1 || item.status == 2"
                        :class="item.status == 1 ? 'program-stop' : 'program-close'"
                        >{{ item.status == 1 ? '已暂停' : '已关闭' }}</span
                      >
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="负责人：" class="select-bar">
                <el-select v-model="formData.uid" size="small" filterable placeholder="请选择负责人">
                  <el-option v-for="item in programMemberOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="计划时间：" class="select-bar">
                <el-date-picker
                  v-model="timeVal"
                  size="small"
                  type="daterange"
                  clearable
                  :format="'yyyy-MM-dd'"
                  :value-format="'yyyy-MM-dd'"
                  placeholder="请选择项目计划结束日期"
                  :range-separator="$t('toptable.to')"
                  :start-placeholder="$t('toptable.startdate')"
                  :end-placeholder="$t('toptable.endingdate')"
                  @change="onchangeTime"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="协作者：" class="select-bar">
                <el-select
                  v-model="formData.members"
                  size="small"
                  multiple
                  filterable
                  collapse-tags
                  placeholder="指派协作者"
                >
                  <el-option v-for="item in programMemberOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="content">
            <ueditor-form
              is="ueditorFrom"
              :border="true"
              :height="`560px`"
              type="notepad"
              ref="ueditorFrom"
              :content="content"
              @input="ueditorEdit"
            />
          </div>
        </div>
        <div class="right-card">
          <el-form-item label="父级任务：" prop="path">
            <el-cascader
              v-model="formData.path"
              :options="programOptions"
              :props="{ checkStrictly: true }"
              collapse-tags
              clearable
              filterable
              @change="handleRemove"
            >
            </el-cascader>
          </el-form-item>
          <el-form-item label="优先级：" class="select-bar">
            <el-select v-model="formData.priority" size="small" placeholder="请选择优先级">
              <el-option v-for="item in priorityOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关联版本：" prop="version_id">
            <el-select
              v-model="formData.version_id"
              size="small"
              clearable
              placeholder="请选择关联版本"
              style="width: 80%"
              filterable
            >
              <el-option v-for="item in programVersionList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
            <span class="pointer settings" @click="setVersion">设置</span>
          </el-form-item>
        </div>
      </el-form>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" @click="handleConfirm(1)">保存并继续新建</el-button>
        <el-button size="small" type="primary" @click="handleConfirm()">保存</el-button>
      </div>
    </el-drawer>
    <!-- 设置关联版本 -->
    <el-dialog
      top="13%"
      title="版本设置"
      :append-to-body="true"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleCloseVersion"
    >
      <draggable v-model="inputs" class="drag-area" @start="dragging = true" @end="dragging = false">
        <div v-for="(input, index) in inputs" :key="index" class="input-row">
          <i class="iconfont icontuodong pointer"></i>
          <el-input v-model="input.name" placeholder="请输入版本名称"></el-input>
          <span class="pointer" @click="removeInput(index)">-</span>
        </div>
      </draggable>
      <span class="add-input pointer" @click="addInput">
        <i class="el-icon-plus"></i>
        <span>添加版本</span>
      </span>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCloseVersion">取 消</el-button>
        <el-button type="primary" @click="submitVersion">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { saveProgramTaskApi, getProgramTaskInfoApi, getProgramVersionApi, setProgramVersionApi } from '@/api/program'
import draggable from 'vuedraggable'
export default {
  name: 'addProgram',
  components: {
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor'),
    draggable
  },
  props: {
    projectList: {
      type: Array,
      default: () => []
    },
    programVersionList: {
      type: Array,
      default: () => []
    },
    programOptions: {
      type: Array,
      default: () => []
    },
    programMemberOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      drawer: false,
      inputs: [
        {
          id: '',
          name: ''
        }
      ],
      dragging: false,
      id: 0,
      principal: [],
      type: 0,
      memberShow: false,
      onlyOne: false,
      edit: false,
      dialogVisible: false,
      timeVal: [],
      formData: {
        status: 0,
        path: []
      },
      priorityOptions: [
        {
          value: 1,
          label: '紧急'
        },
        {
          value: 2,
          label: '高'
        },
        {
          value: 3,
          label: '中'
        },
        {
          value: 4,
          label: '低'
        },
        {
          value: 0,
          label: '无优先级'
        }
      ],
      rule: {
        name: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
        program_id: [{ required: true, message: '请选择关联项目', trigger: 'change' }]
      },
      programId: '',
      currentPid: 0,
      content: ''
    }
  },
  watch: {
    'formData.describe': {
      handler(nVal, oVal) {
        if (nVal !== oVal) {
          this.content = nVal
        }
      },
      deep: true
    }
  },
  methods: {
    // 设置版本
    setVersion() {
      this.dialogVisible = true
      this.getVersion()
    },
    submitVersion() {
      setProgramVersionApi(this.formData.program_id, { data: this.inputs }).then(() => {
        this.dialogVisible = false
        this.inputs = []
        this.$emit('getProgramVersionList', this.formData.program_id)
      })
    },
    // 获取版本
    async getVersion() {
      const result = await getProgramVersionApi({ program_id: this.formData.program_id })
      this.inputs = result.data
    },
    handleCloseVersion() {
      this.dialogVisible = false
      this.inputs = []
    },
    addInput() {
      this.inputs.push({ id: '', name: '' })
    },
    removeInput(index) {
      this.inputs.splice(index, 1)
    },
    // 选择项目
    handleContract(eid, row) {
      this.formData.uid = ''
      this.formData.version_id = ''
      this.formData.members = []
      this.$emit('getProgramVersionList', eid)
      this.$emit('getMemberList', eid)
      this.$emit('getProgramSelectList', row)
    },
    // 获取任务详情
    async getProgramInfo(id) {
      const result = await getProgramTaskInfoApi(id)
      this.formData.program_id = result.data.program_id
      this.formData.path = result.data.path
      this.formData.path.push(this.currentPid)
      this.formData.pid = this.currentPid
      this.$emit('getProgramVersionList', result.data.program_id)
    },
    // 删除标签
    cardTag(index) {
      this.principal.splice(index, 1)
      this.formData.uid = ''
    },
    onchangeTime(e) {
      if (e == null) {
        this.timeVal = ''
        this.formData.plan_start = ''
        this.formData.plan_end = ''
      } else {
        this.formData.plan_start = this.timeVal[0]
        this.formData.plan_end = this.timeVal[1]
      }
    },
    handleRemove(e) {
      this.formData.path = e
      if (e && e.length > 0) {
        this.formData.pid = e[e.length - 1]
      }
    },
    ueditorEdit(e) {
      this.content = e
    },
    handleClose() {
      this.drawer = false
      this.reset()
      this.$refs.formData.resetFields()
    },
    openBox(id, data) {
      this.currentPid = data.pid
      this.formData.pid = data.pid
      this.programId = data.program_id
      this.content = data.describe
      this.drawer = true
      this.edit = id ? true : false
      this.id = id ? id : 0
      let result = {
        program_id: data.program_id,
        id: id
      }
      this.handleContract(this.programId, result)
      if (data) {
        this.formData = data
        this.timeVal = this.formData.plan_start ? [this.formData.plan_start, this.formData.plan_end] : []
        if (this.formData.admins) {
          this.principal = this.formData.admins
          let members = []
          members = this.principal.map((item) => {
            return item.id
          })
          this.formData.members = members
        }
      }
      if (this.edit) {
        this.getProgramInfo(id)
      }
    },
    reset() {
      this.formData = {
        name: '',
        uid: '',
        pid: 0,
        path: [],
        version_id: null,
        members: [],
        plan_start: '',
        plan_end: '',
        status: null,
        priority: '',
        describe: ''
      }
      this.timeVal = []
      this.edit = false
      this.principal = []
      this.content = ''
    },
    // 提交
    handleConfirm(type) {
      this.formData.describe = this.content
      this.formData.status = 0
      this.$refs.formData.validate((valid) => {
        if (valid) {
          let data = {
            page: 1,
            limit: 15,
            types: 0,
            pid: 0,
            program_id: this.programId || null,
            scope_frame: 'all',
            scope_normal: 0
          }
          saveProgramTaskApi(this.formData).then((res) => {
            this.drawer = false
            this.$emit('getTableData', '')
            if (type === 1) {
              let data = {
                program_id: this.formData.program_id
              }
              this.openBox(0, data)
              return
            }
            this.reset()
            this.$refs.formData.resetFields()
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.station ::v-deep.edui-editor-iframeholder {
  height: 300px !important;
}
.station ::v-deep.el-drawer__body {
  padding: 20px 20px 50px 20px;
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
::v-deep.el-cascader,
::v-deep .el-input-number,
::v-deep .el-select,
::v-deep .el-date-editor {
  width: 100%;
}
::v-deep.el-drawer__body {
  padding: 0 !important;
}
.el-form {
  height: 100%;
  display: flex;
  justify-content: space-between;
  .left-card {
    width: 800px;
    padding-top: 20px;
    .el-input__inner,
    .flex-box {
      height: 32px;
      line-height: 32px;
    }
    .content {
      margin-left: 20px;
      margin-right: 20px;
      padding-bottom: 30px;
    }
    .el-form-item {
      margin-right: 20px;
    }
  }

  .right-card {
    width: 320px;
    padding-top: 20px;
    margin-right: 20px;
    border-left: 1px solid #f2f6fc;
    ::v-deep.el-form-item__label {
      width: 94px !important;
    }
    ::v-deep.el-form-item__content {
      margin-left: 94px !important;
    }
  }
}
.align-center {
  align-items: center;
  span {
    font-size: 12px;
    color: #606266;
    margin-right: 20px;
    background: #f5f5f5;
    border-radius: 2px；;
  }
}
.settings {
  margin-left: 12px;
  color: #1890ff;
}
.input-row {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  .icontuodong {
    color: #dcdfe6;
    margin-right: 4px;
  }
  span {
    font-size: 16px;
    padding: 0px 5px;
    border-radius: 50%;
    background: #ed4014;
    color: #fff;
    line-height: 15px;
  }
}
.add-input {
  color: #1890ff;
}

.input-row .el-input {
  flex-grow: 1;
  margin-right: 10px;
}

.program-stop {
  font-size: 12px;
  border: 1px solid #f90;
  color: #f90;
  padding: 0 2px;
}
.program-close {
  font-size: 12px;
  border: 1px solid #909399;
  color: #909399;
  padding: 0 2px;
}
::v-deep.el-input--medium .el-input__inner {
  height: 32px;
  line-height: 32px;
}
</style>
