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
package kr.co.ed.opros.ce.ui.actions;

import java.io.File;
import java.io.IOException;

import kr.co.ed.opros.ce.OPRoSUtil;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class OpenOPRoSStageModelerActionDelegate implements
		IWorkbenchWindowActionDelegate {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction arg0) {
		String REGQUERY = "reg query \"HKCU\\SOFTWARE\\ires\\ires\\path\" /v path";
		String REGSTR = "REG_SZ";
		String startDir=null;
		String result=null;
		try{
			Process process = Runtime.getRuntime().exec(REGQUERY);
			OPRoSRegistryStreamReader reader = new OPRoSRegistryStreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
			result=reader.getResult();
			if(result.isEmpty()){
				OPRoSUtil.openMessageBox("Not Installed StageModeler", SWT.ERROR);
				return;
			}
			int p = result.indexOf(REGSTR);
			if(p!=-1){
				result = result.substring(p+REGSTR.length(),result.length()).trim();
				startDir = result;
				result += "\\StageEditor.exe";
			}else{
				System.out.println("Can't find Environment Modeler");
				System.exit(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			File dir = new File(startDir);
			Runtime.getRuntime().exec(result,null,dir);
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

}
