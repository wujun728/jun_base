## 第一次上传插件 不知道写啥
## 组件方式直接引用
## 另外样式引入 可任选地方
````html
<template>
    <view>
        <cEditor ref="cEditor" @saveContent="saveContent"></cEditor>
    </view>
</template>

<script>
    import cEditor from "../../components/editor.vue"
    export default {
        components: {
            cEditor
        },
        data() {
            return {
            
            }
        },
        methods: {
            saveContent(data) {
                console.log(data);
            }
        }
    }
</script>
````