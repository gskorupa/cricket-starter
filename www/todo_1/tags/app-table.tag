<app-table>
    <div class="container-fluid" if={app.pages.main.visible == 1}>
        <div class="row" >
            <div class="col-md-12">
                <h4 id="netStatus">{opts.title}</h4>
            </div>
        </div>
        <div class="row"  if={app.myData.todos.length>0 && app.pages.main.visible == 1}>
            <div class="col-md-12">
                <table id="todolist" class="table table-condensed">
                    <tr>
                        <th>name</th><th>description</th><th></th>
                    </tr>
                    <tr each={todo in app.myData.todos}>
                        <td>{todo.name}</td><td>{todo.description}</td><td></td>
                    </tr>
                </table>
            </div>
        </div>
        <app-form></app-form>
    </div>     
</app-table>
