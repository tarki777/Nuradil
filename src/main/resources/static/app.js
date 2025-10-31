const chatBox = document.getElementById("chatBox");
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendBtn");

// Пример WebSocket подключения (меняем адрес на ваш сервер)
const socket = new WebSocket("ws://localhost:8080/chat");

socket.onopen = () => {
    appendMessage("Connected to server");
};

socket.onmessage = (event) => {
    appendMessage(event.data);
};

sendBtn.onclick = () => {
    const message = messageInput.value;
    if(message.trim() !== "") {
        socket.send(message);
        messageInput.value = "";
    }
};

function appendMessage(msg) {
    const p = document.createElement("p");
    p.textContent = msg;
    chatBox.appendChild(p);
    chatBox.scrollTop = chatBox.scrollHeight; // автоскролл вниз
}
