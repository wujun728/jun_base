<!-- 系统权限管理组件 -->
<template>
  <div style="background: #fff">
    <!-- 顶部选项卡和搜索框 -->
    <div class="tab-box">
      <el-tabs v-model="activeName" @tab-click="handleClickFn" style="width: 100%">
        <el-tab-pane label="web端" name="first"></el-tab-pane>
        <el-tab-pane label="移动端" name="second"></el-tab-pane>
      </el-tabs>
      <el-input
        v-model="value"
        style="width: 250px"
        clearable
        class="mr10"
        size="small"
        suffix-icon="el-icon-search"
        placeholder="输入权限名称"
      ></el-input>
    </div>

    <!-- 权限列表展示区域 -->
    <div class="list-wrapper">
      <div
        class="list-box"
        :class="{ on: curIndex == index }"
        v-for="(item, index) in searchList"
        :key="index"
        @click="handelclick(item, index)"
      >
        <p class="name">{{ item.name }}</p>
        <p class="fa">{{ item.method }}</p>
        <p class="des">{{ item.uri }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { menuNotSaveApi } from '@/api/system'

export default {
  name: 'SystemAuth',
  data() {
    return {
      value: '',          // 搜索框输入值
      curIndex: null,     // 当前选中项的索引
      activeName: 'first', // 当前激活的tab
      list: [],          // 当前显示的权限列表
      webList: [],       // web端权限列表
      mobileList: []     // 移动端权限列表
    }
  },
  created() {
    this.getList()
  },
  computed: {
    // 根据搜索条件过滤的权限列表
    searchList() {
      if (this.value) {
        return this.list.filter(item => 
          String(item.name).includes(this.value)
        )
      }
      return this.list
    }
  },
  methods: {
    // 获取权限列表数据
    getList() {
      menuNotSaveApi().then((res) => {
        this.webList = res.data.ent || []
        this.mobileList = res.data.uni || []
        this.list = this.webList
      }).catch(error => {
        console.error('获取权限列表失败:', error)
      })
    },
    // 切换tab
    handleClickFn() {
      if (this.activeName == 'first') {
        this.list = this.webList
      } else {
        this.list = this.mobileList
      }
    },
    handelclick(item, index) {
      this.curIndex = index
      fApi.setValue({
        menu_name: item.name,
        methods: item.method,
        api: item.uri
      })
      form_create_helper.close('menu_name')
    }
  }
}
</script>

<style lang="scss" scoped>
.list-wrapper {
  padding-top: 50px;
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}
.tab-box {
  display: flex;
  width: 100%;
  background: #fff;
  position: fixed;
  z-index: 88;
  border-bottom: 1px solid #eeeeee;
}

.list-box {
  position: relative;
  border-radius: 4px;
  width: 24%;
  padding: 10px;
  margin: 5px 1% 5px 0;
  box-sizing: border-box;
  color: #fff;
  font-size: 14px;
  background: rgb(64, 158, 255);
  p {
    margin: 7px 0;
  }
  .name {
    font-size: 16px;
  }
  &:nth-child(4n) {
    margin-right: 0;
  }
  .des {
    word-wrap: break-word;
  }
  &.on {
    background: rgb(230, 162, 60);
  }
}

</style>
