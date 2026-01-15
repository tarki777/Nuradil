const studentsList = document.getElementById('studentsList');
const logoutBtn = document.getElementById('logoutBtn');
const chatBtn = document.getElementById('chatBtn');
const message = document.getElementById('message');

const token = localStorage.getItem('token');
if (!token) {
    window.location.href = '/';
}

async function fetchStudents() {
    try {
        const response = await fetch('/students', {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!response.ok) {
            const data = await response.json();
            message.textContent = data.message || 'Failed to fetch students';
            return;
        }

        const students = await response.json();
        studentsList.innerHTML = '';

        students.forEach(s => {
            const div = document.createElement('div');
            div.style.marginBottom = '10px';
            div.innerHTML = `
                <strong>${s.name}</strong> (Age: ${s.age})
                ${s.role === 'admin' ? '' : `<button onclick="deleteStudent(${s.id})">Delete</button>`}
            `;
            studentsList.appendChild(div);
        });

    } catch (err) {
        message.textContent = 'Network error';
    }
}

async function deleteStudent(id) {
    try {
        const response = await fetch(`/students/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        const data = await response.json();
        if (!response.ok) {
            message.textContent = data.message || 'Failed to delete';
            return;
        }

        message.style.color = '#00ff7f';
        message.textContent = data.message;
        fetchStudents();
    } catch (err) {
        message.textContent = 'Network error';
    }
}

logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('token');
    window.location.href = '/';
});

chatBtn.addEventListener('click', () => {
    window.location.href = '/chat.html';
});

fetchStudents();
