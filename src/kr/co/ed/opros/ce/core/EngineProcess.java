package kr.co.ed.opros.ce.core;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.preferences.PreferenceConstants;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.IProcessInfo;
import org.eclipse.cdt.core.IProcessList;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

/**
 * 엔진 타입별 프로세스를 저장하고 있는 클래스
 * @author hclee
 *
 */
public class EngineProcess {
	public final String ENGINE_NAME = "ComponentEngine.exe";
	
	public Process process;
	public IProcess iProcess;
	
	private int pid;
	private String engineDirPath;
	private String engineFilePath;
	private PrintWriter writer;
	
	public EngineProcess() {	
		this.engineFilePath = OPRoSActivator.getDefault().getPreferenceValue(PreferenceConstants.OPROS_ENGINE_FILE);
		this.engineDirPath 
			= OPRoSActivator.getDefault().getPreferenceValue(PreferenceConstants.OPROS_ENGINE_FILE).replace(ENGINE_NAME, "");
	}
	
	/**
	 * 프로세스 실행
	 */
	public boolean start(ILaunch launch) {
		if(iProcess == null || iProcess.isTerminated()) {
			try {
				process= DebugPlugin.exec(
						new String[] {engineFilePath},
						new File(engineDirPath));
				
				writer = new PrintWriter(process.getOutputStream());
				
				Map<String, String> processAttributes = new HashMap<String, String>();
				processAttributes.put(IProcess.ATTR_PROCESS_TYPE, ENGINE_NAME);
				
				iProcess = DebugPlugin.newProcess(launch, process, ENGINE_NAME);
				iProcess.setAttribute(IProcess.ATTR_CMDLINE, ENGINE_NAME);
				
				IProcessList listd = CCorePlugin.getDefault().getProcessList();						
				for(IProcessInfo info : listd.getProcessList()) {
					if(info.getName().equals(engineFilePath)) {
						pid = info.getPid();
						break;
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
		}		
		return true;
	}
	
	/**
	 * 엔진 프로세스 리턴
	 * 
	 * @return
	 */
	public Process getProcess() {
		return process;
	}
	
	public IProcess getIProcess() {
		return iProcess;
	}
	
	/**
	 * 커맨드 실행
	 * @param cmd
	 */
	public void excuteCommand(String cmd) {
		if(writer != null) {
			try {
				writer.write(cmd);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}		
	}
	
	/**
	 * 엔진 파일 경로 리턴
	 * @return
	 */
	public String getEngineFilePath() {
		return engineFilePath;
	}
	
	public int getPid() {
		return pid;
	}
	
}
