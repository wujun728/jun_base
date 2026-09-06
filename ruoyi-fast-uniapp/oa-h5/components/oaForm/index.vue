<template>
  <view class="content">
    <!-- 表单内容 -->
    <view class="examine-content">
      <uni-forms :border="false" :modelValue="formData" ref="form" label-width="80px" :rules="data.rules">
        <view class="list-item" v-for="(item, indexItem) in listData" :key="indexItem">
          <view v-for="(val, index) in item.data" :key="index">
            <!-- 输入框 -->
            <uni-forms-item class="input-label" required name="name"
              v-if="val.input_type === 'input' && val.type === 'text'">
              <template v-slot:label>
                <view class="uni-forms-item__label">{{ val.key_name }} <text class="iconfont"
                    v-if="val.required == 1">*</text></view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData[val.key]" type="text" :clearable="false"
                :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" :maxlength="val.max"
                placeholder="请输入">
              </uni-easyinput>
            </uni-forms-item>

            <!-- 数字-->
            <uni-forms-item class="input-label" name="price" v-if="val.type === 'number'">
              <template v-slot:label>
                <view class="uni-forms-item__label">{{ val.key_name }}<text class="iconfont"
                    v-if="val.required == 1">*</text> </view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData[val.key]" type="number" :clearable="false"
                :styles="styles" maxlength="11" :placeholder-style="placeholderStyle" :autoHeight="true"
                placeholder="请输入">
              </uni-easyinput>
            </uni-forms-item>

            <!-- 客户标签 -->
            <uni-forms-item class="input-label" v-if="val.key === 'customer_label'">
              <template v-slot:label>
                <view class="uni-forms-item__label">{{ val.key_name }}<text class="iconfont"
                    v-if="val.required == 1">*</text> </view>
              </template>
              <view class="picker-input picker-input-placeholder" @click="changeLabel"
                v-if="formData[val.key].length == 0">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input label-flex" @click="changeLabel">
                <block v-for="(item, index) in data.labelName" :key="index">
                  <text class="label-box"> {{ item }}</text>
                </block>
              </view>
            </uni-forms-item>

            <!-- 单选按钮 -->
            <uni-forms-item class="input-label" v-if="val.type === 'radio'">
              <template v-slot:label>
                <view class="uni-forms-item__label">{{ val.key_name }} <text class="iconfont"
                    v-if="val.required == 1">*</text> </view>
              </template>
              <uni-data-checkbox v-model="formData[val.key]" :localdata="val.options" />
            </uni-forms-item>

            <!-- 省市区-级联选择器 -->
            <uni-forms-item class="input-label" v-if="val.input_type === 'select' && val.options_level > 1">
              <template v-slot:label>
                <view class="uni-forms-item__label">{{ val.key_name }} <text class="iconfont"
                    v-if="val.required == 1">*</text></view>
              </template>
              <uni-data-picker v-model="formData[val.key]" :localdata="val.options"
                :popup-title="val.placeholder || '请选择'" @change="cityChange($event, indexItem, index)">
                <view v-if="!formData[val.key]" class="picker-input picker-input-placeholder">
                  请选择
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <view class="picker-input" v-else>
                  {{ val.text }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
              </uni-data-picker>
            </uni-forms-item>

            <!-- 下拉选择 -->
            <uni-forms-item v-if="val.input_type === 'select' && val.options_level == 1" class="input-label">
              <template v-slot:label>
                <view class="uni-forms-item__label">
                  <text class="label-item">{{ val.key_name }}</text>
                  <text v-if="val.required == 1" class="iconfont">*</text>
                </view>
              </template>
              <view v-if="val.type == 'multiple'">
                <view v-if="!formData[val.key]" class="picker-input picker-input-placeholder"
                  @click="openSelect(val,indexItem,index)">
                  请选择
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <view v-if="formData[val.key]" class="picker-input" @click="openSelect(val,indexItem,index)">
                  {{ val.text }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <multiplePicker v-if="data.show" :show="data.show" :columns="data.options"
                  @change="multiplePickerChange($event, val)" @cancel="ontouchcancel"></multiplePicker>
              </view>

              <picker v-else mode="selector" :value="formData[val.key]" :range="val.options" range-key="text"
                @change="contractChange($event, indexItem, index)">
                <view v-if="!formData[val.key]" class="picker-input picker-input-placeholder">
                  请选择
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <view v-if="formData[val.key]" class="picker-input">
                  {{ val.text || val.text1 }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
              </picker>
            </uni-forms-item>

            <!-- 开始时间 -->
            <uni-forms-item class="input-label" v-if="val.type === 'date'">
              <template v-slot:label>
                <view class="uni-forms-item__label">{{ val.key_name }} <text class="iconfont"
                    v-if="val.required == 1">*</text></view>
              </template>
              <picker mode="date" :value="formData[val.key]" :start="startDate" :end="endDate"
                @change="startDateChange($event, indexItem, index)">
                <view v-if="!formData[val.key]" class="picker-input picker-input-placeholder">
                  请选择
                  <view class="iconfont icon-fanhui"></view>
                </view>
                <view class="picker-input" v-else>
                  {{ formData[val.key] }}
                  <view class="iconfont icon-fanhui"></view>
                </view>
              </picker>
            </uni-forms-item>

            <!-- 上传图片 -->
            <uni-forms-item class="is-direction-top" v-if="val.type === 'images'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36 p24">
                  {{ val.key_name }} <text class="tips">(建议734*1034，大小不超过2M，支持jpg、jpeg、png) </text>
                </view>

                <view class="upload">
                  <view class="box" v-for="(item, indexImg) in val.options" :key="indexImg">
                    <image class="img" :src="item.att_dir" mode="" @click="preview(item)"> </image>
                    <view class="delete" @click="deleteImg(val,item.id)">
                      <text class="iconfont icon-shenpizhongxin-jujue"></text>
                    </view>
                  </view>
                  <view class="upload-box" v-if="data.imgs.length !== val.max" @click="uploadAvatar(indexItem, index)">
                    <view class="iconfont icon-paizhao"></view>
                    <view class="text"> 上传{{ val.key_name }} </view>
                  </view>
                </view>
              </template>
            </uni-forms-item>

            <!-- 上传附件 -->
            <uni-forms-item class="is-direction-top" v-if="val.type === 'file'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36 p24">
                  <view class="label">
                    <view>
                      {{ val.key_name }}
                    </view>
                    <view class="iconfont icon-biaodan-tianjia" v-if="data.flieList.length !== val.max"
                      @click="uploadFlieFn(indexItem, index)"></view>
                  </view>
                  <view class="tips">(建议大小不超过{{ fileSizeOne }}，支持图片、附件、文档) </view>
                </view>
                <view class="flie">
                  <view class="box" v-for="(item, indexs) in val.options" :key="indexs" @click="preview(item)">
                    <view class="left">
                      <image class="slot-image" :src="`/static/image/cloudfile/${isFileTypeIcon(item.real_name)}`">
                      </image>
                      <view style=" width: calc(100% - 40px);">
                        <view class="name">
                          {{ item.real_name }}
                        </view>
                        <view class="size"> {{ formatBytes(item.att_size) || '--' }} </view>
                      </view>
                      <view class="iconfont icon-guanbi-yangshiyi1" @click.stop="deleteFile(val,item.id)">
                      </view>
                    </view>

                  </view>
                </view>
              </template>
            </uni-forms-item>

            <!-- 备注- 文本域 -->
            <uni-forms-item class="is-direction-top" v-if="val.type === 'textarea'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36"> {{ val.key_name }}<text class="iconfont"
                    v-if="val.required == 1">*</text> </view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData[val.key]" type="textarea" :clearable="false"
                :styles="styles" :placeholder-style="placeholderStyle" :adjust-position="false" :maxlength="256"
                :autoHeight="true" :placeholder="val.placeholder">
              </uni-easyinput>
            </uni-forms-item>

            <!-- 富文本 -->
            <uni-forms-item class="is-direction-top height-350" v-if="val.type === 'oaWangeditor'">
              <template v-slot:label>
                <view class="uni-forms-item__label mt36"> {{ val.key_name }}<text class="iconfont"
                    v-if="val.required == 1">*</text> </view>
              </template>
              <!-- {{formData[val.key]}} -->

              <!-- <mosoweEditor></mosoweEditor> -->
              <!-- <mp-html :content="data.value" /> -->
              <!-- <richtext-editor></richtext-editor> -->
              <!-- <piaoyiEditor :values="data.value" :maxlength="3000" @changes="saveContent" :readOnly="data.readOnly"
                  :photoUrl="data.photoUrl" :api="data.api" :name="data.name" /> -->
              <!--  <lsj-edit ref="lsjEdit" placeholder="输入正文" :maxCount="200" @onReady="editReady">
                  <template v-slot:btns="data">
                    <edit-btns :edit="data.edit" @click="onTabClick" @submit="save"></edit-btns>
                  </template>
                </lsj-edit> -->
              <!-- <sp-editor @init="initEditor" :templates="formData[val.key]" @input="inputOver" @upinImage="upinImage"
                @overMax="overMax">
              </sp-editor> -->

              <c-editor :content="formData[val.key]" :placeholder="val.placeholder"
                @saveContent="saveContent"></c-editor>
            </uni-forms-item>
          </view>
        </view>
      </uni-forms>
    </view>

    <!-- 组件 -->
    <selected-label title="客户标签" ref="selectedLabelRef" @changeItem="changeItem" @resetLabel="resetLabel">
    </selected-label>
    <success-popup ref="successPopupRef" :type="0" title="客户" button-title="添加合同" @change="successChange">
    </success-popup>
  </view>
</template>
<script setup>
  import { ref, reactive, toRefs } from 'vue'
  import { formatBytes } from '@/utils/file'
  import message from '@/utils/message'
  import selectedLabel from '@/pages/customer/list/components/selectedLabel.vue'
  import successPopup from '@/pages/customer/list/components/successPopup.vue'
  import cEditor from '@/components/editor-common/editor.vue'
  import multiplePicker from '@/components/multiplePicker/index.vue'
  import { lookPreview, fileSizeOne, isTypeImage, isFileTypeIcon } from '@/utils/helper'
  import { clientlabelApi } from '@/api/customer'
  const props = defineProps({
    listData: {
      type: Array,
      default () {
        return []
      },
    },
  })
  const { listData } = toRefs(props)
  // 定义data数据
  const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff',
  })
  const data = reactive({
    editorOption: {
      placeholder: '请输入...',
    },
    show: false,
    options: [],
    readOnly: false,
    photoUrl: 'http://test.com', //服务器图片域名或者ip
    api: '/upload', //上传图片接口地址
    txt: '',
    name: 'file',
    value: '<div>Hello World!</div>',
    flieList: [],
    indexItem: 0,
    index: 0,
    imgs: [],
    contents: '',
    imageList: [],
    selectedLabel: [],
    eid: '',
    labelName: [], // 客户标签
  })
  // 自定义表单
  const formData = reactive({})

  // 监听数据变化
  watch(
    () => [listData.value],
    (newvalue) => {
      if (newvalue[0].length > 0) {
        newvalue[0].forEach((item, index) => {
          item.data.forEach((val, index1) => {
            formData[val.key] = val.value
            if (val.input_type === 'select' && val.key !== 'customer_label') {
              let text = findNamesByIds(val.options, val.value)
              val.text = text
            } else if (val.key == 'customer_label') {
              getLabel(val.value)
            }
            if (val.type == 'file' && val.files) {
              val.options = val.files
              val.options.map((item) => {
                if (isTypeImage(item.real_name)) {
                  data.imageList.push(item.att_dir)
                }
              })
            }
            if (val.type == 'images' && val.files) {
              val.options = val.files
              val.options.map((item) => {
                if (isTypeImage(item.real_name)) {
                  data.imageList.push(item.att_dir)
                }
              })
            }
          })
        })
      }
    }
  )

  import { onLoad } from '@dcloudio/uni-app'
  const openSelect = (val, itemIndex, index) => {
    data.index = index
    data.indexItem = itemIndex
    data.show = true
    data.options = val.options
  }
  // 级联选择
  const cityChange = (e, index, index1) => {
    let len = e.detail.value
    let text = []
    let id = []
    len.map((item) => {
      text.push(item.text)
      id.push(item.value)
    })
    listData.value[index].data[index1].text = text.join('-')
    let key = listData.value[index].data[index1].key
    formData[key] = id
  }
  const multiplePickerChange = (e, val) => {
    let text = []
    if (e.selected.length > 0) {
      e.selected.map((item) => {
        text.push(item.text)
      })
      listData.value[data.indexItem].data[data.index].text = text.join('-')
      let key = listData.value[data.indexItem].data[data.index].key
      formData[key] = e.value
    }
    ontouchcancel()
  }
  // 关闭弹窗
  const ontouchcancel = () => {
    setTimeout(() => {
      data.show = false
    }, 200)
  }
  // 下拉选择
  const contractChange = (e, index, index1) => {
    let len = e.detail.value
    let text = listData.value[index].data[index1].options[len].text
    let id = listData.value[index].data[index1].options[len].value
    let key = listData.value[index].data[index1].key
    formData[key] = id
    listData.value[index].data[index1].text = text
  }

  // 日期选择
  const startDateChange = (e, index, index1) => {
    let key = listData.value[index].data[index1].key
    formData[key] = e.detail.value
  }

  // 编辑获取标签
  const getLabel = (val) => {
    if (val.length == 0) return false
    data.selectedLabel = val.map(Number)
    // console.log(data.selectedLabel)
    let list = []
    clientlabelApi().then((res) => {
      res.data.list.map((item) => {
        item.children.map((el) => {
          list.push(el)
        })
      })
      myFilter(list, val)
    })
  }
  const myFilter = (arr1, arr2) => {
    const objArr = []
    const labelArr = []
    arr1.map((item) => {
      if (arr2.length > 0) {
        arr2.map((el) => {
          if (item.id == el) {
            objArr.push(item.name)
          }
        })
      }
    })
    data.labelName = objArr
  }

  // 图片与文档预览
  const preview = (item) => {
    lookPreview(item.att_dir, item.real_name, data.imageList)
  }
  let emit = defineEmits(['submitOk'])
  // 提交表单自定义事件
  const submit = () => {
    if (handleConfirm()) {
      emit('submitOk', formData)
    }
  }
  // 上传图片
  import { uploadImage } from '@/utils/file'
  const uploadAvatar = (index, index1) => {
    uploadImage(
        'attach/imgs', {
          relation_type: 'client',
        },
        fileSizeOne
      )
      .then((res) => {
        message.success(res.message)
        let newData = {
          att_dir: res.data.src,
          id: res.data.id,
          att_size: res.data.size,
          real_name: res.data.name,
        }
        let ids = []
        listData.value[index].data[index1].options.push(newData)
        listData.value[index].data[index1].options.map((item) => {
          ids.push(item.id)
          if (isTypeImage(item.real_name)) {
            data.imageList.push(item.att_dir)
          }
        })
        let key = listData.value[index].data[index1].key
        formData[key] = ids
      })
      .catch((error) => {
        message.error(error)
      })
  }

  // 上传附件
  import { uploadFlie } from '@/utils/file'
  const uploadFlieFn = (index, index1) => {
    const datas = {
      relation_id: 1,
      relation_type: 'client',
    }

    uploadFlie('attach/imgs', datas, fileSizeOne)
      .then((res) => {
        if (res.status == 200) {
          message.success(res.message)
          let newData = {
            att_dir: res.data.src,
            id: res.data.id,
            att_size: res.data.size,
            real_name: res.data.name,
          }
          let ids = []
          listData.value[index].data[index1].options.push(newData)
          listData.value[index].data[index1].options.map((item) => {
            ids.push(item.id)
            if (isTypeImage(item.real_name)) {
              data.imageList.push(item.att_dir)
            }
          })
          let key = listData.value[index].data[index1].key
          formData[key] = ids
        }
      })
      .catch((error) => {
        message.error(error)
      })
  }

  // 富文本内容
  const saveContent = (e) => {
    data.contents = e
  }

  // 图片删除
  const deleteImg = (val, id) => {
    formData[val.key] = formData[val.key].filter(function(item) {
      return item !== id
    })
    val.options = val.options.filter(function(item) {
      return item.id !== id
    })
  }
  // 附件删除
  const deleteFile = (val, id) => {
    formData[val.key] = formData[val.key].filter(function(item) {
      return item !== id
    })
    val.options = val.options.filter(function(item) {
      return item.id !== id
    })


  }

  import { useStore } from 'vuex'
  const store = useStore()
  const successPopupRef = ref(null)
  const form = ref(null)
  const loading = ref(false)
  // 提交表单
  const handleConfirm = () => {
    let is_passed = true
    listData.value.map((item) => {
      item.data.map((val) => {
        if (val.type == 'oaWangeditor') {
          formData[val.key] = data.contents
        }

        if (val.required == 1) {
          if (!formData[val.key]) {
            message.error(`${val.key_name}不能为空`)
            is_passed = false
          } else {
            is_passed = true
          }
        }
      })
    })
    return is_passed
  }

  // 获取级联数据
  const findNamesByIds = (tree, id) => {
    if (id.constructor !== Array) {
      id = [id]
    }
    let result = []

    function traverse(node) {
      if (id.includes(node.value)) {
        result.push(node.text)
      }
      if (node.children) {
        for (const child of node.children) {
          traverse(child)
        }
      }
    }

    if (tree) {
      for (const node of tree) {
        if (node) {
          traverse(node)
        }
      }
    }

    let str = ''
    if (result.length > 1) {
      str = result.join(' - ')
    } else {
      str = result[0]
    }
    return str
  }
  // 标签选择
  const selectedLabelRef = ref(null)
  const changeLabel = (e) => {

    selectedLabelRef.value.popupOpen(data.selectedLabel, data.labelName)
  }
  const resetLabel = () => {
    data.selectedLabel = []
    data.labelName = []
  }
  // 标签选择回调
  const changeItem = (e, name) => {
    let key = getKey('customer_label', 'key')
    formData[key] = e
    data.labelName = name
  }
  const getKey = (row, key) => {
    let formKey = ''
    listData.value.map((item) => {
      item.data.map((val) => {
        if (key) {
          if (val.key === row) {
            formKey = val.key
          }
        }
        if (val.type === row) {
          formKey = val.key
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
  defineExpose({
    submit,
  })
</script>
<style lang="scss" scoped>
  .height-350 {
    height: 600rpx;
  }

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
      font-size: 30rpx;
      color: $uni-text-color;
      line-height: 1;
      font-family: PingFang SC-常规体, PingFang SC;

      .iconfont {
        margin-top: 8rpx;
        margin-left: 5rpx;
        color: #ff2529;
      }
    }

    .list-item {
      background-color: #fff;
      border-radius: 12rpx;
      width: 100%;
      padding-left: 24rpx;
      margin-bottom: 20rpx;
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

      .iconfont {
        padding-right: 16rpx;
        margin-top: 7rpx;
        transform: rotate(180deg);
        font-size: 24rpx;
        color: #c0c4cc;
      }
    }

    .picker-input-placeholder {
      color: #c0c4cc;
      font-size: 30rpx;
    }

    ::v-deep .uni-forms-item {
      margin-bottom: 0;
      border-bottom: 1px solid #ebeef5;
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
        max-width: 168rpx;
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

  ::v-deep .uni-textarea-textarea {
    font-size: 30rpx !important;
  }

  // 上传附件
  .flie {
    padding: 24rpx 24rpx 24rpx 0;

    .box {
      width: 100%;
      height: 40px;
      background: #f6f7f9;
      border-radius: 4px 4px 4px 4px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12rpx;
      padding-right: 20rpx;

      .icon-guanbi-yangshiyi1 {
        color: #999999;
      }



      .left {
        width: 100%;
        display: flex;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;

        .slot-image {
          // display: inline-block;
          flex-shrink: 0; // flex布局下图片挤压变形
          width: 52rpx;
          height: 52rpx;
          margin-right: 10rpx;
        }

        .name {
          width: calc(100% - 40px);
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          font-size: 24rpx;
          color: #303133;
        }

        .size {
          font-size: 20rpx;
          color: #909399;
          margin-top: 2rpx;
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
      }

      .delete {
        position: absolute;
        top: 0;
        right: 20rpx;
        width: 32rpx;
        height: 32rpx;
        background: rgba(0, 0, 0, 0.6);
        border-radius: 0 8rpx 0 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;

        .icon-paizhao {
          font-size: 35rpx;
          color: #bfbfbf;
        }
      }
    }
  }
</style>