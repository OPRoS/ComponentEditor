package kr.co.ed.opros.ce.guieditor.dialog;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class OPRoSValueTypeOfDataType extends Dialog {
	protected String valueType;
	protected Combo valueTypeCombo;
	private String dataType;

	private static final String[] dataTypes = {
		OPRoSStrings.getString("DataType0"),
		OPRoSStrings.getString("DataType1"),
		OPRoSStrings.getString("DataType2"),
		OPRoSStrings.getString("DataType3"),
		OPRoSStrings.getString("DataType4"),
		OPRoSStrings.getString("DataType5")};
	public OPRoSValueTypeOfDataType(Shell parent,String dataType) {
		super(parent);
		valueType="";
		this.dataType=dataType;
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText("Set value type of DataType");
		Composite comp = OPRoSUtil.createComposite(parent, SWT.NONE, 2, SWT.NONE);
		OPRoSUtil.createLabel(comp, "Value type of "+dataType+" : ", SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		valueTypeCombo =OPRoSUtil.createCombo(comp, SWT.SIMPLE|SWT.SINGLE,dataTypes,0,1,0,0,0,150,0,GridData.BEGINNING);
		return comp;
	}
	
	@Override
	protected void okPressed() {
		valueType=valueTypeCombo.getText();
		super.okPressed();
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

}
