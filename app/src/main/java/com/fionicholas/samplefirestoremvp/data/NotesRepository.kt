package com.fionicholas.samplefirestoremvp.data

import android.util.Log
import com.fionicholas.samplefirestoremvp.data.model.Notes
import com.fionicholas.samplefirestoremvp.utils.BaseResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class NotesRepository : NotesDataSource {

    private val notesCollection =
        FirebaseFirestore.getInstance().collection("notes")

    override fun getNotes() = flow<BaseResult<List<Notes>>> {
        emit(BaseResult.loading())

        val snapshot = notesCollection.get().await()
        val notes = snapshot.toObjects(Notes::class.java)
        emit(BaseResult.success(notes))

    }.catch {
        emit(BaseResult.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override fun addNotes(notes: Notes) = flow<BaseResult<DocumentReference>> {

        emit(BaseResult.loading())
        Log.d("testt","1")
        val notesRef = notesCollection.add(notes).await()
        Log.d("testt","2")
        emit(BaseResult.success(notesRef))
        Log.d("testt","3")
    }.catch {
        emit(BaseResult.failed(it.message.toString()))
        Log.d("testt","22")
    }.flowOn(Dispatchers.IO )
}