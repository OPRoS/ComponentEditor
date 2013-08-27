package kr.co.ed.opros.ce.editors;

import java.io.File;

import org.eclipse.jface.preference.PathEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class PathEditorEx extends PathEditor {
	
	public PathEditorEx(String name, String labelText,
            String dirChooserLabelText, Composite parent) {
		super(name, labelText, dirChooserLabelText, parent);
	}
	@Override
	protected String getNewInputObject() {
		FileDialog dialog = new FileDialog(getShell());
		//dialog.setFilterExtensions(new String[] {"*.lib"});
        String dir = dialog.open();
        if (dir != null) {
            dir = dir.trim();
            if (dir.length() == 0) {
				return null;
			}
        }
        return dir;
	}
}
