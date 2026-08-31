// R2 - Nursery Inventory : inline edit + edit counter (client session)
(function () {
    "use strict";

    var editCount = 0;

    document.addEventListener("DOMContentLoaded", function () {
        setupCounter();
        setupRows();
    });

    // Visible counter, starts at 0 on page load.
    function setupCounter() {
        var counter = document.getElementById("editCounter");
        if (!counter) {
            counter = document.createElement("p");
            counter.id = "editCounter";
            var table = document.getElementById("inventoryTable");
            table.parentNode.insertBefore(counter, table.nextSibling);
        }
        renderCounter();
    }

    function renderCounter() {
        document.getElementById("editCounter").textContent =
            "Edits saved: " + editCount;
    }

    // Add an Edit button (in its own cell) to every data row.
    function setupRows() {
        var stockCells = document.querySelectorAll('#inventoryTable td[id^="stock-"]');
        for (var i = 0; i < stockCells.length; i++) {
            var id = stockCells[i].getAttribute("data-id");
            var actions = document.createElement("td");
            actions.appendChild(makeEditButton(id));
            stockCells[i].parentNode.appendChild(actions);
        }
    }

    function makeEditButton(id) {
        var btn = document.createElement("button");
        btn.className = "edit-btn";
        btn.setAttribute("data-id", id);
        btn.textContent = "Edit";
        btn.addEventListener("click", function () { startEdit(id); });
        return btn;
    }

    function makeSaveButton(id) {
        var btn = document.createElement("button");
        btn.className = "save-btn";
        btn.setAttribute("data-id", id);
        btn.textContent = "Save";
        btn.addEventListener("click", function () { save(id); });
        return btn;
    }

    // Replace the stock cell with a pre-filled number input.
    function startEdit(id) {
        var stockCell = document.getElementById("stock-" + id);
        var match = stockCell.textContent.trim().match(/\d+/);
        var current = match ? match[0] : "";

        stockCell.innerHTML =
            '<input type="number" id="stock-input-' + id + '" value="' + current + '">';

        var actionsCell = stockCell.nextElementSibling;
        actionsCell.innerHTML = "";
        actionsCell.appendChild(makeSaveButton(id));
    }

    // Send new value to the server; on confirmation, update cell + counter.
    function save(id) {
        var input = document.getElementById("stock-input-" + id);
        var newValue = input.value;

        fetch("update?id=" + id + "&value=" + encodeURIComponent(newValue))
            .then(function (response) {
                if (!response.ok) { throw new Error("save failed"); }
                return response.text();
            })
            .then(function () {
                var stockCell = document.getElementById("stock-" + id);
                stockCell.textContent = newValue;

                var actionsCell = stockCell.nextElementSibling;
                actionsCell.innerHTML = "";
                actionsCell.appendChild(makeEditButton(id));

                editCount++;
                renderCounter();
            })
            .catch(function () {
                // On failure leave the input in place so the user can retry.
            });
    }
})();
