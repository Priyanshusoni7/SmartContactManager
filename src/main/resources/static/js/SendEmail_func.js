function updateForm() {
    const emailType = document.getElementById("emailType").value;
    const recipientField = document.getElementById("recipientField");
    const fileField = document.getElementById("fileField");
    const bodyField = document.getElementById("bodyField");

    if (emailType === "single") {
        recipientField.innerHTML = `
            <label for="to" class="block text-sm font-medium text-gray-700 dark:text-gray-300">Recipient Email</label>
            <input type="email" name="to" id="to" required
                class="mt-2 w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600">
        `;
        fileField.classList.add("hidden");
    } else if (emailType === "multiple") {
        recipientField.innerHTML = `
            <label for="to" class="block text-sm font-medium text-gray-700 dark:text-gray-300">Recipient Emails (Comma Separated)</label>
            <input type="text" name="to" id="to" required
                class="mt-2 w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600">
        `;
        fileField.classList.add("hidden");
    } else if (emailType === "html") {
        bodyField.innerHTML = `
            <label for="body" class="block text-sm font-medium text-gray-700 dark:text-gray-300">HTML Content</label>
            <textarea name="body" id="body" rows="10" required
                class="mt-2 w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600"></textarea>
        `;
        fileField.classList.add("hidden");
    } else if (emailType === "file") {
        fileField.classList.remove("hidden");
    }
}