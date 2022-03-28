package pl.mati.sr.scoreboard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class ScoreTest : DescribeSpec({

    describe("score creation") {
        it("creates successfully with unsigned values") {
            val score = Score(0, 0)
            score.awayTeamScore shouldBe 0
            score.homeTeamScore shouldBe 0
        }


        it("fails to create team if name is empty") {
            listOf(
                -1 to 0,
                0 to -1,
                -1 to -1
            ).forAll { (homeScore, awayScore) ->
                val exception = shouldThrow<IllegalArgumentException> {
                    Score(homeScore, awayScore)
                }
                exception.message shouldBe "Score cannot be negative"
            }
        }
    }

})
