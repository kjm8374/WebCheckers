<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers | ${title}</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">
        <form action="./account-details" method="POST">
            Current username: ${currentUser.name}
            <br/>
            New username:
            <br/>
            <input name="myName" />
            <br/>
            New password:
            <br/>
            <input name="myPassword" type="password" />
            <br/>
            Delete Account:
            <br/>
            To delete your account, confirm by typing your username in the box below
            <br/>
            <input name="deleteAccount" />
            <br/><br/>
            <button type="submit">Change log in info</button>
            </form>
    </div>

</div>
</body>

</html>
