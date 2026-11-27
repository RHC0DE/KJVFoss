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
    composeTestRule.setContent { MainScreen(onItemClick = {}) }
  }

  @Test
  fun localBibleIsLoaded() {
    composeTestRule.waitUntil(timeoutMillis = 10_000) {
      composeTestRule.onAllNodesWithText("Genesis", substring = true).fetchSemanticsNodes().isNotEmpty()
    }
    composeTestRule.onNodeWithText("Genesis").assertExists()
  }

  @Test
  fun insightTabIsRemoved() {
    composeTestRule.waitUntil(timeoutMillis = 10_000) {
      composeTestRule.onAllNodesWithText("Genesis", substring = true).fetchSemanticsNodes().isNotEmpty()
    }
    composeTestRule.onNodeWithText("Insight").assertDoesNotExist()
  }
}
