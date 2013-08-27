package kr.co.ed.opros.ce.provider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.model.IWorkbenchAdapter;


public class TreeLabelProvider extends LabelProvider {
	
	private ResourceManager resourceManager;

	@Override
	public Image getImage(Object element) {
		IWorkbenchAdapter adapter = getAdapter(element);
        if (adapter == null) {
            return null;
        }
        ImageDescriptor descriptor = adapter.getImageDescriptor(element);
        if (descriptor == null) {
            return null;
        }
        descriptor = decorateImage(descriptor, element);
		return (Image) getResourceManager().get(descriptor);
	}

	@Override
	public String getText(Object element) {
		IWorkbenchAdapter adapter = getAdapter(element);
        if (adapter == null) {
            return "";
        }
        String label = adapter.getLabel(element);
        return decorateText(label, element);
	}
	
	protected final IWorkbenchAdapter getAdapter(Object o) {
        return (IWorkbenchAdapter)Util.getAdapter(o, IWorkbenchAdapter.class);
    }
	
	protected ImageDescriptor decorateImage(ImageDescriptor input,
            Object element) {
        return input;
    }
	
	private ResourceManager getResourceManager() {
		if (resourceManager == null) {
			resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return resourceManager;
	}
	
	protected String decorateText(String input, Object element) {
        return input;
    }
	
}
