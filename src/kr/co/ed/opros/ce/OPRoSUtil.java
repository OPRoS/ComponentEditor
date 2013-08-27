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

package kr.co.ed.opros.ce;

//import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import kr.co.ed.opros.ce.core.OPRoSProjectInfo;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariablesModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;
import kr.co.ed.opros.ce.preferences.PreferenceConstants;
import kr.co.ed.opros.ce.wizards.WizardMessages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
//import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * �좎룞���닷뜝�숈삕�좎룞�쇿뜝�숈삕 OPRoS CE�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�ㅻ뙋���좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�ㅻ뙋��
 * �좎뙣�뚮뱶瑜��좎룞�숉떚�좑옙�좎룞�숉떥�좎룞�숉떚�좎룞���좎룞�쇿뜝�숈삕�좎떦�먯삕 �닷뜝�숈삕�좎룞�쇿뜝�깅뙋��
 * �좎룞���닷뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕�좎떬�먯삕.
 * <ul>
 * <li>�좎뙣�숈삕�좎룞���좎뙓�숈삕 �좎룞�쇿뜝占� * <li>�좎룞���좎룞�숉듉/�좎뙏�숈삕�멨뜝�섏룞���좎뙣釉앹삕/�좎뙎琉꾩삕/�좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕��UI �좎룞�쇿뜝占썲뜝�숈삕�좎룞�쇿뜝占� * <li>�좎룞�쇿뜝�숈삕�좎떦琉꾩삕(OPRoS�좎룞�쇿뜝�숈삕�좎룞�숉듃 ��뜝�숈삕)�좎룞���좎룞��MakeFile/VS2008 �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좑옙
 * <li>OPRoS Component Library �좎룞��Include �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�곕━ �좎룞�숈튂 李얍뜝�ｌ슱�쇿뜝�숈삕
 * </ul>
 *  
 * @author jwkim 
 * @author jwkim@ed.co.kr
 * @author sevensky@mju.ac.kr
 * @since OPRoS_CE 1.0.0
 */
public class OPRoSUtil {
	/**
	 * OPRoS_CE�좎룞���좎룞�쇿뜝�됰벝��OPRoS Component Library �좎룞��Include �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�곕━�좎룞���좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�섏슱��
	 */
	private static String OPRoSFilesPath=null;
	public static final String[] dataTypes = {
		OPRoSStrings.getString("DataType0"),
		OPRoSStrings.getString("DataType1"),
		OPRoSStrings.getString("DataType2"),
		OPRoSStrings.getString("DataType3"),
		OPRoSStrings.getString("DataType4"),
		OPRoSStrings.getString("DataType5"),
		OPRoSStrings.getString("DataType6"),
		OPRoSStrings.getString("DataType7"),
		OPRoSStrings.getString("DataType8"),
		OPRoSStrings.getString("DataType9"),
		OPRoSStrings.getString("DataType10"),
		OPRoSStrings.getString("DataType11"),
		OPRoSStrings.getString("DataType12"),
		OPRoSStrings.getString("DataType13"),
		OPRoSStrings.getString("DataType14"),
		OPRoSStrings.getString("DataType15"),
		OPRoSStrings.getString("DataType16"),
		OPRoSStrings.getString("DataType17"),
		OPRoSStrings.getString("DataType18"),
		OPRoSStrings.getString("DataType19")};
	public static final String[] dataNotBoostTypes = {
		OPRoSStrings.getString("DataType0"),
		OPRoSStrings.getString("DataType1"),
		OPRoSStrings.getString("DataType2"),
		OPRoSStrings.getString("DataType3"),
		OPRoSStrings.getString("DataType4"),
		OPRoSStrings.getString("DataType5"),
		OPRoSStrings.getString("DataType6")};
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎뙣�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕黎귛뜝占썲뜝�⑥룞�쇿뜝�숈삕 �좎뙓�숈삕�좎룞���멨뜝�숈삕�좎떬�먯삕.
	 * @param msg		�좎룞�숈쮬�좑옙�좎뙣�숈삕�좎룞��
	 * @param style		�좎뙣�숈삕�좎룞���좎뙓�숈삕 �좎룞�숉��좎룞��SWT Style �좎룞�쇿뜝占�
	 * @see	org.eclipse.swt.widgets.MessageBox
	 * @see org.eclipse.swt.widgets.Display
	 * @see org.eclipse.swt.widgets.Display#getDefault()
	 * @see org.eclipse.swt.widgets.Display#getActiveShell()
	 * @see org.eclipse.swt.SWT
	 */
	public static int openMessageBox(String msg, int style){
		MessageBox msgDlg = new MessageBox(Display.getDefault().getActiveShell(),style);
		msgDlg.setText("OPRoS Component Editor");
		msgDlg.setMessage( msg);
		return msgDlg.open();
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎룞�쇿뜝�숈삕�좎룞��SWT Widget �좎룞�쇿뜝�숈삕�멨뜝�숈삕 �좎룞�쇿뜝�쇰뙋��
	 * @param container		�좎룞�쇿뜝�숈삕�멨뜝�숈삕 �좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떛�귥삕
	 * @param style			�좎룞�쇿뜝�숈삕�멨뜝�숈삕 �좎룞�숉��좎룞��SWT Style Constants)
	 * @param hSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param hIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕 
	 * @param vSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param vIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕
	 * @param width			�좎룞�쇿뜝�숈삕
	 * @param height		�좎룞�쇿뜝�숈삕
	 * @param gridStyle		�좎룞�쇿뜝�숈삕�멨뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞��GridData�좎룞��gridStyle
	 * @return				�좎룞�쇿뜝占폮WT Widget �좎룞�쇿뜝�숈삕��
	 * @see org.eclipse.swt.widgets.List
	 * @see org.eclipse.swt.widgets.Composite
	 * @see org.eclipse.swt.Layout.GridData
	 * @see org.eclipse.swt.SWT
	 */
	public static List createList(Composite container, int style, int hSpan, int hIndent, int vSpan, int vIndent, int width, int height, int gridStyle){
		List list = new List(container, style);		 
		GridData gd= new GridData(SWT.FILL, SWT.FILL, true, true, hSpan, vSpan);
		gd.heightHint = height;
		list.setLayoutData(gd);
		return list;
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎룞�쇿뜝�숈삕�좎룞��SWT Widget �좎룞�숉듉�좎룞���좎룞�쇿뜝�쇰뙋��
	 * @param container		�좎룞�숉듉�좎룞���좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떛�귥삕
	 * @param style			�좎룞�숉듉�좎룞���좎룞�숉��좎룞��SWT Style Constants)
	 * @param text			�좎룞�숉듉�좎룞���쒎뜝�쒕릺�먯삕 �좎떛紐뚯삕
	 * @param hSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param hIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕 
	 * @param vSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param vIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕
	 * @param width			�좎룞�쇿뜝�숈삕
	 * @param height		�좎룞�쇿뜝�숈삕
	 * @param gridStyle		�좎룞�숉듉�좎룞���좎룞�쇿뜝�숈삕�좎룞��GridData�좎룞��gridStyle
	 * @return				�좎룞�쇿뜝占폮WT Widget �좎룞�숉듉
	 * @see org.eclipse.swt.widgets.Button
	 * @see org.eclipse.swt.widgets.Composite
	 * @see org.eclipse.swt.Layout.GridData
	 * @see org.eclipse.swt.SWT
	 */
	public static Button createButton(Composite container, int style, String text,int hSpan, int hIndent, int vSpan, int vIndent,int width, int height, int gridStyle) {
		Button button = new Button(container, style);
		GridData gd = new GridData(gridStyle);
		gd.horizontalSpan = hSpan;
		gd.horizontalIndent = hIndent;
		gd.verticalIndent = vIndent;
		gd.verticalSpan = vSpan;
		gd.widthHint=width;
		gd.heightHint = height;
		button.setLayoutData(gd);
		button.setText(text);
		return button;		
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎룞�쇿뜝�숈삕�좎룞��SWT Widget �좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�쇰뙋��
	 * @param container		�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떛�귥삕
	 * @param text			�좎룫踰⑥슱���쒎뜝�쒕릺�먯삕 �좎떛紐뚯삕
	 * @param style			�좎룞�쇿뜝�숈삕 �좎룞�숉��좎룞��SWT Style Constants)
	 * @param hSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param hIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕 
	 * @param vSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param vIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕
	 * @param gridStyle		�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞��GridData�좎룞��gridStyle
	 * @return				�좎룞�쇿뜝占폮WT Widget �좎룞��
	 * @see org.eclipse.swt.widgets.Label
	 * @see org.eclipse.swt.widgets.Composite
	 * @see org.eclipse.swt.Layout.GridData
	 * @see org.eclipse.swt.SWT
	 */
	public static Label createLabel(Composite container, String text,int style, int hSpan, int hIndent, int vSpan, int vIndent, int gridStyle) {
		Label label = new Label(container, style);
		label.setText(text);
		GridData gd = new GridData(gridStyle);
		gd.horizontalSpan = hSpan;
		gd.horizontalIndent = hIndent;
		gd.verticalIndent = vIndent;
		gd.verticalSpan = vSpan;
		label.setLayoutData(gd);
		return label;
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎룞�쇿뜝�숈삕�좎룞��SWT Widget �좎뙏�숈삕�멨뜝�섏룞�쇿뜝�숈삕 �좎룞�쇿뜝�쇰뙋��
	 * @param container		�좎뙏�숈삕�멨뜝�섏룞�쇿뜝�숈삕 �좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떛�귥삕
	 * @param style			�좎뙏�숈삕�멨뜝�섏룞�쇿뜝�숈삕 �좎룞�숉��좎룞��SWT Style Constants)
	 * @param hSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param hIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕
	 * @param vSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param vIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕
	 * @param width			�좎룞�쇿뜝�숈삕
	 * @param height		�좎룞�쇿뜝�숈삕
	 * @param gridStyle		�좎뙏�숈삕�멨뜝�섏룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞��GridData�좎룞��gridStyle
	 * @return				�좎룞�쇿뜝占폮WT Widget �좎뙏�숈삕�멨뜝�섏룞��
	 * @see org.eclipse.swt.widgets.Text
	 * @see org.eclipse.swt.widgets.Composite
	 * @see org.eclipse.swt.Layout.GridData
	 * @see org.eclipse.swt.SWT
	 */
	public static Text createText(Composite container, int style, int hSpan, int hIndent, int vSpan, int vIndent, int width,int height, int gridStyle) {
		Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, hSpan, vSpan);
		if(width>0)
			gd.widthHint=width;
		if(height>0)
			gd.heightHint=height;
		text.setLayoutData(gd);
		return text;
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎룞�쇿뜝�숈삕�좎룞��SWT Widget �좎뙣釉앹삕�좎뙓�숈삕�좎룞���좎룞�쇿뜝�쇰뙋��
	 * @param container		�좎뙣釉앹삕�좎뙓�숈삕�좎룞���좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떛�귥삕
	 * @param style			�좎뙣釉앹삕�좎뙓�숈삕�좎룞���좎룞�숉��좎룞��SWT Style Constants)
	 * @param items			�좎뙣釉앹삕�좎뙓�숈삕�좎룞���쒎뜝�쒕릺�먯삕 �좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕��
	 * @param initIndex		�좎떗源띿삕�쒎뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕�멨뜝�숈삕 �좎떥�몄삕�좎룞��
	 * @param hSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param hIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕
	 * @param vSpan			�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎��뱀쁺�좎룞���좎룞�쇿뜝�숈삕
	 * @param vIndent		�좎룞�쇿뜝�몃콈�쇿뜝�숈삕�좎룞���좎룞�숁씡�먯삕�좑옙�у뜝�숈삕
	 * @param width			�좎룞�쇿뜝�숈삕
	 * @param height		�좎룞�쇿뜝�숈삕
	 * @param gridStyle		�좎뙣釉앹삕�좎뙓�숈삕�좎룞���좎룞�쇿뜝�숈삕�좎룞��GridData�좎룞��gridStyle
	 * @return				�좎룞�쇿뜝占폮WT Widget �좎뙣釉앹삕�좎뙓�숈삕
	 * @see org.eclipse.swt.widgets.Combo
	 * @see org.eclipse.swt.widgets.Composite
	 * @see org.eclipse.swt.Layout.GridData
	 * @see org.eclipse.swt.SWT
	 */
	public static Combo createCombo(Composite container, int style, String[] items, int initIndex, int hSpan, int hIndent, int vSpan, int vIndent, int width, int height, int gridStyle){
		Combo combo = new Combo(container, style);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, hSpan, vSpan);
		if(width>0)
			gd.widthHint=width;
		if(height>0)
			gd.heightHint=height;
		combo.setLayoutData(gd);
		if(items!=null)
			combo.setItems(items);
		if(initIndex!=-1){
			combo.setText(items[initIndex]);
		}
		combo.select(0);
		return combo;
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎룞�쇿뜝�숈삕�좎룞��SWT Widget �좎뙎猷밸� �좎룞�쇿뜝�쇰뙋��
	 * @param parent		�좎뙎琉꾩삕�좎룞���좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떛�귥삕
	 * @param style			�좎뙎琉꾩삕�좎룞���좎룞�숉��좎룞��SWT Style Constants)
	 * @param text			�좎뙎琉꾩삕 �좎떛紐뚯삕
	 * @param column		�좎뙎琉꾩삕�좎룞���좎룞�쇿뜝�숈삕�좎룞��GridLayout�좎룞���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕
	 * @param gridStyle		�좎뙎琉꾩삕�좎룞���좎룞�쇿뜝�숈삕�좎룞��GridData�좎룞��gridStyle
	 * @return				�좎룞�쇿뜝占폮WT Widget �좎뙎琉꾩삕
	 * @see org.eclipse.swt.widgets.Group
	 * @see org.eclipse.swt.widgets.Composite
	 * @see org.eclipse.swt.Layout.GridData
	 * @see org.eclipse.swt.SWT
	 */
	public static Group createGroup(Composite parent, int style, String text, int column, int gridStyle){
		Group group = new Group(parent, style);
		group.setFont(parent.getFont());
		group.setText(text); 
		GridLayout layout = new GridLayout();
		layout.numColumns = column;
		group.setLayout(layout);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return group;
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�숂��좑옙�좎룞�쇿뜝�숈삕�좎룞��SWT Widget �좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�쇰뙋��
	 * @param parent		�좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떛�귥삕
	 * @param style			�좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�숉��좎룞��SWT Style Constants)
	 * @param column		�좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞��GridLayout�좎룞���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕
	 * @param gridStyle		�좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞��GridData�좎룞��gridStyle
	 * @return				�좎룞�쇿뜝占폮WT Widget �좎룞�쇿뜝�숈삕�좎룞��
	 * @see org.eclipse.swt.widgets.Composite
	 * @see org.eclipse.swt.Layout.GridData
	 * @see org.eclipse.swt.SWT
	 */
	public static Composite createComposite(Composite parent, int style, int column, int gridStyle){
		Composite composite = new Composite(parent, style);
		composite.setFont(parent.getFont());
        GridLayout layout = new GridLayout();
        layout.numColumns = column;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(gridStyle));//�좎룞�쇿뜝�숈삕
        return composite;
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * compile�좎룞���좎룞�쇿뜝�숈삕 makefile�좎룞���좎룞�쇿뜝�쇰뙋��
	 * Project �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝占폦inGW�좎떛紐뚯삕 MinGW�좎룞�쇿뜝�숈삕 Makefile�좎룞���좎룞�쇿뜝�밴낀��
	 * MSVC�좎룞��MSVC �좎룞�쇿뜝�숈삕 Makefile�좎룞���좎룞�쇿뜝�쇰뙋��
	 * 
	 * @see kr.co.ed.opros.ce.core.OPRoSProjectInfo
	 * @see kr.co.ed.opros.ce.OPRoSActivator
	 * @see kr.co.ed.opros.ce.wizards.WizardMessages
	 * @see org.eclipse.core.runtime.Preferences
	 */
	public static void createMakeFile(String libTypeName, OPRoSProjectInfo prjInfo, IProject project){
		OPRoSProjectInfo info;
		String libType;
		if(prjInfo!=null)
			info = prjInfo;
		else
			return;
		if(libTypeName==null || libTypeName.isEmpty())
			libType="dll";
		else
			libType=libTypeName;
		if(info!=null){
			Iterator<String> compIt = info.getComponents();
			while(compIt.hasNext()){
				String compName = compIt.next();
				File dir = new File(info.getLocation()+"/"+info.getPrjName()+"/"+compName+"/Debug");
				if(!dir.exists())
					dir.mkdir();
				dir = new File(info.getLocation()+"/"+info.getPrjName()+"/"+compName+"/Release");
				if(!dir.exists())
					dir.mkdir();
			}
			FileOutputStream outStream=null;
			try {
				File makeFile = new File(info.getLocation()+"/"+info.getPrjName()+"/Makefile");
				outStream = new FileOutputStream(makeFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if(outStream!=null){
//				Preferences pref = OPRoSActivator.getDefault().getPluginPreferences();
				IPreferenceStore pref = OPRoSActivator.getDefault().getPreferenceStore();
				
				String strOPRoSLibPath = pref.getString(PreferenceConstants.OPROS_LIB_PATH);
				strOPRoSLibPath=strOPRoSLibPath.replace("\\", "/");
				String strOPRoSIncPath = pref.getString(PreferenceConstants.OPROS_INC_PATH);
				strOPRoSIncPath=strOPRoSIncPath.replace("\\", "/");
				String strOtherIncPath = pref.getString(PreferenceConstants.OTHER_INC_PATH);
				strOtherIncPath=strOtherIncPath.replace("\\", "/");
				String strOtherLibPath = pref.getString(PreferenceConstants.OTHER_LIB_PATH);
				strOtherLibPath=strOtherLibPath.replace("\\", "/");
				String strVSOption= pref.getString(PreferenceConstants.VS_COMPILE_OPTION);
				String strVSUnicode= pref.getString(PreferenceConstants.VS_UNICODE_OPTION);
				String strMDOption="/MD";
				
				/*
				if(strBoostLibPath.isEmpty())
					strBoostLibPath = "./boost/lib # change this with your boost library path\n";
				if(strBoostIncPath.isEmpty())
					strBoostIncPath = "./boost/include/boost-1_35 # change this with your boost include path\n";
				*/
				if(strOPRoSLibPath.isEmpty())
					strOPRoSLibPath = getOPRoSFilesPath()+"OPRoSFiles/OPRoSLib";
				if(strOPRoSIncPath.isEmpty())
					strOPRoSIncPath = getOPRoSFilesPath()+"OPRoSFiles/OPRoSInc";
				if(strVSOption.isEmpty()){
					strVSOption = "RELEASE";
					strMDOption = "/MD";
				}else if(strVSOption.compareTo("RELEASE")==0){
					strMDOption="/MD";
				}else{
					strMDOption = "/MDd";
				}
				if(strVSUnicode.isEmpty())
					strVSUnicode = "UNICODE";
				Iterator<String> it = info.getComponents();
				String szContents="";
				if(info.getImplLanguage().equals(WizardMessages.getString("NewPjtWizardPage.SelectLanguageGroup.Combo0"))
						|| info.getImplLanguage().equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo3"))){
					//szContents+="OUTDIR=.\\Release\nINTDIR=.\\Release\nOutDir=.\\Release\n";
					//szContents+="\nBOOST_INC="+strBoostIncPath;
					//szContents+="\nBOOST_LIB="+strBoostLibPath;
					szContents+="\nOPROS_INC="+strOPRoSIncPath;
					szContents+="\nOPROS_LIB="+strOPRoSLibPath;
					szContents+="\n\nSO_TYPE= "+libType+" #shared object extensions\n";// PROJECT=\n";
					
					if(info.getImplLanguage().equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo3"))) {
						szContents+="CXX=arm-iwmmxt-linux-gnueabi-g++\n";
					}
					
					
					//szContents+="INC = -I$(BOOST_INC)";
					szContents+="INC = -I$(OPROS_INC)";
					if(!strOtherIncPath.isEmpty()){
						StringTokenizer token=new StringTokenizer(strOtherIncPath,";");
						while(token.hasMoreTokens()){
							szContents+=" -I"+token.nextToken();
						}
					}
					szContents+="\n\n";
					/*
					if(OPRoSUtil.isWindowPlatform()) {
						szContents+="DEBUGCXXFLAGS=-g3 -Wall -c -fmessage-length=0 -mthreads -DMAKE_COMPONENT_DLL $(INC)\n";
						szContents+="RELEASECXXFLAGS=-O2 -Wall -c -fmessage-length=0 -mthreads -DMAKE_COMPONENT_DLL $(INC)\n\n";
					} else {
						szContents+="DEBUGCXXFLAGS=-g3 -Wall -c -fmessage-length=0 -DMAKE_COMPONENT_DLL $(INC)\n";
						szContents+="RELEASECXXFLAGS=-O2 -Wall -c -fmessage-length=0 -DMAKE_COMPONENT_DLL $(INC)\n\n";
					}
					*/
					
					szContents+="DEBUGCXXFLAGS=-g3 -Wall -c -fmessage-length=0 -DMAKE_COMPONENT_DLL $(INC)\n";
					szContents+="RELEASECXXFLAGS=-O2 -Wall -c -fmessage-length=0 -DMAKE_COMPONENT_DLL $(INC)\n\n";
					
					//szContents+="LFLAGS= -L$(BOOST_LIB)";
					
					szContents+="LFLAGS = -L$(OPROS_LIB)";
					if(!strOtherLibPath.isEmpty()){
						StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
						ArrayList<String> list = new ArrayList<String>();
						while(token.hasMoreTokens()){
							File file = new File(token.nextToken());
							if(file != null) {
								if(list.size() == 0) {
									szContents+=" -L"+file.getParent();
									list.add(file.getParent());
								} else {
									boolean isW = true;
									for(String str : list) {
										if(str.equals(file.getParent())) {
											isW = false;
											break;
										}
									}
									if(isW) {
										szContents+=" -L"+file.getParent();
										list.add(file.getParent());
									}
								}
								
							}							
						}
					}
					szContents+="\n\n";
					
					
					szContents+="LIBS= -lOPRoSCDL";
					if(!strOtherLibPath.isEmpty()){
						StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
						while(token.hasMoreTokens()){
							File file = new File(token.nextToken());
							String str = "";
							if(file != null && file.isFile() && file.getName().matches("lib.*")) {
								str = FileUtils.removeExtension(file.getName().substring(3, file.getName().length()));
							} else {
								str = FileUtils.removeExtension(file.getName());
							}
							szContents+=" -l"+str;
						}
					}
					szContents+="\n\n";
					
					String separator = "/";
					if(OPRoSUtil.isWindowPlatform())
						separator = "\\";
					
					
	
					int cnt=0;
					String compName="";
					while(it.hasNext()){
						cnt++;
						compName=it.next();
						szContents+="\nSOURCE"+cnt+"="+compName + separator + compName+".cpp";
						szContents+="\nDEBUGOBJS"+cnt+"="+compName + separator + "Debug" + separator + compName+".o";
						szContents+="\nRELEASEOBJS"+cnt+"="+compName + separator + "Release" + separator + compName+".o";
						
						if(OPRoSUtil.isWindowPlatform()) {
							szContents+="\nDEBUGTARGET"+cnt+"="+compName + separator + "Debug" + separator + compName+".$(SO_TYPE)";
							szContents+="\nRELEASETARGET"+cnt+"="+compName + separator + "Release" + separator + compName+".$(SO_TYPE)";
						} else {
							szContents+="\nDEBUGTARGET"+cnt+"="+compName + separator + "Debug" + separator +"lib"+ compName+".$(SO_TYPE)";
							szContents+="\nRELEASETARGET"+cnt+"="+compName + separator + "Release" + separator +"lib"+ compName+".$(SO_TYPE)";
						}
						
						
						
						szContents+="\n$(DEBUGOBJS"+cnt+"):";
						szContents+="\n	$(CXX) $(DEBUGCXXFLAGS) $(SOURCE"+cnt+") -o $(DEBUGOBJS"+cnt+")\n";
						szContents+="\n$(DEBUGTARGET"+cnt+"): $(DEBUGOBJS"+cnt+")";
						szContents+="\n	$(CXX) $(LFLAGS) -shared -WI,-soname,$@ -o $@ $(DEBUGOBJS"+cnt+") $(LIBS)\n";
						szContents+="\n$(RELEASEOBJS"+cnt+"):";
						szContents+="\n	$(CXX) $(RELEASECXXFLAGS) $(SOURCE"+cnt+") -o $(RELEASEOBJS"+cnt+")\n";
						szContents+="\n$(RELEASETARGET"+cnt+"): $(RELEASEOBJS"+cnt+")";
						szContents+="\n	$(CXX) $(LFLAGS) -shared -WI,-soname,$@ -o $@ $(RELEASEOBJS"+cnt+") $(LIBS)";
					}
					szContents+="\n\nall: clean";
					for(int i=1;i<=cnt;i++){
						szContents+=" $(DEBUGTARGET"+i+") $(RELEASETARGET"+i+")";
					}
					szContents+="\n\ndefault:";
					for(int i=1;i<=cnt;i++){
						szContents+=" $(DEBUGTARGET"+i+")";
					}
					szContents+="\n\nclean:\n	-rm";
					for(int j=1;j<=cnt;j++){
						szContents+=" $(DEBUGOBJS"+j+") $(DEBUGTARGET"+j+") $(RELEASEOBJS"+j+") $(RELEASETARGET"+j+")";
					}
					szContents+="\n";
				}
				else if(info.getImplLanguage().equals(WizardMessages.getString("NewPrjWizardPage.SelectLanguageGroup.Combo1"))){
					//szContents+="\nBOOST_INC="+strBoostIncPath;
					//szContents+="\nBOOST_LIB="+strBoostLibPath;
					szContents+="\nOPROS_INC="+strOPRoSIncPath;
					szContents+="\nOPROS_LIB="+strOPRoSLibPath+"\n\n";
	
					int cnt=0;
					int i=1;
					String compName="";
					while(it.hasNext()){
						cnt++;
						compName=it.next();
						szContents+="PROJECT"+cnt+"="+compName+"\n";
						if(strVSOption.compareTo("RELEASE")==0){
							szContents+="OUTDIR"+cnt+"=.\\$(PROJECT"+cnt+")\\Release\n";
							szContents+="INTDIR"+cnt+"=.\\$(PROJECT"+cnt+")\\Release\n";
							szContents+="OutDir"+cnt+"=.\\$(PROJECT"+cnt+")\\Release\n";
						}else{
							szContents+="OUTDIR"+cnt+"=.\\$(PROJECT"+cnt+")\\Debug\n";
							szContents+="INTDIR"+cnt+"=.\\$(PROJECT"+cnt+")\\Debug\n";
							szContents+="OutDir"+cnt+"=.\\$(PROJECT"+cnt+")\\Debug\n";
						}
						szContents+="TARGET"+cnt+"=$(PROJECT"+cnt+")."+libType+"\n";
						szContents+="OBJ"+cnt+"=$(PROJECT"+cnt+").obj\n\n";
					}
					szContents+="ALL:";
					for(i=1;i<=cnt;i++){
						szContents+="	$(OUTDIR"+i+")\\$(TARGET"+i+")";
					}
					szContents+="\n\nCLEAN:\n";
					for(i=1;i<=cnt;i++){
						szContents+="	-@erase \"$(OUTDIR"+i+")\\$(TARGET"+i+")\"\n";
						szContents+="	-@erase \"$(OUTDIR"+i+")\\$(OBJ"+i+")\"\n";
						szContents+="	-@erase \"$(OUTDIR"+i+")\\$(PROJECT"+i+").lib\"\n";
					}
					for(i=1;i<=cnt;i++){
						szContents+="\n$(OUTDIR"+i+") :\n	if not exist \"$(OUTDIR"+i+")/$(NULL)\" mkdir \"$(OUTDIR"+i+")\"\n";
					}
	
					//szContents+="\nINC = /I \"$(BOOST_INC)\"";
					szContents+="\nINC = /I \"$(OPROS_INC)\"";
					if(!strOtherIncPath.isEmpty()){
						StringTokenizer token=new StringTokenizer(strOtherIncPath,";");
						while(token.hasMoreTokens()){
							szContents+=" -I"+token.nextToken();
						}
					}
					szContents+="\n\n";
					szContents+="CPP=cl.exe\n\n";
					for(i=1;i<=cnt;i++){
						szContents+="CPP_PROJ"+i+"=/O2 /Oi $(INC) /GL /D \"WIN32\" /D \"_WINDOWS\" /D \"N"+strVSOption+"\" /D \"MAKE_COMPONENT_DLL\" /D \"_USRDLL\" /D \"_WINDLL\" /D \"_AFXDLL\" ";
						//szContents+="CPP_PROJ=/O2 /Oi $(INC) /GL /D \"WIN32\" /D \"_WINDOWS\" /D \"N"+strVSOption+"\"  /D \"_USRDLL\" /D \"_WINDLL\" /D \"_AFXDLL\" ";
						szContents+="/D \"_"+strVSUnicode+"\" \\\n";
						szContents+="/D \""+strVSUnicode+"\" /FD /EHsc "+strMDOption+" /Gy /Fo\"$(INTDIR"+i+")\\\\\" /Fd\"$(INTDIR"+i+")\\\\\" /W0 /nologo /c /Zi /TP /errorReport:prompt\n\n";
						szContents+="{$(PROJECT"+i+")}.cpp{$(INTDIR"+i+")}.obj::\n";
						szContents+="	$(CPP) $(CPP_PROJ"+i+") $<\n\n";
					}
					
					szContents+="LINK32=link.exe\n";
					for(i=1;i<=cnt;i++){
						//szContents+="LINK32_FLAGS"+i+"=/OUT:\"$(OUTDIR"+i+")\\$(TARGET"+i+")\" /INCREMENTAL:NO /NOLOGO /libpath:\"$(BOOST_LIB)\" /libpath:\"$(OPROS_LIB)\" \\\n";
						szContents+="LINK32_FLAGS"+i+"=/OUT:\"$(OUTDIR"+i+")\\$(TARGET"+i+")\" /INCREMENTAL:NO /NOLOGO /libpath:\"$(OPROS_LIB)\" ";
						if(!strOtherLibPath.isEmpty()){
							StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
							ArrayList<String> list = new ArrayList<String>();
							while(token.hasMoreTokens()){
								File file = new File(token.nextToken());
								if(file != null) {
									if(list.size() == 0) {
										szContents+="/libpath:"+file.getParent();
										list.add(file.getParent());
									} else {
										boolean isW = true;
										for(String str : list) {
											if(str.equals(file.getParent())) {
												isW = false;
												break;
											}
										}
										if(isW) {
											szContents+="/libpath:"+file.getParent();
											list.add(file.getParent());
										}
									}
									
								}							
							}
						}
						szContents+="\\\n";
						
						
						szContents+="/DLL /MANIFEST /"+strVSOption+" /SUBSYSTEM:WINDOWS /OPT:REF /OPT:ICF /LTCG /DYNAMICBASE /NXCOMPAT /MACHINE:X86 /ERRORREPORT:PROMPT OPRoSCDL.lib";
						if(!strOtherLibPath.isEmpty()){
							StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
							while(token.hasMoreTokens()){
								File file = new File(token.nextToken());							
								szContents+=" "+file.getName();															
							}
						}
						szContents+="\n\n\n";
					}
					
	
					for(i=1;i<=cnt;i++){
						szContents+="LINK32_OBJS"+i+"= \\\n";
						szContents+="	\"$(INTDIR"+i+")/$(OBJ"+i+")\"\n\n\n";
					}
					for(i=1;i<=cnt;i++){
						szContents+="$(OUTDIR"+i+")/$(TARGET"+i+") : $(OUTDIR"+i+") $(DEF_FILE) $(LINK32_OBJS"+i+")\n";
						szContents+="	$(LINK32) $(LINK32_FLAGS"+i+") $(LINK32_OBJS"+i+")\n";
					}
					createVS2008prjFile(info);
					createVS2010prjFile(info, project);
				}
				try {
					outStream.write(szContents.getBytes());
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * MSVC �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞���좎룞�쇿뜝占폲isual Studio 2008�좎룞���좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�쇰뙋��
	 * 	 * 
	 * @see kr.co.ed.opros.ce.core.OPRoSProjectInfo
	 * @see java.io.FileOutputStream
	 * @see kr.co.ed.opros.ce.OPRoSActivator
	 * @see org.eclipse.core.runtime.Preferences
	 */
	public static void createVS2008prjFile(OPRoSProjectInfo prj){
		OPRoSProjectInfo info = prj;
		FileOutputStream prjStream=null;
//		Preferences pref = OPRoSActivator.getDefault().getPluginPreferences();
		IPreferenceStore pref = OPRoSActivator.getDefault().getPreferenceStore();
		//String strBoostLibPath = pref.getString(PreferenceConstants.BOOST_LIB_PATH);
		//strBoostLibPath.replace("\\\\", "/");
		//String strBoostIncPath = pref.getString(PreferenceConstants.BOOST_INC_PATH);
		String strOPRoSLibPath = pref.getString(PreferenceConstants.OPROS_LIB_PATH);
		String strOPRoSIncPath = pref.getString(PreferenceConstants.OPROS_INC_PATH);
		String strVSOption= pref.getString(PreferenceConstants.VS_COMPILE_OPTION);
		String strOtherIncPath = pref.getString(PreferenceConstants.OTHER_INC_PATH);
		strOtherIncPath=strOtherIncPath.replace("\\", "/");
		String strOtherLibPath = pref.getString(PreferenceConstants.OTHER_LIB_PATH);
		strOtherLibPath=strOtherLibPath.replace("\\", "/");
		/*
		if(strBoostLibPath.isEmpty())
			strBoostLibPath = "./boost/lib # change this with your boost library path\n";
		if(strBoostIncPath.isEmpty())
			strBoostIncPath = "./boost/include/boost-1_35 # change this with your boost include path\n";
		*/
		if(strOPRoSLibPath.isEmpty())
			strOPRoSLibPath = getOPRoSFilesPath()+"OPRoSFiles/OPRoSLib";
		if(strOPRoSIncPath.isEmpty())
			strOPRoSIncPath = getOPRoSFilesPath()+"OPRoSFiles/OPRoSInc";
		if(strVSOption.isEmpty())
			strVSOption = "RELEASE";
		
		Iterator<String> it = info.getComponents();
		String szPrjContents="";
		String compName="";
		while(it.hasNext()){
			compName=it.next();
			try {
				prjStream = new FileOutputStream(info.getLocation()+"/"+info.getPrjName()+"/"+compName+"/"+compName+".vcproj");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if(prjStream!=null){
				szPrjContents="<?xml version=\"1.0\" encoding=\"euc-kr\"?>\n";
				szPrjContents+="<VisualStudioProject\n";
				szPrjContents+="ProjectType=\"Visual C++\"\n";
				szPrjContents+="Version=\"9.00\"\n";
				szPrjContents+="Name=\""+compName+"\"\n";
				szPrjContents+="RootNamespace=\""+compName+"\"\n";
				szPrjContents+="Keyword=\"Win32Proj\"\n";
				szPrjContents+="TargetFrameworkVersion=\"0\"\n";
				szPrjContents+=">\n";
				szPrjContents+="<Platforms>\n";
				szPrjContents+="<Platform\n";
				szPrjContents+="Name=\"Win32\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="</Platforms>\n";
				szPrjContents+="<ToolFiles/>\n";
				szPrjContents+="<Configurations>\n";
				szPrjContents+="<Configuration\n";
				szPrjContents+="Name=\"Debug|Win32\"\n";
				szPrjContents+="OutputDirectory=\"$(SolutionDir)$(ConfigurationName)\"\n";
				szPrjContents+="IntermediateDirectory=\"$(ConfigurationName)\"\n";
				szPrjContents+="ConfigurationType=\"2\"\n";
				szPrjContents+="UseOfMFC=\"2\"\n";
				szPrjContents+="CharacterSet=\"1\"\n";
				szPrjContents+=">\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCPreBuildEventTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCCustomBuildTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCXMLDataGeneratorTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCWebServiceProxyGeneratorTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCMIDLTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCCLCompilerTool\"\n";
				szPrjContents+="Optimization=\"0\"\n";
				
				szPrjContents+="AdditionalIncludeDirectories=\""+strOPRoSIncPath+";";
				
				if(!strOtherIncPath.isEmpty()){
					StringTokenizer token=new StringTokenizer(strOtherIncPath,";");
					while(token.hasMoreTokens()){
						szPrjContents+=" "+token.nextToken()+";";
					}
				}
				szPrjContents+="\"\n";
				
				
				//szPrjContents+="PreprocessorDefinitions=\"WIN32;_DEBUG;_CONSOLE;MAKE_COMPONENT_DLL;\"\n";
				szPrjContents+="PreprocessorDefinitions=\"WIN32;_DEBUG;_WINDOWS;_USRDLL;MAKE_COMPONENT_DLL\"\n";
				szPrjContents+="MinimalRebuild=\"true\"\n";
				szPrjContents+="BasicRuntimeChecks=\"3\"\n";
				szPrjContents+="RuntimeLibrary=\"3\"\n";
				szPrjContents+="UsePrecompiledHeader=\"0\"\n";
				szPrjContents+="WarningLevel=\"3\"\n";
				szPrjContents+="DebugInformationFormat=\"4\"\n";
				szPrjContents+="DisableSpecificWarnings=\"4290;4819;4996\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCManagedResourceCompilerTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCResourceCompilerTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCPreLinkEventTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCLinkerTool\"\n";
				szPrjContents+="AdditionalDependencies=\"OPRoSCDL.lib";
				
				if(!strOtherLibPath.isEmpty()){
					StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
					while(token.hasMoreTokens()){
						File file = new File(token.nextToken());
						if(file != null && file.isFile() && FileUtils.getExtension(file.getName()).equals("lib")) {
							szPrjContents+=" "+file.getName();
						}
						
					}
				}
				szPrjContents+="\"\n";				
				
				
				szPrjContents+="LinkIncremental=\"2\"\n";
				szPrjContents+="AdditionalLibraryDirectories=\""+strOPRoSLibPath+";";
				
				if(!strOtherLibPath.isEmpty()){
					StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
					ArrayList<String> list = new ArrayList<String>();
					while(token.hasMoreTokens()){
						File file = new File(token.nextToken());
						if(file != null) {
							if(list.size() == 0) {
								szPrjContents+=" "+file.getParent();
								list.add(file.getParent());
							} else {
								boolean isW = true;
								for(String str : list) {
									if(str.equals(file.getParent())) {
										isW = false;
										break;
									}
								}
								if(isW) {
									szPrjContents+=" "+file.getParent();
									list.add(file.getParent());
								}
							}
							
						}							
					}
				}
				szPrjContents+="\"\n";
				
				szPrjContents+="GenerateDebugInformation=\"true\"\n";
				szPrjContents+="SubSystem=\"1\"\n";
				szPrjContents+="TargetMachine=\"1\"\n";
				szPrjContents+="/>\n";				
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCALinkTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCManifestTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCXDCMakeTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCBscMakeTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCFxCopTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCAppVerifierTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCPostBuildEventTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="</Configuration>\n";
				szPrjContents+="<Configuration\n";
//				if(strVSOption.compareTo("RELEASE")==0){
					szPrjContents+="Name=\"Release|Win32\"\n";
//				}else{
//					szPrjContents+="Name=\"Debug|Win32\"\n";
//				}
				szPrjContents+="OutputDirectory=\"$(SolutionDir)$(ConfigurationName)\"\n";
				szPrjContents+="IntermediateDirectory=\"$(ConfigurationName)\"\n";
				szPrjContents+="ConfigurationType=\"2\"\n";
				szPrjContents+="UseOfMFC=\"2\"\n";
				szPrjContents+="CharacterSet=\"1\"\n";
				szPrjContents+="WholeProgramOptimization=\"1\"\n";
				szPrjContents+=">\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCPreBuildEventTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCCustomBuildTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCXMLDataGeneratorTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCWebServiceProxyGeneratorTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCMIDLTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCCLCompilerTool\"\n";
				szPrjContents+="Optimization=\"2\"\n";
				szPrjContents+="EnableIntrinsicFunctions=\"true\"\n";
				szPrjContents+="AdditionalIncludeDirectories=\""+strOPRoSIncPath+";";
				
				if(!strOtherIncPath.isEmpty()){
					StringTokenizer token=new StringTokenizer(strOtherIncPath,";");
					while(token.hasMoreTokens()){
						szPrjContents+=" "+token.nextToken()+";";
					}
				}
				szPrjContents+="\"\n";
				
				
				//szPrjContents+="PreprocessorDefinitions=\"WIN32;NDEBUG;_CONSOLE;MAKE_COMPONENT_DLL;\"\n";
				szPrjContents+="PreprocessorDefinitions=\"WIN32;NDEBUG;_WINDOWS;_USRDLL;MAKE_COMPONENT_DLL\"\n";
				szPrjContents+="RuntimeLibrary=\"2\"\n";
				szPrjContents+="EnableFunctionLevelLinking=\"true\"\n";
				szPrjContents+="UsePrecompiledHeader=\"0\"\n";
				szPrjContents+="WarningLevel=\"3\"\n";
				szPrjContents+="DebugInformationFormat=\"3\"\n";
				szPrjContents+="DisableSpecificWarnings=\"4290;4819;4996\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCManagedResourceCompilerTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCResourceCompilerTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCPreLinkEventTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCLinkerTool\"\n";
				szPrjContents+="AdditionalDependencies=\"OPRoSCDL.lib";
				
				if(!strOtherLibPath.isEmpty()){
					StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
					while(token.hasMoreTokens()){
						File file = new File(token.nextToken());
						if(file != null && file.isFile() && FileUtils.getExtension(file.getName()).equals("lib")) {
							szPrjContents+=" "+file.getName();
						}
						
					}
				}
				szPrjContents+="\"\n";				
				
				
				szPrjContents+="LinkIncremental=\"2\"\n";
				szPrjContents+="AdditionalLibraryDirectories=\""+strOPRoSLibPath+";";
				
				if(!strOtherLibPath.isEmpty()){
					StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
					ArrayList<String> list = new ArrayList<String>();
					while(token.hasMoreTokens()){
						File file = new File(token.nextToken());
						if(file != null) {
							if(list.size() == 0) {
								szPrjContents+=" "+file.getParent();
								list.add(file.getParent());
							} else {
								boolean isW = true;
								for(String str : list) {
									if(str.equals(file.getParent())) {
										isW = false;
										break;
									}
								}
								if(isW) {
									szPrjContents+=" "+file.getParent();
									list.add(file.getParent());
								}
							}
							
						}							
					}
				}
				szPrjContents+="\"\n";
				
				szPrjContents+="GenerateDebugInformation=\"true\"\n";
				szPrjContents+="SubSystem=\"1\"\n";
				szPrjContents+="OptimizeReferences=\"2\"\n";
				szPrjContents+="EnableCOMDATFolding=\"2\"\n";
				szPrjContents+="TargetMachine=\"1\"\n";
				szPrjContents+="/>\n";				
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCALinkTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCManifestTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCXDCMakeTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCBscMakeTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCFxCopTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCAppVerifierTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="<Tool\n";
				szPrjContents+="Name=\"VCPostBuildEventTool\"\n";
				szPrjContents+="/>\n";
				szPrjContents+="</Configuration>\n";
				szPrjContents+="</Configurations>\n";
				szPrjContents+="<References>\n";
				szPrjContents+="</References>\n";
				szPrjContents+="<Files>\n";
				szPrjContents+="<Filter\n";
				szPrjContents+="Name=\"source\"\n";
				szPrjContents+="Filter=\"cpp;c;cc;cxx;def;odl;idl;hpj;bat;asm;asmx\"\n";
				szPrjContents+=">\n";
				szPrjContents+="<File\n";
				szPrjContents+="RelativePath=\".\\"+compName+".cpp\"\n";
				szPrjContents+=">\n";
				szPrjContents+="</File>";
				szPrjContents+="</Filter>\n";
				szPrjContents+="<Filter\n";
				szPrjContents+="Name=\"header\"\n";
				szPrjContents+="Filter=\"h;hpp;hxx;hm;inl;inc;xsd\"\n";
				szPrjContents+=">\n";
				szPrjContents+="<File\n";
				szPrjContents+="RelativePath=\".\\"+compName+".h\"\n";
				szPrjContents+=">\n";
				szPrjContents+="</File>";
				szPrjContents+="</Filter>\n";
				szPrjContents+="<Filter\n";
				szPrjContents+="Name=\"resource\"\n";
				szPrjContents+="Filter=\"rc;ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe;resx;tiff;tif;png;wav\"\n";
				szPrjContents+=">\n";
				szPrjContents+="</Filter>\n";
				szPrjContents+="</Files>\n";
				szPrjContents+="<Globals>\n";
				szPrjContents+="</Globals>\n";
				szPrjContents+="</VisualStudioProject>\n";
				try {
					prjStream.write(szPrjContents.getBytes());
					prjStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void createVS2010prjFile(OPRoSProjectInfo prj, IProject project){
		Iterator<String> it = prj.getComponents();
		String compName="";
		
		IPreferenceStore pref = OPRoSActivator.getDefault().getPreferenceStore();
		String strOPRoSLibPath = pref.getString(PreferenceConstants.OPROS_LIB_PATH);
		String strOPRoSIncPath = pref.getString(PreferenceConstants.OPROS_INC_PATH);
		String strVSOption= pref.getString(PreferenceConstants.VS_COMPILE_OPTION);
		String strOtherIncPath = pref.getString(PreferenceConstants.OTHER_INC_PATH);
		strOtherIncPath=strOtherIncPath.replace("\\", "/");
		String strOtherLibPath = pref.getString(PreferenceConstants.OTHER_LIB_PATH);
		strOtherLibPath=strOtherLibPath.replace("\\", "/");
		
		if(strOPRoSLibPath.isEmpty())
			strOPRoSLibPath = getOPRoSFilesPath()+"OPRoSFiles/OPRoSLib";
		if(strOPRoSIncPath.isEmpty())
			strOPRoSIncPath = getOPRoSFilesPath()+"OPRoSFiles/OPRoSInc";
		if(strVSOption.isEmpty())
			strVSOption = "RELEASE";
		
		while(it.hasNext()){
			compName=it.next();
			
			Element root = new Element("Project");
			root.setAttribute("DefaultTargets", "Build");
			root.setAttribute("ToolsVersion", "4.0");
			Namespace ns = Namespace.getNamespace("http://schemas.microsoft.com/developer/msbuild/2003");
			root.setNamespace(ns);
			
			Element itemGroup = new Element("ItemGroup");
			itemGroup.setAttribute(new Attribute("Label", "ProjectConfigurations"));
			root.addContent(itemGroup);
			
			String[] modes = {"Debug", "Release"};
			for(String mode : modes) {
				Element projectConfiguration = new Element("ProjectConfiguration");
				projectConfiguration.setAttribute(new Attribute("Include", mode+"|Win32"));
				projectConfiguration.addContent(new Element("Configuration").setText(mode));
				
				projectConfiguration.addContent(new Element("Platform").setText("Win32"));
				itemGroup.addContent(projectConfiguration);
			}
			
			Element propertyGroup = new Element("PropertyGroup");
			root.addContent(propertyGroup);
			propertyGroup.setAttribute(new Attribute("Label", "Globals"));
			propertyGroup.addContent(new Element("RootNamespace").setText(compName));
			propertyGroup.addContent(new Element("Keyword").setText("Win32Proj"));
			
			root.addContent(new Element("Import").setAttribute(new Attribute("Project", "$(VCTargetsPath)\\Microsoft.Cpp.Default.props")));
			
			
			propertyGroup = new Element("PropertyGroup");
			propertyGroup.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='Release|Win32'"));
			propertyGroup.setAttribute(new Attribute("Label", "Configuration"));		
			propertyGroup.addContent(new Element("ConfigurationType").setText("DynamicLibrary"));
			propertyGroup.addContent(new Element("UseOfMfc").setText("Dynamic"));
			propertyGroup.addContent(new Element("CharacterSet").setText("Unicode"));
			propertyGroup.addContent(new Element("WholeProgramOptimization").setText("true"));
			root.addContent(propertyGroup);
			
			propertyGroup = new Element("PropertyGroup");
			propertyGroup.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='Debug|Win32'"));
			propertyGroup.setAttribute(new Attribute("Label", "Configuration"));	
			propertyGroup.addContent(new Element("ConfigurationType").setText("DynamicLibrary"));
			propertyGroup.addContent(new Element("UseOfMfc").setText("Dynamic"));
			propertyGroup.addContent(new Element("CharacterSet").setText("Unicode"));
			root.addContent(propertyGroup);		
			
			Element importe = new Element("Import").setAttribute(new Attribute("Project", "$(VCTargetsPath)\\Microsoft.Cpp.props"));
			Element importGroup = new Element("ImportGroup").setAttribute(new Attribute("Label", "ExtensionSettings"));
			root.addContent(importe);
			root.addContent(importGroup);
			
			for(String mode : modes) {
				importGroup = new Element("ImportGroup");
				importGroup.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='"+mode+"|Win32'"));
				importGroup.setAttribute(new Attribute("Label", "PropertySheets"));
				root.addContent(importGroup);
				
				importe = new Element("Import");
				importe.setAttribute(new Attribute("Project", "$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props"));
				importe.setAttribute(new Attribute("Condition", "exists('$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props')"));
				importe.setAttribute(new Attribute("Label", "LocalAppDataPlatform"));
				importGroup.addContent(importe);
			}
			
			propertyGroup = new Element("PropertyGroup").setAttribute(new Attribute("Label", "UserMacros"));
			root.addContent(propertyGroup);
			
			propertyGroup = new Element("PropertyGroup");
			propertyGroup.setContent(new Element("_ProjectFileVersion").setText("10.0.30319.1"));
			root.addContent(propertyGroup);
			for(String mode : modes) {
				Element outDir = new Element("OutDir").setText("$(SolutionDir)$(Configuration)\\");
				outDir.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='"+mode+"|Win32'"));
				propertyGroup.addContent(outDir);
				
				Element inDir = new Element("IntDir").setText("$(Configuration)\\");
				inDir.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='"+mode+"|Win32'"));
				propertyGroup.addContent(inDir);
				
				Element linkIncremental = new Element("LinkIncremental").setText("true");
				linkIncremental.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='"+mode+"|Win32'"));
				propertyGroup.addContent(linkIncremental);
			}
			
			Element itemDefinitionGroup = new Element("ItemDefinitionGroup");
			itemDefinitionGroup.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='Debug|Win32'"));
			root.addContent(itemDefinitionGroup);
			
			Element clCompile = new Element("ClCompile");
			itemDefinitionGroup.addContent(clCompile);
			clCompile.addContent(new Element("Optimization").setText("Disabled"));
			
			
			String includePath = strOPRoSIncPath + ";";		
			if(!strOtherIncPath.isEmpty()){
				StringTokenizer token=new StringTokenizer(strOtherIncPath,";");
				while(token.hasMoreTokens()){
					includePath+=token.nextToken()+";";
				}
			}
			clCompile.addContent(new Element("AdditionalIncludeDirectories").setText(includePath));
			clCompile.addContent(new Element("PreprocessorDefinitions").setText("WIN32;_DEBUG;_WINDOWS;_USRDLL;MAKE_COMPONENT_DLL;%(PreprocessorDefinitions)"));
			clCompile.addContent(new Element("MinimalRebuild").setText("true"));
			clCompile.addContent(new Element("BasicRuntimeChecks").setText("EnableFastChecks"));
			clCompile.addContent(new Element("RuntimeLibrary").setText("MultiThreadedDebugDLL"));
			clCompile.addContent(new Element("PrecompiledHeader"));
			clCompile.addContent(new Element("WarningLevel").setText("Level3"));
			clCompile.addContent(new Element("DebugInformationFormat").setText("EditAndContinue"));
			clCompile.addContent(new Element("DisableSpecificWarnings").setText("4290;4819;4996;%(DisableSpecificWarnings)"));
			
			Element link = new Element("Link");
			itemDefinitionGroup.addContent(link);
			
			String lib = "OPRoSCDL.lib;";
			
			if(!strOtherLibPath.isEmpty()){
				StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
				while(token.hasMoreTokens()){
					File file = new File(token.nextToken());
					lib+=file.getName()+";";			
				}
			}
			link.addContent(new Element("AdditionalDependencies").setText(lib+"%(AdditionalDependencies)"));
			
			String libPath = strOPRoSLibPath+";";
			
			if(!strOtherLibPath.isEmpty()){
				StringTokenizer token=new StringTokenizer(strOtherLibPath,";");
				ArrayList<String> list = new ArrayList<String>();
				while(token.hasMoreTokens()){
					File file = new File(token.nextToken());
					if(file != null) {
						if(list.size() == 0) {
							libPath+=file.getParent()+";";
							list.add(file.getParent());
						} else {
							boolean isW = true;
							for(String str : list) {
								if(str.equals(file.getParent())) {
									isW = false;
									break;
								}
							}
							if(isW) {
								libPath+=file.getParent()+";";
								list.add(file.getParent());
							}
						}
						
					}							
				}
			}
			link.addContent(new Element("AdditionalLibraryDirectories").setText(libPath+"%(AdditionalLibraryDirectories)"));
			link.addContent(new Element("GenerateDebugInformation").setText("true"));
			link.addContent(new Element("SubSystem").setText("Console"));
			link.addContent(new Element("TargetMachine").setText("MachineX86"));
			
			
			
			itemDefinitionGroup = new Element("ItemDefinitionGroup");
			itemDefinitionGroup.setAttribute(new Attribute("Condition", "'$(Configuration)|$(Platform)'=='Release|Win32'"));
			root.addContent(itemDefinitionGroup);
			
			clCompile = new Element("ClCompile");
			itemDefinitionGroup.addContent(clCompile);
			clCompile.addContent(new Element("Optimization").setText("MaxSpeed"));
			
			
			clCompile.addContent(new Element("IntrinsicFunctions").setText("true"));
			clCompile.addContent(new Element("AdditionalIncludeDirectories").setText(includePath));
			clCompile.addContent(new Element("PreprocessorDefinitions").setText("WIN32;NDEBUG;_WINDOWS;_USRDLL;MAKE_COMPONENT_DLL;%(PreprocessorDefinitions)"));
			clCompile.addContent(new Element("RuntimeLibrary").setText("MultiThreadedDLL"));
			clCompile.addContent(new Element("FunctionLevelLinking").setText("true"));
			clCompile.addContent(new Element("PrecompiledHeader"));
			clCompile.addContent(new Element("WarningLevel").setText("Level3"));
			clCompile.addContent(new Element("DebugInformationFormat").setText("ProgramDatabase"));
			clCompile.addContent(new Element("DisableSpecificWarnings").setText("4290;4819;4996;%(DisableSpecificWarnings)"));
			
			link = new Element("Link");
			itemDefinitionGroup.addContent(link);			
			
			link.addContent(new Element("AdditionalDependencies").setText(lib+"%(AdditionalDependencies)"));
			link.addContent(new Element("AdditionalLibraryDirectories").setText(libPath+"%(AdditionalLibraryDirectories)"));
			link.addContent(new Element("GenerateDebugInformation").setText("true"));
			link.addContent(new Element("SubSystem").setText("Console"));
			link.addContent(new Element("OptimizeReferences").setText("true"));
			link.addContent(new Element("EnableCOMDATFolding").setText("true"));
			link.addContent(new Element("TargetMachine").setText("MachineX86"));
			
			String[] fileExtensions = {".cpp", ".h"};
			for(String fileExtension : fileExtensions) {
				itemGroup = new Element("ItemGroup");
				root.addContent(itemGroup);		
				itemGroup.addContent(new Element("ClCompile").setAttribute("Include", compName + fileExtension));
			}
			
			importe = new Element("Import").setAttribute(new Attribute("Project", "$(VCTargetsPath)\\Microsoft.Cpp.targets"));
			root.addContent(importe);
			
			importGroup = new Element("ImportGroup").setAttribute(new Attribute("Label", "ExtensionTargets"));
			root.addContent(importGroup);
			
			setNamespace(root, ns);
			
			
			IFile vsProfile = project.getFile(compName + File.separator + compName+".vcxproj");
			Document document = new Document(root);
			
			XmlUtil.writeDocumentToIFile(document, vsProfile, null);
		}		
	}
	
	
	public static void setNamespace(Element parent, Namespace namespace) {
		for(Object obj : parent.getChildren()) {
			if(obj instanceof Element) {
				Element child = (Element) obj;
				child.setNamespace(namespace);
				
				if(child.getChildren() != null && child.getChildren().size() != 0) {
					setNamespace(child, namespace);
				}
			}
		}
	}
	
	
	/**
	 * static �좎뙣�뚮벝��
	 * Runtime�좎룞�쇿뜝�숈삕�좎룞��OPRoSFiles �좎룞�쇿뜝�숈삕�좑옙�좎룞�숈튂�좎룞���좎룞�쇿뜝�숈삕�좎듅�먯삕.
	 * 
	 * @return OPRoSFiles�좎룞���좎룞�쇿뜝占�	 * 
	 * @see kr.co.ed.opros.ce.OPRoSActivator
	 * @See org.eclipse.core.runtime.Platform
	 */
	@SuppressWarnings("deprecation")
	public static String getOPRoSFilesPath(){
		if(OPRoSFilesPath==null||OPRoSFilesPath.isEmpty()){
			URL url = OPRoSActivator.getDefault().getBundle().getEntry("/");
			try {
				OPRoSFilesPath = Platform.asLocalURL(url).getPath();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(OPRoSFilesPath.startsWith("/"))
				OPRoSFilesPath = OPRoSFilesPath.substring(1);
		}
		if(!isWindowPlatform()) {
			OPRoSFilesPath  = "//"+OPRoSFilesPath;
		}
		return OPRoSFilesPath;
	}
	
	/**
	 * �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕�좎룞���좎떩�듭삕 JDom Document�좎룞���좎룞�쇿뜝�먨뜝�숈삕�좎룞��
	 * @param prj
	 * @return
	 */
	public static Document getProjectSettingDocument(IProject prj) {
		IFile projectProfile = prj.getFile(prj.getName()+"Prj.xml");
		FileInputStream input=null;
		try {
			input = (FileInputStream) projectProfile.getContents();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build( input );
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(input!=null){
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return doc;
	}
	
	/**
	 * OPRoS �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞���좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕�좎떬�먯삕.
	 * @param prj
	 * @return
	 */
	public static OPRoSProjectInfo getProjectInfo(IProject prj) {
		OPRoSProjectInfo prjInfo = new OPRoSProjectInfo();
		prjInfo.loadProfile(getProjectSettingDocument(prj).getRootElement());
		return prjInfo;
	}
	
	/**
	 * �좎뙏�먯삕 �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞���좎룞�숁틶�좑옙�좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�숈삕�좑옙�좎룞�쇿뜝�숈삕�좎떬�먯삕.
	 * @param prj
	 * @return
	 */
	public static Iterator<String> getComponentList(IProject prj) {		
		OPRoSProjectInfo prjInfo = new OPRoSProjectInfo();
		prjInfo.loadProfile(getProjectSettingDocument(prj).getRootElement());
		return prjInfo.getComponents();		
	}
	
	/**
	 * static �좎뙣�뚮벝��
	 * �좎룞�쇿뜝�숈삕�좎룞�숉듃�좎룞���좎룞�쇿뜝�숈삕�좎룞�쇿뜝占폦inGW,MSVC,JAVA) �좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎듅�먯삕.
	 * 
	 * @param IProject �닷뜝�숈삕�좎룞�쇿뜝�숈삕 �좎떥�숈삕�좎떦�숈삕, �좎룞�쇿뜝�숈삕�좎룞�숉듃 �좎룞�쇿뜝�숈삕
	 * @return MinGW C++�좎룞���좎룞�쇿뜝占��좎룞���좎룞�쇿뜝�숈삕, MSVC C++�좎룞���좎룞�쇿뜝占��좎룞���좎룞�쇿뜝�숈삕
	 *  
	 * @see kr.co.ed.opros.ce.core.OPRoSProjectInfo
	 * @see java.io.FileInputStream
	 * @see org.eclipse.core.resources.IFile
	 * @see org.jdom.input.SAXBuilder
	 * @see org.jdom.Document
	 * @see org.jdom.Element
	 */
	public static int getProjectLanguageSetting(IProject prj, OPRoSProjectInfo info){
		OPRoSProjectInfo prjInfo=info;
		int ret=-1;
        if(prj!=null){	       
			Element root;
			root = getProjectSettingDocument(prj).getRootElement();
			if(prjInfo!=null){
				prjInfo.clear();
				prjInfo.loadProfile(root);
				if(prjInfo.getImplLanguage().equalsIgnoreCase("MSVC C++"))
					ret= 1;
				else if(prjInfo.getImplLanguage().equalsIgnoreCase("MinGW C++"))
					ret= 0;
			}			
        }
		return ret;
	}
	public static Image createImage(String name){
		InputStream stream = OPRoSActivator.class.getResourceAsStream(name);
		Image image = new Image(null,stream);
		try{
			stream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return image;
	}
	public static boolean isDuplicatePortName(String portName,boolean bNotSelf,OPRoSComponentElementModel comModel){
		OPRoSComponentElementModel compModel = comModel;
		boolean bCheck=false;
		if(compModel!=null){
			OPRoSBodyElementModel bodyModel = (OPRoSBodyElementModel)compModel.getParent();
			if(bodyModel!=null){
				Iterator<OPRoSElementBaseModel> it = bodyModel.getChildrenList().iterator();
				OPRoSElementBaseModel model;
				while(it.hasNext()){
					model=it.next();
					if(model instanceof OPRoSPortElementBaseModel){
						if(portName.compareTo(((OPRoSPortElementBaseModel)model).getName())==0){
							if(bNotSelf&&bCheck==false){
								bCheck=true;
							}else{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean isDuplicatePropertyName(String propertyName,boolean bNotSelf,OPRoSComponentElementModel comModel){
		OPRoSComponentElementModel compModel = comModel;
		boolean bCheck=false;
		if(compModel!=null){
			Iterator<OPRoSElementBaseModel> it = compModel.getPropertiesModel().getChildrenList().iterator();
			OPRoSElementBaseModel model;
			while(it.hasNext()){
				model=it.next();
				if(model instanceof OPRoSPropertyElementModel){
					if(propertyName.compareTo(((OPRoSPropertyElementModel)model).getName())==0){
						if(bNotSelf&&bCheck==false){
							bCheck=true;
						}else{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean isDuplicateMonitorVariableName(String name,boolean bNotSelf,OPRoSComponentElementModel comModel){
		OPRoSComponentElementModel compModel = comModel;
		boolean bCheck=false;
		if(compModel!=null){
			Iterator<OPRoSElementBaseModel> it = compModel.getMonitoringVariablesModel().getChildrenList().iterator();
			OPRoSElementBaseModel model;
			while(it.hasNext()){
				model=it.next();
				if(model instanceof MonitoringVariableModel){
					if(name.compareTo(((MonitoringVariableModel)model).getName())==0){
						if(bNotSelf&&bCheck==false){
							bCheck=true;
						}else{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean isDuplicateDataTypeName(String dataTypeName, boolean bNotSelf, OPRoSComponentElementModel comModel){
		OPRoSComponentElementModel compModel = comModel;	
		boolean isDuplicated = false;
		boolean bCheck=false;
		if(compModel!=null){
			Iterator<OPRoSElementBaseModel> it = compModel.getDataTypesModel().getChildrenList().iterator();
			OPRoSElementBaseModel model;
			while(it.hasNext()){
				model=it.next();
				if(model instanceof OPRoSDataTypeElementModel){
					if(dataTypeName.compareTo(((OPRoSDataTypeElementModel)model).getDataTypeFileName())==0){
						if(bNotSelf&&bCheck==false){
							bCheck=true;
						}else{
							isDuplicated = true;
						}
					}
				}
			}
		}
		
		if(!isDuplicated) {
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)compModel.getParent();
			if(parent != null) {
				IFolder folder = parent.getOPRoSEditor().getProject().getFolder(compModel.getComponentName()).getFolder("profile");
				if(folder != null && folder.isAccessible()) {
					IResource[] members = null;
					try {
						members = folder.members();
					} catch (CoreException e) {
						e.printStackTrace();
					}
					
					if(members != null && members.length !=0) {
						for(IResource resource : members) {
							if(dataTypeName.equals(resource.getName())) {
								isDuplicated = true;
								break;
							}
						}
					}					
				}
			}
			
		}
		
		return isDuplicated;
	}
	
	public static boolean isDuplicateServiceTypeName(String serviceTypeName, boolean bNotSelf, OPRoSElementBaseModel comp){
		OPRoSComponentElementModel compModel = (OPRoSComponentElementModel)comp;
		boolean isDuplicated = false;
		boolean bCheck=false;
		if(compModel!=null){
			Iterator<OPRoSElementBaseModel> it = compModel.getServiceTypesModel().getChildrenList().iterator();
			OPRoSElementBaseModel model;
			while(it.hasNext()){
				model=it.next();
				if(model instanceof OPRoSServiceTypeElementModel){
					if(serviceTypeName.compareTo(((OPRoSServiceTypeElementModel)model).getServiceTypeFileName())==0){
						if(bNotSelf&&bCheck==false){
							bCheck=true;
						}else{
							isDuplicated = true;
						}
					}
				}
			}
		}
		
		if(!isDuplicated) {
			OPRoSBodyElementModel parent = (OPRoSBodyElementModel)compModel.getParent();
			if(parent != null) {
				IFolder folder = parent.getOPRoSEditor().getProject().getFolder(compModel.getComponentName()).getFolder("profile");
				if(folder != null && folder.isAccessible()) {
					IResource[] members = null;
					try {
						members = folder.members();
					} catch (CoreException e) {
						e.printStackTrace();
					}
					
					if(members != null && members.length !=0) {
						for(IResource resource : members) {
							if(serviceTypeName.equals(resource.getName())) {
								isDuplicated = true;
								break;
							}
						}
					}					
				}
			}
			
		}
		
		return isDuplicated;
	}
	
	/**
	 * �좎룜�곸껜�좎룞�쇿뜝�숈삕 �좎룞���좎룞�쇿뜝�깅툕�у뜝�숈삕 ��뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕�좎떬�먯삕.
	 * Window �좎띁��: dll
	 * Linux �좎띁��: os
	 * @return
	 */
	public static String getLibType() {
		String libType = "so";
		if(isWindowPlatform()) {
			libType = "dll";
		}
		return libType;
	}
	
	public static boolean isWindowPlatform() {
		boolean isWindow = false;
		String osName = System.getProperty("os.name");
		if(osName.matches("Windows.*")) {
			isWindow = true;
		}
		return isWindow;
	}
	
}
