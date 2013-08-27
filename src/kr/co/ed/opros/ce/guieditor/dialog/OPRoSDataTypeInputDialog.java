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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class OPRoSDataTypeInputDialog extends Dialog {
	protected OPRoSDataTypeInputDialogComposite dataTypeComp;
	private String dataTypeFileName;
	private Document dataTypeDoc;
	private boolean canModify=true;
	private OPRoSComponentElementModel compEle;
	private boolean isNewDataType = false;

	public OPRoSDataTypeInputDialog(Shell parent, OPRoSComponentElementModel compEle) {
		super(parent);
		dataTypeFileName="";
		dataTypeDoc=null;
		this.compEle=compEle;
		this.isNewDataType = true;
	}
	
	public OPRoSDataTypeInputDialog(Shell parent, OPRoSDataTypeElementModel part, OPRoSComponentElementModel compEle) {
		super(parent);
		dataTypeFileName = part.getDataTypeFileName();
		dataTypeDoc = part.getDataTypeDoc();
		this.compEle=compEle;
//		this.index=index;
	}
	
	public OPRoSDataTypeInputDialog(Shell parent, String fileName, Document doc, boolean canModify, OPRoSComponentElementModel compEle){
		super(parent);
		dataTypeFileName=fileName;
		dataTypeDoc=doc;
		this.canModify=canModify;
		this.compEle=compEle;
	}
	
	protected Control createDialogArea(Composite parent){
		this.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX
				| SWT.APPLICATION_MODAL | SWT.TITLE);

		this.getShell().setText(OPRoSStrings.getString("DataTypeInputDlgTitle"));
		dataTypeComp = new OPRoSDataTypeInputDialogComposite(parent,SWT.NULL, 1, GridData.FILL_BOTH, canModify, compEle, isNewDataType);
		if(dataTypeFileName!=null){
			if(!dataTypeFileName.isEmpty()){
				dataTypeComp.setDataTypeFileName(dataTypeFileName);
				dataTypeComp.setEditableDataTypeFileNameText(true);
			}
		}
		if(dataTypeDoc!=null){
			createTree(dataTypeDoc.getRootElement());
		}
		return dataTypeComp;
	}
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button importBtn = createButton(parent, 3, OPRoSStrings.getString("DataTypeInputDlgBtn"), false);
		importBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(),SWT.OPEN);
				dialog.setFilterExtensions(new String[]{OPRoSStrings.getString("DataTypeInputDlgFileType")});
				IWorkspaceRoot rootWorkspace = ResourcesPlugin.getWorkspace().getRoot();//워크스페이스 핸들
				dialog.setFilterPath(rootWorkspace.getLocation().toString());
				String filePath = dialog.open();
				if(filePath != null && !filePath.isEmpty()){
					SAXBuilder builder = new SAXBuilder();
					Document doc = null;
					try {
						doc = builder.build( new FileInputStream(filePath));
					} catch (JDOMException e2) {
						e2.printStackTrace();
					} catch (IOException e3) {
						e3.printStackTrace();
					}
					Element root;
					root = doc.getRootElement();
					createTree(root);
					dataTypeComp.setDataTypeFileName(dialog.getFileName());
				}
			}
		});
		super.createButtonsForButtonBar(parent);
	}
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId==Dialog.OK){
			if(dataTypeComp.getDataTypeFileName().isEmpty()){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("DataTypeFileNameErrorMessage"),SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
			Tree tree = dataTypeComp.getDataTypeTree();
			
			Element dataTypeRoot = new Element(OPRoSStrings.getString("DataTypeRoot"));
			TreeItem root = tree.getItem(0);
			int cnt = root.getItemCount();
			int itemCnt=0;
			Element ele;
			Element subEle;
			Attribute attr;
			String itemStr;
			String name;
			String type;
			for(int i =0;i<cnt;i++){
				itemCnt = root.getItem(i).getItemCount();
				ele = new Element(OPRoSStrings.getString("DataTypeEle"));
				attr = new Attribute(OPRoSStrings.getString("DataTypeEleAttr"),root.getItem(i).getText());
				ele.setAttribute(attr);
				for(int j=0;j<itemCnt;j++){
					subEle = new Element(OPRoSStrings.getString("DataTypeSubEle"));
					itemStr = root.getItem(i).getItem(j).getText();
					int seper = itemStr.indexOf(OPRoSStrings.getString("DataTypeSubEleSeperator"));
					name = itemStr.substring(0, seper);
					type = itemStr.substring(seper+1, itemStr.length());
					attr = new Attribute(OPRoSStrings.getString("DataTypeSubEleAttr"),type);
					subEle.setAttribute(attr);
					subEle.setText(name);
					ele.addContent(subEle);
				}
				dataTypeRoot.addContent(ele);
			}
			if(!dataTypeComp.isFileNameDuplicate()) {				
				dataTypeDoc = new Document(dataTypeRoot);
				if(!dataTypeComp.getDataTypeFileName().endsWith(OPRoSStrings.getString("DataTypeExtension")))
					dataTypeComp.setDataTypeFileName(dataTypeComp.getDataTypeFileName()+OPRoSStrings.getString("DataTypeExtension"));
				dataTypeFileName = dataTypeComp.getDataTypeFileName();
				
				super.buttonPressed(buttonId);
			} else {
				OPRoSUtil.openMessageBox("DataType FileName is Duplicated.",SWT.ERROR|SWT.ICON_ERROR);
			}
			
		}
		else if(buttonId==Dialog.CANCEL){
			super.buttonPressed(buttonId);
		}	
	}
	@SuppressWarnings("unchecked")
	private void createTree(Element root){
		Iterator<Element> items = root.getChildren().iterator();
		Tree tree = dataTypeComp.getDataTypeTree();
		tree.removeAll();
		TreeItem rootItem= new TreeItem(tree,SWT.NONE);
		rootItem.setText(OPRoSStrings.getString("DataTypeRoot"));
		dataTypeComp.setRootItem(rootItem);
		TreeItem item;
		TreeItem subItem;
		Element ele;
		Iterator<Element> subItems;
		while(items.hasNext()){
			ele = items.next();
			item = new TreeItem(rootItem,SWT.NONE);
			item.setText(ele.getAttributeValue(OPRoSStrings.getString("DataTypeEleAttr")));
			subItems = ele.getChildren().iterator();
			while(subItems.hasNext()){
				ele = subItems.next();
				subItem = new TreeItem(item,SWT.NONE);
				subItem.setText(ele.getText()+OPRoSStrings.getString("DataTypeSubEleSeperator")+ele.getAttributeValue(OPRoSStrings.getString("DataTypeSubEleAttr")));
			}
		}
	}
	public String getDataTypeFileName() {
		return dataTypeFileName;
	}
	public void setDataTypeFileName(String dataTypeFileName) {
		this.dataTypeFileName = dataTypeFileName;
	}
	public Document getDataTypeDoc() {
		return dataTypeDoc;
	}
	public void setDataTypeDoc(Document dataTypeDoc) {
		this.dataTypeDoc = dataTypeDoc;
	}
	
}
