<template>
  <view class="content">
    <!-- 表单内容 -->
    <view class="examine-content">
      <uni-forms :border="false" :modelValue="formData" ref="form" label-width="80px" :rules="data.rules">
        <view class="list-item">
          <view v-for="(val, index) in listData" :key="index">
            <!-- 输入框 -->
            <uni-forms-item class="input-label" required name="name" v-if="val.form_value === 'input'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData[val.field_name_en]" type="text" :clearable="false"
                :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" :maxlength="val.max"
                :placeholder="'请输入' + val.field_name"></uni-easyinput>
            </uni-forms-item>

            <!-- 数字-->
            <uni-forms-item class="input-label" name="price" v-if="data.number.includes(val.form_value)">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData[val.field_name_en]" type="number" :clearable="false"
                :styles="styles" maxlength="11" :placeholder-style="placeholderStyle" :autoHeight="true"
                :placeholder="'请输入' + val.field_name"></uni-easyinput>
            </uni-forms-item>
            <!-- 富文本 -->
            <uni-forms-item class="is-direction-top" v-if="val.form_value === 'rich_text'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <c-editor :content="formData[val.field_name_en]" :placeholder="val.placeholder"
                @saveContent="saveContent($event,val)"></c-editor>
            </uni-forms-item>
            <!--   <uni-forms-item class="input-label" name="price" v-if="val.form_value === 'rich_text'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <view class="picker-input picker-input-placeholder">
                <c-editor :content="formData[val.field_name_en]" :placeholder="val.placeholder"
                  @saveContent="saveContent"></c-editor>
              </view>
            </uni-forms-item> -->

            <!-- 开关 -->
            <uni-forms-item class="input-label" name="price" v-if="val.form_value === 'switch'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <view class="picker-input picker-input-placeholder">
                <switch :checked="formData[val.field_name_en] == 0 ? false : true"
                  @change="switchChange($event, val)" />
              </view>
            </uni-forms-item>


            <select-department v-if="val.field_name_en == 'frame_id'" :index="index" :type="`module`" :config-data="val"
              @change="changeDepartment($event, val, index)"></select-department>
            <select-member v-if="data.member.includes(val.field_name_en)" :type="`module`" :index="index"
              :config-data="val" :disabled="data.disabledList.includes(val.field_name_en)"
              @change="changeMember($event, val, index)"></select-member>

            <!-- 复选 -->
            <uni-forms-item class="input-label" v-if="val.form_value === 'checkbox'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <view @click="changeCheck(val, index)"
                v-if="!formData[val.field_name_en]||formData[val.field_name_en].length==0"
                class="picker-input picker-input-placeholder">
                {{ '请选择' + val.field_name }}
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view v-else class="picker-input label-flex" @click="changeCheck(val, index)">
                <text>{{ val.text }}</text>
              </view>

              <multiplePicker :show="data.show" :columns="data.labelData" :defaultIndex=" data.multipleData"
                @change="multiplePickerChange($event, val)" @cancel="ontouchcancel"></multiplePicker>
            </uni-forms-item>

            <!-- 客户标签 -->
            <uni-forms-item class="input-label" v-if="val.form_value == 'tag'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <view @click="changeLabel(val, index, formData[val.field_name_en])"
                v-if="!formData[val.field_name_en] || formData[val.field_name_en].length == 0"
                class="picker-input picker-input-placeholder">
                {{ '请选择' + val.field_name }}
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view v-else class="picker-input label-flex"
                @click="changeLabel(val, index, formData[val.field_name_en])">
                <block v-for="(item, index) in val.text" :key="index">
                  <text class="label-box">{{ item }}</text>
                </block>
              </view>
            </uni-forms-item>

            <!-- 级联复选 -->
            <uni-forms-item class="input-label" v-if="val.form_value == 'cascader'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <view @click="changeCascde(val, index)" v-if="!formData[val.field_name_en]"
                class="picker-input picker-input-placeholder">
                {{ '请选择' + val.field_name }}
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view v-else class="picker-input label-flex" @click="changeCascde(val, index)">
                <text class="overflow-x">{{ val.text }}</text>
                <view class="iconfont icon-fanhui"></view>
              </view>
            </uni-forms-item>

            <!-----一对一------->
            <uni-forms-item class="input-label"
              v-if="val.form_value == 'input_select' && val.field_name_en !== 'frame_id' && !data.member.includes(val.field_name_en)">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <view @click="openOne(val, index)" v-if="!formData[val.field_name_en]"
                class="picker-input picker-input-placeholder">
                {{ '请选择' + val.field_name }}
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view v-else class="picker-input label-flex" @click="openOne(val, index)">
                <text>
                  {{ formData[val.field_name_en].name }}
                </text>
              </view>
            </uni-forms-item>

            <!-- 单选按钮 -->
            <uni-forms-item class="input-label" v-if="val.form_value === 'radio'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <picker mode="selector" :value="val.value" :range="val.data_dict" range-key="name"
                @change="pickerChange($event, val, index)">
                <view v-if="!formData[val.field_name_en]" class="picker-input picker-input-placeholder">
                  {{ '请选择' + val.field_name }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <view v-else class="picker-input">
                  {{ val.text }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
              </picker>
            </uni-forms-item>

            <!-- 省市区-级联选择器 -->
            <uni-forms-item class="input-label" v-if="val.form_value === 'cascader_address'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <uni-data-picker :localdata="addressData" :map="{ text: 'name', value: 'value' }"
                :popup-title="val.placeholder" @change="cityChange($event, val, index)">
                <view v-if="!formData[val.field_name_en]" class="picker-input picker-input-placeholder">
                  {{ '请选择' + val.field_name }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <view class="picker-input" v-else>
                  {{ val.text }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
              </uni-data-picker>
            </uni-forms-item>

            <!-----级联单选------>
            <uni-forms-item class="input-label" v-if="val.form_value === 'cascader_radio'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <uni-data-picker :localdata="val.data_dict" :popup-title="val.placeholder"
                :map="{ text: 'name', value: 'value' }" @change="radioChange($event, val, index)">
                <view v-if="!formData[val.field_name_en]" class="picker-input picker-input-placeholder">
                  {{ '请选择' + val.field_name }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <view class="picker-input" v-else>
                  <view class="overflow-x">
                    {{ val.text }}
                  </view>
                  <view class="iconfont icon-fanhui"></view>
                </view>
              </uni-data-picker>
            </uni-forms-item>

            <!-- 时间组件 -->
            <uni-forms-item class="input-label" v-if="val.form_value === 'date_time_picker'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>

              <view class="xp-picker-content">
                <template v-if="!data.disabledList.includes(val.field_name_en)">
                  <xp-picker v-model="formData[val.field_name_en]" mode="ymdhi" actionPosition="top"
                    :yearRange="[2008, 2070]" @confirm="changeForms" />
                </template>
                <view v-else @click="disabledFn"> {{formData[val.field_name_en]}}</view>
                <view class="iconfont icon-fanhui"></view>
              </view>

            </uni-forms-item>

            <uni-forms-item class="input-label" v-if="val.form_value === 'date_picker'">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>

              <view class="xp-picker-content">
                <xp-picker v-model="formData[val.field_name_en]" mode="ymd" actionPosition="top"
                  :yearRange="[2008, 2070]" @confirm="changeForms" />
                <view class="iconfont icon-fanhui"></view>
              </view>
            </uni-forms-item>

            <!-- 上传图片 -->
            <uni-forms-item class="is-direction-top" v-if="val.form_value === 'image'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36 p24">
                  {{ val.field_name }}
                  <text class="tips"> (建议734*1034，大小不超过2M，支持jpg、jpeg、png) </text>
                </view>

                <view class="upload">
                  <view class="box" v-for="(item, indexImg) in formData[val.field_name_en]" :key="indexImg">
                    <image class="img" :src="item.url" mode="" @click="preview(item)"></image>
                    <view class="delete" @click="deleteImg(val, indexImg)">
                      <text class="iconfont icon-shenpizhongxin-jujue"></text>
                    </view>
                  </view>
                  <view class="upload-box"
                    v-if="!formData[val.field_name_en] || (formData[val.field_name_en] && formData[val.field_name_en].length < val.options.max)"
                    @click="uploadAvatar(val, index)">
                    <view class="iconfont icon-paizhao"></view>
                    <view class="text">上传图片</view>
                  </view>
                </view>
              </template>
            </uni-forms-item>

            <!-- 上传附件 -->
            <uni-forms-item class="is-direction-top" v-if="val.form_value === 'file'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36 p24">
                  <view class="label">
                    <view>
                      {{ val.field_name }}
                    </view>
                    <view class="iconfont icon-biaodan-tianjia"
                      v-if="!formData[val.field_name_en] || (formData[val.field_name_en] && formData[val.field_name_en].length <= val.options.max)"
                      @click="uploadFlieFn(val, index)"></view>
                  </view>
                  <view class="tips"> (建议大小不超过{{ fileSizeOne }}M，支持图片、附件、文档) </view>
                </view>
                <view class="flie">
                  <view class="item" v-for="(item, index) in formData[val.field_name_en]" :key="index"
                    @click="preview(item)">
                    <div class="left-view">
                      <image class="img" v-if="!isTypeImage(item.real_name)"
                        :src="`/static/image/cloudfile/${isFileTypeIcon(item.real_name)}`" mode="widthFix">
                      </image>
                      <image class="img" v-else :src="item.url"> </image>
                    </div>
                    <div class="right-view over-text name">
                      {{ item.real_name }}
                      <view class="size"> {{formatBytes( item.size) || '--' }} </view>
                      <view class="iconfont icon-guanbi-yangshiyi1" @click.stop="deleteFile(val, index)"></view>
                    </div>
                  </view>
                </view>
              </template>
            </uni-forms-item>

            <uni-forms-item class="is-direction-top" v-if="val.form_value === 'textarea'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36">
                  {{ val.field_name }}
                  <text class="iconfont" v-if="val.is_default_value_not_null == 0">*</text>
                </view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData[val.field_name_en]" type="textarea"
                :clearable="false" :styles="styles" :placeholder-style="placeholderStyle" :adjust-position="false"
                :maxlength="256" :autoHeight="true" :placeholder="val.placeholder"></uni-easyinput>
            </uni-forms-item>
          </view>
        </view>
      </uni-forms>
    </view>

    <!--    <view class="slider-laber-bottom" v-if="listData.length > 0">
      <button class="laber-bottom confirm" @click="handleConfirm">提交</button>
    </view> -->

    <!-- 组件 -->
    <cascade ref="cascdeRef" title="选择分类" :listData="data.labelData" @reset="resetCascade" @change="changeCascade"
      @submitOk="cascadeSubmit"></cascade>
    <selected-label title="选择标签" ref="selectedLabelRef" :listData="data.labelData" @changeItem="changeItem"
      @resetLabel="resetLabel"></selected-label>
  </view>
</template>
<script setup>
  import { ref, reactive, toRefs, onMounted, watch } from 'vue'
  import cEditor from '@/components/editor-common/editor.vue'
  import message from '@/utils/message'
  import { useStore } from 'vuex'
  import { formatBytes } from '@/utils/file'
  import selectedLabel from '@/pages/module/components/selectedLabel.vue'
  import selectDepartment from '@/components/examineForm/components/selectDepartment.vue'
  import selectMember from '@/components/examineForm/components/selectMember.vue'
  import multiplePicker from '@/components/multiplePicker/index.vue'
  import cascade from './cascade.vue'
  import { clickNavigateTo, lookPreview, fileSizeOne, isTypeImage, isFileTypeIcon } from '@/utils/helper'
  import { clientlabelApi } from '@/api/customer'
  const store = useStore()
  const props = defineProps({
    listData: {
      type: Array,
      default () {
        return []
      },
    },
    addressData: {
      type: Array,
      default () {
        return []
      },
    },
    info: {
      type: Object,
      default () {
        return {}
      },
    },
    keyName: {
      type: String,
      default () {
        return ''
      },
    },
    formInfo: {
      type: Object,
      default () {
        return {}
      },
    },
  })
  const { listData, keyName, info, addressData, formInfo } = toRefs(props)
  // 定义data数据
  const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff',
  })
  const data = reactive({
    rowData: {}, // 当前点击的字段
    rowIndex: 0,
    multipleData: [],
    labelData: [],
    disabledList: ['user_id', 'update_user_id', 'created_at', 'updated_at'],
    member: ['user_id', 'update_user_id', 'owner_user_id'],
    number: ['input_number', 'input_float', 'input_percentage', 'input_price'],
    show: false,
    photoUrl: 'http://test.com', //服务器图片域名或者ip
    api: '/upload', //上传图片接口地址
    name: 'file',
    value: '<div>Hello World!</div>',
    flieList: [],
    imgs: [],
    imageList: [],
    selectedLabel: [],
    eid: '',
    labelName: [], // 客户标签
  })
  // 自定义表单
  const formData = reactive({})
  // 一对一数据
  const getOneOnOne = computed(() => {
    return store.state.app.oneOnOneData
  })
  watch(
    getOneOnOne,
    (newvalue) => {
      formData[data.rowData.field_name_en] = newvalue
    }, {
      deep: true,
    }
  )

  const changeForms = () => {}

  // 监听数据变化
  watch(
    () => [info.value, listData.value, formInfo.value],
    (newvalue) => {
      Object.keys(newvalue[0]).forEach((item) => {
        formData[item] = newvalue[0][item] ? newvalue[0][item] : ''
      })
      if (newvalue[1].length > 0) {
        newvalue[1].forEach((item, index) => {
          const itemOptions = newvalue[2]?.options[index]?.options;
          if (data.member.includes(item.field_name_en)) {
            item.data_dict = [formData[item.field_name_en]]
          }
          if (item.field_name_en === 'frame_id') {
            item.data_dict = [formData[item.field_name_en]]
          }
          if (item.form_value == 'cascader_radio') {
            const str = findNamesByIds(item.data_dict, formData[item.field_name_en], 'value')
            item.text = str.join('/')
          }
          if (item.form_value == 'checkbox') {
            const str = findNamesByIds(item.data_dict, formData[item.field_name_en], 'value')
            item.text = str.join(',')
          }
          if (item.form_value == 'cascader') {
            let values = []
            if (formData[item.field_name_en] && formData[item.field_name_en].length > 0) {
              formData[item.field_name_en].map((i) => {
                values = values.concat(i)
              })
              setCheckboxStatus(item.data_dict, values)
              const names = extractCheckboxName(item.data_dict)
              item.text = names.map((i) => i.join('/')).join(',')
            }

          }
          if (item.form_value == 'cascader_address') {
            let ids = formData[item.field_name_en]?.map(String)
            const str = findNamesByIds(addressData.value, ids, 'value')
            item.text = str.join('/')
          }
          if (item.form_value == 'tag') {
            item.text = findNamesByIds(item.data_dict, formData[item.field_name_en], 'id')
          }
          if (item.form_value == 'radio') {
            if (itemOptions) {
              formData[item.field_name_en] = itemOptions.defaultValue
            }
            item.data_dict.map((val) => {
              if (val.value == formData[item.field_name_en]) {
                item.text = val.name
              }
            })
          }
        })
      }
    }
  )

  const extractCheckboxName = (data, path = []) => {
    let result = []

    for (let i = 0; i < data.length; i++) {
      const item = data[i]

      if (item.children && item.children.length > 0) {
        // 递归处理有子节点的情况
        const childPath = [...path, item.name]
        const childValues = extractCheckboxName(item.children, childPath)
        result = result.concat(childValues)
      } else if (item.checkbox === true) {
        // 只提取没有子节点且 checkbox 属性为 true 的数据
        result.push([...path, item.name])
      }
    }

    return result
  }

  const openSelect = () => {
    data.show = true
  }
  // 级联选择
  const cityChange = (e, val, index) => {
    let len = e.detail.value
    let arr = []
    let str = []
    len.map((item) => {
      arr.push(item.value)
      str.push(item.text)
    })
    formData[val.field_name_en] = arr
    val.text = str.join('/')
  }
  // 复选
  const multiplePickerChange = (e, val, index) => {
    let text = []
    if (e.selected.length > 0) {
      e.selected.map((item) => {
        text.push(item.text)
      })
      listData.value[data.rowIndex].text = text.join('、')
      formData[listData.value[data.rowIndex].field_name_en] = e.value
    }
    ontouchcancel()
  }
  const disabledFn = () => {
    message.error('禁止手动修改数据')
    return false
  }

  // 单选数据
  const pickerChange = (e, val, index) => {
    let selectIndex = e.detail.value
    formData[val.field_name_en] = val.data_dict[selectIndex].value
    val.text = val.data_dict[selectIndex].name
  }

  // 选择一对一
  const openOne = (val, index) => {
    data.rowData = val
    clickNavigateTo(`/pages/module/oneOnOne?id=${val.id}&&keyName=${keyName.value}`)
  }

  const saveContent = (e, val) => {
    formData[val.field_name_en] = e
  }

  // 选择成员
  const changeMember = (e, val, index) => {
    const len = store.state.app.depSelectIndex
    if (len > -1) {
      listData.value[len].data_dict = e
    }

    formData[val.field_name_en] = e[0]
  }

  // 选择部门
  const changeDepartment = (e, val, index) => {
    const len = store.state.app.depSelectIndex
    if (len > -1) {
      listData.value[len].data_dict = e
    }

    formData[val.field_name_en] = e[0]
  }
  // 关闭弹窗
  const ontouchcancel = () => {
    setTimeout(() => {
      data.show = false
    }, 200)
  }
  // 级联单选
  const radioChange = (e, val, index) => {
    formData[val.field_name_en] = []
    let str = []
    e.detail.value.map((item) => {
      str.push(item.text)
      formData[val.field_name_en].push(item.value)
    })
    val.text = str.join('/')
  }

  // 开关
  const switchChange = (e, val) => {
    formData[val.field_name_en] = e.detail.value ? '1' : '0'
  }

  // 日期选择
  const startDateChange = (e, val, index) => {
    formData[val.field_name_en] = e.detail.value
  }

  // 级联选择回显数据
  const findNamesByIds = (tree, ids, type) => {
    let result = []

    function traverse(node) {
      if (ids?.includes(node[type])) {
        result.push(node.name)
      }
      if (node.children) {
        for (const child of node.children) {
          traverse(child)
        }
      }
    }
    for (const node of tree) {
      traverse(node)
    }
    return result
  }

  // 图片与文档预览
  const preview = (item) => {
    lookPreview(item.att_dir, item.real_name, data.imageList)
  }
  let emit = defineEmits(['submitOk'])

  // 上传图片
  import { uploadImage } from '@/utils/file'
  const uploadAvatar = (val, index) => {
    data.rowData = val
    data.rowIndex = index
    uploadImage(
        'attach/imgs', {
          relation_type: 'client',
        },
        fileSizeOne
      )
      .then((res) => {
        message.success(res.message)
        formData[val.field_name_en] = formData[val.field_name_en] || []
        let newData = {
          url: res.data.src,
          id: res.data.id,
          size: res.data.size,
          name: res.data.name,
        }
        formData[val.field_name_en].push(newData)
        formData[val.field_name_en].map((item) => {
          if (isTypeImage(item.real_name)) {
            data.imageList.push(item.att_dir)
          }
        })
      })
      .catch((error) => {
        message.error(error)
      })
  }

  // 上传附件
  import { uploadFlie } from '@/utils/file'
  const uploadFlieFn = (val, index) => {
    data.rowData = val
    data.rowIndex = index
    const datas = {
      relation_id: 1,
      relation_type: 'client',
    }
    uploadFlie('attach/imgs', datas, fileSizeOne)
      .then((res) => {
        if (res.status == 200) {
          formData[val.field_name_en] = formData[val.field_name_en] || []
          message.success(res.message)
          console.log(res.data)
          let newData = {
            url: res.data.src,
            file_url: res.data.src,
            id: res.data.id,
            size: res.data.size,
            name: res.data.name,
            real_name: res.data.name,
          }
          formData[val.field_name_en].push(newData)
          formData[val.field_name_en].map((item) => {
            if (isTypeImage(item.real_name)) {
              data.imageList.push(item.att_dir)
            }
          })
          // formData[val.field_name_en] = data.flieList
        }
      })
      .catch((error) => {
        message.error(error)
      })
  }

  // 图片删除
  const deleteImg = (val, index) => {
    formData[val.field_name_en].splice(index, 1)
  }
  // 附件删除
  const deleteFile = (val, index) => {
    formData[val.field_name_en].splice(index, 1)
  }

  const successPopupRef = ref(null)
  const form = ref(null)
  const loading = ref(false)
  // 提交表单
  const handleConfirm = () => {
    for (let i = 0; i < listData.value.length; i++) {
      if (listData.value[i].is_default_value_not_null == 0) {
        if (!formData[listData.value[i].field_name_en]) {
          message.error(listData.value[i].field_name + '不能为空')
          return false
        }
      }
    }

    emit('submitOk', formData)
  }

  // 标签选择
  const selectedLabelRef = ref(null)
  const cascdeRef = ref(null)
  const changeLabel = (e, index, selectedLabel) => {
    data.rowData = e
    data.rowIndex = index
    data.labelData = e.data_dict
    selectedLabelRef.value.popupOpen(selectedLabel, e.text || [])
  }

  const changeCheck = (e, index) => {
    data.rowData = e
    data.rowIndex = index
    data.labelData = e.data_dict
    data.multipleData = formData[e.field_name_en]
    data.show = true
  }
  const changeCascde = (e, index) => {
    data.rowData = e
    data.rowIndex = index
    data.labelData = e.data_dict
    let value = []
    if (formData[e.field_name_en] && formData[e.field_name_en].length > 0) {
      formData[e.field_name_en].map((item) => {
        value = value.concat(item)
      })
    }

    setCheckboxStatus(data.labelData, value)
    cascdeRef.value.popupOpen(formData[e.field_name_en] || [])
  }

  const resetLabel = () => {
    formData[data.rowData.field_name_en] = []
    listData.value[data.rowIndex].text = ''
  }
  // 标签选择回调
  const changeItem = (e, name) => {
    formData[data.rowData.field_name_en] = e
    listData.value[data.rowIndex].text = name
  }

  // 级联复选回调
  const cascadeSubmit = (e) => {
    formData[data.rowData.field_name_en] = e.value
    listData.value[data.rowIndex].text = e.name
  }

  const resetCascade = () => {
    function resetCheckbox(labelData) {
      labelData.map((item) => {
        item.checkbox = false
        if (item.children && item.children.length > 0) {
          resetCheckbox(item.children)
        }
      })
    }
    resetCheckbox(data.labelData)
    formData[data.rowData.field_name_en] = []
    listData.value[data.rowIndex].text = ''
  }

  const changeCascade = (item) => {
    let val = findParentValues(data.labelData, item.value)
    selectCheckboxByvalue(data.labelData, val, item.checkbox)
  }
  //设置
  const setCheckboxStatus = (data, checkboxValues) => {
    data.map((item) => {
      if (checkboxValues.includes(item.value)) {
        // 如果当前节点的 value 在 checkboxValues 中存在，则设置 checkbox 为 true
        item.checkbox = true
      }
      if (item.children && item.children.length > 0) {
        // 递归处理子节点
        setCheckboxStatus(item.children, checkboxValues)
      }
    })
  }

  const findParentValues = (data, targetValue) => {
    const parentValues = []

    function traverse(nodes, parentValues) {
      for (const node of nodes) {
        if (node.value === targetValue) {
          return true
        }

        if (node.children && node.children.length > 0) {
          parentValues.push(node.value)
          const found = traverse(node.children, parentValues)
          if (found) {
            return true
          }
          parentValues.pop()
        }
      }

      return false
    }

    traverse(data, parentValues)
    return parentValues
  }

  const selectCheckboxByvalue = (labelData, checkboxValue, checkbox) => {
    labelData.map((item) => {
      if (checkboxValue.includes(item.value)) {
        item.checkbox = checkbox
      }
      if (item.children && item.children.length) {
        selectCheckboxByvalue(item.children, checkboxValue, checkbox)
      }
    })
  }

  const getKey = (row, key) => {
    let formKey = ''
    listData.value.map((item) => {
      item.data.map((val) => {
        if (key) {
          if (val.key === row) {
            formKey = val.field_name_en
          }
        }
        if (val.type === row) {
          formKey = val.field_name_en
        }
      })
    })
    return formKey
  }

  const startDate = computed(() => {
    return getDate('start')
  })

  const endDate = computed(() => {
    return getDate('end')
  })
  defineExpose({
    handleConfirm,
  })
  // 日期选择器
  const getDate = (type) => {
    const date = new Date()
    let year = date.getFullYear()
    let month = date.getMonth() + 1
    let day = date.getDate()
    if (type === 'start') {
      year = year - 60
    } else if (type === 'end') {
      year = year + 20
    }
    month = month > 9 ? month : '0' + month
    day = day > 9 ? day : '0' + day
    return `${year}-${month}-${day}`
  }
</script>
<style lang="scss" scoped>
  .content {
    width: 100%;
    position: relative;

    .label {
      padding-right: 24rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16rpx;

      .icon-biaodan-tianjia {
        color: #c0c4cc !important;
        font-size: 34rpx;
      }
    }

    .tips {
      font-size: 20rpx;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: 400;
      color: #999999;
      margin-bottom: 20rpx;
    }

    .uni-forms-item__label {
      height: auto;
      padding: 0;
      line-height: 1;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 30rpx;
      color: #303133;

      .iconfont {
        margin-top: 8rpx;
        margin-left: 5rpx;
        color: #ff2529;
      }
    }

    .list-item {
      background-color: #fff;
      border-radius: 12rpx;
      padding-left: 24rpx;
      margin: 20rpx;
      padding-bottom: 30rpx;
    }

    .mt36 {
      margin-top: 36rpx;
    }

    .mt20 {
      margin-top: 20rpx;
    }

    .uni-easyinput__content-textarea {
      min-height: 252rpx;
    }

    ::v-deep.uni-data-checklist .checklist-group {
      display: flex;
      justify-content: flex-end;

      .checklist-box {
        margin-left: 20rpx;
        margin-right: 0;
      }
    }

    .picker-input {
      text-align: right;
      color: $uni-text-color;
      font-size: 30rpx;
      align-items: center;
      display: flex;
      justify-content: flex-end;


      .overflow-x {
        width: 400rpx;
        white-space: nowrap;
        overflow-x: auto;
        display: -webkit-box;
        -webkit-overflow-scrolling: touch;
        margin: 0.5rem 0.5rem;
      }

      .overflow-x::-webkit-scrollbar {
        display: none;
      }

      .iconfont {
        padding-right: 16rpx;
        margin-top: 7rpx;
        transform: rotate(180deg);
        font-size: 24rpx;
        color: #c0c4cc;
      }
    }

    .picker-input-placeholder {
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 30rpx;
      color: #c0c4cc;
    }

    ::v-deep .uni-forms-item {
      margin-bottom: 0;
      // border-bottom: 1px solid #ebeef5;
    }

    .input-label {
      padding: 18rpx 24rpx 18rpx 0;
      align-items: center;
      min-height: 108rpx;

      ::v-deep .uni-easyinput__content-input {
        text-align: right;
        padding-right: 0 !important;
      }

      ::v-deep .uni-forms-item__label {
        // max-width: 168rpx;
        display: flex;
        line-height: 1.2;

        .iconfont {
          width: 16rpx;
        }
      }
    }

    .label-flex {
      padding-left: 50rpx;
      display: flex;
      flex-wrap: wrap;
      justify-content: flex-end;

      .label-box {
        display: inline-block;
        width: 100rpx;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        padding: 0 10rpx;
        height: 50rpx;
        font-size: 26rpx;
        text-align: center;
        line-height: 50rpx;
        background-color: #f4f4f5;
        border-color: #e9e9eb;
        color: #909399;
        border-radius: 8rpx;
        margin-right: 10rpx;
        margin-top: 10rpx;
      }
    }
  }

  ::v-deep .uni-input-input {
    font-size: 30rpx !important;
  }

  ::v-deep .uni-switch-input {
    height: 30px;
  }

  ::v-deep .uni-switch-input:after {
    height: 28px;
  }

  ::v-deep .uni-switch-input:before {
    background-color: #c0c4cc;
  }

  ::v-deep .uni-textarea-textarea {
    font-size: 30rpx !important;
    overflow-y: auto !important;
  }

  // 上传附件
  .flie {
    padding: 24rpx 24rpx 24rpx 0;

    .item {
      width: 100%;
      height: 42px;
      padding: 7px;
      background: #f6f7f9;
      border-radius: 4px 4px 4px 4px;
      display: flex;
      position: relative;

      .icon-guanbi-yangshiyi1 {
        position: absolute;
        right: 7px;
        top: 7px;
        color: #909399;
      }

      .left-view {
        width: 26px;
        min-width: 26px;
        height: 100%;

        .img {
          display: block;
          margin: 0;
          width: 26px;
          height: 26px;
          border-radius: 2px 2px 2px 2px;
        }
      }

      .right-view {
        width: 80%;
        height: 100%;
        margin-left: 5px;

        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 12px;
        color: #303133;

        .size {
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 10px;
          color: #909399;
        }
      }
    }
  }

  // 上传图片
  .upload {
    width: 100%;
    min-height: 236rpx;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    padding: 28rpx 28rpx 0 28rpx;

    .upload-box {
      width: 140rpx;
      height: 140rpx;
      border-radius: 8rpx 8rpx 8rpx 8rpx;
      border: 2rpx solid #dddddd;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      margin-bottom: 20rpx;

      .icon-paizhao {
        font-size: 40rpx;
        color: #bfbfbf;
      }

      .text {
        margin-top: 20rpx;
        font-size: 24rpx;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: 400;
        color: #999999;
      }
    }

    .box {
      position: relative;

      .img {
        display: block;
        width: 140rpx;
        height: 140rpx;
        margin-right: 24rpx;
        margin-bottom: 24rpx;
        border-radius: 8rpx;
      }

      .delete {
        position: absolute;
        top: 0;
        right: 22rpx;
        width: 32rpx;
        height: 32rpx;
        background: rgba(0, 0, 0, 0.6);
        border-radius: 0 8rpx 0 16rpx;
        display: flex;

        align-items: center;
        justify-content: center;

        .icon-shenpizhongxin-jujue {
          color: rgba(255, 255, 255, 0.8);
          font-size: 24rpx;
          font-weight: 400px;
        }

        .icon-paizhao {
          font-size: 35rpx;
          color: #bfbfbf;
        }
      }
    }
  }

  .slider-laber-bottom {
    margin-top: 30rpx;
    padding: 0 20rpx;
    display: flex;
    height: 86rpx;
    align-items: center;

    .laber-bottom {
      height: 100%;
      border-radius: 8rpx;
      border: none;
      font-size: $uni-font-size-default;
      line-height: 86rpx;

      &::after {
        border: 0;
      }
    }

    .confirm {
      width: 100%;
      background: #308bf8;
      color: #fff;
    }
  }
</style>