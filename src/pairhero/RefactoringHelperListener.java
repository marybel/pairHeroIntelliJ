package pairhero;

import com.intellij.openapi.project.Project;
import com.intellij.usageView.UsageInfo;

public class RefactoringHelperListener implements com.intellij.refactoring.RefactoringHelper {
	private PairHeroToolWindowFactory pairHeroToolWindowFactory;

	public RefactoringHelperListener() {
		pairHeroToolWindowFactory = PairHeroToolWindowFactory.getToolWindowFactory();
	}

	@Override
	public Object prepareOperation(UsageInfo[] usageInfos) {
		return null;
	}

	@Override
	public void performOperation(Project project, Object operationData) {
		if (pairHeroToolWindowFactory != null && pairHeroToolWindowFactory.isGameOngoing()) {
			pairHeroToolWindowFactory.onRefactoring();
		}
	}
}
