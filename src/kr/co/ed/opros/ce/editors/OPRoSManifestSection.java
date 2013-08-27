package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.FormLayoutFactory;
import kr.co.ed.opros.ce.ui.OPRoSDependenciesSectionComposite;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * lib�� res�� ������ÿ� �߰��ϱ� ���� Ʈ�� ������ �����ϴ� ����
 * @author hclee
 *
 */
public class OPRoSManifestSection extends OPRosFormSection {

	public OPRoSManifestPage page;
	
	public OPRoSDependenciesSectionComposite selectionSection;

	public OPRoSManifestSection(OPRoSManifestPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit);
		this.page = page;		
		
		createClient(getSection(), toolkit);
		getSection().setText(EditorMessages.getString("OPRoSDependenciesEditor.RequiredFileGroup.Title"));
		getSection().setDescription(EditorMessages.getString("OPRoSDependenciesEditor.RequiredFileGroup.Description"));
	}	

	@Override
	protected void createClient(Section section, FormToolkit toolkit) {
		Composite container = createClientContainer(section, 2, toolkit);
		
		selectionSection = new OPRoSDependenciesSectionComposite(page.getOPRoSDependenciesEditor());
		selectionSection.crateTreeView(container);
		
		toolkit.paintBordersFor(container);
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setClient(container);
		
	}

	@Override
	public void dispose() {
		super.dispose();
		selectionSection.dispose();
	}
	
	public CheckboxTreeViewer getTreeViewer(){
		return selectionSection.getTreeViewer();
	}
	
	/**
	 * Ʈ���� �����ϴ� ��������Ʈ Ŭ���� ����.
	 * @return
	 */
	public OPRoSDependenciesSectionComposite getSelectionSection() {
		return selectionSection;
	}
	
}
