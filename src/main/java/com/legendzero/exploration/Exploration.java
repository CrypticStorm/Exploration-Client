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

import com.legendzero.exploration.entity.Player;
import com.legendzero.exploration.physics.Physics;
import com.legendzero.exploration.render.RenderState;
import com.legendzero.exploration.world.World;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author CrypticStorm
 */
public class Exploration {

    private final int viewSize;

    private Player player;
    private World world;
    private RenderState state;
    private Set<Physics> physicsEngines;

    private Exploration() throws LWJGLException {
        this.viewSize = 250;
        this.state = RenderState.GAME;
        this.physicsEngines = new HashSet<Physics>();

        this.initDisplay();
        this.initGame();
        this.initControls();

        this.runGame();

        this.endGame();
    }

    public Player getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public RenderState getRenderState() {
        return this.state;
    }

    public int getViewSize() {
        return this.viewSize;
    }

    public void addPhysicsEngine(Physics physics) {
        this.physicsEngines.add(physics);
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
    }

    private void initGame() {
        this.player = new Player("CrypticStorm");
        this.world = new World(this, 1000, 1000);
        this.world.spawnEntity(player, this.world.getSpawnLocation().add(0.5, 0));
    }

    private void initControls() {

    }

    private void runGame() {
        while (!Display.isCloseRequested()) {
            for(Physics physics : this.physicsEngines) {
                physics.update(this);
            }
            state.render(this);
            Display.update();
            Display.sync(60);
        }
    }

    private void endGame() {
        Display.destroy();
    }

    public static void main(String[] args) {
        if (!loadNatives()) {
            System.exit(0);
        }
        try {
            Exploration game = new Exploration();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
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
