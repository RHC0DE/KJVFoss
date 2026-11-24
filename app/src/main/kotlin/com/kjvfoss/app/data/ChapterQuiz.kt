package com.kjvfoss.app.data

data class ChapterInsight(
  val verseCount: Int,
  val summaryParagraph: String,
  val keyVerses: List<Verse>,
  val openingVerse: Verse,
  val middleVerse: Verse,
  val closingVerse: Verse,
  val capitalizedTerms: List<String>,
)

fun buildChapterInsight(chapter: BibleChapter): ChapterInsight {
  val commonCapitalizedWords = setOf(
    "A", "An", "And", "As", "At", "Behold", "But", "By", "For", "From", "He", "Her",
    "His", "I", "In", "It", "Lo", "My", "Nay", "Now", "Of", "On", "Or", "Our", "She",
    "So", "That", "The", "Their", "Them", "Then", "There", "These", "They", "This",
    "Those", "Thou", "Thus", "Thy", "To", "When", "Where", "Which", "Who", "Whosoever",
    "With", "Ye", "Yea", "You", "Your",
  )
  val terms = Regex("\\b[A-Z][A-Za-z]+\\b")
    .findAll(chapter.verses.joinToString(" ") { it.text })
    .map { it.value }
    .filterNot { it in commonCapitalizedWords }
    .distinct()
    .take(12)
    .toList()
  val openingVerse = chapter.verses.first()
  val middleVerse = chapter.verses[chapter.verses.size / 2]
  val closingVerse = chapter.verses.last()
  val keyVerses = chapter.verses.sortedByDescending { it.text.length }.take(3).sortedBy(Verse::number)
  val progressionVerses = listOf(
    0,
    chapter.verses.size / 4,
    chapter.verses.size / 2,
    chapter.verses.size * 3 / 4,
    chapter.verses.lastIndex,
  ).map(chapter.verses::get)
  val summaryVerses = (progressionVerses + keyVerses)
    .distinctBy(Verse::reference)
    .sortedBy(Verse::number)
  val keyVerseReferences = keyVerses.map(Verse::reference).toSet()
  val summary = summaryVerses.map { verse ->
    val introduction = when {
      verse.reference == openingVerse.reference -> "The chapter opens in verse ${verse.number}:"
      verse.reference == closingVerse.reference -> "It closes in verse ${verse.number}:"
      verse.reference in keyVerseReferences -> "A key passage in verse ${verse.number} adds detail:"
      verse.number < middleVerse.number -> "As the passage develops, verse ${verse.number} records:"
      else -> "Later in the chapter, verse ${verse.number} records:"
    }
    "$introduction “${verse.text}”"
  }.joinToString(" ")
  return ChapterInsight(
    verseCount = chapter.verses.size,
    summaryParagraph = summary,
    keyVerses = keyVerses,
    openingVerse = openingVerse,
    middleVerse = middleVerse,
    closingVerse = closingVerse,
    capitalizedTerms = terms,
  )
}
