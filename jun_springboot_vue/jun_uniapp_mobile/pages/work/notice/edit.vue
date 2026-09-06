<template>
  <view class="mobile-item-container">
    <Navbar :title="noticeId ? '修改公告' : '新增公告'" bgColor="#fff" :h5Show="false"></Navbar>
    <u--form ref="noticeForm" :model="notice" :rules="rules" labelPosition="left">
      <u-form-item label="标题" prop="noticeTitle" borderBottom>
        <u--textarea v-model="notice.noticeTitle" placeholder="请输入标题" :count="false" :maxlength="30" :autoHeight="true" confirmType="done"></u--textarea>
      </u-form-item>
      <u-form-item label="类型" prop="noticeType" borderBottom @click="actionShow = true;">
        <u--input v-model="noticeTypeName" disabled disabledColor="#ffffff" placeholder="请选择类型" border="none"></u--input>
				<u-icon slot="right" name="arrow-right"></u-icon>
      </u-form-item>
      <u-form-item label="状态" prop="status" borderBottom>
        <u-radio-group v-model="notice.status">
          <u-radio shape="circle" label="正常" name="0" checked></u-radio>
          <u-radio shape="circle" label="关闭" name="1"></u-radio>
        </u-radio-group>
      </u-form-item>
      <u-form-item label="正文" prop="noticeContent">
        <u--textarea v-model="notice.noticeContent" placeholder="请输入标题" :count="true" :maxlength="600" confirmType="done"></u--textarea>
      </u-form-item>
    </u--form>
    <u-action-sheet :actions="actions" :title="actionTitle" :show="actionShow" @close="actionShow = false" @select="actionSelect"></u-action-sheet>
    <u-row :gutter="16" style="margin-top: 36px;">
      <u-col :span="6">
        <u-button v-if="noticeId" type="error" text="删除" @click="del"></u-button>
        <u-button v-else icon="arrow-left" text="返回" plain @click="goBack()"></u-button>
      </u-col>
      <u-col :span="6">
        <u-button type="primary" text="提交" @click="submit"></u-button>
      </u-col>
    </u-row>
  </view>
</template>

<script>
import * as NoticeApi from '@/api/work/notice'
import Navbar from '@/components/navbar/Navbar'

export default {
  components: {
    Navbar,
  },
  data () {
    return {
      noticeId: undefined,
      notice: {
        noticeTitle: '',
        status: '0',
        noticeContent: ''
      },
      actionShow: false,
      actions: [{
        name: '通知',
        value: '1'
      }, {
        name: '公告',
        value: '2'
      }],
      actionTitle: '',
      noticeTypeName: null,
      rules: {
        noticeTitle: [ { required: true, message: '请输入公告标题', trigger: ['blur', 'change'] } ],
        noticeType: [ { required: true, message: '请选择公告类型', trigger: ['blur', 'change'] } ],
        status: [ { required: true, message: '请选择公告状态', trigger: ['blur', 'change'] } ],
        noticeContent: [ { required: true, message: '请输入公告正文', trigger: ['blur', 'change'] } ],
      }
    }
  },
  onLoad (params) {
    this.noticeId = params.id
    this.loadData()
  },
  methods: {
    loadData () {
      if (this.noticeId) {
        const app = this
        NoticeApi.noticeById(this.noticeId).then(res => {
          app.notice = res.data
        })
      }
    },
    del () {
      NoticeApi.noticeDelete(this.noticeId).then(res => {
        uni.showToast({ title: '保存成功！' })
      })
    },
    submit () {
      this.$refs.noticeForm.validate().then(res => {
        if (this.noticeId) {
          NoticeApi.noticeModify(this.notice).then(res => {
            uni.showToast({ title: '提交成功！' })
          })
        } else {
          NoticeApi.noticeAdd(this.notice).then(res => {
            uni.showToast({ title: '提交成功！' })
          })
        }
      });
    },
    actionSelect (item) {
      this.noticeTypeName = item.name;
      this.notice.noticeType = item.value;
      this.$refs.noticeForm.validateField('noticeType');
    },
    goBack () {
      uni.navigateBack({ delta: 1});
    }
  }
}
</script>