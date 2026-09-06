/******************************* 默认的弹层提交事件 ******************************/
import _remote from '../remoteApi'
import crudUtil from "@/utils/server-crud"

/**
 * 默认的提交事件
 * @param _self
 * @param row
 * @param hide
 */
function defaultSubmit(_self, row, hide) {
  let submitUrl = _self.dialogConfig.config["submitUrl"];
  let method = _self.dialogConfig.config["method"] || "post";
  if (!submitUrl) {
    _self.$message.error("后端接口没有配置,请查看[dialogConfig.config.submitUrl]属性");
    return;
  }
  let url = crudUtil.completionDomain(_self, submitUrl);
  // 执行远程处理
  _remote.api(url, method, row, (res) => {
    if (crudUtil.processDMLResponse(_self, res)) {
      // 刷新表格
      _self.closeDialog();
    } else {
      _self.openDialog();
    }
  });
  hide();
}


export default {
  defaultSubmit
}
