<template>
  <ant-modal
    :visible="open"
    :modal-title="formTitle"
    :adjust-size="true"
    @cancel="cancel"
    modalHeight="480"
    modalWidth="640">
    <a-form-model ref="form" :model="form" :rules="rules" slot="content" layout="vertical">
      <a-row class="form-row" :gutter="32">
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="上级部门" prop="parentId">
            <select-dept
              v-model="form.parentId"
              select-model="single"
              :select-scope="selectScope"
              @callBack="onSelectDept"
            />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="部门编码" prop="deptCode">
            <a-input v-model="form.deptCode" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="部门名称" prop="deptName">
            <a-input v-model="form.deptName" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="部门全称" prop="deptFullName">
            <a-input v-model="form.deptFullName" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="排序" prop="treeSort">
            <a-input-number v-model="form.treeSort" :min="0" style="width: 100%"/>
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="类型" prop="deptType">
            <a-radio-group v-model="form.deptType" button-style="solid">
              <a-radio-button v-for="(d, index) in deptTypeOptions" :key="index" :disabled="deptTypeEnableValue.indexOf(d.dictValue) === -1" :value="d.dictValue">{{ d.dictLabel }}</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-collapse :bordered="false" expandIconPosition="left">
        <template #expandIcon="props">
          <a-icon type="caret-right" :rotate="props.isActive ? 90 : 0" />
        </template>
        <a-collapse-panel key="1" header="详细信息" :style="customStyle">
          <a-row :gutter="32">
            <a-col :lg="12" :md="12" :sm="24">
              <a-form-model-item label="负责人" prop="leader">
                <a-input v-model="form.leader" placeholder="请输入" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="12" :md="12" :sm="24">
              <a-form-model-item label="办公电话" prop="phone">
                <a-input v-model="form.phone" placeholder="请输入" />
              </a-form-model-item>
            </a-col>

            <a-col :lg="12" :md="12" :sm="24">
              <a-form-model-item label="联系地址" prop="address">
                <a-input v-model="form.address" placeholder="请输入" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="12" :md="12" :sm="24">
              <a-form-model-item label="邮政编码" prop="zipCode">
                <a-input v-model="form.zipCode" placeholder="请输入" />
              </a-form-model-item>
            </a-col>

            <a-col :lg="12" :md="12" :sm="24">
              <a-form-model-item label="电子邮箱" prop="email">
                <a-input v-model="form.email" placeholder="请输入" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="24" :md="24" :sm="24" :span="24">
              <a-form-model-item label="备注" prop="remark">
                <a-input v-model="form.remark" placeholder="请输入" type="textarea" allow-clear />
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-collapse-panel>
      </a-collapse>

    </a-form-model>
    <template slot="footer">
      <a-button @click="cancel">
        取消
      </a-button>
      <a-button type="primary" @click="submitForm">
        保存
      </a-button>
    </template>
  </ant-modal>
</template>
<script>
  import sysDeptForm from './SysDeptForm'
  export default {
    ...sysDeptForm
      }
</script>
