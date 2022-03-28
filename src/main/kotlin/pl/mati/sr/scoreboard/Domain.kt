package pl.mati.sr.scoreboard

typealias MatchId = String

data class Team(val name: String) {
    init {
        if (name.isEmpty()) {
            throw IllegalArgumentException("name cannot be empty")
        }
    }
}

data class Score(val homeTeamScore: Int, val awayTeamScore: Int) {
    init {
        if(homeTeamScore < 0 || awayTeamScore < 0) throw NegativeScoreException()
    }
}

data class Match(
    val id: MatchId,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score,
)

interface ScoreBoard {
    fun startAMatch(homeTeam: Team, awayTeam: Team): Match
    fun updateScore(match: Match, newScore: Score): Match
    fun getSummary(): List<Match>
    fun finnishMatch(match: Match)
}

class DuplicateMatchException : IllegalArgumentException("Home Team and Away Team cannot be the same")
class MatchInProgressException : IllegalArgumentException("There is already game between given teams")
class MatchNotFound(matchId: MatchId): IllegalStateException("Match with id $matchId has not been found")
class NegativeScoreException: IllegalArgumentException("Score cannot be negative")
