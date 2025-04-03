@param String title
@param gg.jte.Content extraStyle = null
@param gg.jte.Content extraScript = null

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/webjars/bulma/1.0.3/css/bulma.min.css">
    <link rel="stylesheet" href="/webjars/fortawesome__fontawesome-free/6.7.2/css/all.min.css">
    @if(extraStyle!=null)
        ${extraStyle}
    @endif
    @if(extraScript!=null)
        ${extraScript}
    @endif
</head>
