package kr.co.ed.opros.ce;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.ed.opros.ce.guieditor.figure.OPRoSComponentElementFigure;
import kr.co.ed.opros.ce.guieditor.model.OPRoSBodyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSElementBaseModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPortElementBaseModel;

import org.eclipse.cdt.internal.core.model.CContainer;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class OPRoSUtil2 {
	
	//컴포넌트 템플릿 위치
	public static final String COMPONENT_TEMPLATE_PATH = "OPRoSFiles/Component_Template";
	
	/**
	 * 메니페스트 파일을 읽어서 해당 컴포넌트 폴더를 얻어온다.
	 * @return
	 */
	public static IResource getComponentContainer(IFile iFile) throws Exception {	
		return iFile.getParent();
	}
	
	public static String getComponentInfomation(IFile iFile, String type) {
		String info = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(iFile.getLocation().toFile()));
			String s;
			while ((s = in.readLine()) != null) {
				if(s.matches(type)) {
					info = s.substring(s.indexOf(":")+1, s.length());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * 해당 프로젝트가 OPRoS 프로젝트인지를 검사한다.
	 * @param project
	 * @return
	 */
	public static boolean isOPRoSProject(IProject project, String natureId) {
		if(!project.isAccessible())
			return false;
		try {
			return project.hasNature(natureId);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 워크스페이스에 있는 프로젝트중 OPRoS 프로젝트를 받아온다.
	 * @return
	 */
	public static List<IProject> getOPRoSProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		if (projects == null || projects.length == 0)
			return null;

		List<IProject> rtn = new ArrayList<IProject>();
		IProjectDescription pd = null;
		String[] natureIds = null;

		for (int i = 0; i < projects.length; i++) {
			if (projects[i].isOpen()) {
				try {
					pd = projects[i].getDescription();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				natureIds = pd.getNatureIds();
				for (int j = 0; j < natureIds.length; j++) {
					if (natureIds[j].equals(OPRoSNature.NATURE_ID)) {
						rtn.add(projects[i]);
						break;
					}
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 해당 프로젝트가 오프러스 프로젝트인지를 검사한다.
	 * @param project
	 * @return
	 */
	public static boolean isOPRoSProject(IProject project) {
		if(project != null && project.isOpen()) {
			IProjectDescription pd = null;
			try {
				pd = project.getDescription();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			for(String id : pd.getNatureIds()) {
				if(id.equals(OPRoSNature.NATURE_ID)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 플러그인 내부에 포함되어있는 컴포넌트 템플릿 경로를 담은
	 * 파일을 리턴한다.
	 * @return
	 */
	public static String getComponentTemplatePath() {		
		return OPRoSUtil.getOPRoSFilesPath() + COMPONENT_TEMPLATE_PATH;
	}
	
	/**
	 * 해당 프로젝트 폴더를 리플레쉬한다.
	 * @param project
	 * @throws CoreException
	 */
	public static void refreshProject(IProject project) {
    	try{
    		project.refreshLocal(IResource.DEPTH_INFINITE, null);//프로젝트 리프레쉬
		}catch(CoreException e){
			e.printStackTrace();
		}
    }
	
	/**
	 * 프로젝트 이름으로 중복된 프로젝트가 있는지 검사한다.
	 * @param projectName
	 * @return
	 */
	public static boolean isDuplicateProject(String projectName) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject project : projects) {
			if(project.getName().equals(projectName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 프로젝트를 리턴한다.
	 * @param name
	 * @return
	 */
	public static IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}
	
	/**
	 * 선택된 셀렉션이 오프러스 컴포넌트인지를 검사한다.
	 * @param selection
	 * @return
	 */
	public static IResource getOPRoSComponent(ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
			
			IResource resource = null;
			if (selectedElement instanceof IContainer)
				resource = (IContainer) selectedElement;
			else if (selectedElement instanceof IResource)
				resource = (IResource)selectedElement;
			else if(selectedElement instanceof CContainer)
				resource = ((CContainer) selectedElement).getParent().getResource();
			
			if(resource == null || resource instanceof IFile)
				return null;
			if(!OPRoSUtil2.isOPRoSProject(resource.getProject()))
				return null;
			
			Iterator<String> iter = OPRoSUtil.getComponentList(resource.getProject());
			while(iter.hasNext()) {
				String name = iter.next();
				if(resource.getName().equals(name)) {
					return resource;
				}
			}
		}
		return null;
	}
	
	public static IResource getOPRoSComponent2(ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
			
			IResource resource = null;
			if(selectedElement instanceof CContainer) {
				resource = ((CContainer) selectedElement).getResource();
				
				if(resource == null || resource instanceof IFile)
					return null;
				if(!OPRoSUtil2.isOPRoSProject(resource.getProject()))
					return null;
				
				Iterator<String> iter = OPRoSUtil.getComponentList(resource.getProject());
				while(iter.hasNext()) {
					String name = iter.next();
					if(resource.getName().equals(name)) {
						return resource;
					}
				}
			}
			
		}
		return null;
	}
	
	
	public static List<IResource> getSavedResource(IFile file, String componentName) {	
		List<IResource> list = new ArrayList<IResource>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file.getLocation().toFile()));
			String s;
			while ((s = in.readLine()) != null) {
				if(s.matches("element:.*")){
					String fileName = s.substring(s.lastIndexOf(":")+1, s.length()).replace("/", "");
					if(fileName.equals(componentName+".xml"))
						continue;
					
					IResource resource = (IResource)findFile((IFolder)file.getParent(), fileName);
					if(resource != null)
						list.add(resource);
				}
			}
			in.close();	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	/**
	 * 디팬던시에 체크된 파일을 찾아 리턴한다.
	 * @param parent
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static IResource findFile(IFolder parent, String fileName) throws Exception {
		IResource file = null;
		for(IResource resource : parent.members()) {			
			if(resource.getName().equals(fileName)) {
				file = resource;
			}
			/*
			if(file == null && resource instanceof IFolder) {
				file = findFile((IFolder)resource, fileName);
			}
			*/			
		}
		return file;
	}
	
	/**
	 * 문자열에 특수문자가 포함됐는지 체크한다.
	 * @param str
	 * @return
	 */
	public static boolean stringCheck(String str) {
		int n = str.length();
		char chr;
		for (int i = 0; i < n; i++) {
			chr = str.charAt(i);
			if (!Character.isJavaIdentifierPart(chr)) {
				return false;
			}
		}
		return true;
	}	
	
	/**
	 * 포트 위치 결정
	 * @param model
	 * @return
	 */
	public static Rectangle setLayout(OPRoSPortElementBaseModel model) {
		if(model.getParent()==null){
			return null;
		}
		Rectangle compRect = ((OPRoSBodyElementModel)model.getParent()).getComponentModel().getLayout();	
		return setLayout(model.getLayout().getLocation(), model, compRect);
	}
	
	public static Rectangle setLayout(Point p, OPRoSPortElementBaseModel model, Rectangle compRect) {
		
		int IMAGE_SIZE = OPRoSPortElementBaseModel.IMAGE_SIZE;
		int FONT_HIGHT_SIZE = OPRoSPortElementBaseModel.FONT_HIGHT_SIZE;	
		
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
					portRect.width=(int) (model.getWidthSize()+IMAGE_SIZE);
					portRect.x=compRect.x+IMAGE_SIZE/2-portRect.width;
					model.setDirection(2);
				}else{//South Decision
					portRect.width=model.getWidthSize();
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
					model.setDirection(3);
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
					portRect.width=(int) (model.getWidthSize()+IMAGE_SIZE);
					portRect.x = compRect.x+compRect.width-IMAGE_SIZE / 2;
					model.setDirection(1);
				}else{//South Decision
					portRect.width=model.getWidthSize();
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
					model.setDirection(3);
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
				portRect.width=(int) (model.getWidthSize()+IMAGE_SIZE);
				portRect.x=compRect.x+IMAGE_SIZE/2-portRect.width;
				model.setDirection(2);
			}else{//East
				portRect.height=IMAGE_SIZE;
				if(p.y>compRect.y+compRect.height-portRect.height)
					portRect.y=compRect.y+compRect.height-portRect.height;
				else if(p.y < compRect.y)
					portRect.y=compRect.y;
				else
					portRect.y=p.y;
				portRect.height=IMAGE_SIZE;
				portRect.width=(int) (model.getWidthSize()+IMAGE_SIZE);
				portRect.x=compRect.x+compRect.width-IMAGE_SIZE/2;
				model.setDirection(1);
			}
		}
		return portRect;
	}
	
public static Rectangle setLayout(Point p, Dimension r, OPRoSPortElementBaseModel model, Rectangle compRect, double zoom) {
		
		int IMAGE_SIZE = (int) (OPRoSPortElementBaseModel.IMAGE_SIZE * zoom);
		int FONT_HIGHT_SIZE = (int) (OPRoSPortElementBaseModel.FONT_HIGHT_SIZE * zoom);	
		
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
					portRect.width=(int) (r.width+IMAGE_SIZE);
					portRect.x=compRect.x+IMAGE_SIZE/2-portRect.width;
					model.setDirection(2);
				}else{//South Decision
					portRect.width=r.width;
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
					model.setDirection(3);
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
					portRect.width=(int) (r.width+IMAGE_SIZE);
					portRect.x = compRect.x+compRect.width-IMAGE_SIZE / 2;
					model.setDirection(1);
				}else{//South Decision
					portRect.width=r.width;
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
					model.setDirection(3);
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
				portRect.width=(int) (model.getWidthSize()+IMAGE_SIZE);
				portRect.x=compRect.x+IMAGE_SIZE/2-portRect.width;
				model.setDirection(2);
			}else{//East
				portRect.height=IMAGE_SIZE;
				if(p.y>compRect.y+compRect.height-portRect.height)
					portRect.y=compRect.y+compRect.height-portRect.height;
				else if(p.y < compRect.y)
					portRect.y=compRect.y;
				else
					portRect.y=p.y;
				portRect.height=IMAGE_SIZE;
				portRect.width=(int) (model.getWidthSize()+IMAGE_SIZE);
				portRect.x=compRect.x+compRect.width-IMAGE_SIZE/2;
				model.setDirection(1);
			}
		}
		return portRect;
	}
	
}
