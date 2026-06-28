package com.aliothmoon.maameow.data.model

import android.content.Context
import androidx.annotation.StringRes
import com.aliothmoon.maameow.R

enum class TaskTypeInfo(
    @param:StringRes val nameRes: Int,
    val defaultConfig: () -> TaskParamProvider
) {
    WAKE_UP(R.string.task_type_wake_up, { WakeUpConfig() }),
    RECRUITING(R.string.task_type_recruiting, { RecruitConfig() }),
    BASE(R.string.task_type_base, { InfrastConfig() }),
    COMBAT(R.string.task_type_combat, { FightConfig() }),
    MALL(R.string.task_type_mall, { MallConfig() }),
    MISSION(R.string.task_type_mission, { AwardConfig() }),
    AUTO_ROGUELIKE(R.string.task_type_auto_roguelike, { RoguelikeConfig() }),
    RECLAMATION(R.string.task_type_reclamation, { ReclamationConfig() });

    fun defaultName(context: Context): String = context.getString(nameRes)
}
