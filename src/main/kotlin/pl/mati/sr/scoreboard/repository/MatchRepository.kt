package pl.mati.sr.scoreboard.repository

import pl.mati.sr.scoreboard.MatchId
import pl.mati.sr.scoreboard.Score
import java.time.Instant

data class TeamEntity(val teamName: String)

data class MatchEntity(
    val id: MatchId,
    val homeTeam: TeamEntity,
    val awayTeam: TeamEntity,
    val score: Score = Score(0, 0),
    val lastUpdated: Instant = Instant.now(),
    val isFinished: Boolean = false,
)

interface MatchRepository {
    fun findMatch(matchId: MatchId): MatchEntity?
    fun createAMatch(homeTeam: TeamEntity, awayTeam: TeamEntity): MatchEntity
    fun updateMatch(modifiedMatch: MatchEntity): MatchEntity?
    fun getAllUnfinishedMatches(): List<MatchEntity>
    fun deleteMatch(matchId: MatchId): MatchEntity?
}

class TeamIsAssociatedWithUnfinishedMatch: IllegalStateException()
