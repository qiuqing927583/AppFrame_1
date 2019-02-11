package com.android.yawei.jhoa.addressbook;

import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.bean.User;

/**
 * Created by Yusz on 2018-6-13.
 */

public interface FragmentCall {

    public void OnClickGroup(GroupBean info);
    public void OnClickUser(User user);
    public void OnSreachUser(String userName);
    public void OnSeclectUserSize(int size);
}
