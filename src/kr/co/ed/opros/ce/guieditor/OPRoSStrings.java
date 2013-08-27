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
package kr.co.ed.opros.ce.guieditor;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class OPRoSStrings {
	// Bundle ID
	private static final String BUNDLE_ID = "kr.co.ed.opros.ce.guieditor.OPRoSGUIProfileEditorStrings"; //$NON-NLS-1$
	//Resource bundle.
	private static ResourceBundle resourceBundle;
	static {
		try {
			resourceBundle = ResourceBundle.getBundle(BUNDLE_ID);
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}
	public static String getFormattedString(String key, String arg) {
		return MessageFormat.format(getString(key), (Object[])new String[] { arg });
	}

	public static String getFormattedString(String key, String[] args) {
		return MessageFormat.format(getString(key), (Object[])args);
	}

	public static String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (NullPointerException e) {
			return "#" + key + "#"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private OPRoSStrings() {
		// No constructor
	}
}
