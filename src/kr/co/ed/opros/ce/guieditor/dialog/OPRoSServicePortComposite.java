package kr.co.ed.opros.ce.guieditor.dialog;

import java.util.HashMap;
import java.util.Iterator;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;
import kr.co.ed.opros.ce.wizards.WizardMessages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.jdom.Document;
import org.jdom.Element;

public class OPRoSServicePortComposite extends Composite {
	
	protected Text servicePortNameText;
	protected Combo servicePortTypeCombo;
	protected Text servicePortUsageText;
	protected Text servicePortReferenceText;
	protected Text servicePortDescriptionText;
	protected List serviceTypeList;
	protected Label messageLabel;
	protected HashMap<String,Document> map;
	protected boolean isProvided=false;
	protected Button serviceNewReferenceButton;
	protected OPRoSElementBaseModel modifyModel=null;
	protected OPRoSComponentElementModel compEle=null;	
	private static final String[] serviceTypes = {OPRoSStrings.getString("ServiceTypeDefaultValue")};
	
	public OPRoSServicePortComposite(Composite parent, int style, int column, int gridStyle, 
			boolean isProvided,OPRoSElementBaseModel model,OPRoSComponentElementModel compEle) {
		super(parent, style);
		this.modifyModel=model;
		this.compEle=compEle;
		this.isProvided=isProvided;
		map=new HashMap<String,Document>();
		this.setFont(parent.getFont());
        GridLayout layout = new GridLayout();
        layout.numColumns = column;
        this.setLayout(layout);
        this.setLayoutData(new GridData(gridStyle));
        createMethodPortsGroup(this);
	}
	
	protected void createMethodPortsGroup(Composite parent){
		Group compGroup=null;
		if(isProvided)
			compGroup = OPRoSUtil.createGroup(parent, SWT.NONE, OPRoSStrings.getString("ServiceProvidedPortTitle"),
				8, GridData.BEGINNING);
		else
			compGroup = OPRoSUtil.createGroup(parent, SWT.NONE, OPRoSStrings.getString("ServiceRequiredPortTitle"),
					8, GridData.BEGINNING);
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("ServicePortNameLabel"),
        		SWT.NONE,2,5,1,0,GridData.BEGINNING);
        servicePortNameText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,2,0,1,0,0,0,GridData.BEGINNING);
        servicePortNameText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent evt) {
				if(modifyModel==null){
					if(OPRoSUtil.isDuplicatePortName(servicePortNameText.getText(),false,compEle)){
						messageLabel.setText(OPRoSStrings.getString("PortNameDuplicateError"));
					}else{
						messageLabel.setText("");
					}
				}else{
					if(OPRoSUtil.isDuplicatePortName(servicePortNameText.getText(),true,compEle)){
						messageLabel.setText(OPRoSStrings.getString("PortNameDuplicateError"));
					}else{
						messageLabel.setText("");
					}
				}
			}
        });
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("ServicePortTypeLabel"),
        		SWT.NONE,2,5,1,0,GridData.BEGINNING);
        servicePortTypeCombo =OPRoSUtil.createCombo(compGroup, SWT.READ_ONLY|SWT.SINGLE, serviceTypes,0,2,0,1,0,0,0,GridData.BEGINNING);
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("ServicePortUsageLabel"),
        		SWT.NONE,2,5,1,0,GridData.BEGINNING);
        servicePortUsageText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,2,0,1,0,120,0,GridData.BEGINNING);
        servicePortUsageText.setEditable(false);
        if(isProvided){
        	servicePortUsageText.setText(OPRoSStrings.getString("ServicePortUsage0"));
        }else{
        	servicePortUsageText.setText(OPRoSStrings.getString("ServicePortUsage1"));
        }
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("ServicePortReferLabel"),
        		SWT.NONE,2,5,1,0,GridData.BEGINNING);
        servicePortReferenceText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,2,0,1,0,100,0,GridData.BEGINNING);
        servicePortReferenceText.setEditable(false);
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("ServicePortDesciptLabel"),
        		SWT.NONE,2,5,1,0,GridData.BEGINNING);
        
        servicePortDescriptionText = OPRoSUtil.createText(compGroup,SWT.BORDER|SWT.SINGLE,6,0,1,0,340,0,GridData.BEGINNING);
        OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("ServiceTypeListLabel"),
        		SWT.NONE,5,5,1,5,GridData.BEGINNING);
        
        //serviceNewReferenceButton = OPRoSUtil.createButton(compGroup,SWT.NONE,WizardMessages.getString("PortsWizardPage.MethodPorts.NewReferButtonText"),
        //		3,50,1,0,148,20,GridData.BEGINNING);
        
        serviceNewReferenceButton = new Button(compGroup, SWT.NONE);
        serviceNewReferenceButton.setText(WizardMessages.getString("PortsWizardPage.MethodPorts.NewReferButtonText"));
        GridData gd = new GridData(SWT.FILL, SWT.RIGHT, true, false, 3, 1);
        gd.heightHint = 22;
        serviceNewReferenceButton.setLayoutData(gd);
        
        serviceNewReferenceButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				OPRoSServiceTypeInputDialog dlg = new OPRoSServiceTypeInputDialog(null,compEle);
				dlg.open();
				if(dlg.getReturnCode()==InputDialog.OK){
					if(compEle!=null){
						OPRoSElementBaseModel servTypesModel = compEle.getServiceTypesModel();
						if(servTypesModel!=null){
							OPRoSServiceTypeElementModel element = new OPRoSServiceTypeElementModel();
							element.setServiceTypeFileName(dlg.getServiceTypeFileName());
							element.setServiceTypeDoc(dlg.getServiceTypeDoc());
							servTypesModel.addChild(element);
							serviceTypeList.add(element.getServiceTypeFileName());
							map.put(element.getServiceTypeFileName(), element.getServiceTypeDoc());
						}
					}
				}
			}
		});
        serviceTypeList = OPRoSUtil.createList(compGroup,SWT.SINGLE|SWT.BORDER,8,5,1,0,430,100,GridData.BEGINNING);
        if(compEle!=null){
        	serviceTypeList.removeAll();
        	Iterator<OPRoSElementBaseModel> it = compEle.getServiceTypesModel().getChildrenList().iterator();
        	while(it.hasNext()){
        		OPRoSServiceTypeElementModel serviceModel=(OPRoSServiceTypeElementModel)it.next();
        		serviceTypeList.add(serviceModel.getServiceTypeFileName());
        		map.put(serviceModel.getServiceTypeFileName(), serviceModel.getServiceTypeDoc());
        	}
        }
        serviceTypeList.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e) {
				int index = serviceTypeList.getSelectionIndex();
				if(index > -1) {
					servicePortReferenceText.setText(serviceTypeList.getItem(index));
					if(map.containsKey(serviceTypeList.getItem(index))){
						Document doc = map.get(serviceTypeList.getItem(index));
						Iterator<Element> eles;
						Element ele;
						eles = doc.getRootElement().getChildren(OPRoSStrings.getString("ServiceTypeEle")).iterator();
						servicePortTypeCombo.removeAll();
						while(eles.hasNext()){
							ele = eles.next();
							servicePortTypeCombo.add(ele.getChildTextTrim("type_name"));
							servicePortTypeCombo.select(0);
						}
					}
				}
				
			}
		});
        serviceTypeList.addMouseListener(new MouseAdapter(){
        	public void mouseDoubleClick(MouseEvent e){
        		int index = serviceTypeList.getSelectionIndex();
        		OPRoSServiceTypeInputDialog dlg = 
        			new OPRoSServiceTypeInputDialog(getShell(),serviceTypeList.getItem(index),map.get(serviceTypeList.getItem(index)),false);
        		dlg.open();
        	}
        });
        messageLabel =  OPRoSUtil.createLabel(compGroup, "                                        ",
        		SWT.NONE,8,5,1,0,GridData.BEGINNING);
        messageLabel.setForeground(ColorConstants.red);
        Label label = OPRoSUtil.createLabel(compGroup, OPRoSStrings.getString("NotifyNeedSourceModify"),
        		SWT.NONE,8,5,1,5,GridData.BEGINNING);
        label.setForeground(ColorConstants.blue);
        if(modifyModel!=null){
        	OPRoSPortElementBaseModel element;
       		element = (OPRoSPortElementBaseModel)modifyModel;
        	servicePortNameText.setText(element.getName());
        	servicePortUsageText.setText(element.getUsage());
        	int index;
        	if(isProvided)
        		index=serviceTypeList.indexOf(((OPRoSServiceProvidedPortElementModel)element).getReference());
        	else
        		index=serviceTypeList.indexOf(((OPRoSServiceRequiredPortElementModel)element).getReference());
        	if(index>=0){
	        	serviceTypeList.select(index);
	        	servicePortReferenceText.setText(serviceTypeList.getItem(index));
				if(map.containsKey(serviceTypeList.getItem(index))){
					Document doc = map.get(serviceTypeList.getItem(index));
					Iterator<Element> eles;
					Element ele;
					eles = doc.getRootElement().getChildren(OPRoSStrings.getString("ServiceTypeEle")).iterator();
					servicePortTypeCombo.removeAll();
					while(eles.hasNext()){
						ele = eles.next();
						servicePortTypeCombo.add(ele.getChildTextTrim("type_name"));
					}
					servicePortTypeCombo.select(servicePortTypeCombo.indexOf(element.getType()));
				}
	        	servicePortDescriptionText.setText(element.getDescription());
        	}
        }
	}
	
	public String getPortName(){
		return servicePortNameText.getText();
	}
	
	public String getPortType(){
		return servicePortTypeCombo.getText();
	}
	
	public String getPortRefer(){
		return servicePortReferenceText.getText();
	}
	
	public String getPortDescript(){
		return servicePortDescriptionText.getText();
	}
	
	public HashMap<String,Document> getServiceTypeMap(){
		return map;
	}
	
}
