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
		<caption>Joueurs enregistrés</caption>

		<tr>
			<th style="width: 250px">Nom <a style="float: right"
				href="/rest/v1/player/showall/html/sort/player/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a>
			</th>
			<c:forEach var="t" items="${it.elotypes}">
				<th style="width: 150px">ELO ${t} <a style="float: right"
				href="/rest/v1/player/showall/html/sort/${t}/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a></th>
			</c:forEach>
			<th style="width: 150px">ELO moyen <a style="float: right"
				href="/rest/v1/player/showall/html/sort/moyenne/${it.previous}/${it.updown}"> <img
					src="/static/images/sort.png">
			</a></th>
			<th style="width: 250px">Historique</th>
			<th style="width: 100px">Status</th>
		</tr>
		
		
			<c:if test="${it.modif}">
				<tr>
						<td>
					<input type="text" id="createPlayer" name="createPlayer"
								value="" style="width: 200px">
							<a style="float: right"
								onclick="javascript:createPlayer()"> <img
								src="/static/images/plus.png">
							</a>
					</td>
						<c:forEach var="t" items="${it.elotypes}">
							<td></td>
						</c:forEach>
						<td>
						</td>
						<td style="width: 100px" id="status-createPlayer"></td>
					</tr>

			</c:if>
		<c:forEach var="p" items="${it.players}">
			<tr>
				<td>${p.name} 
				<c:if test="${it.delete}">
					<a style="float: right"
						href="/rest/v1/player/delete/${p.name}"> <img
						src="/static/images/delete.jpg">
					</a>
				</c:if>
			</td>
				<c:forEach var="t" items="${it.elotypes}">
					<td><c:if test="${it.modif}">
							<input type="text" id="${p.name}-${t}" name="elo"
								onchange="javascript:updateElo('${p.name}', '#status-${p.getNameNoPoint()}', '${t}', this.value)"
								value="${p.getElo(t)}">
						</c:if> <c:if test="${it.modif == false}">
							${p.getElo(t)}
						</c:if></td>
				</c:forEach>
				<td> ${p.getEloMoyenne()}</td>
				<td><script>
					$(document).ready(function(){
						$("#button-${p.getNameNoPoint()}").click(function(){
						    $("#historique-${p.getNameNoPoint()}").toggle(400);
						});
					});
					</script>
					<button id="button-${p.getNameNoPoint()}">Voir</button>
					<table id="historique-${p.getNameNoPoint()}" style="display: none">
						<tr>
							<th>Date</th>
							<c:forEach var="t" items="${it.elotypes}">
								<th>${t}</th>
							</c:forEach>
						</tr>
						<c:forEach var="h" items="${p.historiqueElo.values()}">
							<tr>
								<td>${h.jour}/${h.mois+1}/${h.annee}-${h.heure}:${h.minute}:${h.seconde}</td>
								<c:forEach var="t" items="${it.elotypes}">
									<td>${h.elo.getElo(t)}</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table></td>
				<td style="width: 100px" id="status-${p.getNameNoPoint()}"></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>