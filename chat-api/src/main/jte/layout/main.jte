@param String title
@param String content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/webjars/bulma/css/bulma.min.css">
    <script defer src="/webjars/fontawesome/5.6.3/js/all.js"></script>
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
        ${content}
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
