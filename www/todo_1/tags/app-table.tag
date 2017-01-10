<app-table>
    <div class="container-fluid" if={app.myData.todos.length>0}>
        <div class="row">
            <div class="col-md-12">
                <h3 id="netStatus">{opts.title}</h3>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="todolist" class="table table-condensed">
                    <tr each={todo in app.myData.todos}>
                        <td>{todo.name}</td><td>{todo.description}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</app-table>
