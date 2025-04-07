@param String title = "Admin - Sessions"
@param Boolean authenticated = false
@param String username = ""
@param Boolean isAdmin = false
<!--

    Copyright © 2025 BellSoft (info@bell-sw.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->

<!DOCTYPE html>
<html lang="en">
@template.fragments.header(
    title = title,
    extraScript = @`<script src="/webjars/axios/1.8.4/dist/axios.min.js"></script>"`
)
<body>
@template.fragments.navbar(
    authenticated = authenticated,
    username = username,
    isAdmin = isAdmin
)

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
@template.fragments.footer()
</body>
</html>
