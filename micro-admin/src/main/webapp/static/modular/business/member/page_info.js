/**
 * 初始化用户详情对话框
 */
var MemberInfoDlg = {
    MemberInfoData : {},
    editor: null
};

/**
 * 清除数据
 */
MemberInfoDlg.clearData = function() {
    this.MemberInfoData = {};
}

/**
 * 关闭此对话框
 */
MemberInfoDlg.close = function() {
    parent.layer.close(window.parent.Member.layerIndex);
}

/**
 * 数据完整性校验
 */
MemberInfoDlg.validate = function () {
    $('#submitForm').data("bootstrapValidator").resetForm();
    $('#submitForm').bootstrapValidator('validate');
    return $("#submitForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
MemberInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }
    //提交信息
    var formData = new FormData($("#submitForm")[0]);

    //详情
    //formData.append("content",MemberInfoDlg.editor.txt.html());

    $.ajax({
        url:  Feng.ctxPath + "/member/add" ,
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (returndata) {
            if(returndata.code == 200){
                Feng.success("添加成功!");
                window.parent.Member.table.refresh();
                MemberInfoDlg.close();
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
MemberInfoDlg.editSubmit = function() {

    if (!this.validate()) {
        return;
    }

    var formData = new FormData($("#submitForm")[0]);
    $.ajax({
        url:  Feng.ctxPath + "/member/edit" ,
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (returndata) {
            if(returndata.code == 200){
                Feng.success("修改成功!");
                window.parent.Member.table.refresh();
                MemberInfoDlg.close();
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
    Feng.initValidator("submitForm", MemberInfoDlg.validateFields);
});
