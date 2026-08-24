<script>
export default {
  name: 'LeRender',

  props: {
    renderFn: {
      type: Function,
      required: true
    }
  },

  render() {
    return this.renderFn()
  }
}
</script>
