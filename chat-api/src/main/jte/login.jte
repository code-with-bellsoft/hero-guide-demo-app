@param String title = "Chat Application - Login"
@param String error = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/webjars/bulma/1.0.3/css/bulma.min.css">
    <script defer src="/webjars/fontawesome/5.6.3/index.js"></script>
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
            </div>
        </div>
    </nav>

    <section class="section">
        <div class="container">
            <div class="columns">
                <div class="column is-6 is-offset-3">
                    <div class="card">
                        <div class="card-header">
                            <p class="card-header-title">
                                Login to Chat Application
                            </p>
                        </div>
                        <div class="card-content">
                            @if(error != null)
                                <div class="notification is-danger">
                                    ${error}
                                </div>
                            @endif
                            <form action="/login" method="post">
                                <div class="field">
                                    <label class="label">Username</label>
                                    <div class="control has-icons-left">
                                        <input class="input" type="text" name="username" placeholder="Enter your username" required>
                                        <span class="icon is-small is-left">
                                            <i class="fas fa-user"></i>
                                        </span>
                                    </div>
                                </div>
                                <div class="field">
                                    <label class="label">Password</label>
                                    <div class="control has-icons-left">
                                        <input class="input" type="password" name="password" placeholder="Enter your password" required>
                                        <span class="icon is-small is-left">
                                            <i class="fas fa-lock"></i>
                                        </span>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="control">
                                        <button class="button is-primary is-fullwidth" type="submit">
                                            <span class="icon">
                                                <i class="fas fa-sign-in-alt"></i>
                                            </span>
                                            <span>Login</span>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="card-footer">
                            <p class="card-footer-item">
                                Don't have an account? <a href="/register" class="ml-2">Register</a>
                            </p>
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