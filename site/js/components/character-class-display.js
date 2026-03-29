class CharacterClassDisplay extends HTMLElement {

    constructor() {
        super();
        this.lang = LanguageSelector.getLanguage();

        document.addEventListener('selectedLanguage', () => {
            this.lang = LanguageSelector.getLanguage();
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
                character-class-display {
                    max-width: 80%;
                    margin-left: auto;
                    margin-right: auto;
                    padding: 0 20px;
                    line-height: 1.6;
                }

                character-class-display h2 {
                    margin-top: 30px;
                    margin-bottom: 10px;
                }

                character-class-display p {
                    margin-bottom: 1em;
                }

                character-class-display {
                    display: block;
                    padding: 20px;
                }
            </style>
            <h1>${this.#title()}</h1>

            <section>
                <h2>${headers.description}</h2>
                ${this.#lore()}
            </section>

            <section>
                <h2>${this.#characteristics()}</h2>
                <section>
                    ${this.#baseCharacteristics()}
                </section>
                <section>
                    ${this.#skills()}
                </section>
                <section id='class-table'>

                </section>
            </section>

            <section>
                ${headers.source} : ${this.#source()}
            </section>
         `;

         document.getElementById('class-table').appendChild(new ClassTable(this.data['class_table']));
    }

    #title() {
       return this.data['name'][`${this.lang}`];
    }

    #lore() {
        return this.data['lore'][`${this.lang}`]
            .map(elem => `
                <p>${elem.title ? '<em>' + elem.title + ' : </em>' : ''} ${elem.content}</p>
            `).join('')
    }

    #source() {
        return `<a href="../sources/${this.source['name']['fr']}">` + this.source['name'][`${this.lang}`] + '</a>';
    }

    #characteristics() {
        return this.lang === 'fr' ? 'Caractéristiques' : 'Characteristics';
    }

    #baseCharacteristics() {
        return this.data['game_rule_information'][`${this.lang}`]
            .map(elem => `
                <p>${elem.title ? '<em>' + elem.title + ' : </em>' : ''} ${elem.content}</p>
            `).join('')
    }

    #skills() {
        let list = this.data['skills']['list'][`${this.lang}`];
        let number = this.data['skills']['perLevel'];
        if (this.lang === 'fr') {
            return `<em>Compétences : </em> (${number}+modificateur d'intelligence)x4 points de compétence au niveau 1, puis (${number}+modificateur d'intelligence) à chaque niveau. Les compétence de classe sont : ${list}.`
        } else {
            return `<em>Skills : </em> (${number}+intelligence modifier)x4 skill points at level 1, then (${number}+intelligence modifier) each level. The class skills are : ${list}.`
        }
    }
}

customElements.define('character-class-display', CharacterClassDisplay);
