<template>
  <div>
    <el-form ref="form" :model="form" :rules="formRules" label-position="top">
      <div class="p20">
        <div class="title">模型设置</div>
        <el-form-item prop="models_id">
          <div slot="label" class="label">
            AI模型
            <popover :tips="`选择模型设置中配置好的AI模型`"></popover>
            <div class="parameter" @click="openFn">参数设置</div>
          </div>
          <el-select v-model="form.models_id" placeholder="请选模型" size="small" style="width: 100%">
            <el-option v-for="item in options" :key="item.value" :label="item.lable" :value="item.value"></el-option>
          </el-select>
          <!-- <el-input v-model="form.name" size="small" maxlength="20" show-word-limit></el-input> -->
        </el-form-item>
      </div>
      <el-divider></el-divider>
      <div class="p20">
        <el-form-item>
          <div slot="label" class="label">
            <span class="el-icon-caret-right" :class="show4 ? 'rotating' : 'norotating'" @click="show4 = !show4"></span>
            开场白
            <popover :tips="`将在用户开启对话时展示，引导用户快速了解功能并开启对话`" :width="250"></popover>
          </div>
          <el-collapse-transition>
            <div v-show="show4">
              <el-input
                type="textarea"
                class="textareaBox height90"
                placeholder="请输入内容"
                v-model="form.prologue_text"
                maxlength="200"
              >
              </el-input>
              <div class="append">
                <span class="num-color">{{ form.prologue_text.length }} / 200</span>
                <span class="iconfont iconzhankai2" @click="openText(200, form.prologue_text, 'prologue_text')"></span>
              </div>
            </div>
          </el-collapse-transition>
        </el-form-item>
        <el-form-item>
          <div slot="label" class="label">
            <span class="el-icon-caret-right" :class="show1 ? 'rotating' : 'norotating'" @click="show1 = !show1"></span>
            开场白问题
            <popover :tips="`至少写3个，超出后随机取3个展示`"></popover>
          </div>
          <el-collapse-transition>
            <div v-show="show1">
              <div v-for="(item, index) in form.prologue_list" :key="index">
                <el-input
                  v-model="form.prologue_list[index]"
                  class="parameter-input mb8"
                  size="small"
                  maxlength="50"
                  placeholder="请输入开场白问题"
                >
                  <div slot="suffix" class="del-text">
                    <span class="num-color">{{ form.prologue_list[index].length }}/50</span>
                    <i class="el-icon-delete" @click="delprologue(index)"></i>
                  </div>
                </el-input>
              </div>
              <div @click="addprologue" class="parameter1"><span class="el-icon-circle-plus-outline" />新增问题</div>
            </div>
          </el-collapse-transition>
        </el-form-item>
      </div>
      <el-divider style="width: 100%"></el-divider>
      <div class="p20">
        <div class="title">插件</div>

        <el-form-item>
          <div slot="label" style="width: 100%" class="label flex-between" @click.prevent>
            <div class="flex">
              <span
                class="el-icon-caret-right"
                :class="show5 ? 'rotating' : 'norotating'"
                @click.prevent="show5 = !show5"
              ></span>
              数据库
              <popover
                :tips="`扩展智能体的数据库知识储备，为用户提供更针对性的答案，智能体公开发布后可生成优质问答，用于调优`"
              ></popover>
            </div>
            <div class="flex-center">
              <div class="json-text">
                <span @click.prevent="addJson">数据库设置</span>
              </div>

              <el-switch
                v-model="form.is_table"
                active-text="开启"
                inactive-text="关闭"
                :active-value="1"
                :inactive-value="0"
                style="width: 60px"
              >
              </el-switch>
            </div>
          </div>
          <el-collapse-transition>
            <div v-show="show5 && form.is_table == 1">
              <el-input type="textarea" class="textareaBox height90" placeholder="请输入内容" v-model="form.content">
              </el-input>
              <div class="append">
                <span class="iconfont iconzhankai2" @click="addJson"></span>
              </div>
            </div>
          </el-collapse-transition>
        </el-form-item>
        <el-form-item v-if="form.is_table == 1">
          <div slot="label" style="width: 100%" class="label flex-between" @click.prevent>
            <div class="flex">
              <span
                class="el-icon-caret-right"
                :class="show6 ? 'rotating' : 'norotating'"
                @click.prevent="show6 = !show6"
              ></span>
              整理数据规则
              <popover :tips="`您可以对数据库表查询之后，返回内容的整理格式进行描述`"></popover>
            </div>
          </div>
          <el-collapse-transition>
            <div v-show="show6">
              <el-input
                type="textarea"
                class="textareaBox height90"
                placeholder="请输入内容"
                v-model="form.data_arrange_text"
                maxlength="1000"
              >
              </el-input>
              <div class="append">
                <span class="num-color">{{ form.data_arrange_text.length }} / 1000</span>
                <span
                  class="iconfont iconzhankai2"
                  @click="openText(1000, form.data_arrange_text, 'data_arrange_text')"
                ></span>
              </div>
            </div>
          </el-collapse-transition>
        </el-form-item>

        <el-form-item v-if="form.is_table == 1">
          <div slot="label" class="label">
            <span class="el-icon-caret-right" :class="show2 ? 'rotating' : 'norotating'" @click="show2 = !show2"></span>
            关键字
            <popover :tips="`用户提问触发关键字，才会根据数据库进行查询`"></popover>
          </div>
          <el-collapse-transition>
            <div v-show="show2">
              <div v-for="(item, index) in form.keyword" :key="index">
                <el-input
                  v-model="form.keyword[index]"
                  class="parameter-input mb8"
                  size="small"
                  maxlength="20"
                  placeholder="请输入关键词"
                >
                  <div slot="suffix" class="del-text">
                    <span class="num-color">{{ form.keyword[index].length }}/20</span>
                    <i class="el-icon-delete" @click="delkeyWord(index)"></i>
                  </div>
                </el-input>
              </div>
              <div @click="addkeyWord" class="parameter1"><span class="el-icon-circle-plus-outline" />新增关键字</div>
            </div>
          </el-collapse-transition>
        </el-form-item>
      </div>
      <el-divider style="margin: 20px -20px"></el-divider>
      <div class="p20">
        <el-form-item>
          <div slot="label" class="label">
            <span class="el-icon-caret-right" :class="show3 ? 'rotating' : 'norotating'" @click="show3 = !show3"></span>
            参考对话轮数
            <popover :tips="`本次回答内容根据最近轮次的参考对话`" :width="270"></popover>
          </div>
          <el-collapse-transition>
            <div v-show="show3" class="flex">
              <el-slider v-model="form.count_number" :max="10" style="width: 100%"></el-slider>
              <el-input-number
                v-model="form.count_number"
                :controls="false"
                size="small"
                :max="10"
                :min="0"
                style="width: 61px"
                class="ml20"
              ></el-input-number>
            </div>
          </el-collapse-transition>
        </el-form-item>
      </div>
    </el-form>
    <!-- 参数设置 -->
    <oa-dialog ref="oaDialog" :fromData="fromData" @submit="submit" @handleClose="handleClose">
      <el-table :data="tableList" style="width: 100%">
        <el-table-column prop="date" label="参数名称" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" size="small" placeholder="请输入参数名称"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="filed" label="参数标识" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row.filed" size="small" placeholder="请输入参数标识"></el-input> </template
        ></el-table-column>
        <el-table-column prop="value" label="默认值" width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row.value" size="small" placeholder="请输入默认值"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="message" label="提示"
          ><template slot-scope="scope">
            <el-input v-model="scope.row.message" size="small" placeholder="请输入提示"></el-input> </template
        ></el-table-column>
        <el-table-column label="操作" width="90">
          <template slot-scope="scope">
            <el-button type="text" @click="handlerDelet(scope.row, scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="addText" @click="addJsonTable">+ 添加参数</div>
    </oa-dialog>
    <!-- 选择数据库 -->
    <jsonDialog ref="jsonDialog" @submit="getJsonData" :content="form.content" :list="form.tables"></jsonDialog>
    <textDialog ref="textDialog" @submit="getTextData"></textDialog>
    <!-- <databaseTable></databaseTable> -->
  </div>
</template>
<script>
import popover from './popover'
import databaseTable from './databaseTable'
import jsonDialog from './jsonDialog'
import textDialog from './textDialog'
import { getModelsSelectListApi } from '@/api/chatAi'
import oaDialog from '@/components/form-common/dialog-form'

export default {
  name: '',
  components: { popover, oaDialog, databaseTable, jsonDialog, textDialog },
  props: {
    info: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      fromData: {
        title: '参数设置',
        width: '980px',
        type: 'slot',
        btnText: '确定'
      },

      options: [],
      tableList: [],

      form: {
        models_id: '',
        json: [], // 参数设置
        count_number: 0,
        is_table: 1,
        tables: [],
        keyword: [],
        content: '',
        data_arrange_text: '',
        prologue_text: '',
        prologue_list: [''] // 问题
      },
      formRules: {
        models_id: {
          required: true,
          message: '请选择模型',
          trigger: 'change'
        }
      },
      input: 3,
      textarea: '',
      show1: true,
      show2: true,
      show3: true,
      show4: true,
      show5: true,
      show6: true,
      placeholder: '请输入提示词'
    }
  },
  computed: {
    previewState() {
      const { prologue_text, prologue_list } = this.form
      return {
        prologueText: prologue_text,
        prologueList: prologue_list.filter((item) => item !== '')
      }
    }
  },
  watch: {
    info: {
      handler(val) {
        const isUndef = val => val === undefined || val === null;
        for (let key in this.form) {
          if (!isUndef(val[key])) {
            // 直接使用 props 传递来的数组，需要深拷贝
            // 否则 prologue_list 变化时，会引起 info 的变化，导致表单状态丢失
            if (Array.isArray(val[key])) {
              this.form[key] = JSON.parse(JSON.stringify(val[key]))
            } else {
              this.form[key] = val[key]
            }
          }
        }
      },
      immediate: true,
      deep: true
    },
    previewState(state) {
      this.$emit('update-preview', state)
    }
  },
  mounted() {
    this.getOptions()
  },

  methods: {
    addJson(event) {
      event.stopImmediatePropagation()

      this.$refs.jsonDialog.openBox(this.form.content, this.form.tables)
    },

    // 提交数据
    getData(val) {
      if (this.form.prologue_list.length > 0) {
        let newArr = []
        this.form.prologue_list.forEach((item) => {
          if (item) {
            newArr.push(item)
          }
        })
        this.form.prologue_list = newArr
      }
      if (this.form.keyword.length > 0) {
        let newArr = []
        this.form.keyword.forEach((item) => {
          if (item) {
            newArr.push(item)
          }
        })
        this.form.keyword = newArr
      }

      if (val) {
        return new Promise((resolve, reject) => {
          this.$refs['form'].validate((valid) => {
            if (!valid) {
              reject({ target: 'modelForm' })
              return
            }
            resolve({ modelForm: this.form })
          })
        })
      } else {
        return new Promise((resolve, reject) => {
          resolve({ modelForm: this.form })
        })
      }
    },

    getTextData(val, type) {
      this.form[type] = val
    },

    getJsonData(list, data) {
      this.form.tables = list
      this.form.content = data
    },

    handleClose() {
      this.tableList = [{ name: '', filed: '', value: '', message: '' }]
    },

    handlerDelet(row, index) {
      this.$modalSure('确定删除此参数').then(() => {
        this.tableList.splice(index, 1)
      })
    },
    openText(max, text, type) {
      let obj = {
        max,
        text,
        type
      }
      this.$refs.textDialog.openBox(obj)
    },
    addprologue() {
      this.form.prologue_list.push('')
    },
    delprologue(index) {
      this.form.prologue_list.splice(index, 1)
    },
    addkeyWord() {
      this.form.keyword.push('')
    },
    delkeyWord(index) {
      this.form.keyword.splice(index, 1)
    },
    submit() {
      for (const obj of this.tableList) {
        for (const value of Object.values(obj)) {
          let values = Object.values(obj)
          if (values.every((value) => value === '')) {
          } else if (obj.value === '' || obj.name === '' || obj.filed === '') {
            this.$message({
              type: 'error',
              message: '请填写完整,参数设置不能为空'
            })
            return false
          }
        }
      }
      this.tableList.forEach((item, index) => {
        let values = Object.values(item)
        if (values.every((value) => value === '')) {
          this.tableList.splice(index, 1)
        }
      })

      this.form.json = JSON.parse(JSON.stringify(this.tableList))
      this.$refs.oaDialog.handleClose()
    },
    addJsonTable() {
      this.tableList.push({
        name: '',
        filed: '',
        value: '',
        message: ''
      })
    },
    async getOptions() {
      const data = await getModelsSelectListApi()
      this.options = data.data
    },
    addItem() {
      this.form.list.push({
        value: ''
      })
    },
    openFn() {
      this.$refs.oaDialog.openBox()
      this.tableList = JSON.parse(JSON.stringify(this.form.json))
    }
  }
}
</script>
<style scoped lang="scss">
::v-deep .el-form-item__label {
  display: flex;
  align-items: center;
  height: 18px;
  margin-bottom: 10px;
  padding: 0;
  position: relative;
}
.num-color {
  color: #909399;
}
.parameter-input {
  ::v-deep.el-input__inner {
    padding-right: 70px;
  }
}
.p20 {
  padding: 0 20px;
}
.mb8 {
  margin-bottom: 8px;
}
.required-icon {
  color: #ed4014;
  margin-right: 4px;
}
.json-text {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #1890ff;
  margin-right: 12px;
}
.del-text {
  display: flex;
  align-items: center;
  // width: 70px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #606266;

  .el-icon-delete {
    font-size: 14px;
    margin-left: 8px;
    cursor: pointer;
  }
}
.label {
  display: flex;
  font-weight: 500;
  font-size: 13px;
  color: #303133;
  line-height: 13px;
}
.iconshuoming {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
}
.parameter {
  position: absolute;
  right: 0;
  cursor: pointer;
  font-weight: 400;
  font-size: 13px;
  color: #1890ff;
}
.parameter1 {
  cursor: pointer;
  font-weight: 400;
  font-size: 12px;
  color: #1890ff;
}
.el-icon-circle-plus-outline {
  font-size: 13px;
  margin-right: 4px;
}
.el-icon-plus {
  position: absolute;
  right: 0;
  cursor: pointer;
  color: #606266;
  font-size: 13px;
}
.el-icon-caret-right {
  font-size: 14px;
  cursor: pointer;
  margin-right: 4px;
}
.rotating {
  transform: rotate(90deg);
}

.title {
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 12px;
  color: #606266;
  margin-bottom: 10px;
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
.height90 {
  ::v-deep .el-textarea__inner {
    height: 90px;
  }
}
::v-deep .el-slider__button-wrapper {
  z-index: 50;
}
.append {
  // background: #fff;
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
.addText {
  cursor: pointer;
  margin-top: 20px;
  font-weight: 400;
  font-size: 13px;
  color: #1890ff;
  .el-icon-plus {
    display: inline-block;

    color: #1890ff;
  }
}
</style>
