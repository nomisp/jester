 <%@page contentType="text/html" import="java.util.*" %>
 <%@page import="ch.jester.model.*" %>
 <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--  http://www.java-samples.com/jsp  -->  
<html>
<body>
<img src="/icon128x128.jpg"/>
<jsp:useBean id="tournaments" class="ch.jester.server.jspmodel.Tournaments"/>
<h1>Turniere:</h1>
<UL>
<%
    Iterator<Tournament> iterator = tournaments.getTournaments().iterator();
    while (iterator.hasNext()) {
        Tournament t = iterator.next();
%>
        <LI> <%= t.getName() %> / <%= t.getId() %> </LI>
        <r/>
<%
		Iterator<Category> catIt = t.getCategories().iterator();
		while(catIt.hasNext()){
			Category cat = catIt.next();
			%>
				
				<LI> <%= cat.getDescription() %> <%= tournaments.getCategoryLinks(cat) %>  </LI>
			
			<%
		}

    }
%>
</UL>

</body>
</html>  