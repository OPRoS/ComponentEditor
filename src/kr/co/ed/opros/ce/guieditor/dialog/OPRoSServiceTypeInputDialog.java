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
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

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

public class OPRoSServiceTypeInputDialog extends Dialog {
	
	protected OPRoSServiceTypeInputDialogComposite serviceTypeComp;
	private String serviceTypeFileName;
	private Document serviceTypeDoc;
	protected OPRoSElementBaseModel eleModel;
	private boolean canModify=true;
	private boolean isNewServiceType = false;
	
	public OPRoSServiceTypeInputDialog(Shell parent, OPRoSElementBaseModel compEle) {
		super(parent);
		eleModel=compEle;
		isNewServiceType = true;
	}
	
	public OPRoSServiceTypeInputDialog(Shell parent, OPRoSServiceTypeElementModel model,
			OPRoSComponentElementModel compEle) {
		super(parent);
		if(model!=null){
			serviceTypeFileName = model.getServiceTypeFileName();
			serviceTypeDoc = model.getServiceTypeDoc();
			eleModel = compEle;
		}
	}
	
	public OPRoSServiceTypeInputDialog(Shell parent, String fileName, Document doc, boolean canModify){
		super(parent);
		serviceTypeFileName=fileName;
		serviceTypeDoc=doc;
		this.canModify=canModify;
	}

	protected Control createDialogArea(Composite parent){
		this.setShellStyle(SWT.TITLE);
		this.getShell().setText(OPRoSStrings.getString("ServiceTypeInputDlgTitle"));
		serviceTypeComp = new OPRoSServiceTypeInputDialogComposite(parent,SWT.NULL, 1, GridData.FILL_BOTH, 
				canModify, eleModel, isNewServiceType);
		if(serviceTypeFileName!=null){
			if(!serviceTypeFileName.isEmpty()){
				serviceTypeComp.setServiceTypeFileName(serviceTypeFileName);
				serviceTypeComp.setEditableServiceTypeFileNameText(true);
			}
		}
		if(serviceTypeDoc!=null){
			createTree(serviceTypeDoc.getRootElement());
		}
		return serviceTypeComp;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button importBtn = createButton(parent, 3, 
				OPRoSStrings.getString("ServiceTypeInputDlgBtn"),false);
		importBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(),SWT.OPEN);
				dialog.setFilterExtensions(new String[]{OPRoSStrings.getString("ServiceTypeInputDlgFileType")});
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
					serviceTypeComp.setServiceTypeFileName(dialog.getFileName());
				}
			}
		});
		super.createButtonsForButtonBar(parent);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId==Dialog.OK){
			if(serviceTypeComp.getServiceTypeFileName().isEmpty()){
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("ServiceTypeFileNameErrorMessage")
						,SWT.ERROR|SWT.ICON_ERROR);
				return;
			}
			Tree tree = serviceTypeComp.getServiceTypeTree();
			
			Element serviceTypeRoot = new Element(OPRoSStrings.getString("ServiceTypeRoot"));
			TreeItem root = tree.getItem(0);
			int typeCnt = root.getItemCount();
			int serviceCnt=0;
			int paramCnt=0;
			Element ele;
			Element subEle;
			Element subEle1;
			Element subEle2;
			Attribute attr;
			String itemStr;
			int seper=0;
			int seper2=0;
			for(int i =0;i<typeCnt;i++){
				ele = new Element(OPRoSStrings.getString("ServiceTypeEle"));
	
				itemStr = root.getItem(i).getText();
				seper = root.getItem(i).getText().indexOf(":");
	
				subEle = new Element(OPRoSStrings.getString("ServiceTypeNameEle"));
				subEle.setText(itemStr);
				ele.addContent(subEle);
	
				serviceCnt = root.getItem(i).getItemCount();
				for(int j=0;j<serviceCnt;j++){
					subEle = new Element(OPRoSStrings.getString("ServiceEle"));
					itemStr = root.getItem(i).getItem(j).getText();
					seper = itemStr.indexOf(OPRoSStrings.getString("ServiceSeper1"));
					seper2 = itemStr.lastIndexOf(OPRoSStrings.getString("ServiceSeper2"));
					attr = new Attribute(OPRoSStrings.getString("ServiceAttr1"),itemStr.substring(0, seper));
					subEle.setAttribute(attr);
					attr = new Attribute(OPRoSStrings.getString("ServiceAttr2"),itemStr.substring(seper+3, seper2));
					subEle.setAttribute(attr);
					attr = new Attribute(OPRoSStrings.getString("ServiceAttr3"),itemStr.substring(seper2+1, itemStr.length()));
					subEle.setAttribute(attr);
					ele.addContent(subEle);
				
					paramCnt = root.getItem(i).getItem(j).getItemCount();
					for(int k=0;k<paramCnt;k++){
						subEle1 = new Element(OPRoSStrings.getString("ServiceParamEle"));
						itemStr = root.getItem(i).getItem(j).getItem(k).getText();
						seper = itemStr.indexOf(OPRoSStrings.getString("ServiceSeper2"));
						attr = new Attribute(OPRoSStrings.getString("ServiceParamAttr"),Integer.toString(k+1));
						subEle1.setAttribute(attr);
						subEle2 = new Element(OPRoSStrings.getString("ServiceParamNameEle"));
						subEle2.setText(itemStr.substring(0,seper));
						subEle1.addContent(subEle2);
						subEle2 = new Element(OPRoSStrings.getString("ServiceParamTypeEle"));
						subEle2.setText(itemStr.substring(seper+1,itemStr.length()));
						subEle1.addContent(subEle2);
						
						subEle.addContent(subEle1);
					}
				}
				serviceTypeRoot.addContent(ele);
			}
			serviceTypeDoc = new Document(serviceTypeRoot);
			
			if(!serviceTypeComp.isFileNameDuplicate()) {				
				if(!serviceTypeComp.getServiceTypeFileName().endsWith(".xml"))
					serviceTypeComp.setServiceTypeFileName(serviceTypeComp.getServiceTypeFileName()+".xml");
				serviceTypeFileName=serviceTypeComp.getServiceTypeFileName();				
				super.buttonPressed(buttonId);
			} else {
				OPRoSUtil.openMessageBox(OPRoSStrings.getString("ServiceTypeFileNameDuplicationError"), SWT.ERROR|SWT.ICON_ERROR);
			}
			
		}
		else if(buttonId==Dialog.CANCEL){
			super.buttonPressed(buttonId);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void createTree(Element root){
		Iterator<Element> items = root.getChildren().iterator();
		Tree tree = serviceTypeComp.getServiceTypeTree();
		tree.removeAll();
		TreeItem rootItem= new TreeItem(tree,SWT.NONE);
		rootItem.setText(OPRoSStrings.getString("ServiceTypeRoot"));
		serviceTypeComp.setRootItem(rootItem);
		TreeItem typeItem;
		TreeItem serviceItem;
		TreeItem paramItem;
		Element ele;
		Iterator<Element> subItems;
		Iterator<Element> subItems1;
		String text="";
		while(items.hasNext()){
			ele = items.next();
			typeItem = new TreeItem(rootItem,SWT.NONE);
			subItems = ele.getChildren().iterator();
			while(subItems.hasNext()){
				ele = subItems.next();
				if(ele.getName().compareTo(OPRoSStrings.getString("ServiceTypeNameEle"))==0){
					text += ele.getText();
					typeItem.setText(text);
					text="";
				}
				else{
					subItems1= ele.getChildren().iterator();
					serviceItem = new TreeItem(typeItem,SWT.NONE);
					serviceItem.setText(ele.getAttributeValue(OPRoSStrings.getString("ServiceAttr1"))
							+OPRoSStrings.getString("ServiceSeper3")+
							ele.getAttributeValue(OPRoSStrings.getString("ServiceAttr2"))+
							OPRoSStrings.getString("ServiceSeper2")+
							ele.getAttributeValue(OPRoSStrings.getString("ServiceAttr3")));
					while(subItems1.hasNext()){
						ele = subItems1.next();
						paramItem = new TreeItem(serviceItem,SWT.NONE);
						paramItem.setText(ele.getChildText(OPRoSStrings.getString("ServiceParamNameEle"))
								+OPRoSStrings.getString("ServiceSeper2")
								+ele.getChildText(OPRoSStrings.getString("ServiceParamTypeEle")));
					}
				}
				
			}
		}
	}
	
	public String getServiceTypeFileName() {
		return serviceTypeFileName;
	}
	public void setServiceTypeFileName(String serviceTypeFileName) {
		this.serviceTypeFileName = serviceTypeFileName;
	}
	public Document getServiceTypeDoc() {
		return serviceTypeDoc;
	}
	public void setServiceTypeDoc(Document serviceTypeDoc) {
		this.serviceTypeDoc = serviceTypeDoc;
	}
	
}
