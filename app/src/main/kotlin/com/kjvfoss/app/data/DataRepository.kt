package com.kjvfoss.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.userDataStore by preferencesDataStore(name = "kjvfoss_user_data")
private val savedDataKey = stringPreferencesKey("saved_data")
private val lastBookKey = stringPreferencesKey("last_book")
private val lastChapterKey = intPreferencesKey("last_chapter")
private val json = Json { ignoreUnknownKeys = true }

data class Verse(val book: String, val chapter: Int, val number: Int, val text: String) {
  val reference: String get() = "$book $chapter:$number"
}

data class BibleChapter(val book: String, val number: Int, val verses: List<Verse>) {
  val reference: String get() = "$book $number"
}

data class BibleData(val books: List<String>, val chapters: List<BibleChapter>) {
  fun chaptersFor(book: String): List<BibleChapter> = chapters.filter { it.book == book }
  fun chapter(book: String, number: Int): BibleChapter? =
    chapters.firstOrNull { it.book == book && it.number == number }
}

@Serializable
data class SavedVerse(
  val reference: String,
  val highlightColor: String? = null,
  val bookmarked: Boolean = false,
)

@Serializable
data class SavedData(
  val verses: List<SavedVerse> = emptyList(),
)

class BibleRepository(private val context: Context) {
  fun savedData(): Flow<SavedData> = context.userDataStore.data.map { preferences ->
    preferences[savedDataKey]?.let { json.decodeFromString<SavedData>(it) } ?: SavedData()
  }

  suspend fun setBookmark(reference: String, bookmarked: Boolean) = updateSavedVerse(reference) {
    it.copy(bookmarked = bookmarked)
  }

  suspend fun setHighlight(reference: String, color: String?) = updateSavedVerse(reference) {
    it.copy(highlightColor = color)
  }

  fun lastLocation(): Flow<Pair<String, Int>?> = context.userDataStore.data.map { preferences ->
    preferences[lastBookKey]?.let { book ->
      book to (preferences[lastChapterKey] ?: 1)
    }
  }

  suspend fun saveLocation(book: String, chapter: Int) {
    context.userDataStore.edit { preferences ->
      preferences[lastBookKey] = book
      preferences[lastChapterKey] = chapter
    }
  }

  private suspend fun updateSavedVerse(
    reference: String,
    transform: (SavedVerse) -> SavedVerse,
  ) {
    context.userDataStore.edit { preferences ->
      val current = preferences[savedDataKey]?.let { json.decodeFromString<SavedData>(it) } ?: SavedData()
      val previous = current.verses.firstOrNull { it.reference == reference } ?: SavedVerse(reference)
      val updated = transform(previous)
      val verses = current.verses.filterNot { it.reference == reference } +
        updated.takeIf { it.bookmarked || it.highlightColor != null }.let { listOfNotNull(it) }
      preferences[savedDataKey] = json.encodeToString(current.copy(verses = verses))
    }
  }

  fun loadBible(): BibleData {
    val source = context.assets.open("kjv.json").bufferedReader().use { it.readText() }
    val sourceVerses = json.decodeFromString<Map<String, String>>(source)
    val grouped = linkedMapOf<Pair<String, Int>, MutableList<Verse>>()
    val keyPattern = Regex("^(.+) (\\d+):(\\d+)$")
    sourceVerses.forEach { (key, text) ->
      val match = keyPattern.matchEntire(key)
        ?: error("Invalid KJV reference in kjv.json: $key")
      val book = match.groupValues[1]
      val chapter = match.groupValues[2].toInt()
      val number = match.groupValues[3].toInt()
      grouped.getOrPut(book to chapter) { mutableListOf() }
        .add(Verse(book, chapter, number, text))
    }
    val chapters = grouped.map { (key, verses) ->
      BibleChapter(key.first, key.second, verses.sortedBy(Verse::number))
    }
    return BibleData(chapters.map(BibleChapter::book).distinct(), chapters)
  }
}
