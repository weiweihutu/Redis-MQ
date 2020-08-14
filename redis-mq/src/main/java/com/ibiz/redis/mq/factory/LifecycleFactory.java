package com.ibiz.redis.mq.factory;


import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.config.InstanceConfig;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.lifecycle.Lifecycle;
import com.ibiz.mq.common.util.ValidateUtil;

import java.util.Objects;

/**
 * @auther 喻场
 * @date 2020/7/2012:37
 */
public class LifecycleFactory {

    public static Lifecycle getNewInstance(InstanceConfig config) {
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(Lifecycle.class);
        Lifecycle lifecycle = (Lifecycle) extensionLoader.getNewInstance(config.getProtocol());
        ValidateUtil.validate(lifecycle, Objects::isNull, ErrorCode.COMMON_CODE, "not support lifecycle protocol:" + config.getProtocol());
        return lifecycle;
    }

}
