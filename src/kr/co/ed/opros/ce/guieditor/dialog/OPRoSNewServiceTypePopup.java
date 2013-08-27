/*******************************************************************************
* Copyright (C) 2008 ED Corp., All rights reserved
*  
*  
* This programs is the production of ED Copr. research & development activity;
* you cannot use it and cannot redistribute it and cannot modify it and
* cannot  store it in any media(disk, photo or otherwise) without the prior
* permission of ED.
* 
* You should have received the license for this program to use any purpose.
* If not, contact the ED CORPORATION, 517-15, SangDaeWon-Dong, JungWon-Gu,
* SeongNam-City, GyeongGi-Do, Korea. (Zip : 462-806), http://www.ed.co.kr
* 
* NO PART OF THIS WORK BY THE THIS COPYRIGHT HEREON MAY BE REPRODUCED, STORED
* IN RETRIEVAL SYSTENS, IN ANY FORM OR BY ANY MEANS, ELECTRONIC, MECHANICAL,
* PHOTOCOPYING, RECORDING OR OTHERWISE, WITHOUT THE PRIOR PERMISSION OF ED Corp.
*
* @Author: sevensky(Juwon Kim), (jwkim@ed.co.kr, sevensky@mju.ac.kr)
********************************************************************************/
package kr.co.ed.opros.ce.guieditor.dialog;

import java.util.Iterator;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Document;
import org.jdom.Element;

public class OPRoSNewServiceTypePopup extends Dialog {
	protected String name;
	protected String type;
	protected String callType;
	protected Text nameText;
	protected Combo typeCombo;
	protected Combo callTypeCombo;
	protected int nType;
	protected OPRoSElementBaseModel compEle=null;

	private static final String[] callTypes = {
		"blocking","nonblocking"
	};
	
	public OPRoSNewServiceTypePopup(Shell parentShell) {
		super(parentShell);
		nType = 0;
		name="";
		type="";
		callType="";
	}
	public OPRoSNewServiceTypePopup(Shell parentShell, int nType, OPRoSElementBaseModel compEle) {
		this(parentShell);
		this.nType=nType;
		this.compEle=compEle;
	}

	@SuppressWarnings("unchecked")
	protected Control createDialogArea(Composite parent){
		parent.getShell().setText("Input New Service");
		Composite comp = OPRoSUtil.createComposite(parent, SWT.NONE, 2, SWT.NONE);
		if(nType==0)
			OPRoSUtil.createLabel(comp, "Service Type Name : ", SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		else if(nType==1)
			OPRoSUtil.createLabel(comp, "Service Name : ", SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		else
			OPRoSUtil.createLabel(comp, "Parameter Name : ", SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		nameText = OPRoSUtil.createText(comp, SWT.BORDER|SWT.SINGLE, 1, 0, 0, 0, 150, 0, GridData.BEGINNING);
		if(nType==0){
		}
		else if(nType==1)
			OPRoSUtil.createLabel(comp, "Return Data Type : ", SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		else
			OPRoSUtil.createLabel(comp, "Parameter Data Type : ", SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		if(nType==0){
			
		}
		else if(nType==1){
			typeCombo =OPRoSUtil.createCombo(comp, SWT.SIMPLE|SWT.SINGLE,OPRoSUtil.dataNotBoostTypes,0,1,0,0,0,150,0,GridData.BEGINNING);
			typeCombo.add("void", 0);
			typeCombo.select(0);
			if(compEle!=null){
				OPRoSComponentElementModel model = getComponentElementModel(compEle);
				OPRoSElementBaseModel dataType = model.getDataTypesModel();
				
				Iterator<OPRoSElementBaseModel> it = getComponentElementModel(compEle).getDataTypesModel().getChildrenList().iterator();
				while(it.hasNext()){
					OPRoSDataTypeElementModel dataModel=(OPRoSDataTypeElementModel)it.next();
					Iterator<Element> eles;
					Element ele;
					Document doc = dataModel.getDataTypeDoc();
					eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
					while(eles.hasNext()){
						ele=eles.next();
						typeCombo.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")), 0);
					}
				}
			}
			OPRoSUtil.createLabel(comp, "Call Type : ", SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
			callTypeCombo =OPRoSUtil.createCombo(comp, SWT.READ_ONLY|SWT.SINGLE,callTypes,0,1,0,0,0,150,0,GridData.BEGINNING);
			typeCombo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					String strDataType = typeCombo.getText();
					if(strDataType.compareTo("void")==0){
						if(callTypeCombo.getItemCount()==1){
							callTypeCombo.add("nonblocking");
						}
//					}else if(strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type7"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type8"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type9"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type10"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type11"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type12"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type13"))==0)
//					{
//						OPRoSValueTypeOfDataTypeDialog dlg=null;
//						dlg = new OPRoSValueTypeOfDataTypeDialog(getShell(),strDataType);
//						dlg.open();
//						if(dlg.getReturnCode()==Dialog.OK){
//							if(!dlg.getValueType().isEmpty()){
//								typeCombo.add(strDataType+"<"+dlg.getValueType()+">");
//								typeCombo.select(typeCombo.getItemCount()-1);
//							}
//							else{
//								OPRoSUtil.openMessageBox(WizardMessages.getString("ErrorMessage"), SWT.OK|SWT.ICON_WARNING);
//							}
//						}
//						if(callTypeCombo.getItemCount()==2){
//							callTypeCombo.remove(1);
//						}
					}else{
						if(callTypeCombo.getItemCount()==2){
							callTypeCombo.remove(1);
						}
					}
				}
			});
		}
		else{
			typeCombo =OPRoSUtil.createCombo(comp, SWT.SIMPLE|SWT.SINGLE,OPRoSUtil.dataNotBoostTypes,0,1,0,0,0,150,0,GridData.BEGINNING);
			if(compEle!=null){
				Iterator<OPRoSElementBaseModel> it = getComponentElementModel(compEle).getDataTypesModel().getChildrenList().iterator();
				OPRoSDataTypeElementModel dataModel;
				Iterator<Element> eles;
				Element ele;
				Document doc;
				while(it.hasNext()){
					dataModel=(OPRoSDataTypeElementModel)it.next();
					doc = dataModel.getDataTypeDoc();
					eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
					while(eles.hasNext()){
						ele=eles.next();
						typeCombo.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")), 0);
					}
				}
			}
//			typeCombo.addModifyListener(new ModifyListener() {
//				@Override
//				public void modifyText(ModifyEvent e) {
//					String strDataType = typeCombo.getText();
//					if(strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type7"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type8"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type9"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type10"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type11"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type12"))==0||
//							strDataType.compareTo(WizardMessages.getString("PropertyWizardPage.PropertiesGroup.Type13"))==0)
//					{
//						OPRoSValueTypeOfDataTypeDialog dlg=null;
//						dlg = new OPRoSValueTypeOfDataTypeDialog(getShell(),strDataType);
//						dlg.open();
//						if(dlg.getReturnCode()==Dialog.OK){
//							if(!dlg.getValueType().isEmpty()){
//								typeCombo.add(strDataType+"<"+dlg.getValueType()+">");
//								typeCombo.select(typeCombo.getItemCount()-1);
//							}
//							else{
//								OPRoSUtil.openMessageBox(WizardMessages.getString("ErrorMessage"), SWT.OK|SWT.ICON_WARNING);
//							}
//						}
//					}
//				}
//			});
		}
		
		return comp;
	}
	
	public OPRoSComponentElementModel getComponentElementModel(OPRoSElementBaseModel model) {
		if(model instanceof OPRoSComponentElementModel)
			return (OPRoSComponentElementModel)model;
		
		if(model.getParent() != null) {			
			getComponentElementModel(model.getParent());
		}		
		return null;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		name = nameText.getText();
		if(typeCombo!=null)
			type = typeCombo.getText();
		if(callTypeCombo!=null)
			callType = callTypeCombo.getText();
		super.buttonPressed(buttonId);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type=type;
	}
	public String getCallType(){
		return callType;
	}
}
