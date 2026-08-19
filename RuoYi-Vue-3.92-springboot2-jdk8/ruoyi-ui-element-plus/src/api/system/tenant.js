import request from "@/utils/request";

const TENANT_BASE_URL = "/api/v1/tenants";

/**
 * 租户信息
 */

const TenantAPI = {
  /**
   * 获取当前用户可访问的租户列表
   */
  getTenantList() {
    return request({
      url: `${TENANT_BASE_URL}/options`,
      method: "get",
    });
  },

  /**
   * 获取当前租户信息
   */
  getCurrentTenant() {
    return request({
      url: `${TENANT_BASE_URL}/current`,
      method: "get",
    });
  },

  /**
   * 切换租户
   *
   * @param tenantId 目标租户ID
   */
  switchTenant(tenantId) {
    return request({
      url: `${TENANT_BASE_URL}/${tenantId}/switch`,
      method: "post",
    });
  },

  /** 获取租户分页数据（平台租户管理） */
  getPage(queryParams) {
    return request({
      url: `${TENANT_BASE_URL}`,
      method: "get",
      params: queryParams,
    });
  },

  /** 获取租户表单数据 */
  getFormData(tenantId) {
    return request({
      url: `${TENANT_BASE_URL}/${tenantId}/form`,
      method: "get",
    });
  },

  /** 新增租户并初始化默认数据 */
  create(data) {
    return request({
      url: `${TENANT_BASE_URL}`,
      method: "post",
      data,
    });
  },

  /** 修改租户 */
  update(tenantId, data) {
    return request({
      url: `${TENANT_BASE_URL}/${tenantId}`,
      method: "put",
      data,
    });
  },

  /** 删除租户（批量） */
  deleteByIds(ids) {
    return request({
      url: `${TENANT_BASE_URL}/${ids}`,
      method: "delete",
    });
  },

  /** 修改租户状态 */
  updateStatus(tenantId, status) {
    return request({
      url: `${TENANT_BASE_URL}/${tenantId}/status`,
      method: "put",
      params: { status },
    });
  },

  /** 获取租户菜单ID集合 */
  getTenantMenuIds(tenantId) {
    return request({
      url: `${TENANT_BASE_URL}/${tenantId}/menuIds`,
      method: "get",
    });
  },

  /** 更新租户菜单 */
  updateTenantMenus(tenantId, menuIds) {
    return request({
      url: `${TENANT_BASE_URL}/${tenantId}/menus`,
      method: "put",
      data: menuIds,
    });
  },
};

export default TenantAPI;
