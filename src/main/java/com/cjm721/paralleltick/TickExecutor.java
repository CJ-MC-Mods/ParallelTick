package com.cjm721.paralleltick;

import com.cjm721.paralleltick.api.ParallelInternalTick;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.*;

public class TickExecutor {

    public static TickExecutor INSTANCE;

    Collection<WeakReference<ParallelInternalTick>> internalTick;
    private ForkJoinPool pool;


    public TickExecutor() {
        INSTANCE = this;
        setupQueues();
        createExecutor();
    }

    private void setupQueues() {
        internalTick = new ArrayList<>();
    }

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {
        if(event.phase != TickEvent.Phase.START)
            return;
        Collection<WeakReference<ParallelInternalTick>> internalCurrent = internalTick;
        setupQueues();

        ForkJoinTask temp = pool.submit(() -> internalCurrent.parallelStream().forEach(wr -> {
            ParallelInternalTick runnable = wr.get();
            if(runnable != null && runnable.internalTick()) {
//              System.out.println(Thread.currentThread().getName());
                this.addToInternalTick(wr);
            }
        }));

        try {
            temp.get();
        } catch (InterruptedException | ExecutionException  e) {
            e.printStackTrace();
        }
    }

    public void addToInternalTick(@Nonnull ParallelInternalTick task){
        addToInternalTick(new WeakReference<>(task));
    }

    private void addToInternalTick(@Nonnull WeakReference<ParallelInternalTick> wrTask) {
        synchronized (internalTick) {
            internalTick.add(wrTask);
        }
    }

    private void createExecutor() {
        pool = new ForkJoinPool(4);
    }
}
