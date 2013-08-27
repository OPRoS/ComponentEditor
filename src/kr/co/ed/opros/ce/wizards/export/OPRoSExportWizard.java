package kr.co.ed.opros.ce.wizards.export;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.wizards.datatransfer.FileSystemExportWizard;

@SuppressWarnings("restriction")
public class OPRoSExportWizard extends FileSystemExportWizard{
	IStructuredSelection selection;
	private OPRoSExportWizardPage mainPage;
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return mainPage.finish();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void init(IWorkbench workbench, IStructuredSelection arg1) {
		super.init(workbench, arg1);
		selection=arg1;
		List selectedResources = IDE.computeSelectedResources(arg1);
		if(!selectedResources.isEmpty()){
			this.selection = new StructuredSelection(selectedResources);
		}
		if (selection.isEmpty() && workbench.getActiveWorkbenchWindow() != null) {
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			if (page != null) {
				IEditorPart currentEditor = page.getActiveEditor();
				if (currentEditor != null) {
					Object  selectedResource = currentEditor.getEditorInput().getAdapter(IResource.class);
					if (selectedResource != null) {
						selection = new StructuredSelection(selectedResource);
					}
				}
			}
		}
		setWindowTitle(DataTransferMessages.DataTransfer_export);
		setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("icons/opros.bmp"));//$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		mainPage= new OPRoSExportWizardPage(selection);
		addPage(mainPage);
	}

}
