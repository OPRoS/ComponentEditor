package kr.co.ed.opros.ce.guieditor.figure;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

public class OPRoSBodyElementFigure extends FreeformLayer {
	
	private Label label;
	
	public OPRoSBodyElementFigure(){
		setLayoutManager(new FreeformLayout());
		
		label=new Label();
		label.setForegroundColor(ColorConstants.blue);
		add(label);
		setConstraint(label,new Rectangle(5,5,-1,-1));
	}
	
	public void setLayout(Rectangle rect){
		setBounds(rect);
	}
	
	public void setFilename(String text){
		//label.setText(OPRoSStrings.getString("BodyFigureLabel")+text);
	}
	
}