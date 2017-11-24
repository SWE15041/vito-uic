package com.vito.common.util.cache;

import com.vito.common.util.bean.SerializeUtil;
import com.vito.common.util.string.StringUtil;
import com.vito.common.util.validate.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

/**
 * <p>redis通用工具类</p>
 *
 * @author zhaixm
 */
public class RedisCacheUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheUtil.class);

    private JedisPool jedisPool = null;

    /**
     * <p>传入ip和端口号构建redis 连接池</p>
     *
     * @param ip   ip
     * @param port 端口
     */
    public RedisCacheUtil(String ip, int port) {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(500);
            // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWaitMillis(1000 * 100);
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            // pool = new JedisPool(config, "192.168.0.121", 6379, 100000);
            jedisPool = new JedisPool(config, ip, port, 100000);
        }
    }

    /**
     * <p>通过配置对象 ip 端口 构建连接池</p>
     *
     * @param config 配置对象
     * @param ip     ip
     * @param prot   端口
     */
    public RedisCacheUtil(JedisPoolConfig config, String ip, int prot) {
        if (jedisPool == null) {
            jedisPool = new JedisPool(config, ip, prot, 10000);
        }
    }

    /**
     * <p>通过配置对象 ip 端口 超时时间 构建连接池</p>
     *
     * @param config  配置对象
     * @param ip      ip
     * @param prot    端口
     * @param timeout 超时时间
     */
    public RedisCacheUtil(JedisPoolConfig config, String ip, int prot, int timeout) {
        if (jedisPool == null) {
            jedisPool = new JedisPool(config, ip, prot, timeout);
        }
    }

    /**
     * <p>通过连接池对象 构建一个连接池</p>
     *
     * @param jedisPool 连接池对象
     */
    public RedisCacheUtil(JedisPool jedisPool) {
        if (this.jedisPool == null) {
            this.jedisPool = jedisPool;
        }
    }

    /**
     * 获取redis连接对象
     *
     * @return
     */
    public Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    /**
     * 关闭redis连接池
     */
    public void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    /**
     * <p>通过key获取储存在redis中的value</p>
     * <p>并释放连接</p>
     *
     * @param key
     * @return 成功返回value 失败返回null
     */
    public Object get(String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            byte[] bytes = jedis.get(convertObj2Bytes(key));
            return SerializeUtil.convertBytes2Obj(bytes);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>向redis存入key和value,并释放连接资源</p>
     * <p>如果key已经存在 则覆盖</p>
     *
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 0
     */
    public String set(String key, Object value) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            return jedis.set(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(value));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }


    /**
     * <p>删除指定的key,也可以传入一个包含key的数组</p>
     *
     * @param keys 一个key  也可以使 string 数组
     * @return 返回删除成功的个数
     */
    public Long del(String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            return jedis.del(keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key向指定的value值追加值</p>
     *
     * @param key
     * @param obj
     * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度  异常返回0L
     */
    public Long append(String key, Object obj) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.append(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(obj));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>判断key是否存在</p>
     *
     * @param key
     * @return true OR false
     */
    public Boolean exists(String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            return jedis.exists(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>设置key的过期时间</p>
     *
     * @param key
     * @return true OR false
     */
    public void expire(String key, int seconds) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            jedis.expire(key, seconds);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>设置key value,如果key已经存在则返回0,nx==> not exist</p>
     *
     * @param key
     * @param value
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public Long setnx(String key, Object value) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            return jedis.setnx(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(value));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>设置key value并制定这个键值的有效期</p>
     *
     * @param key
     * @param value
     * @param seconds 单位:秒
     * @return 成功返回OK 失败和异常返回null
     */
    public String setex(String key, Object value, int seconds) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            return jedis.setex(convertObj2Bytes(key), seconds, SerializeUtil.convertObj2Bytes(value));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }


    /**
     * <p>通过key 和offset 从指定的位置开始将原先value替换</p>
     * <p>下标从0开始,offset表示从offset下标开始替换</p>
     * <p>如果替换的字符串长度过小则会这样</p>
     * <p>example:</p>
     * <p>value : bigsea@zto.cn</p>
     * <p>str : abc </p>
     * <P>从下标7开始替换  则结果为</p>
     * <p>RES : bigsea.abc.cn</p>
     *
     * @param key
     * @param str
     * @param offset 下标位置
     * @return 返回替换后  value 的长度
     */
    public Long setrange(String key, String str, int offset) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            return jedis.setrange(key, offset, str);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }


    /**
     * <p>通过批量的key获取批量的value</p>
     *
     * @param keys string数组 也可以是一个key
     * @return 成功返回value的集合, 失败返回null的集合 ,异常返回空
     */
    public List<String> mget(String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        List<String> values = null;
        try {
            jedis = getJedis();
            values = jedis.mget(keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return values;
    }

    /**
     * <p>批量的设置key:value,可以一个</p>
     * <p>example:</p>
     * <p>  obj.mset(new String[]{"key2","value1","key2","value2"})</p>
     *
     * @param keysvalues
     * @return 成功返回OK 失败 异常 返回 null
     */
    public String mset(Object... keysvalues) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.mset(convertObjs2Bytes(keysvalues));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    private byte[] convertObj2Bytes(Object obj) {
        if (Validator.isNull(obj)) {
            return null;
        }
        if (obj instanceof String) {
            return StringUtil.getBytes((String) obj);
        }
        return SerializeUtil.convertObj2Bytes(obj);
    }

    private byte[][] convertObjs2Bytes(Object[] objs) {
        List<byte[]> byteList = new ArrayList<>();
        for (Object obj : objs) {
            byteList.add(SerializeUtil.convertObj2Bytes(obj));
        }
        byte[][] byteArr = new byte[objs.length][];
        return byteList.toArray(byteArr);
    }

    /**
     * <p>批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚</p>
     * <p>example:</p>
     * <p>  obj.msetnx(new String[]{"key2","value1","key2","value2"})</p>
     *
     * @param keysvalues
     * @return 成功返回1 失败返回0
     */
    public Long msetnx(Object... keysvalues) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = 0L;
        try {
            jedis = getJedis();
            List<byte[]> byteList = new ArrayList<>();
            for (Object keyvalue : keysvalues) {
                byteList.add(SerializeUtil.convertObj2Bytes(keyvalue));
            }
            byte[][] byteArr = new byte[keysvalues.length][];
            res = jedis.msetnx(byteList.toArray(byteArr));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>设置key的值,并返回一个旧值</p>
     *
     * @param key
     * @param value
     * @return 旧值 如果key不存在 则返回null
     */
    public String getSet(String key, String value) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.getSet(key, value);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过下标 和key 获取指定下标位置的 value</p>
     *
     * @param key
     * @param startOffset 开始位置 从0 开始 负数表示从右边开始截取
     * @param endOffset
     * @return 如果没有返回null
     */
    public String getrange(String key, int startOffset, int endOffset) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.getrange(key, startOffset, endOffset);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1</p>
     *
     * @param key
     * @return 加值后的结果
     */
    public Long incr(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.incr(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key给指定的value加值,如果key不存在,则这是value为该值</p>
     *
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(String key, Long integer) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.incrBy(key, integer);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>对key的值做减减操作,如果key不存在,则设置key为-1</p>
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.decr(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>减去指定的值</p>
     *
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(String key, Long integer) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.decrBy(key, integer);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取value值的长度</p>
     *
     * @param key
     * @return 失败返回null
     */
    public Long serlen(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.strlen(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key给field设置指定的值,如果key不存在,则先创建</p>
     *
     * @param key
     * @param field 字段
     * @param value
     * @return 如果存在返回0 异常返回null
     */
    public Long hset(String key, Object field, Object value) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.hset(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(field), SerializeUtil.convertObj2Bytes(value));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0</p>
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hsetnx(String key, Object field, Object value) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.hsetnx(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(field), SerializeUtil.convertObj2Bytes(value));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key同时设置 hash的多个field</p>
     *
     * @param key
     * @param data
     * @return 返回OK 异常返回null
     */
    public String hmset(String key, Map<Object, Object> data) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            Map<byte[], byte[]> byteMap = new HashMap<>();
            for (Map.Entry entry : data.entrySet()) {
                Object entryKey = entry.getKey();
                Object entryVal = entry.getValue();
                byteMap.put(SerializeUtil.convertObj2Bytes(entryKey), SerializeUtil.convertObj2Bytes(entryVal));
            }
            res = jedis.hmset(convertObj2Bytes(key), byteMap);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key 和 field 获取指定的 value</p>
     *
     * @param key
     * @param field
     * @return 没有返回null
     */
    public Object hget(String key, Object field) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            byte[] bytes = jedis.hget(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(field));
            return SerializeUtil.convertBytes2Obj(bytes);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key 和 fields 获取指定的value 如果没有对应的value则返回null</p>
     *
     * @param key
     * @param fields 可以使 一个String 也可以是 String数组
     * @return
     */
    public List<Object> hmget(String key, Object... fields) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            List<byte[]> bytesList = jedis.hmget(convertObj2Bytes(key), convertObjs2Bytes(fields));
            List<Object> res = new ArrayList<>();
            for (byte[] bytes : bytesList) {
                res.add(SerializeUtil.convertBytes2Obj(bytes));
            }
            return res;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key给指定的field的value加上给定的值</p>
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrby(String key, Object field, Long value) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.hincrBy(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(field), value);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key和field判断是否有指定的value存在</p>
     *
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key, Object field) {
        Jedis jedis = null;
        boolean broken = false;
        Boolean res = false;
        try {
            jedis = getJedis();
            res = jedis.hexists(convertObj2Bytes(key), SerializeUtil.convertObj2Bytes(field));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回field的数量</p>
     *
     * @param key
     * @return
     */
    public Long hlen(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.hlen(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;

    }

    /**
     * <p>通过key 删除指定的 field </p>
     *
     * @param key
     * @param fields 可以是 一个 field 也可以是 一个数组
     * @return
     */
    public Long hdel(String key, Object... fields) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.hdel(convertObj2Bytes(key), convertObjs2Bytes(fields));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回所有的field</p>
     *
     * @param key
     * @return
     */
    public Set<Object> hkeys(String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            Set<Object> res = new HashSet<>();
            jedis = getJedis();
            Set<byte[]> hkeys = jedis.hkeys(convertObj2Bytes(key));
            for (byte[] hkey : hkeys) {
                res.add(SerializeUtil.convertBytes2Obj(hkey));
            }
            return res;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key返回所有和key有关的value</p>
     *
     * @param key
     * @return
     */
    public List<Object> hvals(String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            List<Object> res = new ArrayList<>();
            jedis = getJedis();
            List<byte[]> hvals = jedis.hvals(convertObj2Bytes(key));
            for (byte[] hval : hvals) {
                res.add(SerializeUtil.convertBytes2Obj(hval));
            }
            return res;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key获取所有的field和value</p>
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hgetAll(String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            Map<Object, Object> res = new HashMap<>();
            jedis = getJedis();
            Map<byte[], byte[]> bytesMap = jedis.hgetAll(convertObj2Bytes(key));
            for (Map.Entry<byte[], byte[]> entry : bytesMap.entrySet()) {
                res.put(SerializeUtil.convertBytes2Obj(entry.getKey()), SerializeUtil.convertBytes2Obj(entry.getValue()));
            }
            return res;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key向list头部添加字符串</p>
     *
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long lpush(String key, String... strs) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.lpush(key, strs);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key向list尾部添加字符串</p>
     *
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key, String... strs) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.rpush(key, strs);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key在list指定的位置之前或者之后 添加字符串元素</p>
     *
     * @param key
     * @param where LIST_POSITION枚举类型
     * @param pivot list里面的value
     * @param value 添加的value
     * @return
     */
    public Long linsert(String key, LIST_POSITION where,
                        String pivot, String value) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.linsert(key, where, pivot, value);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key设置list指定下标位置的value</p>
     * <p>如果下标超过list里面value的个数则报错</p>
     *
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public String lset(String key, Long index, String value) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.lset(key, index, value);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key从对应的list中删除指定的count个 和 value相同的元素</p>
     *
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回被删除的个数
     */
    public Long lrem(String key, long count, String value) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.lrem(key, count, value);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key保留list中从strat下标开始到end下标结束的value值</p>
     *
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public String ltrim(String key, long start, long end) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.ltrim(key, start, end);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key从list的头部删除一个value,并返回该value</p>
     *
     * @param key
     * @return
     */
    public String lpop(String key) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.lpop(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key从list尾部删除一个value,并返回该元素</p>
     *
     * @param key
     * @return
     */
    public String rpop(String key) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.rpop(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value</p>
     * <p>如果第一个list为空或者不存在则返回null</p>
     *
     * @param srckey
     * @param dstkey
     * @return
     */
    public String rpoplpush(String srckey, String dstkey) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.rpoplpush(srckey, dstkey);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取list中指定下标位置的value</p>
     *
     * @param key
     * @param index
     * @return 如果没有返回null
     */
    public String lindex(String key, long index) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.lindex(key, index);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回list的长度</p>
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.llen(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取list指定下标位置的value</p>
     * <p>如果start 为 0 end 为 -1 则返回全部的list中的value</p>
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        boolean broken = false;
        List<String> res = null;
        try {
            jedis = getJedis();
            res = jedis.lrange(key, start, end);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key向指定的set中添加value</p>
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public Long sadd(String key, String... members) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.sadd(key, members);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key删除set中对应的value值</p>
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public Long srem(String key, String... members) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.srem(key, members);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key随机删除一个set中的value并返回该值</p>
     *
     * @param key
     * @return
     */
    public String spop(String key) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.spop(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取set中的差集</p>
     * <p>以第一个set为标准</p>
     *
     * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        Set<String> res = null;
        try {
            jedis = getJedis();
            res = jedis.sdiff(keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取set中的差集并存入到另一个key中</p>
     * <p>以第一个set为标准</p>
     *
     * @param dstkey 差集存入的key
     * @param keys   可以使一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.sdiffstore(dstkey, keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取指定set中的交集</p>
     *
     * @param keys 可以使一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sinter(String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        Set<String> res = null;
        try {
            jedis = getJedis();
            res = jedis.sinter(keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取指定set中的交集 并将结果存入新的set中</p>
     *
     * @param dstkey
     * @param keys   可以使一个string 也可以是一个string数组
     * @return
     */
    public Long sinterstore(String dstkey, String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.sinterstore(dstkey, keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回所有set的并集</p>
     *
     * @param keys 可以使一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sunion(String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        Set<String> res = null;
        try {
            jedis = getJedis();
            res = jedis.sunion(keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回所有set的并集,并存入到新的set中</p>
     *
     * @param dstkey
     * @param keys   可以使一个string 也可以是一个string数组
     * @return
     */
    public Long sunionstore(String dstkey, String... keys) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.sunionstore(dstkey, keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key将set中的value移除并添加到第二个set中</p>
     *
     * @param srckey 需要移除的
     * @param dstkey 添加的
     * @param member set中的value
     * @return
     */
    public Long smove(String srckey, String dstkey, String member) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.smove(srckey, dstkey, member);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取set中value的个数</p>
     *
     * @param key
     * @return
     */
    public Long scard(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.scard(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key判断value是否是set中的元素</p>
     *
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(String key, String member) {
        Jedis jedis = null;
        boolean broken = false;
        Boolean res = null;
        try {
            jedis = getJedis();
            res = jedis.sismember(key, member);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取set中随机的value,不删除元素</p>
     *
     * @param key
     * @return
     */
    public String srandmember(String key) {
        Jedis jedis = null;
        boolean broken = false;
        String res = null;
        try {
            jedis = getJedis();
            res = jedis.srandmember(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取set中所有的value</p>
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Set<String> res = null;
        try {
            jedis = getJedis();
            res = jedis.smembers(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key向zset中添加value,score,其中score就是用来排序的</p>
     * <p>如果该value已经存在则根据score更新元素</p>
     *
     * @param key
     * @param scoreMembers
     * @return
     */
    public Long zadd(String key, Map<Object, Double> scoreMembers) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            Map<byte[], Double> scoreByteMembers = new HashMap<>();
            for (Map.Entry<Object, Double> scoreMember : scoreMembers.entrySet()) {
                Object member = scoreMember.getKey();
                scoreByteMembers.put(convertObj2Bytes(member), scoreMember.getValue());
            }
            res = jedis.zadd(convertObj2Bytes(key), scoreByteMembers);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key向zset中添加value,score,其中score就是用来排序的</p>
     * <p>如果该value已经存在则根据score更新元素</p>
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(String key, double score, Object member) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zadd(convertObj2Bytes(key), score, convertObj2Bytes(member));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key删除在zset中指定的value</p>
     *
     * @param key
     * @param members 可以使一个string 也可以是一个string数组
     * @return
     */
    public Long zrem(String key, Object... members) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zrem(convertObj2Bytes(key), convertObjs2Bytes(members));
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key增加该zset中value的score的值</p>
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = null;
        boolean broken = false;
        Double res = null;
        try {
            jedis = getJedis();
            res = jedis.zincrby(key, score, member);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回zset中value的排名</p>
     * <p>下标从小到大排序</p>
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key, String member) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zrank(key, member);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回zset中value的排名</p>
     * <p>下标从大到小排序</p>
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zrevrank(key, member);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key将获取score从start到end中zset的value</p>
     * <p>socre从大到小排序</p>
     * <p>当start为0 end为-1时返回全部</p>
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zrange(String key, long start, long end) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            Set<byte[]> bytesSet = jedis.zrange(convertObj2Bytes(key), start, end);
            Set<Object> members = new HashSet<>();
            for (byte[] bytes : bytesSet) {
                members.add(SerializeUtil.convertBytes2Obj(bytes));
            }
            return members;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key将获取score从start到end中zset的value</p>
     * <p>socre从大到小排序</p>
     * <p>当start为0 end为-1时返回全部</p>
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            Set<byte[]> bytesSet = jedis.zrevrange(convertObj2Bytes(key), start, end);
            Set<Object> members = new HashSet<>();
            for (byte[] bytes : bytesSet) {
                members.add(SerializeUtil.convertBytes2Obj(bytes));
            }
            return members;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key正序返回指定score内zset中的member</p>
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> zrangeByScore(String key, Object min, Object max) {
        return zrangeByScore(key, min, max, 0, -1);
    }

    /**
     * <p>通过key正序返回指定score内zset中的member，可设置偏移量及要获取的数量</p>
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> zrangeByScore(String key, Object min, Object max, int offset, int count) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            Set<byte[]> bytesSet;
            if (count < 0) {
                bytesSet = jedis.zrangeByScore(convertObj2Bytes(key), convertObj2Bytes(min), convertObj2Bytes(max));
            } else {
                bytesSet = jedis.zrangeByScore(convertObj2Bytes(key), convertObj2Bytes(min), convertObj2Bytes(max), offset, count);
            }
            Set<Object> members = new HashSet<>();
            for (byte[] bytes : bytesSet) {
                members.add(SerializeUtil.convertBytes2Obj(bytes));
            }
            return members;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>通过key倒序返回指定score内zset中的value</p>
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<Object> zrevrangeByScore(String key, Object max, Object min) {
        return zrevrangeByScore(key, max, min, 0, -1);
    }

    /**
     * <p>通过key倒序返回指定score内zset中的value，可设置偏移量及要获取的数量</p>
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<Object> zrevrangeByScore(String key, Object max, Object min, int offset, int count) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            Set<byte[]> bytesSet;
            if (count < 0) {
                bytesSet = jedis.zrevrangeByScore(convertObj2Bytes(key), convertObj2Bytes(max), convertObj2Bytes(min));
            } else {
                bytesSet = jedis.zrevrangeByScore(convertObj2Bytes(key), convertObj2Bytes(max), convertObj2Bytes(min), offset, count);
            }
            Set<Object> members = new HashSet<>();
            for (byte[] bytes : bytesSet) {
                members.add(SerializeUtil.convertBytes2Obj(bytes));
            }
            return members;
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * <p>返回指定区间内zset中value的数量</p>
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key, String min, String max) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zcount(key, min, max);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key返回zset中的value个数</p>
     *
     * @param key
     * @return
     */
    public Long zcard(String key) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zcard(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key获取zset中value的score值</p>
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        boolean broken = false;
        Double res = null;
        try {
            jedis = getJedis();
            res = jedis.zscore(key, member);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key删除给定区间内的元素</p>
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zremrangeByRank(key, start, end);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key删除指定score内的元素</p>
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = null;
        boolean broken = false;
        Long res = null;
        try {
            jedis = getJedis();
            res = jedis.zremrangeByScore(key, start, end);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>返回满足pattern表达式的所有key</p>
     * <p>keys(*)</p>
     * <p>返回所有的key</p>
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        boolean broken = false;
        Set<String> res = null;
        try {
            jedis = getJedis();
            res = jedis.keys(pattern);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * <p>通过key判断值得类型</p>
     *
     * @param key
     * @return
     */
    public String type(String key) {
        Jedis jedis = null;
        String res = null;
        boolean broken = false;
        try {
            jedis = getJedis();
            res = jedis.type(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            closeResource(jedis, broken);
        }
        return res;
    }

    /**
     * Handle jedisException, write log and return whether the connection is broken.
     */
    public boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            logger.error("Redis connection lost.", jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                logger.error("Redis connection are read-only slave.", jedisException);
            } else {
                // dataException, isBroken=false
                return false;
            }
        } else {
            logger.error("Jedis exception happen.", jedisException);
        }
        return true;
    }

    /**
     * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
     */
    public void closeResource(Jedis jedis, boolean conectionBroken) {
        try {
            if (conectionBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            logger.error("return back jedis failed, will fore close the jedis.", e);
        }
    }

}
