<template>
    <CommonModal v-model:show="modalShow" :title="modalTitle" @onSubmit="onSubmit" style="width: 800px">
        <n-form
            ref="modalFormRef"
            :model="modalForm"
            :inline="false"
            :rules="rules">
            <n-grid :cols="24" :x-gap="24">
                <n-form-item-gi :span="12" label="岗位名称" path="postName">
                    <n-input v-model:value="modalForm.postName" clearable placeholder="请输入岗位名称"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="岗位编码" path="postCode">
                    <n-input v-model:value="modalForm.postCode" clearable placeholder="请输入岗位编码"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="角色顺序" path="sort">
                    <n-input-number v-model:value="modalForm.sort" clearable placeholder="请输入角色顺序" style="width: 100%"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="状态" path="status">
                    <n-radio-group v-model:value="modalForm.status" name="radioGroup2" size="small">
                        <n-radio-button v-for="(item, index) in statusDict" :key="index" :value="item.value">
                            {{ item.label }}
                        </n-radio-button>
                    </n-radio-group>
                </n-form-item-gi>
                <n-form-item-gi :span="24" label="备注" path="remark">
                    <n-input v-model:value="modalForm.remark" clearable type="textarea" placeholder="请输入备注"/>
                </n-form-item-gi>
            </n-grid>
        </n-form>
    </CommonModal>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import CommonModal from "@/components/commonModal/index.vue";
import {useCommonModal} from "@/components/commonModal/useCommonModal";
import {postController} from "@/api";
import {useStaticDict} from "@/dictionary/useStaticDict";

export default defineComponent({
    name: "UserEditForm",
    components: {
        CommonModal
    },
    setup() {
        const {
            modalShow,
            modalTitle,
            modalForm,
            modalFormRef,
            modalLoading,
            onAdd,
            onEdit,
            onSubmit,
            onDeleteById,
            onDeleteByIds
        } = useCommonModal("岗位", {status: "0"},
            postController.add,
            postController.edit,
            postController.deleteById,
            postController.deleteByIds,
            postController.getPostDetailsById
        )
        const {statusDict} = useStaticDict()

        const rules = {
            postName: {type: "string", required: true, message: "请输入岗位名称", trigger: ["input", "blur"]},
            postCode: {type: "string", required: true, message: "请输入岗位编码", trigger: ["input", "blur"]},
            sort: {type: "number", required: true, message: "请输入排序", trigger: ["input", "blur"]},
            status: {type: "string", required: true, message: "请选择状态", trigger: ["blur", "change"]},
        }

        return {
            modalShow,
            modalTitle,
            modalForm,
            modalFormRef,
            modalLoading,
            rules,
            onAdd,
            onEdit,
            onSubmit,
            onDeleteById,
            onDeleteByIds,
            statusDict,
        }
    }
})
</script>

<style scoped>

</style>