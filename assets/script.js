
var canvas = null;

function update() {
	var currentdate = new Date();
	canvas.innerHTML = currentdate.getHours() + ":"  
                + currentdate.getMinutes() + ":" 
                + currentdate.getSeconds();
}

function init() {
	canvas = document.getElementById('canvas');
	window.setInterval(update, 1000);
}
