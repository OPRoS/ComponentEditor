package kr.co.ed.opros.ce.ui;

import java.util.Iterator;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.provider.TreeContentProviderEx;
import kr.co.ed.opros.ce.provider.TreeLabelProvider;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

public class OPRoSDependenciesSectionCompositeEx extends
		OPRoSDependenciesSectionComposite {
	
	public Iterator<String> compIter;

	@Override
	protected void setTreeViewerProvider(CheckboxTreeViewer fTreeViewer) {
		fTreeViewer.setContentProvider(new TreeContentProviderEx(this));
		fTreeViewer.setLabelProvider(new TreeLabelProvider());
	}

	@Override
	public void setInput(IContainer obj) {		
		if(obj instanceof IProject) {
			setCompIter(OPRoSUtil.getComponentList((IProject)obj));
			//if(getCompIter() != null)
				//super.setInput(obj);
		}
	}
	
	public Iterator<String> getCompIter() {
		return compIter;
	}

	public void setCompIter(Iterator<String> compIter) {
		this.compIter = compIter;
	}
	
}
