package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.FormLayoutFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 오프로스 메니페스트 기본정보 설정 섹션
 * @author hclee
 *
 */
public class OPRoSComponentGeneralSection extends OPRosFormSection {
	
	public OPRoSManifestPage page;
	
	public Text text_compName;
	
	public Text text_compVersion;
	
	public Text text_compDesc;

	public OPRoSComponentGeneralSection(OPRoSManifestPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit);
		this.page = page;		
		
		createClient(getSection(), toolkit);
		getSection().setText(EditorMessages.getString("OPRoSDependenciesEditor.GeneralGroup.Title"));
		getSection().setDescription(EditorMessages.getString("OPRoSDependenciesEditor.GeneralGroup.Description"));
	}

	@Override
	protected void createClient(Section section, FormToolkit toolkit) {
		Composite container = createClientContainer(section, 2, toolkit);

		Label label = new Label(container, SWT.NONE);
		label.setText("Name : ");
		
		text_compName = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		text_compName.setLayoutData(gd);
		
		label = new Label(container, SWT.NONE);
		label.setText("Version : ");
		
		text_compVersion = new Text(container, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		text_compVersion.setLayoutData(gd);
		
		
		label = new Label(container, SWT.NONE);
		label.setText("Description : ");
		gd = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		//label.setLayoutData(gd);
		
		text_compDesc = new Text(container, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		//gd.heightHint = 100;
		text_compDesc.setLayoutData(gd);
		
		
		
		toolkit.paintBordersFor(container);
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setClient(container);
		
		initialization();
	}
	
	/**
	 * 초기화
	 */
	public void initialization() {
		text_compName.setText(page.getComponentName());
		text_compVersion.setText(page.getComponentVersion());
		text_compDesc.setText(page.getComponentDescription());
		
		addTextModifyListener(text_compVersion);
		addTextModifyListener(text_compDesc);
	}
	
	public void addTextModifyListener(Text text) {
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				((OPRoSManifestEditor)page.getEditor()).setDirty(true);
			}
		});
	}
	
	/**
	 * 컴포넌트 버전을 리턴한다.
	 * @return
	 */
	public String getComponentVersion() {
		return text_compVersion.getText();
	}
	
	/**
	 * 컴포넌트 설명을 리턴한다.
	 * @return
	 */
	public String getComponentDescription() {
		return text_compDesc.getText();
	}

}
