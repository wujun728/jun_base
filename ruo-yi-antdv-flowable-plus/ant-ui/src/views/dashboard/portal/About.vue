<template>
  <div class="typical-home" ref="portaletDiv">
    <a-row :gutter="[24,24]" class="module-list">
      <!-- 待办事项 -->
      <a-col :span="24" >
        <div class="module-in module-in01 modlule-show" style="height: 260px;">
          <a-page-header :ghost="false" title="平台简介">
            <template slot="extra">
              <a-icon type="more" />
            </template>
          </a-page-header>
          <p>
            <a-alert message="免费开源【欢迎star和fork项目,请点击下方按钮访问】" type="info" show-icon />
          </p>
          <p style="text-indent: 30px;">
            基于开源项目“ AiDex Sharp 快速开发平台 ”改造而成，集成flowable、mybatis-plus 。
          </p>
          <p>
            当前版本：spring-boot2.5.5、flowable6.5、mybatis-plus3.4.3
          </p>

          <p>
            <a-row :gutter="[24,24]">
              <a-col :span="8">
                <a-button block style="height: 40px;" type="primary" @click="goTarget('https://gitee.com/lwq8886666/ruo-yi-antdv-flowable-plus')">
                  访问ruo-yi-antdv-flowable-plus
                </a-button>
              </a-col>
              <a-col :span="8">
                <a-button block style="height: 40px;" type="primary" ghost @click="goTarget('https://gitee.com/y_project/RuoYi-Vue')">
                  访问若依
                </a-button>
              </a-col>
              <a-col :span="8">
                <a-button block style="height: 40px;" type="primary" ghost @click="caseDisplay('')">
                  案例
                </a-button>
              </a-col>
            </a-row>
          </p>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
  export default {
    data () {
    },
    computed: {
    },
    created () {
    },
    methods: {
      goTarget (url) {
        window.open(url)
      },
      caseDisplay (url) {
        this.$message.success(
          '案例正在研发中，目前尚未实现，敬请期待！',
          3
         )
      }
    }
  }
</script>
