<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel=stylesheet type="text/css" href="/static/CSS/players.css">
<script language="javascript" type="text/javascript"
	src="/static/JS/player.js"></script>
<script src="/static/JS/jquery-1.11.3.js"></script>
<title>ELO: All Players</title>
</head>
<body >

	<table>
		<caption>Connexions enregistrés</caption>

		<tr>
			<th style="width: 250px">IP <a style="float: right"
				href="/rest/v1/service/connections/sort/ip/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a>
			</th>
			<th style="width: 250px">derniere connexion <a style="float: right"
				href="/rest/v1/service/connections/sort/lastco/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a>
			</th>
			<th style="width: 250px">occurences 30sec <a style="float: right"
				href="/rest/v1/service/connections/sort/occ/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a>
			</th>
			<th style="width: 250px">occurences totales <a style="float: right"
				href="/rest/v1/service/connections/sort/occall/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a>
			</th>
			<th style="width: 250px">alertes spam <a style="float: right"
				href="/rest/v1/service/connections/sort/spam/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a>
			</th>
			<th style="width: 250px">connections <a style="float: right"> 
			</a>
			</th>
		</tr>
		
		<c:forEach var="c" items="${it.coData}">
			<tr>
				<td>${c.ip}</td>
				<td>${c.getLastConnectionDateAsString()}</td>
				<td>${c.occurences}</td>
				<td>${c.totalOccurences}</td>
				<td>${c.spamAlert}</td>
				<td>
				<script>
					$(document).ready(function(){
						$("#button-${c.getIpNoPoint()}").click(function(){
						    $("#historique-${c.getIpNoPoint()}").toggle(400);
						});
					});
					</script>
					<button id="button-${c.getIpNoPoint()}">Voir</button>
					<table id="historique-${c.getIpNoPoint()}" style="display: none">
						<tr>
							<th>Historique</th>
						</tr>
						<c:forEach var="h" items="${c.getAllConnectionsAsString()}">
							<tr>
								<td>${h}</td>
							</tr>
						</c:forEach>
					</table>
				</td>
			
		</c:forEach>
	</table>
</body>
</html>