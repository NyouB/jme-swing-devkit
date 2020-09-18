package com.jayfella.devkit.jme;

import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;

/**
 * A seperate class that allows us to avoid referencing imports to a bullet physics dependency that might not exist.
 */
public class BulletPhysics {

    /**
     * Creates an instance of BulletAppState.
     * @return a new BulletAppState instance.
     */
    public static AppState createAppState() {
        BulletAppState bulletAppState = new BulletAppState();
        bulletAppState.setSpeed(0);

        return bulletAppState;
    }

}
