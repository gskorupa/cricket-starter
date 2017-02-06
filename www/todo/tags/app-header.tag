<app-header>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-links" aria-expanded="false">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">{ text.name[app.language] }</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="navbar-links">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#" data-toggle="collapse" data-target="#navbar-links">{ text.tasks[app.language] }</a></li>
                    <li><a href="#about" data-toggle="collapse" data-target="#navbar-links">{ text.about[app.language] }</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
    <script>
        this.text = {
            "name": {
                "en": "TODOs",
                "pl": "Zadania"
                },
            "tasks": {
                "en": "Tasks list",
                "pl": "Lista zadań"
                },
            "about": {
                "en": "About",
                "pl": "O programie"
                }
        }
    </script>
</app-header>
