package com.ibiz.mq.common;

import com.ibiz.mq.common.constant.Constant;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.util.ClassUtil;
import com.ibiz.mq.common.util.RuntimeError;
import com.ibiz.mq.common.util.StringUtil;
import com.ibiz.mq.common.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @auther yc
 * @date 2020/7/1619:03
 */
public class ExtensionLoader<T> {
    private static Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
    private Class<T> type;
    public static final ConcurrentMap<Class<?>, ExtensionLoader<?>> SERVICE_LOADER = new ConcurrentHashMap<>(64);
    public final ConcurrentMap<String, ServiceHolder<?>> INSTANCE_HOLDER = new ConcurrentHashMap<>(64);
    public final ServiceHolder<Map<String, Optional<Class<?>>>> CLAZZ_HOLDER = new ServiceHolder<>();

    private ExtensionLoader(Class<T> type) { this.type = type; }

    /**
     * 采用多例,获取一个新实例
     * @param instanceName 实例名称
     * @return
     */
    public T getNewInstance(String instanceName) {
        ServiceHolder<?> serviceHolder = createHolder(instanceName);
        return (T)serviceHolder.get().get();
    }

    /**
     * 单例模式
     * 会在INSTANCE_HOLDER中缓存实例
     * @param instanceName 实例名称
     * @return
     */
    public T getInstance(String instanceName) {
        ValidateUtil.validate(instanceName, StringUtil::isBlank, ErrorCode.COMMON_CODE, "instanceName is null");
        return getInstance(instanceName, null);
    }

    public T getInstance(String instanceName, String defaultName) {
        instanceName = null != instanceName && !"".equals(instanceName.trim()) ? instanceName : defaultName;
        ValidateUtil.validate(instanceName, StringUtil::isBlank, ErrorCode.COMMON_CODE, "all of instanceName and defaultName are null");
        ServiceHolder<?> serviceHolder = INSTANCE_HOLDER.get(instanceName);
        if (Objects.isNull(serviceHolder)) {
            synchronized (INSTANCE_HOLDER) {
                serviceHolder = INSTANCE_HOLDER.get(instanceName);
                if (Objects.isNull(serviceHolder)) {
                    serviceHolder = createHolder(instanceName);
                    INSTANCE_HOLDER.put(instanceName, serviceHolder);
                }
            }
        }
        return (T)serviceHolder.get().get();
    }

    private ServiceHolder<?> createHolder(String instanceName) {
        ServiceHolder serviceHolder = new ServiceHolder();
        Optional<Map<String, Optional<Class<?>>>> classCache = CLAZZ_HOLDER.get();
        if (!classCache.isPresent()) {
            synchronized (CLAZZ_HOLDER) {
                classCache = CLAZZ_HOLDER.get();
                if (!classCache.isPresent()) {
                    loadInstance(instanceName, serviceHolder);
                }
            }
        } else {
            Optional<Class<?>> clazz = classCache.get().getOrDefault(instanceName, Optional.empty());
            if (!clazz.isPresent()) {
                synchronized (CLAZZ_HOLDER) {
                    clazz = CLAZZ_HOLDER.get().get().get(instanceName);
                    if (Objects.isNull(clazz)) {
                        loadInstance(instanceName, serviceHolder);
                    }
                }
                return serviceHolder;
            }
            serviceHolder.set((T) ClassUtil.getNewInstance(clazz.get()));
        }
        return serviceHolder;
    }

    private void loadInstance(String instanceName, ServiceHolder serviceHolder) {
        loadClass();
        Optional<Map<String, Optional<Class<?>>>> classCache = CLAZZ_HOLDER.get();
        Optional<Class<?>> clazz = classCache.get().getOrDefault(instanceName, Optional.empty());
        ValidateUtil.validate(clazz, (o) -> !o.isPresent(), ErrorCode.COMMON_CODE, "load instance not exist class:" + type.getName() + " , instanceName:" + instanceName);
        serviceHolder.set((T)ClassUtil.getNewInstance(clazz.get()));
    }

    private void loadClass() {
        Map<String, Optional<Class<?>>> classCache = new HashMap<>();
        try {
            ClassPathResource cpr = new ClassPathResource(Constant.SPI_CONFIG_ROOT_PATH + type.getName());
            classCache = new HashMap<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(cpr.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                int anIdx;
                line = (anIdx = line.indexOf(Constant.ANNOTATION_FLAG)) == 0 ? "" :
                        anIdx <= 0 ? line.trim() :
                                line.substring(0, anIdx);
                String[] nameToClass = line.split("=");
                if ("".equals(line)) {
                    continue;
                }
                ValidateUtil.validate(nameToClass, (o) -> o.length != 2, ErrorCode.COMMON_CODE, "加载 type:" + type.getName() + " 异常, 配置项错误：" + line);
                boolean existName = classCache.containsKey(nameToClass[0].trim());
                ValidateUtil.validate(existName, (o) -> o, ErrorCode.COMMON_CODE, "type:" + type.getName() + " name：" + nameToClass[0] + " repeat ...");
                Class<?> clazz = ClassUtil.getClass(nameToClass[1].trim());
                classCache.put(nameToClass[0].trim(), Optional.ofNullable(clazz));
            }
        } catch (Exception e) {
            RuntimeError.creator("loadClass " + Constant.SPI_CONFIG_ROOT_PATH + type.getName() + " error", e);
        }
        logger.info("load class :{} instance class :{}", type.getName(), classCache.values().stream().filter(Optional::isPresent).map(o -> o.get().getName()).collect(Collectors.joining(",")));
        CLAZZ_HOLDER.set(classCache);
    }

    public static <T> ExtensionLoader getServiceLoader(Class<T> type) {
        ExtensionLoader<?> loader = SERVICE_LOADER.getOrDefault(type, new ExtensionLoader<>(type));
        SERVICE_LOADER.putIfAbsent(type, loader);
        return loader;
    }

    static class ServiceHolder<T> {
        T value;
        Optional<T> get() {return Optional.ofNullable(value);}
        void set(T value) {this.value = value;}
    }

}
