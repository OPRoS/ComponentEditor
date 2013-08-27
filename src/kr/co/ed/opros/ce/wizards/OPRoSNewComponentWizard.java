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
package kr.co.ed.opros.ce.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
//import org.opros.test.core.common.AbstractProjectManager;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "xml". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class OPRoSNewComponentWizard extends Wizard implements INewWizard {
	private OPRoSGUIWizardPage newCompWizardPage;
	public boolean isGraphicalWizardType = false;
	
	public boolean isCreateComponentSucc;
	
	public IStructuredSelection selection;
	
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		this.setWindowTitle(WizardMessages.getString("NewCompWizardPage.Title"));
		
		newCompWizardPage = new OPRoSGUIWizardPage(WizardMessages.getString("NewGraphicalCompWizardPage.Title"), selection);
		newCompWizardPage.setDescription(WizardMessages.getString("NewGraphicalCompWizardPage.Description"));
		newCompWizardPage.setTitle(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
		addPage(newCompWizardPage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try {
			progressService.runInUI(PlatformUI.getWorkbench().getProgressService(),
					new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor){
					monitor.beginTask("Creating OPRoS Project", 5);
					newCompWizardPage.prjInfo.addComponent(newCompWizardPage.compModel.getComponentName());
					monitor.worked(1);
					
					isCreateComponentSucc =  newCompWizardPage.performFinishProgress(monitor, newCompWizardPage.getProject());
					monitor.done();
				}
			}, ResourcesPlugin.getWorkspace().getRoot());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isCreateComponentSucc;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
	
	
	/*
	@Override
	public boolean canFinish() {
		if(newCompWizardPage.prjInfo.getPrjName().isEmpty()
				||newCompWizardPage.compModel.getComponentName().isEmpty()
				)
			return false;
		if(!newCompWizardPage.validatePage()){
			return false;
		}
		return true;
	}
	*/
}