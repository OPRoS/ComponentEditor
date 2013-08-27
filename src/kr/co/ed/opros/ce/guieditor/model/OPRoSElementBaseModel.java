package kr.co.ed.opros.ce.guieditor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.guieditor.PropertyView.OPRoSElementPropertySource;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jdom.Element;

public class OPRoSElementBaseModel implements IAdaptable {
	private Rectangle layout;
	private List<OPRoSElementBaseModel> children;
	private OPRoSElementBaseModel parent;
	private Color foregroundColor;
	private Color backgroundColor;
	private int border;
	private boolean collapsed;
	
//	private OPRoSElementGuide verticalGuide, horizontalGuide;
	
	private IPropertySource propertySource = null;
	
	public static final String PROPERTY_LAYOUT = "OPRoSGUIProfileEditorElementBase_Layout_Change";
	public static final String PROPERTY_REMOVE = "OPRoSGUIProfileEditorElementBase_Remove";
	public static final String PROPERTY_ADD = "OPRoSGUIProfileEditorElementBase_Add";
	public static final String PROPERTY_COLLAPSED = "OPRoSGUIProfileEditorElementBase_Collapsed";
	
	private PropertyChangeSupport listeners;
	
	public OPRoSElementBaseModel(){
		children = new ArrayList<OPRoSElementBaseModel>();
		parent=null;
		listeners = new PropertyChangeSupport(this);
		layout=new Rectangle(10,10,100,100);
		foregroundColor = ColorConstants.black;
		backgroundColor = ColorConstants.white;
		border = 1;
//		initializeData();
	}
	public Rectangle getLayout(){
		return layout;
	}
	public void setLayout(Rectangle newLayout){
		Rectangle oldLayout = layout;
		layout = newLayout;
		if(this instanceof OPRoSComponentElementModel){
			if((newLayout.width-oldLayout.width)!=0||(newLayout.height-oldLayout.height)!=0){
				layout = checkCompLayout(newLayout);	
			}
			OPRoSElementBaseModel parent = getParent();
			if(parent!=null){
				Iterator<OPRoSElementBaseModel> it = parent.getChildrenList().iterator();
				while(it.hasNext()){
					OPRoSElementBaseModel model = (OPRoSElementBaseModel)it.next();
					if(model instanceof OPRoSPortElementBaseModel){
						Rectangle modelRect = model.getLayout();
						Rectangle rect = new Rectangle(modelRect.x,modelRect.y,modelRect.width,modelRect.height);
						int nDir = ((OPRoSPortElementBaseModel) model).getDirection();
						if(nDir==1){
							if(modelRect.x!=layout.x+layout.width){
								rect.x=layout.x+layout.width;
							}
							if((oldLayout.y!=layout.y)&&(oldLayout.height==layout.height)){
								rect.y=rect.y+layout.y-oldLayout.y;
							}
						}else if(nDir==2){
							if(modelRect.x+modelRect.width!=layout.x){
								rect.x=layout.x-rect.width;
							}
							if((oldLayout.y!=layout.y)&&(oldLayout.height==layout.height)){
								rect.y=rect.y=rect.y+layout.y-oldLayout.y;
							}
						}else if(nDir==3){
							if(modelRect.y!=layout.y+layout.height){
								rect.y=layout.y+layout.height;
							}
							if((oldLayout.x!=layout.x)&&(oldLayout.width==layout.width)){
								rect.x=rect.x+layout.x-oldLayout.x;
							}
						}else{
							if(modelRect.y+modelRect.height!=layout.y){
								rect.y=layout.y-rect.height;
							}
							if((oldLayout.x!=layout.x)&&(oldLayout.width==layout.width)){
								rect.x=rect.x+layout.x-oldLayout.x;
							}
						}
						model.setLayout(rect);
					}
				}
			}
		}
		getListeners().firePropertyChange(PROPERTY_LAYOUT,oldLayout,newLayout);
	}
	
	public boolean addChild(OPRoSElementBaseModel child){
		boolean b = children.add(child);
		if(b){
			child.setParent(this);
			getListeners().firePropertyChange(PROPERTY_ADD, null, child);
		}
		return b;
	}
	
	public boolean addChild(OPRoSElementBaseModel child, int index){
		if (child != null ) {
			children.add(index, child);
			child.setParent(this);
			getListeners().firePropertyChange(PROPERTY_ADD, null, child);
		}
		return false;
	}
	
	
	public boolean removeChild(OPRoSElementBaseModel child){
		boolean b= children.remove(child);
		if(b){
			getListeners().firePropertyChange(PROPERTY_REMOVE, child, null);
		}
		return b;
	}
	public List<OPRoSElementBaseModel> getChildrenList(){
		return children;
	}
	public OPRoSElementBaseModel getParent(){
		return parent;
	}
	public void setParent(OPRoSElementBaseModel parent){
		this.parent = parent;
	}
	public OPRoSElementBaseModel getChild(int index){
		return children.get(index);
	}
	public Color getForegroundColor(){
		return foregroundColor;
	}
	public void setForegroundColor(Color foregroundColor){
		this.foregroundColor = foregroundColor;
	}
	public Color getBackgroundColor(){
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor){
		this.backgroundColor = backgroundColor;
	}
	public int getBorder(){
		return border;
	}
	public void setBorder(int border){
		this.border=border;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		listeners.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		listeners.removePropertyChangeListener(listener);
	}
	public PropertyChangeSupport getListeners(){
		return listeners;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == IPropertySource.class){
			if(propertySource == null)
				propertySource = new OPRoSElementPropertySource(this);
			return propertySource;
		}
		return null;
	}
	
	public boolean contains(OPRoSElementBaseModel child){
		return children.contains(child);
	}
	
	private Rectangle checkCompLayout(Rectangle rect){
		Rectangle r = new Rectangle(rect.x,rect.y,rect.width,rect.height);
		OPRoSElementBaseModel parent = getParent();
		if(parent!=null){
			Iterator<OPRoSElementBaseModel> it = parent.getChildrenList().iterator();
			while(it.hasNext()){
				OPRoSElementBaseModel model = (OPRoSElementBaseModel)it.next();
				if(model instanceof OPRoSPortElementBaseModel){
					Rectangle modelRect = model.getLayout();
					int nDir = ((OPRoSPortElementBaseModel) model).getDirection();
					if(nDir==1){
	//					if(r.x+r.width<modelRect.x){
	//						r.width=modelRect.x-rect.x;
	//					}
						if(r.y>modelRect.y+modelRect.height/2){
							r.y=modelRect.y+modelRect.height/2;
						}
						if(r.y+r.height<modelRect.y+modelRect.height/2){
							r.height=modelRect.y+modelRect.height/2-r.y;
						}
					}else if(nDir==2){
	//					if(r.x>modelRect.x+modelRect.width){
	//						r.x=modelRect.x+modelRect.width;
	//					}
						if(r.y > modelRect.y+modelRect.height/2){
							r.y=modelRect.y+modelRect.height/2;
						}
						if(r.y+r.height<modelRect.y+modelRect.height/2){
							r.height=modelRect.y+modelRect.height/2-r.y;
						}
					}else if(nDir==3){
	//					if(r.y>modelRect.y+modelRect.height){
	//						r.y=modelRect.y+modelRect.height;
	//					}
						if(r.x>modelRect.x+modelRect.width/2){
							r.x=modelRect.x+modelRect.width/2;
						}
						if(r.x+r.width<modelRect.x+modelRect.width/2){
							r.width=modelRect.x+modelRect.width/2-r.x;
						}
					}else{
	//					if(r.y+r.height<modelRect.y){
	//						r.height=modelRect.y-r.y;
	//					}
						if(r.x>modelRect.x+modelRect.width/2){
							r.x=modelRect.x+modelRect.width/2;
						}
						if(r.x+r.width<modelRect.x+modelRect.width/2){
							r.width=modelRect.x+modelRect.width/2-r.x;
						}
					}
				}
			}
		}
		return r;
	}
	public void doLoad(Element rootEle){
		
	}
	
	public boolean isCollapsed() {
		return collapsed;
	}
	
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
		getListeners().firePropertyChange(PROPERTY_COLLAPSED, null ,null);
	}
}
