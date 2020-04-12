package com.pichaeljanson.diceroll

private val ROLL_PATTERN = longArrayOf(0, 200)
private val NONE_PATTERN = longArrayOf(0)

enum class VibrationType(val pattern: LongArray) {
    ROLL(ROLL_PATTERN),
    NONE(NONE_PATTERN)
}