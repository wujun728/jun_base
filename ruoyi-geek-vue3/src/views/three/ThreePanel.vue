<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import { TreeNode } from './three-plus/ThreeHelper';
import type { TreeNodeData } from 'element-plus/es/components/tree-v2/src/types'
import { ElTreeV2 } from 'element-plus';
import { transform } from './director';
const props = defineProps<{
    modelthree: TreeNode[]
}>()
const treeProps = {
    children: 'children',
    label: 'label',
    value: 'id'
}
const emit = defineEmits(['handleNodeClick', 'refresh'])
const input = ref('all')
const types = ref<Array<{ value: string, label: string }>>([
    { value: 'all', label: '全部' },
    { value: 'Mesh', label: '网格' },
    { value: 'Group', label: '组' },
    { value: 'Object3D', label: '对象' },
    { value: 'Light', label: '光源' },
])
watch(() => props.modelthree, () => {
    const typesArray = []
    typesArray.push({ 'value': 'all', 'label': '全部' })
    props.modelthree.forEach(item => typesArray.push({ label: item.type, value: item.type }))
    types.value = typesArray.filter((item, index, array) => array.indexOf(item) === index)
})
function handleNodeClick(item: any) {
    emit('handleNodeClick', item)
}
const treeRef = ref<InstanceType<typeof ElTreeV2>>()
const filterMethod = (query: string, node: TreeNodeData) => node.type.includes(query) || query === 'all'
const selectMethod = () => treeRef.value!.filter(input.value)

</script>
<template>
    <div style="width: 100%;">
        <div class="title">
            <div>模型树</div>
            <el-button @click="$emit('refresh')" size="small">
                <div style="width:124px">重新加载模型树</div>
            </el-button>
        </div>
        <div class="search">
            <div>筛选</div>
            <el-select size="small" style="width:150px" v-model="input" @change="selectMethod">
                <el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
        </div>
        <el-tree-v2 :height="300" style="height: 100%;background: none;" :data="modelthree" node-key="id"
            :props="treeProps" @node-click="handleNodeClick" :expand-on-click-node="false" ref="treeRef"
            :filter-method="filterMethod">
            <template #default="{ node }">
                <span class="prefix">
                    [{{ node.data.type }}]
                </span>
                <span>{{ node.label }}</span>
            </template>
        </el-tree-v2>
    </div>
</template>

<style scoped lang="scss">
.title {
    height: 35px;
    display: flex;
    justify-content: space-between;
}

.search {
    height: 35px;
    display: flex;
    justify-content: space-between;
    border-bottom: 2px solid #eee;
    margin-bottom: 10px;
}
</style>