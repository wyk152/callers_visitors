/**
 * 商家初始化
 */
var Company = {
    id: "CompanyTable",	//表格id
    seItem: null,		//选中的条目
    seItems: '',//多个选中条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Company.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '系统编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '商家名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '商家编码', field: 'areaCode', visible: true, align: 'center', valign: 'middle'},
        {title: '省份', field: 'province', visible: true, align: 'center', valign: 'middle'},
        {title: '商家地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
        {title: '商家电话', field: 'tel', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Company.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Company.seItem = selected[0];
        Company.seItems = ""; //清空数据
        for(var i = 0 ; i < selected.length; i++){
           Company.seItems += selected[i].id + ",";
        }
        return true;
    }
};

/**
 * 点击添加商家
 */
Company.addPage = function () {
    var index = layer.open({
        type: 2,
        title: '添加商家',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/merchant/add_page'
    });
    this.layerIndex = index;
};

/**
 * 编辑商家
 */
Company.editPage = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '商家详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/merchant/edit_page/' + Company.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除商家
 */
Company.deletePage = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/merchant/delete", function (data) {
            Feng.success("删除成功!");
            Company.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ids",Company.seItems);
        ajax.start();
    }
};

/**
 * 查询商家列表
 */
Company.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Company.table.refresh({query: queryData});
};

/**
 * 验证微信
 */
Company.validPage = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/merchant/valid", function (data) {
            if (data.code == 200) {
                Feng.success("验证成功!");
            }else{
                Feng.success("验证失败!错误："+data.message);
            }
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("companyId",Company.seItem.id);
        ajax.start();
    }
};

/**
 * 分配账号
 */
Company.assignPage = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分配账号',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/merchant/assign_page/' + Company.seItem.id
        });
        this.layerIndex = index;
    }
};

$(function () {
    var defaultColunms = Company.initColumn();
    var table = new BSTable(Company.id, "/merchant/list", defaultColunms);
    table.setPaginationType("server");
    Company.table = table.init();
});
