package com.vito.storage.model;

import java.util.*;

/**
 * Describe: 查询条件
 * CreateUser: zhaixm
 * CreateTime: 2015/5/14 11:21
 */
public class Condition {

    /**
     * 查询字段名
     */
    private String fieldName;

    /**
     * 格式化后的数据
     */
    private Object formatFieldValue;

    /**
     * 格式化前的原始数据
     */
    private Object oriFieldValue;

    /**
     * 查询类型
     */
    private SearchType searchType;

    private List<Condition> subConditions = new ArrayList<>();

    private LinkType linkType;

    /**
     * @param fieldName        查询字段名
     * @param oriFieldValue    字段原始值
     * @param formatFieldValue 字段格式化后的值
     * @param searchType       查询类型
     */
    public Condition(String fieldName, Object oriFieldValue, Object formatFieldValue, String searchType) {
        this(fieldName, oriFieldValue, formatFieldValue, SearchType.valueOf(searchType.toUpperCase()));
    }

    public Condition(String fieldName, Object oriFieldValue, Object formatFieldValue, SearchType searchType) {
        this.fieldName = fieldName;
        this.oriFieldValue = oriFieldValue;
        this.formatFieldValue = formatFieldValue;
        this.searchType = searchType;
    }

    public Condition(LinkType linkType, Condition... conditions) {
        this.linkType = linkType;
        Collections.addAll(subConditions, conditions);
    }

    public boolean isLinkCond() {
        return linkType != null;
    }

    public void addSubCondition(Condition condition) {
        if (isLinkCond()) {
            subConditions.add(condition);
        } else {
            throw new RuntimeException("当前查询条件非关系连接条件，不可增加子条件");
        }
    }

    /**
     * 查询条件值+查询对象Map集
     * @return
     */
    public Map<String, Object> toMap() {
        Map<String, Object> valueMap = toValueMap(true);
        valueMap.putAll(toCondMap());
        return valueMap;
    }

    /**
     * 生成查询条件值Map
     * @param format
     * @return
     */
    private Map<String, Object> toValueMap(boolean format) {
        Map<String, Object> condMap = new HashMap<>();
        if (isLinkCond()) {
            for (Condition cond : getSubConditions()) {
                condMap.putAll(cond.toValueMap(format));
            }
        } else {
            Object fieldValue = getOriFieldValue();
            if (format) {
                fieldValue = getFormatFieldValue();
            }
            if (!condMap.containsKey(getFieldName())) {
                condMap.put(getFieldName(), fieldValue);
            } else {
                Object condValue = condMap.get(getFieldName());
                if (List.class.isAssignableFrom(condValue.getClass())) {
                    List condValList = (List) condValue;
                    condValList.add(fieldValue);
                } else {
                    List condValList = new ArrayList();
                    condValList.add(condValue);
                    condValList.add(fieldValue);
                    condMap.put(getFieldName(), condValList);
                }
            }
        }
        return condMap;
    }

    public Map<String, Object> toFormatValueMap() {
        return toValueMap(true);
    }

    public Map<String, Object> toOrigValueMap() {
        return toValueMap(false);
    }

    /**
     * 生成查询条件对象Map
     *
     * @return
     */
    public Map<String, Object> toCondMap() {
        Map<String, Object> condMap = new HashMap<>();
        if (isLinkCond()) {
            for (Condition cond : getSubConditions()) {
                condMap.putAll(cond.toCondMap());
            }
        } else {
            String condKey = getFieldName() + "Cond";
            if (!condMap.containsKey(condKey)) {
                condMap.put(condKey, this);
            }
        }
        return condMap;
    }

    /**
     * 获取条件语句
     *
     * @return
     */
    public String getCondSql() {
        StringBuilder sb = new StringBuilder();
        String operSymbol = searchType.getOperSymbol().toString();
        switch (searchType) {
            case ISNULL:
                sb.append(operSymbol);
                break;
            case ISNOTNULL:
                sb.append(operSymbol);
                break;
            case LLIKE:
                sb.append(operSymbol).append(" '%' #{").append(getFieldName()).append("} ");
                break;
            case RLIKE:
                sb.append(operSymbol).append(" #{").append(getFieldName()).append("} '%' ");
                break;
            case LIKE:
                sb.append(operSymbol).append(" '%' #{").append(getFieldName()).append("} '%' ");
                break;
            case IN:
                sb.append(operSymbol);
                Object inVals = getFormatFieldValue();
                if (inVals instanceof Integer[]) {
                    Integer[] inValInts = (Integer[]) inVals;
                    sb.append(" (");
                    int i = 0;
                    for (Integer inValInt : inValInts) {
                        sb.append(inValInt);
                        if (++i < inValInts.length) {
                            sb.append(",");
                        }
                    }
                    sb.append(")");
                } else if (inVals instanceof String[]) {
                    String[] inValStrs = (String[]) inVals;
                    sb.append(" (");
                    int i = 0;
                    for (String inValStr : inValStrs) {
                        sb.append("'").append(inValStr).append("'");
                        if (++i < inValStrs.length) {
                            sb.append(",");
                        }
                    }
                    sb.append(")");
                }
                break;
            default:
                sb.append(operSymbol).append(" #{").append(getFieldName()).append("}");
                break;
        }
        return sb.toString();
    }

    public static Condition and(Condition... conditions) {
        return new Condition(LinkType.AND, conditions);
    }

    public static Condition or(Condition... conditions) {
        return new Condition(LinkType.OR, conditions);
    }

    public static Condition eq(String fieldName, Object fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.EQ);
    }

    public static Condition eq(String fieldName, Object oriFieldValue, Object formatFieldValue) {
        return new Condition(fieldName, oriFieldValue, formatFieldValue, SearchType.EQ);
    }

    public static Condition lt(String fieldName, Object fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.LT);
    }

    public static Condition lt(String fieldName, Object oriFieldValue, Object formatFieldValue) {
        return new Condition(fieldName, oriFieldValue, formatFieldValue, SearchType.LT);
    }

    public static Condition gt(String fieldName, Object fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.GT);
    }

    public static Condition gt(String fieldName, Object oriFieldValue, Object formatFieldValue) {
        return new Condition(fieldName, oriFieldValue, formatFieldValue, SearchType.GT);
    }

    public static Condition le(String fieldName, Object fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.LE);
    }

    public static Condition le(String fieldName, Object oriFieldValue, Object formatFieldValue) {
        return new Condition(fieldName, oriFieldValue, formatFieldValue, SearchType.LE);
    }

    public static Condition ge(String fieldName, Object fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.GE);
    }

    public static Condition ge(String fieldName, Object oriFieldValue, Object formatFieldValue) {
        return new Condition(fieldName, oriFieldValue, formatFieldValue, SearchType.GE);
    }

    public static Condition like(String fieldName, Object fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.LIKE);
    }

    public static Condition like(String fieldName, Object oriFieldValue, Object formatFieldValue) {
        return new Condition(fieldName, oriFieldValue, formatFieldValue, SearchType.LIKE);
    }

    public static Condition in(String fieldName, Object... fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.IN);
    }

    public static Condition nin(String fieldName, Object... fieldValue) {
        return new Condition(fieldName, fieldValue, fieldValue, SearchType.NIN);
    }

    public static Condition exist(String fieldName) {
        return new Condition(fieldName, true, true, SearchType.EXIST);
    }

    public static Condition noExist(String fieldName) {
        return new Condition(fieldName, false, false, SearchType.EXIST);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFormatFieldValue() {
        return formatFieldValue;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Object getOriFieldValue() {
        return oriFieldValue;
    }

    public List<Condition> getSubConditions() {
        return subConditions;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public static enum SearchType {

        EQ("="), NE("<>"), LT("<"), GT(">"), LE("<="), GE(">="), LIKE("like"), LLIKE("like"), RLIKE("like"), IN("in"), NIN("not in"), EXIST("is exist"), ISNULL("is null"), ISNOTNULL("is not null");

        private String operSymbol;

        private SearchType(String operSymbol) {
            this.operSymbol = operSymbol;
        }

        public String getOperSymbol() {
            return operSymbol;
        }
    }

    public static enum LinkType {

        AND, OR;

    }

}
