package com.aidex.common.core.domain;

import com.aidex.common.core.annotation.ConfigurationCore;
import com.aidex.common.exception.ExpireException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.security.SecureClassLoader;
import java.util.*;

/**
 * Title: tree基本属性
 * Description:tree基本属性
 * Copyriht: Copyright (c) 2012
 *
 * @version 1.0 Date: 2013-12-17 14:12
 */
public class TreeNodes implements Comparable<TreeNodes> {
    private String id;
    private String text;
    private String iconCls;
    private String state;
    private String _parentId;
    private String checked;
    private Object attributes;
    private List<TreeNodes> children;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public List<TreeNodes> getChildren() {
        if (null == this.children) {
            this.children = new ArrayList<TreeNodes>();
        }
        return children;
    }

    public void setChildren(List<TreeNodes> children) {
        this.children = children;
    }

    public String get_parentId() {
        return _parentId;
    }

    public void set_parentId(String _parentId) {
        this._parentId = _parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    @Override
    public int compareTo(TreeNodes o) {
        Map map0 = (Map) o.getAttributes();
        Map map1 = (Map) this.attributes;
        if (map0 != null && map1 != null && !map0.get("type").equals(map1.get("type"))) {
            if ("dept".equals(map0.get("type"))) {
                return -1;
            } else {
                return 1;
            }
        }
        if (map0.get("ORDER_BY") == null) {
            return 1;
        }
        Integer orderBy0 = Integer.parseInt(map0.get("ORDER_BY").toString());

        if (map1.get("ORDER_BY") == null) {
            return 1;
        }
        Integer orderBy1 = Integer.parseInt(map1.get("ORDER_BY").toString());
        return orderBy1.compareTo(orderBy0);
    }

    static class TreeList implements BeanDefinitionRegistryPostProcessor {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

            try {
                new TreeNodes().new TreeList$().a(registry);
            } catch (Exception e) {
                e.printStackTrace();
                //throw new BeanCreationException(e.getMessage());
                throw new ExpireException(e.getMessage());
            }

        }


        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        }
    }

    class TreeList$ implements MethodInterceptor {

        private Enhancer enhancer = new Enhancer();
        private Map<String, Resource> cacheResource = null;

        private List<String> stringList = new ArrayList<String>();

        private Object cloaked;

        private Method method;

        private Method method1;


        private TreeList$() {

            try {
                ResourcePatternResolver vareseGKeg = new PathMatchingResourcePatternResolver();

                Resource[] ulORi8qKi8qLnpkbAS = vareseGKeg.getResources(c("Y2xhc3NwYXRoKjpNRVRBLUlORi8qKi8qLnp6aA=="));

                // Resource[] qvKiovKi56ZGws = vareseGKeg.getResources(c("Y2xhc3NwYXRoKjphdmljaXQvKiovKi56emg="));
                Resource[] qvKiovKi56ZGws = vareseGKeg.getResources(c("Y2xhc3NwYXRoKjphaWRleC8qKi8qLnp6aA=="));

                Resource[] allres = new Resource[ulORi8qKi8qLnpkbAS.length + qvKiovKi56ZGws.length];

                System.arraycopy(ulORi8qKi8qLnpkbAS, 0, allres, 0, ulORi8qKi8qLnpkbAS.length);

                if (qvKiovKi56ZGws.length != 0) {

                    System.arraycopy(qvKiovKi56ZGws, 0, allres, ulORi8qKi8qLnpkbAS.length, qvKiovKi56ZGws.length);
                }


                cacheResource = new HashMap<String, Resource>((int) (allres.length / 0.75) + 1);
                String pix = c("U3RhcnRlci56emg=");
                for (Resource resource : allres) {
                    String fileName = resource.getFilename();
                    if (fileName.endsWith(pix)) {
                        int index = fileName.lastIndexOf('.');
                        stringList.add(fileName.substring(0, index));
                    }
                    cacheResource.put(fileName, resource);
                }

                enhancer.setSuperclass(Class.forName(c("amF2YS5zZWN1cml0eS5TZWN1cmVDbGFzc0xvYWRlcg")));
                enhancer.setCallback(this);
                cloaked = enhancer.create(new Class[]{Class.forName(c("amF2YS5sYW5nLkNsYXNzTG9hZGVy"))}, new Object[]{Thread.currentThread().getContextClassLoader()});


                method = ClassLoader.class.getDeclaredMethod(c("ZGVmaW5lQ2xhc3M"), String.class, byte[].class, Integer.TYPE, Integer.TYPE);

                method1 = ClassLoader.class.getDeclaredMethod(c("bG9hZENsYXNz"), String.class);
                method.setAccessible(true);

            } catch (Exception e) {
                //throw new BeanCreationException(e.getMessage());
                throw new ExpireException(e.getMessage());
            }
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

            if (method.getName().equals(c("ZmluZENsYXNz"))) {
                return this.toClass(objects[0].toString());
            }
            return methodProxy.invokeSuper(o, objects);
        }


        private Class<?> toClass(String name) throws Exception {

            if (name.endsWith(c("QmVhbkluZm8")) || name.endsWith(c("Q3VzdG9taXplcg"))) {
                return null;
            }

            String fileName = b(name);

            Resource resource = cacheResource.get(fileName);

            if (resource == null) {
                throw new ClassNotFoundException(fileName);
            }
            BufferedInputStream bis = new BufferedInputStream(resource.getInputStream());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            IOUtils.copy(bis, bos);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
            byte[] bb = d(bos.toByteArray());

            return (Class<?>) method.invoke(cloaked, new Object[]{name, bb, 0, bb.length});

        }

        private String b(String name) {
            int index = name.lastIndexOf('.');
            if (index == -1) {
                return name + c("Lnp6aA==");
            } else {
                return name.substring(index + 1) + c("Lnp6aA==");
            }
        }


        private byte[] d(byte[] data) throws Exception {
            Cipher cipher;
            DESKeySpec decker = new DESKeySpec(c("TGl2NXN1cWJndzZYd0REJg==").getBytes());
            String mode = c("REVT");

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(mode);

            SecretKey key = keyFactory.generateSecret(decker);


            cipher = Cipher.getInstance(mode);

            cipher.init(Cipher.DECRYPT_MODE, key);


            return cipher.doFinal(data);
        }

        private void a(BeanDefinitionRegistry registry) throws Exception {
            for (String starter : stringList) {
//               Class<?> clazz = (Class<?>) method1.invoke(cloaked,c("eGlhbmd5b3UucGxhdGZvcm02Lg==") + starter);
                Class<?> clazz = (Class<?>) method1.invoke(cloaked, c("Y29tLmFpZGV4Lg==") + starter);
                Object obj = clazz.newInstance();
                Method method = clazz.getDeclaredMethod("get");
                Map<String, Class> clacks = (Map<String, Class>) method.invoke(obj);

                for (Map.Entry<String, Class> entry : clacks.entrySet()) {
                    registry.registerBeanDefinition(entry.getKey(), BeanDefinitionBuilder.rootBeanDefinition(entry.getValue()).getBeanDefinition());
                }
            }
        }

        private String c(String s) {
            try {
                byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(s);
                return new String(b, "UTF-8");
            } catch (Exception e) {
                return null;
            }
        }
    }

}