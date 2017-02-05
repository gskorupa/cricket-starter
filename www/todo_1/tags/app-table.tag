<app-table>
    <div class="container-fluid" if={app.pages.main.visible == 1}>
        <div class="row" >
            <div class="col-md-12">
                <h4 id="netStatus">{text.title[app.language]}</h4>
            </div>
        </div>
        <div class="row"  if={app.myData.todos.length>0}>
            <div class="col-md-12">
                <table id="todolist" class="table table-condensed">
                    <tr>
                        <th>{text.h_name[app.language]}</th><th>{text.h_description[app.language]}</th><th></th>
                    </tr>
                    <tr each={todo in app.myData.todos}>
                        <td>{todo.name}</td><td>{todo.description}</td><td></td>
                    </tr>
                </table>
            </div>
        </div>
        <app-form></app-form>
    </div>
    <script>
        
        globalEvents.on('data:submitted',function(event){
            if(app.debug) { console.log("Here We go!") }
            getData()
        });
        globalEvents.on('data:ready',function(event){
            if(app.debug) { console.log("ready!") }
            riot.update()
        });
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
</app-table>
