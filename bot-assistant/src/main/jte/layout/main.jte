@param String title
@param String content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/webjars/bulma/css/bulma.min.css">
    <script defer src="/webjars/fontawesome/index.js"></script>
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
        ${content}
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
