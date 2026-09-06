<template>
  <div class="container">
    <!-- 面包屑-->
    <breadcrumb :items="['系统工具', '代码生成', '修改生成配置']"/>
    <a-spin :loading="loading" tip="加载中">
      <a-card
          class="general-card r-nav-card"
          :body-style="{ padding: '15px' }"
          :header-style="{ padding: '15px' }"
      >
        <a-row>
          <a-col>
            <a-tabs default-active-key="1">
              <a-tab-pane key="1" title="基本信息">
                <edit-table-basic-info ref="basicInfoRef" v-model="state.basicInfo"/>
              </a-tab-pane>
              <a-tab-pane key="2" title="字段信息">
                <edit-table-field-info ref="fieldInfoRef" v-model="state.fieldInfo"/>
              </a-tab-pane>
              <a-tab-pane key="3" title="生成信息">
                <edit-table-gen-info ref="genInfoRef" v-model="state.basicInfo" :tables="state.tablesInfo"/>
              </a-tab-pane>
            </a-tabs>
          </a-col>
          <a-col class="submit-nav-button">
            <a-space :size="16">
              <a-button type="primary" @click="submitForm()">提交</a-button>
              <a-button @click="close()">返回</a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-card>
    </a-spin>
  </div>

</template>

<script setup lang="ts">

import {computed, reactive, ref, watch} from "vue";
import {useRouter} from "vue-router";
import {useTabBarStore} from "@/store";
import {TagProps} from "@/store/modules/tab-bar/types";
import EditTableBasicInfo from "@/views/tool/gen/components/edit-table-basic-info.vue";
import EditTableFieldInfo from "@/views/tool/gen/components/edit-table-field-info.vue";
import EditTableGenInfo from "@/views/tool/gen/components/edit-table-gen-info.vue";
import {ColumnRow, GenTableDetailInfo, getGenTable, updateGenTable} from "@/api/tool/gen";
import useLoading from "@/hooks/loading";
import {Message} from "@arco-design/web-vue";

const state = reactive({
  form: {},
  basicInfo: {},
  fieldInfo: [],
  tablesInfo: {} as ColumnRow[],
})

const basicInfoRef = ref();
const fieldInfoRef = ref();
const genInfoRef = ref();

const router = useRouter();
const tabBarStore = useTabBarStore();
const {loading, setLoading} = useLoading(true);

// 关闭按钮
const close = () => {
  const tabList = tabBarStore.getTabList;
  const tag = tabList.find((x) => x.name === router.currentRoute.value.name) as TagProps
  const idx = tabList.findIndex((x) => x.name === tag.name)
  tabBarStore.deleteTag(idx, tag);
  const latest = tabList[idx - 1]; // 获取队列的前一个tab
  router.push({name: latest.name});
}

// 提交
const submitForm = async () => {
  const fieldInfoTmp = state.fieldInfo.map((d: ColumnRow) => {
    const tmp = {...d}
    tmp.isQuery = d.isQuery ? '1' : undefined
    tmp.isRequired = d.isRequired ? '1' : undefined
    tmp.isInsert = d.isInsert ? '1' : undefined
    tmp.isEdit = d.isEdit ? '1' : undefined
    tmp.isList = d.isList ? '1' : undefined
    return tmp
  })

  const basicFormErrors = await basicInfoRef.value.validateForm()
  const genInFormErrors = await genInfoRef.value.validateForm()

  if (!basicFormErrors && !genInFormErrors) {
    const genTable = {...state.basicInfo} as GenTableDetailInfo;
    genTable.columns = fieldInfoTmp;
    genTable.params = {
      treeCode: genTable.treeCode,
      treeName: genTable.treeName,
      treeParentCode: genTable.treeParentCode,
      parentMenuId: genTable.parentMenuId
    };
    const {code, msg} = await updateGenTable(genTable)
    if (code && code === 200) {
      Message.success(msg)
      close()
    }
  } else {
    Message.warning("表单验证不通过，请检查！")
  }
}

const tableId = computed(() => {
  return router.currentRoute.value.params && router.currentRoute.value.params.tableId;
})

// 初始化信息
const init = async (value: string) => {
  if (value) {
    setLoading(true)
    const {code, data} = await getGenTable(value);
    const {rows, info, tables} = data
    state.basicInfo = info
    state.fieldInfo = rows.map((d: ColumnRow) => {
      const tmp = {...d}
      tmp.isQuery = d.isQuery === '1'
      tmp.isRequired = d.isRequired === '1'
      tmp.isInsert = d.isInsert === '1'
      tmp.isEdit = d.isEdit === '1'
      tmp.isList = d.isList === '1'
      return tmp
    })
    state.tablesInfo = tables
    setLoading(false)
  }
}

watch(() => tableId, (value) => {
  if (value) {
    init(value.value as string);
  }
}, {immediate: true})
</script>

<style scoped lang="less">

.container {
  padding: 0 20px 20px 20px;
}
.submit-nav-button {
  text-align: center;
  margin: 20px 0 0 0;
}

</style>