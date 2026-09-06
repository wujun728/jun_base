<template>
  <a-drawer
    width="480"
    :title="formTitle"
    :label-col="4"
    :wrapper-col="14"
    :visible="open"
    :body-style="{height:'calc(100vh - 100px)',overflow:'auto'}"
    @close="cancel">
    <a-form-model ref="form" :model="form" :rules="rules" layout="vertical">
      <a-spin :spinning="spinning" :delay="delayTime" tip="Loading...">
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
        <a-form-model-item label="菜单类型" prop="menuType">
          <a-radio-group v-model="form.menuType" button-style="solid">
            <a-radio-button v-for="(d, index) in menuTypeOptions" :key="index" :disabled="menuTypeEnableValue.indexOf(d.menuTypeValue) === -1" :value="d.menuTypeValue">{{ d.menuTypeLabel }}</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
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
        <a-form-model-item label="菜单编码" prop="menuCode">
          <a-input v-model="form.menuCode" placeholder="请输入" />
        </a-form-model-item>
        <a-form-model-item label="菜单名称" prop="menuName">
          <a-input v-model="form.menuName" placeholder="请输入" />
        </a-form-model-item>
        <a-form-model-item label="排序" prop="treeSort">
          <a-input-number v-model="form.treeSort" :min="0" style="width: 100%" />
        </a-form-model-item>
        <a-form-model-item label="是否外链" prop="isFrame" v-if="form.menuType != 'F'">
          <a-radio-group v-model="form.isFrame" button-style="solid">
            <a-radio-button value="0">是</a-radio-button>
            <a-radio-button value="1">否</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="路由地址" prop="path" v-if="form.menuType != 'F'">
          <a-input v-model="form.path" placeholder="请输入" />
        </a-form-model-item>
        <a-form-model-item label="组件路径" prop="component" v-if="form.menuType == 'C'">
          <a-input v-model="form.component" placeholder="请输入" />
        </a-form-model-item>
        <a-form-model-item label="权限标识" prop="perms" v-if="form.menuType != 'M'">
          <a-input v-model="form.perms" placeholder="请输入" />
        </a-form-model-item>
        <a-form-model-item label="是否显示" prop="visible" v-if="form.menuType != 'F'">
          <a-radio-group v-model="form.visible" button-style="solid">
            <a-radio-button v-for="(d, index) in visibleOptions" :key="index" :value="d.dictValue">{{
              d.dictLabel
            }}</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="状态" prop="status" v-if="form.menuType != 'F'">
          <a-radio-group v-model="form.status" button-style="solid">
            <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
              d.dictLabel
            }}</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="是否缓存" prop="isCache" v-if="form.menuType == 'C'">
          <a-radio-group v-model="form.isCache" button-style="solid">
            <a-radio-button value="0">缓存</a-radio-button>
            <a-radio-button value="1">不缓存</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="备注" prop="remark">
          <a-input v-model="form.remark" placeholder="请输入" type="textarea" allow-clear />
        </a-form-model-item>
      </a-spin>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" @click="submitForm">
            保存
          </a-button>
          <a-button @click="cancel">
            取消
          </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
</template>

<script>
import MenuForm from './MenuForm'
export default {
  ...MenuForm
}
</script>
