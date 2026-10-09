let allEscalations = [];
let allEmployees = [];
let allTasks = [];

async function loadEscalations() {
    const tbody = document.getElementById('escalationBody');
    try {
        const res = await apiFetch('/api/escalations');
        allEscalations = await res.json();
        renderList(allEscalations);
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="7" class="loading" style="color:#dc2626">Failed to load escalations</td></tr>';
    }
}

async function loadEmployees() {
    try {
        const res = await apiFetch('/api/employees');
        allEmployees = await res.json();
        const opts = allEmployees.map(e => `<option value="${e.id}">${escapeHtml(e.fullName)}</option>`).join('');
        document.getElementById('escEscalatedBy').innerHTML = '<option value="">— Escalated By —</option>' + opts;
        document.getElementById('escEscalatedTo').innerHTML = '<option value="">— Escalated To —</option>' + opts;
    } catch (e) {
        console.error('Employees load failed', e);
    }
}

async function loadTasks() {
    try {
        const res = await apiFetch('/api/tasks');
        allTasks = await res.json();
        document.getElementById('escTask').innerHTML =
            '<option value="">— Select Task —</option>' +
            allTasks.map(t => `<option value="${t.id}">${escapeHtml(t.title)}</option>`).join('');
    } catch (e) {
        console.error('Tasks load failed', e);
    }
}

function renderList(list) {
    const tbody = document.getElementById('escalationBody');
    if (list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="loading">No escalations found</td></tr>';
        return;
    }
    tbody.innerHTML = list.map(e => `
        <tr>
            <td><span class="escalation-reason" title="${escapeHtml(e.reason)}">${escapeHtml(e.reason)}</span></td>
            <td>${e.taskTitle ? escapeHtml(e.taskTitle) : '<em style="color:#94a3b8">—</em>'}</td>
            <td><span class="badge ${e.priority}">${e.priority}</span></td>
            <td>
                <select onchange="changeStatus(${e.id}, this.value)" style="padding:.3rem;border-radius:6px;border:1px solid #e2e8f0">
                    ${['TODO', 'IN_PROGRESS', 'REVIEW', 'DONE'].map(s =>
                        `<option value="${s}" ${e.status === s ? 'selected' : ''}>${s.replace('_', ' ')}</option>`
                    ).join('')}
                </select>
            </td>
            <td>${e.escalatedByName ? escapeHtml(e.escalatedByName) : '<em style="color:#94a3b8">—</em>'}</td>
            <td>${e.escalatedToName ? escapeHtml(e.escalatedToName) : '<em style="color:#94a3b8">—</em>'}</td>
            <td style="text-align:right">
    ${Auth.can('escalation.delete') ? `<button class="action-btn delete" onclick="deleteEscalation(${e.id})">Delete</button>` : ''}
</td>
        </tr>
    `).join('');
}

function filterList() {
    const q = document.getElementById('searchBox').value.toLowerCase();
    const status = document.getElementById('filterStatus').value;
    const filtered = allEscalations.filter(e => {
        const matchQ = !q || e.reason.toLowerCase().includes(q) ||
            (e.taskTitle && e.taskTitle.toLowerCase().includes(q));
        const matchS = !status || e.status === status;
        return matchQ && matchS;
    });
    renderList(filtered);
}

function openForm() {
    document.getElementById('escalationModal').style.display = 'flex';
    document.getElementById('escReason').value = '';
    document.getElementById('escPriority').value = 'HIGH';
    document.getElementById('escStatus').value = 'TODO';
    document.getElementById('escTask').value = '';
    document.getElementById('escEscalatedBy').value = '';
    document.getElementById('escEscalatedTo').value = '';
}

function closeForm() {
    document.getElementById('escalationModal').style.display = 'none';
}

async function saveEscalation() {
    const reason = document.getElementById('escReason').value.trim();
    if (!reason) { alert('Reason is required'); return; }

    const taskVal = document.getElementById('escTask').value;
    const byVal = document.getElementById('escEscalatedBy').value;
    const toVal = document.getElementById('escEscalatedTo').value;

    const body = {
        reason,
        priority: document.getElementById('escPriority').value,
        status: document.getElementById('escStatus').value,
        taskId: taskVal ? Number(taskVal) : null,
        escalatedById: byVal ? Number(byVal) : null,
        escalatedToId: toVal ? Number(toVal) : null
    };

    try {
        const res = await apiFetch('/api/escalations', { method: 'POST', body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Save failed');
        closeForm();
        loadEscalations();
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

async function changeStatus(id, status) {
    try {
        await apiFetch(`/api/escalations/${id}/status`, {
            method: 'PATCH',
            body: JSON.stringify({ status })
        });
        loadEscalations();
    } catch (e) {
        alert('Failed to update status');
    }
}

async function deleteEscalation(id) {
    if (!confirm('Delete this escalation?')) return;
    try {
        await apiFetch(`/api/escalations/${id}`, { method: 'DELETE' });
        loadEscalations();
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



loadEscalations();
loadEmployees();
loadTasks();