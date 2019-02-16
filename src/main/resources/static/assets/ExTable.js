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
        pageList: [5, 10, 25, 50, 100],
        search: true,
        strictSearch: true,
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
    }

    function _initTable(id, settings) {
        var params = $.extend({}, bootstrapTable_default, settings);
        if (typeof params.url == 'undefined') {
            throw '初始化表格失败，请配置url参数！';
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