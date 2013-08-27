package kr.co.ed.opros.ce.provider;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ComponentListContentProvider implements IStructuredContentProvider{

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof File && ((File)inputElement).isDirectory() )	{
			List<File> list = new ArrayList<File>();
			for(File file : ((File)inputElement).listFiles()) {
				if(file.isDirectory())
					list.add(file);
			}
			return list.toArray(new File[list.size()]);		
		}
		else if (inputElement instanceof IProject) {
			try {
				List<IResource> list = new ArrayList<IResource>();
				IResource[] res = ((IProject)inputElement).members();
				for(int i=0; i<res.length; i++) {
					if(res[i] instanceof IFolder) {
						Iterator<String> iter = OPRoSUtil.getComponentList((IProject)inputElement);
						while(iter.hasNext()) {
							if(((IFolder)res[i]).getName().equals(iter.next())) {
								list.add(res[i]);
								break;
							}
						}
					}
				}
				return list.toArray();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new Object[0];
	}

}
