-- =============================================
-- RBAC权限菜单初始化SQL
-- OA办公系统: 2000-2499
-- CMS内容管理: 3000-3499
-- DEV项目管理: 4000-4499
-- Blog博客系统: 5000-5499
-- =============================================

-- =============================================
-- 一、OA办公系统菜单 (2000-2499)
-- =============================================

-- 一级目录
INSERT INTO sys_menu VALUES('2000', 'OA办公', '0', '5', 'oa', NULL, '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', NULL, 'OA办公系统目录');

-- 二级菜单 - CRM客户管理
INSERT INTO sys_menu VALUES('2001', '客户管理', '2000', '1', 'customer', 'oa/customer/index', '', 1, 0, 'C', '0', '0', 'oa:customer:list', 'peoples', 'admin', sysdate(), '', NULL, '客户管理菜单');
INSERT INTO sys_menu VALUES('2002', '合同管理', '2000', '2', 'contract', 'oa/contract/index', '', 1, 0, 'C', '0', '0', 'oa:contract:list', 'documentation', 'admin', sysdate(), '', NULL, '合同管理菜单');
INSERT INTO sys_menu VALUES('2003', '采购管理', '2000', '3', 'purchase', 'oa/purchase/index', '', 1, 0, 'C', '0', '0', 'oa:purchase:list', 'shopping', 'admin', sysdate(), '', NULL, '采购管理菜单');
INSERT INTO sys_menu VALUES('2004', '项目管理', '2000', '4', 'project', 'oa/project/index', '', 1, 0, 'C', '0', '0', 'oa:project:list', 'clipboard', 'admin', sysdate(), '', NULL, '项目管理菜单');
INSERT INTO sys_menu VALUES('2005', '报销管理', '2000', '5', 'expense', 'oa/expense/index', '', 1, 0, 'C', '0', '0', 'oa:expense:list', 'money', 'admin', sysdate(), '', NULL, '报销管理菜单');
INSERT INTO sys_menu VALUES('2006', '发票管理', '2000', '6', 'invoice', 'oa/invoice/index', '', 1, 0, 'C', '0', '0', 'oa:invoice:list', 'bill', 'admin', sysdate(), '', NULL, '发票管理菜单');
INSERT INTO sys_menu VALUES('2007', '工单管理', '2000', '7', 'ticket', 'oa/ticket/index', '', 1, 0, 'C', '0', '0', 'oa:ticket:list', 'message', 'admin', sysdate(), '', NULL, '工单管理菜单');
INSERT INTO sys_menu VALUES('2008', '借款管理', '2000', '8', 'loan', 'oa/loan/index', '', 1, 0, 'C', '0', '0', 'oa:loan:list', 'money', 'admin', sysdate(), '', NULL, '借款管理菜单');

-- 二级菜单 - 行政办公
INSERT INTO sys_menu VALUES('2010', '请假管理', '2000', '10', 'leaves', 'oa/leaves/index', '', 1, 0, 'C', '0', '0', 'oa:leaves:list', 'date', 'admin', sysdate(), '', NULL, '请假管理菜单');
INSERT INTO sys_menu VALUES('2011', '出差管理', '2000', '11', 'trips', 'oa/trips/index', '', 1, 0, 'C', '0', '0', 'oa:trips:list', 'guide', 'admin', sysdate(), '', NULL, '出差管理菜单');
INSERT INTO sys_menu VALUES('2012', '外出管理', '2000', '12', 'outs', 'oa/outs/index', '', 1, 0, 'C', '0', '0', 'oa:outs:list', 'exit-fullscreen', 'admin', sysdate(), '', NULL, '外出管理菜单');
INSERT INTO sys_menu VALUES('2013', '加班管理', '2000', '13', 'overtimes', 'oa/overtimes/index', '', 1, 0, 'C', '0', '0', 'oa:overtimes:list', 'time', 'admin', sysdate(), '', NULL, '加班管理菜单');
INSERT INTO sys_menu VALUES('2014', '审批管理', '2000', '14', 'approve', 'oa/approve/index', '', 1, 0, 'C', '0', '0', 'oa:approve:list', 'checkbox', 'admin', sysdate(), '', NULL, '审批管理菜单');
INSERT INTO sys_menu VALUES('2015', '考勤管理', '2000', '15', 'attendance', 'oa/attendance/index', '', 1, 0, 'C', '0', '0', 'oa:attendance:list', 'time-range', 'admin', sysdate(), '', NULL, '考勤管理菜单');

-- 二级菜单 - 日常工作
INSERT INTO sys_menu VALUES('2020', '工作计划', '2000', '20', 'plan', 'oa/plan/index', '', 1, 0, 'C', '0', '0', 'oa:plan:list', 'edit', 'admin', sysdate(), '', NULL, '工作计划菜单');
INSERT INTO sys_menu VALUES('2021', '日程管理', '2000', '21', 'schedule', 'oa/schedule/index', '', 1, 0, 'C', '0', '0', 'oa:schedule:list', 'date', 'admin', sysdate(), '', NULL, '日程管理菜单');
INSERT INTO sys_menu VALUES('2022', '工作日志', '2000', '22', 'work', 'oa/work/index', '', 1, 0, 'C', '0', '0', 'oa:work:list', 'log', 'admin', sysdate(), '', NULL, '工作日志菜单');
INSERT INTO sys_menu VALUES('2023', '备忘录', '2000', '23', 'note', 'oa/note/index', '', 1, 0, 'C', '0', '0', 'oa:note:list', 'form', 'admin', sysdate(), '', NULL, '备忘录菜单');

-- 二级菜单 - 资产行政
INSERT INTO sys_menu VALUES('2030', '资产管理', '2000', '30', 'property', 'oa/property/index', '', 1, 0, 'C', '0', '0', 'oa:property:list', 'component', 'admin', sysdate(), '', NULL, '资产管理菜单');
INSERT INTO sys_menu VALUES('2031', '会议室', '2000', '31', 'meetingRoom', 'oa/meetingRoom/index', '', 1, 0, 'C', '0', '0', 'oa:meetingRoom:list', 'row', 'admin', sysdate(), '', NULL, '会议室菜单');
INSERT INTO sys_menu VALUES('2032', '会议预约', '2000', '32', 'meetingOrder', 'oa/meetingOrder/index', '', 1, 0, 'C', '0', '0', 'oa:meetingOrder:list', 'date-range', 'admin', sysdate(), '', NULL, '会议预约菜单');
INSERT INTO sys_menu VALUES('2033', '车辆管理', '2000', '33', 'car', 'oa/car/index', '', 1, 0, 'C', '0', '0', 'oa:car:list', 'redis', 'admin', sysdate(), '', NULL, '车辆管理菜单');
INSERT INTO sys_menu VALUES('2034', '网盘管理', '2000', '34', 'disk', 'oa/disk/index', '', 1, 0, 'C', '0', '0', 'oa:disk:list', 'upload', 'admin', sysdate(), '', NULL, '网盘管理菜单');

-- 二级菜单 - 产品管理
INSERT INTO sys_menu VALUES('2035', '产品管理', '2000', '35', 'product', 'oa/product/index', '', 1, 0, 'C', '0', '0', 'oa:product:list', 'shopping', 'admin', sysdate(), '', NULL, '产品管理菜单');

-- 二级菜单 - 人事管理
INSERT INTO sys_menu VALUES('2040', '部门管理', '2000', '40', 'oaDepartment', 'oa/department/index', '', 1, 0, 'C', '0', '0', 'oa:department:list', 'tree', 'admin', sysdate(), '', NULL, 'OA部门管理菜单');
INSERT INTO sys_menu VALUES('2041', '职位管理', '2000', '41', 'position', 'oa/position/index', '', 1, 0, 'C', '0', '0', 'oa:position:list', 'post', 'admin', sysdate(), '', NULL, '职位管理菜单');
INSERT INTO sys_menu VALUES('2042', '人才管理', '2000', '42', 'talent', 'oa/talent/index', '', 1, 0, 'C', '0', '0', 'oa:talent:list', 'user', 'admin', sysdate(), '', NULL, '人才管理菜单');
INSERT INTO sys_menu VALUES('2043', '档案管理', '2000', '43', 'adminProfiles', 'oa/adminProfiles/index', '', 1, 0, 'C', '0', '0', 'oa:adminProfiles:list', 'documentation', 'admin', sysdate(), '', NULL, '档案管理菜单');
INSERT INTO sys_menu VALUES('2044', '劳动合同', '2000', '44', 'laborContract', 'oa/laborContract/index', '', 1, 0, 'C', '0', '0', 'oa:laborContract:list', 'validCode', 'admin', sysdate(), '', NULL, '劳动合同菜单');
INSERT INTO sys_menu VALUES('2045', '离职管理', '2000', '45', 'personalQuit', 'oa/personalQuit/index', '', 1, 0, 'C', '0', '0', 'oa:personalQuit:list', 'logininfor', 'admin', sysdate(), '', NULL, '离职管理菜单');
INSERT INTO sys_menu VALUES('2046', '奖惩管理', '2000', '46', 'rewards', 'oa/rewards/index', '', 1, 0, 'C', '0', '0', 'oa:rewards:list', 'star', 'admin', sysdate(), '', NULL, '奖惩管理菜单');
INSERT INTO sys_menu VALUES('2047', '关怀管理', '2000', '47', 'care', 'oa/care/index', '', 1, 0, 'C', '0', '0', 'oa:care:list', 'email', 'admin', sysdate(), '', NULL, '关怀管理菜单');

-- 二级菜单 - 审批流程
INSERT INTO sys_menu VALUES('2050', '审批流程', '2000', '50', 'flow', 'oa/flow/index', '', 1, 0, 'C', '0', '0', 'oa:flow:list', 'cascader', 'admin', sysdate(), '', NULL, '审批流程菜单');

-- 二级菜单 - 系统设置
INSERT INTO sys_menu VALUES('2055', '公告管理', '2000', '55', 'oaArticle', 'oa/article/index', '', 1, 0, 'C', '0', '0', 'oa:article:list', 'message', 'admin', sysdate(), '', NULL, 'OA公告管理菜单');
INSERT INTO sys_menu VALUES('2056', '消息管理', '2000', '56', 'oaMessage', 'oa/message/index', '', 1, 0, 'C', '0', '0', 'oa:message:list', 'email', 'admin', sysdate(), '', NULL, '消息管理菜单');
INSERT INTO sys_menu VALUES('2057', 'OA配置', '2000', '57', 'oaConfig', 'oa/config/index', '', 1, 0, 'C', '0', '0', 'oa:config:list', 'system', 'admin', sysdate(), '', NULL, 'OA配置菜单');
INSERT INTO sys_menu VALUES('2058', '企业信息', '2000', '58', 'enterprise', 'oa/enterprise/index', '', 1, 0, 'C', '0', '0', 'oa:enterprise:list', 'international', 'admin', sysdate(), '', NULL, '企业信息菜单');
INSERT INTO sys_menu VALUES('2059', '文件管理', '2000', '59', 'oaFile', 'oa/file/index', '', 1, 0, 'C', '0', '0', 'oa:file:list', 'upload', 'admin', sysdate(), '', NULL, 'OA文件管理菜单');

-- OA按钮权限 (客户管理)
INSERT INTO sys_menu VALUES('2100', '客户查询', '2001', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:customer:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2101', '客户新增', '2001', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:customer:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2102', '客户修改', '2001', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:customer:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2103', '客户删除', '2001', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:customer:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2104', '客户导出', '2001', '5', '', '', '', 1, 0, 'F', '0', '0', 'oa:customer:export', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (合同管理)
INSERT INTO sys_menu VALUES('2110', '合同查询', '2002', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:contract:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2111', '合同新增', '2002', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:contract:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2112', '合同修改', '2002', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:contract:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2113', '合同删除', '2002', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:contract:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (项目管理)
INSERT INTO sys_menu VALUES('2120', '项目查询', '2004', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:project:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2121', '项目新增', '2004', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:project:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2122', '项目修改', '2004', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:project:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2123', '项目删除', '2004', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:project:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (报销管理)
INSERT INTO sys_menu VALUES('2130', '报销查询', '2005', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:expense:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2131', '报销新增', '2005', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:expense:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2132', '报销修改', '2005', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:expense:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2133', '报销删除', '2005', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:expense:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (请假管理)
INSERT INTO sys_menu VALUES('2140', '请假查询', '2010', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:leaves:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2141', '请假新增', '2010', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:leaves:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2142', '请假修改', '2010', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:leaves:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2143', '请假删除', '2010', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:leaves:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (审批管理)
INSERT INTO sys_menu VALUES('2150', '审批查询', '2014', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:approve:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2151', '审批新增', '2014', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:approve:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2152', '审批修改', '2014', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:approve:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2153', '审批删除', '2014', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:approve:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (考勤管理)
INSERT INTO sys_menu VALUES('2160', '考勤查询', '2015', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendance:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2161', '考勤新增', '2015', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendance:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2162', '考勤修改', '2015', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendance:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2163', '考勤删除', '2015', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendance:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (工作计划)
INSERT INTO sys_menu VALUES('2170', '计划查询', '2020', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:plan:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2171', '计划新增', '2020', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:plan:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2172', '计划修改', '2020', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:plan:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2173', '计划删除', '2020', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:plan:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (工作日志)
INSERT INTO sys_menu VALUES('2180', '日志查询', '2022', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:work:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2181', '日志新增', '2022', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:work:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2182', '日志修改', '2022', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:work:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2183', '日志删除', '2022', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:work:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (资产管理)
INSERT INTO sys_menu VALUES('2190', '资产查询', '2030', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:property:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2191', '资产新增', '2030', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:property:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2192', '资产修改', '2030', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:property:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2193', '资产删除', '2030', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:property:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (审批流程)
INSERT INTO sys_menu VALUES('2200', '流程查询', '2050', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:flow:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2201', '流程新增', '2050', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:flow:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2202', '流程修改', '2050', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:flow:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2203', '流程删除', '2050', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:flow:remove', '#', 'admin', sysdate(), '', NULL, '');

-- OA按钮权限 (人才管理)
INSERT INTO sys_menu VALUES('2210', '人才查询', '2042', '1', '', '', '', 1, 0, 'F', '0', '0', 'oa:talent:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2211', '人才新增', '2042', '2', '', '', '', 1, 0, 'F', '0', '0', 'oa:talent:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2212', '人才修改', '2042', '3', '', '', '', 1, 0, 'F', '0', '0', 'oa:talent:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2213', '人才删除', '2042', '4', '', '', '', 1, 0, 'F', '0', '0', 'oa:talent:remove', '#', 'admin', sysdate(), '', NULL, '');


-- =============================================
-- 二、CMS内容管理系统菜单 (3000-3499)
-- =============================================

-- 一级目录
INSERT INTO sys_menu VALUES('3000', 'CMS内容', '0', '6', 'cms', NULL, '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', sysdate(), '', NULL, 'CMS内容管理系统目录');

-- 二级菜单 - 内容管理
INSERT INTO sys_menu VALUES('3001', '文章管理', '3000', '1', 'article', 'cms/article/index', '', 1, 0, 'C', '0', '0', 'cms:article:list', 'edit', 'admin', sysdate(), '', NULL, '文章管理菜单');
INSERT INTO sys_menu VALUES('3002', '文章分类', '3000', '2', 'articleCate', 'cms/articleCate/index', '', 1, 0, 'C', '0', '0', 'cms:articleCate:list', 'tree-table', 'admin', sysdate(), '', NULL, '文章分类菜单');
INSERT INTO sys_menu VALUES('3003', '图集管理', '3000', '3', 'gallery', 'cms/gallery/index', '', 1, 0, 'C', '0', '0', 'cms:gallery:list', 'image', 'admin', sysdate(), '', NULL, '图集管理菜单');
INSERT INTO sys_menu VALUES('3004', '图集分类', '3000', '4', 'galleryCate', 'cms/galleryCate/index', '', 1, 0, 'C', '0', '0', 'cms:galleryCate:list', 'tree-table', 'admin', sysdate(), '', NULL, '图集分类菜单');
INSERT INTO sys_menu VALUES('3005', '商品管理', '3000', '5', 'goods', 'cms/goods/index', '', 1, 0, 'C', '0', '0', 'cms:goods:list', 'shopping', 'admin', sysdate(), '', NULL, '商品管理菜单');
INSERT INTO sys_menu VALUES('3006', '商品分类', '3000', '6', 'goodsCate', 'cms/goodsCate/index', '', 1, 0, 'C', '0', '0', 'cms:goodsCate:list', 'tree-table', 'admin', sysdate(), '', NULL, '商品分类菜单');
INSERT INTO sys_menu VALUES('3007', '单页管理', '3000', '7', 'pages', 'cms/pages/index', '', 1, 0, 'C', '0', '0', 'cms:pages:list', 'form', 'admin', sysdate(), '', NULL, '单页管理菜单');

-- 二级菜单 - 站点管理
INSERT INTO sys_menu VALUES('3010', '导航管理', '3000', '10', 'nav', 'cms/nav/index', '', 1, 0, 'C', '0', '0', 'cms:nav:list', 'list', 'admin', sysdate(), '', NULL, '导航管理菜单');
INSERT INTO sys_menu VALUES('3011', '轮播管理', '3000', '11', 'slide', 'cms/slide/index', '', 1, 0, 'C', '0', '0', 'cms:slide:list', 'carousel', 'admin', sysdate(), '', NULL, '轮播管理菜单');
INSERT INTO sys_menu VALUES('3012', '友情链接', '3000', '12', 'links', 'cms/links/index', '', 1, 0, 'C', '0', '0', 'cms:links:list', 'link', 'admin', sysdate(), '', NULL, '友情链接菜单');
INSERT INTO sys_menu VALUES('3013', '关键词管理', '3000', '13', 'keywords', 'cms/keywords/index', '', 1, 0, 'C', '0', '0', 'cms:keywords:list', 'search', 'admin', sysdate(), '', NULL, '关键词管理菜单');
INSERT INTO sys_menu VALUES('3014', '站点地图', '3000', '14', 'sitemap', 'cms/sitemap/index', '', 1, 0, 'C', '0', '0', 'cms:sitemap:list', 'international', 'admin', sysdate(), '', NULL, '站点地图菜单');

-- 二级菜单 - 用户管理
INSERT INTO sys_menu VALUES('3020', 'CMS用户', '3000', '20', 'cmsUser', 'cms/user/index', '', 1, 0, 'C', '0', '0', 'cms:user:list', 'user', 'admin', sysdate(), '', NULL, 'CMS用户菜单');
INSERT INTO sys_menu VALUES('3021', '用户等级', '3000', '21', 'userLevel', 'cms/userLevel/index', '', 1, 0, 'C', '0', '0', 'cms:userLevel:list', 'star', 'admin', sysdate(), '', NULL, '用户等级菜单');

-- 二级菜单 - 系统设置
INSERT INTO sys_menu VALUES('3030', '文件管理', '3000', '30', 'cmsFile', 'cms/file/index', '', 1, 0, 'C', '0', '0', 'cms:file:list', 'upload', 'admin', sysdate(), '', NULL, 'CMS文件管理菜单');
INSERT INTO sys_menu VALUES('3031', 'CMS配置', '3000', '31', 'cmsConfig', 'cms/config/index', '', 1, 0, 'C', '0', '0', 'cms:config:list', 'system', 'admin', sysdate(), '', NULL, 'CMS配置菜单');

-- CMS按钮权限 (文章管理)
INSERT INTO sys_menu VALUES('3100', '文章查询', '3001', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:article:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3101', '文章新增', '3001', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:article:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3102', '文章修改', '3001', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:article:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3103', '文章删除', '3001', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:article:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3104', '文章导出', '3001', '5', '', '', '', 1, 0, 'F', '0', '0', 'cms:article:export', '#', 'admin', sysdate(), '', NULL, '');

-- CMS按钮权限 (图集管理)
INSERT INTO sys_menu VALUES('3110', '图集查询', '3003', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:gallery:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3111', '图集新增', '3003', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:gallery:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3112', '图集修改', '3003', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:gallery:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3113', '图集删除', '3003', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:gallery:remove', '#', 'admin', sysdate(), '', NULL, '');

-- CMS按钮权限 (商品管理)
INSERT INTO sys_menu VALUES('3120', '商品查询', '3005', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:goods:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3121', '商品新增', '3005', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:goods:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3122', '商品修改', '3005', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:goods:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3123', '商品删除', '3005', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:goods:remove', '#', 'admin', sysdate(), '', NULL, '');

-- CMS按钮权限 (单页管理)
INSERT INTO sys_menu VALUES('3130', '单页查询', '3007', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:pages:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3131', '单页新增', '3007', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:pages:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3132', '单页修改', '3007', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:pages:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3133', '单页删除', '3007', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:pages:remove', '#', 'admin', sysdate(), '', NULL, '');

-- CMS按钮权限 (导航管理)
INSERT INTO sys_menu VALUES('3140', '导航查询', '3010', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:nav:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3141', '导航新增', '3010', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:nav:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3142', '导航修改', '3010', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:nav:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3143', '导航删除', '3010', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:nav:remove', '#', 'admin', sysdate(), '', NULL, '');

-- CMS按钮权限 (轮播管理)
INSERT INTO sys_menu VALUES('3150', '轮播查询', '3011', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:slide:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3151', '轮播新增', '3011', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:slide:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3152', '轮播修改', '3011', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:slide:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3153', '轮播删除', '3011', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:slide:remove', '#', 'admin', sysdate(), '', NULL, '');

-- CMS按钮权限 (友情链接)
INSERT INTO sys_menu VALUES('3160', '链接查询', '3012', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:links:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3161', '链接新增', '3012', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:links:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3162', '链接修改', '3012', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:links:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3163', '链接删除', '3012', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:links:remove', '#', 'admin', sysdate(), '', NULL, '');

-- CMS按钮权限 (CMS用户)
INSERT INTO sys_menu VALUES('3170', '用户查询', '3020', '1', '', '', '', 1, 0, 'F', '0', '0', 'cms:user:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3171', '用户新增', '3020', '2', '', '', '', 1, 0, 'F', '0', '0', 'cms:user:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3172', '用户修改', '3020', '3', '', '', '', 1, 0, 'F', '0', '0', 'cms:user:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('3173', '用户删除', '3020', '4', '', '', '', 1, 0, 'F', '0', '0', 'cms:user:remove', '#', 'admin', sysdate(), '', NULL, '');


-- =============================================
-- 三、DEV项目管理系统菜单 (4000-4499)
-- =============================================

-- 一级目录
INSERT INTO sys_menu VALUES('4000', 'DEV项目', '0', '7', 'dev', NULL, '', 1, 0, 'M', '0', '0', '', 'clipboard', 'admin', sysdate(), '', NULL, 'DEV项目管理系统目录');

-- 二级菜单 - 产品项目
INSERT INTO sys_menu VALUES('4001', '产品管理', '4000', '1', 'devProduct', 'dev/product/index', '', 1, 0, 'C', '0', '0', 'dev:product:list', 'shopping', 'admin', sysdate(), '', NULL, '产品管理菜单');
INSERT INTO sys_menu VALUES('4002', '项目管理', '4000', '2', 'devProject', 'dev/project/index', '', 1, 0, 'C', '0', '0', 'dev:project:list', 'clipboard', 'admin', sysdate(), '', NULL, '项目管理菜单');
INSERT INTO sys_menu VALUES('4003', '任务管理', '4000', '3', 'task', 'dev/task/index', '', 1, 0, 'C', '0', '0', 'dev:task:list', 'checkbox', 'admin', sysdate(), '', NULL, '任务管理菜单');
INSERT INTO sys_menu VALUES('4004', '任务分类', '4000', '4', 'taskCate', 'dev/taskCate/index', '', 1, 0, 'C', '0', '0', 'dev:taskCate:list', 'tree-table', 'admin', sysdate(), '', NULL, '任务分类菜单');

-- 二级菜单 - 文档知识
INSERT INTO sys_menu VALUES('4010', '文档管理', '4000', '10', 'document', 'dev/document/index', '', 1, 0, 'C', '0', '0', 'dev:document:list', 'documentation', 'admin', sysdate(), '', NULL, '文档管理菜单');
INSERT INTO sys_menu VALUES('4011', '知识管理', '4000', '11', 'knowledge', 'dev/knowledge/index', '', 1, 0, 'C', '0', '0', 'dev:knowledge:list', 'education', 'admin', sysdate(), '', NULL, '知识管理菜单');
INSERT INTO sys_menu VALUES('4012', '知识分类', '4000', '12', 'knowledgeCate', 'dev/knowledgeCate/index', '', 1, 0, 'C', '0', '0', 'dev:knowledgeCate:list', 'tree-table', 'admin', sysdate(), '', NULL, '知识分类菜单');

-- 二级菜单 - 日常工作
INSERT INTO sys_menu VALUES('4020', '日程管理', '4000', '20', 'devSchedule', 'dev/schedule/index', '', 1, 0, 'C', '0', '0', 'dev:schedule:list', 'date', 'admin', sysdate(), '', NULL, '日程管理菜单');
INSERT INTO sys_menu VALUES('4021', '工作日志', '4000', '21', 'devWork', 'dev/work/index', '', 1, 0, 'C', '0', '0', 'dev:work:list', 'log', 'admin', sysdate(), '', NULL, '工作日志菜单');
INSERT INTO sys_menu VALUES('4022', '备忘录', '4000', '22', 'devNote', 'dev/note/index', '', 1, 0, 'C', '0', '0', 'dev:note:list', 'form', 'admin', sysdate(), '', NULL, '备忘录菜单');

-- 二级菜单 - 系统设置
INSERT INTO sys_menu VALUES('4030', '消息管理', '4000', '30', 'devMessage', 'dev/message/index', '', 1, 0, 'C', '0', '0', 'dev:message:list', 'email', 'admin', sysdate(), '', NULL, '消息管理菜单');
INSERT INTO sys_menu VALUES('4031', '文件管理', '4000', '31', 'devFile', 'dev/file/index', '', 1, 0, 'C', '0', '0', 'dev:file:list', 'upload', 'admin', sysdate(), '', NULL, '文件管理菜单');
INSERT INTO sys_menu VALUES('4032', 'DEV配置', '4000', '32', 'devConfig', 'dev/config/index', '', 1, 0, 'C', '0', '0', 'dev:config:list', 'system', 'admin', sysdate(), '', NULL, 'DEV配置菜单');

-- DEV按钮权限 (产品管理)
INSERT INTO sys_menu VALUES('4100', '产品查询', '4001', '1', '', '', '', 1, 0, 'F', '0', '0', 'dev:product:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4101', '产品新增', '4001', '2', '', '', '', 1, 0, 'F', '0', '0', 'dev:product:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4102', '产品修改', '4001', '3', '', '', '', 1, 0, 'F', '0', '0', 'dev:product:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4103', '产品删除', '4001', '4', '', '', '', 1, 0, 'F', '0', '0', 'dev:product:remove', '#', 'admin', sysdate(), '', NULL, '');

-- DEV按钮权限 (项目管理)
INSERT INTO sys_menu VALUES('4110', '项目查询', '4002', '1', '', '', '', 1, 0, 'F', '0', '0', 'dev:project:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4111', '项目新增', '4002', '2', '', '', '', 1, 0, 'F', '0', '0', 'dev:project:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4112', '项目修改', '4002', '3', '', '', '', 1, 0, 'F', '0', '0', 'dev:project:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4113', '项目删除', '4002', '4', '', '', '', 1, 0, 'F', '0', '0', 'dev:project:remove', '#', 'admin', sysdate(), '', NULL, '');

-- DEV按钮权限 (任务管理)
INSERT INTO sys_menu VALUES('4120', '任务查询', '4003', '1', '', '', '', 1, 0, 'F', '0', '0', 'dev:task:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4121', '任务新增', '4003', '2', '', '', '', 1, 0, 'F', '0', '0', 'dev:task:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4122', '任务修改', '4003', '3', '', '', '', 1, 0, 'F', '0', '0', 'dev:task:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4123', '任务删除', '4003', '4', '', '', '', 1, 0, 'F', '0', '0', 'dev:task:remove', '#', 'admin', sysdate(), '', NULL, '');

-- DEV按钮权限 (文档管理)
INSERT INTO sys_menu VALUES('4130', '文档查询', '4010', '1', '', '', '', 1, 0, 'F', '0', '0', 'dev:document:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4131', '文档新增', '4010', '2', '', '', '', 1, 0, 'F', '0', '0', 'dev:document:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4132', '文档修改', '4010', '3', '', '', '', 1, 0, 'F', '0', '0', 'dev:document:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4133', '文档删除', '4010', '4', '', '', '', 1, 0, 'F', '0', '0', 'dev:document:remove', '#', 'admin', sysdate(), '', NULL, '');

-- DEV按钮权限 (知识管理)
INSERT INTO sys_menu VALUES('4140', '知识查询', '4011', '1', '', '', '', 1, 0, 'F', '0', '0', 'dev:knowledge:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4141', '知识新增', '4011', '2', '', '', '', 1, 0, 'F', '0', '0', 'dev:knowledge:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4142', '知识修改', '4011', '3', '', '', '', 1, 0, 'F', '0', '0', 'dev:knowledge:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4143', '知识删除', '4011', '4', '', '', '', 1, 0, 'F', '0', '0', 'dev:knowledge:remove', '#', 'admin', sysdate(), '', NULL, '');

-- DEV按钮权限 (工作日志)
INSERT INTO sys_menu VALUES('4150', '日志查询', '4021', '1', '', '', '', 1, 0, 'F', '0', '0', 'dev:work:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4151', '日志新增', '4021', '2', '', '', '', 1, 0, 'F', '0', '0', 'dev:work:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4152', '日志修改', '4021', '3', '', '', '', 1, 0, 'F', '0', '0', 'dev:work:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('4153', '日志删除', '4021', '4', '', '', '', 1, 0, 'F', '0', '0', 'dev:work:remove', '#', 'admin', sysdate(), '', NULL, '');


-- =============================================
-- 四、Blog博客系统菜单 (5000-5499)
-- =============================================

-- 一级目录
INSERT INTO sys_menu VALUES('5000', 'Blog博客', '0', '8', 'blog', NULL, '', 1, 0, 'M', '0', '0', '', 'edit', 'admin', sysdate(), '', NULL, 'Blog博客系统目录');

-- 二级菜单 - 内容管理
INSERT INTO sys_menu VALUES('5001', '文章管理', '5000', '1', 'blogArticle', 'blog/article/index', '', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', sysdate(), '', NULL, '博客文章管理菜单');
INSERT INTO sys_menu VALUES('5002', '文章分类', '5000', '2', 'blogArticleCate', 'blog/articleCate/index', '', 1, 0, 'C', '0', '0', 'blog:articleCate:list', 'tree-table', 'admin', sysdate(), '', NULL, '文章分类菜单');
INSERT INTO sys_menu VALUES('5003', '单页管理', '5000', '3', 'blogPages', 'blog/pages/index', '', 1, 0, 'C', '0', '0', 'blog:pages:list', 'form', 'admin', sysdate(), '', NULL, '单页管理菜单');
INSERT INTO sys_menu VALUES('5004', '电子书管理', '5000', '4', 'book', 'blog/book/index', '', 1, 0, 'C', '0', '0', 'blog:book:list', 'education', 'admin', sysdate(), '', NULL, '电子书管理菜单');
INSERT INTO sys_menu VALUES('5005', '文档管理', '5000', '5', 'blogDoc', 'blog/doc/index', '', 1, 0, 'C', '0', '0', 'blog:doc:list', 'documentation', 'admin', sysdate(), '', NULL, '文档管理菜单');

-- 二级菜单 - 站点管理
INSERT INTO sys_menu VALUES('5010', '导航管理', '5000', '10', 'blogNav', 'blog/nav/index', '', 1, 0, 'C', '0', '0', 'blog:nav:list', 'list', 'admin', sysdate(), '', NULL, '博客导航管理菜单');
INSERT INTO sys_menu VALUES('5011', '轮播管理', '5000', '11', 'blogSlide', 'blog/slide/index', '', 1, 0, 'C', '0', '0', 'blog:slide:list', 'carousel', 'admin', sysdate(), '', NULL, '博客轮播管理菜单');
INSERT INTO sys_menu VALUES('5012', '友情链接', '5000', '12', 'blogLinks', 'blog/links/index', '', 1, 0, 'C', '0', '0', 'blog:links:list', 'link', 'admin', sysdate(), '', NULL, '博客友情链接菜单');
INSERT INTO sys_menu VALUES('5013', '关键词管理', '5000', '13', 'blogKeywords', 'blog/keywords/index', '', 1, 0, 'C', '0', '0', 'blog:keywords:list', 'search', 'admin', sysdate(), '', NULL, '博客关键词管理菜单');
INSERT INTO sys_menu VALUES('5014', '互动管理', '5000', '14', 'action', 'blog/action/index', '', 1, 0, 'C', '0', '0', 'blog:action:list', 'star', 'admin', sysdate(), '', NULL, '互动管理菜单');

-- 二级菜单 - 用户管理
INSERT INTO sys_menu VALUES('5020', '博客用户', '5000', '20', 'blogUser', 'blog/user/index', '', 1, 0, 'C', '0', '0', 'blog:user:list', 'user', 'admin', sysdate(), '', NULL, '博客用户菜单');
INSERT INTO sys_menu VALUES('5021', '用户等级', '5000', '21', 'blogUserLevel', 'blog/userLevel/index', '', 1, 0, 'C', '0', '0', 'blog:userLevel:list', 'star', 'admin', sysdate(), '', NULL, '博客用户等级菜单');

-- 二级菜单 - 系统设置
INSERT INTO sys_menu VALUES('5030', '站点地图', '5000', '30', 'blogSitemap', 'blog/sitemap/index', '', 1, 0, 'C', '0', '0', 'blog:sitemap:list', 'international', 'admin', sysdate(), '', NULL, '博客站点地图菜单');
INSERT INTO sys_menu VALUES('5031', '文件管理', '5000', '31', 'blogFile', 'blog/file/index', '', 1, 0, 'C', '0', '0', 'blog:file:list', 'upload', 'admin', sysdate(), '', NULL, '博客文件管理菜单');
INSERT INTO sys_menu VALUES('5032', '博客配置', '5000', '32', 'blogConfig', 'blog/config/index', '', 1, 0, 'C', '0', '0', 'blog:config:list', 'system', 'admin', sysdate(), '', NULL, '博客配置菜单');

-- Blog按钮权限 (文章管理)
INSERT INTO sys_menu VALUES('5100', '文章查询', '5001', '1', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5101', '文章新增', '5001', '2', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5102', '文章修改', '5001', '3', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5103', '文章删除', '5001', '4', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5104', '文章导出', '5001', '5', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:export', '#', 'admin', sysdate(), '', NULL, '');

-- Blog按钮权限 (单页管理)
INSERT INTO sys_menu VALUES('5110', '单页查询', '5003', '1', '', '', '', 1, 0, 'F', '0', '0', 'blog:pages:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5111', '单页新增', '5003', '2', '', '', '', 1, 0, 'F', '0', '0', 'blog:pages:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5112', '单页修改', '5003', '3', '', '', '', 1, 0, 'F', '0', '0', 'blog:pages:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5113', '单页删除', '5003', '4', '', '', '', 1, 0, 'F', '0', '0', 'blog:pages:remove', '#', 'admin', sysdate(), '', NULL, '');

-- Blog按钮权限 (电子书管理)
INSERT INTO sys_menu VALUES('5120', '电子书查询', '5004', '1', '', '', '', 1, 0, 'F', '0', '0', 'blog:book:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5121', '电子书新增', '5004', '2', '', '', '', 1, 0, 'F', '0', '0', 'blog:book:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5122', '电子书修改', '5004', '3', '', '', '', 1, 0, 'F', '0', '0', 'blog:book:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5123', '电子书删除', '5004', '4', '', '', '', 1, 0, 'F', '0', '0', 'blog:book:remove', '#', 'admin', sysdate(), '', NULL, '');

-- Blog按钮权限 (导航管理)
INSERT INTO sys_menu VALUES('5130', '导航查询', '5010', '1', '', '', '', 1, 0, 'F', '0', '0', 'blog:nav:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5131', '导航新增', '5010', '2', '', '', '', 1, 0, 'F', '0', '0', 'blog:nav:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5132', '导航修改', '5010', '3', '', '', '', 1, 0, 'F', '0', '0', 'blog:nav:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5133', '导航删除', '5010', '4', '', '', '', 1, 0, 'F', '0', '0', 'blog:nav:remove', '#', 'admin', sysdate(), '', NULL, '');

-- Blog按钮权限 (轮播管理)
INSERT INTO sys_menu VALUES('5140', '轮播查询', '5011', '1', '', '', '', 1, 0, 'F', '0', '0', 'blog:slide:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5141', '轮播新增', '5011', '2', '', '', '', 1, 0, 'F', '0', '0', 'blog:slide:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5142', '轮播修改', '5011', '3', '', '', '', 1, 0, 'F', '0', '0', 'blog:slide:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5143', '轮播删除', '5011', '4', '', '', '', 1, 0, 'F', '0', '0', 'blog:slide:remove', '#', 'admin', sysdate(), '', NULL, '');

-- Blog按钮权限 (友情链接)
INSERT INTO sys_menu VALUES('5150', '链接查询', '5012', '1', '', '', '', 1, 0, 'F', '0', '0', 'blog:links:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5151', '链接新增', '5012', '2', '', '', '', 1, 0, 'F', '0', '0', 'blog:links:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5152', '链接修改', '5012', '3', '', '', '', 1, 0, 'F', '0', '0', 'blog:links:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5153', '链接删除', '5012', '4', '', '', '', 1, 0, 'F', '0', '0', 'blog:links:remove', '#', 'admin', sysdate(), '', NULL, '');

-- Blog按钮权限 (博客用户)
INSERT INTO sys_menu VALUES('5160', '用户查询', '5020', '1', '', '', '', 1, 0, 'F', '0', '0', 'blog:user:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5161', '用户新增', '5020', '2', '', '', '', 1, 0, 'F', '0', '0', 'blog:user:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5162', '用户修改', '5020', '3', '', '', '', 1, 0, 'F', '0', '0', 'blog:user:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('5163', '用户删除', '5020', '4', '', '', '', 1, 0, 'F', '0', '0', 'blog:user:remove', '#', 'admin', sysdate(), '', NULL, '');


-- =============================================
-- 五、角色与菜单关联（admin角色拥有所有权限）
-- =============================================

-- 将所有新菜单分配给admin角色(role_id=1)
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
-- OA菜单
(1, 2000),(1, 2001),(1, 2002),(1, 2003),(1, 2004),(1, 2005),(1, 2006),(1, 2007),(1, 2008),
(1, 2010),(1, 2011),(1, 2012),(1, 2013),(1, 2014),(1, 2015),
(1, 2020),(1, 2021),(1, 2022),(1, 2023),
(1, 2030),(1, 2031),(1, 2032),(1, 2033),(1, 2034),(1, 2035),
(1, 2040),(1, 2041),(1, 2042),(1, 2043),(1, 2044),(1, 2045),(1, 2046),(1, 2047),
(1, 2050),
(1, 2055),(1, 2056),(1, 2057),(1, 2058),(1, 2059),
(1, 2100),(1, 2101),(1, 2102),(1, 2103),(1, 2104),
(1, 2110),(1, 2111),(1, 2112),(1, 2113),
(1, 2120),(1, 2121),(1, 2122),(1, 2123),
(1, 2130),(1, 2131),(1, 2132),(1, 2133),
(1, 2140),(1, 2141),(1, 2142),(1, 2143),
(1, 2150),(1, 2151),(1, 2152),(1, 2153),
(1, 2160),(1, 2161),(1, 2162),(1, 2163),
(1, 2170),(1, 2171),(1, 2172),(1, 2173),
(1, 2180),(1, 2181),(1, 2182),(1, 2183),
(1, 2190),(1, 2191),(1, 2192),(1, 2193),
(1, 2200),(1, 2201),(1, 2202),(1, 2203),
(1, 2210),(1, 2211),(1, 2212),(1, 2213),
-- CMS菜单
(1, 3000),(1, 3001),(1, 3002),(1, 3003),(1, 3004),(1, 3005),(1, 3006),(1, 3007),
(1, 3010),(1, 3011),(1, 3012),(1, 3013),(1, 3014),
(1, 3020),(1, 3021),
(1, 3030),(1, 3031),
(1, 3100),(1, 3101),(1, 3102),(1, 3103),(1, 3104),
(1, 3110),(1, 3111),(1, 3112),(1, 3113),
(1, 3120),(1, 3121),(1, 3122),(1, 3123),
(1, 3130),(1, 3131),(1, 3132),(1, 3133),
(1, 3140),(1, 3141),(1, 3142),(1, 3143),
(1, 3150),(1, 3151),(1, 3152),(1, 3153),
(1, 3160),(1, 3161),(1, 3162),(1, 3163),
(1, 3170),(1, 3171),(1, 3172),(1, 3173),
-- DEV菜单
(1, 4000),(1, 4001),(1, 4002),(1, 4003),(1, 4004),
(1, 4010),(1, 4011),(1, 4012),
(1, 4020),(1, 4021),(1, 4022),
(1, 4030),(1, 4031),(1, 4032),
(1, 4100),(1, 4101),(1, 4102),(1, 4103),
(1, 4110),(1, 4111),(1, 4112),(1, 4113),
(1, 4120),(1, 4121),(1, 4122),(1, 4123),
(1, 4130),(1, 4131),(1, 4132),(1, 4133),
(1, 4140),(1, 4141),(1, 4142),(1, 4143),
(1, 4150),(1, 4151),(1, 4152),(1, 4153),
-- Blog菜单
(1, 5000),(1, 5001),(1, 5002),(1, 5003),(1, 5004),(1, 5005),
(1, 5010),(1, 5011),(1, 5012),(1, 5013),(1, 5014),
(1, 5020),(1, 5021),
(1, 5030),(1, 5031),(1, 5032),
(1, 5100),(1, 5101),(1, 5102),(1, 5103),(1, 5104),
(1, 5110),(1, 5111),(1, 5112),(1, 5113),
(1, 5120),(1, 5121),(1, 5122),(1, 5123),
(1, 5130),(1, 5131),(1, 5132),(1, 5133),
(1, 5140),(1, 5141),(1, 5142),(1, 5143),
(1, 5150),(1, 5151),(1, 5152),(1, 5153),
(1, 5160),(1, 5161),(1, 5162),(1, 5163);
