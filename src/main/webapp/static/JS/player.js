function updateElo(nom, status, elo, val){
	var setelo = '/rest/v1/player/setelo/name/' + nom + 
	'/elo/' + val + 
	'/type/' + elo + 
	'/save/false';
	$.ajax({
		url : setelo,
		type : 'GET',
		success: function (response){
			$(status).html("MaJ OK");
		},
		error:function (xhr, ajaxOptions, thrownError){
			$(status).append("Erreur MaJ");
		}
	});
}
function createPlayer(){
	var name = $("#createPlayer").val();
	if (name == undefined || !name || /^\s*$/.test(name)){
		$("#status-createPlayer").append("Erreur de nom");
		return;
	}
	var reload = '/rest/v1/player/create/' + name;
	window.location.href = reload;
	
}



function reloadMatch(val){
	var reload = '/rest/v1/elo/match/manage/' + val;
	window.location.href = reload;
}

function reloadTeamMaking(val){
	var reload = '/rest/v1/team/makehtml/' + val;
	window.location.href = reload;
}

function submitMatch(nb){

	var blue = "";
	var red = "";
	var no_b = true;
	var no_r = true;
	for (var int = 1; int <= nb; int++) {
		b = $('#b' + int).val();
		if (b != ""){
			no_b = false;
		}
		r = $('#r' + int).val();
		if (r != ""){
			no_r = false
		}
		blue = blue + b;
		red = red + r;
		if (int < nb){
			blue = blue + ",";
			red = red + ",";
		}
	}
	
	if (no_b){
		$('#status').html("joueurs bleu vide")
		return;
	}
	if (no_r){
		$('#status').html("joueurs rouge vide")
		return;
	}
	sb = $('#scoreB').val();	
	if (sb == ""){
		$('#status').html("score bleu vide")
		return;
	}
	if (isNaN(sb)){
		$('#status').html("score bleu non numerique")
		return;
	}

	sr = $('#scoreR').val();
	if (sr == ""){
		$('#status').html("score rouge vide")
		return;
	}
	if (isNaN(sr)){
		$('#status').html("score rouge non numerique")
		return;
	}

	type = $('input[name=elo]:checked').val();
	if (type == ""){
		$('#status').html("type de match vide")
		return;
	}
	var w;
	var l;
	var s;
	if (sb > sr){
		s = sb + "-" + sr;
		w = blue;
		l = red;
	} else {
		s = sr + "-" + sb;
		w = red;
		l = blue;
	}

	create = $('input[name=create]:checked').val();

	var reload = "/rest/v1/elo/match/manage/"+nb+"/winners/"+w+"/loosers/"+l+"/score/"+s+"/type/"+type+"/create/"+create;
	window.location.href = reload;
}



function submitPlayersForTeam(nb){

	var blue = "";
	for (var int = 1; int <= nb; int++) {
		b = $('#b' + int).val();
		if (b != ""){
			b = $('#b' + int).val();
			blue = blue + b + ",";
		}
	}
	if (blue.charAt(blue.length-1) == ','){
		blue = blue.substring(0,blue.length-1);
	}
	if (blue == ""){
		var reload = "/rest/v1/team/makehtml/"+nb;
		window.location.href = reload;
	} else {
		type = $('input[name=elo]:checked').val();
		if (type == "" || type == undefined){
			$('#status').html("type de match vide")
			return;
		}

		create = $('input[name=create]:checked').val();

		var reload = "/rest/v1/team/makehtml/players/"+blue+"/type/"+type+"/create/"+create;
		window.location.href = reload;
	}
}