package com.github.lit.rpc.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 18:03
 */
@Slf4j
public class LitRpcServiceRegister implements ApplicationContextAware {

    private LitRpcServiceExporter litRpcServiceExpoter = new LitRpcServiceExporter();

    @Override
    @SneakyThrows
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> rpcServerBeans = applicationContext.getBeansWithAnnotation(LitRpcServer.class);

        if (rpcServerBeans.size() > 1) {
            log.info("LitRpcServer 配置大于 1 个, 将随机选取一个");
        }
        Object rpcServerBean = rpcServerBeans.values().iterator().next();
        Class<?> rpcServerClass = rpcServerBean.getClass();
        log.info("选取 {} 类上的 LitRpcServer 配置", rpcServerClass.getName());
        LitRpcServer rpcServer = AnnotationUtils.findAnnotation(rpcServerClass, LitRpcServer.class);
        String[] servicePackages = rpcServer.servicePackages();

        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resourceLoader);
        // 根据包注册服务
        for (String servicePackage : servicePackages) {
            String resourcePath = ClassUtils.convertClassNameToResourcePath(servicePackage);
            Resource[] resources = patternResolver.getResources("classpath*:" + resourcePath + "/**/*.class");
            for (Resource resource : resources) {

                MetadataReader reader = readerFactory.getMetadataReader(resource);
                if (!reader.getClassMetadata().isInterface()) {
                    continue;
                }
                Class<?> clazz = ClassUtils.forName(reader.getClassMetadata().getClassName(), patternResolver.getClassLoader());
                register(clazz, applicationContext);
            }
        }

        // 注册指定接口注册服务
        Class[] serviceInterfaces = rpcServer.serviceInterfaces();
        for (Class serviceInterface : serviceInterfaces) {
            register(serviceInterface, applicationContext);
        }

        // 启动netty, 暴露服务接口
        litRpcServiceExpoter.startServer(rpcServer.port());
    }


    private void register(Class<?> interfaceClass, ApplicationContext applicationContext) {
        if (!interfaceClass.isInterface()) {
            return;
        }

        Map<String, ?> beans = applicationContext.getBeansOfType(interfaceClass);
        if (CollectionUtils.isEmpty(beans) || beans.size() > 1) {
            log.warn("接口 {} 的实现类大于 1 个, 将不注册此接口", interfaceClass.getName());
            return;
        }
        log.info("注册服务接口: {}", interfaceClass.getName());
        LitRpcContext.put(interfaceClass, beans.values().iterator().next());
    }


}