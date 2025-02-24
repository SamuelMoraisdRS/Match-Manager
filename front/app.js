const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/basic-broker'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    let status = connected ? "<strong>Connected</strong>" : "<strong>Disconnected</strong>"
    $("#connection-status").html(status);
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function createMatch() {
    stompClient.subscribe('/topics/created', (message) => {
        console.log(`Mensagem recebida pelo back: ${message.body}`);
        updateContent(document.getElementById("match-code"), message.body);
    });

    stompClient.publish({
        destination: "/app/create",
        body: JSON.stringify({ 'message': $("#message").val() })
    });
}

function join(matchCode) {
    stompClient.publish({
        destination: '/app/join',
        body: matchCode
    })
    stompClient.subscribe(`/topics/created/${matchCode}`, (message) => {
        console.log(`Mensagem do Join Match: ${message.body}`);
        updateContent(document.getElementById("join-message"), message.body);
    });

}

function frameToString(frame) {
    let content = "";
    Object.entries(frame).forEach((entry) => {
        content = content.concat(`${entry[0]} : ${entry[1]}\n`)
        console.log(`Content: ${content}`);
    })
    return content;
}

function updateContent(element, message) {
    let newMsg = document.createElement("div");
    newMsg.innerText = message;
    element.appendChild(newMsg);
}

$(function () {

    $("#join-form").on('submit', (e) => {
        e.preventDefault();
        let value = document.getElementById('join-input').value;
        console.log(`valor no form : ${value}`)
        join(value);
    });
    $("#connect").click(() => connect());
    $("#create").click(() => createMatch());
    $("#disconnect").click(() => disconnect());
});