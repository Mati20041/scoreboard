package pl.mati.sr.scoreboard


class InMemoryScoreBoard : ScoreBoard {
    override fun startAMatch(homeTeam: Team, awayTeam: Team): Match {
        if (homeTeam == awayTeam) throw IllegalMatchException()
        return Match("id", homeTeam, awayTeam, homeTeamScore = 0, awayTeamScore = 0)
    }
}
