class LanguageSelector extends HTMLElement {

    static LANGUAGES = [
        { code: 'fr', label: 'FR' },
        { code: 'en', label: 'EN' }
    ];

    static getLanguage() {
        return localStorage.getItem('selectedLanguage') || 'fr';
    }

    constructor() {
        super();
        this._selected = LanguageSelector.getLanguage();
    }

    connectedCallback() {
        this.render();
    }

    get selected() {
        return this._selected;
    }

    set selected(langCode) {
        if (this._selected !== langCode) {
            this._selected = langCode;
            localStorage.setItem('selectedLanguage', langCode);
            this.render();
            this.dispatchEvent(new CustomEvent('selectedLanguage', {
                bubbles: true,
                composed: true
            }));
        }
    }

    render() {
        this.innerHTML = LanguageSelector.LANGUAGES.map(lang => {
            const activeClass = lang.code === this._selected ? 'active' : '';
            return `<button data-lang="${lang.code}" class="${activeClass}">${lang.label}</button>`;
        }).join('');

        this.querySelectorAll('button').forEach(btn => {
            btn.addEventListener('click', () => {
                this.selected = btn.dataset.lang;
            });
        });
    }
}

customElements.define('language-selector', LanguageSelector);

