@param String title = "Chat Application"
@param Boolean authenticated = false
@param String username = ""
@param Boolean isAdmin = false

<!DOCTYPE html>
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
