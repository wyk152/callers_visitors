package com.easymicro.persistence.modular.model.business;

import com.easymicro.persistence.core.model.BaseApiModel;

import javax.persistence.*;


/**************************************
 * 访客记录
 * @author LinYingQiang
 * @date 2018-09-19 11:43
 * @qq 961410800
 *
************************************/
@Entity
@Table(name = "bus_access_record")
public class AccessRecord extends BaseApiModel {

    @Column(name = "v_name")
    private String vname;

    @Column(name = "u_name")
    private String uname;

    /** 来访记录ID */
    @Column(name = "id")
    private Integer id;

    /** 生成二维码的编码（生成规则：MD5(id+creatTime)生成的串，从第一位开始取值，每隔3位取一次，共取8位） */
    @Column(name = "recode")
    private String rcode;

    /** 随行人员父级ID */
    @Column(name = "parent_id")
    private Integer parentid;

    /** 访客ID */
    @Column(name = "v_id")
    private Long vid;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    /** 被访人ID（u_user） */
    private Integer uid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member user;


    /** 访客信息记录时间 */
    @Column(name = "createtime")
    private String createtime;

    /** 访问开始时间 */
    @Column(name = "starttime")
    private String starttime;

    /** 访客结束时间 */
    @Column(name = "endtime")
    private String endtime;

    /** 注销访问时间 */
    @Column(name = "logofftime")
    private String logofftime;

    /** 记录当前状态（0：进行中 1：超时未注销 2：结束 3：未登记） */
    @Column(name = "status")
    private Integer status;

    /** 来访事由 */
    @Column(name = "reasons")
    private String reasons;

    /** 来访单位 */
    @Column(name = "unit")
    private String unit;

    /** 来访人数 */
    @Column(name = "num")
    private Integer num;

    /** 访客记录类型（0：非预约 1：预约） */
    @Column(name = "type")
    private Integer type;

    /** 是否打印访客凭条（0：未打印 1：已打印） */
    @Column(name = "isprintvoucher")
    private Integer isprintvoucher;

    /** 是否发放卡（0：未发放 1：已发放） */
    @Column(name = "ispullcard")
    private Integer ispullcard;

    /** 发放卡片类型（0：IC卡） */
    @Column(name = "cardtype")
    private Integer cardtype;

    /** 发放卡卡号 */
    @Column(name = "cardnum")
    private String cardnum;

    /** 访客现场照片 */
    @Column(name = "sitephoto")
    private String sitephoto;

    // 上传公安网标识（0：未上传 1：已上传）
    @Column(name = "is_upload")
    private Integer isUpload;

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getLogofftime() {
        return logofftime;
    }

    public void setLogofftime(String logofftime) {
        this.logofftime = logofftime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsprintvoucher() {
        return isprintvoucher;
    }

    public void setIsprintvoucher(Integer isprintvoucher) {
        this.isprintvoucher = isprintvoucher;
    }

    public Integer getIspullcard() {
        return ispullcard;
    }

    public void setIspullcard(Integer ispullcard) {
        this.ispullcard = ispullcard;
    }

    public Integer getCardtype() {
        return cardtype;
    }

    public void setCardtype(Integer cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getSitephoto() {
        return sitephoto;
    }

    public void setSitephoto(String sitephoto) {
        this.sitephoto = sitephoto;
    }

    public Integer getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(Integer isUpload) {
        this.isUpload = isUpload;
    }
}
