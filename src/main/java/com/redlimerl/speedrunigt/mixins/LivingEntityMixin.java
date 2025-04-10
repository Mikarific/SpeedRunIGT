package com.redlimerl.speedrunigt.mixins;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.InGameTimerUtils;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow protected boolean dead;

    @Shadow @Nullable protected LazyEntityReference<PlayerEntity> attackingPlayer;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource source, CallbackInfo ci) {
        @NotNull InGameTimer timer = InGameTimer.getInstance();

        if (this.isRemoved() || this.dead || timer.getStatus() == TimerStatus.NONE) return;

        // For Timelines
        if (this.getType() == EntityType.WITHER && this.attackingPlayer != null) timer.tryInsertNewTimeline("kill_wither");
        if (this.getType() == EntityType.ELDER_GUARDIAN && this.attackingPlayer != null) timer.tryInsertNewTimeline("kill_elder_guardian");
        if (this.getType() == EntityType.WARDEN && this.attackingPlayer != null) timer.tryInsertNewTimeline("kill_warden");
        if (this.getType() == EntityType.ENDER_DRAGON) timer.tryInsertNewTimeline("kill_ender_dragon");

        if (this.getType() == EntityType.ENDER_DRAGON && !this.getWorld().isClient()) {
            InGameTimerUtils.IS_KILLED_ENDER_DRAGON = true;
        }
    }
}
