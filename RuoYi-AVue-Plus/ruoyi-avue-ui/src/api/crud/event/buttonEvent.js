import _remote from '../remoteApi'
import crudUtil from "@/utils/server-crud"

///////////////////////////////////////// 通用的按钮触发事件 /////////////////////////////////////////////////////

/**
 * 通用的测试
 * @param self  vue的上下文
 * @param item  按钮的参数
 * @param row   数据行的参数
 * @param index 数据下标行的参数
 */
export function testA(self, item, row, index) {
  self.$message.success('testA --> ' + JSON.stringify(row) + '数据序号' + index);
}

export function testB(self, item, row, index) {
  self.$message.success('testB --> ' + JSON.stringify(row) + '数据序号' + index);
}
