<script lang="ts" setup>
import { computed, ComputedRef, ref, watch } from 'vue';
import * as THREE from 'three'
import { transform } from './director'

// 定义参数项的类型
interface ParameterItem {
    name: string;
    value?: string | number;
}

// 定义参数组的类型
interface ParameterGroup {
    GroupName: string;
    Parameters: ParameterItem[];
}
// 使用 defineProps 来定义 props 并指定其类型
const parameters = ref<ParameterGroup[]>([])
const material = ref<THREE.Material>()
watch(() => transform.object, () => {
    console.log(transform.object);
    
    if (transform.object) {
        parameters.value = transform.object.userData.Parameters
        const model: THREE.Mesh = transform.object as THREE.Mesh
        if (model.material) {
            material.value = model.material as THREE.Material
        } else {
            material.value = {} as THREE.Material
        }
    } else {
        parameters.value = []
    }
});
</script>
<template>
    <div v-if="transform.object">
        <div>名称：{{ transform.object.name }}</div>
        <div v-if="material">材质名称：{{ material.name }}</div>
        <div class="parameter-list">
            <div v-for="(group, index) in parameters" :key="index" class="parameter-group">
                <div class="parameter-group-name">{{ group.GroupName }}</div>
                <ul>
                    <li v-for="(param, paramIndex) in group.Parameters" :key="paramIndex" class="parameter-item">
                        <div class="parameter-name">{{ param.name }}:</div>
                        <div class="parameter-value">
                            {{ param.value !== undefined ? param.value : '未定义' }}
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>

</template>
<style scoped lang="scss">
.parameter-list {
    font-family: Arial, sans-serif;
    display: flex;
}

.parameter-group {
    padding: 5px;
    margin-bottom: 20px;
    width: 200px;
    border: #2c3e50 1px solid;
    border-radius: 5px;
    margin: 3px;

    & ul {
        list-style-type: none;
        padding: 0 10px;
        overflow: auto;
        max-height: 150px;
        font-size: 12px;

        .parameter-item {
            line-height: 1.5;
            display: flex;
            justify-content: space-between;

            .parameter-name {}

            .parameter-value {}
        }
    }

    .parameter-group-name {
        margin-bottom: 10px;
        color: #2c3e50;
    }


}
</style>