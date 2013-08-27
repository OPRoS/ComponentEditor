package kr.co.ed.opros.ce.guieditor;

import kr.co.ed.opros.ce.guieditor.actions.OPRoSAddElementAction;
import kr.co.ed.opros.ce.guieditor.editpart.MonitoringVariablesTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSComponentTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSDataTypesTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSExeEnvironmentTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPropertiesTreeEditPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSServiceTypesTreeEditPart;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

public class OPRoSOutlineContextMenuProvider extends
		OPRoSContextMenuProvider {

	public OPRoSOutlineContextMenuProvider(
			EditPartViewer viewer, ActionRegistry registry) {
		super(viewer, registry);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		
		IAction action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		if(getViewer().getSelectedEditParts().get(0)!=null){
			if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSComponentTreeEditPart){
				addAddDomainContribution(menu);
				menu.add(new Separator());
				addAddInfoDomainContribution(menu);
				addAddPropertyDomainContribution(menu);
				addAddDataTypeDomainContribution(menu);
				addAddServiceTypeDomainContribution(menu);
				addAddMonitoringVariableDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSExeEnvironmentTreeEditPart){
				addAddInfoDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSPropertiesTreeEditPart){
				addAddPropertyDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSDataTypesTreeEditPart){
				addAddDataTypeDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof OPRoSServiceTypesTreeEditPart){
				addAddServiceTypeDomainContribution(menu);
			}else if(getViewer().getSelectedEditParts().get(0) instanceof MonitoringVariablesTreeEditPart){
				addAddMonitoringVariableDomainContribution(menu);
			}
		}
		
//		super.buildContextMenu(menu);
	}
	private void addAddDomainContribution(IMenuManager menu){
		IAction action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_SERVICE_PROVIDED_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_SERVICE_REQUIRED_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_DATA_IN_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_DATA_OUT_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_EVENT_IN_PORT);
		if(action!=null&&action.isEnabled())
			menu.add(action);
		action= getActionRegistry().getAction(OPRoSAddElementAction.ADD_EVENT_OUT_PORT);
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
}
