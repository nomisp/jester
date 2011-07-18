 <%@page contentType="text/html" import="java.util.*" %>
 <%@page import="ch.jester.model.*" %>
 <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--  http://www.java-samples.com/jsp  -->  
<html>
<body>
<jsp:useBean id="tournaments" class="ch.jester.server.jspmodel.Tournaments"/>
<h1>Tournaments:</h1>
<UL>
<%
    Iterator<Tournament> iterator = tournaments.getTournaments().iterator();
    while (iterator.hasNext()) {
        Tournament t = iterator.next();
%>
        <LI> <%= t.getName() %> / <%= t.getId() %> </LI>
<%
		Iterator<Category> catIt = t.getCategories().iterator();
		while(catIt.hasNext()){
			Category cat = catIt.next();
			%>
				
				<LI> <%= cat.getDescription() %> / <%= cat.getId() %>  </LI>
			
			<%
		}

    }
%>
</UL>

</body>
</html>  