package ch.jester.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.core.runtime.NullProgressMonitor;

import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.adaptable.IHierarchyAdapter;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;
import ch.jester.commonservices.api.web.IHTTPSessionAware;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Tournament;


public class OSGiGatewayServlet extends HttpServlet {
	private static final long serialVersionUID = 2407211936729344173L;
	ServiceUtility su = new ServiceUtility();
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String aliasParameter = req.getParameter("reportalias");
		String categoryParameter = req.getParameter("category");
		String tournamentParameter = req.getParameter("tournament");
		IReport report = fetchReport(aliasParameter);
		
		Category cat = fetchInputCategory(categoryParameter);
		Tournament tournament = fetchInputTournament(tournamentParameter);
		
		Collection<?> inputCollection = null;
		if(tournament!=null){
			inputCollection = tryGetInput(tournament, report.getInputBeanClass());
		}else if(cat!=null){
			inputCollection = tryGetInput(cat, report.getInputBeanClass());
		}
		generateReport(req, resp, report, inputCollection);
	}
	
	private Collection<?> tryGetInput(IHierarchyAdapter adapter, Class<?> c){
		return adapter.getChildrenCollection(c);
	}
	
	private IReport fetchReport(String pAlias){
		IReportEngine engine = su.getService(IReportEngine.class);
		IReport report = engine.getRepository().getReport(pAlias);
		return report;
	}
	
	private Category fetchInputCategory(String pId){
		List<Tournament> tlist = su.getDaoServiceByEntity(Tournament.class).executeNamedQuery(Tournament.QUERY_GETALLACTIVE);
		for(Tournament t:tlist){
			for(Category c:t.getCategories()){
				if(c.getId().toString().equals(pId)){
					return c;
				}
			}
		}
		return null;
	}
	private Tournament fetchInputTournament(String pId){
		List<Tournament> tlist = su.getDaoServiceByEntity(Tournament.class).executeNamedQuery(Tournament.QUERY_GETALLACTIVE);
		for(Tournament t:tlist){
			if(t.getId().toString().equals(pId)){
				return t;
			}
		}
		return null;
	}
	
	private String generateReport(HttpServletRequest req, HttpServletResponse resp, IReport report, Collection<?> inputCollection) throws IOException{
		IReportEngine engine = su.getService(IReportEngine.class);
		//einfach mal zum zeigen, dass es geht
		HttpSession session = req.getSession(true);
		IReportResult result = engine.generate(report, inputCollection, new NullProgressMonitor());
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
}
