package com.ruoyi.ehcache.serializer;

import java.nio.ByteBuffer;

import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.ruoyi.common.constant.Constants;

/**
 * Ehcache 使用 FastJson2 序列化
 * 
 * @author ruoyi
 */
public class FastJson2JsonEhcacheSerializer implements Serializer<Object> {

    private static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(Constants.JSON_WHITELIST_STR);

    public FastJson2JsonEhcacheSerializer(ClassLoader classLoader) {
        // Ehcache 要求构造函数接收 ClassLoader 参数
    }

    @Override
    public ByteBuffer serialize(Object object) throws SerializerException {
        if (object == null) {
            return ByteBuffer.wrap(new byte[0]);
        }
        try {
            byte[] bytes = JSON.toJSONBytes(object, JSONWriter.Feature.WriteClassName);
            return ByteBuffer.wrap(bytes);
        } catch (Exception e) {
            throw new SerializerException("Failed to serialize object", e);
        }
    }

    @Override
    public Object read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        if (binary == null || binary.remaining() == 0) {
            return null;
        }
        try {
            byte[] bytes = new byte[binary.remaining()];
            binary.get(bytes);
            return JSON.parseObject(bytes, Object.class, AUTO_TYPE_FILTER);
        } catch (Exception e) {
            throw new SerializerException("Failed to deserialize object", e);
        }
    }

    @Override
    public boolean equals(Object object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        Object deserialized = read(binary == null ? null : binary.duplicate());
        if (object == null) {
            return deserialized == null;
        }
        return object.equals(deserialized);
    }
}
