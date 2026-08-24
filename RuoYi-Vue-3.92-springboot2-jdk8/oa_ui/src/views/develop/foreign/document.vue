<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '0px' }">
      <el-row>
        <el-col v-bind="gridl">
          <tree :list="list" @getJson="getJson" />
        </el-col>
        <el-col v-bind="gridr" class="boder-left">
          <div class="title-16">{{ parentName }}-{{ jsonData.name }}</div>

          <!-- 1 -->
          <div class="mt20">
            <div class="name">1.新增接口调用</div>
            <div class="bg-grey mt10 flex-center">
              {{ jsonData.method }} {{ host }}{{ jsonData.url }}
              <span class="iconfont iconfuzhi-01" @click="copy(host + jsonData.url)"></span>
            </div>
          </div>
          <div class="mt30">
            <div class="name">2.请求Header</div>
            <div class="mt10 table-box">
              <el-table :data="headerData" style="width: 100%" :row-class-name="tableRowNone">
                <el-table-column prop="name" label="字段名称"> </el-table-column>
                <el-table-column prop="form_type" label="字段类型"> </el-table-column>
                <!-- <el-table-column prop="default" label="默认值"> </el-table-column> -->
                <el-table-column prop="is_must" label="必填">
                  <template slot-scope="scope">
                    {{ scope.row.is_must ? '是' : '否' }}
                  </template>
                </el-table-column>
                <el-table-column prop="message" label="说明"> </el-table-column>
              </el-table>
            </div>
          </div>
          <div class="mt30">
            <div class="name">3.请求参数</div>
            <div class="mt10 table-box">
              <el-table v-if="jsonData.path_prams" :data="jsonData.path_prams" style="width: 100%">
                <el-table-column prop="name" label="字段名称"> </el-table-column>
                <el-table-column prop="form_type" label="字段类型"> </el-table-column>
                <el-table-column prop="is_must" label="必填">
                  <template slot-scope="scope">
                    {{ scope.row.is_must ? '是' : '否' }}
                  </template>
                </el-table-column>
                <el-table-column prop="message" label="说明"> </el-table-column>
              </el-table>
            </div>
            <div class="mt10 table-box">
              <el-table v-if="jsonData.get_prams" :data="jsonData.get_prams" style="width: 100%">
                <el-table-column prop="name" label="字段名称"> </el-table-column>
                <el-table-column prop="form_type" label="字段类型"> </el-table-column>
                <!-- <el-table-column prop="default" label="默认值"> </el-table-column> -->
                <el-table-column prop="is_must" label="必填">
                  <template slot-scope="scope">
                    {{ scope.row.is_must ? '是' : '否' }}
                  </template>
                </el-table-column>
                <el-table-column prop="message" label="说明"> </el-table-column>
              </el-table>
            </div>
            <div class="mt10 table-box">
              <el-table v-if="jsonData.post_prams" :data="jsonData.post_prams" style="width: 100%">
                <el-table-column prop="name" label="字段名称"> </el-table-column>
                <el-table-column prop="form_type" label="字段类型"> </el-table-column>

                <el-table-column prop="is_must" label="必填">
                  <template slot-scope="scope">
                    {{ scope.row.is_must ? '是' : '否' }}
                  </template>
                </el-table-column>
                <el-table-column prop="message" label="说明"> </el-table-column>
              </el-table>
            </div>
          </div>
          <div class="mt30">
            <div class="name">4.返回参数</div>
            <div class="mt10 table-box">
              <el-table
                :data="jsonData.response_data"
                row-key="name"
                :tree-props="{ children: 'children' }"
                default-expand-all
                style="width: 100%"
              >
                <el-table-column prop="name" label="字段名称"> </el-table-column>
                <el-table-column prop="form_type" label="字段类型"> </el-table-column>
                <el-table-column prop="message" label="说明"> </el-table-column>
              </el-table>
            </div>
          </div>
          <div class="mt30">
            <div class="name">5.请求数据示例</div>
            <div class="mt10">
              <json-viewer :value="jsonData.request_json" :expand-depth="8" copyable></json-viewer>
            </div>
          </div>
          <div class="mt30">
            <div class="name">6.返回数据示例</div>
            <div class="mt10">
              <json-viewer
                style="height: 300px; width: 100%"
                :value="jsonData.response_json"
                :expand-depth="8"
                copyable
              ></json-viewer>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import { getDocsApi } from '@/api/develop'
import JsonViewer from 'vue-json-viewer'
export default {
  name: '',
  components: {
    tree: () => import('./components/tree'),
    JsonViewer
  },
  props: {},
  data() {
    return {
      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      tableData: [],
      headerData: [
        {
          name: 'Content-Type',
          form_type: 'string',
          is_must: false,
          message: 'application/json; charset=utf-8'
        },
        {
          name: 'Authorization',
          form_type: 'string',
          is_must: true,
          message: 'Bearer <授权登录token>'
        }
      ],
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      },
      list: [],
      host: '',
      jsonData: {},
      parentName: ''
    }
  },

  mounted() {
    this.getData()
    this.host = location.origin + '/'
  },
  methods: {
    tableRowNone({ row, rowIndex }) {
      if (this.jsonData.url === 'open/auth/login' && rowIndex === 1) {
        return 'warning-row'
      } else {
        return ''
      }
    },
    copy(val) {
      clipboard.writeText(val)
      this.$message.success('复制成功')
    },
    async getData() {
      const res = await getDocsApi()
      this.list = res.data
      this.parentName = res.data[0].name
      this.jsonData = res.data[0].children[0]
    },
    getJson(data, name) {
      this.parentName = name
      this.jsonData = data
    }
  }
}
</script>
<style scoped lang="scss">
.boder-left {
  height: calc(100vh - 80px);
  border-left: 1px solid #eeeeee;
  padding: 20px;
  overflow: auto;
}
.bg-grey {
  background: #eeeeee;
  padding: 20px;
  border-radius: 4px;
  .iconfuzhi-01 {
    cursor: pointer;
    color: #1890ff;
    margin-left: 10px;
    font-size: 14px;
  }
}
::v-deep .el-table .warning-row {
  display: none;
}
::v-deep.jv-container.jv-light {
  background: #eeeeee;
}
</style>
