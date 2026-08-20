<template>
  <u-gap :height="height" :bgColor="bgColor"></u-gap>
</template>

<script>
export default {
  props: {
    height: {
      type: [String, Number],
      default: 16
    },
    bgColor: {
      type: String,
      default: '#f4f4f5'
    }
  }
}
</script>