package kr.co.ed.opros.ce.guieditor.dialog;

import java.util.HashMap;
import java.util.Iterator;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Document;
import org.jdom.Element;

public class MonitorVariableInputDialog extends Dialog {
	private String name;
	private String type;
	private String usingDataTypeFileName="";
	private Text nameText;
	private Combo typeCombo;
	protected Label messageLabel;
	private HashMap<String,String> map;
	protected OPRoSComponentElementModel compEle=null;
	public MonitoringVariableModel model;
	
	public MonitorVariableInputDialog(Shell parentShell, OPRoSComponentElementModel compEle) {
		super(parentShell);
		this.compEle=compEle;
		map=new HashMap<String, String>();
	}
	
	public MonitorVariableInputDialog(Shell parentShell, MonitoringVariableModel model, OPRoSComponentElementModel compEle) {
		this(parentShell, compEle);
		this.model = model;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(OPRoSStrings.getString("MonitorVariableInputDlgTitle"));
		Composite comp = OPRoSUtil.createComposite(parent, SWT.NONE, 2, SWT.NONE);
		OPRoSUtil.createLabel(comp, OPRoSStrings.getString("MonitorVariableInputDlgNameLabel"),	SWT.NONE, 1, 5, 0, 0, GridData.BEGINNING);
		nameText = OPRoSUtil.createText(comp, SWT.BORDER|SWT.SINGLE, 1, 0, 0, 0, 150, 0, GridData.BEGINNING);
		nameText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent evt) {
				if(OPRoSUtil.isDuplicateMonitorVariableName(nameText.getText(), false, compEle) && model == null){
					messageLabel.setText(OPRoSStrings.getString("MonitorVariableNameDuplicateError"));
				}else{
					messageLabel.setText("");
				}
			}
        });
		OPRoSUtil.createLabel(comp, OPRoSStrings.getString("MonitorVariableDlgTypeLabel"),	SWT.NONE, 1, 5, 0, 0, GridData.BEGINNING);
		typeCombo = OPRoSUtil.createCombo(comp, SWT.SIMPLE|SWT.SINGLE,OPRoSUtil.dataNotBoostTypes,0,1,0,0,0,150,0,GridData.BEGINNING);
		if(compEle!=null){
			Iterator<OPRoSElementBaseModel> it = compEle.getDataTypesModel().getChildrenList().iterator();
			while(it.hasNext()){
				OPRoSDataTypeElementModel dataModel=(OPRoSDataTypeElementModel)it.next();
				Iterator<Element> eles;
				Element ele;
				Document doc = dataModel.getDataTypeDoc();
				eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
				while(eles.hasNext()){
					ele=eles.next();
					typeCombo.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")), 0);
					map.put(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")), dataModel.getDataTypeFileName());
				}
			}
		}
		typeCombo.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				String strDataType = typeCombo.getText();
				if(strDataType.compareTo(OPRoSStrings.getString("DataType7"))==0||
  				   strDataType.compareTo(OPRoSStrings.getString("DataType8"))==0||
  				   strDataType.compareTo(OPRoSStrings.getString("DataType9"))==0||
  				   strDataType.compareTo(OPRoSStrings.getString("DataType10"))==0||
  				   strDataType.compareTo(OPRoSStrings.getString("DataType11"))==0||
  				   strDataType.compareTo(OPRoSStrings.getString("DataType12"))==0||
  				   strDataType.compareTo(OPRoSStrings.getString("DataType13"))==0)
				{
					OPRoSValueTypeOfDataType dlg=null;
					dlg = new OPRoSValueTypeOfDataType(getShell(),strDataType);
					dlg.open();
					if(dlg.getReturnCode()==Dialog.OK){
						if(!dlg.getValueType().isEmpty()){
							typeCombo.add(strDataType+"<"+dlg.getValueType()+">");
							typeCombo.select(typeCombo.getItemCount()-1);
						}
					}
				}
			}
        });
		messageLabel =  OPRoSUtil.createLabel(comp, "                                        ",
        		SWT.NONE,2,5,0,0,GridData.BEGINNING);
        messageLabel.setForeground(ColorConstants.red);
        Initialization();
		return comp;
	}
	
	public void Initialization() {
		if(model != null) {
			nameText.setText(model.getName());
			for(int i=0; i<typeCombo.getItems().length; i++) {
				if(model.getType().equals(typeCombo.getItems()[i])){
					typeCombo.select(i);
					break;
				}
			}
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId==Dialog.OK){
			setName(nameText.getText());
			if(getName().isEmpty()){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("MonitorVariableNameEmptyError"), SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
			setType(typeCombo.getText());
			if(getType().isEmpty()){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("MonitorVariableTypeEmptyError"), SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
			if(OPRoSUtil.isDuplicateMonitorVariableName(getName(),false,compEle) && model == null){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("MonitorVariableNameDuplicateError"), SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
			
			if(map.containsKey(type)){
				setUsingDataTypeFileName(map.get(getType()));
			}
		}
		super.buttonPressed(buttonId);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsingDataTypeFileName() {
		return usingDataTypeFileName;
	}
	public void setUsingDataTypeFileName(String usingDataTypeFileName) {
		this.usingDataTypeFileName = usingDataTypeFileName;
	}
}
