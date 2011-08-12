 <%@page contentType="text/html" import="java.util.*" %>
 <%@page import="ch.jester.model.*" %>
 <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--  http://www.java-samples.com/jsp  -->  
<html>
<header><title>jester</title></header>
<link rel="icon" type="image/ico" href="favicon.ico"/>
<link rel="shortcut icon" href="favicon.ico"/>
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
        <LI> <%= t.getName() %>  <%= tournaments.getAllPlayersLink(t) %>, <%= tournaments.getRankingLink(t) %> </LI>
        <r/>
<%
		Iterator<Category> catIt = t.getCategories().iterator();
		while(catIt.hasNext()){
			Category cat = catIt.next();
			%>
				
				<LI> <%= cat.getDescription() %> <%= tournaments.getPairingLink(cat) %>  </LI>
			
			<%
		}

    }
%>
</UL>

</body>
</html>  