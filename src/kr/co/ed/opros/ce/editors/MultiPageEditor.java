package kr.co.ed.opros.ce.editors;

import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.StringUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.command.OPRoSDeleteCommandEx;
import kr.co.ed.opros.ce.guieditor.command.OPRoSPortCreateCommandEx;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;

import org.eclipse.cdt.core.model.BufferChangedEvent;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.IBuffer;
import org.eclipse.cdt.core.model.IBufferChangedListener;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.model.Field;
import org.eclipse.cdt.internal.core.model.Structure;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GEFPlugin;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

public class MultiPageEditor extends MultiPageEditorPart implements
		IResourceChangeListener, ISelectionListener, ISelectionChangedListener {
	
	public OPRoSGUIProfileEditor profileEditor;
	
	public CEditorEx cppEditor;
	
	public CEditorEx hEditor;
	
	public MultiContentOutline outline;
	
	public IPropertySheetPage page; 
	
	private EditorPart part;
	
	public int oldIndex;
	
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site, input);
		part = this;
		OPRoSActivator.getWorkspace().addResourceChangeListener(this);
		site.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IFileEditorInput input = (IFileEditorInput)getEditorInput();
		IPath iPath = input.getFile().getFullPath();
		
		if(event.getDelta()==null)
			return;
		
		//에디터로 열린 파일이 삭제될때 에디터를 닫는다.
		IResourceDelta relatedDelta = event.getDelta().findMember(iPath);
		if(relatedDelta != null){
			int kind = relatedDelta.getKind();			
			if(kind == IResourceDelta.REMOVED){
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {
						getSite().getPage().closeEditor(part, false);			
					}
				});				
			}
		}		
	}

	@Override
	protected void createPages() {
		try{			
			profileEditor = new OPRoSGUIProfileEditor();
			setPageText(addPage(profileEditor, getEditorInput()), "Profile View");
			setPartName(profileEditor.model.compEle.getComponentName());		
			profileEditor.getGraphicalViewer().addSelectionChangedListener(this);
			
			
			cppEditor = new CEditorEx();
			setPageText(addPage(cppEditor, new FileEditorInput(profileEditor.getSourceFile("cpp"))), "Source View");
			
			hEditor = new CEditorEx();
			setPageText(addPage(hEditor, new FileEditorInput(profileEditor.getSourceFile("h"))), "Header View");
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		savePage();		
		
		setOutlinePage(newPageIndex);
		setActionBar();
		
		//헤더파일에 추가된 포트를 검사하여 다이어그램에 추가
		if(getActiveEditor() == profileEditor){
			//if(oldIndex == 2)
				checkPort();
		}		
		oldIndex = newPageIndex;
		
	}
	
	/**
	 * 추가된 필드 엘리먼트 리스트를 리턴한다.
	 * @return
	 */
	public List<OPRoSPortElementBaseModel> getAddedPort() {
		ITranslationUnit unit = (ITranslationUnit) hEditor.getCElement();
		if (unit == null)
			return null;		
		
		List<OPRoSPortElementBaseModel> newElement = new ArrayList<OPRoSPortElementBaseModel>();		
		List<ICElement> list = getCFieldList(unit);
		if(list != null && list.size()!=0) {
			for(ICElement field : list) {
				String[] elementType = getCElementType((Field)field);
				if(elementType != null && elementType.length == 2) {
					OPRoSPortElementBaseModel model = findPort(field.getElementName(), elementType);
					if(model == null){
						
						String name = field.getElementName();
						String[] separation = getCElementType((Field)field);
						
						if(separation != null && separation.length == 2){
							OPRoSPortElementBaseModel port = getPortModelInstence(separation[0]);
							if(port != null) {
								port.setName(name);
								port.setType(separation[1]);			
								port.setParent(profileEditor.getModel());
								port.setLayout(new Rectangle(0, 0, 30, 30));
								
								if(port instanceof OPRoSDataInPortElementModel) {
									ITranslationUnit unit1 = (ITranslationUnit) cppEditor.getCElement();
									if(unit1 == null)
										continue;
									
									ICElement element = getCFunction(unit1);
										
									String policy = "";
									String size = "";
									
									((OPRoSDataInPortElementModel)port).setQueueingPolicy(policy);
									((OPRoSDataInPortElementModel)port).setQueueSize(size);
								}
								
								newElement.add(port);
							}
							
						}				
					}
				}
			}	
		}		
		return newElement;	
	}
	
	public ICElement getCFunction(ITranslationUnit unit1) {
		List<ICElement> list = null;
		try {
			list = unit1.getChildrenOfType(70);
		} catch (CModelException e) {
			e.printStackTrace();
		}
		
		for(ICElement element : list) {
			System.out.println(element.getElementName());
			System.out.println("");
		}
		return null;
	}
	
	/**
	 * 삭제된 필드 엘리먼트 리스트를 리턴한다.
	 * @return
	 */
	public List<OPRoSPortElementBaseModel> getRemovedPort() {		
		List<OPRoSPortElementBaseModel> removeElement = new ArrayList<OPRoSPortElementBaseModel>();		
		for(OPRoSPortElementBaseModel model : profileEditor.getModel().getPortModel()) {
			
			if(model instanceof OPRoSServiceProvidedPortElementModel 
					|| model instanceof OPRoSServiceRequiredPortElementModel) {
				continue;
			}
			
			if(!isPort(model)) {
				removeElement.add(model);
			}
		}	
		return removeElement;
	}
	
	/**
	 * 헤더파일에 등록되어있는 C엘리먼트중 필드값만 구해온다.
	 * @return
	 */
	public List<ICElement> getCFieldList(ITranslationUnit unit) {		
		try {
			List<ICElement> list = unit.getChildrenOfType(ICElement.C_CLASS);
			if(list.size() == 1) {
				return ((Structure)list.get(0)).getChildrenOfType(ICElement.C_FIELD);
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	IBufferChangedListener listener = new IBufferChangedListener() {		
		@Override
		public void bufferChanged(BufferChangedEvent arg0) {
			System.out.println("Text : "+arg0.getText()+", length : " + arg0.getLength() + ", offset : " + arg0.getOffset());	
		}
	};
	
	public IBuffer getHeaderFileBuffer() {
		ITranslationUnit unit = (ITranslationUnit) hEditor.getCElement();
		IBuffer buffer = null;
		try {
			buffer = unit.getBuffer();
		} catch (CModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}
	
	/**
	 * 포트 변경여부를 검사하여 포트 변경
	 */
	public void checkPort() {
		//추가된 포트를 검사하여 다이어그램에 추가
		List<OPRoSPortElementBaseModel> list = getAddedPort();
		if(list == null)
			return;
		
		for(OPRoSPortElementBaseModel model : list) {
			createPort(model);
		}
		
		//삭제된 포트를 검사하여 다이어그램에 추가
		list = getRemovedPort();
		if(list == null)
			return;
		
		for(OPRoSPortElementBaseModel model : list) {
			removePort(model);
		}
		
	}
	
	/**
	 * 검색 조건에 맞는 포트모델을 가져온다.
	 * @param portName
	 * @param portType
	 * @return
	 */
	public OPRoSPortElementBaseModel findPort(String portName, String[] portType) {
		if(portName != null && !portName.equals("") && portType != null && portType.length != 0) {
			for(OPRoSPortElementBaseModel element : profileEditor.getModel().getPortModel()) {			
				if(element.getName().equals(portName) && element.getType().equals(portType[1]))
					return element;			
			}
		}		
		return null;
	}
	
	public boolean isPort(OPRoSPortElementBaseModel model) {
		for(ICElement field : getCFieldList((ITranslationUnit) hEditor.getCElement())) {
			String[] types = getCElementType((Field)field);
			if(types != null && types.length == 2) {
				if(model.getName().equals(field.getElementName()) 
						&& model.getType().equals(types[1])) {
					return true;
				}
			}		
		}		
		return false;
	}
	
	/**
	 * 포트 타입에 맞는 인스턴스를 리턴한다.
	 * @param id
	 * @return
	 */
	public OPRoSPortElementBaseModel getPortModelInstence(String id) {
		OPRoSPortElementBaseModel model = null;
		if(id.equals("InputEventPort")) {
			model = new OPRoSEventInPortElementModel();
			model.setUsage(OPRoSStrings.getString("EventPortUsage0"));	
		}
		else if (id.equals("OutputEventPort")) {
			model = new OPRoSEventOutPortElementModel();
			model.setUsage(OPRoSStrings.getString("EventPortUsage1"));	
		}	
		else if (id.equals("InputDataPort")) {
			model = new OPRoSDataInPortElementModel();
			model.setUsage(OPRoSStrings.getString("DataPortUsage0"));
		}
		else if (id.equals("OutputDataPort")) {
			model = new OPRoSDataOutPortElementModel();
			model.setUsage(OPRoSStrings.getString("DataPortUsage1"));
		}
		return model;
	}
	
	/**
	 * 포트를 생성한다.
	 * @param portName
	 * @param portType
	 * @param model
	 */
	public boolean createPort(OPRoSPortElementBaseModel model) {		
		OPRoSPortCreateCommandEx command = new OPRoSPortCreateCommandEx();
		command.setElement(model);
		command.setParent(profileEditor.getModel());
		command.setLayout(model.getLayout());
		
		profileEditor.getCommandStack().execute(command);	
		return true;
	}
	
	/**
	 * 포트모델을 찾아 삭제한다.
	 * @param portName
	 * @param portType
	 */
	public boolean removePort(OPRoSPortElementBaseModel model) {		
		OPRoSDeleteCommandEx command = new OPRoSDeleteCommandEx();
		command.setModel(model);
		command.setParentModel(profileEditor.getModel());
		
		
		profileEditor.getCommandStack().execute(command);		
		return true;
	}
	
	/**
	 * 필드 엘리먼트로부터 엘리먼트 타입을 구해온다.
	 * @param field
	 * @return
	 */
	public String[] getCElementType(Field field) {
		String[] separation = null;
		try {
			separation = StringUtil.stringSeparation(field.getTypeName());
		} catch (CModelException e) {
			e.printStackTrace();
		}
		return separation;
	}
	
	public void bufferChange() {
		ITranslationUnit unit = (ITranslationUnit) hEditor.getCElement();		
		if (unit == null)
			return;
		
		IBuffer buffer = getHeaderFileBuffer();		
		BufferChangedEvent event = new BufferChangedEvent(buffer, 1601, 0, "a");
		unit.bufferChanged(event);
		
		StringBuffer sBuffer = new StringBuffer();
		char[] c = buffer.getCharacters();
		for(char ch : c) {
			sBuffer.append(ch);
		}
		buffer.setContents(sBuffer.toString());
	}
	
	/**
	 * 페이지가 바뀔때 아웃라인을 새로 셋팅한다.
	 * @param index
	 */
	public void setOutlinePage(int index) {
		IEditorPart editor = getEditor(index);
		if(outline != null)
			outline.setPageActive((IContentOutlinePage)editor.getAdapter(IContentOutlinePage.class));
	}
	
	/**
	 * 페이지가 바뀔때 액션바를 셋팅한다.
	 */
	public void setActionBar() {
		MultiPageEditorContributor contributor = (MultiPageEditorContributor)getEditorSite().getActionBarContributor();
		contributor.setActivePage(getActiveEditor());
	}
	
	public void savePage() {
		if(profileEditor.isDirty() 
				|| cppEditor.isDirty()
				|| hEditor.isDirty()) {
			
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());
			IProgressMonitor monitor = dialog.getProgressMonitor();
			dialog.open();
			doSave(monitor);
			monitor.done();
			dialog.close();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if(profileEditor.isDirty())
			profileEditor.doSave(monitor);
		
		if(cppEditor.isDirty())
			cppEditor.doSave(monitor);
		
		if(hEditor.isDirty())
			hEditor.doSave(monitor);		
	}

	@Override
	public void doSaveAs() {
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IContentOutlinePage.class) {		
			if(outline == null)
				outline = new MultiContentOutline((IContentOutlinePage)((EditorPart)getSelectedPage()).getAdapter(adapter));
			return outline;
		}
		
		if (adapter == IPropertySheetPage.class) {
			Object obj = super.getAdapter(adapter);
			return obj;
		}		
		return super.getAdapter(adapter);
	}
	

	@Override
	public void dispose() {
		super.dispose();
		OPRoSActivator.getWorkspace().removeResourceChangeListener(this);
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		profileEditor.getGraphicalViewer().removeSelectionChangedListener(this);
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
		if(getActiveEditor() == profileEditor) {
			profileEditor.selectionChanged(profileEditor, arg1);
		}
	}


	@Override
	public void selectionChanged(SelectionChangedEvent event) {		
		ISelectionProvider outerProvider = getSite().getSelectionProvider();
		if (outerProvider instanceof MultiPageSelectionProvider) {
			MultiPageSelectionProvider provider = (MultiPageSelectionProvider) outerProvider;
			provider.fireSelectionChanged(event);
			provider.firePostSelectionChanged(event);
		}
	}

}
