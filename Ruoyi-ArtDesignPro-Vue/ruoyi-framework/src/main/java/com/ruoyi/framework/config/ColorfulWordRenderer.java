package com.ruoyi.framework.config;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;
import com.google.code.kaptcha.text.WordRenderer;
import com.google.code.kaptcha.util.Configurable;

/**
 * Renders a compact, readable captcha with colorful characters and subtle lines.
 */
public class ColorfulWordRenderer extends Configurable implements WordRenderer
{
    private static final Color[] COLORS = {
        new Color(61, 126, 255),
        new Color(15, 190, 170),
        new Color(255, 170, 35),
        new Color(255, 111, 84),
        new Color(232, 72, 91)
    };

    private final Random random = new SecureRandom();

    @Override
    public BufferedImage renderWord(String word, int width, int height)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try
        {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            drawInterferenceLines(graphics, width, height);

            int fontSize = getConfig().getTextProducerFontSize();
            Font[] fonts = getConfig().getTextProducerFonts(fontSize);
            int step = width / (word.length() + 1);
            int baseline = (height + fontSize) / 2 - 3;

            for (int i = 0; i < word.length(); i++)
            {
                graphics.setColor(COLORS[i % COLORS.length]);
                graphics.setFont(fonts[random.nextInt(fonts.length)]);

                AffineTransform original = graphics.getTransform();
                int x = step * (i + 1) - fontSize / 3;
                graphics.rotate(Math.toRadians(random.nextInt(17) - 8), x + fontSize / 3.0, baseline - fontSize / 3.0);
                graphics.drawString(String.valueOf(word.charAt(i)), x, baseline + random.nextInt(5) - 2);
                graphics.setTransform(original);
            }
            return image;
        }
        finally
        {
            graphics.dispose();
        }
    }

    private void drawInterferenceLines(Graphics2D graphics, int width, int height)
    {
        graphics.setStroke(new BasicStroke(1.2F));
        for (int i = 0; i < 3; i++)
        {
            Color color = COLORS[random.nextInt(COLORS.length)];
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120));
            int y = 10 + random.nextInt(Math.max(height - 20, 1));
            graphics.drawLine(4, y, width - 4, y + random.nextInt(13) - 6);
        }
    }
}
