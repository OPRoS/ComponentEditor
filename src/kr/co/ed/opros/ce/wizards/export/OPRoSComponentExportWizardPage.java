package kr.co.ed.opros.ce.wizards.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.FileUtils;
import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.provider.ComponentListContentProvider;
import kr.co.ed.opros.ce.provider.ComponentListLabelProvider;
import kr.co.ed.opros.ce.ui.OPRoSDependenciesSectionComposite;

import org.eclipse.cdt.internal.core.model.CContainer;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.dialogs.ExportPage;
import org.opros.mainpreference.Activator;
import org.opros.mainpreference.preferences.PreferenceConstants;

public class OPRoSComponentExportWizardPage extends ExportPage {
	
	public static final String DEBUG_MODE = "Debug";
	
	public static final String RELEASE_MODE = "Release";
	
	public String selectedMode;	

	public CheckboxTableViewer componentListViewer;
	
	public IFolder selectedComponent;
	
	public OPRoSDependenciesSectionComposite selectionSection;	

	public Combo comboProject;
	
	public IStructuredSelection currentSelection;
	
	public Iterator<String> compIter;
	
	public Text textExportLocation;
	
	public boolean isSourceAttached;

	

	public OPRoSComponentExportWizardPage(IWorkbench aWorkbench,
			IStructuredSelection currentSelection) {
		super(aWorkbench, currentSelection);
		this.currentSelection = currentSelection;
		this.selectedMode = RELEASE_MODE;
		this.isSourceAttached = false;
		
		setTitle("Export OPRoS Component");
		//setImageDescriptor(OPRoSActivator.getImageDescriptor(IIconConstants.IMG_EXPORT_ICON));
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(3, false));
		
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		group.setLayout(new GridLayout(1, false));
		group.setText("&Project");
		
		comboProject = new Combo(group, SWT.READ_ONLY);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		comboProject.setLayoutData(gd);	   
		addComboSelectionListener(comboProject);		
		
		createResourcesGroup(composite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("To archive file:");
		
		textExportLocation = new Text(composite, SWT.NONE | SWT.BORDER);
		textExportLocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textExportLocation.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent arg0) {				
				dialogChange();
			}
		});
		
		Button button = new Button(composite, SWT.PUSH);
		button.setText("B&rowse...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getContainer().getShell(), SWT.SAVE | SWT.SHEET);
		        dialog.setFilterExtensions(new String[] { "*.tar" }); //$NON-NLS-1$ //$NON-NLS-2$
		        dialog.setText("Export to archive file");
		        String currentSourceString = getDestinationValue();
		        int lastSeparatorIndex = currentSourceString
		                .lastIndexOf(File.separator);
		        if (lastSeparatorIndex != -1) {
					dialog.setFilterPath(currentSourceString.substring(0,
		                    lastSeparatorIndex));
				}
		        String selectedFileName = dialog.open();

		        if (selectedFileName != null) {
		            setErrorMessage(null);
		            textExportLocation.setText(selectedFileName);
		        }
			}			
		});
		
		label = new Label(composite, SWT.NONE);
		
		group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		group.setLayout(new GridLayout(1, false));
		group.setText("Options");
		
		button = new Button(group, SWT.CHECK);
		button.setText("Source attached");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isSourceAttached = ((Button)e.widget).getSelection();
			}
		});
		
		
		button = new Button(group, SWT.RADIO);
		button.setText("Debug");
		button.setImage(OPRoSActivator.getImage(IIconConstants.ICON_CONFIG_TOOL));
		button.addSelectionListener(new RadioButtonListener(DEBUG_MODE));
		
		
		button = new Button(group, SWT.RADIO);
		button.setText("Release");
		button.setImage(OPRoSActivator.getImage(IIconConstants.ICON_CONFIG_TOOL));
		button.setSelection(true);
		button.addSelectionListener(new RadioButtonListener(RELEASE_MODE));
		
		initialize();
		setControl(composite);
	}
	
	protected String getDestinationValue() {
        return textExportLocation.getText().trim();
    }
	
	/**
	 * 콤보박스에 리스너 등록
	 * @param combo
	 */
	private void addComboSelectionListener(Combo combo){
		combo.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {	        	
        		setComponentItems();
        		dialogChange();
	        }
	    });
	}
	
	/**
	 * 콤보박스에서 프로젝트가 선택되었을때 해당프로젝트에 포함되어있는
	 * 컴포넌트 리스트를 리스트뷰에 뿌린다.
	 */
	public void setComponentItems() {
		setCompIter(OPRoSUtil.getComponentList(getSelectedProject()));
		componentListViewer.setInput(getSelectedProject());
		
		selectionSection.setInput(null);
	}
	
	
	/**
	 * 리소스 그룹
	 * @param parent
	 */
	public void createResourcesGroup(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd.heightHint = 200;
		group.setLayoutData(gd);
		group.setLayout(new GridLayout(2, false));
		group.setText("Resources");
		
		createProjectGroup(group);
		createModelGroup(group);
	}
	
	/**
	 * 프로젝트 선택 그룹
	 * @param group
	 */
	public void createProjectGroup(Group group){		
		componentListViewer = CheckboxTableViewer.newCheckList(group, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(componentListViewer.getControl());
		componentListViewer.setContentProvider(new ComponentListContentProvider());
		componentListViewer.setLabelProvider(new ComponentListLabelProvider());
        setAddSelectionLister(componentListViewer);
	}	
	
	/**
	 * 모델 선택 그룹
	 * @param group
	 */
	public void createModelGroup(Group group) {		
		selectionSection = new OPRoSDependenciesSectionComposite();
		selectionSection.crateTreeView(group);
	}
	
	/**
	 * 체크박스 테이블 뷰어에 체크 셀렉션 리스너를 등록
	 * @param viewer
	 */
	private void setAddSelectionLister(final CheckboxTableViewer viewer) {
		viewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent Event) {
				itemStateChaged(Event, viewer);
			}
		});
	}
	
	/**
	 * 체크박스 테이블을 싱글 셀렉션이 되게하며, 선택된 아이템을 저장한다.
	 * @param event
	 */
	private void itemStateChaged(CheckStateChangedEvent event, CheckboxTableViewer viewer) {		
		boolean singleSelection = true;
		
		if (singleSelection) {
			viewer.setCheckedElements(new Object[0]);
		}
		if (event.getChecked()) {
			if (singleSelection) {
				viewer.setChecked(event.getElement(), true);
				selectedComponent = (IFolder)event.getElement();
				checkedComponentJob();
			}
		}
		else{
			if(singleSelection){
				selectedComponent = null;
				checkedComponentJob();
			}
		}
		dialogChange();
	}
	
	/**
	 * 프로젝트가 선택되었을때 해당 프로젝트의 오프로스 컴포넌트를 구해서 
	 * 트리뷰에 뿌려준다.
	 * @param project
	 */
	public void checkedComponentJob(){
		if(selectedComponent != null) {
			selectionSection.getContentProvider().setComponentName(selectedComponent.getName());
			selectionSection.setInput(selectedComponent, 
					getComponentDependenciesFile());
			
			String exportFilePath = Activator.getDefault()
					.getPreferenceStore()
					.getString(PreferenceConstants.LOCAL_REPOSITORY_PATH)
					+"\\"+ selectedComponent.getName() + ".tar";
			
			textExportLocation.setText(exportFilePath);
			
			
		}
		else {
			selectionSection.setInput(null);
			textExportLocation.setText("");
		}
				
	}
	
	
	@Override
	protected void initialize() {		
		comboProject.setItems(getProjectList());		
		
		//셀렉션이 있을경우 해당 프로젝트를 기본으로 선택해 놓는다.
		if (currentSelection.size() > 1)
			return;
		Object obj = currentSelection.getFirstElement();
		
		if(obj != null) {
			IProject project;
			if (obj instanceof IContainer)
				project = ((IContainer) obj).getProject();
			else if(obj instanceof CContainer)
				project = ((CContainer) obj).getParent().getResource().getProject();
			else
				project = ((IResource) obj).getProject();		
		
			if(project != null) {
				for(int i=0; i<getProjectList().length; i++) {
					if(getProjectList()[i].equals(project.getName())) {
						comboProject.select(i);
						setComponentItems();
		        		dialogChange();
						break;
					}
				}
			}
		}		
		dialogChange();
	}
	
	/**
	 * 프로젝트 목록을 반환
	 * @return
	 */
	public String[] getProjectList() {
		List<String> list = new ArrayList<String>();
		List<IProject> projects = OPRoSUtil2.getOPRoSProjects();
		if(projects != null && projects.size() !=0) {
			for(IProject project : projects) {
				list.add(project.getName());
			}
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	public Iterator<String> getCompIter() {
		return compIter;
	}

	public void setCompIter(Iterator<String> compIter) {
		this.compIter = compIter;
	}
	
	protected void dialogChange(){
		if(comboProject.getSelectionIndex() == -1){
			updateStatus("Select a Project");
			return;
		}
		
		if(componentListViewer.getCheckedElements().length == 0) {
			updateStatus("Select a Component");
			return;
		}
		
		if(textExportLocation.getText().equals("")) {
			updateStatus("Invalid repository location");
			return;
		}
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	/**
	 * Export 모드를 선택하는 라디오 버튼용 리스너
	 * @author hclee
	 *
	 */
	class RadioButtonListener extends SelectionAdapter {
		
		String type;
		
		public RadioButtonListener(String type) {
			this.type = type;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			selectedMode = type;
		}		
		
	}
	
	/**
	 * Exprot 모드 구분자
	 * @return
	 */
	public String getSelectedMode() {
		return selectedMode;
	}
	
	/**
	 * 체크된 리소스
	 * @return
	 */
	public List<Object> getCheckedResource() {
		List<Object> list = selectionSection.getCheckedResource();
		if(list == null)
			list = new ArrayList<Object>();
		return list;
	}
	
	public IProject getSelectedProject() {
		return OPRoSUtil2.getOPRoSProjects().get(comboProject.getSelectionIndex());
	}
	
	public IFile getComponentDependenciesFile() {
		return selectedComponent.getFile(new Path("OPRoS.mf"));
	}
	
	public OPRoSDependenciesSectionComposite getSelectionSection() {
		return selectionSection;
	}
	
	public String getExportLocation() {
		return textExportLocation.getText();
	}
	
	public boolean isSourceAttached() {
		return isSourceAttached;
	}
	
}
