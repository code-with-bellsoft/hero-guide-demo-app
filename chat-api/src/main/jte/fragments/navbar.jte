@param Boolean authenticated = false
@param String username = ""
@param Boolean isAdmin = false

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