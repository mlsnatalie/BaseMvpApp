package com.libs.core.business.events;

/**
 * 诊股相关的事件
 */
public class StockEvent extends BaseEvent {

    public StockEvent(int eventId) {
        super(eventId);
    }

    public static final String CATE_NAME = "cate_name";
    public static final String TAB_PAGE = "tab_page";
    public static final String WEB_URL = "web_url";

    public static final int EVENT_DELETE_HISTORY = 0x2001;      //删除历史
    public static final int EVENT_SWITCH_STOCK_TOTAL = 0x2002;  //选股总榜
    public static final int EVENT_SELECT_STOCK_TOTAL = 0x2003;  //选股总数
    public static final int EVENT_RECORD_FREE_COUNT = 0x2004;   //记录免费诊股总数
    public static final int EVENT_SWITCH_STOCK_SCHOOL = 0x2005; //切换到小股学堂
    public static final int EVENT_SWITCH_MASTER = 0x2006;       //切换到大咖专区
    public static final int EVENT_SWITCH_MASTER_LIST = 0x2007;  //切换到大咖专区
    public static final int EVENT_ADD_STOCK_SELF = 0x2008; //增加至自选
    public static final int EVENT_DELETE_STOCK_SELF = 0x2009; //从自选删除
    public static final int EVENT_REFRESH_STOCK_QUOTE = 0x2010; //刷新行情主页
    public static final int EVENT_SWITCH_MELON_EXPRESS = 0x2011; //切换到西瓜速递
    public static final int EVENT_SWITCH_WEB_PAGE = 0x2012; //切换到h5页面
    public static final int EVENT_SWITCH_MARKET = 0x2013; //切换到市场页面
    public static final int EVENT_H5_REFRESH_SELF = 0x2014; //h5刷新列表
    public static final int EVENT_XGYX_REFRESH_SELF = 0x2015; //首页卡片视图刷新广播
    public static final int EVENT_SWITCH_QUOTE_PAGE = 0x2016; //切换到详情
    public static final int EVENT_REFRESH_SELF = 0x2017; //刷新自选
    public static final int EVENT_REFRESH_MASTER_BOTTOM = 0x2018;//刷新大咖推荐和牛人课堂
}
