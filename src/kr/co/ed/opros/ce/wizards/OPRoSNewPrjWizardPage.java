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

import java.net.URI;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.core.OPRoSProjectInfo;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentElementModel;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;

@SuppressWarnings("restriction")
public class OPRoSNewPrjWizardPage extends WizardPage {
	protected Combo langCombo;
	protected Combo wizardTypeCombo;
	protected Text projectNameField;
	protected OPRoSComponentElementModel compModel;
	protected ProjectContentsLocationArea locationArea;
	protected String defaultProjectName = WizardMessages.getString("NewPjtWizardPage.DefaultPjtName");
	private static String[] targetLanguages = null;
	
	private OPRoSProjectInfo prjInfo;
	
	protected OPRoSNewPrjWizardPage(String pageName,OPRoSComponentElementModel compModel,OPRoSProjectInfo prjInfo) {
		super(pageName);
		this.compModel=compModel;
		this.prjInfo = prjInfo;
		
		if(OPRoSUtil.isWindowPlatform()) {
			targetLanguages = new String[]{
					WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Combo0"),
					WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo1"),
					WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo3")};
		} else {
			targetLanguages = new String[]{
					WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Combo0")};
		}
	}
	@Override
	public IWizardPage getPreviousPage() {
		//Proejct페이지에서 처음 프로젝트 선택페이지로 못넘어가게 함.
		//ProjectWizard의 WizardType선택시 getNextPage()메소드가 2호출됨에 따라 0/1로 구분해놓았으나
		//이페이지에서 뒤로 가버리면 로직이 꼬임.
		return null;
	}
	@Override
	public void createControl(Composite parent) {
		Composite composite = OPRoSUtil.createComposite(parent, SWT.NULL, 1, GridData.FILL_BOTH);

        initializeDialogUnits(parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
                IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE);

        Group gr = createProjectNameGroup(composite);
        locationArea = new ProjectContentsLocationArea(getErrorReporter(), gr);
        if(defaultProjectName != null) {
			locationArea.updateProjectName(defaultProjectName);
		}
		setButtonLayoutData(locationArea.getBrowseButton());
		//createProjectSettingGroup(composite);
		createSelectLang(composite);
		
        setControl(composite);
	}
	
	/*
	private void createProjectSettingGroup(Composite container) {
		Group group = OPRoSUtil.createGroup(container, SWT.NONE, WizardMessages.getString("NewPjtWizardPage.ProjectSettingGroup.Title"),
				2, GridData.FILL_HORIZONTAL);
		OPRoSUtil.createLabel(group, WizardMessages.getString("NewPjtWizardPage.ProjectSettingGroup.SourceLabelName"),
				SWT.NONE,1,30,1,0,GridData.BEGINNING); 
		sourceText = OPRoSUtil.createText(group,SWT.BORDER|SWT.SINGLE,1,0,1,0,200,0,GridData.FILL_HORIZONTAL);	
		sourceText.setText(WizardMessages.getString("NewPjtWizardPage.ProjectSettingGroup.SourceText"));//store.getString(PreferenceConstants.SRCBIN_SRCNAME));
		sourceText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				prjInfo.setSrcFolder(getSourceFolder());
				
			}
    	});
		
		OPRoSUtil.createLabel(group, WizardMessages.getString("NewPjtWizardPage.ProjectSettingGroup.OutputLabelName"),
				SWT.NONE,1,30,1,0,GridData.BEGINNING); 
		outputText = OPRoSUtil.createText(group,SWT.BORDER|SWT.SINGLE,1,0,1,0,200,0,GridData.FILL_HORIZONTAL);	
		outputText.setText(WizardMessages.getString("NewPjtWizardPage.ProjectSettingGroup.OutputText"));
		outputText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				prjInfo.setBinFolder(getOutputFolder());
			}
    	});
	}
	*/
	private IErrorMessageReporter getErrorReporter() {
		return new IErrorMessageReporter(){
			@SuppressWarnings("unused")
			public void reportError(String errorMessage) {
				setErrorMessage(errorMessage);
				boolean valid = errorMessage == null;
				if(valid) {
					valid = validatePage();
				}
				
				setPageComplete(valid);
			}

			@Override
			public void reportError(String errorMessage, boolean infoOnly) {
				
			}
		};
	}
	protected boolean validatePage() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

        String projectFieldContents = getProjectName();
        if (projectFieldContents.equals("")) { //$NON-NLS-1$
            setErrorMessage(null);
            setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
            return false;
        }

        IStatus nameStatus = workspace.validateName(projectFieldContents,
                IResource.PROJECT);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }

        IProject handle = getProjectHandle();
        if (handle.exists()) {
            setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
            return false;
        }
        
        if (!locationArea.isDefault()) {
        	String validLocationMessage = locationArea.checkValidLocation();
        	if (validLocationMessage != null) { //there is no destination location given
        		setErrorMessage(validLocationMessage);
        		return false;
        	}
        	setErrorMessage("OPRoS Prject : Only use default location");
        	return false;
        }
        setErrorMessage(null);
        setMessage(null);
        return true;
    }
	public IProject getProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
    }
	public String getProjectName() {
	    if (projectNameField == null) {
	    	return defaultProjectName;
		}
	    else if(projectNameField.getText().isEmpty()){
	    }
	    return projectNameField.getText().trim();
	}
	private final Group createProjectNameGroup(Composite parent) {
		//Group projectGroup = OPRoSUtil.createGroup(parent, SWT.NONE, WizardMessages.getString("NewPjtWizardPage.ProjectNameGroup.Title")
		//		, 1, GridData.FILL_HORIZONTAL);
		Group projectGroup = new Group(parent, SWT.NONE);
		projectGroup.setText(WizardMessages.getString("NewPjtWizardPage.ProjectNameGroup.Title"));
		projectGroup.setLayout(new GridLayout());
		projectGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite projectComposite = OPRoSUtil.createComposite(projectGroup, SWT.NONE, 2, GridData.FILL_HORIZONTAL);
        OPRoSUtil.createLabel(projectComposite, IDEWorkbenchMessages.WizardNewProjectCreationPage_nameLabel,
        		SWT.NONE,1, 0, 1, 0,GridData.BEGINNING);
        // new project name entry field
        projectNameField = OPRoSUtil.createText(projectComposite, SWT.BORDER, 1, 0, 1, 0, 200, 0,GridData.FILL_HORIZONTAL);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (defaultProjectName != null) {
			projectNameField.setText(defaultProjectName);
		}
        projectNameField.addModifyListener(nameModifyListener);
        return projectGroup;
    }
	private ModifyListener nameModifyListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			prjInfo.setPrjName(getProjectName());
			setLocationForSelection();
            boolean valid = validatePage();
//            mustNotSelectDialog = true;
            setPageComplete(valid);//finish 활성화
//            mustNotSelectDialog = false;
		}
    };
        
	@Override
	public IWizardPage getNextPage() {
		if(!validatePage()){
			return this;
		}
		prjInfo.setPrjName(getProjectName());
//		if(getWizardType().compareTo(wizardTypes[0])==0){
//			((OPRoSNewPrjWizard)getWizard()).isGraphicalWizardType=true;
//			OPRoSGUIWizardPage page = new OPRoSGUIWizardPage(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
//			page.setDescription(WizardMessages.getString("NewGraphicalCompWizardPage.Description"));
//			page.setTitle(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
//			((OPRoSNewPrjWizard)getWizard()).addPage(page);
//			prjInfo.setLocation(getLocationPath().toString());
//			return page;
//		}else{
			prjInfo.setLocation(getLocationPath().toString());
			return super.getNextPage();
//		}
//    	if(!mustNotSelectDialog){
//    		mustNotSelectDialog=false;
//			OPRoSWizardTypeSelectDialog dlg = new OPRoSWizardTypeSelectDialog(getShell());
//			int ret = dlg.open();
//			if(ret==Dialog.OK){
//				if(dlg.getSelectedWizardType().compareTo(OPRoSWizardTypeSelectDialog.wizardTypes[0])==0){
//					prjInfo.setLocation(getLocationPath().toString());
//					return super.getNextPage();
//				}
//				else if(dlg.getSelectedWizardType().compareTo(OPRoSWizardTypeSelectDialog.wizardTypes[1])==0){
//					((OPRoSNewPrjWizard)getWizard()).isGraphicalWizardType=true;
//					OPRoSGUIWizardPage page = new OPRoSGUIWizardPage(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
//					page.setDescription(WizardMessages.getString("NewGraphicalCompWizardPage.Description"));
//					page.setTitle(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
//					((OPRoSNewPrjWizard)getWizard()).addPage(page);
////					OPRoSNewGraphicalComponentWizardPage newGraphicalCompWizardPage = new OPRoSNewGraphicalComponentWizardPage(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
////					newGraphicalCompWizardPage.setDescription(WizardMessages.getString("NewGraphicalCompWizardPage.Description"));
////					newGraphicalCompWizardPage.setTitle(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
////
////					((OPRoSNewPrjWizard)getWizard()).addPage(newGraphicalCompWizardPage);
////					prjInfo.setLocation(getLocationPath().toString());
////					if(newGraphicalCompWizardPage==null)
////						OPRoSUtil.openMessageBox("Null newGraphicalWizardPage", SWT.OK);
////					if((OPRoSNewPrjWizard)getWizard()==null)
////						OPRoSUtil.openMessageBox("Null MainWizard", SWT.OK);
////					return newGraphicalCompWizardPage;
//					prjInfo.setLocation(getLocationPath().toString());
//					return page;
//				}
//			}
//			else{
//				mustNotSelectDialog=false;
//				return this;
//			}
//		}
//		else{
//			mustNotSelectDialog=false;
//			return super.getNextPage();
//		}
//		return super.getNextPage();
	}
    
	void setLocationForSelection() {
    	locationArea.updateProjectName(getProjectName());
    }
	
    public IPath getLocationPath() {
        return new Path(locationArea.getProjectLocation());
    }
    
    public URI getLocationURI() {
    	return locationArea.getProjectLocationURI();
    }
    
    public String getLanguage(){
    	return langCombo.getText().trim();
    }
    
    public String getWizardType(){
    	return wizardTypeCombo.getText().trim();
    }
    
    private void createSelectLang(Composite parent){
		//Group group = OPRoSUtil.createGroup(parent, SWT.None, WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Title"), 
		//		2, GridData.FILL_HORIZONTAL);
    	Group group = new Group(parent, SWT.NONE);
    	group.setText(WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Title"));
    	group.setLayout(new GridLayout(2, false));
    	group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    	
		OPRoSUtil.createLabel(group, WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Label"),
				SWT.NONE,1,0,1,0,GridData.BEGINNING);
		langCombo = OPRoSUtil.createCombo(group, SWT.READ_ONLY|SWT.SINGLE, targetLanguages, 0, 1, 0, 1, 0, 0, 0,GridData.FILL_HORIZONTAL);
		langCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				prjInfo.setImplLanguage(getLanguage());
				/*
				if(compModel!=null){
					
					if(((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel())!=null){
						((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).setImplLang(langCombo.getText());
						int i =langCombo.getSelectionIndex();
						if(i==0)
							((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).setCompiler("g++");
						else if(i==1)
							((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).setCompiler("MSVC");
						else
							((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).setCompiler("JDK");
					}					
				}
				*/
			}
    	});
		
	}
    
}
