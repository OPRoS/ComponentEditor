package kr.co.ed.opros.ce.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSUtil2;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.provider.ComponentListContentProvider;
import kr.co.ed.opros.ce.provider.ComponentListLabelProvider;
import kr.co.ed.opros.ce.wizards.OPRoSGUIWizardPage;
import kr.co.ed.opros.ce.wizards.WizardMessages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class OPRoSGUIOverviewComposite extends Composite {
	protected Text compNameText;
	protected OPRoSComponentElementModel compModel;
	public Combo comboMainCategory;
	public Combo comboMiddleCategory;
	public CheckboxTableViewer componentListViewer;
	public File selectedComponent;
	public OPRoSGUIWizardPage page;
	public Button btn_UseTemplate;	
	
	public static final String USER_COMPONENT_LOCATION = "C:\\test\\";
	
	
	public OPRoSGUIOverviewComposite(Composite parent, int style) {
		super(parent, style);
	}
	public OPRoSGUIOverviewComposite(Composite parent, int style, int column, int gridStyle, OPRoSGUIWizardPage page) {
		super(parent, style);
		this.setFont(parent.getFont());
        GridLayout layout = new GridLayout();
        layout.numColumns = column;
        this.setLayout(layout);
        this.setLayoutData(new GridData(gridStyle));
        this.page = page;

        createComponentGroup(this);
		//loadData();
//		compNameText.addVerifyListener(
//        		new VerifyListener(){
//        			public void verifyText(VerifyEvent e){
//        				if(e.character!=8){
//	        				if(e.start==0)
//	        					e.doit=e.text.matches("[a-zA-Z]|_");
//	        				else
//	        					e.doit=e.text.matches("[a-zA-Z]|_|[0-9]");
//        				}
//        			}
//        		});
		compNameText.addVerifyListener(
				new VerifyListener(){
					public void verifyText(VerifyEvent e){
						if(e.text.length()>0){
							if(e.text.length()>1){
			    				if(e.start==0){
			    					e.doit=e.text.substring(0,1).matches("[a-zA-Z]|_");
			    					if(e.doit){
			    						e.text.substring(1).matches("[a-zA-Z]|_|[0-9]");
			    					}
			    				}
			    				else{
			    					e.doit=e.text.matches("[a-zA-Z]|_|[0-9]");
			    				}
							}else{
								if(e.start==0){
			    					e.doit=e.text.matches("[a-zA-Z]|_");
			    				}
			    				else{
			    					e.doit=e.text.matches("[a-zA-Z]|_|[0-9]");
			    				}
							}
		    				
						}
					}
				});
	}
	
	protected void createComponentGroup(Composite parent){
		
		Label label = new Label(parent, SWT.NULL);
		GridData gd = new GridData();
		label.setLayoutData(gd);
		label.setText(WizardMessages.getString("NewCompWizardPage.ComponentGroup.NameLabel"));
		
		compNameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		compNameText.setLayoutData(gd);
		
		label = new Label(parent, SWT.NULL);
		
		btn_UseTemplate = new Button(parent, SWT.CHECK);
		btn_UseTemplate.setText("Use component template");
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		btn_UseTemplate.setLayoutData(gd);
		setCheckBoxListener(btn_UseTemplate);
		
		
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		group.setLayout(new GridLayout(2, false));		
		
		//component : main catagory
		comboMainCategory = new Combo(group, SWT.READ_ONLY);
		comboMainCategory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//component : middle catagory
		comboMiddleCategory = new Combo(group, SWT.READ_ONLY);
		comboMiddleCategory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		setComboListener(comboMainCategory);
		setComboListener(comboMiddleCategory);
		
		componentListViewer = CheckboxTableViewer.newCheckList(group, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(componentListViewer.getControl());
		componentListViewer.setContentProvider(new ComponentListContentProvider());
		componentListViewer.setLabelProvider(new ComponentListLabelProvider());
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd.heightHint = 200;
		componentListViewer.getTable().setLayoutData(gd);
        setAddSelectionLister(componentListViewer);
		
		initialize();
	}
	
	public void initialize() {
		disableAllCombo();
	}
	
	public void disableAllCombo() {
		
		comboMainCategory.select(0);
		comboMiddleCategory.select(0);
		
		comboMainCategory.removeAll();
		comboMiddleCategory.removeAll();
		componentListViewer.setInput(null);	
		
		comboMainCategory.setEnabled(false);
		comboMiddleCategory.setEnabled(false);
		componentListViewer.getTable().setEnabled(false);
	}
	/*
	public void loadData(){
		setCompName(page.compModel.getComponentName());
	}
	*/
	public Text getCompNameText() {
		return compNameText;
	}
	
	public String getCompName() {
		return compNameText.getText();
	}
	/*
	public void setCompName(String compName) {
		this.compNameText.setText(compName);
	}
	*/
	
	/**
	 * 체크박스 테이블 뷰어에 체크 셀렉션 리스너를 등록
	 * @param viewer
	 */
	private void setAddSelectionLister(final CheckboxTableViewer viewer) {
		viewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent Event) {
				itemStateChaged(Event, viewer);
			}
		});
	}
	
	/**
	 * 체크박스 테이블을 싱글 셀렉션이 되게하며, 선택된 아이템을 저장한다.
	 * @param event
	 */
	private void itemStateChaged(CheckStateChangedEvent event, CheckboxTableViewer viewer) {		
		boolean singleSelection = true;		
		if (singleSelection) {
			viewer.setCheckedElements(new Object[0]);
		}
		if (event.getChecked()) {
			viewer.setChecked(event.getElement(), true);
			selectedComponent = (File)event.getElement();
		}
		else{
			selectedComponent = null;
		}
		page.dialogChange();
	}	
	
	
	/**
	 * 콤보박스에 리스너 등록
	 * @param combo
	 */
	public void setComboListener(Combo combo) {
		combo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent evt) {			
				Combo c = (Combo)evt.widget;
				if(c == comboMainCategory) {
					String path = OPRoSUtil2.getComponentTemplatePath()+ "\\" + comboMainCategory.getText();
					if(comboMainCategory.getSelectionIndex() == 1)
						path = USER_COMPONENT_LOCATION;
							
					setComboItem(comboMiddleCategory, new File(path));
					componentListViewer.setInput(null);
					
					
				}
				else if (c == comboMiddleCategory) {
					File file = new File(OPRoSUtil2.getComponentTemplatePath()
							+ "\\" + comboMainCategory.getText() + "\\"
							+ comboMiddleCategory.getText());
					
					if(comboMainCategory.getSelectionIndex() == 1)
						file = new File(USER_COMPONENT_LOCATION + comboMiddleCategory.getText());
					
					componentListViewer.setInput(file);
					componentListViewer.getTable().setEnabled(true);
				}
				page.dialogChange();
			}
		});
	}
	
	/**
	 * 체크박스 버튼에 리스너 등록
	 * @param button
	 */
	public void setCheckBoxListener(Button button){
		
		button.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
		        if(b.getSelection()) {
		        	setComboItem(comboMainCategory,  new File(OPRoSUtil2.getComponentTemplatePath()));		        	
		        } else {
		        	disableAllCombo();
		        }
		        page.dialogChange();
		    }	
			
		});
	}
	
	/**
	 * 콤보박스에 아이템을 넣는다.
	 * @param combo
	 * @param file
	 */
	public void setComboItem(Combo combo, File file) {
		if(file.isDirectory()) {			 
			List<String> str = new ArrayList<String>(); 
			if(combo == comboMainCategory) {
				str.add("컴포넌트 대분류");
				str.add("User definition component");
			}
			else if(combo == comboMiddleCategory) {
				str.add("컴포넌트 중분류");
			}
			
    		File[] files = file.listFiles();
    		for(int i=0; i<files.length; i++) {
    			if(files[i].isDirectory())
    				str.add(files[i].getName());
    		}
    		
    		combo.setItems(str.toArray(new String[str.size()]));
    		combo.select(0);
    		combo.setEnabled(true);	   
    	}
	}
	
	
	public boolean isUseTemplate() {
		return btn_UseTemplate.getSelection();
	}
	
	public Combo getComboMainCategory() {
		return comboMainCategory;
	}
	
	public Combo getComboMiddleCategory() {
		return comboMiddleCategory;
	}
	
	/**
	 * 선택된 컴포넌트 리턴.
	 * @return
	 */
	public File getSelectedComponent() {
		return selectedComponent;
	}
	
}
