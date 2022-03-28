package pl.mati.sr.scoreboard

class InMemoryScoreBoard(private val matchRepository: MatchRepository) : ScoreBoard {
    override fun startAMatch(homeTeam: Team, awayTeam: Team): Match {
        if (homeTeam == awayTeam) throw DuplicateMatchException()
        return matchRepository.createAMatch(homeTeam, awayTeam)
    }

    override fun updateScore(match: Match, newScore: Score): Match {
        val foundMatch = matchRepository.getMatch(match.id) ?: throw MatchNotFound(match.id)
        val modifiedMatch = foundMatch.copy(score = newScore)
        return matchRepository.updateMatch(modifiedMatch) ?: throw MatchNotFound(match.id)
    }

    override fun getSummary(): List<Match> {
        return matchRepository.getAllActiveMatches().sortedByDescending { it.score.totalScore }
    }

    override fun finnishMatch(match: Match) {
        val foundMatch = matchRepository.getMatch(match.id) ?: throw MatchNotFound(match.id)
        matchRepository.deleteMatch(foundMatch)
    }
}

fun Match.containsTeam(team: Team): Boolean {
    return this.awayTeam == team || this.homeTeam == team
}
