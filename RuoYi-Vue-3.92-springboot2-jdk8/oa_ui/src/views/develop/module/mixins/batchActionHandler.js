export default {
  methods: {
    handleBottomBatchSelectChange() {
      this.$refs.table.toggleAllSelection()
    },
    handleBatchShareData() {
      this.handleDropdown('share')
    },
    handleBatchTransferData() {
      this.handleDropdown('transfer')
    }
  }
}