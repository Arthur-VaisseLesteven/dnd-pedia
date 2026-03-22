// enhancments : upon having something typed in replace the glass by a cross that would empty the search field


class contentTable extends HTMLElement {
    columns;

    constructor() {
        super();

        this.innerHTML = `
        <style>
        content-table .content-table-row {
            cursor: pointer;
        }
        
        content-table .content-table-search {
            position: relative;
            top: 50px;
            left: 5px
        }
        tr.content-table-row:hover {
          box-shadow: 0 8px 30px rgba(196, 149, 106, 0.2),
                      0 2px 8px rgba(0, 0, 0, 0.4);
          background: rgba(196, 149, 106, 0.5);
        }
        </style>
        <div class="w3-container">
            <div>
              <input type="text" placeholder="Filtrer" class="w3-margin-bottom content-table-search">
            </div>
            <table class="w3-table w3-border w3-centered w3-card">
                <thead class="content-table-headers">
                </thead>
                <tbody class="content-table-body">
                </tbody>
            </table>
        </div>`

        this.columns = this.buildColumnsFromAttributes();
        this.buildHeaderLine();
        this.querySelector('.content-table-search').onkeyup = () => this.filterContent();
    }

    buildColumnsFromAttributes() {
        let columns = [],
            counter = 0;

        do {
            counter += 1;
            if (this.hasAttribute(`column-${counter}`)) {
                columns.push(this.getAttribute(`column-${counter}`));
            }
        } while (columns.length === counter);

        return columns;
    }

    buildHeaderLine() {
        this.querySelector(".content-table-headers").innerHTML = '<tr>' + this.columns.map(column => `<th>${column}</th>`).join('') + '</tr>';
    }

    /**
     * @param content.href      link to the content page
     * @param content.label     label of the content
     * @param content.source    source that content comes from
     */
    addContent(content) {
        this.querySelector(".content-table-body").insertAdjacentHTML('beforeend', this.buildContentLine(content))
    }

    buildContentLine(content) {
        return `<tr class="content-table-row" onclick="location.href=\'${content.href}\'">` + this.columns.map(column => `<td>${content[column]}</td>`).join('') + '</tr>';
    }

    filterContent() {
        for (let contentLine of this.querySelectorAll('.content-table-row')) {
            contentLine.style.display = this.matchSearch(contentLine) ? 'table-row' : 'none';
        }
    }

    matchSearch(contentLine) {
        return contentLine.textContent.toLowerCase().includes(this.querySelector(".content-table-search").value.toLowerCase());
    }
}

customElements.define('content-table', contentTable);