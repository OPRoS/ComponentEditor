package kr.co.ed.opros.ce.guieditor.policy;

import kr.co.ed.opros.ce.guieditor.command.ElementMoveCommand;
import kr.co.ed.opros.ce.guieditor.command.MonitoringVariableCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSDataTypeElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSExeEnvironmentCPUElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSExeEnvironmentOSElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSPropertyElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSServiceTypeElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariablesModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypesElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertiesElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypesElementModel;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;


/**
 * ÄÁÅ×ÀÌ³Ê Á¤Ã¥
 * @author lee6607
 *
 */
public class NodeContainerEditPolicy extends FlowLayoutEditPolicy {
	
	/**
	 * ¸ðµ¨ »ý¼º Ä¿¸àµå
	 */
	@Override
	protected Command getCreateCommand(CreateRequest arg0) {	
		OPRoSElementBaseModel childModel = ((OPRoSElementBaseModel)arg0.getNewObject());
		OPRoSElementBaseModel parentModel = ((OPRoSElementBaseModel)getHost().getModel());	
		
		int index = getHost().getChildren().indexOf(getInsertionReference(arg0));
		
		
		if(childModel instanceof OPRoSServiceTypeElementModel
				&& parentModel instanceof OPRoSServiceTypesElementModel) {	
			
			OPRoSServiceTypeElementCreateCommand command = new OPRoSServiceTypeElementCreateCommand(index);			
			command.setParent(parentModel.getParent());
			command.setElement(childModel);
			return command;
		}
		else if(childModel instanceof OPRoSExeEnvironmentCPUElementModel
				&& parentModel instanceof OPRoSExeEnvironmentElementModel) {	
			
			OPRoSExeEnvironmentCPUElementCreateCommand command = new OPRoSExeEnvironmentCPUElementCreateCommand(index);
			command.setParent(parentModel.getParent());
			command.setElement(childModel);
			return command;
		}
		
		else if(childModel instanceof OPRoSExeEnvironmentOSElementModel
				&& parentModel instanceof OPRoSExeEnvironmentElementModel) {	
			OPRoSExeEnvironmentOSElementCreateCommand command = new OPRoSExeEnvironmentOSElementCreateCommand(index);
			command.setParent(parentModel.getParent());
			command.setElement(childModel);
			return command;
		}
		
		else if(childModel instanceof OPRoSPropertyElementModel
				&& parentModel instanceof OPRoSPropertiesElementModel) {	
			
			OPRoSPropertyElementCreateCommand command = new OPRoSPropertyElementCreateCommand(index);
			command.setParent(parentModel.getParent());
			command.setElement(childModel);
			return command;
		}
		else if(childModel instanceof OPRoSDataTypeElementModel
				&& parentModel instanceof OPRoSDataTypesElementModel) {	
			
			OPRoSDataTypeElementCreateCommand command = new OPRoSDataTypeElementCreateCommand(index);
			command.setParent(parentModel.getParent());
			command.setElement(childModel);
			return command;
		}
		
		else if(childModel instanceof MonitoringVariableModel
				&& parentModel instanceof MonitoringVariablesModel) {
			MonitoringVariableCreateCommand command = new MonitoringVariableCreateCommand(index);
			command.setParent(parentModel.getParent());
			command.setElement(childModel);
			return command;
		}
		
		
		return null;
	}

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		/*
		if (child.getModel() instanceof CharonModel) {
			CharonModel member = (CharonModel) child.getModel();
			ComponentModel newCompartment = (ComponentModel) getHost().getModel();
			ComponentModel oldCompartment = (ComponentModel) child.getParent().getModel();
			if (after != null) {
				return new ElementAddCommand(member, newCompartment,
						oldCompartment, (ComponentModel) after.getModel());
			}
			return new ElementAddCommand(member, newCompartment,
					oldCompartment, null);
		}
		*/
		return null;
	}
	
	/**
	 * ¸ðµ¨ ÀÌµ¿Ä¿¸àµå
	 */
	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		
		if (after != null) {
			return new ElementMoveCommand(
					(OPRoSElementBaseModel) child.getModel(),
					(OPRoSElementBaseModel) getHost().getModel(),
					(OPRoSElementBaseModel) after.getModel());
		}
		return new ElementMoveCommand((OPRoSElementBaseModel) child.getModel(),
				(OPRoSElementBaseModel) getHost().getModel(), null);
	}
	
	@Override
	protected boolean isHorizontal() {
		IFigure figure = ((GraphicalEditPart) getHost()).getContentPane();
		LayoutManager layout = figure.getLayoutManager();
		if (layout instanceof FlowLayout)
			return ((FlowLayout) figure.getLayoutManager()).isHorizontal();
		if (layout instanceof ToolbarLayout)
			return ((ToolbarLayout) figure.getLayoutManager()).isHorizontal();
		return false;
	}
	
}
