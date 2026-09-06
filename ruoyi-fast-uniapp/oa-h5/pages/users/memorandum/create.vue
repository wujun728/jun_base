<template>
	<BaseContainer>
		<view class="create-page-body">
			<view>
				<view class="status_bar"></view>
				<default-nav-bar :is-right="true" :is-show-title="data.isShowTitle"
					:default-title="data.defaultTitle"></default-nav-bar>
			</view>

			<input id="input" v-model="data.title" class="title-input" type="text" placeholder-class="input-placeholder"
				:maxlength="32" placeholder="请输入标题" @focus="handleTitleFocus" @blur="handleTitleBlur" />
			<BaseRichEditor placeholder="尽情创造你的个人笔记吧" class="custom-editor-wrap" :content="data.content" @change="saveContent"
				:toolbarVisible="toolbarVisible" @save="saveRight" v-if="isLoadedData" />
		</view>
		<global-index />
	</BaseContainer>
</template>

<script setup>
import BaseContainer from "@/components/BaseContainer/index.vue";
import BaseRichEditor from "@/components/BaseRichEditor/index.vue";
import defaultNavBar from '@/components/defaultNavBar/index'
import cEditor from '@/components/editor-common/editor.vue'
import globalIndex from '@/components/globalIndex/index.vue'
import { ref, reactive, onMounted } from "vue"
import message from '@/utils/message'
import { delayedReLaunch } from '@/utils/helper'
import { userMemorialSaveApi, userMemorialEditApi } from '@/api/user'
import { useInputFocus } from "@/components/BaseRichEditor/hooks/common";

// 标题输入框 focus 时工具栏也会被唤起，iOS 端由于是监听编辑器 foucs 后手动计算工具栏偏移距离，所有没有此问题，只有 Android 需要特殊处理
const toolbarVisible = ref(true);
const [isTitleFocus, handleTitleFocus, handleTitleBlur] = useInputFocus((status) => {
	toolbarVisible.value = !status;
});

const data = reactive({
	title: '',
	content: '',
	contents: '',
	tab: '',
	pid: '',
	isEdit: false,
	defaultTitle: '',
	isShowTitle: false,
	default: {},
	id: ''
})

import { onLoad } from '@dcloudio/uni-app'
onLoad((e) => {
	if (e.tab) {
		data.tab = e.tab
		data.pid = e.pid
		if (e.id) {
			data.id = e.id
			data.defaultTitle = '编辑笔记'
			data.isShowTitle = true
			data.isEdit = true
			getMemorialInfo(e.id)
		} else {
			data.defaultTitle = '创建笔记'
			data.isShowTitle = true
		}
	}
})

const isLoadedData = computed(() => {
	return data.id ? !!data.title : true;
})

const loading = ref(false)

import { userMemorialInfoApi } from '@/api/user'

// 获取记事本详情
const getMemorialInfo = (id) => {
	userMemorialInfoApi(id).then(res => {
		if (res.data) {
			data.default = res.data
			data.title = res.data.title
			data.content = res.data.content
			data.contents = res.data.content
		}
	}).catch((error) => {
		message.error(error.message)
	})
}

// 监听内容变化
const saveContent = (e) => {
	data.contents = e
}

const saveRight = () => {
	if (!data.title) {
		message.error('请输入记事本标题')
		return false
	}
	if (!data.contents) {
		message.error('请输入记事本内容')
		return false
	}
	const res = {
		title: data.title,
		content: data.contents,
		pid: data.pid
	}
	if (!loading.value) {
		if (data.isEdit) {
			res.pid = data.default.pid
			setMemorialEdit(data.default.id, res)
		} else {
			memorialSave(res)
		}
	}
}

// 保存记事本
const memorialSave = (datas) => {
	loading.value = true
	userMemorialSaveApi(datas).then(res => {
		message.success(res.message)
		loading.value = true
		delayedReLaunch(`/pages/users/memorandum/index?tab=${data.tab}&id=${data.pid}`)
	}).catch((error) => {
		loading.value = false
		message.error(error.message)
	})
}

// 编辑记事本移动至
const setMemorialEdit = (id, datas) => {
	loading.value = true
	userMemorialEditApi(id, datas).then(res => {
		message.success('编辑成功')
		loading.value = true
		delayedReLaunch(`/pages/users/memorandum/index?tab=${data.tab}&id=${data.pid}`)
	}).catch((error) => {
		loading.value = false
		message.error(error.message)
	})
}
</script>

<style>
page {
	background-color: #fff;
	overscroll-behavior-y: none;
}
</style>

<style scoped lang="scss">
.create-page-body {
	height: var(--full-vh);
	display: flex;
	flex-flow: column nowrap;
}

// 标题编辑栏高度
$title-input-height: 124rpx;


.title-input {
	height: $title-input-height;
	border: none;
	box-sizing: border-box;
	border-bottom: 1px solid $uni-line-style-color-three;
	font-size: 36rpx;
	font-weight: 500;
	color: $uni-text-color;
	margin: 0 30rpx;
}

:deep(.input-placeholder) {
	color: $uni-text-color-five;
}

:deep(.custom-editor-wrap) {
	padding: {
		top: 40rpx;
		left: 30rpx;
		right: 30rpx;
	}

	background-image: linear-gradient(to bottom, #fff 0%, #f6f6f6 100%);
}
</style>