<template>
  <u-tabs :activeStyle="activeStyle" :list="tabs" @change="(tab) => $emit('change', tab)"></u-tabs>
</template>

<script>
export default {
  props: {
    tabs: Array
  },
  data () {
    return {
      activeStyle: {
        color: '#303133',
        fontSize: '20px',
        fontWeight: 'bold',
        transform: 'scale(1.05)'
      }
    }
  }
}
</script>