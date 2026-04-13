package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Activity2 : AppCompatActivity() {
    private lateinit var buttonSoumettre: Button

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // on vas chercher les valeurs de joueur 1 et 2 passees en extra
        val bundle:Bundle? = intent.extras
        val joueur1 = bundle?.getString("joueur1")
        val joueur2 = bundle?.getString("joueur2")

        // on met les champs de noms pour les joueurs 1 et 2 au nom deja dabs le jeu principal par defaut
        findViewById<EditText>(R.id.editTextJoueur1).setText(joueur1)
        findViewById<EditText>(R.id.editTextJoueur2).setText(joueur2)

        // bouton pour soumettre les noms
        buttonSoumettre = findViewById(R.id.buttonSoumettre)
        buttonSoumettre.setOnClickListener(this::soumettreClick)
    }

    // fonction poue soumettre les noms
    fun soumettreClick(view: View){
        // on declare les edit text
        val editTextJoueur1 = findViewById<EditText>(R.id.editTextJoueur1)
        val editTextJoueur2 = findViewById<EditText>(R.id.editTextJoueur2)
        // on vas chercher le text dans les edit text
        val joueur1 = editTextJoueur1.text.toString()
        val joueur2 = editTextJoueur2.text.toString()

        // on envoie les valeurs au main activity en intent
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("joueur1", joueur1)
        intent.putExtra("joueur2", joueur2)
        intent.putExtra("partieActive", true)
        mainViewModel.partieActive = true
        startActivity(intent)
        finish()
    }
}