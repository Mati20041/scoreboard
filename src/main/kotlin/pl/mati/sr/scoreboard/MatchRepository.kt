package pl.mati.sr.scoreboard

import java.util.concurrent.atomic.AtomicInteger

interface MatchRepository {
    fun getMatch(matchId: MatchId): Match?
    fun createAMatch(homeTeam: Team, awayTeam: Team): Match
    fun updateMatch(modifiedMatch: Match): Match
    fun getAllActiveMatches(): List<Match>
}

class InMemoryMatchRepository : MatchRepository {
    private val dataBase: MutableMap<String, Match> = mutableMapOf()
    private val counter = AtomicInteger()

    override fun getMatch(matchId: MatchId) = dataBase[matchId]

    override fun createAMatch(homeTeam: Team, awayTeam: Team): Match {
        if (dataBase.values.any { it.containsTeam(homeTeam) || it.containsTeam(awayTeam) }) {
            throw MatchInProgressException()
        }
        val newMatch = Match("id${counter.incrementAndGet()}", homeTeam, awayTeam, Score(0, 0))
        dataBase[newMatch.id] = newMatch
        return newMatch
    }

    override fun updateMatch(modifiedMatch: Match): Match {
        dataBase[modifiedMatch.id] = modifiedMatch
        return modifiedMatch
    }

    override fun getAllActiveMatches(): List<Match> = dataBase.values.toList()

}
