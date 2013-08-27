/**
 * 
 */
package kr.co.ed.opros.ce.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author jwkim
 *
 */
public class OPRoSGUIProfileEditorContributor extends ActionBarContributor {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
//		addRetargetAction(new RetargetAction(
//				GEFActionConstants.TOGGLE_RULER_VISIBILITY,
//				GEFMessages.ToggleRulerVisibility_Label,
//				IAction.AS_CHECK_BOX));
	}

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
//		// TODO Auto-generated method stub
//		super.contributeToMenu(menuManager);
//		MenuManager viewMenu = new MenuManager("View");
//		viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		// TODO Auto-generated method stub
		super.contributeToToolBar(toolBarManager);
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));	
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		
		
//		toolBarManager.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	@Override
	protected void declareGlobalActionKeys() {
		
	}

}
