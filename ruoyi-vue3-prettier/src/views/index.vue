<template>
  <div class="home">
    <el-card>
      <template #title>
        <span>关于</span>
      </template>
      <div>
        一直想重构若依后台管理系统的前端代码，于是利用下班时间进行重构，如此有了若依前后端分离版本的重构版，此系统会陆续跟着若依的版本进行更新，非常感谢BuildAdmin给的ui界面！！
      </div>
    </el-card>
    <el-card>
      <template #title>
        <span>技术栈</span>
      </template>
      <TextLink :text-arrs="technologyStacks" />
      <div class="description">
        <el-descriptions title="npm依赖" :column="2" border>
          <template v-for="(itemData, index) in dependencies" :key="index">
            <el-descriptions-item>
              <template #label> {{ itemData.name }} </template>
              <el-tag size="small">{{ itemData.description }}</el-tag>
            </el-descriptions-item>
          </template>
        </el-descriptions>
      </div>
    </el-card>
    <el-card>
      <template #title>项目规范</template>
      <el-descriptions title="文件命名规范" :column="1" border>
        <template v-for="(itemData, index) in fileNameInfo" :key="index">
          <el-descriptions-item>
            <template #label> {{ itemData.name }} </template>
            <el-tag size="small">{{ itemData.description }}</el-tag>
          </el-descriptions-item>
        </template>
      </el-descriptions>
      <el-descriptions class="mt20" title="组件命名规范" :column="1" border>
        <template v-for="(itemData, index) in componentName" :key="index">
          <el-descriptions-item>
            <template #label> {{ itemData.name }} </template>
            <el-tag size="small">{{ itemData.description }}</el-tag>
          </el-descriptions-item>
        </template>
      </el-descriptions>
    </el-card>
    <el-card>
      <template #title>
        <span>Git提交规范</span>
      </template>
      <el-descriptions title="文件命名规范" :column="1" border>
        <template v-for="(itemData, index) in gitInfo" :key="index">
          <el-descriptions-item>
            <template #label> {{ itemData.name }} </template>
            <el-tag size="small">{{ itemData.description }}</el-tag>
          </el-descriptions-item>
        </template>
      </el-descriptions>
    </el-card>
    <el-card>
      <template #title>
        <span>项目结构</span>
      </template>
      <CodeInfo language="bash" :code="projectDir"></CodeInfo>
    </el-card>
  </div>
</template>

<script setup name="Index">
import TextLink from '@/BaseComponent/TextLink'
import CodeInfo from '@/BaseComponent/Code'
import { technologyStacks, dependencies, projectDir } from './config'
const fileNameInfo = [
  { name: '文件夹', description: '统一小写，多单词用驼峰' },
  {
    name: '文件(.js .vue .json)',
    description: '统一小写，多单词用驼峰',
  },
]
const componentName = [
  { name: '组件的文件', description: '统一大驼峰' },
  {
    name: '组件的目录结构',
    description: '例如 table table/src/index.vue, 统一在table/index.js导出',
  },
  {
    name: '组件导包顺序',
    description:
      '导vue技术栈的包 , 导第三方的工具函数 , 导本地的组件, 导本地的工具函数',
  },
  { name: '组件的名称', description: '统一大驼峰' },
  {
    name: 'template标签',
    description: '大写 ( 例如：<BaseForm/> )',
  },
  {
    name: 'template标签属性顺序',
    description: 'v-if , v-for , ref, class, style, ... ,事件',
  },
  {
    name: '组件的props',
    description: '大驼峰，必须编写类型',
  },
]
const gitInfo = [
  { name: 'add 操作', description: 'git add .' },
  { name: 'commit 操作', description: 'npm run commit ' },
  { name: 'pull 操作', description: 'git pull ' },
  { name: 'push 操作', description: 'git push ' },
  {
    name: 'git地址',
    description: 'https://gitee.com/codelm/ruoyi-vue3-lmw',
  },
]
</script>

<style scoped lang="scss">
.home {
  :deep(.el-card) {
    margin-bottom: 15px;
  }
  margin: var(--ba-main-space);
}

.description {
  margin-bottom: 20px;
}
</style>
