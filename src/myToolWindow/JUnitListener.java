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
import myToolWindow.game.Game;

public class JUnitListener extends TestStatusListener {
	private ToolWindow myToolWindow;
	private Boolean previousTestPassed = Boolean.TRUE;

	// implements IDEAJUnitListener {
	public JUnitListener() {
		System.out.println("Instance of JUnitListener");
	}

	@Override
	public void testSuiteFinished(AbstractTestProxy abstractTestProxy) {
		System.out.println("testSuiteFinished. isPassed = " + abstractTestProxy.isPassed());
		myToolWindow = ToolWindowManager.getInstance(getProject()).getToolWindow("Sample Calendar");
		MyToolWindowFactory myToolWindowFactory = getToolWindowFactory(myToolWindow);
		System.out.println("myToolWindowFactory = " + myToolWindowFactory);
		if (myToolWindowFactory != null) {
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


	private Game game;

	// assuming that the first test was green makes the role switch on the first
	// test failure.

	// private MyTestListener listener;

	// public JUnitSubscriber() {
	// listener = new MyTestListener();
	// registerTestRuns();
	// }

	public void subscribe(Game game) {
		this.game = game;
	}

	// public void unregister() {
	// JUnitCore.removeTestRunListener(listener);
	// }
	//
	// void registerTestRuns() {
	// JUnitCore.addTestRunListener(listener);
	// }

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
