import java.lang.NumberFormatException
import kotlin.system.exitProcess

const val PLAYER_1_MOVE = 'X'
const val PLAYER_2_MOVE = 'O'
const val INITIAL_VALUE = '-'

enum class Player {
    PLAYER1,
    PLAYER2,
    DRAW
}

var board = Array(9) { INITIAL_VALUE }

fun reset(board: Array<Char>) = board.fill(INITIAL_VALUE)
fun isSquareFull(move: Int, board: Array<Char>): Boolean = board[move - 1] != INITIAL_VALUE

fun checkWin(board: Array<Char>): Player? {
    val winningCombos = listOf(listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), listOf(0, 3, 6),
        listOf(1, 4, 7), listOf(2, 5, 8), listOf(0, 4, 8), listOf(2, 4, 6)) // a 2d list of possible winning combinations
    for ((a, b, c) in winningCombos) { // a, b, c are the values of each nested list in winningCombos, e.g. a = 0, b = 1, c = 2
        if (board[a] == board[b] && board[b] == board[c] && board[c] != INITIAL_VALUE) // if board[a] == [b] == [c] and they are not initial value
            return if (board[c] == PLAYER_1_MOVE) Player.PLAYER1 else Player.PLAYER2
    }
    return null
}

fun isBoardFull(board: Array<Char>): Boolean {
    var full = true
    board.forEach {
        if (it == INITIAL_VALUE) {
            full = false
            return@forEach
        }
    }
    return full
}

fun fillBoard(move: Int, board: Array<Char>, player: Player): Int {
    if (isSquareFull(move, board)) {
        println("That square is already full.")
        return -1
    } else board[move - 1] = if (player == Player.PLAYER1) PLAYER_1_MOVE else PLAYER_2_MOVE
    return 0
}

fun printBoard(board: Array<Char>) {
    for (i in board.indices) {
        when {
            i == 8 -> print(" ${board[i]}")
            (i + 1) in 1..8 && (i + 1) % 3 == 0 -> println(" ${board[i]}\n-----------")
            else -> print(" ${board[i]} |")
        }
    }
    println()
}

fun input(turn: Player): Int {
    var inputInt: Int
    do {
        print("${playerString(turn)}'s turn: ")
        val input = readln()
        println()
        try {
            inputInt = input.toInt()
            if (inputInt !in 1.. 9) {
                println("Please enter a number from 1 to 9.")
                continue
            } else
                break
        } catch (e: NumberFormatException) {
            when {
                input == "exit" -> {
                    println("Thanks for playing!")
                    exitProcess(0)
                }
                input == "reset" -> reset(board)
                input == "print" -> printBoard(board)
                input.toLong() > 2147483647 -> println("Please enter a number from 1 to 9.")
                else -> {
                    println("Please enter valid input.")
                    continue
                }
            }
            return -1
        }
    } while(true)
    return inputInt
}

fun playerString(player: Player): String = if (player == Player.PLAYER1) "Player 1" else "Player 2"
fun resultHandling(result: Player) {
    if (result != Player.DRAW) println("\n${playerString(result)} won the game!")
    else println("The game was a draw!")
    exitProcess(0)
}


fun main() {
    println("""
        Welcome to TicTacToe!
        Enter a number from 1 to 9 to play, increasing from left to right first, and then up to down, like this:
         [1] | [2] | [3]
        -----------------
         [4] | [5] | [6]
        -----------------
         [7] | [8] | [9]
        Type "print" to print the board, "reset" to reset it and "exit" to exit.
    """.trimIndent())
    var turn: Player = Player.PLAYER1

    while (true) {
        println("\n--------------------\n")
        if (isBoardFull(board))
            resultHandling(Player.DRAW)
        val input = input(turn)
        if (input == -1) {
            turn = Player.PLAYER1
            continue
        }
        if (fillBoard(input, board, turn) != -1) {
            printBoard(board)
            val result = checkWin(board)
            if (result != null) resultHandling(result)
            turn = if (turn == Player.PLAYER1) Player.PLAYER2 else Player.PLAYER1
        }
    }
}