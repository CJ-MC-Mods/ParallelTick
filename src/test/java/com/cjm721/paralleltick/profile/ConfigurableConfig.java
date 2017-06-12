package com.cjm721.paralleltick.profile;

import net.minecraftforge.common.config.Config;

@Config(modid = ConfigurableTE.MODID)
public class ConfigurableConfig {

    @Config.Comment("Amount of time each TE execution should take, Total time is Micro+Nano")
    public static long teSleepTimeMicro = 0;
    @Config.Comment("Amount of time each TE execution should take, Total time is Micro+Nano")
    public static int teSleepTimeNano = 200;
}
