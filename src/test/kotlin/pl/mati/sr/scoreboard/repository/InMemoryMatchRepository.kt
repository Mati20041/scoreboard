package pl.mati.sr.scoreboard.repository

import pl.mati.sr.scoreboard.MatchId
import pl.mati.sr.scoreboard.Team
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

class InMemoryMatchRepository : MatchRepository {
    private val dataBase: MutableMap<String, MatchEntity> = mutableMapOf()
    private val counter = AtomicInteger()

    override fun findMatch(matchId: MatchId) = dataBase[matchId]

    override fun createAMatch(homeTeam: Team, awayTeam: Team): MatchEntity {
        if (getAllUnfinishedMatches().any { it.containsTeam(homeTeam) || it.containsTeam(awayTeam) }) {
            throw MatchInProgressException()
        }
        val newMatch = MatchEntity("id${counter.incrementAndGet()}", homeTeam, awayTeam)
        dataBase[newMatch.id] = newMatch
        return newMatch
    }

    override fun updateMatch(modifiedMatch: MatchEntity): MatchEntity? {
        if (!dataBase.containsKey(modifiedMatch.id)) {
            return null
        }
        dataBase[modifiedMatch.id] = modifiedMatch.copy(lastUpdated = Instant.now())
        return modifiedMatch
    }

    override fun getAllUnfinishedMatches() = dataBase.values.filterNot { it.isFinished }.toList()
    override fun deleteMatch(matchId: MatchId) = dataBase.remove(matchId)
}

fun MatchEntity.containsTeam(team: Team): Boolean {
    return this.awayTeam == team || this.homeTeam == team
}
