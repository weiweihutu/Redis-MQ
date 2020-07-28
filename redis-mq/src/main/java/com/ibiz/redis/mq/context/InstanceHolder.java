package com.ibiz.redis.mq.context;

import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.lifecycle.Lifecycle;
import com.ibiz.mq.common.util.ValidateUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther yc
 * @date 2020/7/2019:56
 */
public class InstanceHolder {
    private Map<String, Lifecycle> INSTANCE_LIFECYCLE = new ConcurrentHashMap<>(16);
    private Set<String> INSTANCE_IDS = new HashSet<>();

    public Lifecycle getLifecycle(String instanceId) {
        return INSTANCE_LIFECYCLE.get(instanceId);
    }
    public void registry(String instanceId, Lifecycle lifecycle) {
        ValidateUtil.validate(instanceId, (o) -> !INSTANCE_IDS.add(instanceId), ErrorCode.COMMON_CODE, "repeat instanceId:" + instanceId);
        INSTANCE_LIFECYCLE.putIfAbsent(instanceId, lifecycle);
    }

    public static InstanceHolder getInstanceHolder() {
        return LifecycleHolder.LIFECYCLE_HOLDER;
    }

    /**
     * 销毁已启动BOSS线程,停止WORK线程。清理BOSS线程和WORK线程池
     */
    public void deploy() {
        INSTANCE_LIFECYCLE.values().stream().forEach(Lifecycle::deploy);
    }

    private static class LifecycleHolder {
        private static InstanceHolder LIFECYCLE_HOLDER = new InstanceHolder();
    }
    private InstanceHolder(){}
}
