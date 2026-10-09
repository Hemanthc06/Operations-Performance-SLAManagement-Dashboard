let allEmployees = [];
let allTeams = [];

async function loadEmployees() {
    const tbody = document.getElementById('employeeBody');
    try {
        const res = await apiFetch('/api/employees');
        allEmployees = await res.json();
        renderList(allEmployees);
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="6" class="loading" style="color:#dc2626">Failed to load employees</td></tr>';
    }
}

async function loadTeams() {
    try {
        const res = await apiFetch('/api/teams');
        allTeams = await res.json();
        const select = document.getElementById('empTeam');
        select.innerHTML = '<option value="">— No Team —</option>' +
            allTeams.map(t => `<option value="${t.id}">${escapeHtml(t.name)}</option>`).join('');
    } catch (e) {
        console.error('Teams load failed', e);
    }
}

function renderList(list) {
    const tbody = document.getElementById('employeeBody');
    if (list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="loading">No employees yet</td></tr>';
        return;
    }
    tbody.innerHTML = list.map(e => `
        <tr>
            <td><strong>${escapeHtml(e.fullName)}</strong></td>
            <td>${escapeHtml(e.email || '—')}</td>
            <td>${escapeHtml(e.designation || '—')}</td>
            <td>${escapeHtml(e.department || '—')}</td>
            <td>${e.teamName ? escapeHtml(e.teamName) : '<em style="color:#94a3b8">None</em>'}</td>
            <td style="text-align:right">
    ${Auth.can('employee.update') ? `<button class="action-btn edit" onclick="editEmployee(${e.id})">Edit</button>` : ''}
    ${Auth.can('employee.delete') ? `<button class="action-btn delete" onclick="deleteEmployee(${e.id})">Delete</button>` : ''}
</td>
        </tr>
    `).join('');
}

function filterList() {
    const q = document.getElementById('searchBox').value.toLowerCase();
    const filtered = allEmployees.filter(e =>
        e.fullName.toLowerCase().includes(q) ||
        (e.email && e.email.toLowerCase().includes(q)) ||
        (e.department && e.department.toLowerCase().includes(q))
    );
    renderList(filtered);
}

function openForm(emp) {
    document.getElementById('employeeModal').style.display = 'flex';
    document.getElementById('modalTitle').textContent = emp ? 'Edit Employee' : 'New Employee';
    document.getElementById('empId').value = emp ? emp.id : '';
    document.getElementById('empFullName').value = emp ? emp.fullName : '';
    document.getElementById('empEmail').value = emp ? (emp.email || '') : '';
    document.getElementById('empDesignation').value = emp ? (emp.designation || '') : '';
    document.getElementById('empDepartment').value = emp ? (emp.department || '') : '';
    document.getElementById('empTeam').value = emp && emp.teamId ? emp.teamId : '';
}

function editEmployee(id) {
    const emp = allEmployees.find(e => e.id === id);
    if (emp) openForm(emp);
}

function closeForm() {
    document.getElementById('employeeModal').style.display = 'none';
}

async function saveEmployee() {
    const id = document.getElementById('empId').value;
    const fullName = document.getElementById('empFullName').value.trim();
    if (!fullName) { alert('Full Name is required'); return; }

    const teamVal = document.getElementById('empTeam').value;
    const body = {
        fullName,
        email: document.getElementById('empEmail').value.trim() || null,
        designation: document.getElementById('empDesignation').value.trim() || null,
        department: document.getElementById('empDepartment').value.trim() || null,
        teamId: teamVal ? Number(teamVal) : null
    };

    const url = id ? `/api/employees/${id}` : '/api/employees';
    const method = id ? 'PUT' : 'POST';

    try {
        const res = await apiFetch(url, { method, body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Save failed');
        closeForm();
        loadEmployees();
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

async function deleteEmployee(id) {
    if (!confirm('Delete this employee?')) return;
    try {
        await apiFetch(`/api/employees/${id}`, { method: 'DELETE' });
        loadEmployees();
    } catch (e) {
        alert('Failed to delete');
    }
}
// Fill sidebar
(function fillSidebar() {
    const nameEl = document.getElementById('userLabelSide');
    const roleEl = document.getElementById('userRoleSide');
    const initialsEl = document.getElementById('avatarInitials');
    if (nameEl) nameEl.textContent = Auth.getFullName();
    if (roleEl) roleEl.textContent = Auth.getRole();
    if (initialsEl) {
        initialsEl.textContent = Auth.getFullName()
            .split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();
    }
})();


loadEmployees();
loadTeams();