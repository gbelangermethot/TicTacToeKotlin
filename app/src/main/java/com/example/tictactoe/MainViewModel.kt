package com.example.tictactoe

import android.media.Image
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
    //variables du main view model
        var joueurActif:Int?
            get() = savedStateHandle.get("joueurActif_cle") ?: 1
            set(value) = savedStateHandle.set("joueurActif_cle",value)
        var partieActive:Boolean
            get() = savedStateHandle.get("partieActive_cle") ?: false
            set(value) = savedStateHandle.set("partieActive_cle",value)
        var grid: Array<Array<String>>
            get() = savedStateHandle["grid_cle"] ?: Array(3) { Array(3) { "" } }
            set(value) {
                savedStateHandle["grid_cle"] = value
            }
        var joueur1:String
            get() = savedStateHandle.get("joueur1_cle") ?: "Joueur 1"
            set(value) = savedStateHandle.set("joueur1_cle",value)
        var joueur2:String
            get() = savedStateHandle.get("joueur2_cle") ?: "Joueur 2"
            set(value) = savedStateHandle.set("joueur2_cle",value)
        var imageActive:Int
            get() = savedStateHandle.get("imageActive_cle") ?: R.drawable.cross
            set(value) = savedStateHandle.set("imageActive_cle", value)
        var gagnant:Int?
            get() = savedStateHandle.get("gagnant_cle") ?: 1
            set(value) = savedStateHandle.set("gagnant_cle", value)


}
