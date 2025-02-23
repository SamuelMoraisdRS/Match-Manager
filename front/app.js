const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/basic-broker'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    updateContent(frame);
    stompClient.subscribe('/topics/created', (message) => {
        console.log(`Mensagem recebida pelo back: ${message.body}`);
        updateContent(message);
    });
    stompClient.publish({
        destination: "/app/create",
        body: JSON.stringify({ 'message': $("#message").val() })
    });
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
    if (connected) {
        $("#content").show();
    }
    else {
        $("#content").hide();
    }
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.publish({
        destination: "/app/create",
        body: JSON.stringify({ 'message': $("#message").val() })
    });
    $("#message").val("");
}

function frameToString(frame) {
    let content= "";
    Object.entries(frame).forEach((entry) => {
        content = content.concat(`${entry[0]} : ${entry[1]}\n` )
        console.log(`Content: ${content}`);
    })
    return content;
}

function updateContent(message) {
    console.log(Object.entries(message));
    let newMsg = document.createElement("div");
    newMsg.innerText = frameToString(message);
    document.getElementById("content").appendChild(newMsg, document.createElement("br"));
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());
});