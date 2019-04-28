$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sport/student/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '学号', name: 'stuNumber', index: 'stu_number', width: 80 }, 			
			{ label: '联系方式', name: 'mobile', index: 'mobile', width: 80 }, 			
			{ label: '学生姓名', name: 'realname', index: 'realname', width: 80 }, 			
			{ label: '性别', name: 'gender', index: 'gender', width: 80 },
			{ label: '出生日期', name: 'birthday', index: 'birthday', width: 80 }, 			
			{ label: '家长姓名', name: 'familyName', index: 'family_name', width: 80 }, 			
			{ label: '家长联系方式', name: 'familyMobile', index: 'family_mobile', width: 80 }, 			
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }, 			
			{ label: '注册/报名时间', name: 'registerTime', index: 'register_time', width: 100 },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '修改时间', name: 'updateTime', index: 'update_time', width: 80 },
            { label: '操作', name: 'id', index: 'state', width: 120, edittype:"button",
                formatter: function(cellVal,grid,rows,id){
                    let addGradeBtn = "<button class='btn btn-primary ' onclick='vm.addGrade("+cellVal+")' >添加数据</button>" ;
                    //let queryGrade =  "<button class='btn btn-warning ' onclick=\"queryDetail(" + rows+","+cellVal + ")\">查看</button>" ;
                    return addGradeBtn /*+ queryGrade*/;
                }
            }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        // rownumbers: true,
        // rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        shrinkToFit:false,
        autoScroll: true, //开启水平滚动条
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	//$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    })
        //.navGrid('#jqGridPager',{edit:false,add:true,del:false,search:false})
    ;
});



var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
        showGrade: true,
		title: null,
        project: {},
        rowData: {},
		student: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.student = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.student.id == null ? "sport/student/save" : "sport/student/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.student),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "sport/student/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "sport/student/info/"+id, function(r){
                vm.student = r.student;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        getProjectInfo: function(id){
            vm.rowData = getSelectedRowData(id);
            var birthday = vm.rowData.birthday;
            $.get(baseURL + "sport/project/listByBirthday?birthday="+birthday, function(r){
                vm.project = r.data;
                $("#form-project-group").empty();
                $("#form-project-group").append("<div class=\"form-group\">")
                for(i=0; i<r.data.length;i++) {
                    var pro = r.data[i];
                    let input = "<div class='col-sm-3 control-label'>"+pro.projectName+"</div> <div class='col-sm-3'>" +
                        "<input type='hidden'  name='projectId["+i+"]' value='"+pro.id+"'/>" +
                        "<input type='text' class='form-control' "+"name='proGrade["+i+"]' placeholder='请输入'/>" +
                        "</div>"
                    if(i>0 && i%2==0){
                        $("#form-project-group").append("</div><div class=\"form-group\">")
                        $("#form-project-group").append(input);
                    }else{
                        $("#form-project-group").append(input);
                    }
                    if(i==r.data.length-1){
                        $("#form-project-group").append("</div>");
                    }
                }
            });
        },
        addGrade: function(id){
		    vm.showGrade = false;
            vm.getProjectInfo(id);

            layer.open({
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['70%', '450px'], //宽高
                closeBtn: 1, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                title: '添加体测数据',
                content: $("#addGradeId")
            });
        },
        saveAllProjectGrade: function (event) {
            $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                let url = baseURL + "sport/grade/save";
                params.studentId = vm.student.id;
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.student),
                    success: function(r){
                        if(r.code === 0){
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#saveStudentGrade').button('reset');
                            $('#saveStudentGrade').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
            })

        }
	}
});


