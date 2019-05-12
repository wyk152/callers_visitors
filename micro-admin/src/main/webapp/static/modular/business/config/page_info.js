/**
 * 初始化配置详情对话框
 */
var ConfigInfoDlg = {
    ConfigInfoData : {},
    editor: null
};

/**
 * 清除数据
 */
ConfigInfoDlg.clearData = function() {
    this.ConfigInfoData = {};
}

/**
 * 关闭此对话框
 */
ConfigInfoDlg.close = function() {
    parent.layer.close(window.parent.Config.layerIndex);
}

/**
 * 数据完整性校验
 */
ConfigInfoDlg.validate = function () {
    $('#submitForm').data("bootstrapValidator").resetForm();
    $('#submitForm').bootstrapValidator('validate');
    return $("#submitForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
ConfigInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    //提交信息
    var formData = new FormData($("#submitForm")[0]);

    //详情
    //formData.append("content",ConfigInfoDlg.editor.txt.html());

    $.ajax({
        url:  Feng.ctxPath + "/config/add" ,
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (returndata) {
            if(returndata.code == 200){
                Feng.success("添加成功!");
                window.parent.Config.table.refresh();
                ConfigInfoDlg.close();
            }else{
                Feng.error("添加失败!错误："+returndata.message);
            }
        },
         error: function (returndata) {
         Feng.error("添加失败!");
        }
   });
}

/**
 * 提交修改
 */
ConfigInfoDlg.editSubmit = function() {

    if (!this.validate()) {
        return;
    }

    var formData = new FormData($("#submitForm")[0]);
    $.ajax({
        url:  Feng.ctxPath + "/config/edit" ,
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (returndata) {
            if(returndata.code == 200){
                Feng.success("修改成功!");
                window.parent.Config.table.refresh();
                ConfigInfoDlg.close();
            }else{
                Feng.error("修改失败!错误："+returndata.message);
            }
        },
        error: function (returndata) {
           Feng.error("修改失败!");
        }
     });
}

$(function() {
    Feng.initValidator("submitForm", ConfigInfoDlg.validateFields);
});
