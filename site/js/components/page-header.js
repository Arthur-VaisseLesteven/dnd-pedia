class PageHeader extends HTMLElement {
    connectedCallback() {
        const baseUrl = this.getAttribute('base-url') || '.';
        this.innerHTML = `
            <header class="banner">
                <a href="${baseUrl}/index.html">
                    <img src="${baseUrl}/imgs/banner.svg" alt="D&D Pedia – Dungeons & Dragons 3.5 Compendium">
                </a>
            </header>
        `;
    }
}

customElements.define('page-header', PageHeader);
