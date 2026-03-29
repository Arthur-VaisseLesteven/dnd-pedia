class ClassTable extends HTMLElement {

	static GOOD_BBA = [ '+1', '+2', '+3', '+4', '+5', '+6/+1', '+7/+2', '+8/+3', '+9/+4', '+10/+5', '+11/+6/+1', '+12/+7/+2', '+13/+8/+3', '+14/+9/+4', '+15/+10/+5', '+16/+11/+6/+1', '+17/+12/+7/+2', '+18/+13/+8/+3', '+19/+14/+9/+4', '+20/+15/+10/+5' ];
	static AVERAGE_BBA = [ '+0', '+1', '+2', '+3', '+3', '+4', '+5', '+6/+1', '+6/+1', '+7/+2', '+8/+3', '+9/+4', '+9/+4', '+10/+5', '+11/+6/+1', '+12/+7/+2', '+12/+7/+2', '+13/+8/+3', '+14/+9/+4', '+15/+10/+5' ];
	static POOR_BBA = [ '+0', '+1', '+1', '+2', '+2', '+3', '+3', '+4', '+4', '+5', '+5', '+6/+1', '+6/+1', '+7/+2', '+7/+2', '+8/+3', '+8/+3', '+9/+4', '+9/+4', '+10/+5' ];
	static GOOD_SAVE= [ '+2', '+3', '+3', '+4', '+4', '+5', '+5', '+6', '+6', '+7', '+7', '+8', '+8', '+9', '+9', '+10', '+10', '+11', '+11', '+12' ];
	static POOR_SAVE = [ '+0', '+0', '+1', '+1', '+1', '+2', '+2', '+2', '+3', '+3', '+3', '+4', '+4', '+4', '+5', '+5', '+5', '+6', '+6', '+6' ];
	static LEVEL = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20'];
	static HEADERS = {
		'fr': {
			level : 'Niveau',
			bba : 'Bonus de base à l’attaque',
			fortitude: 'Bonus de base de Vigueur',
			reflex: 'Bonus de base de Réflexes',
			will: 'Bonus de base de Volonté',
			special: 'Spécial'
		},
		'en': {
			level : 'Level',
            bba : 'Base attack bonus',
            fortitude: 'Base Fortitude bonus',
            reflex: 'Base reflex bonus',
            will: 'Base will bonus',
            special: 'Special'
		}
	}

	constructor(table_data) {
			super();
			this.lang = 'fr';
			this.table_data = table_data;

			document.addEventListener('selectedLanguage', (e) => {
                this.lang = e.detail.language;
                this.render();
            });

			if (this.table_data['bba'] === 'GOOD') {
				this.bba = ClassTable.GOOD_BBA;
			} else if (this.table_data['bba'] === 'AVERAGE') {
				this.bba = ClassTable.AVERAGE_BBA;
			} else if (this.table_data['bba'] === 'POOR') {
				this.bba = ClassTable.POOR_BBA
			}

			this.fortitude = this.table_data['fortitude'] === 'GOOD' ? ClassTable.GOOD_SAVE : ClassTable.POOR_SAVE;
			this.reflex = this.table_data['reflex'] === 'GOOD' ? ClassTable.GOOD_SAVE : ClassTable.POOR_SAVE;
			this.will = this.table_data['will'] === 'GOOD' ? ClassTable.GOOD_SAVE : ClassTable.POOR_SAVE;

			this.render();
	}

	render() {
		let rows = []

		this.table_data['special'][`${this.lang}`].forEach((item, index) => {
			rows.push([
				ClassTable.LEVEL[index],
				this.bba[index],
				this.fortitude[index],
				this.reflex[index],
				this.will[index],
				item
			]);
		});

		let level = ClassTable.HEADERS[`${this.lang}`].level;
		let bba = ClassTable.HEADERS[`${this.lang}`].bba;
		let fortitude = ClassTable.HEADERS[`${this.lang}`].fortitude;
		let reflex = ClassTable.HEADERS[`${this.lang}`].reflex;
		let will = ClassTable.HEADERS[`${this.lang}`].will;
		let special = ClassTable.HEADERS[`${this.lang}`].special;

		let formatedRows = rows.map(row => this.#formatTableRow(row)).join('');

		this.innerHTML = `
		<table>
			<thead>
				<tr>
					<th>${level}</th>
					<th>${bba}</th>
					<th>${fortitude}</th>
					<th>${reflex}</th>
					<th>${will}</th>
					<th>${special}</th>
				</tr>
			</thead>
			<tbody>` + formatedRows + `
			</tbody>
		</table>`;
	}

	#formatTableRow(rowContent) {
		return `<tr>
			${rowContent.map(cellContent => this.#formatTableCell(cellContent)).join('')}
		</tr>
		`;
	}

	#formatTableCell(cellContent) {
		return `<td>${cellContent}</td>`;
	}
}

customElements.define('class-table', ClassTable);
