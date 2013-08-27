package kr.co.ed.opros.ce.guieditor.dialog;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class OPRoSEventPortDialog extends Dialog {
	private String portName="";
	private String portType="";
	private String portDescript="";
	private boolean isInput=true;
	private String usingDataTypeFileName="";
	private OPRoSEventPortDialogComposite contents;
	private OPRoSElementBaseModel model=null;
	private OPRoSBodyElementModel bodyModel = null;
	public OPRoSEventPortDialog(Shell parentShell,boolean isInput,OPRoSElementBaseModel model,
			OPRoSBodyElementModel bodyModel) {
		super(parentShell);
		this.isInput=isInput;
		this.model=model;
		this.bodyModel=bodyModel;
		this.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX
				| SWT.APPLICATION_MODAL);
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		contents = new OPRoSEventPortDialogComposite(parent, SWT.NULL, 1, GridData.FILL_BOTH,isInput,
				model,bodyModel.compEle);
		return contents;
	}
	@Override
	protected void okPressed() {
		portName=contents.getPortName();
		portType= contents.getPortType();
		if(getPortName().isEmpty()){
			OPRoSUtil.openMessageBox(OPRoSStrings.getString("EventPortErrorMessage0"), 
					SWT.ERROR|SWT.ICON_ERROR);
			return;
		}
		if(model!=null){
			if(OPRoSUtil.isDuplicatePortName(getPortName(),true,bodyModel.compEle)){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("PortNameDuplicateError"), 
						SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
		}else{
			if(OPRoSUtil.isDuplicatePortName(getPortName(),false,bodyModel.compEle)){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("PortNameDuplicateError"), 
						SWT.ERROR|SWT.ICON_ERROR);
				return;
			}			
		}
		if(getPortType().isEmpty()||
				getPortType().compareTo(OPRoSStrings.getString("ServiceTypeDefaultValue"))==0){
			OPRoSUtil.openMessageBox(OPRoSStrings.getString("EventPortErrorMessage1"), 
					SWT.ERROR|SWT.ICON_ERROR);
			return;
		}
		portDescript=contents.getPortDescript();
		if(contents.getMap().containsKey(portType)){
			usingDataTypeFileName=contents.getMap().get(portType);
		}
		super.okPressed();
	}
	public String getPortName(){
		return portName;
	}
	public String getPortType(){
		return portType;
	}
	public String getPortDescript(){
		return portDescript;
	}
	public String getUsingDataTypeFileName(){
		return usingDataTypeFileName;
	}
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(OPRoSStrings.getString("EventPortTitle"));
		super.configureShell(newShell);
	}
	
}
