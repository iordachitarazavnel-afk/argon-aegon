package dev.lvstrng.dev.module.modules.client;

import dev.lvstrng.managers.ConfigManager;

public class AutoConfigModule {

    private boolean enabled = false;

    public void toggle() {
        enabled = !enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {
        ConfigManager.createConfig();
        System.out.println("Modul activat - config creat.");
    }

    public void onDisable() {
        System.out.println("Modul dezactivat.");
    }
}
