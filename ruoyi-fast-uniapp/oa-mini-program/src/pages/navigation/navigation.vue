<script lang="ts" setup>
import GG1 from '@/static/img/gg1.png'
const list = ref<any>([GG1])
</script>
<template>
    <m-start-ad :list="list" url="/pages/login/login"  afterColor="#dae4f1" bgColor='#c5d4f3' :time="10"/>
</template>
