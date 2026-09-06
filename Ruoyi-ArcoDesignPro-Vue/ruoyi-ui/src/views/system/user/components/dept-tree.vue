<template>
  <a-input-search
      v-model="searchKey"
      placeholder="搜索"
      style="margin-bottom: 8px; max-width: 240px"
  />
  <a-tree
      v-model:expanded-keys="getExpandedKeys"
      :default-expanded-keys="getExpandedKeys"
      :data="treeData"
      :field-names="{
              key: 'id',
              title: 'label',
              children: 'children',
            }"
      @select="handleSelected"
  >
    <template #label="nodeData">
      <template v-if="getMatchIndex(nodeData?.label) < 0">{{ nodeData?.label }}</template>
      <span v-else>
              {{ nodeData?.title?.substr(0, index) }}
                <span style="color: var(--color-primary-light-4);">
                  {{ nodeData?.title?.substr(index, searchKey.length) }}
                </span>
                {{ nodeData?.title?.substr(index + searchKey.length) }}
              </span>
    </template>
  </a-tree>
</template>

<script lang="ts" setup>
    import {computed, ref} from "vue";
    import {DeptTreeData, getDeptTreeSelect} from "@/api/system/user";
    import useLoading from "@/hooks/loading";

    const deptOptions = ref<DeptTreeData[]>([]);
    const searchKey = ref("");
    const {loading, setLoading} = useLoading(true);
    const emit = defineEmits(["select"]);

    // 获取部门树
    const fetchDeptData = async () => {
        setLoading(true);
        try {
    const {data} = await getDeptTreeSelect();
    deptOptions.value = data;
  } finally {
    setLoading(false);
  }
};
fetchDeptData();

// 获取展开节点 (默认展开两个层级)
const getExpandedKeys = computed(() => {
  const result: number[] = [];
  deptOptions.value.forEach((it: DeptTreeData) => {
    result.push(it.id);
    if (it.children && it.children.length > 0) {
      it.children.forEach((itc: DeptTreeData) => {
        result.push(itc.id);
      });
    }
  });
  return result;
});

function searchData(keyword: string) {
  const loop = (data: DeptTreeData[]) => {
    const result: DeptTreeData[] = [];
    data.forEach((item: DeptTreeData) => {
      if (item.label.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
        result.push({...item});
      } else if (item.children) {
        const filterData = loop(item.children);
        if (filterData.length) {
          result.push({
            ...item,
            children: filterData
          });
        }
      }
    });
    return result;
  };
  return loop(deptOptions.value);
}

const treeData = computed(() => {
  if (!searchKey.value) return deptOptions.value;
  return searchData(searchKey.value);
});

function getMatchIndex(title: string) {
  if (!searchKey.value) return -1;
  return title.toLowerCase().indexOf(searchKey.value.toLowerCase());
}

const handleSelected = (id: number[]) => {
  emit("select", id);
};

</script>

<script lang="ts">
export default {
  name: "DeptTree"
};
</script>

<style scoped>

</style>