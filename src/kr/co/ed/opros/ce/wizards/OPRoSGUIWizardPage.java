package kr.co.ed.opros.ce.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jd.xml.xslt.Stylesheet;

import kr.co.ed.opros.ce.ComponentUtil;
import kr.co.ed.opros.ce.FileUtils;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.core.OPRoSProjectInfo;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentElementModel;
import kr.co.ed.opros.ce.ui.OPRoSGUIOverviewComposite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OPRoSGUIWizardPage extends WizardPage {
	protected OPRoSGUIOverviewComposite overviewComposite;
	public OPRoSComponentElementModel compModel;	
	public OPRoSProjectInfo prjInfo;
	public Text text_location;	
	public Button btn_location;
	public IProject project;	
	public static final String COMPONENT_NAME_MARK = "<%=Component_Name=%>";
	
	public OPRoSGUIWizardPage(String name) {
		super(name);
	}
	
	public OPRoSGUIWizardPage(String pageName, OPRoSComponentElementModel compModel,
			OPRoSProjectInfo prjInfo) {
		super(pageName);
		this.compModel = compModel;
		this.compModel.setComponentName("");
		this.prjInfo = prjInfo;		
	}
	
	public OPRoSGUIWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName);
		
		this.prjInfo = new OPRoSProjectInfo();
		this.compModel = new OPRoSComponentElementModel();
		
		if (selection != null && !selection.isEmpty()) {
            Object selectedElement = selection.getFirstElement();
            if (selectedElement instanceof IResource) {
            	if(OPRoSUtil2.isOPRoSProject(((IResource)selectedElement).getProject())) {
            		project = ((IResource)selectedElement).getProject();
            		setInfomation(project);
            	}           	
            }
        }
		else {			
			this.compModel.initializeData();			
			this.prjInfo.clear();			
		}
		compModel.initializeData();
		this.compModel.setComponentName("");
	}
	
	public void setInfomation(IProject project) {	
		IFile projectProfile = project.getFile(project.getName()+"Prj.xml");
		FileInputStream input=null;
		try {
			input = (FileInputStream) projectProfile.getContents();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build( input );
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element root;
		root = doc.getRootElement();
		prjInfo.loadProfile(root);		
		
		if(input!=null){
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = OPRoSUtil.createComposite(parent, SWT.NONE, 1, GridData.FILL_BOTH);
	
        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
		 		"org.eclipse.ui.ide.new_project_wizard_page_context");
        overviewComposite = new OPRoSGUIOverviewComposite(composite, SWT.NONE, 2, GridData.FILL_BOTH, this);
        overviewComposite.getCompNameText().addModifyListener(nameModifyListener);       
        
        if(getWizard() instanceof OPRoSNewComponentWizard){
        	Group group = new Group(composite, SWT.V_SCROLL);
    		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
    		group.setLayout(new GridLayout(3, false));
    		
    		Label label = new Label(group, SWT.NONE);
    		label.setText("Into project : ");
    		
    		text_location = new Text(group, SWT.NONE | SWT.BORDER);
    		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
    		text_location.setLayoutData(gd);
    		
    		btn_location = new Button(group, SWT.PUSH);
    		btn_location.setText("Browse...");
    		btn_location.addSelectionListener(new SelectionAdapter() {
    			public void widgetSelected(SelectionEvent event) {    				
    				IProject prj = handleBrowse();
    				if(prj != null && OPRoSUtil2.isOPRoSProject(prj)) {
    					project = prj; 
    					setInfomation(project);
        				text_location.setText(project.getName());
        				//overviewComposite.loadData();
    				}
    				else{
    					project = null; 
    				}
    					
    				dialogChange();
    			}
    		});
    		
    		if(project != null)
    			text_location.setText(project.getName());
        }       

        dialogChange();
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
	}
	
	private IProject handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select a Project.");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				IResource resource =ResourcesPlugin.getWorkspace().getRoot().findMember((Path) result[0]);
				if(resource instanceof IProject)
					return (IProject)resource;
				else 
					return resource.getProject();
			}
		}
		return null;
	}
	
	public boolean validatePage(){
		if(getComponentName()!=null && !getComponentName().isEmpty()){
			Iterator<String> it = prjInfo.getComponents();
			while(it.hasNext()){
				if(overviewComposite.getCompName().compareToIgnoreCase(it.next())==0){
					setErrorMessage("ComponentName is Duplicated");
					return false;
				}
			}
			//setErrorMessage(null);
			return true;
		}
		else{
			return false;
		}
	}
	private ModifyListener nameModifyListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			compModel.setComponentName(overviewComposite.getCompName());
			OPRoSExeEnvironmentElementModel envModel =(OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel();
			String libraryName = compModel.getComponentName()+"."+envModel.getLibraryType();
			if(envModel.getLibraryType().equals("so")) {
				libraryName = "lib"+compModel.getComponentName()+"."+envModel.getLibraryType();
			} 
			envModel.setLibraryName(libraryName);
            dialogChange();
		}
    };
    
    private String getComponentName(){
		if(overviewComposite!=null)
			return overviewComposite.getCompName();
		return "";
	}
    
    /**
	 * �좎룞�쇿뜝�숈삕�숋옙�좎룞�숉솕�좎룞���좎룞�쇿뜝�숈삕�좎룞�쇿뜝占쏀샇�좎룞�쇿뜝�붿눦���좎룞�쇿뜝�숈삕�좑옙�좎룞�쇿뜝�숈삕�좎룞���좎룞�숉듃�좎룞��
	 */
	public void dialogChange() {		
		
		if(getComponentName() == null || getComponentName().equals("")) {
			updateStatus("Enter the component name");
			return;
		}		
		
		if (getWizard() instanceof OPRoSNewComponentWizard && project == null) {
			updateStatus("Select a OPRoS project");
			return;
		}
		
		if(!validatePage())
			return;
		
				
		if(overviewComposite.isUseTemplate()) {
			if(overviewComposite.getComboMainCategory().getSelectionIndex() == 0) {
				updateStatus("Select a main component category");
				return;
			}		
			if(overviewComposite.getComboMiddleCategory().getSelectionIndex() == 0) {
				updateStatus("Select a middle component category");
				return;
			}
			if(overviewComposite.getSelectedComponent() == null) {
				updateStatus("Select a component");
				return;
			}
			updateStatus(null);
		}
		else {
			updateStatus(null);
		}
	}
    
    public void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
    
    /**
     * �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞��
     * @param monitor
     * @param newProject
     * @return
     */
    public boolean performFinishProgress(IProgressMonitor monitor, IProject newProject) {    	
    	if(newProject == null)
    		return false;    	
    	IFolder componentFolder = modifyProject(newProject, compModel.getComponentName());    	    
    	doFinish(monitor, newProject, componentFolder, null);
    	
		return true;
	}
    
    public boolean performFinishProgress(IProgressMonitor monitor, IProject newProject, IFolder componentFolder, IFile profile) {
    	if(newProject == null)
    		return false;    	
    	
    	doFinish(monitor, newProject, componentFolder, profile);  	
    	return true;
    }
    
    public void doFinish(IProgressMonitor monitor, IProject newProject, IFolder componentFolder, IFile profile){    	
    	createManifestFile(componentFolder, monitor);
    	profilize(newProject, monitor, profile);
    	
    	
    	if(profile == null) {
    		if(overviewComposite.isUseTemplate()) {    		
        		profile = importComponent(monitor, newProject, overviewComposite.getSelectedComponent());			
        	}
        	else {
        		profile = newComponentFile(monitor, newProject);
        	}
    	}  
    	else {
    		String referPath = OPRoSUtil.getOPRoSFilesPath();
			
			int index = profile.getName().lastIndexOf(".");
			String outFileName = profile.getName().substring(0, index);
			String profileDirPath = profile.getParent().getLocation().toOSString().replace("\\", "/");
			
			String[] params = {
					"-param","filename","'"+profileDirPath+"/"+outFileName+"'",
					"-param","outpath","'"+profileDirPath.substring(0,profileDirPath.lastIndexOf("/"))+"/'",
					referPath+"OPRoSFiles/GenerateProfiles2011.xsl",referPath+"/OPRoSFiles/GenerateProfiles2011.xsl"};
			Stylesheet.main(params);
    	}
    	
    	createMakeFile(monitor, newProject);
    	refreshProject(newProject);
    	
    	if(profile != null && profile.isAccessible())
    		openComponentEditor(profile);
    }
    
    public void createMakeFile(IProgressMonitor monitor, IProject project) {
    	OPRoSUtil.createMakeFile(((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).getLibraryType(), prjInfo, project);
    	monitor.worked(1);	
    	
    }
    
    
    
    public void profilize(IProject project, IProgressMonitor monitor, IFile profile) {
    	monitor.worked(1);
		prjInfo.setSrcFolder(project.getLocation()+"/"+compModel.getComponentName());
		prjInfo.setBinFolder(project.getLocation()+"/"+compModel.getComponentName());
		profilize(project, profile);
		monitor.worked(1);
    }
    
    /**
     * �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎뙣�먯삕�좎뜲�ㅽ듃 �좎룞�쇿뜝�숈삕 �좎룞��
     * @param componentFolder
     * @param monitor
     */
    public void createManifestFile(IFolder componentFolder, IProgressMonitor monitor) {
    	try {
    		String str = ComponentUtil.createManifestStr(null, compModel.getComponentName(), componentFolder, compModel.getVersion(), "", false);
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		baos.write(str.getBytes());
    		
    		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        	IFile manifestFile = componentFolder.getFile("OPRoS.mf");
        	manifestFile.create(bais, true, monitor);
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
    }
    
    public void refreshProject(IProject project) {
    	try{
    		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		}catch(CoreException e){
			e.printStackTrace();
		}
    }
    
    /**
     * �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕�좎룞���좎떛�몄삕�좎뙏�쎌삕 �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞���좎룞�쇿뜝�밸뙋���좎룞�쇿뜝占�
     * @param monitor
     * @param newProject
     * @param importComponent
     * @throws Exception
     */
    private IFile importComponent(IProgressMonitor monitor, IProject newProject, File importComponent) {
    	
    	IFolder compFolder = newProject.getFolder(compModel.getComponentName());
    	IFolder profileFolder = compFolder.getFolder("profile");
    	
    	File cppFile = null, headerFile = null, profile = null;
    	List<File> list = new ArrayList<File>();
    	
    	File[] res = importComponent.listFiles();
    	for(int i=0; i<res.length; i++) {
    		if(FileUtils.getExtension(res[i].getName()).equals("cpp"))
    			cppFile = res[i];
    		else if(FileUtils.getExtension(res[i].getName()).equals("h"))
    			headerFile = res[i];
    		else if(res[i].getName().equals(importComponent.getName()+".xml"))
    			profile = res[i];
    		else if(FileUtils.getExtension(res[i].getName()).equals("xml"))
    			list.add(res[i]);
    	}
    	
    	IFile componentProFile = generateComponentProfile(profile, profileFolder, monitor);
		if(componentProFile != null) {
    		for(File file : list) {
    			IFile ifle = profileFolder.getFile(file.getName());
    			try {
					ifle.create(new FileInputStream(file), true, monitor);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String referPath = OPRoSUtil.getOPRoSFilesPath();
				
				int index = file.getName().lastIndexOf(".");
				String outFileName = file.getName().substring(0, index);
				String profileDirPath = profileFolder.getLocation().toOSString().replace("\\", "/");
				
				String[] params = {
						"-param","filename","'"+profileDirPath+"/"+outFileName+"'",
						"-param","outpath","'"+profileDirPath.substring(0,profileDirPath.lastIndexOf("/"))+"/'",
						referPath+"OPRoSFiles/GenerateProfiles2011.xsl",referPath+"/OPRoSFiles/GenerateProfiles2011.xsl"};
				Stylesheet.main(params);
    		}
    	}	
		
		String cppString = generateComponentStr(cppFile);
		if(cppString != null) {
			createComponentSourceFile(cppString, getComponentName()+".cpp", compFolder, monitor);
		}
		
		String headerString = generateComponentStr(headerFile);
		if(cppString != null) {
			createComponentSourceFile(headerString, getComponentName()+".h", compFolder, monitor);
		}
		
		return componentProFile;
    }
    
    /**
     * �좎룞�쇿뜝��쨪�쇿뜝�숈삕�멨뜝�숈삕 �좎��숈삕�좎룞�쇿뜝�숈삕 �좎룞�숉듃�좎룞�쇿뜝�숈삕�좎룞���좎��숈삕�좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�쇰뙋��
     * @param componentStr
     * @param folder
     * @param monitor
     */
    private void createComponentSourceFile(String componentStr, String fileName, IFolder folder, IProgressMonitor monitor) {
    	try{
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		baos.write(componentStr.getBytes());
    		
    		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());			
    		
    		IFile ifile = folder.getFile(fileName);
    		ifile.create(bais, true, monitor);
    		
    		baos.close();
    		bais.close();
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎��숈삕 �좎룞�쇿뜝�뱀슱�쇿뜝�숈삕 �닷뜝�숈삕�좎룞�쇿뜝�숈삕�좑옙�좎뙃�쎌삕�좎룞�쇿뜝�숈삕 �좎뙐袁쇰뙋��
     * @param cppFile
     * @param compFolder
     * @param monitor
     * @return
     */
    private String generateComponentStr(File cppFile) {
    	if(cppFile == null)
    		return null;
    	
    	StringBuffer buffer = new StringBuffer();
    	try{
    		LineNumberReader lnreader = new LineNumberReader(new FileReader(cppFile));   		
    		String s;
    		while ((s = lnreader.readLine()) != null) {
    			buffer.append((s+"\r\n").replace(COMPONENT_NAME_MARK, getComponentName()));
    		}
    		lnreader.close();    		
    	} catch (IOException e) {
    		e.printStackTrace();
    		return null;
		}    	
    	return buffer.toString();
    }
    
    /**
     * �좎뙏�먯삕 �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕�좎룞���좎�琉꾩삕�좎떢�쎌삕 �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎떛紐뚯삕�좎룞���좎뙐袁멸낀��
     * �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�몃쨪���좎룞�쇿뜝�숈삕�좎떬�먯삕.
     * @param profile
     * @param parent
     * @param monitor
     * @return
     */
    private IFile generateComponentProfile(File profile, IFolder parent, IProgressMonitor monitor) {
    	if(profile == null || parent == null)
    		return null;
    	
    	IFile componentProFile = null;
    	
    	try{
    		SAXBuilder builder = new SAXBuilder();
    		Document doc = builder.build( new FileInputStream(profile));
    		Element element = doc.getRootElement();
    		element.getChild("name").setText(getComponentName());
    		
    		XMLOutputter opt = new XMLOutputter();
    		Format form = opt.getFormat();
    		form.setEncoding("euc-kr");
    		form.setLineSeparator("\r\n");
    		form.setIndent("	");
    		form.setTextMode(Format.TextMode.TRIM);
    		opt.setFormat(form);
    		
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		opt.output(doc, baos);
    		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    		
    		componentProFile = parent.getFile(getComponentName()+".xml");
    		componentProFile.create(bais, true, monitor);
    	
    		baos.close();
    		bais.close();
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
    		e.printStackTrace();
		} catch (JDOMException e) {
    		e.printStackTrace();
		}
    	return componentProFile;
    }
    
    /**
     * �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�밸뙋���좎룞�쇿뜝占�     * @param monitor
     * @param newProject
     */
    private IFile newComponentFile(IProgressMonitor monitor, IProject newProject) {    	
    	String referPath = OPRoSUtil.getOPRoSFilesPath();    	
		String[] params = {
				"-param","filename","'"+newProject.getLocation()+"/"+compModel.getComponentName()+"/profile/"+compModel.getComponentName()+"'",
				//"-param","usage","",
				"-param","outpath","'"+newProject.getLocation()+"/"+compModel.getComponentName()/*+"/src/"*/+"/'",
				referPath+"OPRoSFiles/GenerateProfiles2011.xsl",referPath+"/OPRoSFiles/GenerateProfiles2011.xsl"};			
			
		Stylesheet.main(params);	
		
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		File file = (new File(newProject.getLocation()+"/"+compModel.getComponentName()+"/profile/"+compModel.getComponentName()+".xml"));
		IPath location = Path.fromOSString(file.getAbsolutePath());
		IFile fileTemp = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
		
		return fileTemp;
    }
    
    private void openComponentEditor(IFile file) {
    	final IEditorInput ei = new FileEditorInput(file);
    	
    	PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(ei, "kr.co.ed.opros.ce.editors.OPRoSGUIProfileEditor");
				} 
				catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
    }
    
    private void profilize(IProject newProject, IFile profile){
		Element prjRoot = new Element("OPRoSProject");
		prjInfo.profilize(prjRoot);
		Document prjDoc = new Document(prjRoot);
		XMLOutputter opt = new XMLOutputter();
		Format form = opt.getFormat();
		form.setEncoding("euc-kr");
		form.setLineSeparator("\r\n");
		form.setIndent("	");
		form.setTextMode(Format.TextMode.TRIM);
		opt.setFormat(form);
		try{
			FileOutputStream outPrjStream = new FileOutputStream(newProject.getLocation()+"/"+prjInfo.getPrjName()+"Prj.xml");
			opt.output(prjDoc, outPrjStream);
			outPrjStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		if(profile == null ) {
			Element compRoot = new Element("component_profile");
			
			//�좎룞�쇿뜝�숈삕�좎룞���좎듅�먯삕 �좎룞�쇿뜝�숈삕�좎룞���좎떖�쎌삕 �좎룞�쇿뜝�숈삕
			String impLang = prjInfo.getImplLanguage();
			((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).setImplLang(impLang);
			if(impLang.equals(WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Combo0"))) {
				((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).setCompiler("g++");
			} else if(impLang.equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo1"))) {
				((OPRoSExeEnvironmentElementModel)compModel.getExeEnvironmentModel()).setCompiler("MSVC");
			}
			
			
			compModel.doSave(compRoot, newProject.getLocation()+"/"+compModel.getComponentName()+"/profile");
			Document compDoc = new Document(compRoot);
			try{
				String componentName = compModel.getComponentName();
				if(componentName != null && !componentName.equals("")) {
					FileOutputStream outStream = new FileOutputStream(newProject.getLocation()+"/"+componentName+"/profile/"+componentName+".xml");
					opt.output(compDoc, outStream);
					outStream.close();
				}			
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
		//refreshProject(newProject);
	}
    
    /**
     * �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞�쇿뜝占썲뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝占썲뜝�숈삕�좎룞�쇿뜝占썲뜝�숈삕�좎떬�먯삕.
     * @param newProject
     * @return
     */
    public IFolder modifyProject(IProject newProject, String componentName){
		IFolder compFolder = newProject.getFolder(componentName);
		if(!compFolder.isAccessible()) {
			createIFolder(compFolder);
		}
		String[] names = {"profile", "Debug", "Release", "res"};
		if(compFolder.isAccessible()) {
			for(int i=0; i<names.length; i++) {
				createIFolder(compFolder.getFolder(names[i]));
			}
		}
		
		return compFolder;
	}
    
    /**
     * �좎룞�쇿뜝�숈삕�좑옙�좎룞�쇿뜝�쇰뙋��
     * @param parent
     * @param folderName
     */
    public void createIFolder(IFolder iFolder) {
		if(!iFolder.exists()){
			try{
				iFolder.create(false, true, null);
			}catch(CoreException e){
				e.printStackTrace();
			}
		}
    }
    
    public OPRoSComponentElementModel getCompModel() {
		return compModel;
	}

	public OPRoSProjectInfo getPrjInfo() {
		return prjInfo;
	}
	
	public IProject getProject() {
		return project;
	}
    
}
