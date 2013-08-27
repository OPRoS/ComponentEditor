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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class OPRoSRegistryStreamReader extends Thread {
	private InputStream is;
	private StringWriter sw;
	OPRoSRegistryStreamReader(InputStream is){
		this.is = is;
		sw=new StringWriter();
	}
	public void run(){
		try{
			int c;
			while((c=is.read())!=-1)
				sw.write(c);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	String getResult(){
		return sw.toString();
	}
}
