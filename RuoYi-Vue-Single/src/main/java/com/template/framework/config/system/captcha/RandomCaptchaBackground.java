package com.template.framework.config.system.captcha;

import com.google.code.kaptcha.BackgroundProducer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomCaptchaBackground implements BackgroundProducer {
    private static final Color[][] GRADIENTS = {
            {new Color(232, 240, 253), new Color(187, 222, 251)}, // 浅蓝到中蓝
            {new Color(240, 249, 235), new Color(203, 235, 205)}, // 浅绿到薄荷绿
            {new Color(253, 236, 234), new Color(251, 209, 212)}, // 浅粉到玫瑰粉
            {new Color(255, 255, 204), new Color(153, 204, 255)}, // 亮黄到天蓝
            {new Color(204, 255, 204), new Color(255, 153, 204)}, // 荧光绿到粉红
            {new Color(255, 229, 204), new Color(204, 204, 255)},  // 蜜桃到薰衣草
            {new Color(245, 245, 245), new Color(224, 224, 224)}, // 白到浅灰
            {new Color(250, 250, 255), new Color(220, 230, 255)}, // 白到淡蓝灰
            {new Color(255, 250, 245), new Color(255, 235, 220)},  // 白到浅杏
            {new Color(230, 246, 238), new Color(173, 216, 197)}, // 薄荷到海沫绿
            {new Color(253, 245, 230), new Color(250, 215, 160)}, // 亚麻到沙色
            {new Color(232, 237, 250), new Color(180, 200, 235)},  // 淡蓝到勿忘我蓝
            {new Color(255, 243, 224), new Color(255, 200, 150)}, // 乳白到珊瑚
            {new Color(224, 255, 240), new Color(100, 255, 200)}, // 荧光薄荷绿
            {new Color(240, 224, 255), new Color(200, 180, 255)}  // 淡紫到紫罗兰
    };

    @Override
    public BufferedImage addBackground(BufferedImage baseImage) {
        int width = baseImage.getWidth();
        int height = baseImage.getHeight();

        BufferedImage imageWithBackground = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imageWithBackground.createGraphics();

        // 绘制随机渐变背景
        Random rand = new Random();
        Color[] gradient = GRADIENTS[rand.nextInt(GRADIENTS.length)];
        GradientPaint paint = new GradientPaint(0, 0, gradient[0], width, height, gradient[1]);
        g2d.setPaint(paint);
        g2d.fillRect(0, 0, width, height);

        // 叠加原始验证码图像
        g2d.drawImage(baseImage, 0, 0, null);
        g2d.dispose();

        return imageWithBackground;
    }
}