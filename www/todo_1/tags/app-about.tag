<app-about>
    <div class="container-fluid" if={app.pages.about['visible'] == 1}>
        <div class="row">
            <div class="col-md-12">
                <h1>{text.title[app.language]}</h1>
                <p>{text.description[app.language]}</p>
            </div>
        </div>
    </div>
    <script>
        var self = this;
        self.text = {
            "title": {
                "en": "about",
                "pl": "o programie"
                },
            "description": {
                "en": "Features:<ul>"+
                      "<li>works online or offline (without backend data API)</li>"+
                      "<li>text localization</li>"+
                      "<li>Riot features: tags, router, events</li>"+
                      "<li>form submit library</li>"+
                      "</ul>",
                 "pl": "Lorem ipsum dolor sit amet magna. Sed ultricies ante. Mauris leo."
                }
        }
    </script>
</app-about>