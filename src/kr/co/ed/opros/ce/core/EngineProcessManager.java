package kr.co.ed.opros.ce.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunch;

import kr.co.ed.opros.ce.wizards.WizardMessages;

/**
 * ���� Ÿ�Ժ� ���μ��� ����
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
	 * ��� ���μ����� ������Ų��.
	 * @return
	 */
	public boolean allTerminate() {
		if(engineProcess.getIProcess() != null && engineProcess.getIProcess().canTerminate()) {
			engineProcess.excuteCommand("app stop\r\n");
		}	
		return true;
	}
	
	/**
	 * Ÿ�Կ� �´� ���μ��� ����.
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
	 * Ŭ���� �ν��Ͻ� ��ȯ
	 * @return
	 */
	public static EngineProcessManager getInstence() {
		if(instance == null)
			instance = new EngineProcessManager();
		return instance;
	}	
	
}
