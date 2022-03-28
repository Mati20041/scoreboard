package pl.mati.sr.scoreboard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class InMemoryScoreBoardTest : DescribeSpec({
    describe("InMemoryScoreBoard") {
        val scoreBoard = InMemoryScoreBoard()

        describe("starting a match") {

            val homeTeam = Team("HomeTeam")
            val awayTeam = Team("AwayTeam")

            it("starts a match") {
                val result = scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = awayTeam)

                result.awayTeam shouldBe awayTeam
                result.homeTeam shouldBe homeTeam
                result.awayTeamScore shouldBe 0
                result.homeTeamScore shouldBe 0
                result.id shouldNotBe null
            }

            it("throws error if home team is the same as away team") {
                val exception = shouldThrow<IllegalMatchException> {
                    scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = homeTeam)
                }
                exception.message shouldBe "Home Team and Away Team cannot be the same"
            }

        }
    }
})
