import Cookies from 'js-cookie';

export function modalSure(title) {
  return new Promise((resolve, reject) => {
    this.$confirm(`确定${title || '永久删除该数据'}`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'sure-modal',
    })
      .then(() => {
        resolve();
      })
      .catch(() => {
        reject();
        this.$message({
          type: 'info',
          message: '已取消',
        });
      });
  });
}

export function handleDeleteTable(length, tableFrom) {
  if (length === 1 && tableFrom.page > 1) return (tableFrom.page = tableFrom.page - 1);
}