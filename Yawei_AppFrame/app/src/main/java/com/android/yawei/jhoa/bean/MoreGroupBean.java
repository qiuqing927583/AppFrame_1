package com.android.yawei.jhoa.bean;

import java.util.List;

/**
 * Created by Yusz on 2018-1-16.
 */

public class MoreGroupBean {
    private String groupName;
    private List<AppOptionBean> listOption;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<AppOptionBean> getListOption() {
        return listOption;
    }

    public void setListOption(List<AppOptionBean> listOption) {
        this.listOption = listOption;
    }
}
