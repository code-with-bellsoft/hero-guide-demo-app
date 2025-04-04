@param String title = "Admin - Users"
@param Boolean authenticated = false
@param String username = ""
@param Boolean isAdmin = false

<!DOCTYPE html>
<!--
  #%L
  hero-guide-demo-app
  %%
  Copyright (C) 2025 BellSoft
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  -->

<html lang="en">
@template.fragments.header(
title = title,
extraScript = @`
<script src="/webjars/axios/1.8.4/dist/axios.min.js"></script>`
)
<body>
@template.fragments.navbar(
authenticated = authenticated,
username = username,
isAdmin = isAdmin
)

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
    document.addEventListener('DOMContentLoaded', function () {
        // Fetch all users
        axios.get('/api/users')
            .then(function (response) {
                const users = response.data;
                const tableBody = document.getElementById('users-table-body');

                users.forEach(function (user) {
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
                        user.roles.forEach(function (role) {
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
                    editRolesBtn.onclick = function () {
                        openEditRolesModal(user);
                    };
                    actionsCell.appendChild(editRolesBtn);

                    // Delete button
                    const deleteBtn = document.createElement('button');
                    deleteBtn.className = 'button is-small is-danger';
                    deleteBtn.textContent = 'Delete';
                    deleteBtn.onclick = function () {
                        if (confirm('Are you sure you want to delete this user?')) {
                            axios.delete('/api/users/' + user.id)
                                .then(function () {
                                    row.remove();
                                })
                                .catch(function (error) {
                                    alert('Error deleting user: ' + error.response.data.message);
                                });
                        }
                    };
                    actionsCell.appendChild(deleteBtn);

                    row.appendChild(actionsCell);

                    tableBody.appendChild(row);
                });
            })
            .catch(function (error) {
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
            user.roles.forEach(function (role) {
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

        axios.put('/api/users/' + userId, {roles: roles})
            .then(function () {
                closeModal();
                // Reload the page to reflect changes
                window.location.reload();
            })
            .catch(function (error) {
                alert('Error updating user roles: ' + error.response.data.message);
            });
    }
</script>
@template.fragments.footer()
</body>
</html>
