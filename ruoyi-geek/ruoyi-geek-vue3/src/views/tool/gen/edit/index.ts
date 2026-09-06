import { getGenTable, updateGenTable } from "@/api/tool/gen";
import { modal } from "@/plugins";
import { createGlobalState } from "@vueuse/core";
import { ref } from "vue";

export class BaseEntity {
  /** 搜索值 */
  searchValue!: string;

  /** 创建者 */
  createBy!: string;

  /** 创建时间 */
  createTime!: string;

  /** 更新者 */
  updateBy!: string;

  /** 更新时间 */
  updateTime!: string;

  /** 备注 */
  remark!: string;

  /** 请求参数 */
  params!: Record<string, any>;
}

export class GenTableVo extends BaseEntity {
  /** 业务表 */
  table!: GenTable;

  /** 业务表的列 */
  columns!: GenColumn[];

  /** 关联信息 */
  joinTablesMate!: GenJoin[];

  /** 参与关联的表 */
  joinTables!: GenTable[];

  /** 参与关联的列 */
  joinColumns!: GenColumn[];

  getAllGenTables(): GenTable[] {
    const allGenTables: GenTable[] = [];
    allGenTables.push(this.table);
    if (this.joinTables) {
      allGenTables.push(...this.joinTables);
    }
    return allGenTables;
  }

  getAllGenColumns(): GenColumn[] {
    const allGenColumns: GenColumn[] = [];
    if (this.columns) {
      allGenColumns.push(...this.columns);
    }
    if (this.joinColumns) {
      allGenColumns.push(...this.joinColumns);
    }
    return allGenColumns;
  }

  getTableMap(): Record<number, GenTable> {
    const tableMap: Record<number, GenTable> = {};
    if (this.table) {
      tableMap[this.table.tableId] = this.table;
    }
    if (this.joinTables) {
      for (const genTable of this.joinTables) {
        if (genTable) {
          tableMap[genTable.tableId] = genTable;
        }
      }
    }
    return tableMap;
  }

  getTableAliasMap(): Record<number, string> {
    const tableMap: Record<number, string> = {};
    if (this.table) {
      tableMap[this.table.tableId] = this.table.tableAlias;
    }
    if (this.joinTablesMate) {
      for (const genTable of this.joinTablesMate) {
        if (genTable) {
          tableMap[genTable.leftTableId] = genTable.leftTableAlias;
          tableMap[genTable.rightTableId] = genTable.rightTableAlias;
        }
      }
    }
    return tableMap;
  }

  getColumnMap(): Record<number, GenColumn> {
    const columnMap: Record<number, GenColumn> = {};
    const genTables = this.getAllGenTables();
    for (const genTable of genTables) {
      for (const genTableColumn of genTable.columns) {
        columnMap[genTableColumn.columnId] = genTableColumn;
      }
    }
    return columnMap;
  }
}

export class GenJoin extends BaseEntity {
  /** 表编号 */
  tableId!: number;

  leftTableId!: number;

  /** 关联表编号 */
  rightTableId!: number;

  /** 新引入的表 */
  newTableId!: number;

  /** 主表别名 */
  leftTableAlias!: string;

  /** 关联表别名 */
  rightTableAlias!: string;

  /** 主表外键 */
  leftTableFk!: number;

  /** 关联表外键 */
  rightTableFk!: number;

  /** 连接类型 */
  joinType!: string;

  /** 关联字段 */
  joinColumns!: string[];

  orderNum!: number;
}

export class GenTable extends BaseEntity {
  /** 编号 */
  tableId!: number;

  /** 表名称 */
  tableName!: string;

  /** 表别名 */
  tableAlias!: string;

  /** 表描述 */
  tableComment!: string;

  /** 关联父表的表名 */
  subTableName!: string;

  /** 本表关联父表的外键名 */
  subTableFkName!: string;

  /** 实体类名称(首字母大写) */
  className!: string;

  /** 使用的模板（crud单表操作 tree树表操作 sub主子表操作） */
  tplCategory!: string;

  /** 前端类型（element-ui模版 element-plus模版） */
  tplWebType!: string;

  /** 生成包路径 */
  packageName!: string;

  /** 生成模块名 */
  moduleName!: string;

  /** 生成业务名 */
  businessName!: string;

  /** 生成功能名 */
  functionName!: string;

  /** 生成作者 */
  functionAuthor!: string;

  /** 生成代码方式（0zip压缩包 1自定义路径） */
  genType!: string;

  /** 生成路径（不填默认项目路径） */
  genPath!: string;

  /** 主键信息 */
  pkColumn!: GenColumn;

  /** 子表信息 */
  subTable!: GenTable;

  /** 表列信息 */
  columns!: GenColumn[];

  /** 其它生成选项 */
  options!: string;

  /** 树编码字段 */
  treeCode!: string;

  /** 树父编码字段 */
  treeParentCode!: string;

  /** 树名称字段 */
  treeName!: string;

  /** 上级菜单ID字段 */
  parentMenuId!: string;

  /** 上级菜单名称字段 */
  parentMenuName!: string;

  /** 是否含有关联字段 */
  haveSubColumn!: string;
}

export class GenColumn extends BaseEntity {
  /** 编号 */
  columnId!: number;

  /** 归属表编号 */
  tableId!: number;

  /** 列名称 */
  columnName!: string;

  /** 列描述 */
  columnComment!: string;

  /** 列类型 */
  columnType!: string;

  /** JAVA类型 */
  javaType!: string;

  /** JAVA字段名 */
  javaField!: string;

  /** 是否主键（1是） */
  isPk!: string;

  /** 是否自增（1是） */
  isIncrement!: string;

  /** 是否必填（1是） */
  isRequired!: string;

  /** 是否为插入字段（1是） */
  isInsert!: string;

  /** 是否编辑字段（1是） */
  isEdit!: string;

  /** 是否列表字段（1是） */
  isList!: string;

  /** 是否查询字段（1是） */
  isQuery!: string;

  /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
  queryType!: string;

  /**
   * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
   */
  htmlType!: string;

  /** 字典类型 */
  dictType!: string;

  /** 排序 */
  sort!: number;

  /** 关联表名称 */
  subColumnTableName!: string;

  /** 关联字段名称 */
  subColumnFkName!: string;

  /** 映射字段名称 */
  subColumnName!: string;

  /** 映射字段Java字段名 */
  subColumnJavaField!: string;

  /** 映射字段Java类型 */
  subColumnJavaType!: string;
}

export const genTableState = createGlobalState(() => {
  const info = ref<GenTable>(new GenTable());
  const tables = ref<GenTable[]>([]);
  const joins = ref<GenJoin[]>([]);
  const columns = ref<GenColumn[]>([]);
  const tableDict = ref<any>({});

  const initGenTableVo = (tableId: string) => {
    getGenTable(tableId).then(res => {
      columns.value = res.data.columns;
      info.value = res.data.table;
      tables.value = res.data.joinTables;
      joins.value = res.data.joinTablesMate;
    });
  }
  const updateGenTableVo = () => {
    const genTable = Object.assign({}, info.value);
    genTable.columns = columns.value;
    genTable.params = {
      treeCode: info.value.treeCode,
      treeName: info.value.treeName,
      treeParentCode: info.value.treeParentCode,
      parentMenuId: info.value.parentMenuId
    };
    updateGenTable({
      table: genTable,
      columns: columns.value,
      joinTables: tables.value,
      joinColumns: [],
      joinTablesMate: joins.value
    }).then(res => {
      modal.msgSuccess(res.msg);
      if (res.code === 200) {
        close();
      }
    });
  }
  return { info, tables, joins, columns, tableDict, initGenTableVo, updateGenTableVo };
})