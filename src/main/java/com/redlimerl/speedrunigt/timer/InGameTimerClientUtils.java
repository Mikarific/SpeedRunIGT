package com.redlimerl.speedrunigt.timer;

import com.redlimerl.speedrunigt.gui.screen.FailedCategoryInitScreen;
import com.redlimerl.speedrunigt.mixins.access.MinecraftClientAccessorForAttack;
import com.redlimerl.speedrunigt.mixins.access.WorldRendererAccessor;
import com.redlimerl.speedrunigt.timer.category.InvalidCategoryException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class InGameTimerClientUtils {

    public static boolean canUnpauseTimer(boolean checkRender) {
        MinecraftClient client = MinecraftClient.getInstance();
        InGameTimer timer = InGameTimer.getInstance();

        if (timer.getStatus() != TimerStatus.IDLE) return false;

        if (!client.isPaused() && client.worldRenderer != null && client.isWindowFocused() && client.mouse.isCursorLocked()
                && !InGameTimerUtils.IS_CHANGING_DIMENSION) {
            if (checkRender) {
                WorldRendererAccessor worldRenderer = (WorldRendererAccessor) client.worldRenderer;
                int chunks = worldRenderer.invokeCompletedChunkCount();
                int entities = worldRenderer.getRenderedEntityCount() - (client.options.getPerspective().isFirstPerson() ? 0 : 1);

                return chunks + entities > 0;
            }
            return true;
        }
        return false;
    }

    public static float getGeneratedChunkRatio() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null && client.player != null) {
            int chunks = client.options.getViewDistance().getValue() * 2 + 1;
            return (float) client.world.getChunkManager().getLoadedChunkCount() / (chunks*chunks);
        }
        return 0;
    }

    public static boolean isHardcoreWorld() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && client.player.getWorld().getLevelProperties().isHardcore();
    }

    public static Long getPlayerTime() {
        MinecraftServer server = MinecraftClient.getInstance().getServer();
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (server != null && server.getGameInstance() != null && clientPlayer != null) {
            ServerPlayerEntity player = server.getGameInstance().getPlayerManager().getPlayer(clientPlayer.getUuid());
            ServerStatHandler statHandler = server.getGameInstance().getPlayerManager().createStatHandler(player);
            return statHandler == null ? null : statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME)) * 50L;
        }
        return null;
    }

    public static @Nullable FailedCategoryInitScreen FAILED_CATEGORY_INIT_SCREEN = null;
    static void setCategoryWarningScreen(@Nullable String conditionFileName, InvalidCategoryException exception) {
        if (MinecraftClient.getInstance().currentScreen == null)
            FAILED_CATEGORY_INIT_SCREEN = new FailedCategoryInitScreen(conditionFileName, exception);
        else MinecraftClient.getInstance().setScreen(new FailedCategoryInitScreen(conditionFileName, exception));
    }

    static MinecraftServer getClientServer() {
        return MinecraftClient.getInstance().getServer();
    }

    public static boolean isFocusedClick() {
        return MinecraftClient.getInstance().player != null && !MinecraftClient.getInstance().player.isUsingItem()
                && ((MinecraftClientAccessorForAttack) MinecraftClient.getInstance()).getAttackCoolDown() <= 0;
    }
}
