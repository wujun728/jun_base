<!-- 低代码-触发器设计页面 -->
<template>
  <div class="bag">
    <!-- 头部 -->
    <div class="header">
      <div class="title">
        当前触发器：{{ form.name }}
        <el-popover placement="bottom" ref="popoverRef" width="400" trigger="click">
          <div>
            <el-input v-model="value" placeholder="请输入触发器名称" style="width: 250px" size="small"></el-input>
            <el-button size="small" class="ml14" type="text" @click="close">取消</el-button>
            <el-button type="primary" size="small" @click="nameSubmit">确定</el-button>
          </div>
          <span class="el-icon-edit-outline" slot="reference"></span>
        </el-popover>
      </div>
      <div class="mr24 flex">
        <div class="btn btn-left" @click="save">仅保存</div>
        <div class="btn btn-right" @click="save(1)">保存并关闭</div>
      </div>
    </div>
    <!-- 头部 -->

    <!-- 内容 -->
    <div class="content">
      <el-timeline>
        <el-timeline-item v-for="(item, index) in activities" :key="index" :icon="item.icon" :color="item.color">
          <div class="tips">{{ item.title }}</div>
          <div v-if="item.id === 1" class="phase_1">
            <el-form ref="form" :model="form" label-width="100px">
              <el-form-item label="应用实体" class="mb14">
                <el-cascader
                  v-model="crud_id"
                  :options="crudOptions"
                  :show-all-levels="false"
                  filterable
                  size="small"
                  :disabled="id == 0 ? false : true"
                  :props="props"
                  style="width: 300px"
                  clearable
                  @change="crudChange"
                  placeholder="请选择实体"
                >
                </el-cascader>
              </el-form-item>
              <el-form-item label="触发动作">
                <el-checkbox-group v-model="action" @change="actionChange()">
                  <div style="width: 430px">
                    <el-checkbox
                      v-for="(item, index) in actionList"
                      :key="index"
                      :label="item.value"
                      :disabled="info.event === 'data_check' && !['create', 'update'].includes(item.value)"
                      >{{ item.label }}
                      <span
                        v-if="item.value === 'update'"
                        class="el-icon-setting"
                        @click.prevent="editUpdate(item.value)"
                      ></span>
                    </el-checkbox>
                  </div>
                </el-checkbox-group>
                <template v-if="action && action.length > 0 && action.includes('timer')">
                  执行周期：
                  <el-select v-model="form.timer_type" size="small" placeholder="请选择">
                    <el-option v-for="item in timeOptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                  <el-select
                    v-if="form.timer_type == 5"
                    v-model="form.timer_options.weekday"
                    size="small"
                    placeholder="请选择"
                  >
                    <el-option v-for="item in weekOptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                  <el-input
                    v-if="form.timer_type == 7"
                    placeholder="请输入"
                    type="number"
                    @input="numberChange('month', 12)"
                    @change="numberChange('month', 12)"
                    style="width: 160px"
                    v-model="form.timer_options.month"
                    size="small"
                  >
                    <span slot="suffix" class="text-16">月</span>
                  </el-input>
                  <el-input
                    v-if="['6', '7', '3'].includes(form.timer_type)"
                    placeholder="请输入"
                    type="number"
                    @input="numberChange('day', 31)"
                    @change="numberChange('day', 31)"
                    style="width: 160px"
                    v-model="form.timer_options.day"
                    size="small"
                  >
                    <span slot="suffix" class="text-16">日</span>
                  </el-input>
                  <el-input
                    v-if="!['0', '1'].includes(form.timer_type)"
                    placeholder="请输入"
                    type="number"
                    @input="numberChange('hour', 59)"
                    @change="numberChange('hour', 59)"
                    style="width: 160px"
                    v-model="form.timer_options.hour"
                    size="small"
                  >
                    <span slot="suffix" class="text-16">时</span>
                  </el-input>
                  <el-input
                    v-if="form.timer_type != '0'"
                    placeholder="请输入"
                    type="number"
                    @input="numberChange('minute', 59)"
                    @change="numberChange('minute', 59)"
                    style="width: 160px"
                    v-model="form.timer_options.minute"
                    size="small"
                  >
                    <span slot="suffix" class="text-16">分</span>
                  </el-input>
                  <el-input
                    v-if="['4', '5', '6', '0'].includes(form.timer_type)"
                    placeholder="请输入"
                    type="number"
                    @input="numberChange('second', 59)"
                    @change="numberChange('second', 59)"
                    style="width: 160px"
                    v-model="form.timer_options.second"
                    size="small"
                  >
                    <span slot="suffix" class="text-16">秒</span>
                  </el-input>
                  <div class="prompt mt10 ml50">
                    {{ getStr(form.timer_type) }}
                  </div>
                </template>
              </el-form-item>
              <el-form-item label="附加过滤条件" v-if="info.event !== 'get_data'">
                <div class="default-color pointer fz-12">
                  <span @click="OpenSettings('additional_search')">
                    {{
                      form.additional_search && form.additional_search.length > 0
                        ? '已设置条件（' + form.additional_search.length + '）'
                        : '点击设置'
                    }}</span
                  >
                </div>
                <div class="prompt">只有满足指定条件的实体操作才会触发该触发器</div>
              </el-form-item>
            </el-form>
          </div>

          <div v-if="item.id === 2">
            <el-form ref="form" :model="form" label-width="100px">
              <el-form-item label="执行操作">
                <div>{{ event_name }}</div>
              </el-form-item>
              <!-- 操作内容-动态添加 -->
              <el-form-item label="操作内容">
                <div class="box">
                  <el-form ref="form" :model="form" label-width="90px" v-if="id != 0">
                    <!-- 数据校验 -->
                    <template v-if="info.event === 'data_check'">
                      <el-form-item label="校验条件">
                        <div class="default-color pointer fz-12">
                          <span @click="OpenSettings('testOptions')">{{
                            form.testOptions && form.testOptions.length > 0
                              ? '已设置条件（' + form.testOptions.length + '）'
                              : '点击设置'
                          }}</span>
                        </div>
                        <div class="prompt">符合校验条件的数据/记录在操作时会提示失败</div>
                      </el-form-item>
                    </template>
                    <!-- 数据校验 -->

                    <!-- 发送通知 -->
                    <template v-if="info.event === 'send_notice'">
                      <el-form-item label="接受人员">
                        <select-member
                          :value="userList || []"
                          :placeholder="`选择成员`"
                          @getSelectList="getSelectList"
                          style="width: 100%"
                        ></select-member>
                      </el-form-item>
                      <!-- <el-form-item label="通知类型" class="mt14">
                        <el-checkbox-group v-model="notify_type">
                          <el-checkbox :label="0">通知</el-checkbox>
                        </el-checkbox-group>
                      </el-form-item> -->
                      <el-form-item label="推送标题" class="mt14">
                        <el-input
                          class="textPosition"
                          placeholder="请输入推送标题"
                          @input="onInput"
                          v-model="form.title"
                        >
                        </el-input>
                        <el-popover placement="left" trigger="hover">
                          <div class="field-box">
                            <div
                              class="field-text over-text"
                              v-for="(item, index) in field"
                              :key="index"
                              @click="handleClick(item, 1)"
                            >
                              {{ item.label }}
                            </div>
                          </div>
                          <span class="el-icon-chat-dot-square icon" slot="reference"></span>
                        </el-popover>
                        <div class="prompt mt14">
                          标题支持字段变量，如 <span class="color-file">{createdOn}</span> (其中 createdOn
                          为源实体的字段内部标识)
                        </div>
                      </el-form-item>
                    </template>
                    <el-form-item
                      v-if="['send_notice', 'data_check'].includes(info.event)"
                      :label="info.event == 'data_check' ? '提示内容' : '推送内容'"
                      class="mt14"
                    >
                      <el-input
                        class="textPosition"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入推送内容"
                        v-model="form.template"
                      >
                      </el-input>
                      <span class="prompt"
                        >校验未通过时的提示内容。内容支持字段变量，如 <span class="color-file">{createdOn} </span>(其中
                        createdOn 为源实体的字段内部标识)</span
                      >

                      <el-popover placement="left" trigger="hover">
                        <div class="field-box">
                          <div
                            class="field-text over-text"
                            v-for="(item, index) in field"
                            :key="index"
                            @click="handleClick(item)"
                          >
                            {{ item.label }}
                          </div>
                        </div>
                        <span class="el-icon-chat-dot-square icon" slot="reference"></span>
                      </el-popover>
                    </el-form-item>
                    <el-form-item>
                      <el-table
                        v-if="info.event == 'send_notice'"
                        ref="multipleTable"
                        :data="messagePushData"
                        @selection-change="handleSelectionChange"
                      >
                        <el-table-column type="selection"></el-table-column>
                        <el-table-column label="推送渠道 " width="150">
                          <template slot-scope="scope">
                            <img :src="scope.row.icon" alt="" class="img" />
                          </template>
                        </el-table-column>
                        <el-table-column label="显示名称" prop="name" width="150"> </el-table-column>
                        <el-table-column label="操作">
                          <template slot-scope="scope" v-if="scope.row.status !== 'system_status'">
                            <el-input
                              v-model="form[scope.row.key]"
                              :placeholder="scope.row.placeholder"
                              size="small"
                              style="width: 350px"
                            ></el-input>
                          </template>
                        </el-table-column>
                      </el-table>
                    </el-form-item>
                    <!-- 发送通知 -->

                    <!-- 自动审核 -->
                    <el-form-item
                      label="选择审批流程"
                      v-if="['auto_revoke_approve', 'auto_approve'].includes(info.event)"
                    >
                      <el-select
                        size="small"
                        v-model="form.crud_approve_id"
                        :placeholder="info.event === 'auto_approve' ? '请选择审批流程' : '请搜索选择关联撤销记录'"
                        style="width: 300px"
                      >
                        <el-option v-for="(item, index) in approval" :key="index" :label="item.name" :value="item.id">
                        </el-option>
                      </el-select>
                      <div class="prompt mt14">
                        {{
                          info.event === 'auto_approve'
                            ? '需要先添加审批流程才能在此处选择'
                            : '可撤销源实体记录或其他关联记录'
                        }}
                      </div>
                    </el-form-item>
                    <!-- 自动审核 -->
                    <!-- 日程待办 -->
                    <template v-if="info.event === 'to_do_schedule'">
                      <to-do-schedule ref="toDoSchedule" :data="form.options" :options="relatedData"></to-do-schedule>
                    </template>
                    <!-- 日程待办 -->

                    <!-- 获取数据 -->
                    <template v-if="info.event === 'get_data' || info.event === 'push_data'">
                      <el-form-item label="链接地址">
                        <el-select
                          size="small"
                          v-model="form.curl_id"
                          filterable
                          placeholder="链接地址"
                          style="width: 300px"
                        >
                          <el-option v-for="item in dataList" :key="item.id" :label="item.title" :value="item.id">
                          </el-option>
                        </el-select>
                        <div class="mb14">
                          <el-button
                            v-if="info.event == 'get_data'"
                            type="primary"
                            :loading="curlLoading"
                            size="small"
                            class="mt10"
                            @click="crudSendFn(1)"
                            >测试请求</el-button
                          >
                        </div>
                        <div v-if="info.event == 'get_data'" class="prompt mb20">
                          注意：必须执行完测试请求之后，源字段中才会有对应的数据
                        </div>
                      </el-form-item>
                    </template>
                    <!-- 获取数据 -->

                    <!-- 字段更新/聚合 -->
                    <el-form-item
                      label="目标实体"
                      v-if="
                        [
                          'field_update',
                          'field_aggregate',
                          'group_aggregate',
                          'auto_create',
                          'get_data',
                          'push_data'
                        ].includes(info.event)
                      "
                    >
                      <el-select
                        size="small"
                        v-model="form.target_crud_id"
                        filterable
                        placeholder="应用实体名称"
                        style="width: 300px"
                        @change="changeTargetCrudId"
                      >
                        <el-option
                          v-for="(item, index) in targetEntity"
                          :key="index"
                          :label="item.table_name"
                          :value="item.id"
                        >
                        </el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="聚合目标条件" class="mt14" v-if="info.event === 'group_aggregate'">
                      <div class="default-color pointer fz-12">
                        <span @click="OpenSettings('aggregate_target_search')">
                          {{
                            form.aggregate_target_search && form.aggregate_target_search.length > 0
                              ? '已设置条件（' + form.aggregate_target_search.length + '）'
                              : '点击设置'
                          }}</span
                        >
                      </div>
                      <div class="prompt">仅会聚合到符合条件的数据上</div>
                    </el-form-item>

                    <el-form-item label="重复数据处理" class="mt14" v-if="info.event === 'get_data'">
                      <el-radio-group v-model="form.options.is_skip_value">
                        <el-radio :label="`0`">跳过</el-radio>
                        <el-radio :label="`1`">更新</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <!-- 动态更新渲染组件 -->
                    <template
                      v-if="
                        [
                          'field_update',
                          'field_aggregate',
                          'group_aggregate',
                          'auto_create',
                          'get_data',
                          'push_data'
                        ].includes(info.event)
                      "
                    >
                      <updateContent
                        :type="info.event"
                        :list="getOptions(info.event)"
                        :groupList="groupList"
                        :options="options"
                        :uniqidOptions="uniqidOptions"
                        :groupField="groupField"
                        :targetField="targetField"
                        :action="action"
                        :field="field"
                        ref="updateContent"
                    /></template>

                    <el-form-item
                      label="聚合数据条件"
                      class="mt14"
                      v-if="info.event === 'field_aggregate' || info.event === 'group_aggregate'"
                    >
                      <div class="default-color pointer fz-12">
                        <span @click="OpenSettings('aggregate_data_search')">
                          {{
                            form.aggregate_data_search && form.aggregate_data_search.length > 0
                              ? '已设置条件（' + form.aggregate_data_search.length + '）'
                              : '点击设置'
                          }}</span
                        >
                      </div>
                      <div class="prompt">仅会聚合到符合过滤条件的数据上</div>
                    </el-form-item>
                  </el-form>
                </div>
              </el-form-item>
              <!-- 字段更新/聚合 -->
              <el-form-item label="执行优先级">
                <el-input-number v-model="form.sort" :min="0" size="small" style="width: 220px"></el-input-number>
                <div class="prompt mt14">优先级高（数字大）的会被先执行，优先级相等的先执行创建早的触发器</div>
              </el-form-item>
            </el-form>
          </div>
        </el-timeline-item>
      </el-timeline>
    </div>
    <!-- 内容 -->

    <!-- 测试请求数据回显 -->
    <el-dialog title="请求数据" :visible.sync="isRequest" width="50%">
      <json-viewer style="height: 600px; width: 100%" :value="jsonData" :expand-depth="8" copyable></json-viewer>
    </el-dialog>

    <!-- 指定字段更新 -->
    <checkbox-dialog
      ref="checkboxDialog"
      :name="name"
      :showName="showName"
      :title="`指定字段`"
      :type="`view`"
      @getData="getData"
    ></checkbox-dialog>
    <!-- 条件设置弹窗 -->
    <el-drawer
      size="650px"
      direction="rtl"
      title="条件设置"
      :append-to-body="true"
      :wrapperClosable="false"
      class="condition_copyer"
      :visible.sync="$store.state.business.conditionDialog"
    >
      <condition-dialog
        :id="crud_id"
        :eventStr="`event`"
        :additionalBoolean="additional_boolean"
        v-if="$store.state.business.conditionDialog"
      />
    </el-drawer>
  </div>
</template>
<script>
import JsonViewer from 'vue-json-viewer'
import Commnt from './components/commonData'

import {
  dataEventActionApi,
  dataEventInfoApi,
  dataEventCrudApi,
  databaseListApi,
  dataEventTypeApi,
  dataEventUpdateApi,
  crudGetCurlListApi,
  crudSendApi,
  crudModuleInfoApi,
  dataEventSaveApi,
  getDatabaseApi
} from '@/api/develop'
import debounce from '@form-create/utils/lib/debounce'
export default {
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    conditionDialog: () => import('@/components/develop/conditionDialog'),
    checkboxDialog: () => import('@/components/develop/checkboxDialog'),
    updateContent: () => import('@/components/develop/updateContent'),
    toDoSchedule: () => import('@/components/develop/toDoSchedule'),
    JsonViewer
  },
  data() {
    return {
      id: 0, // 触发器id
      crud_id: 0, // 实体id
      crud_name: '--', // 实体名称
      event_name: '--', // 触发动作名称
      info: {}, // 触发器详情
      isRequest: false,
      jsonData: {},
      props: {
        multiple: false,
        label: 'label',
        value: 'value',
        children: 'children',
        emitPath: false //绑定的内容只获取最后一级的value值。
      },
      form: {
        name: '',
        title: '',
        action: [],
        sms_template_id: '', // 短信模板id
        timer: '',
        additional_search: [],
        field_options: [],
        template: '',
        timer_type: '',
        timer_options: {},
        update_field_options: [],
        options: {}
      },

      crudInfo: {},
      keyName: '',
      relatedData: {},
      groupList: [{ form_field_uniqid: '', to_form_field_uniqid: '' }], // 分组字段
      action: [], // 选中的触发动作
      notify_type: [], // 选择通知类型
      actionList: [], // 获取执行动作类型
      targetEntity: [], // 目标实体
      approval: [], // 审批流程数据
      options: [], // 条件
      uniqidOptions: [],
      field: [], // 源字段
      dataList: [], // 接口列表
      curlLoading: false,
      value: '',
      name: '所有字段',
      showName: '已展示字段',
      additional_boolean: '1',
      userList: [],
      crudOptions: [],
      weekOptions: Commnt.weekOptions,
      messagePushData: Commnt.messagePushData,
      timeOptions: Commnt.timeOptions,
      activities: Commnt.activities
    }
  },
  created() {
    this.getDatabase()
    this.getAction()
    if (this.$route.query.id) {
      this.crud_id = Number(this.$route.query.crud_id)
      this.type = 'edit'
      this.id = this.$route.query.id
      this.getAssociation()
    } else {
      this.info.event = this.$route.query.event
      this.form.event = this.$route.query.event
      this.form.name = this.$route.query.event_name
      this.event_name = this.$route.query.event_name
    }
  },

  computed: {
    // 目标字段
    targetField() {
      if (this.form.target_crud_id && this.targetEntity.length > 0) {
        let index = this.targetEntity.findIndex((item) => item.id === this.form.target_crud_id)
        if (this.info.event === 'group_aggregate') {
          let formData = this.targetEntity[index].rule_field.filter((item) => {
            return !['file', 'image'].includes(item.form_value)
          })
          return formData
        } else {
          let formData = []
          if (this.targetEntity[index] && this.targetEntity[index].field.length > 0) {
            formData = this.targetEntity[index].field.filter((item) => {
              return !['file', 'image'].includes(item.form_value)
            })
          } else {
            formData = []
          }

          return formData
        }
      }
    },

    // 分组聚合字段
    groupField() {
      if (this.form.target_crud_id && this.targetEntity.length > 0) {
        let index = this.targetEntity.findIndex((item) => item.id === this.form.target_crud_id)
        if (this.info.event === 'group_aggregate') {
          return this.targetEntity[index].field
        } else {
          return []
        }
      }
    },

    actionChange() {
      if (!this.action.includes('timer')) {
        this.form.timer = ''
      }
    },

    // 设置条件内容
    additional_search: {
      get() {
        return this.$store.state.business.fieldOptions.list
      },
      set(val) {}
    },

    // 设置条件内容
    additional_Type: {
      get() {
        return this.$store.state.business.fieldOptions.type
      },
      set(val) {}
    },

    additional_search_boolean: {
      get() {
        return this.$store.state.business.fieldOptions.additional_search_boolean
      },
      set(val) {}
    }
  },

  watch: {
    // 给条件设置组装数据
    additional_search(val) {
      this.form[this.additional_Type] = []
      val.map((item) => {
        let obj = {
          operator: item.value,
          form_field_uniqid: item.field,
          value: item.option,
          obj: item
        }
        if (!item.option && item.category === 2) {
          obj.value = []
          item.options.userList.map((i) => {
            obj.value.push(i.value)
          })
        } else if (!item.option && item.category === 1) {
          obj.value = []
          item.options.depList.map((i) => {
            obj.value.push(i.id)
          })
        }
        if (obj.operator === 'between') {
          let data = {
            min: item.min,
            max: item.max
          }
          obj.value = data
        }

        this.$nextTick(() => {
          this.form[this.additional_Type].push(obj)
        })
      })
    },
    additional_search_boolean(val) {
      // 条件设置-条件规则
      if (this.additional_Type === 'additional_search') {
        this.form.additional_search_boolean = this.additional_search_boolean
      } else if (this.additional_Type === 'aggregate_data_search') {
        this.form.aggregate_data_search_boolean = this.additional_search_boolean
      } else if (this.additional_Type === 'aggregate_target_search') {
        this.form.aggregate_target_search_boolean = this.additional_search_boolean
      } else if (this.additional_Type === 'testOptions') {
        this.form.options = {
          search: this.form.testOptions,
          search_boolean: this.additional_search_boolean
        }
      }
    }
  },

  methods: {
    getcrudInfo() {
      crudModuleInfoApi(this.keyName, this.crud_id).then((res) => {
        this.crudInfo = res.data
      })
    },

    // 消息推送渠道选择
    handleSelectionChange(e) {
      e.map((item) => {
        this.form[item.status] = 1
      })
    },
    // 打开指定更新字段
    editUpdate() {
      let list = [...this.crudInfo.crudInfo.customField, ...this.crudInfo.crudInfo.systemField]
      let fieldList = []
      if (this.form.update_field_options.length > 0) {
        list.map((item) => {
          if (this.form.update_field_options.includes(item.field_name_en)) {
            fieldList.push(item)
          }
        })
      }
      this.crudInfo.crudInfo.systemField = this.crudInfo.crudInfo.systemListField.filter((item) => {
        return !['id', 'frame_id', 'created_at'].includes(item.field_name_en)
      })
      this.crudInfo.crudInfo.customField = this.crudInfo.crudInfo.customListField
      this.$refs.checkboxDialog.openBox(this.crudInfo.crudInfo, fieldList)
    },
    // 获取应用数据
    getDatabase() {
      getDatabaseApi().then((res) => {
        this.crudOptions = res.data
      })
    },

    // 切换实体
    crudChange(e) {
      let obj = {
        crud_id: this.crud_id,
        event: this.form.event,
        name: this.form.name
      }
      dataEventSaveApi(obj).then((res) => {
        this.id = res.data.id
        this.getAssociation()
      })
    },

    // 更新指定字段
    getData(data) {
      this.form.update_field_options = data.ids
    },
    // 数据提交
    save: debounce(function (val) {
      if (['field_aggregate', 'group_aggregate'].includes(this.info.event)) {
        this.form.aggregate_field_rule = this.$refs.updateContent[0].updateList
      } else if (['field_update', 'auto_create', 'get_data', 'push_data'].includes(this.info.event)) {
        this.form.field_options = this.$refs.updateContent[0].updateList
      }
      // 分组聚合的数据
      if (this.info.event === 'group_aggregate') {
        this.form.aggregate_data_field = this.$refs.updateContent[0].groupList
      }

      if (this.info.event === 'send_notice') {
        this.form.send_user = []
        if (this.userList.length == 0) {
          return this.$message.error('请先添加成员')
        }
        this.userList.map((item) => {
          this.form.send_user.push(item.id)
        })
        this.form.options = {
          title: this.form.title
        }
        this.form.notify_type = this.notify_type
      }

      if (this.info.event === 'to_do_schedule') {
        let formObj = this.$refs.toDoSchedule[0].form
        for (let key in formObj) {
          this.form.options[key] = formObj[key]
        }
      }

      this.form.action = this.action
      if (!this.form.name) {
        return this.$message.error('触发器名称不能为空')
      }
      if (!this.form.action) {
        return this.$message.error('触发动作不能为空')
      }
      if (this.additional_Type === 'testOptions') {
        this.form.options = {
          search: this.form.testOptions,
          search_boolean: this.additional_search_boolean
        }
      }
      dataEventUpdateApi(this.id, this.form).then((res) => {
        if (val == 1 && res.status == 200) {
          this.handleClose()
        }
      })
    }, 1000),

    // 打开条件设置弹窗
    OpenSettings(type) {
      let list = []
      if (this.form[type] && this.form[type].length > 0) {
        this.form[type].map((item) => {
          list.push(item.obj)
        })
      } else {
        list = []
      }

      let boolean = '1'
      if (type === 'additional_search') {
        boolean = this.form.additional_search_boolean + '' || '1'
      } else if (type === 'aggregate_data_search') {
        boolean = this.form.aggregate_data_search_boolean + '' || '1'
      } else if (type === 'aggregate_target_search') {
        boolean = this.form.aggregate_target_search_boolean + '' || '1'
      } else if (type === 'testOptions') {
        boolean = this.form.options ? this.form.options.search_boolean + '' : '1'
      }
      this.additional_boolean = boolean
      let data = {
        list,
        type,
        additional_search_boolean: boolean
      }

      this.$store.commit('uadatefieldOptions', data)
      this.$store.commit('updateConditionDialog', true)
    },

    changeTargetCrudId() {
      this.$refs.updateContent[0].updateList = []
    },

    // 获取数据-接口列表
    async getDataList() {
      let objData = {
        page: 1,
        limit: 0
      }
      if (this.info.event == 'push_data') {
        objData['method'] = 'post'
      }

      const data = await crudGetCurlListApi(objData)
      this.dataList = data.data.list
    },

    // 发送请求
    crudSendFn(val) {
      this.field = []
      if (!this.form.curl_id) return this.$message.error('链接地址不能为空')
      this.curlLoading = true
      crudSendApi(this.form.curl_id).then((res) => {
        if (res.status == 200) {
          this.jsonData = res.data
          if (val) {
            this.isRequest = true
          }

          let key = res.data.key
          key.map((item) => {
            let obj = {
              label: item,
              value: item
            }
            this.field.push(obj)
          })
        }
        this.curlLoading = false
      })
    },

    handleClick(val, type) {
      if (!this.form.title) {
        this.form.title = ''
      }
      if (type === 1) {
        this.$set(this.form, 'title', this.form.title + '{' + val.value + '}')
        this.onInput()
      } else {
        this.form.template = this.form.template + '{' + val.value + '}'
      }
    },
    getStr(type) {
      switch (type) {
        case '0':
          return `每隔${this.form.timer_options.second || 'N'}秒执行一次`
        case '1':
          return `每隔${this.form.timer_options.minute || 'N'}分钟执行一次`
        case '2':
          return `每隔${this.form.timer_options.hour || 'N'}小时${this.form.timer_options.minute || 'N'}分钟执行一次`
        case '3':
          return `每隔${this.form.timer_options.day || 'N'}天${this.form.timer_options.hour || 'N'}小时${
            this.form.timer_options.minute || 'N'
          }分钟执行一次`
        case '4':
          return `每天${this.form.timer_options.hour || 'N'}小时${this.form.timer_options.minute || 'N'}分钟${
            this.form.timer_options.second || 'N'
          }秒执行一次`
        case '5':
          return `每个星期${this.form.timer_options.weekday || 'N'}的${this.form.timer_options.hour || 'N'}时${
            this.form.timer_options.minute || 'N'
          }分${this.form.timer_options.second || 'N'}秒执行一次`
        case '6':
          return `每月${this.form.timer_options.day || 'N'}日${this.form.timer_options.hour || 'N'}时${
            this.form.timer_options.minute || 'N'
          }分${this.form.timer_options.second || 'N'}秒执行一次`
        case '7':
          return `每年${this.form.timer_options.month || 'N'}月${this.form.timer_options.day || 'N'}日的${
            this.form.timer_options.hour || 'N'
          }时${this.form.timer_options.minute || 'N'}分${this.form.timer_options.second || 'N'}秒执行一次`
      }
    },

    numberChange(val, maxNum) {
      //重新渲染
      this.$nextTick(() => {
        //比较输入的值和最大值，返回小的
        let num = Math.min(Number(this.form.timer_options[val]), maxNum)
        //输入负值的情况下， = 0（可根据实际需求更该）
        if (num < 0) {
          this.form.timer_options[val] = 0
        } else {
          //反之
          this.form.timer_options[val] = num
        }
      })
    },

    onInput() {
      this.$forceUpdate()
    },

    // 动态获取子组件数据来源
    getOptions(event) {
      let eventList = []
      if (['get_data', 'push_data', 'field_update', 'auto_create'].includes(event)) {
        eventList = this.form.field_options
      } else if (event === 'field_aggregate') {
        eventList = this.form.aggregate_field_rule
      } else if (event === 'group_aggregate') {
        eventList = this.form.aggregate_field_rule
      }
      if ((!eventList || eventList.length == 0) && event !== '') {
        eventList = [{ form_field_uniqid: '', operator: this.options[0].value, to_form_field_uniqid: '' }]
      }
      return eventList
    },

    // 获取触发器关联数据
    async getAssociation() {
      dataEventCrudApi(this.crud_id, this.id).then((res) => {
        this.relatedData = res.data
        this.targetEntity = res.data.list
        this.options = res.data.update_type
        this.uniqidOptions = [{ label: '字段值', value: 'field_value' }]
        this.field = res.data.field
        this.approval = res.data.approve
        this.getInfo()
      })
    },

    // 获取实体数据-渲染实体名称
    async getEntity(id) {
      const where = {
        cate_id: ''
      }
      const data = await databaseListApi(where)
      let list = data.data.list
      let index = list.findIndex((item) => item.id === id)
      this.crud_name = list[index].table_name
      this.keyName = list[index].table_name_en
      this.getcrudInfo()
    },

    // 获取触发动作
    async getEvent(event) {
      const data = await dataEventTypeApi(this.crud_id)
      let index = data.data.findIndex((item) => item.value === event)
      this.event_name = data.data[index].label
    },

    // 获取执行动作类型
    getAction() {
      dataEventActionApi().then((res) => {
        this.actionList = res.data
      })
    },

    // 获取触发器详情
    async getInfo() {
      dataEventInfoApi(this.id).then((res) => {
        this.info = res.data
        this.form = res.data
        this.form.timer_type = this.form.timer_type + ''
        if (this.info.event == 'auto_approve') {
          this.actionList = this.actionList.filter((item) => {
            return !['approve_success', 'approve_create', 'timer'].includes(item.value)
          })
        } else if (['push_data', 'to_do_schedule'].includes(this.info.event)) {
          if (this.info.event === 'push_data') {
            this.form.curl_id = this.form.curl_id == 0 ? '' : this.form.curl_id
            this.getDataList()
          }
          this.actionList = this.actionList.filter((item) => {
            return !['timer'].includes(item.value)
          })
        } else if (this.info.event == 'auto_revoke_approve') {
          this.actionList = this.actionList.filter((item) => {
            return !['approve_create', 'timer'].includes(item.value)
          })
        } else if (this.info.event == 'get_data') {
          this.actionList = [{ label: '定期执行', value: 'timer' }]

          this.form.curl_id = this.form.curl_id == 0 ? '' : this.form.curl_id
          this.getDataList()
          this.field = []
          if (this.form.curl_id) {
            this.crudSendFn()
          }
        }
        if (this.form.timer == 0) {
          this.form.timer = ''
        }
        this.value = this.form.name
        this.action = this.info.action || []
        if (this.id > 0 && this.info.event === 'group_aggregate') {
        } else {
          this.form.target_crud_id = this.info.target_crud_id
        }

        this.form.field_options = this.info.field_options
        let list = []
        if (this.info.event === 'group_aggregate') {
          this.groupList = this.form.aggregate_data_field || []
        }
        if (this.info.event === 'data_check') {
          if (this.form.options && this.form.options.search && this.form.options.search.length > 0) {
            this.$set(this.form, 'testOptions', this.form.options.search)
          } else {
            this.$set(this.form, 'testOptions', [])
          }
        }

        this.getOptions(this.info.event)
        if (this.info.additional_search && this.info.additional_search.length > 0) {
          this.info.additional_search.map((item) => {
            list.push(item.obj)
          })
          this.additional_search = list
        }

        if (this.form.target_crud_id !== 0) {
        } else if (this.form.target_crud_id == 0 && ['auto_create', 'field_aggregate'].includes(this.info.event)) {
          this.form.target_crud_id = this.targetEntity[0] ? this.targetEntity[0].id : ''
        } else {
          this.form.target_crud_id = this.info.crud_id
        }

        if (res.data.send_user && res.data.send_user.length > 0) {
          this.userList = res.data.send_user
          this.userList.forEach((item) => {
            item.value = item.id
          })
        }

        this.list = this.getEntity(this.info.crud_id)
        this.getEvent(this.info.event)
        if (this.info.event === 'send_notice') {
          this.form.title = this.form.options.title
          this.notify_type = this.form.notify_type.map(Number) || []
          this.$nextTick(() => {
            this.messagePushData.forEach((item) => {
              if (this.form[item.status] == 1) {
                item.is_edit = true
                this.$refs.multipleTable[0].toggleRowSelection(item, true)
              }
            })
          })
        }
      })
    },

    // 选择成员回调
    getSelectList(data) {
      if (data.length > 0) {
        data.forEach((item) => {
          item.id = item.value
        })
      }
      this.userList = data
    },

    cardTag(index) {
      this.userList.splice(index, 1)
    },

    handleClose() {
      window.opener = null
      window.open('about:blank', '_top').close()
    },

    nameSubmit() {
      this.form.name = this.value
      this.close()
    },

    close() {
      this.$refs.popoverRef.doClose()
    },

    getLabel(val) {
      let obj = {
        field_update: '更新规则',
        field_aggregate: '聚合规则',
        group_aggregate: '聚合规则',
        auto_create: '创建规则'
      }
      return obj[val]
    }
  }
}
</script>
<style scoped lang="scss">
.bag {
  height: 100vh;
  background: #fff;
}
.ml50 {
  margin-left: 70px;
}
.img {
  display: block;
  width: 32px;
  height: 32px;
}
.iconbianji {
  color: #1890ff;
  cursor: pointer;
}
.header {
  width: 100%;
  background: #1890ff;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .title {
    margin-left: 20px;
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #fff;
  }
}

.btn {
  cursor: pointer;
  padding: 0 14px;
  height: 32px;
  line-height: 32px;
  text-align: center;
  border-radius: 4px;
  font-size: 13px;
}
.btn-left {
  color: #fff;
  border: 1px solid #fff;
}
.btn-right {
  cursor: pointer;
  margin-left: 14px;
  color: #1890ff;
  background-color: #fff;
}
.mr24 {
  margin-right: 24px;
}
.content {
  margin-top: 20px;
  padding-bottom: 50px;
  padding-left: 150px;
  background: #fff;

  ::v-deep .el-checkbox__label {
    font-size: 13px;
  }
  .tips {
    width: 90px;
    text-align: right;
    margin-left: -130px;
  }
  .prompt {
    font-size: 13px;
    color: #909399;
    line-height: 10px;
  }
}
.box {
  padding: 20px 14px;
  width: 80%;
  height: 100%;

  background: #f7fbff;
  border-radius: 8px;
}
.phase_1 {
  ::v-deep .el-form-item {
    margin-bottom: 0;
  }
}
.el-icon-edit-outline {
  margin-left: 5px;
  font-size: 18px;
  cursor: pointer;
}
.fz-12 {
  font-size: 13px;
}
.textPosition {
  position: relative;
}
::v-deep .el-textarea__inner {
  resize: none;
  font-size: 13px;
}
.icon {
  font-size: 16px;
  position: absolute;
  right: 5px;
  bottom: 39px;
}
.line {
  border-bottom: 1px solid #e8ebf2;
  margin: 10px 0;
}
.field-text {
  cursor: pointer;
  height: 32px;
  line-height: 32px;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
}
.field-text:hover {
  background: #f7fbff;
  color: #1890ff;
}
.field-box {
  height: 350px;
  overflow-y: auto;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
}
.field-box::-webkit-scrollbar {
  height: 0;
  width: 0;
}
::v-deep.jv-container .jv-code {
  max-height: 600px;
}
::v-deep .el-checkbox {
  width: 100px;
}
::v-deep.jv-container .jv-code {
  overflow-y: scroll;
}
</style>
