<!-- 项目详情侧滑弹窗  -->
<template>
  <div class="station">
    <el-form ref="formData" :model="formData" label-width="110px">
      <el-form-item label="项目名称：" prop="name" class="lh13">
        <span>{{ formData.name || '- -' }}</span>
      </el-form-item>

      <el-form-item label="负责人：" prop="uid" class="select-bar">
        <div class="mb-10">
          <div class="avatar-box members-box" v-for="(item, index) in formData.admins" :key="index">
            <img :src="item.avatar" alt="" />
            <span>{{ item.name || '- -' }}</span>
          </div>
        </div>
      </el-form-item>

      <el-form-item label="项目成员：" class="select-bar">
        <div class="mb-10">
          <div class="avatar-box members-box" v-for="(item, index) in formData.members" :key="index">
            <img :src="item.avatar" alt="" />
            <span class="members-box-content">{{ item.name }}</span>
          </div>
          <span v-if="!formData.members">- -</span>
        </div>
      </el-form-item>

      <el-form-item label="计划开始：" class="select-bar" prop="start_date">
        <span>{{ formData.start_date || '- -' }}</span>
      </el-form-item>

      <el-form-item label="计划结束：" class="select-bar" prop="end_date">
        <span>{{ formData.end_date || '- -' }}</span>
      </el-form-item>

      <el-form-item label="关联客户：" prop="eid">
        <div>
          <span>{{ formData.customer ? formData.customer.customer_name : '- -' }}</span>
        </div>
      </el-form-item>
      <el-form-item v-if="formData.eid" label="关联合同：" prop="cid">
        <div>
          <span>{{ formData.contract ? formData.contract.contract_name : '--' }}</span>
        </div>
      </el-form-item>

      <el-form-item label="项目状态：" prop="status">
        <span>
          {{ formData.status == 0 ? '正常' : formData.status == 1 ? '暂停' : '关闭' }}
        </span>
      </el-form-item>

      <el-form-item label="项目描述：">
        <span>{{ formData.describe || '- -' }}</span>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'detailsForm',
  props: {
    formData: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      drawer: false,
      id: 0,
      type: '',
      edit: false
    }
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
.station ::v-deep.edui-editor-iframeholder {
  height: 300px !important;
}
.mb-10 {
  margin-bottom: -10px;
}
.station {
  ::v-deep .el-form-item__label {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #606266;
    line-height: 22px;
  }
  ::v-deep .el-form-item__content {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #303133;
    line-height: 22px;
  }
}

::v-deep.el-cascader,
::v-deep .el-input-number,
::v-deep .el-select,
::v-deep .el-date-editor {
  width: 100%;
}
.avatar-box {
  margin-right: 12px;
  background: #f7f7f7;
  padding: 5px 10px;
  border-radius: 4px;
  margin-bottom: 10px;
  img {
    width: 16px;
    height: 16px;
    border-radius: 50%;
  }
}
.members-box {
  display: inline-block;
}
.members-box-content {
  margin: 0 3px 3px 0;
}
::v-deep .el-form-item {
  align-items: center;
  margin-bottom: 30px;
}
</style>
