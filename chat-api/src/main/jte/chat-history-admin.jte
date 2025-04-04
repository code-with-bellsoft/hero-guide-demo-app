@param String title = "Admin - Chat History"
@param Boolean authenticated = false
@param String username = ""
@param Boolean isAdmin = false
@param String sessionId = ""

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
    extraScript = @`<script src="/webjars/axios/1.8.4/dist/axios.min.js"></script>`
)
<body>
@template.fragments.navbar(
    authenticated = authenticated,
    username = username,
    isAdmin = isAdmin
)

<section class="section">
    <div class="container">
        <h1 class="title">Chat History Administration</h1>

        @if(sessionId.isEmpty())
            <div class="select mb-4">
                <select id="session-selector">
                    <option value="">Select a session</option>
                    <!-- Sessions will be loaded here -->
                </select>
            </div>
        @else
            <h2 class="subtitle">Session ID: ${sessionId}</h2>
            <a href="/admin/sessions" class="button is-info mb-4">Back to Sessions</a>
        @endif

        <div class="table-container">
            <table class="table is-fullwidth is-striped is-hoverable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Session ID</th>
                    <th>Sender</th>
                    <th>Content</th>
                    <th>Type</th>
                    <th>Timestamp</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="messages-table-body">
                <!-- Messages will be loaded here -->
                </tbody>
            </table>
        </div>
    </div>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const sessionId = '${sessionId}';

        if (!sessionId) {
            // Load sessions for the dropdown
            axios.get('/api/sessions/all')
                .then(function (response) {
                    const sessions = response.data;
                    const selector = document.getElementById('session-selector');

                    sessions.forEach(function (session) {
                        const option = document.createElement('option');
                        option.value = session.id;
                        option.textContent = session.name + ' (' + session.id + ')';
                        selector.appendChild(option);
                    });

                    // Add change event listener
                    selector.addEventListener('change', function () {
                        if (this.value) {
                            loadMessages(this.value);
                        } else {
                            loadAllMessages();
                        }
                    });

                    // Load all messages initially
                    loadAllMessages();
                })
                .catch(function (error) {
                    console.error('Error fetching sessions:', error);
                    alert('Error loading sessions. Please try again.');
                });
        } else {
            // Load messages for the specified session
            loadMessages(sessionId);
        }

        function loadMessages(sessionId) {
            axios.get('/api/chat/history/' + sessionId)
                .then(function (response) {
                    displayMessages(response.data);
                })
                .catch(function (error) {
                    console.error('Error fetching messages:', error);
                    alert('Error loading messages. Please try again.');
                });
        }

        function loadAllMessages() {
            axios.get('/api/chat/history/all')
                .then(function (response) {
                    displayMessages(response.data);
                })
                .catch(function (error) {
                    console.error('Error fetching all messages:', error);
                    alert('Error loading all messages. Please try again.');
                });
        }

        function displayMessages(messages) {
            const tableBody = document.getElementById('messages-table-body');
            tableBody.innerHTML = '';

            messages.forEach(function (message) {
                const row = document.createElement('tr');

                // ID
                const idCell = document.createElement('td');
                idCell.textContent = message.id;
                row.appendChild(idCell);

                // Session ID
                const sessionIdCell = document.createElement('td');
                sessionIdCell.textContent = message.sessionId;
                row.appendChild(sessionIdCell);

                // Sender
                const senderCell = document.createElement('td');
                senderCell.textContent = message.sender;
                row.appendChild(senderCell);

                // Content
                const contentCell = document.createElement('td');
                contentCell.textContent = message.content;
                row.appendChild(contentCell);

                // Type
                const typeCell = document.createElement('td');
                typeCell.textContent = message.type;
                row.appendChild(typeCell);

                // Timestamp
                const timestampCell = document.createElement('td');
                timestampCell.textContent = new Date(message.timestamp).toLocaleString();
                row.appendChild(timestampCell);

                // Actions
                const actionsCell = document.createElement('td');
                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'button is-small is-danger';
                deleteBtn.textContent = 'Delete';
                deleteBtn.onclick = function () {
                    if (confirm('Are you sure you want to delete this message?')) {
                        axios.delete('/api/chat/history/' + message.id)
                            .then(function () {
                                row.remove();
                            })
                            .catch(function (error) {
                                alert('Error deleting message: ' + error.response.data.message);
                            });
                    }
                };
                actionsCell.appendChild(deleteBtn);
                row.appendChild(actionsCell);

                tableBody.appendChild(row);
            });
        }
    });
</script>
@template.fragments.footer()
</body>
</html>
