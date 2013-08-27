package kr.co.ed.opros.ce.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.ComponentUtil;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.editors.EditorMessages;
import kr.co.ed.opros.ce.editors.OPRoSComponentGeneralSection;
import kr.co.ed.opros.ce.editors.OPRoSManifestEditor;
import kr.co.ed.opros.ce.editors.OPRoSManifestPage;
import kr.co.ed.opros.ce.provider.TreeContentProvider;
import kr.co.ed.opros.ce.provider.TreeLabelProvider;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 오프러스 컴포넌트 리소스 익스포트를 위한 트리뷰어 구성 페이지
 * @author hclee
 *
 */
public class OPRoSDependenciesSectionComposite implements IResourceChangeListener {
	
	public CheckboxTreeViewer fTreeViewer;
	
	protected IContainer fBundleRoot;	
	
	protected IResource fOriginalResource, fParentResource;
	
	protected boolean isChecked;
	
	public IFile inputFile;	
	
	public OPRoSManifestEditor editor;
	
	public OPRoSManifestPage page;
	
	public TreeContentProvider contentProvider;	
	

	public OPRoSDependenciesSectionComposite() {
		//리소스 체인지 리스너 등록
		OPRoSActivator.getWorkspace().addResourceChangeListener(this);
	}

	public OPRoSDependenciesSectionComposite(IFile inputFile) {
		this();
		this.inputFile = inputFile;		
	}	
	
	public OPRoSDependenciesSectionComposite(OPRoSManifestEditor editor) {		
		this(editor.getInputFile());
		this.editor = editor;
		this.page = (OPRoSManifestPage)editor.getSelectedPage();
	}
	
	public void crateTreeView(Composite composite){
		fTreeViewer = new CheckboxTreeViewer(composite, SWT.CHECK | SWT.BORDER);
		setTreeViewerProvider(fTreeViewer);
		fTreeViewer.setAutoExpandLevel(0);
		fTreeViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(final CheckStateChangedEvent event) {
				final Object element = event.getElement();
				handleCheckStateChanged((IResource)element, event.getChecked());
				
				// 에디터에 변화가 생겼다는것을 통지
				if(editor != null)
					editor.setDirty(true);
			}
		});
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 300;
		gd.widthHint = 100;
		fTreeViewer.getTree().setLayoutData(gd);
		initialize();
	}
	
	/**
	 * 트리뷰어에 프로바이더를 등록한다.
	 */
	protected void setTreeViewerProvider(CheckboxTreeViewer fTreeViewer) {
		fTreeViewer.setContentProvider(getContentProvider());
		fTreeViewer.setLabelProvider(new TreeLabelProvider());
	}
	
	/**
	 * 페이지 초기화
	 */
	private void initialize() {
		try {
			if(getInputFile() != null) {			
				//트리뷰어에 아이템 입력
				setInput((IContainer)OPRoSUtil2.getComponentContainer(getInputFile()));		
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 메니페스트에 저장되어있는 아이템과 트리뷰어에 출력되는
	 * 아이템을 비교하여 체크한다.
	 * @throws Exception
	 */
	public void treeCheck() throws Exception {
		
		fTreeViewer.setAllChecked(false);
		
		//트리뷰어에 입력된 아이템
		List<IResource> l = getTreeViewerItem(new ArrayList<IResource>()
				, (IResource)fTreeViewer.getInput());		
		//메니페스트 파일에 저장된 리소스
		List<IResource> list = getSavedResource();
		
		for(IResource resource : list) {
			for(IResource o : l) {
				if(resource.equals(o)) 
					handleCheckStateChanged(o, true);
			}
		}
	}
	
	/**
	 * 트리뷰어에 입력된 아이템을 구해 리턴한다.
	 * @param list
	 * @param resource
	 * @return
	 * @throws CoreException
	 */
	private List<IResource> getTreeViewerItem(List<IResource> list,
			IResource resource) throws CoreException {
		
		if (resource instanceof IFolder) {
			IResource[] r = ((IFolder) resource).members();
			for (int i = 0; i < r.length; i++) {
				getTreeViewerItem(list, r[i]);
			}
		}
		list.add(resource);
		return list;
	}

	protected void createViewerPartControl(Composite parent, int style,
			int span, FormToolkit toolkit) {

	}
	
	/**
	 * 체크박스 트리뷰어의 상태에 변화가 생겼을때 호출
	 * @param event
	 */
	public void handleCheckStateChanged(IResource resource, boolean isChecked) {	
		fOriginalResource = resource;
		this.isChecked = isChecked;
		if (!isChecked) {
			resource = handleAllUnselected(resource, resource.getName());
		}
		fParentResource = resource;
		setTreeChecked();	
	}
	
	/**
	 * 트리에 아이템 체크를 전반적으로 관리
	 */
	protected void setTreeChecked() {
		if ((fParentResource == null && fOriginalResource != null) 
				|| (fOriginalResource == null && fParentResource != null)) {
			initializeCheckState();
			return;
		}
		
		fTreeViewer.setChecked(fParentResource, isChecked);
		fTreeViewer.setGrayed(fOriginalResource, false);
		fTreeViewer.setParentsGrayed(fParentResource, true);
		setParentsChecked(fParentResource);
		fTreeViewer.setGrayed(fParentResource, false);
		if (fParentResource instanceof IFolder) {
			fTreeViewer.setSubtreeChecked(fParentResource, isChecked);
			setChildrenGrayed(fParentResource, false);
		}
		while (!fOriginalResource.equals(fParentResource)) {
			fTreeViewer.setChecked(fOriginalResource, isChecked);
			fOriginalResource = fOriginalResource.getParent();
		}
		fParentResource = null;
		fOriginalResource = null;
	}
	
	protected void initializeCheckState() {
		uncheckAll();
	}
	
	public void uncheckAll() {
		fTreeViewer.setCheckedElements(new Object[0]);
	}
	
	protected void setChildrenGrayed(IResource folder, boolean isGray) {
		fTreeViewer.setGrayed(folder, isGray);
		if (((TreeContentProvider) fTreeViewer.getContentProvider()).hasChildren(folder)) {
			Object[] members = getFolderChildren(folder);
			for (int i = 0; i < members.length; i++) {
				setChildrenGrayed((IFolder) members[i], isGray);
			}
		}
	}
	
	protected IResource handleAllUnselected(IResource resource, String name) {
		IResource parent = resource.getParent();
		if (parent.equals(fBundleRoot)) {
			return resource;
		}
		try {
			boolean uncheck = true;
			IResource[] members = ((IContainer) parent).members();
			for (int i = 0; i < members.length; i++) {
				if (fTreeViewer.getChecked(members[i]) && !members[i].getName().equals(name))
					uncheck = false;
			}
			if (uncheck) {
				return handleAllUnselected(parent, parent.getName());
			}
			return resource;
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 부모 폴더를 체크
	 * @param resource
	 */
	protected void setParentsChecked(IResource resource) {
		if (resource.getParent() != resource.getProject()) {
			fTreeViewer.setChecked(resource.getParent(), true);
			setParentsChecked(resource.getParent());
		}
	}
	
	/**
	 * 선택된 폴더 밑에있는 자식들을 구한다.
	 * @param parent
	 * @return
	 */
	public Object[] getFolderChildren(Object parent) {
		IResource[] members = null;
		try {
			if (!(parent instanceof IFolder))
				return new Object[0];
			members = ((IFolder) parent).members();
			ArrayList<Object> results = new ArrayList<Object>();
			for (int i = 0; i < members.length; i++) {
				if ((members[i].getType() == IResource.FOLDER)) {
					results.add(members[i]);
				}
			}
			return results.toArray();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return new Object[0];
	}
	
	
	
	/**
	 * 메니페스트 파일에 정의되어있는  lib, src 구성요소를 구한다. 
	 * @return
	 * @throws Exception
	 */
	private List<IResource> getSavedResource() throws Exception {	
		List<IResource> list = new ArrayList<IResource>();
		BufferedReader in = getInputFileBufferReader();
		String s;
		while ((s = in.readLine()) != null) {
			if(s.matches("element:.*")){
				String fileName = s.substring(s.lastIndexOf(":")+1, s.length()).replace("/", "");
				if(fileName.equals(getComponentName()+".xml"))
					continue;
				
				IResource resource = (IResource)findFile((IFolder)getInputFile().getParent(), fileName);
				if(resource != null)
					list.add(resource);
			}
		}
		in.close();		
		return list;
	}
	
	/**
	 * 디팬던시에 체크된 파일을 찾아 리턴한다.
	 * @param parent
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public IResource findFile(IFolder parent, String fileName) throws Exception {
		IResource file = null;
		for(IResource resource : parent.members()) {			
			if(resource.getName().equals(fileName)) {
				file = resource;
			}
			/*
			if(file == null && resource instanceof IFolder) {
				file = findFile((IFolder)resource, fileName);
			}
			*/			
		}
		return file;
	}
	
	/**
	 * 트리뷰어에 아이템을 넣는다.
	 * @param obj
	 */
	public void setInput(IContainer obj) {
		fBundleRoot = obj;
		fTreeViewer.setInput(fBundleRoot);
		
		//메니페스트에 저장되어있는 트리 체크
		if(obj != null && getInputFile() != null) {
			try {
				treeCheck();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setInput(IContainer obj, IFile inputFile) {
		if(inputFile.isAccessible()) {
			setInputFile(inputFile);
		}		
		setInput(obj);
	}
	
	/**
	 * 트리뷰어 반환
	 * @return
	 */
	public CheckboxTreeViewer getTreeViewer() {
		return fTreeViewer;
	}
	
	public void dispose() {
		OPRoSActivator.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent e) {
		if (e.getDelta() == null)
			return;		
		
		Runnable runnable = new Runnable() {			
			@Override
			public void run() {
				//프로젝트에 파일들에 변화가 생겼을때 트리뷰어를 리플레쉬
				if(!fTreeViewer.getControl().isDisposed())
					fTreeViewer.refresh(true);
			}
		};
		update(runnable);	
	}
	
	 /**
     * Runnable 실행
     * @param runnable
     */
    private void update(Runnable runnable) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
        if (windows != null && windows.length > 0) {
            Display display = windows[0].getShell().getDisplay();
            display.asyncExec(runnable);
        } else
            runnable.run();
    }
	
	/**
	 * 에디터로 열려진 파일의 버퍼리더를 리턴한다.
	 * @return
	 * @throws Exception
	 */
	public BufferedReader getInputFileBufferReader() throws Exception {
		return new BufferedReader(new FileReader( getInputFile().getLocation().toFile()));   
	}
	
	/**
	 * 입력된 파일을 리턴한다.
	 * @return
	 */
	public IFile getInputFile() {
		return inputFile;
	}
	
	/**
	 * 입력된 파일을 리턴한다.
	 * @return
	 */
	public void setInputFile(IFile inputFile) {
		this.inputFile = inputFile;
	}
	
	/**
	 * 트리뷰어로 인풋되는 리소스
	 * @return
	 */
	public IContainer getfBundleRoot() {
		return fBundleRoot;
	}
	
	/**
	 * 트리뷰어에 체크된 아이템 리턴
	 * @return
	 */
	public List<Object> getCheckedResource() {
		Object[] obj = fTreeViewer.getCheckedElements();
		List<Object> list = new ArrayList<Object>();
		for(int i=0; i<obj.length; i++) {
			if(obj[i] instanceof IResource)
				list.add(obj[i]);
		}
		return list;
	}
	
	/**
	 * 디펜던시 파일을 저장한다.
	 * @param monitor
	 * @return
	 */
	public boolean doSave(IProgressMonitor monitor, boolean isSourceAttacth){	
		try {
			if(getInputFile() != null) {
				String compVersion  = "";
				String compDesc = "";
				
				if(page != null) {
					OPRoSComponentGeneralSection section = page.getGeneralSection();
					if(section != null) {
						compVersion = section.getComponentVersion();
						compDesc = section.getComponentDescription();
					}
						
				}
				else {
					compVersion  = OPRoSUtil2.getComponentInfomation(getInputFile(), EditorMessages.getString("Manifest.Version")+".*");
					compDesc = OPRoSUtil2.getComponentInfomation(getInputFile(), EditorMessages.getString("Manifest.Description")+".*");
				}
				
				String str = ComponentUtil.createManifestStr(getCheckedResource(), getComponentName(), (IFolder)getInputFile().getParent(), compVersion, compDesc, isSourceAttacth);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				baos.write(str.getBytes());
				
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());			
				getInputFile().setContents(bais, true, false, monitor);
				
				baos.close();
				bais.close();
			} else {
				return false;
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 컴포넌트 이름을 리턴한다.
	 * @return
	 */
	public String getComponentName() {
		String componentName = "";
		try{
			if(getInputFile() != null)
				componentName = OPRoSUtil2.getComponentContainer(getInputFile()).getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return componentName;
	}
	
	/**
	 * 트리뷰에 쓰이는 컨텐츠 프로바이더 리턴
	 * @return
	 */
	public TreeContentProvider getContentProvider() {
		if(contentProvider == null)
			contentProvider = new TreeContentProvider(getComponentName());
		return contentProvider;
	}

}
