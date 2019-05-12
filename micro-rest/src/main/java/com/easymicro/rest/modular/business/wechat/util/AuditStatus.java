package com.easymicro.rest.modular.business.wechat.util;

public enum AuditStatus {

    All(0,"全部"),
    WAIT(1,"待审核"),
    AUDITED(2,"已审核"),
    FAIL(3,"未通过"),
    ;


    public static String getNotes(Integer status){

        String notes="";

        switch (status){
            case 0:notes = All.notes;break;
            case 1:notes = WAIT.notes;break;
            case 2:notes = AUDITED.notes;break;
            case 3:notes = FAIL.notes;break;
        }

        return notes;
    }

    private Integer status;
    private String notes;

    AuditStatus(Integer status, String notes) {
        this.status = status;
        this.notes = notes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
