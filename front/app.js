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
        updateContent(document.getElementById("match-code"), message.body);
    });

    stompClient.publish({
        destination: "/app/create",
        body: JSON.stringify(formData)
    });
}

function join(formData) {
    stompClient.subscribe(`/topics/created/${formData.matchCode}`, (message) => {
        updateContent(document.getElementById("join-message"), message.body);
    });
    stompClient.publish({
        destination: '/app/join',
        body: JSON.stringify(formData)
    })
}

function getPlayers(formData) {
    stompClient.subscribe(`/topics/created/players/${formData.matchCode}`, (message) => {
        updateContent(document.getElementById("players-message"), message.body);
    });
    stompClient.publish({
        destination: `/app/players/${formData.matchCode}`,
        body: formData.matchCode
    })
}

function updateContent(element, message) {
    element.innerHTML = message;
}

function matchBegins() {
    $("#match-status").inner("Started");
}

function endMatch() {
    $("#match-status").append("Over");
}

function hideElementBttn(button,element) {
    element.toggle();
    button.text(`${element.is(":visible") ? "Hide" : "Show"} ${button.name()}`);
}

function sendHeartbeat(formData) {
    console.log(`formData do Heartbeat: ${Object.entries(formData)}`);
    stompClient.subscribe(`/topics/created/heartbeat/${formData.matchCode}`, (message) => {
        console.log(`Mensagem recebida no heartbeat: ${message.body}`);
        updateContent(document.getElementById("match-status"), message.body);
    });

    stompClient.publish({
        destination: '/app/heartbeat',
        body: JSON.stringify(formData)
    })
}

$(function () {
    $("#connection-hide").click(() => hideElementBttn($("#connection-hide"), $("#connection")));

    $("#creation-hide").click(() => hideElementBttn($("#creation-hide"),$("#creation")));
    $("#create-form").submit((e) => {
        e.preventDefault();
        let formData = getFormDataObj($("#create-form").serializeArray());
        createMatch(formData);
    })

    $("#join-form").on('submit', (e) => {
        e.preventDefault();
        let formData = getFormDataObj($("#join-form").serializeArray());
        join(formData);
    });
    $("#join-hide").click(() => hideElementBttn($("#join-hide"), $("#joining")));

    $("#heartbeat-form").submit((e) => {
        e.preventDefault();
        let formData = getFormDataObj($("#heartbeat-form").serializeArray());
        sendHeartbeat(formData);
    })
    $("#heartbeat-hide").click(() => hideElementBttn($("#heartbeat-hide"), $("#heartbeat")));

    $('#players-form').submit((e) => {
        e.preventDefault();
        const formData = getFormDataObj($('#players-form').serializeArray());
        getPlayers(formData);
    })
    $("#players-hide").click(() => hideElementBttn($("#players-hide"), $("#players")));

    $("#connect").click(() => connect());

    $("#disconnect").click(() => disconnect());
});