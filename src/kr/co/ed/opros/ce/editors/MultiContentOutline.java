package kr.co.ed.opros.ce.editors;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * 멀티 페이지 에디터에서 에디터가 바뀔때
 * 선택된 에디터에 맞는 아웃라인 페이지를 적용 시킨다.
 * @author lee6607
 *
 */
public class MultiContentOutline extends ContentOutlinePage implements IContentOutlinePage {

	protected PageBook fPagebook;
	protected IContentOutlinePage fCurrentPage;
	private IPageSite pageSite;

	public MultiContentOutline(IContentOutlinePage page) {
		super();
		this.fCurrentPage = page;
	}	

	@Override
	public void init(IPageSite pageSite) {
		// TODO Auto-generated method stub
		this.pageSite = pageSite;
		super.init(pageSite);
	}

	@Override
	public void createControl(Composite parent) {
		fPagebook = new PageBook(parent, SWT.NONE);
		if (fCurrentPage != null)
			setPageActive(fCurrentPage);
	}

	@Override
	public Control getControl() {
		return fPagebook;
	}
	
	/**
	 * 아웃라인을 적용시킨다.
	 * @param page
	 */
	public void setPageActive(IContentOutlinePage page) {
		if (page instanceof OPRoSGUIProfileEditorOutlinePage)
			clearActionBars();
		
		if (page != null) {
			fCurrentPage = page;
			((Page)fCurrentPage).init(pageSite);
			if (fPagebook == null) {
				return;
			}
			Control control = page.getControl();

			if (control == null || control.isDisposed()) {
				page.createControl(fPagebook);
				control = page.getControl();
			}
			fPagebook.showPage(control);
		}
	}
	
	public void clearActionBars() {
		getSite().getActionBars().getMenuManager().removeAll();
		getSite().getActionBars().getToolBarManager().removeAll();
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (fCurrentPage != null)
			fCurrentPage.addSelectionChangedListener(listener);
	}

	@Override
	public ISelection getSelection() {
		return (fCurrentPage != null) ? fCurrentPage.getSelection() : null;
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		if (fCurrentPage != null)
			fCurrentPage.removeSelectionChangedListener(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		if (fCurrentPage != null)
			fCurrentPage.setSelection(selection);
	}

	@Override
	public void setFocus() {
		if (fCurrentPage != null)
			fCurrentPage.setFocus();
	}

}
