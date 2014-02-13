package net.ninjacat.pop500;

import android.app.ActivityManager;
import android.app.Application;
import net.ninjacat.pop500.config.Injector;
import net.ninjacat.pop500.logger.Logger;

public class PxApp extends Application {

    private static PxApp instance;
    private Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        injector = new Injector(this.getApplicationContext(), getAvailableMemory());
    }

    private long getAvailableMemory() {
        ActivityManager systemService = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        long memoryClass = systemService.getMemoryClass() * 1024 * 1024;
        Logger.debug("[Injector] Available memory: " + memoryClass);
        return memoryClass;
    }

    public static PxApp getApp() {
        return instance;
    }

    public Injector getInjector() {
        return injector;
    }

    public <T> T getInstance(Class<T> instanceClass) {
        return injector.getContext().getInstance(instanceClass);
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}
