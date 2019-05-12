/**
 * 配置初始化
 */
var Config = {
    id: "ConfigTable",	//表格id
    seItem: null,		//选中的条目
    seItems: '',//多个选中条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Config.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '系统编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '配置名称', field: 'configName', visible: true, align: 'center', valign: 'middle'},
        {title: '配置值', field: 'configValue', visible: true, align: 'center', valign: 'middle'},
        {title: '备注', field: 'tips', visible: true, align: 'center', valign: 'middle'},
        {title: '修改时间', field: 'updatetime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Config.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Config.seItem = selected[0];
        Config.seItems = ""; //清空数据
        for(var i = 0 ; i < selected.length; i++){
           Config.seItems += selected[i].id + ",";
        }
        return true;
    }
};

/**
 * 点击添加配置
 */
Config.addPage = function () {
    var index = layer.open({
        type: 2,
        title: '添加配置',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/config/add_page'
    });
    this.layerIndex = index;
};

/**
 * 编辑配置
 */
Config.editPage = function () {
    if (this.check()) {
        if($('#' + this.id).bootstrapTable('getSelections').length > 1){
            return Feng.error("只能选中一条记录！");
        }
        var index = layer.open({
            type: 2,
            title: '配置详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/config/edit_page/' + Config.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除配置
 */
Config.deletePage = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/config/delete", function (data) {
            Feng.success("删除成功!");
            Config.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ids",Config.seItems);
        ajax.start();
    }
};

/**
 * 查询配置列表
 */
Config.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Config.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Config.initColumn();
    var table = new BSTable(Config.id, "/config/list", defaultColunms);
    table.setPaginationType("server");
    Config.table = table.init();
});
