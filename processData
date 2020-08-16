const Web3 = require('web3');
const fs = require('fs');
const rpcURL = "https://mainnet.infura.io/v3/*YOUR-TOKEN*";
const web3 = new Web3(rpcURL);

let transactions;
let receipts;
let tKeys;
let rKeys;
let transactionsToContract = {};
let fimData = {};

function readJson(file) {
	console.log(file);
	fs.readFile(file[2], (err, s) => {
		if(err) {
			console.log("Error reading file", err);
			return;
		}
		try {
			transactions = JSON.parse(s);
			console.log("Succesfully read the file!");
		}
		catch(e) {
			console.log("Error parsing the file", e);
		};
		tKeys = Object.keys(transactions);
		console.log("Checking transactions");
		getTransactionsToContract(transactions, file);
	});
}

async function getTransactionsToContract(object, file) {
	let value;
	let key;
	console.log(tKeys.length);
	for(let i = 0; i < tKeys.length; i++) {
		console.log(i);
		key = tKeys[i];
		value = object[key];
		let from = value.from;
		let to   = value.to;
		else if(to != null) {
			let res = await web3.eth.getCode(to);
			if(res != '0x') { //0x is only if there is no code present
				transactionsToContract[value.hash] = value;
			}
		}
	}
	else {
	writeAsJson("txToContract2.json", transactionsToContract);
	}
}

function writeAsJson(name, object) {
	console.log("Saving txs as JSON file");
	var txJson = JSON.stringify(object);
	writeToFile(name, txJson);
}


function writeToFile(name, data) {
	fs.appendFile(name, data, function(err) {if (err) {
		return console.error(err);
		}
		console.log("Written successfully!")}); 
}

readJson(process.argv); // a command line argument for the file to be read
