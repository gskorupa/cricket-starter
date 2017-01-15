<app-form>
        <div class="row">
            <div class="col-md-12">
                <button class="btn btn-default pull-right" onclick="app.pages.main.toggleForm()">+</button>
            </div>
        </div>
        <div class="row"  show={app.pages.main.isFormVisible()}>
            <div class="col-md-12">
                <form class="w3-container" method="POST" 
                      onsubmit="postData(this, getData); return false;">
                    <div class="form-group">
                        <label for="name">Task name</label>
                        <input class="form-control" id="name" name="name" type="text" required>
                    </div>
                    <div class="form-group">
                        <label for="description">Description</label>
                        <input class="form-control" id="description" name="description" type="text">
                    </div>
                    <button type="submit" class="btn btn-default pull-right">Save</button>
                </form>
            </div>
        </div>
</app-form>