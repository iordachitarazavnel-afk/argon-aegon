package dev.lvstrng.argon;

import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.gui.ClickGui;
import dev.lvstrng.argon.managers.FriendManager;
import dev.lvstrng.argon.managers.ModuleManager;
import dev.lvstrng.argon.managers.ProfileManager;
import dev.lvstrng.argon.utils.rotation.RotatorManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("all")
public final class Argon {

    public RotatorManager rotatorManager;
    public ProfileManager profileManager;
    public ModuleManager moduleManager;
    public EventManager eventManager;
    public FriendManager friendManager;
    public static MinecraftClient mc;
    public String version = " b1.3";
    public static boolean BETA;
    public static Argon INSTANCE;
    public boolean guiInitialized;
    public ClickGui clickGui;
    public Screen previousScreen = null;
    public long lastModified;
    public File argonJar;

    // CONFIG PATH
    public static File ARGON_FOLDER;
    public static File CONFIG_FILE;

    public Argon() throws InterruptedException, IOException {
        INSTANCE = this;

        mc = MinecraftClient.getInstance();

        // Initialize managers
        this.eventManager = new EventManager();
        this.moduleManager = new ModuleManager();
        this.clickGui = new ClickGui();
        this.rotatorManager = new RotatorManager();
        this.profileManager = new ProfileManager();
        this.friendManager = new FriendManager();

        // Create folder + config
        createConfig();

        // Load profile after config exists
        this.getProfileManager().loadProfile();

        this.setLastModified();
        this.guiInitialized = false;
    }

    private void createConfig() {
        try {
            // .minecraft folder
            File mcDir = mc.runDirectory;

            // Create Argon folder
            ARGON_FOLDER = new File(mcDir, "Argon");
            if (!ARGON_FOLDER.exists()) {
                ARGON_FOLDER.mkdirs();
            }

            // Create 1.21.json config
            CONFIG_FILE = new File(ARGON_FOLDER, "1.21.json");
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
                System.out.println("[Argon] Created 1.21 config.");
            } else {
                System.out.println("[Argon] Loaded existing 1.21 config.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ClickGui getClickGui() {
        return clickGui;
    }

    public void resetModifiedDate() {
        if (this.argonJar != null) {
            this.argonJar.setLastModified(lastModified);
        }
    }

    public String getVersion() {
        return version;
    }

    public void setLastModified() {
        try {
            this.argonJar = new File(Argon.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            this.lastModified = argonJar.lastModified();
        } catch (URISyntaxException ignored) {}
    }
}
