package com.example.tictactoe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //declaration du view model
    private val mainViewModel: MainViewModel by viewModels()
    //declaration du binding
    private lateinit var binding: ActivityMainBinding
    // declaration du tableau pour le jeu
    private lateinit var imageViewGrid: Array<Array<ImageView>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // on vas chercher les valeur passe en intent pour le nom des joueurs et les assignes aux champs respectifs
        val bundle:Bundle? = intent.extras
        mainViewModel.joueur1 = bundle?.getString("joueur1") ?: "Joueur 1"
        if(mainViewModel.joueur1 == ""){
            mainViewModel.joueur1 = "Joueur 1"
        }
        mainViewModel.joueur2 = bundle?.getString("joueur2") ?: "Joueur 2"
        if(mainViewModel.joueur2 == ""){
            mainViewModel.joueur2 = "Joueur 2"
        }

        // on vas chercher le jouer actif passe en intent pas activity gagnant
        // le joueur1 est le joueur actif par defaut
        if(!mainViewModel.partieActive)mainViewModel.joueurActif = bundle?.getInt("joueurActif")
        if (mainViewModel.joueurActif == 0){
            mainViewModel.joueurActif =1
        }

        // si la partie n'est pas active, on initialise le tableau avec des valeurs vide partour
        if(!mainViewModel.partieActive) {
            mainViewModel.grid = arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", ""),
                arrayOf("", "", "")
            )
        }

        // on vas voir si une partie active a ete passe en intent
        mainViewModel.partieActive = bundle?.getBoolean("partieActive") ?: false

        // on initialise la partie selon le joueur actif
        if(mainViewModel.partieActive){
            if(mainViewModel.joueurActif == 1) {
                mainViewModel.imageActive = R.drawable.cross
                binding.textViewJoueur1.setBackgroundColor(Color.parseColor("#00FF00"))
            }
            else{
                mainViewModel.imageActive = R.drawable.circle
                binding.textViewJoueur2.setBackgroundColor(Color.parseColor("#00FF00"))
            }
        }

        // on met les nom des joueurs aux champs respectifs
        binding.textViewJoueur1.text = mainViewModel.joueur1
        binding.textViewJoueur2.text = mainViewModel.joueur2

        // on cree un tableau pour contenir chacun des image view
        imageViewGrid =arrayOf(
        arrayOf(binding.imageViewA1, binding.imageViewA2, binding.imageViewA3),
        arrayOf(binding.imageViewB1, binding.imageViewB2, binding.imageViewB3),
        arrayOf(binding.imageViewC1, binding.imageViewC2, binding.imageViewC3)
        )

        // on met les images vide pour chaque cellule
        updateSymbol()

        //on donne un onClockListener a chaque imageView
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                imageViewGrid[i][j].setOnClickListener(this::imgClick)
            }
        }

        //on donne un on click listener au boutton start
        binding.buttonStart.setOnClickListener(this::commencerClick)
    }

    // fonction pour rechercher l<index du tableau par rapport au imageview clique
    private fun findCellPosition(view: View): Pair<Int, Int> {
        // On suppose ici que view est l'un des ImageView de la grille
        return when (view) {
            binding.imageViewA1 -> Pair(0, 0)
            binding.imageViewA2 -> Pair(0, 1)
            binding.imageViewA3 -> Pair(0, 2)
            binding.imageViewB1 -> Pair(1, 0)
            binding.imageViewB2 -> Pair(1, 1)
            binding.imageViewB3 -> Pair(1, 2)
            binding.imageViewC1 -> Pair(2, 0)
            binding.imageViewC2 -> Pair(2, 1)
            binding.imageViewC3 -> Pair(2, 2)
            else -> throw IllegalArgumentException("Unknown view clicked")
        }
    }

    // fonction declanche lorsque un imageView est clique
    private fun imgClick(view: View) {
        if (mainViewModel.partieActive) {
            // Trouve l'indice de la cellule en fonction de la vue cliquée
            val (i, j) = findCellPosition(view)

            // Utilise les indices pour accéder à la cellule dans le grid
            val cell = mainViewModel.grid[i][j]

            // Si la cellule n'est pas déjà sélectionnée et la partie est active
            if (cell.isEmpty() && mainViewModel.partieActive) {
                // Met à jour la valeur dans le tableau grid via le ViewModel
                mainViewModel.grid[i][j] = if (mainViewModel.joueurActif == 1) {
                    "x"
                } else {
                    "o"
                }
            }
            //on met le display a jour
            updateSymbol()
            // on change le joueur actif
            updateJoueurActif()
        }
    }

    // fonction pour mettre le display a jour
    fun updateSymbol(){
        // on parcour le tableau
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                // pour chaque index on change la source de l'image du imageView selon la valeur dans le tableau du viewMOdel
                if(mainViewModel.grid[i][j] == ""){
                    imageViewGrid[i][j].setImageResource(R.drawable.vide)
                }
                if(mainViewModel.grid[i][j] == "x"){
                    imageViewGrid[i][j].setImageResource(R.drawable.cross)
                }
                if(mainViewModel.grid[i][j] == "o"){
                    imageViewGrid[i][j].setImageResource(R.drawable.circle)
                }
            }
        }
    }

    // fonction pour mettre a jour le joueur actif
    fun updateJoueurActif(){
        // on verifie si un condition de victoire est remplie
        verifierVictoire()
        // on change le joueur actif et update l'interface pour monter le joueur actif
        if(mainViewModel.partieActive) {
            if (mainViewModel.joueurActif == 2) {
                mainViewModel.joueurActif = 1
                binding.textViewJoueur1.setBackgroundColor(Color.parseColor("#00FF00"))
                binding.textViewJoueur2.setBackgroundColor(Color.parseColor("#FFFFFF"))

            } else {
                mainViewModel.joueurActif = 2
                binding.textViewJoueur2.setBackgroundColor(Color.parseColor("#00FF00"))
                binding.textViewJoueur1.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }
    }

    // fonction qui verifie les conditions de victoire
    fun verifierVictoire(){
        var gagnant = false
        // verifie si une un lignet horizontal, verticale ou diagonale on tous la valeur "x" ou "o" et affiche le gagnant
        if((mainViewModel.grid[0][0] == "x" && mainViewModel.grid[0][1] == "x" && mainViewModel.grid[0][2] == "x") ||
            (mainViewModel.grid[1][0] == "x" && mainViewModel.grid[1][1] == "x" && mainViewModel.grid[1][2] == "x") ||
            (mainViewModel.grid[2][0] == "x" && mainViewModel.grid[2][1] == "x" && mainViewModel.grid[2][2] == "x") ||
            (mainViewModel.grid[0][0] == "x" && mainViewModel.grid[1][0] == "x" && mainViewModel.grid[2][0] == "x") ||
            (mainViewModel.grid[0][1] == "x" && mainViewModel.grid[1][1] == "x" && mainViewModel.grid[2][1] == "x") ||
            (mainViewModel.grid[0][2] == "x" && mainViewModel.grid[1][2] == "x" && mainViewModel.grid[2][2] == "x") ||
            (mainViewModel.grid[0][0] == "x" && mainViewModel.grid[1][1] == "x" && mainViewModel.grid[2][2] == "x") ||
            (mainViewModel.grid[0][2] == "x" && mainViewModel.grid[1][1] == "x" && mainViewModel.grid[2][0] == "x")){
            mainViewModel.partieActive = false
            gagnant = true
            afficherGagnant(mainViewModel.joueurActif)
        }
        if((mainViewModel.grid[0][0] == "o" && mainViewModel.grid[0][1] == "o" && mainViewModel.grid[0][2] == "o") ||
            (mainViewModel.grid[1][0] == "o" && mainViewModel.grid[1][1] == "o" && mainViewModel.grid[1][2] == "o") ||
            (mainViewModel.grid[2][0] == "o" && mainViewModel.grid[2][1] == "o" && mainViewModel.grid[2][2] == "o") ||
            (mainViewModel.grid[0][0] == "o" && mainViewModel.grid[1][0] == "o" && mainViewModel.grid[2][0] == "o") ||
            (mainViewModel.grid[0][1] == "o" && mainViewModel.grid[1][1] == "o" && mainViewModel.grid[2][1] == "o") ||
            (mainViewModel.grid[0][2] == "o" && mainViewModel.grid[1][2] == "o" && mainViewModel.grid[2][2] == "o") ||
            (mainViewModel.grid[0][0] == "o" && mainViewModel.grid[1][1] == "o" && mainViewModel.grid[2][2] == "o") ||
            (mainViewModel.grid[0][2] == "o" && mainViewModel.grid[1][1] == "o" && mainViewModel.grid[2][0] == "o")){
            mainViewModel.partieActive = false
            gagnant = true
            afficherGagnant(mainViewModel.joueurActif)
        }
        // verifie si toute les cases sont remplies
        var partieNulle = true
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (mainViewModel.grid[i][j] == "") {
                    partieNulle = false
                    break
                }
            }
        }
        // si toute les cases sont remplies la boucle n'est pas brisee et la variable partie nulle reste a vrai
        if (partieNulle && !gagnant) {
            // on affiche la partie nulle
            afficherPartieNulle(partieNulle)
        }
    }

    // fonction pour afficher le gagnant
    fun afficherGagnant(joueur: Int?) {
        // on assigne le jouer a ctif au gagnant
        mainViewModel.gagnant = joueur
        // on passe le gagnant et le nom des joueurs en intention a l'activite gagnant
        intent = Intent(this, ActivityGagnant::class.java)
        intent.putExtra("joueurActif", joueur)
        intent.putExtra("joueur1", mainViewModel.joueur1)
        intent.putExtra("joueur2", mainViewModel.joueur2)
        startActivity(intent)
        finish()
    }

    // fonction pour afficher la partie nulle
    fun afficherPartieNulle(partieNulle: Boolean){
        // on passe la partie nulle et le jouer actif a l'activite gagnant
        intent = Intent(this, ActivityGagnant::class.java)
        intent.putExtra("partieNulle", partieNulle)
        intent.putExtra("joueurActif", mainViewModel.joueurActif)
        intent.putExtra("joueur1", mainViewModel.joueur1)
        intent.putExtra("joueur2", mainViewModel.joueur2)
        startActivity(intent)
        finish()
    }
// fonction pour commencer la partie
    fun commencerClick(view: View){
        // on passe les valeur de joueur 1 et joueur 2 en intent a l'activity2 pour qu'elle mette ces nom par defaut
        if(!mainViewModel.partieActive) {
            intent = Intent(this, Activity2::class.java)
            intent.putExtra("joueur1", mainViewModel.joueur1)
            intent.putExtra("joueur2", mainViewModel.joueur2)
            startActivity(intent)
            finish()
        }
    }
}