package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.FormLayoutFactory;
import kr.co.ed.opros.ce.IHelpContextIds;
import kr.co.ed.opros.ce.IIconConstants;
import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil2;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * ������� ������
 * @author hclee
 *
 */
public class OPRoSManifestPage extends OPRoSFormPage {
	
	public static final String PAGE_ID = EditorMessages.getString("OPRoSDependenciesEditor.ID");
	
	public static final String PAGE_NAME = EditorMessages.getString("OPRoSDependenciesEditor.PageName");
	
	public OPRoSManifestSection dependenciesSection;
	
	public OPRoSComponentGeneralSection generalSection;	

	public FormEditor editor;
	
	public IFile inputFile;
	

	public OPRoSManifestPage(FormEditor editor) {
		super(editor, PAGE_ID, PAGE_NAME);
		this.editor = editor;
		this.inputFile = ((OPRoSManifestEditor)editor).getInputFile();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setImage(OPRoSActivator.getDefault().getImage(IIconConstants.ICON_DEPENDENCIES_EDITOR));
		form.setText(EditorMessages.getString("OPRoSDependenciesEditor.WindowTitle"));
		form.getBody().setLayout(FormLayoutFactory.createFormGridLayout(true, 1));	
		
		
		generalSection = new OPRoSComponentGeneralSection(this, form.getBody(),toolkit);	
		dependenciesSection = new OPRoSManifestSection(this, form.getBody(),toolkit);		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), IHelpContextIds.BUILD_PAGE);
	}
	
	/**
	 * �������� ������� �����͸� �����Ѵ�.
	 * @return
	 */
	public OPRoSManifestEditor getOPRoSDependenciesEditor(){
		return (OPRoSManifestEditor)editor;
	}
	
	/**
	 * ������� ������ �����Ѵ�.
	 * @return
	 */
	public OPRoSManifestSection getDependenciesSection() {
		return dependenciesSection;
	}
	
	/**
	 * ������Ʈ ���ʷ� ������ �����Ѵ�.
	 * @return
	 */
	public OPRoSComponentGeneralSection getGeneralSection() {
		return generalSection;
	}
	
	/**
	 * ������Ʈ �̸��� �����Ѵ�.
	 * @return
	 */
	public String getComponentName() {
		String componentName = "";
		try{
			if(getInputFile() != null)
				componentName = OPRoSUtil2.getComponentContainer(getInputFile()).getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return componentName;
	}
	
	/**
	 * ������Ʈ �������� ����
	 * @return
	 */
	public String getComponentVersion() {
		return OPRoSUtil2.getComponentInfomation(getInputFile(), EditorMessages.getString("Manifest.Version")+".*");
	}
	
	/**
	 * ������Ʈ ���� ����
	 * @return
	 */
	public String getComponentDescription() {
		return OPRoSUtil2.getComponentInfomation(getInputFile(), EditorMessages.getString("Manifest.Description")+".*");
	}
	
	public IFile getInputFile() {
		return inputFile;
	}
	
}
