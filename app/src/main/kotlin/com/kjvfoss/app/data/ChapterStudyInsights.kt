package com.kjvfoss.app.data

sealed interface StudyRoot {
  val englishTerm: String
  val originalScript: String
  val transliteration: String
  val literalMeaning: String
}

data class HebrewRoot(
  override val englishTerm: String,
  val hebrew: String,
  override val transliteration: String,
  override val literalMeaning: String,
  val matchedWords: Set<String>,
) : StudyRoot {
  override val originalScript: String get() = hebrew
}

private val oldTestamentBooks = setOf(
  "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth",
  "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra",
  "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Solomon's Song",
  "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos",
  "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah",
  "Malachi",
)

private val HebrewRoots = listOf(
  HebrewRoot("beginning", "בְּרֵאשִׁית", "bereshit", "beginning", setOf("beginning")),
  HebrewRoot("create", "בָּרָא", "bara", "create", setOf("create", "created", "createth")),
  HebrewRoot("God", "אֱלֹהִים", "Elohim", "God; gods", setOf("god", "gods")),
  HebrewRoot("heaven", "שָׁמַיִם", "shamayim", "heavens; sky", setOf("heaven", "heavens")),
  HebrewRoot("earth", "אֶרֶץ", "erets", "earth; land", setOf("earth", "land", "lands")),
  HebrewRoot("light", "אוֹר", "or", "light", setOf("light")),
  HebrewRoot("day", "יוֹם", "yom", "day", setOf("day", "days")),
  HebrewRoot("human", "אָדָם", "adam", "human; person", setOf("man", "men", "adam")),
  HebrewRoot("spirit", "רוּחַ", "ruach", "wind; breath; spirit", setOf("spirit", "spirits")),
  HebrewRoot("covenant", "בְּרִית", "berit", "covenant; agreement", setOf("covenant", "covenants")),
  HebrewRoot("king", "מֶלֶךְ", "melekh", "king", setOf("king", "kings")),
  HebrewRoot("peace", "שָׁלוֹם", "shalom", "peace; wholeness", setOf("peace")),
  HebrewRoot("wisdom", "חָכְמָה", "chokhmah", "wisdom", setOf("wisdom")),
  HebrewRoot("life", "חַיִּים", "chayyim", "life", setOf("life", "lives")),
  HebrewRoot("love", "אָהַב", "ahav", "love", setOf("love", "loved")),
  HebrewRoot("holy", "קָדוֹשׁ", "qadosh", "holy; set apart", setOf("holy")),
  HebrewRoot("name", "שֵׁם", "shem", "name", setOf("name", "names")),
  HebrewRoot("house", "בַּיִת", "bayit", "house; household", setOf("house", "houses")),
  HebrewRoot("water", "מַיִם", "mayim", "water", setOf("water", "waters")),
  HebrewRoot("blood", "דָּם", "dam", "blood", setOf("blood")),
  HebrewRoot("father", "אָב", "av", "father", setOf("father", "fathers")),
  HebrewRoot("son", "בֵּן", "ben", "son", setOf("son", "sons")),
  HebrewRoot("word", "דָּבָר", "davar", "word; matter", setOf("word", "words")),
  HebrewRoot("way", "דֶּרֶךְ", "derekh", "way; path", setOf("way", "ways", "path", "paths")),
  HebrewRoot("bless", "בָּרַךְ", "barakh", "bless", setOf("bless", "blessed", "blessing")),
  HebrewRoot("righteousness", "צֶדֶק", "tsedeq", "righteousness; justice", setOf("righteousness")),
  HebrewRoot("salvation", "יְשׁוּעָה", "yeshuah", "deliverance; salvation", setOf("salvation")),
  HebrewRoot("servant", "עֶבֶד", "eved", "servant", setOf("servant", "servants")),
  HebrewRoot("sin", "חַטָּאת", "chatat", "sin; offence", setOf("sin", "sins")),
  HebrewRoot("heart", "לֵב", "lev", "heart; inner person", setOf("heart", "hearts")),
)

fun isOldTestamentChapter(chapter: BibleChapter): Boolean = chapter.book in oldTestamentBooks

fun findHebrewRoots(chapter: BibleChapter): List<HebrewRoot> {
  if (!isOldTestamentChapter(chapter)) return emptyList()
  val chapterWords = Regex("[a-z']+")
    .findAll(chapter.verses.joinToString(" ") { it.text.lowercase() })
    .map { it.value.trim('\'') }
    .toSet()
  return HebrewRoots.filter { root -> root.matchedWords.any(chapterWords::contains) }.take(8)
}

