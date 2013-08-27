package kr.co.ed.opros.ce.guieditor.figure;


import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Image;


/**
 * 기본적으로 레이블이 포함된 피규어
 * @author hclee
 *
 */
public class LabeledFigure extends Figure implements ILabeledFigure {

	public Label label;

	private IFigure containerFigure;

	public LabeledFigure() {
		setContainerFigure(new Figure());
		
		setLayoutManager(getLyout());
		getContentPane().setLayoutManager(getContainerLayout());
		
		label = new Label();	
		
		add(label);
		add(getContentPane());
		setForegroundColor(ColorConstants.black);
		setOpaque(true);
	}
	
	public AbstractLayout getLyout() {
		ToolbarLayout layout = new ToolbarLayout(false);
		layout.setStretchMinorAxis(true);		
		layout.setSpacing(0);
		
		return layout;
	}
	
	public AbstractLayout getContainerLayout() {
		ToolbarLayout tbLayout = new ToolbarLayout(false);
		//tbLayout.setStretchMinorAxis(true);
		tbLayout.setSpacing(0);
		return tbLayout;
	}

	public IFigure getContentPane() {
		return containerFigure;
	}

	public Label getLabel() {
		return label;
	}

	private void setContainerFigure(IFigure containerFigure) {
		this.containerFigure = containerFigure;
	}
	
	@Override
	public void setIcon(Image image){
		label.setIcon(image);
	}
	
	@Override
	public void setText(String text) {
		label.setText(text);
	}

}
