let allTasks = [];
let allEmployees = [];

// Fill sidebar info (works on any page)
function fillSidebar() {
    const nameEl = document.getElementById('userLabelSide');
    const roleEl = document.getElementById('userRoleSide');
    const initialsEl = document.getElementById('avatarInitials');

    if (nameEl) nameEl.textContent = Auth.getFullName();
    if (roleEl) roleEl.textContent = Auth.getRole();
    if (initialsEl) {
        initialsEl.textContent = Auth.getFullName()
            .split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();
    }
}

async function loadTasks() {
    const tbody = document.getElementById('taskBody');
    tbody.innerHTML = '<tr><td colspan="6" class="loading">Loading...</td></tr>';

    try {
        const token = Auth.getToken();
        console.log('[DEBUG] Token:', token ? token.substring(0, 20) + '...' : 'NULL');

        if (!token) {
            window.location.href = '/index.html';
            return;
        }

        const res = await fetch('http://localhost:8080/api/tasks', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        });

        console.log('[DEBUG] Response status:', res.status);

        if (res.status === 401) {
            Auth.logout();
            return;
        }

        if (!res.ok) {
            const text = await res.text();
            console.error('[DEBUG] Error response:', text);
            throw new Error('Server returned ' + res.status);
        }

        allTasks = await res.json();
        console.log('[DEBUG] Tasks received:', allTasks);
        renderTasks(allTasks);
    } catch (e) {
        console.error('[DEBUG] Load tasks failed:', e);
        tbody.innerHTML = '<tr><td colspan="6" class="loading" style="color:#dc2626">' +
            'Error: ' + e.message + '</td></tr>';
    }
}

async function loadEmployees() {
    try {
        const res = await apiFetch('/api/employees');
        if (!res.ok) return;
        allEmployees = await res.json();
        const select = document.getElementById('taskAssignee');
        if (!select) return;
        select.innerHTML = '<option value="">— Unassigned —</option>' +
            allEmployees.map(e => `<option value="${e.id}">${escapeHtml(e.fullName)}</option>`).join('');
    } catch (e) {
        console.error('Could not load employees', e);
    }
}

function renderTasks(tasks) {
    const tbody = document.getElementById('taskBody');
    if (!tbody) return;

    if (tasks.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="loading">No tasks found</td></tr>';
        return;
    }

    tbody.innerHTML = tasks.map(t => `
        <tr>
            <td><strong>${escapeHtml(t.title)}</strong></td>
            <td>${t.assignedToName ? escapeHtml(t.assignedToName) : '<em style="color:#94a3b8">Unassigned</em>'}</td>
            <td><span class="badge ${t.priority}">${t.priority}</span></td>
            <td>
                <select onchange="changeStatus(${t.id}, this.value)" style="padding:.3rem;border-radius:6px;border:1px solid #e2e8f0">
                    ${['TODO', 'IN_PROGRESS', 'REVIEW', 'DONE'].map(s =>
                        `<option value="${s}" ${t.status === s ? 'selected' : ''}>${s.replace('_', ' ')}</option>`
                    ).join('')}
                </select>
            </td>
            <td>${t.dueDate || '—'}</td>
            <td style="text-align:right">
    ${Auth.can('task.update') ? `<button class="action-btn edit" onclick="editTask(${t.id})">Edit</button>` : ''}
    ${Auth.can('task.delete') ? `<button class="action-btn delete" onclick="deleteTask(${t.id})">Delete</button>` : ''}
</td>
        </tr>
    `).join('');
}

function filterTasks() {
    const q = document.getElementById('searchBox').value.toLowerCase();
    const status = document.getElementById('filterStatus').value;
    const priority = document.getElementById('filterPriority').value;

    const filtered = allTasks.filter(t => {
        const matchQ = !q || t.title.toLowerCase().includes(q) ||
            (t.assignedToName && t.assignedToName.toLowerCase().includes(q));
        const matchS = !status || t.status === status;
        const matchP = !priority || t.priority === priority;
        return matchQ && matchS && matchP;
    });
    renderTasks(filtered);
}

function openTaskForm(task) {
    document.getElementById('taskModal').style.display = 'flex';
    document.getElementById('modalTitle').textContent = task ? 'Edit Task' : 'New Task';
    document.getElementById('taskId').value = task ? task.id : '';
    document.getElementById('taskTitle').value = task ? task.title : '';
    document.getElementById('taskDesc').value = task ? (task.description || '') : '';
    document.getElementById('taskPriority').value = task ? task.priority : 'MEDIUM';
    document.getElementById('taskStatus').value = task ? task.status : 'TODO';
    document.getElementById('taskAssignee').value = task && task.assignedToId ? task.assignedToId : '';
    document.getElementById('taskDue').value = task && task.dueDate ? task.dueDate : '';
}

function editTask(id) {
    const task = allTasks.find(t => t.id === id);
    if (task) openTaskForm(task);
}

function closeTaskForm() {
    document.getElementById('taskModal').style.display = 'none';
}

async function saveTask() {
    const id = document.getElementById('taskId').value;
    const title = document.getElementById('taskTitle').value.trim();
    if (!title) { alert('Title is required'); return; }

    const assigneeVal = document.getElementById('taskAssignee').value;
    const body = {
        title,
        description: document.getElementById('taskDesc').value.trim() || null,
        priority: document.getElementById('taskPriority').value,
        status: document.getElementById('taskStatus').value,
        assignedToId: assigneeVal ? Number(assigneeVal) : null,
        dueDate: document.getElementById('taskDue').value || null
    };

    const url = id ? `/api/tasks/${id}` : '/api/tasks';
    const method = id ? 'PUT' : 'POST';

    try {
        const res = await apiFetch(url, { method, body: JSON.stringify(body) });
        if (!res.ok) {
            const err = await res.text();
            throw new Error('Save failed: ' + err);
        }
        closeTaskForm();
        loadTasks();
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

async function changeStatus(id, status) {
    try {
        await apiFetch(`/api/tasks/${id}/status`, {
            method: 'PATCH',
            body: JSON.stringify({ status })
        });
        loadTasks();
    } catch (e) {
        alert('Failed to update status');
    }
}

async function deleteTask(id) {
    if (!confirm('Delete this task?')) return;
    try {
        await apiFetch(`/api/tasks/${id}`, { method: 'DELETE' });
        loadTasks();
    } catch (e) {
        alert('Failed to delete');
    }
}

// Init
fillSidebar();
loadTasks();
loadEmployees();