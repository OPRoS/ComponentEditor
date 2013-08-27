package kr.co.ed.opros.ce.provider;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.ui.OPRoSDependenciesSectionCompositeEx;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;

public class TreeContentProviderEx extends TreeContentProvider {
	
	public OPRoSDependenciesSectionCompositeEx compsiteEx;
	
	public TreeContentProviderEx(OPRoSDependenciesSectionCompositeEx compsiteEx) {
		super(compsiteEx.getComponentName());
		this.compsiteEx = compsiteEx;
	}
	
	@Override
	public Object[] getElements(Object parent) {
		if (parent instanceof IContainer) {
			try {				
				List<Object> list = new ArrayList<Object>(); 
				Object[] obj = ((IContainer) parent).members();
				
				//컴포넌트로 등록된 폴더만 보이게 한다.
				for(int i=0; i<obj.length; i++) {
					if(obj[i] instanceof IFolder ) {						
						Iterator<String> iter = compsiteEx.getCompIter();
						while(iter.hasNext()) {
							String compName = iter.next();
							if(((IFolder)obj[i]).getName().equals(compName)) {
								list.add(obj[i]);
								break;
							}							
						}	
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
	public Object[] getChildren(Object parent) {
		try {
			if (parent instanceof IFolder) {
				
				boolean isAdd = true;
				
				Object[] obj = getElements(compsiteEx.getfBundleRoot());
				for(int i=0; i<obj.length; i++) {
					if(((IFolder) parent).getParent() == obj[i]) {
						isAdd = false;
						if(((IFolder) parent).getName().equals("lib") || ((IFolder) parent).getName().equals("res")) {
							return ((IFolder) parent).members();
						}
					}
					
				}
				if(isAdd) {
					return ((IFolder) parent).members();
				}
				
			}
				
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return new Object[0];
	}
	
}
