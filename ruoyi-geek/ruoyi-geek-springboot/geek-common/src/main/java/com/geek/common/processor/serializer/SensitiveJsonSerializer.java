package com.geek.common.processor.serializer;

import java.util.Objects;

import com.geek.common.annotation.Sensitive;
import com.geek.common.core.domain.model.LoginUser;
import com.geek.common.utils.SecurityUtils;
import com.mybatisflex.core.mask.MaskManager;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * 数据脱敏序列化过滤
 *
 * @author geek
 */
public class SensitiveJsonSerializer extends ValueSerializer<String> {
    private String desensitizedType;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializationContext serializers) throws JacksonException {
        if (desensitization()) {
            gen.writeString(MaskManager.mask(desensitizedType,value).toString());
        } else {
            gen.writeString(value);
        }
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext prov, BeanProperty property) {
        Sensitive annotation = property.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.desensitizedType = annotation.desensitizedType();
            return this;
        }
        return prov.findValueSerializer(property.getType());
    }

    /**
     * 是否需要脱敏处理
     */
    private boolean desensitization() {
        try {
            LoginUser securityUser = SecurityUtils.getLoginUser();
            // 管理员不脱敏
            return !securityUser.getUser().isAdmin();
        } catch (Exception e) {
            return true;
        }
    }
}