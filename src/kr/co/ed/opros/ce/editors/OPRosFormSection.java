package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.FormLayoutFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public abstract class OPRosFormSection extends SectionPart {
	
	public OPRoSManifestPage dPage;
	
	public OPRosFormSection(OPRoSManifestPage page, 
			Composite parent, FormToolkit toolkit){
		super(parent, toolkit, ExpandableComposite.TITLE_BAR |Section.DESCRIPTION);
		dPage = page;	
		
		getSection().clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		getSection().setData("part", this); //$NON-NLS-1$
		
		
	}
	
	protected abstract void createClient(Section section, FormToolkit toolkit);
	
	protected IProject getProject() {
		return dPage.getOPRoSDependenciesEditor().getProject();
	}
	
	protected Composite createClientContainer(Composite parent, int span, FormToolkit toolkit) {
		Composite container = toolkit.createComposite(parent);
		container.setLayout(FormLayoutFactory.createSectionClientGridLayout(false, span));
		return container;
	}
}
