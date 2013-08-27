package kr.co.ed.opros.ce.guieditor.figure;

import java.util.List;

import kr.co.ed.opros.ce.CommonFont;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class OPRoSComponentElementFigure extends LabeledFigure {
	
	public static int xPoint = 28;
	public static int yPoint = 18;
	public static int roundSizeA = 16;
	public static int roundSizeB = 10;
	
	public static int marginSize = 25;
	
	public OPRoSComponentElementFigure(){
		super();
		
		getLabel().setBackgroundColor(new Color(null, 000, 148, 230));
		getLabel().setForegroundColor(ColorConstants.white);
		getLabel().setOpaque(true);
		getLabel().setFont(CommonFont.font6);
		getLabel().setBorder(new LineBorder(new Color(null, 000, 127, 198)));
		
		getContentPane().setBorder(new LineBorder(new Color(null, 000, 127, 198)));
	}
	
	@Override
	public AbstractLayout getContainerLayout() {
	
		ToolbarLayout layout = new ToolbarLayout(true);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(0);
		
		//FlowLayout layout = new FlowLayout(true);
		return layout;
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		
		Rectangle r = bounds;
		
		/*
		Image img = OPRoSActivator.getImageDescriptor(IIconConstants.IMG_COMPONENT_BODY).createImage();		
		graphics.drawImage(img, new Rectangle(0, 0, img.getBounds().width, img.getBounds().height), r);
		
		
		graphics.drawImage(OPRoSActivator.getImageDescriptor(IIconConstants.IMG_COMPOSITE_TOPLEFT).createImage(), r.x, r.y + yPoint -1);
		graphics.drawImage(OPRoSActivator.getImageDescriptor(IIconConstants.IMG_COMPOSITE_TOPRIGHT).createImage(), r.x + r.width -10, r.y + yPoint -1);
		
		graphics.drawImage(OPRoSActivator.getImageDescriptor(IIconConstants.IMG_COMPOSITE_BOTTOMRIGHT).createImage(), r.x + r.width - 10 , r.y + r.height - 10);
		graphics.drawImage(OPRoSActivator.getImageDescriptor(IIconConstants.IMG_COMPOSITE_BOTTOMLEFT).createImage(), r.x, r.y + r.height - 10);
		*/
		
		
		graphics.setLineWidth(1);
		graphics.setForegroundColor(new Color(null, 000, 148, 230));
		graphics.setBackgroundColor(new Color(null, 89, 185, 238));
		
		
		int x = (r.width - (xPoint * 2)) /3;
		
		PointList pl = new PointList(20);		
		pl.addPoint(r.x + (roundSizeA/2) -1, r.y + yPoint);
		pl.addPoint(r.x + x, r.y + yPoint);
		pl.addPoint(r.x + x, r.y + (roundSizeB/2) -1);
		pl.addPoint(r.x + x + (roundSizeB/2) -1 , r.y);
		pl.addPoint(r.x + x + xPoint - (roundSizeB/2) +1, r.y);
		pl.addPoint(r.x + x + xPoint, r.y + (roundSizeB/2) -1 );
		pl.addPoint(r.x + x + xPoint , r.y + yPoint);
		pl.addPoint(r.x + (x*2) + xPoint, r.y + yPoint);
		pl.addPoint(r.x + (x*2) + xPoint, r.y + (roundSizeB/2) -1);
		pl.addPoint(r.x + (x*2) + xPoint + (roundSizeB/2) -1, r.y);
		pl.addPoint(r.x + (x*2) + (xPoint *2) - (roundSizeB/2) +1, r.y);
		pl.addPoint(r.x + (x*2) + (xPoint *2), r.y + (roundSizeB/2) -1);
		pl.addPoint(r.x + (x*2) + (xPoint *2) , r.y + yPoint);
		pl.addPoint(r.x + r.width - (roundSizeA/2), r.y + yPoint);
		pl.addPoint(r.x + r.width-1 , r.y + yPoint + (roundSizeA/2)-1);
		pl.addPoint(r.x + r.width-1, r.y + r.height - (roundSizeA/2));
		pl.addPoint(r.x + r.width - (roundSizeA /2) , r.y + r.height -1 );
		pl.addPoint(r.x + (roundSizeA /2) -1, r.y + r.height -1);
		pl.addPoint(r.x, r.y + r.height - (roundSizeA /2));
		pl.addPoint(r.x, r.y + yPoint + (roundSizeA /2) -1);
		graphics.fillPolygon(pl);		
		
		graphics.fillArc(r.x, r.y + yPoint, roundSizeA, roundSizeA, 90, 90);
		graphics.fillArc(r.x + x, r.y , roundSizeB, roundSizeB, 90, 90);
		graphics.fillArc(r.x + x + xPoint - (roundSizeB) +1, r.y, roundSizeB, roundSizeB, 360, 90);
		graphics.fillArc(r.x + (x*2) + xPoint, r.y, roundSizeB, roundSizeB, 90, 90);
		graphics.fillArc(r.x + (x*2) + (xPoint *2) - (roundSizeB) +1, r.y, roundSizeB, roundSizeB, 360, 90);
		graphics.fillArc(r.x + r.width - (roundSizeA) , r.y + yPoint, roundSizeA, roundSizeA, 360, 90);
		graphics.fillArc(r.x + r.width - (roundSizeA) , r.y + r.height - (roundSizeA), roundSizeA, roundSizeA, 270, 90);
		graphics.fillArc(r.x, r.y + r.height - (roundSizeA), roundSizeA, roundSizeA, 180, 90);
		
		
		graphics.drawArc(r.x, r.y + yPoint, roundSizeA, roundSizeA, 90, 90);
		graphics.drawLine(r.x + (roundSizeA/2), r.y + yPoint, r.x + x, r.y + yPoint);
		graphics.drawLine(r.x + x, r.y + yPoint, r.x + x, r.y + (roundSizeB/2));
		graphics.drawArc(r.x + x, r.y, roundSizeB, roundSizeB, 90, 90);
		graphics.drawLine(r.x + x + (roundSizeB/2), r.y , r.x + x + xPoint - (roundSizeB/2), r.y);
		graphics.drawArc(r.x + x + xPoint - (roundSizeB), r.y, roundSizeB, roundSizeB, 360, 90);
		graphics.drawLine(r.x + x + xPoint, r.y + (roundSizeB/2), r.x + x + xPoint , r.y + yPoint);
		graphics.drawLine(r.x + x + xPoint , r.y + yPoint, r.x + (x*2) + xPoint, r.y + yPoint);
		
		graphics.drawLine(r.x + (x*2) + xPoint, r.y + yPoint, r.x + (x*2) + xPoint, r.y + (roundSizeB/2));
		graphics.drawArc(r.x + (x*2) + xPoint, r.y, roundSizeB, roundSizeB, 90, 90);
		graphics.drawLine(r.x + (x*2) + xPoint + (roundSizeB/2), r.y, r.x + (x*2) + (xPoint *2) - (roundSizeB/2), r.y);
		graphics.drawArc(r.x + (x*2) + (xPoint *2) - (roundSizeB), r.y, roundSizeB, roundSizeB, 360, 90);
		graphics.drawLine(r.x + (x*2) + (xPoint *2), r.y + (roundSizeB/2), r.x + (x*2) + (xPoint *2) , r.y + yPoint);
		graphics.drawLine(r.x + (x*2) + (xPoint *2) , r.y + yPoint, r.x + r.width - (roundSizeA/2), r.y + yPoint);
		graphics.drawArc(r.x + r.width - (roundSizeA) -1 , r.y + yPoint, roundSizeA, roundSizeA, 360, 90);
		
		graphics.drawLine(r.x + r.width-1 , r.y + yPoint + (roundSizeA/2), r.x + r.width-1, r.y + r.height - (roundSizeA/2) -1);
		graphics.drawArc(r.x + r.width - (roundSizeA) -1 , r.y + r.height - (roundSizeA) -1, roundSizeA, roundSizeA, 270, 90);
		graphics.drawLine(r.x + r.width - (roundSizeA /2) -1 , r.y + r.height -1 , r.x + (roundSizeA /2), r.y + r.height -1);
		graphics.drawArc(r.x, r.y + r.height - (roundSizeA) -1, roundSizeA, roundSizeA, 180, 90);
		graphics.drawLine(r.x, r.y + r.height - (roundSizeA /2), r.x, r.y + yPoint + (roundSizeA /2));	
		
	}

	public void setLayout(Rectangle rect){
		getParent().setConstraint(this, rect);
		setConstraint(getLabel(), new Rectangle(rect.width/8, 45, rect.width*3/4, 31));
		
		if(getChildren().contains(getContentPane())){
			int width = (rect.width -41) / 4;
			List<Object> list = getContentPane().getChildren();
			for(Object obj : list) {
				IFigure figure = (IFigure)obj;
				//getContentPane().setMinimumSize(new Dimension(100, 100));
				getContentPane().setConstraint(figure, new Rectangle(0, 0, width, 50));
				//figure.setMinimumSize(new Dimension(width, 50));
				
			}
			setConstraint(getContentPane(), new Rectangle(marginSize, 85, rect.width - (marginSize * 2 + 1), rect.height - 110));	
		}
		
	}
	
	@Override
	public AbstractLayout getLyout() {
		return new XYLayout();
	}
	
}
