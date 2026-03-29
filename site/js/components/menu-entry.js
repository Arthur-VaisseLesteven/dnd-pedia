class MenuEntry extends HTMLElement {

    constructor() {
        super();

        let href = this.getAttribute('href');
        let src = this.getAttribute('src');
        let title = this.getAttribute('title');
        let subtitle = this.getAttribute('subtitle');

        this.innerHTML = `
            <a href="${href}" class="category-card">
                <img class="category-card__image" src="${src}" alt="=${title}">
                <div class="category-card__body">
                    <div class="category-card__title">${title}</div>
                    <div class="category-card__subtitle">${subtitle}</div>
                </div>
            </a>
        `;
    }

}

customElements.define('menu-entry', MenuEntry);
