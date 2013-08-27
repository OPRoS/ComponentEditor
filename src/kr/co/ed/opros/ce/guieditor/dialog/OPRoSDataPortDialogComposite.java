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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.wizards.WizardMessages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.jdom.Document;
import org.jdom.Element;

public class OPRoSDataPortDialogComposite extends Composite {
	protected Text dataNameText;
	protected Combo dataTypeCombo;
	protected Text dataUsageText;
	protected Combo dataPolicyCombo;
	protected Text portQueueingSizeText;
	protected Text dataTypeReferenceText;
	protected Button referenceDelButton;
	protected List dataTypeList;
	protected Text dataPortDescriptionText;
	protected Label messageLabel;
	protected HashMap<String,Document> map;
	protected boolean isInput=true;
	protected Button dataNewReferenceButton;
	protected OPRoSElementBaseModel modifyModel=null;
	protected OPRoSBodyElementModel bodyModel =null;
	
	private static final String[] dataPolicies = {
		OPRoSStrings.getString("DataPortPolicy0"),
		OPRoSStrings.getString("DataPortPolicy1"),
		OPRoSStrings.getString("DataPortPolicy2")};

	public OPRoSDataPortDialogComposite(Composite parent, int style, int column, int gridStyle, boolean isInput,OPRoSElementBaseModel model,OPRoSBodyElementModel bodyModel) {
		super(parent, style);
		modifyModel=model;
		this.bodyModel=bodyModel;
		this.isInput=isInput;
		map = new HashMap<String,Document>();
		this.setFont(parent.getFont());
        GridLayout layout = new GridLayout();
        layout.numColumns = column;
        this.setLayout(layout);
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        createDataPortsGroup(this);
	}

	@SuppressWarnings("unchecked")
	protected void createDataPortsGroup(Composite parent){
		Group compGroup=null;
		if(isInput)
			compGroup = OPRoSUtil.createGroup(parent, SWT.NONE, OPRoSStrings.getString("DataInPortTitle"),
				8, GridData.BEGINNING);
		else
			compGroup = OPRoSUtil.createGroup(parent, SWT.NONE, OPRoSStrings.getString("DataOutPortTitle"),
					8, GridData.BEGINNING);
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortNameLabel"),
        		SWT.NONE,1,5,1,0,GridData.BEGINNING);
        dataNameText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,3,0,1,0,100,0,GridData.BEGINNING);
        dataNameText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent evt) {
				if(modifyModel==null){
					if(OPRoSUtil.isDuplicatePortName(dataNameText.getText(),false,bodyModel.compEle)){
						messageLabel.setText(OPRoSStrings.getString("PortNameDuplicateError"));
					}else{
						messageLabel.setText("");
					}
				}else{
					if(OPRoSUtil.isDuplicatePortName(dataNameText.getText(),true,bodyModel.compEle)){
						messageLabel.setText(OPRoSStrings.getString("PortNameDuplicateError"));
					}else{
						messageLabel.setText("");
					}
				}
			}
        });
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortTypeLabel"),
        		SWT.NONE,1,5,1,0,GridData.BEGINNING);
        dataTypeCombo = OPRoSUtil.createCombo(compGroup,SWT.SIMPLE|SWT.SINGLE,OPRoSUtil.dataNotBoostTypes,-1,3,0,1,0,100,0,GridData.BEGINNING);
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortUsageLabel"),
        		SWT.NONE,1,5,1,0,GridData.BEGINNING);
        dataUsageText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,3,0,1,0,100,0,GridData.BEGINNING);
        dataUsageText.setEditable(false);
        if(isInput)
        	dataUsageText.setText(OPRoSStrings.getString("DataPortUsage0"));
        else
        	dataUsageText.setText(OPRoSStrings.getString("DataPortUsage1"));
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortReferLabel"),
        		SWT.NONE,1,5,1,0,GridData.BEGINNING);
        dataTypeReferenceText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,3,0,1,0,100,0,GridData.BEGINNING);
        dataTypeReferenceText.setEditable(false);
        if(isInput){
	        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortPolicyLabel"),
	        		SWT.NONE,1,5,1,0,GridData.BEGINNING);
	        dataPolicyCombo = OPRoSUtil.createCombo(compGroup, SWT.READ_ONLY|SWT.SINGLE,dataPolicies,0,3,0,1,0,70,0,GridData.FILL_HORIZONTAL);
	        dataPolicyCombo.addModifyListener(new ModifyListener(){

				@Override
				public void modifyText(ModifyEvent evt) {
					String strPolicy = dataPolicyCombo.getText();
					if(strPolicy.compareTo(OPRoSStrings.getString("DataPortPolicy2"))==0){
						portQueueingSizeText.setText("1");
					}
				}
	        	
	        });
	        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortQueueSizeLabel"),
	        		SWT.NONE,1,5,1,0,GridData.BEGINNING);
	        portQueueingSizeText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,3,0,1,0,100,0,GridData.BEGINNING);
	        portQueueingSizeText.addVerifyListener(
	        		new VerifyListener(){
	        			public void verifyText(VerifyEvent e){
	        				e.doit=e.text.matches("[0-9]*");
	        			}
	        		});
        }
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortDscriptLabel"),
        		SWT.NONE,1,5,1,0,GridData.BEGINNING);
        dataPortDescriptionText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,7,0,1,0,50,0,GridData.FILL_HORIZONTAL);        
        
//        dataTypeCombo.addModifyListener(new ModifyListener(){
//			@Override
//			public void modifyText(ModifyEvent e) {
//				String strDataType = dataTypeCombo.getText();
//				if(strDataType.compareTo(OPRoSStrings.getString("DataType7"))==0||
//						strDataType.compareTo(OPRoSStrings.getString("DataType8"))==0||
//		  				strDataType.compareTo(OPRoSStrings.getString("DataType9"))==0||
//		  				strDataType.compareTo(OPRoSStrings.getString("DataType10"))==0||
//		  				strDataType.compareTo(OPRoSStrings.getString("DataType11"))==0||
//		  				strDataType.compareTo(OPRoSStrings.getString("DataType12"))==0||
//		  				strDataType.compareTo(OPRoSStrings.getString("DataType13"))==0)
//				{
//					OPRoSValueTypeOfDataTypeDialog dlg=null;
//					dlg = new OPRoSValueTypeOfDataTypeDialog(getShell(),strDataType);
//					dlg.open();
//					if(dlg.getReturnCode()==Dialog.OK){
//						if(!dlg.getValueType().isEmpty()){
//							dataTypeCombo.add(strDataType+OPRoSStrings.getString("DataTypeSymbol1")+dlg.getValueType()+OPRoSStrings.getString("DataTypeSymbol2"));
//							dataTypeCombo.select(dataTypeCombo.getItemCount()-1);
//						}
//					}
//				}
//			}
//        });
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("DataPortTypeListLabel"),
        		SWT.NONE,2,5,1,5,GridData.BEGINNING);
        
        referenceDelButton = new Button(compGroup, SWT.NONE);
        referenceDelButton.setText(OPRoSStrings.getString("DataPortDelReferBtn"));
        GridData gd = new GridData(SWT.FILL, SWT.RIGHT, true, false, 3, 1);
        gd.heightHint = 22;
        referenceDelButton.setLayoutData(gd);
        
        referenceDelButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				dataTypeReferenceText.setText("");
				dataTypeCombo.removeAll();
//				for(int i=0;i<20;i++)
//					dataTypeCombo.add(OPRoSUtil.dataTypes[i]);
				for(int i=0;i<7;i++)
					dataTypeCombo.add(OPRoSUtil.dataNotBoostTypes[i]);
			}
		});
        
        dataNewReferenceButton = new Button(compGroup, SWT.NONE);
        dataNewReferenceButton.setText(WizardMessages.getString("PortsWizardPage.MethodPorts.NewReferButtonText"));
        gd = new GridData(SWT.FILL, SWT.RIGHT, true, false, 3, 1);
        gd.heightHint = 22;
        dataNewReferenceButton.setLayoutData(gd);
        
        dataNewReferenceButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				OPRoSDataTypeInputDialog dlg = new OPRoSDataTypeInputDialog(null,bodyModel.compEle);
				dlg.open();
				if(dlg.getReturnCode()==InputDialog.OK){
					OPRoSComponentElementModel model = bodyModel.compEle;
					if(model!=null){
						OPRoSElementBaseModel dataTypesModel = model.getDataTypesModel();
						if(dataTypesModel!=null){
							OPRoSDataTypeElementModel element = new OPRoSDataTypeElementModel();
							element.setDataTypeFileName(dlg.getDataTypeFileName());
							element.setDataTypeDoc(dlg.getDataTypeDoc());
							dataTypesModel.addChild(element);
							dataTypeList.add(element.getDataTypeFileName());
							map.put(element.getDataTypeFileName(), element.getDataTypeDoc());
						}
					}
				}
			}
		});
        dataTypeList = OPRoSUtil.createList(compGroup,SWT.SINGLE|SWT.BORDER,8,5,1,0,330,70,GridData.FILL_HORIZONTAL);
        OPRoSComponentElementModel model = bodyModel.compEle;
        if(model!=null){
        	Iterator<OPRoSElementBaseModel> it = model.getDataTypesModel().getChildrenList().iterator();
        	while(it.hasNext()){
        		OPRoSDataTypeElementModel dataModel=(OPRoSDataTypeElementModel)it.next();
        		dataTypeList.add(dataModel.getDataTypeFileName());
        		map.put(dataModel.getDataTypeFileName(), dataModel.getDataTypeDoc());
        	}
        }
        dataTypeList.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e) {
				int index = dataTypeList.getSelectionIndex();
				dataTypeReferenceText.setText(dataTypeList.getItem(index));
				if(map.containsKey(dataTypeList.getItem(index))){
					Document doc = map.get(dataTypeList.getItem(index));
					Iterator<Element> eles;
					Element ele;
					eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
					dataTypeCombo.removeAll();
					while(eles.hasNext()){
						ele = eles.next();
						dataTypeCombo.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")));
					}
					dataTypeCombo.select(0);
				}
			}
 		});
        dataTypeList.addMouseListener(new MouseAdapter(){
        	public void mouseDoubleClick(MouseEvent e){
        		int index = dataTypeList.getSelectionIndex();
        		OPRoSDataTypeInputDialog dlg = 
        			new OPRoSDataTypeInputDialog(getShell(),dataTypeList.getItem(index),map.get(dataTypeList.getItem(index)),false,bodyModel.compEle);
        		dlg.open();
        	}
        });
        messageLabel =  OPRoSUtil.createLabel(compGroup, "                                        ",
        		SWT.NONE,8,5,1,0,GridData.BEGINNING);
        messageLabel.setForeground(ColorConstants.red);
        Label label = OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("NotifyNeedSourceModify"),
        		SWT.NONE,8,5,1,5,GridData.BEGINNING);
        label.setForeground(ColorConstants.blue);
        if(modifyModel!=null){
    		OPRoSPortElementBaseModel element;
       		element = (OPRoSPortElementBaseModel)modifyModel;
       		dataNameText.setText(element.getName());
       		dataUsageText.setText(element.getUsage());
       		int index;
        	if(isInput){
        		index=dataTypeList.indexOf(((OPRoSDataInPortElementModel)element).getReference());
        		dataPolicyCombo.select(dataPolicyCombo.indexOf(((OPRoSDataInPortElementModel)element).getQueueingPolicy()));
        		portQueueingSizeText.setText(((OPRoSDataInPortElementModel)element).getQueueSize());
        	}
        	else{
        		index=dataTypeList.indexOf(((OPRoSDataOutPortElementModel)element).getReference());
        	}
        	if(index>=0){
        		dataTypeList.select(index);
        		dataTypeReferenceText.setText(dataTypeList.getItem(index));
        		if(map.containsKey(dataTypeList.getItem(index))){
					Document doc = map.get(dataTypeList.getItem(index));
					Iterator<Element> eles;
					Element ele;
					eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
					dataTypeCombo.removeAll();
					while(eles.hasNext()){
						ele = eles.next();
						dataTypeCombo.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")));
					}
					dataTypeCombo.select(dataTypeCombo.indexOf(element.getType()));
				}
        	}else{
        		int nIndex = dataTypeCombo.indexOf(element.getType());
        		if(nIndex!=-1){
        			dataTypeCombo.select(nIndex);
        		}else{
        			dataTypeCombo.add(element.getType());
        			dataTypeCombo.select(dataTypeCombo.indexOf(element.getType()));
        		}
        	}
        	dataPortDescriptionText.setText(element.getDescription());
        }
	}

	public void setDataTypeList(Iterator<String> it){
		if(it!=null && it.hasNext())
			dataTypeList.removeAll();
		while(it.hasNext()){
			dataTypeList.add(it.next());
		}
	}
	public Iterator<String> getDataTypeList(){
		int nCnt = dataTypeList.getItemCount();
		if(nCnt>0){
			String[] list = dataTypeList.getItems();
			ArrayList<String> ary = new ArrayList<String>();
			for(int i=0;i<nCnt;i++){
				ary.add(list[i]);
			}
			return ary.iterator();
		}
		return null;
	}
	public String getPortName(){
		return dataNameText.getText();
	}
	public String getPortType(){
		return dataTypeCombo.getText();
	}
	public String getPortPolicy(){
		return dataPolicyCombo.getText();
	}
	public String getPOrtQueueSize(){
		return portQueueingSizeText.getText();
	}
	public String getPortRefer(){
		return dataTypeReferenceText.getText();
	}
	public String getPortDescript(){
		return dataPortDescriptionText.getText();
	}
	public HashMap<String,Document> getDataTypeMap(){
		return map;
	}
}
