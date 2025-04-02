@param String title = "Admin - Users"
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
            <h1 class="title">Users Administration</h1>
            <div class="table-container">
                <table class="table is-fullwidth is-striped is-hoverable">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Display Name</th>
                            <th>Roles</th>
                            <th>Enabled</th>
                            <th>Created At</th>
                            <th>Updated At</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="users-table-body">
                        <!-- Users will be loaded here -->
                    </tbody>
                </table>
            </div>
        </div>
    </section>

    <!-- Modal for editing user roles -->
    <div id="edit-roles-modal" class="modal">
        <div class="modal-background"></div>
        <div class="modal-card">
            <header class="modal-card-head">
                <p class="modal-card-title">Edit User Roles</p>
                <button class="delete" aria-label="close" onclick="closeModal()"></button>
            </header>
            <section class="modal-card-body">
                <div class="field">
                    <label class="label">Username</label>
                    <div class="control">
                        <input id="edit-username" class="input" type="text" readonly>
                    </div>
                </div>
                <div class="field">
                    <label class="label">Roles</label>
                    <div class="control">
                        <label class="checkbox">
                            <input id="role-user" type="checkbox" value="USER">
                            USER
                        </label>
                        <label class="checkbox">
                            <input id="role-admin" type="checkbox" value="ADMIN">
                            ADMIN
                        </label>
                    </div>
                </div>
                <input id="edit-user-id" type="hidden">
            </section>
            <footer class="modal-card-foot">
                <button class="button is-success" onclick="saveUserRoles()">Save changes</button>
                <button class="button" onclick="closeModal()">Cancel</button>
            </footer>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Fetch all users
            axios.get('/api/users')
                .then(function(response) {
                    const users = response.data;
                    const tableBody = document.getElementById('users-table-body');
                    
                    users.forEach(function(user) {
                        const row = document.createElement('tr');
                        
                        // ID
                        const idCell = document.createElement('td');
                        idCell.textContent = user.id;
                        row.appendChild(idCell);
                        
                        // Username
                        const usernameCell = document.createElement('td');
                        usernameCell.textContent = user.username;
                        row.appendChild(usernameCell);
                        
                        // Email
                        const emailCell = document.createElement('td');
                        emailCell.textContent = user.email || '-';
                        row.appendChild(emailCell);
                        
                        // Display Name
                        const displayNameCell = document.createElement('td');
                        displayNameCell.textContent = user.displayName || '-';
                        row.appendChild(displayNameCell);
                        
                        // Roles
                        const rolesCell = document.createElement('td');
                        if (user.roles && user.roles.length > 0) {
                            user.roles.forEach(function(role) {
                                const roleSpan = document.createElement('span');
                                roleSpan.className = 'tag is-info mr-1';
                                roleSpan.textContent = role;
                                rolesCell.appendChild(roleSpan);
                            });
                        } else {
                            rolesCell.textContent = '-';
                        }
                        row.appendChild(rolesCell);
                        
                        // Enabled
                        const enabledCell = document.createElement('td');
                        enabledCell.innerHTML = user.enabled ? 
                            '<span class="tag is-success">Yes</span>' : 
                            '<span class="tag is-danger">No</span>';
                        row.appendChild(enabledCell);
                        
                        // Created At
                        const createdAtCell = document.createElement('td');
                        createdAtCell.textContent = user.createdAt ? 
                            new Date(user.createdAt).toLocaleString() : '-';
                        row.appendChild(createdAtCell);
                        
                        // Updated At
                        const updatedAtCell = document.createElement('td');
                        updatedAtCell.textContent = user.updatedAt ? 
                            new Date(user.updatedAt).toLocaleString() : '-';
                        row.appendChild(updatedAtCell);
                        
                        // Actions
                        const actionsCell = document.createElement('td');
                        
                        // Edit roles button
                        const editRolesBtn = document.createElement('button');
                        editRolesBtn.className = 'button is-small is-info mr-1';
                        editRolesBtn.textContent = 'Edit Roles';
                        editRolesBtn.onclick = function() {
                            openEditRolesModal(user);
                        };
                        actionsCell.appendChild(editRolesBtn);
                        
                        // Delete button
                        const deleteBtn = document.createElement('button');
                        deleteBtn.className = 'button is-small is-danger';
                        deleteBtn.textContent = 'Delete';
                        deleteBtn.onclick = function() {
                            if (confirm('Are you sure you want to delete this user?')) {
                                axios.delete('/api/users/' + user.id)
                                    .then(function() {
                                        row.remove();
                                    })
                                    .catch(function(error) {
                                        alert('Error deleting user: ' + error.response.data.message);
                                    });
                            }
                        };
                        actionsCell.appendChild(deleteBtn);
                        
                        row.appendChild(actionsCell);
                        
                        tableBody.appendChild(row);
                    });
                })
                .catch(function(error) {
                    console.error('Error fetching users:', error);
                    alert('Error loading users. Please try again.');
                });
        });
        
        function openEditRolesModal(user) {
            document.getElementById('edit-username').value = user.username;
            document.getElementById('edit-user-id').value = user.id;
            
            // Reset checkboxes
            document.getElementById('role-user').checked = false;
            document.getElementById('role-admin').checked = false;
            
            // Set checkboxes based on user roles
            if (user.roles) {
                user.roles.forEach(function(role) {
                    if (role === 'USER') {
                        document.getElementById('role-user').checked = true;
                    } else if (role === 'ADMIN') {
                        document.getElementById('role-admin').checked = true;
                    }
                });
            }
            
            // Show modal
            document.getElementById('edit-roles-modal').classList.add('is-active');
        }
        
        function closeModal() {
            document.getElementById('edit-roles-modal').classList.remove('is-active');
        }
        
        function saveUserRoles() {
            const userId = document.getElementById('edit-user-id').value;
            const roles = [];
            
            if (document.getElementById('role-user').checked) {
                roles.push('USER');
            }
            if (document.getElementById('role-admin').checked) {
                roles.push('ADMIN');
            }
            
            axios.put('/api/users/' + userId, { roles: roles })
                .then(function() {
                    closeModal();
                    // Reload the page to reflect changes
                    window.location.reload();
                })
                .catch(function(error) {
                    alert('Error updating user roles: ' + error.response.data.message);
                });
        }
    </script>
</body>
</html>