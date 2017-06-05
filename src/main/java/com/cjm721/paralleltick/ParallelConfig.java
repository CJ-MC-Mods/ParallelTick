package com.cjm721.paralleltick;

import net.minecraftforge.common.config.Config;

@Config(modid = ParallelTick.MODID)
public class ParallelConfig {

    @Config.Comment({"If set to false will use Virtual Core Count - 2. If set to true will use threadPoolCount as the max amount of threads "})
    public static boolean forceCount = false;
    @Config.Comment({"Sets the max size of the thread pool"})
    public static int threadPoolCount = 4;
}
