@param String title = "Admin - Sessions"
@param Boolean authenticated = false
@param String username = ""
@param Boolean isAdmin = false

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/webjars/bulma/1.0.3/css/bulma.min.css">
    <link rel="stylesheet" href="/webjars/fortawesome__fontawesome-free/6.7.2/css/all.min.css">
    <script src="/webjars/axios/1.8.4/dist/axios.min.js"></script>
</head>
<body>
<nav class="navbar is-primary" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
        <a class="navbar-item" href="/">
            <strong>Chat Application</strong>
        </a>
    </div>
    <div class="navbar-menu">
        <div class="navbar-start">
            <a class="navbar-item" href="/">Home</a>
            <a class="navbar-item" href="/chat">Chat</a>
            @if(isAdmin)
                <div class="navbar-item has-dropdown is-hoverable">
                    <a class="navbar-link">Admin</a>
                    <div class="navbar-dropdown">
                        <a class="navbar-item" href="/admin/sessions">Sessions</a>
                        <a class="navbar-item" href="/admin/chat/history">Chat History</a>
                        <a class="navbar-item" href="/admin/users">Users</a>
                    </div>
                </div>
            @endif
        </div>
        <div class="navbar-end">
            <div class="navbar-item">
                @if(authenticated)
                    <div class="buttons">
                        <span class="navbar-item">Welcome, ${username}</span>
                        <a class="button is-light" href="/logout">
                            <strong>Logout</strong>
                        </a>
                    </div>
                @else
                    <div class="buttons">
                        <a class="button is-primary" href="/login">
                            <strong>Login</strong>
                        </a>
                    </div>
                @endif
            </div>
        </div>
    </div>
</nav>

<section class="section">
    <div class="container">
        <h1 class="title">Chat Sessions Administration</h1>
        <div class="table-container">
            <table class="table is-fullwidth is-striped is-hoverable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Created By</th>
                    <th>Participants</th>
                    <th>Active</th>
                    <th>Bot Enabled</th>
                    <th>Created At</th>
                    <th>Last Message At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="sessions-table-body">
                <!-- Sessions will be loaded here -->
                </tbody>
            </table>
        </div>
    </div>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Fetch all sessions
        axios.get('/api/sessions/all')
            .then(function (response) {
                const sessions = response.data;
                const tableBody = document.getElementById('sessions-table-body');

                sessions.forEach(function (session) {
                    const row = document.createElement('tr');

                    // ID
                    const idCell = document.createElement('td');
                    idCell.textContent = session.id;
                    row.appendChild(idCell);

                    // Name
                    const nameCell = document.createElement('td');
                    nameCell.textContent = session.name;
                    row.appendChild(nameCell);

                    // Description
                    const descCell = document.createElement('td');
                    descCell.textContent = session.description || '-';
                    row.appendChild(descCell);

                    // Created By
                    const createdByCell = document.createElement('td');
                    createdByCell.textContent = session.createdBy;
                    row.appendChild(createdByCell);

                    // Participants
                    const participantsCell = document.createElement('td');
                    participantsCell.textContent = session.participants ? session.participants.length : 0;
                    row.appendChild(participantsCell);

                    // Active
                    const activeCell = document.createElement('td');
                    activeCell.innerHTML = session.active ?
                        '<span class="tag is-success">Yes</span>' :
                        '<span class="tag is-danger">No</span>';
                    row.appendChild(activeCell);

                    // Bot Enabled
                    const botEnabledCell = document.createElement('td');
                    botEnabledCell.innerHTML = session.botEnabled ?
                        '<span class="tag is-success">Yes</span>' :
                        '<span class="tag is-danger">No</span>';
                    row.appendChild(botEnabledCell);

                    // Created At
                    const createdAtCell = document.createElement('td');
                    createdAtCell.textContent = new Date(session.createdAt).toLocaleString();
                    row.appendChild(createdAtCell);

                    // Last Message At
                    const lastMessageAtCell = document.createElement('td');
                    lastMessageAtCell.textContent = session.lastMessageAt ?
                        new Date(session.lastMessageAt).toLocaleString() : '-';
                    row.appendChild(lastMessageAtCell);

                    // Actions
                    const actionsCell = document.createElement('td');
                    const viewBtn = document.createElement('button');
                    viewBtn.className = 'button is-small is-info mr-1';
                    viewBtn.textContent = 'View';
                    viewBtn.onclick = function () {
                        window.location.href = '/admin/chat/history/' + session.id;
                    };
                    actionsCell.appendChild(viewBtn);

                    const deleteBtn = document.createElement('button');
                    deleteBtn.className = 'button is-small is-danger';
                    deleteBtn.textContent = 'Delete';
                    deleteBtn.onclick = function () {
                        if (confirm('Are you sure you want to delete this session?')) {
                            axios.delete('/api/sessions/' + session.id)
                                .then(function () {
                                    row.remove();
                                })
                                .catch(function (error) {
                                    alert('Error deleting session: ' + error.response.data.message);
                                });
                        }
                    };
                    actionsCell.appendChild(deleteBtn);

                    row.appendChild(actionsCell);

                    tableBody.appendChild(row);
                });
            })
            .catch(function (error) {
                console.error('Error fetching sessions:', error);
                alert('Error loading sessions. Please try again.');
            });
    });
</script>
</body>
</html>