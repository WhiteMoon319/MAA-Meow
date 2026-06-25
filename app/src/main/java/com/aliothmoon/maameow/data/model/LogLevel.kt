package com.aliothmoon.maameow.data.model

/** 参考 MaaWPFGUI 的 UiLogColor；颜色经 [LogColorRole] 间接表达。 */
enum class LogLevel(
    val displayName: String,
    val colorRole: LogColorRole,
    val severity: LogSeverity
) {
    MESSAGE("MSG", LogColorRole.DEFAULT, LogSeverity.MESSAGE),
    INFO("INFO", LogColorRole.INFO, LogSeverity.INFO),
    SUCCESS("SUCCESS", LogColorRole.SUCCESS, LogSeverity.INFO),
    WARNING("WRN", LogColorRole.WARNING, LogSeverity.WARNING),
    ERROR("ERR", LogColorRole.ERROR, LogSeverity.ERROR),
    TRACE("TRACE", LogColorRole.TRACE, LogSeverity.TRACE),

    RECRUIT_STAR_1("1星", LogColorRole.STAR_1, LogSeverity.INFO),
    RECRUIT_STAR_2("2星", LogColorRole.STAR_2, LogSeverity.INFO),
    RECRUIT_STAR_3("3星", LogColorRole.STAR_3, LogSeverity.INFO),
    RECRUIT_STAR_4("4星", LogColorRole.STAR_4, LogSeverity.INFO),
    RECRUIT_STAR_5("5星", LogColorRole.STAR_5, LogSeverity.INFO),
    RECRUIT_STAR_6("6星", LogColorRole.STAR_6, LogSeverity.INFO),
    RECRUIT_ROBOT("机械", LogColorRole.ROBOT, LogSeverity.INFO),

    ROGUELIKE_SUCCESS("战斗成功", LogColorRole.ROGUELIKE_SUCCESS, LogSeverity.INFO),
    ROGUELIKE_COMBAT("作战", LogColorRole.ROGUELIKE_COMBAT, LogSeverity.INFO),
    ROGUELIKE_EMERGENCY("紧急", LogColorRole.ROGUELIKE_EMERGENCY, LogSeverity.INFO),
    ROGUELIKE_BOSS("领袖", LogColorRole.ROGUELIKE_BOSS, LogSeverity.INFO),
    ROGUELIKE_ABANDON("放弃", LogColorRole.ROGUELIKE_ABANDON, LogSeverity.INFO),

    RARE("稀有", LogColorRole.RARE, LogSeverity.INFO);

    companion object {
        /** 1..6 → 对应星级，超出范围回退到 [MESSAGE]。 */
        fun forRecruitStar(star: Int): LogLevel = when (star) {
            1 -> RECRUIT_STAR_1
            2 -> RECRUIT_STAR_2
            3 -> RECRUIT_STAR_3
            4 -> RECRUIT_STAR_4
            5 -> RECRUIT_STAR_5
            6 -> RECRUIT_STAR_6
            else -> MESSAGE
        }
    }
}
