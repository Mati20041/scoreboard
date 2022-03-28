package pl.mati.sr.scoreboard

typealias MatchId = String

data class Team(val name: String) {
    init {
        if (name.isEmpty()) {
            throw IllegalArgumentException("name cannot be empty")
        }
    }
}

data class Match(
    val id: MatchId,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeTeamScore: Int = 0,
    val awayTeamScore: Int = 0
)

interface ScoreBoard {
    fun startAMatch(homeTeam: Team, awayTeam: Team): Match
}
