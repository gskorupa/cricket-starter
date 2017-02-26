<app-table>
    
    <div class="container-fluid" if={app.currentPage == 'main'}>
        <div class="row" >
            <div class="col-md-12">
                <h4 id="netStatus">{text.title[app.language]}</h4>
            </div>
        </div>
        <div class="row"  if={app.myData.todos.length>0}>
            <div class="col-md-12">
                <table id="todolist" class="table table-condensed">
                    <tr>
                        <th>{text.h_name[app.language]}</th>
                        <th>{text.h_description[app.language]}</th>
                        <th>action</th>
                    </tr>
                    <tr each={todo in app.myData.todos}>
                        <td>{todo.name}</td>
                        <td>{todo.description}</td>
                        <td><span class=delete onclick="deleteRow({todo.id})">[ delete ]</span></td>
                    </tr>
                </table>
            </div>
        </div>
        <app-form></app-form>
    </div>
    
    <script>
        globalEvents.on('data:submitted',function(event){
            if(app.debug) { console.log("Here We go!") }
            getData(app.resourcesUrl, app.myData, globalEvents, "data:ready", "data:error", app.debug)
        });
        globalEvents.on('data:ready',function(event){
            if(app.debug) { console.log("ready!") }
            riot.update()
        });
        
        deleteRow = function(id){
            if(app.offline){
                deleteLocally(id, globalEvents, "data:ready" , "data:error", app.debug)
            }else{
                deleteData(id, app.resourcesUrl, globalEvents, "data:submitted", "data:error", app.debug)
            }
        }
        
        deleteLocally = function(id, eventBus, successEventName, errorEventName, debug){
            if(debug){ console.log("deleting id="+id) }
            var ok = false;
            for (var i = app.myData.todos.length - 1; i >= 0; i--) {
                if (app.myData.todos[i].id == id) {
                    app.myData.todos.splice(i, 1);
                    ok = true;
                    break;
                }
            }
            if(ok){
                eventBus.trigger(successEventName);
            }else{
                eventBus.trigger(errorEventName);
            }          
        }
        
        this.text = {
            "title": {
                "en": "my tasks",
                "pl": "moje zadania"
                },
            "h_name": {
                "en": "task",
                "pl": "zadanie"
                },
            "h_description": {
                "en": "description",
                "pl": "opis"
                }
        }
    </script>
    <style>
        span.delete:hover {
            color: red;
            text-decoration: underline;
        }
    </style>
</app-table>
