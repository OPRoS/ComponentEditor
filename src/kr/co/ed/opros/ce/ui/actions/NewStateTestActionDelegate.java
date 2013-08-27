package kr.co.ed.opros.ce.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
//import org.opros.test.core.OPRoSFileException;
//import org.opros.test.core.OPRoSTestConfigException;
//import org.opros.test.core.common.TestProjectException;
//import org.opros.test.core.statetest.StateTestData;
//import org.opros.test.core.statetest.StateTestProjectManager;

public class NewStateTestActionDelegate implements IObjectActionDelegate {
	private String strCompPath="";
	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction arg0) {
		if(strCompPath.isEmpty()){
			return;
		}else{
//			StateTestData testData = new StateTestData();
//			testData.setProjectName("ST_01");
//			try {
//				new StateTestProjectManager(strCompPath,testData);
//			} catch (TestProjectException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (OPRoSTestConfigException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (OPRoSFileException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
		if (selection != null && !selection.isEmpty()) {
			if(selection instanceof IStructuredSelection){
				Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
				if (selectedElement instanceof IAdaptable) {
	                IAdaptable adaptable = (IAdaptable) selectedElement;  
	                IResource resource;
	                resource = (IResource) adaptable.getAdapter(IResource.class);
	                
	                if (resource != null && resource.getType() != IResource.ROOT) {
	                	while (resource.getType() != IResource.FOLDER) {
	                		resource = resource.getParent();
	                    }
	                	String name=resource.getName();
	                	name+="/profile/";
	                	name+= resource.getName();
	                	name+=".xml";
	                	IFile file = resource.getProject().getFile(name);
	                	if(!file.isAccessible()){
	                		strCompPath="";
	                		while (resource.getType() != IResource.ROOT) {
		                		resource = resource.getParent();
		                		name=resource.getName();
		                		name+= "/profile/"; 
		                		name+=resource.getName();
		                		name+=".xml";
			                	file = resource.getProject().getFile(name);
			                	if(file.isAccessible()){
			                		strCompPath=resource.getRawLocation().toString();
			                		break;
			                	}
		                    }
	                	}else{
	                		strCompPath=resource.getRawLocation().toString();
	                	}
	                }else{
	                	strCompPath="";
	                }
	            }
	        }
		}

	}

}
