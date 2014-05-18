package pairhero;

import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.execution.testframework.TestStatusListener;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.wm.ToolWindowEP;
import com.intellij.openapi.wm.ToolWindowFactory;

//TODO: Make this into a state machine that can prevent green test from counting when they should not.
//TODO: Add logging
public class JUnitListener extends TestStatusListener {

	PairHeroToolWindowFactory pairHeroToolWindowFactory;
	private Boolean previousTestPassed;

	public JUnitListener() {
		pairHeroToolWindowFactory = getToolWindowFactory();
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

	private PairHeroToolWindowFactory getToolWindowFactory() {
		try {
			ToolWindowEP[] toolWindowExtensionPoints = Extensions.getExtensions(ToolWindowEP.EP_NAME);
			for (final ToolWindowEP toolWindowEP : toolWindowExtensionPoints) {
				ToolWindowFactory toolWindowFactory = toolWindowEP.getToolWindowFactory();
				if (toolWindowFactory instanceof PairHeroToolWindowFactory) {
					return (PairHeroToolWindowFactory) toolWindowFactory;
				}
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		return null;
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

	void onTestFailed() {
		if (previousTestPassed != Boolean.FALSE) {
			pairHeroToolWindowFactory.onSwitchRole();
		}
		previousTestPassed = Boolean.FALSE;
	}
}
