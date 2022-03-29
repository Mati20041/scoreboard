package pl.mati.sr.scoreboard

import pl.mati.sr.scoreboard.repository.MatchEntity
import pl.mati.sr.scoreboard.repository.MatchRepository
import pl.mati.sr.scoreboard.repository.TeamEntity

private val summaryMatchComparator =
    compareByDescending<MatchEntity> { it.score.totalScore }.thenByDescending { it.lastUpdated }

class RepositoryScoreBoard(private val matchRepository: MatchRepository) : ScoreBoard {
    override fun startMatch(homeTeam: Team, awayTeam: Team): Match {
        if (homeTeam == awayTeam) throw SameTeamInMatchException()
        return matchRepository.createAMatch(homeTeam.toEntity(), awayTeam.toEntity()).toMatch()
    }

    override fun updateMatchScore(match: Match, newScore: Score): Match {
        val foundedMatch = matchRepository.findMatch(match.id) ?: throw MatchNotFound(match.id)
        if (foundedMatch.isFinished) {
            throw MatchAlreadyFinished()
        }
        val modifiedMatch = foundedMatch.copy(score = newScore)
        val updatedMatch = matchRepository.updateMatch(modifiedMatch) ?: throw MatchNotFound(match.id)
        return updatedMatch.toMatch()
    }

    override fun getSummary(): List<Match> = matchRepository
        .getAllUnfinishedMatches()
        .sortedWith(summaryMatchComparator)
        .map(MatchEntity::toMatch)

    override fun finishMatch(match: Match) {
        val foundMatch = matchRepository.findMatch(match.id) ?: throw MatchNotFound(match.id)
        if (foundMatch.isFinished) {
            throw MatchAlreadyFinished()
        }
        matchRepository.updateMatch(foundMatch.copy(isFinished = true))
    }
}

fun MatchEntity.toMatch() = Match(id, homeTeam.toTeam(), awayTeam.toTeam(), score)
fun TeamEntity.toTeam() = Team(teamName)
fun Team.toEntity() = TeamEntity(name)
