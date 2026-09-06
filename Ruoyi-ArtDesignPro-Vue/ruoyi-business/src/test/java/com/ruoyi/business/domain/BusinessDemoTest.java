package com.ruoyi.business.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BusinessDemoTest
{
    @Test
    void shouldSetBusinessDemoFields()
    {
        BusinessDemo demo = new BusinessDemo();

        demo.setDemoId(1L);
        demo.setDemoName("测试业务");
        demo.setStatus("0");

        assertEquals(1L, demo.getDemoId());
        assertEquals("测试业务", demo.getDemoName());
        assertEquals("0", demo.getStatus());
    }
}
