package pl.mati.sr.scoreboard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
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

        it("fails on finishing a finished match") {
            val match = scoreBoard.startAMatch(homeTeam, awayTeam)
            scoreBoard.finnishMatch(match)

            shouldThrow<MatchAlreadyFinished> {
                scoreBoard.finnishMatch(match)
            }
        }

        it("fails on finishing a not started match") {
            val match = Match("id", homeTeam, awayTeam, score = Score(0, 0))

            shouldThrow<MatchNotFound> {
                scoreBoard.finnishMatch(match)
            }
        }
    }

    describe("matches summary") {

        it("returns all started matches") {
            val firstMatch = scoreBoard.startAMatch(Team("1"), Team("2"))
            val secondMatch = scoreBoard.startAMatch(Team("3"), Team("4"))

            val result = scoreBoard.getSummary()

            result shouldContainExactlyInAnyOrder listOf(firstMatch, secondMatch)
        }

        describe("summary order") {

            it("returns matches sorted by the score") {
                val matches = listOf(
                    scoreBoard.startAMatch(Team("1"), Team("2")),
                    scoreBoard.startAMatch(Team("3"), Team("4")),
                    scoreBoard.startAMatch(Team("5"), Team("7")),
                )
                scoreBoard.updateScore(matches[0], Score(1, 1))
                scoreBoard.updateScore(matches[1], Score(3, 3))
                scoreBoard.updateScore(matches[2], Score(2, 2))

                val summary = scoreBoard.getSummary()

                summary.map { it.id } shouldContainExactly  listOf(matches[1].id, matches[2].id, matches[0].id)
            }

            it("returns order by last updated if score is the same ") {
                val matches = listOf(
                    scoreBoard.startAMatch(Team("1"), Team("2")),
                    scoreBoard.startAMatch(Team("3"), Team("4")),
                    scoreBoard.startAMatch(Team("5"), Team("7")),
                )
                val greatestId = scoreBoard.updateScore(matches[0], Score(10, 2)).id
                val sameScoreId = scoreBoard.updateScore(matches[1], Score(3, 5)).id
                val lastUpdatedSameScoredId = scoreBoard.updateScore(matches[2], Score(5, 3)).id

                val summary = scoreBoard.getSummary()

                summary.map { it.id } shouldContainExactly  listOf(greatestId, lastUpdatedSameScoredId, sameScoreId)
            }

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
