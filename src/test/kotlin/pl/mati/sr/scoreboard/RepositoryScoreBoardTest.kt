package pl.mati.sr.scoreboard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import pl.mati.sr.scoreboard.repository.InMemoryMatchRepository

val homeTeam = Team("HomeTeam")
val awayTeam = Team("AwayTeam")

class RepositoryScoreBoardTest : DescribeSpec({
    var scoreBoard = RepositoryScoreBoard(InMemoryMatchRepository())

    afterEach {
        scoreBoard = RepositoryScoreBoard(InMemoryMatchRepository())
    }

    describe("starting a match") {

        it("starts a match") {
            val result = scoreBoard.startMatch(homeTeam, awayTeam)

            result.awayTeam shouldBe awayTeam
            result.homeTeam shouldBe homeTeam
            result.score shouldBe Score(0, 0)
            result.id shouldNotBe null
        }

        it("throws error if home team is the same as away team") {
            val exception = shouldThrow<DuplicateMatchException> {
                scoreBoard.startMatch(homeTeam, homeTeam)
            }
            exception.message shouldBe "Home Team and Away Team cannot be the same"
        }

        describe("rejects starting a match with the teams that are currently playing") {
            beforeEach {
                scoreBoard.startMatch(homeTeam, awayTeam)
            }

            it("same order") {
                val exception = shouldThrow<MatchInProgressException> {
                    scoreBoard.startMatch(homeTeam, awayTeam)
                }
                exception.message shouldBe "There is already game between given teams"
            }

            it("opposite order") {
                val exception = shouldThrow<MatchInProgressException> {
                    scoreBoard.startMatch(homeTeam, awayTeam)
                }
                exception.message shouldBe "There is already game between given teams"
            }
        }
    }

    describe("finishing match") {

        it("finishes a match") {
            val match = scoreBoard.startMatch(homeTeam, awayTeam)
            scoreBoard.getSummary().shouldNotBeEmpty()

            scoreBoard.finnishMatch(match)
            scoreBoard.getSummary().shouldBeEmpty()
        }

        it("fails on finishing a finished match") {
            val match = scoreBoard.startMatch(homeTeam, awayTeam)
            scoreBoard.finnishMatch(match)

            shouldThrow<MatchAlreadyFinished> {
                scoreBoard.finnishMatch(match)
            }
        }

        it("fails on finishing a not started match") {
            val match = Match("id", homeTeam, awayTeam, Score(0, 0))

            shouldThrow<MatchNotFound> {
                scoreBoard.finnishMatch(match)
            }
        }
    }

    describe("matches summary") {

        it("returns all started matches") {
            val firstMatch = scoreBoard.startMatch(Team("1"), Team("2"))
            val secondMatch = scoreBoard.startMatch(Team("3"), Team("4"))

            val result = scoreBoard.getSummary()

            result shouldContainExactlyInAnyOrder listOf(firstMatch, secondMatch)
        }

        describe("summary order") {

            it("returns matches sorted by the score") {
                val matches = listOf(
                    scoreBoard.startMatch(Team("1"), Team("2")),
                    scoreBoard.startMatch(Team("3"), Team("4")),
                    scoreBoard.startMatch(Team("5"), Team("7")),
                )
                scoreBoard.updateMatchScore(matches[0], Score(1, 1))
                scoreBoard.updateMatchScore(matches[1], Score(3, 3))
                scoreBoard.updateMatchScore(matches[2], Score(2, 2))

                val summary = scoreBoard.getSummary()

                summary.map { it.id } shouldContainExactly listOf(matches[1].id, matches[2].id, matches[0].id)
            }

            it("returns order by last updated if score is the same ") {
                val matches = listOf(
                    scoreBoard.startMatch(Team("1"), Team("2")),
                    scoreBoard.startMatch(Team("3"), Team("4")),
                    scoreBoard.startMatch(Team("5"), Team("7")),
                )
                val greatestId = scoreBoard.updateMatchScore(matches[0], Score(10, 2)).id
                val sameScoreId = scoreBoard.updateMatchScore(matches[1], Score(3, 5)).id
                val lastUpdatedSameScoredId = scoreBoard.updateMatchScore(matches[2], Score(5, 3)).id

                val summary = scoreBoard.getSummary()

                summary.map { it.id } shouldContainExactly listOf(greatestId, lastUpdatedSameScoredId, sameScoreId)
            }

        }
    }

    describe("match score") {

        it("updates match score") {
            val aMatch = scoreBoard.startMatch(homeTeam, awayTeam)
            val newScore = Score(1, 3)

            val updatedMatch = scoreBoard.updateMatchScore(aMatch, newScore)

            updatedMatch.score shouldBe newScore
        }
    }
})
