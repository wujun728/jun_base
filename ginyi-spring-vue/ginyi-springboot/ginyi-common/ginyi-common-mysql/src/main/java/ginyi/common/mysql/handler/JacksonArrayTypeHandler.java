package ginyi.common.mysql.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JacksonArrayTypeHandler extends AbstractJsonTypeHandler<List> {

    private final Class<? extends Object> type;

    private final ObjectMapper objectMapper;

    public JacksonArrayTypeHandler(Class<?> type) {
        objectMapper = new ObjectMapper();
        this.type = type;
    }


    @SneakyThrows
    @Override
    protected List parse(String json) {
        if (type.equals(List.class)) {
            return objectMapper.readValue(json, List.class);
        } else {
            return JSON.parseArray(json, type);
        }
    }

    @SneakyThrows
    @Override
    protected String toJson(List obj) {
        return objectMapper.writeValueAsString(obj);
    }
}

