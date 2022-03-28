package pl.mati.sr.scoreboard

import pl.mati.sr.scoreboard.repository.MatchDao
import pl.mati.sr.scoreboard.repository.MatchRepository

private val summaryMatchComparator = compareByDescending<MatchDao> { it.score.totalScore }.thenByDescending { it.lastUpdated }

class RepositoryScoreBoard(private val matchRepository: MatchRepository) : ScoreBoard {
    override fun startMatch(homeTeam: Team, awayTeam: Team): Match {
        if (homeTeam == awayTeam) throw DuplicateMatchException()
        return matchRepository.createAMatch(homeTeam, awayTeam).toMatch()
    }

    override fun updateMatchScore(match: Match, newScore: Score): Match {
        val foundedMatch = matchRepository.findMatch(match.id) ?: throw MatchNotFound(match.id)
        val modifiedMatch = foundedMatch.copy(score = newScore)
        val updatedMatch = matchRepository.updateMatch(modifiedMatch) ?: throw MatchNotFound(match.id)
        return updatedMatch.toMatch()
    }

    override fun getSummary(): List<Match> {
        return matchRepository
            .getAllUnfinishedMatches()
            .sortedWith(summaryMatchComparator)
            .map(MatchDao::toMatch)
    }

    override fun finishMatch(match: Match) {
        val foundMatch = matchRepository.findMatch(match.id) ?: throw MatchNotFound(match.id)
        if(foundMatch.isFinished) {
            throw MatchAlreadyFinished()
        }
        matchRepository.updateMatch(foundMatch.copy(isFinished = true))
    }
}

fun MatchDao.toMatch() = Match(id, homeTeam, awayTeam, score)
