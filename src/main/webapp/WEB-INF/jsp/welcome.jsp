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
<title>ELO</title>
</head>
<body >

	<table>
		<caption>Toutes les sections</caption>

			<tr style="width: 200px">
				<td style="width: 200px">Voir tous les joueurs
			</td>
				<td style="width: 100px"><a href="/rest/v1/player/showall/html/">LIEN</a></td>
			</tr>
			<tr style="width: 200px">
				<td style="width: 200px">Enregistrer un match
			</td>
				<td style="width: 100px"><a href="/rest/v1/elo/match/manage/">LIEN</a></td>
			</tr>
			<tr style="width: 200px">
				<td style="width: 200px">Générer une team
			</td>
				<td style="width: 100px"><a href="/rest/v1/team/makehtml">LIEN</a></td>
			</tr>
	</table>
</body>
</html>