package com.vito.website.web.controller;

import com.vito.common.util.bean.ReflectionUtil;
import com.vito.storage.domain.BaseEntity;
import com.vito.storage.model.Condition;
import com.vito.storage.model.Order;
import com.vito.storage.model.Page;
import com.vito.storage.service.EntityCRUDService;
import com.vito.storage.service.QueryService;
import com.vito.website.util.GridModelParser;
import com.vito.website.util.GridModelUtil;
import com.vito.website.util.WebGridModelParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/3/3 15:30
 */
public abstract class BaseGridController<T extends BaseEntity, ID extends Serializable> extends BaseController {

    @Autowired
    private QueryService queryService;

    // controller是单例  不能有全局变量，否则会导致请求的查询条件错乱必须保证线程安全
    protected ThreadLocal<Condition> conditionLocal = new ThreadLocal<>();
    protected ThreadLocal<Page> pageLocal = new ThreadLocal<>();
    protected ThreadLocal<Order> orderLocal = new ThreadLocal<>();

    protected void setPage(Page page) {
        pageLocal.set(page);
    }

    protected Page getPage() {
        return pageLocal.get();
    }

    protected void setCondition(Condition condition) {
        conditionLocal.set(condition);
    }

    protected Condition getCondition() {
        return conditionLocal.get();
    }

    protected void setOrder(Order order) {
        orderLocal.set(order);
    }

    protected Order getOrder() {
        return orderLocal.get();
    }

    public Page<T> query() {
        return pageQuery();
    }

    public List<T> getAll() {
        return getEntityService().getAll();
    }

    public T get(ID id) {
        return getEntityService().get(id);
    }

    public T save(T entity) {
        return getEntityService().save(entity);
    }

    public T update(ID id, T entity) {
        entity.setId(id);
        return getEntityService().updateNotNull(entity);
    }

    public Boolean delete(ID id) {
        getEntityService().delete(id);
        return true;
    }

    protected abstract EntityCRUDService<T, ID> getEntityService();

    /**
     * 解析request获取查询条件、排序条件等
     */
    public void before() {
        super.before();
        GridModelParser gridParser = new WebGridModelParser(getRequest());
        Page page = gridParser.parsePage();
        setPage(page);
        Condition condition = gridParser.parseCondition();
        setCondition(condition);
        Order order = gridParser.parseOrders();
        setOrder(order);
    }

    /**
     * 添加排序字段
     *
     * @param fieldName
     * @param sortType
     */
    protected void addOrder(String fieldName, Order.SortType sortType) {
        addOrder(new Order(fieldName, sortType));
    }

    protected void addOrder(Order subOrder) {
        Order order = getOrder();
        if (order == null) {
            order = Order.init(subOrder);
        } else {
            if (order.isLinkOrder()) {
                order.add(subOrder);
            } else {
                order = Order.init(order, subOrder);
            }
        }
        setOrder(order);
    }

    /**
     * 添加查询条件
     *
     * @param fieldName
     * @param oriValue
     * @param formatVal
     * @param searchType
     */
    protected void addCondition(String fieldName, Object oriValue, Object formatVal, Condition.SearchType searchType) {
        Condition cond = new Condition(fieldName, oriValue, formatVal, searchType);
        addCondition(cond);
    }

    /**
     * 添加查询条件
     *
     * @param cond
     */
    protected void addCondition(Condition cond) {
        Condition condition = getCondition();
        if (condition == null) {
            condition = Condition.and(cond);
        } else {
            if (condition.isLinkCond()) {
                condition.addSubCondition(cond);
            } else {
                condition = Condition.and(condition, cond);
            }
        }
        setCondition(condition);
    }

    /**
     * 将查询条件值、排序条件等传回页面，用于回显
     */
    public void after() {
        Condition condition = getCondition();
        Map<String, Object> conditionMap = GridModelUtil.convertCondition(condition);
        getRequest().setAttribute("conditions", conditionMap);
        super.after();
    }

    protected Page pageQuery(String countStatement, String qryStatement) {
        Page page = getPage();
        doPageQuery(page, countStatement, qryStatement);
        getRequest().setAttribute("page", page);
        return page;
    }

    protected void doPageQuery(Page<T> page, String countStatement, String qryStatement) {
        Condition condition = getCondition();
        Order order = getOrder();
        if (page.getTotalCount() < 0) {
            long totalCount = getQueryService().count(countStatement, condition);
            page.setTotalCount(totalCount);
        }
        // 当有数据时才执行分页查询
        if (page.getEndIndex() - page.getBeginIndex() > 0) {
            List items = getQueryService().queryForPagination(qryStatement, condition, order, ((Long) page.getBeginIndex())
                    .intValue(), ((Long) page.getEndIndex()).intValue());
            page.setItems(items);
        }
        // 当有数据且pageSize小于0时直接查询全部数据，用于前端不分页时调用
        if (page.getPageSize() < 0 && page.getTotalCount() > 0) {
            List results = getQueryService().queryForPagination(qryStatement, condition, order, ((Long) page.getBeginIndex())
                    .intValue(), ((Long) page.getTotalCount()).intValue());
            page.setItems(results);
        }
    }

    protected Page pageQuery() {
//        String namespace = getEntityClass().getSimpleName();
        // 由于mybatis扫描MybatisMapper子对象的时候注册上去的mapper名称是全路径，所以此处要统一 以便一个对象的mapper可以防止一起
        String namespace = getEntityClass().getName() + "Mapper";
        return pageQuery(namespace + ".countList", namespace + ".selectList");
    }

    protected Class<T> getEntityClass() {
        return ReflectionUtil.getSuperClassGenricType(this.getClass());
    }

    protected QueryService getQueryService() {
        return queryService;
    }

}
