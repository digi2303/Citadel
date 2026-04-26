package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.generation.SurfaceRulesManager;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;

@Mixin(value = NoiseGeneratorSettings.class, priority = 500)
public class NoiseGeneratorSettingsMixin {

    @Shadow
    @Final
    private SurfaceRules.RuleSource surfaceRule;

    @ModifyReturnValue(method = "surfaceRule", at = @At("RETURN"))
    private SurfaceRules.RuleSource citadel$surfaceRule(SurfaceRules.RuleSource original) {
        return SurfaceRulesManager.mergeOverworldRules(original);
    }
}
