package kr.co.ed.opros.ce.preferences;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.ui.ConnectionSelectDialog;

import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.files.ui.dialogs.SystemRemoteFileDialog;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class OPRoSRemoteFileFieldEditor extends StringButtonFieldEditor {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControl(Composite parent) {
		super.createControl(parent);
		Button btn = this.getChangeControl(parent);
		btn.setText("Remote Browse");
		btn.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
			
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				openRemoteBrowser();
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
			
			}
			
		});
		this.setButtonLayoutData(btn);
	}
	public OPRoSRemoteFileFieldEditor(String str,String title, Composite parent){
		super(str,title,parent);
	}
	@Override
	protected String changePressed() {
		return "TESTS";
	}
	
	private void openRemoteBrowser(){
		IHost currentConnectionSelected = getCurrentConnection();
		if(currentConnectionSelected==null){
			OPRoSUtil.openMessageBox("Must OPRoS Connection is created", SWT.ERROR);
		}else{
			SystemRemoteFileDialog dlg = new SystemRemoteFileDialog(
					getShell(),
					"Remote System Browser",
					currentConnectionSelected);
			dlg.setBlockOnOpen(true);
			if (dlg.open() == Window.OK) {
				Object retObj = dlg.getSelectedObject();
				if (retObj instanceof IRemoteFile) {
					IRemoteFile selectedFile = (IRemoteFile) retObj;
					this.setStringValue(selectedFile.getAbsolutePath());
				}
			}
		}
	}
	protected IHost getCurrentConnection() {
		ConnectionSelectDialog dlg = new ConnectionSelectDialog(CUIPlugin.getActiveWorkbenchShell());
		int ret = dlg.open();
		IHost remoteHost=null;
		if(ret==Dialog.OK){
			remoteHost= dlg.getSelectionHost();//RSEHelper.getRemoteConnectionByName("OPRoS");
		}else{
			return null;
		}
		return remoteHost;
    }
}
