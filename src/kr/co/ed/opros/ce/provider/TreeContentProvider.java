package kr.co.ed.opros.ce.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProvider implements ITreeContentProvider {
	
	public String componentName;	

	public TreeContentProvider(String compName) {
		this.componentName = compName;
	}	
	
	

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(Object parent) {
		/*
		try {
			if (parent instanceof IFolder)
				return ((IFolder) parent).members();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		*/
		return new Object[0];
	}
	
	@Override
	public Object[] getElements(Object parent) {
		if (parent instanceof IContainer) {
			try {
				List<Object> list = new ArrayList<Object>(); 
				Object[] obj = ((IContainer) parent).members();
				
				//해당 컴포넌트 폴더에서 res 폴더만 출력한다.
				for(int i=0; i<obj.length; i++) {
					if(obj[i] instanceof IResource) {
						IResource resource = (IResource)obj[i];
						boolean isExclusion = false;
						for(String str : getExclusionResource()) {							
							if(resource.getName().equals(str)){
								isExclusion = true;
								break;
							}							
						}
						if(!isExclusion)
							list.add(obj[i]);
					}
				}			
				return list.toArray();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element != null && element instanceof IResource) {
			return ((IResource) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IFolder){	
			return getChildren(element).length > 0;
		}			
		return false;
	}
	
	public void setComponentName(String name) {
		this.componentName = name;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	/**
	 * 트리뷰에서 제외할 리소스 목록을 리턴한다.
	 * @return
	 */
	public String[] getExclusionResource() {
		String[] temp = { "Debug", "profile", "Release",
				getComponentName() + ".cpp", getComponentName() + ".h",
				"OPRoS.mf" };
		
		return temp;
	}

}
