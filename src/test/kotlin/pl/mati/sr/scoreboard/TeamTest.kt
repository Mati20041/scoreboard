package pl.mati.sr.scoreboard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TeamTest : DescribeSpec({

    describe("team creation") {

        it("creates successfully") {
            val team = Team("TeamName")
            team.name shouldBe "TeamName"
        }

        it("fails to create team if name is empty") {
            val exception = shouldThrow<IllegalArgumentException> {
                Team("")
            }
            exception.message shouldBe "Name cannot be empty"
        }
    }

})
