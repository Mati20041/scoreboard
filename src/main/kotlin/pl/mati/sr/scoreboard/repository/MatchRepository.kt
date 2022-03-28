package pl.mati.sr.scoreboard.repository

import pl.mati.sr.scoreboard.MatchId
import pl.mati.sr.scoreboard.Score
import pl.mati.sr.scoreboard.Team
import java.time.Instant

data class MatchDao(
    val id: MatchId,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score = Score(0, 0),
    val lastUpdated: Instant = Instant.now(),
    val isFinished: Boolean = false,
)

interface MatchRepository {
    fun findMatch(matchId: MatchId): MatchDao?
    fun createAMatch(homeTeam: Team, awayTeam: Team): MatchDao
    fun updateMatch(modifiedMatch: MatchDao): MatchDao?
    fun getAllUnfinishedMatches(): List<MatchDao>
    fun deleteMatch(matchId: MatchId): MatchDao?
}

class MatchInProgressException : IllegalArgumentException("There is already game between given teams")
