package kr.co.ed.opros.ce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.eclipse.core.resources.IFile;

public class FileUtils {
	
	public static void copy(File source, String target) {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		
		File targetFile = new File(target);
		if(!targetFile.getParentFile().isDirectory()) {
			targetFile.getParentFile().mkdirs();
		}
		
		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fcout.close();
			} catch (IOException ioe) {
			}
			try {
				fcin.close();
			} catch (IOException ioe) {
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
			}
			try {
				inputStream.close();
			} catch (IOException ioe) {
			}
		}
	}
	
	public static boolean copy(IFile source, String target) {
		boolean isSuccess = true;
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			inputStream = (FileInputStream)source.getContents();
			outputStream = new FileOutputStream(target);
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		} finally {
			try {
				if(fcout != null)
					fcout.close();
				if(fcin != null)
					fcin.close();
				if(outputStream != null)
					outputStream.close();
				if(inputStream != null)
					inputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return isSuccess;
	}
	
	/**
	 * 폴더가 포함된 파일을 복사한다.
	 * @param sourceLocation 원본파일
	 * @param targetLocation 복사될 파일
	 * @throws IOException
	 */
	public static void copyDirectory(File sourceLocation, File targetLocation) {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.isDirectory()) {
				targetLocation.mkdir();
			}
			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} 
		else {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(sourceLocation);
				out = new FileOutputStream(targetLocation);
				
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * 파일 확장자를 구한다.
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	}
	
	public static String removeExtension(String fileName) {
		return fileName.replace(fileName.substring(fileName.lastIndexOf("."),fileName.length()), "");
	}

}
