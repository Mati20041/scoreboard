package pl.mati.sr.scoreboard

typealias MatchId = String

data class Match(
    val id: MatchId,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score,
)

data class Team(val name: String) {
    init {
        if (name.isEmpty()) {
            throw IllegalArgumentException("Name cannot be empty")
        }
    }
}

data class Score(val homeTeamScore: Int, val awayTeamScore: Int) {
    init {
        if(homeTeamScore < 0 || awayTeamScore < 0) {
            throw IllegalArgumentException("Score cannot be negative")
        }
    }
    val totalScore = homeTeamScore + awayTeamScore
}

interface ScoreBoard {
    fun startMatch(homeTeam: Team, awayTeam: Team): Match
    fun updateMatchScore(match: Match, newScore: Score): Match
    fun getSummary(): List<Match>
    fun finishMatch(match: Match)
}

class SameTeamInMatchException : IllegalArgumentException("Home Team and Away Team cannot be the same")
class MatchNotFound(matchId: MatchId): IllegalStateException("Match with id $matchId has not been found")
class MatchAlreadyFinished: IllegalStateException("Match is finished")
