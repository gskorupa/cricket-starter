<app-form>
    <div class="row">
        <div class="col-md-12">
            <button class="btn btn-default pull-right" onclick={ toggleForm }>+</button>
        </div>
    </div>
    <div class="row" show={ visible }>
         <div class="col-md-12">
            <form class="w3-container" onsubmit={ submitForm }>
                <div class="form-group">
                    <label for="name">{ text.l_name[app.language] }</label>
                    <input class="form-control" id="name" name="name" type="text" required>
                </div>
                <div class="form-group">
                    <label for="description">{ text.l_description[app.language] }</label>
                    <input class="form-control" id="description" name="description" type="text">
                </div>
                <button type="submit" class="btn btn-default pull-right">{ text.l_save[app.language] }</button>
            </form>
        </div>
    </div>
    <script>
        this.visible = true
        
        globalEvents.on('data:submitted',function(event){
            if(app.debug) { console.log("I'm happy!") }
        });
        
        submitForm = function(e){
            if(app.debug) { console.log("submitting ..."+e.target) }
            if (app.offline) {
                submitLocally(e.target, "data:submitted");
            }else{
                postFormData(e.target, app.resourcesUrl, "data:submitted")
            }
            e.target.reset()
            e.preventDefault()
        }

        toggleForm = function(e){
            if(app.debug) { console.log("hello") };
            if (app.myData.todos.length == 0) {
                this.visible = true;
            } else {
                this.visible = !this.visible;
            }
        }
        
        // store application form data locally (if the service is not available)
        submitLocally = function(oFormElement, eventName) {
            var newToDo = {
                "name": oFormElement.elements["name"].value,
                "description": oFormElement.elements["description"].value
            };
            app.myData.todos.push(newToDo);
            globalEvents.trigger(eventName);
        }
        
        this.text = {
            "l_name": {
                "en": "Task name",
                "pl": "Nazwa zadania"
                },
            "l_description": {
                "en": "Description",
                "pl": "Opis"
                },
            "l_save": {
                "en": "Save",
                "pl": "Zapisz"
                }
        }

    </script>
</app-form>