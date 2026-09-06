import {defineStore} from "pinia";
import {storeKeyEnums} from "@/enums/storeKeyEnums";
import {IUser} from "@/interface/modules/system";
import {storage} from "@/hooks/useStorage";
import {userController} from "@/api";
import {useSystemStore} from "@/store/modules/useSystemStore";

export const useUserStore = defineStore(storeKeyEnums.USER, {
    state: (): IUser => ({
        username: undefined,
        tokenKey: undefined,
        authorization: undefined
    }),
    getters: {
        getName(): string | undefined {
            return this.username
        },
    },
    actions: {
        login(username: string | undefined, data: any): Promise<any> {
            return new Promise((resolve, reject) => {
                this.username = username
                this.tokenKey = data.tokenHeader
                this.authorization = data.token
                storage.set(data.tokenHeader, data.token)
                resolve(null);
            })
        },
        logout(): Promise<any> {
            return new Promise((resolve, reject) => {
                userController.logout().then(() => {
                    useUserStore().$reset()
                    useSystemStore().removeAllTabs()
                    useSystemStore().resetBreadMenuList()
                    storage.clear()
                    resolve(null);
                })
            })
        }
    }
})