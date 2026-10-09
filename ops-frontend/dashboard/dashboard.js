if (!Auth.isLoggedIn()) {
    window.location.href = '/index.html';
}

// Fill sidebar (works on this page)
const fullName = Auth.getFullName();
const role = Auth.getRole();

const nameEl = document.getElementById('userLabelSide');
if (nameEl) nameEl.textContent = fullName;
const roleEl = document.getElementById('userRoleSide');
if (roleEl) roleEl.textContent = role;
const initialsEl = document.getElementById('avatarInitials');
if (initialsEl) {
    initialsEl.textContent = fullName.split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();
}

// Load KPIs
async function loadMetrics() {
    try {
        const res = await apiFetch('/api/dashboard/metrics');
        const m = await res.json();
        const total = document.getElementById('kpiTotal'); if (total) total.textContent = m.total;
        const completed = document.getElementById('kpiCompleted'); if (completed) completed.textContent = m.completed;
        const inProgress = document.getElementById('kpiInProgress'); if (inProgress) inProgress.textContent = m.inProgress;
        const overdue = document.getElementById('kpiOverdue'); if (overdue) overdue.textContent = m.overdue;
        const rate = document.getElementById('kpiRate'); if (rate) rate.textContent = (m.completionRate || 0).toFixed(1) + '%';
    } catch (e) { console.error('Metrics error:', e); }
}

let statusChart;
async function loadStatusChart() {
    const canvas = document.getElementById('statusChart');
    if (!canvas) return;
    try {
        const res = await apiFetch('/api/dashboard/tasks-by-status');
        const data = await res.json();
        const ctx = canvas.getContext('2d');
        if (statusChart) statusChart.destroy();
        statusChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['To Do', 'In Progress', 'Review', 'Done'],
                datasets: [{
                    data: [data.TODO || 0, data.IN_PROGRESS || 0, data.REVIEW || 0, data.DONE || 0],
                    backgroundColor: ['#94a3b8', '#3b82f6', '#f59e0b', '#22c55e'],
                    borderWidth: 0
                }]
            },
            options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom' } } }
        });
    } catch (e) { console.error('Status chart error:', e); }
}

let perfChart;
async function loadPerfChart() {
    const canvas = document.getElementById('perfChart');
    if (!canvas) return;
    try {
        const res = await apiFetch('/api/dashboard/performance');
        const data = await res.json();
        const ctx = canvas.getContext('2d');
        if (perfChart) perfChart.destroy();
        perfChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map(d => d.name || 'Unknown'),
                datasets: [
                    { label: 'Total', data: data.map(d => Number(d.total) || 0), backgroundColor: '#93c5fd', borderRadius: 6 },
                    { label: 'Completed', data: data.map(d => Number(d.completed) || 0), backgroundColor: '#22c55e', borderRadius: 6 },
                    { label: 'Overdue', data: data.map(d => Number(d.overdue) || 0), backgroundColor: '#ef4444', borderRadius: 6 }
                ]
            },
            options: {
                responsive: true, maintainAspectRatio: false,
                plugins: { legend: { position: 'bottom' } },
                scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
            }
        });
    } catch (e) { console.error('Perf chart error:', e); }
}

loadMetrics();
loadStatusChart();
loadPerfChart();