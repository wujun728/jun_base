import http from "@/api";
import { addDateRange } from "@/utils";
import { ResPage } from "@/api/interface";
export function listMenu(params) {
  params = addDateRange(params, "createTime");
  console.log(params);

  return new Promise((resolve, reject) => {
    http
      .get<ResPage<Menu.MenuOptions>>(`/system/menu/list`, params)
      .then(res => {
        resolve({
          rows: handleTree(res.data, "menuId")
        });
      })
      .catch(err => reject(err));
  });
}

export function handleTree(data, id, parentId?, children?) {
  let config = {
    id: id || "id",
    parentId: parentId || "parentId",
    childrenList: children || "children"
  };

  let childrenListMap = {};
  let nodeIds = {};
  let tree: any[] = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

// 新增菜单
export function addMenu(data) {
  return http.post("/system/menu", data);
}

// 修改菜单
export function updateMenu(data) {
  return http.put("/system/menu", data);
}
// 查询菜单详细
export function getMenu(menuId) {
  return http.get("/system/menu/" + menuId);
}

// 删除菜单
export function delMenu(menuId) {
  return http.delete("/system/menu/" + menuId);
}
