package com.legendzero.exploration.gui;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.entity.IPlayer;

import javax.vecmath.Color4f;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.*;

public class GuiHealthbarPartial extends GuiComponent {

    private final Supplier<Double> ratioFunction;
    private final Supplier<Color4f> minColor;
    private final Supplier<Color4f> maxColor;
    private final double minRatio;
    private final double maxRatio;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final double skew;

    public GuiHealthbarPartial(IPlayer player) {
        this(player, 10, 10, 100, 20, 10);
    }

    public GuiHealthbarPartial(IPlayer player, double x, double y, double width, double height, double skew) {
        this(player, 0.0, 1.0, x, y, width, height, skew);
    }

    public GuiHealthbarPartial(IPlayer player, double minRatio, double maxRatio,
                               double x, double y, double width, double height, double skew) {
        this(getRatioSupplier(player), () -> new Color4f(1f, 0f, 0f, 0f), () -> new Color4f(1f, 0f, 0f, 0f),
                minRatio, maxRatio, x, y, width, height, skew);
    }


    public GuiHealthbarPartial(Supplier<Double> ratioFunction, Supplier<Color4f> minColor, Supplier<Color4f> maxColor,
                               double x, double y, double width, double height, double skew) {
        this(ratioFunction, minColor, maxColor,
                0.0, 1.0, x, y, width, height, skew);
    }

    public GuiHealthbarPartial(Supplier<Double> ratioFunction, Supplier<Color4f> minColor, Supplier<Color4f> maxColor,
                               double minRatio, double maxRatio, double x, double y,
                               double width, double height, double skew) {
        this.ratioFunction = ratioFunction;
        this.minColor = minColor;
        this.maxColor = maxColor;
        this.minRatio = minRatio;
        this.maxRatio = maxRatio;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.skew = skew;
    }

    @Override
    public void render(IExploration game) {
        double ratio = (this.ratioFunction.get() - this.minRatio) / (this.maxRatio - this.minRatio);
        if (ratio < 0.0) {
            ratio = 0.0;
        } else if (ratio > 1.0) {
            ratio = 1.0;
        }
        Color4f color = this.minColor.get();
        color.interpolate(this.maxColor.get(), (float) ratio);
        glPushMatrix();
        glLoadIdentity();

        glTranslated(this.x, this.y, 0);

        glBegin(GL_QUADS);
        {
            glColor4f(color.x, color.y, color.z, color.w);
            glVertex2d(this.skew, 0);
            glVertex2d(ratio * this.width + this.skew, 0);
            glVertex2d(ratio * this.width, this.height);
            glVertex2d(0, this.height);

            glColor4f(0f, 0f, 0f, 1f);
            glVertex2d(ratio * this.width + this.skew, 0);
            glVertex2d(this.width + this.skew, 0);
            glVertex2d(this.width, this.height);
            glVertex2d(ratio * this.width, this.height);
        }
        glEnd();
        glColor4f(1f, 1f, 1f, 1f);
        glBegin(GL_LINE_LOOP);
        {
            glVertex2d(this.skew, 0);
            glVertex2d(this.width + this.skew, 0);
            glVertex2d(this.width, this.height);
            glVertex2d(0, this.height);
        }
        glEnd();

        glPopMatrix();
    }

    private static Supplier<Double> getRatioSupplier(final IPlayer player) {
        return () -> player.getHealth() / player.getMaxHealth();
    }
}
