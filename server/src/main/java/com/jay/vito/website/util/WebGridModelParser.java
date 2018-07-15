package com.jay.vito.website.util;

import com.vito.common.util.validate.Validator;
import com.vito.common.util.web.WebUtil;
import com.vito.storage.model.Condition;
import com.vito.storage.model.Order;
import com.vito.storage.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/5/14 13:49
 */
public class WebGridModelParser implements GridModelParser {

    private static final Logger LOG = LoggerFactory.getLogger(WebGridModelParser.class);

    private HttpServletRequest request;

    private Map<String, Object> requestParams;

    public WebGridModelParser(HttpServletRequest request) {
        this.request = request;
        parseParams();
    }

    private void parseParams() {
        requestParams = WebUtil.parseParams(request);
    }

    @Override
    public Page parsePage() {
        int pageNo = Validator.isNotNull(request.getParameter("pageNo")) ? Integer.valueOf(request.getParameter("pageNo")) : 1;
        Page page = new Page(pageNo);
        String pageSize = request.getParameter("pageSize");
        if (Validator.isNotNull(pageSize)) {
            page.setPageSize(Integer.valueOf(pageSize));
        }
        return page;
    }

    @Override
    public Condition parseCondition() {
        List<Condition> conditions = new ArrayList<>();
        if (Validator.isNotNull(requestParams)) {
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                String paramKey = entry.getKey();
                Object paramValue = entry.getValue();
                Condition condition = GridModelUtil.parseCondition(paramKey, paramValue);
                if (condition != null) {
                    conditions.add(condition);
                }
            }
        }
        return Condition.and(conditions.toArray(new Condition[conditions.size()]));
    }

    @Override
    public Order parseOrders() {
        List<Order> orders = new ArrayList<>();
        if (Validator.isNotNull(requestParams)) {
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                String paramKey = entry.getKey();
                Object paramValue = entry.getValue();
                Order order = GridModelUtil.parseOrder(paramKey, (String) paramValue);
                if (order != null) {
                    orders.add(order);
                }
            }
        }
        return Order.init(orders);
    }

}
