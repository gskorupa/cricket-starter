<app-form>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <button class="btn btn-default pull-right">+</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form class="w3-container" method="POST" 
                      onsubmit="AJAXSubmit(this, AJAXGet);
                              return false;">
                    <div class="form-group">
                        <label for="name">Task name</label>
                        <input class="form-control" id="name" name="name" type="text">
                    </div>
                    <div class="form-group">
                        <label for="description">Description</label>
                        <input class="form-control" id="description" name="description" type="text">
                    </div>
                    <button type="submit" class="btn btn-default pull-right">Save</button>
                </form>
            </div>
        </div>
    </div>
</app-form>