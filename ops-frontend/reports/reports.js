let currentReport = null;
let reportStatusChart, reportPriorityChart;

async function loadReport() {
    try {
        const res = await apiFetch('/api/reports');
        const report = await res.json();
        currentReport = report;

        document.getElementById('rTotalTasks').textContent = report.totalTasks || 0;
        document.getElementById('rCompletedTasks').textContent = report.completedTasks || 0;
        document.getElementById('rOpenIssues').textContent = report.openIssues || 0;
        document.getElementById('rBreaches').textContent = report.breaches || 0;

        renderStatusChart(report.tasksByStatus || {});
        renderPriorityChart(report.tasksByPriority || {});
    } catch (e) {
        console.error('Report load failed', e);
    }
}

function renderStatusChart(data) {
    const canvas = document.getElementById('reportStatusChart');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    if (reportStatusChart) reportStatusChart.destroy();

    const labels = Object.keys(data);
    const values = Object.values(data);
    const colors = {
        TODO: '#94a3b8',
        IN_PROGRESS: '#3b82f6',
        REVIEW: '#f59e0b',
        DONE: '#22c55e'
    };

    reportStatusChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Tasks',
                data: values,
                backgroundColor: labels.map(l => colors[l] || '#6366f1'),
                borderRadius: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
        }
    });
}

function renderPriorityChart(data) {
    const canvas = document.getElementById('reportPriorityChart');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    if (reportPriorityChart) reportPriorityChart.destroy();

    const labels = Object.keys(data);
    const values = Object.values(data);
    const colors = {
        LOW: '#94a3b8',
        MEDIUM: '#3b82f6',
        HIGH: '#f97316',
        CRITICAL: '#ef4444'
    };

    reportPriorityChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: values,
                backgroundColor: labels.map(l => colors[l] || '#6366f1'),
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { position: 'bottom' } }
        }
    });
}

function exportCSV() {
    if (!currentReport) {
        alert('Report not loaded yet');
        return;
    }

    const rows = [
        ['Metric', 'Value'],
        ['Generated At', currentReport.generatedAt || ''],
        ['Total Tasks', currentReport.totalTasks || 0],
        ['Completed Tasks', currentReport.completedTasks || 0],
        ['Open Issues', currentReport.openIssues || 0],
        ['SLA Breaches', currentReport.breaches || 0],
        [],
        ['Tasks by Status', ''],
    ];

    const statuses = currentReport.tasksByStatus || {};
    Object.keys(statuses).forEach(k => rows.push([k, statuses[k]]));

    rows.push([]);
    rows.push(['Tasks by Priority', '']);
    const priorities = currentReport.tasksByPriority || {};
    Object.keys(priorities).forEach(k => rows.push([k, priorities[k]]));

    const csv = rows.map(r => r.map(v => `"${v}"`).join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `ops-report-${new Date().toISOString().slice(0,10)}.csv`;
    a.click();
    URL.revokeObjectURL(url);
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

loadReport();