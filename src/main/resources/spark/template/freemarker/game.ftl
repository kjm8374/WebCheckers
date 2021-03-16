<!DOCTYPE html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>

  <title>${title} | Web Checkers</title>
  <link rel="stylesheet" href="/css/style.css">
  <link rel="stylesheet" href="/css/game.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script>
  window.gameData = {
    "gameID" : ${gameID!'null'},
    "currentUser" : "${currentUser.name}",
    "viewMode" : "${viewMode}",
    "modeOptions" : ${modeOptionsAsJSON!'{}'},
    "redPlayer" : "${redPlayer.name}",
    "whitePlayer" : "${whitePlayer.name}",
    "activeColor" : "${activeColor}"
  };
  </script>
  <#if ((activeColor == "WHITE" && whitePlayer.name == currentUser.name) || (activeColor == "RED" && redPlayer.name == currentUser.name))>
         <audio id="audio" src="/ogg/piano_loop_syncopation.ogg" autoplay = ${((activeColor == "WHITE" && whitePlayer.name == currentUser.name) || (activeColor == "RED" && redPlayer.name == currentUser.name))?c}></audio>
  </#if>
 </head>
<body>
  <div class="page">
    <h1>Web Checkers | Game View</h1>
    
    <#include "nav-bar.ftl" />

    <div class="body">

      <div id="help_text" class="INFO"></div>

      <div>
        <div id="game-controls">

          <fieldset id="game-info">
            <legend>Info</legend>

            <#include "message.ftl" />

            <div>
              <table data-color='RED'>
                <tr>
                  <td><img src="../img/single-piece-red.svg" /></td>
                  <td class="name">Red</td>
                </tr>
              </table>
              <table data-color='WHITE'>
                <tr>
                  <td><img src="../img/single-piece-white.svg" /></td>
                  <td class="name">White</td>
                </tr>
              </table>
            </div>
          </fieldset>
          
          <fieldset id="game-toolbar">
            <legend>Controls</legend>
            <div class="toolbar"></div>
          </fieldset>
          
        </div>
  
        <div class="game-board">
          <table id="game-board">
            <tbody>
            <#list board.iterator() as row>
              <tr data-row="${row.getIndex()}">
              <#list row.iterator() as space>
                <td data-cell="${space.getCellIdx()}"
                    <#if space.isValid() >
                    class="Space"
                    </#if>
                    >
                <#if space.getPiece()??>
                  <div class="Piece"
                       id="piece-${row.getIndex()}-${space.getCellIdx()}"
                       data-type="${space.getPiece().getType()}"
                       data-color="${space.getPiece().getColor()}">
                  </div>
                </#if>
                </td>
              </#list>
              </tr>
            </#list>
            </tbody>
          </table>
        </div>
      </div>

    </div>
  </div>
  <script data-main="/js/game/index" src="/js/require.js"></script>
  </body>
</html>
