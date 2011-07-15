package ch.jester.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.core.runtime.NullProgressMonitor;

import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;
import ch.jester.commonservices.api.web.IHTTPSessionAware;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Player;


public class OSGiGatewayServlet extends HttpServlet {
	ServiceUtility su = new ServiceUtility();
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		writePlayerList(req, resp);

	}
	private String writePlayerList(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		IReportEngine engine = su.getService(IReportEngine.class);
		//einfach mal zum zeigen, dass es geht
		IReport report = engine.getRepository().getReport("playerlist");
		HttpSession session = req.getSession(true);
		IReportResult result = engine.generate(report, su.getDaoServiceByEntity(Player.class).getAll(), new NullProgressMonitor());
		IHTTPSessionAware sessionadapter = AdapterUtility.getAdaptedObject(result, IHTTPSessionAware.class);
		if(sessionadapter!=null){
			sessionadapter.setSession(session);
		}
		ByteArrayOutputStream out;
		BufferedOutputStream bout = new BufferedOutputStream(out = new ByteArrayOutputStream());
		try {
			result.export(ExportType.HTML, bout);
			bout.flush();
			resp.getOutputStream().write(out.toByteArray());
			resp.getOutputStream().flush();

		}finally{
			out.close();
			bout.close();
			bout = null;
			out = null;
		}
		return null;
	}
	
	private String getPlayerLink(){
		return "<a href=players>Player List</a>";
	}
}
