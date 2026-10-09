const API_BASE = 'http://localhost:8080';

const Auth = {
    getToken: () => localStorage.getItem('token'),
    getFullName: () => localStorage.getItem('fullName') || localStorage.getItem('username') || 'User',
    getRole: () => localStorage.getItem('role') || 'EMPLOYEE',
    setSession: (data) => {
        localStorage.setItem('token', data.token);
        localStorage.setItem('username', data.username);
        localStorage.setItem('fullName', data.fullName);
        localStorage.setItem('role', data.role);
    },
    isLoggedIn: () => !!localStorage.getItem('token'),
    logout: () => {
        localStorage.clear();
        window.location.href = '/index.html';
    }
};

async function apiFetch(url, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {})
    };
    const token = Auth.getToken();
    if (token) headers['Authorization'] = 'Bearer ' + token;

    const res = await fetch(API_BASE + url, { ...options, headers });

    if (res.status === 401) {
        Auth.logout();
        throw new Error('Session expired');
    }
    return res;
}

function escapeHtml(str) {
    if (str === null || str === undefined) return '';
    return String(str).replace(/[&<>"']/g, c => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    }[c]));
}

async function logout() {
    Auth.logout();
}
// Role helpers
Auth.isAdmin = () => Auth.getRole() === 'ADMIN';
Auth.isManager = () => Auth.getRole() === 'MANAGER';
Auth.isEmployee = () => Auth.getRole() === 'EMPLOYEE';

Auth.can = (action) => {
    const role = Auth.getRole();
    const perms = {
        'task.create':   ['ADMIN', 'MANAGER'],
        'task.update':   ['ADMIN', 'MANAGER'],
        'task.delete':   ['ADMIN', 'MANAGER'],
        'task.changeStatus': ['ADMIN', 'MANAGER', 'EMPLOYEE'],
        'employee.create':  ['ADMIN'],
        'employee.update':  ['ADMIN'],
        'employee.delete':  ['ADMIN'],
        'team.create':      ['ADMIN', 'MANAGER'],
        'team.update':      ['ADMIN', 'MANAGER'],
        'team.delete':      ['ADMIN'],
        'sla.create':       ['ADMIN', 'MANAGER'],
        'sla.delete':       ['ADMIN', 'MANAGER'],
        'issue.create':     ['ADMIN', 'MANAGER', 'EMPLOYEE'],
        'issue.delete':     ['ADMIN', 'MANAGER'],
        'escalation.create':['ADMIN', 'MANAGER'],
        'escalation.delete':['ADMIN', 'MANAGER'],
        'report.view':      ['ADMIN', 'MANAGER'],
    };
    return perms[action] ? perms[action].includes(role) : true;
};

// Show or hide elements based on role
function applyRolePermissions() {
    document.querySelectorAll('[data-role]').forEach(el => {
        const allowed = el.dataset.role.split(',').map(r => r.trim());
        if (!allowed.includes(Auth.getRole())) {
            el.style.display = 'none';
        }
    });

    document.querySelectorAll('[data-perm]').forEach(el => {
        if (!Auth.can(el.dataset.perm)) {
            el.style.display = 'none';
        }
    });
}
Auth.can = (action) => {
    const role = Auth.getRole();
    const perms = {
        'task.create':   ['ADMIN', 'MANAGER'],
        'task.update':   ['ADMIN', 'MANAGER'],
        'task.delete':   ['ADMIN', 'MANAGER'],
        'task.changeStatus': ['ADMIN', 'MANAGER', 'EMPLOYEE'],
        'employee.create':  ['ADMIN'],
        'employee.update':  ['ADMIN'],
        'employee.delete':  ['ADMIN'],
        'team.create':      ['ADMIN', 'MANAGER'],
        'team.update':      ['ADMIN', 'MANAGER'],
        'team.delete':      ['ADMIN'],
        'sla.create':       ['ADMIN', 'MANAGER'],
        'sla.delete':       ['ADMIN', 'MANAGER'],
        'issue.create':     ['ADMIN', 'MANAGER', 'EMPLOYEE'],
        'issue.delete':     ['ADMIN', 'MANAGER'],
        'escalation.create':['ADMIN', 'MANAGER'],
        'escalation.delete':['ADMIN', 'MANAGER'],
        'report.view':      ['ADMIN', 'MANAGER'],
    };
    return perms[action] ? perms[action].includes(role) : true;
};

function applyRolePermissions() {
    document.querySelectorAll('[data-role]').forEach(el => {
        const allowed = el.dataset.role.split(',').map(r => r.trim());
        if (!allowed.includes(Auth.getRole())) {
            el.style.display = 'none';
        }
    });

    document.querySelectorAll('[data-perm]').forEach(el => {
        if (!Auth.can(el.dataset.perm)) {
            el.style.display = 'none';
        }
    });
}

document.addEventListener('DOMContentLoaded', applyRolePermissions);

// Run once DOM is ready
document.addEventListener('DOMContentLoaded', applyRolePermissions);