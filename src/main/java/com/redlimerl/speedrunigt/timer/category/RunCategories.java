package com.redlimerl.speedrunigt.timer.category;

import com.redlimerl.speedrunigt.option.SpeedRunOption;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.running.RunType;

import java.util.function.Function;

public class RunCategories {
    public static RunCategory ERROR_CATEGORY = new RunCategory("unknown","mc");

    public static Function<InGameTimer, Boolean> anyPercentRetime = timer ->
            !SpeedRunOption.getOption(SpeedRunOptions.TIMER_LEGACY_IGT_MODE) && !timer.isCoop() && timer.getRunType() == RunType.RANDOM_SEED && !timer.isRTAMode() &&
                    (SpeedRunOption.getOption(SpeedRunOptions.ALWAYS_USE_AUTO_RETIME) || timer.getInGameTime(false) < 1000 * 60 * 13);
    public static RunCategory KILL_DRAGON = new RunCategory("KILL_DRAGON","mc_af");
    public static RunCategory CUSTOM = new RunCategory("CUSTOM","mc_af#");
    public static RunCategory ALL_ADVANCEMENTS = RunCategoryBuilder.create("ALL_ADVANCEMENTS","mc_af#All_Advancements", "speedrunigt.option.timer_category.all_advancements")
            .setCanSegment(true).build();
}
