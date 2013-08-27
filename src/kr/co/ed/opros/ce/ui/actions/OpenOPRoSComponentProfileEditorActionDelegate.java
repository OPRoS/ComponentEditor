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
package kr.co.ed.opros.ce.ui.actions;

import kr.co.ed.opros.ce.OPRoSUtil2;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class OpenOPRoSComponentProfileEditorActionDelegate implements
		IObjectActionDelegate {
	private IFile inputFile;
	@Override
	public void setActivePart(IAction action, IWorkbenchPart part) {
	}

	@Override
	public void run(IAction action) {		
		if(inputFile==null)
			return;
		
		IWorkbenchPage page =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorInput editorInput = new FileEditorInput(inputFile);
		try {
			page.openEditor(editorInput, "kr.co.ed.opros.ce.editors.OPRoSGUIProfileEditor");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {		
		
		arg0.setEnabled(false);
    	inputFile=null;
		
		if (selection == null || selection.isEmpty()) 
			return;
		
		IResource resource = OPRoSUtil2.getOPRoSComponent(selection);
		if(resource != null) {
			arg0.setEnabled(true);
			
			String proFile=resource.getName(); 
			proFile+="/profile/";
			proFile+= resource.getName();
			proFile+=".xml";
			if(resource.getProject().getFile(proFile).isAccessible()) {
				inputFile = resource.getProject().getFile(proFile);
			}
			
		}
	}
	
	
	/*
	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
		System.out.println(selection);
		if (selection != null && !selection.isEmpty()) {
			if(selection instanceof IStructuredSelection){
				Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
				if (selectedElement instanceof IResource ) {
					IResource resource = (IResource) selectedElement;  
	                //resource = (IResource) adaptable.getAdapter(IResource.class);
	                if (resource != null && !(resource instanceof  IProject) && !(resource instanceof IFile)) {
	                	while (!(resource instanceof IFolder)) {
	                		resource = resource.getParent();
	                    }
	                	String name=resource.getName(); 
	                	name+="/profile/";
	                	name+= resource.getName();
	                	name+=".xml";
	                	IFile file = resource.getProject().getFile(name);
	                	if(!file.isAccessible()){
	                		while (resource.getType() != IResource.ROOT) {
		                		resource = resource.getParent();
		                		name=resource.getName();
		                		name+= "/profile/"; 
		                		name+=resource.getName();
		                		name+=".xml";
			                	file = resource.getProject().getFile(name);
			                	if(file == null)
			                		return;
			                	
			                	if(file.isAccessible()){
			                		inputFile=(IFile)file;
			                		break;
			                	}
		                    }
	                	}
	                	else{
	                		inputFile=file;
	                	}
	                	arg0.setEnabled(true);
	                }else{
	                	arg0.setEnabled(false);
	                	inputFile=null;
	                }
	            }
	        }
		}
	}
	*/
}
