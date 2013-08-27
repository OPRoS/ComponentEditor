package kr.co.ed.opros.ce.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunch;

import kr.co.ed.opros.ce.wizards.WizardMessages;

/**
 * 엔진 타입별 프로세스 관리
 * @author hclee
 *
 */
public class EngineProcessManager {
	
	public static EngineProcessManager instance;
	public EngineProcess engineProcess;
	
	public EngineProcessManager() {
		this.engineProcess  = new EngineProcess();		
	}
	
	/**
	 * 모든 프로세스를 중지시킨다.
	 * @return
	 */
	public boolean allTerminate() {
		if(engineProcess.getIProcess() != null && engineProcess.getIProcess().canTerminate()) {
			engineProcess.excuteCommand("app stop\r\n");
		}	
		return true;
	}
	
	/**
	 * 타입에 맞는 프로세스 리턴.
	 * @param compiler
	 * @param mode
	 * @return
	 */
	public EngineProcess getProcess(ILaunch launch) {
		if(engineProcess.start(launch))
			return engineProcess;
		return null;
	}
	
	/**
	 * 클래스 인스턴스 반환
	 * @return
	 */
	public static EngineProcessManager getInstence() {
		if(instance == null)
			instance = new EngineProcessManager();
		return instance;
	}	
	
}
