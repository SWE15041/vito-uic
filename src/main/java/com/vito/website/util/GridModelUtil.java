package com.vito.website.util;

import com.vito.common.util.validate.Validator;
import com.vito.storage.model.Condition;
import com.vito.storage.model.Order;
import com.vito.website.model.DataType;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/5/14 13:49
 */
public class GridModelUtil {

    private static final Logger LOG = Logger.getLogger(GridModelUtil.class);

    private static final String CONDITION_PREFIX = "_filter_";

    private static final String SORT_PREFIX = "_sort_";

    public static Condition parseCondition(String conditionStr, Object condValue) {
        if (Validator.isNotNull(conditionStr) && condValue != null && conditionStr.startsWith(CONDITION_PREFIX)) {
                try {
//                Pattern compile = Pattern.compile(CONDITION_PREFIX + "([^_]+)_([^#]+)(#(.*))?");
                Pattern compile = Pattern.compile(CONDITION_PREFIX + "([^_]+)_([^-]+)(-(.*))?");
                Matcher matcher = compile.matcher(conditionStr);
                if (matcher.find()) {
                    String searchTypeStr = matcher.group(1);
                    String fieldName = matcher.group(2);
                    String fieldType = matcher.group(4);
                    Object formatFieldValue = condValue;
                    if (Validator.isNotNull(fieldType)) {
                        DataType dataType = DataType.valueOf(fieldType.toUpperCase());
                        formatFieldValue = dataType.parse((String) condValue);
                    }
                    Condition condition = new Condition(fieldName, condValue, formatFieldValue, searchTypeStr);
                    return condition;
                }
            } catch (Exception e) {
                LOG.error("解析查询条件出错", e);
            }
        }
        return null;
    }

    public static Order parseOrder(String orderStr, String sortType) {
        if (Validator.isNotNull(orderStr) && orderStr.startsWith(SORT_PREFIX)) {
            try {
                Pattern compile = Pattern.compile(SORT_PREFIX + "(.+)");
                Matcher matcher = compile.matcher(orderStr);
                if (matcher.find()) {
                    String fieldName = matcher.group(1);
                    return new Order(fieldName, sortType);
                }
            } catch (Exception e) {
                LOG.error("解析查询条件出错", e);
            }
        }
        return null;
    }

    public static Map<String, Object> convertCondition(Condition condition) {
        Map<String, Object> conditionMap = new HashMap<>();
        convertCondition2Map(condition, conditionMap);
        return conditionMap;
    }

    private static void convertCondition2Map(Condition condition, Map<String, Object> map) {
        if (Validator.isNotNull(condition)) {
            if (condition.isLinkCond()) {
                for (Condition cond : condition.getSubConditions()) {
                    convertCondition2Map(cond, map);
                }
            } else {
                if (!map.containsKey(condition.getFieldName())) {
                    map.put(condition.getFieldName(), condition.getOriFieldValue());
                } else {
                    Object condValue = map.get(condition.getFieldName());
                    if (List.class.isAssignableFrom(condValue.getClass())) {
                        List condValList = (List)condValue;
                        condValList.add(condition.getOriFieldValue());
                    } else {
                        List condValList = new ArrayList();
                        condValList.add(condValue);
                        condValList.add(condition.getOriFieldValue());
                        map.put(condition.getFieldName(), condValList);
                    }
                }
            }
        }
    }

    public static String convertOrders(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        if (Validator.isNotNull(orders)) {
            int i = 1;
            for (Order order : orders) {
                sb.append(order.getFieldName()).append(" ").append(order.getSortType());
                if (i++ < orders.size()) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
//        Pattern compile = Pattern.compile("_filter_([^_]+)_([^#]+)(#(.*))?");
//        Matcher matcher = compile.matcher("_filter_eq_keyword_name#date");
        Pattern compile = Pattern.compile(CONDITION_PREFIX + "([^_]+)_([^-]+)(-(.*))?");
        Matcher matcher = compile.matcher("_filter_like_order_state-long");
        if (matcher.find()) {
            String searchTypeStr = matcher.group(1);
            String fieldName = matcher.group(2);
            String fieldType = matcher.group(4);
            System.out.println(searchTypeStr + " " + fieldName + " " + " " + fieldType);
        }
    }
}
