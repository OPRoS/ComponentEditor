package kr.co.ed.opros.ce.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.editors.text.TextEditor;

public class TextEditorEx extends TextEditor {
	
	public OPRoSManifestEditor dEditor;
	
	public TextEditorEx(OPRoSManifestEditor dEditor) {
		super();
		this.dEditor = dEditor;
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		// TODO Auto-generated method stub
		super.doSave(progressMonitor);
		dEditor.setDirty(false);
	}
	
}
