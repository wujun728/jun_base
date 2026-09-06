<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import * as THREE from 'three'
import director, { refreshThree } from './director';
import { loadModel } from './three-plus/utils';

const FACTORY_GLTF_URL = encodeURI('/glb/factory/Planta Tratamiento Aguas Residuales FES Acatlán.gltf')
const MODEL_LABEL = 'factory.gltf'
let globModel: THREE.Object3D | null = null
const LoadModelLoading = ref(false)
const LoadModelStatus = ref('加载模型')

async function handelLoadModel() {
    if (!director) return
    if (LoadModelLoading.value) return
    LoadModelLoading.value = true
    LoadModelStatus.value = '加载中...'
    const m = await loadModel(
        { gltf: FACTORY_GLTF_URL },
        'gltf',
        progress => LoadModelStatus.value = `当前进度${Math.round(progress.loaded / progress.total * 100)}%`
    )
    m.scale.set(0.3, 0.3, 0.3)
    globModel = m
    director.scene.add(m)
    refreshThree()
    LoadModelStatus.value = "完成"
    LoadModelLoading.value = false
}

onMounted(() => {
    handelLoadModel()
})

const ambientLight = ref({ intensity: 0 })
ambientLight.value = director.ambientLight
const FPS = ref(30)
watch(FPS, () => director ? director.FPS = FPS.value : void 0)
</script>
<template>
    <el-form label-width="100px" size="small">
        <el-form-item label="模型">
            <div>{{ MODEL_LABEL }}</div>
        </el-form-item>
        <el-form-item label="加载模型">
            <el-button @click="handelLoadModel" v-loading="LoadModelLoading">
                {{ LoadModelStatus }}
            </el-button>
        </el-form-item>
        <el-form-item label="FPS">
            <el-select v-model="FPS" placeholder="Select" style="width: 100%">
                <el-option v-for="item in [15, 30, 45, 60, 90, 120]" :key="item" :label="item" :value="item" />
            </el-select>
        </el-form-item>
        <el-form-item label="环境光强度">
            <el-slider v-model="ambientLight.intensity" :step="0.1" :max="30" />
        </el-form-item>
    </el-form>
</template>