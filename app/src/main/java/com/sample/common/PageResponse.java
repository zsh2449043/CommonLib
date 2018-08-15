package com.sample.common;

import java.util.List;

public class PageResponse {

    /**
     * status : true
     * msg : success
     * title : photo Class
     * parameters : {"intervalTime":""}
     * page : [{"id":284,"pid":0,"name":"主页面","launchId":75,"pageType":1,"style":1,"childPage":[],"index":1}]
     * date : 1533889118073
     */

    private boolean status;
    private String msg;
    private String title;
    private ParametersBean parameters;
    private long date;
    private List<PageBean> page;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ParametersBean getParameters() {
        return parameters;
    }

    public void setParameters(ParametersBean parameters) {
        this.parameters = parameters;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<PageBean> getPage() {
        return page;
    }

    public void setPage(List<PageBean> page) {
        this.page = page;
    }

    public static class ParametersBean {
        /**
         * intervalTime :
         */

        private String intervalTime;

        public String getIntervalTime() {
            return intervalTime;
        }

        public void setIntervalTime(String intervalTime) {
            this.intervalTime = intervalTime;
        }
    }

    public static class PageBean {
        /**
         * id : 284
         * pid : 0
         * name : 主页面
         * launchId : 75
         * pageType : 1
         * style : 1
         * childPage : []
         * index : 1
         */

        private int id;
        private int pid;
        private String name;
        private int launchId;
        private int pageType;
        private int style;
        private int index;
        private List<?> childPage;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLaunchId() {
            return launchId;
        }

        public void setLaunchId(int launchId) {
            this.launchId = launchId;
        }

        public int getPageType() {
            return pageType;
        }

        public void setPageType(int pageType) {
            this.pageType = pageType;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<?> getChildPage() {
            return childPage;
        }

        public void setChildPage(List<?> childPage) {
            this.childPage = childPage;
        }
    }
}
