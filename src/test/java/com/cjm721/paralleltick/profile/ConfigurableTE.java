package com.cjm721.paralleltick.profile;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ConfigurableTE.MODID)
public class ConfigurableTE {

    public static final String MODID = "configurablete";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        GameRegistry.register(new ConfigurableBlock());
    }
}
