package com.geek.common.processor.serializer;

import java.util.Objects;

import com.geek.common.annotation.FilePath;
import com.geek.common.config.GeekConfig;
import com.geek.common.core.storage.GeekStorageBucket;
import com.geek.common.core.storage.service.StorageService;
import com.geek.common.utils.StringUtils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class FilePathJsonSerializer extends ValueSerializer<String> {

    StorageService storageService;

    @Override
    public ValueSerializer<?> createContextual(SerializationContext prov, BeanProperty property) {
        FilePath annotation = property.getAnnotation(FilePath.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            String storageName = annotation.value();
            GeekStorageBucket geekStorageBucket = GeekConfig.getGeekStorageBucket();
            if (StringUtils.isNotEmpty(storageName)) {
                this.storageService = new StorageService(geekStorageBucket.getStorageBucket(storageName));
            } else {
                this.storageService = new StorageService(geekStorageBucket);
            }
            return this;
        }
        return prov.findValueSerializer(property.getType());
    }

    @Override
    public void serialize(String arg0, JsonGenerator arg1, SerializationContext arg2)
            throws JacksonException {
        if (storageService != null) {
            String url;
            try {
                url = storageService.generateUrl(arg0);
                arg1.writeString(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            arg1.writeString(arg0);
        }
    }

}
