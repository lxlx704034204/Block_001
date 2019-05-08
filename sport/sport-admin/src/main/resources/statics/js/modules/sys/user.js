$(function () {
    extracted();
});
/*
 jgGrid-jsonReader 需要的分页数据格式
{
    "msg":"success",
    "code":0,
    "page":{
        "totalCount":1,
        "pageSize":10,
        "totalPage":1,
        "currPage":1,
        "list":[
            {
                "userId":1,
                "username":"admin",
                "salt":"YzcmCZNvbXocrsz9dm8e",
                "email":"root@renren.io",
                "mobile":"13612345678",
                "status":1,
                "roleIdList":null,
                "createTime":"2016-11-11 11:11:11",
                "deptId":1,
                "deptName":"人人开源集团"
            }
        ]
    }
}

*/


function extracted() {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [
            {label: '用户ID',   name: 'userId',  index: "user_id", width: 45, key: true},
            {label: '用户名',   name: 'username',                  width: 75},
            {label: '所属部门', name: 'deptName', sortable: false, width: 75},
            {label: '邮箱',     name: 'email',                     width: 90},
            {label: '手机号',   name: 'mobile',                    width: 100},
            {
                label: '状态',  name: 'status',                    width: 60,
                formatter: function (value, options, row) {
                    return value === 0 ?
                        '<span class="label label-danger">禁用</span>' :
                        '<span class="label label-success">正常</span>';
                }
            },
            {label: '创建时间',  name: 'createTime', index: "create_time", width: 85}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
}


var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            username: null
        },
        showList: true,
        title:null,
        roleList:{},
        user:{
            status:1,
            deptId:null,
            deptName:null,
            roleIdList:[]
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.roleList = {};
            vm.user = {deptName:null, deptId:null, status:1, roleIdList:[]};

            //获取角色信息
            this.getRoleList();

            vm.getDept();
        },
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
                }
            })
        },
        update: function () {
            var userId = getSelectedRow();
            if(userId == null){
                return ;
            }

            vm.showList = false;
            vm.title = "修改";

            vm.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },
        permissions: function () {
            var userId = getSelectedRow();
            if(userId == null){
                return ;
            }

            window.location.href=baseURL+"sys/permissions/index/"+userId;
        },
        del: function () {
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function () {
            var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        getUser: function(userId){
            $.get(baseURL + "sys/user/info/"+userId, function(r){
                vm.user = r.user;
                vm.user.password = null;

                vm.getDept();
            });
        },
        getRoleList: function(){
            $.get(baseURL + "sys/role/select", function(r){
                vm.roleList = r.list;
            });
        },
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');



            $("#jqGrid")
                .jqGrid(
                    'setGridParam',     // ① setGridParam 用于设置jqGrid的options选项。返回jqGrid对象
                    {
                        datatype:'json',    //② datatype为指定发送数据的格式；
                        postData:{'username': vm.q.username}, //发送数据 ③ postData为发送请求的数据，以key:value的形式发送，多个参数可以以逗号”,”间隔；
                        page:page           // ④ page 为指定查询结果跳转到第 几 页；
                    }
                ).trigger("reloadGrid"); //重新载入 ⑤ trigger(“reloadGrid”);为重新载入jqGrid表格。  https://www.cnblogs.com/wshsdlau/archive/2013/01/20/2868843.html
        }
    }
});
/*

----http://www.cnblogs.com/lipan/archive/2010/11/25/1887160.html--start---
 jqgrid getGridParam、setGridParam ：
    getGridParam方法：
    　　getGridParam("url")： 获取当前的AJAX的URL
    　　getGridParam("sortname")：排序的字段
    　　getGridParam("sortorder")：排序的顺序
    　　getGridParam("selrow")：得到选中行的ID
    　　getGridParam("page")：当前的页数
    　　getGridParam("rowNum")：当前有多少行
    　　getGridParam("datatype")：得到当前的datatype
    　　getGridParam("records")：得到总记录数
    　　getGridParam("selarrrow")：可以多选时，返回选中行的ID
    setGridParam方法：
    　　setGridParam({url:newvalue})：可以设置一个grid的ajax url，可配合trigger("reloadGrid")使用
    　　setGridParam({sortname:newvalue})：设置排序的字段
    　　setGridParam({sortorder:newvalue})：设置排序的顺序asc or desc
    　　setGridParam({page:newvalue})：设置翻到第几页
    　　setGridParam({rowNum:newvalue})：设置当前每页显示的行数
    　　setGridParam({datatype:newvalue})：设置新的datatype(xml,json)

    形式2：
        jQuery('#tableID').jqGrid('getGridParam','url'))
    　　 jQuery("#tableID").jqGrid('setGridParam',{page:2}).trigger("reloadGrid")

----http://www.cnblogs.com/lipan/archive/2010/11/25/1887160.html--end---



*/