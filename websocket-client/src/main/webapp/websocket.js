var ws = new WebSocket("ws://127.0.0.1:8083/?ticket=xxxxx");
var statusBar = document.getElementById("statusBar");
var hintBar = document.getElementById("hintBar");
var contentField = document.getElementById("content");
var messageBoard = document.getElementById("messageBoard");
var nickField = document.getElementById("nick");
var node;
var nick;
var connected = false;
var binded = false;
var DOMAIN = "yk.im";
var RESOURCE = "ws";
var fetchReceiversUrl = "http://127.0.0.1:8084/easychat/user/list";
ws.onopen = function() {
    setStatusBar("Connected!");
    connected = true;
};

ws.onmessage = function (evt) {
	var data = evt.data;
	var packet;
	try {
		packet = JSON.parse(data);
	} catch(e) {
		setHintBar(data);
		return;
	}
	if (packet.type == PacketType.BIND) {
		processBind(packet.payload);
	} else if (packet.type == PacketType.MESSAGE) {
		processMessage(packet.payload);
	} else if (packet.type == PacketType.REPLY) {
		processReply(packet.payload);
	}
};

ws.onclose = function() {
    alert("Closed!");
    setStatusBar("Disconnected");
    binded = false;
    connected = false;
};

ws.onerror = function(err) {
    alert("Error: " + err + ", plz refresh!");
};

function setStatusBar(msg) {
	statusBar.innerHTML=msg;
}

function setHintBar(msg) {
	hintBar.innerHTML=msg;
}

/*
 *============process for packet============
 */
function processBind(payload) {
	var bind = JSON.parse(payload);
	if (bind.code == '200') {
		binded = true;
		node = bind.nickName;
		nick = bind.nickName;
		setStatusBar("Binded! nick:" + nick);
	} else {
		alert('bind failed, error : ' + bind.message);
		binded = false;
		setStatusBar("Unbinded!");
	}
}

function processMessage(payload) {
	var message = JSON.parse(payload);
	var records = messageBoard.value;
	if (records == '') {
		messageBoard.value = message.from + ":" + message.body;
	} else {
		messageBoard.value = records + "\r\n" + message.from + ":" + message.body;
	}
}

function processReply(payload) {
	var reply = JSON.parse(payload);
	if (reply.code == 'r1') {
		setHintBar("message has sent to chat server");
	} else if (reply.code == 'r2') {
		setHintBar("message has been received");
	}
	
}