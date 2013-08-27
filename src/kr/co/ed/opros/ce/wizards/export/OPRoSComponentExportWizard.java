package kr.co.ed.opros.ce.wizards.export;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.ArchiveFileExportOperationEx;
import kr.co.ed.opros.ce.FileUtils;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil2;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileExportOperation;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.TarFileExporter;

public class OPRoSComponentExportWizard  extends Wizard implements IExportWizard {
	
	public OPRoSComponentExportWizardPage page;
	
	public TarFileExporter exporter;
	
	public IWorkbench aWorkbench;

	@Override
	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		this.aWorkbench = aWorkbench;
		
		setWindowTitle(DataTransferMessages.DataTransfer_export);
        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportdir_wiz.png"));//$NON-NLS-1$
        setNeedsProgressMonitor(true);
        
		page = new OPRoSComponentExportWizardPage(aWorkbench, currentSelection);
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(aWorkbench
				.getActiveWorkbenchWindow().getShell());
		dialog.open();
		IProgressMonitor monitor = dialog.getProgressMonitor();
		
		page.getSelectionSection().doSave(monitor, true);	
		
		
		//프로젝트 빌드
		try {					
			page.getSelectedProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		
		
		List<Object> list = page.getCheckedResource();	
		
		//릴리즈, 디버그 파일을 구분하여 추가
		try {
			IResource[] resource = page.getSelectionSection().getfBundleRoot()
					.getFolder(new Path(page.getSelectedMode())).members();
			
			if(resource != null && resource.length != 0){
				for(int i=0; i<resource.length; i++) {
					if(resource[i].getFileExtension().equals("dll")) {
						list.add(resource[i]);
					}
				}
			}
			
			//메니페스트 파일추가
			list.add(page.getSelectionSection().getInputFile());
			
			//프로파일 추가
			IFolder componentFolder = (IFolder)page.getSelectionSection().getfBundleRoot();
			IFolder profileFolder = componentFolder.getFolder(new Path("profile"));
			if(profileFolder != null && profileFolder.isAccessible()) {
				IResource[] resources = profileFolder.members();
				for(IResource file : resources) {
					if(file instanceof IFile)
						list.add(file);
				}
			}
			
			String componentName = page.getSelectionSection().getfBundleRoot().getName();
			
			String[] destinations = {page.getExportLocation(), 
					componentFolder.getProject().getLocation().toOSString() + File.separator + componentName+".tar"};
			for(String destination : destinations){
				exporter = new TarFileExporter(destination, false);
				
				
				for(Object obj : list) {
					writeResource((IResource)obj, "/");
				}
				
				//소스파일 첨부시
				if(page.isSourceAttached) {
					String[] extensions = {"cpp", "h"};
					for(String extension : extensions) {
						IFile file = page.getSelectionSection().getfBundleRoot().getFile(new Path(componentName +"."+extension));
						//writeResource((IResource)file, componentName + File.separator + "src");
						writeResource((IResource)file, "src");
					}
				}
				exporter.finished();
			}
			OPRoSUtil2.refreshProject(componentFolder.getProject());
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
			
		monitor.done();
		dialog.close();
		
		return true;
	}
	
	/**
	 * 타르 익스프터에 리소스 추가
	 * @param resource
	 * @param parentPath
	 */
	public void writeResource(IResource resource, String parentPath) {
		if(resource instanceof IFile) {
			IFile res = (IFile)resource;	
			try {
				exporter.write(res, parentPath + File.separator + res.getName());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		else if(resource instanceof IFolder) {
			IFolder folder = (IFolder) resource;
			try {
				IResource[] res = folder.members();
				for(IResource re : res) {
					writeResource(re, parentPath + File.separator + folder.getName());
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

}
