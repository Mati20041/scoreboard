package pl.mati.sr.scoreboard

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class InMemoryScoreBoardTest : DescribeSpec({
    describe("InMemoryScoreBoard") {
        val inMemoryScoreBoard = InMemoryScoreBoard()

        describe("starting a match") {

            val homeTeam = Team("HomeTeam")
            val awayTeam = Team("AwayTeam")

            it("starts a match") {
                val result = inMemoryScoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = awayTeam)

                result.awayTeam shouldBe awayTeam
                result.homeTeam shouldBe homeTeam
                result.awayTeamScore shouldBe 0
                result.homeTeamScore shouldBe 0
                result.id shouldNotBe null
            }

        }
    }
})
