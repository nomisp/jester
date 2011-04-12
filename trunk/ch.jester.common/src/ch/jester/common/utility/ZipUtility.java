package ch.jester.common.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtility {
	public static List<String> getZipEntries(String file) {
		try {
			List<String> entries = new ArrayList<String>();
			ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
			ZipEntry entry;

			while((entry = zis.getNextEntry())!=null){
				entries.add(entry.getName());
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
}
