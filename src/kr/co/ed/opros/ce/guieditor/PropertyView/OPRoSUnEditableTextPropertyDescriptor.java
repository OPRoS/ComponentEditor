package kr.co.ed.opros.ce.guieditor.PropertyView;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class OPRoSUnEditableTextPropertyDescriptor extends TextPropertyDescriptor {

	public OPRoSUnEditableTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return null; 
	}
	
}
