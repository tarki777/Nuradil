let isLogin = true;

const formTitle = document.getElementById('formTitle');
const submitBtn = document.getElementById('submitBtn');
const switchForm = document.getElementById('switchForm');
const message = document.getElementById('message');

switchForm.addEventListener('click', () => {
    isLogin = !isLogin;
    formTitle.textContent = isLogin ? 'Login' : 'Register';
    submitBtn.textContent = isLogin ? 'Login' : 'Register';
    switchForm.textContent = isLogin ? "Don't have an account? Register" : "Already have an account? Login";
    message.textContent = '';
});

submitBtn.addEventListener('click', async () => {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    if (!username || !password) {
        message.textContent = 'Please fill all fields';
        return;
    }

    const endpoint = isLogin ? '/auth/login' : '/auth/register';
    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        const data = await response.json();

        if (!response.ok) {
            message.textContent = data.message || 'Error';
            return;
        }

        localStorage.setItem('token', data.token);
        message.style.color = '#00ff7f';
        message.textContent = isLogin ? 'Login successful!' : 'Registration successful!';
        
        setTimeout(() => {
            window.location.href = '/students.html';
        }, 1000);
    } catch (err) {
        message.textContent = 'Network error';
    }
});
