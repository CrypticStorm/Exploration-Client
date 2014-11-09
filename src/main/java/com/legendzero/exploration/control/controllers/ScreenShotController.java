/*
 * Copyright (C) 2013 Legend Zero
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.legendzero.exploration.control.controllers;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.control.AbstractController;
import com.legendzero.exploration.entity.Player;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author CrypticStorm
 */
public class ScreenShotController extends AbstractController {

    private final DateFormat format;
    private final File folder;
    private long lastScreenshot = 0L;

    public ScreenShotController(Player player) {
        super(player);
        this.format = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss'.png'");
        this.folder = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "screenshots");
        if (!folder.exists()) {
            if(!folder.mkdir()) {
                System.err.println("Error creating screenshot directory: " + folder);
            }
        }
    }

    @Override
    public void update(IExploration game) {
        if (Keyboard.isKeyDown(Keyboard.KEY_F2) && System.currentTimeMillis() - this.lastScreenshot >= 1000) {
            this.lastScreenshot = System.currentTimeMillis();
            GL11.glReadBuffer(GL11.GL_FRONT);
            int width = Display.getWidth();
            int height = Display.getHeight();
            int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
            GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            File file = new File(folder, this.format.format(new Date()));
            System.out.println(file.getAbsolutePath());
            String type = "PNG";
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int i = (x + (width * y)) * bpp;
                    int r = buffer.get(i) & 0xFF;
                    int g = buffer.get(i + 1) & 0xFF;
                    int b = buffer.get(i + 2) & 0xFF;
                    int a = buffer.get(i + 3) & 0xFF;
                    image.setRGB(x, height - (y + 1), (a << 24) | (r << 16) | (g << 8) | b);
                }
            }

            try {
                if(file.createNewFile()) {
                    ImageIO.write(image, type, file);
                } else {
                    System.err.println("File already exists! Ignoring screenshot.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
