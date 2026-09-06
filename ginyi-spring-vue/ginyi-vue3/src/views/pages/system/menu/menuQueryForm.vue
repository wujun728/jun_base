<template>
    <CommonForm
        :submitButtonText="'查询'"
        @onSubmit="query"
        @onReset="reset">
        <n-form
            ref="formRef"
            :model="formValue"
            :inline="true"
            :label-width="80">
            <n-grid
                x-gap="12"
                :cols="3">
                <n-grid-item>
                    <n-form-item label="菜单名称" path="menuName">
                        <n-input v-model:value="formValue.menuName" placeholder="请输入菜单名称" clearable v-focus/>
                    </n-form-item>
                </n-grid-item>
                <n-grid-item>
                    <n-form-item label="状态" path="status">
                        <n-select v-model:value="formValue.status" clearable :options="statusDict" />
                    </n-form-item>
                </n-grid-item>
                <n-grid-item>
                    <n-form-item label="起止时间" path="time">
                        <n-date-picker v-model:formatted-value="formValue.time"
                                       clearable
                                       value-format="yyyy-MM-dd HH:mm:ss"
                                       update-value-on-close type="datetimerange" :actions="['confirm', 'clear']"/>
                    </n-form-item>
                </n-grid-item>
            </n-grid>
        </n-form>
    </CommonForm>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import CommonForm from "@/components/commonForm/index.vue";
import {useCommonForm} from "@/components/commonForm/useCommonForm";
import {useStaticDict} from "@/dictionary/useStaticDict";

export default defineComponent({
    name: "DeptQueryValue",
    components: {
        CommonForm
    },
    emits: ["onQuery", "onReset"],
    setup(props, context) {
        const {
            formRef,
            formValue,
            onReset,
            onQuery
        } = useCommonForm({})

        const {statusDict} = useStaticDict()

        const query = () => {
            onQuery().then(res => {
                context.emit("onQuery", res)
            })
        }

        const reset = () => {
            onReset().then(res => {
                context.emit("onReset", res)
            })
        }

        return {
            formRef,
            formValue,
            query,
            reset,
            statusDict
        }
    }
})
</script>