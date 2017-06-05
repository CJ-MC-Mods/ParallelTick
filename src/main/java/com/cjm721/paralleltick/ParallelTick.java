package com.cjm721.paralleltick;

import com.cjm721.paralleltick.api.ParallelWorldTick;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(modid = ParallelTick.MODID, version = ParallelTick.VERSION,
        acceptedMinecraftVersions = "1.11.2",
        useMetadata = true
)
public class ParallelTick {
    public static final String MODID = "paralleltick";
    public static final String VERSION = "${mod_version}";


    @Mod.Instance(ParallelTick.MODID)
    public static ParallelTick INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TickExecutor());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
