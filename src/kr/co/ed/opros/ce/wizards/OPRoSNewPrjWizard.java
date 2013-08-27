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
import java.util.HashSet;
import java.util.Set;

import kr.co.ed.opros.ce.OPRoSNature;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.core.OPRoSProjectInfo;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CSourceEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.ICSourceEntry;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IProjectType;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.progress.IProgressService;

public class OPRoSNewPrjWizard extends Wizard implements INewWizard {
	private OPRoSNewPrjWizardPage newPrjWizardPage;
	private OPRoSGUIWizardPage newCompWizardPage;
	private IProject newProject;
	private OPRoSProjectInfo prjInfo = new OPRoSProjectInfo();
	private OPRoSComponentElementModel compModel = new OPRoSComponentElementModel();
	public boolean isGraphicalWizardType = true;
	IProjectDescription prjDescription;
	public String compName;
	
	public OPRoSNewPrjWizard() {
		
	}
	
	public OPRoSNewPrjWizard(String name) {
		setCompName(name);
	}
	
	public static void SendMessage(String msg){
		OPRoSUtil.openMessageBox(msg, SWT.OK);
	}
	
	public boolean createProjectWaitForEnd(){
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try {
			progressService.runInUI(PlatformUI.getWorkbench().getProgressService(),
					new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor){
					monitor.beginTask("Creating OPRoS Project", 5);
					prjInfo.addComponent(compModel.getComponentName());
					monitor.worked(1);
					
					setCompName(compModel.getComponentName());
					newCompWizardPage.performFinishProgress(monitor, createNewProject(prjInfo));
					monitor.done();
				}
			}, ResourcesPlugin.getWorkspace().getRoot());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean performFinish() {
		setProjectInfo();
		boolean bReturn = createProjectWaitForEnd();
		
		//프로젝트가 정상적으로 생성된경우 오프러스 네추어 아이디 추가
		if(bReturn){
			try {
				OPRoSNature.addNature(newProject, new NullProgressMonitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bReturn;
	}

	@Override
	public boolean canFinish() {
		if(prjInfo.getPrjName().isEmpty()
				||compModel.getComponentName().isEmpty()
				)
			return false;
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		prjInfo.clear();
		compModel.initializeData();
//		progressService = workbench.getProgressService();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
//		if(page instanceof OPRoSNewPrjWizardPage){
//			
//		}
		return super.getNextPage(page);
	}

	public void addPages(){
		newPrjWizardPage = new OPRoSNewPrjWizardPage(WizardMessages.getString("NewPjtWizardPage.Title"),compModel,prjInfo);
		newPrjWizardPage.setDescription(WizardMessages.getString("NewPjtWizardPage.Description"));
		newPrjWizardPage.setTitle(WizardMessages.getString("NewPjtWizardPage.Title"));
		this.setWindowTitle(WizardMessages.getString("NewPjtWizard.WindowTitle"));
		addPage(newPrjWizardPage);
		newCompWizardPage = new OPRoSGUIWizardPage(WizardMessages.getString("NewGraphicalCompWizardPage.Title"),compModel,prjInfo);
		newCompWizardPage.setDescription(WizardMessages.getString("NewGraphicalCompWizardPage.Description"));
		newCompWizardPage.setTitle(WizardMessages.getString("NewGraphicalCompWizardPage.Title"));
		addPage(newCompWizardPage);
	}
	
	public IProject createNewProject(OPRoSProjectInfo prjInfo){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();//워크스페이스 핸들
		IWorkspaceRoot root = workspace.getRoot();//워크스페이스 루트핸들
		final IProject newProjectHandle = root.getProject(prjInfo.getPrjName()); //프로젝트 핸들을 가져와본다.
		prjDescription=null;
		if(!newProjectHandle.exists()){//이미 존재하는 이름의 프로젝트 핸들인가?
			prjDescription = workspace.newProjectDescription(newProjectHandle.getName());
			IPath projectLocation = newProjectHandle.getRawLocation();
			if((projectLocation!=null)&&(!projectLocation.equals(Platform.getLocation()))){
				prjDescription.setLocation(projectLocation);
			}
			//구현언어에 따른 프로젝트 리소스 생성 
			if (prjInfo
					.getImplLanguage()
					.equals(WizardMessages
							.getString("NewPjtWizardPage.SelectLanguageGroup.Combo0"))) {
				try {
					newProject = CCorePlugin.getDefault().createCDTProject(
							prjDescription, newProjectHandle,
							new NullProgressMonitor());
				} catch (OperationCanceledException e) {
					e.printStackTrace();
				} catch (CoreException e) {
					e.printStackTrace();
				}

			}
			else if(prjInfo.getImplLanguage().equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo1"))){
				try{
					newProject = CCorePlugin.getDefault().createCDTProject(prjDescription, newProjectHandle, new NullProgressMonitor());
				} catch (OperationCanceledException e){
					e.printStackTrace();
				} catch (CoreException e){
					e.printStackTrace();
				}
			}
			else if(prjInfo.getImplLanguage().equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo3"))){
				try{
					newProject = CCorePlugin.getDefault().createCDTProject(prjDescription, newProjectHandle, new NullProgressMonitor());
				} catch (OperationCanceledException e){
					e.printStackTrace();
				} catch (CoreException e){
					e.printStackTrace();
				}
			}
		}else{
			IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					newProjectHandle.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				}
			};
			NullProgressMonitor monitor = new NullProgressMonitor();
			try	{
				workspace.run(runnable, root, IWorkspace.AVOID_UPDATE, monitor);
			} catch(CoreException e){
				e.printStackTrace();
			}
			newProject = newProjectHandle;
		}
		try{
			//ManagedCProjectNature.addNature(newProject,WizardMessages.getString("NewPjtWizard.CPPNature"),null);
			//새 프로젝트에 Nature 할당
			CCProjectNature.addCCNature(newProject, new NullProgressMonitor());			
			
		//	CProjectNature.addCNature(newProject, new NullProgressMonitor());
			ICProjectDescriptionManager prjManager = CoreModel.getDefault().getProjectDescriptionManager();
			//CoreModel의 기본 CProjectDescriptionManager를 통해 CProjectDescription 생성
			ICProjectDescription prjCDescription = prjManager.createProjectDescription(newProject, false);
			//ManageBuildManager를 통해 ManagedBuildInfo 생성
			IManagedBuildInfo cdtBuildInfo = ManagedBuildManager.createBuildInfo(newProject);
			//cdt.managedbuild.target.gnu.mingw.exe ProjectType을 가져옮 
			IProjectType cdtPrjType=null;
			//cdt.managedbuild.toolchain.gnu.mingw.base Toolchain을 가져옮
			IToolChain cdtToolChain=null;
			if(prjInfo.getImplLanguage().equals(WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Combo0"))){
				cdtPrjType = 
					ManagedBuildManager.getExtensionProjectType(WizardMessages.getString("NewPjtWizard.CDTType"));
				cdtToolChain = 
					ManagedBuildManager.getExtensionToolChain(WizardMessages.getString("NewPjtWizard.CDTToolChain"));
			}
			else if(prjInfo.getImplLanguage().equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo1"))){
				cdtPrjType = 
					ManagedBuildManager.getExtensionProjectType(WizardMessages.getString("NewPjtWizard.CDTMSVCType"));
				cdtToolChain = 
					ManagedBuildManager.getExtensionToolChain(WizardMessages.getString("NewPjtWizard.CDTMSVCToolChain"));
			}
			else if(prjInfo.getImplLanguage().equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo3"))){
				cdtPrjType = 
					ManagedBuildManager.getExtensionProjectType(WizardMessages.getString("NewPjtWizard.CDTARMType"));
				cdtToolChain = 
					ManagedBuildManager.getExtensionToolChain(WizardMessages.getString("NewPjtWizard.CDTARMToolChain"));
			}
			//위의 프로젝트 타입과 툴체인을 가지는 ManagedProject 생성
			IManagedProject cdtPrj = new ManagedProject(newProject,cdtPrjType);
			cdtBuildInfo.setManagedProject(cdtPrj);
			
			IConfiguration[] cdtConfigs = ManagedBuildManager.getExtensionConfigurations(cdtToolChain,cdtPrjType);
			for(IConfiguration iCfg : cdtConfigs){
				if(!(iCfg instanceof Configuration)){
					continue;
				}
				Configuration cfg = (Configuration) iCfg;
				String calId = ManagedBuildManager.calculateChildId(cfg.getId(),null);
				Configuration cfg1 = new Configuration((ManagedProject) cdtPrj,cfg,calId,false,true);
				
				cfg1.enableInternalBuilder(false);
				ICConfigurationDescription cfgDes =
					prjCDescription.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, cfg1.getConfigurationData());
				cfg1.setConfigurationDescription(cfgDes);
				cfg1.exportArtifactInfo();
				IBuilder cdtBuilder = cfg1.getEditableBuilder();
				
				if(cdtBuilder != null){
					cdtBuilder.setAutoBuildEnable(false);
					cdtBuilder.setManagedBuildOn(false);
//					String buildPath = cdtBuilder.getBuildPath();
//					int index = buildPath.lastIndexOf("/");
//					String lastBuildPath = buildPath.substring(index);
					cdtBuilder.setBuildPath("${workspace_loc:/"+prjInfo.getPrjName()/*+"/"+compInfo.getComponentName()+prjInfo.getBinFolder()*/+"/}");
					
				}
				cfg1.setName(cfg.getName());
				cfg1.setArtifactName(newProject.getName());
			}
			try{
				prjManager.setProjectDescription(newProject, prjCDescription);
				if(!newProject.isOpen()){
					newProject.open(new NullProgressMonitor());
				}
			} catch (CoreException e){
				e.printStackTrace();
			}
			
			IFolder compFolder = newProject.getFolder(getCompName());
			if(!compFolder.exists()){
				try{
					compFolder.create(false, true, null);
				}catch(CoreException e){
					e.printStackTrace();
				}
			}
			
			ICConfigurationDescription cfgs[] = prjCDescription.getConfigurations();
			for(ICConfigurationDescription cfg : cfgs){
				ICSourceEntry[] entries = cfg.getSourceEntries();
				Set<ICSourceEntry> set=new HashSet<ICSourceEntry>();
				set.add(new CSourceEntry(compFolder,null,0));
				entries=set.toArray(new ICSourceEntry[set.size()]);
				cfg.setSourceEntries(entries);
			}
		}catch (CoreException e){
			e.printStackTrace();
			return null;
		}
		
		return newProject;
	}
	
	public void changePerspective(String id){
		if(!(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId().equals(id))){
			boolean question = MessageDialog.openQuestion(getShell(), WizardMessages.getString("NewPjtWizard.ChangePerspective.Title"), 
					WizardMessages.getString("NewPjtWizard.ChangePerspective.Decsription"));
			if(question){
				try {
					PlatformUI.getWorkbench().showPerspective(id, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				} catch (WorkbenchException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			newProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}
	
	private void setProjectInfo(){
		prjInfo.setPrjName(newPrjWizardPage.getProjectName());
		prjInfo.setImplLanguage(newPrjWizardPage.getLanguage());
		prjInfo.setLocation(newPrjWizardPage.getLocationPath().toString());
	}
	
	@Override
	public boolean performCancel() {
		prjInfo.clear();
		compModel.initializeData();
		return super.performCancel();
	}
	
	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}
	
}
