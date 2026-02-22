package dev.lvstrng.module.modules.client;

import dev.lvstrng.Lvstrng;
import dev.lvstrng.module.Category;
import dev.lvstrng.module.Module;
import dev.lvstrng.module.setting.BooleanSetting;
import dev.lvstrng.module.setting.NumberSetting;
import dev.lvstrng.module.setting.Setting;
import dev.lvstrng.module.setting.StringSetting;
import dev.lvstrng.utils.EncryptedString;

public final class ConfigDebug extends Module {

    private final BooleanSetting testBoolean = new BooleanSetting(
            EncryptedString.of("Test Boolean"), false)
            .setDescription(EncryptedString.of("Test boolean setting"));

    private final NumberSetting testNumber = new NumberSetting(
            EncryptedString.of("Test Number"), 50.0, 1.0, 100.0, 1.0)
            .setDescription(EncryptedString.of("Test number setting"));

    private final StringSetting testString = new StringSetting(
            EncryptedString.of("Test String"), "Default Value")
            .setDescription(EncryptedString.of("Test string setting"));

    public ConfigDebug() {
        super(EncryptedString.of("Config Debug"),
                EncryptedString.of("Debug module for testing config saving"),
                -1,
                Category.CLIENT);

        this.addSettings(new Setting[]{testBoolean, testNumber, testString});
    }

    @Override
    public void onEnable() {
        System.out.println("[ConfigDebug] Module enabled - testing config saving...");

        // Modificăm valorile pentru test
        testBoolean.setValue(true);
        testNumber.setValue(75.0); // corect
        testString.setValue("Test Value Changed");

        // Salvăm config-ul folosind noul ConfigManager
        Lvstrng.INSTANCE.getConfigManager().manualSave();

        System.out.println("[ConfigDebug] Config save test completed. Check console for results.");

        // Dezactivăm modulul imediat după test
        this.toggle();
    }

    @Override
    public void onDisable() {
        // Nu facem nimic aici
    }
}
