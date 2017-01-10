<app-form>
    <div class="w3-container">
        <h3>Add new task</h3>
    </div>
    <form class="w3-container" method="POST" 
          onsubmit="AJAXSubmit(this, AJAXGet);
                  return false;">
        <label>Task name</label>
        <input class="w3-input w3-border" name="name" type="text">
        <label>Description</label>
        <input class="w3-input w3-border" name="description" type="text">
        <p></p>
        <button class="w3-btn w3-blue">Save</button>
    </form>
    <div class="w3-container">
        <p></p>
    </div>
</app-form>