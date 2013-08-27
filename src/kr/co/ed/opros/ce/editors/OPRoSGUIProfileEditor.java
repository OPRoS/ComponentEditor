package kr.co.ed.opros.ce.editors;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jd.xml.xslt.Stylesheet;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.core.OPRoSProjectInfo;
import kr.co.ed.opros.ce.guieditor.OPRoSContextMenuProvider;
import kr.co.ed.opros.ce.guieditor.OPRoSElementEditPartFactory;
import kr.co.ed.opros.ce.guieditor.PaletteViewerCreator;
import kr.co.ed.opros.ce.guieditor.actions.OPRoSAddElementAction;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceProvidedPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceRequiredPortElementPart;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;
import kr.co.ed.opros.ce.handler.FormWizardArrowKeyHandler;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.wizards.datatransfer.TarFileExporter;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OPRoSGUIProfileEditor extends GraphicalEditorWithFlyoutPalette implements IResourceChangeListener{
	public OPRoSBodyElementModel model;
	KeyHandler keyHandler;
	private static OPRoSGUIProfileEditor editor;
	private IProject project;
	private OPRoSProjectInfo prjInfo=new OPRoSProjectInfo();
	private OPRoSGUIProfileEditorOutlinePage outlinePage;
	private PaletteRoot root;
	private boolean isOPRoSDirty=false; 
	private boolean isOPRoSDirtyCheck=false;
	protected FileEditorInput editorInput;
	public IFile inputFile;	
	
	public OPRoSGUIProfileEditor() {
		setEditDomain(new DefaultEditDomain(this));
		editor=this;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	@Override
	public CommandStack getCommandStack(){
		return super.getCommandStack();
	}

	@Override
	public GraphicalViewer getGraphicalViewer() {
		// TODO Auto-generated method stub
		return super.getGraphicalViewer();
	}
	
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		
		//워크스페이스 내의 파일이 들어올경우(IFile)
		if (input instanceof IFileEditorInput) {
			inputFile = ((IFileEditorInput)input).getFile();
			
			model = new OPRoSBodyElementModel(this);		
			doLoad();
			setPartName(model.compEle.getComponentName());
			
        }
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(model);
		viewer.addDropTargetListener(new PaletteDragAndDropTargetListener(viewer));
		viewer.setKeyHandler(new FormWizardArrowKeyHandler(viewer, getSite().getSelectionProvider(), getCommandStack()));
		//getGraphicalViewer().getEditDomain().setActiveTool(new MarqueeSelectionTool());
	}	
	
	/**
	 * 컴포넌트용 소스파일을 가져온다.
	 * 
	 * @param type
	 * @return
	 */
	public IFile getSourceFile(String type) {
		IFolder parent = (IFolder) ((IFileEditorInput) getEditorInput())
				.getFile().getParent().getParent();
		return parent.getFile(model.compEle.getComponentName() + "." + type);
	}

	 protected PaletteViewerProvider createPaletteViewerProvider() {
        return new PaletteViewerProvider(getEditDomain()) {
            protected void configurePaletteViewer(PaletteViewer viewer) {
                super.configurePaletteViewer(viewer);
                viewer.addDragSourceListener(new PaletteTransferDragSourceListener(viewer));
            }
        };
    }
	 
	public RootEditPart getRootEditPart(){
		return getGraphicalViewer().getRootEditPart();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		//OPRoSRegenSourceQuestionDialog dlg = new OPRoSRegenSourceQuestionDialog(null, model.isNeedSourceModify());
		//dlg.open();
		//if(dlg.getReturnCode()==OPRoSRegenSourceQuestionDialog.MODIFY||dlg.getReturnCode()==OPRoSRegenSourceQuestionDialog.NORMALSAVE){
		if(model.isNeedSourceModify()) {
			IProgressService service = PlatformUI.getWorkbench().getProgressService();
			try {
				service.run(true, true, new IRunnableWithProgress(){

					@Override
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						monitor.beginTask("OPRoS Component Profile & SourceCode Modify",10);
						XMLOutputter opt = new XMLOutputter();
						Format form = opt.getFormat();
						form.setEncoding("euc-kr");
						form.setLineSeparator("\r\n");
						form.setIndent("	");
						form.setTextMode(Format.TextMode.TRIM);
						opt.setFormat(form);
					
						Element compRoot = new Element("component_profile");
						model.doSave(compRoot);
						monitor.worked(1);
						Document compDoc = new Document(compRoot);
						try{
							FileOutputStream outStream = new FileOutputStream(model.getFilename());
							opt.output(compDoc,outStream);
							outStream.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						monitor.worked(1);
						//modifySource();
						//model.clearModifyPortList();
						monitor.done();		
						
					}
					
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			XMLOutputter opt = new XMLOutputter();
//			Format form = opt.getFormat();
//			form.setEncoding("euc-kr");
//			form.setLineSeparator("\r\n");
//			form.setIndent("	");
//			form.setTextMode(Format.TextMode.TRIM);
//			opt.setFormat(form);
//		
//			Element compRoot = new Element("component_profile");
//			model.doSave(compRoot);
//			Document compDoc = new Document(compRoot);
//			try{
//				FileOutputStream outStream = new FileOutputStream(model.getFilename());
//				opt.output(compDoc,outStream);
//				outStream.close();
//			}catch(IOException e){
//				e.printStackTrace();
//			}
//			modifySource();
//			model.clearModifyPortList();
		//}else if(dlg.getReturnCode()==OPRoSRegenSourceQuestionDialog.REGEN){
		}else {
			XMLOutputter opt = new XMLOutputter();
			Format form = opt.getFormat();
			form.setEncoding("euc-kr");
			form.setLineSeparator("\r\n");
			form.setIndent("	");
			form.setTextMode(Format.TextMode.TRIM);
			opt.setFormat(form);
		
			Element compRoot = new Element("component_profile");
			model.doSave(compRoot);
			Document compDoc = new Document(compRoot);
			try{
				FileOutputStream outStream = new FileOutputStream(model.getFilename());
				opt.output(compDoc,outStream);
				outStream.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			/*
			String referPath=OPRoSUtil.getOPRoSFilesPath();
			String[] params = {
					"-param","filename","'"+project.getLocation()+"/"+model.compEle.getComponentName()+"/profile/"+model.compEle.getComponentName()+"'",
					"-param","outpath","'"+project.getLocation()+"/"+model.compEle.getComponentName()+"/'",
					referPath+"/OPRoSFiles/GenerateProfiles2011.xsl",referPath+"/OPRoSFiles/GenerateProfiles2011.xsl"};
			Stylesheet.main(params);
			//model.clearModifyPortList();			
			*/
		}
	
		//컴파일러를 변경할 수도 있기때문에...
		//프리퍼런스 창에서 컴파일 옵션 변경시 메이크파일 변경하게끔 해놨기 때문에 주석처리
		/*
		OPRoSUtil.createMakeFile(((OPRoSExeEnvironmentElementModel)model.compEle.getExeEnvironmentModel()).getLibraryType(),prjInfo);
		try{
			project.refreshLocal(IResource.DEPTH_INFINITE, null);//프로젝트 리프레쉬
		}catch(CoreException e){
			e.printStackTrace();
		}
		*/
		isOPRoSDirty=false;
		isOPRoSDirtyCheck=true;
		firePropertyChange(IEditorPart.PROP_DIRTY);
		getCommandStack().markSaveLocation();		
		
		//포트 변경사항 적용
		if (getModel().isNeedSourceModify()) {				
			modifySource();
			getModel().clearModifyPortList();
		}	
		
		OPRoSUtil2.refreshProject(project);
	}
	
	protected void configureGraphicalViewer(){
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new OPRoSElementEditPartFactory());
		//viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		
		configureZoomLevel();
		//x();
		IAction createServiceProvidedPortAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_SERVICE_PROVIDED_PORT);
		IAction createServiceReqiredPortAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_SERVICE_REQUIRED_PORT);
		IAction createDataInPortAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_DATA_IN_PORT);
		IAction createDataOutPortAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_DATA_OUT_PORT);
		IAction createEventInPortAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_EVENT_IN_PORT);
		IAction createEventOutPortAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_EVENT_OUT_PORT);
		IAction createExeEnvOSAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_OS);
		IAction createExeEnvCPUAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_CPU);
		IAction createPropertyAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_PROPERTY);
		IAction createDataTypeAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_DATA_TYPE);
		IAction createServiceTypeAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_SERVICE_TYPE);
		IAction createNewServicePortTestAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.NEW_SERVICE_PORT_TEST);
		IAction createMonitoringVariableAction = new OPRoSAddElementAction(this,OPRoSAddElementAction.ADD_MONITORING_VARIABLE);
		
		getActionRegistry().registerAction(createServiceReqiredPortAction);
		getActionRegistry().registerAction(createServiceProvidedPortAction);		
		getActionRegistry().registerAction(createDataOutPortAction);
		getActionRegistry().registerAction(createDataInPortAction);		
		getActionRegistry().registerAction(createEventOutPortAction);
		getActionRegistry().registerAction(createEventInPortAction);
		getActionRegistry().registerAction(createExeEnvOSAction);
		getActionRegistry().registerAction(createExeEnvCPUAction);
		getActionRegistry().registerAction(createPropertyAction);
		getActionRegistry().registerAction(createDataTypeAction);
		getActionRegistry().registerAction(createServiceTypeAction);
		getActionRegistry().registerAction(createNewServicePortTestAction);
		getActionRegistry().registerAction(createMonitoringVariableAction);
		viewer.setContextMenu(new OPRoSContextMenuProvider(viewer, getActionRegistry()));
		
		
//		configureRuler();
	}
	
	public IAction getAction(String id) {		
		return getActionRegistry().getAction(id);	
	}

	private void configureZoomLevel(){
		GraphicalViewer viewer = getGraphicalViewer();
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		viewer.setRootEditPart(rootEditPart);
		
		ZoomManager manager = rootEditPart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));
		
		double[] zoomLevels = new double[]{
				0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0
		};
		manager.setZoomLevels(zoomLevels);
		
		List<String> contributions = new ArrayList<String>();
		contributions.add(ZoomManager.FIT_ALL);
		contributions.add(ZoomManager.FIT_HEIGHT);
		contributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(contributions);
		
		keyHandler = new KeyHandler();
		viewer.setKeyHandler(keyHandler);
		
		keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD), getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		
		//Mouse wheel
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), MouseWheelZoomHandler.SINGLETON);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class type) {
		if(type==ZoomManager.class)
			return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
		else if(type == IContentOutlinePage.class){
			if(outlinePage == null)
				outlinePage = new OPRoSGUIProfileEditorOutlinePage(this);
			return outlinePage;
		}
		else
			return super.getAdapter(type);
	}
	
	public void updateOutline(){
		((SashForm)outlinePage.getControl()).update();
	}	
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);		
		updateActions(getSelectionActions());
	}
	
	@Override
	protected void updateActions(List actionIds) {
		ActionRegistry registry = getActionRegistry();
		Iterator iter = actionIds.iterator();
		while (iter.hasNext()) {
			IAction action = registry.getAction(iter.next());
			if (action instanceof UpdateAction)
				((UpdateAction) action).update();
		}
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		if(root==null)
			 root = PaletteViewerCreator.createPaletteRoot();		
		return root;
	}

	public OPRoSBodyElementModel getModel() {
		return model;
	}
	
	static public OPRoSGUIProfileEditor getInstance(){
		return editor;
	}
	@Override
	public boolean isDirty() {
		return isOPRoSDirty;
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		if(!isOPRoSDirty&&!isOPRoSDirtyCheck){
			isOPRoSDirty=true;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}else if(!isOPRoSDirty&&isOPRoSDirtyCheck){
			isOPRoSDirtyCheck=false;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	private void doLoad(){
		FileInputStream inputStream = null;
		editorInput = (FileEditorInput)this.getEditorInput();
		IFile ifile = editorInput.getFile();
		if(ifile.exists()){
			try {
				inputStream=(FileInputStream) ifile.getContents();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(inputStream!=null){
			SAXBuilder builder = new SAXBuilder();
			Document doc = null;
			try {
				doc = builder.build( inputStream );
				inputStream.close();
				model = doLoad(((IPathEditorInput)this.getEditorInput()).getPath().toString(), doc);
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadOPRoSProject(this.getEditorInput());
	}
	
	public OPRoSBodyElementModel doLoad(String fileName, Document doc) {
		Element root;
		root = doc.getRootElement();
		if(root.getName().compareToIgnoreCase("component_profile")!=0){
			OPRoSUtil.openMessageBox("This File is Not OPRoS Profile!", SWT.OK);
			return null;
		}
		OPRoSBodyElementModel model = new OPRoSBodyElementModel(this);
		model.doLoad(fileName, root);
		return model;
	}
	
	private void loadOPRoSProject(IEditorInput editorInput){
		IWorkspaceRoot rootWorkspace = ResourcesPlugin.getWorkspace().getRoot();//워크스페이스 핸들
		IResource res = rootWorkspace.findMember(editorInput.getToolTipText());
		if (res != null && res.getType() != IResource.ROOT) {
        	while (res.getType() != IResource.PROJECT) {
        		res = res.getParent();
            }
        }else{
        	res=null;
        }
        project=((IFileEditorInput)editorInput).getFile().getProject();
        if(project!=null){
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
			if(prjInfo!=null){
				prjInfo.clear();
				prjInfo.loadProfile(root);
				if(input!=null){
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
        }
	}
	
	@SuppressWarnings("unchecked")
	public void modifySource(){
		HashSet<OPRoSPortElementBaseModel> provideSet = new HashSet<OPRoSPortElementBaseModel>();
		ArrayList<String> includeList = new ArrayList<String>();
		String strCompName=model.compEle.getComponentName();
		String hfileName = project.getLocation()+"/"+strCompName+"/"+strCompName+".h";
		String cppfileName = project.getLocation()+"/"+strCompName+"/"+strCompName+".cpp";
		File headerFile = new File(hfileName);
		File cppFile = new File(cppfileName);
	
		int b;
		StringBuffer fileContents = new StringBuffer();
		StringBuffer cppFileContents = new StringBuffer();
		try
		{		
			BufferedReader buffRead = new BufferedReader(new FileReader(headerFile));
			while ((b = buffRead.read()) != -1) {
				fileContents.append((char)b);
			}			
			buffRead.close();		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{		
			BufferedReader buffRead = new BufferedReader(new FileReader(cppFile));
			while ((b = buffRead.read()) != -1) {
				cppFileContents.append((char)b);
			}			
			buffRead.close();		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String addString="";
		Iterator<OPRoSPortElementBaseModel> it;
		OPRoSPortElementBaseModel portModel;
		//dataPort delete h modify
		it = model.getDelPortList();
		while(it.hasNext()){
			portModel = (OPRoSPortElementBaseModel)it.next();
			if(portModel instanceof OPRoSDataInPortElementModel){
				int nIndex = findIndexContents(fileContents,portModel.getName(),0,false);
				while(nIndex!=-1){
					int nCommentStart = fileContents.lastIndexOf("\n",nIndex)+1;
					nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
					fileContents.insert(nCommentStart,"/*");
					fileContents.insert(nIndex+2, "*/");
					nIndex = findIndexContents(fileContents,portModel.getName(),nIndex+2,false);
				}
				//Source File 생성자의 데이터 포트 생성자 삭제
				String strCPP=strCompName+"(";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				int nBraceIndex=0;
				int nDelPosition=0;
				while(nCppIndex!=-1){
					nCppIndex = findIndexContents(cppFileContents,")",nCppIndex,false);
					nBraceIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
					nDelPosition = findIndexContents(cppFileContents,portModel.getName(),nCppIndex,false);
					if(nDelPosition!=-1&&nCppIndex<nDelPosition&&nDelPosition<nBraceIndex){
						int newDelPosition=findDelpositionConstructor(cppFileContents,nDelPosition);
						if(newDelPosition!=-1){
							if(cppFileContents.charAt(newDelPosition)==':'){
								int nReplaceIndex = findNextConstructor(cppFileContents,newDelPosition);
								if(nReplaceIndex!=-1){
									cppFileContents.replace(nReplaceIndex, nReplaceIndex+1, ":");
								}
							}
						}
						nCppIndex = findIndexContents(cppFileContents,")",nDelPosition,false);
						cppFileContents.insert(nDelPosition-1, "/*");//:포함 삭제때문에 -1
						cppFileContents.insert(nCppIndex+3, "*/");
						nCppIndex = findIndexContents(cppFileContents,strCPP,nCppIndex+3,false);
					}else{
						nCppIndex = findIndexContents(cppFileContents,strCPP,nBraceIndex,false);
					}
				}
				//SourceFile addPort 문 삭제
				strCPP="void "+strCompName+"::portSetup()";
				nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				int nPortNameIndex=0;
				if(nCppIndex!=-1){
					String strDataDelPort="addPort";
					nDelPosition = findIndexContents(cppFileContents,strDataDelPort,nCppIndex,false);
					nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
					int nParaseIndex =0;
					while(nDelPosition!=-1&&nPortNameIndex!=-1){
						nParaseIndex = findIndexContents(cppFileContents,")",nDelPosition,false);
						if(nParaseIndex!=-1){
							if(nPortNameIndex>nParaseIndex){
								nDelPosition = findIndexContents(cppFileContents,strDataDelPort,nParaseIndex,false);
							}
							else if(nDelPosition<nPortNameIndex && nPortNameIndex<nParaseIndex){
								int nCommentStart = nDelPosition;
								nDelPosition = findIndexContents(cppFileContents,";",nDelPosition,false)+1;
								cppFileContents.insert(nCommentStart, "/*");
								cppFileContents.insert(nDelPosition+2, "*/");
								nDelPosition = findIndexContents(cppFileContents,strDataDelPort,nDelPosition+2,false);
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
							}else{
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nPortNameIndex+portModel.getName().length(),false);
							}
						}else{
							break;
						}
					}
				}
			}
			else if(portModel instanceof OPRoSDataOutPortElementModel){
				//HeaderFile 인스턴스 삭제
				int nIndex = findIndexContents(fileContents,portModel.getName(),0,false);
				while(nIndex!=-1){
					int nCommentStart = fileContents.lastIndexOf("\n",nIndex)+1;
					nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
					fileContents.insert(nCommentStart, "/*");
					fileContents.insert(nIndex+2, "*/");
					nIndex = findIndexContents(fileContents,portModel.getName(),nIndex+2,false);
				}
				//SourceFile addPort문 삭제
				String strCPP="void "+strCompName+"::portSetup()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				int nPortNameIndex=0;
				if(nCppIndex!=-1){
					String strDataDelPort="addPort";
					int nDelPosition = findIndexContents(cppFileContents,strDataDelPort,nCppIndex,false);
					nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
					int nParaseIndex =0;
					while(nDelPosition!=-1&&nPortNameIndex!=-1){
						nParaseIndex = findIndexContents(cppFileContents,")",nDelPosition,false);
						if(nParaseIndex!=-1){
							if(nPortNameIndex>nParaseIndex){
								nDelPosition = findIndexContents(cppFileContents,strDataDelPort,nParaseIndex,false);
							}
							else if(nDelPosition<nPortNameIndex && nPortNameIndex<nParaseIndex){
								int nCommentStart = nDelPosition;
								nDelPosition = findIndexContents(cppFileContents,";",nDelPosition,false)+1;
								cppFileContents.insert(nCommentStart, "/*");
								cppFileContents.insert(nDelPosition+2, "*/");
								nDelPosition = findIndexContents(cppFileContents,strDataDelPort,nDelPosition+2,false);
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
							}else{
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nPortNameIndex+portModel.getName().length(),false);
							}
						}else{
							break;
						}
					}
				}
			}
		}
		//dataPort add h modify
		int classIndex = findIndexContents(fileContents,"class",0,false);
		if(classIndex==-1)
			return;
		it=model.getAddPortList();
		boolean isAdded=false;
		int protectedIndex=0;
		if(it.hasNext()){
			protectedIndex=addString.length();
			addString="\nprotected:\n//data\n";
		}
		while(it.hasNext()){
			 portModel = (OPRoSPortElementBaseModel)it.next();
			 if(portModel instanceof OPRoSDataInPortElementModel){
				 isAdded=true;
				 //HeaderFile 인스턴스 선언
				 addString = addString+"\tInputDataPort< "+portModel.getType()+" > "+portModel.getName()+";\n";
				 //SourceFile 생성자 데이터포트 생성 추가
				 String strCPP=strCompName+"(";
				 int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				 while(nCppIndex!=-1){
					 nCppIndex = findIndexContents(cppFileContents,")",nCppIndex,false);
					 int nBraceIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
					 int nColonIndex = findIndexContents(cppFileContents,":",nCppIndex,false);
					 if(cppFileContents.charAt(nCppIndex+1)!=';'){
						 if(nCppIndex<nColonIndex&&nColonIndex<nBraceIndex){
							 cppFileContents.insert(nBraceIndex-1, "\n\t,"+portModel.getName()+"(OPROS_"+((OPRoSDataInPortElementModel)portModel).getQueueingPolicy()+","+((OPRoSDataInPortElementModel)portModel).getQueueSize()+")");
						 }else{
							 cppFileContents.insert(nCppIndex+1, "\n\t:"+portModel.getName()+"(OPROS_"+((OPRoSDataInPortElementModel)portModel).getQueueingPolicy()+","+((OPRoSDataInPortElementModel)portModel).getQueueSize()+")"); 
						 }
					 }
					 nCppIndex = findIndexContents(cppFileContents,strCPP,nCppIndex,false);
				 }
				 
				 //SourceFile addPort문 추가
				 strCPP="void "+strCompName+"::portSetup()";
				 nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				 if(nCppIndex!=-1){
					 nCppIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
					 cppFileContents.insert(nCppIndex+1, "\n\t//data port setup\n\taddPort(\""+portModel.getName()+"\", &"+portModel.getName()+");\n");
				 }
			 }
			 else if(portModel instanceof OPRoSDataOutPortElementModel){
				 isAdded=true;
				 //HeaderFile 인스턴스 선언
				 addString = addString+"\tOutputDataPort< "+portModel.getType()+" > "+portModel.getName()+";\n";
				 //SourceFile addPort문 추가
				 String strCPP="void "+strCompName+"::portSetup()";
				 int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				 if(nCppIndex!=-1){
					 nCppIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
					 cppFileContents.insert(nCppIndex+1, "\n\t//data port setup\n\taddPort(\""+portModel.getName()+"\", &"+portModel.getName()+");\n");
				 }
			 }
			 //HeaderFile UserDataType의 include문 추가
			 if(portModel instanceof OPRoSDataOutPortElementModel||portModel instanceof OPRoSDataInPortElementModel){
				 boolean bSystemType=false;
//				 for(int i=0;i<20;i++){
//					 if(OPRoSUtil.dataTypes[i].compareTo(portModel.getType())==0)
//						 bSystemType=true;
//					 else if(i>6){
//						 if(portModel.getType().startsWith(OPRoSUtil.dataTypes[i]))
//							 bSystemType=true;
//					 }
//				 }
				 for(int i=0;i<7;i++){
					 if(OPRoSUtil.dataNotBoostTypes[i].compareTo(portModel.getType())==0)
						 bSystemType=true;
//					 else if(i>6){
//						 if(portModel.getType().startsWith(OPRoSUtil.dataTypes[i]))
//							 bSystemType=true;
//					 }
				 }
				 if(!bSystemType&&findIndexContents(fileContents,portModel.getType(),0,false)==-1&&!includeList.contains(portModel.getType())){
					 String strInclude="";
					 strInclude = "#include \""+portModel.getType()+".h\"";
					 int nIndex = findIndexContents(fileContents,strInclude,0,false);
					 if(nIndex==-1){
						 fileContents.insert(classIndex-1, "\n"+strInclude+"\n");
						 includeList.add(portModel.getType());
						 classIndex = findIndexContents(fileContents,"class",0,false);
						if(classIndex==-1)return;
					 }
				 }
			 }
		}
		if(!isAdded){
			addString=addString.substring(0, protectedIndex);
		}
		isAdded=false;
		
		//eventPort delete h modify
		it = model.getDelPortList();
		while(it.hasNext()){
			portModel = (OPRoSPortElementBaseModel)it.next();
			if(portModel instanceof OPRoSEventInPortElementModel){
				//HeaderFile EventPort Instance delete
				int nIndex = findIndexContents(fileContents,portModel.getName(),0,false);
				while(nIndex!=-1){
					int nCommentStart = fileContents.lastIndexOf("\n",nIndex)+1;
					nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
					fileContents.insert(nCommentStart, "/*");
					fileContents.insert(nIndex+2, "*/");
					nIndex = findIndexContents(fileContents,portModel.getName(),nIndex+2,false);
				}
				//SourceFile addPort Statement delete
				String strCPP="void "+strCompName+"::portSetup()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				int nPortNameIndex=0;
				if(nCppIndex!=-1){
					String strEventDelPort = "addPort";
					int nDelPosition = findIndexContents(cppFileContents,strEventDelPort,nCppIndex,false);
					nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
					int nParaseIndex =0;
					while(nDelPosition!=-1&&nPortNameIndex!=-1){
						nParaseIndex = findIndexContents(cppFileContents,")",nDelPosition,false);
						if(nParaseIndex!=-1){
							if(nPortNameIndex>nParaseIndex){
								nDelPosition = findIndexContents(cppFileContents,strEventDelPort,nParaseIndex,false);
							}
							else if(nDelPosition<nPortNameIndex && nPortNameIndex<nParaseIndex){
								int nCommentStart=nDelPosition;
								nDelPosition = findIndexContents(cppFileContents,";",nDelPosition,false)+1;
								cppFileContents.insert(nCommentStart, "/*");
								cppFileContents.insert(nDelPosition+2, "*/");
								nDelPosition = findIndexContents(cppFileContents,strEventDelPort,nDelPosition+2,false);
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
							}else{
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nPortNameIndex+portModel.getName().length(),false);
							}
						}else{
							break;
						}
					}
					strEventDelPort=portModel.getName()+".setOwner(";
					int nDelPosition1 = findIndexContents(cppFileContents,strEventDelPort,0,false);
					if(nDelPosition1!=-1){
						int nBraceIndex = findIndexContents(cppFileContents,"}",nCppIndex,false);
						if(nCppIndex<nDelPosition1&&nDelPosition1<nBraceIndex){
							int nCommentStart=nDelPosition1;
							nDelPosition1 = findIndexContents(cppFileContents,";",nDelPosition1,false)+1;
							cppFileContents.insert(nCommentStart, "/*");
							cppFileContents.insert(nDelPosition1+2, "*/");
						}
					}
				}
			}
			else if(portModel instanceof OPRoSEventOutPortElementModel){
				//HeaderFile EventPort Instance delete
				int nIndex = findIndexContents(fileContents,portModel.getName(),0,false);
				while(nIndex!=-1){
					int nCommentStart = fileContents.lastIndexOf("\n",nIndex)+1;
					
					nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
					fileContents.insert(nCommentStart, "/*");
					fileContents.insert(nIndex+2, "*/");
					nIndex = findIndexContents(fileContents,portModel.getName(),nIndex+2,false);
				}
				String strCPP="void "+strCompName+"::portSetup()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				if(nCppIndex!=-1){
					String strEventDelPort = "addPort";
					int nDelPosition = findIndexContents(cppFileContents,strEventDelPort,nCppIndex,false);
					int nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
					int nParaseIndex =0;
					while(nDelPosition!=-1&&nPortNameIndex!=-1){
						nParaseIndex = findIndexContents(cppFileContents,")",nDelPosition,false);
						if(nParaseIndex!=-1){
							if(nPortNameIndex>nParaseIndex){
								nDelPosition = findIndexContents(cppFileContents,strEventDelPort,nParaseIndex,false);
							}
							else if(nDelPosition<nPortNameIndex && nPortNameIndex<nParaseIndex){
								int nCommentStart=nDelPosition;
								
								nDelPosition = findIndexContents(cppFileContents,";",nDelPosition,false)+1;
								cppFileContents.insert(nCommentStart, "/*");
								cppFileContents.insert(nDelPosition+2, "*/");
								nDelPosition = findIndexContents(cppFileContents,strEventDelPort,nDelPosition+2,false);
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,false);
							}else{
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nPortNameIndex+portModel.getName().length(),false);
							}
						}else{
							break;
						}
					}
				}
			}
		}
		//eventPort add h modify
		classIndex = findIndexContents(fileContents,"class",0,false);
		if(classIndex==-1)
			return;
		it=model.getAddPortList();
		if(it.hasNext()){
			protectedIndex=addString.length();
			addString=addString + "\nprotected:\n// event\n";
		}
		while(it.hasNext()){
			portModel=(OPRoSPortElementBaseModel)it.next();
			if(portModel instanceof OPRoSEventInPortElementModel){
				isAdded=true;
				addString = addString+"\tInputEventPort< "+portModel.getType()+" > "+portModel.getName()+";\n";
				
				String strCPP="void "+strCompName+"::portSetup()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				if(nCppIndex!=-1){
					String strEventAddPort="addPort(\""+portModel.getName()+"\",";
					if(findIndexContents(cppFileContents,strEventAddPort,0,false)==-1){
						nCppIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
						cppFileContents.insert(nCppIndex+1, "\n\t//event port setup\n\taddPort(\""+portModel.getName()+"\", &"
								+portModel.getName()+");\n\t"+portModel.getName()+".setOwner(this);\n");
					}
				}
			}
			else if(portModel instanceof OPRoSEventOutPortElementModel){
				isAdded=true;
				addString = addString+"\tOutputEventPort< "+portModel.getType()+" > "+portModel.getName()+";\n";
				String strCPP="void "+strCompName+"::portSetup()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				if(nCppIndex!=-1){
					String strEventAddPort="addPort(\""+portModel.getName()+"\",";
					if(findIndexContents(cppFileContents,strEventAddPort,0,false)==-1){
						nCppIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
						cppFileContents.insert(nCppIndex+1, "\n\t//event port setup\n\taddPort(\""+portModel.getName()+"\", &"+portModel.getName()+");\n");
					}
				}
			}
			if(portModel instanceof OPRoSEventOutPortElementModel||portModel instanceof OPRoSEventInPortElementModel){
				boolean bSystemType=false;
//				for(int i=0;i<20;i++){
//					if(OPRoSUtil.dataTypes[i].compareTo(portModel.getType())==0)
//						bSystemType=true;
//				}
				for(int i=0;i<7;i++){
					if(OPRoSUtil.dataNotBoostTypes[i].compareTo(portModel.getType())==0)
						bSystemType=true;
				}
				if(!bSystemType&&findIndexContents(fileContents,portModel.getType(),0,false)==-1&&!includeList.contains(portModel.getType())){
					 String strInclude="";
					 strInclude = "#include \""+portModel.getType()+".h\"";
					 int nIndex = findIndexContents(fileContents,strInclude,0,false);
					 if(nIndex==-1){
						 fileContents.insert(classIndex-1, "\n"+strInclude+"\n");
						 includeList.add(portModel.getType());
						classIndex = findIndexContents(fileContents,"class",0,false);
						if(classIndex==-1)return;
					 }
				}
			}
		}
		if(!isAdded){
			addString=addString.substring(0, protectedIndex);
		}
		isAdded=false;

		//methodPort delete h modify
		it = model.getDelPortList();
		while(it.hasNext()){
			portModel = (OPRoSPortElementBaseModel)it.next();
			if(portModel instanceof OPRoSServiceRequiredPortElementModel){
				int nIndex = findIndexContents(fileContents,"ptr"+portModel.getName(),0,false);
				while(nIndex!=-1){
					int nCommentStart = fileContents.lastIndexOf("\n",nIndex)+1;
					nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
					fileContents.insert(nCommentStart,"/*");
					fileContents.insert(nIndex+2, "*/");
					nIndex = findIndexContents(fileContents,"ptr"+portModel.getName(),nIndex+2,false);
				}
				String strCPP="void "+strCompName+"::portSetup()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				int nPortNameIndex=0;
				if(nCppIndex!=-1){
					String strMethodDelPort="addPort";
					int nDelPosition = findIndexContents(cppFileContents,strMethodDelPort,nCppIndex,false);
					nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,true);
					int nParaseIndex =0;
					while(nDelPosition!=-1&&nPortNameIndex!=-1){
						nParaseIndex = findIndexContents(cppFileContents,")",nDelPosition,false);
						if(nParaseIndex!=-1){
							if(nPortNameIndex>nParaseIndex){
								nDelPosition = findIndexContents(cppFileContents,strMethodDelPort,nParaseIndex,false);
							}
							else if(nDelPosition<nPortNameIndex && nPortNameIndex<nParaseIndex){
								//addPort 찾음.
								String strVariableName = findVarName(cppFileContents,nPortNameIndex);
								if(!strVariableName.isEmpty()){
									int nDecla = findIndexContents(cppFileContents,strVariableName,0,false);
									while(nDecla!=-1){
										int nCommentStart = cppFileContents.lastIndexOf("\n",nDecla)+1;
										nDecla = findIndexContents(cppFileContents,";",nDecla,false)+1;
										cppFileContents.insert(nCommentStart, "/*");
										cppFileContents.insert(nDecla+2, "*/");
										nDecla = findIndexContents(cppFileContents,strVariableName,nDecla+2,false);
									}
									break;
								}
							}else{
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nPortNameIndex+portModel.getName().length(),true);
							}
						}else{
							break;
						}
					}
				}
				
			}
			else if(portModel instanceof OPRoSServiceProvidedPortElementModel){
				int nIndex = findIndexContents(fileContents,"ptr"+portModel.getName(),0,false);
				while(nIndex!=-1){
					int nCommentStart = fileContents.lastIndexOf("\n",nIndex)+1;
					nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
					fileContents.insert(nCommentStart,"/*");
					fileContents.insert(nIndex+2, "*/");
					nIndex = findIndexContents(fileContents,"ptr"+portModel.getName(),nIndex+2,false);
				}
				String strCPP="void "+strCompName+"::portSetup()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				int nPortNameIndex=0;
				if(nCppIndex!=-1){
					String strMethodDelPort="addPort";
					int nDelPosition = findIndexContents(cppFileContents,strMethodDelPort,nCppIndex,false);
					nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nDelPosition,true);
					int nParaseIndex =0;
					while(nDelPosition!=-1&&nPortNameIndex!=-1){
						nParaseIndex = findIndexContents(cppFileContents,")",nDelPosition,false);
						if(nParaseIndex!=-1){
							if(nPortNameIndex>nParaseIndex){
								nDelPosition = findIndexContents(cppFileContents,strMethodDelPort,nParaseIndex,false);
							}
							else if(nDelPosition<nPortNameIndex && nPortNameIndex<nParaseIndex){
								//addPort 찾음.
								String strVariableName = findVarName(cppFileContents,nPortNameIndex);
								if(!strVariableName.isEmpty()){
									int nDecla = findIndexContents(cppFileContents,strVariableName,0,false);
									while(nDecla!=-1){
										int nCommentStart = cppFileContents.lastIndexOf("\n",nDecla)+1;
										nDecla = findIndexContents(cppFileContents,";",nDecla,false)+1;
										cppFileContents.insert(nCommentStart, "/*");
										cppFileContents.insert(nDecla+2, "*/");
										nDecla = findIndexContents(cppFileContents,strVariableName,nDecla+2,false);
									}
									break;
								}
							}else{
								nPortNameIndex = findIndexContents(cppFileContents,portModel.getName(),nPortNameIndex+portModel.getName().length(),true);
							}
						}else{
							break;
						}
					}
				}
			}
		}
		//methodPort add h modify
		classIndex = findIndexContents(fileContents,"class",0,false);
		if(classIndex==-1)
			return;
		it=model.getAddPortList();
		if(it.hasNext()){
			protectedIndex=addString.length();
			addString=addString+"\nprotected:\n// service\n";
		}
		while(it.hasNext()){
			portModel=(OPRoSPortElementBaseModel)it.next();
			if(portModel instanceof OPRoSServiceProvidedPortElementModel){
				isAdded=true;
				addString=addString+"// method for provider\n\tI"+portModel.getType()+" *ptr"+portModel.getName()+";\n";
				if(findIndexContents(fileContents,portModel.getType()+"Provided",0,false)==-1&&!includeList.contains(portModel.getType()+"Provided")){
					 String strInclude="";
					 strInclude = "#include \""+portModel.getType()+"Provided.h\"";
					 int nIndex = findIndexContents(fileContents, strInclude, 0, false);
					 if(nIndex==-1){
						 fileContents.insert(classIndex-1, "\n"+strInclude+"\n");
						 includeList.add(portModel.getType()+"Provided");
						 provideSet.add(portModel);
						 classIndex = findIndexContents(fileContents,"class",0,false);
						 if(classIndex==-1)return;
					 }
				 }
				
				String strCPP="public I"+portModel.getType();
				 int nCppIndex = findIndexContents(fileContents,strCPP,0,false);
				 if(nCppIndex==-1){
					 int nBraceIndex = findIndexContents(fileContents,"{",classIndex,false);
					 fileContents.insert(nBraceIndex-1, "\n\t,"+strCPP);
				 }
				 
				strCPP="void "+strCompName+"::portSetup()";
				nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				if(nCppIndex!=-1){
					nCppIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
					int nServiceCnt=getServiceCnt(cppFileContents,strCompName);
					cppFileContents.insert(nCppIndex+1, "\n\tProvidedServicePort *pa"+String.valueOf(nServiceCnt+1)+";\n"
						+"\tpa"+String.valueOf(nServiceCnt+1)+"=new "+portModel.getType()+"Provided(this);\n"
						+"\taddPort(\""+portModel.getName()+"\",pa"+String.valueOf(nServiceCnt+1)+");\n");
				}
			}else if(portModel instanceof OPRoSServiceRequiredPortElementModel){
				isAdded=true;
				addString=addString+"// method for required\n\t"+portModel.getType()+"Required *ptr"+portModel.getName()+";\n";
				if(findIndexContents(fileContents,portModel.getType()+"Required",0,false)==-1&&!includeList.contains(portModel.getType()+"Required")){
					 String strInclude="";
					 strInclude = "\n#include \""+portModel.getType()+"Required.h\"\n";
					 int nIndex = findIndexContents(fileContents,strInclude,0,false);
					 if(nIndex==-1){
						 fileContents.insert(classIndex-1, "\n"+strInclude+"\n");
						 includeList.add(portModel.getType()+"Required");
						 classIndex = findIndexContents(fileContents,"class",0,false);
						 if(classIndex==-1)return;
					 }
				 }
				String strCPP=strCompName+"::"+strCompName+"()";
				int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				if(nCppIndex!=-1){
					int nAddPosition = findIndexContents(cppFileContents,"{",nCppIndex,false);
					if(nAddPosition!=-1){
						cppFileContents.insert(nAddPosition+1, "\n\tptr"+portModel.getName()+" = NULL;\n");
					}
				 }
				 strCPP=strCompName+"::"+strCompName+"(const std::string &name)";
				 nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				 if(nCppIndex!=-1){
					int nAddPosition = findIndexContents(cppFileContents,"{",nCppIndex,false);
					if(nAddPosition!=-1){
						cppFileContents.insert(nAddPosition+1, "\n\tptr"+portModel.getName()+" = NULL;\n");
					}
				 }
				strCPP="void "+strCompName+"::portSetup()";
				nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
				if(nCppIndex!=-1){
					nCppIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
					cppFileContents.insert(nCppIndex+1,"\n\tptr"+portModel.getName()+"=new "+portModel.getType()+"Required();\n"
							+"\taddPort(\""+portModel.getName()+"\",ptr"+portModel.getName()+");\n");
				}
			}
		}
		
		if(!isAdded){
			addString=addString.substring(0, protectedIndex);
		} else {
			protectedIndex=addString.length();
		}
		isAdded=false;
		
		
		//변경사항이 있는 서비스 타입중 소스코드에 적용할 서비스타입 선별
		ArrayList<OPRoSServiceTypeElementModel> changeServiceTypes = model.getChangeServiceType();
		ArrayList<OPRoSServiceTypeElementModel> delServiceTypes = new ArrayList<OPRoSServiceTypeElementModel>();
		for(OPRoSPortElementBaseModel port : model.getPortModel()) {
			if(port instanceof OPRoSServiceProvidedPortElementModel) {
				OPRoSServiceProvidedPortElementModel provide = (OPRoSServiceProvidedPortElementModel)port;
				for(OPRoSServiceTypeElementModel serviceType : changeServiceTypes) {
					if(provide.getReference().equals(serviceType.getServiceTypeFileName())) {
						if(!provideSet.contains(port)) {
							provideSet.add(port);
							delServiceTypes.add(serviceType);
						}
						break;
					}
				}
			}
		}
		
		
		
		// 서비스 타입내용 제거
		for(OPRoSServiceTypeElementModel serviceType : delServiceTypes) {
			Document serviceTypeDoc = serviceType.getServiceTypeDoc();
			if(serviceType.getOldServiceTypeDoc() != null) {
				serviceTypeDoc = serviceType.getOldServiceTypeDoc();
			}				
			Iterator eleIt = serviceTypeDoc.getRootElement().getChildren().iterator();
			while(eleIt.hasNext()){
				Element ele = (Element)eleIt.next();
				String strTypeName = ele.getChildText("type_name");
				Iterator methodIt = ele.getChildren("method").iterator();
				while(methodIt.hasNext()){
					String strMethod="";
					String strCppMethod="";
					Element methodEle = (Element)methodIt.next();
					strMethod = "virtual "+ methodEle.getAttributeValue("return_type")+" "+methodEle.getAttributeValue("name")+"(";
					strCppMethod = methodEle.getAttributeValue("return_type")+" "+strCompName+"::"+methodEle.getAttributeValue("name")+"(";
					Iterator paramIt = methodEle.getChildren("param").iterator();
					if(paramIt.hasNext()){
						Element paramEle = (Element)paramIt.next();
						strMethod = strMethod + paramEle.getChildText("type")+" "+paramEle.getChildText("name");
						strCppMethod = strCppMethod + paramEle.getChildText("type")+" "+paramEle.getChildText("name");
					}
					while(paramIt.hasNext()){
						Element paramEle = (Element)paramIt.next();
						strMethod = strMethod + ","+paramEle.getChildText("type")+" "+paramEle.getChildText("name");
						strCppMethod = strCppMethod + ","+paramEle.getChildText("type")+" "+paramEle.getChildText("name");
					}
					strMethod = strMethod + ");";
					
					//header 주석처리
					int nIndex = findIndexContents(fileContents, strMethod, 0, false);
					while(nIndex!=-1){
						int nCommentStart = fileContents.lastIndexOf("\n", nIndex)+1;
						nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
						fileContents.insert(nCommentStart,"/*");
						fileContents.insert(nIndex+2, "*/");
						nIndex = findIndexContents(fileContents, strMethod, nIndex+2, false);
					}
					
					
					strCppMethod = strCppMethod + ")";
					
					String strCPP=strCompName+"::~"+strCompName+"()";
					int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
					if(nCppIndex!=-1){
						nCppIndex = findIndexContents(cppFileContents,"}",nCppIndex,false);
					}
					
					//cpp 주석처리
					nCppIndex = findIndexContents(cppFileContents, strCppMethod, 0, false);
					while(nCppIndex!=-1){
						int nCommentStart = cppFileContents.lastIndexOf("\n", nCppIndex)+1;
						nCppIndex=findIndexContents(cppFileContents,"}", nCppIndex, false)+1;
						cppFileContents.insert(nCommentStart,"/*");
						cppFileContents.insert(nCppIndex + 2, "*/");
						nCppIndex = findIndexContents(cppFileContents, strCppMethod, nCppIndex+2, false);
					}
				}						
				
			}
			serviceType.setOldServiceTypeDoc(null);
					
 		}
		
		Iterator<OPRoSPortElementBaseModel> PMIt = provideSet.iterator();
		if(PMIt.hasNext()){
			protectedIndex = addString.length();
			addString = addString + "\npublic:\n";
		}
		Document serviceTypeDoc=null;
		
		// 서비스 타입내용 추가
		while(PMIt.hasNext()){
			OPRoSPortElementBaseModel serviceModel=PMIt.next();
			if(serviceModel!=null){
				if(serviceModel instanceof OPRoSServiceProvidedPortElementModel)
					serviceTypeDoc = model.compEle.getServiceTypeDoc(((OPRoSServiceProvidedPortElementModel)serviceModel).getReference());
			}
			if(serviceTypeDoc!=null){
				Iterator eleIt = serviceTypeDoc.getRootElement().getChildren().iterator();
				while(eleIt.hasNext()){
					Element ele = (Element)eleIt.next();
					String strTypeName = ele.getChildText("type_name");
					if(strTypeName.compareTo(serviceModel.getType())==0){
						Iterator methodIt = ele.getChildren("method").iterator();
						while(methodIt.hasNext()){
							String strMethod="";
							String strCppMethod="";
							Element methodEle = (Element)methodIt.next();
							strMethod = "virtual "+ methodEle.getAttributeValue("return_type")+" "+methodEle.getAttributeValue("name")+"(";
							strCppMethod = methodEle.getAttributeValue("return_type")+" "+strCompName+"::"+methodEle.getAttributeValue("name")+"(";
							Iterator paramIt = methodEle.getChildren("param").iterator();
							if(paramIt.hasNext()){
								Element paramEle = (Element)paramIt.next();
								strMethod = strMethod + paramEle.getChildText("type")+" "+paramEle.getChildText("name");
								strCppMethod = strCppMethod + paramEle.getChildText("type")+" "+paramEle.getChildText("name");
							}
							while(paramIt.hasNext()){
								Element paramEle = (Element)paramIt.next();
								strMethod = strMethod + ","+paramEle.getChildText("type")+" "+paramEle.getChildText("name");
								strCppMethod = strCppMethod + ","+paramEle.getChildText("type")+" "+paramEle.getChildText("name");
							}
							strMethod = strMethod + ");";
							strCppMethod = strCppMethod + ")";
							if(findIndexContents(fileContents,strMethod,0,false)==-1) {
								isAdded = true;
								addString = addString+strMethod+"\n";
							}
								
							strMethod="";
							
							String strCPP=strCompName+"::~"+strCompName+"()";
							int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
							if(nCppIndex!=-1){
								nCppIndex = findIndexContents(cppFileContents,"}",nCppIndex,false);
								if(findIndexContents(cppFileContents,strCppMethod,0,false)==-1)
									cppFileContents.insert(nCppIndex+1, "\n"+strCppMethod+"\n{\n\t//user code hear\n}\n");
								strMethod="";
							}							
						}						
					}
				}
			}
		}
		
		if(!isAdded){
			addString=addString.substring(0, protectedIndex);
		} else {
			protectedIndex=addString.length();
		}
		isAdded=false;
		
		//모니터링 변수 내용 추가
		ArrayList<MonitoringVariableModel> addMonitoring = model.getAddMonitoringList();		
		classIndex = findIndexContents(fileContents, "class", 0, false);
		if(classIndex==-1)
			return;
		if(addMonitoring.size() > 0){
			protectedIndex=addString.length();
			addString = addString+"\nprotected:\n// monitor variables\n";
		}
		
		for(MonitoringVariableModel monitor : addMonitoring) {			
			String hStr = monitor.getType()+" m_"+monitor.getName();
			
			//중복체크
			if(findIndexContents(fileContents, hStr, 0, false) == -1) {
				isAdded=true;
				addString = addString+"\t"+hStr+";\n";
			} else if(findIndexContents(fileContents, "/*\t"+hStr+"*/", 0, false) != -1) {
				isAdded=true;
				addString = addString+"\t"+hStr+";\n";
			}
			 
			String strCPP = "void "+strCompName+"::portSetup()";
			int nCppIndex = findIndexContents(cppFileContents,strCPP,0,false);
			if(nCppIndex != -1){
				String strCppMethod = "EXPORT_VARIABLE(\""+monitor.getName()+"\", m_"+monitor.getName()+");";
				if(findIndexContents(cppFileContents, strCppMethod, nCppIndex, false) == -1) {
					nCppIndex = findIndexContents(cppFileContents,"{",nCppIndex,false);
					cppFileContents.insert(nCppIndex+1, "\n\t// export variable setup\n" +
							"\t"+strCppMethod+"\n");
				}				
			}
		}
		
		if(!isAdded){
			addString=addString.substring(0, protectedIndex);
		}
		isAdded=false;
		
		//모니터링 변수내용 삭제
		ArrayList<MonitoringVariableModel> delMonitoring = model.getDelMonitoringList();
		for(MonitoringVariableModel monitor : delMonitoring) {	
			String name = monitor.getName();
			if(monitor.getOldName() != null && !monitor.getOldName().equals("")) {
				name = monitor.getOldName();				
			}
			
			String type = monitor.getType();
			if(monitor.getOldType() != null && !monitor.getOldType().equals("")) {
				type = monitor.getOldType();
			}
			
			String hStr = type+" m_"+name;
			//header 주석처리
			int nIndex = findIndexContents(fileContents, hStr, 0, false);
			while(nIndex!=-1){
				int nCommentStart = fileContents.lastIndexOf("\n", nIndex)+1;
				nIndex=findIndexContents(fileContents,";",nIndex,false)+1;
				fileContents.insert(nCommentStart,"/*");
				fileContents.insert(nIndex+2, "*/");
				nIndex = findIndexContents(fileContents, hStr, nIndex+2, false);
			}
			
			//cpp 주석처리
			String strCppMethod = "EXPORT_VARIABLE(\""+name+"\", m_"+name+");";
			
			//변수 타입만 바뀐 변경모델일 경우 주석처리를 안하고 나머지 경우는 모두 주석처리
			if(monitor.getOldType() != null && !monitor.getOldType().equals("")) {
				if(monitor.getOldName() != null && !monitor.getOldName().equals("")) {
					int nCppIndex = findIndexContents(cppFileContents, strCppMethod, 0, false);
					while(nCppIndex!=-1){
						int nCommentStart = cppFileContents.lastIndexOf("\n", nCppIndex)+1;
						nCppIndex = findIndexContents(cppFileContents, ";", nCppIndex, false)+1;
						cppFileContents.insert(nCommentStart,"/*");
						cppFileContents.insert(nCppIndex + 2, "*/");
						nCppIndex = findIndexContents(cppFileContents, strCppMethod, nCppIndex+2, false);
					}
				}
			} else {
				int nCppIndex = findIndexContents(cppFileContents, strCppMethod, 0, false);
				while(nCppIndex!=-1){
					int nCommentStart = cppFileContents.lastIndexOf("\t", nCppIndex)+1;
					nCppIndex = findIndexContents(cppFileContents, ";", nCppIndex, false)+1;
					cppFileContents.insert(nCommentStart,"/*");
					cppFileContents.insert(nCppIndex + 2, "*/");
					nCppIndex = findIndexContents(cppFileContents, strCppMethod, nCppIndex+2, false);
				}
			}
			
			
			monitor.setOldName("");
			monitor.setOldType("");
 		}
		
				
		classIndex = findIndexContents(fileContents,"class",0,false);
		if(classIndex==-1)
			return;
		int index = findIndexContents(fileContents,"{",classIndex,false);
		if(index==-1)
			return;
		
		fileContents.insert(index+1, addString);
		
		
		
		IFile iCppFile = getProject().getFile(model.getComponentModel().getComponentName()
				+ File.separator
				+ model.getComponentModel().getComponentName()+".cpp");
		IFile iheaderFile = getProject().getFile(model.getComponentModel().getComponentName()
				+ File.separator
				+ model.getComponentModel().getComponentName()+".h");
		
		if(iCppFile != null && iCppFile.isAccessible())
			setFile(cppFileContents.toString(), iCppFile);
		
		if(iheaderFile != null && iheaderFile.isAccessible())
			setFile(fileContents.toString(), iheaderFile);
		
		/*
		try
		{
			
			System.out.println(fileContents.toString());
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(headerFile));
			buffWrite.write(fileContents.toString(), 0, fileContents.length());
			buffWrite.flush();
			buffWrite.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		try
		{
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(cppFile));
			buffWrite.write(cppFileContents.toString(), 0, cppFileContents.length());
			buffWrite.flush();
			buffWrite.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		*/
	}
	
	public void setFile(String str, IFile file) {
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(str.getBytes());
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());			
			file.setContents(bais, true, false, null);
			
			baos.close();
			bais.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//만일 ""안에 찾는 내용이 있을 경우 오류가 날 수 있음.
	private int findIndexContents(StringBuffer fileContents,String str,int index, boolean bQuoter){
		int nIndex=index;
		int nCommentStartIndex=0;
		int nCommentEndIndex=0;
		int nCommentIndex=0;
		nCommentStartIndex= fileContents.indexOf("/*",nCommentStartIndex);
		if(!bQuoter)
			nCommentStartIndex=findIndexContentsQuotation(fileContents,"/*",nCommentStartIndex);
		nCommentEndIndex= fileContents.indexOf("*/",nCommentEndIndex);
		if(!bQuoter)
			nCommentEndIndex=findIndexContentsQuotation(fileContents,"*/",nCommentEndIndex);
		nIndex=fileContents.indexOf(str,nIndex);
		if(nIndex==-1)
			return nIndex;
		else{
			if(str.compareTo("{")!=0&&str.compareTo("}")!=0&&str.compareTo("(")!=0&&str.compareTo(")")!=0&&str.compareTo(";")!=0&&str.compareTo(":")!=0&&str.compareTo(",")!=0)
				nIndex = findIndexContents2(fileContents,str,nIndex);
			if(!bQuoter)
				nIndex = findIndexContentsQuotation(fileContents,str,nIndex);
			if(nIndex==-1)
				return nIndex;
		}
		
		while(nCommentStartIndex!=-1&&nCommentEndIndex!=-1){
			//코멘트 이전에 존재함.
			if(nIndex<nCommentStartIndex){
				return nIndex;
				//코멘트 안에 존재함.
			}else if(nCommentStartIndex<nIndex&&nIndex<nCommentEndIndex){
				nIndex=fileContents.indexOf(str,nIndex+str.length());
				if(nIndex==-1)
					return nIndex;
				else{
					if(str.compareTo("{")!=0&&str.compareTo("}")!=0&&str.compareTo("(")!=0&&str.compareTo(")")!=0&&str.compareTo(";")!=0&&str.compareTo(":")!=0&&str.compareTo(",")!=0)
						nIndex = findIndexContents2(fileContents,str,nIndex);
					if(!bQuoter)
						nIndex = findIndexContentsQuotation(fileContents,str,nIndex);
					if(nIndex==-1)
						return nIndex;
				}
				//코멘트이후에 나옴.
			}else if(nIndex>nCommentEndIndex){
				nCommentIndex = fileContents.indexOf("/*",nCommentStartIndex+2);
				if(!bQuoter)
					nCommentIndex=findIndexContentsQuotation(fileContents,"/*",nCommentIndex);
				//더이상의 코멘트는 없을 경우
				if(nCommentIndex==-1){
					nCommentIndex=fileContents.lastIndexOf("//",nIndex);
					int nCR=fileContents.lastIndexOf("\n",nIndex);
					if(nCommentIndex==-1)
						return nIndex;
					else if(nCommentIndex>nCR){
						nIndex=fileContents.indexOf(str,nIndex+str.length());
						if(nIndex==-1)
							return nIndex;
						else{
							if(str.compareTo("{")!=0&&str.compareTo("}")!=0&&str.compareTo("(")!=0&&str.compareTo(")")!=0&&str.compareTo(";")!=0&&str.compareTo(":")!=0&&str.compareTo(",")!=0)
								nIndex = findIndexContents2(fileContents,str,nIndex);
							if(!bQuoter)
								nIndex = findIndexContentsQuotation(fileContents,str,nIndex);
							if(nIndex==-1)
								return nIndex;
						}
					}else{
						return nIndex;
					}
					//새로운 코멘트 시작
				}else if(nCommentIndex>nCommentEndIndex){
					nCommentStartIndex=nCommentIndex;
					nCommentEndIndex= fileContents.indexOf("*/",nCommentEndIndex+2);
					if(!bQuoter)
						nCommentEndIndex=findIndexContentsQuotation(fileContents,"*/",nCommentEndIndex);
					//코멘트 안의 코맨트 시작
				}else if(nCommentIndex<nCommentEndIndex){
					nCommentStartIndex=fileContents.indexOf("/*",nCommentStartIndex+2);
					if(!bQuoter)
						nCommentStartIndex=findIndexContentsQuotation(fileContents,"/*",nCommentStartIndex);
				}
			}
		}
		return -1;
	}
	
	private int findIndexContents2(StringBuffer fileContents,String str,int nIndex){
		while(nIndex!=-1){
			char ch = fileContents.charAt(nIndex-1);
			if((ch>='0'&&ch<='9')||(ch>='A'&&ch<='Z')||(ch>='a'&&ch<='z')||(ch=='_')||(ch=='~')){
				nIndex=fileContents.indexOf(str,nIndex+str.length());
			}else{
				ch = fileContents.charAt(nIndex+str.length());
				if((ch>='0'&&ch<='9')||(ch>='A'&&ch<='Z')||(ch>='a'&&ch<='z')||(ch=='_')){
					if(fileContents.charAt(nIndex+str.length()-1)=='(')//ComponentName+( 형태를 찾기위해
						return nIndex;
					nIndex=fileContents.indexOf(str,nIndex+str.length());
				}else{
					return nIndex;
				}
			}
		}
		return -1;
	}
	
	private int findIndexContentsQuotation(StringBuffer fileContents,String str,int nIndex){
		int nQuotation=-1;
		while(nIndex!=-1){
			nQuotation=fileContents.indexOf("\"",nQuotation+1);
			if(nQuotation==-1)
				return nIndex;
			if(nQuotation<nIndex){
				nQuotation=fileContents.indexOf("\"",nQuotation+1);
				if(nQuotation>nIndex){
					nIndex=fileContents.indexOf(str,nIndex+str.length()); 
				}
			}else{
				return nIndex;
			}
		}
		return -1;
	}
	
	private int getServiceCnt(StringBuffer fileContents,String strCompName){
		int nCnt=0;
		String strCPP="void "+strCompName+"::portSetup()";
		int nCppIndex = findIndexContents(fileContents,strCPP,0,false);
		int nPort1=fileContents.indexOf("ProvidedServicePort ",nCppIndex);
		while(nPort1!=-1){
			String strPortNo="";
			nPort1=fileContents.indexOf(";",nPort1);
			strPortNo=fileContents.substring(nPort1-1,nPort1);
			int nFindCnt=Integer.valueOf(strPortNo);
			if(nCnt<nFindCnt)
				nCnt=nFindCnt;
			nPort1=fileContents.indexOf("ProvidedServicePort ",nPort1);
		}
		return nCnt;
	}
	
	private int findDelpositionConstructor(StringBuffer cppFileContents,int nDelPosition){
		int nCnt=1;
		char ch = cppFileContents.charAt(nDelPosition-nCnt);
		while(ch!=','&&ch!=':'){
			if(ch==')')
				return -1;
			nCnt++;
			ch = cppFileContents.charAt(nDelPosition-nCnt);
		}
		return nDelPosition-nCnt;
	}
	
	private int findNextConstructor(StringBuffer cppFileContents,int nDelPosition){
		int nIndex=-1;
		int nBraceIndex = findIndexContents(cppFileContents,"{",nDelPosition,false);
		int nParaseIndex = findIndexContents(cppFileContents,"(",nDelPosition,false);
		nParaseIndex = findIndexContents(cppFileContents,"(",nParaseIndex+1,false);
		if(nDelPosition<nParaseIndex&&nParaseIndex<nBraceIndex){
			nIndex = cppFileContents.lastIndexOf(",",nParaseIndex);
		}
		return nIndex;
	}
	
	private String findVarName(StringBuffer cppFileContents,int nPortNameIndex){
		int nComma = findIndexContents(cppFileContents,",",nPortNameIndex,false);
		int nParase = findIndexContents(cppFileContents,")",nComma,false);
		String strRet = cppFileContents.substring(nComma+1, nParase);
		strRet = strRet.trim();
		return strRet;
	}

	@Override
	public void setFocus() {
		/*
		String id = "kr.co.ed.opros.ce.ui.OPRoSPerspective";
		try {
			PlatformUI.getWorkbench().showPerspective(id, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
		*/
		super.setFocus();
	}
	
	public void setOPRoSDirty(){
		isOPRoSDirty = false;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
	/**
	 * 에디터에 인풋된 파일을 리턴한다.
	 * @return
	 */
	public IFile getInputFile() {
		return inputFile;
	}
	
	protected void closeEditor(boolean save) {
		getSite().getPage().closeEditor(this, save);
	}
	
	@Override
    public void resourceChanged(IResourceChangeEvent event) {
        IResourceDelta mainDelta = event.getDelta();
        if (mainDelta == null)
            return;       
        
        IResourceDelta affectedElement = mainDelta.findMember(getInputFile().getFullPath());
        if (affectedElement != null)
            processDelta(affectedElement);
        
    }
    
    private boolean processDelta(final IResourceDelta delta) {
        Runnable changeRunnable = null;
        final IResource resource = delta.getResource();
        switch (delta.getKind()) {        
	        case IResourceDelta.REMOVED:        		        	
        		if ((IResourceDelta.MOVED_TO & delta.getFlags()) != 0) {
	                changeRunnable = new Runnable() {
	                    public void run() {
	                        IPath path = delta.getMovedToPath();
	                        IFile newFile = null;
	                        if(resource instanceof IFile)
	                        	newFile = delta.getResource().getWorkspace().getRoot().getFile(path);
	                        else if(resource instanceof IProject)
	                        	newFile = ((IProject)resource).getFile("eee.xml");
	                        
	                        if (newFile != null) {
	                            sourceChanged(newFile);
	                        }
	                    }
	                };
	            } else {
	                changeRunnable = new Runnable() {
	                    public void run() {
                    		closeEditor(false);
	                    }
	                };
	            }	        	
	            break;
	            
        }
        
        if (changeRunnable != null)
            update(changeRunnable);
        return true;
    }
    
    private void sourceChanged(IFile newFile) {
        FileEditorInput newInput = new FileEditorInput(newFile);
        setInputWithNotify(newInput);
        setPartName(newFile.getProject().getName());
    }
    
    private void update(Runnable runnable) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
        if (windows != null && windows.length > 0) {
            Display display = windows[0].getShell().getDisplay();
            display.asyncExec(runnable);
        } else
            runnable.run();
    }
	
	public IProject getProject(){
		return project;
	}
	public String getSelectionServicePortName(){
		String name="";
		if(getGraphicalViewer().getSelectedEditParts().get(0) instanceof OPRoSServiceProvidedPortElementPart){
			OPRoSServiceProvidedPortElementPart elePart;
			OPRoSServiceProvidedPortElementModel eleModel;
			elePart = (OPRoSServiceProvidedPortElementPart)getGraphicalViewer().getSelectedEditParts().get(0);
			eleModel = (OPRoSServiceProvidedPortElementModel)elePart.getModel();
			name = eleModel.getName();
		} else if(getGraphicalViewer().getSelectedEditParts().get(0) instanceof OPRoSServiceRequiredPortElementPart){
			OPRoSServiceRequiredPortElementPart elePart;
			OPRoSServiceRequiredPortElementModel eleModel;
			elePart = (OPRoSServiceRequiredPortElementPart)getGraphicalViewer().getSelectedEditParts().get(0);
			eleModel = (OPRoSServiceRequiredPortElementModel)elePart.getModel();
			name = eleModel.getName();
		}
		return name;
	}
	
	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	@Override
	public ActionRegistry getActionRegistry() {
		return super.getActionRegistry();
	}

	@Override
	public SelectionSynchronizer getSelectionSynchronizer() {
		return super.getSelectionSynchronizer();
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	
	/**
	 * 에디터에 변화가 일어났을때나 저장이됐을때
	 * PropertyChangeListener에 이를 통보한다.
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		isOPRoSDirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
}
