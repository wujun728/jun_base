import request from "@/utils/request";

const TENANT_PLAN_BASE_URL = "/api/v1/tenant-plans";

const TenantPlanAPI = {
  /** 获取租户套餐分页数据 */
  getPage(queryParams) {
    return request({
      url: `${TENANT_PLAN_BASE_URL}`,
      method: "get",
      params: queryParams,
    });
  },

  /** 获取租户套餐表单数据 */
  getFormData(planId) {
    return request({
      url: `${TENANT_PLAN_BASE_URL}/${planId}/form`,
      method: "get",
    });
  },

  /** 新增租户套餐 */
  create(data) {
    return request({ url: `${TENANT_PLAN_BASE_URL}`, method: "post", data });
  },

  /** 修改租户套餐 */
  update(planId, data) {
    return request({ url: `${TENANT_PLAN_BASE_URL}/${planId}`, method: "put", data });
  },

  /** 删除租户套餐 */
  deleteByIds(ids) {
    return request({ url: `${TENANT_PLAN_BASE_URL}/${ids}`, method: "delete" });
  },

  /** 获取租户方案下拉选项 */
  getOptions() {
    return request({
      url: `${TENANT_PLAN_BASE_URL}/options`,
      method: "get",
    });
  },

  /** 获取方案菜单ID集合 */
  getPlanMenuIds(planId) {
    return request({
      url: `${TENANT_PLAN_BASE_URL}/${planId}/menuIds`,
      method: "get",
    });
  },

  /** 更新方案菜单 */
  updatePlanMenus(planId, menuIds) {
    return request({
      url: `${TENANT_PLAN_BASE_URL}/${planId}/menus`,
      method: "put",
      data: menuIds,
    });
  },
};

export default TenantPlanAPI;
