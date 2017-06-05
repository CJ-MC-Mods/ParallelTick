package com.cjm721.paralleltick;

import com.cjm721.paralleltick.api.ParallelInternalTick;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TickExecutor {

    public static TickExecutor INSTANCE;

    Collection<WeakReference<ParallelInternalTick>> internalTick;
    private ForkJoinPool pool;


    public TickExecutor() {
        INSTANCE = this;
        setupQueues();
    }

    private void setupQueues() {
        internalTick = new LinkedList<>();
    }

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {
        if(event.phase != TickEvent.Phase.START)
            return;
        Collection<WeakReference<ParallelInternalTick>> internalCurrent = internalTick;
        setupQueues();

        createExecutor();
        ForkJoinTask<Stream<WeakReference<ParallelInternalTick>>> temp = pool.submit(() -> internalCurrent.parallelStream().filter(wr -> {
            ParallelInternalTick runnable = wr.get();
            return runnable != null && runnable.internalTick();
        }));

        try {
            Stream<WeakReference<ParallelInternalTick>> result = temp.get();
            internalCurrent.addAll(result.collect(Collectors.toList()));
        } catch (InterruptedException | ExecutionException  e) {
            e.printStackTrace();
        }
    }

    public void addToInteralTick(@Nonnull ParallelInternalTick task){
        internalTick.add(new WeakReference<>(task));
    }

    private void createExecutor() {
        pool = new ForkJoinPool(4);
    }
}
