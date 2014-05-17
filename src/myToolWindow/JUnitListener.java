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
	private ToolWindow myToolWindow;
	private ToolWindowFactory myToolWindowFactory;

	// implements IDEAJUnitListener {
	public JUnitListener() {
		System.out.println("Instance of JUnitListener");
	}

	@Override
	public void testSuiteFinished(AbstractTestProxy abstractTestProxy) {
		System.out.println("testSuiteFinished. isPassed = " + abstractTestProxy.isPassed());
		myToolWindow = ToolWindowManager.getInstance(getProject()).getToolWindow("Sample Calendar");
		myToolWindowFactory = getToolWindowFactory(myToolWindow);
		System.out.println("myToolWindowFactory = " + myToolWindowFactory);
		((MyToolWindowFactory) myToolWindowFactory).currentDateTime();
	}

	private Project getProject() {
		ProjectManager PM = ProjectManager.getInstance();
		Project[] AllProjects = PM.getOpenProjects();
		for (Project proj : AllProjects) {
			System.out.println("proj.getName() = " + proj.getName());

		}
		return AllProjects[0];
	}

	private ToolWindowFactory getToolWindowFactory(ToolWindow myToolWindow) {
		ToolWindowEP[] beans = Extensions.getExtensions(ToolWindowEP.EP_NAME);
		for (final ToolWindowEP toolWindowEP : beans) {
			ToolWindowFactory toolWindowFactory = toolWindowEP.getToolWindowFactory();
			if (toolWindowFactory instanceof MyToolWindowFactory)
				return toolWindowFactory;
		}
		return null;
	}
}
