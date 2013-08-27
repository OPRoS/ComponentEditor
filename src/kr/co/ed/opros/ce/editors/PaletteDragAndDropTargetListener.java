package kr.co.ed.opros.ce.editors;

import kr.co.ed.opros.ce.guieditor.OPRoSElementCreationFactory;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.swt.dnd.DropTargetEvent;

public class PaletteDragAndDropTargetListener extends
		TemplateTransferDropTargetListener {

	public PaletteDragAndDropTargetListener(EditPartViewer viewer) {
		super(viewer);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CreationFactory getFactory(Object template) {
		// TODO Auto-generated method stub
		return (OPRoSElementCreationFactory)template;
	}
	
    public void dragOver(DropTargetEvent event) {
        try {
            super.dragOver(event);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void drop(DropTargetEvent event) {
		// TODO Auto-generated method stub
		super.drop(event);
	}
    
}
