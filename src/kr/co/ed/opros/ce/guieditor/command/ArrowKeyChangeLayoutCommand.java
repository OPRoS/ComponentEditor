package kr.co.ed.opros.ce.guieditor.command;

import java.util.List;

import kr.co.ed.opros.ce.guieditor.editpart.OPRoSElementPartBase;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;

import org.eclipse.gef.commands.Command;

public class ArrowKeyChangeLayoutCommand extends Command {
	
	private List<OPRoSElementPartBase> selectedEditPartList;
	
	private Integer differenceWidthOrX;
	
	private Integer differenceHeightOrY;
	
	private boolean bTranslate = true;
	
	private boolean bChanged;

	public ArrowKeyChangeLayoutCommand(List<OPRoSElementPartBase> selectedEditPartList, Integer differenceWidthOrX, Integer differenceHeightOrY) {
		super();
		this.selectedEditPartList = selectedEditPartList;
		this.differenceWidthOrX = differenceWidthOrX;
		this.differenceHeightOrY = differenceHeightOrY;
	}

	public ArrowKeyChangeLayoutCommand(List<OPRoSElementPartBase> selectedEditPartList, Integer differenceWidthOrX, Integer differenceHeightOrY, boolean bTranslate) {
		this(selectedEditPartList, differenceWidthOrX, differenceHeightOrY);
		this.bTranslate = bTranslate;
	}

	@Override
	public boolean canExecute() {
		return selectedEditPartList != null && differenceWidthOrX != null && differenceHeightOrY != null;
	}

	@Override
	public void execute() {
		changeLayout();
	}
	
	/**
	 * 레이아웃을 변경합니다.
	 */
	private void changeLayout() {
		changeLayout(false);
	}
	
	/**
	 * 레이아웃을 변경합니다.
	 * @param bUndo
	 */
	private void changeLayout(boolean bUndo) {
		for (Object selectedObject : selectedEditPartList) {
			OPRoSElementBaseModel editPart = (OPRoSElementBaseModel)((OPRoSElementPartBase)selectedObject).getModel();
					
			int wx = bUndo ? -differenceWidthOrX : differenceWidthOrX;
			int hy = bUndo ? -differenceHeightOrY : differenceHeightOrY;
			
			if (bTranslate) {
				editPart.setLayout(editPart.getLayout().getTranslated(wx, hy));
			} else {
				editPart.setLayout(editPart.getLayout().getResized(wx, hy));
			}
		}
		
		bChanged = !bUndo;
	}

	@Override
	public void redo() {
		changeLayout();
	}

	@Override
	public boolean canUndo() {
		return bChanged;
	}

	@Override
	public void undo() {
		changeLayout(true);
	}

}