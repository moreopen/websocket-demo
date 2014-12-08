var VERSION = "1.0";
var PacketType = {
	BIND : "1",
	MESSAGE : "2",
	REPLY : "3"
}

function Packet(type, payload) {
	this.type = type;
	this.payload = payload;
}

function Message(from, to, id, body) {
	this.from = from;
	this.to = to;
	this.id = id;
	this.body = body;
}

function Bind(nickName, domain, platform, version) {
	this.nickName = nickName;
	this.domain = domain;
	this.platform = platform;
	this.version = version;
}