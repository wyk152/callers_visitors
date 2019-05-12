/**
 * 用户初始化
 */
var Member = {
    id: "MemberTable",	//表格id
    seItem: null,		//选中的条目
    seItems: '',//多个选中条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Member.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '系统编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
        {title: '修改时间', field: 'updatetime', visible: true, align: 'center', valign: 'middle'},
        {title: '微信昵称', field: 'nickName', visible: true, align: 'center', valign: 'middle'},
        {title: '姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '手机', field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '部门名称', field: 'deptName', visible: true, align: 'center', valign: 'middle'},
        {title: '公司名称', field: 'companyName', visible: true, align: 'center', valign: 'middle'},
        {title: '公司地址', field: 'companyAddress', visible: true, align: 'center', valign: 'middle'},
        {title: '职务', field: 'dutyName', visible: true, align: 'center', valign: 'middle'},
        {title: '身份证号', field: 'cardNo', visible: true, align: 'center', valign: 'middle'},
        {title: '车牌号', field: 'carNo', visible: true, align: 'center', valign: 'middle'},
        {title: '商家编号', field: 'areaCode', visible: true, align: 'center', valign: 'middle'},
        {title: '账号类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
        {title: '微信头像', field: 'avatarUrl', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Member.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Member.seItem = selected[0];
        Member.seItems = ""; //清空数据
        for(var i = 0 ; i < selected.length; i++){
           Member.seItems += selected[i].id + ",";
        }
        return true;
    }
};

/**
 * 点击添加用户
 */
Member.addPage = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/member/add_page'
    });
    this.layerIndex = index;
};

/**
 * 编辑用户
 */
Member.editPage = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/member/edit_page/' + Member.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户
 */
Member.deletePage = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/member/delete", function (data) {
            Feng.success("删除成功!");
            Member.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ids",Member.seItems);
        ajax.start();
    }
};

/**
 * 查询用户列表
 */
Member.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Member.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Member.initColumn();
    var table = new BSTable(Member.id, "/member/list", defaultColunms);
    table.setPaginationType("server");
    Member.table = table.init();
});
