package kr.co.ed.opros.ce.guieditor.figure;

import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class OPRoSEventInPortElementFigure extends Figure {
	private XYLayout layout;
	private ImageFigure image;
	private Label label;
	private Image icon;
	private int direction;
	
	public Image getWidgetImage(){
		if(direction==1)
			icon = OPRoSUtil.createImage(OPRoSStrings.getString("EventInFigureE"));
		else if(direction==2)
			icon = OPRoSUtil.createImage(OPRoSStrings.getString("EventInFigureW"));
		else if(direction==3)
			icon = OPRoSUtil.createImage(OPRoSStrings.getString("EventInFigureS"));
		else if(direction==4)
			icon = OPRoSUtil.createImage(OPRoSStrings.getString("EventInFigureN"));
		return icon;
	}
	public OPRoSEventInPortElementFigure(){
		direction=4;
		layout = new XYLayout();
		setLayoutManager(layout);
		
		label = new Label();
		label.setForegroundColor(ColorConstants.darkGray);
		label.setFont(new Font(null,OPRoSStrings.getString("EventInFigureFont"),10,SWT.NONE));
		add(label,ToolbarLayout.ALIGN_CENTER);
		label.setLabelAlignment(Label.CENTER);
		setConstraint(label,new Rectangle(0,0,-1,-1));
		
		image = new ImageFigure();
		image.setImage(getWidgetImage());
		add(image,ToolbarLayout.ALIGN_CENTER);
		setConstraint(image,new Rectangle(0,10,-1,-1));

		setOpaque(false);
	}
	public void setLayout(Rectangle rect){
		getParent().setConstraint(this,rect);
		image.setImage(getWidgetImage());
		
		if(direction==1){
			setConstraint(label,new Rectangle(OPRoSPortElementBaseModel.IMAGE_SIZE,OPRoSPortElementBaseModel.IMAGE_SIZE/2-5,-1,-1));
			setConstraint(image,new Rectangle(0,0,OPRoSPortElementBaseModel.IMAGE_SIZE,OPRoSPortElementBaseModel.IMAGE_SIZE));
			label.setLabelAlignment(Label.LEFT);
		}else if(direction==2){
			setConstraint(label,new Rectangle(0,OPRoSPortElementBaseModel.IMAGE_SIZE/2-5,-1,-1));
			setConstraint(image,new Rectangle(rect.width-OPRoSPortElementBaseModel.IMAGE_SIZE,0,OPRoSPortElementBaseModel.IMAGE_SIZE,OPRoSPortElementBaseModel.IMAGE_SIZE));
			label.setLabelAlignment(Label.RIGHT);
		}else if(direction==3){
			setConstraint(label,new Rectangle(0,OPRoSPortElementBaseModel.IMAGE_SIZE,-1,-1));
			setConstraint(image,new Rectangle(rect.width/2-OPRoSPortElementBaseModel.IMAGE_SIZE/2,0,OPRoSPortElementBaseModel.IMAGE_SIZE,OPRoSPortElementBaseModel.IMAGE_SIZE));
			label.setLabelAlignment(Label.CENTER);
		}else if(direction==4){
			setConstraint(label,new Rectangle(0,0,-1,-1));
			setConstraint(image,new Rectangle(rect.width/2-OPRoSPortElementBaseModel.IMAGE_SIZE/2,14,OPRoSPortElementBaseModel.IMAGE_SIZE,OPRoSPortElementBaseModel.IMAGE_SIZE));
			label.setLabelAlignment(Label.CENTER);
		}
	}
	public void setText(String text){
		label.setText(text);
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
