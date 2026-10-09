let allIssues = [];
let allEmployees = [];

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

async function loadIssues() {
    const tbody = document.getElementById('issueBody');
    try {
        const res = await apiFetch('/api/issues');
        if (!res.ok) throw new Error('HTTP ' + res.status);
        allIssues = await res.json();
        renderList(allIssues);
    } catch (e) {
        console.error('Load issues failed:', e);
        tbody.innerHTML = '<tr><td colspan="6" class="loading" style="color:#dc2626">Failed to load issues</td></tr>';
    }
}

async function loadEmployees() {
    try {
        console.log('[DEBUG] Loading employees...');
        const res = await apiFetch('/api/employees');
        console.log('[DEBUG] Employees status:', res.status);

        if (!res.ok) throw new Error('HTTP ' + res.status);
        allEmployees = await res.json();
        console.log('[DEBUG] Employees loaded:', allEmployees.length);

        const reported = document.getElementById('issueReportedBy');
        const assignee = document.getElementById('issueAssignee');

        const options = allEmployees
            .map(e => `<option value="${e.id}">${escapeHtml(e.fullName)}</option>`)
            .join('');

        if (reported) {
            reported.innerHTML = '<option value="">— Reported By —</option>' + options;
        }
        if (assignee) {
            assignee.innerHTML = '<option value="">— Assigned To —</option>' + options;
        }
    } catch (e) {
        console.error('Employees load failed:', e);
    }
}

function renderList(list) {
    const tbody = document.getElementById('issueBody');
    if (!tbody) return;

    if (list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="loading">No issues found</td></tr>';
        return;
    }

    tbody.innerHTML = list.map(i => `
        <tr>
            <td><strong>${escapeHtml(i.title)}</strong></td>
            <td><span class="badge ${i.priority}">${i.priority}</span></td>
            <td>
                <select onchange="changeStatus(${i.id}, this.value)" style="padding:.3rem;border-radius:6px;border:1px solid #e2e8f0">
                    ${['OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'].map(s =>
                        `<option value="${s}" ${i.status === s ? 'selected' : ''}>${s.replace('_', ' ')}</option>`
                    ).join('')}
                </select>
            </td>
            <td>${i.reportedByName ? escapeHtml(i.reportedByName) : '<em style="color:#94a3b8">Unknown</em>'}</td>
            <td>${i.assignedToName ? escapeHtml(i.assignedToName) : '<em style="color:#94a3b8">Unassigned</em>'}</td>
            <td style="text-align:right">
                ${Auth.can('issue.delete') ? `<button class="action-btn delete" onclick="deleteIssue(${i.id})">Delete</button>` : ''}
            </td>
        </tr>
    `).join('');
}

function filterList() {
    const q = document.getElementById('searchBox').value.toLowerCase();
    const status = document.getElementById('filterStatus').value;
    const priority = document.getElementById('filterPriority').value;

    const filtered = allIssues.filter(i => {
        const matchQ = !q || i.title.toLowerCase().includes(q);
        const matchS = !status || i.status === status;
        const matchP = !priority || i.priority === priority;
        return matchQ && matchS && matchP;
    });
    renderList(filtered);
}

function openForm() {
    document.getElementById('issueModal').style.display = 'flex';
    document.getElementById('issueTitle').value = '';
    document.getElementById('issueDesc').value = '';
    document.getElementById('issuePriority').value = 'MEDIUM';
    document.getElementById('issueStatus').value = 'OPEN';
    document.getElementById('issueReportedBy').value = '';
    document.getElementById('issueAssignee').value = '';
}

function closeForm() {
    document.getElementById('issueModal').style.display = 'none';
}

async function saveIssue() {
    const title = document.getElementById('issueTitle').value.trim();
    if (!title) { alert('Title is required'); return; }

    const rbv = document.getElementById('issueReportedBy').value;
    const asv = document.getElementById('issueAssignee').value;

    const body = {
        title,
        description: document.getElementById('issueDesc').value.trim() || null,
        priority: document.getElementById('issuePriority').value,
        status: document.getElementById('issueStatus').value,
        reportedById: rbv ? Number(rbv) : null,
        assignedToId: asv ? Number(asv) : null
    };

    try {
        const res = await apiFetch('/api/issues', { method: 'POST', body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Save failed');
        closeForm();
        loadIssues();
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

async function changeStatus(id, status) {
    try {
        await apiFetch(`/api/issues/${id}/status`, {
            method: 'PATCH',
            body: JSON.stringify({ status })
        });
        loadIssues();
    } catch (e) {
        alert('Failed to update status');
    }
}

async function deleteIssue(id) {
    if (!confirm('Delete this issue?')) return;
    try {
        await apiFetch(`/api/issues/${id}`, { method: 'DELETE' });
        loadIssues();
    } catch (e) {
        alert('Failed to delete');
    }
}

// Init
loadIssues();
loadEmployees();