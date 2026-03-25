class RaceDisplay extends HTMLElement {

    constructor() {
        super();
        this.lang = 'fr';

        document.addEventListener('selectedLanguage', (e) => {
            this.lang = e.detail.language;
            if (this.data) {
                this.render();
            }
        });
    }

    setData(source, data) {
         this.source = source;
         this.data = data;
         this.render();
    }

    render() {
         const sectionHeaders = {
             fr: { description: 'Description', racialFeatures: 'Particularités Raciales', source: 'Source' },
             en: { description: 'Description', racialFeatures: 'Racial Features', source: 'Source' }
         };
         const headers = sectionHeaders[this.lang] || sectionHeaders['en'];

         this.innerHTML = `
            <style>
                race-display {
                    max-width: 80%;
                    margin-left: auto;
                    margin-right: auto;
                    padding: 0 20px;
                    line-height: 1.6;
                }

                race-display h2 {
                    margin-top: 30px;
                    margin-bottom: 10px;
                }

                race-display p {
                    margin-bottom: 1em;
                }

                race-display {
                    display: block;
                    padding: 20px;
                }
            </style>
            <h1>${this.#title()}</h1>

            <section>
                <h2>${headers.racialFeatures}</h2>
                ${this.#racialFeatures()}
            </section>

            <section>
                <h2>${headers.description}</h2>
                ${this.#lore()}
            </section>

            ${headers.source} : ${this.#source()}
         `;
    }

    #title() {
       return this.data['name'][`${this.lang}`];
    }

    #lore() {
        return this.data['lore'][`${this.lang}`]
            .map(elem => `
                <p>${elem.title ? '<em>' + elem.title + '</em>' : ''} ${elem.content}</p>
            `).join('')
    }

    #racialFeatures() {
        let items = this.data['racial-features'][`${this.lang}`].map(elem => `<li>${elem.content}</li>`).join('');
        return `<ul>
                    ${items}
                </ul>
        `
    }

    #source() {
        return `<a href="../sources/${this.source['name']['fr']}">` + this.source['name'][`${this.lang}`] + '</a>';
    }
}

customElements.define('race-display', RaceDisplay);
