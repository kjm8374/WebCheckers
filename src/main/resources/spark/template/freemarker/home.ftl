<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="refresh" content="10">
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers | ${title}</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">

        <!-- Provide a message to the user, if supplied. -->
        <#include "message.ftl" />

        <h2 style="color:#8A2BE2"> Players Online </h2>
        <#-- If the currentUser is signed in-->
        <#if currentUser??>
            <ul>
                <#-- iterating through the list of users -->
                <#list usersList>
                <#--Part executed once if we have more than 0 items -->
                    <li> List of players :
                    <ul>
                        <#items as user>
                        <#-- Part repeated for each item -->
                            <li> <a href="/game?opponent=${user.name}"> ${user.name} </a> </li>
                        </#items>
                        <#--Part executed once if we have more than 0 items -->
                    </ul>
                <#else>
                <#-- Part executed when there are 0 items -->
                    <li><i>There are no other players available to play at this time. </i></li>
                </#list>
                </li>
            </ul>
            <hr>
            <h2 style="color:#8A2BE2"> Replays </h2>
            <ul>
                <#-- iterating through the list of replays -->
                <#list replaysList>
                <#--Part executed once if we have more than 0 items -->
                    <li> List of replays:
                    <ul>
                        <#items as replay>
                        <#-- Part repeated for each item -->
                            <#-- Add a link to view the replay -->
                            <li> <a href="/replay/game?gameID=${replay.getGameID()}"> ${replay.toString()} </a> </li>
                        </#items>
                        <#--Part executed once if we have more than 0 items -->
                    </ul>
                <#else>
                <#-- Part executed when there are 0 items -->
                    <li><i>There are no replays. </i></li>
                </#list>
                </li>
            </ul>
        <#-- If the currentUser is not signed in-->
        <#else>
            <ul>
                <#-- if the number of players is 1-->
                <#if usersList?size = 1>
                    <li><i> There is ${usersList?size} player signed in. </i></li>
                <#else>
                    <li><i> There are ${usersList?size} players signed in. </i></li>
                </#if>
            </ul>
        </#if>
    </div>

</div>
</body>

</html>
