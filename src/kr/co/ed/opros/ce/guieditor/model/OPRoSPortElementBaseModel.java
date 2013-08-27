package kr.co.ed.opros.ce.guieditor.model;

import kr.co.ed.opros.ce.guieditor.OPRoSStrings;
import kr.co.ed.opros.ce.guieditor.figure.OPRoSComponentElementFigure;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class OPRoSPortElementBaseModel extends OPRoSElementBaseModel {
	
	public static final String PROPERTY_PORT_NAME = "Port Name";
	public static final String PROPERTY_PORT_DESCRIPTION = "Port Description";
	public static final String PROPERTY_PORT_USAGE = "Port Usage";
	public static final String PROPERTY_PORT_TYPE = "Port Type";
	public static final int IMAGE_SIZE = 32;
	public static final float FONT_WIDTH_SIZE=(float)9;
	public static final int FONT_HIGHT_SIZE=15;
	/**
	 * 포트 이름
	 */
	private String name;
	/**
	 * 포트 설명
	 */
	private String description;
	/**
	 * 포트의 사용 타입 이름
	 * <p> ServicePort에서는 "required"/"provided"
	 * <p> DataPort/EventPort에서는 "input"/"output"
	 */
	private String usage;
	/**
	 * 포트의 타입 또는 데이터 타입 이름
	 * <p> ServicePort에서는 메소드 포트 타입 이름
	 * <p> DataPort/EventPort 에서는 데이터 타입 이름
	 */
	private String type;
	
	private int direction;
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * 클래스 생성자
	 */
	public OPRoSPortElementBaseModel() {
		super();
		initializeData();
	}
	
	public void initializeData(){
//		super.initializeData();
		setLayout(new Rectangle(102,110,IMAGE_SIZE,IMAGE_SIZE));
		name=OPRoSStrings.getString("PortBaseEleNameDefault");
		description=OPRoSStrings.getString("PortBaseEleDscrpDefault");
		usage=OPRoSStrings.getString("PortBaseEleUsageDefault");
		type=OPRoSStrings.getString("PortBaseEleTypeDefault");
		direction=4;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldValue = this.getName();
		this.name = name;
		//getListeners().firePropertyChange(PROPERTY_PORT_NAME, oldValue, name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		String oldValue = this.getDescription();
		this.description = description;
		//getListeners().firePropertyChange(PROPERTY_PORT_DESCRIPTION, oldValue, description);
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		String oldValue = this.getUsage();
		this.usage = usage;
		//getListeners().firePropertyChange(PROPERTY_PORT_USAGE, oldValue, usage);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		String oldValue = this.getType();
		this.type = type;
		//getListeners().firePropertyChange(PROPERTY_PORT_TYPE, oldValue, type);
	}

	@Override
	public void setLayout(Rectangle newLayout) {
		if(getParent()==null){
			super.setLayout(newLayout);
			return;
		}
		Rectangle compRect = ((OPRoSBodyElementModel)getParent()).compEle.getLayout();
		Point p = new Point(newLayout.x,newLayout.y);
		int northDistance = p.y-compRect.y;
		int southDistance = compRect.y + compRect.height - p.y;
		int eastDistance = compRect.x + compRect.width - p.x;
		int westDistance = p.x-compRect.x;
		
		Rectangle portRect = new Rectangle();
		if( northDistance > southDistance ){//South
			if(eastDistance > westDistance){//West
				if(southDistance > westDistance){//West Decision
					portRect.height=IMAGE_SIZE;
					if(p.y>compRect.y+compRect.height - portRect.height)
						portRect.y=compRect.y+compRect.height-portRect.height;
					else if(p.y < compRect.y - OPRoSComponentElementFigure.xPoint)
						portRect.y = compRect.y - OPRoSComponentElementFigure.xPoint;
					else
						portRect.y=p.y;
					portRect.width=(int) (getWidthSize()+IMAGE_SIZE);
					portRect.x=compRect.x+IMAGE_SIZE/2-portRect.width;
					setDirection(2);
					super.setLayout(portRect);
				}else{//South Decision
					portRect.width=getWidthSize();
					if(portRect.width<IMAGE_SIZE)
						portRect.width=IMAGE_SIZE;
					if(p.x>compRect.x+compRect.width - portRect.width)
						portRect.x=compRect.x+compRect.width - portRect.width;
					else if(p.x<compRect.x)
						portRect.x=compRect.x;
					else
						portRect.x=p.x;
					portRect.height=IMAGE_SIZE+FONT_HIGHT_SIZE;
					portRect.y=compRect.y+compRect.height-IMAGE_SIZE/2;
					setDirection(3);
					super.setLayout(portRect);
				}
			}else{//East
				if(southDistance > eastDistance){//East Decision
					portRect.height = IMAGE_SIZE;
					if(p.y > compRect.y + compRect.height - portRect.height)
						portRect.y = compRect.y + compRect.height - portRect.height;
					else if(p.y < compRect.y - OPRoSComponentElementFigure.xPoint)
						portRect.y = compRect.y - OPRoSComponentElementFigure.xPoint;
					else
						portRect.y=p.y;
					portRect.width=(int) (getWidthSize()+IMAGE_SIZE);
					portRect.x = compRect.x+compRect.width-IMAGE_SIZE / 2;
					setDirection(1);
					super.setLayout(portRect);
				}else{//South Decision
					portRect.width=getWidthSize();
					if(portRect.width < IMAGE_SIZE)
						portRect.width = IMAGE_SIZE;
					if(p.x > compRect.x + compRect.width - portRect.width)
						portRect.x=compRect.x+compRect.width - portRect.width;
					else if(p.x<compRect.x)
						portRect.x=compRect.x;
					else
						portRect.x=p.x;
					portRect.height=IMAGE_SIZE+FONT_HIGHT_SIZE;
					portRect.y=compRect.y+compRect.height-IMAGE_SIZE/2;
					setDirection(3);
					super.setLayout(portRect);
				}
			}
		} 
		//북쪽으로 못올라가게 위치를 제한한다.
		else{//North
			if(eastDistance > westDistance){//West
				portRect.height=IMAGE_SIZE;
				if(p.y>compRect.y+compRect.height-portRect.height)
					portRect.y=compRect.y+compRect.height-portRect.height;
				else if(p.y < compRect.y)
					portRect.y=compRect.y;
				else
					portRect.y=p.y;
				portRect.width=(int) (getWidthSize()+IMAGE_SIZE);
				portRect.x=compRect.x+IMAGE_SIZE/2-portRect.width;
				setDirection(2);
				super.setLayout(portRect);
			}else{//East
				portRect.height=IMAGE_SIZE;
				if(p.y>compRect.y+compRect.height-portRect.height)
					portRect.y=compRect.y+compRect.height-portRect.height;
				else if(p.y < compRect.y)
					portRect.y=compRect.y;
				else
					portRect.y=p.y;
				portRect.height=IMAGE_SIZE;
				portRect.width=(int) (getWidthSize()+IMAGE_SIZE);
				portRect.x=compRect.x+compRect.width-IMAGE_SIZE/2;
				setDirection(1);
				super.setLayout(portRect);
			}
		}
		
		/*else{//North
			if(eastDistance > westDistance){//West
				if(northDistance > westDistance){//West Decision
					portRect.height=IMAGE_SIZE;
					if(p.y>compRect.y+compRect.height)
						portRect.y=compRect.y+compRect.height-portRect.height;
					else if(p.y < compRect.y)
						portRect.y=compRect.y;
					else
						portRect.y=p.y;
					portRect.width=(int) (getWidthSize()+IMAGE_SIZE);
					portRect.x=compRect.x+IMAGE_SIZE/2-portRect.width;
					setDirection(2);
					super.setLayout(portRect);
				}else{//North Decision
					portRect.width = getWidthSize();
					if(portRect.width<IMAGE_SIZE)
						portRect.width=IMAGE_SIZE;
					if(p.x>compRect.x+compRect.width)
						portRect.x=compRect.x+compRect.width-portRect.width;
					else if(p.x<compRect.x)
						portRect.x=compRect.x;
					else
						portRect.x=p.x;
					portRect.height=IMAGE_SIZE+FONT_HIGHT_SIZE;
					portRect.y=compRect.y+IMAGE_SIZE/2-portRect.height;
					setDirection(4);
					super.setLayout(portRect);
				}
			}else{//East
				if(northDistance > eastDistance){//East Decision
					portRect.height=IMAGE_SIZE;
					if(p.y>compRect.y+compRect.height)
						portRect.y=compRect.y+compRect.height-portRect.height;
					else if(p.y<compRect.y)
						portRect.y=compRect.y;
					else
						portRect.y=p.y;
					portRect.height=IMAGE_SIZE;
					portRect.width=(int) (getWidthSize()+IMAGE_SIZE);
					portRect.x=compRect.x+compRect.width-IMAGE_SIZE/2;
					setDirection(1);
					super.setLayout(portRect);
				}else{//North Decision
					portRect.width=getWidthSize();
					if(portRect.width<IMAGE_SIZE)
						portRect.width=IMAGE_SIZE;
					if(p.x>compRect.x+compRect.width)
						portRect.x=compRect.x+compRect.width-portRect.width;
					else if(p.x<compRect.x)
						portRect.x=compRect.x;
					else
						portRect.x=p.x;
					portRect.height=IMAGE_SIZE+FONT_HIGHT_SIZE;
					portRect.y=compRect.y+IMAGE_SIZE/2-portRect.height;
					setDirection(4);
					super.setLayout(portRect);
				}
			}
			
		}*/
	}
	
	public int getWidthSize(){
		float fWidthSize=0;
		String strName=getName();
		char nChar;
		for(int i=0;i<strName.length();i++){
			nChar=strName.charAt(i);
			if(nChar>='0'&&nChar<='9'){
				fWidthSize=fWidthSize+7;
			}else if(nChar>='A'&&nChar<='Z'){
				if(nChar=='I')
					fWidthSize=fWidthSize+3;
				else if(nChar=='J')
					fWidthSize=fWidthSize+6;
				else if(nChar=='F'||nChar=='L')
					fWidthSize=fWidthSize+7;
				else if(nChar=='A'||nChar=='E'||nChar=='K'||nChar=='T'||nChar=='V'||nChar=='X'||nChar=='Y')
					fWidthSize=fWidthSize+8;
				else if(nChar=='G')
					fWidthSize=fWidthSize+FONT_WIDTH_SIZE+1;
				else if(nChar=='M')
					fWidthSize=fWidthSize+FONT_WIDTH_SIZE+2;
				else if(nChar=='W')
					fWidthSize=fWidthSize+FONT_WIDTH_SIZE+3;
				else
					fWidthSize=fWidthSize+FONT_WIDTH_SIZE;
			}else if(nChar>='a'&&nChar<='z'){
				if(nChar=='i'||nChar=='l')
					fWidthSize=fWidthSize+(float)3;
				else if(nChar=='j'||nChar=='f'||nChar=='t')
					fWidthSize=fWidthSize+(float)3.5;
				else if(nChar=='k'||nChar=='r')
					fWidthSize=fWidthSize+5;
				else if(nChar=='v')
					fWidthSize=fWidthSize+6;
				else if(nChar=='x'||nChar=='y'||nChar=='z')
					fWidthSize=fWidthSize+7;
				else if(nChar=='w')
					fWidthSize=fWidthSize+10;
				else if(nChar=='m')
					fWidthSize=fWidthSize+11;
				else
					fWidthSize=fWidthSize+8;
			}else{
				fWidthSize=fWidthSize+FONT_WIDTH_SIZE;
			}
		}
		return (int)fWidthSize;
	}
}
