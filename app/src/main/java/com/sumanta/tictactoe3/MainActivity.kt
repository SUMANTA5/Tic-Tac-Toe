package com.sumanta.tictactoe3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }

    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //View
        val buttonRestart = findViewById<Button>(R.id.button_restart)
        val textViewResult = findViewById<TextView>(R.id.text_view_result)
        //
        loadBoard()


        //restart button
        buttonRestart.setOnClickListener {
            board = Board()
            textViewResult.text = ""
            mapBoardToUi()
        }
    }

    //MapUi
    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        //
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        //
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        //
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    //loadBoard
    private fun loadBoard() {
        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(
                        ContextCompat.getColor(
                                this,
                                R.color.colorPrimary
                        )
                )
                boardCells[i][j]?.setOnClickListener(CallClickListener(i, j))
                findViewById<GridLayout>(R.id.layout_board).addView(boardCells[i][j])
            }
        }
    }

    //create onclickListener
    inner class CallClickListener(private val i: Int, private val j: Int
    ) : View.OnClickListener {
        override fun onClick(p0: View?) {

            if (!board.isGameOver) {
                val cell = Cell(i, j)
                //Player Move
                board.placeMove(cell, Board.PLAYER)
                //Computer Move
                if (board.availableCells.isNotEmpty()) {
                    val cCell = board.availableCells[Random.nextInt(0, board.availableCells.size)]
                    board.placeMove(cCell, Board.COMPUTER)
                }
                mapBoardToUi()
            }

            when {
                board.hasComputerWon() -> findViewById<TextView>(R.id.text_view_result).text = "Computer Won"
                board.hasPlayerWon() -> findViewById<TextView>(R.id.text_view_result).text = "Player Won"
                board.isGameOver -> findViewById<TextView>(R.id.text_view_result).text = "Game Tied"
            }

        }
    }

}