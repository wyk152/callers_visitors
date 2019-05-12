/**
 * 初始化商家详情对话框
 */
var CompanyInfoDlg = {
    CompanyInfoData : {},
    editor: null,
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '商家名称不能为空'
                }
            }
        },
        areaCode: {
            validators: {
                notEmpty: {
                    message: '商家编码不能为空'
                }
            }
        },
        appId: {
            validators: {
                notEmpty: {
                    message: '微信APP_ID不能为空'
                }
            }
        },
        appScrect: {
            validators: {
                notEmpty: {
                    message: '微信APP_SCRECT不能为空'
                }
            }
        },
    }
};

/**
 * 清除数据
 */
CompanyInfoDlg.clearData = function() {
    this.CompanyInfoData = {};
}

/**
 * 关闭此对话框
 */
CompanyInfoDlg.close = function() {
    parent.layer.close(window.parent.Company.layerIndex);
}

/**
 * 数据完整性校验
 */
CompanyInfoDlg.validate = function () {
    $('#submitForm').data("bootstrapValidator").resetForm();
    $('#submitForm').bootstrapValidator('validate');
    return $("#submitForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
CompanyInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    //提交信息
    var formData = new FormData($("#submitForm")[0]);

    //详情
    //formData.append("content",CompanyInfoDlg.editor.txt.html());

    $.ajax({
        url:  Feng.ctxPath + "/merchant/add" ,
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (returndata) {
            if(returndata.code == 200){
                Feng.success("添加成功!");
                window.parent.Company.table.refresh();
                CompanyInfoDlg.close();
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
CompanyInfoDlg.editSubmit = function() {

    if (!this.validate()) {
        return;
    }

    var formData = new FormData($("#submitForm")[0]);
    $.ajax({
        url:  Feng.ctxPath + "/merchant/edit" ,
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (returndata) {
            if(returndata.code == 200){
                Feng.success("修改成功!");
                window.parent.Company.table.refresh();
                CompanyInfoDlg.close();
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
    Feng.initValidator("submitForm", CompanyInfoDlg.validateFields);
});
