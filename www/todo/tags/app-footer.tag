<app-footer>
    <footer class="container-fluid">
        <div class="row">
            <div class="col-md-12 disabled">
                <p></p>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12 disabled">
                <p>{app.offline ? "offline" : "online"}</p>
            </div>
        </div>
    </footer>
    <style>
        :scope {
            margin-top: 10px;
            display: block;
            background-color: #b4b9de;
        }
    </style>
</app-footer>