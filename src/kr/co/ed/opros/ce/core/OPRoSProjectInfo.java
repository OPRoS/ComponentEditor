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

package kr.co.ed.opros.ce.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Element;

public class OPRoSProjectInfo implements IProfilization{
	protected String prjName;
	protected String srcFolder;
	protected String binFolder;
	protected String implLanguage="MinGW C++";
	protected String location;
	protected ArrayList<String> componentList;

	public OPRoSProjectInfo() {
		componentList = new ArrayList<String>();
		clear();
	}
	
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getSrcFolder() {
		return srcFolder;
	}
	public void setSrcFolder(String srcFolder) {
		this.srcFolder = srcFolder;
	}
	public String getBinFolder() {
		return binFolder;
	}
	public void setBinFolder(String binFolder) {
		this.binFolder = binFolder;
	}
	public String getImplLanguage() {
		return implLanguage;
	}
	public void setImplLanguage(String implLanguage) {
		this.implLanguage = implLanguage;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void addComponent(String compName){
		Iterator<String> it = this.componentList.iterator();
		while(it.hasNext()){
			if(it.next().compareTo(compName)==0){
				return;
			}
		}
		this.componentList.add(compName);
	}
	public Iterator<String> getComponents(){
		return this.componentList.iterator();
	}
	public void removeComponent(String compName){
		this.componentList.remove(compName);
	}
	@Override
	public void profilize(Element parentEle) {
		try{
			Element prjNameEle = new Element("name");
			Element srcFolderEle = new Element("src");
			Element binFolderEle = new Element("bin");
			Element implLangEle = new Element("language");
			Element locationEle = new Element("location");
			Element componentsEle = new Element("components");
			prjNameEle.setText(prjName);
			srcFolderEle.setText(srcFolder);
			binFolderEle.setText(binFolder);
			implLangEle.setText(implLanguage);
			locationEle.setText(location);
			Element componentEle;
			Iterator<String> it = this.componentList.iterator();
			while(it.hasNext()){
				componentEle = new Element("component");
				componentEle.setText(it.next());
				componentsEle.addContent(componentEle);
			}
			parentEle.addContent(prjNameEle);
			parentEle.addContent(srcFolderEle);
			parentEle.addContent(binFolderEle);
			parentEle.addContent(implLangEle);
			parentEle.addContent(locationEle);
			parentEle.addContent(componentsEle);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void loadProfile(Element parentEle) {
		try{
			setPrjName(parentEle.getChildTextTrim("name"));
			setSrcFolder(parentEle.getChildTextTrim("src"));
			setBinFolder(parentEle.getChildTextTrim("bin"));
			setImplLanguage(parentEle.getChildTextTrim("language"));
			setLocation(parentEle.getChildTextTrim("location"));
			Element componentsEle = parentEle.getChild("components");
			if(componentsEle!=null){
				Iterator<Element> it = componentsEle.getChildren("component").iterator();
				if(it!=null){
					while(it.hasNext()){
						addComponent(it.next().getText());
					}
				}
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
	}
	public void clear(){
		prjName="";
		srcFolder="";
		binFolder="";
		implLanguage="MinGW C++";
		location="";
		componentList.clear();
	}
}
