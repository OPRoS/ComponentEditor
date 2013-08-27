/*******************************************************************************
* Copyright (C) 2008 ED Corp., All rights reserved
*  
*  
* This programs is the production of ED Copr. research & development activity;
* you cannot use it and cannot redistribute it and cannot modify it and
* cannot  store it in any media(disk, photo or otherwise) without the prior
* permission of ED.
* 
* You should have received the license for this program to use any purpose.
* If not, contact the ED CORPORATION, 517-15, SangDaeWon-Dong, JungWon-Gu,
* SeongNam-City, GyeongGi-Do, Korea. (Zip : 462-806), http://www.ed.co.kr
* 
* NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
* IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
* PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ED Corp.
*
* @Author: sevensky(Juwon Kim), (jwkim@ed.co.kr, sevensky@mju.ac.kr)
********************************************************************************/
package kr.co.ed.opros.ce.ui.perspectives;

import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

public class OPRoSPerspectiveFactory implements IPerspectiveFactory {
	
	public static final String ID = "kr.co.ed.opros.ce.ui.OPRoSPerspective";
	
	public OPRoSPerspectiveFactory() {
		super();
	}

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
//		layout.addStandaloneView(IPageLayout.ID_OUTLINE, true, IPageLayout.RIGHT, 3f, editorArea);
		
		IFolderLayout folder1= layout.createFolder("topLeft", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
		folder1.addView(ProjectExplorer.VIEW_ID);
		folder1.addPlaceholder(CUIPlugin.CVIEW_ID);
//		folder1.addPlaceholder(IPageLayout.ID_RES_NAV);
		folder1.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		
		IFolderLayout folder2= layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.75, editorArea); //$NON-NLS-1$
		folder2.addView(IPageLayout.ID_PROBLEM_VIEW);
		folder2.addView(IPageLayout.ID_TASK_LIST);
		folder2.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		folder2.addView(IPageLayout.ID_PROP_SHEET);
		
		IFolderLayout folder3= layout.createFolder("topRight", IPageLayout.RIGHT,(float)0.75, editorArea); //$NON-NLS-1$
		folder3.addView(IPageLayout.ID_OUTLINE);
		folder3.addView("org.eclipse.cdt.make.ui.views.MakeView");

		layout.addActionSet(CUIPlugin.SEARCH_ACTION_SET_ID);
		layout.addActionSet(CUIPlugin.ID_CELEMENT_CREATION_ACTION_SET);
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addActionSet(IDebugUIConstants.DEBUG_ACTION_SET);
		layout.addActionSet("kr.co.ed.opros.ce.workbenchActionSet");
		
		// views - build console
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		
		// views - searching
		layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);
		
		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(CUIPlugin.CVIEW_ID);
//		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut("kr.co.ed.opros.ce.workbenchActionSet");
		layout.addShowViewShortcut("kr.or.opros.simulation.monitor.view");
	}
	private void addCWizardShortcuts(IPageLayout layout) {
		layout.addNewWizardShortcut("kr.co.ed.opros.ce.wizards.OPRoSNewPrjWizard");
		layout.addNewWizardShortcut("kr.co.ed.opros.ce.wizards.OPRoSNewComponentWizard");
		layout.addNewWizardShortcut("kr.co.ed.opros.ce.wizards.OPROSRSENewConnectionWizard");
	}

}
