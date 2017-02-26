<app-footer>
    <footer class="container-fluid">
        <div class="row">
            <div class="col-12 disabled">
                <p></p>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-3 disabled">
                <p class="acti" onclick={toggleNetworkMode}>{app.offline ? "offline" : "online"}</p>
            </div>
            <div class="col-sm-3 disabled">
                <p class="acti" onclick={toggleLanguage}>{app.language}</p>
            </div>
            <div class="col-sm-6 disabled">
                <p></p>
            </div>
        </div>
    </footer>
    <script>
        toggleNetworkMode = function(){
            app.offline = !app.offline
        }
        toggleLanguage = function(){
            if(app.language=="en"){
                app.language="pl"
            }else{
                app.language="en"
            }
            riot.update()
        }
    </script>
    <style>
        :scope {
            margin-top: 10px;
            display: block;
            background-color: #b4b9de;
        }
        p.acti:hover {
            color: red;
            text-decoration: underline;
        }
    </style>
</app-footer>