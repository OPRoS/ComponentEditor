package kr.co.ed.opros.ce.wizards.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.provider.ComponentListContentProvider;
import kr.co.ed.opros.ce.provider.ComponentListLabelProvider;
import kr.co.ed.opros.ce.provider.ProjectListContentProvider;
import kr.co.ed.opros.ce.provider.TreeLabelProvider;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.dialogs.ExportPage;

public class UserComponentRegisterWizardPage extends ExportPage {
	
	public TableViewer projectListViewer;
	
	public CheckboxTableViewer componentListViewer;
	
	public List<IFolder> selectedComponent;	

	public Combo comboCategory;
	
	public IStructuredSelection selection;
	
	public UserComponentRegisterWizardPage(IWorkbench aWorkbench,
			IStructuredSelection currentSelection) {
		super(aWorkbench, currentSelection);
		
		this.selectedComponent = new ArrayList<IFolder>();
		this.selection = currentSelection;
		
		setTitle("Component Templet Register");
	}
	
	@Override
	public void createControl(Composite parent) {		
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		
		Group group = new Group(composite, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd.heightHint = 200;
		group.setLayoutData(gd);
		group.setLayout(new GridLayout(2, false));
		group.setText("Resources");
		
		projectListViewer = new TableViewer(group, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(projectListViewer.getControl());
		projectListViewer.setContentProvider(new ProjectListContentProvider());
		projectListViewer.setLabelProvider(new TreeLabelProvider());
        setListViewerSelectionLister(projectListViewer);
		
		componentListViewer = CheckboxTableViewer.newCheckList(group, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(componentListViewer.getControl());
		componentListViewer.setContentProvider(new ComponentListContentProvider());
		componentListViewer.setLabelProvider(new ComponentListLabelProvider());
        setAddSelectionLister(componentListViewer);
        
        Label label = new Label(composite, SWT.NONE);
		label.setText("Component Category : ");
		
		comboCategory = new Combo(composite, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		comboCategory.setLayoutData(gd);	   
		addComboSelectionListener(comboCategory);
		
		initialize();
		setControl(composite);
	}
	
	@Override
	protected void initialize() {		
		projectListViewer.setInput(OPRoSUtil2.getOPRoSProjects());
		comboCategory.setItems(getComboItem());
		comboCategory.select(0);
		
		if(selection != null) {
			projectListViewer.setSelection(selection);
		}
		
		dialogChange();
	}
	
	/**
	 * �޺��� ���� �������� ��ȯ�Ѵ�.
	 * @return
	 */
	public String[] getComboItem() {
		List<String> list = new ArrayList<String>();
		list.add("Default");
		
		File userLocation = new File(UserComponentRegisterWizard.USER_COMPONENT_LOCATION);
		if(userLocation.isDirectory()) {
			File[] files = userLocation.listFiles();
			for(File file : files) {
				if(file.isDirectory() && !file.getName().equals("Default"))
					list.add(file.getName());
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	private void setListViewerSelectionLister(final TableViewer viewer) {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if(event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection)event.getSelection();
					IProject project = (IProject)selection.getFirstElement();
					componentListViewer.setInput(project);		
					dialogChange();
				}
			}
		});
	}
	
	/**
	 * üũ�ڽ� ���̺� �� üũ ������ �����ʸ� ���
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
	 * ���õ� �������� �����Ѵ�.
	 * @param event
	 */
	private void itemStateChaged(CheckStateChangedEvent event, CheckboxTableViewer viewer) {		
		
		if (event.getChecked()) {			
			selectedComponent.add((IFolder)event.getElement());
		}
		else{
			selectedComponent.remove((IFolder)event.getElement());
		}
		dialogChange();
	}
	
	public void dialogChange() {
		if(projectListViewer.getSelection() == null || projectListViewer.getSelection().isEmpty()) {
			updateStatus("Select a OPRoS Project");
			return;
		}
		if(componentListViewer.getCheckedElements().length == 0) {
			updateStatus("Select a Component");
			return;
		}		
		if(comboCategory.getText() == null || comboCategory.getText().equals("")) {
			updateStatus("Enter the Component Category");
			return;
		}
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	/**
	 * �޺��ڽ��� ������ ���
	 * @param combo
	 */
	private void addComboSelectionListener(Combo combo){
		combo.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
        		dialogChange();
	        }
	    });
	}
	
	/**
	 * ���õ� ������Ʈ ����Ʈ�� ��ȯ.
	 * @return
	 */
	public List<IFolder> getSelectedComponent() {
		return selectedComponent;
	}
	
	public String getCategoryName() {
		return comboCategory.getText();
	}

}
