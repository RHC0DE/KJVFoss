package com.kjvfoss.app.ui.main

import com.kjvfoss.app.data.BibleChapter
import com.kjvfoss.app.data.Verse
import com.kjvfoss.app.data.buildChapterInsight
import com.kjvfoss.app.data.findHebrewRoots
import com.kjvfoss.app.data.findGreekRoots
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MainScreenViewModelTest {
  @Test
  fun chapterInsightOnlyReturnsVersesFromItsChapter() {
    val verses = listOf(
      Verse("Genesis", 1, 1, "In the beginning God created the heaven and the earth."),
      Verse("Genesis", 1, 2, "And God said, Let there be light: and there was light."),
    )
    val insight = buildChapterInsight(BibleChapter("Genesis", 1, verses))

    assertEquals(2, insight.verseCount)
    assertTrue(insight.summaryParagraph.contains(verses.first().text))
    assertTrue(insight.summaryParagraph.contains(verses.last().text))
    assertEquals(verses.first(), insight.openingVerse)
    assertEquals(verses[1], insight.middleVerse)
    assertEquals(verses.last(), insight.closingVerse)
    assertTrue(insight.keyVerses.all { it in verses })
    assertTrue("God" in insight.capitalizedTerms)
    assertTrue(findHebrewRoots(BibleChapter("Genesis", 1, verses)).any { it.englishTerm == "God" })
  }

  @Test
  fun chapterInsightSummaryCoversTheChapterProgression() {
    val verses = (1..12).map { number ->
      Verse("Genesis", 2, number, "Verse $number describes a distinct event in the chapter.")
    }

    val summary = buildChapterInsight(BibleChapter("Genesis", 2, verses)).summaryParagraph

    assertTrue(summary.contains(verses.first().text))
    assertTrue(summary.contains(verses[3].text))
    assertTrue(summary.contains(verses[6].text))
    assertTrue(summary.contains(verses[9].text))
    assertTrue(summary.contains(verses.last().text))
    assertTrue(summary.split("“").size >= 6)
  }

  @Test
  fun newTestamentChapterDoesNotReceiveHebrewRootMappings() {
    val chapter = BibleChapter(
      "John",
      3,
      listOf(
        Verse("John", 3, 3, "Except a man be born again, he cannot see the kingdom of God."),
        Verse("John", 3, 16, "For God so loved the world, that he gave his only begotten Son."),
      ),
    )

    assertTrue(findHebrewRoots(chapter).isEmpty())
    assertTrue(findGreekRoots(chapter).any { it.greek == "γεννηθῇ ἄνωθεν" })
    assertTrue(findGreekRoots(chapter).all { it.greek !in setOf("בָּרָא", "אֱלֹהִים") })
  }
}
