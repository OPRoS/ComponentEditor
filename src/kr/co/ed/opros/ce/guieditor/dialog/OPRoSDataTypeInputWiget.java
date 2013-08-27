package kr.co.ed.opros.ce.guieditor.dialog;

import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

/**
 * �좎룞�쇿뜝�숈삕 ��뜝�숈삕 �좎룞���좎룞�쇿뜝�깆뼲�숁뀭�좑옙�좎룞�쇿뜝�숈삕�좎떗�몄삕 �좎룞�숃펹�쎌삕�좑옙�붷뜝�숈삕 �좎룞���좎룞�쇿뜝�숈삕�좎룞��
 * @author hclee
 *
 */
public class OPRoSDataTypeInputWiget {
	
	public OPRoSDataTypeInputDialogComposite parent;
	
	public Composite composite;
	
	public Text text_name;
	
	public OPRoSDataTypeInputWiget(OPRoSDataTypeInputDialogComposite parent) {
		this.parent = parent;
		this.composite = parent.getChangeContainer();
	}
	
	
	/**
	 * �좎룞�쇿뜝�숈삕 �좎뙎琉꾩삕 �좎룞�숉듃�좎룞���좎룞�쇿뜝�쒕릺�듭삕�좎룞�쇿뜝�숈삕 �붷뜝�숈삕
	 * @param parent
	 */
	public Control[] createServiceRootGroup(final TreeItem item) {
		Label label = createLabel("Group Name of Data Calls : " +
				"Group Names of functions or methods that are accessed between data ports.", 
				50, SWT.BORDER | SWT.NONE | SWT.CENTER);
		
		Button btn_AddMethod = getButtonInstance(composite, false, 2);
		btn_AddMethod.setText("Add User Data Type");
		btn_AddMethod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				composite.layout(createServiceCreateGroup(item, true));
			}			
		});
		
		Button btn_Delete = getButtonInstance(composite, false, 1);
		btn_Delete.setText("Delete");
		btn_Delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteTreeItem(item);
			}			
		});
		
		List<Control> list = new ArrayList<Control>();
		list.add(label);
		list.add(btn_AddMethod);
		list.add(btn_Delete);
		
		return list.toArray(new Control[list.size()]);
	}	
	
	/**
	 * �좎룞�쇿뜝�숈삕 ��뜝�숈삕 �좎룞�쇿뜝�숈삕�붷뜝�숈삕
	 * @param parent
	 */
	public Control[] createServiceCreateGroup(final TreeItem item, final boolean isNew) {		
		Label label = createLabel("Data Type Name", 0, SWT.NONE);	
		
		final Text text_ServiceTypeName = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		text_ServiceTypeName.setLayoutData(gd);		
		
		Button btn_Previous = getButtonInstance(composite, true, 1);
		btn_Previous.setText("Previous");
		btn_Previous.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				if(isNew)
					composite.layout(createServiceRootGroup(item));
				else
					composite.layout(createMethodRootGroup(item));
			}
			
		});		
		
		
		Button btn_Ok = getButtonInstance(composite, true, 1);
		btn_Ok.setText("OK");
		btn_Ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = text_ServiceTypeName.getText();	
				parent.setDataTypeFileName(name);
				disposeWiget(composite);
				if(isNew) {
					if(!isExistItem(parent.getRootItem(), name)){
						TreeItem item = new TreeItem(parent.getRootItem(),SWT.NONE);
						item.setText(name);							
						composite.layout(createServiceRootGroup(item));
						parent.getDataTypeTree().setSelection(item);
						parent.changeRightComposite(item);
					}
					else{
						OPRoSUtil.openMessageBox(OPRoSStrings.getString("DuplicationErrorMessage"),
								SWT.OK|SWT.ICON_WARNING);
					}
				}				
				else {
					item.setText(name);
					composite.layout(createMethodRootGroup(item));
					
				}
				item.setExpanded(true);		
			}
		});
		
		if(!isNew)
			text_ServiceTypeName.setText(item.getText());
		
		List<Control> list = new ArrayList<Control>();
		list.add(label);
		list.add(text_ServiceTypeName);
		list.add(btn_Previous);
		list.add(btn_Ok);
		
		return list.toArray(new Control[list.size()]);
	}
	
	/**
	 * �좎뙣�뚮뱶瑜��좎룞�쇿뜝�숈삕�좎떦紐뚯삕 �좎룞�쇿뜝�숈삕�좎룞���좎뙎琉꾩삕
	 * @return
	 */
	public Control[] createMethodRootGroup(final TreeItem item) {	
		Label label = createLabel("Data Call Names : " +
				"Names of functions or methods that are accessed between service ports.", 
				50, SWT.BORDER | SWT.NONE | SWT.CENTER);
		
		Button btn_AddMethod = getButtonInstance(composite, false, 1);
		btn_AddMethod.setText("Add Data");
		btn_AddMethod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				composite.layout(createParameterCreateGroup(item, true));
			}			
		});
		
		Button btn_Edit = getButtonInstance(composite, false, 1);
		btn_Edit.setText("Edit");
		btn_Edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				composite.layout(createServiceCreateGroup(item, false));
			}			
		});
		
		Button btn_Delete = getButtonInstance(composite, false, 1);
		btn_Delete.setText("Delete");
		btn_Delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem parentItem = item.getParentItem();
				deleteTreeItem(item);
				parent.getDataTypeTree().setSelection(parentItem);
				parent.changeRightComposite(parentItem);
			}			
		});
		
		List<Control> list = new ArrayList<Control>();
		list.add(label);
		list.add(btn_AddMethod);
		list.add(btn_Edit);
		list.add(btn_Delete);
		
		return list.toArray(new Control[list.size()]);
	}
	
	/**
	 * �좎뙣�뚮뱶瑜��좎룞�쇿뜝�숈삕�좎떦紐뚯삕 �좎룞�쇿뜝�숈삕�좎룞���좎뙎琉꾩삕
	 * @return
	 */
	public Control[] createMethodCreateGroup(final TreeItem item, final boolean isNew) {	
		Label label = createLabel("Service Name", 0, SWT.NONE);
		
		final Text text_ServiceTypeName = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		text_ServiceTypeName.setLayoutData(gd);
		
		Label label2 = createLabel("Return Data Type", 0, SWT.NONE);
		
		final Combo typeCombo = new Combo(composite, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		typeCombo.setLayoutData(gd);
		typeCombo.setItems(OPRoSUtil.dataNotBoostTypes);
		for(String type : parent.getTypeItem(true)){
			typeCombo.add(type, 0);
		}
		typeCombo.select(0);
		/*
		typeCombo.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				Combo combo = (Combo)e.getSource();
				String str = combo.getText();	
				
				if(!OPRoSUtil2.stringCheck(str)) {
					MessageDialog.openError(composite.getShell(), "Error", "Do not use special characters.");
					str = str.substring(0, str.length() -1);
					combo.setText(str);
					combo.setSelection(new Point(0, str.length()));
				}
			}
		});
		*/
		
		Label label3 = createLabel("Call Type", 0, SWT.NONE);
		final Combo callTypeCombo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		callTypeCombo.setLayoutData(gd);		
		
		typeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				String strDataType = ((Combo)e.getSource()).getText();
				if(strDataType.compareTo("void")==0){
					if(callTypeCombo.getItemCount()==1){
						callTypeCombo.add("nonblocking");
					}
				}else{
					if(callTypeCombo.getItemCount()==2){
						callTypeCombo.remove(1);
					}
				}
			}			
		});
				
		Button btn_Previous = getButtonInstance(composite, true, 1);
		btn_Previous.setText("Previous");
		btn_Previous.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				if(isNew)
					composite.layout(createMethodRootGroup(item));
				else 
					composite.layout(createParameterRootGroup(item));
			}
			
		});
		
		Button btn_Ok = getButtonInstance(composite, true, 1);
		btn_Ok.setText("OK");
		btn_Ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = text_ServiceTypeName.getText()+"():"+typeCombo.getText()+":"+callTypeCombo.getText();
				disposeWiget(composite);
				if(isNew) {
					TreeItem newItem = new TreeItem(item, SWT.NONE);
					newItem.setText(name);
					composite.layout(createMethodRootGroup(item));
					parent.getDataTypeTree().setSelection(newItem);
					parent.changeRightComposite(newItem);
				}
				else {
					item.setText(name);
					composite.layout(createParameterRootGroup(item));
				}
				item.setExpanded(true);	
			}
		});
		
		
		
		if(!isNew) {
			String[] str = item.getText().split(":");
			text_ServiceTypeName.setText(str[0].replace("()", ""));
			for(int i=0; i<typeCombo.getItems().length; i++) {
				String typeName = typeCombo.getItems()[i];
				if(typeName.equals(str[1])){
					typeCombo.select(i);
					break;
				}
			}			
		}
		
		if(typeCombo.getText().equals("void"))
			callTypeCombo.setItems(new String[]{"blocking","nonblocking"});
		else
			callTypeCombo.setItems(new String[]{"blocking"});
		callTypeCombo.select(0);
		
		if(!isNew) {
			String[] str = item.getText().split(":");
			for(int i=0; i<callTypeCombo.getItems().length; i++) {
				String callType = callTypeCombo.getItems()[i];
				if(callType.equals(str[2])){
					callTypeCombo.select(i);
					break;
				}
			}
		}
		
		List<Control> list = new ArrayList<Control>();
		list.add(label);
		list.add(text_ServiceTypeName);
		list.add(label2);
		list.add(typeCombo);
		list.add(label3);
		list.add(callTypeCombo);
		
		return list.toArray(new Control[list.size()]);
	}
	
	/**
	 * �좎뙣�뚮뱶瑜��좎룞�쇿뜝�숈삕�좎떦紐뚯삕 �좎룞�쇿뜝�숈삕�좎룞���좎뙎琉꾩삕
	 * @return
	 */
	public Control[] createParameterRootGroup(final TreeItem item) {	
		Label label = createLabel("Parameter Names of Service Call : " +
				"Parameters of the function or the method.", 50, SWT.BORDER | SWT.NONE | SWT.CENTER);
		
		Button btn_AddMethod = getButtonInstance(composite, false, 1);
		//btn_AddMethod.setText("Add method\nParameter");
		btn_AddMethod.setText("Add Parameter");
		btn_AddMethod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				composite.layout(createParameterCreateGroup(item, true));
			}			
		});
		
		Button btn_Edit = getButtonInstance(composite, false, 1);
		btn_Edit.setText("Edit");
		btn_Edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				composite.layout(createMethodCreateGroup(item, false));
			}			
		});
		
		Button btn_Delete = getButtonInstance(composite, false, 1);
		btn_Delete.setText("Delete");
		btn_Delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem parentItem = item.getParentItem();
				deleteTreeItem(item);
				parent.getDataTypeTree().setSelection(parentItem);
				parent.changeRightComposite(parentItem);
			}			
		});
		
		List<Control> list = new ArrayList<Control>();
		list.add(label);
		list.add(btn_AddMethod);
		list.add(btn_Edit);
		list.add(btn_Delete);
		
		return list.toArray(new Control[list.size()]);
	}	
	
	/**
	 * �좎뙣�뚮뱶瑜��좎룞�쇿뜝�숈삕�좎떦紐뚯삕 �좎룞�쇿뜝�숈삕�좎룞���좎뙎琉꾩삕
	 * @return
	 */
	public Control[] createParameterCreateGroup(final TreeItem item, final boolean isNew) {	
		Label label = createLabel("Data Name", 0, SWT.NONE);
		
		final Text text_ServiceTypeName = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		text_ServiceTypeName.setLayoutData(gd);
		
		Label label2 = createLabel("Data Type", 0, SWT.NONE);
		final Combo typeCombo = new Combo(composite, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		typeCombo.setLayoutData(gd);
		typeCombo.setItems(OPRoSUtil.dataNotBoostTypes);
		for(String type : parent.getTypeItem(false)){
			typeCombo.add(type, 0);
		}
		typeCombo.select(0);
		/*
		typeCombo.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				Combo combo = (Combo)e.getSource();
				String str = combo.getText();				
				if(!OPRoSUtil2.stringCheck(str)) {
					MessageDialog.openError(composite.getShell(), "Error", "Do not use special characters.");
					str = str.substring(0, str.length() -1);
					combo.setText(str);
					combo.setSelection(new Point(0, str.length()));
				}
			}
		});
		*/
		
		Button btn_Previous = getButtonInstance(composite, true, 1);
		btn_Previous.setText("Previous");
		btn_Previous.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				if(isNew)
					composite.layout(createMethodRootGroup(item));
				else
					composite.layout(createParameterGroup(item));
			}			
		});
		
		Button btn_Ok = getButtonInstance(composite, true, 1);
		btn_Ok.setText("OK");
		btn_Ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = text_ServiceTypeName.getText()+":"+typeCombo.getText();
				disposeWiget(composite);
				if(isNew) {
					TreeItem newItem = new TreeItem(item, SWT.NONE);
					newItem.setText(name);
					composite.layout(createParameterRootGroup(item));
					parent.getDataTypeTree().setSelection(newItem);
					parent.changeRightComposite(newItem);
				}
				else {
					item.setText(name);
					composite.layout(createParameterGroup(item));
				}
				item.setExpanded(true);
			}			
		});
		
		
		if(!isNew) {
			String[] str = item.getText().split(":");
			text_ServiceTypeName.setText(str[0]);
			for(int i=0; i<typeCombo.getItems().length; i++) {
				String typeName = typeCombo.getItems()[i];
				if(typeName.equals(str[1])){
					typeCombo.select(i);
					break;
				}
			}
		}	
		
		List<Control> list = new ArrayList<Control>();
		list.add(label);
		list.add(text_ServiceTypeName);
		list.add(label2);
		list.add(typeCombo);
		
		return list.toArray(new Control[list.size()]);
	}
	
	/**
	 * �좎뙣�뚮뱶瑜��좎룞�쇿뜝�숈삕�좎떦紐뚯삕 �좎룞�쇿뜝�숈삕�좎룞���좎뙎琉꾩삕
	 * @return
	 */
	public Control[] createParameterGroup(final TreeItem item) {	
		Label label = createLabel("Parameter of the function or the method.", 
				50, SWT.BORDER | SWT.NONE | SWT.CENTER);
		
		Button btn_Edit = getButtonInstance(composite, false, 1);
		btn_Edit.setText("Edit");
		btn_Edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeWiget(composite);
				composite.layout(createParameterCreateGroup(item, false));
			}			
		});
		
		Button btn_Delete = getButtonInstance(composite, false, 1);
		btn_Delete.setText("Delete");
		btn_Delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem parentItem = item.getParentItem();
				deleteTreeItem(item);
				parent.getDataTypeTree().setSelection(parentItem);
				parent.changeRightComposite(parentItem);
			}			
		});
		
		List<Control> list = new ArrayList<Control>();
		list.add(label);
		list.add(btn_Edit);
		list.add(btn_Delete);
		
		return list.toArray(new Control[list.size()]);
	}	
	
	/**
	 * �좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�쇰뙋��
	 * @param text
	 * @param height
	 * @param style
	 * @return
	 */
	private Label createLabel(String text, int height, int style){
		Label label = new Label(composite, style);
		label.setText(text);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd.widthHint = 250;
		if(height != 0)
			gd.heightHint = height;
		label.setLayoutData(gd);
		return label;
	}
	
	
	
	/**
	 * �좎룞�숉듉 �좎떥�숈삕�좎떦�숈삕�좎룞���좎룞�쇿뜝�숈삕�좎떬�먯삕.
	 * @param parent
	 * @param isBottom
	 * @return
	 */
	public Button getButtonInstance(Composite composite, boolean isBottom, int index) {
		Button button = new Button(composite, SWT.PUSH | SWT.CENTER | SWT.MULTI);
		GridData gd = new GridData(GridData.END, SWT.BOTTOM, true, isBottom, index, 5);
		gd.widthHint = 120;
		//gd.heightHint = 40;
		button.setLayoutData(gd);
		return button;
	}
	
	/**
	 * �좎룞�쇿뜝�쒕벝���멨뜝�숈삕�좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떬�먯삕.
	 * @param item
	 */
	public void deleteTreeItem(TreeItem item) {
		if(item != null){
			if(!item.equals(parent.rootItem)){
				item.removeAll();
				item.dispose();
			}
			else{
				item.removeAll();
			}
		}
	}
	
	/**
	 * �좎뙏�먯삕 �멨뜝�숈삕�좎룞���좎뙥怨ㅼ삕�좎떦琉꾩삕�좎룞���좎떛紐뚯삕�좎룞���멨뜝�숈삕�좎룞���좎룞�쇿뜝�숈삕�좎떦�먯삕�좎룞���좎떙�쇱삕
	 * @param parent
	 * @param itemName
	 * @return
	 */
	public boolean isExistItem(TreeItem parent, String itemName){
		int cnt = parent.getItemCount();
		for(int i=0;i<cnt;i++){
			if(parent.getItem(i).getText().compareTo(itemName)==0)
				return true;
		}
		return false;
	}
	
	/**
	 * �좎룞�쇿뜝�숈삕�좎룞���좎룞�쇿뜝�됰벝���좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎룞�쇿뜝�숈삕 �좎룞�쇿뜝�숈삕�좎떬�먯삕.
	 * @param parent
	 */
	public void disposeWiget(Composite parent){
		for(Control control : parent.getChildren()) {
			control.dispose();
		}
	}
	
}
