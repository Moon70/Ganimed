package lunartools;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Comparator;
import java.util.TreeSet;

import javax.swing.ImageIcon;

public class FileTools {

	/**
	 * Write bytearray to file.
	 * 
	 * @param file The file to create or append to
	 * @param bytearray The bytearray to write
	 * @throws IOException
	 */
	public static void writeFile(File file,byte[] bytearray) throws IOException{
		writeFile(file,bytearray,false);
	}

	/**
	 * Write (create/overwrite or append) bytearray to file.
	 * 
	 * @param file The file to create/overwrite or append to
	 * @param bytearray The bytearray to write
	 * @param append <code>false</code> to create/overwrite a file, <true> to append to existing file
	 * @throws IOException
	 */
	public static void writeFile(File file,byte[] bytearray,boolean append) throws IOException{
		FileOutputStream fileOutputStream=new FileOutputStream(file.getAbsolutePath(),append);
		try{
			fileOutputStream.write(bytearray);
			fileOutputStream.flush();
		}finally{
			if(fileOutputStream!=null){
				fileOutputStream.close();
			}
		}
	}

	/**
	 * Write (create/overwrite or append) string to file.
	 * 
	 * @param file The file to create/overwrite or append to
	 * @param string The string to write
	 * @param append <code>false</code> to create/overwrite a file, <true> to append to existing file
	 * @param charset Name of the charset for creating the textfile
	 * @throws IOException
	 */
	public static void writeFile(File file,String string,boolean append,String charset) throws IOException{
		byte[] bytes=string.getBytes(charset);
		writeFile(file,bytes,append);
	}

	/**
	 * Write (create/overwrite or append) string to file.
	 * 
	 * @param file The file to create/overwrite or append to
	 * @param stringBuffer The stringbuffer to write
	 * @param append <code>false</code> to create/overwrite a file, <true> to append to existing file
	 * @param charset Name of the charset for creating the textfile
	 * @throws IOException
	 */
	public static void writeFile(File file,StringBuffer stringBuffer,boolean append,String charset) throws IOException{
		byte[] bytes=stringBuffer.toString().getBytes(charset);
		writeFile(file,bytes,append);
	}

	/**
	 * Read file into a bytearay.
	 * 
	 * @param file The file to read
	 * @return Content of file as bytearray
	 * @throws IOException
	 */
	public static byte[] readFileAsByteArray(File file) throws IOException{
		InputStream inputStream=null;
		long lenFile=file.length();
		byte[] bytes=new byte[(int)lenFile];
		try{
			inputStream=new FileInputStream(file);
			inputStream.read(bytes);
		}finally{
			if(inputStream!=null){
				inputStream.close();
			}
		}
		return bytes;
	}

	/**
	 * Reads the inputstream and puts the data into a stringbuffer.
	 * 
	 * @param inputStream The inputstream to read
	 * @param charset Name of the inputstream´s charset
	 * @return StringBuffer Data of inputstream as stringbuffer
	 * @throws IOException
	 */
	public static StringBuffer getStringBufferFromInputStream(InputStream inputStream,String charset) throws IOException{
		final char[] buffer=new char[1024];
		StringBuffer stringBuffer=new StringBuffer();
		InputStreamReader inputStreamReader=new InputStreamReader(inputStream,charset);
		BufferedReader bufferedReader=null;
		try {
			bufferedReader=new BufferedReader(inputStreamReader);
			int len;
			while((len=bufferedReader.read(buffer))!=-1){
				stringBuffer.append(buffer,0,len);
			}
		} finally {
			if(bufferedReader!=null) {
				bufferedReader.close();
			}
		}
		return stringBuffer;
	}

	public static byte[] getBytearrayFromInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		final byte[] buffer=new byte[1024];
		int len=0;
		try {
			while((len=inputStream.read(buffer))>0) {
				baos.write(buffer, 0, len);
			}
		} finally {
			inputStream.close();
		}
		return baos.toByteArray();
	}
	
	/**
	 * Read file into a stringbuffer.
	 * 
	 * @param file The file to read
	 * @param charset Name of the file´s charset
	 * @return Content of file as stringbuffer
	 * @throws IOException
	 */
	public static StringBuffer readFileAsStringBuffer(File file,String charset) throws IOException{
		return getStringBufferFromInputStream(new FileInputStream(file),charset);
	}

	public static ImageIcon createImageIcon(String iconpath, Object context) {
		URL url = context.getClass().getResource(iconpath);
		if (url != null) {
			return new ImageIcon(url);
		} else {
			System.err.println("Error loading icon: " + iconpath);
			return null;
		}
	}
	
	public static Image createImage(String imagepath, Object context) throws IOException {
		InputStream inputStream = context.getClass().getResourceAsStream(imagepath);
		byte[] imagedata=getBytearrayFromInputStream(inputStream);
		Image image=Toolkit.getDefaultToolkit().createImage(imagedata);
		return image;
	}
	
	public static File[] listFilesSorted(File folder) {
		File[] files=folder.listFiles();
		TreeSet<File> treeSet=new TreeSet<File>(new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		for(File file:files) {
			treeSet.add(file);
		}
		return treeSet.toArray(files);
	}
	
}
