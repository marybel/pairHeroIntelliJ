package pairhero

import com.intellij.execution.testframework.AbstractTestProxy
import spock.lang.Specification

class JUnitListenerSpec extends Specification {
    JUnitListener jUnitListener = new JUnitListener();

    def "Given the game is stopped, previous test status should be cleared after current tests finish"() {
        given:
        gameIsStopped()

        when:
        jUnitListener.testSuiteFinished(Mock(AbstractTestProxy))

        then:
        jUnitListener.previousTestPassed == null
    }


    def "Given the game has just started, when a test is failing, the players should switch roles"() {
        given:
        gameHasJustStarted()

        when:
        jUnitListener.testSuiteFinished(atLeastOneTestIsFailing())

        then:
        1 * jUnitListener.pairHeroToolWindowFactory.onSwitchRole()
        !jUnitListener.previousTestPassed
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

    def "Given the game is ongoing and all test have just been fixed, the players should get points"() {
        given:
        lastTestRunFailed()
        gameIsOnGoing()

        when:
        jUnitListener.testSuiteFinished(allTestsArePassing())

        then:
        1 * jUnitListener.pairHeroToolWindowFactory.onGreenTest()
        jUnitListener.previousTestPassed
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
        jUnitListener.previousTestPassed = Boolean.FALSE
    }

    private void lastTestRunPassed() {
        jUnitListener.previousTestPassed = Boolean.TRUE
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
        jUnitListener.previousTestPassed = null
        gameIsOnGoing()
    }
}
