package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil2;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.EditorPart;

/**
 * �������� ������Ʈ�� ���Ҵ��ø� ����ϴ� ������
 * ������Ʈ�� ���Ե� �޴��佺Ʈ ���� ����� ����.
 * @author hclee
 *
 */
public class OPRoSManifestEditor extends FormEditor implements IResourceChangeListener, IPropertyChangeListener {
	
	private EditorPart part;
	
	public boolean dirty;
	
	private OPRoSManifestPage dPage;
	
	private TextEditorEx textEditor;

	public OPRoSManifestEditor() {
		this.part = this;
		OPRoSActivator.getWorkspace().addResourceChangeListener(this);
		this.part.addPartPropertyListener(this);
		
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		
		try {
			setPartName(OPRoSUtil2.getComponentContainer(getInputFile()).getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void addPages() {
		try {
			if (getEditorInput() instanceof IFileEditorInput){
				dPage = new OPRoSManifestPage(this);
				addPage(dPage);
				
				textEditor = new TextEditorEx(this);
				setPageText(addPage(textEditor, getEditorInput()), "MANIFEST.MF");
			}				
		} catch (PartInitException e) {
			e.printStackTrace();
		}		
	}
	
	

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		switch(newPageIndex) {
		case 0 : 
			try {
				dPage.getDependenciesSection().getSelectionSection().treeCheck();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		switch(getActivePage()) {
			case 0 : 
				setDirty(!dPage.getDependenciesSection().getSelectionSection().doSave(monitor, false));
				break;
			case 1 :
				textEditor.doSave(monitor);
				break;
		}
	}

	@Override
	public void doSaveAs() {}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	/**
	 * �ش� ������Ʈ ����.
	 * @return
	 */
	public IProject getProject(){
		if(getEditorInput() instanceof IFileEditorInput){
			return ((IFileEditorInput)getEditorInput()).getFile().getProject();
		}
		return null;
	}
	
	

	@Override
	public void dispose() {
		super.dispose();
		this.part.removePartPropertyListener(this);
		OPRoSActivator.getWorkspace().removeResourceChangeListener(this);
		dPage.getDependenciesSection().dispose();
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		final IFileEditorInput input = (IFileEditorInput)getEditorInput();
		IPath iPath = input.getFile().getFullPath();
		
		if(event.getDelta()==null)
			return;
		
		//�����ͷ� ���� ������ �����ɶ� �����͸� �ݴ´�.
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
			else if(kind == IResourceDelta.CHANGED) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {
						
					}
				});
			}
		}
		
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		String str = arg0.getProperty();
		if(str.equals(IEditorPart.PROP_DIRTY)){
			
		}		
	}
	
	/**
	 * �����Ϳ� ��ȭ�� �Ͼ������ �����̵�����
	 * PropertyChangeListener�� �̸� �뺸�Ѵ�.
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		});
	}
	
	@Override
	protected void handlePropertyChange(int propertyId) {
		if(propertyId == IEditorPart.PROP_DIRTY) {
			setDirty(true);
		}		
		super.handlePropertyChange(propertyId);
	}
	
	/**
	 * �����ͷ� ���� �Է������� �����Ѵ�.
	 * @return
	 */
	public IFile getInputFile(){
		return ((IFileEditorInput)getEditorInput()).getFile();
	}
	
}
