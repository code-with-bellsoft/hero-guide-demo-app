@param String title = "Chat Application"
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
@template.fragments.header(title = title)
<body>
@template.fragments.navbar(
    authenticated = authenticated,
    username = username,
    isAdmin = isAdmin
)

<section class="section">
    <div class="container">
        <div class="columns">
            <div class="column is-8 is-offset-2">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            Welcome to the Chat Application
                        </p>
                    </div>
                    <div class="card-content">
                        <div class="content">
                            <p>A simple chat application built with Spring Boot, WebSocket, and MongoDB.</p>
                            <p>Features:</p>
                            <ul>
                                <li><strong><a href="/chat">Messaging</a></strong>: Real-time chat using WebSocket</li>
                                <li><strong>Storage</strong>: Chat history saved in MongoDB</li>
                                <li><strong>AI Assistant</strong>: Optional bot responses in chat sessions</li>
                                <li><strong><a href="/login">Authentication</a></strong>: User login and role-based access</li>
                                <li><strong><a href="/chat">Session Management</a></strong>: Create and manage chat rooms</li>
                                <li><strong><a href="/admin/sessions">Admin Tools</a></strong>: User, session, and chat history management</li>
                            </ul>
                            <p>Use this app for team chats, support, or any group conversation.</p>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="/chat" class="card-footer-item">
                                <span class="icon">
                                    <i class="fas fa-comments"></i>
                                </span>
                            <span>Start Chatting</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

@template.fragments.footer()
</body>
</html>
