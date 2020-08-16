const Web3 = require('web3');
const rpcURL = "https://mainnet.infura.io/v3/*YOUR-TOKEN*";
const web3 = new Web3(rpcURL);
const fs = require('fs');

let tx = [];
let nrtx = 0;
let block;
let bnr = 4595000;
let contractaddresses = [];
let nrofparents = 300;
let writeEveryXBlocks = 10;
let codearray = [];
let argv = [];
let cbc = 0;

let receipts = {};
let transactions = {};
let contracts = {};

function accuireLatestBlock() {
	return web3.eth.getBlockNumber();
}

//get the transactions out of a block
async function getTxs(carg){
	console.log("Processing " + nrofparents.toString() + " blocks");
	argv = carg
	console.log(bnr);
	block = await web3.eth.getBlock(bnr);
	tx = block.transactions;
	nrtx += tx.length;
	getContracts();
}

//put the transactions in a dictionary with the unique hash as the key. The transaction is also checked for contract creation, if it was, the contract's code gets copied
async function getContracts() {
	let receipt;
	let transaction;
	for (let i = 0; i < tx.length; i++) {
		receipt = await web3.eth.getTransactionReceipt(tx[i].toString());
		transaction = await web3.eth.getTransaction(tx[i].toString());
		transactions[transaction.hash] = transaction;
		receipts[receipt.transactionHash] = receipt;
		let from = receipt.from;
		let receipt1 = receipt.contractAddress;
		if (receipt1 != null){
			console.log(receipt1);
			contractaddresses.push(receipt1);
			contracts[receipt1] = await getCode(receipt1);
		}
	}
	if (nrofparents > 0) { //if there are still parent blocks to process: repeat
		nrofparents--;
		writeEveryXBlocks--;
		if (writeEveryXBlocks === 0) {
			console.log("Writing data to file");
			processContracts(argv[2]);
			writeEveryXBlocks = 10;
			receipts = {};
			transactions = {};
			contracts = {};
		}
		console.log("Scanning parent");
		gotoParent();
	}
	else {
		console.log("Accuired contracts");
		processContracts(argv[2]);
	}
}

async function getCode(address) {
	if (address === null) return 0;
	else {
		let code = await web3.eth.getCode(address);
		if (code != '0x') {
			return code;
		}
		else return 0;
	}
}


async function gotoParent() {
	let parentHash = block.parentHash;
	block = await web3.eth.getBlock(parentHash);
	console.log(block.number);
	tx = block.transactions;
	nrtx += tx.length;
	getContracts();
}

//this has two options (decided by a command line argument which data will be written to a file.
async function processContracts(manner) {
	if (manner === "code") {
		for (let i = 0; i < contractaddresses.length; i++) {
			let addressString = contractaddresses[i].toString();
			codearray[addressString] = await web3.eth.getCode(addressString);
		}
		for (i = 0; i < codearray.length; i++) {
			writeToFile("code.txt", codearray[i] + "\n");
		}
		console.log("done");
	}
	else if (manner === "json") {
		console.log("Saving receipts as JSON file");
		var receiptJson = JSON.stringify(receipts);
		var txJson = JSON.stringify(transactions);
		var codeJson = JSON.stringify(contracts);
		writeToFile("transactions1.json", txJson);
		writeToFile("receipts1.json", receiptJson);
		writeToFile("code1.json", codeJson);
	}
	else {
		console.log("ERROR: This manner is not supported: " + manner);
	}
}


function writeToFile(name, data) {
	fs.appendFile(name, data, function(err) {if (err) {
		return console.error(err);
		}
		console.log("Written successfully!")}); 
}

getTxs(process.argv); //optional argument from command line to specify the manner of processing.