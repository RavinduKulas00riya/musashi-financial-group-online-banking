(() => {
    if (window.TimelyOperationsPage) {
        return window.TimelyOperationsPage;
    }

    function init() {
        console.log("TimelyOperationsPage initialized");

        setupOverlayAndSortFilterButtons();
        setupCopyIcons();
    }

    async function showRefreshButton() {
        const btn = document.getElementById("refresh-btn");
        const msg = document.getElementById("refresh-msg");

        console.log(msg);

        btn.classList.add("show");
        msg.classList.add("show");

        await new Promise((resolve) => setTimeout(resolve, 3000));
        msg.classList.remove("show");

        setTimeout(showRefreshButton, 6000);
    }

    function hideRefreshButton() {
        const btn = document.getElementById("refresh-btn");
        const msg = document.getElementById("refresh-msg");

        btn.classList.remove("show");
        msg.classList.remove("show");
    }

    function setupCopyIcons(){
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
    }



    function setupOverlayAndSortFilterButtons(){
        const TOoverlay = document.getElementById("TO_overlay");

        const openFiltersBtn = document.getElementById("openFilters");
        const filterDiv = document.getElementById("filterDiv");
        const applyBtn = document.getElementById("applyBtn");
        const resetBtn = document.getElementById("resetBtn");

        const openSortBtn = document.getElementById("openSort");
        const sortDiv = document.getElementById("sortDiv");

        openFiltersBtn.addEventListener("click", () => {
            TOoverlay.classList.add("show");
            filterDiv.classList.add("show");
        });

        openSortBtn.addEventListener("click", () => {
            TOoverlay.classList.add("show");
            sortDiv.classList.add("show");
        });

        function closeModal(action) {
            console.log(action + " button clicked");
            // TOoverlay.classList.remove("show");
            // filterDiv.classList.remove("show");
        }

        applyBtn.addEventListener("click", () => closeModal("Apply"));
        resetBtn.addEventListener("click", () => closeModal("Reset"));

        TOoverlay.addEventListener("click", (e) => {
            if (e.target === TOoverlay) {
                closeModal("overlay");
                TOoverlay.classList.remove("show");
                filterDiv.classList.remove("show");
                sortDiv.classList.remove("show");
            }
        });
    }

    function cleanup() {
        console.log("TransferHistoryPage cleanup");
        // if (socket && socket.readyState === WebSocket.OPEN) socket.close();
        // socket = null;
    }

    const pageAPI = { init, cleanup };
    window.TimelyOperationsPage = pageAPI;
    return pageAPI;
})();