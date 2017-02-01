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
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<!-- <script src="/static/JS/jquery-1.11.3.js"></script> -->
<script>
  $(function() {
    var availableTags = [
      "ActionScript",
      "AppleScript",
      "Asp",
      "BASIC",
      "C",
      "C++",
      "Clojure",
      "COBOL",
      "ColdFusion",
      "Erlang",
      "Fortran",
      "Groovy",
      "Haskell",
      "Java",
      "JavaScript",
      "Lisp",
      "Perl",
      "PHP",
      "Python",
      "Ruby",
      "Scala",
      "Scheme"
    ];
    function split( val ) {
      return val.split( /,\s*/ );
    }
    function extractLast( term ) {
      return split( term ).pop();
    }
 
    $( 	"${it.allTags}" )
    // don't navigate away from the field on tab when selecting an item
    .bind( "keydown", function( event ) {
      if ( event.keyCode === $.ui.keyCode.TAB &&
          $( this ).autocomplete( "instance" ).menu.active ) {
        event.preventDefault();
      }
    })
    .autocomplete({
      minLength: 0,
//       source: function( request, response ) {
//         // delegate back to autocomplete, but extract the last term
//         response( $.ui.autocomplete.filter(
//           availableTags, extractLast( request.term ) ) );
//       },

        source: function( request, response ) {
          $.getJSON( "/rest/v1/player/completion/term/" + extractLast( request.term ), {
            term: extractLast( request.term )
          }, response );
        },
        search: function() {
          // custom minLength
          var term = extractLast( this.value );
          if ( term.length < 2 ) {
            return false;
          }
        },


      focus: function() {
        // prevent value inserted on focus
        return false;
      },
      select: function( event, ui ) {
        var terms = split( this.value );
        // remove the current input
        terms.pop();
        // add the selected item
        terms.push( ui.item.value );
        // add placeholder to get the comma-and-space at the end
        terms.push( "" );
        this.value = terms.join( "" );
        return false;
      }
    });
  });
  </script>


<title>ELO: Match</title>
</head>
<body>
	<table>
		<caption>Composition du match</caption>

		<tr>
			<th colspan="2" style="width: 250px; color: blue;">BLEUS</th>
			<th colspan="2" style="width: 250px; color: red;">ROUGES</th>
		</tr>
		<c:forEach begin="1" end="${it.taille}" var="i">
			<tr>
				<td>Joueur ${i}</td>
				<td><div class="ui-widget"><input type="text" value="${it.tm.getBluePlayer(i-1)}"
					id="b${i}" style="width: 150px"></div></td>
				<td>Joueur ${i}</td>
				<td><div class="ui-widget"><input type="text" value="${it.tm.getRedPlayer(i-1)}"
					id="r${i}" style="width: 150px"></div></td>
			</tr>
		</c:forEach>

	</table>

	<br />
	<table style="border: 0px">
		<tr>
			<td style="font-weight: bold; color: blue; border: 0px">Nombre
				de joueurs :</td>
			<td style="width: 75px; border: 0px"><input type="text"
				id="${p.name}-${t}" name="elo"
				onchange="javascript:reloadMatch(this.value)" value="${it.taille}">
			</td>
		</tr>
		<tr>
			<td style="font-weight: bold; color: blue; border: 0px">Status :</td>
			<td style="width: 275px; border: 0px">
				<div id="status">${it.status}</div>
			</td>
		</tr>
	</table>


	<br />
	<br />
	<table style="border: 0px">
		<caption style="font-weight: bold;">SCORE</caption>
		<tr>
			<td style="font-weight: bold; color: blue; border: 0px">BLEU</td>
			<td style="width: 100px; border: 0px"><input id="scoreB"
				type="text" value=""></td>
			<td style="width: 100px; border: 0px"><input id="scoreR"
				type="text" value=""></td>
			<td style="font-weight: bold; color: red; border: 0px">ROUGE</td>
		</tr>
		<tr>
			<td colspan="2" style="font-weight: bold; border: 0px">Créer les
				joueurs ?</td>
			<td colspan="2" style="border: 0px"><input type="radio"
				name="create" value="true" id="true" checked="checked" /> <label
				for="true">oui</label> <input type="radio" name="create"
				value="false" id="false" /> <label for="false">non</label></td>
		</tr>
	</table>
	<br />
	<br />
	<table>
		<tr>
			<td style="font-weight: bold; border: 0px">ELO :</td>
			<c:forEach var="t" items="${it.elotypes}">
				<tr>
					<td style="width: 50px; border: 0px"></td>
					<td style="width: 150px; border: 0px; text-align: left;"><input
						type="radio" name="elo" value="${t}" id="${t}" /> <label
						for="${t}">${t}</label></td>
					</trs>
			</c:forEach>
		</tr>
	</table>
	<br />
	<input type="submit" value="Submit"
		style="font-weight: bold; margin-left: 45%; width: 10%"
		onclick="javascript:submitMatch(${it.taille})">
</body>
</html>