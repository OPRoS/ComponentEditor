package kr.co.ed.opros.ce.ui.actions;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.OPRoSUtil;
import kr.co.ed.opros.ce.preferences.PreferenceConstants;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
//import org.eclipse.core.runtime.Preferences;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class DebugOPRoSProjectActionDelegate implements
IWorkbenchWindowActionDelegate {

	private IProject selectionProject;
	private String appDllName;
	private String compName;
	private ISelection select=null;
	Image enableImg = OPRoSUtil.createImage("/icons/debug_icon.gif");
	ImageDescriptor enableImage= ImageDescriptor.createFromImage(enableImg);
	Image disableImg = OPRoSUtil.createImage("/icons/debug_icon_disable.gif");
	ImageDescriptor disableImage= ImageDescriptor.createFromImage(disableImg);
	
	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
	}

	@Override
	public void run(IAction action) {
		if(select==null||selectionProject==null){
			OPRoSUtil.openMessageBox("Component Folder is not Selected ", SWT.ERROR);
			return;
		}
//		Preferences pref = OPRoSActivator.getDefault().getPluginPreferences();
		IPreferenceStore pref = OPRoSActivator.getDefault().getPreferenceStore();
		IFile dllFile=selectionProject.getFile(appDllName);
		IFile profileFile = selectionProject.getFile("/"+compName+"/profile/"+compName+".xml");
		String strDllFile = dllFile.getLocation().toOSString();
		String strProfileFile = profileFile.getLocation().toOSString();
		String strEngineFile = pref.getString(PreferenceConstants.OPROS_ENGINE_FILE);
		if(strEngineFile.isEmpty()){
			OPRoSUtil.openMessageBox("Error : Empty Engine Name or Engine Position\nRetry after Setting Preference", SWT.ERROR);
			return;
		}
		InputDialog appNameDlg = new InputDialog(null,"Input Application Name","Enter a Application Name","",null);
		appNameDlg.open();
		if(appNameDlg.getReturnCode()!=InputDialog.OK)
			return;
		String strAppName = appNameDlg.getValue();
		if(strAppName.isEmpty())
			return;
		String repository = modifySystemFile(strEngineFile,strAppName);
		if(repository==null){
			OPRoSUtil.openMessageBox("Error : Wrong Config File", SWT.ERROR);
			return;
		}
		File dirRepo = new File(repository+"\\"+strAppName);
		if(!dirRepo.exists()){
			OPRoSUtil.openMessageBox("Error : Wrong Repository Position\nRetry after Setting Preference or System.xml", SWT.ERROR);
			return;
		}
		String[] copyCMD={"cmd.exe", "/c", "copy "+strDllFile+" "+repository+"\\"+strAppName};
		String[] copyCMD1={"cmd.exe", "/c", "copy "+strProfileFile+" "+repository+"\\"+strAppName};
		
		int index = strEngineFile.lastIndexOf('\\');
		File dir = new File(strEngineFile.substring(0,index));
		if(!dir.exists()){
			OPRoSUtil.openMessageBox("Error : Wrong Engine Position\nRetry after Setting Preference", SWT.ERROR);
			return;
		}
		String[] engineCMD={"cmd.exe", "/k", "start", strEngineFile};
		Runtime rt=Runtime.getRuntime();
		try {
			Process p = rt.exec(copyCMD);
			p.waitFor();
			p.destroy();
			p=rt.exec(copyCMD1);
			p.waitFor();
			p.destroy();
			rt.exec(engineCMD,null,dir);
			Robot tRobot = new Robot();
			tRobot.delay(50);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}catch (AWTException e) {
			e.printStackTrace();
		}

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		//ILaunchConfigurationType type = manager.getLaunchConfigurationType("org.eclipse.cdt.launch.localAttachCLaunch");
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(ICDTLaunchConfigurationConstants.ID_LAUNCH_C_ATTACH);
		try
		{
			ILaunchConfigurationWorkingCopy configuration = type.newInstance(null, selectionProject.getName()+" (debug)");
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME,selectionProject.getName());
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,appDllName);
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE,ICDTLaunchConfigurationConstants.DEBUGGER_MODE_ATTACH);
			configuration.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ID,"org.eclipse.cdt.debug.mi.core.MinGW");
			ILaunchConfiguration config = configuration.doSave();	
			DebugUITools.launch(config, ILaunchManager.DEBUG_MODE);
			return;
		}
		catch(CoreException e)
		{
			e.printStackTrace();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection != null && !selection.isEmpty()) {
			select=selection;
			if(selection instanceof IStructuredSelection){
				Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
				if (selectedElement instanceof IAdaptable) {
	                IAdaptable adaptable = (IAdaptable) selectedElement;  
	                IResource resource;
	                resource = (IResource) adaptable.getAdapter(IResource.class);
	                if (resource != null && resource.getType() != IResource.ROOT && resource.getParent().getType()!= IResource.ROOT) {
	                	while (resource.getType() != IResource.FOLDER || resource.getParent().getParent().getType() != IResource.ROOT){
	                		resource = resource.getParent();
	                		if(resource==null||resource.getParent()==null||resource.getParent().getParent()==null)
	                			return;
	                    }
	                	if(resource!=null&&resource.getParent()!=null&&resource.getParent().getParent()!=null){
		                	selectionProject=resource.getProject();
		                	compName=resource.getName(); 
		                	appDllName = compName+"/Debug/"+compName+".dll";
		                	action.setEnabled(true);
		                	action.setImageDescriptor(enableImage);
	                	}else{
	                		selectionProject=null;
	                		compName=null;
	    	            	appDllName = null;
		                	action.setEnabled(false);
		        			action.setImageDescriptor(disableImage);
	                	}
	                }else{
	                	selectionProject=null;
	                	compName=null;
		            	appDllName = null;
	                	action.setEnabled(false);
	        			action.setImageDescriptor(disableImage);
	                }
	            }else{
	            	selectionProject=null;
	            	compName=null;
	            	appDllName = null;
	            	action.setEnabled(false);
        			action.setImageDescriptor(disableImage);
	            }
	        }else{
	        	selectionProject=null;
            	compName=null;
            	appDllName = null;
	        	action.setEnabled(false);
    			action.setImageDescriptor(disableImage);
	        }
		}else{
			select=null;
			action.setEnabled(false);
			action.setImageDescriptor(disableImage);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private String modifySystemFile(String strEngine,String appName){
		int index = strEngine.lastIndexOf('\\');
		String strBinFolder = strEngine.substring(0, index);
		String strConfigFileName=strBinFolder+"\\Config\\system.xml";
		String strRepositoryFolder=null;
		FileInputStream inputStream = null;
		boolean bCheck=true;
		try {
			inputStream=new FileInputStream(strConfigFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		if(inputStream!=null){
			SAXBuilder builder = new SAXBuilder();
			Document doc = null;
			try {
				doc = builder.build( inputStream );
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Element root;
			if(doc!=null){
				root = doc.getRootElement();
				if(root!=null){
					Element ele = root.getChild("system_props");
					if(ele!=null){
						Iterator<Element> it = ele.getChildren("prop").iterator();
						Attribute attr;
						while(it.hasNext()){
							ele = it.next();
							attr=ele.getAttribute("name");
							if(attr!=null){
								if(attr.getValue().compareTo("app.default")==0){
									ele.setText(appName);
									bCheck=false;
								}else if(attr.getValue().compareTo("deploy.dir")==0){
									strRepositoryFolder=getRepositoryFolder(strBinFolder,ele.getText());
								}
							}
						}
						if(bCheck){
							if(inputStream!=null){
								try {
									inputStream.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							return null;
						}
						ele = root.getChild("package_deployer");
						if(ele!=null){
							it = ele.getChildren("prop").iterator();
							while(it.hasNext()){
								ele = it.next();
								attr=ele.getAttribute("name");
								if(attr!=null){
									if(attr.getValue().compareTo("load.type")==0){
										ele.setText("list");
										bCheck=false;
										break;
									}
								}
							}
							if(bCheck){
								if(inputStream!=null){
									try {
										inputStream.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								return null;
							}
						}else{
							if(bCheck){
								if(inputStream!=null){
									try {
										inputStream.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								return null;
							}
						}
						ele = root.getChild("package_deployer");
						if(ele!=null){
							it = ele.getChildren("app_load").iterator();
							bCheck=true;
							while(it.hasNext()){
								ele = it.next();
								if(ele.getText().compareTo(appName)==0){
									bCheck=false;
									break;
								}
							}
							if(bCheck){
								Element newEle=new Element("app_load");
								newEle.setText(appName);
								ele=root.getChild("package_deployer");
								if(ele!=null)
									ele.addContent(newEle);
							}
						}else{
							if(bCheck){
								if(inputStream!=null){
									try {
										inputStream.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								return null;
							}
						}
						if(inputStream!=null){
							try {
								inputStream.close();
								inputStream=null;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						XMLOutputter opt = new XMLOutputter();
						Format form = opt.getFormat();
						form.setEncoding("euc-kr");
						form.setLineSeparator("\r\n");
						form.setIndent("	");
						form.setTextMode(Format.TextMode.TRIM);
						opt.setFormat(form);
						try{
							FileOutputStream outStream = new FileOutputStream(strConfigFileName);
							opt.output(doc,outStream);
							outStream.close();
						}catch(IOException e){
							e.printStackTrace();
						}
					}else{
						if(inputStream!=null){
							try {
								inputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						return null;
					}
				}else{
					if(inputStream!=null){
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return null;
				}
			}else{
				if(inputStream!=null){
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		}else{
			return null;
		}
		if(strRepositoryFolder==null){
			strRepositoryFolder = strBinFolder+"\\Repository";
		}
		return strRepositoryFolder;
	}
	private String getRepositoryFolder(String binFolder, String deploy){
		int index;
		if(deploy.startsWith("${")){
			index = deploy.indexOf('}');
			if(index!=-1){
				deploy=deploy.substring(index+1);
			}
			return getRepositoryFolder(binFolder,deploy);
		}else{
			index = deploy.indexOf('/');
			if(index!=-1){
				if(index==0){
					deploy = deploy.substring(1);
					return getRepositoryFolder(binFolder,deploy);
				}else{
					String sub = deploy.substring(0, index);
					if(sub.compareTo("..")==0){
						deploy = deploy.substring(index+1);
						index = binFolder.lastIndexOf('\\');
						binFolder = binFolder.substring(0, index);
						return getRepositoryFolder(binFolder,deploy);
					}else if(sub.compareTo(".")==0){
						deploy = deploy.substring(index+1);
						return getRepositoryFolder(binFolder,deploy);
					}else{
						binFolder=binFolder+"\\"+sub;
						deploy = deploy.substring(index+1);
						return getRepositoryFolder(binFolder,deploy);
					}
				}
			}else{
				index = deploy.indexOf('\\');
				if(index!=-1){
					if(index==0){
						deploy = deploy.substring(1);
						return getRepositoryFolder(binFolder,deploy);
					}else{
						String sub = deploy.substring(0, index);
						if(sub.compareTo("..")==0){
							deploy = deploy.substring(index+1);
							index = binFolder.lastIndexOf('\\');
							binFolder = binFolder.substring(0, index);
							return getRepositoryFolder(binFolder,deploy);
						}else if(sub.compareTo(".")==0){
							deploy = deploy.substring(index+1);
							return getRepositoryFolder(binFolder,deploy);
						}else{
							binFolder=binFolder+"\\"+sub;
							deploy = deploy.substring(index+1);
							return getRepositoryFolder(binFolder,deploy);
						}
					}
				}else{
					binFolder+="\\"+deploy;
					return binFolder;
				}
			}
		}
	}
}
