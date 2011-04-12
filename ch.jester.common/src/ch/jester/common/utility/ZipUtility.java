package ch.jester.common.utility;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtility {
	public static List<String> getZipEntries(String file, boolean includeDirs) {
		try {
			List<String> entries = new ArrayList<String>();
			ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
			ZipEntry entry;

			while((entry = zis.getNextEntry())!=null){
				if(entry.isDirectory()){
					if(includeDirs){
						entries.add(entry.getName());
					}
					
				}else{
					entries.add(entry.getName());
				}
			}
			zis.close();
			return entries;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	public static InputStream getZipEntry(String zipFile, String zipEntry) {
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry entry;
			while((entry = zis.getNextEntry())!=null){
				if(entry.getName().equals(zipEntry)){
					 InputStream instream = deflate(zis, entry);
					 zis.close();
					 return instream;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static InputStream deflate(ZipInputStream zis, ZipEntry entry) {
		                int size;
		                byte[] buffer = new byte[2048];

		                ByteArrayOutputStream fos = new ByteArrayOutputStream();
		                BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

		                try {
							while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
							    bos.write(buffer, 0, size);
							}
			                bos.flush();
			                bos.close();
			              
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					return new ByteArrayInputStream(fos.toByteArray());
		    
	
		
	}
}
