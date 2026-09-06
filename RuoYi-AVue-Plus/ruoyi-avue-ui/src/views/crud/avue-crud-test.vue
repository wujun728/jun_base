<template>
  <div class="basic-container">
    <!--    <el-card class="basic-container__card">-->
    <h3>个人-服务注册管理</h3>
    <avue-crud :option="option"
               v-bind="bindVal"
               v-on="onEvent"
               v-model="form"
               :page.sync="page"
               :data="data">
    </avue-crud>
    <!--    </el-card>-->
  </div>
</template>

<script>
// 后续做权限控制
// import hasPermi from './permission/hasPermi'

// todo 如果要测试请先去mixins/crud.js中将
//           'on-load': this.getList,  //注释掉
export default window.$crudCommon({
  beforeCreate() {
    // let _self = this;
    // console.log("页面初始化");
    // let query = _self.$route.query;
    // getServerObjectApi(query.server).then((res) => {
    //   debugger;
    //   renderData(_self, _self.$route.query, res.data)
    // });
  },
  data() {
    return {
      "config": {
        "save": "/liukx/update",
        "update": "/liukx/update",
        "list": "/liukx/list",
        "domain": "http://127.0.0.1:8765"
      },
      "data": [
        {
          "id": "0",
          "username": "测试数据_0",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 0,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "1",
          "username": "测试数据_1",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 2,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "2",
          "username": "测试数据_2",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 4,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "3",
          "username": "测试数据_3",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 6,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "4",
          "username": "测试数据_4",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 8,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "5",
          "username": "测试数据_5",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 10,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "6",
          "username": "测试数据_6",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 12,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "7",
          "username": "测试数据_7",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 14,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "8",
          "username": "测试数据_8",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 16,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        },
        {
          "id": "9",
          "username": "测试数据_9",
          "checkStatus": "BASKETBALL",
          "likeStar": "1",
          "age": 18,
          "sex": "MAN",
          "validDate": "2021-08-20T02:27:13.811+0000",
          "interest": [
            "BADMINTON",
            "BASKETBALL"
          ],
          "status": 1
        }
      ],
      "form": {},
      "params": {},
      "api": {},
      "loading": false,
      "page": {
        "pageSize": 10,
        "total": 100
      }

    };
  }, methods: {
    commitUpdate(row, index, done, loading) {
      console.log("提交更新", row);
      // setTimeout(()=>{
      //   loading();
      //   this.$message.success('3秒后关闭表单')
      // },5000)
      this.$message.success('修改成功')
      // done();
    },
    eventDel(row, index) {
    },

    //列表后操作方法
    listAfter() {
    },

    //新增前操作方法
    addBefore() {
      this.form.createUser = 'small'
      debugger;
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
    }
  }
}, {
  name: 'crud/crud',//模块名字
  list: 'list',//列表接口名字
  update: 'update',//更新接口名字
  add: 'add',//新增接口名字
  del: 'del',//删除接口名字
  rowKey: 'id',//主键
  pageNumber: 'pageNumber',//页码
  pageSize: 'pageSize',//页数
  res: (data) => { // 这里设置函数处理
    // 应对服务接口数据结构不一致的转换
    // let response = {
    //   data: data.data,
    //   total: data.total
    // }
    return data;
  }
});
</script>

<style lang="scss">
</style>
