package ch.jester.common.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

public class ObjectXMLSerializer {
	JAXBContext mContext;
	private Class<?>[] mclz;
	public ObjectXMLSerializer() {

	}
	public void prepareContext(Class<?>... pClasses) throws JAXBException {
		mclz = Arrays.copyOf(pClasses, pClasses.length + 1);
		mclz[pClasses.length] = XMLList.class;
		mContext = JAXBContext.newInstance(mclz);
	}
	
	public SerializationWriter createWriter(String pFileName) throws FileNotFoundException{
		return new SerializationWriter(new ZipOutputStream(new FileOutputStream(pFileName)), mContext);
	}

	public ZipSerializationReader createReader(String pFileName) throws FileNotFoundException{
		return new ZipSerializationReader(new ZipInputStream(new FileInputStream(pFileName)), mContext);
	}

	public SerializationReader createReader(InputStream inStream) throws FileNotFoundException{
		return new SerializationReader(inStream, mContext);
	}


	public static class SerializationReader{
		InputStream mzis; 
		ZipEntry entry;
		JAXBContext mContext;
		public SerializationReader(InputStream pzis, JAXBContext pContext) {
			mzis = pzis;
			mContext = pContext;
		}
		
		public List read() throws IOException {
			ZipEntry ze;
			

			try {
				XMLList<?> pt = (XMLList<?>) mContext
						.createUnmarshaller()
						.unmarshal(mzis);
				return pt.getList();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		public void close() throws IOException {
				mzis.close();
		}
	}
	
 public static class ZipSerializationReader{
		ZipInputStream mzis; 
		ZipEntry entry;
		JAXBContext mContext;
		public ZipSerializationReader(ZipInputStream pzis, JAXBContext pContext) {
			mzis = pzis;
			mContext = pContext;
		}
		
		public List read(String pEntriy) throws IOException {
			ZipEntry ze;

			while ((ze = mzis.getNextEntry()) != null) {
				if (ze.getName().equals(pEntriy)) {
					break;
				}
			}
			

			try {
				XMLList<?> pt = (XMLList<?>) mContext
						.createUnmarshaller()
						.unmarshal(ZipUtility.deflate(mzis, ze));
				return pt.getList();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		public void close() throws IOException {
				mzis.closeEntry();
				mzis.close();
		}
	}

	
public static class SerializationWriter{
		ZipOutputStream mzos; 
		ZipEntry entry;
		JAXBContext mContext;
		public SerializationWriter(ZipOutputStream pzos, JAXBContext pContext) {
			mzos = pzos;
			mContext = pContext;
		}
		public void newEntry(String pString) throws IOException {
			if(entry!=null){mzos.closeEntry();}
			mzos.putNextEntry(entry = new ZipEntry(pString));
		}
		public void close() throws IOException {
				mzos.flush();
				mzos.closeEntry();
				mzos.close();
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void write(List plist) {
			try {
				XMLList list = new XMLList();
				list.setList(plist);
				mContext.createMarshaller().marshal(list, mzos);
				mzos.flush();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void prepareContext(Class<?>... pClz) throws JAXBException {
			Class<?>[] clz = Arrays.copyOf(pClz, pClz.length + 1);
			clz[pClz.length] = XMLList.class;
			mContext = JAXBContext.newInstance(clz);
			
		}
	}
}
@XmlRootElement(name = "Root")
class XMLList<T> extends ArrayList<T> {
	// @XmlElement(name = "List")
	protected List<T> item;

	@XmlElementWrapper(name = "List")
	@XmlElement(name = "Object")
	public List<T> getList() {
		return this.item;
	}

	public void setList(List<T> p) {
		this.item = p;
	}
}