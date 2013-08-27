package kr.co.ed.opros.ce.ui.actions;

import java.lang.reflect.InvocationTargetException;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.ui.ConnectionSelectDialog;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

public class RemoteDebugOPRoSProjectActionDelegate implements
		IWorkbenchWindowActionDelegate {
	private IProject selectionProject;
	private String appDllName;
	private String compName;
	private ISelection select=null;
	private Image enableImg = OPRoSUtil.createImage("/icons/RemoteDebug.gif");
	private Image disableImg = OPRoSUtil.createImage("/icons/RemoteDebugDisable.gif");
	private ImageDescriptor enableImage= ImageDescriptor.createFromImage(enableImg);
	private ImageDescriptor disableImage= ImageDescriptor.createFromImage(disableImg);
	IHost remoteHost;
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
//		IHost[] hosts = RSECorePlugin.getDefault().getSystemRegistry().getHosts();
//		int nHostCnt = RSECorePlugin.getDefault().getSystemRegistry().getHostCount();
		bConnected=false;
//		for(int i=0;i<nHostCnt;i++){
//			if(hosts[i].getAliasName().compareTo("OPRoS")==0){
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
							monitor.beginTask("Try Connecting...", 10);
							remoteHost.getSubSystems()[0].connect(monitor, true);
							monitor.done();
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

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType("org.eclipse.rse.remotecdt.RemoteApplicationLaunch");
		try
		{
			ILaunchConfigurationWorkingCopy configuration = type.newInstance(null, selectionProject.getName()+" (RemoteDebug)");
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME,selectionProject.getName());
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,appDllName);
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE,ICDTLaunchConfigurationConstants.DEBUGGER_MODE_RUN);
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ID,"org.eclipse.cdt.debug.mi.core.GDBServerCDebugger");
//			configuration.setAttribute(IRemoteConnectionConfigurationConstants.ATTR_REMOTE_PATH, "C:\\TEST\\OPRoSCDLMinGW\\Repository\\hello\\HelloMaker.dll");
			configuration.setAttribute(DebugPlugin.getUniqueIdentifier() + ".REMOTE_TCP", remoteHost.getName());
			configuration.setAttribute(DebugPlugin.getUniqueIdentifier() + ".ATTR_SKIP_DOWNLOAD_TO_TARGET", true);
			
			ILaunchConfiguration config = configuration.doSave();	
			DebugUITools.launch(config, ILaunchManager.DEBUG_MODE);
			return;
		}
		catch(CoreException e)
		{
			e.printStackTrace();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
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
