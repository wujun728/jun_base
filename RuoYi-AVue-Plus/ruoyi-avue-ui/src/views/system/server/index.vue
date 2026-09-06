<template>
  <div class="basic-container">
    <!--    <el-card class="basic-container__card">-->
    <h3>个人-服务注册管理</h3>
    <avue-crud v-bind="bindVal"
               v-on="onEvent"
               v-model="form"
               :page.sync="page">
    </avue-crud>
    <!--    </el-card>-->
  </div>
</template>

<script>

export default window.$crudCommon({

  beforeCreate() {
    console.log("页面初始化");
  },
  data() {
    return {
      // page: {
      //   pageSize: 1,
      //   pagerCount: 5,
      //   // total: 20
      //   // currentPage: 2
      //   // pageSizes: [1, 3, 5, 7, 9]
      // },
      // data: menu_const.serverListData
    };
  }, methods: {
    //列表前操作方法
    listBefore() {
    },

    //列表后操作方法
    listAfter() {
    },

    //新增前操作方法
    addBefore() {
      this.form.createUser = 'small'
    },
    //新增后操作方法
    addAfter() {
    },

    //修改前操作方法
    updateBefore() {
      this.form.updateUser = 'small'
    },

    //修改后操作方法
    updateAfter() {
    },

    //删除前操作方法
    delBefore() {
    },

    //删除后操作方法
    delAfter() {
    },
  }
}, {
  name: 'system/server',//模块名字
  list: 'listServer',//列表接口名字
  update: 'updateServer',//更新接口名字
  add: 'addServer',//新增接口名字
  del: 'delServer',//删除接口名字
  rowKey: 'id',//主键
  pageNumber: 'pageNumber',//页码
  pageSize: 'pageSize',//页数
  res: (data) => {
    return data;
  },//列表的结构
  total: 'total',//总页数
  data: 'rows'//列表属性
})
</script>

<style lang="scss">
</style>
