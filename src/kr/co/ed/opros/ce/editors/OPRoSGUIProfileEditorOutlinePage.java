package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.guieditor.OPRoSElementTreeEditPartFactory;
import kr.co.ed.opros.ce.guieditor.OPRoSOutlineContextMenuProvider;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;

/**
 * 컴포넌트 에디터 아웃라인 페이지
 * @author hclee
 *
 */
public class OPRoSGUIProfileEditorOutlinePage extends ContentOutlinePage {
	
	private SashForm sash;
	private ScrollableThumbnail thumbnail;
	private DisposeListener disposeListener;
	public ElementChangeDialogCall elementChangecall;
	
	public OPRoSGUIProfileEditor editor;
	
	public OPRoSGUIProfileEditorOutlinePage(OPRoSGUIProfileEditor editor) {
		super(new TreeViewer());
		this.editor = editor;		
	}
	
	@Override
	public void createControl(Composite parent) {
		sash = new SashForm(parent,SWT.VERTICAL);
		getViewer().createControl(sash);
		getViewer().setEditDomain(editor.getEditDomain());
		getViewer().setEditPartFactory(new OPRoSElementTreeEditPartFactory());
		getViewer().setContents(editor.getModel());
		editor.getSelectionSynchronizer().addViewer(getViewer());
		
		Canvas canvas = new Canvas(sash, SWT.BORDER);
		
		LightweightSystem lws = new LightweightSystem(canvas);
		RootEditPart rootEditPart = editor.getGraphicalViewer().getRootEditPart(); 
		thumbnail = new ScrollableThumbnail(
				(Viewport)((ScalableFreeformRootEditPart)rootEditPart).getFigure());
		thumbnail.setSource(
				((ScalableFreeformRootEditPart) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS));
		lws.setContents(thumbnail);
		disposeListener = new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if(thumbnail != null){
					thumbnail.deactivate();
					thumbnail = null;
				}
			}
		};
		editor.getGraphicalViewer().getControl().addDisposeListener(disposeListener);
		getViewer().getControl().addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent evt) {
				Object obj = ((TreeItem[])((Tree)evt.getSource()).getSelection())[0].getData();
				
				ElementChangeDialogCall call = getElementChangecall();			
				call.setEditpart((EditPart)obj);
				call.run();
			}
			@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent arg0) {
			}
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent arg0) {
			}
			
		});
	}
	
	@Override
	public Control getControl() {
		return sash;
	}
	
	@Override
	public void dispose() {
		editor.getSelectionSynchronizer().removeViewer(getViewer());
		Control control = editor.getGraphicalViewer().getControl();
		if(control!=null && !control.isDisposed()){
			control.removeDisposeListener(disposeListener);
		}
		super.dispose();
	}
	
	@Override
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		
		ActionRegistry registry = editor.getActionRegistry();
		IActionBars bars = pageSite.getActionBars();
		String id = ActionFactory.UNDO.getId();
		bars.setGlobalActionHandler(id,registry.getAction(id));
		
		id = ActionFactory.REDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		
		id = ActionFactory.DELETE.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		
		bars.updateActionBars();
		getViewer().setKeyHandler(editor.keyHandler);
		getViewer().setContextMenu(new OPRoSOutlineContextMenuProvider(getViewer(),editor.getActionRegistry()));
		this.getSite().setSelectionProvider(getViewer());
	}
	
	/**
	 * 엘리먼트에 따른 다이얼로그를 띄워 수정할 수 있게해주는 클래스를 리턴한다.
	 * @return
	 */
	public ElementChangeDialogCall getElementChangecall() {
		if(elementChangecall == null)
			elementChangecall = new ElementChangeDialogCall(null);
		return elementChangecall;
	}
}
