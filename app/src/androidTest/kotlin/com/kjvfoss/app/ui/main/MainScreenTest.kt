package com.kjvfoss.app.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** UI tests for [com.kjvfoss.app.ui.main.MainScreen]. */
class MainScreenTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent { MainScreen() }
  }

  @Test
  fun localBibleIsLoaded() {
    waitForBibleLoaded()
    composeTestRule.onNodeWithText("Genesis").assertExists()
  }

  @Test
  fun loadingAnimationFinishesWhenBibleIsLoaded() {
    composeTestRule.waitUntil(timeoutMillis = 10_000) {
      composeTestRule.onAllNodesWithText("Loading the KJV…").fetchSemanticsNodes().isNotEmpty()
    }
    composeTestRule.onNodeWithText("Loading the KJV…").assertExists()

    waitForBibleLoaded()
    composeTestRule.onNodeWithText("Loading the KJV…").assertDoesNotExist()
  }

  @Test
  fun insightTabIsRemoved() {
    waitForBibleLoaded()
    composeTestRule.onNodeWithText("Insight").assertDoesNotExist()
  }

  private fun waitForBibleLoaded() {
    composeTestRule.waitUntil(timeoutMillis = 10_000) {
      composeTestRule.mainClock.advanceTimeBy(16)
      composeTestRule.onAllNodesWithText("Genesis", substring = true).fetchSemanticsNodes().isNotEmpty()
    }
  }
}
