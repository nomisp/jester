package ch.jester.importmanagerservice.impl.internal;

import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.importer.IImportHandler;

public class SSBTextImporter implements IImportHandler{

	@Override
	public Object handleImport(InputStream pInputStream, IProgressMonitor pMonitor) {
		Scanner scanner = new Scanner(pInputStream, "UTF-8");
		while(scanner.hasNextLine()){
			System.out.println(scanner.nextLine());
		}
		return null;
	}

}
