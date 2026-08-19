var successCallback = function () {};
var el5;
var Authorization = localStorage.getItem("Authorization");
console.log(Authorization);
layui.use(['eleTree', 'common' , 'xnUtil', 'xmSelect', 'laydate'], function () {
    var $ = layui.$
        , form = layui.form
        , eleTree = layui.eleTree
        , common = layui.common;
        var xnUtil = layui.xnUtil;
        var xmSelect = layui.xmSelect;
        var laydate = layui.laydate;
    //部门树
    $("[name='deptName']").on("click", function (e) {
        e.stopPropagation();
        if (!el5) {
            el5 = eleTree.render({
                elem: '.deptName',
                url: ctx + "system/dept/treeData2",
                request: {
                    name: "name",
                    key: "id",
                    children: "children",
                    checked: "checked",
                    disabled: "disabled",
                    isLeaf: "isLeaf"
                },
                response: {
                    statusName: "code",
                    statusCode: 0,
                    dataName: "data"
                },
                defaultExpandAll: true,
                expandOnClickNode: false,
                highlightCurrent: true
            });
            $(".eleTree").css("position", "absolute");
        }
        $(".deptName").toggle();
    });
    eleTree.on("nodeClick(deptNameTree)", function (d) {
        $("[name='deptName']").val(d.data.currentData.name);
        $("[name='deptId']").val(d.data.currentData.id);
        $(".deptName").hide();
    });

    // 渲染字典下拉
    CoreUtil.sendGet('/dev-api/system/dict/data/type/'+"sys_user_sex", {}, function (res) { 
        console.log(res); 
        var sexDataRenderIns = xmSelect.render({
            el: '#sexBox',
            name: 'sex',
            data: res.data,
            layVerify: 'required',
            layVerType: 'tips',
            radio: true,
            clickClose: true,
            model: { icon:'hidden', label: { type: 'text' }},
            prop: {
                name: 'dictLabel',
                value: 'dictCode'
            },
            tips: '请选择性别'
        });
   });
   CoreUtil.sendGet('/dev-api/system/user/'+"", {}, function (res) { 
        console.log(res); 
        remoteDataRenderIns = xmSelect.render({
            el: '#posIdListBox',
            name: 'post',
            data: res.posts,
            layVerify: 'required',
            layVerType: 'tips',
            radio: false,
            prop: {
                name: 'postName',
                value: 'postId'
            },
            tips: '请选择职位'
        });
    });
   CoreUtil.sendGet('/dev-api/system/user/'+"", {}, function (res) { 
        console.log(res); 
        remoteDataRenderIns = xmSelect.render({
            el: '#roleListBox',
            name: 'post',
            data: res.roles,
            layVerify: 'required',
            layVerType: 'tips',
            radio: false,
            prop: {
                name: 'roleName',
                value: 'roleId'
            },
            tips: '请选择角色'
        });
    });


    // admin.req(getProjectUrl() + 'sysOrg/tree', function(res){
        CoreUtil.sendGet('/dev-api/system/user/deptTree/'+"", {}, function (res) { 
        //admin.req( 'tree.json', function(res){
            // 渲染下拉树
            orgTreeRenderIns = xmSelect.render({
                el: '#orgSelectBox',
                name: 'sysEmpParam.orgId',
                height: '250px',
                layVerify: 'required',
                layVerType: 'tips',
                data: res.data,
                // initValue: 1/* editData ? (editData.sysEmpInfo?(editData.sysEmpInfo.orgId?[editData.sysEmpInfo.orgId]:[]):[]):[] */,
                model: {label: {type: 'text'}},
                prop: {
                    name: 'label',
                    value: 'id'
                },
                radio: true,
                clickClose: true,
                tree: {
                    show: true,
                    indent: 15,
                    strict: false,
                    expandedKeys: true
                },
                tips: '请选择机构'
            });
        });

    


    //表单校验
    form.verify({
        pass: [
            /^[\S]{2,25}$/
            , '密码必须2到25位，且不能出现空格'
        ],
        username: function (value, item) {
            var msg;
            if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                msg = '用户名不能有特殊字符';
            }
            if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                msg = '用户名首尾不能出现下划线\'_\'';
            }
            if (/^\d+\d+\d$/.test(value)) {
                msg = '用户名不能全为数字';
            }
            /* $.ajax({
                type: "GET",
                url: ctx + "system/user/checkLoginNameUnique",
                headers: {'Accept': 'application/json', 'Authorization': localStorage.getItem("Authorization")},
                async: false,
                dataType: "json",
                data: {
                    loginName: $("[name='loginName']").val(),
                    userId: $.trim($("[name='userId']").val())
                },
                success: function (res) {
                    if (res != "0") {
                        msg = "登录用户名已存在，请修改！";
                    }
                },
                error: function () {
                    msg = "验证登录名出错！";
                }
            }); */
            return msg;
        }
    });
    //表单提交
    var userId = $.trim($('#userId').val());
    var submitBtnId = userId ? 'btn-edit' : 'btn-add';
    form.render(null, 'form-user');
    form.on('submit(' + submitBtnId + ')', function (data) {
        var roleIds = common.getCheckValues("role");
        var postIds = common.getCheckValues("post");
        data.field.roleIds = roleIds;
        data.field.postIds = postIds;
        data.field.status = data.field.status === "on" ? "1" : "0";
        var url = userId ? (ctx + '/system/user/edit') : (ctx + '/system/user/add');
        $.ajax({
            type: "post",
            url: url,
            data: data.field,
            async: false,
            error: function (re) {
                layui.layer.msg("系统错误");
            },
            success: function (data) {
                console.log("success:", data);
                if (data.code > 0) {
                    layui.layer.msg(data.msg);
                } else {
                    successCallback();
                }
            }
        });
        return false;
    });
});