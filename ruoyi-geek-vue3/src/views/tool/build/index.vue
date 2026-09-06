<script setup lang="ts">
import { getTemplate, updateTemplate, addTemplate } from '@modules/form/api/template';
import modal from '@/plugins/modal';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const route = useRoute();
const router = useRouter();
const vfDesigner = ref<any>(null);
onMounted(() => {
    const sectionDom: HTMLDivElement = vfDesigner.value.$el;
    const mainHeader = sectionDom.querySelector('.main-header');
    if (route.query.id) {
        getTemplate(route.query.id).then((res: any) => {
            form.value = res.data;
            vfDesigner.value.setFormJson(JSON.parse(res.data.formSchema ?? {}));
        })
    }
    if (mainHeader) {
        mainHeader.remove();
    }
})
const dialogVisible = ref(false);
const form = ref<{
    formId: number | null,
    formName: string,
    formSchema: string,
    formVersion: string,
}>({
    formId: null,
    formName: '',
    formSchema: '',
    formVersion: '',
})
const rules = ref({
    formName: [
        { required: true, message: "表单名称不能为空", trigger: "blur" }
    ],
    formVersion: [
        { required: true, message: "表单版本不能为空", trigger: "blur" }
    ],
})
function submitFormData() {
    form.value.formSchema = JSON.stringify(vfDesigner.value.getFormJson());
    if (!!route.query.id) {
        form.value.formId = Number(route.query.id);
        updateTemplate(form.value).then((res: any) => {
            modal.msgSuccess('修改成功');
            dialogVisible.value = false;
        }).then(() => {
            router.push("/formManagement/formtemplate")
        })
    } else {
        addTemplate(form.value).then((res: any) => {
            modal.msgSuccess('新增成功');
            dialogVisible.value = false;
        }).then(() => {
            router.push("/formManagement/formtemplate")
        })
    }
}
</script>
<template>
    <div class="vForm">
        <v-form-designer ref="vfDesigner">
            <template #customSaveButton>
                <el-button type="primary" link @click="dialogVisible = true" icon='promotion'>保存</el-button>
            </template>
        </v-form-designer>
        <el-dialog title="提示" v-model="dialogVisible" width="500px">
            <el-form :model="form" :rules="rules">
                <el-form-item label="表单名称" prop="formName">
                    <el-input v-model="form.formName" placeholder="请输入表单名称" />
                </el-form-item>
                <el-form-item label="表单版本" prop="formVersion">
                    <el-input v-model="form.formVersion" placeholder="请输入表单版本" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="submitFormData">提交</el-button>
            </template>
        </el-dialog>
    </div>
</template>
<style lang="scss" scoped>
.vForm {
    .main-container {
        margin-left: 0 !important;
        height: calc(100vh - 84px) !important;
        overflow-y: auto;
    }
}
</style>