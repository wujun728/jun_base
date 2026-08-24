<template>
  <div class="station">
    <el-drawer
      :title="id > 0 ? '编辑接口' : '新增接口'"
      :visible.sync="drawer"
      direction="rtl"
      :modal="true"
      :wrapperClosable="false"
      :before-close="handleClose"
      size="800px"
    >
      <div class="form-box">
        <el-form :model="form" :rules="rules" ref="ruleForm" label-width="110px">
          <el-form-item label="接口标题：" prop="title">
            <el-input v-model="form.title" size="small" placeholder="请输入接口标题"></el-input>
          </el-form-item>
          <el-form-item label="授权请求：" prop="is_pre">
            <el-switch
              v-model="form.is_pre"
              :active-value="`1`"
              :inactive-value="`0`"
              active-text="开启"
              inactive-text="关闭"
            >
            </el-switch>
          </el-form-item>
          <template v-if="form.is_pre == '1'">
            <el-form-item label="授权链接：" prop="pre_url">
              <el-input v-model="form.pre_url" size="small" placeholder="请输入前置链接"></el-input>
            </el-form-item>
            <el-form-item label="请求方式：" prop="pre_method">
              <el-select v-model="form.pre_method" size="small" style="width: 100%">
                <el-option
                  v-for="item in requestMethod"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <!-- 请求头 -->
            <el-form-item label="授权Headers：">
              <div class="table-box">
                <el-table :data="form.pre_headers" style="width: 100%">
                  <el-table-column prop="name" label="参数名">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.name" size="small" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="value" label="参数值">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.value" size="small" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column width="40px">
                    <template slot-scope="scope">
                      <span class="iconfont iconshanchu pointer" @click="deleteFn('pre_headers', scope.$index)"></span>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="addBtn">
                  <el-button type="text" icon="el-icon-plus" @click="addFn('pre_headers')">添加行</el-button>
                </div>
              </div>
            </el-form-item>
            <!-- 请求体 -->
            <el-form-item label="授权form-data：">
              <div class="table-box">
                <el-table :data="form.pre_data" style="width: 100%">
                  <el-table-column prop="name" label="参数名">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.name" size="small" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column prop="value" label="参数值">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.value" size="small" placeholder="请输入"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column width="40px">
                    <template slot-scope="scope">
                      <span class="iconfont iconshanchu pointer" @click="deleteFn('pre_data', scope.$index)"></span>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="addBtn">
                  <el-button type="text" icon="el-icon-plus" @click="addFn('pre_data')">添加行</el-button>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="缓存时间：">
              <el-input v-model="form.pre_cache_time" size="small" placeholder="请输入缓存时间" type="number">
                <span slot="suffix" class="text-16">秒</span>
              </el-input>
              <el-button type="primary" size="small" class="mt14" @click="requestFn">测试请求</el-button>
            </el-form-item>
          </template>

          <el-form-item label="链接地址：" prop="url">
            <el-input v-model="form.url" size="small" placeholder="请输入链接地址"></el-input>
          </el-form-item>
          <el-form-item label="请求方式：" prop="method">
            <el-select v-model="form.method" size="small" style="width: 100%">
              <el-option
                v-for="item in requestMethod"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <!-- 请求头 -->
          <el-form-item label="Headers：">
            <div class="table-box">
              <el-table :data="form.headers" style="width: 100%">
                <el-table-column prop="name" label="参数名">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.name" size="small" placeholder="请输入"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="参数来源" v-if="form.is_pre == 1">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.type" size="small" placeholder="请选择">
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="前缀">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.prefix" size="small" placeholder="请输入"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="value" label="参数值">
                  <template slot-scope="scope">
                    <el-select v-if="scope.row.type == '1'" v-model="scope.row.value" size="small" placeholder="请选择">
                      <el-option v-for="item in fieldValue" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                    <el-input v-else v-model="scope.row.value" size="small" placeholder="请输入"></el-input>
                  </template>
                </el-table-column>
                <el-table-column width="40px">
                  <template slot-scope="scope">
                    <span class="iconfont iconshanchu pointer" @click="deleteFn('headers', scope.$index)"></span>
                  </template>
                </el-table-column>
              </el-table>
              <div class="addBtn">
                <el-button type="text" icon="el-icon-plus" @click="addFn('headers')">添加行</el-button>
              </div>
            </div>
          </el-form-item>
          <!-- 请求体 -->
          <el-form-item label="form-data：">
            <div class="table-box">
              <el-table :data="form.data" style="width: 100%">
                <el-table-column prop="name" label="参数名">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.name" size="small" placeholder="请输入"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="参数来源" v-if="form.is_pre == 1">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.type" size="small" placeholder="请选择" @change="changeFn(scope.row)">
                      <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                        :disabled="isDisabled(item) && item.value == 2"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="value" label="参数值">
                  <template slot-scope="scope">
                    <el-select v-if="scope.row.type == '1'" v-model="scope.row.value" size="small" placeholder="请选择">
                      <el-option v-for="item in fieldValue" :key="item.value" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                    <el-input v-else-if="0" v-model="scope.row.value" size="small" placeholder="请输入"></el-input>
                    <el-input
                      v-else-if="2"
                      v-model="scope.row.value"
                      type="number"
                      size="small"
                      placeholder="请输入数字"
                    ></el-input>
                  </template>
                </el-table-column>
                <el-table-column width="40px">
                  <template slot-scope="scope">
                    <span class="iconfont iconshanchu pointer" @click="deleteFn('data', scope.$index)"></span>
                  </template>
                </el-table-column>
              </el-table>
              <div class="addBtn">
                <el-button type="text" icon="el-icon-plus" @click="addFn('data')">添加行</el-button>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button :loading="loading" size="small" type="primary" @click="handleConfirm('ruleForm')">{{
          $t('public.ok')
        }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { crudTestSendApi, crudAddCurlApi, crudGetCurlEditApi, crudPutCurlApi } from '@/api/develop'
export default {
  name: '',
  props: {},
  data() {
    return {
      drawer: false,
      loading: false,
      id: 0,

      selectedList: [],
      form: {
        title: '',
        url: '',
        is_pre: '1',
        method: 'get',
        headers: [{ name: '', type: '0', value: '' }],
        data: [{ name: '', type: '0', value: '' }],
        pre_method: 'get',
        pre_url: '',
        pre_headers: [{ name: '', value: '' }],
        pre_data: [{ name: '', value: '' }],
        pre_cache_time: ''
      },
      requestMethod: [
        {
          value: 'get',
          label: 'get'
        },
        {
          value: 'post',
          label: 'post'
        },
        {
          value: 'put',
          label: 'put'
        },
        {
          value: 'delete',
          label: 'delete'
        }
      ],
      rules: {
        title: [{ required: true, message: '请输入接口标题', trigger: 'blur' }],
        is_pre: [{ required: true, message: '请选择请求类型', trigger: 'blur' }],
        url: [{ required: true, message: '请输入链接地址', trigger: 'blur' }],
        method: [{ required: true, message: '请选择请求方式', trigger: 'blur' }],
        pre_method: [{ required: true, message: '请选择请求方式', trigger: 'blur' }],
        pre_url: [{ required: true, message: '请输入地址', trigger: 'blur' }]
      },
      fieldValue: [],
      options: [
        {
          value: '0',
          label: '固定值'
        },
        {
          value: '1',
          label: '响应值'
        },
        {
          value: '2',
          label: '自增值'
        }
      ]
    }
  },

  methods: {
    openBox(id) {
      if (id) {
        this.id = id
        this.getInfo(id)
      }
      this.drawer = true
    },
    addFn(val, type) {
      if (type === 'pre') {
        this.form[val].push({ name: '', type: '0', value: '' })
      } else {
        this.form[val].push({ name: '', value: '' })
      }
    },
    changeFn(data) {
   
    },
    // 判断是否禁用
    isDisabled(item) {
      const list = this.form.data.map((e) => e.type)

      // 判断下拉项是否再列表里，存在则禁用
      return list.includes(item.value)
    },
    // 测试请求
    requestFn() {
      let data = {
        url: this.form.pre_url,
        method: this.form.pre_method,
        headers: this.form.pre_headers,
        data: this.form.pre_data
      }
      this.fieldValue = []
      crudTestSendApi(data).then((res) => {
        if (res.status == 200) {
          let key = res.data.key
          key.map((item) => {
            let obj = {
              label: item,
              value: item
            }
            this.fieldValue.push(obj)
          })
        }
      })
    },

    deleteFn(val, index) {
      this.form[val].splice(index, 1)
    },

    getInfo(id) {
      crudGetCurlEditApi(id).then((res) => {
        for (let key in res.data) {
          this.form[key] = res.data[key]
        }
        this.form.is_pre = this.form.is_pre + ''
      })
    },

    handleConfirm() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.id > 0) {
            crudPutCurlApi(this.id, this.form).then((res) => {
              if (res.status == 200) {
                this.handleClose()
                this.$emit('getList')
              }
              this.loading = false
            })
          } else {
            crudAddCurlApi(this.form).then((res) => {
              if (res.status == 200) {
                this.handleClose()
                this.$emit('getList')
              }
              this.loading = false
            })
          }
        }
      })
    },
    handleClose() {
      this.$refs.ruleForm.resetFields()
      this.id = 0
      this.drawer = false
      this.form.headers = [{ name: '', type: '0', value: '' }]
      this.form.data = [{ name: '', type: '0', value: '' }]
      this.form.pre_headers = [{ name: '', value: '' }]
      this.form.pre_data = [{ name: '', value: '' }]
    }
  }
}
</script>
<style scoped lang="scss">
.form-box {
  padding: 20px;
  padding-bottom: 50px;
}
</style>
