package com.kjvfoss.app.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "study_notes")
data class StudyNote(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val body: String,
  val book: String? = null,
  val chapter: Int? = null,
  val updatedAt: Long = System.currentTimeMillis(),
) {
  val reference: String?
    get() = if (book != null && chapter != null) "$book $chapter" else null
}

@Dao
interface NotesDao {
  @Query("SELECT * FROM study_notes ORDER BY updatedAt DESC")
  fun observeAll(): Flow<List<StudyNote>>

  @Insert
  suspend fun insert(note: StudyNote): Long

  @Update
  suspend fun update(note: StudyNote)

  @Delete
  suspend fun delete(note: StudyNote)
}

@Database(entities = [StudyNote::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
  abstract fun notesDao(): NotesDao
}

class NotesRepository(context: Context) {
  private val dao = Room.databaseBuilder(
    context.applicationContext,
    NotesDatabase::class.java,
    "kjvfoss_notes.db",
  ).build().notesDao()

  fun observeNotes(): Flow<List<StudyNote>> = dao.observeAll()

  suspend fun save(note: StudyNote) {
    val updated = note.copy(updatedAt = System.currentTimeMillis())
    if (note.id == 0L) dao.insert(updated) else dao.update(updated)
  }

  suspend fun delete(note: StudyNote) {
    dao.delete(note)
  }
}
