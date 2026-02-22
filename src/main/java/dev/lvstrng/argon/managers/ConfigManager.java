package dev.lvstrng.argon.managers;

import net.minecraft.client.MinecraftClient;

import java.io.*;

public class ConfigManager {

    private static final File BASE_FOLDER =
            new File(MinecraftClient.getInstance().runDirectory, "MyClient");

    private static final File CONFIG_FILE =
            new File(BASE_FOLDER, "1.21.json");

    // Creează folder + config
    public static void createConfig() {
        try {
            if (!BASE_FOLDER.exists()) {
                BASE_FOLDER.mkdirs();
            }

            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();

                FileWriter writer = new FileWriter(CONFIG_FILE);
                writer.write("{\n");
                writer.write("  \"version\": \"1.21\",\n");
                writer.write("  \"exampleModule\": false\n");
                writer.write("}");
                writer.close();

                System.out.println("Config 1.21 creat!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Încarcă config
    public static void loadConfig() {
        try {
            if (!CONFIG_FILE.exists()) {
                createConfig();
            }

            BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Salvează config
    public static void saveConfig(String content) {
        try {
            FileWriter writer = new FileWriter(CONFIG_FILE);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
