package kr.co.ed.opros.ce.editors;

import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.model.CModelManager;
import org.eclipse.cdt.internal.ui.editor.CEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;

public class CEditorEx extends CEditor {
	
	public ICElement cElement;
	
	/**
	 * 현재 에디터로 인풋된 파일의 C 엘리먼트를 구한다.
	 * @return
	 */
	public ICElement getCElement() {
		if(cElement == null) {
			IFile file =  ((IFileEditorInput) getEditorInput()).getFile();
			cElement = CModelManager.getDefault().create(file, null);
		}		
		return cElement;	
	}
	
	/*
	public ICElement getCElement() {
		if(fOutlinePage != null)
			return fOutlinePage.getRoot();
		return null;
	}
	*/
	
}
