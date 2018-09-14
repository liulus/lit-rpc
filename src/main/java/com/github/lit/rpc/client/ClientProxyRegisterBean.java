package com.github.lit.rpc.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-14 16:39
 */
@Slf4j
public class ClientProxyRegisterBean implements BeanDefinitionRegistryPostProcessor,
        ResourceLoaderAware, ApplicationContextAware {

    private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

    private ResourceLoader resourceLoader;

    private ApplicationContext applicationContext;

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("postProcessBeanDefinitionRegistry");
        Map<String, Object> rpcClientBeans = applicationContext.getBeansWithAnnotation(LitRpcClient.class);

        for (Object rpcClientBean : rpcClientBeans.values()) {
            Class<?> rpcClientClass = rpcClientBean.getClass();
            log.info("配置 {} 类上的 LitRpcClient 配置", rpcClientClass.getName());
            LitRpcClient rpcClient = AnnotationUtils.findAnnotation(rpcClientClass, LitRpcClient.class);
            String[] servicePackages = rpcClient.servicePackages();

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
                    registerClientProxy(clazz, registry, rpcClient.host(), rpcClient.port());
                }
            }

            // 注册指定接口注册服务
            Class[] serviceInterfaces = rpcClient.serviceInterfaces();
            for (Class serviceInterface : serviceInterfaces) {
                registerClientProxy(serviceInterface, registry, rpcClient.host(), rpcClient.port());
            }
        }

    }


    private void registerClientProxy(Class<?> interfaceClass, BeanDefinitionRegistry registry, String host, int port) {
        if (!interfaceClass.isInterface()) {
            return;
        }
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(ClientProxyFactoryBean.class)
                .addPropertyValue("interfaceClass", interfaceClass)
                .addPropertyValue("host", host)
                .addPropertyValue("port", port)
                .getBeanDefinition();
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }
}
