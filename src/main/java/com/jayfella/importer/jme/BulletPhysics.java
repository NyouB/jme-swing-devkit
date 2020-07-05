package com.jayfella.importer.jme;

import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;

public class BulletPhysics {

    /**
     * Creates an instance of BulletAppState. Avoids us loading these imports if Bullet Physics is not in the classpath.
     * @return
     */
    public static AppState createAppState() {
        BulletAppState bulletAppState = new BulletAppState();
        bulletAppState.setSpeed(0);

        return bulletAppState;
    }

}
