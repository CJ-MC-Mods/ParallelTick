package com.cjm721.paralleltick;

import com.cjm721.paralleltick.api.ParallelizableTick;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class TickExecutor {

    ConcurrentLinkedQueue<WeakReference<ParallelizableTick>> internalTick;
    ConcurrentLinkedQueue<WeakReference<ParallelizableTick>> worldTick;
    ConcurrentLinkedQueue<WeakReference<ParallelizableTick>> serverTick;
    private ForkJoinPool pool;


    public TickExecutor() {
        settupQueues();
    }

    private void settupQueues() {
        internalTick = new ConcurrentLinkedQueue<>();
        worldTick = new ConcurrentLinkedQueue<>();
        serverTick = new ConcurrentLinkedQueue<>();
    }

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {
        if(event.phase != TickEvent.Phase.START)
            return;

        ConcurrentLinkedQueue<WeakReference<ParallelizableTick>> internalCurrent = internalTick;
        ConcurrentLinkedQueue<WeakReference<ParallelizableTick>> worldCurrent = worldTick;
        ConcurrentLinkedQueue<WeakReference<ParallelizableTick>> serverCurrent = serverTick;
        settupQueues();

        createExecutor();
        executeCollection(internalCurrent);

        awaitShutdown();
        createExecutor();
        executeCollection(worldCurrent);

        awaitShutdown();
        createExecutor();
        executeCollection(serverCurrent);
    }

    private void executeCollection(@Nonnull Collection<WeakReference<ParallelizableTick>> runnableList) {
        pool.execute(() -> runnableList.parallelStream().collect(wr -> {
            ParallelizableTick runnable = wr.get();
            if(runnable != null) {
                runnable.parallelTick();
            }
        }));

        pool.shutdown();
    }

    private void awaitShutdown() {
        try {
            pool.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addToInteralTick(@Nonnull ParallelizableTick task){
        internalTick.add(new WeakReference<>(task));
    }

    public void removeFromInternalTick(@Nonnull ParallelizableTick task) {

    }

    public void addToWorldTick(@Nonnull int worldID, @Nonnull ParallelizableTick task) {
        worldTick.add(new WeakReference<>(task));
    }

    public void removeFromWorldTick(@Nonnull ParallelizableTick task) {
    }

    public void addToServerTick(@Nonnull ParallelizableTick task) {
        serverTick.add(new WeakReference<>(task));
    }

    private void createExecutor() {
        pool = new ForkJoinPool(4);
    }
}
