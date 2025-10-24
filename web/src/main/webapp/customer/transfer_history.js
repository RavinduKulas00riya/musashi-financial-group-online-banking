(function () {
    const resetBtn = document.getElementById("reset");
    resetBtn.addEventListener("click", () => {
        const accountNum = document.getElementById("account-num");
        const counterparty = document.getElementById("counterparty");
        const startDate = document.getElementById("startDate");
        const endDate = document.getElementById("endDate");
        const options = document.querySelectorAll(".custom-option");
        const select = document.getElementById("type-select");
        const trigger = select.querySelector(".custom-select-trigger");
        const textSpan = trigger.querySelector("span");
        const defaultOption = select.querySelector(
            '.custom-option[data-value="0"]'
        );
        options.forEach((option) => {
            option.classList.remove("selected");
        });

        accountNum.value = "";
        counterparty.value = "";
        options.forEach((o) => o.classList.remove("selected"));
        defaultOption.classList.add("selected");
        textSpan.textContent = defaultOption.textContent;
        trigger.dataset.value = defaultOption.dataset.value;
        trigger.classList.remove("active");
        select.classList.remove("open");
        startDate.value = "";
        startDate.style.borderColor = "#0d0d0d46";
        endDate.value = "";
        endDate.style.borderColor = "#0d0d0d46";
    });
})();

document.querySelectorAll(".custom-select").forEach((select) => {
    const trigger = select.querySelector(".custom-select-trigger");
    const options = select.querySelectorAll(".custom-option");

    // ✅ Get the <span> inside trigger (we'll update only this)
    const textSpan = trigger.querySelector("span");

    // open/close dropdown
    trigger.addEventListener("click", () => {
        document.querySelectorAll(".custom-select").forEach((s) => {
            if (s !== select) s.classList.remove("open");
        });
        select.classList.toggle("open");
    });

    // option click
    options.forEach((option) => {
        option.addEventListener("click", () => {
            options.forEach((o) => o.classList.remove("selected"));
            option.classList.add("selected");

            // ✅ Update only the text content (not the whole trigger)
            textSpan.textContent = option.textContent;

            trigger.dataset.value = option.dataset.value;
            select.classList.remove("open");

            // 🔹 Update border color depending on value
            if (option.dataset.value === "0") {
                trigger.classList.remove("active"); // grey
            } else {
                trigger.classList.add("active"); // blue
            }
        });
    });
});

// close dropdown when clicking outside
document.addEventListener("click", (e) => {
    if (!e.target.closest(".custom-select")) {
        document
            .querySelectorAll(".custom-select")
            .forEach((s) => s.classList.remove("open"));
    }
});

(function () {
    const inputs = document.querySelectorAll(".dateInput");
    const dateError = document.getElementById("dateError");

    function isValidDate(dateStr) {
        const regex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
        const match = dateStr.match(regex);
        if (!match) return false;

        const day = parseInt(match[1]);
        const month = parseInt(match[2]);
        const year = parseInt(match[3]);

        const date = new Date(year, month - 1, day);
        return (
            date.getFullYear() === year &&
            date.getMonth() === month - 1 &&
            date.getDate() === day
        );
    }

    function formatDate(digits) {
        const n = digits.length;
        let out = "";

        if (n > 0) {
            // day (DD)
            out += digits.slice(0, Math.min(2, n));
            // add slash after day as soon as day is complete (n >= 2)
            if (n >= 2) out += "/";

            // month (MM)
            if (n > 2) {
                out += digits.slice(2, Math.min(4, n));
                // add slash after month as soon as month is complete (n >= 4)
                if (n >= 4) out += "/";
            }

            // year (YYYY)
            if (n > 4) {
                out += digits.slice(4, Math.min(8, n));
            }
        }

        return out;
    }

    inputs.forEach((input) => {
        input.addEventListener("input", () => {
            const raw = input.value;
            const digits = raw.replace(/\D/g, "").slice(0, 8);
            input.value = formatDate(digits);
        });

        input.addEventListener("keydown", (e) => {
            const allowed = [
                "Backspace",
                "Delete",
                "ArrowLeft",
                "ArrowRight",
                "Tab",
                "Home",
                "End",
            ];
            if (allowed.includes(e.key)) return;
            if (/^[0-9]$/.test(e.key)) return;
            e.preventDefault(); // block letters/symbols
        });

        // input.addEventListener("paste", (e) => {
        //   e.preventDefault();
        //   const text = (e.clipboardData || window.clipboardData).getData(
        //     "text"
        //   );
        //   const digits = text.replace(/\D/g, "").slice(0, 8);
        //   input.value = formatDate(digits);
        // });

        input.addEventListener("blur", () => {
            const value = input.value.trim();

            if (value === "") {
                dateError.classList.remove("show");
                input.style.borderColor = "#0d0d0d46";
                return;
            }

            if (!isValidDate(value)) {
                dateError.classList.add("show");
                input.style.borderColor = "#d8000c";
            } else {
                dateError.classList.remove("show");
                input.style.borderColor = "#007bff";
            }
        });

        input.addEventListener("input", () => {
            dateError.classList.remove("show");
            input.style.borderColor = "#0d0d0d46";
        });
    });
})();

document.querySelectorAll(".copy-icon").forEach((icon) => {
    icon.addEventListener("click", () => {
        // optional: copy nearby text to clipboard
        // const textToCopy = icon.nextElementSibling.textContent.trim();
        // navigator.clipboard.writeText(textToCopy);

        // change icon and tooltip
        icon.classList.remove("fa-clone");
        icon.classList.add("fa-check");

        // revert after 3s
        setTimeout(() => {
            icon.classList.remove("fa-check");
            icon.classList.add("fa-clone");
        }, 3000);
    });
});