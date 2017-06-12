package com.cjm721.paralleltick.profile;

import com.cjm721.paralleltick.api.ParallelInternalTick;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class ConfigurableBlock extends Block implements ITileEntityProvider{

    public ConfigurableBlock() {
        super(Material.ROCK);

        setRegistryName("config_block");

        GameRegistry.registerTileEntity(ConfigTE.class, "configTE");
}


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ConfigTE();
    }

    private static class ConfigTE extends TileEntity implements ParallelInternalTick{

        @Override
        public boolean internalTick() {
            try {
                Thread.sleep(ConfigurableConfig.teSleepTimeMicro,ConfigurableConfig.teSleepTimeNano);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}
