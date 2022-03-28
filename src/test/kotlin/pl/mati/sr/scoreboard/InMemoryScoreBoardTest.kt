package pl.mati.sr.scoreboard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

val homeTeam = Team("HomeTeam")
val awayTeam = Team("AwayTeam")

class InMemoryScoreBoardTest : DescribeSpec({
    var scoreBoard = InMemoryScoreBoard(InMemoryMatchRepository())

    afterEach {
        scoreBoard = InMemoryScoreBoard(InMemoryMatchRepository())
    }

    describe("starting a match") {

        it("starts a match") {
            val result = scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = awayTeam)

            result.awayTeam shouldBe awayTeam
            result.homeTeam shouldBe homeTeam
            result.score shouldBe Score(0, 0)
            result.id shouldNotBe null
        }

        it("throws error if home team is the same as away team") {
            val exception = shouldThrow<DuplicateMatchException> {
                scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = homeTeam)
            }
            exception.message shouldBe "Home Team and Away Team cannot be the same"
        }

        describe("rejects starting a match with the teams that are currently playing") {
            beforeEach {
                scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = awayTeam)
            }

            it("same order") {
                val exception = shouldThrow<MatchInProgressException> {
                    scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = awayTeam)
                }
                exception.message shouldBe "There is already game between given teams"
            }

            it("opposite order") {
                val exception = shouldThrow<MatchInProgressException> {
                    scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = awayTeam)
                }
                exception.message shouldBe "There is already game between given teams"
            }
        }
    }

    describe("finishing match") {

        it("finishes a match") {
            val match = scoreBoard.startAMatch(homeTeam, awayTeam)
            scoreBoard.getSummary().shouldNotBeEmpty()

            scoreBoard.finnishMatch(match)
            scoreBoard.getSummary().shouldBeEmpty()
        }
    }

    describe("matches summary") {

        it("returns all started matches") {
            val firstMatch = scoreBoard.startAMatch(Team("1"), Team("2"))
            val secondMatch = scoreBoard.startAMatch(Team("3"), Team("4"))

            val result = scoreBoard.getSummary()

            result shouldContainExactlyInAnyOrder listOf(firstMatch, secondMatch)
        }

    }

    describe("match score") {

        it("updates match score") {
            val aMatch = scoreBoard.startAMatch(homeTeam = homeTeam, awayTeam = awayTeam)
            val newScore = Score(1, 3)

            val updatedMatch = scoreBoard.updateScore(aMatch, newScore)

            updatedMatch.score shouldBe newScore
        }
    }
})
