package com.aliothmoon.maameow.data.achievement


object AchievementDefinitions {

    val all: List<AchievementDefinition> = buildList {
        // region BASIC_USAGE
        achievement(
            id = AchievementIds.SANITY_SPENDER_1,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.SANITY_SPENDER_GROUP,
            target = 10, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "StageDrops-Stars-3")
            }
        }
        achievement(
            id = AchievementIds.SANITY_SPENDER_2,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.SANITY_SPENDER_GROUP,
            target = 100, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "StageDrops-Stars-3")
            }
        }
        achievement(
            id = AchievementIds.SANITY_SPENDER_3,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.SANITY_SPENDER_GROUP,
            target = 1000, groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "StageDrops-Stars-3")
            }
        }
        achievement(
            id = AchievementIds.SANITY_SAVER_1,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.SANITY_SAVER_GROUP,
            target = 1, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.MEDICINE_USED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.SANITY_SAVER_2,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.SANITY_SAVER_GROUP,
            target = 10, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.MEDICINE_USED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.SANITY_SAVER_3,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.SANITY_SAVER_GROUP,
            target = 50, groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.MEDICINE_USED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.ROGUELIKE_GAME_PASS_1,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_GAME_PASS_GROUP,
            target = 1, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "GamePass")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_GAME_PASS_2,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_GAME_PASS_GROUP,
            target = 5, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "GamePass")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_GAME_PASS_3,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_GAME_PASS_GROUP,
            target = 10, groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "GamePass")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_N04,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_N_GROUP,
            groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.UNLOCK) {
                where("task" to "GamePass")
                condition("difficulty", AchievementConditionOp.GTE, "4")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_N08,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_N_GROUP,
            groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.UNLOCK) {
                where("task" to "GamePass")
                condition("difficulty", AchievementConditionOp.GTE, "8")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_N12,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_N_GROUP,
            groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.UNLOCK) {
                where("task" to "GamePass")
                condition("difficulty", AchievementConditionOp.GTE, "12")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_N15,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_N_GROUP,
            rare = true, groupIndex = 4, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.UNLOCK) {
                where("task" to "GamePass")
                condition("difficulty", AchievementConditionOp.GTE, "15")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_RETREAT,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_GROUP,
            target = 100, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "ExitThenAbandon")
            }
        }
        achievement(
            id = AchievementIds.ROGUELIKE_GOLD_MAX,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.ROGUELIKE_GROUP,
            target = 999, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.SET_MAX) {
                amount = 999
                where("task" to "StageTraderInvestSystemFull")
            }
        }
        achievement(
            id = AchievementIds.FIRST_LAUNCH,
            category = AchievementCategory.BASIC_USAGE,
            releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.SANITY_EXPIRE,
            category = AchievementCategory.BASIC_USAGE,
            target = 8, releasePhase = 1,
        ) {
            trigger(AchievementEvents.MEDICINE_USED, AchievementTriggerMode.SET_MAX) {
                valueKey = "expiringTotal"
                where("isExpiring" to "true")
                condition("expiringTotal", AchievementConditionOp.GTE, "8")
            }
        }
        achievement(
            id = AchievementIds.RECRUIT_GAMBLER,
            category = AchievementCategory.BASIC_USAGE,
            target = 50, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "RecruitRefreshConfirm")
            }
        }
        achievement(
            id = AchievementIds.CLUE_COLLECTOR,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.CLUE_USE_GROUP,
            target = 20, groupIndex = 1, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.INCREMENT) {
                where("taskchain" to "Infrast", "task" to "UnlockClues")
            }
        }
        achievement(
            id = AchievementIds.CLUE_PHILOSOPHER,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.CLUE_USE_GROUP,
            target = 50, groupIndex = 2, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.INCREMENT) {
                where("taskchain" to "Infrast", "task" to "UnlockClues")
            }
        }
        achievement(
            id = AchievementIds.CLUE_OBSESSION,
            category = AchievementCategory.BASIC_USAGE,
            target = 7, rare = true, groupIndex = 3, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.DAILY_STREAK) {
                where("taskchain" to "Infrast", "task" to "UnlockClues")
            }
        }
        achievement(
            id = AchievementIds.CLUE_SHARER,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.CLUE_SEND_GROUP,
            target = 20, groupIndex = 1, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.INCREMENT) {
                where("taskchain" to "Infrast", "task" to "SendClues")
            }
        }
        achievement(
            id = AchievementIds.CLUE_PHILANTHROPIST,
            category = AchievementCategory.BASIC_USAGE,
            group = AchievementIds.CLUE_SEND_GROUP,
            target = 50, groupIndex = 2, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.INCREMENT) {
                where("taskchain" to "Infrast", "task" to "SendClues")
            }
        }
        achievement(
            id = AchievementIds.TIME_MANAGEMENT_MASTER,
            category = AchievementCategory.BASIC_USAGE,
            hidden = true, rare = true, releasePhase = 3,
        ) {
            trigger(AchievementEvents.SCHEDULE_SAVED, AchievementTriggerMode.UNLOCK) {
                condition("timerCount", AchievementConditionOp.GTE, "7")
            }
        }
        achievement(
            id = AchievementIds.DOUBLE_SYNC,
            category = AchievementCategory.BASIC_USAGE,
            releasePhase = 3,
        ) {
            trigger(AchievementEvents.TOOLBOX_RESULT, AchievementTriggerMode.UNLOCK) {
                where("tool" to "DepotOperBox")
            }
        }
        achievement(
            id = AchievementIds.RESUME_RECORD,
            category = AchievementCategory.BASIC_USAGE,
            releasePhase = 3,
        ) {
            trigger(AchievementEvents.TOOLBOX_RESULT, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.QUEUE_EXPANSION,
            category = AchievementCategory.BASIC_USAGE,
            releasePhase = 3,
        ) {
            trigger(AchievementEvents.TASK_NODE_ADDED, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.QUEUE_SIMPLIFIER,
            category = AchievementCategory.BASIC_USAGE,
            releasePhase = 3,
        ) {
            trigger(AchievementEvents.TASK_NODE_REMOVED, AchievementTriggerMode.UNLOCK)
        }
        // endregion

        // region FEATURE_EXPLORATION
        achievement(
            id = AchievementIds.SCHEDULE_MASTER_1,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.SCHEDULE_MASTER_GROUP,
            target = 1, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.SCHEDULE_SAVED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.SCHEDULE_MASTER_2,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.SCHEDULE_MASTER_GROUP,
            target = 100, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.SCHEDULE_SAVED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.MIRROR_CHYAN_FIRST_USE,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.MIRROR_CHYAN_GROUP,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.UPDATE_COMPLETED, AchievementTriggerMode.UNLOCK) {
                where("source" to "MIRROR_CHYAN")
            }
        }
        achievement(
            id = AchievementIds.MIRROR_CHYAN_CDK_ERROR,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.MIRROR_CHYAN_GROUP,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.UPDATE_FAILED, AchievementTriggerMode.UNLOCK) {
                where("source" to "MIRROR_CHYAN", "errorType" to "CDK")
            }
        }
        achievement(
            id = AchievementIds.PIONEER_1,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.PIONEER_GROUP,
            groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.UPDATE_COMPLETED, AchievementTriggerMode.UNLOCK) {
                where("kind" to "app", "channel" to "BETA")
            }
        }
        achievement(
            id = AchievementIds.PIONEER_2,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.PIONEER_GROUP,
            hidden = true, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.UPDATE_COMPLETED, AchievementTriggerMode.UNLOCK) {
                where("kind" to "app", "channel" to "BETA")
            }
        }
        achievement(
            id = AchievementIds.PIONEER_3,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.PIONEER_GROUP,
            hidden = true, groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.UNLOCK) {
                condition("version", AchievementConditionOp.CONTAINS, "dev")
            }
        }
        achievement(
            id = AchievementIds.MOSQUITO_LEG,
            category = AchievementCategory.FEATURE_EXPLORATION,
            target = 5, releasePhase = 1,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.INCREMENT) {
                where("taskchain" to "Mall", "task" to "StageDrops-Stars-3")
            }
        }
        achievement(
            id = AchievementIds.LOG_SUPERVISOR,
            category = AchievementCategory.FEATURE_EXPLORATION,
            releasePhase = 2,
        ) {
            trigger(AchievementEvents.ALL_TASKS_COMPLETED, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.TASK_CHAIN_KING,
            category = AchievementCategory.FEATURE_EXPLORATION,
            target = 7, releasePhase = 2,
        ) {
            trigger(AchievementEvents.MISSION_STARTED, AchievementTriggerMode.UNLOCK) {
                condition("taskCount", AchievementConditionOp.GT, "7")
            }
        }
        achievement(
            id = AchievementIds.WAREHOUSE_MISER,
            category = AchievementCategory.FEATURE_EXPLORATION,
            target = 10000, releasePhase = 2,
        ) {
            trigger(AchievementEvents.TOOLBOX_RESULT, AchievementTriggerMode.SET_MAX) {
                valueKey = "maxCount"
                where("tool" to "Depot")
                condition("maxCount", AchievementConditionOp.GTE, "10000")
            }
        }
        achievement(
            id = AchievementIds.HR_SPECIALIST,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.HR_MANAGER_GROUP,
            target = 10, groupIndex = 1, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "RecruitConfirm")
            }
        }
        achievement(
            id = AchievementIds.HR_SENIOR_SPECIALIST,
            category = AchievementCategory.FEATURE_EXPLORATION,
            group = AchievementIds.HR_MANAGER_GROUP,
            target = 20, groupIndex = 2, releasePhase = 2,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_STARTED, AchievementTriggerMode.INCREMENT) {
                where("task" to "RecruitConfirm")
            }
        }
        achievement(
            id = AchievementIds.LINGUIST,
            category = AchievementCategory.FEATURE_EXPLORATION,
            releasePhase = 2,
        ) {
            trigger(AchievementEvents.LANGUAGE_CHANGED, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.ACHIEVEMENT_OBSERVER,
            category = AchievementCategory.FEATURE_EXPLORATION,
            releasePhase = 3,
        ) {
            trigger(AchievementEvents.ACHIEVEMENT_PAGE_OPENED, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.PRIVATE_DORM_MANAGER,
            category = AchievementCategory.FEATURE_EXPLORATION,
            releasePhase = 3,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.UNLOCK) {
                where("taskchain" to "Infrast")
            }
        }
        // endregion

        // region AUTO_BATTLE
        achievement(
            id = AchievementIds.USE_COPILOT_1,
            category = AchievementCategory.AUTO_BATTLE,
            group = AchievementIds.USE_COPILOT_GROUP,
            target = 1, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.COPILOT_SUCCESS, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.USE_COPILOT_2,
            category = AchievementCategory.AUTO_BATTLE,
            group = AchievementIds.USE_COPILOT_GROUP,
            target = 10, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.COPILOT_SUCCESS, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.USE_COPILOT_3,
            category = AchievementCategory.AUTO_BATTLE,
            group = AchievementIds.USE_COPILOT_GROUP,
            target = 100, groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.COPILOT_SUCCESS, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.COPILOT_LIKE_GIVEN_1,
            category = AchievementCategory.AUTO_BATTLE,
            group = AchievementIds.COPILOT_LIKE_GIVEN_GROUP,
            target = 1, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.COPILOT_LIKED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.COPILOT_LIKE_GIVEN_2,
            category = AchievementCategory.AUTO_BATTLE,
            group = AchievementIds.COPILOT_LIKE_GIVEN_GROUP,
            target = 10, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.COPILOT_LIKED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.COPILOT_LIKE_GIVEN_3,
            category = AchievementCategory.AUTO_BATTLE,
            group = AchievementIds.COPILOT_LIKE_GIVEN_GROUP,
            target = 50, groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.COPILOT_LIKED, AchievementTriggerMode.INCREMENT)
        }
        achievement(
            id = AchievementIds.COPILOT_ERROR,
            category = AchievementCategory.AUTO_BATTLE,
            releasePhase = 1,
        ) {
            trigger(AchievementEvents.SUB_TASK_ERROR, AchievementTriggerMode.UNLOCK) {
                where("subtask" to "CopilotTask")
            }
        }
        achievement(
            id = AchievementIds.MAP_OUTDATED,
            category = AchievementCategory.AUTO_BATTLE,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.SUB_TASK_EXTRA_INFO, AchievementTriggerMode.UNLOCK) {
                where("what" to "UnsupportedLevel")
            }
        }
        achievement(
            id = AchievementIds.IRREPLACEABLE,
            category = AchievementCategory.AUTO_BATTLE,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.SUB_TASK_ERROR, AchievementTriggerMode.UNLOCK) {
                where("subtask" to "BattleFormationTask")
            }
        }
        // endregion

        // region HUMOR
        achievement(
            id = AchievementIds.QUICK_CLOSER,
            category = AchievementCategory.HUMOR,
            hidden = true, releasePhase = 1,
        )
        achievement(
            id = AchievementIds.TACTICAL_RETREAT,
            category = AchievementCategory.HUMOR,
            releasePhase = 1,
        ) {
            trigger(AchievementEvents.TASK_STOPPED, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.MARTIAN,
            category = AchievementCategory.HUMOR,
            hidden = true, releasePhase = 1,
        )
        achievement(
            id = AchievementIds.RECRUIT_NO_SIX_STAR,
            category = AchievementCategory.HUMOR,
            group = AchievementIds.RECRUIT_GROUP,
            target = 500, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.RECRUIT_RESULT, AchievementTriggerMode.INCREMENT) {
                condition("level", AchievementConditionOp.LT, "6")
            }
        }
        achievement(
            id = AchievementIds.RECRUIT_NO_SIX_STAR_STREAK,
            category = AchievementCategory.HUMOR,
            group = AchievementIds.RECRUIT_GROUP,
            target = 500, hidden = true, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.RECRUIT_RESULT, AchievementTriggerMode.INCREMENT) {
                condition("level", AchievementConditionOp.LT, "6")
            }
            trigger(AchievementEvents.RECRUIT_RESULT, AchievementTriggerMode.RESET) {
                condition("level", AchievementConditionOp.GTE, "6")
            }
        }
        // endregion

        // region BUG_RELATED
        achievement(
            id = AchievementIds.CONGRATULATION_ERROR,
            category = AchievementCategory.BUG_RELATED,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.TASK_CHAIN_ERROR, AchievementTriggerMode.UNLOCK)
            trigger(AchievementEvents.SUB_TASK_ERROR, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.UNEXPECTED_CRASH,
            category = AchievementCategory.BUG_RELATED,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.ERROR_LOG_OPENED, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.PROBLEM_FEEDBACK,
            category = AchievementCategory.BUG_RELATED,
            releasePhase = 1,
        )
        achievement(
            id = AchievementIds.CDN_TORTURE,
            category = AchievementCategory.BUG_RELATED,
            target = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.UPDATE_FAILED, AchievementTriggerMode.INCREMENT) {
                where("kind" to "resource")
            }
        }
        achievement(
            id = AchievementIds.BACKSTAGE_EXPLORER,
            category = AchievementCategory.BUG_RELATED,
            releasePhase = 3,
        )
        // endregion

        // region BEHAVIOR
        achievement(
            id = AchievementIds.MISSION_START_COUNT,
            category = AchievementCategory.BEHAVIOR,
            target = 4, releasePhase = 1,
        ) {
            trigger(AchievementEvents.MISSION_STARTED, AchievementTriggerMode.SAME_DAY_COUNT)
        }
        achievement(
            id = AchievementIds.LONG_TASK_TIMEOUT,
            category = AchievementCategory.BEHAVIOR,
            releasePhase = 1,
        )
        achievement(
            id = AchievementIds.PROXY_ONLINE_3_HOURS,
            category = AchievementCategory.BEHAVIOR,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.ALL_TASKS_COMPLETED, AchievementTriggerMode.UNLOCK) {
                condition("elapsedMillis", AchievementConditionOp.GTE, "10800000")
            }
        }
        achievement(
            id = AchievementIds.TASK_START_CANCEL,
            category = AchievementCategory.BEHAVIOR,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.TASK_STOPPED, AchievementTriggerMode.UNLOCK)
        }
        achievement(
            id = AchievementIds.USE_DAILY_1,
            category = AchievementCategory.BEHAVIOR,
            group = AchievementIds.USE_DAILY_GROUP,
            target = 7, groupIndex = 1, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.DAILY_STREAK)
        }
        achievement(
            id = AchievementIds.USE_DAILY_2,
            category = AchievementCategory.BEHAVIOR,
            group = AchievementIds.USE_DAILY_GROUP,
            target = 30, groupIndex = 2, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.DAILY_STREAK)
        }
        achievement(
            id = AchievementIds.USE_DAILY_3,
            category = AchievementCategory.BEHAVIOR,
            group = AchievementIds.USE_DAILY_GROUP,
            target = 365, rare = true, groupIndex = 3, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.DAILY_STREAK)
        }
        achievement(
            id = AchievementIds.UPDATE_OBSESSION,
            category = AchievementCategory.BEHAVIOR,
            group = AchievementIds.UPDATE_GROUP,
            groupIndex = 1, releasePhase = 2,
        )
        achievement(
            id = AchievementIds.UPDATE_EARLY_BIRD,
            category = AchievementCategory.BEHAVIOR,
            group = AchievementIds.UPDATE_GROUP,
            hidden = true, groupIndex = 2, releasePhase = 2,
        )
        // endregion

        // region EASTER_EGG
        achievement(
            id = AchievementIds.APRIL_FOOLS,
            category = AchievementCategory.EASTER_EGG,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.UNLOCK) {
                condition("monthDay", AchievementConditionOp.MONTH_DAY, "04-01")
            }
        }
        achievement(
            id = AchievementIds.MIDNIGHT_LAUNCH,
            category = AchievementCategory.EASTER_EGG,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.UNLOCK) {
                condition("hour", AchievementConditionOp.BETWEEN, "0..3")
            }
        }
        achievement(
            id = AchievementIds.LUNAR_NEW_YEAR,
            category = AchievementCategory.EASTER_EGG,
            hidden = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.UNLOCK) {
                condition("monthDay", AchievementConditionOp.MONTH_DAY_BETWEEN, "01-28..02-04")
            }
        }
        achievement(
            id = AchievementIds.LUCKY,
            category = AchievementCategory.EASTER_EGG,
            hidden = true, rare = true, releasePhase = 1,
        ) {
            trigger(AchievementEvents.APP_LAUNCH, AchievementTriggerMode.UNLOCK) {
                condition("random", AchievementConditionOp.LT, "0.000799")
            }
        }
        achievement(
            id = AchievementIds.SANITY_PLANNER,
            category = AchievementCategory.EASTER_EGG,
            rare = true, releasePhase = 2,
        )
        achievement(
            id = AchievementIds.WAREHOUSE_KEEPER,
            category = AchievementCategory.EASTER_EGG,
            hidden = true, releasePhase = 2,
        ) {
            trigger(AchievementEvents.TOOLBOX_RESULT, AchievementTriggerMode.UNLOCK) {
                where("tool" to "OperBox", "hasPallas" to "true")
            }
        }
        achievement(
            id = AchievementIds.PALLAS_STARTER,
            category = AchievementCategory.EASTER_EGG,
            hidden = true, rare = true, releasePhase = 3,
        ) {
            trigger(AchievementEvents.PROCESS_TASK_COMPLETED, AchievementTriggerMode.UNLOCK) {
                where("taskchain" to "Roguelike", "task" to "StartExplore", "coreChar" to "帕拉斯")
            }
        }
        achievement(
            id = AchievementIds.SLACKING_OFF,
            category = AchievementCategory.EASTER_EGG,
            hidden = true, releasePhase = 3,
        ) {
            trigger(AchievementEvents.MINI_GAME_STARTED, AchievementTriggerMode.UNLOCK)
        }
        // endregion
    }
}

private class TriggerBuilder {
    var amount: Int = 1
    var dateKey: String = ""
    var valueKey: String = ""
    private val where = mutableMapOf<String, String>()
    private val conditions = mutableListOf<AchievementCondition>()

    fun where(vararg pairs: Pair<String, String>) {
        where.putAll(pairs)
    }

    fun condition(field: String, op: AchievementConditionOp, value: String) {
        conditions += AchievementCondition(field, op, value)
    }

    fun build(event: String, mode: AchievementTriggerMode) = AchievementTrigger(
        event = event,
        mode = mode,
        amount = amount,
        where = where.toMap(),
        conditions = conditions.toList(),
        dateKey = dateKey,
        valueKey = valueKey,
    )
}

private class AchievementBuilder {
    private val triggers = mutableListOf<AchievementTrigger>()

    fun trigger(
        event: String,
        mode: AchievementTriggerMode = AchievementTriggerMode.UNLOCK,
        block: TriggerBuilder.() -> Unit = {},
    ) {
        triggers += TriggerBuilder().apply(block).build(event, mode)
    }

    fun build(def: AchievementDefinition): AchievementDefinition = def.copy(
        trigger = triggers.firstOrNull(),
        triggers = triggers.drop(1),
    )
}

private fun MutableList<AchievementDefinition>.achievement(
    id: String,
    category: AchievementCategory,
    group: String = "",
    target: Int = 0,
    hidden: Boolean = false,
    rare: Boolean = false,
    groupIndex: Int = Int.MAX_VALUE,
    releasePhase: Int = 1,
    block: AchievementBuilder.() -> Unit = {},
) {
    val def =
        AchievementDefinition(id, category, group, target, hidden, rare, groupIndex, releasePhase)
    add(AchievementBuilder().apply(block).build(def))
}
