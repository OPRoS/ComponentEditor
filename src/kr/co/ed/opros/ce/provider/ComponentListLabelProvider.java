package kr.co.ed.opros.ce.provider;

import java.io.File;

import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ComponentListLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		return OPRoSActivator.getImageDescriptor(IIconConstants.ICON_OPROS_COMPONENT).createImage();
	}

	@Override
	public String getText(Object element) {
		if(element instanceof IResource)
			return  ((IResource)element).getName();
		else if(element instanceof File) 
			return ((File)element).getName();
		else 
			return (String)element;
			
	}
	
}
