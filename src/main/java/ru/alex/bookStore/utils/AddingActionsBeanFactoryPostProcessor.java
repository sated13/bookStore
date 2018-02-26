package ru.alex.bookStore.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import ru.alex.bookStore.Annotations.AfterExecutingRedirectTo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class AddingActionsBeanFactoryPostProcessor implements BeanPostProcessor {

    Map<String, Class> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = bean.getClass();
        Method[] methods = beanClass.getMethods();
        for (Method method: methods) {
            if (method.isAnnotationPresent(AfterExecutingRedirectTo.class)) {
                map.put(beanName, beanClass);
                return bean;
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = bean.getClass();
        System.out.println("beanClass: " + beanClass);
        if (null != beanClass) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Object returnValue = null;
                    System.out.println("method: " + method.getName());
                    if (method.isAnnotationPresent(AfterExecutingRedirectTo.class)) {
                        returnValue = method.invoke(bean, args);
                        System.out.println("returnValue: " + returnValue);
                        Method methodGetPage = bean.getClass().getMethod("getPage");
                        System.out.println("methodGetPage: " + methodGetPage);
                        Object returnValueFromGetPage = methodGetPage.invoke(bean);
                        Method methodSetLocation = returnValueFromGetPage.getClass().getMethod("setLocation");
                        methodSetLocation.invoke(returnValueFromGetPage, new Object[] {method.getAnnotation(AfterExecutingRedirectTo.class).pathToRedirect()});
                    }
                    return returnValue;
                }
            });
        }

        return bean;
    }
}
