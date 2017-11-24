package com.vito.common.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * 专门处理json格式的解析
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JsonParser
{
    /**
     * JSON处理含有嵌套关系对象
     * 注意：这样获得到的字符串中，引起嵌套循环的属性会置为null
     *
     * @param obj
     * @return
     */
    public static JSONObject getJsonObject(Object obj){
        if (obj instanceof String) {
            return JSONObject.parseObject((String) obj);
        } else {
            return (JSONObject) JSONObject.toJSON(obj);
        }
    }

    /**
     * JSON处理含有嵌套关系对象
     * 注意：这样获得到的字符串中，引起嵌套循环的属性会置为null
     *
     * @param obj
     * @return
     */
    public static JSONArray getJsonArray(Object obj) {
        if (obj instanceof String) {
            return JSONArray.parseArray((String) obj);
        } else {
            return (JSONArray) JSONArray.toJSON(obj);
        }
    }

    /**
     * 解析JSON字符串成一个MAP
     *
     * @param jsonStr json字符串，格式如： {dictTable:"BM_XB",groupValue:"分组值"}
     * @return
     */
    public static Map<String, String> parseJson2Map(String jsonStr){

        Map<String, String> result = new HashMap<String, String>();

        JSONObject jsonObj = JsonParser.getJsonObject(jsonStr);

        for(Object key : jsonObj.keySet()){
            result.put((String)key, jsonObj.get(key)==null ? "":jsonObj.get(key).toString());
        }
        return result;
    }

    /**
     * 解析Json字符串，自动转换成一个对象的集合
     *
     * @param jsonStr  如：[{tabId:'tab_tdjxjh',display:'true',index:'1'},{tabId:'tab_tdjxjh',display:'true',index:'1'},{tabId:'tab_tdjxjh',display:'true',index:'1'}]
     * @param objClass  DdrzTabConf.class
     * @return
     */
    public static <T> List<T> parseJson2List(String jsonStr, Class<T> objClass){
        if(StringUtils.isEmpty(jsonStr))
            return null;

        JSONArray jsonArray = JsonParser.getJsonArray(jsonStr);
        List<T> list = new ArrayList<T>();
        int size = jsonArray.size();
        for (int i = 0; i < size; ++i) {
            JSONObject obj = jsonArray.getJSONObject(i);
            list.add(JSONObject.toJavaObject(obj, objClass));
        }

        return list;
    }

    public static <T> T parseJson2Obj(String jsonStr, Class<T> objClass) {
        JSONObject jsonObject = getJsonObject(jsonStr);
        return JSONObject.toJavaObject(jsonObject, objClass);
    }

    @SuppressWarnings({ "deprecation" })
    public static List<JSONObject> parseJsonArray(String jsonArrayStr){
        if(StringUtils.isEmpty(jsonArrayStr))
            return null;

        JSONArray jsonArray = JsonParser.getJsonArray(jsonArrayStr);
        List<JSONObject> result = new ArrayList<JSONObject>();

        int size = jsonArray.size();
        for (int i = 0; i < size; ++i) {
            result.add(jsonArray.getJSONObject(i));
        }

        return result;
    }

    /**
     * 把对象转换成为Json字符串
     *
     * @param obj
     * @return
     */
    public static String convertObjectToJson(Object obj, final String... excludeProps){
        if(obj == null){
            throw new RuntimeException("对象参数不能为空");
        }
        String jsonString = JSONObject.toJSONString(obj, filter(excludeProps));
        return jsonString;
    }

    /**
     * 把数组或集合对象转换成为Json字符串
     *
     * @param obj
     * @return
     */
    public static String convertArrayToJson(Object obj, final String... excludeProps){
        if(obj == null){
            throw new RuntimeException("对象参数不能为空");
        }
        String jsonString = JSONArray.toJSONString(obj, filter(excludeProps));
        return jsonString;
    }

    private static PropertyFilter filter(final String... excludeProps) {
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean apply(Object target, String propName, Object propVal) {
                for (String excludeField :excludeProps) {
                    if (excludeField.equals(propName)) {
                        return false;
                    }
                }
                return true;
            }
        };
        return filter;
    }

    public static void main(String[] args)
    {
        Map<String,String> linkedMap = new LinkedHashMap<String,String>();
        linkedMap.put("bb","bbbbb");
        linkedMap.put("22","22222");
        linkedMap.put("11","11111");
        linkedMap.put("aa","aaaaa");
        for(String nextObj : linkedMap.keySet()){
            System.out.println(nextObj + " = " + linkedMap.get(nextObj));
        }
        System.out.println(convertObjectToJson(linkedMap));

        String str1 = "[{wideKnow:'chenmk',attrVal:'20080702~20080702'},{wideKnow:'chmk35',attrVal:'~20080702'}]";
        System.out.println(parseJsonArray(str1));

        String str = "801:'801_WF_SBTYSQD_GL,802_WF_SBTYSQD_GL,803_WF_SBTYSQD_GL',805:'805_WF_SQDYQ_GL',806:'806_WF_SQDSY_GL',807:'807_WF_XCKCD_GL',810:'810_WF_FSBGTZD_GL',811:'811_WF_SDGATZD_GL',812:'812_WF_ZDRWSQD_GL',813:'813_WF_ZDZTZD_GL',814:'814_WF_BGDYA_GL',901:'901_WF_GZP_DQDYZ_GL',902:'902_WF_GZP_DQDEZ_GL',903:'903_WF_GZP_BDZDDZY_GL',904:'904_WF_GZP_BDZSGYJQXD_GL',905:'905_WF_GZP_ECGZAQCSP_GL',906:'906_WF_GZP_DLXLDYZ_GL',907:'907_WF_GZP_DLXLDEZ_GL',908:'908_WF_GZP_DLXLDDZY_GL',909:'909_WF_GZP_DLXLSGYJQXD_GL',910:'910_WF_GZP_XLDLGZRWD_GL',911:'911_WF_GZP_DLDLDYZ_GL',912:'912_WF_GZP_DLDLDEZ_GL'";
        str = "{"+str+"}";
        Map<String,String> map = parseJson2Map(str);
        System.out.println(map);

        String tmp = "[{a:'chenmk',b:['EEE-DDD','MMMM']},{a:'chmk35',b:['ddddddddd','ccccccccc']}]";
        List<JSONObject> result = parseJsonArray(tmp);

        for(JSONObject jsonObj : result){
            System.out.println(jsonObj.getString("a"));
            Object obj = jsonObj.get("b");
            System.out.println(obj);
            System.out.println(jsonObj.getJSONArray("b").get(0));
        }
        List<Student> students = new ArrayList<>();
        Student student = new Student("张三", "山东兖州");
        students.add(student);
        Student student1 = new Student("李四", "山东德州");
        students.add(student1);
        System.out.println(convertArrayToJson(students, "addr"));
    }

    public static class Student {
        private String name;
        private String addr;

        public Student(String name, String addr) {
            this.name = name;
            this.addr = addr;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }
    }

}
