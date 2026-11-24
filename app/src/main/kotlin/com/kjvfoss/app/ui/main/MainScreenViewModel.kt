package com.kjvfoss.app.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import com.kjvfoss.app.data.BibleData
import com.kjvfoss.app.data.BibleRepository
import com.kjvfoss.app.data.NotesRepository
import com.kjvfoss.app.data.SavedData
import com.kjvfoss.app.data.StudyNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MainScreenUiState(
  val bible: BibleData? = null,
  val savedData: SavedData = SavedData(),
  val notes: List<StudyNote> = emptyList(),
  val lastLocation: Pair<String, Int>? = null,
  val loading: Boolean = true,
  val error: String? = null,
)

class MainScreenViewModel(context: Context) : ViewModel() {
  private val repository = BibleRepository(context.applicationContext)
  private val notesRepository = NotesRepository(context.applicationContext)
  private val mutableUiState = MutableStateFlow(MainScreenUiState())
  val uiState: StateFlow<MainScreenUiState> = mutableUiState.asStateFlow()

  init {
    viewModelScope.launch {
      try {
        val bible = withContext(Dispatchers.IO) { repository.loadBible() }
        val persistedData = combine(
          repository.savedData(),
          notesRepository.observeNotes(),
          repository.lastLocation(),
        ) { saved, notes, location ->
          Triple(saved, notes, location)
        }
        val (saved, notes, location) = persistedData.first()
        mutableUiState.value = mutableUiState.value.copy(
          bible = bible,
          savedData = saved,
          notes = notes,
          lastLocation = location,
          loading = false,
        )
        persistedData.collect { (saved, notes, location) ->
          mutableUiState.value = mutableUiState.value.copy(
            savedData = saved,
            notes = notes,
            lastLocation = location,
          )
        }
      } catch (exception: CancellationException) {
        throw exception
      } catch (exception: Exception) {
        mutableUiState.value = mutableUiState.value.copy(
          loading = false,
          error = exception.message ?: "Local data could not be read.",
        )
      }
    }
  }

  fun setBookmark(reference: String, bookmarked: Boolean) {
    viewModelScope.launch { repository.setBookmark(reference, bookmarked) }
  }

  fun setHighlight(reference: String, color: String?) {
    viewModelScope.launch { repository.setHighlight(reference, color) }
  }

  fun saveLocation(book: String, chapter: Int) {
    viewModelScope.launch { repository.saveLocation(book, chapter) }
  }

  fun saveNote(note: StudyNote) {
    viewModelScope.launch { notesRepository.save(note) }
  }

  fun deleteNote(note: StudyNote) {
    viewModelScope.launch { notesRepository.delete(note) }
  }
}
