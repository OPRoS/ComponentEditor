package kr.co.ed.opros.ce.wizards.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList ;
import java.util.Collection ;
import java.util.HashMap ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;

import kr.co.ed.opros.ce.core.OPRoSProjectInfo;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.internal.ide.dialogs.IElementFilter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OPRoSResourceTreeAndListGroup extends EventManager implements
	ICheckStateListener, ISelectionChangedListener, ITreeViewerListener {
	private Object  root;
    private Object  currentTreeSelection;
    @SuppressWarnings("unchecked")
	private Collection  expandedTreeNodes = new HashSet ();
    @SuppressWarnings("unchecked")
	private Map  checkedStateStore = new HashMap (9);
    @SuppressWarnings("unchecked")
	private Collection  whiteCheckedTreeItems = new HashSet ();
    private ITreeContentProvider treeContentProvider;
    private IStructuredContentProvider listContentProvider;
    private ILabelProvider treeLabelProvider;
//    private ILabelProvider listLabelProvider;
    private CheckboxTreeViewer treeViewer;
    public IProject prj;
//	private CheckboxTableViewer listViewer;
	private static int PREFERRED_HEIGHT = 150;

	public OPRoSResourceTreeAndListGroup(Composite parent, Object  rootObject,
			ITreeContentProvider treeContentProvider,
			ILabelProvider treeLabelProvider,
			IStructuredContentProvider listContentProvider,
			ILabelProvider listLabelProvider, int style, boolean useHeightHint) {
		root = rootObject;
		this.treeContentProvider = treeContentProvider;
		this.listContentProvider = listContentProvider;
		this.treeLabelProvider = treeLabelProvider;
//		this.listLabelProvider = listLabelProvider;
		createContents(parent, style, useHeightHint);
	}
	public void aboutToOpen() {
		determineWhiteCheckedDescendents(root);
		checkNewTreeElements(treeContentProvider.getElements(root));
		currentTreeSelection = null;
		Object [] elements = treeContentProvider.getElements(root);
		Object  primary = elements.length > 0 ? elements[0] : null;
		if (primary != null) {
			treeViewer.setSelection(new StructuredSelection(primary));
		}
		treeViewer.getControl().setFocus();
	}
	public void addCheckStateListener(ICheckStateListener listener) {
		addListenerObject(listener);
	}	
	protected boolean areAllChildrenWhiteChecked(Object  treeElement) {
		Object [] children = treeContentProvider.getChildren(treeElement);
		for (int i = 0; i < children.length; ++i) {
			if (!whiteCheckedTreeItems.contains(children[i])) {
				return false;
			}
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	protected boolean areAllElementsChecked(Object  treeElement) {
		List  checkedElements = (List ) checkedStateStore.get(treeElement);
		if (checkedElements == null) {
			return false;
		}
		return getListItemsSize(treeElement) == checkedElements.size();
	}
	protected void checkNewTreeElements(Object [] elements) {
		for (int i = 0; i < elements.length; ++i) {
			Object  currentElement = elements[i];
			boolean checked = checkedStateStore.containsKey(currentElement);
			treeViewer.setChecked(currentElement, checked);
			treeViewer.setGrayed(currentElement, checked&& !whiteCheckedTreeItems.contains(currentElement));
		}
	}
	public void checkStateChanged(final CheckStateChangedEvent event) {
		BusyIndicator.showWhile(treeViewer.getControl().getDisplay(),
				new Runnable () {
			public void run() {
				if (event.getCheckable().equals(treeViewer)) {
					treeItemChecked(event.getElement(), event.getChecked());
				} else {
					listItemChecked(event.getElement(), event.getChecked(), true);
				}
				notifyCheckStateChangeListeners(event);
			}
		});
	}
	protected void createContents(Composite parent, int style,
			boolean useHeightHint) {
		
		Composite composite = new Composite(parent, style);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
//		layout.numColumns = 2;
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createTreeViewer(composite, useHeightHint);
		createListViewer(composite, useHeightHint);
		initialize();
	}
	protected void createListViewer(Composite parent, boolean useHeightHint) {
//		listViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
//		GridData data = new GridData(GridData.FILL_BOTH);
//		if (useHeightHint) {
//			data.heightHint = PREFERRED_HEIGHT;
//		}
//		listViewer.getTable().setLayoutData(data);
//		listViewer.getTable().setFont(parent.getFont());
//		listViewer.setContentProvider(listContentProvider);
//		listViewer.setLabelProvider(listLabelProvider);
//		listViewer.addCheckStateListener(this);
	}
	protected void createTreeViewer(Composite parent, boolean useHeightHint) {
		Tree tree = new Tree(parent, SWT.CHECK | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		if (useHeightHint) {
			data.heightHint = PREFERRED_HEIGHT;
		}
		tree.setLayoutData(data);
		tree.setFont(parent.getFont());
		treeViewer = new CheckboxTreeViewer(tree);
		treeViewer.setContentProvider(treeContentProvider);
		treeViewer.setLabelProvider(treeLabelProvider);
		treeViewer.addTreeListener(this);
		treeViewer.addCheckStateListener(this);
		treeViewer.addSelectionChangedListener(this);
	}
	@SuppressWarnings("unchecked")
	protected boolean determineShouldBeAtLeastGrayChecked(Object  treeElement) {
		List  checked = (List ) checkedStateStore.get(treeElement);
		if (checked != null && (!checked.isEmpty())) {
			return true;
		}
		if (expandedTreeNodes.contains(treeElement)) {
			Object [] children = treeContentProvider.getChildren(treeElement);
			for (int i = 0; i < children.length; ++i) {
				if (checkedStateStore.containsKey(children[i])) {
					return true;
				}
			}
		}
		return false;
	}
	protected boolean determineShouldBeWhiteChecked(Object  treeElement) {
		return areAllChildrenWhiteChecked(treeElement)&& areAllElementsChecked(treeElement);
	}
	protected void determineWhiteCheckedDescendents(Object  treeElement) {
		Object [] children = treeContentProvider.getElements(treeElement);
		for (int i = 0; i < children.length; ++i) {
			determineWhiteCheckedDescendents(children[i]);
		}
		if (determineShouldBeWhiteChecked(treeElement)) {
			setWhiteChecked(treeElement, true);
		}
	}
	public void expandAll() {
		treeViewer.expandAll();
	}
	private void expandTreeElement(final Object  item) {
		BusyIndicator.showWhile(treeViewer.getControl().getDisplay(),
				new Runnable () {
			@SuppressWarnings("unchecked")
			public void run() {
				if (expandedTreeNodes.contains(item)) {
					checkNewTreeElements(treeContentProvider.getChildren(item));
				} else {
					expandedTreeNodes.add(item);
					if (whiteCheckedTreeItems.contains(item)) {
						Object [] children = treeContentProvider.getChildren(item);
						for (int i = 0; i < children.length; ++i) {
							if (!whiteCheckedTreeItems.contains(children[i])) {
								Object  child = children[i];
								setWhiteChecked(child, true);
								treeViewer.setChecked(child, true);
								checkedStateStore.put(child,new ArrayList ());
							}
						}
						setListForWhiteSelection(item);
					}
				}
			}
		});
	}
	private void findAllSelectedListElements(Object  treeElement,
			String  parentLabel, boolean addAll, IElementFilter filter,
			IProgressMonitor monitor) throws InterruptedException  {
		
		String  fullLabel = null;
		if (monitor != null && monitor.isCanceled()) {
			return;
		}
		if (monitor != null) {
			fullLabel = getFullLabel(treeElement, parentLabel);
			monitor.subTask(fullLabel);
		}
		
		if (addAll) {
			filter.filterElements(listContentProvider.getElements(treeElement),	monitor);
		} else { //Add what we have stored
			if (checkedStateStore.containsKey(treeElement)) {
				filter.filterElements((Collection ) checkedStateStore.get(treeElement), monitor);
			}
		}
		Object [] treeChildren = treeContentProvider.getChildren(treeElement);
		for (int i = 0; i < treeChildren.length; i++) {
			Object  child = treeChildren[i];
			if (addAll) {
				findAllSelectedListElements(child, fullLabel, true, filter,monitor);
			} else { //Only continue for those with checked state
				if (checkedStateStore.containsKey(child)) {
					findAllSelectedListElements(child, fullLabel,
							whiteCheckedTreeItems.contains(child), filter,
							monitor);
				}
			}
			
		}
	}
	@SuppressWarnings("unchecked")
	private void findAllWhiteCheckedItems(Object  treeElement, Collection  result) {
		if (whiteCheckedTreeItems.contains(treeElement)) {
//			result.add(treeElement);
			prj = ((IProject)treeElement);
//			int lang = OPRoSUtil.getProjectLanguageSetting(prj);
			Iterator<String> compNames = findCompName(prj);
			String compName;
			while(compNames.hasNext()){
				compName = compNames.next();
//				if(lang==0){
				result.add(prj.getFolder(compName).getFolder("Release").getFile(compName+".dll"));
//				}
//				else if(lang==1){
//					result.add(prj.getFolder("Release").getFile(compName+".dll"));
//				}
				IFile exportFile = prj.getFile(compName+".xml");
				if(!exportFile.exists()){
					IFile profileFile = prj.getFolder(compName).getFolder("profile").getFile(compName+".xml");
					
					FileInputStream input=null;
					OPRoSProjectInfo prjInfo = new OPRoSProjectInfo();
					IFile projectProfile = prj.getFile(prj.getName()+"Prj.xml");
					FileInputStream prjInput=null;
					try {
						prjInput = (FileInputStream) projectProfile.getContents();
					} catch (CoreException e) {
						e.printStackTrace();
					}
					SAXBuilder prjBuilder = new SAXBuilder();
					Document prjDoc = null;
					try {
						prjDoc = prjBuilder.build( prjInput );
					} catch (JDOMException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Element prjRoot;
					prjRoot = prjDoc.getRootElement();
					prjInfo.clear();
					prjInfo.loadProfile(prjRoot);
					if(prjInput!=null){
						try {
							prjInput.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					try {
						input =(FileInputStream) profileFile.getContents();
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
						root.removeChild("layout");
						Element ele = root.getChild("ports");
						Iterator<Element> it = ele.getChildren().iterator();
						Element portEle;
						while(it.hasNext()){
							portEle=it.next();
							portEle.removeChild("layout");
						}
						Element exportRoot = (Element) root.clone();
						
						XMLOutputter opt = new XMLOutputter();
						Format form = opt.getFormat();
						form.setEncoding("euc-kr");
						form.setLineSeparator("\r\n");
						form.setIndent("	");
						form.setTextMode(Format.TextMode.TRIM);
						opt.setFormat(form);
					
						Document compDoc = new Document(exportRoot);
						try{
							FileOutputStream outStream = new FileOutputStream(new File(prjInfo.getLocation()+"/"+prjInfo.getPrjName()+"/"+compName+".xml"));
							opt.output(compDoc,outStream);
							outStream.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						try {
							input.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						prj.refreshLocal(IResource.DEPTH_INFINITE, null);
						exportFile = prj.getFile(compName+".xml");
						exportFile.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e2) {
						e2.printStackTrace();
					}
				}
				result.add(exportFile);
			}
		} else {
			return;
//			Collection  listChildren = (Collection ) checkedStateStore.get(treeElement);
//			if (listChildren == null) {
//				return;
//			}
//			result.addAll(listChildren);
//			Object [] children = treeContentProvider.getChildren(treeElement);
//			for (int i = 0; i < children.length; ++i) {
//				findAllWhiteCheckedItems(children[i], result);
//			}
		}
	}
	public void getAllCheckedListItems(IElementFilter filter,
			IProgressMonitor monitor) throws InterruptedException  {
		Object [] children = treeContentProvider.getChildren(root);
		for (int i = 0; i < children.length; ++i) {
			findAllSelectedListElements(children[i], null,
					whiteCheckedTreeItems.contains(children[i]), filter,
					monitor);
		}
	}
	public List  getAllCheckedListItems() {
		final ArrayList  returnValue = new ArrayList ();
		IElementFilter passThroughFilter = new IElementFilter() {
			
			public void filterElements(Collection  elements,
					IProgressMonitor monitor) {
				returnValue.addAll(elements);
			}
			public void filterElements(Object [] elements,
					IProgressMonitor monitor) {
				for (int i = 0; i < elements.length; i++) {
					returnValue.add(elements[i]);
				}
			}
		};
		try {
			getAllCheckedListItems(passThroughFilter, null);
		} catch (InterruptedException  exception) {
			return new ArrayList ();
		}
		return returnValue;
		
	}
	@SuppressWarnings("unchecked")
	public List  getAllWhiteCheckedItems() {
		List  result = new ArrayList ();
		Object [] children = treeContentProvider.getChildren(root);
		for (int i = 0; i < children.length; ++i) {
			findAllWhiteCheckedItems(children[i], result);
		}
		return result;
	}
	
	public int getCheckedElementCount() {
		return checkedStateStore.size();
	}
	
	protected String  getFullLabel(Object  treeElement, String  parentLabel) {
		String  label = parentLabel;
		if (parentLabel == null){
			label = ""; //$NON-NLS-1$
		}
		IPath parentName = new Path(label);
		
		String  elementText = treeLabelProvider.getText(treeElement);
		if(elementText == null) {
			return parentName.toString();
		}
		return parentName.append(elementText).toString();
	}
	protected int getListItemsSize(Object  treeElement) {
		Object [] elements = listContentProvider.getElements(treeElement);
		return elements.length;
	}
	public Table getListTable() {
//		return this.listViewer.getTable();
		return new Table(null, 0);
	}
	@SuppressWarnings("unchecked")
	protected void grayCheckHierarchy(Object  treeElement) {
		expandTreeElement(treeElement);
		
// 	if this tree element is already gray then its ancestors all are as well
		if (checkedStateStore.containsKey(treeElement)) {
			return; // no need to proceed upwards from here
		}
		
		checkedStateStore.put(treeElement, new ArrayList ());
		Object  parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			grayCheckHierarchy(parent);
		}
	}
	private void grayUpdateHierarchy(Object  treeElement) {
		boolean shouldBeAtLeastGray = determineShouldBeAtLeastGrayChecked(treeElement);
		
		treeViewer.setGrayChecked(treeElement, shouldBeAtLeastGray);
		
		if (whiteCheckedTreeItems.contains(treeElement)) {
			whiteCheckedTreeItems.remove(treeElement);
		}
		
// 	proceed up the tree element hierarchy
		Object  parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			grayUpdateHierarchy(parent);
		}
	}
	public void initialCheckListItem(Object  element) {
		Object  parent = treeContentProvider.getParent(element);
		selectAndReveal(parent);
//		listViewer.setChecked(element, true);
		listItemChecked(element, true, false);
		grayUpdateHierarchy(parent);
	}
	public void initialCheckTreeItem(Object  element) {
		treeItemChecked(element, true);
		selectAndReveal(element);
	}
	private void selectAndReveal(Object  treeElement) {
		treeViewer.reveal(treeElement);
		IStructuredSelection selection = new StructuredSelection(treeElement);
		treeViewer.setSelection(selection);
	}
	@SuppressWarnings("unchecked")
	protected void initialize() {
		treeViewer.setInput(root);
		this.expandedTreeNodes = new ArrayList ();
		this.expandedTreeNodes.add(root);
		
	}
	@SuppressWarnings("unchecked")
	protected void listItemChecked(Object  listElement, boolean state,
			boolean updatingFromSelection) {
		List  checkedListItems = (List ) checkedStateStore.get(currentTreeSelection);
//	If it has not been expanded do so as the selection of list items will affect gray state
		if (!expandedTreeNodes.contains(currentTreeSelection)) {
			expandTreeElement(currentTreeSelection);
		}
		if (state) {
			if (checkedListItems == null) {
// 	since the associated tree item has gone from 0 -> 1 checked
// 	list items, tree checking may need to be updated
				grayCheckHierarchy(currentTreeSelection);
				checkedListItems = (List ) checkedStateStore.get(currentTreeSelection);
			}
			checkedListItems.add(listElement);
		} else {
			checkedListItems.remove(listElement);
			if (checkedListItems.isEmpty()) {
// 	since the associated tree item has gone from 1 -> 0 checked
// 	list items, tree checking may need to be updated
				ungrayCheckHierarchy(currentTreeSelection);
			}
		}
//	Update the list with the selections if there are any
		if (checkedListItems.size() > 0) {
			checkedStateStore.put(currentTreeSelection, checkedListItems);
		}
		if (updatingFromSelection) {
			grayUpdateHierarchy(currentTreeSelection);
		}
	}
	
	protected void notifyCheckStateChangeListeners(
			final CheckStateChangedEvent event) {
		Object [] array = getListeners();
		for (int i = 0; i < array.length; i++) {
			final ICheckStateListener l = (ICheckStateListener) array[i];
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					l.checkStateChanged(event);
				}
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void populateListViewer(final Object  treeElement) {
//		listViewer.setInput(treeElement);
		
//	If the element is white checked but not expanded we have not set up all of the children yet
		if (!(expandedTreeNodes.contains(treeElement))
				&& whiteCheckedTreeItems.contains(treeElement)) {
//	Potentially long operation - show a busy cursor
			BusyIndicator.showWhile(treeViewer.getControl().getDisplay(),
					new Runnable () {
				public void run() {
					setListForWhiteSelection(treeElement);
//					listViewer.setAllChecked(true);
				}
			});
		} else {
			List  listItemsToCheck = (List ) checkedStateStore.get(treeElement);
			
			if (listItemsToCheck != null) {
				Iterator  listItemsEnum = listItemsToCheck.iterator();
				while (listItemsEnum.hasNext()) {
//					listViewer.setChecked(listItemsEnum.next(), true);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void primeHierarchyForSelection(Object  item, Set  selectedNodes) {
		
//	Only prime it if we haven't visited yet
		if (selectedNodes.contains(item)) {
			return;
		}	
		
		checkedStateStore.put(item, new ArrayList());
		
//	mark as expanded as we are going to populate it after this
		expandedTreeNodes.add(item);
		selectedNodes.add(item);
		
		Object  parent = treeContentProvider.getParent(item);
		if (parent != null) {
			primeHierarchyForSelection(parent, selectedNodes);
		}
	}
	
	public void removeCheckStateListener(ICheckStateListener listener) {
		removeListenerObject(listener);
	}
	
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Object  selectedElement = selection.getFirstElement();
		if (selectedElement == null) {
			currentTreeSelection = null;
//			listViewer.setInput(currentTreeSelection);
			return;
		}
// 	ie.- if not an item deselection
		if (selectedElement != currentTreeSelection) {
			populateListViewer(selectedElement);
		}
		currentTreeSelection = selectedElement;
	}
	
	public void setAllSelections(final boolean selection) {
//	If there is no root there is nothing to select
		if (root == null) {
			return;
		}
//	Potentially long operation - show a busy cursor
		BusyIndicator.showWhile(treeViewer.getControl().getDisplay(),
				new Runnable () {
			public void run() {
				setTreeChecked(root, selection);
//				listViewer.setAllChecked(selection);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void setListForWhiteSelection(Object  treeElement) {
		Object [] listItems = listContentProvider.getElements(treeElement);
		List  listItemsChecked = new ArrayList ();
		for (int i = 0; i < listItems.length; ++i) {
			listItemsChecked.add(listItems[i]);
		}
		checkedStateStore.put(treeElement, listItemsChecked);
	}
	
	public void setListProviders(IStructuredContentProvider contentProvider,
			ILabelProvider labelProvider) {
//		listViewer.setContentProvider(contentProvider);
//		listViewer.setLabelProvider(labelProvider);
	}
	
	public void setListComparator(ViewerComparator comparator) {
//		listViewer.setComparator(comparator);
	}
	
	public void setRoot(Object  newRoot) {
		this.root = newRoot;
		initialize();
	}
	
	protected void setTreeChecked(Object  treeElement, boolean state) {
		if (treeElement.equals(currentTreeSelection)) {
//			listViewer.setAllChecked(state);
		}
		
		if (state) {
			setListForWhiteSelection(treeElement);
		} else {
			checkedStateStore.remove(treeElement);
		}
		setWhiteChecked(treeElement, state);
		treeViewer.setChecked(treeElement, state);
		treeViewer.setGrayed(treeElement, false);
		
// 	now logically check/uncheck all children as well if it has been expanded
		if (expandedTreeNodes.contains(treeElement)) {
			Object [] children = treeContentProvider.getChildren(treeElement);
			for (int i = 0; i < children.length; ++i) {
				setTreeChecked(children[i], state);
			}
		}
	}
	
	public void setTreeProviders(ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(labelProvider);
	}
	
	public void setTreeComparator(ViewerComparator comparator) {
		treeViewer.setComparator(comparator);
	}
	
	@SuppressWarnings("unchecked")
	protected void setWhiteChecked(Object  treeElement, boolean isWhiteChecked) {
		if (isWhiteChecked) {
			if (!whiteCheckedTreeItems.contains(treeElement)) {
				whiteCheckedTreeItems.add(treeElement);
			}
		} else {
			whiteCheckedTreeItems.remove(treeElement);
		}
	}
	/**
* 	Handle the collapsing of an element in a tree viewer
	*/
	public void treeCollapsed(TreeExpansionEvent event) {
// 	We don't need to do anything with this
	}
	
	/**
* 	Handle the expansionsion of an element in a tree viewer
	*/
	public void treeExpanded(TreeExpansionEvent event) {
		expandTreeElement(event.getElement());
	}
	/**
* 	Callback that's invoked when the checked status of an item in the tree
* 	is changed by the user.
	*/
	protected void treeItemChecked(Object  treeElement, boolean state) {
// 	recursively adjust all child tree elements appropriately
		setTreeChecked(treeElement, state);
		Object  parent = treeContentProvider.getParent(treeElement);
		if (parent == null) {
			return;
		}
// 	now update upwards in the tree hierarchy 
		if (state) {
			grayCheckHierarchy(parent);
		} else {
			ungrayCheckHierarchy(parent);
		}
		
//	Update the hierarchy but do not white select the parent
		grayUpdateHierarchy(parent);
	}
	
	/**
* 	Logically un-gray-check all ancestors of treeItem iff appropriate.
	*/
	protected void ungrayCheckHierarchy(Object  treeElement) {
		if (!determineShouldBeAtLeastGrayChecked(treeElement)) {
			checkedStateStore.remove(treeElement);
		}
		Object  parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			ungrayCheckHierarchy(parent);
		}
	}
	
	/**
* 	Set the checked state of self and all ancestors appropriately
	*/
	protected void updateHierarchy(Object  treeElement) {
		boolean whiteChecked = determineShouldBeWhiteChecked(treeElement);
		boolean shouldBeAtLeastGray = determineShouldBeAtLeastGrayChecked(treeElement);
		
		treeViewer.setChecked(treeElement, shouldBeAtLeastGray);
		setWhiteChecked(treeElement, whiteChecked);
		if (whiteChecked) {
			treeViewer.setGrayed(treeElement, false);
		} else {
			treeViewer.setGrayed(treeElement, shouldBeAtLeastGray);
		}
// 	proceed up the tree element hierarchy but gray select all of them
		Object  parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			grayUpdateHierarchy(parent);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateSelections(Map  items) {
// 	We are replacing all selected items with the given selected items,
// 	so reinitialize everything.
//		this.listViewer.setAllChecked(false);
		this.treeViewer.setCheckedElements(new Object [0]);
		this.whiteCheckedTreeItems = new HashSet ();
		Set  selectedNodes = new HashSet ();
		checkedStateStore = new HashMap ();
		
//	Update the store before the hierarchy to prevent updating parents before all of the children are done
		Iterator  keyIterator = items.keySet().iterator();
		while (keyIterator.hasNext()) {
			Object  key = keyIterator.next();
			List  selections = (List ) items.get(key);
//	Replace the items in the checked state store with those from the supplied items 
			checkedStateStore.put(key, selections);
			selectedNodes.add(key);
// proceed up the tree element hierarchy
			Object  parent = treeContentProvider.getParent(key);
			if (parent != null) {
// 	proceed up the tree element hierarchy and make sure everything is in the table 
				primeHierarchyForSelection(parent, selectedNodes);
			}
		}
		
// 	Update the checked tree items. Since each tree item has a selected
// item, all the tree items will be gray checked.
		treeViewer.setCheckedElements(checkedStateStore.keySet().toArray());
		treeViewer.setGrayedElements(checkedStateStore.keySet().toArray());
		
// Update the listView of the currently selected tree item.
		if (currentTreeSelection != null) {
			Object  displayItems = items.get(currentTreeSelection);
			if (displayItems != null) {
//				listViewer.setCheckedElements(((List ) displayItems).toArray());
			}
		}
	}
	public void setFocus() {
		this.treeViewer.getTree().setFocus();
	}
	private Iterator<String> findCompName(IProject prj){
		IFile projectProfile = prj.getFile(prj.getName()+"Prj.xml");
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
		OPRoSProjectInfo prjInfo = new OPRoSProjectInfo();
		prjInfo.loadProfile(root);
		if(input!=null){
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return prjInfo.getComponents();
	}
	public void removeTempProfile(){
		Iterator<String> compNames = findCompName(prj);
		String compName;
		while(compNames.hasNext()){
			compName = compNames.next();
			IFile exportFile = prj.getFile(compName+".xml");
			try {
				exportFile.delete(false, null);
				prj.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

