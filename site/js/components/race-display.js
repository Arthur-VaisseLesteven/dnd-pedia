class RaceDisplay extends HTMLElement {

    constructor() {
        super();
    }

    setData(data) {
         this.innerHTML = `
         `;
    }
}

customElements.define('race-display', RaceDisplay);
