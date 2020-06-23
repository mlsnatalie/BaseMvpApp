package com.libs.core.web;

import java.io.Serializable;

public class StockNoticeVo implements Serializable {

    /**
     * ANNOUNMTID : 4865527
     * ANNOUNMT1 : 临时公告
     * ANNOUNMT2 : 万华化学关于吸收合并烟台万华化工有限公司暨关联交易实施进展公告
     * PUBLISHDATE : 2018-11-13 00:00:00
     * ENTRYDATE : 2018-11-12 00:00:00
     * ENTRYTIME : 18:00:22
     */

    private int ANNOUNMTID;
    private String ANNOUNMT1;
    private String ANNOUNMT2;
    private String PUBLISHDATE;
    private String ENTRYDATE;
    private String ENTRYTIME;
    private String link;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getANNOUNMTID() {
        return ANNOUNMTID;
    }

    public void setANNOUNMTID(int ANNOUNMTID) {
        this.ANNOUNMTID = ANNOUNMTID;
    }

    public String getANNOUNMT1() {
        return ANNOUNMT1;
    }

    public void setANNOUNMT1(String ANNOUNMT1) {
        this.ANNOUNMT1 = ANNOUNMT1;
    }

    public String getANNOUNMT2() {
        return ANNOUNMT2;
    }

    public void setANNOUNMT2(String ANNOUNMT2) {
        this.ANNOUNMT2 = ANNOUNMT2;
    }

    public String getPUBLISHDATE() {
        return PUBLISHDATE;
    }

    public void setPUBLISHDATE(String PUBLISHDATE) {
        this.PUBLISHDATE = PUBLISHDATE;
    }

    public String getENTRYDATE() {
        return ENTRYDATE;
    }

    public void setENTRYDATE(String ENTRYDATE) {
        this.ENTRYDATE = ENTRYDATE;
    }

    public String getENTRYTIME() {
        return ENTRYTIME;
    }

    public void setENTRYTIME(String ENTRYTIME) {
        this.ENTRYTIME = ENTRYTIME;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
