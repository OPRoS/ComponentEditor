package kr.co.ed.opros.ce;

import java.util.Iterator;

import org.eclipse.cdt.internal.core.model.CContainer;
import org.eclipse.cdt.internal.core.model.SourceRoot;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class OPRoSComponentDecorators implements ILabelDecorator {

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image decorateImage(Image arg1, Object arg0) {
		if(arg0 != null && arg0 instanceof CContainer) {
			IProject project = ((CContainer)arg0).getCProject().getProject();
			if(!OPRoSUtil2.isOPRoSProject(project))
				return null;
			
			if(!(((CContainer)arg0).getParent() instanceof SourceRoot))
				return null;
			
			Iterator<String> iter = OPRoSUtil.getComponentList(project);			
			while(iter.hasNext()) {
				if(((CContainer)arg0).getElementName().equals(iter.next())){
					return OPRoSActivator.getImageDescriptor(IIconConstants.ICON_OPROS_COMPONENT).createImage();
				}					
			}			
		}		
		return null;
	}

	@Override
	public String decorateText(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
