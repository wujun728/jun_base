<script setup lang="ts">
import { ref, watch } from 'vue';
import * as THREE from 'three'
import director, { refreshThree } from './director';
import { loadModel } from './three-plus/utils';
let globModel: THREE.Object3D | null = null
let fileUrl: string | null = null
const fileInput = ref<HTMLInputElement | null>(null)
const LoadModelLoading = ref(false)
const LoadModelStatus = ref("加载模型")
function triggerFileInput() {
    if (fileInput.value) {
        fileInput.value.click();
    }
}
function handleFileChange(event: Event) {
    const target = event.target as HTMLInputElement;
    const file = target.files?.[0];
    if (!file) return;

    fileUrl = URL.createObjectURL(file);
    LoadModelStatus.value = `已选择文件: ${file.name}`;
}

async function handelLoadModel() {
    if (!director) return
    if (!fileUrl) return
    LoadModelLoading.value = true
    const m = await loadModel(
        { gltf: fileUrl },
        "glb",
        progress => LoadModelStatus.value = `当前进度${Math.round(progress.loaded / progress.total * 100)}%`
    )
    m.scale.set(0.3, 0.3, 0.3)
    globModel = m
    director.scene.add(m)
    refreshThree()
    LoadModelStatus.value = "完成"
    LoadModelLoading.value = false
}

const ambientLight = ref({ intensity: 0 })
ambientLight.value = director.ambientLight
const FPS = ref(30)
watch(FPS, () => director ? director.FPS = FPS.value : void 0)
</script>
<template>
    <el-form label-width="100px" size="small">
        <el-form-item label="选择模型">
            <input type="file" @change="handleFileChange" accept=".glb" style="display: none;" ref="fileInput" />
            <el-button @click="triggerFileInput">选择本地文件</el-button>
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