
-- MySQL DDL for Enterprise Procurement & Inventory (Beginner CRUD Version) with sys_ prefix
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1) 基础资料

DROP TABLE IF EXISTS sys_supplier;
CREATE TABLE sys_supplier (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(128) NOT NULL COMMENT '供应商名称',
  short_name VARCHAR(64) DEFAULT NULL COMMENT '供应商名称简拼',
  address VARCHAR(256) DEFAULT NULL COMMENT '地址',
  postal_code VARCHAR(16) DEFAULT NULL COMMENT '邮政编码',
  phone VARCHAR(32) DEFAULT NULL COMMENT '电话',
  fax VARCHAR(32) DEFAULT NULL COMMENT '传真',
  website VARCHAR(128) DEFAULT NULL COMMENT '网址',
  email VARCHAR(128) DEFAULT NULL COMMENT '电子邮件',
  region VARCHAR(64) DEFAULT NULL COMMENT '地区',
  registered_capital DECIMAL(18,2) DEFAULT NULL COMMENT '注册资金',
  legal_person VARCHAR(64) DEFAULT NULL COMMENT '法人代表',
  bank_name VARCHAR(128) DEFAULT NULL COMMENT '开户银行',
  bank_account VARCHAR(64) DEFAULT NULL COMMENT '银行帐号',
  bank_address VARCHAR(128) DEFAULT NULL COMMENT '银行地址',
  tax_no VARCHAR(64) DEFAULT NULL COMMENT '纳税号',
  first_coop_date DATE DEFAULT NULL COMMENT '初次合作日期',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_supplier_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供货商';

DROP TABLE IF EXISTS sys_supplier_contact;
CREATE TABLE sys_supplier_contact (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  supplier_id BIGINT UNSIGNED NOT NULL COMMENT '供应商ID',
  name VARCHAR(64) NOT NULL COMMENT '姓名',
  title VARCHAR(64) DEFAULT NULL COMMENT '职务',
  gender CHAR(1) DEFAULT NULL COMMENT '性别 M/F',
  start_coop_date DATE DEFAULT NULL COMMENT '合作开始日期',
  phone1 VARCHAR(32) DEFAULT NULL COMMENT '电话1',
  phone2 VARCHAR(32) DEFAULT NULL COMMENT '电话2',
  mobile VARCHAR(32) DEFAULT NULL COMMENT '移动电话',
  email VARCHAR(128) DEFAULT NULL COMMENT '电子邮件',
  fax VARCHAR(32) DEFAULT NULL COMMENT '传真',
  birthday DATE DEFAULT NULL COMMENT '出生日期',
  hobby VARCHAR(128) DEFAULT NULL COMMENT '爱好',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  KEY idx_contact_supplier (supplier_id),
  CONSTRAINT fk_contact_supplier FOREIGN KEY (supplier_id) REFERENCES sys_supplier (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供货商联系人';

DROP TABLE IF EXISTS sys_product;
CREATE TABLE sys_product (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '商品编码',
  name VARCHAR(128) NOT NULL COMMENT '商品名称',
  short_name VARCHAR(64) DEFAULT NULL COMMENT '简拼',
  unit VARCHAR(32) DEFAULT NULL COMMENT '计量单位',
  spec_model VARCHAR(128) DEFAULT NULL COMMENT '规格型号',
  manufacturer VARCHAR(128) DEFAULT NULL COMMENT '厂家',
  price_public DECIMAL(18,2) DEFAULT NULL COMMENT '公开报价',
  price_cost DECIMAL(18,2) DEFAULT NULL COMMENT '成本价',
  update_date DATE DEFAULT NULL COMMENT '更新日期',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_product_code (code),
  KEY idx_product_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品';

DROP TABLE IF EXISTS sys_warehouse;
CREATE TABLE sys_warehouse (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(128) NOT NULL COMMENT '仓库名称',
  description VARCHAR(256) DEFAULT NULL COMMENT '说明',
  capacity_qty DECIMAL(18,2) DEFAULT NULL COMMENT '仓库可容纳数量',
  address VARCHAR(256) DEFAULT NULL COMMENT '仓库地址',
  manager_name VARCHAR(64) DEFAULT NULL COMMENT '负责人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_wh_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库';

-- 2) 采购管理

DROP TABLE IF EXISTS sys_po_header;
CREATE TABLE sys_po_header (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '订单号',
  supplier_id BIGINT UNSIGNED NOT NULL COMMENT '供货商ID',
  order_date DATE NOT NULL COMMENT '订货日期',
  delivery_date DATE DEFAULT NULL COMMENT '交货日期',
  salesman VARCHAR(64) DEFAULT NULL COMMENT '业务员',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  void_by VARCHAR(64) DEFAULT NULL COMMENT '作废人',
  is_void TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否作废(0否,1是)',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_po_code (code),
  KEY idx_po_supplier (supplier_id),
  CONSTRAINT fk_po_supplier FOREIGN KEY (supplier_id) REFERENCES sys_supplier (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购订单-主表';

DROP TABLE IF EXISTS sys_po_line;
CREATE TABLE sys_po_line (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  po_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  line_no INT NOT NULL COMMENT '行号',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编码(冗余)',
  product_name VARCHAR(128) DEFAULT NULL COMMENT '产品名称(冗余)',
  short_name VARCHAR(64) DEFAULT NULL COMMENT '简拼(冗余)',
  spec VARCHAR(128) DEFAULT NULL COMMENT '规格',
  unit VARCHAR(32) DEFAULT NULL COMMENT '计量单位',
  qty_ordered DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '数量',
  price DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '单价',
  manufacturer VARCHAR(128) DEFAULT NULL COMMENT '厂家',
  warehouse_id BIGINT UNSIGNED DEFAULT NULL COMMENT '仓库ID',
  qty_purchased DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '已采购数量(已收货)',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_po_line (po_id, line_no),
  KEY idx_po_line_product (product_id),
  KEY idx_po_line_wh (warehouse_id),
  CONSTRAINT fk_po_line_po FOREIGN KEY (po_id) REFERENCES sys_po_header (id) ON DELETE CASCADE,
  CONSTRAINT fk_po_line_product FOREIGN KEY (product_id) REFERENCES sys_product (id),
  CONSTRAINT fk_po_line_wh FOREIGN KEY (warehouse_id) REFERENCES sys_warehouse (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购订单-明细';

DROP TABLE IF EXISTS sys_grn_header;
CREATE TABLE sys_grn_header (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '进货单号',
  supplier_id BIGINT UNSIGNED NOT NULL COMMENT '供货商ID',
  received_date DATE NOT NULL COMMENT '进货日期',
  salesman VARCHAR(64) DEFAULT NULL COMMENT '业务员',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_grn_code (code),
  KEY idx_grn_supplier (supplier_id),
  CONSTRAINT fk_grn_supplier FOREIGN KEY (supplier_id) REFERENCES sys_supplier (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购进货单-主表';

DROP TABLE IF EXISTS sys_grn_line;
CREATE TABLE sys_grn_line (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  grn_id BIGINT UNSIGNED NOT NULL COMMENT '进货单ID',
  line_no INT NOT NULL COMMENT '行号',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编码(冗余)',
  product_name VARCHAR(128) DEFAULT NULL COMMENT '产品名称(冗余)',
  short_name VARCHAR(64) DEFAULT NULL COMMENT '简拼(冗余)',
  spec VARCHAR(128) DEFAULT NULL COMMENT '规格',
  unit VARCHAR(32) DEFAULT NULL COMMENT '计量单位',
  qty DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '数量',
  price DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '单价',
  manufacturer VARCHAR(128) DEFAULT NULL COMMENT '厂家',
  warehouse_id BIGINT UNSIGNED DEFAULT NULL COMMENT '仓库ID',
  location VARCHAR(64) DEFAULT NULL COMMENT '货位',
  tax_rate DECIMAL(5,2) DEFAULT NULL COMMENT '税率(%)',
  po_line_id BIGINT UNSIGNED DEFAULT NULL COMMENT '关联采购订单明细ID',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_grn_line (grn_id, line_no),
  KEY idx_grn_line_product (product_id),
  KEY idx_grn_line_wh (warehouse_id),
  KEY idx_grn_line_po (po_line_id),
  CONSTRAINT fk_grn_line_grn FOREIGN KEY (grn_id) REFERENCES sys_grn_header (id) ON DELETE CASCADE,
  CONSTRAINT fk_grn_line_product FOREIGN KEY (product_id) REFERENCES sys_product (id),
  CONSTRAINT fk_grn_line_wh FOREIGN KEY (warehouse_id) REFERENCES sys_warehouse (id),
  CONSTRAINT fk_grn_line_po FOREIGN KEY (po_line_id) REFERENCES sys_po_line (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购进货单-明细';

DROP TABLE IF EXISTS sys_ap_payment;
CREATE TABLE sys_ap_payment (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '付款单据号',
  supplier_id BIGINT UNSIGNED NOT NULL COMMENT '供货商ID',
  salesman VARCHAR(64) DEFAULT NULL COMMENT '业务员',
  pay_date DATE NOT NULL COMMENT '付款日期',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  receiver VARCHAR(64) DEFAULT NULL COMMENT '收款人',
  bill_no VARCHAR(64) DEFAULT NULL COMMENT '票据号码',
  bill_amount DECIMAL(18,2) DEFAULT NULL COMMENT '票据金额',
  invoice_no VARCHAR(64) DEFAULT NULL COMMENT '发票号码',
  invoice_amount DECIMAL(18,2) DEFAULT NULL COMMENT '发票金额',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_payment_code (code),
  KEY idx_payment_supplier (supplier_id),
  CONSTRAINT fk_payment_supplier FOREIGN KEY (supplier_id) REFERENCES sys_supplier (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购付款登记';

DROP TABLE IF EXISTS sys_ap_prepayment;
CREATE TABLE sys_ap_prepayment (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '预付款单据号',
  origin_code VARCHAR(64) DEFAULT NULL COMMENT '原始单据号',
  supplier_id BIGINT UNSIGNED NOT NULL COMMENT '供货商ID',
  pay_method VARCHAR(32) DEFAULT NULL COMMENT '付款方式',
  amount DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '付款金额',
  pay_date DATE NOT NULL COMMENT '时间',
  salesman VARCHAR(64) DEFAULT NULL COMMENT '业务员',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  payment_voucher_code VARCHAR(64) DEFAULT NULL COMMENT '付款单据号(冲抵来源)',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_prepayment_code (code),
  KEY idx_prepayment_supplier (supplier_id),
  CONSTRAINT fk_prepayment_supplier FOREIGN KEY (supplier_id) REFERENCES sys_supplier (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购预付款登记';

-- 3) 库存管理单据

DROP TABLE IF EXISTS sys_return_header;
CREATE TABLE sys_return_header (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '退库单据号',
  return_date DATE NOT NULL COMMENT '日期',
  salesman VARCHAR(64) DEFAULT NULL COMMENT '业务员',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_return_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退库单-主表';

DROP TABLE IF EXISTS sys_return_line;
CREATE TABLE sys_return_line (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  return_id BIGINT UNSIGNED NOT NULL COMMENT '退库单ID',
  line_no INT NOT NULL COMMENT '行号',
  ref_outbound_code VARCHAR(64) DEFAULT NULL COMMENT '出货单单据号(参考)',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编码(冗余)',
  product_name VARCHAR(128) DEFAULT NULL COMMENT '产品名称(冗余)',
  spec VARCHAR(128) DEFAULT NULL COMMENT '规格',
  qty DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '退库数量',
  unit VARCHAR(32) DEFAULT NULL COMMENT '计量单位',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_return_line (return_id, line_no),
  KEY idx_return_line_product (product_id),
  CONSTRAINT fk_return_line_header FOREIGN KEY (return_id) REFERENCES sys_return_header (id) ON DELETE CASCADE,
  CONSTRAINT fk_return_line_product FOREIGN KEY (product_id) REFERENCES sys_product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退库单-明细';

DROP TABLE IF EXISTS sys_loan_header;
CREATE TABLE sys_loan_header (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '借出单据号',
  borrower VARCHAR(64) NOT NULL COMMENT '借货人',
  loan_date DATE NOT NULL COMMENT '借货时间',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_loan_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='借出单-主表';

DROP TABLE IF EXISTS sys_loan_line;
CREATE TABLE sys_loan_line (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  loan_id BIGINT UNSIGNED NOT NULL COMMENT '借出单ID',
  line_no INT NOT NULL COMMENT '行号',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编码(冗余)',
  product_name VARCHAR(128) DEFAULT NULL COMMENT '产品名称(冗余)',
  spec VARCHAR(128) DEFAULT NULL COMMENT '产品规格',
  unit VARCHAR(32) DEFAULT NULL COMMENT '计量单位',
  qty DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '产品数量',
  qty_returned DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '已还数量',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_loan_line (loan_id, line_no),
  KEY idx_loan_line_product (product_id),
  CONSTRAINT fk_loan_line_header FOREIGN KEY (loan_id) REFERENCES sys_loan_header (id) ON DELETE CASCADE,
  CONSTRAINT fk_loan_line_product FOREIGN KEY (product_id) REFERENCES sys_product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='借出单-明细';

DROP TABLE IF EXISTS sys_loan_return_header;
CREATE TABLE sys_loan_return_header (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '归还单据号',
  returner VARCHAR(64) NOT NULL COMMENT '还货人',
  return_date DATE NOT NULL COMMENT '还货时间',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_loan_return_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='归还单-主表';

DROP TABLE IF EXISTS sys_loan_return_line;
CREATE TABLE sys_loan_return_line (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  loan_return_id BIGINT UNSIGNED NOT NULL COMMENT '归还单ID',
  line_no INT NOT NULL COMMENT '行号',
  loan_code VARCHAR(64) DEFAULT NULL COMMENT '借出单据号',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编码(冗余)',
  qty DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '归还数量',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_loan_return_line (loan_return_id, line_no),
  KEY idx_loan_return_line_product (product_id),
  CONSTRAINT fk_loan_return_line_header FOREIGN KEY (loan_return_id) REFERENCES sys_loan_return_header (id) ON DELETE CASCADE,
  CONSTRAINT fk_loan_return_line_product FOREIGN KEY (product_id) REFERENCES sys_product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='归还单-明细';

DROP TABLE IF EXISTS sys_transfer_header;
CREATE TABLE sys_transfer_header (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '调拨单据号',
  dst_warehouse_id BIGINT UNSIGNED NOT NULL COMMENT '目标仓库',
  src_warehouse_id BIGINT UNSIGNED NOT NULL COMMENT '源仓库',
  transfer_date DATE NOT NULL COMMENT '调拨日期',
  transfer_by VARCHAR(64) DEFAULT NULL COMMENT '调拨人',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_transfer_code (code),
  KEY idx_transfer_src (src_warehouse_id),
  KEY idx_transfer_dst (dst_warehouse_id),
  CONSTRAINT fk_transfer_src FOREIGN KEY (src_warehouse_id) REFERENCES sys_warehouse (id),
  CONSTRAINT fk_transfer_dst FOREIGN KEY (dst_warehouse_id) REFERENCES sys_warehouse (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='调拨单-主表';

DROP TABLE IF EXISTS sys_transfer_line;
CREATE TABLE sys_transfer_line (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  transfer_id BIGINT UNSIGNED NOT NULL COMMENT '调拨单ID',
  line_no INT NOT NULL COMMENT '行号',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编码(冗余)',
  product_name VARCHAR(128) DEFAULT NULL COMMENT '名称(冗余)',
  qty DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '调拨数量',
  price DECIMAL(18,2) DEFAULT NULL COMMENT '单价',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_transfer_line (transfer_id, line_no),
  KEY idx_transfer_line_product (product_id),
  CONSTRAINT fk_transfer_line_header FOREIGN KEY (transfer_id) REFERENCES sys_transfer_header (id) ON DELETE CASCADE,
  CONSTRAINT fk_transfer_line_product FOREIGN KEY (product_id) REFERENCES sys_product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='调拨单-明细';

DROP TABLE IF EXISTS sys_stock_count_header;
CREATE TABLE sys_stock_count_header (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(64) NOT NULL COMMENT '盘点单据号',
  count_date DATE NOT NULL COMMENT '盘点日期',
  salesman VARCHAR(64) DEFAULT NULL COMMENT '业务员',
  operator VARCHAR(64) DEFAULT NULL COMMENT '操作员',
  approver VARCHAR(64) DEFAULT NULL COMMENT '审核人',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_stock_count_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='盘点单-主表';

DROP TABLE IF EXISTS sys_stock_count_line;
CREATE TABLE sys_stock_count_line (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  count_id BIGINT UNSIGNED NOT NULL COMMENT '盘点单ID',
  line_no INT NOT NULL COMMENT '行号',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编号(冗余)',
  product_name VARCHAR(128) DEFAULT NULL COMMENT '名称(冗余)',
  short_name VARCHAR(64) DEFAULT NULL COMMENT '简拼(冗余)',
  warehouse_id BIGINT UNSIGNED NOT NULL COMMENT '仓库ID',
  qty_on_hand DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '当前库存数量',
  qty_actual DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '实际库存数量',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_stock_count_line (count_id, line_no),
  KEY idx_stock_count_product (product_id),
  KEY idx_stock_count_wh (warehouse_id),
  CONSTRAINT fk_stock_count_line_header FOREIGN KEY (count_id) REFERENCES sys_stock_count_header (id) ON DELETE CASCADE,
  CONSTRAINT fk_stock_count_line_product FOREIGN KEY (product_id) REFERENCES sys_product (id),
  CONSTRAINT fk_stock_count_line_wh FOREIGN KEY (warehouse_id) REFERENCES sys_warehouse (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='盘点单-明细';

-- 4) 库存现势

DROP TABLE IF EXISTS sys_inventory_balance;
CREATE TABLE sys_inventory_balance (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  warehouse_id BIGINT UNSIGNED NOT NULL COMMENT '仓库ID',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
  qty_on_hand DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '当前库存数量',
  create_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  remark VARCHAR(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY uk_inv_balance (warehouse_id, product_id),
  KEY idx_inv_wh (warehouse_id),
  KEY idx_inv_product (product_id),
  CONSTRAINT fk_inv_balance_wh FOREIGN KEY (warehouse_id) REFERENCES sys_warehouse (id),
  CONSTRAINT fk_inv_balance_product FOREIGN KEY (product_id) REFERENCES sys_product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存现势(按仓库+产品)';

SET FOREIGN_KEY_CHECKS = 1;