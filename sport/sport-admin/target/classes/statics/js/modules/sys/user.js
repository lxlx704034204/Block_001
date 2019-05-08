$(function () {
    jqgridInitPage();
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
//jgGrid文档：
    https://blog.mn886.net/jqGrid/
    https://blog.csdn.net/keke_Xin/article/details/84439023

*/


function jqgridInitPage() {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",               //这个参数指定了jqGrid调用的数据的格式，常用格式有json，xml，local。
        // colNames:["用户ID","用户名","所属部门","邮箱","手机号","状态","创建时间"],  //这个参数指定了jqGrid每列的title，按顺序依次排列，并且可以看出实际上它就是一个字符串数组。
        colModel: [                     //这个参数指定了jqGrid每列的title，按顺序依次排列，并且可以看出实际上它就是一个字符串数组。
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
        // jqgrid中jsonReader使用userdata传递自定义数据内容  https://blog.csdn.net/hgyu/article/details/80614075
        //

        jsonReader: {
            root:    "page.list",
            page:    "page.currPage",
            total:   "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {  //prmNames选项详解： https://my.oschina.net/u/1454202/blog/481403
            page: "page",   // 表示请求页码的参数名称
            rows: "limit",  // 表示请求行数的参数名称
            order: "order"  // 表示采用的排序方式的参数名称 ...
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

        /* vue-reload方法： https://blog.csdn.net/zjl199303/article/details/82655635
        1.场景：在处理列表时，常常有删除一条数据或者新增数据之后需要重新刷新当前页面的需求。
        2.遇到的问题：
                1. 用vue-router重新路由到当前页面，页面是不进行刷新的
                2.采用window.reload()，或者router.go(0)刷新时，整个浏览器进行了重新加载，闪烁，体验不好
        */
        reload: function () {
            vm.showList = true;
            // 条件查询  点击查询按钮的时候：
            var page = $("#jqGrid").jqGrid('getGridParam', 'page'); //'getGridParam'命令 获取当前的页数

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
 ☆ jqgrid 的 getGridParam、setGridParam 用法：
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

        jQuery("grid_id").getGridParam('userData')
        jQuery("grid_id").jqGrid('getGridParam', 'userData')


----http://www.cnblogs.com/lipan/archive/2010/11/25/1887160.html--end---



*/