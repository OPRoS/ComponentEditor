package kr.co.ed.opros.ce.guieditor.policy;

import kr.co.ed.opros.ce.guieditor.command.MonitoringVariableCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSDataTypeElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSElementChangeLayoutCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSExeEnvironmentCPUElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSExeEnvironmentOSElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSPortCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSPropertyElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.command.OPRoSServiceTypeElementCreateCommand;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSBodyElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSComponentElementPart;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSElementPartBase;
import kr.co.ed.opros.ce.guieditor.editpart.OPRoSPortBasePart;
import kr.co.ed.opros.ce.guieditor.figure.ILabeledFigure;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSComponentElementFigure;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSComponentElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class OPRoSElementEditLayoutPolicy extends XYLayoutEditPolicy {
	
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof OPRoSPortBasePart) {
			return new NonResizableEditPolicyEx();
		} else {
			return new ResizableEditPolicy();
		}
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		Rectangle rectangle = (Rectangle) constraint;
		
		OPRoSElementChangeLayoutCommand command = new OPRoSElementChangeLayoutCommand();           
        command.setModel(child.getModel());   
        command.setConstraint(rectangle);       
        
        //컴포넌트일때 넓이가 자식보다 커지지 못하게함
        if(child.getModel() instanceof OPRoSComponentElementModel) {
        	int width = 0;
        	for(Object obj : ((ILabeledFigure)((OPRoSElementPartBase)child).getFigure()).getContentPane().getChildren()) {
        		if(obj instanceof IFigure) {
        			IFigure figure = (IFigure)obj;
            		width += figure.getPreferredSize().width;
        		} 
        	}
        	
        	width += OPRoSComponentElementFigure.marginSize * 2 + 3;
        	if(rectangle.getSize().width > width) {
        		rectangle.setSize(width, rectangle.getSize().height);
        	}
        }
        return command;
    }   
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if(request.getType()==REQ_CREATE && getHost() instanceof OPRoSBodyElementPart){
			if(request.getNewObject() instanceof OPRoSPortElementBaseModel){
				OPRoSPortCreateCommand command = new OPRoSPortCreateCommand();
				OPRoSComponentElementModel compModel = ((OPRoSBodyElementModel)getHost().getModel()).compEle;
				//command.setParent(getHost().getModel());
				command.setParent(compModel.getParent());
				command.setElement(request.getNewObject());
				Rectangle constraint = (Rectangle) getConstraintFor(request);
				constraint.x = (constraint.x<0)?0:constraint.x;
				constraint.y = (constraint.y<0)?0:constraint.y;
//				constraint.width = (constraint.width<=0) ? ((OPRoSElementBaseModel)(request.getNewObject())).getLayout().width:constraint.width;
//				constraint.height = (constraint.height<=0) ? ((OPRoSElementBaseModel)(request.getNewObject())).getLayout().height:constraint.height;
				
				Rectangle compRect=compModel.getLayout();
				Point p = new Point(constraint.x,constraint.y);
				Rectangle maxRect = new Rectangle(
						compRect.x-OPRoSPortElementBaseModel.IMAGE_SIZE,
						compRect.y-OPRoSPortElementBaseModel.IMAGE_SIZE,
						compRect.width+OPRoSPortElementBaseModel.IMAGE_SIZE*2,
						compRect.height+OPRoSPortElementBaseModel.IMAGE_SIZE*2);
				if(!maxRect.contains(p)){
					return null;
				}
				
				command.setLayout(constraint);
				return command;
			}
		}else if(request.getType()==REQ_CREATE && getHost() instanceof OPRoSComponentElementPart){
			if(request.getNewObject() instanceof OPRoSExeEnvironmentOSElementModel){
				OPRoSExeEnvironmentOSElementCreateCommand command = new OPRoSExeEnvironmentOSElementCreateCommand();
				command.setParent(getHost().getModel());
				command.setElement(request.getNewObject());
				return command;
			}else if(request.getNewObject() instanceof OPRoSExeEnvironmentCPUElementModel){
				OPRoSExeEnvironmentCPUElementCreateCommand command = new OPRoSExeEnvironmentCPUElementCreateCommand();
				command.setParent(getHost().getModel());
				command.setElement(request.getNewObject());
				return command;
			}else if(request.getNewObject() instanceof OPRoSPropertyElementModel){
				OPRoSPropertyElementCreateCommand command = new OPRoSPropertyElementCreateCommand();
				command.setParent(getHost().getModel());
				command.setElement(request.getNewObject());
				return command;
			}else if(request.getNewObject() instanceof OPRoSDataTypeElementModel){
				OPRoSDataTypeElementCreateCommand command = new OPRoSDataTypeElementCreateCommand();
				command.setParent(getHost().getModel());
				command.setElement(request.getNewObject());
				return command;
			}else if(request.getNewObject() instanceof OPRoSServiceTypeElementModel){
				OPRoSServiceTypeElementCreateCommand command = new OPRoSServiceTypeElementCreateCommand();
				command.setParent(getHost().getModel());
				command.setElement(request.getNewObject());
				return command;
			}else if(request.getNewObject() instanceof MonitoringVariableModel){
				MonitoringVariableCreateCommand command = new MonitoringVariableCreateCommand();
				command.setParent(getHost().getModel());
				command.setElement(request.getNewObject());
				return command;
			}else if(request.getNewObject() instanceof OPRoSPortElementBaseModel){
				OPRoSPortCreateCommand command = new OPRoSPortCreateCommand();
				OPRoSComponentElementModel compModel = (OPRoSComponentElementModel)getHost().getModel();
				
				command.setParent(compModel.getParent());
				command.setElement(request.getNewObject());
				Rectangle constraint = (Rectangle) getConstraintFor(request);
				constraint.x = (constraint.x<0)?0:constraint.x;
				constraint.y = (constraint.y<0)?0:constraint.y;
				Rectangle compRect=compModel.getLayout();
				Point p = new Point(constraint.x+compRect.x,constraint.y+compRect.y);
				Rectangle minRect = new Rectangle(
						compRect.x+OPRoSPortElementBaseModel.IMAGE_SIZE,
						compRect.y+OPRoSPortElementBaseModel.IMAGE_SIZE,
						compRect.width-OPRoSPortElementBaseModel.IMAGE_SIZE*2,
						compRect.height-OPRoSPortElementBaseModel.IMAGE_SIZE*2);
				if(minRect.contains(p)){
					return null;
				}
				constraint.x=p.x;
				constraint.y=p.y;
				command.setLayout(constraint);
				return command;
			}
		}
		return null;
	}
	
	protected IFigure getLayoutContainer() {
		return ((GraphicalEditPart) getHost()).getFigure();
	}
	
}
