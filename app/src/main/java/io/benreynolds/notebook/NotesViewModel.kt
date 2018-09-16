package io.benreynolds.notebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class NotesViewModel(private var noteDatabase: NoteDatabase) : ViewModel() {
    private val noteDao: NoteDao = noteDatabase.noteDao()
    val notes: MutableLiveData<List<Note>> = MutableLiveData()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        launch(UI) {
            val loadedNotes = mutableListOf<Note>()
            withContext(DefaultDispatcher) {

                noteDatabase.clearAllTables()

                loadedNotes.addAll(
                        arrayListOf(
                                Note(title = "Cu vix nihil affert noster, sit aperiri recusabo", body = "Mel probo putant praesent in, usu dicam prodesset ei, adhuc aperiam cum et. At his everti quaeque mediocrem, atqui aperiri equidem vis ei. Etiam similique nam id, est ei fugit imperdiet. Probo mollis vituperata mel in."),
                                Note(title = "Accumsan platonem duo in", body = "Ea hinc principes molestiae eos, esse temporibus qui ea. Nulla clita assueverit his cu. Ei case ubique urbanitas eam. Cum ex offendit honestatis, inimicus electram assueverit vim eu, in movet placerat mandamus nec. Te nam utamur sententiae, nec gubergren vituperatoribus at. An cum prompta persecuti sententiae."),
                                Note(title = "Ex adhuc aeterno praesent pro", body = "No his virtute instructior. At pro meis pertinax adipiscing. Ei nominati forensibus mel. Modus scripta sed in, ei usu vide ludus, an vis rebum mollis interesset. Mei in pertinacia abhorreant quaerendum, magna inani definiebas te sed, nam altera fabulas id. Quo et inani discere gubergren, ut mea incorrupte dissentiunt, mutat exerci iracundia vix et. Eos tale facilisis ei, quem eripuit ut quo."),
                                Note(title = "Ius scaevola cotidieque", body = "Sint graecis apeirian eos at, an mel nominati neglegentur, an deserunt postulant posidonium vix. An nec solum prompta."),
                                Note(title = "Ex feugiat menandri sed, at omnesque dissentias his", body = "Eos et exerci luptatum cotidieque. No sea regione utroque neglegentur, cu accusata scriptorem eam. Nam magna cotidieque id, quo id dicta nullam, nisl luptatum facilisis et cum. Eum semper mnesarchum disputando an, eam cu vide urbanitas, eum no melius fabulas appetere. Nam choro labores scribentur te, vis erant inimicus ad, vero imperdiet qui et. Fastidii disputationi mei ne, lorem nemore pertinax mea et, nec oratio debitis detracto te.")
                        )
                )
            }
            notes.value = loadedNotes
        }
    }
}
