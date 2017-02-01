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
<title>ELO: Match</title>
</head>
<body>

	<table >
		<caption>Participants</caption>

		<tr>
			<th colspan="2" style="width: 250px; color:blue;">JOUEURS</th>
		</tr>
		<c:forEach begin="1" end="${it.taille}" var="i">
			<tr >
				<td>Joueur ${i}</td>
				<td><input type="text" value="" id="b${i}" style="width: 150px"></td>
			</tr>
		</c:forEach>

	</table>
	
	<br/> 
		<table style="border: 0px">
		<tr >
			<td style="font-weight: bold; color:blue;border: 0px">Nombre de joueurs :</td>
			<td style="width: 75px;border: 0px">
				<input type="text" id="${p.name}-${t}" name="elo"
					onchange="javascript:reloadTeamMaking(this.value)"
					value="${it.taille}">
			</td>
		</tr>
		<tr >
			<td style="font-weight: bold; color:blue;border: 0px">Status :</td>
			<td style="width: 275px;border: 0px">
				<div id="status">${it.status}</div>
			</td>
		</tr>
	</table>
	
	
	<br/> <br/>
	<table style="border: 0px">
		<tr >
			<td colspan="2"  style="font-weight: bold;border: 0px">Créer les joueurs ?</td>
			<td colspan="2" style="border: 0px">
				<input type="radio" name="create" value="true" id="true" checked="checked"/> <label for="true">oui</label>
				<input type="radio" name="create" value="false" id="false" /> <label for="false">non</label>
			</td>
		</tr>
	</table>
	<br/> <br/>
	<table>
	<tr>
		<td style="font-weight: bold;border: 0px">ELO :</td>
			<c:forEach var="t" items="${it.elotypes}">
				<tr><td style="width: 50px;border: 0px"></td>
					<td style="width: 150px;border: 0px;text-align: left;">
						<input type="radio" name="elo" value="${t}" id="${t}" />
						<label for="${t}">${t}</label>
					</td>
					</trs>
			</c:forEach>
		</tr>
	</table>
	<br/>
	<input type="submit" value="Submit" style="font-weight: bold;margin-left:45%;width:10%"
		onclick="javascript:submitPlayersForTeam(${it.taille})">
</body>
</html>