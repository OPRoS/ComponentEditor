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

package kr.co.ed.opros.ce.preferences;

import kr.co.ed.opros.ce.OPRoSActivator;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class OPRoS_CE_CopyrighterPage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	public OPRoS_CE_CopyrighterPage(){
		super(GRID);
		setPreferenceStore(OPRoSActivator.getDefault().getPreferenceStore());
		setDescription("Copyright Infomation Setting Page of OPRoS Component Editor");
	}
	@Override
	protected void createFieldEditors() {
		addField(
			new StringFieldEditor(PreferenceConstants.COPYRIGHT_NAME, "Copyright Name:", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.COPYRIGHT_ADDRESS, "Copyright Address:", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.COPYRIGHT_PHONE, "Copyright Phone:", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.COPYRIGHT_HOMEPAGE, "Copyright Homepage:", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}

}
