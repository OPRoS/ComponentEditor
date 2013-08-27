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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = OPRoSActivator.getDefault().getPreferenceStore();
		String path = OPRoSUtil.getOPRoSFilesPath();
		path+="OPRoSFiles/";
		//store.setDefault(PreferenceConstants.OPROS_LIB_PATH,path+"OPRoSLib");
		//store.setDefault(PreferenceConstants.OPROS_INC_PATH,path+"OPRoSInc");
		
		store.setDefault(PreferenceConstants.OPROS_LIB_PATH, "C:\\Program Files\\OPRoS\\OporsDevelopment\\IDE\\CommonLibrary\\lib");
		store.setDefault(PreferenceConstants.OPROS_INC_PATH, "C:\\Program Files\\OPRoS\\OporsDevelopment\\IDE\\CommonLibrary\\include");
		
		store.setDefault(PreferenceConstants.OPROS_ENGINE_FILE, "C:\\Program Files\\OPRoS\\OprosRobot\\Binanry\\ComponentEngine.exe");
		store.setDefault(PreferenceConstants.OPROS_REPOSITORY_PATH, "C:\\Program Files\\OPRoS\\OprosRobot\\Package");
		//store.setDefault(PreferenceConstants.OTHER_INC_PATH, "C:\\Program Files\\OPRoS\\OPRoS_Dev\\IDE\\Common_Library\\DeviceAPI");
		/*
		store.setDefault(PreferenceConstants.COPYRIGHT_NAME, "");
		store.setDefault(PreferenceConstants.COPYRIGHT_ADDRESS, "");
		store.setDefault(PreferenceConstants.COPYRIGHT_PHONE,"");
		store.setDefault(PreferenceConstants.COPYRIGHT_HOMEPAGE,"");
		*/
		store.setDefault(PreferenceConstants.VS_COMPILE_OPTION,"RELEASE");
		store.setDefault(PreferenceConstants.VS_UNICODE_OPTION,"UNICODE");
		//store.setDefault(PreferenceConstants.OPROS_REPOSITORY_PATH, "");
		//store.setDefault(PreferenceConstants.OPROS_BOOST_SERIALIZATION_LIBNAME, "libboost_serialization-mgw34-mt");
	}

}
