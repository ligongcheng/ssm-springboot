//设置提示时间
toastr.options.timeOut = 3000;
//全局的ajax访问设置
$.ajaxSetup({

    complete: function (XMLHttpRequest, textStatus) {
        //通过code取得操作状态，并提示
        XMLHttpRequest.success(function (da) {
            if (da.code == 200) {
                toastr.success('操作成功');
            } else if (da.code == 400) {
                toastr.error(da.msg)
            }
        });
        //通过XMLHttpRequest取得响应头，sessionstatus，
        var sessionstatus = XMLHttpRequest.getResponseHeader("session-status");
        if (sessionstatus === "timeout") {
            //如果超时就处理 ，指定要跳转的页面(比如登陆页)
            window.location.replace("/login.html");
        }
    }
});

function permTree(ele, roleId) {
    var rolePermIds;
    $.ajax({
        url: "/rolePermIds",
        type: "get",
        data: {id: roleId},
        async: false,
        success: function (data) {
            rolePermIds = data;
        }
    });
    $.ajax({
        url: "/permTree",
        type: "get",
        success: function (da) {
            $(ele).jstree("destroy");
            $(ele).jstree({
                "core": {
                    'data': da.children
                },
                "plugins": ["checkbox", "wholerow"],
                /*'state': {
                    "opened": true
                }*/
            }).on("loaded.jstree", function (event, data) {
                //$(ele).jstree().open_all();
                $(ele).jstree('select_node', rolePermIds);
            });
        }
    });
}

//初始化侧边菜单
$(function () {
    $('#side-menu').metisMenu();
});


function loadUser(ele) {
    var url = $(ele).attr("url");
    $("#mainPage").load(url, function () {
        var $userTable = $('#userTable');
        var settings = {
            url: "/userList",
            queryParams: function (params) {
                return {
                    pageSize: params.limit,
                    pageNum: params.offset / params.limit + 1,
                    search: params.search
                    //roleName: $(".form").find("input[name='roleName']").val().trim(),
                };
            },
            columns: [{
                checkbox: true,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'username',
                title: '用户名'
            }, {
                field: 'nickname',
                title: '昵称'
            }, {
                field: 'roleName',
                title: '角色'
            }, {
                field: 'age',
                title: '年龄'
            }, {
                field: 'sex',
                title: '性别',
                formatter: function (s) {
                    if (s === 0) {
                        return "女"
                    } else if (s === 1) {
                        return "男"
                    } else return "保密";
                }
            }, {
                field: 'isDelete',
                title: '是否有效',
                formatter: function (s) {
                    return s === 0 ? "<span class=\"label label-success\">有效</span>" : "<span class=\"label label-warning\">冻结</span>"
                }
            }, {
                field: 'id',
                visible: false
            }, {
                field: 'roleId',
                visible: false
            }]

        };
        $ExTable.initTable($userTable, settings);
        $(".fixed-table-toolbar").find(".search input").attr("placeholder", "搜索用户名");


        //var $table = $("#online");
        var $remove = $("#btn_delete");
        var $edit = $("#btn_edit");
        var $add = $("#btn_add");
        var $userEditModal = $("#editUserModal");
        var $addUserModal = $("#addUserModal");

        //var $modal = $('#modal');

        $remove.click(function () {
            var rows = getRowSelections("#userTable");
            if (rows.length !== 0) {
                bootbox.confirm({
                    size: "small",
                    message: "确定提交当前操作吗?",
                    buttons: {
                        cancel: {
                            className: 'btn-success',
                            label: '<i class="fa fa-times"></i> 取消'
                        },
                        confirm: {
                            className: 'btn-danger',
                            label: '<i class="fa fa-check"></i> 确定'
                        }
                    },
                    callback: function (result) {
                        if (result) {
                            $.ajax({
                                url: "/userList",
                                contentType: "application/json",
                                data: JSON.stringify(rows),
                                type: "delete",
                                success: function (da) {
                                    $userTable.bootstrapTable("refresh");
                                }
                            });
                        }
                    }
                });
            } else {
                toastr.warning('请选择数据');
            }
        });

        $edit.click(function () {
            var rows = getRowSelections("#userTable");
            if (rows.length !== 1) {
                toastr.warning('请选择有效数据');
                return;
            }
            $userEditModal.find("#exampleModalLabel").text("修改用户");
            $userEditModal.find("#userId").val(rows[0].id);
            $userEditModal.find("#nickname").val(rows[0].nickname);
            $userEditModal.find("#age").val(rows[0].age);
            $userEditModal.find("#userName").val(rows[0].username);
            var sel = $userEditModal.find("#isDelete")[0].options;
            sel.selectedIndex = rows[0].isDelete;
            $userEditModal.find("#sex").val(rows[0].sex);
            if (rows[0].sex == 0) {
                $userEditModal.find("#female").prop("checked", true);
            } else if (rows[0].sex == 1) {
                $userEditModal.find("#male").prop("checked", true);
            } else {
                $userEditModal.find("#fm").prop("checked", true);
            }
            var roleList = loadRoleList();
            var $roleList = $userEditModal.find("#roleId");
            $roleList.empty();
            for (var i = 0; i < roleList.length; i++) {
                //var flag = rows[0].roleName == roleList[i].name;
                $roleList[0].options.add(new Option(roleList[i].name, roleList[i].id));
            }
            $roleList.val(rows[0].roleId);
            $userEditModal.modal("show");
        });

        $userEditModal.find('#submit').click(function () {
            $.ajax({
                url: "/user",
                data: $("#userEditForm").serialize(),
                type: "put",
                success: function (da) {
                    $userEditModal.modal("hide");
                    $userTable.bootstrapTable("refresh");
                }
            });
        });

        $add.click(function () {
            $addUserModal.modal("show");
        });

        $addUserModal.find("#doAddUser").click(function () {
            $.ajax({
                url: "/login/userregister",
                data: $("#addUserForm").serialize(),
                type: "post",
                success: function (da) {
                    $addUserModal.modal("hide");
                    $userTable.bootstrapTable("refresh");
                }
            });
        })


    });
}

function loadRoleList() {
    var roleList = [];
    $.ajax({
        url: "/roleList",
        async: false,
        type: "get",
        success: function (da) {
            roleList = da;
        }
    });
    return roleList;
}

function loadPerm(ele) {
    var url = $(ele).attr("url");
    $("#mainPage").load(url, function () {
        var $permTable = $('#permTable');
        var settings = {
            url: "/permList",
            queryParams: function (params) {
                return {
                    pageSize: params.limit,
                    pageNum: params.offset / params.limit + 1,
                    search: params.search
                    //roleName: $(".form").find("input[name='roleName']").val().trim(),
                };
            },
            columns: [{
                checkbox: true,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'name',
                title: '权限名'
            }, {
                field: 'url',
                title: '链接'
            }, {
                field: 'percode',
                title: '权限'
            }, {
                field: 'type',
                title: '类型',
                formatter: function (s) {
                    return s == 2 ? "<span class=\"label label-success\">按钮</span>" : "<span class=\"label label-warning\">菜单</span>"
                }
            }, {
                field: 'available',
                title: '是否有效',
                formatter: function (s) {
                    return s == 1 ? "<span class=\"label label-success\">有效</span>" : "<span class=\"label label-warning\">无效</span>"
                }
            }, {
                field: 'description',
                title: '描述'
            }, {
                field: 'id',
                visible: false
            }]

        };
        $ExTable.initTable($permTable, settings);
        $(".fixed-table-toolbar").find(".search input").attr("placeholder", "搜索权限名");

        var $remove = $("#btn_delete");
        var $edit = $("#btn_edit");
        var $add = $("#btn_add");
        //var $userEditModal = $("#editUserModal");

        $remove.click(function () {
            var rows = getRowSelections("#permTable");
            if (rows.length !== 0) {
                bootbox.confirm({
                    size: "small",
                    message: "确定提交当前操作吗?",
                    buttons: {
                        cancel: {
                            className: 'btn-success',
                            label: '<i class="fa fa-times"></i> 取消'
                        },
                        confirm: {
                            className: 'btn-danger',
                            label: '<i class="fa fa-check"></i> 确定'
                        }
                    },
                    callback: function (result) {
                        if (result) {
                            $.ajax({
                                url: "/perm",
                                contentType: "application/json",
                                data: JSON.stringify(rows),
                                type: "delete",
                                success: function (da) {
                                    $permTable.bootstrapTable("refresh");
                                }
                            });
                        }
                    }
                });
            } else {
                toastr.warning('请选择数据');
            }
        });

    });
}

function loadRole(ele) {
    var url = $(ele).attr("url");
    $("#mainPage").load(url, function () {
        var $role = $('#roleTable');
        var settings = {
            url: "/roleList",
            sidePagination: "client",
            queryParams: function (params) {
                return {
                    pageSize: params.limit,
                    pageNum: params.offset / params.limit + 1,
                    search: params.search
                    //roleName: $(".form").find("input[name='roleName']").val().trim(),
                };
            },
            columns: [{
                field: 'state',
                checkbox: true,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'name',
                title: '角色名称'
            }, {
                field: 'available',
                title: '是否有效',
                formatter: function (s) {
                    return s == "1" ? "<span class=\"label label-success\">有效</span>" : "<span class=\"label label-warning\">无效</span>"
                }
            }, {
                field: 'id',
                title: 'id号'
            }]
        };
        $ExTable.initTable($role, settings);
        $(".fixed-table-toolbar").find(".search input").attr("placeholder", "搜索角色");


        var $remove = $("#btn_delete");
        var $edit = $("#btn_edit");
        var $add = $("#btn_add");

        var $addRoleModal = $('#addRoleModal');
        var $editRoleModal = $('#editRoleModal');

        $add.click(function () {
            $addRoleModal.find("#exampleModalLabel").text("新增角色");
            $addRoleModal.find("#roleId").attr("disabled", "true");
            $addRoleModal.find("#roleName").val("");
            $addRoleModal.modal("show");
        });
        $addRoleModal.find("#submit").click(function () {
            $.ajax({
                url: "/role",
                data: $("#roleAddForm").serialize(),
                type: "put",
                success: function (da) {
                    $addRoleModal.modal("hide");
                    $role.bootstrapTable("refresh");
                }
            });
        });

        $role.on('check.bs.table uncheck.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
            $edit.prop('disabled', !$role.bootstrapTable('getSelections').length);
            $remove.prop('disabled', !$role.bootstrapTable('getSelections').length);
        });

        $edit.click(function () {
            var ids = getIdSelections("#roleTable");
            var rows = getRowSelections("#roleTable");
            $editRoleModal.find("#exampleModalLabel").text("修改角色");
            if (rows.length === 1) {
                $editRoleModal.find("#roleId").val(rows[0].id);
                $editRoleModal.find("#roleName").val(rows[0].name);
                $editRoleModal.find("#available-1").prop("checked", rows[0].available === 1);
                $editRoleModal.find("#available-0").prop("checked", rows[0].available === 0);
                //$editRoleModal.find("#permTree").jstree.destroy();
                permTree("#permTree", rows[0].id);
                $editRoleModal.modal("show");
            } else {
                toastr.warning('请选择要修改的数据');
            }
        });
        $editRoleModal.find("#submit").click(function () {
            //alert($("#roleEditForm").serialize() +"&rolePermIds="+ $("#permTree").jstree(true).get_selected(false));
            $.ajax({
                url: "/role",
                data: $("#roleEditForm").serialize() + "&rolePermIds=" + $("#permTree").jstree(true).get_selected(false),
                type: "post",
                success: function (da) {
                    $remove.prop('disabled', true);
                    $editRoleModal.modal("hide");
                    $role.bootstrapTable("refresh");
                }
            });
        });

        $remove.click(function () {
            var rows = getRowSelections("#roleTable");
            if (rows.length !== 0) {
                $.ajax({
                    url: "/role",
                    contentType: "application/json",
                    data: JSON.stringify(rows),
                    type: "delete",
                    success: function (da) {
                        $remove.prop('disabled', true);
                        $role.bootstrapTable("refresh");
                    }
                });
            } else {
                toastr.warning('请选择要删除的数据');
            }
        });
    });

}

function loadOnline(ele) {

    var url = $(ele).attr("url");
    $("#mainPage").load(url, function () {
        $('#online').bootstrapTable({
            pagination: true,
            sidePagination: "client",
            //uniqueId: "id",
            toolbar: '#toolbar', // 工具按钮用哪个容器
            pageNumber: 1, // 初始化加载第一页，默认第一页
            pageSize: 8, // 每页的记录行数（*）
            singleSelect: true,
            clickToSelect: true,
            showRefresh: true, // 是否显示刷新按钮
            //checkbox:true,
            pageList: [8, 15, 25], // 可供选择的每页的行数（*）
            //sortable : true, // 是否启用排序
            //sortOrder : "asc", // 排序方式
            search: true, // 是否显示表格搜索
            showColumns: true, // 是否显示所有的列
            detailView: false,//父子表
            /*onExpandRow: function (index, row, $detail) {
                InitSubTable(index, row, $detail);
            },*/

            idField: "id",
            columns: [{
                checkbox: true,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'principal',
                title: '用户名'
            }, {
                field: 'state',
                title: '状态',
                formatter: function (s) {
                    return s == 1 ? "<span class=\"label label-success\">在线</span>" : "<span class=\"label label-warning\">离线</span>"
                }
            }, {
                field: 'date',
                title: '最后活跃时间',
                formatter: dateFormatter
            }, {
                field: 'loginTime',
                title: '登录时间',
                formatter: dateFormatter
            }, {
                field: 'ip',
                title: '登录ip'
            }, {
                field: 'id',
                visible: false
            }],
            //data: online
            url: "/online"
        });

        //var $table = $("#online");
        var $remove = $("#btn_delete");

        $("#online").on('check.bs.table uncheck.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
            $remove.prop('disabled', !$("#online").bootstrapTable('getSelections').length);
        });

        $remove.click(function () {
            var ids = getIdSelections("#online");
            if (ids.length != 0) {
                $.ajax({
                    url: "/forcelogout",
                    data: {ids: ids.toString()},
                    type: "post",
                    success: function () {
                        //alert("ok")
                        $("#online").bootstrapTable('remove', {
                            field: 'id',
                            values: ids
                        });
                    }
                });
                $remove.prop('disabled', true);
            }

            //alert(ids);
        });

    });

}

function dateFormatter(time) {
    var now = new Date(time);
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var date = now.getDate();
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
}

function loadapicount(ele) {
    var url = $(ele).attr("url");
    $("#mainPage").load(url, function () {
        $.ajax({
            url: "/apitimelist",
            type: "post",
            success: function (data) {
                $.each(data, function (i, n) {
                    $("#apitime").append("<option>" + n + "</option>");
                });
            }
        });

        $.ajax({
            url: "/apiurilist",
            type: "post",
            success: function (data) {
                $.each(data, function (i, n) {
                    $("#apilist").append("<option>" + n + "</option>");
                });
            }
        });

        var apiresult;
        $("#btn-api").click(function () {
            $.ajax({
                url: "/apimonitor",
                type: "post",
                data: $("#formSearch").serialize(),
                success: function (data) {
                    apiresult = data;
                    $("#api-table").bootstrapTable('destroy');
                    $('#api-table').bootstrapTable({
                        pagination: true,
                        sidePagination: "client",
                        pageNumber: 1, // 初始化加载第一页，默认第一页
                        pageSize: 8, // 每页的记录行数（*）
                        pageList: [8, 15, 25], // 可供选择的每页的行数（*）
                        search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                        showColumns: false, // 是否显示所有的列
                        columns: [{
                            field: 'apiTime',
                            title: '时间'
                        }, {
                            field: 'apiList',
                            title: '调用次数'
                        }],
                        data: apiresult
                    });
                }
            });
        });
    });

}

function loadApiPage(ele) {
    var url = $(ele).attr("url");
    var Params = function (params) {
        var temp = {
            limit: params.limit, // 页面大小
            offset: params.offset // 页码
        };
    };
    $("#mainPage").load(url, function () {
        $('#api').bootstrapTable({
            url: '/getAllUrlMap', // 请求后台的URL（*）
            method: 'get', // 请求方式（*）
            // toolbar: '#toolbar', //工具按钮用哪个容器
            striped: true, // 是否显示行间隔色
            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, // 是否显示分页（*）
            sortable: false, // 是否启用排序
            sortOrder: "asc", // 排序方式
            queryParams: Params, // 传递参数（*）
            sidePagination: "client", // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, // 初始化加载第一页，默认第一页
            pageSize: 10, // 每页的记录行数（*）
            pageList: [10, 20], // 可供选择的每页的行数（*）
            search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            showColumns: false, // 是否显示所有的列
            showRefresh: false, // 是否显示刷新按钮
            minimumCountColumns: 2, // 最少允许的列数
            clickToSelect: true, // 是否启用点击选中行
            height: 500, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID", // 每一行的唯一标识，一般为主键列
            showToggle: false, // 是否显示详细视图和列表视图的切换按钮
            cardView: false, // 是否显示详细视图
            detailView: false, // 是否显示父子表
            columns: [{
                checkbox: false
            }, {
                field: 'requestUrl',
                title: '请求uri'
            }, {
                field: 'requestType',
                title: '请求类型'
            }, {
                field: 'controllerName',
                title: '控制器名称'
            }, {
                field: 'methodParmaTypes',
                title: '请求参数类型'
            }, {
                field: 'methodName',
                title: '请求方法名'
            }]
        });


    });
}

// 初始table
function openTable() {

    // 1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    /*// 2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();*/

}

var TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#tb_departments').bootstrapTable({
            url: '/userList', // 请求后台的URL（*）
            method: 'post', // 请求方式（*）
            toolbar: '#toolbar', // 工具按钮用哪个容器
            striped: true, // 是否显示行间隔色
            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, // 是否显示分页（*）
            sortable: false, // 是否启用排序
            sortOrder: "asc", // 排序方式
            queryParams: oTableInit.queryParams, // 传递参数（*）
            sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, // 初始化加载第一页，默认第一页
            pageSize: 3, // 每页的记录行数（*）
            pageList: [3, 5, 10, 20], // 可供选择的每页的行数（*）
            search: true, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            showColumns: true, // 是否显示所有的列
            showRefresh: true, // 是否显示刷新按钮
            minimumCountColumns: 2, // 最少允许的列数
            clickToSelect: true, // 是否启用点击选中行
            height: 500, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID", // 每一行的唯一标识，一般为主键列
            showToggle: false, // 是否显示详细视图和列表视图的切换按钮
            cardView: false, // 是否显示详细视图
            detailView: false, // 是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'username',
                title: '部门名称'
            }, {
                field: 'nickname',
                title: '上级部门'
            }, {
                field: 'city',
                title: '部门级别'
            }, {
                field: 'province',
                title: '描述'
            }]
        });
    };

    // 得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit, // 页面大小
            offset: params.offset, // 页码
        };
        return temp;
    };
    return oTableInit;
};

/*var ButtonInit = function() {
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function() {
		// 初始化页面上面的按钮事件
	};

	return oInit;
};*/

//初始化子表格(无线循环)
function InitSubTable(index, row, $detail) {
    var parentid = row.MENU_ID;
    var cur_table = $detail.html('<table></table>').find('table');
    $(cur_table).bootstrapTable({
        url: '/api/MenuApi/GetChildrenMenu',
        method: 'get',
        queryParams: {strParentID: parentid},
        ajaxOptions: {strParentID: parentid},
        clickToSelect: true,
        detailView: true,//父子表
        uniqueId: "MENU_ID",
        pageSize: 10,
        pageList: [10, 25],
        columns: [{
            checkbox: true
        }, {
            field: 'MENU_NAME',
            title: '菜单名称'
        }, {
            field: 'MENU_URL',
            title: '菜单URL'
        }, {
            field: 'PARENT_ID',
            title: '父级菜单'
        }, {
            field: 'MENU_LEVEL',
            title: '菜单级别'
        },],
        //无线循环取子表，直到子表里面没有记录
        onExpandRow: function (index, row, $Subdetail) {
            oInit.InitSubTable(index, row, $Subdetail);
        }
    });
};

function getIdSelections(table) {
    return $.map($(table).bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getRowSelections(table) {
    return $(table).bootstrapTable('getSelections')
}