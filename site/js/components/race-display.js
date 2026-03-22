class RaceDisplay extends HTMLElement {

    constructor() {
        super();
    }

    setData(source, data) {
         this.source = source;
         this.data = data;
         this.lang = 'fr';

         this.innerHTML = `
            <style>
                race-display {
                    max-width: 800px;
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
                <h2>Description</h2>
                ${this.#lore()}
            </section>

            <section>
                <h2>Particularités Raciale</h2>
                ${this.#racialFeatures()}
            </section>

            Source : ${this.#source()}
         `;
    }

    #title() {
       return this.data['name'][`${this.lang}`];
    }

    #lore() {
        return this.data['lore'][`${this.lang}`]
            .map(elem => `
                <section>
                    ${elem.title ? '<h3>' + elem.title + '</h3>' : ''}
                    <p>${elem.content}</p>
                </section>
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
