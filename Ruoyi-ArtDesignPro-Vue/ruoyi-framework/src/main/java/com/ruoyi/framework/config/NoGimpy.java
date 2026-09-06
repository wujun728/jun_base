package com.ruoyi.framework.config;

import java.awt.image.BufferedImage;
import com.google.code.kaptcha.GimpyEngine;

/**
 * Keeps captcha characters crisp after rendering.
 */
public class NoGimpy implements GimpyEngine
{
    @Override
    public BufferedImage getDistortedImage(BufferedImage baseImage)
    {
        return baseImage;
    }
}
