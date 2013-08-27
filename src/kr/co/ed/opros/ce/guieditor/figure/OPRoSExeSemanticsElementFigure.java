package kr.co.ed.opros.ce.guieditor.figure;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

public class OPRoSExeSemanticsElementFigure extends Figure {
	private XYLayout layout;
	private ImageFigure image;
	private Image icon;
	public Image getWidgetImage(){
		if(icon==null)
			icon = OPRoSUtil.createImage(OPRoSStrings.getString("ExeSemanticsIcon"));
		return icon;
	}
	
	public OPRoSExeSemanticsElementFigure(){
		super();
		
		layout = new XYLayout();
		setLayoutManager(layout);
		image = new ImageFigure();
		image.setImage(getWidgetImage());
		add(image,ToolbarLayout.ALIGN_CENTER);
		setConstraint(image,new Rectangle(0,0,-1,-1));
		setOpaque(false);	
	}
	
	public void setLayout(Rectangle rect){
		getParent().setConstraint(this, rect);
		setConstraint(image, new Rectangle(0,0,-1,-1));
	}
}
