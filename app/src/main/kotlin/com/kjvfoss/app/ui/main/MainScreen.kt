package com.kjvfoss.app.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kjvfoss.app.data.BibleChapter
import com.kjvfoss.app.data.BibleData
import com.kjvfoss.app.data.SavedData
import com.kjvfoss.app.data.SavedVerse
import com.kjvfoss.app.data.StudyNote
import com.kjvfoss.app.data.Verse

private val highlightColors = listOf(
  "#FFF0A8" to "Yellow",
  "#C9EAC2" to "Green",
  "#C7E3FF" to "Blue",
)

@Composable
fun MainScreen(
  onToggleTheme: () -> Unit = {},
  darkTheme: Boolean = false,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(context.applicationContext) }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val bible = uiState.bible
  when {
    uiState.loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Loading the KJV…")
    }
    uiState.error != null -> Column(modifier.padding(24.dp)) {
      Text("Unable to load local data.", style = MaterialTheme.typography.titleMedium)
      Text(uiState.error.orEmpty())
    }
    bible != null -> BibleHome(
      bible = bible,
      savedData = uiState.savedData,
      notes = uiState.notes,
      lastLocation = uiState.lastLocation,
      viewModel = viewModel,
      onToggleTheme = onToggleTheme,
      darkTheme = darkTheme,
      modifier = modifier,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BibleHome(
  bible: BibleData,
  savedData: SavedData,
  notes: List<StudyNote>,
  lastLocation: Pair<String, Int>? = null,
  viewModel: MainScreenViewModel,
  onToggleTheme: () -> Unit,
  darkTheme: Boolean,
  modifier: Modifier = Modifier,
) {
  val initialBook = lastLocation?.first?.takeIf(bible.books::contains) ?: bible.books.first()
  val initialChapter = lastLocation?.second
    ?.takeIf { bible.chapter(initialBook, it) != null } ?: 1
  var selectedBook by remember(bible) { mutableStateOf(initialBook) }
  var selectedChapter by remember(bible) { mutableIntStateOf(initialChapter) }
  var selectedTab by rememberSaveable { mutableIntStateOf(0) }
  var fontSize by rememberSaveable { mutableStateOf(20f) }
  var selectedVerse by remember { mutableStateOf<Verse?>(null) }
  LaunchedEffect(selectedBook, selectedChapter) {
    viewModel.saveLocation(selectedBook, selectedChapter)
  }
  val chapterList = bible.chaptersFor(selectedBook)
  val chapter = chapterList.firstOrNull { it.number == selectedChapter } ?: chapterList.first()
  val tabLabels = listOf("Read", "Saved", "Notes")
  val tabIcons = listOf(
    Icons.AutoMirrored.Rounded.MenuBook,
    Icons.Rounded.Bookmark,
    Icons.Rounded.EditNote,
  )

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          if (selectedTab == 2) {
            Text("Notes")
          } else if (selectedTab == 1) {
            Text("Saved")
          }
        },
        actions = {
          var fontMenuExpanded by remember { mutableStateOf(false) }
          Box {
            TextButton(onClick = { fontMenuExpanded = true }) { Text("Aa") }
            DropdownMenu(
              expanded = fontMenuExpanded,
              onDismissRequest = { fontMenuExpanded = false },
            ) {
              listOf(16f, 20f, 24f, 28f).forEach { size ->
                DropdownMenuItem(
                  text = { Text("${size.toInt()} sp") },
                  onClick = {
                    fontSize = size
                    fontMenuExpanded = false
                  },
                )
              }
            }
          }
          IconButton(onClick = onToggleTheme) {
            Icon(
              imageVector = if (darkTheme) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
              contentDescription = if (darkTheme) "Switch to light mode" else "Switch to dark mode",
            )
          }
        },
      )
    },
    bottomBar = {
      NavigationBar {
        tabLabels.forEachIndexed { index, label ->
          NavigationBarItem(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            icon = { Icon(tabIcons[index], contentDescription = null) },
            label = { Text(label) },
          )
        }
      }
    },
  ) { padding ->
    Column(Modifier.fillMaxSize().padding(padding)) {
      when (selectedTab) {
        0 -> {
          ReaderControls(
            bible = bible,
            book = selectedBook,
            chapter = chapter,
            onBookChange = {
              selectedBook = it
              selectedChapter = 1
            },
            onChapterChange = { selectedChapter = it },
          )
          LazyColumn(Modifier.weight(1f)) {
            items(chapter.verses, key = Verse::reference) { verse ->
              val saved = savedData.verses.firstOrNull { it.reference == verse.reference }
              VerseRow(verse, saved, fontSize) { selectedVerse = verse }
            }
            item(key = "chapter_navigation") {
              val chapterIndex = bible.chapters.indexOf(chapter)
              val previousChapter = bible.chapters.getOrNull(chapterIndex - 1)
              val nextChapter = bible.chapters.getOrNull(chapterIndex + 1)
              Row(
                Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                if (previousChapter != null) {
                  OutlinedButton(onClick = {
                    selectedBook = previousChapter.book
                    selectedChapter = previousChapter.number
                  }) {
                    Text("← ${previousChapter.reference}")
                  }
                } else {
                  Spacer(Modifier.width(1.dp))
                }
                if (nextChapter != null) {
                  OutlinedButton(onClick = {
                    selectedBook = nextChapter.book
                    selectedChapter = nextChapter.number
                  }) {
                    Text("${nextChapter.reference} →")
                  }
                }
              }
            }
          }
        }
        1 -> {
          SavedItems(
            bible = bible,
            savedData = savedData,
            onOpen = { reference ->
              val target = bible.chapters.firstOrNull { chapter ->
                chapter.verses.any { it.reference == reference }
              } ?: return@SavedItems
              selectedBook = target.book
              selectedChapter = target.number
              selectedTab = 0
            },
            onRemoveBookmark = { viewModel.setBookmark(it.reference, false) },
            onRemoveHighlight = { viewModel.setHighlight(it.reference, null) },
          )
        }
        2 -> NotesScreen(
          notes = notes,
          chapter = chapter,
          onSave = viewModel::saveNote,
          onDelete = viewModel::deleteNote,
          modifier = Modifier.weight(1f),
        )
      }
    }
  }

  selectedVerse?.let { verse ->
    VerseActionsDialog(
      verse = verse,
      saved = savedData.verses.firstOrNull { it.reference == verse.reference },
      onBookmark = { viewModel.setBookmark(verse.reference, it) },
      onHighlight = { viewModel.setHighlight(verse.reference, it) },
      onDismiss = { selectedVerse = null },
    )
  }
}

@Composable
private fun ReaderControls(
  bible: BibleData,
  book: String,
  chapter: BibleChapter,
  onBookChange: (String) -> Unit,
  onChapterChange: (Int) -> Unit,
) {
  var booksExpanded by remember { mutableStateOf(false) }
  var chaptersExpanded by remember { mutableStateOf(false) }
  Row(
    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Box(Modifier.weight(1f)) {
      TextButton(onClick = { booksExpanded = true }) { Text(book) }
      DropdownMenu(expanded = booksExpanded, onDismissRequest = { booksExpanded = false }) {
        bible.books.forEach { option ->
          DropdownMenuItem(text = { Text(option) }, onClick = {
            onBookChange(option)
            booksExpanded = false
          })
        }
      }
    }
    Box {
      TextButton(onClick = { chaptersExpanded = true }) { Text("Chapter ${chapter.number} ▾") }
      DropdownMenu(expanded = chaptersExpanded, onDismissRequest = { chaptersExpanded = false }) {
        bible.chaptersFor(book).forEach { option ->
          DropdownMenuItem(text = { Text(option.number.toString()) }, onClick = {
            onChapterChange(option.number)
            chaptersExpanded = false
          })
        }
      }
    }
  }
}

@Composable
private fun VerseRow(
  verse: Verse,
  saved: SavedVerse?,
  fontSize: Float,
  onSelect: () -> Unit,
) {
  val backgroundColor = highlightColors.firstOrNull { it.first == saved?.highlightColor }
    ?.first?.let(::highlightComposeColor) ?: Color.Transparent
  Column(
    Modifier.fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 3.dp)
      .background(backgroundColor, MaterialTheme.shapes.medium)
      .combinedClickable(onClick = onSelect, onLongClick = onSelect)
      .padding(12.dp),
  ) {
    Row(verticalAlignment = Alignment.Top) {
      Text(
        text = verse.number.toString(),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(32.dp),
      )
      Text(verse.text, fontSize = fontSize.sp, lineHeight = (fontSize * 1.48f).sp)
    }
    if (saved?.bookmarked == true) {
      Text("★ Bookmark", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(start = 32.dp, top = 4.dp))
    }
  }
}

@Composable
private fun VerseActionsDialog(
  verse: Verse,
  saved: SavedVerse?,
  onBookmark: (Boolean) -> Unit,
  onHighlight: (String?) -> Unit,
  onDismiss: () -> Unit,
) {
  val context = LocalContext.current

  fun copyToClipboard(label: String, text: String) {
    val clipboard = context.getSystemService(ClipboardManager::class.java)
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(verse.reference) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(verse.text)
        TextButton(onClick = {
          copyToClipboard("Verse", "${verse.reference} - ${verse.text}")
        }) {
          Text("Copy Verse")
        }
        TextButton(onClick = {
          copyToClipboard("Reference", verse.reference)
        }) {
          Text("Copy Reference")
        }
        TextButton(onClick = {
          val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, verse.reference)
          }
          context.startActivity(Intent.createChooser(shareIntent, "Share reference"))
        }) {
          Text("Share Reference")
        }
        Text("Highlight color", style = MaterialTheme.typography.titleSmall)
        highlightColors.forEach { (color, label) ->
          TextButton(onClick = { onHighlight(if (saved?.highlightColor == color) null else color) }) {
            Text(if (saved?.highlightColor == color) "✓ $label" else label)
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = { onBookmark(saved?.bookmarked != true) }) {
        Text(if (saved?.bookmarked == true) "Remove bookmark" else "Save bookmark")
      }
    },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
  )
}

@Composable
private fun SavedItems(
  bible: BibleData,
  savedData: SavedData,
  onOpen: (String) -> Unit,
  onRemoveBookmark: (SavedVerse) -> Unit,
  onRemoveHighlight: (SavedVerse) -> Unit,
) {
  val savedVerses = savedData.verses.filter { it.bookmarked || it.highlightColor != null }
  LazyColumn(Modifier.fillMaxSize().padding(12.dp)) {
    if (savedVerses.isEmpty()) {
      item { Text("No bookmarks or highlights yet.", modifier = Modifier.padding(12.dp)) }
    }
    items(savedVerses, key = SavedVerse::reference) { saved ->
      val verse = bible.chapters.asSequence().flatMap { it.verses.asSequence() }
        .firstOrNull { it.reference == saved.reference }
      if (verse != null) {
        Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
          Column(Modifier.padding(12.dp)) {
            TextButton(onClick = { onOpen(saved.reference) }) {
              Text(saved.reference, fontWeight = FontWeight.Bold)
            }
            Text(verse.text)
            Row {
              if (saved.bookmarked) TextButton(onClick = { onRemoveBookmark(saved) }) { Text("Remove bookmark") }
              if (saved.highlightColor != null) TextButton(onClick = { onRemoveHighlight(saved) }) { Text("Remove highlight") }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun NotesScreen(
  notes: List<StudyNote>,
  chapter: BibleChapter,
  onSave: (StudyNote) -> Unit,
  onDelete: (StudyNote) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showEditor by rememberSaveable { mutableStateOf(false) }
  var editingId by rememberSaveable { mutableStateOf<Long?>(null) }
  var title by rememberSaveable { mutableStateOf("") }
  var body by rememberSaveable { mutableStateOf("") }
  var noteReference by rememberSaveable { mutableStateOf<String?>(chapter.reference) }
  var filterToCurrentChapter by rememberSaveable { mutableStateOf(false) }
  var referenceMenuExpanded by remember { mutableStateOf(false) }
  val currentReference = chapter.reference
  val existingReference = notes.firstOrNull { it.id == editingId }?.reference
  val referenceChoices = listOf<String?>(null, currentReference, existingReference).distinct()
  val visibleNotes = if (filterToCurrentChapter) {
    notes.filter { it.book == chapter.book && it.chapter == chapter.number }
  } else {
    notes
  }

  Column(modifier.fillMaxSize().padding(16.dp)) {
    Text("Create and organize study notes, questions, or reflections.", style = MaterialTheme.typography.bodyMedium)
    Row(
      Modifier.fillMaxWidth().padding(vertical = 10.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      FilterChip(
        selected = !filterToCurrentChapter,
        onClick = { filterToCurrentChapter = false },
        label = { Text("All notes") },
      )
      FilterChip(
        selected = filterToCurrentChapter,
        onClick = { filterToCurrentChapter = true },
        label = { Text(currentReference) },
      )
      Spacer(Modifier.weight(1f))
      Button(onClick = {
        editingId = null
        title = ""
        body = ""
        noteReference = currentReference
        showEditor = true
      }) { Text("New note") }
    }

    if (showEditor) {
      Card(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(if (editingId == null) "New note" else "Edit note", style = MaterialTheme.typography.titleMedium)
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
          )
          OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Note, question, or reflection") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
          )
          Box {
            TextButton(onClick = { referenceMenuExpanded = true }) {
              Text(noteReference ?: "No chapter link")
            }
            DropdownMenu(
              expanded = referenceMenuExpanded,
              onDismissRequest = { referenceMenuExpanded = false },
            ) {
              referenceChoices.forEach { reference ->
                DropdownMenuItem(
                  text = { Text(reference ?: "No chapter link") },
                  onClick = {
                    noteReference = reference
                    referenceMenuExpanded = false
                  },
                )
              }
            }
          }
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              enabled = title.isNotBlank() && body.isNotBlank(),
              onClick = {
                val match = noteReference?.let { Regex("^(.+) (\\d+)$").matchEntire(it) }
                onSave(
                  StudyNote(
                    id = editingId ?: 0L,
                    title = title.trim(),
                    body = body.trim(),
                    book = match?.groupValues?.get(1),
                    chapter = match?.groupValues?.get(2)?.toInt(),
                  )
                )
                showEditor = false
                editingId = null
              },
            ) { Text("Save") }
            TextButton(onClick = { showEditor = false; editingId = null }) { Text("Cancel") }
          }
        }
      }
    }

    LazyColumn(Modifier.weight(1f)) {
      if (visibleNotes.isEmpty()) {
        item {
          Text(
            if (filterToCurrentChapter) "No notes for $currentReference yet." else "No notes yet.",
            modifier = Modifier.padding(vertical = 12.dp),
          )
        }
      }
      items(visibleNotes, key = StudyNote::id) { note ->
        Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
          Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(note.reference ?: "General note", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(note.title, style = MaterialTheme.typography.titleMedium)
            Text(note.body, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              TextButton(onClick = {
                editingId = note.id
                title = note.title
                body = note.body
                noteReference = note.reference
                showEditor = true
              }) { Text("Edit") }
              TextButton(onClick = { onDelete(note) }) { Text("Delete") }
            }
          }
        }
      }
    }
  }
}

private fun highlightComposeColor(hex: String): Color = when (hex) {
  "#FFF0A8" -> Color(0xFFFFF0A8)
  "#C9EAC2" -> Color(0xFFC9EAC2)
  "#C7E3FF" -> Color(0xFFC7E3FF)
  else -> Color.Transparent
}

