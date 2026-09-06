<template>
    <div style="display: flex; align-items: center">
        <span style="margin-right: 15px">{{ username }}</span>
        <n-dropdown :options="options" @select="handleSelect" :show-arrow="true">
            <n-avatar round size="medium" :src="logo" style="margin-right: 10px"/>
        </n-dropdown>
    </div>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {storeToRefs} from "pinia";
import {LogOutOutline as LogoutIcon, Moon, PersonCircleOutline as UserIcon, SunnySharp} from '@vicons/ionicons5'
import {useRenderIcon} from "@/plugins/naive-ui/common";
import {useUserStore} from "@/store/modules/useUserStore";
import {useCommonRouter} from "@/router";
import {setting} from "@/config/setting";
import {useRouterStore} from "@/store/modules/useRouterStore";


export default defineComponent({
    name: "Personnel",
    components: {
        Moon, SunnySharp
    },
    setup() {
        const {username} = storeToRefs(useUserStore());
        const {logo} = setting;
        const options = ref<Array<{ label: string, key: string, icon?: Function }>>([
            {
                label: '个人中心',
                key: 'person',
                icon: useRenderIcon(UserIcon)
            },
            {
                label: '退出登录',
                key: 'logout',
                icon: useRenderIcon(LogoutIcon)
            }
        ])
        const handleSelect = (key: string) => {
            if (key === "logout") {
                useUserStore().logout().then(() => {
                    useCommonRouter("login")
                    useRouterStore().$reset()
                    window.$message.success("退出成功")
                })
            }
        }

        return {
            logo,
            options,
            username,
            handleSelect
        }
    }
})
</script>

<style scoped>

</style>