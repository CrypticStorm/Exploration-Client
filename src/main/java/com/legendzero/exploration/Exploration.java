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
package com.legendzero.exploration;

import com.legendzero.exploration.api.IExploration;
import com.legendzero.exploration.api.element.ElementManager;
import com.legendzero.exploration.api.element.IElementManager;
import com.legendzero.exploration.api.element.loader.ElementLoader;
import com.legendzero.exploration.api.entity.IPlayer;
import com.legendzero.exploration.api.font.ITrueTypeFont;
import com.legendzero.exploration.api.world.IWorld;
import com.legendzero.exploration.entity.Player;
import com.legendzero.exploration.font.TrueTypeFont;
import com.legendzero.exploration.materials.DefaultMaterials;
import com.legendzero.exploration.render.RenderState;
import com.legendzero.exploration.util.AABB;
import com.legendzero.exploration.world.World;
import com.legendzero.exploration.world.WorldManager;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.vecmath.Vector2d;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author CrypticStorm
 */
public class Exploration implements IExploration {

    private final int viewSize;

    private boolean isRunning;
    private Player player;
    private final File baseDirectory;
    private final IElementManager elementManager;
    private final WorldManager worldManager;
    private final List<IWorld> worlds;
    private RenderState state;
    private ITrueTypeFont font;

    private Exploration() {
        this.isRunning = true;
        this.viewSize = 250;
        this.baseDirectory = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        this.state = RenderState.GAME;

        DefaultMaterials.init();

        this.elementManager = new ElementManager(this);
        this.elementManager.registerLoader(ElementLoader.class);
        this.elementManager.loadElements(new File(this.baseDirectory, "elements"));
        this.elementManager.enableElements();

        this.worldManager = new WorldManager(new File(this.baseDirectory, "worlds"));
        this.worlds = this.worldManager.loadWorlds();

        this.initDisplay();
        this.initGame();
        this.initControls();
    }

    public IPlayer getPlayer() {
        return this.player;
    }

    public IElementManager getElementManager() {
        return this.elementManager;
    }

    public AABB getVisibleAABB() {
        return this.state.getAABB();
    }

    public List<IWorld> getWorlds() {
        return this.worlds;
    }

    public void addWorld(IWorld world) {
        this.worlds.add(world);
    }

    public int getViewSize() {
        return this.viewSize;
    }

    public ITrueTypeFont getFont() {
        return this.font;
    }

    public void stop() {
        this.isRunning = false;
    }

    private void initDisplay() {
        Display.setTitle("Exploration");
        try {
            Display.setDisplayMode(new DisplayMode(1920, 1080));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        font = new TrueTypeFont(new Font("Times New Roman", Font.PLAIN, 24), false);
    }

    private void initGame() {
        this.player = new Player("CrypticStorm", true);
        this.player.setFlying(true);
        IWorld world;
        if (this.worlds.isEmpty()) {
            world = new World("Main", 100, 100, new Vector2d(0.0, -0.1), new Vector2d(0.5, 0.5));
            this.worlds.add(world);
        } else {
            world = this.worlds.get(0);
        }
        world.spawnEntity(player, world.getSpawnLocation().copy().add(0.5, 0));
    }

    private void initControls() {

    }

    public void runGame() {
        while (!Display.isCloseRequested() && this.isRunning) {
            for (IWorld world : this.worlds) {
                world.update(this);
            }
            state.render(this);
            Display.update();
            Display.sync(60);
        }
    }

    public void endGame() {
        Display.destroy();

        this.worldManager.saveWorlds(this.worlds);
    }

    public static void main(String[] args) {
        if (!loadNatives()) {
            System.exit(0);
        }
        Exploration game = new Exploration();
        game.runGame();
        game.endGame();
    }

    private static boolean loadNatives() {
        // Get .jar dir. new File(".") and property "user.dir" will not work if .jar is called from
        // a different directory, e.g. java -jar /someOtherDirectory/myApp.jar
        String nativeDir;
        try {
            nativeDir = new File(Exploration.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException uriEx) {
            try {
                // Try to resort to current dir. May still fail later due to bad start dir.
                uriEx.printStackTrace();
                nativeDir = new File(".").getCanonicalPath();
            } catch (IOException ioEx) {
                // Completely failed
                ioEx.printStackTrace();
                return false;
            }
        }
        // Append library subdir
        nativeDir += File.separator + "natives";
        System.setProperty("org.lwjgl.librarypath", nativeDir);
        return true;
    }
}
