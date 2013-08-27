package kr.co.ed.opros.ce.guieditor.figure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;


/**
 * ���̺��� ����ϱ� ���� ��� ���� �������̽�
 * @author hclee
 *
 */
public interface ILabeledFigure extends IFigure {

	public Label getLabel();

	public IFigure getContentPane();
	
	public void setIcon(Image image);
	
	public void setText(String text);
	
}
