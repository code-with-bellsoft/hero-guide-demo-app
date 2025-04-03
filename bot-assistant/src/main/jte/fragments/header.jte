@param String title
@param String extraStyle = ""
@param String extraScript = ""

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/webjars/bulma/1.0.3/css/bulma.min.css">
    <link rel="stylesheet" href="/webjars/fortawesome__fontawesome-free/6.7.2/css/all.min.css">
    @if(!extraStyle.isEmpty())
        $unsafe{extraStyle}
    @endif
    @if(!extraScript.isEmpty())
        $unsafe{extraScript}
    @endif
</head>
