package com.vito.common.util.string;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ParamStrFormatUtil {

    private static Logger logger = LoggerFactory.getLogger(ParamStrFormatUtil.class);

    /**
     * 获取字符串中的所有参数名
     *
     * @param paramStr
     * @return
     */
    public static Set<String> parse(String paramStr) {
        Set<String> params = new HashSet<String>(0);
        if (StringUtils.isNotBlank(paramStr)) {
            Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}");
            Matcher matcher = pattern.matcher(paramStr);
            try {
                while (matcher.find()) {
                    String group = matcher.group();
                    String param = group.substring(2, group.length() - 1);
                    params.add(param);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return params;
    }

    /**
     * 推荐使用FreemarkerUtil来格式化带参数的字符串
     *
     * @param paramStr
     * @param paramMap
     * @return
     */
    @Deprecated
    public static String format(String paramStr, Map paramMap) {
        return format(paramStr, paramMap, null);
    }

    /**
     * 取得替换参数后的url字符串
     *
     * @param paramStr 格式为：finance/consumption.do?id=${ID}&backUrl=${currentUrl}
     * @param paramMap 包含url中所使用参数的map
     * @param source   url中可以使用source中的属性，可以用objA.objB.name格式
     * @return
     */
    @Deprecated
    public static String format(String paramStr, Map<String, ?> paramMap, Object source) {
        String formatUrl = paramStr;
        Set<String> params = ParamStrFormatUtil.parse(paramStr);
        try {
            for (String param : params) {
                Object fieldValue = "";
                if (!paramMap.keySet().contains(param)) {
                    fieldValue = source;
                    for (String splitParam : param.split("\\.")) {
                        if (fieldValue != null) {
                            try {
                                fieldValue = PropertyUtils.getProperty(fieldValue, splitParam);
                            } catch (NoSuchMethodException e) {
                                fieldValue = "";
                                e.printStackTrace();
                                logger.error("无法取得URL:" + paramStr + "中的参数:" + param + "的值");
                                break;
                            }
                        } else {
                            fieldValue = "";
                            logger.error("无法取得URL:" + paramStr + "中的参数:" + param + "的值");
                            break;
                        }
                    }
                } else {
                    if (paramMap.get(param) != null) {
                        fieldValue = String.valueOf(paramMap.get(param));
                    }
                }
                formatUrl = formatUrl.replaceAll("\\$\\{" + param + "\\}", fieldValue.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return formatUrl;
    }

    public static void main(String[] args) throws Exception {
//		String url = "commonSimpleList.do?listId=PROJECT_VERSION_LOG&query_param_version_id_eq=${VERSION_ID}&page.pageIndex=${page.pageIndex}";
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("VERSION_ID", "2000");
//		SimpleListController controller = new CommonSimpleListController();
//		controller.getPage().setPageIndex("3");
//		System.out.println(PropertyUtils.getProperty(controller, "page"));
//		System.out.println(format(url, map, controller));

    }
}
