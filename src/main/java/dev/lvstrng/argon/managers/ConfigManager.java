package dev.lvstrng.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lvstrng.Lvstrng;
import dev.lvstrng.module.Module;
import dev.lvstrng.module.setting.*;
import dev.lvstrng.utils.EncryptedString;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.*;

public final class ConfigManager {

    private JsonObject jsonObject;
    private final File configFile;
    private final Gson gson;

    public ConfigManager() {
        File mcDir = net.minecraft.client.MinecraftClient.getInstance().runDirectory;
        File configFolder = new File(mcDir, "Lvstrng");

        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        this.configFile = new File(configFolder, "1.21.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.jsonObject = new JsonObject();
        this.loadConfigFromFile();
    }

    private void loadConfigFromFile() {
        try {
            if (configFile.exists()) {
                FileReader reader = new FileReader(configFile);
                jsonObject = gson.fromJson(reader, JsonObject.class);
                if (jsonObject == null) jsonObject = new JsonObject();
                reader.close();
            } else {
                jsonObject = new JsonObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = new JsonObject();
        }
    }

    public void loadProfile() {
        if (jsonObject == null) return;

        for (Object obj : Lvstrng.INSTANCE.getModuleManager().c()) {
            Module module = (Module) obj;
            String moduleName = getModuleName(module);

            if (!jsonObject.has(moduleName)) continue;

            JsonObject moduleJson = jsonObject.getAsJsonObject(moduleName);

            if (moduleJson.has("enabled") && moduleJson.get("enabled").getAsBoolean()) {
                module.toggle(true);
            }

            for (Object settingObj : module.getSettings()) {
                Setting setting = (Setting) settingObj;
                String settingName = getSettingName(setting);

                if (!moduleJson.has(settingName)) continue;

                setValueFromJson(setting, moduleJson.get(settingName), module);
            }
        }
    }

    private String getModuleName(Module module) {
        CharSequence name = module.getName();
        return name instanceof EncryptedString ? name.toString() : name.toString();
    }

    private String getSettingName(Setting setting) {
        CharSequence name = setting.getName();
        return name instanceof EncryptedString ? name.toString() : name.toString();
    }

    private void setValueFromJson(Setting setting, JsonElement element, Module module) {

        if (setting instanceof BooleanSetting s)
            s.setValue(element.getAsBoolean());

        else if (setting instanceof ModeSetting s)
            s.setModeIndex(element.getAsInt());

        else if (setting instanceof NumberSetting s)
            s.setValue(element.getAsDouble());

        else if (setting instanceof BindSetting s) {
            s.setValue(element.getAsInt());
            if (s.isModuleKey())
                module.setKeybind(element.getAsInt());
        }

        else if (setting instanceof StringSetting s)
            s.setValue(element.getAsString());

        else if (setting instanceof MinMaxSetting s) {
            JsonObject obj = element.getAsJsonObject();
            s.setCurrentMin(obj.get("min").getAsDouble());
            s.setCurrentMax(obj.get("max").getAsDouble());
        }

        else if (setting instanceof ItemSetting s)
            s.setItem(Registries.ITEM.get(Identifier.of(element.getAsString())));

        else if (setting instanceof MacroSetting s) {
            s.clearCommands();
            for (JsonElement e : element.getAsJsonArray())
                s.addCommand(e.getAsString());
        }
    }

    public void saveConfig() {
        shutdown();
    }

    public void shutdown() {
        jsonObject = new JsonObject();

        for (Object obj : Lvstrng.INSTANCE.getModuleManager().c()) {
            Module module = (Module) obj;
            JsonObject moduleJson = new JsonObject();

            moduleJson.addProperty("enabled", module.isEnabled());

            for (Object settingObj : module.getSettings()) {
                Setting setting = (Setting) settingObj;
                saveSetting(setting, moduleJson, module);
            }

            jsonObject.add(getModuleName(module), moduleJson);
        }

        saveToFile();
    }

    private void saveSetting(Setting setting, JsonObject json, Module module) {

        String name = getSettingName(setting);

        if (setting instanceof BooleanSetting s)
            json.addProperty(name, s.getValue());

        else if (setting instanceof ModeSetting s)
            json.addProperty(name, s.getModeIndex());

        else if (setting instanceof NumberSetting s)
            json.addProperty(name, s.getValue());

        else if (setting instanceof BindSetting s)
            json.addProperty(name, s.getValue());

        else if (setting instanceof StringSetting s)
            json.addProperty(name, s.getValue());

        else if (setting instanceof MinMaxSetting s) {
            JsonObject obj = new JsonObject();
            obj.addProperty("min", s.getCurrentMin());
            obj.addProperty("max", s.getCurrentMax());
            json.add(name, obj);
        }

        else if (setting instanceof ItemSetting s)
            json.addProperty(name,
                    Registries.ITEM.getId(s.getItem()).toString());

        else if (setting instanceof MacroSetting s) {
            JsonArray array = new JsonArray();
            for (Object cmd : s.getCommands())
                array.add((String) cmd);
            json.add(name, array);
        }
    }

    private void saveToFile() {
        try {
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(jsonObject, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getConfigFile() {
        return configFile;
    }
    }
