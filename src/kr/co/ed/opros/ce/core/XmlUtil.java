package kr.co.ed.opros.ce.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlUtil {
	
	public static XMLOutputter getXMLOutputter() { 
		XMLOutputter opt = new XMLOutputter();
		Format form = opt.getFormat();
		form.setEncoding("utf-8");
		form.setLineSeparator("\r\n");
		form.setIndent("	");
		form.setTextMode(Format.TextMode.TRIM);
		opt.setFormat(form);
		
		return opt;
	}
	
	/**
	 * IFile의 다큐먼트를 리턴한다.
	 * @param inputFile
	 * @return
	 */
	public synchronized static Document getIFileDocument(IFile file) {
		Document document = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			document = builder.build(file.getContents());
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}	
		return document;
	}
	
	/**
	 * 완성된 JDom Document를 파일에 쓴다.
	 * @param doc
	 * @param iFile
	 * @param monitor
	 */
	public synchronized static void writeDocumentToIFile(Document doc, IFile iFile, IProgressMonitor monitor) {
		XMLOutputter opt = getXMLOutputter();		
		String xmlStr = opt.outputString(doc);		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(xmlStr.getBytes());
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			if(!iFile.exists()) {
				iFile.create(bais, true, monitor);
			} else {
				iFile.setContents(bais, true, false, monitor);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
