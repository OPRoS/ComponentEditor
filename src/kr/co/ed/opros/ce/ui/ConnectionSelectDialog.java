package kr.co.ed.opros.ce.ui;

import javax.swing.JOptionPane;

import kr.co.ed.opros.ce.wizards.OPRoSRSENewConnectionWizard;

import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class ConnectionSelectDialog extends Dialog {
	private static final int NEW_CONNECTION = IDialogConstants.NO_TO_ALL_ID+10;
	private static final int DELETE_CONNECTION = IDialogConstants.NO_TO_ALL_ID+11;
	private IHost m_targetHost=null;
	private List hostList=null;
	IHost[] hosts;
	int nHostCnt;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, NEW_CONNECTION, "New Host", false);
		createButton(parent, DELETE_CONNECTION, "Delete", false);
		super.createButtonsForButtonBar(parent);
		
	}
	
	public ConnectionSelectDialog(Shell parentShell){
		super(parentShell);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId==Dialog.CANCEL){
			m_targetHost=null;
		}else if(buttonId==NEW_CONNECTION){
			OPRoSRSENewConnectionWizard wizard=new OPRoSRSENewConnectionWizard();
			WizardDialog dialog = new WizardDialog(CUIPlugin.getActiveWorkbenchShell(),wizard);
			dialog.create();
			dialog.open();
			reflashHostList();
		}else if(buttonId==DELETE_CONNECTION){
			if(m_targetHost!=null){
				RSECorePlugin.getDefault().getSystemRegistry().deleteHost(m_targetHost);
				reflashHostList();
			}else{
				JOptionPane.showMessageDialog(null, "Host is not Selected");
			}
		}
		super.buttonPressed(buttonId);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite)super.createDialogArea(parent);
		FormLayout layout = new FormLayout();
		comp.setLayout(layout);
		Label label = new Label(comp,SWT.NONE);
		label.setText(" Connection Name List : ");
		FormData labelLayoutData = new FormData();
		labelLayoutData.top = new FormAttachment(5,0);
		labelLayoutData.left = new FormAttachment(2,0);
		label.setLayoutData(labelLayoutData);
		hostList = new List(comp, SWT.SINGLE|SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(15,0);
		layoutData.left = new FormAttachment(0,0);
		layoutData.right = new FormAttachment(100);
		layoutData.bottom = new FormAttachment(100);
		layoutData.height=150;
		layoutData.width=100;
		hostList.setLayoutData(layoutData);
		hostList.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				int selectIndex = hostList.getSelectionIndex();
				m_targetHost = hosts[selectIndex];
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				int selectIndex = hostList.getSelectionIndex();
				m_targetHost = hosts[selectIndex];
			}
		});
		reflashHostList();
		this.getShell().setText("Select a Target Robot Connection");
		return comp;
	}
	
	private void reflashHostList(){
		try {
			RSECorePlugin.waitForInitCompletion();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		hosts = RSECorePlugin.getDefault().getSystemRegistry().getHosts();
		nHostCnt = RSECorePlugin.getDefault().getSystemRegistry().getHostCount();
		hostList.removeAll();
		for(int i=0;i<nHostCnt;i++){
			hostList.add(String.valueOf(i+1)+". "+hosts[i].getName());
		}
		if(nHostCnt>0){
			hostList.select(nHostCnt-1);
			m_targetHost=hosts[nHostCnt-1];
		}
	}
	
	public IHost getSelectionHost(){
		return m_targetHost;
	}
}
