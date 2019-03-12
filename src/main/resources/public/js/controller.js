class Controller {

    /**
     * @param {!View} view A View instance
     */
    constructor(view) {
        Controller.GET = "GET";
        Controller.POST = "POST";
        Controller.PUT = "PUT";
        Controller.DELETE = "DELETE";

        this.view = view;
        this._activeState = '';
        this._lastActiveState = null;

        view.bindAddItem(this.addItem.bind(this));
        view.bindEditItemSave(this.editItemSave.bind(this));
        view.bindEditItemCancel(this.editItemCancel.bind(this));
        view.bindRemoveItem(this.removeItem.bind(this));
        view.bindToggleItem(this.toggleCompleted.bind(this));
        view.bindRemoveCompleted(this.removeCompletedItems.bind(this));
        view.bindToggleAll(this.toggleAll.bind(this));

        view.bindFilterAll(this.updateState.bind(this));
        view.bindFilterActive(this.updateState.bind(this));
        view.bindFilterComplete(this.updateState.bind(this));

        this.updateState('');
    }

    updateState(newState) {
        this._activeState = newState;
        this._refresh();
        this.view.updateFilterButtons(newState);
    }


    /**
     * Sends an AJAX request.
     * @param endpoint The endpoint of the request, for example "/login"
     * @param method Request method. Can be "GET", "POST", "PUT", "DELETE",..
     * @param params Parameters in URL format, like "param1=3&param3=asdf"
     * @param onSuccess A function to call when the request completes successfully.
     *                  The data will be in event.target.response
     */
    sendAjax(endpoint, method, params = null, onSuccess = null) {
        const scope = this;
        const req = new XMLHttpRequest();
        req.addEventListener("load", function (event) {
            onSuccess.call(scope, event);
        });
        req.addEventListener("error", function (err) {
            console.log("Request failed for " + endpoint + " error: " + err);
        });
        req.open(method, endpoint);
        if (method === Controller.POST || method === Controller.PUT) {
            req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        }
        req.send(params);
    }

    /**
     * Add an item and display it in the list.
     */
    addItem(title) {
        this.sendAjax("addTodo", Controller.POST, "todo-title=" + title, function (event) {
            this.view.clearNewTodo();
            this._refresh(true);
            this._checkResponse(event.target.response, "addTodo");
        });
    }

    /**
     * Save an item in edit.
     */
    editItemSave(id, title) {
        if (title.length) {
            this.sendAjax("todos/" + id, Controller.PUT, "todo-title=" + title, function (event) {
                this.view.editItemDone(id, title);
                this._checkResponse(event.target.response, "todos/" + id);
            });
        } else {
            this.removeItem(id);
        }
    }

    /**
     * Cancel the item editing mode.
     */
    editItemCancel(id) {
        this.sendAjax("todos/" + id, Controller.GET, null, function (event) {
            this.view.editItemDone(id, event.target.response);
        });
    }

    /**
     * Remove the data and elements related to an Item.
     */
    removeItem(id) {
        this.sendAjax("todos/" + id, Controller.DELETE, null, function (event) {
            this.view.removeItem(id);
            this._checkResponse(event.target.response, "todos/" + id);

        });
    }

    /**
     * Remove all completed items.
     */
    removeCompletedItems() {
        this.sendAjax("todos/completed", Controller.DELETE, null, function (event) {
            this._refresh(true);
            this._checkResponse(event.target.response, "todos/completed");
        });
    }

    /**
     * Update an item in based on the state of completed.
     */
    toggleCompleted(id, completed) {
        this.sendAjax("todos/" + id + "/toggle_status", Controller.PUT, "status=" + completed, function (event) {
            this._refresh(true);
            this._checkResponse(event.target.response, "todos/" + id + "/toggle_status");
        });
    }

    /**
     * Set all items to complete or active.
     */
    toggleAll(completed) {
        this.sendAjax("todos/toggle_all", Controller.PUT, "toggle-all=" + completed, function (event) {
            this._refresh(true);
            this._checkResponse(event.target.response, "todos/toggle_all");
        });
    }

    /**
     * Refresh the list based on the current route.
     */
    _refresh(force) {
        const state = this._activeState;

        if (force || this._lastActiveState !== '' || this._lastActiveState !== state) {
            // an item looks like: {id:abc, title:"something", completed:true}
            this.sendAjax("list", Controller.POST, "status=" + state, function (event) {
                const respObj = JSON.parse(event.target.response);

                this.view.showItems(respObj);

                const total = respObj.length;
                const completed = respObj.filter(item => item.completed === true).length;
                const active = total - completed;

                this.view.setItemsLeft(active);
                this.view.setClearCompletedButtonVisibility(completed > 0);

                this.view.setCompleteAllCheckbox(completed === total);
                this.view.setMainVisibility(total > 0);
            });
        }
        this._lastActiveState = state;
    }

    _checkResponse(resp, funcName) {
        let respObj;
        try {
            respObj = JSON.parse(resp);
        } catch (e) {
            console.log(e);
        }
        if (!respObj || respObj.success !== true) {
            console.log("Error in the response for " + funcName)
        }
    }
}
