package com.ruoyi.framework.web.domain.server;

import com.ruoyi.common.utils.Arith;
import lombok.Data;

/**
 * 內存相关信息
 * 
 * @author ruoyi
 */
@Data
public class Mem
{
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;


    public double getUsage()
    {
        return Arith.mul(Arith.div(used, total, 4), 100);
    }
}
