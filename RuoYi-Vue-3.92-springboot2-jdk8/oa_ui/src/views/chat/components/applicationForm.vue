<template>
  <div>
    <div class="flex-row-center">
      <div class="upload-logo">
        <img :src="form.pic" alt="" class="logo" @click="uploadPicture" />
        <div class="btn acea-row row-center-wrapper" @click="uploadPicture">
          <i class="iconfont iconbianji3"></i>
        </div>
      </div>
    </div>
    <el-form ref="form" :model="form" label-position="top" :rules="formRules">
      <el-form-item prop="name">
        <div slot="label" class="label">
          应用名称
          <popover :tips="`AI应用展示名称`" :width="150" />
        </div>
        <el-input v-model="form.name" size="small" maxlength="20" show-word-limit></el-input>
      </el-form-item>
      <el-form-item>
        <div slot="label" class="label">
          应用简介
          <popover :tips="`请输入一段简明扼要又吸睛的介绍，可以快速吸引用户使用你的智能体`" :width="315" />
        </div>
        <el-input v-model="form.info" type="textarea" size="small" maxlength="100" show-word-limit></el-input>
      </el-form-item>
      <el-form-item prop="tooltip_text">
        <div slot="label" class="label">
          <!-- <span class="el-icon-caret-right" :class="show2 ? 'rotating' : 'norotating'" @click="show2 = !show2"></span> -->
          <!-- <span class="required-icon">*</span> -->
          人设与回复逻辑
          <popover
            :tips="`通过提示词，你能精确设定应用的作用范围。包括指定应用将扮演的角色，能够使用的组件以及输出结果的格式与风格；此外你还可以规定应用不得执行哪些操作等`"
          ></popover>
        </div>
        <el-collapse-transition>
          <div>
            <el-input
              v-model="form.tooltip_text"
              type="textarea"
              class="textareaBox height395"
              :placeholder="placeholder"
              maxlength="3000"
            >
            </el-input>
            <div class="append">
              <span class="num-color"> {{ form.tooltip_text.length }} / 3000</span>
              <span class="iconfont iconshanchujilu" @click="form.tooltip_text = ''"></span>
              <span class="iconfont iconzhankai2" @click="openText(3000, form.tooltip_text, 'tooltip_text')"></span>
            </div>
          </div>
        </el-collapse-transition>
      </el-form-item>
      <el-form-item prop="edit">
        <div slot="label" class="label">
          编辑权限 <popover :tips="`拥有编辑权限的成员，可以进行AI应用编排`"></popover>
        </div>
        <selectMember
          ref="selectMember"
          :value="edituser"
          :placeholder="`请选择拥有编辑权限的人员(多选)`"
          @getSelectList="getSelectList($event, 'edit')"
        ></selectMember>
      </el-form-item>
      <el-form-item prop="auth_ids">
        <div slot="label" class="label">应用权限 <popover :tips="`请选择允许使用该应用的人员`"></popover></div>
        <selectMember
          ref="selectMember"
          :value="authuser"
          :placeholder="`请选择应用权限(多选)`"
          @getSelectList="getSelectList($event, 'auth_ids')"
        ></selectMember>
      </el-form-item>
      <el-form-item prop="use_limit">
        <div slot="label" class="label">
          使用频次
          <popover :tips="`用于设置允许用户使用频次，到达限制以后不允许继续访问，0表示不限制`"></popover>
        </div>
        <el-input v-model="form.use_limit" size="small" type="number">
          <span slot="suffix" class="suffix-text"> 次/人天</span>
        </el-input>
      </el-form-item>
      <el-form-item prop="sort">
        <div slot="label" class="label">
          应用排序
          <popover :tips="`应用顺序调整，数字越大越靠前`" />
        </div>
        <el-input
          v-model="form.sort"
          :min="0"
          size="small"
          type="number"
          oninput="value=value.replace(/^0+(\d)|[^\d]+/g,'')"
        >
        </el-input>
      </el-form-item>
    </el-form>
    <el-dialog :before-close="handleClose" :visible.sync="dialogVisible" title="选择图片" v-bind="$attrs" width="850px">
      <upload-picture ref="uploadPicture" :check-button="true" @getImage="getImage"></upload-picture>
    </el-dialog>
    <textDialog ref="textDialog" @submit="getTextData"></textDialog>
  </div>
</template>
<script>
import popover from './popover'
import uploadPicture from '@/components/uploadPicture/index'
import selectMember from '@/components/form-common/select-member'
import { extractArrayIds } from '@/libs/public'
import textDialog from './textDialog'

export default {
  name: '',
  components: { popover, selectMember, uploadPicture, textDialog },
  props: {
    info: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    var validate = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入提示词'))
      } else {
        callback()
      }
    }
    var userValidate = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请选择编辑权限'))
      } else {
        callback()
      }
    }
    var authValidate = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error('请选择应用人员'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      url: '',
      form: {
        name: '',
        info: '',
        pic: '',
        tooltip_text: '',
        edit: [],
        auth_ids: [],
        use_limit: 0,
        sort: 0
      },
      edituser: [],
      authuser: [],
      formRules: {
        name: [{ required: true, message: '请输入应用名称', trigger: 'blur' }]
        // edit: [{ required: true, validator: userValidate, trigger: 'blur' }],
        // auth_ids: [{ required: true, validator: authValidate, trigger: 'blur' }],
        // use_limit: [{ required: true, message: '请输入使用频次', trigger: 'change' }]
        // tooltip_text: {
        //   validator: validate,
        //   trigger: 'blur'
        // }
      }
    }
  },

  watch: {
    info: {
      handler(val) {
        for (let key in this.form) {
          if (val[key]) {
            this.form[key] = val[key]
          }
        }
        this.form.edit = extractArrayIds(val.edit, 'id')
        this.form.auth_ids = extractArrayIds(val.auth_ids, 'id')
        this.edituser = val.edit
        this.authuser = val.auth_ids
      },
      immediate: true,
      deep: true
    }
  },
  mounted() {
    this.form.pic = JSON.parse(localStorage.getItem('sitedata')).site_logo
  },
  computed: {
    placeholder: function () {
      let str = `#角色规范
你是一个 XXXX 小助手，你的任务是 XXXX。

#思考规范

- 在回答问题时，你需要分析用户的问题，确保理解需求和上下文。
- 当用户的需求不明确时，你应该主动优先明确用户需求。
- 对于超出 本角色 小助手服务范围的需求，你需要按如下话术委婉拒答：抱歉，并引导用户提出关于 本角色 相关的问题。

#回复规范

- 你需要以 简洁高效的语气风格 回复用户。
- 在每次结束对话时你可以向用户提问并引导相关话题深入。`
      return str
    }
  },

  methods: {
    getTextData(val, type) {
      this.form[type] = val
    },
    openText(max, text, type) {
      let obj = {
        max,
        text,
        type
      }
      this.$refs.textDialog.openBox(obj)
    },
    // 提交数据
    getData(val) {
      if (val) {
        return new Promise((resolve, reject) => {
          this.$refs['form'].validate((valid) => {
            if (!valid) {
              reject({ target: 'applicationForm' })
              return
            }
            resolve({ applicationForm: this.form })
          })
        })
      } else {
        return new Promise((resolve, reject) => {
          resolve({ applicationForm: this.form })
        })
      }
    },
    // 选择人员
    getSelectList(val, key) {
      let userId = []
      if (key === 'edit') {
        this.edituser = val
      }

      val.map((item) => {
        userId.push(item.value)
      })
      this.form[key] = userId
    },
    uploadPicture() {
      this.dialogVisible = true
    },
    handleClose() {
      this.dialogVisible = false
    },
    getImage(val) {
      this.form.pic = val.att_dir
      this.dialogVisible = false
    }
  }
}
</script>
<style scoped lang="scss">
::v-deep.el-form-item__label {
  display: flex;
  align-items: center;
  height: 18px;
  margin-bottom: 10px;
  padding: 0;
}
.num-color {
  color: #909399;
}
.required-icon {
  color: #ed4014;
  margin-right: 4px;
}
.textareaBox {
  position: relative;
  ::v-deep .el-textarea__inner {
    resize: none;
  }
}
.height395 {
  ::v-deep .el-textarea__inner {
    height: 395px;
  }
}

.label {
  display: flex;
  font-weight: 400;
  font-size: 13px;
  color: #303133;
  line-height: 13px;
}
.iconshuoming {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
}
.suffix-text {
  font-weight: 400;
  font-size: 12px;
  color: #303133;
}

.upload-logo {
  position: relative;
  &:hover .btn {
    display: flex;
  }
  .logo {
    cursor: pointer;
    display: block;
    width: 68px;
    height: 68px;
    border-radius: 50%;
    margin: 20px 0;
  }

  .btn {
    display: none;
    position: absolute;
    top: 20px;
    right: 0;
    bottom: 0;
    left: 0;
    width: 68px;
    height: 68px;
    border-radius: 50%;
    background: rgba(43, 42, 42, 0.4);
    .iconbianji3 {
      color: #fff;
      font-size: 20px;
    }
  }
}
.append {
  background: #fff;
  display: flex;
  position: absolute;
  bottom: 1px;
  right: 10px;
  font-weight: 400;
  font-size: 12px;
  color: #606266;
  .iconfont {
    font-size: 14px;
    margin-left: 8px;
    cursor: pointer;
  }
}
</style>
