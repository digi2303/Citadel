package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.world.ModifiableTickRateServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTickRateManager;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ModifiableTickRateServer {

    @Shadow
    public abstract ServerTickRateManager tickRateManager();

    @Unique private long citadel$masterMs;

    @Inject(
            method = "runServer",
            remap = CitadelConstants.REMAPREFS,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;startMetricsRecordingTick()V",
                    shift = At.Shift.BEFORE
            )
    )
    protected void citadel_beforeServerTick(CallbackInfo ci) {
        masterTick();
    }

    private void masterTick() {
        citadel$masterMs += 50L;
    }

    @Override
    public void setGlobalTickLengthMs(long msPerTick) {
        // Vanilla does this already, let's defer to Vanilla.
        if (msPerTick < 0) {
            this.tickRateManager().setTickRate(SharedConstants.TICKS_PER_SECOND);
        } else {
            this.tickRateManager().setTickRate((float) (1000L / msPerTick));
        }
    }

    @Override
    public long getMasterMs() {
        return citadel$masterMs;
    }
}
