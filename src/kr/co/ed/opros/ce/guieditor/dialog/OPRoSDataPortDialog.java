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

public class OPRoSDataPortDialog extends Dialog {
	private String portName="";
	private String portType="";
	private String portDescript="";
	private String portRefer="";
	private String portPolicy="";
	private String portQueueSize="";
	private HashMap<String,Document> map;
	private boolean isInput=true;
	private OPRoSDataPortDialogComposite contents;
	private OPRoSElementBaseModel model=null;
	private OPRoSBodyElementModel bodyModel=null;
	public OPRoSDataPortDialog(Shell parentShell, boolean isInput,OPRoSElementBaseModel model,OPRoSBodyElementModel bodyModel) {
		super(parentShell);
		map=new HashMap<String,Document>();
		this.isInput=isInput;
		this.model=model;
		this.bodyModel=bodyModel;
		this.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX
				| SWT.APPLICATION_MODAL);
	}

	@Override
	protected Control createDialogArea(Composite parent) {		
		contents = new OPRoSDataPortDialogComposite(parent, SWT.NULL, 1, GridData.FILL_BOTH, isInput, model,bodyModel);
		return contents;
	}
	@Override
	protected void okPressed() {
		portName=contents.getPortName();
		portType= contents.getPortType();
		portRefer=contents.getPortRefer();
		if(getPortName().isEmpty()){
			OPRoSUtil.openMessageBox(OPRoSStrings.getString("DataPortErrorMessage0"), SWT.ERROR|SWT.ICON_ERROR);
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
			OPRoSUtil.openMessageBox(OPRoSStrings.getString("DataPortErrorMessage3"), SWT.ERROR|SWT.ICON_ERROR);
			return;
		}
		if(isInput){
			portPolicy = contents.getPortPolicy();
			portQueueSize = contents.getPOrtQueueSize();
			if(portQueueSize.isEmpty()){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("DataPortErrorMessage2"), SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
		}
		portDescript=contents.getPortDescript();
		map=contents.getDataTypeMap();
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
	public String getPortPolicy(){
		return portPolicy;
	}
	public String getPortQueueSize(){
		return portQueueSize;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(OPRoSStrings.getString("DataPortTitle"));
		super.configureShell(newShell);
	}
	
}
