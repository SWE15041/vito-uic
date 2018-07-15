package com.jay.vito.storage.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describe: 排序字段
 * CreateUser: zhaixm
 * CreateTime: 2015/5/14 11:21
 */
public class Order {

    private String fieldName;
    private SortType sortType;
    private boolean linkOrder = false;
    private List<Order> orders = new ArrayList<>();

    public Order(String fieldName, String sortType) {
        this.fieldName = fieldName;
        this.sortType = SortType.valueOf(sortType.toUpperCase());
    }

    public Order(String fieldName, SortType sortType) {
        this.fieldName = fieldName;
        this.sortType = sortType;
    }

    public Order(boolean linkOrder, Order... orders) {
        this.linkOrder = linkOrder;
        Collections.addAll(this.orders, orders);
    }

    public static Order init(Order... orders) {
        return new Order(true, orders);
    }

    public static Order init(List<Order> orders) {
        Order[] orderArr = new Order[orders.size()];
        orders.toArray(orderArr);
        return init(orderArr);
    }

    public boolean isLinkOrder() {
        return linkOrder;
    }

    public void add(Order order) {
        if (linkOrder) {
            orders.add(order);
        } else {
            throw new RuntimeException("当前排序对象非排序连接对象，不可增加子排序");
        }
    }

    public static Order desc(String fieldName) {
        return new Order(fieldName, SortType.DESC);
    }

    public static Order asc(String fieldName) {
        return new Order(fieldName, SortType.ASC);
    }

    public String orderSql() {
        if (linkOrder) {
            StringBuilder sb = new StringBuilder();
            int i=0;
            for (Order order : orders) {
                sb.append(order.orderSql());
                if (++i < orders.size()) {
                    sb.append(",");
                }
            }
            return sb.toString();
        } else {
            return fieldName + " " + sortType;
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public static enum SortType {
        ASC, DESC
    }
}
