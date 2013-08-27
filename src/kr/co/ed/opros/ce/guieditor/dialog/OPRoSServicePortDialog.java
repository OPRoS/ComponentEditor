package kr.co.ed.opros.ce.guieditor.dialog;

import java.util.HashMap;

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
import org.jdom.Document;

public class OPRoSServicePortDialog extends Dialog {
	
	private String portName;
	private String portType;
	private String portDescript;
	private String portRefer;
	private HashMap<String,Document> map;
	private boolean isRequired=false;
	private OPRoSServicePortComposite contents;
	private OPRoSElementBaseModel model=null;
	private OPRoSBodyElementModel bodyModel=null;
	
	public OPRoSServicePortDialog(Shell parentShell, boolean isRequired,
			OPRoSElementBaseModel model, OPRoSBodyElementModel bodyModel) {
		super(parentShell);
		map=new HashMap<String,Document>();
		this.isRequired=isRequired;
		this.model=model;
		this.bodyModel=bodyModel;
		this.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX
				| SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		contents = new OPRoSServicePortComposite(parent, SWT.NULL, 1, GridData.FILL_BOTH, isRequired,model,
				bodyModel.compEle);
		return contents;
	}

	@Override
	protected void okPressed() {
		portName=contents.getPortName();
		portType= contents.getPortType();
		portRefer=contents.getPortRefer();
		if(getPortName().isEmpty()){
			OPRoSUtil.openMessageBox(OPRoSStrings.getString("ServicePortErrorMessage0"), SWT.ERROR|SWT.ICON_ERROR);
			return;
		}
		if(model!=null){
			if(OPRoSUtil.isDuplicatePortName(getPortName(),true,bodyModel.compEle)){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("PortNameDuplicateError"), SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
		}else{
			if(OPRoSUtil.isDuplicatePortName(getPortName(),false,bodyModel.compEle)){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("PortNameDuplicateError"), SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
		}
		if(getPortType().isEmpty()||getPortType().compareTo(OPRoSStrings.getString("ServiceTypeDefaultValue"))==0){
			OPRoSUtil.openMessageBox(OPRoSStrings.getString("ServicePortErrorMessage3"), SWT.ERROR|SWT.ICON_ERROR);
			return;
		}
		if(getPortRefer().isEmpty()){
			OPRoSUtil.openMessageBox(OPRoSStrings.getString("ServicePortErrorMessage1"), SWT.ERROR|SWT.ICON_ERROR);
			return;
		}
		portDescript=contents.getPortDescript();
		map=contents.getServiceTypeMap();
		super.okPressed();
	}
	
	public String getPortName(){
		return portName;
	}
	
	public String getPortType(){
		return portType;
	}
	
	public String getPortRefer(){
		return portRefer;
	}
	
	public String getPortDescript(){
		return portDescript;
	}
	
	public HashMap<String,Document> getServiceTypeMap(){
		return map;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(OPRoSStrings.getString("ServicePortTitle"));
		super.configureShell(newShell);
	}
	
}
