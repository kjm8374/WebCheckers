<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers | ${title}</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">
        <#if message??>
        <div class="message ${messageType}">${message}</div>
        </#if>

        <form action="./signin" method="POST">
            Name
            <br/>
            <input name="myName" />
            <br/>
            Password (optional)
            <br/>
            <input name="myPassword" type="password" />
            <br/><br/>
            <button type="submit">Log In</button>
            </form>
    </div>

</div>
</body>

</html>