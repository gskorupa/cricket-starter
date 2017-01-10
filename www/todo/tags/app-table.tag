<app-table>
    <div class="w3-container" if={app.myData.todos.length>0}>
        <h3 id="netStatus">{opts.title}</h3>
    </div>
    <div class="w3-container" if={app.myData.todos.length>0}>
        <table id="todolist" class="w3-table">
            <tr each={todo in app.myData.todos}>
                <td>{todo.name}</td><td>{todo.description}</td>
            </tr>
        </table>
    </div>
</app-table>
