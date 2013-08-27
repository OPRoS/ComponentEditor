package kr.co.ed.opros.ce.wizards.export;

import java.io.File;
import java.util.List;

import kr.co.ed.opros.ce.FileUtils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * 사용자 컴포넌트를 템플릿으로 등록하는 위저드
 * @author hclee
 *
 */
public class UserComponentRegisterWizard extends Wizard implements
		IExportWizard {
	
	public UserComponentRegisterWizardPage page;
	
	public static final String USER_COMPONENT_LOCATION = "C:\\test\\";
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Component Register");
	    setNeedsProgressMonitor(true);
		
	    page = new UserComponentRegisterWizardPage(workbench, selection);
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public boolean canFinish() {
		if(USER_COMPONENT_LOCATION != null && !USER_COMPONENT_LOCATION.equals("")) {
			return true;
		}		
		
		return false;
	}

	@Override
	public boolean performFinish() {
		List<IFolder> list = page.getSelectedComponent();
		for(IFolder folder : list) {
			String name = folder.getName();
			String targetFolder = USER_COMPONENT_LOCATION +page.getCategoryName()+"\\"+ name+"\\";
			if(!(new File(targetFolder).isDirectory())) {
				new File(targetFolder).mkdirs();
			}
			
			File cppFile = folder.getFile(name+".cpp").getLocation().toFile();
			if(cppFile != null && cppFile.isFile())
				FileUtils.copyDirectory(cppFile, new File(targetFolder + cppFile.getName()));
			
			File headerFile = folder.getFile(name+".h").getLocation().toFile();		
			if(headerFile != null && headerFile.isFile())
				FileUtils.copyDirectory(headerFile, new File(targetFolder + headerFile.getName()));
			
			File[] profiles = folder.getFolder("profile").getLocation().toFile().listFiles();
			for(File pro : profiles) {
				if(pro != null && pro.isFile())
					FileUtils.copyDirectory(pro, new File(targetFolder + pro.getName()));
			}
			
			
		}
		return true;
	}

}
