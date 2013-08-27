package kr.co.ed.opros.ce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class OPRoSNature implements IProjectNature {

	private IProject fProject;
	public final static String NATURE_ID = "kr.co.ed.core.oprosNature";

	public OPRoSNature() {
	}

	public OPRoSNature(IProject project) {
		setProject(project);
	}

	public static void addNature(IProject project, IProgressMonitor monitor) throws CoreException {
		addNature(project, NATURE_ID, monitor);
	}

	public static void removeNature(IProject project, IProgressMonitor monitor) throws CoreException {
		removeNature(project, NATURE_ID, monitor);
	}	
	
	
	public static void addNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();
		for (int i = 0; i < prevNatures.length; i++) {
			if (natureId.equals(prevNatures[i]))
				return;
		}
		String[] newNatures = new String[prevNatures.length + 1];
	
		System.arraycopy(prevNatures, 0, newNatures, 1, prevNatures.length);
		newNatures[0] = natureId;

		newNatures[0] = natureId;
		for (int i = 0; i < prevNatures.length; i++) {
			newNatures[i+1] = prevNatures[i];
		}
		
		description.setNatureIds(newNatures);
		project.setDescription(description, monitor);
	}


	@SuppressWarnings("unchecked")
	public static void removeNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();
		List newNatures = new ArrayList(Arrays.asList(prevNatures));
		newNatures.remove(natureId);
		description.setNatureIds((String[]) newNatures.toArray(new String[newNatures.size()]));
		project.setDescription(description, monitor);
	}

	/**
	 * @see IProjectNature#configure
	 */
	public void configure() throws CoreException {
	}

	/**
	 * @see IProjectNature#deconfigure
	 */
	public void deconfigure() throws CoreException {
	}

	/**
	 * @see IProjectNature#getProject
	 */
	public IProject getProject() {
		return fProject;
	}

	/**
	 * @see IProjectNature#setProject
	 */
	public void setProject(IProject project) {
		fProject = project;
	}

}
