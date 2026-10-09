let allSLAs = [];
let allEmployees = [];

async function loadSLAs() {
    const tbody = document.getElementById('slaBody');
    try {
        const res = await apiFetch('/api/sla');
        allSLAs = await res.json();
        renderList(allSLAs);
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="7" class="loading" style="color:#dc2626">Failed to load SLAs</td></tr>';
    }
}

async function loadEmployees() {
    try {
        const res = await apiFetch('/api/employees');
        allEmployees = await res.json();
        const select = document.getElementById('slaAssignee');
        select.innerHTML = '<option value="">— Unassigned —</option>' +
            allEmployees.map(e => `<option value="${e.id}">${escapeHtml(e.fullName)}</option>`).join('');
    } catch (e) {
        console.error('Employees load failed', e);
    }
}

function renderList(list) {
    const tbody = document.getElementById('slaBody');
    if (list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="loading">No SLA records yet</td></tr>';
        return;
    }
    tbody.innerHTML = list.map(s => `
        <tr>
            <td><strong>${escapeHtml(s.title)}</strong></td>
            <td><span class="badge ${s.priority}">${s.priority}</span></td>
            <td>${s.targetHours}</td>
            <td>${s.actualHours != null ? s.actualHours : '—'}</td>
            <td><span class="badge ${s.status}">${s.status.replace('_', ' ')}</span></td>
            <td>${s.assignedToName ? escapeHtml(s.assignedToName) : '<em style="color:#94a3b8">None</em>'}</td>
            <td style="text-align:right">
    ${Auth.can('sla.delete') ? `<button class="action-btn delete" onclick="deleteSLA(${s.id})">Delete</button>` : ''}
</td>
        </tr>
    `).join('');
}

function filterList() {
    const q = document.getElementById('searchBox').value.toLowerCase();
    const status = document.getElementById('filterStatus').value;
    const filtered = allSLAs.filter(s => {
        const matchQ = !q || s.title.toLowerCase().includes(q);
        const matchS = !status || s.status === status;
        return matchQ && matchS;
    });
    renderList(filtered);
}

function openForm() {
    document.getElementById('slaModal').style.display = 'flex';
    document.getElementById('slaTitle').value = '';
    document.getElementById('slaPriority').value = 'MEDIUM';
    document.getElementById('slaStatus').value = 'ON_TRACK';
    document.getElementById('slaTargetHours').value = '';
    document.getElementById('slaActualHours').value = '';
    document.getElementById('slaAssignee').value = '';
}

function closeForm() {
    document.getElementById('slaModal').style.display = 'none';
}

async function saveSLA() {
    const title = document.getElementById('slaTitle').value.trim();
    const targetHours = document.getElementById('slaTargetHours').value;

    if (!title) { alert('Title is required'); return; }
    if (!targetHours) { alert('Target hours is required'); return; }

    const assigneeVal = document.getElementById('slaAssignee').value;
    const actualVal = document.getElementById('slaActualHours').value;

    const body = {
        title,
        priority: document.getElementById('slaPriority').value,
        status: document.getElementById('slaStatus').value,
        targetHours: Number(targetHours),
        actualHours: actualVal ? Number(actualVal) : null,
        assignedToId: assigneeVal ? Number(assigneeVal) : null
    };

    try {
        const res = await apiFetch('/api/sla', { method: 'POST', body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Save failed');
        closeForm();
        loadSLAs();
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

async function deleteSLA(id) {
    if (!confirm('Delete this SLA record?')) return;
    try {
        await apiFetch(`/api/sla/${id}`, { method: 'DELETE' });
        loadSLAs();
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


loadSLAs();
loadEmployees();