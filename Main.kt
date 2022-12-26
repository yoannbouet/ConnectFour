// Puissance quatre

package connectfour

const val MAX_RWS_CLMNS = 9

class Parameters {
    var firstPlayer = ""; var secondPlayer = ""
    var rows = 0; var columns = 0
    var playersTurn = ""
    var end = 0
    var gamesTotal = 0
    var gamesPlayedCounter = 0
    var firstPlayerScore = 0 ; var secondPlayerScore = 0
    var availableColumns = mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    var board: MutableList<MutableList<String>> = mutableListOf(
        mutableListOf(" 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║ ", "║"),
        mutableListOf("╚═", "╩═", "╩═", "╩═", "╩═", "╩═", "╩═", "╩═", "╩═", "╝")
    )
}

fun main() {
    // Start
    println("Connect Four")
    val game = Parameters()

    // Input player names
    println("First player's name:"); game.firstPlayer = readln()
    println("Second player's name:"); game.secondPlayer = readln()
    if (game.firstPlayer == "") game.firstPlayer = "first"
    if (game.secondPlayer == "") game.secondPlayer = "second"
    game.playersTurn = game.firstPlayer

    // Input board values
    while (game.rows !in 5..9 || game.columns !in 5..9) boardValues(game)

    // Input one or more games
    while (game.gamesTotal == 0) (singleGameOrMultiple(game))

    // Print game setup
    println(
        "${game.firstPlayer} VS ${game.secondPlayer}\n" +
                "${game.rows} X ${game.columns} board"
    )
    println(if (game.gamesTotal == 1) "Single game" else "Total ${game.gamesTotal} games\n"+
            "Game #${game.gamesPlayedCounter + 1}"
    )

    boardBuilder(game)
    while (game.gamesTotal != game.gamesPlayedCounter) {
        if (game.end == 1) break
        gameSession(game)
    }
    if (game.gamesTotal != 1) println("Score\n" +
            "${game.firstPlayer}: ${game.firstPlayerScore} ${game.secondPlayer}: ${game.secondPlayerScore}")
    println("Game over!")
}

fun singleGameOrMultiple(game: Parameters) {
    println("Do you want to play single or multiple games?\n" +
            "For a single game, input 1 or press Enter\n" +
            "Input a number of games:")
    val input = readln()
    val regex = Regex("""\d+""")
    if (input.isEmpty()) {
        game.gamesTotal = 1
        return
    } else if (!input.matches(regex) || input.toInt() == 0) {
        println("Invalid input")
        return
    } else if (input.toInt() == 1) {
        game.gamesTotal = 1
        return
    } else if (input.toInt() != 0) {
        game.gamesTotal = input.toInt()
        return
    }
}

fun gameSession(game: Parameters) {
    val regex = Regex("""\d+""")
    // Player 1's Turn
    if (game.playersTurn == game.firstPlayer) {
        println("${game.firstPlayer}'s turn:")
        val move = readln()
        if (move == "end") {
            game.end = 1
            return
        } else if (!regex.matches(move)) {
            println("Incorrect column number")
            return
        } else if (move.toInt() !in 1..game.columns) {
            println("The column number is out of range (1 - ${game.columns})")
            return
        } else if (move !in game.availableColumns) {
            println("Column $move is full")
            return
        }
        boardSession(game, symbol = "o", columns = move.toInt())
        game.playersTurn = game.secondPlayer
        return
    }
    // Player 2's Turn
    if (game.playersTurn == game.secondPlayer) {
        println("${game.secondPlayer}'s turn:")
        val move = readln()
        if (move == "end") {
            game.end = 1
            return
        } else if (!regex.matches(move)) {
            println("Incorrect column number")
            return
        } else if (move.toInt() !in 1..game.columns) {
            println("The column number is out of range (1 - ${game.columns})")
            return
        } else if (move !in game.availableColumns) {
            println("Column $move is full")
            return
        }
        boardSession(game, symbol = "*", columns = move.toInt())
        game.playersTurn = game.firstPlayer
        return
    }
}

fun boardValues(game: Parameters) {
    // Values Prompt
    println("Set the board dimensions (Rows x Columns)\n" + "Press Enter for default (6 x 7)")
    val input = readln().trim().uppercase()
    val regex = Regex("""\d+\s*[xX]\s*\d+""")
    // Invalid
    if (input.isNotEmpty() && !regex.matches(input)) {
        println("Invalid input")
        return
    }
    // Set Values
    game.rows = if (input.isNotEmpty()) input.split("X").first().trim().toInt() else 6
    game.columns = if (input.isNotEmpty()) input.split("X").last().trim().toInt() else 7
    // Out of Range
    if (game.rows !in 5..9) {
        println("Board rows should be from 5 to 9")
        return
    }
    if (game.columns !in 5..9) {
        println("Board columns should be from 5 to 9")
        return
    }
}

fun boardBuilder(game: Parameters) {
    // Board Setup
    repeat(MAX_RWS_CLMNS - game.rows) {
        game.board.removeAt(game.board.lastIndex - 1)
    }
    repeat(MAX_RWS_CLMNS - game.columns) {
        game.board.first().remove(game.board.first().last())
        game.board.last().removeAt(game.board.last().lastIndex - 1)
        for (i in 1 until game.board.lastIndex) game.board[i].removeAt(game.board[i].lastIndex - 1)
    }
    // Print
    for (i in game.board.indices) {
        println(game.board[i].joinToString(""))
    }
}

fun gameScore(game: Parameters, str: String = "") {
    game.gamesPlayedCounter++
    game.availableColumns = mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    for (str in 0 until game.rows) {
        for (list in 1 until game.board.lastIndex) {
            game.board[list][str] = "║ "
        }
    }
    if (str != "draw") {
        if (game.playersTurn == game.firstPlayer) {
            game.firstPlayerScore += 2
        } else game.secondPlayerScore += 2
    } else {
        game.firstPlayerScore++
        game.secondPlayerScore++
    }
    if (game.gamesTotal != 1 && game.gamesTotal != game.gamesPlayedCounter) {
        println("Score\n" +
                "${game.firstPlayer}: ${game.firstPlayerScore} ${game.secondPlayer}: ${game.secondPlayerScore}\n" +
                "Game #${game.gamesPlayedCounter + 1}"
        )
        for (i in game.board.indices) {
            println(game.board[i].joinToString(""))
        }
    }
    return
}

fun boardSession(game: Parameters, symbol: String = "", columns: Int = 0) {
    // Insert Coin
    for (i in game.board.lastIndex downTo 1) if (game.board[i][columns - 1] ==  "║ ") {
        game.board[i].set(columns - 1, "║$symbol")
        if (i == 1) game.availableColumns.set(columns - 1, "")
        break
    }
    // Print
    for (i in game.board.indices) {
        println(game.board[i].joinToString(""))
    }
    // Horizontal / Vertical Win
    val winningSequence = "║$symbol║$symbol║$symbol║$symbol"
    for (str in 0 until game.rows) {
        var verticalWin = ""
        for (list in 1 until game.board.lastIndex) {
            verticalWin += game.board[list][str]
            if (winningSequence in game.board[list].joinToString("") || winningSequence in verticalWin) {
                println("Player ${game.playersTurn} won")
                gameScore(game)
                return
            }
        }
    }
    // Diagonal Win #1
    for (str in 0 until game.rows - 2) {
        for (list in 1 until game.board.lastIndex - 2) {
            if (game.board[list][str] == "║$symbol" &&
                game.board[list + 1][str + 1] == "║$symbol" &&
                game.board[list + 2][str + 2] == "║$symbol" &&
                game.board[list + 3][str + 3] == "║$symbol"
            ) {
                println("Player ${game.playersTurn} won")
                gameScore(game)
                return
            }
        }
    }
    // Diagonal Win #2
    for (str in game.rows - 1 downTo 3) {
        for (list in 1 until game.board.lastIndex - 2) {
            if (game.board[list][str] == "║$symbol" &&
                game.board[list + 1][str - 1] == "║$symbol" &&
                game.board[list + 2][str - 2] == "║$symbol" &&
                game.board[list + 3][str - 3] == "║$symbol"
            ) {
                println("Player ${game.playersTurn} won")
                gameScore(game)
                return
            }
        }
    }
    // Draw
    if ("║ " !in game.board.toString()) {
        println("It is a draw")
        gameScore(game, "draw")
        return
    }
}