class RacePage extends HTMLElement {

    connectedCallback() {
        const savedLang = localStorage.getItem('dnd-pedia-lang');
        const initialLang = savedLang || (navigator.language.startsWith('fr') ? 'fr' : 'en');

        // Build the header (name + source + lang toggle) and prepend it
        const header = document.createElement('div');
        header.className = 'race-header';
        header.innerHTML = `
            <div class="race-header__top">
                <div class="race-header__lang-toggle">
                    <button data-lang="en" class="lang-btn">EN</button>
                    <button data-lang="fr" class="lang-btn">FR</button>
                </div>
                <span class="race-header__source" data-lang="en">${this.getAttribute('source-en') || ''}</span>
                <span class="race-header__source" data-lang="fr">${this.getAttribute('source-fr') || ''}</span>
            </div>
            <h1 class="race-header__name" data-lang="en">${this.getAttribute('name-en') || ''}</h1>
            <h1 class="race-header__name" data-lang="fr">${this.getAttribute('name-fr') || ''}</h1>
        `;
        this.prepend(header);

        // Wire language toggle buttons
        this.querySelectorAll('.lang-btn').forEach(btn => {
            btn.addEventListener('click', () => this.switchLang(btn.dataset.lang));
        });

        // Apply initial language
        this.switchLang(initialLang);
    }

    switchLang(lang) {
        localStorage.setItem('dnd-pedia-lang', lang);

        // Show/hide all elements with data-lang
        this.querySelectorAll('[data-lang]').forEach(el => {
            el.style.display = el.dataset.lang === lang ? '' : 'none';
        });

        // Highlight active toggle button
        this.querySelectorAll('.lang-btn').forEach(btn => {
            btn.classList.toggle('active', btn.dataset.lang === lang);
        });
    }
}

customElements.define('race-page', RacePage);

