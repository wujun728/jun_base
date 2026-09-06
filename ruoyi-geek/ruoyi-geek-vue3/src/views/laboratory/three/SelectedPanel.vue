<script setup lang="ts">
import director, { transform, unselectObject } from './director';
import { Delete, MagicStick } from '@element-plus/icons-vue'
import { initExplodeModel } from './three-plus/ExplodeControls';
import { computed, onMounted } from 'vue';
import * as THREE from 'three'
const reExplode = () => {
    if (!transform.object) return
    initExplodeModel(transform.object)
    transform.explode = 0
}

function handelCapModel() {
    if (!director || !transform.object) return
    director.controls.capsControls.enabled = !director.controls.capsControls.enabled
    director.controls.dragControls.enabled = !director.controls.capsControls.enabled
    director.controls.capsControls.objects = transform.object
}

const mesh = computed(() => transform.object instanceof THREE.Mesh ? transform.object : null)
const getSide = (side: number) => {
    switch (side) {
        case THREE.FrontSide:
            return '正面';
        case THREE.BackSide:
            return '背面';
        case THREE.DoubleSide:
            return '双面';
        default:
            return '未知';
    }
};
const name = computed(() => transform.object ?
    transform.object.name ? transform.object.name : 'model'
    : '无'
)
</script>
<template>
    <div>
        <el-form label-width="100px" size="small" label-position="left">
            <div>
                <el-form-item label="当前选择的模型">
                    <div style="display: flex;justify-content: space-between;width: 100%;">
                        <span>{{ name }}</span>
                        <el-button type="danger" circle @click="unselectObject" :icon="Delete" size="small" />
                    </div>
                </el-form-item>
                <el-form-item>
                    <template #label>
                        <div
                            style="display: inline-flex;align-items: center;justify-content: space-between;width: 100%;">
                            <div>爆炸距离</div>
                            <el-button :icon="MagicStick" circle size="small" @click="reExplode" />
                        </div>
                    </template>
                    <el-slider v-model="transform.explode" :step="0.1" :max="30" :min="1" />
                </el-form-item>
                <el-form-item label="当前变换模式">
                    <el-select v-model="transform.mode">
                        <el-option label="translate" value="translate" />
                        <el-option label="rotate" value="rotate" />
                        <el-option label="scale" value="scale" />
                    </el-select>
                </el-form-item>
                <el-form-item label="刨面模型">
                    <el-button @click="handelCapModel">
                        刨面模型
                    </el-button>
                </el-form-item>
            </div>
            <div v-if="mesh">
                <el-form-item label="颜色">
                    {{ mesh.material.color.getHexString() }}
                </el-form-item>
                <el-form-item label="线框模式">
                    {{ mesh.material.wireframe }}
                </el-form-item>
                <el-form-item label="渲染面">
                    {{ getSide(mesh.material.side) }}
                </el-form-item>
                <el-form-item label="透明">
                    {{ mesh.material.transparent }}
                </el-form-item>
                <el-form-item label="透明度">
                    {{ mesh.material.opacity }}
                </el-form-item>
                <el-form-item v-if="mesh.material.map" label="纹理贴图">
                    {{ mesh.material.map.name }}
                </el-form-item>
                <el-form-item v-if="mesh.material.normalMap" label="法线贴图">
                    {{ mesh.material.normalMap.name }}
                </el-form-item>
                <el-form-item v-if="mesh.material.bumpMap" label="凹凸贴图">
                    {{ mesh.material.bumpMap.name }}
                </el-form-item>
                <el-form-item v-if="mesh.material.specularMap" label="高光贴图">
                    {{ mesh.material.specularMap.name }}
                </el-form-item>
            </div>
        </el-form>
    </div>
</template>