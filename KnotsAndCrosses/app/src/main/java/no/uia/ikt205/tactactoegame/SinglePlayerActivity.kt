package no.uia.ikt205.tactactoegame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.util.Log
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState
import no.uia.ikt205.knotsandcrosses.databinding.ActivityGameBinding

class SinglePlayerActivity : AppCompatActivity(){

    private lateinit var binding: ActivityGameBinding
    var turn: Boolean = true
    private var playerSign: Char = '\u0000'
    private var opponentSign: Char = '\u0000'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var game: Game? = intent.getParcelableExtra("game")
        GameManager.joinGame("Bot", game?.gameId.toString())

        game?.let {
            loadState(it.state)
            Log.d("GameActivity", "Joined Game")
        }
        game?.let {
            loadPlayers(it)
        }
        buttonInitializer(game)

        binding.gameId.text = game?.gameId.toString()


        val mainHandler = Handler(Looper.getMainLooper())

        // Updates game
        mainHandler.post(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                Log.d("Poller", game?.gameId.toString())
                GameManager.pollGame(game?.gameId.toString()) { newGame: Game? ->
                    if (game?.players != newGame?.players && newGame != null)
                        with(binding) {
                            playersId.text = newGame.players[0] + " - X\n" + newGame.players[1] + " - O"
                        }

                    if (game?.state != newGame?.state && newGame != null) {
                        game = newGame
                        game?.let {
                            loadState(it.state)
                        }
                        buttonInitializer(game)
                        turn = true
                        game?.let {
                            checkWin(it.state)
                        }
                    }

                }
                mainHandler.postDelayed(this, 5000)
            }
        })
    }

    private fun buttonInitializer(game: Game?) {
        binding.nullNull.setOnClickListener {
            makeMove(game, 0, 0)
        }
        binding.nullOne.setOnClickListener {
            makeMove(game, 0, 1)
        }
        binding.nullTwo.setOnClickListener {
            makeMove(game, 0, 2)
        }
        binding.oneNull.setOnClickListener {
            makeMove(game, 1, 0)
        }
        binding.oneOne.setOnClickListener {
            makeMove(game, 1, 1)
        }
        binding.oneTwo.setOnClickListener {
            makeMove(game, 1, 2)
        }
        binding.twoNull.setOnClickListener {
            makeMove(game, 2, 0)
        }
        binding.twoOne.setOnClickListener {
            makeMove(game, 2, 1)
        }
        binding.twoTwo.setOnClickListener {
            makeMove(game, 2, 2)
        }
    }

    private fun makeMove(game: Game?, row: Int, column: Int) {
        if (turn) {
            if (game != null) {
                if (game.state[row][column] == '0') {
                    game.state[row][column] = playerSign
                    game.state.let {

                    }
                    turn = false
                }
            }

        }
        game?.let { loadState(it.state) }
        game?.let { checkWin(it.state) }
    }

    fun loadState(state: GameState) {
        // 1st row
        binding.nullNull.text = convertToChar(state[0][0])
        binding.nullOne.text = convertToChar(state[0][1])
        binding.nullTwo.text = convertToChar(state[0][2])
        // 2nd row
        binding.oneNull.text = convertToChar(state[1][0])
        binding.oneOne.text = convertToChar(state[1][1])
        binding.oneTwo.text = convertToChar(state[1][2])
        // 3rd row
        binding.twoNull.text = convertToChar(state[2][0])
        binding.twoOne.text = convertToChar(state[2][1])
        binding.twoTwo.text = convertToChar(state[2][2])
    }

    @SuppressLint("SetTextI18n")
    fun loadPlayers(game: Game) {
        if (game.players.size == 1) {
            playerSign = 'X'
            opponentSign = '0'
            binding.playersId.text = game.players[0] + " - X"
        } else {
            playerSign = 'O'
            opponentSign = 'X'
            with(binding) {
                playersId.text = game.players[0] + " - X\n" + game.players[1] + " - O"
            }
            turn = true
        }
    }

    fun checkWin(state: GameState) {
        // Checks rows x-axis
        if((state[0][0] == 'O') && (state[0][1] == 'O') && (state[0][2] == 'O'))
            winner("O won!")
        else if((state[1][0] == 'O') && (state[1][1] == 'O') && (state[1][2] == 'O'))
            winner("O won!")
        else if((state[2][0] == 'O') && (state[2][1] == 'O') && (state[2][2] == 'O'))
            winner("O won!")
        else if((state[0][0] == 'X') && (state[0][1] == 'X') && (state[0][2] == 'X'))
            winner("X won!")
        else if((state[1][0] == 'X') && (state[1][1] == 'X') && (state[1][2] == 'X'))
            winner("X won!")
        else if((state[2][0] == 'X') && (state[2][1] == 'X') && (state[2][2] == 'X'))
            winner("X won!")
        // Checks rows y-axis
        else if((state[0][0] == 'O') && (state[1][0] == 'O') && (state[2][0] == 'O'))
            winner("O won!")
        else if((state[0][1] == 'O') && (state[1][1] == 'O') && (state[2][1] == 'O'))
            winner("O won!")
        else if((state[0][2] == 'O') && (state[1][2] == 'O') && (state[2][2] == 'O'))
            winner("O won!")
        else if((state[0][0] == 'X') && (state[1][0] == 'X') && (state[2][0] == 'X'))
            winner("X won!")
        else if((state[0][1] == 'X') && (state[1][1] == 'X') && (state[2][1] == 'X'))
            winner("X won!")
        else if((state[0][2] == 'X') && (state[1][2] == 'X') && (state[2][2] == 'X'))
            winner("X won!")
        // Checks diagonally
        else if((state[0][0] == 'O') && (state[1][1] == 'O') && (state[2][2] == 'O'))
            winner("O won!")
        else if((state[0][2] == 'O') && (state[1][1] == 'O') && (state[2][0] == 'O'))
            winner("O won!")
        else if((state[0][0] == 'X') && (state[1][1] == 'X') && (state[2][2] == 'X'))
            winner("X won!")
        else if((state[0][2] == 'X') && (state[1][1] == 'X') && (state[2][0] == 'X'))
            winner("X won!")
        // Checks for draw
        else if((state[0][0] != '0') && (state[0][1] != '0') && (state[0][2] != '0') &&
            (state[1][0] != '0') && (state[1][1] != '0') && (state[1][2] != '0') &&
            (state[2][0] != '0') && (state[2][1] != '0') && (state[2][2] != '0'))
            winner("Draw!")


    }

    private fun winner(winCondition:String) {
        turn=false
        val intent = Intent(this, WinActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, winCondition)
        }
        startActivity(intent)
    }

    private fun convertToChar(c: Char): String {
        if(c == '0')
            return " "
        else
            return c.toString()
    }

}