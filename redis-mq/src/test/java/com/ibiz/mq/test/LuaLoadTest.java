package com.ibiz.mq.test;

import com.ibiz.redis.mq.constant.Constant;
import com.ibiz.redis.mq.script.ScriptManager;
import org.junit.Test;

/**
 * @auther 喻场
 * @date 2020/7/2217:56
 */
public class LuaLoadTest {

    @Test
    public void load() {
        String str = ScriptManager.getInstance().loadScript(Constant.LUA_PUBLISH);
        System.out.println(str);
    }
}
