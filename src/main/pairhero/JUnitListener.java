package pairhero;

import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.execution.testframework.TestStatusListener;

//TODO: Make this into a state machine that can prevent green test from counting when they should not.
//TODO: Add logging
public class JUnitListener extends TestStatusListener {
    private PairHeroToolWindowFactory pairHeroToolWindowFactory;
    private Boolean previousTestPassed;

    public JUnitListener() {
        pairHeroToolWindowFactory = PairHeroToolWindowFactory.getToolWindowFactory();
    }

    @Override
    public void testSuiteFinished(AbstractTestProxy abstractTestProxy) {
        if (pairHeroToolWindowFactory != null) {
            if (pairHeroToolWindowFactory.isGameOngoing()) {
                notifyTestSuiteFinished(abstractTestProxy);
            } else {
                forgetPreviousTestResult();
            }
        }
    }

    private void forgetPreviousTestResult() {
        previousTestPassed = null;
    }

    private void notifyTestSuiteFinished(
            AbstractTestProxy abstractTestProxy) {
        if (abstractTestProxy.isPassed()) {
            onTestPass();
        } else {
            onTestFailed();
        }
    }

    private void onTestPass() {
        if (previousTestPassed == Boolean.FALSE) {
            pairHeroToolWindowFactory.onGreenTest();
        }
        previousTestPassed = Boolean.TRUE;
    }

    private void onTestFailed() {
        if (previousTestPassed != Boolean.FALSE) {
            pairHeroToolWindowFactory.onSwitchRole();
        }
        previousTestPassed = Boolean.FALSE;
    }
}
