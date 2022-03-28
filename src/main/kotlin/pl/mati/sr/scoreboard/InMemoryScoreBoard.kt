package pl.mati.sr.scoreboard

class InMemoryScoreBoard(private val matchRepository: MatchRepository) : ScoreBoard {

    private val summaryMatchComparator = compareByDescending<MatchDao> { it.score.totalScore }.thenByDescending { it.lastUpdated }

    override fun startAMatch(homeTeam: Team, awayTeam: Team): Match {
        if (homeTeam == awayTeam) throw DuplicateMatchException()
        return matchRepository.createAMatch(homeTeam, awayTeam).toMatch()
    }

    override fun updateScore(match: Match, newScore: Score): Match {
        val foundMatch = matchRepository.getMatch(match.id) ?: throw MatchNotFound(match.id)
        val modifiedMatch = foundMatch.copy(score = newScore)
        val updatedMatch = matchRepository.updateMatch(modifiedMatch) ?: throw MatchNotFound(match.id)
        return updatedMatch.toMatch()
    }

    override fun getSummary(): List<Match> {
        return matchRepository
            .getAllUnfinishedMatches()
            .sortedWith(summaryMatchComparator)
            .map(MatchDao::toMatch)
    }

    override fun finnishMatch(match: Match) {
        val foundMatch = matchRepository.getMatch(match.id) ?: throw MatchNotFound(match.id)
        if(foundMatch.isFinished) {
            throw MatchAlreadyFinished()
        }
        matchRepository.updateMatch(foundMatch.copy(isFinished = true))
    }
}

fun MatchDao.toMatch() = Match(id, homeTeam, awayTeam, score)
