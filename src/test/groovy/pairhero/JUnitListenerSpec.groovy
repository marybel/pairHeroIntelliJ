package pairhero

import com.intellij.execution.testframework.AbstractTestProxy
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicBoolean

class JUnitListenerSpec extends Specification {
    JUnitListener jUnitListener

    def "setup"() {
        jUnitListener = new JUnitListener()
    }

    def "previous test status should be cleared after current tests finish"() {
        given: "game is stopped"
        gameIsStopped()

        when:
        jUnitListener.testSuiteFinished(Mock(AbstractTestProxy))

        then:
        jUnitListener.previousTestPassed == null
    }


    def "players should switch roles"() {
        given: "the game has just started"
        gameHasJustStarted()

        when: "a test is failing"
        jUnitListener.testSuiteFinished(atLeastOneTestIsFailing())

        then:
        1 * jUnitListener.pairHeroToolWindowFactory.onSwitchRole()
        !jUnitListener.previousTestPassed.get()
    }


    def "Given the game is just started, when all test are passing, the players should make one fail before switching roles"() {
        given:
        gameHasJustStarted()

        when:
        jUnitListener.testSuiteFinished(allTestsArePassing())

        then:
        0 * jUnitListener.pairHeroToolWindowFactory.onSwitchRole()
        jUnitListener.previousTestPassed
    }

    def "players should get points"() {
        given: "game is ongoing and all test have just been fixed"
        lastTestRunFailed()
        gameIsOnGoing()

        when:
        jUnitListener.testSuiteFinished(allTestsArePassing())

        then:
        1 * jUnitListener.pairHeroToolWindowFactory.onGreenTest()
        jUnitListener.previousTestPassed.get()
    }


    def "Given the game is ongoing and all test have been passing, the players should get no points"() {
        given:
        lastTestRunPassed()
        gameIsOnGoing()

        when:
        jUnitListener.testSuiteFinished(allTestsArePassing())

        then:
        0 * jUnitListener.pairHeroToolWindowFactory.onGreenTest()
        jUnitListener.previousTestPassed
    }

    def gameIsStopped() {
        jUnitListener.pairHeroToolWindowFactory = Mock(PairHeroToolWindowFactory) {
            isGameOngoing() >> false
        }
    }

    private void gameIsOnGoing() {
        jUnitListener.pairHeroToolWindowFactory = Mock(PairHeroToolWindowFactory) {
            isGameOngoing() >> true
        }
    }

    private void lastTestRunFailed() {
        jUnitListener.previousTestPassed = new AtomicBoolean(Boolean.FALSE)
    }

    private void lastTestRunPassed() {
        jUnitListener.previousTestPassed = new AtomicBoolean(Boolean.TRUE)
    }

    private AbstractTestProxy atLeastOneTestIsFailing() {
        Mock(AbstractTestProxy) {
            isPassed() >> false
        }
    }

    private AbstractTestProxy allTestsArePassing() {
        Mock(AbstractTestProxy) {
            isPassed() >> true
        }
    }

    private gameHasJustStarted() {
        gameIsOnGoing()
    }
}
