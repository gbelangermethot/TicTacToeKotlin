package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tictactoe.databinding.ActivityGagnantBinding
import com.example.tictactoe.databinding.ActivityMainBinding

class ActivityGagnant : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    //declaration de variables
    private lateinit var binding: ActivityGagnantBinding
    private var gagnant:Int = 0
    private var partieNulle = false
    private lateinit var joueur1:String
    private lateinit var joueur2:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGagnantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // on recherche le joue gagnant passe en intent
        val bundle:Bundle? = intent.extras
        gagnant = bundle?.getInt("joueurActif") ?: 1
        joueur1 = bundle?.getString("joueur1") ?: "Joueur 1"
        joueur2 = bundle?.getString("joueur2") ?: "Joueur 2"
        val joueurGagnant = if(gagnant == 1){
            joueur1
        }
        else{
            joueur2
        }
        // on affiche le joueur gagnant
        binding.textViewGagnant.text = joueurGagnant

        // on verifie si une partie nulle est passe en intent et affiche le message de parti nulle par dessu le message de gagnant
        partieNulle = bundle?.getBoolean("partieNulle") ?: false
        if(partieNulle){
            partieNulle = true
            affichagePartieNulle()
        }
        // boutton pour recommencer une nouvelle partie
        binding.buttonRecommencer.setOnClickListener(this::recommencerClick)
        // boutton pour retourner a l'ecran pour entrer les noms
        binding.buttonChangerNom.setOnClickListener(this::changerNomClick)
    }

    private fun affichagePartieNulle() {
        binding.textViewStill.text = "La partie est nulle"
        binding.textViewGagnant.text = "AUCUN GAGNANT"
    }

    //fonction pour afficher la partie nulle
    private fun changerNomClick(view: View?) {
        intent = Intent(this, Activity2::class.java)
        startActivity(intent)
        finish()
    }

    // fonction pour recommener une partie
    private fun recommencerClick(view: View) {
        // on envoie le gagnan en intent ou met le joueur oppose au joueur actif si la partie etait nulle pour que
        // la main activity puisse decider qui commence la partie
        if(partieNulle){
            if(gagnant == 1){
                gagnant = 2
            }
            else{
                gagnant = 1
            }
        }
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("partieActive", true)
        intent.putExtra("joueurActif", gagnant)
        intent.putExtra("joueur1", joueur1)
        intent.putExtra("joueur2", joueur2)
        startActivity(intent)
        finish()
    }
}