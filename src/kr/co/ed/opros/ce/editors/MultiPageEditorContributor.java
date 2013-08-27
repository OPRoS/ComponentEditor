package kr.co.ed.opros.ce.editors;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;


public class MultiPageEditorContributor extends MultiPageEditorActionBarContributor {
	
	private static final String[] PROFILEEDITOR_ACTION_IDS = {
		ActionFactory.DELETE.getId(),
		ActionFactory.UNDO.getId(),
		ActionFactory.REDO.getId(),
		GEFActionConstants.ZOOM_IN,
		GEFActionConstants.ZOOM_OUT,
		ActionFactory.SAVE.getId()		
	};

	public MultiPageEditorContributor() {
		super();
	}

	@Override
	public void setActivePage(IEditorPart part) {
		if(part != null && part instanceof OPRoSGUIProfileEditor) {
			ActionRegistry registry = (ActionRegistry)part.getAdapter(ActionRegistry.class);
			if(registry == null)
				return;
			
			IActionBars bars = getActionBars();
			
			for(String id : PROFILEEDITOR_ACTION_IDS) {
				IAction action = registry.getAction(id);
				bars.setGlobalActionHandler(id, action);
			}
			getActionBars().updateActionBars();
		}	
		
		/*
		if(part instanceof OPRoSGUIProfileEditor) {
			hookGlobalProfileAction((OPRoSGUIProfileEditor)part, getActionBars());				
		}
		*/
	}
	
	private void hookGlobalProfileAction(OPRoSGUIProfileEditor part, IActionBars actionBars) {
		for(String id : PROFILEEDITOR_ACTION_IDS) {
			actionBars.setGlobalActionHandler(id, part.getAction(id));
		}
		actionBars.updateActionBars();
	}
	
}