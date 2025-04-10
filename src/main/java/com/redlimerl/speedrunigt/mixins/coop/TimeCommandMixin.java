package com.redlimerl.speedrunigt.mixins.coop;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import com.redlimerl.speedrunigt.timer.packet.TimerPacketUtils;
import com.redlimerl.speedrunigt.timer.packet.packets.TimerStartPacket;
import net.minecraft.server.GameInstance;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TimeCommand.class)
public class TimeCommandMixin {

    @Inject(method = "executeSet", at = @At("RETURN"))
    private static void onSet(ServerCommandSource source, int time, CallbackInfoReturnable<Integer> cir) {
        try (GameInstance gameInstance = source.method_69818()) {
            if (time == 0 && InGameTimer.getInstance().getStatus() != TimerStatus.NONE && InGameTimer.getInstance().isCoop()
                    && gameInstance != null && gameInstance.getServer() != null) {
                TimerPacketUtils.sendServer2ClientPacket(gameInstance.getServer(), new TimerStartPacket(InGameTimer.getInstance(), 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
