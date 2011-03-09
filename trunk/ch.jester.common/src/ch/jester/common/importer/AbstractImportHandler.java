package ch.jester.common.importer;

import ch.jester.common.activator.internal.CommonActivator;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.logging.ILogger;

public abstract class AbstractImportHandler implements IImportHandler{
	protected ILogger mLogger = CommonActivator.getInstance().getActivationContext().getLogger();
}
