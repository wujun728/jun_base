package com.ruoyi.client.utils;

import com.ruoyi.client.model.AVueCrudModel;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonParseUtilsTest extends TestCase {

    public void testParse() {
    }

    @Test
    public void testToJson() {
        AVueCrudModel mock = Mockito.mock(AVueCrudModel.class);
        System.out.println(mock);
    }
}