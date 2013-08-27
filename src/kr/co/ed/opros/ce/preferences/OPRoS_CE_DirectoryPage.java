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

package kr.co.ed.opros.ce.preferences;

import java.util.List;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.core.OPRoSProjectInfo;
import kr.co.ed.opros.ce.editors.PathEditorEx;
import kr.co.ed.opros.ce.guieditor.dialog.ProjectSelectionDialog;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class OPRoS_CE_DirectoryPage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	private boolean isChange;

	private DirectoryFieldEditor oprosLib;
	private DirectoryFieldEditor oprosInc;
	private FileFieldEditor oprosEngineFile;
	
	private PathEditor otherInc;
	private PathEditor otherLib;
	private RadioGroupFieldEditor vsCompile;
	private RadioGroupFieldEditor vsUnicode;
//	private OPRoSRemoteFileFieldEditor remoteRepository;
	private DirectoryFieldEditor oprosRepository;
	
	public OPRoS_CE_DirectoryPage(){
		super(GRID);
		setPreferenceStore(OPRoSActivator.getDefault().getPreferenceStore());
		setDescription("Compile Preference Setting Page of OPRoS Component Editor");
		this.isChange = false;
	}
	
	@Override
	protected void createFieldEditors() {
		oprosLib=new DirectoryFieldEditor(PreferenceConstants.OPROS_LIB_PATH, 
				"OPRoS Library Directory:", getFieldEditorParent());
		addField(oprosLib);
		oprosInc=new DirectoryFieldEditor(PreferenceConstants.OPROS_INC_PATH, 
				"OPRoS Include Directory:", getFieldEditorParent());
		addField(oprosInc);
		oprosEngineFile = new FileFieldEditor(PreferenceConstants.OPROS_ENGINE_FILE,
				"OPRoS Engine File :", getFieldEditorParent());
		addField(oprosEngineFile);
//		remoteRepository = new OPRoSRemoteFileFieldEditor(PreferenceConstants.OPROS_REMOTE_REPOSITORY_PATH,
//				"OPRoS Remote Repositry Path :", getFieldEditorParent());
//		addField(remoteRepository);
		oprosRepository = new DirectoryFieldEditor(PreferenceConstants.OPROS_REPOSITORY_PATH,
				"OPRoS Engine Repository:", getFieldEditorParent());
		addField(oprosRepository);
		otherInc = new PathEditor(PreferenceConstants.OTHER_INC_PATH, "Include Path :",
				"Other Include Directory:", getFieldEditorParent());
		addField(otherInc);
		
		otherLib = new PathEditorEx(PreferenceConstants.OTHER_LIB_PATH, "Library File :",
				"Other Library File:", getFieldEditorParent());
		
		addField(otherLib);
		
		
		vsCompile=new RadioGroupFieldEditor(
				PreferenceConstants.VS_COMPILE_OPTION,
			"Compile Option for Visual Studio 2010 :",
			1,
			new String[][] { { "Release", "RELEASE" }, {
				"Debug", "DEBUG" }
		}, getFieldEditorParent());
		addField(vsCompile);
		vsUnicode=new RadioGroupFieldEditor(
				PreferenceConstants.VS_UNICODE_OPTION,
				"Encoding Option for Visual Studio 2010 :",
				1,
				new String[][] { { "Unicode", "UNICODE" }, {
					"Multibyte", "MBCS" }
			}, getFieldEditorParent());
		addField(vsUnicode);
		this.setSize(new Point(200,200));
	}

	@Override
	public void init(IWorkbench arg0) {

	}

	@Override
	public boolean performOk() {
		boolean bRet = super.performOk();
		if(!isChange) {
			modifyMakefile();
			isChange = true;
		}
		return bRet; 
	}

	/**
	 * 변경된 컴파일 정보에 따라 메이크파일을 변경한다.
	 */
	private void modifyMakefile() {
		List<IProject> list = OPRoSUtil2.getOPRoSProjects();
		if(list == null || list.size() == 0) {
			return;
		}
		ProjectSelectionDialog dialog = new ProjectSelectionDialog(getShell(), list);
		dialog.open();
		if(dialog.getReturnCode() == Dialog.OK) {
			for(Object object : dialog.getSelectedItem()) {
				IProject project = (IProject) object;
				OPRoSProjectInfo info = OPRoSUtil.getProjectInfo(project);
				OPRoSUtil.createMakeFile(OPRoSUtil.getLibType(), info, project);
				
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
