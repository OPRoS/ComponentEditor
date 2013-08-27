package kr.co.ed.opros.ce.wizards.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.dialogs.DialogUtil;
import org.eclipse.ui.internal.wizards.datatransfer.FileSystemExportOperation;
import org.eclipse.ui.internal.wizards.datatransfer.IDataTransferHelpContextIds;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceExportPage1;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

@SuppressWarnings({ "restriction", "deprecation" })
public class OPRoSExportWizardPage extends WizardFileSystemResourceExportPage1 {
	private IStructuredSelection initialResourceSelection;
	private OPRoSResourceTreeAndListGroup resourceGroup;
	
	public OPRoSExportWizardPage(IStructuredSelection selection) {
		super(selection);
		initialResourceSelection=selection;
	}

	@Override
	protected void createOptionsGroupButtons(Group optionsGroup) {
		super.createOptionsGroupButtons(optionsGroup);
		createDirectoryStructureButton.setVisible(false);
		createSelectionOnlyButton.setVisible(false);
		createSelectionOnlyButton.setSelection(true);
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
		          | GridData.HORIZONTAL_ALIGN_FILL));
		composite.setFont(parent.getFont());
		
//		createResourcesGroup(composite);
		 List<IProject>  input = new ArrayList<IProject> ();
		 IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		 for (int i = 0; i < projects.length; i++) {
			 if (projects[i].isOpen()) {
				 input.add(projects[i]);
			 }
		 }
		 this.resourceGroup = new OPRoSResourceTreeAndListGroup(composite, input,
				 getResourceProvider(IResource.FOLDER|IResource.PROJECT),
				 WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(),
				 getResourceProvider(IResource.FILE), 
				 WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(), 
				 SWT.NONE,
				 DialogUtil.inRegularFontMode(composite));
		 ICheckStateListener listener = new ICheckStateListener() {
			 public void checkStateChanged(CheckStateChangedEvent event) {
				 updateWidgetEnablements();
			 }
		 };
		 this.resourceGroup.addCheckStateListener(listener);
		 
//		createButtonsGroup(composite);
//		Font font = parent.getFont();
//		Composite buttonComposite = new Composite(parent, SWT.NONE);
//		buttonComposite.setFont(parent.getFont());
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 3;
//		layout.makeColumnsEqualWidth = true;
//		buttonComposite.setLayout(layout);
//		buttonComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
//		| GridData.HORIZONTAL_ALIGN_FILL));
//		// types edit button
//		Button selectTypesButton = createButton(buttonComposite,
//		IDialogConstants.SELECT_TYPES_ID, IDEWorkbenchMessages.WizardTransferPage_selectTypes, false);
//		SelectionListener listener1 = new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				handleTypesEditButtonPressed();
//			}
//		};
//		selectTypesButton.addSelectionListener(listener1);
//		selectTypesButton.setFont(font);
//		setButtonLayoutData(selectTypesButton);
//		Button selectButton = createButton(buttonComposite,
//		IDialogConstants.SELECT_ALL_ID, IDEWorkbenchMessages.WizardTransferPage_selectAll, false);
//		listener1 = new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				resourceGroup.setAllSelections(true);
//			}
//		};
//		selectButton.addSelectionListener(listener1);
//		selectButton.setFont(font);
//		setButtonLayoutData(selectButton);
//		Button deselectButton = createButton(buttonComposite,
//				IDialogConstants.DESELECT_ALL_ID, IDEWorkbenchMessages.WizardTransferPage_deselectAll, false);
//		listener1 = new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				resourceGroup.setAllSelections(false);
//			}
//		};
//		deselectButton.addSelectionListener(listener1);
//		deselectButton.setFont(font);
//		setButtonLayoutData(deselectButton);

		 
		createDestinationGroup(composite);
		createOptionsGroup(composite);
		restoreResourceSpecificationWidgetValues(); // ie.- local
		restoreWidgetValues(); // ie.- subclass hook
		if (initialResourceSelection != null) {
		        setupBasedOnInitialSelections();
		}
		updateWidgetEnablements();
		setPageComplete(determinePageCompletion());
		setErrorMessage(null); // should not initially have error message
		setControl(composite);

		giveFocusToDestination();
		WorkbenchHelp.setHelp(
				getControl(),
				IDataTransferHelpContextIds.FILE_SYSTEM_EXPORT_WIZARD_PAGE);

	}
	private ITreeContentProvider getResourceProvider(final int resourceType) {
		return new WorkbenchContentProvider() {
			@SuppressWarnings("unchecked")
			public Object [] getChildren(Object  o) {
				if (o instanceof IContainer) {
					IResource[] members = null;
					try {
						members = ((IContainer) o).members();
					} catch (CoreException e) {
						//just return an empty set of children
						return new Object [0];
					}
					//filter out the desired resource types
					ArrayList<IResource>  results = new ArrayList<IResource> ();
					for (int i = 0; i < members.length; i++) {
						//And the test bits with the resource types to see if they are what we want
						if ((members[i].getType() & resourceType) > 0) {
							if(members[i].getParent()!=null){
								if(members[i].getParent().getParent()!=null){
									if(members[i].getParent().getParent().getParent()!=null){
									}else{
										results.add(members[i]);
									}
								}else
									results.add(members[i]);
							}else
								results.add(members[i]);
						}
					}
					return results.toArray();
				} 
				//input element case
				if (o instanceof ArrayList ) {
					return ((ArrayList) o).toArray();
				} 
				return new Object [0];
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Iterator getSelectedResourcesIterator() {
		// TODO Auto-generated method stub
		return this.resourceGroup.getAllCheckedListItems().iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List getWhiteCheckedResources() {
		return this.resourceGroup.getAllWhiteCheckedItems();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setupBasedOnInitialSelections() {
		Iterator  it = this.initialResourceSelection.iterator();
		while (it.hasNext()) {
			IResource currentResource = (IResource) it.next();
			if (currentResource.getType() == IResource.FILE) {
				this.resourceGroup.initialCheckListItem(currentResource);
			} else {
				this.resourceGroup.initialCheckTreeItem(currentResource);
			}
		}
	}

	@SuppressWarnings("unused")
	private void setupSelectionsBasedOnSelectedTypes() {
		 Runnable  runnable = new Runnable () {
			 @SuppressWarnings("unchecked")
			public void run() {
				 Map  selectionMap = new Hashtable ();
				 //	Only get the white selected ones
				 Iterator  resourceIterator = resourceGroup.getAllWhiteCheckedItems().iterator();
				 while (resourceIterator.hasNext()) {
					 //	handle the files here - white checked containers require recursion
					 IResource resource = (IResource) resourceIterator.next();
					 if (resource.getType() == IResource.FILE) {
						 if (hasExportableExtension(resource.getName())) {
							 List  resourceList = new ArrayList ();
							 IContainer parent = resource.getParent();
							 if (selectionMap.containsKey(parent)) {
								 resourceList = (List ) selectionMap.get(parent);
							 }
							 resourceList.add(resource);
							 selectionMap.put(parent, resourceList);
						 }
					 } else {
						 setupSelectionsBasedOnSelectedTypes(selectionMap,
								 (IContainer) resource);
					 }
				 }
				 resourceGroup.updateSelections(selectionMap);
			 }
		 };
		BusyIndicator.showWhile(getShell().getDisplay(), runnable);
	 }
	@SuppressWarnings("unchecked")
	private void setupSelectionsBasedOnSelectedTypes(Map  selectionMap,IContainer parent) {
		List  selections = new ArrayList ();
		IResource[] resources;
		boolean hasFiles = false;
		try {
			resources = parent.members();
		} catch (CoreException exception) {
			//	Just return if we can't get any info
			return;
		}
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.getType() == IResource.FILE) {
				if (hasExportableExtension(resource.getName())) {
					hasFiles = true;
					selections.add(resource);
				}
			} else {
				setupSelectionsBasedOnSelectedTypes(selectionMap,
						(IContainer) resource);
			}
		}

		//Only add it to the list if there are files in this folder
		if (hasFiles) {
			selectionMap.put(parent, selections);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean finish() {
		List resourcesToExport = getWhiteCheckedResources();
		if (!ensureTargetIsValid(new File(getDestinationValue()))) {
			return false;
		}

		// Save dirty editors if possible but do not stop if not all are saved
		saveDirtyEditors();
		// about to invoke the operation so save our state
		saveWidgetValues();
		boolean bFinish =executeExportOperation(new FileSystemExportOperation(null,
				resourcesToExport, getDestinationValue(), this));
		resourceGroup.removeTempProfile();
		return bFinish;
	}
	
}
