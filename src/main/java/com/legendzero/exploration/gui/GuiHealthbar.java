package com.legendzero.exploration.gui;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.entity.IPlayer;

import javax.vecmath.Color4f;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

public class GuiHealthbar extends GuiComponent {

    private final Collection<GuiHealthbarPartial> healthbarPartials;

    public GuiHealthbar(IPlayer player) {
        this(player, 10, 10, 100, 20, 10);
    }

    public GuiHealthbar(IPlayer player, double x, double y, double w, double h, double skew) {
        this(getRatioSupplier(player), () -> new Color4f(1f, 0f, 0f, 0f), () -> new Color4f(1f, 0f, 0f, 0f),
                x, y, w, h, skew);
    }

    public GuiHealthbar(Supplier<Double> ratioFunction, Supplier<Color4f> minColor, Supplier<Color4f> maxColor,
                        double x, double y, double w, double h, double skew) {
        this.healthbarPartials = new LinkedList<>();
        this.addPartial(new GuiHealthbarPartial(ratioFunction, minColor, maxColor, x, y, w, h, skew));
    }

    public void addPartial(GuiHealthbarPartial partial) {
        this.healthbarPartials.add(partial);
    }

    @Override
    public void render(IExploration game) {
        for (GuiHealthbarPartial healthbarPartial : this.healthbarPartials) {
            healthbarPartial.render(game);
        }
    }

    private static Supplier<Double> getRatioSupplier(final IPlayer player) {
        return () -> player.getHealth() / player.getMaxHealth();
    }
}
