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
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Document;
import org.jdom.Element;

public class OPRoSDataTypeInputDialogComposite extends Composite {
	
	protected Tree dataTypeTree;
	protected TreeItem rootItem;
	protected TreeItem selectedItem;
	protected MenuManager popupMenu;
	protected Text dataTypeFileNameText;
	protected Label messageLabel;
	protected boolean canModify=true;
	protected OPRoSComponentElementModel compEle=null;
	public OPRoSDataTypeInputWiget inputWiget;
	public Composite changeContainer;
	public boolean isFileNameDuplicate;
	private boolean isNewDataType;
	
	

	public OPRoSDataTypeInputDialogComposite(Composite parent, int style, int column, int gridStyle, 
			boolean canModify, OPRoSComponentElementModel compEle, boolean isNewDataType) {
		super(parent, style);
		this.canModify=canModify;
		this.isNewDataType = isNewDataType;
		this.compEle=compEle;
		this.setFont(parent.getFont());
        GridLayout layout = new GridLayout();
        layout.numColumns = column;
        this.setLayout(layout);
        this.setLayoutData(new GridData(gridStyle));
        popupMenu = new MenuManager();
        createDataTypeGroup(this);
        this.isFileNameDuplicate = false;
       
	}
	
	protected void createDataTypeGroup(Composite parent){
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		layout.numColumns = 2;
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("OPRoS Service Types");
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		label.setLayoutData(gd);
		
		Composite container1 = new Composite(parent, SWT.BORDER);
		container1.setBackground(new Color(null, 255, 255, 255));
		layout = new GridLayout();
		container1.setLayout(layout);
		layout.numColumns = 2;	
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container1);
		
		changeContainer = new Composite(parent, SWT.BORDER);
		layout = new GridLayout();
		changeContainer.setLayout(layout);
		layout.numColumns = 3;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(changeContainer);
		
		if(dataTypeTree==null)
			dataTypeTree = new Tree(container1,SWT.VIRTUAL|SWT.BORDER);
        gd= new GridData(SWT.SINGLE|SWT.BORDER);
        gd.widthHint=170;
        gd.heightHint=300;
        gd.horizontalSpan=2;
        dataTypeTree.setLayoutData(gd);
        
        rootItem = new TreeItem(dataTypeTree,SWT.NONE);
        rootItem.setText(OPRoSStrings.getString("DataTypeRoot"));
        selectedItem = rootItem;
        dataTypeTree.deselectAll();
        //dataTypeTree.setLinesVisible(true);
        rootItem.setExpanded(true);
        
        inputWiget = new OPRoSDataTypeInputWiget(this);
        changeContainer.layout(inputWiget.createServiceRootGroup(rootItem));
        
        if(canModify){
        	dataTypeTree.addMouseListener(new MouseListener(){
				@Override
				public void mouseDoubleClick(MouseEvent arg0) {}
				@Override 
				public void mouseDown(MouseEvent e) {
					if(e.button==3){
						selectedItem = ((Tree)e.getSource()).getItem(new Point(e.x,e.y));
						dataTypeTree.setMenu(getMenu(selectedItem));
					}
				}
				@Override
				public void mouseUp(MouseEvent arg0) {}
        		
        	});
        	
        	dataTypeTree.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {				
					if(((Tree)e.getSource()).getSelection() != null && (((Tree)e.getSource()).getSelection()).length >0) {
						TreeItem item = ((Tree)e.getSource()).getSelection()[0];
						changeRightComposite(item);
					}		
					
				}
        		
			});
        }
        
        Composite container3 = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		layout = new GridLayout();
		container3.setLayout(layout);
		container3.setLayoutData(gd);
		layout.numColumns = 2;
        
        OPRoSUtil.createLabel(container3, OPRoSStrings.getString("FileNameLabel"),SWT.NONE,1,0,1,0,GridData.BEGINNING);
        dataTypeFileNameText=new Text(container3, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        dataTypeFileNameText.setLayoutData(gd);
        
        if(!canModify || !isNewDataType)
        	dataTypeFileNameText.setEnabled(false);
        else{
        	dataTypeFileNameText.addModifyListener(new ModifyListener(){
				@Override
				public void modifyText(ModifyEvent evt) {
					String dataTypeName = dataTypeFileNameText.getText();
					if(!dataTypeName.endsWith(OPRoSStrings.getString("DataTypeExtension")))
						dataTypeName = dataTypeName+OPRoSStrings.getString("DataTypeExtension");
					if(OPRoSUtil.isDuplicateDataTypeName(dataTypeName, false, compEle)){
						messageLabel.setText(OPRoSStrings.getString("DataTypeFileNameDuplicationError"));
						isFileNameDuplicate = true;
					}else{
						messageLabel.setText("");
						isFileNameDuplicate = false;
					}			
				}
        	});
        }
        messageLabel =  OPRoSUtil.createLabel(container1, "                                        ",
        		SWT.NONE,2,0,1,0,GridData.BEGINNING);
        messageLabel.setBackground(ColorConstants.white);
        messageLabel.setForeground(ColorConstants.red);
	}
	
	public void changeRightComposite(TreeItem item) {
		//占쏙옙占쏙옙 占쏙옙占쏙옙
		for(Control control : changeContainer.getChildren()) {
			control.dispose();
		}
		if(item.getParentItem()!=null){
			if(item.getParentItem().getParentItem()==null){
				changeContainer.layout(inputWiget.createMethodRootGroup(item));
			}else if(item.getParentItem().getParentItem().getParentItem()==null){
				changeContainer.layout(inputWiget.createParameterGroup(item));
			}
		}
		else {
			changeContainer.layout(inputWiget.createServiceRootGroup(item));
		}
	}
	
	public TreeItem getRootItem() {
		return rootItem;
	}
	
	public void setRootItem(TreeItem rootItem) {
		this.rootItem = rootItem;
	}
	
	private class NewDataTypeAction extends Action{
		public NewDataTypeAction(){
			super(OPRoSStrings.getString("NewDataTypeAction"));
		}
		public void run(){
			if(selectedItem!=null){
				if(selectedItem.equals(rootItem)){
					/*
					InputDialog dlg = new InputDialog(getShell(),
							OPRoSStrings.getString("NewDataTypeDlgTitle"),
							OPRoSStrings.getString("NewDataTypeDlgLabel"),
							OPRoSStrings.getString("NewDataTypeDlgDefaultValue"),null);
					dlg.open();
					if(!dlg.getValue().isEmpty()&&dlg.getReturnCode()==InputDialog.OK){
						if(!isExistItem(rootItem,dlg.getValue())){
							TreeItem item = new TreeItem(rootItem,SWT.NONE);
							item.setText(dlg.getValue());
							rootItem.setExpanded(true);
						}
						else{
							OPRoSUtil.openMessageBox(OPRoSStrings.getString("DuplicationErrorMessage"), SWT.OK|SWT.ICON_WARNING);
						}
					}
					*/
					inputWiget.disposeWiget(changeContainer);
					changeContainer.layout(inputWiget.createServiceCreateGroup(selectedItem, true));
				}
			}
		}
	}
	private class NewDataTypeItemAction extends Action{
		public NewDataTypeItemAction(){
			super(OPRoSStrings.getString("NewDataTypeItemAction"));
		}
		public void run(){
			if(selectedItem!=null){
				if(!selectedItem.equals(rootItem)){
					/*
					OPRoSNewDataTypeItemPopup dlg=null;
					dlg = new OPRoSNewDataTypeItemPopup(getShell());
					dlg.open();
					if(dlg.getReturnCode()==InputDialog.OK){
						if(!dlg.getName().isEmpty()&&!dlg.getType().isEmpty()){
							if(!isExistItem(selectedItem,dlg.getName())){
								TreeItem item = new TreeItem(selectedItem,SWT.NONE);
								item.setText(dlg.getName()+OPRoSStrings.getString("DataTypeSubEleSeperator")+dlg.getType());
								selectedItem.setExpanded(true);
							}
							else{
								OPRoSUtil.openMessageBox(OPRoSStrings.getString("DuplicationErrorMessage"), SWT.OK|SWT.ICON_WARNING);
							}
						}
					}
					*/
					inputWiget.disposeWiget(changeContainer);
					changeContainer.layout(inputWiget.createParameterCreateGroup(selectedItem, true));
				}
			}
		}
	}
	private class NewDataTypeDeleteItemAction extends Action{
		public NewDataTypeDeleteItemAction(){
			super(OPRoSStrings.getString("DelDataTypeItemAction"));
		}
		public void run(){
			if(selectedItem!=null){
				if(!selectedItem.equals(rootItem)){
					selectedItem.removeAll();
					selectedItem.dispose();
				}
				else{
					selectedItem.removeAll();
				}
			}
		}
	}
	public Tree getDataTypeTree() {
		return dataTypeTree;
	}
	public void setDataTypeTree(Tree tree){
		dataTypeTree=tree;
		this.redraw();
	}
	private boolean isExistItem(TreeItem parent, String itemName){
		int cnt = parent.getItemCount();
		for(int i=0;i<cnt;i++){
			if(parent.getItem(i).getText().compareTo(itemName)==0)
				return true;
		}
		return false;
	}
	public void setDataTypeFileName(String str){
		dataTypeFileNameText.setText(str);
	}
	public String getDataTypeFileName(){
		return dataTypeFileNameText.getText();
	}
	public void setEditableDataTypeFileNameText(boolean bEnable){
		dataTypeFileNameText.setEditable(bEnable);
	}
	
	/**
	 * 占쏜델울옙 占쏙옙溝퓸占쏙옙獵占�占쏙옙占쏙옙占쏙옙 타占쏙옙占쏙옙 占쏙옙占싼댐옙.
	 * @return	
	 */
	public List<String> getTypeItem(boolean isVoid) {
		List<String> list = new ArrayList<String>();
		if(isVoid)
			list.add("void");
		
		if(compEle != null){
			OPRoSComponentElementModel model = getComponentElementModel(compEle);
			OPRoSElementBaseModel dataType = model.getDataTypesModel();
			List<OPRoSElementBaseModel> dataTypes = dataType.getChildrenList();
			if(dataTypes == null || dataTypes.size() ==0 )
				return list;
			
			Iterator<OPRoSElementBaseModel> it = dataTypes.iterator();
			while(it.hasNext()){
				OPRoSDataTypeElementModel dataModel=(OPRoSDataTypeElementModel)it.next();
				Iterator<Element> eles;
				Element ele;
				Document doc = dataModel.getDataTypeDoc();
				eles = doc.getRootElement().getChildren(OPRoSStrings.getString("DataTypeEle")).iterator();
				while(eles.hasNext()){
					ele=eles.next();
					list.add(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")));
				}
			}
		}
		return list;
	}
	
	/**
	 * 占쌔댐옙 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙트 占쏙옙占쏙옙 占쏙옙占싼댐옙.
	 * @param model
	 * @return
	 */
	public OPRoSComponentElementModel getComponentElementModel(OPRoSElementBaseModel model) {
		if(model instanceof OPRoSComponentElementModel)
			return (OPRoSComponentElementModel)model;
		
		if(model.getParent() != null) {			
			return getComponentElementModel(model.getParent());
		}		
		return null;
	}
	
	private Menu getMenu(TreeItem item){
		popupMenu.removeAll();
		if(item.getParentItem()!=null){
			if(item.getParentItem().getParentItem()!=null){
				popupMenu.add(new NewDataTypeDeleteItemAction());
			}else{
				popupMenu.add(new NewDataTypeItemAction());
				popupMenu.add(new NewDataTypeDeleteItemAction());
			}
		}else{
			popupMenu.add(new NewDataTypeAction());
			popupMenu.add(new NewDataTypeDeleteItemAction());
		}
		return popupMenu.createContextMenu(dataTypeTree);
	}
	
	public Composite getChangeContainer() {
		return changeContainer;
	}
	
	/**
	 * 占쏙옙占쏙옙占싱몌옙占쏙옙 占쌩븝옙占쏙옙占쏙옙占쏙옙 占싯삼옙
	 * @return
	 */
	public boolean isFileNameDuplicate() {
		return isFileNameDuplicate;
	}
}
