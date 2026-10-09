// Redirect if already logged in
if (Auth.isLoggedIn()) {
    window.location.href = '/dashboard/dashboard.html';
}

function showTab(tab, event) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    if (event && event.target) event.target.classList.add('active');

    if (tab === 'login') {
        document.getElementById('loginTab').style.display = 'block';
        document.getElementById('registerTab').style.display = 'none';
    } else {
        document.getElementById('loginTab').style.display = 'none';
        document.getElementById('registerTab').style.display = 'block';
    }
    document.getElementById('errorMsg').textContent = '';
}

async function login() {
    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value;

    if (!username || !password) {
        showError('Please enter username and password');
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();

        if (!res.ok) {
            showError(data.error || 'Login failed');
            return;
        }

        Auth.setSession(data);
        window.location.href = '/dashboard/dashboard.html';
    } catch (e) {
        showError('Network error: ' + e.message);
    }
}

async function register() {
    const body = {
        fullName: document.getElementById('regFullName').value.trim(),
        email: document.getElementById('regEmail').value.trim(),
        username: document.getElementById('regUsername').value.trim(),
        password: document.getElementById('regPassword').value,
        role: document.getElementById('regRole').value
    };

    if (!body.username || !body.password || !body.fullName) {
        showError('Username, password and full name required');
        return;
    }

    try {
        const res = await fetch(API_BASE + '/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const data = await res.json();

        if (!res.ok) {
            showError(data.error || 'Registration failed');
            return;
        }

        Auth.setSession(data);
        window.location.href = '/dashboard/dashboard.html';
    } catch (e) {
        showError('Network error: ' + e.message);
    }
}

function showError(msg) {
    document.getElementById('errorMsg').textContent = msg;
}

document.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
        const registerVisible = document.getElementById('registerTab').style.display !== 'none';
        if (registerVisible) register();
        else login();
    }
});