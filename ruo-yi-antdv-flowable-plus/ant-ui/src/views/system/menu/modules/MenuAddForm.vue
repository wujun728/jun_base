<template>
  <ant-modal
    :visible="open"
    :modal-title="formTitle"
    :adjust-size="true"
    modalWidth="640"
    modalHeight="560"
    @cancel="cancel"
  >
    <a-form-model ref="form" :model="form" :rules="rules" slot="content" layout="vertical">
      <a-row class="form-row" :gutter="32">
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="上级菜单" prop="parentId">
            <a-tree-select
              v-model="form.parentId"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data="menuOptions"
              placeholder="请选择"
              :replaceFields="{ children: 'children', title: 'menuName', key: 'id', value: 'id' }"
              tree-default-expand-all
              @change="onMenuTreeChange"
            >
            </a-tree-select>
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="菜单类型" prop="menuType">
            <!--            <a-radio-group v-model="form.menuType" button-style="solid">
              <a-radio-button value="M">目录</a-radio-button>
              <a-radio-button value="C">菜单</a-radio-button>
              <a-radio-button value="F">按钮</a-radio-button>
            </a-radio-group>-->
            <a-radio-group v-model="form.menuType" button-style="solid">
              <a-radio-button v-for="(d, index) in menuTypeOptions" :key="index" :disabled="menuTypeEnableValue.indexOf(d.menuTypeValue) === -1" :value="d.menuTypeValue">{{ d.menuTypeLabel }}</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
        <a-col :lg="24" :md="24" :sm="24" v-if="form.menuType != 'F'">
          <a-form-model-item label="图标" prop="icon">
            <a-space size="large" class="selectIconBox">
              <a-icon :component="allIcon[form.icon + 'Icon']" v-if="allIcon[form.icon + 'Icon']" class="selectIcon" />
              <a-icon :type="form.icon" v-if="!allIcon[form.icon + 'Icon']" />
              <a @click="selectIcon" class="selectup">
                <a-icon :type="SelectIcon" />
              </a>
            </a-space>
            <a-card :bordered="false" v-if="iconVisible">
              <icon-selector v-model="form.icon" @change="handleIconChange" :svgIcons="iconList" :allIcon="allIcon" />
            </a-card>
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="菜单编码" prop="menuCode">
            <a-input v-model="form.menuCode" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="菜单名称" prop="menuName">
            <a-input v-model="form.menuName" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-model-item label="排序" prop="treeSort">
            <a-input-number v-model="form.treeSort" :min="0" style="width: 100%" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24" v-if="form.menuType != 'F'">
          <a-form-model-item label="是否外链" prop="isFrame">
            <a-radio-group v-model="form.isFrame" button-style="solid">
              <a-radio-button value="0">是</a-radio-button>
              <a-radio-button value="1">否</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24" v-if="form.menuType != 'F'">
          <a-form-model-item label="路由地址" prop="path">
            <a-input v-model="form.path" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24" v-if="form.menuType == 'C'">
          <a-form-model-item label="组件路径" prop="component">
            <a-input v-model="form.component" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24" v-if="form.menuType != 'M'">
          <a-form-model-item label="权限标识" prop="perms">
            <a-input v-model="form.perms" placeholder="请输入" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24" v-if="form.menuType != 'F'">
          <a-form-model-item label="是否显示" prop="visible">
            <a-radio-group v-model="form.visible" button-style="solid">
              <a-radio-button v-for="(d, index) in visibleOptions" :key="index" :value="d.dictValue">{{
                d.dictLabel
              }}</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24" v-if="form.menuType != 'F'">
          <a-form-model-item label="状态" prop="status">
            <a-radio-group v-model="form.status" button-style="solid">
              <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
                d.dictLabel
              }}</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24" v-if="form.menuType == 'C'">
          <a-form-model-item label="是否缓存" prop="isCache">
            <a-radio-group v-model="form.isCache" button-style="solid">
              <a-radio-button value="0">缓存</a-radio-button>
              <a-radio-button value="1">不缓存</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="备注" prop="remark">
            <a-input v-model="form.remark" placeholder="请输入" type="textarea" allow-clear />
          </a-form-model-item>
        </a-col>
      </a-row>
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
import MenuForm from './MenuForm'
export default {
  ...MenuForm
}
</script>
