/**
 * Copyright (c) 2021 Jordi Saumell
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.hardiksachan.ledger.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Immutable
data class MotionDuration(
    val durationShort1: Long,
    val durationShort2: Long,
    val durationMedium1: Long,
    val durationMedium2: Long,
    val durationLong1: Long,
    val durationLong2: Long,
)

@Immutable
data class MotionTransition(
    val transitionSlide: Int,
    val transitionInitialScale: Float,
    val transitionTargetScale: Float,
    val transitionDuration: Long,
)

val LocalMotionDuration = staticCompositionLocalOf {
    defaultMotionDuration
}

val LocalMotionTransition = staticCompositionLocalOf {
    defaultMotionTransition
}

@Composable
fun MotionTheme(content: @Composable () -> Unit) {
    val motionTransition = defaultMotionTransition.copy(
        transitionSlide = with(LocalDensity.current) { TRANSITION_SLIDE_DPS.dp.toPx().toInt() },
    )
    CompositionLocalProvider(
        LocalMotionDuration provides defaultMotionDuration,
        LocalMotionTransition provides motionTransition,
    ) {
        content()
    }
}

object MotionTheme {
    val duration: MotionDuration
        @Composable
        get() = LocalMotionDuration.current

    val transition: MotionTransition
        @Composable
        get() = LocalMotionTransition.current
}

private val defaultMotionDuration = MotionDuration(
    durationShort1 = 100,
    durationShort2 = 200,
    durationMedium1 = 300,
    durationMedium2 = 400,
    durationLong1 = 500,
    durationLong2 = 600,
)

private val defaultMotionTransition = MotionTransition(
    transitionSlide = 50,
    transitionInitialScale = 0.8F,
    transitionTargetScale = 1.1F,
    transitionDuration = defaultMotionDuration.durationMedium1,
)

private const val TRANSITION_SLIDE_DPS = 30F
