package kr.co.ed.opros.ce.ui.perspectives;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PerspectiveAdapter;

public class OPRoSPerspectiveEventHandler extends PerspectiveAdapter {

	@Override
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective,
			IWorkbenchPartReference partRef, String changeId) {
		// TODO Auto-generated method stub
		super.perspectiveChanged(page, perspective, partRef, changeId);
	}

	@Override
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub
		super.perspectiveActivated(page, perspective);
	}


	@Override
	public void perspectiveClosed(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub
		super.perspectiveClosed(page, perspective);
	}

	@Override
	public void perspectiveDeactivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub
		super.perspectiveDeactivated(page, perspective);
	}

	@Override
	public void perspectiveOpened(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub
		super.perspectiveOpened(page, perspective);
	}

	@Override
	public void perspectivePreDeactivate(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub
		super.perspectivePreDeactivate(page, perspective);
	}

	@Override
	public void perspectiveSavedAs(IWorkbenchPage page,
			IPerspectiveDescriptor oldPerspective,
			IPerspectiveDescriptor newPerspective) {
		// TODO Auto-generated method stub
		super.perspectiveSavedAs(page, oldPerspective, newPerspective);
	}

}
