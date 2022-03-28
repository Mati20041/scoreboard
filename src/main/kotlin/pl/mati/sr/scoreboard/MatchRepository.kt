package pl.mati.sr.scoreboard

import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

data class MatchDao(
    val id: MatchId,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score = Score(0, 0),
    val lastUpdated: Instant = Instant.now(),
    val isFinished: Boolean = false,
)

interface MatchRepository {
    fun getMatch(matchId: MatchId): MatchDao?
    fun createAMatch(homeTeam: Team, awayTeam: Team): MatchDao
    fun updateMatch(modifiedMatch: MatchDao): MatchDao?
    fun getAllUnfinishedMatches(): List<MatchDao>
    fun deleteMatch(matchId: MatchId): MatchDao?
}

class InMemoryMatchRepository : MatchRepository {
    private val dataBase: MutableMap<String, MatchDao> = mutableMapOf()
    private val counter = AtomicInteger()

    override fun getMatch(matchId: MatchId) = dataBase[matchId]

    override fun createAMatch(homeTeam: Team, awayTeam: Team): MatchDao {
        if (dataBase.values.any { it.containsTeam(homeTeam) || it.containsTeam(awayTeam) }) {
            throw MatchInProgressException()
        }
        val newMatch = MatchDao("id${counter.incrementAndGet()}", homeTeam, awayTeam)
        dataBase[newMatch.id] = newMatch
        return newMatch
    }

    override fun updateMatch(modifiedMatch: MatchDao): MatchDao? {
        if (!dataBase.containsKey(modifiedMatch.id)) {
            return null
        }
        dataBase[modifiedMatch.id] = modifiedMatch
        return modifiedMatch
    }

    override fun getAllUnfinishedMatches() = dataBase.values.filterNot { it.isFinished }.toList()
    override fun deleteMatch(matchId: MatchId) = dataBase.remove(matchId)
}

fun MatchDao.containsTeam(team: Team): Boolean {
    return this.awayTeam == team || this.homeTeam == team
}
