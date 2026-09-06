<template>
<!--  https://wangbin3162.gitee.io/bin-code-editor/#/jsonEditor -->
  <div>
    <b-code-editor v-model="jsonStr" :auto-format="true" :smart-indent="true" theme="eclipse" :indent-unit="4" :line-wrap="false" ref="editor"></b-code-editor>
    <br>
    <el-button type="primary" @click="onSubmit">提交</el-button>
  </div>
</template>


<script>
const jsonData =`{
    "employees": [{
      "firstName": "Bill",
      "lastName": "Gates"
    }, {
      "firstName": "George",
      "lastName": "Bush"
    }, {
      "firstName": "Thomas",
      "lastName": "Carter"
    }]
  }`

export default {
  name: "vue-json-test",
  data() {
    return {
      jsonStr:jsonData
    }
  },
  methods: {
    // 检测json格式
    isJSON(str) {
      if (typeof str == 'string') {
        try {
          var obj=JSON.parse(str);
          if(typeof obj == 'object' && obj ){
            return true;
          }else{
            return false;
          }

        } catch(e) {
          return false;
        }
      }else if (typeof str == 'object'  && str) {
        return true;
      }
    },
    onSubmit(){
      if (!this.isJSON(this.jsonStr)){
        this.$message.error(`json格式错误`)
        return false
      }
      this.$message.success('json格式正确')
    }
  }
}
</script>

<style scoped>

</style>
