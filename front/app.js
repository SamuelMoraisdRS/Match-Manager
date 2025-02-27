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

function getFormDataObj(formArray) {
    let formData = {};
    formArray.forEach((entry) => formData[entry.name] = entry.value);
    return formData;
}

function createMatch(formData) {

    stompClient.subscribe('/topics/created', (message) => {
        console.log(`Mensagem recebida pelo back: ${message.body}`);
        updateContent(document.getElementById("match-code"), message.body);
    });

    stompClient.publish({
        destination: "/app/create",
        body: JSON.stringify(formData)
    });
}

function join(formData) {

    stompClient.publish({
        destination: '/app/join',
        body: JSON.stringify(formData)
    })
    stompClient.subscribe(`/topics/created/${formData.matchCode}`, (message) => {
        console.log(`Mensagem do Join Match: ${message.body}`);
        updateContent(document.getElementById("join-message"), message.body);
    });

}

function updateContent(element, message) {
    let newMsg = document.createElement("div");
    newMsg.innerText = message;
    element.appendChild(newMsg);
}

$(function () {

    $("#join-form").on('submit', (e) => {
        e.preventDefault();
        let formData = getFormDataObj($("#join-form").serializeArray());
        join(formData);
    });
    $("#connect").click(() => connect());
    $("#create-form").submit((e) => {
        e.preventDefault();
        let formData = getFormDataObj($("#create-form").serializeArray());
        console.log(formData);
        createMatch(formData);
    })
    $("#disconnect").click(() => disconnect());
});