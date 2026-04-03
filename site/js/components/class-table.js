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
			bba : 'BBA',
			fortitude: 'Vigueur',
			reflex: 'Réflexes',
			will: 'Volonté',
			special: 'Spécial'
		},
		'en': {
			level : 'Level',
            bba : 'BAB',
            fortitude: 'Fortitude',
            reflex: 'Reflex',
            will: 'Will',
            special: 'Special'
		}
	}

	constructor(table_data) {
			super();
			this.lang = LanguageSelector.getLanguage();
			this.table_data = table_data;

			document.addEventListener('selectedLanguage', () => {
                this.lang = LanguageSelector.getLanguage();
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
		const h = ClassTable.HEADERS[`${this.lang}`];
		const rows = this.table_data['special'][`${this.lang}`].map((special, index) => ({
			level:     ClassTable.LEVEL[index],
			bba:       this.bba[index],
			fortitude: this.fortitude[index],
			reflex:    this.reflex[index],
			will:      this.will[index],
			special
		}));

		const formattedRows = rows.map((row, index) => this.#formatRow(row, index)).join('');

		this.innerHTML = `
		<style>
			.ct-wrapper {
				overflow-x: auto;
				width: 100%;
				border-radius: 8px;
				border: 1px solid rgba(139, 94, 60, 0.3);
			}

			.ct {
				width: 100%;
				border-collapse: collapse;
				font-family: 'LibreBaskerville', 'Georgia', serif;
			}

			.ct thead tr {
				background-color: #16213e;
				color: #c4956a;
				font-weight: bold;
				border-bottom: 2px solid rgba(196, 149, 106, 0.5);
			}

			.ct th,
			.ct td {
				padding: 8px 12px;
				text-align: left;
				white-space: nowrap;
				width: 1%;
			}

			.ct th.ct-cell-special,
			.ct td.ct-cell-special {
				width: 100%;
				white-space: normal;
			}

			.ct tbody tr.odd  { background-color: #2a2a3e; }
			.ct tbody tr.even { background-color: #1a1a2e; }
			.ct tbody tr:hover { background-color: #3a3a50; }
		</style>
		<div class="ct-wrapper">
			<table class="ct">
				<thead>
					<tr>
						<th>${h.level}</th>
						<th>${h.bba}</th>
						<th>${h.fortitude}</th>
						<th>${h.reflex}</th>
						<th>${h.will}</th>
						<th class="ct-cell-special">${h.special}</th>
					</tr>
				</thead>
				<tbody>
					${formattedRows}
				</tbody>
			</table>
		</div>`;
	}

	#formatRow(row, index) {
		const parity = index % 2 === 0 ? 'odd' : 'even';
		return `<tr class="${parity}">
			<td>${row.level}</td>
			<td>${row.bba}</td>
			<td>${row.fortitude}</td>
			<td>${row.reflex}</td>
			<td>${row.will}</td>
			<td class="ct-cell-special">${row.special}</td>
		</tr>`;
	}
}

customElements.define('class-table', ClassTable);
