# $auth 对象

`$auth` 对象用于验证用户是否拥有某（些）权限或角色，它定义在 `plugins/auth.js` 文件中。

## 验证用户权限

```js
const proxy = inject('proxy')

// 验证用户是否具备某权限
proxy.$auth.hasPermi('system:user:add')

// 验证用户是否含有指定权限，只需包含其中一个
proxy.$auth.hasPermiOr(['system:user:add', 'system:user:update'])

// 验证用户是否含有指定权限，必须全部拥有
proxy.$auth.hasPermiAnd(['system:user:add', 'system:user:update'])
```

## 验证用户角色

```js
const proxy = inject('proxy')

// 验证用户是否具备某角色
proxy.$auth.hasRole('admin')

// 验证用户是否含有指定角色，只需包含其中一个
proxy.$auth.hasRoleOr(['admin', 'common'])

// 验证用户是否含有指定角色，必须全部拥有
proxy.$auth.hasRoleAnd(['admin', 'common'])
```
