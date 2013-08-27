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

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

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

public class OPRoSNewDataTypeItemPopup extends Dialog {
	protected String name;
	protected String type;
	protected Text nameText;
	protected Combo typeCombo;
	
	public OPRoSNewDataTypeItemPopup(Shell parent) {
		super(parent);
	}
	protected Control createDialogArea(Composite parent){
		parent.getShell().setText(OPRoSStrings.getString("NewDataTypeItemDlgTitle"));
		Composite comp = OPRoSUtil.createComposite(parent, SWT.NONE, 2, SWT.NONE);
		OPRoSUtil.createLabel(comp, OPRoSStrings.getString("NewDataTypeItemDlgNameLabel"),	SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		nameText = OPRoSUtil.createText(comp, SWT.BORDER|SWT.SINGLE, 1, 0, 0, 0, 150, 0, GridData.BEGINNING);
		OPRoSUtil.createLabel(comp, OPRoSStrings.getString("NewDataTypeItemDlgTypeLabel"),	SWT.NONE, 1, 30, 0, 0, GridData.BEGINNING);
		typeCombo = OPRoSUtil.createCombo(comp, SWT.SIMPLE|SWT.SINGLE,OPRoSUtil.dataNotBoostTypes,0,1,0,0,0,150,0,GridData.BEGINNING);
//		typeCombo.addModifyListener(new ModifyListener(){
//
//			@Override
//			public void modifyText(ModifyEvent e) {
//				String strDataType = typeCombo.getText();
//				if(strDataType.compareTo(OPRoSStrings.getString("DataType7"))==0||
//  				   strDataType.compareTo(OPRoSStrings.getString("DataType8"))==0||
//  				   strDataType.compareTo(OPRoSStrings.getString("DataType9"))==0||
//  				   strDataType.compareTo(OPRoSStrings.getString("DataType10"))==0||
//  				   strDataType.compareTo(OPRoSStrings.getString("DataType11"))==0||
//  				   strDataType.compareTo(OPRoSStrings.getString("DataType12"))==0||
//  				   strDataType.compareTo(OPRoSStrings.getString("DataType13"))==0)
//				{
//					OPRoSValueTypeOfDataTypeDialog dlg=null;
//					dlg = new OPRoSValueTypeOfDataTypeDialog(getShell(),strDataType);
//					dlg.open();
//					if(dlg.getReturnCode()==Dialog.OK){
//						if(!dlg.getValueType().isEmpty()){
//							typeCombo.add(strDataType+OPRoSStrings.getString("DataTypeSymbol1")+dlg.getValueType()+OPRoSStrings.getString("DataTypeSymbol2"));
//							typeCombo.select(typeCombo.getItemCount()-1);
//						}
//					}
//				}
//			}
//        });
		return comp;
	}
	@Override
	protected void buttonPressed(int buttonId) {
		name = nameText.getText();
		type = typeCombo.getText();
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
}
