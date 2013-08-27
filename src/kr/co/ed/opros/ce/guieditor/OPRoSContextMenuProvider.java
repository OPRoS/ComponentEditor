package kr.co.ed.opros.ce.guieditor;

import kr.co.ed.opros.ce.guieditor.actions.OPRoSAddElementAction;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSComponentElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypesElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertiesElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceProvidedPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceRequiredPortElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypesElementPart;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

public class OPRoSContextMenuProvider extends
		ContextMenuProvider {

	private ActionRegistry actionRegistry;
	
	public OPRoSContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		addEditDomainContribution(menu);
		menu.add(new Separator());
		if(getViewer().getSelectedEditParts() != null &&
				getViewer().getSelectedEditParts().size() != 0 && 
				getViewer().getSelectedEditParts().get(0)!= null){
			
			if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSComponentElementPart){
				addAddDomainContribution(menu);
				menu.add(new Separator());
				addAddInfoDomainContribution(menu);
				addAddPropertyDomainContribution(menu);
				addAddDataTypeDomainContribution(menu);
				addAddServiceTypeDomainContribution(menu);
				addAddMonitoringVariableDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSExeEnvironmentElementPart){
				addAddInfoDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSPropertiesElementPart){
				addAddPropertyDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSDataTypesElementPart){
				addAddDataTypeDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSServiceTypesElementPart){
				addAddServiceTypeDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSServiceProvidedPortElementPart
					||getViewer().getSelectedEditParts().get(0) instanceof OPRoSServiceRequiredPortElementPart){
				addNewServicePortTestDomainContribution(menu);
			}
		}
	}

	public ActionRegistry getActionRegistry(){
		return actionRegistry;
	}
	public void setActionRegistry(ActionRegistry registry){
		this.actionRegistry = registry;
	}
	
	private void addEditDomainContribution(IMenuManager menu){
		IAction action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		
		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		
		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
		action = getActionRegistry().getAction(GEFActionConstants.ZOOM_IN);
		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
		
		action = getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT);
		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
	}
	
	private void addAddDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_SERVICE_REQUIRED_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_SERVICE_PROVIDED_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_DATA_OUT_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_DATA_IN_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_EVENT_OUT_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_EVENT_IN_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		
	}
	
	private void addAddInfoDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_OS);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_CPU);
		if(action!=null&&action.isEnabled())
			menu.add(action);
	}
	
	private void addAddPropertyDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_PROPERTY);
		if(action!=null&&action.isEnabled())
			menu.add(action);
	}
	
	private void addAddDataTypeDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_DATA_TYPE);
		if(action!=null&&action.isEnabled())
			menu.add(action);
	}
	
	private void addAddServiceTypeDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_SERVICE_TYPE);
		if(action!=null&&action.isEnabled())
			menu.add(action);
	}
	
	private void addAddMonitoringVariableDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_MONITORING_VARIABLE);
		if(action!=null&&action.isEnabled())
			menu.add(action);
	}
	
	
	private void addNewServicePortTestDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.NEW_SERVICE_PORT_TEST);
		if(action!=null&&action.isEnabled())
			menu.add(action);
	}
}
