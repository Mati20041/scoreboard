package pl.mati.sr.scoreboard.repository

import pl.mati.sr.scoreboard.MatchId
import pl.mati.sr.scoreboard.Score
import pl.mati.sr.scoreboard.Team
import java.time.Instant

data class MatchEntity(
    val id: MatchId,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score = Score(0, 0),
    val lastUpdated: Instant = Instant.now(),
    val isFinished: Boolean = false,
)

interface MatchRepository {
    fun findMatch(matchId: MatchId): MatchEntity?
    fun createAMatch(homeTeam: Team, awayTeam: Team): MatchEntity
    fun updateMatch(modifiedMatch: MatchEntity): MatchEntity?
    fun getAllUnfinishedMatches(): List<MatchEntity>
    fun deleteMatch(matchId: MatchId): MatchEntity?
}

class MatchInProgressException : IllegalArgumentException("There is already game between given teams")
