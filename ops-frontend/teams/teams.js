let allTeams = [];

async function loadTeams() {
    const tbody = document.getElementById('teamBody');
    try {
        const res = await apiFetch('/api/teams');
        allTeams = await res.json();
        renderList(allTeams);
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="4" class="loading" style="color:#dc2626">Failed to load teams</td></tr>';
    }
}

function renderList(list) {
    const tbody = document.getElementById('teamBody');
    if (list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="loading">No teams yet</td></tr>';
        return;
    }
    tbody.innerHTML = list.map(t => `
        <tr>
            <td><strong>${escapeHtml(t.name)}</strong></td>
            <td>${t.description ? escapeHtml(t.description) : '<em style="color:#94a3b8">No description</em>'}</td>
            <td><span class="member-count">${t.memberCount || 0} members</span></td>
            <td style="text-align:right">
    ${Auth.can('employee.update') ? `<button class="action-btn edit" onclick="editEmployee(${e.id})">Edit</button>` : ''}
    ${Auth.can('employee.delete') ? `<button class="action-btn delete" onclick="deleteEmployee(${e.id})">Delete</button>` : ''}
</td>
        </tr>
    `).join('');
}

function filterList() {
    const q = document.getElementById('searchBox').value.toLowerCase();
    const filtered = allTeams.filter(t =>
        t.name.toLowerCase().includes(q) ||
        (t.description && t.description.toLowerCase().includes(q))
    );
    renderList(filtered);
}

function openForm(team) {
    document.getElementById('teamModal').style.display = 'flex';
    document.getElementById('modalTitle').textContent = team ? 'Edit Team' : 'New Team';
    document.getElementById('teamId').value = team ? team.id : '';
    document.getElementById('teamName').value = team ? team.name : '';
    document.getElementById('teamDesc').value = team ? (team.description || '') : '';
}

function editTeam(id) {
    const t = allTeams.find(x => x.id === id);
    if (t) openForm(t);
}

function closeForm() {
    document.getElementById('teamModal').style.display = 'none';
}

async function saveTeam() {
    const id = document.getElementById('teamId').value;
    const name = document.getElementById('teamName').value.trim();
    if (!name) { alert('Team Name is required'); return; }

    const body = {
        name,
        description: document.getElementById('teamDesc').value.trim() || null
    };

    const url = id ? `/api/teams/${id}` : '/api/teams';
    const method = id ? 'PUT' : 'POST';

    try {
        const res = await apiFetch(url, { method, body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Save failed');
        closeForm();
        loadTeams();
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

async function deleteTeam(id) {
    if (!confirm('Delete this team?')) return;
    try {
        await apiFetch(`/api/teams/${id}`, { method: 'DELETE' });
        loadTeams();
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
loadTeams();