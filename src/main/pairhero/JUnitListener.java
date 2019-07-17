package pairhero;

import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.execution.testframework.TestStatusListener;

import java.util.concurrent.atomic.AtomicBoolean;

//TODO: Make this into a state machine that can prevent green test from counting when they should not.
//TODO: Add logging
public class JUnitListener extends TestStatusListener {
    private PairHeroToolWindowFactory pairHeroToolWindowFactory;
    private AtomicBoolean previousTestPassed;

    public JUnitListener() {
        pairHeroToolWindowFactory = PairHeroToolWindowFactory.getToolWindowFactory();
    }

    @Override
    public void testSuiteFinished(AbstractTestProxy abstractTestProxy) {
        if (previousTestPassed == null) {
            previousTestPassed = new AtomicBoolean(true);
        }
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
        if (previousTestPassed.compareAndSet(false, true)) {
            pairHeroToolWindowFactory.onGreenTest();
        }
    }

    private void onTestFailed() {
        if (previousTestPassed.compareAndSet(true, false)) {
            pairHeroToolWindowFactory.onSwitchRole();
        }
    }
}
