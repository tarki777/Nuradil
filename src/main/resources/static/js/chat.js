const chatBox = document.getElementById('chatBox');
const messageInput = document.getElementById('messageInput');
const sendBtn = document.getElementById('sendBtn');
const backBtn = document.getElementById('backBtn');
const status = document.getElementById('status');

const token = localStorage.getItem('token');
if (!token) {
    window.location.href = '/';
}

const wsProtocol = location.protocol === 'https:' ? 'wss' : 'ws';
const ws = new WebSocket(`${wsProtocol}://${location.host}/chat/ws?token=${token}`);

ws.onopen = () => {
    status.textContent = 'Connected';
    status.style.color = '#00ff7f';
};

ws.onmessage = (event) => {
    const data = JSON.parse(event.data);
    const div = document.createElement('div');
    div.innerHTML = `<strong>${data.user}:</strong> ${data.message}`;
    chatBox.appendChild(div);
    chatBox.scrollTop = chatBox.scrollHeight;
};

ws.onclose = () => {
    status.textContent = 'Disconnected';
    status.style.color = '#ff6347';
};

sendBtn.addEventListener('click', sendMessage);
messageInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') sendMessage();
});

backBtn.addEventListener('click', () => {
    window.location.href = '/students.html';
});

function sendMessage() {
    const message = messageInput.value.trim();
    if (!message) return;
    ws.send(JSON.stringify({ message }));
    messageInput.value = '';
}
