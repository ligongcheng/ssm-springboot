//设置提示时间
toastr.options.timeOut = 3000;
//全局的ajax访问设置
$.ajaxSetup({
    dataType: "json",
    complete: function (XMLHttpRequest) {
        var date = undefined;
        try {
            date = JSON.parse(XMLHttpRequest.responseText);
        } catch (e) {
        }
        var status = XMLHttpRequest.status;
        //通过code取得操作状态，并提示
        if (date != undefined) {
            if (date.code == 200) {
                toastr.success('操作成功');
            } else if (date.code != undefined && date.msg != undefined) {
                toastr.error(date.msg)
            }
        }
        //通过XMLHttpRequest取得响应头，sessionstatus，
        var sessionstatus = XMLHttpRequest.getResponseHeader("session-status");
        if (sessionstatus === "timeout") {
            //如果超时就处理 ，指定要跳转的页面(比如登陆页)
            window.top.location.href = "/login?_" + new Date().getTime();
        }
    }
});

function permTree(ele, roleId) {
    var rolePermIds;
    $.ajax({
        url: "/sys/rolePermIds",
        type: "get",
        data: {id: roleId},
        async: false,
        success: function (data) {
            rolePermIds = data;
        }
    });
    $.ajax({
        url: "/sys/perm/tree",
        type: "get",
        success: function (da) {
            $(ele).jstree("destroy");
            $(ele).jstree({
                "core": {
                    'data': da.children
                },
                "plugins": ["checkbox", "wholerow"],
                "checkbox": {
                    "three_state": false // 取消选择父节点后选中所有子节点
                }
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


function loadUser() {

    var $userTable = $('#userTable');
    var settings = {
        url: "/sys/user",
        singleSelect: true,
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
        var ids = getIdSelections("#userTable");
        var s = function () {
            $.ajax({
                url: "/sys/user/" + ids[0],
                type: "delete",
                success: function (da) {
                    $userTable.bootstrapTable("refresh");
                }
            });
        };
        if (ids.length !== 0) {
            tip.confirm(s);
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
            url: "/sys/user",
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
            url: "/sys/user/register",
            data: $("#addUserForm").serialize(),
            type: "post",
            success: function (da) {
                $addUserModal.modal("hide");
                $userTable.bootstrapTable("refresh");
            }
        });
    })

}

function loadJob() {

    function operateFormatter(value, row, index) {
        return [
            '<a class="start" href="javascript:void(0)" title="start">',
            '<button type="button" class="btn btn-primary btn-sm">开始</button>',
            '</a>  ',
            '<a class="stop" href="javascript:void(0)" title="stop">',
            '<button type="button" class="btn btn-warning btn-sm">暂停</button>',
            '</a> ',
            '<a class="remove" href="javascript:void(0)" title="remove">',
            '<button type="button" class="btn btn-danger btn-sm">删除</button>',
            '</a> '
        ].join('')
    }

    window.operateEvents = {
        'click .start': function (e, value, row, index) {
            $.ajax({
                url: "/job/status",
                contentType: "application/json",
                data: JSON.stringify({"id": row.id, "status": 1}),
                type: "put",
                success: function (res) {
                    $jobTable.bootstrapTable("refresh");
                }
            })
        },
        'click .stop': function (e, value, row, index) {
            $.ajax({
                url: "/job/status",
                contentType: "application/json",
                data: JSON.stringify({"id": row.id, "status": 0}),
                type: "put",
                success: function (res) {
                    $jobTable.bootstrapTable("refresh");
                }
            })
        },
        'click .remove': function (e, value, row, index) {
            $.ajax({
                url: "/job/" + row.id,
                type: "delete",
                success: function (res) {
                    $jobTable.bootstrapTable("refresh");
                }
            })
        }
    }

    var $jobTable = $('#jobTable');
    var settings = {
        url: "/job",
        singleSelect: true,
        search: false,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'name',
            title: '任务名'
        }, {
            field: 'desc',
            title: '任务描述'
        }, {
            field: 'classNmae',
            title: '任务类'
        }, {
            field: 'cronExpression',
            title: 'Cron表达式'
        }, {
            field: 'status',
            title: '状态',
            formatter: function (s) {
                return s === 1 ? "<span class=\"label label-success\">运行</span>" : "<span class=\"label label-warning\">停止</span>"
            }
        }, {
            field: 'id',
            visible: false
        }, {
            field: 'operate',
            title: '操作',
            events: window.operateEvents,
            formatter: operateFormatter
        }]

    };
    $ExTable.initTable($jobTable, settings);
    $(".fixed-table-toolbar").find(".search input").attr("placeholder", "搜索用户名");


    var $add = $("#btn_add");
    var $addJobModal = $("#addJobModal");

    $add.click(function () {
        $addJobModal.modal("show");
    });

    $addJobModal.find("#doAddJob").click(function () {
        var t = $("#addJobForm").serializeArray();
        var d = {}
        $.each(t, function () {
            d[this.name] = this.value;
        });
        $.ajax({
            url: "/job",
            data: JSON.stringify(d),
            contentType: "application/json",
            type: "post",
            success: function (da) {
                $addJobModal.modal("hide");
                $jobTable.bootstrapTable("refresh");
            }
        });
    })

}

function loadLeave() {

    function operateFormatter(value, row, index) {
        // 提交 编辑 取消 进度查询
        var ls = [];
        if (row.state === 0) {
            ls.push('<a class="submit" href="javascript:void(0)" title="submit"><button type="button" class="btn btn-primary btn-sm">提交</button></a> ')
            ls.push('<a class="edit" href="javascript:void(0)" title="edit"><button type="button" class="btn btn-primary btn-sm">编辑</button></a> ')
        }
        if (row.state === 1 || row.state === 2 || row.state === 4) {
            ls.push('<a class="query" href="javascript:void(0)" title="query"><button type="button" class="btn btn-primary btn-sm">进度查询</button></a> ')
        }
        if (row.state === 1) {
            ls.push('<a class="cancel" href="javascript:void(0)" title="cancel"><button type="button" class="btn btn-danger btn-sm">取消申请</button></a>')
        }
        return ls.join('')
    }

    window.operateEvents = {
        'click .submit': function (e, value, row, index) {
            $.ajax({
                url: "/act/leave/submit",
                data: row,
                type: "post",
                success: function (res) {
                    $leaveTable.bootstrapTable("refresh");
                }
            })
        },
        'click .edit': function (e, value, row, index) {
            console.log(JSON.stringify(row))
        },
        'click .cancel': function (e, value, row, index) {
            $.ajax({
                url: "/act/leave/cancel",
                data: row,
                type: "put",
                success: function (res) {
                    $leaveTable.bootstrapTable("refresh");
                }
            })
        },
        'click .query': function (e, value, row, index) {
            $img.append('<img alt="" src="/act/leave/img?processInstanceId=' + row.processInstanceId + '"/>');
            $imgModal.modal("show");
        },
    }
    $('#img').on('hidden.bs.modal', function (e) {
        $img.empty()
    })

    var $imgModal = $('#img');
    var $img = $('#actimg');
    var $leaveTable = $('#leaveTable');
    var settings = {
        url: "/act/leave/list",
        singleSelect: true,
        search: false,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'username',
            title: '申请人'
        }, {
            field: 'leaveDay',
            title: '请假天数'
        }, {
            field: 'startTime',
            title: '开始时间'
        }, {
            field: 'endTime',
            title: '结束时间'
        }, {
            field: 'reason',
            title: '请假原因'
        }, {
            field: 'processInstanceId',
            title: '流程实例Id'
        }, {
            field: 'state',
            title: '状态',
            formatter: function (s) {
                var state = ''
                if (s === 0) {
                    state = "<span class=\"label label-default\">未提交</span>"
                } else if (s === 1) {
                    state = "<span class=\"label label-info\">审批中</span>"
                } else if (s === 2) {
                    state = "<span class=\"label label-success\">审批通过</span>"
                } else if (s === 3) {
                    state = "<span class=\"label label-danger\">未提交_已放弃</span>"
                } else if (s === 4) {
                    state = "<span class=\"label label-danger\">已提交_已放弃</span>"
                }
                return state
            }
        }, {
            field: 'id',
            visible: false
        }, {
            field: 'userId',
            visible: false
        }, {
            field: 'operate',
            title: '操作',
            events: window.operateEvents,
            formatter: operateFormatter
        }]

    };
    $ExTable.initTable($leaveTable, settings);

    var $add = $("#btn_add");
    var $addLeaveModal = $("#addLeaveModal");

    $add.click(function () {
        $addLeaveModal.modal("show");
    });

    $addLeaveModal.find("#doAddLeave").click(function () {
        var t = $("#addLeaveForm").serializeArray();
        var d = {}
        $.each(t, function () {
            d[this.name] = this.value;
        });
        $.ajax({
            url: "/act/leave/apply",
            data: JSON.stringify(d),
            contentType: "application/json",
            type: "post",
            success: function (da) {
                $addLeaveModal.modal("hide");
                $leaveTable.bootstrapTable("refresh");
            }
        });
    })

}

function loadTask() {

    function operateFormatter(value, row, index) {
        // 提交 编辑 取消 进度查询
        var ls = [];
        if (row.assign) {
            ls.push('<a class="deal" href="javascript:void(0)" title="submit"><button type="button" class="btn btn-primary btn-sm">处理</button></a> ')
        } else {
            ls.push('<a class="accept" href="javascript:void(0)" title="accept"><button type="button" class="btn btn-primary btn-sm">签收</button></a> ')
        }
        return ls.join('')
    }

    window.operateEvents = {
        'click .deal': function (e, value, row, index) {
            $.ajax({
                url: "/act/leave/" + row.taskId,
                type: "get",
                success: function (res) {
                    console.log(JSON.stringify(res))
                    $('#username').val(res.data.username);
                    $('#leaveDay').val(res.data.leaveDay);
                    $('#startTime').val(res.data.startTime);
                    $('#endTime').val(res.data.endTime);
                    $('#reason').val(res.data.reason);
                    $('#taskId').val(row.taskId);
                    var comment = {
                        data: res.data.comments,
                        pagination: false,
                        sidePagination: 'client',
                        toolbar: '',
                        showColumns: false,
                        showRefresh: false,
                        singleSelect: true,
                        search: false,
                        queryParams: function (params) {
                            return {
                                pageSize: params.limit,
                                pageNum: params.offset / params.limit + 1
                            };
                        },
                        columns: [{
                            field: 'taskName',
                            title: '任务节点'
                        }, {
                            field: 'assignUser',
                            title: '处理人'
                        }, {
                            field: 'approve',
                            title: '是否同意',
                            formatter: function (s) {
                                return s ? "<span class=\"label label-success\">同意</span>" : "<span class=\"label label-warning\">不同意</span>"
                            }
                        }, {
                            field: 'comment',
                            title: '批注'
                        }]

                    };
                    $ExTable.initTable($commentTable, comment);
                    $dealModal.modal("show");
                }
            })
        },

        'click .accept': function (e, value, row, index) {
            $.ajax({
                url: "/act/accept",
                data: row,
                type: "get",
                success: function (res) {
                    $taskTable.bootstrapTable("refresh");
                }
            })
        },
    }

    var $taskTable = $('#taskTable');
    var $commentTable = $('#commentTable');
    var settings = {
        url: "/act/list",
        singleSelect: true,
        search: false,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'username',
            title: '申请人'
        }, {
            field: 'deployName',
            title: '类别'
        }, {
            field: 'taskId',
            title: '任务Id'
        }, {
            field: 'assign',
            visible: false
        }, {
            field: 'operate',
            title: '操作',
            events: window.operateEvents,
            formatter: operateFormatter
        }]

    };

    $ExTable.initTable($taskTable, settings);

    var $dealModal = $("#dealModal");
    $('#dealModal').on('hidden.bs.modal', function (e) {
        $commentTable.bootstrapTable('destroy')
    })
    $dealModal.find("#doDeal").click(function () {
        var t = $("#dealForm").serializeArray();
        var d = {};
        $.each(t, function () {
            d[this.name] = this.value;
        });
        $.ajax({
            url: "/act/leave/deal",
            data: d,
            type: "post",
            success: function (da) {
                $dealModal.modal("hide");
                $taskTable.bootstrapTable("refresh");
            }
        });
    })

}

function loadJoblog() {

    function operateFormatter(value, row, index) {
        return [
            '<a class="remove" href="javascript:void(0)" title="remove">',
            '<button type="button" class="btn btn-danger btn-sm">删除</button>',
            '</a> '
        ].join('')
    }

    window.operateEvents = {
        'click .remove': function (e, value, row, index) {
            $.ajax({
                url: "/joblog",
                type: "delete",
                data: JSON.stringify([row.id]),
                contentType: "application/json",
                success: function (res) {
                    jobTable.bootstrapTable("refresh");
                }
            })
        }
    }

    var jobTable = $('#jobTable');
    var settings = {
        url: "/joblog",
        singleSelect: false,
        search: false,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'jobId',
            title: '任务id'
        }, {
            field: 'className',
            title: '任务类'
        }, {
            field: 'status',
            title: '状态',
        }, {
            field: 'error',
            title: '异常信息',
        }, {
            field: 'times',
            title: '第几次执行',
        }, {
            field: 'duration',
            title: '任务用时(毫秒)',
        }, {
            field: 'createTime',
            title: '开始执行时间',
            formatter: dateFormatter
        }, {
            field: 'id',
            visible: false
        }, {
            field: 'operate',
            title: '操作',
            events: window.operateEvents,
            formatter: operateFormatter
        }]

    };
    $ExTable.initTable(jobTable, settings);

    var $del = $("#btn_del");

    $del.click(function () {
        var rows = getRowSelections("#jobTable");
        var ids = []
        for (let row of rows) {
            ids.push(row.id)
        }
        $.ajax({
            url: "/joblog",
            data: JSON.stringify(ids),
            contentType: "application/json",
            type: "delete",
            success: function (da) {
                jobTable.bootstrapTable("refresh");
            }
        });
    });
}

function loadRoleList() {
    var roleList = [];
    $.ajax({
        url: "/sys/role",
        async: false,
        type: "get",
        success: function (da) {
            roleList = da;
        }
    });
    return roleList;
}

function loadPerm() {
    var $permTable = $('#permTable');
    var settings = {
        url: "/sys/perm",
        singleSelect: true,
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
    var $permModal = $("#permModal");
    var $editPermModal = $("#editPermModal");
    $remove.click(function () {
        var rows = getIdSelections("#permTable");
        var s = function () {
            $.ajax({
                url: "/sys/perm/" + rows[0],
                type: "delete",
                success: function (da) {
                    $permTable.bootstrapTable("refresh");
                }
            });
        };
        if (rows.length !== 0) {
            tip.confirm(s);
        } else {
            toastr.warning('请选择数据');
        }
    });

    $add.click(function () {
        $("#permTitle").text("添加权限");
        $("#permId").prop("disabled", true);
        var $parentid = $("#parentid");
        $parentid.empty();
        $parentid.append($('<option value="0">根节点</option>'));
        $.ajax({
            url: "/sys/perm/menuList",
            type: "get",
            success: function (item) {
                $.each(item, function (i, n) {
                    $parentid.append($('<option value="' + n.id + '" >' + n.name + '</option>'))
                })
            }
        });
        $permModal.modal("show")
    });
    $permModal.find("#submit").click(function () {
        $.ajax({
            url: "/sys/perm",
            type: "post",
            data: $("#permForm").serialize(),
            success: function () {
                $permModal.modal("hide");
                $permTable.bootstrapTable("refresh")
            }
        });
    });

    $edit.click(function () {
        $("#editPermTitle").text("修改权限");
        $("#editPermId").prop("disabled", false);
        var $editParentid = $("#editParentid");
        $editParentid.empty();
        var rows = getIdSelections($permTable);
        if (rows.length > 0) {
            $editParentid.append($('<option value="0">根节点</option>'));
            $.ajax({
                url: "/sys/perm/menuList",
                type: "get",
                async: false,
                success: function (item) {
                    $.each(item, function (i, n) {
                        if (i !== rows[0]) {
                            $editParentid.append($('<option>').val(n.id).text(n.name))
                        }
                    })
                }
            });
            $.ajax({
                url: "/sys/perm/" + rows[0],
                type: "get",
                success: function (res) {
                    $("#editPermType").find('option[value="' + res.type + '"]').prop("selected", true);
                    $("#editPermId").val(res.id);
                    $("#editPermName").val(res.name);
                    $("#editPermUri").val(res.url);
                    $("#editPercode").val(res.percode);
                    $("#editParentid").val(res.parentid);
                    $("#editDescription").val(res.description);
                    $("#editSort").val(res.sort);
                    $editPermModal.find("#available-1").prop("checked", res.available == 1);
                    $editPermModal.find("#available-0").prop("checked", res.available == 0);
                    $editPermModal.modal("show");
                }
            });
        }
    });
    $editPermModal.find("#editSubmit").click(function () {
        var rows = getIdSelections($permTable);
        $.ajax({
            url: "/sys/perm/" + rows[0],
            type: "put",
            data: $("#editPermForm").serialize(),
            success: function () {
                $editPermModal.modal("hide");
                $permTable.bootstrapTable("refresh");
            }
        });
    });
}

function loadRole() {
    var $role = $('#roleTable');
    var settings = {
        url: "/sys/role",
        singleSelect: true,
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
            field: 'id',
            title: 'id号'
        }, {
            field: 'available',
            title: '是否有效',
            formatter: function (s) {
                return s == "1" ? "<span class=\"label label-success\">有效</span>" : "<span class=\"label label-warning\">无效</span>"
            }
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
        $addRoleModal.find("#addRoleTitle").text("新增角色");
        $addRoleModal.find("#addRoleId").prop("disabled", "true");
        $addRoleModal.find("#addRoleName").val("");
        $addRoleModal.modal("show");
    });
    $addRoleModal.find("#addSubmit").click(function () {
        $.ajax({
            url: "/sys/role",
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
            permTree("#rolePermTree", rows[0].id);
            $editRoleModal.modal("show");
        } else {
            toastr.warning('请选择要修改的数据');
        }
    });
    $editRoleModal.find("#submit").click(function () {
        $.ajax({
            url: "/sys/role",
            data: $("#roleEditForm").serialize() + "&rolePermIds=" + $("#rolePermTree").jstree(true).get_selected(false),
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
        var s = function () {
            $.ajax({
                url: "/sys/role",
                contentType: "application/json",
                data: JSON.stringify(rows),
                type: "delete",
                success: function (da) {
                    $remove.prop('disabled', true);
                    $role.bootstrapTable("refresh");
                }
            });
        };
        if (rows.length !== 0) {
            tip.confirm(s);
        } else {
            toastr.warning('请选择要删除的数据');
        }
    });


}

function loadOnline() {
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
            field: 'id',
            title: '会话id',
            visible: true
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
        }],
        //data: online
        url: "/sys/online"
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
                url: "/sys/forcelogout",
                data: {id: ids.toString()},
                type: "post",
                success: function () {
                    //alert("ok")
                    $("#online").bootstrapTable('refresh')
                }
            });
            //$remove.prop('disabled', true);
        }

        //alert(ids);
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

function loadapicount() {
    $.ajax({
        url: "/sys/apitimelist",
        type: "post",
        async: false,
        success: function (data) {
            $.each(data, function (i, n) {
                $("#apitime").append("<option>" + n + "</option>");
            });
        }
    });

    $.ajax({
        url: "/sys/apiurilist",
        type: "post",
        async: false,
        success: function (data) {
            $.each(data, function (i, n) {
                $("#apilist").append("<option>" + n + "</option>");
            });
        }
    });

    var apiresult;
    var api_click = function () {
        $.ajax({
            url: "/sys/apimonitor",
            type: "get",
            data: $("#formSearch").serialize(),
            success: function (data) {
                apiresult = data;
                $("#api-table").bootstrapTable('destroy');
                $("#api-table").bootstrapTable({
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
    };
    api_click();
    $("#btn-api-cli").click(
        function () {
            api_click()
        }
    );
}

function loadApiPage() {
    var Params = function (params) {
        return {
            pageSize: params.limit,
            pageNum: params.offset / params.limit + 1,
            search: params.search
        };
    };

    $('#api').bootstrapTable({
        url: '/sys/getAllUrlMap', // 请求后台的URL（*）
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

var $ExTable = (function () {
    var bootstrapTable_default = {
        method: 'get',
        striped: false,
        cache: false,
        pagination: true,
        toolbar: '#toolbar',
        sortable: false,
        sidePagination: "server",
        pageNumber: 1,
        pageSize: 5,
        pageList: [5, 15, 25, 50, 100],
        search: true,
        strictSearch: false,
        showColumns: true,
        showRefresh: true,
        minimumCountColumns: 2,
        clickToSelect: true,
        idField: "id",
        uniqueId: "id",
        cardView: false,
        detailView: false,
        smartDisplay: false,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                search: params.search
            };
        }
    };

    function _initTable(id, settings) {
        var params = $.extend({}, bootstrapTable_default, settings);
        if (typeof params.url == 'undefined' && typeof params.data == 'undefined') {
            throw '初始化表格失败，请配置url或data参数！';
        }
        if (typeof params.columns == 'undefined') {
            throw '初始化表格失败，请配置columns参数！';
        }
        $(id).bootstrapTable(params);
    }

    return {
        initTable: function (id, settings) {
            _initTable(id, settings);
        },
        refreshTable: function (id) {
            $(id).bootstrapTable('refresh');
        }
    }
})($);

var tip = {};

tip.confirm = function (oper) {
    var operation = oper;
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
        callback: function (res) {
            if (res) {
                oper();
            }
        }
    });
}
