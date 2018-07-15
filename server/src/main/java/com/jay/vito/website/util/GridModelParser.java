package com.jay.vito.website.util;
/**
 * 等级：A
 * Created by zhaixm on 2015/5/14.
 */

import com.vito.storage.model.Condition;
import com.vito.storage.model.Order;
import com.vito.storage.model.Page;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/5/14 11:43
 */
public interface GridModelParser {

    /**
     * 解析出分页对象
     * @return
     */
    Page parsePage();

    Condition parseCondition();

    Order parseOrders();

}
