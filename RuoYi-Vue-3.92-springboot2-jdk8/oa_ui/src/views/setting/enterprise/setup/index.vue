<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card :body-style="{ padding: 0 }">
        <el-tabs v-model="activeName" class="cr-header-tabs" @tab-click="handleClick">
          <el-tab-pane
            v-for="item in tabData"
            :key="'id' + item.key"
            :label="item.label"
            :name="String(item.key)"
          ></el-tab-pane>
        </el-tabs>
        <div class="content">
          <cloudfile :rule="rule" v-if="activeName === 'wps_config'" />
          <oneNumber v-else-if="activeName === 'yiht_config'" :fromData="rule" />
          <storage v-else-if="activeName === 'storage_config'" />
          <div v-else>
            <el-scrollbar style="height: calc(100vh - 190px)">
              <form-create v-if="fromData" :option="option" :rule="fromData.rule" @submit="onSubmit" />
            </el-scrollbar>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import cloudfile from './components/cloudfile'
import oneNumber from './components/oneNumber'
import storage from './components/storage.vue'
import formCreate from '@form-create/element-ui'
import request from '@/api/request'
import { configCateApi, configUpdateDataApi } from '@/api/setting'
import { site } from '@/api/user'
import { AiEmbeddedManage } from "@/libs/ai-embedded"

export default {
  name: 'CompanyInfo',
  components: {
    cloudfile,
    oneNumber,
    storage,
    formCreate: formCreate.$form()
  },
  data() {
    return {
      activeName: 0,
      fromData: null,
      rule: [],
      tabData: [],
      option: {
        form: {
          labelWidth: '150px',
          labelSuffix: '：'
        },
        global: {
          upload: {
            props: {
              onSuccess(rep, file) {
                if (rep.status === 200) {
                  file.url = rep.data.src
                }
              }
            }
          }
        }
      }
    }
  },
  mounted() {
    this.getConfigCate()
  },
  destroyed() {},
  methods: {
    getSide() {
      site().then((res) => {
        localStorage.setItem('sitedata', JSON.stringify(res.data))
      })
    },
    async getConfigCate() {
      const res = await configCateApi()
      this.tabData = res.data
      this.activeName = res.data[0].key
      this.handleClick()
    },
    handleClick() {
      if (this.activeName) {
        this.getConfigData({ category: this.activeName })
      }
    },
    getConfigData(data) {
      configUpdateDataApi(data).then((res) => {
        this.fromData = res.data
        this.rule = res.data.rule
      })
    },
    onSubmit(formData) {
      request[this.fromData.method.toLowerCase()](this.fromData.action, formData)
        .then(res => {
          const aiEmbedded = AiEmbeddedManage.getAiEmbedded();
          if (formData.ai_status === 1) {

            if (!aiEmbedded) {
              AiEmbeddedManage
                .getAiEmbedded()
                .init(this.$store.getters.token);
            } else if (!aiEmbedded?._inited) {
              aiEmbedded.init(this.$store.getters.token);
            }
          } else {
            aiEmbedded?.destroy();
          }
        });
      localStorage.setItem('isWebConfig', formData.global_watermark_status)
      setTimeout(() => {
        this.getSide()
      }, 3000)
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
.cr-header-tabs {
  // border-bottom: 1px solid #EEEEEE;
  // margin-bottom: 20px;
  // padding-left: 20px;
}
::v-deep.el-tabs__header {
  border-bottom: 1px solid #eeeeee;
}
::v-deep.el-tabs__nav {
  margin-left: 20px;
}
::v-deep.el-tabs__item {
  height: 60px;
  line-height: 60px;
}
.content {
  padding: 20px;
}
</style>
