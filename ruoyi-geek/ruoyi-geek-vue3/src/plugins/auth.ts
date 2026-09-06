import useUserStore from "@/store/modules/user";

function authPermission(permission: string): boolean {
  const all_permission = "*:*:*";
  const permissions = useUserStore().permissions;
  if (permission && permission.length > 0) {
    return permissions.some((v) => {
      return all_permission === v || v === permission;
    });
  } else {
    return false;
  }
}

function authRole(role: string): boolean {
  const super_admin = "admin";
  const roles = useUserStore().roles;
  if (role && role.length > 0) {
    return roles.some((v) => {
      return super_admin === v || v === role;
    });
  } else {
    return false;
  }
}

export default {
  /**
   * 验证用户是否具备某权限
   * @param permission 权限符
   * @returns
   */
  hasPermi(permission: string): boolean {
    return authPermission(permission);
  },
  /**
   * 验证用户是否含有指定权限，只需包含其中一个
   * @param permissions 权限符数组
   * @returns
   */
  hasPermiOr(permissions: Array<string>): boolean {
    return permissions.some((item) => {
      return authPermission(item);
    });
  },
  /**
   * 验证用户是否含有指定权限，必须全部拥有
   * @param permissions 权限符数组
   * @returns
   */
  hasPermiAnd(permissions: Array<string>): boolean {
    return permissions.every((item) => {
      return authPermission(item);
    });
  },
  /**
   * 验证用户是否具备某角色
   * @param role 角色
   * @returns
   */
  hasRole(role: string): boolean {
    return authRole(role);
  },
  /**
   * 验证用户是否含有指定角色，只需包含其中一个
   * @param roles 角色数组
   * @returns
   */
  hasRoleOr(roles: Array<string>): boolean {
    return roles.some((item) => {
      return authRole(item);
    });
  },
  /**
   * 验证用户是否含有指定角色，必须全部拥有
   * @param roles 角色数组
   * @returns
   */
  hasRoleAnd(roles: Array<string>): boolean {
    return roles.every((item) => {
      return authRole(item);
    });
  },
};
