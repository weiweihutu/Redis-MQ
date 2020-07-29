package com.ibiz.redis.mq.script;

import com.google.common.io.CharStreams;
import com.ibiz.redis.mq.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import redis.clients.jedis.Jedis;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis lua脚本管理
 * @auther yc
 * @date 2020/7/2212:56
 */
public class ScriptManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**脚本缓存*/
    public static final Map<String, String> SCRIPT_CACHE = new ConcurrentHashMap<>();
    /**脚本生成的sha*/
    public static final Map<String, byte[]> SHA_CACHE = new ConcurrentHashMap<>();
    private Lock lock = new ReentrantLock();
    public static ScriptManager getInstance() {
        return ScriptManagerHolder.SCRIPT_MANAGER;
    }
    public static class ScriptManagerHolder {
        static ScriptManager SCRIPT_MANAGER = new ScriptManager();
    }

    public byte[] loadSha(Jedis jedis, String scriptName) {
        byte[] sha = SHA_CACHE.get(scriptName);
        if (null == sha) {
            lock.lock();
            try {
                sha = SHA_CACHE.get(scriptName);
                if (null == sha) {
                    //加载脚本
                    String script = loadScript(scriptName);
                    byte[] bytes = jedis.scriptLoad(script.getBytes(StandardCharsets.UTF_8));
                    SHA_CACHE.put(scriptName, bytes);
                }
            } catch (Exception e) {
                logger.error("load sha script error scriptName :{}", scriptName);
                throw new RuntimeException("load sha script error scriptName" + scriptName);
            } finally {
                lock.unlock();
            }
        }
        return SHA_CACHE.get(scriptName);
    }

    public String loadScript(String scriptName) {
        String script = SCRIPT_CACHE.get(scriptName);
        if (null == script || "".equals(script.trim())) {
            ClassPathResource cpr = new ClassPathResource(Constant.LUA_SCRIPT_ROOT_PATH + scriptName);
            //重新加载
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(cpr.getInputStream(), StandardCharsets.UTF_8));
                script = CharStreams.toString(reader);
                SCRIPT_CACHE.put(scriptName, script);
            } catch (IOException e) {
                logger.error("load script " + Constant.LUA_SCRIPT_ROOT_PATH + scriptName + " error");
                throw new RuntimeException("load script " + Constant.LUA_SCRIPT_ROOT_PATH + scriptName + " error");
            }
        }
        return SCRIPT_CACHE.get(scriptName);
    }
}
