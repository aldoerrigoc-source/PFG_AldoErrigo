# Exercise R2 — Nursery Inventory

You are given a working web application that displays a nursery's plant inventory in a table: image, name, and stock quantity for each plant. Right now the table is read-only.

## What you need to add

**1. Inline editing.** For each row, add an "Edit" control that lets the user change the stock value and save it to the server, updating the cell immediately — without reloading the page. To make this checkable, please follow this structure:

- The edit control must be a `<button class="edit-btn" data-id="X">`, where `X` is the plant's id.
- Clicking it must replace the stock cell's content with a number input, `<input id="stock-input-X">`, pre-filled with the current stock value.
- Add a save control, `<button class="save-btn" data-id="X">`, that sends the new value to the server and, once the server confirms it was saved, updates the stock cell back to plain text showing the new value.

**2. Edit counter.** Add a visible counter, with `id="editCounter"`, that shows how many edits have been saved successfully during the current browser session (e.g. "Edits saved: 2").

- It must start at 0 when the page loads.
- It must increase by 1 every time the server confirms an edit was saved successfully.
- It must accumulate across multiple edits (if you save 3 edits, it must show 3), not reset between them.
- All of this without ever reloading the page.

You don't need to change any other behavior of the application.
