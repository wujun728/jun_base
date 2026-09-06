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
                :cols="5">
                <n-grid-item>
                    <n-form-item label="用户账号" path="userName">
                        <n-input v-model:value="formValue.userName" placeholder="请输入用户账号" clearable v-focus/>
                    </n-form-item>
                </n-grid-item>
                <n-grid-item>
                    <n-form-item label="用户昵称" path="user.nickName">
                        <n-input v-model:value="formValue.nickName" placeholder="请输入昵称" clearable/>
                    </n-form-item>
                </n-grid-item>
                <n-grid-item>
                    <n-form-item label="状态" path="status">
                        <n-select v-model:value="formValue.status" clearable :options="statusDict"/>
                    </n-form-item>
                </n-grid-item>
                <n-grid-item>
                    <n-form-item label="手机号码" path="phoneNumber">
                        <n-input v-model:value="formValue.phoneNumber" placeholder="请输入手机号码" clearable/>
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
    name: "UserQueryValue",
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
            formRef, formValue,
            query,
            reset,
            statusDict
        }
    }
})
</script>