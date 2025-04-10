package com.redlimerl.speedrunigt.impl;

import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.timer.PracticeTimerManager;
import com.redlimerl.speedrunigt.timer.category.RunCategory;

import java.util.ArrayList;
import java.util.Collection;

import static com.redlimerl.speedrunigt.timer.category.RunCategories.*;

public class CategoryRegistryImpl implements SpeedRunIGTApi {
    @Override
    public Collection<RunCategory> registerCategories() {
        ArrayList<RunCategory> list = new ArrayList<>();
        list.add(KILL_DRAGON);
        list.add(CUSTOM);
        list.add(PracticeTimerManager.PRACTICE_CATEGORY);
        list.add(ALL_ADVANCEMENTS);
        return list;
    }
}
