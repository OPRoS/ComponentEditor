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


import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.ICModel;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class OpenOPRoSRobotModelerActionDelegate implements
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
//		String REGQUERY = "reg query \"HKCU\\SOFTWARE\\ires\\ires\\path\" /v path";
//		String REGSTR = "REG_SZ";
//		String startDir=null;
//		String result=null;
//		try{
//			Process process = Runtime.getRuntime().exec(REGQUERY);
//			OPRoSRegistryStreamReader reader = new OPRoSRegistryStreamReader(process.getInputStream());
//			reader.start();
//			process.waitFor();
//			reader.join();
//			result=reader.getResult();
//			if(result.isEmpty()){
//				OPRoSUtil.openMessageBox("Not Installed RobotModeler", SWT.ERROR);
//				return;
//			}
//			int p = result.indexOf(REGSTR);
//			if(p!=-1){
//				result = result.substring(p+REGSTR.length(),result.length()).trim();
//				startDir = result;
//				result += "\\RobotEditor.exe";
//			}else{
//				System.out.println("Can't find RobotModeler");
//				System.exit(1);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		try{
//			File dir = new File(startDir);
//			Runtime.getRuntime().exec(result,null,dir);
//			//Runtime.getRuntime().exec("java -jar C:\\kr.re.etri.tpl.taskmodel.edit_1.0.0.jar");
//		}catch(IOException e){
//			e.printStackTrace();
//		}
		ICModel model = CCorePlugin.getDefault().getCoreModel().getDefault().getCModel();

		System.out.println(model.toString());
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

}
