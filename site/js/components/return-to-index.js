class ReturnToIndex extends HTMLElement {

    static get observedAttributes() {
        return ['href', 'label'];
    }

    constructor() {
        super();
    }

    connectedCallback() {
        this.render();
    }

    attributeChangedCallback() {
        this.render();
    }

    get href() {
        return this.getAttribute('href') || './list.html';
    }

    get label() {
        return this.getAttribute('label') || '';
    }

    render() {
        const labelHtml = this.label ? `<span>${this.label}</span>` : '';
        this.innerHTML = `<a href="${this.href}"><span class="arrow">&#x2190;</span>${labelHtml}</a>`;
    }
}

customElements.define('return-to-index', ReturnToIndex);

