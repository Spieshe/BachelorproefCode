const fs = require('fs');
const Web3 = require('web3');
const rpcURL = "https://mainnet.infura.io/v3/*YOUR-TOKEN*";
const web3 = new Web3(rpcURL);

let filename;
let transactions = {};
let stamps = {};
let transactionValues;


function readJson(file, wndw) {
	fs.readFile("blockStamps.json", (err, st) => {
		if(err) {
			console.log("Error reading file", err);
			return;
		}
		try {
			stamps = JSON.parse(st);
			console.log("Succesfully read the file!");
		}
		catch(e) {
			console.log("Error parsing the file", e);
		};
	});
	console.log("starting at block: " + wndw);
	fs.readFile(file, (err, s) => {
		if(err) {
			console.log("Error reading file", err);
			return;
		}
		try {
			transactions = JSON.parse(s);
			transactionValues = Object.values(transactions);
			console.log("Succesfully read the file!");
		}
		catch(e) {
			console.log("Error parsing the file", e);
		};
		console.log("Checking transactions");
		let res = getBlocksInWindow2(wndw, 0);
		filterUsers(res[1]);
		let strt = res[0];
		for(let i = 1; i > 0; i++) {
			let ret_val = repeat(transactionValues[strt].blockNumber, strt);
			if(ret_val == 0) {
				break;
			}
			strt = ret_val;
		}
		forAllUsers();
		makeUidDict();
		countContracts();
	});
}

let filtered = []

function countContracts() {
	let seen = [];
	for(entry of filtered) {
		if(seen.indexOf(entry[0]) == -1)
			seen.push(entry[0]);
	}
	console.log("USERS AMOUNT: " + seen.length);
}

function makeUidDict() {
	console.log("Creating uid dict");
	let i = 0;
	console.log(filtered[0][0]);
	console.log("LENGTH: " + filtered.length);
	let string = "";
	for(entry of filtered) { 
		string += entry[0] + " --> " + entry[1] + " = " + i + "\n";
		i++;
	} 
	writeToFile("dict.txt", string);
}

function writeToFile(name, data) {
	fs.appendFile(name, data, function(err) {if (err) {
		return console.error(err);
		}
		console.log("Written successfully!")}); 
}

//will sort every transaction in a window for every user
function forAllUsers(){
	console.log("Generating files");
	for(index in users){
		forUser(index);
	}
	console.log("DONE");
}

function forUser(nr) {
	let user = txs[nr];
	for(let i = 0; i < user.length; i++){
		for(let j = 0; j < user[i].length; j++){
			if(!checkDupe(user[i][j], filtered)){
				filtered.push(user[i][j]);
			}
		}
	}
	toTxt(user, filtered, nr);
}

//filters out duplicate transactions in one window of 5 minutes
function checkDupe(array1, array2){
	if(array1.length === 0 || array2.length === 0){
		console.log("one array was empty");
		return false;
	}
	for(let k = 0; k < array2.length; k++){
		if(equals(array1, array2[k]))
			return true;
	}
	return false;
}

function equals(a1, a2){
	if(a1.length != a2.length)
		return false;
	for(let l = 0; l < a1.length; l++){
		if(a1[l] === a2[l])	
			continue;
		else
			return false;
	}
	return true;
}

function indexOf(el, array){
	for(let i = 0; i < array.length; i++){
		if(equals(el, array[i]))
			return i;
	}
	return -1;
}

function toTxt(user, filtered, uid){
	if(!checkUsefulness(user))
		return;
	for(let i = 0; i < user.length; i++){
		let seen = [];
		for(let j = 0; j < user[i].length; j++){
			let entry = user[i][j];
			let index = indexOf(entry, filtered);
			if(seen.indexOf(index) != -1){
				continue;
			}
			seen.push(index);
			fs.appendFileSync("./fimsets/fim" + uid + ".txt", index + " ", function(err) {if (err) {
				return console.error(err);
				}
			});
		}
		fs.appendFileSync("./fimsets/fim" + uid + ".txt", "\n", function(err) {if (err) {
				return console.error(err);
				}
		});
	}
}

function checkUsefulness(user){
	if(user.length === 1) //if the user's transaction window has but one transaction we do not consider it
		return false;
	for(index in user){
		if(user[index].length < 2)
			continue;
		else
			return true;
	}
	return false;
}

function repeat(wndw, start) {
	console.log("repeating for: " + wndw);
	let res = getBlocksInWindow2(wndw, start);
	console.log(Object.keys(res[1]).length);
	if(Object.keys(res[1]).length == 0) {
		console.log("No more blocks");
		return 0;
	}
	filterUsers(res[1]);
	return res[0];
}

function getBlocksInWindow2(startingBlock, start) {
	let blocks = {};
	let saved = 0;
	
	
	//Windowing on an exact amount of 5 minutes based on a block's unix timestamp
	let window_size = 300; //300 unix time is 5 minutes
	let begin_timestamp = stamps[startingBlock];
	let end_timestamp = begin_timestamp - window_size;
	let j = start;
	
	while(begin_timestamp >= end_timestamp) {
		let bNr = transactionValues[j].blockNumber;
		console.log(j + " " + bNr);
		for(j;j < transactionValues.length; j += 1) {
			let currNr = transactionValues[j].blockNumber;
			if(bNr == currNr) { //keep going if we're still in the same block
				let value = transactionValues[j];
				if(undefined == value) {
					console.log("undefined transaction");
					return [j, blocks];
				}
				let key = value.hash;
				let transaction = {};
				transaction[key] = value;
				blocks = {...blocks, ...transaction};
			}
			else {
				if(Math.abs(bNr-currNr) > 2) { // this test is to prevent going from like block 4.595000 to 4594000 in one step if they happen to be agjacent in the json file
					console.log("Reached a new block point: " +bNr+" "+currNr);
					return [j+1, blocks];
				}
				let new_stamp = stamps[currNr]; //update the timestamp and repeat
				begin_timestamp = new_stamp;
				break;
			}
		}
		if(j >= transactionValues.length)
			return [0, blocks]; //returning 0 here means we have processed everything
	}
	return [j+1, blocks]; // else we return the index we stopped at for the next loop to resume.
}

let users = [];
let txs = [];
//this filters every transaction to an entry in an array correponding to the user that mmade them. The entry in the array is again an array with as entries the 5 minute windows
function filterUsers(blocks) {
	let tempT = [];
	let tempU = [];
	let blocksArray = Object.values(blocks);
	for(transaction of blocksArray){
		let user = transaction.from;
		let index = users.indexOf(user);
		let indexTempU = tempU.indexOf(user);
		if(index == -1){
			users.push(user);
			index = users.indexOf(user);
			txs[index] = [];
		}
		indexTempU = tempU.indexOf(user);
		if(indexTempU == -1) {
			tempU.push(user);
			indexTempU = tempU.indexOf(user);
			tempT[indexTempU] = [];
		}
		let tx = [transaction.from, transaction.to];
		tempT[indexTempU].push(tx);
	}
	for(idx in tempT) {
		let tx = tempT[idx];
		let user = tempU[idx];
		let index = users.indexOf(user);
		txs[index].push(tx);
	}
}

readJson("txToContract.json", 4595000);