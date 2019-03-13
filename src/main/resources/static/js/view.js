class View {

    constructor() {

        View.ENTER_KEY = 13;
        View.ESCAPE_KEY = 27;

        this.$todoList = document.querySelector('.todo-list');
        this.$todoItemCounter = document.querySelector('.todo-count');
        this.$clearCompleted = document.querySelector('.clear-completed');
        this.$main = document.querySelector('.main');
        this.$toggleAll = document.querySelector('.toggle-all');
        this.$newTodo = document.querySelector('.new-todo');
        View.delegate(this.$todoList, 'li label', 'dblclick', ({target}) => {
            this.editItem(target);
        });

        this.$filterAll = document.getElementById("filter_");
        this.$filterActive = document.getElementById("filter_active");
        this.$filterComplete = document.getElementById("filter_complete");
    }

    bindFilterAll(handler) {
        this.$filterAll.addEventListener('click', ({target}) => {
            handler('');
        });
    }

    bindFilterActive(handler) {
        this.$filterActive.addEventListener('click', ({target}) => {
            handler('active');
        });
    }

    bindFilterComplete(handler) {
        this.$filterComplete.addEventListener('click', ({target}) => {
            handler('complete');
        });
    }

    /**
     * Put an item into edit mode.
     *
     * @param target Target item's label Element
     */
    editItem(target) {
        const listItem = target.parentElement;

        listItem.classList.add('editing');

        const input = document.createElement('input');
        input.className = 'edit';

        input.value = target.innerText;
        listItem.appendChild(input);
        input.focus();
    }

    /**
     * Populate the todolist with a list of items.
     *
     * @param items Array of items to display
     */
    showItems(items) {
        this.$todoList.innerHTML = "";
        for (let item of items) {
            let row = document.querySelector('#todoListItemTemplate');
            let clone = document.importNode(row.content, true);

            clone.querySelector("li").dataset.id = item.id;
            if (item.completed) {
                clone.querySelector("li").className = "completed";
                clone.querySelector(".toggle").checked = true;
            }
            clone.querySelector("label").innerHTML = View.escapeForHTML(item.title);

            this.$todoList.appendChild(clone);
        }
    }

    /**
     * Remove an item from the view.
     *
     * @param {number} id Item ID of the item to remove
     */
    removeItem(id) {
        const elem = document.querySelector(`[data-id="${id}"]`);
        if (elem) {
            this.$todoList.removeChild(elem);
        }
    }

    /**
     * Set the number in the 'items left' display.
     *
     * @param {number} itemsLeft Number of items left
     */
    setItemsLeft(itemsLeft) {
        const itemStr = itemsLeft !== 1 ? "items" : "item";
        this.$todoItemCounter.innerHTML = itemsLeft + " " + itemStr + " left";
    }

    /**
     * Set the visibility of the "Clear completed" button.
     *
     * @param {boolean} visible Desired visibility of the button
     */
    setClearCompletedButtonVisibility(visible) {
        this.$clearCompleted.style.display = visible ? 'block' : 'none';
    }

    /**
     * Set the visibility of the main content and footer.
     *
     * @param {boolean} visible Desired visibility
     */
    setMainVisibility(visible) {
        this.$main.style.display = visible ? 'block' : 'none';
    }

    /**
     * Set the checked state of the Complete All checkbox.
     *
     * @param {boolean} checked The desired checked state
     */
    setCompleteAllCheckbox(checked) {
        this.$toggleAll.checked = checked;
    }

    /**
     * Change the appearance of the filter buttons based on the route.
     *
     * @param {string} route The current route
     */
    updateFilterButtons(route) {
        document.querySelector('.filters>.selected').className = '';
        document.getElementById("filter_" + route).className = 'selected';
    }

    /**
     * Clear the new item input
     */
    clearNewTodo() {
        this.$newTodo.value = '';
    }

    /**
     * Bring an item out of edit mode.
     *
     * @param {!number} id Item ID of the item in edit
     * @param {!string} title New title for the item in edit
     */
    editItemDone(id, title) {
        const listItem = document.querySelector(`[data-id="${id}"]`);

        const input = listItem.querySelector('input.edit');
        listItem.removeChild(input);

        listItem.classList.remove('editing');

        listItem.querySelector('label').textContent = title;
    }

    /**
     * @param {Function} handler Function called on synthetic event.
     */
    bindAddItem(handler) {
        this.$newTodo.addEventListener('change', ({target}) => {
            const title = target.value.trim();
            if (title) {
                handler(title);
            }
        });
    }

    /**
     * @param {Function} handler Function called on synthetic event.
     */
    bindRemoveCompleted(handler) {
        this.$clearCompleted.addEventListener('click', handler);
    }

    /**
     * @param {Function} handler Function called on synthetic event.
     */
    bindToggleAll(handler) {
        this.$toggleAll.addEventListener('click', ({target}) => {
            handler(target.checked);
        });
    }

    /**
     * @param {Function} handler Function called on synthetic event.
     */
    bindRemoveItem(handler) {
        View.delegate(this.$todoList, '.destroy', 'click', ({target}) => {
            handler(View.itemId(target));
        });
    }

    /**
     * @param {Function} handler Function called on synthetic event.
     */
    bindToggleItem(handler) {
        View.delegate(this.$todoList, '.toggle', 'click', ({target}) => {
            handler(View.itemId(target), target.checked);
        });
    }

    /**
     * @param {Function} handler Function called on synthetic event.
     */
    bindEditItemSave(handler) {
        View.delegate(this.$todoList, 'li .edit', 'blur', ({target}) => {
            if (!target.dataset.iscanceled) {
                handler(View.itemId(target), target.value.trim());
            }
        }, true);

        // Remove the cursor from the input when you hit enter just like if it were a real form
        View.delegate(this.$todoList, 'li .edit', 'keypress', ({target, keyCode}) => {
            if (keyCode === View.ENTER_KEY) {
                target.blur();
            }
        });
    }

    /**
     * @param {Function} handler Function called on synthetic event.
     */
    bindEditItemCancel(handler) {
        View.delegate(this.$todoList, 'li .edit', 'keyup', ({target, keyCode}) => {
            if (keyCode === View.ESCAPE_KEY) {
                target.dataset.iscanceled = true;
                target.blur();
                handler(View.itemId(target));
            }
        });
    }

    // helpers

    /**
     * Attach a handler to an event for all elements matching a selector.
     *
     * @param {Element} target Element which the event must bubble to
     * @param {string} selector Selector to match
     * @param {string} type Event name
     * @param {Function} handler Function called when the event bubbles to target
     *                           from an element matching selector
     * @param {boolean} [capture] Capture the event
     */
    static delegate(target, selector, type, handler, capture) {
        const dispatchEvent = event => {
            const targetElement = event.target;
            const potentialElements = target.querySelectorAll(selector);

            for (var elem of potentialElements) {
                if (elem === targetElement) {
                    handler.call(targetElement, event);
                    break;
                }
            }
        };
        target.addEventListener(type, dispatchEvent, capture);
    }

    /**
     * Encode less-than and ampersand characters with entity codes to make user-
     * provided text safe to parse as HTML.
     *
     * @param {string} s String to escape
     *
     * @returns {string} String with unsafe characters escaped with entity codes
     */
    static escapeForHTML(s) {
        return s.replace(/[&<]/g, c => c === '&' ? '&amp;' : '&lt;');
    }

    static itemId(element) {
        return element.parentNode.dataset.id;
    }

}
