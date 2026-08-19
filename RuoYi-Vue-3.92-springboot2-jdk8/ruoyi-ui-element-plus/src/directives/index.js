import { hasPerm } from "./permission";
import hasPermi from "./permission/hasPermi";
import hasRole from "./permission/hasRole";
import copyText from "./common/copyText";

// 全局注册 directive
export function setupDirective(app) {
  // 壳层指令
  app.directive("hasPerm", hasPerm);
  // 若依兼容指令
  app.directive("hasPermi", hasPermi);
  app.directive("hasRole", hasRole);
  app.directive("copyText", copyText);
}