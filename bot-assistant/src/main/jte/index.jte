@param String title = "Bot Assistant"

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
    <nav class="navbar is-info" role="navigation" aria-label="main navigation">
        <div class="navbar-brand">
            <a class="navbar-item" href="/">
                <strong>Bot Assistant</strong>
            </a>
        </div>
        <div class="navbar-menu">
            <div class="navbar-start">
                <a class="navbar-item" href="/">Home</a>
                <a class="navbar-item" href="/assistant">Assistant</a>
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
                                Welcome to the Bot Assistant
                            </p>
                        </div>
                        <div class="card-content">
                            <div class="content">
                                <p>This is an AI-powered bot assistant built with Spring Boot, Spring AI, and Redis.</p>
                                <p>Features:</p>
                                <ul>
                                    <li>AI-powered responses using OpenAI</li>
                                    <li>Caching with Redis for improved performance</li>
                                    <li>RESTful API for integration with other services</li>
                                </ul>
                            </div>
                        </div>
                        <div class="card-footer">
                            <a href="/assistant" class="card-footer-item">
                                <span class="icon">
                                    <i class="fas fa-robot"></i>
                                </span>
                                <span>Try the Assistant</span>
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
                <strong>Bot Assistant</strong> powered by Spring Boot, OpenAI, and jte.
            </p>
        </div>
    </footer>
</body>
</html>
