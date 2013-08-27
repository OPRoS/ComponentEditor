package kr.co.ed.opros.ce.ui.actions;

import java.lang.reflect.InvocationTargetException;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.ui.ConnectionSelectDialog;

import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.files.ui.dialogs.SystemRemoteFileDialog;
import org.eclipse.rse.files.ui.resources.UniversalFileTransferUtility;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

public class SendComponentToRobotActionDelegate implements
		IWorkbenchWindowActionDelegate {
	private IProject selectionProject;
	private String appDllName;
	private String compName;
	private ISelection select=null;
	private Image enableImg = OPRoSUtil.createImage("/icons/SendComp.gif");
	private Image disableImg = OPRoSUtil.createImage("/icons/SendCompDis.gif");
	private ImageDescriptor enableImage= ImageDescriptor.createFromImage(enableImg);
	private ImageDescriptor disableImage= ImageDescriptor.createFromImage(disableImg);
	IFile dllFile;
	IFile profileFile;
	IRemoteFile selectedFile;
	IHost remoteHost;
	int nSize;
	boolean bConnected;
	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow arg0) {

	}

	@Override
	public void run(IAction arg0) {
		if(select==null||selectionProject==null){
			OPRoSUtil.openMessageBox("Component Folder is not Selected ", SWT.ERROR);
			return;
		}
		bConnected=false;
		ConnectionSelectDialog dlg = new ConnectionSelectDialog(CUIPlugin.getActiveWorkbenchShell());
		int ret = dlg.open();
		remoteHost=null;
		if(ret==Dialog.OK){
			remoteHost= dlg.getSelectionHost();//RSEHelper.getRemoteConnectionByName("OPRoS");
		}else{
			return;
		}
			
		if(remoteHost!=null){
				IProgressService service = PlatformUI.getWorkbench().getProgressService();
				try {
					service.run(true, true, new IRunnableWithProgress(){

						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
							try {
//								monitor.beginTask("Try Connecting...", 10);
								remoteHost.getSubSystems()[0].connect(monitor, true);
//								monitor.done();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(remoteHost.isOffline())
								bConnected=false;
							else
								bConnected=true;
						}
						
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		if(!bConnected){
			OPRoSUtil.openMessageBox("Robot Connection is Fail!\nDebugging Stop!", SWT.ERROR);
			return;
		}
		
		SystemRemoteFileDialog rsDlg = new SystemRemoteFileDialog(
				CUIPlugin.getActiveWorkbenchShell(),
				"Remote System Browser : Select OPRoS Repository in Robot System",
				remoteHost);
		rsDlg.setBlockOnOpen(true);
		if (rsDlg.open() == Dialog.OK) {
			Object retObj = rsDlg.getSelectedObject();
			if (retObj instanceof IRemoteFile) {
				selectedFile = (IRemoteFile) retObj;
				dllFile=selectionProject.getFile(appDllName);
				nSize=0;
				nSize=(int) dllFile.getLocation().toFile().length();
				String profileName = appDllName;
				profileName=profileName.replace("/Debug/", "/profile/");
				profileName=profileName.replace(".dll", ".xml");
				profileFile = selectionProject.getFile(profileName);
				nSize+=(int) profileFile.getLocation().toFile().length();
//				ProgressMonitorDialog pmDlg = new ProgressMonitorDialog(null);
//				IProgressMonitor monitor = pmDlg.getProgressMonitor();
//				pmDlg.setCancelable(true);
//				pmDlg.open();
//				monitor.beginTask("TEST", 10);
				IProgressService service = PlatformUI.getWorkbench().getProgressService();
				try {
					service.run(true, true, new IRunnableWithProgress(){

						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
							monitor.beginTask("Debug OPRoS Commponent Deploy",nSize);
							UniversalFileTransferUtility.uploadResourceFromWorkspace(dllFile, selectedFile,monitor);
							UniversalFileTransferUtility.uploadResourceFromWorkspace(profileFile, selectedFile, monitor);
							monitor.done();
						}
						
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}else{
			return;
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection != null && !selection.isEmpty()) {
			select=selection;
			if(selection instanceof IStructuredSelection){
				Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
				if (selectedElement instanceof IAdaptable) {
	                IAdaptable adaptable = (IAdaptable) selectedElement;  
	                IResource resource;
	                resource = (IResource) adaptable.getAdapter(IResource.class);
	                if (resource != null && resource.getType() != IResource.ROOT && resource.getParent().getType()!= IResource.ROOT) {
	                	while (resource.getType() != IResource.FOLDER || resource.getParent().getParent().getType() != IResource.ROOT){
	                		resource = resource.getParent();
	                		if(resource==null||resource.getParent()==null||resource.getParent().getParent()==null)
	                			return;
	                    }
	                	if(resource!=null&&resource.getParent()!=null&&resource.getParent().getParent()!=null){
		                	selectionProject=resource.getProject();
		                	compName=resource.getName(); 
		                	appDllName = compName+"/Debug/"+compName+".dll";
		                	action.setEnabled(true);
		                	action.setImageDescriptor(enableImage);
	                	}else{
	                		selectionProject=null;
	                		compName=null;
	    	            	appDllName = null;
		                	action.setEnabled(false);
		        			action.setImageDescriptor(disableImage);
	                	}
	                }else{
	                	selectionProject=null;
	                	compName=null;
		            	appDllName = null;
	                	action.setEnabled(false);
	        			action.setImageDescriptor(disableImage);
	                }
	            }else{
	            	selectionProject=null;
	            	compName=null;
	            	appDllName = null;
	            	action.setEnabled(false);
        			action.setImageDescriptor(disableImage);
	            }
	        }else{
	        	selectionProject=null;
            	compName=null;
            	appDllName = null;
	        	action.setEnabled(false);
    			action.setImageDescriptor(disableImage);
	        }
		}else{
			select=null;
			action.setEnabled(false);
			action.setImageDescriptor(disableImage);
		}

	}
}

