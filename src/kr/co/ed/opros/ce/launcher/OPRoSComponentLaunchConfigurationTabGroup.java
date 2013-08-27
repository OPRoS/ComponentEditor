package kr.co.ed.opros.ce.launcher;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class OPRoSComponentLaunchConfigurationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	public OPRoSComponentLaunchConfigurationTabGroup() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new ComponentLaunchMainTab()
		};
		
		setTabs(tabs);

	}

}
