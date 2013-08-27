package kr.co.ed.opros.ce.guieditor;

import java.util.ArrayList;
import java.util.List;

import kr.co.ed.opros.ce.OPRoSActivator;
import kr.co.ed.opros.ce.guieditor.model.MonitoringVariableModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSDataTypeElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventInPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSEventOutPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentCPUElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSExeEnvironmentOSElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSPropertyElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceProvidedPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceRequiredPortElementModel;
import kr.co.ed.opros.ce.guieditor.model.OPRoSServiceTypeElementModel;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;

public class PaletteViewerCreator {
	static public PaletteRoot createPaletteRoot(){
		PaletteRoot root = new PaletteRoot();
		PaletteGroup controlGroup = new PaletteGroup(OPRoSStrings.getString("Group"));
		root.add(controlGroup);
		SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
		controlGroup.add(selectionToolEntry);
		root.setDefaultEntry(selectionToolEntry);
		controlGroup.add(new MarqueeToolEntry());
		
		PaletteDrawer portDrawer = new PaletteDrawer(OPRoSStrings.getString("PortTool"),ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("PortToolIcon")));
		List<CombinedTemplateCreationEntry> portEntries = new ArrayList<CombinedTemplateCreationEntry>();
		
		portEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("ServiceRequiredEntry"),OPRoSStrings.getString("ServiceRequiredEntryTip"),
				new OPRoSElementCreationFactory(OPRoSServiceRequiredPortElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceRequiredIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceRequiredIcon"))));
		portEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("ServiceProvidedEntry"),
				OPRoSStrings.getString("ServiceProvidedEntryTip"),
				new OPRoSElementCreationFactory(OPRoSServiceProvidedPortElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceProvidedIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceProvidedIcon"))));		
		
		
		portEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("DataOutEntry"),OPRoSStrings.getString("DataOutEntryTip"),
				new OPRoSElementCreationFactory(OPRoSDataOutPortElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataOutIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataOutIcon"))));
		portEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("DataInEntry"),OPRoSStrings.getString("DataInEntryTip"),
				new OPRoSElementCreationFactory(OPRoSDataInPortElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataInIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataInIcon"))));
		
		portEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("EventOutEntry"),OPRoSStrings.getString("EventOutEntryTip"),
				new OPRoSElementCreationFactory(OPRoSEventOutPortElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("EventOutIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("EventOutIcon"))));
		portEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("EventInEntry"),OPRoSStrings.getString("EventInEntryTip"),
				new OPRoSElementCreationFactory(OPRoSEventInPortElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("EventInIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("EventInIcon"))));		
		portDrawer.addAll(portEntries);
		
		PaletteDrawer compDefineDrawer = new PaletteDrawer(OPRoSStrings.getString("InfoTool"),ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("InfoToolIcon")));
		List<CombinedTemplateCreationEntry> compEntries = new ArrayList<CombinedTemplateCreationEntry>();
		compEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("OSEntry"),OPRoSStrings.getString("OSEntryTip"),
				new OPRoSElementCreationFactory(OPRoSExeEnvironmentOSElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("OSIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("OSIcon"))));
		compEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("CPUEntry"),OPRoSStrings.getString("CPUEntryTip"),
				new OPRoSElementCreationFactory(OPRoSExeEnvironmentCPUElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("CPUIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("CPUIcon"))));
		compEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("PropertyEntry"),OPRoSStrings.getString("PropertyEntryTip"),
				new OPRoSElementCreationFactory(OPRoSPropertyElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("PropertyIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("PropertyIcon"))));
		compEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("DataTypeEntry"),OPRoSStrings.getString("DataTypeEntryTip"),
				new OPRoSElementCreationFactory(OPRoSDataTypeElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataTypeIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("DataTypeIcon"))));
		compEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("ServiceTypeEntry"),OPRoSStrings.getString("ServiceTypeEntryTip"),
				new OPRoSElementCreationFactory(OPRoSServiceTypeElementModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceTypeIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("ServiceTypeIcon"))));
		
		compEntries.add(new CombinedTemplateCreationEntry(OPRoSStrings.getString("MonitoringVariableEntry"),OPRoSStrings.getString("MonitoringVariableEntryTip"),
				new OPRoSElementCreationFactory(MonitoringVariableModel.class),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("MonitoringVariableTreeIcon")),
				ImageDescriptor.createFromFile(OPRoSActivator.class, OPRoSStrings.getString("MonitoringVariableTreeIcon"))));
		compDefineDrawer.addAll(compEntries);
		root.add(portDrawer);
		root.add(compDefineDrawer);
		return root;
	}
}
