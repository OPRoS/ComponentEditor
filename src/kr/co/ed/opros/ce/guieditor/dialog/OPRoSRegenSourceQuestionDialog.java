package kr.co.ed.opros.ce.guieditor.dialog;

import kr.co.ed.opros.ce.OPRoSUtil;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class OPRoSRegenSourceQuestionDialog extends Dialog {
	public static final int MODIFY = 1001;
	public static final int REGEN = 1002;
	public static final int NORMALSAVE = 1003;
	protected boolean m_bRegen=false;
	public OPRoSRegenSourceQuestionDialog(Shell parentShell) {
		super(parentShell);
	}
	public OPRoSRegenSourceQuestionDialog(Shell parentShell,boolean bRegen) {
		super(parentShell);
		m_bRegen=bRegen;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite)super.createDialogArea(parent);
		if(m_bRegen)
			OPRoSUtil.createLabel(container, "Click Button [Modify]/[REGEN]/[CANCLE].\n\n"+
					"[MODIFY] : Source codes are updated\n"+
					"[REGEN]  : Source codes are newly regeneration",
					SWT.NONE,1,5,1,0,GridData.BEGINNING);
		else
			OPRoSUtil.createLabel(container, "Will Do Save Only Component Profile.\n\n",
					SWT.NONE,1,5,1,0,GridData.BEGINNING);
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if(m_bRegen){
			Button modifyBtn = createButton(parent, MODIFY, "Modify", true);
			modifyBtn.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
					setReturnCode(MODIFY);
					close();
				}
			});
			Button regenBtn = createButton(parent, REGEN, "Regen", false);
			regenBtn.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
					setReturnCode(REGEN);
					close();
				}
			});
		}else{
			Button normalSaveBtn = createButton(parent, NORMALSAVE, "Save", true);
			normalSaveBtn.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
					setReturnCode(NORMALSAVE);
					close();
				}
			});
		}
		Button cancelBtn = createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
		cancelBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				setReturnCode(IDialogConstants.CANCEL_ID);
				close();
			}
		});
//		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Save Option Dialog");
		super.configureShell(newShell);
	}

}
