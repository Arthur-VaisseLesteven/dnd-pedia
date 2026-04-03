class ScrollToTop extends HTMLElement {

    connectedCallback() {
        this.innerHTML = `<button aria-label="Back to top">&#x2191;</button>`;
        this._button = this.querySelector('button');

        this._onScroll = () => {
            this.classList.toggle('visible', window.scrollY > 100);
        };

        window.addEventListener('scroll', this._onScroll, { passive: true });

        this._button.addEventListener('click', () => {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }

    disconnectedCallback() {
        window.removeEventListener('scroll', this._onScroll);
    }
}

customElements.define('scroll-to-top', ScrollToTop);
