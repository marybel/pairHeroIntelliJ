package myToolWindow;

import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.execution.testframework.TestStatusListener;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowEP;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;

public class JUnitListener extends TestStatusListener {
	public static final String TOOL_WINDOW_ID = "PairHero";

	private ToolWindow myToolWindow;
	private Boolean previousTestPassed = Boolean.TRUE;


	@Override
	public void testSuiteFinished(AbstractTestProxy abstractTestProxy) {
		myToolWindow = ToolWindowManager.getInstance(getProject()).getToolWindow(TOOL_WINDOW_ID);
		MyToolWindowFactory myToolWindowFactory = getToolWindowFactory(myToolWindow);

		if (myToolWindowFactory != null && myToolWindowFactory.isGameOngoing()) {
			notifyPluginTestSuiteFinished(myToolWindowFactory, abstractTestProxy);
		}

	}

	private void notifyPluginTestSuiteFinished(MyToolWindowFactory myToolWindowFactory,
			AbstractTestProxy abstractTestProxy) {
		if (abstractTestProxy.isPassed()) {
			onTestPass(myToolWindowFactory);
		} else {
			onTestFailed(myToolWindowFactory);
		}

	}

	private Project getProject() {
		ProjectManager PM = ProjectManager.getInstance();
		Project[] AllProjects = PM.getOpenProjects();
		for (Project proj : AllProjects) {
			System.out.println("proj.getName() = " + proj.getName());
		}
		return AllProjects[0];
	}

	private MyToolWindowFactory getToolWindowFactory(ToolWindow myToolWindow) {
		ToolWindowEP[] beans = Extensions.getExtensions(ToolWindowEP.EP_NAME);
		for (final ToolWindowEP toolWindowEP : beans) {
			ToolWindowFactory toolWindowFactory = toolWindowEP.getToolWindowFactory();
			if (toolWindowFactory instanceof MyToolWindowFactory)
				return (MyToolWindowFactory) toolWindowFactory;
		}
		return null;
	}

	void onTestPass(MyToolWindowFactory myToolWindowFactory) {
		previousTestPassed = Boolean.TRUE;
		myToolWindowFactory.onGreenTest();

	}

	void onTestFailed(MyToolWindowFactory myToolWindowFactory) {
		if (previousTestPassed == Boolean.TRUE) {
			myToolWindowFactory.onSwitchRole();

		}
		previousTestPassed = Boolean.FALSE;
	}
}
