@param String title = "Chat Application"
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
                                <p>This is a real-time chat application built with Spring Boot, WebSocket, and MongoDB.</p>
                                <p>Features:</p>
                                <ul>
                                    <li>Real-time messaging with WebSocket</li>
                                    <li>Chat history stored in MongoDB</li>
                                    <li>AI-powered bot assistant</li>
                                    <li>User authentication and authorization</li>
                                </ul>
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

    <footer class="footer">
        <div class="content has-text-centered">
            <p>
                <strong>Chat Application</strong> powered by Spring Boot and jte.
            </p>
        </div>
    </footer>
</body>
</html>
