<!-- @FileDescription: 公共-全局-操作日志组件 应用举例：低代码操作日志、任务操作日志-->
<template>
  <div class="">
    <!-- 任务操作日志 -->
    <el-table v-if="!type" :data="list" style="width: 100%" :row-class-name="iconHadler">
      <el-table-column prop="desc">
        <template slot-scope="scope">
          【{{ scope.row.action_type == 1 ? '创建' : '编辑' }}】{{ scope.row.operator }}
          <span style="max-width: 550px" v-html="scope.row.title" @click="previewPicture($event)"></span>
          <span class="created-time">{{ scope.row.created_at }}</span>
        </template>
      </el-table-column>
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div class="flex expand-describe">
            <div class="flex expand-left">
              <span>{{ scope.row.describe[0].title }}</span>
              <p v-html="scope.row.describe[0].value"></p>
            </div>
            <div class="line-short"></div>
            <div class="flex expand-right">
              <span>{{ scope.row.describe[1].title }}</span>
              <p v-html="scope.row.describe[1].value"></p>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!-- 低代码操作日志 -->
    <el-table v-else :data="list" style="width: 100%" :row-class-name="iconShow" class="tableBox">
      <el-table-column prop="desc">
        <template slot-scope="scope">
          <div v-if="!formValueType.includes(scope.row.form_value)" class="flex align-center">
            <span class="name-text"> 【{{ getLogType(scope.row.log_type) }}】{{ scope.row.user.name }}</span>

            <template v-if="scope.row.log_type === 'share_delete'">
              <span class="modify-text">&nbsp;删除&nbsp;协作人员</span>
              <span class="fwweight-bold">&nbsp;&nbsp;{{ scope.row.after_value }} </span>
            </template>
            <template v-else-if="scope.row.log_type === 'share_create'">
              <span class="modify-text">&nbsp;添加 &nbsp;协作人员</span>
              <span class="fwweight-bold"
                >&nbsp;&nbsp;{{ scope.row.before_value }} <span class="modify-text">&nbsp;协作权限&nbsp;</span>
                {{ scope.row.after_value }}</span
              >
            </template>
            <template v-else-if="scope.row.log_type === 'create'">
              <span class="modify-text">&nbsp;新增了一条数据&nbsp;</span>
              <!-- <span class="fwweight-bold"> {{ keyName }}</span> -->
            </template>
            <template v-else-if="scope.row.log_type === 'share_update'">
              <span class="modify-text">&nbsp;将&nbsp;协作人员</span>&nbsp;{{ scope.row.share_user.name }}&nbsp;<span
                class="modify-text"
                >的权限</span
              >
              <span class="modify-text"> &nbsp;由&nbsp;</span>
              <span class="fwweight-bold">{{ scope.row.before_value }}</span>
              <span class="modify-tex m6t">修改为</span>
              <span class="fwweight-bold">{{ scope.row.after_value }}</span>
            </template>
            <template v-else>
              <span class="modify-text">&nbsp;将</span>
              <span class="fwweight-bold">{{ scope.row.field_name || '负责人' }}</span>
              <span class="modify-text mr6">从</span>
              <span class="fwweight-bold max-width">{{ getValue(scope.row.before_value, scope.row.form_value) }}</span>
              <span class="modify-text m6">修改为</span>
              <span class="fwweight-bold max-width">{{ getValue(scope.row.after_value, scope.row.form_value) }} </span>
            </template>
            <span class="modify-text ml20">{{ scope.row.created_at }}</span>
          </div>

          <div v-if="formValueType.includes(scope.row.form_value)">
            <span class="name-text"> 【{{ getLogType(scope.row.log_type) }}】{{ scope.row.user.name }}</span>
            <span class="modify-text">修改了</span>
            <span class="fwweight-bold">{{ scope.row.field_name }}</span>
            <span class="modify-text ml20">{{ scope.row.created_at }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div class="flex expand-describe">
            <div class="flex expand-left">
              <span class="modify-text">修改前：</span>

              <!-- 图片 -->
              <template v-if="scope.row.form_value == 'image'">
                <div class="flex" v-for="(item, index) in scope.row.before_value">
                  <img :src="item.url" alt="" class="img" @click="filePreview(item)" />
                </div>
              </template>
              <div
                v-else-if="scope.row.form_value === 'rich_text'"
                class="rich_text"
                v-html="scope.row.before_value"
              ></div>
              <div v-else-if="scope.row.form_value === 'file'">
                <oaUploadList :fileList="scope.row.before_value"></oaUploadList>
              </div>
              <div v-else-if="scope.row.form_value === 'tag'">
                <el-tag size="small" class="mb5" v-for="(val, index) in scope.row.before_value" :key="index">
                  {{ val }}
                </el-tag>
              </div>
              <p v-else>{{ getValue(scope.row.before_value, scope.row.form_value) }}</p>
            </div>
            <div class="line-short"></div>
            <div class="flex expand-right">
              <span class="modify-text">修改后：</span>
              <template v-if="scope.row.form_value == 'image'">
                <div class="flex" v-for="(item, index) in scope.row.after_value">
                  <img :src="item.url" alt="" class="img" @click="filePreview(item)" />
                </div>
              </template>
              <div
                v-else-if="scope.row.form_value === 'rich_text'"
                class="rich_text"
                v-html="scope.row.after_value"
              ></div>
              <div v-else-if="scope.row.form_value === 'file'">
                <oaUploadList :fileList="scope.row.after_value"></oaUploadList>
              </div>
              <div v-else-if="scope.row.form_value === 'tag'">
                <el-tag size="small" class="mb5" v-for="(val, index) in scope.row.after_value" :key="index">
                  {{ val }}
                </el-tag>
              </div>
              <p v-else>{{ getValue(scope.row.after_value, scope.row.form_value) }}</p>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 打开文件 -->
    <fileDialog ref="viewFile"></fileDialog>
  </div>
</template>
<script>
import oaUploadList from '@/components/form-common/oa-uploadList'
export default {
  name: '',
  components: { oaUploadList, fileDialog: () => import('@/components/openFile/previewDialog ') },
  props: {
    list: {
      type: Array,
      default: () => []
    },
    type: {
      type: String,
      default: ''
    },
    keyName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      formValueType: ['cascader', 'image', 'file', 'textarea', 'rich_text', 'checkbox', 'tag']
    }
  },

  methods: {
    iconHadler({ row }) {
      if (row.describe || row.describe.length == 0) {
        return 'icon-no'
      }
    },
    iconShow({ row }) {
      if (!this.formValueType.includes(row.form_value)) {
        return 'icon-no'
      }
    },
    getLogType(type) {
      let obj = {
        create: '新增',
        update: '修改',
        delete: '删除',
        approve: '审批',
        share_create: '添加协作者',
        share_delete: '删除协作者',
        share_update: '编辑协作者',
        transfer: '移交'
      }
      return obj[type]
    },
    // 数组转成字符串
    getValue(val, type) {
      let str = ''
      let arr = []
      if (type === 'radio') {
        str = val ? val.name : ''
      } else if (type === 'checkbox') {
        val.map((item) => {
          arr.push(item.name)
        })
        str = arr.join(' 、')
      } else if (type === 'tag') {
        val.map((item) => {
          arr.push(item)
        })
        str = arr.join(' 、')
      } else if (type === 'cascader') {
        str = val ? val.toString() : '--'
      } else {
        str = val ? val.toString() : '--'
      }

      return str || '空值'
    }
  }
}
</script>
<style scoped lang="scss">
::v-deep.icon-no .el-table__expand-icon {
  display: none;
}
::v-deep.el-table__header-wrapper {
  display: none;
}
.align-center {
  align-items: center;
}
.max-width {
  display: inline-block;
  max-width: 150px;
  overflow-x: hidden;
  white-space: nowrap;
}

.img {
  width: 54px;
  height: 54px;
  margin-right: 10px;
  cursor: pointer;
}
.created-time {
  color: #909399;
}
.fwweight-bold {
  display: inline-block;
  font-weight: 400;
  font-size: 14px;
  color: #303133;
  line-height: 14px;
}
.mb5 {
  margin-bottom: 5px;
  margin-right: 5px;
}
.expand-describe {
  background: #f9f9f9;
  margin: 0 20px;
  padding: 6px 20px 10px;
  font-size: 14px;
  border-radius: 4px;

  .expand-left {
    width: 50%;
    align-items: flex-start;
    ::v-deep table {
      border: 1px solid #ccc;
    }

    ::v-deep table th {
      border: 1px solid #ccc;
    }
    ::v-deep table td {
      padding: 10px 5px;
      border: 1px solid #ccc;
    }
  }
  .rich_text {
    ::v-deep p {
      img {
        width: 90%;
      }
    }
  }
  .expand-right {
    width: 50%;
    margin-left: 20px;
    align-items: flex-start;
    ::v-deep table {
      border: 1px solid #ccc;
    }

    ::v-deep table th {
      border: 1px solid #ccc;
    }
    ::v-deep table td {
      padding: 10px 5px;
      border: 1px solid #ccc;
    }
  }
  span {
    // color: #909399;
    min-width: 60px;
    display: inline-block;
  }
  p {
    color: #606266;
    line-height: 16px;
    margin-right: 36px;
    margin-bottom: 0;
    ::v-deepp {
      font-size: 14px;
      margin: 0;
    }
  }
}
.name-text {
  font-weight: 400;
  font-size: 14px;
  color: #606266;
}
.modify-text {
  display: inline-block;
  font-weight: 400;
  font-size: 14px;
  color: #909399;
}
.m6 {
  margin: 0 6px;
}
.mr6 {
  margin-right: 6px;
}
.tableBox {
  ::v-deep .el-table td.el-table__cell div {
    display: flex;
    align-items: center;
  }
}
.line-short {
  width: 1px;
  background: rgba(204, 204, 204, 0.3);
}
</style>
