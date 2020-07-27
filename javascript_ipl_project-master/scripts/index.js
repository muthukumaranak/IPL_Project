getData();
getDeliveryData();

var yearID = 1;
var winnerID = 10;
var extraRunID = 11;

var startingMatchId = 0;
var endingMatchId = 60;

var match_ID = 0;
var bowler_ID = 8;
var totalRuns_ID = 17;

var columns = [];
var year = [];
var winner = [];

var bowler = []
var totalRunsGiven = []

const yearObj = {};
const winnerObj = {};
const extraRunsObj = {};

const bowlerAndBalls = {};
const bowlerRunsGiven = {};

async function getData() {
    const response = await fetch('dataset.csv');
    const data = await response.text();

    const table = data.split('\n').slice(1);
    table.forEach(row => {
        columns = row.split(',');
        year.push(columns[yearID]);
        winner.push(columns[winnerID]);
        if (columns[yearID] == 2016) {
            if (extraRunsObj[columns[winnerID]] === undefined) {
                extraRunsObj[columns[winnerID]] = columns[extraRunID];
            }
            else {
                var currentRun = Number(extraRunsObj[columns[winnerID]]);
                extraRunsObj[columns[winnerID]] = Number(columns[extraRunID]) + currentRun;
            }
        }
    });
}


async function getDeliveryData() {
    const response = await fetch('delivery.csv');
    const data = await response.text();
    const table = data.split('\n').slice(1);
    table.forEach(row => {
        columns = row.split(',');
        if (columns[match_ID] > startingMatchId && columns[match_ID] < endingMatchId) {
            bowler.push(columns[bowler_ID]);
            totalRunsGiven.push(columns[totalRuns_ID]);
        }
    });
}


function findmatchesPlayedPerYear() {
    console.clear();
    year.sort(function (a, b) { return a - b });
    console.log("Matches Played Per Year");
    for (var i = 0; i < year.length; i++) {
        if (yearObj[year[i]] === undefined) {
            yearObj[year[i]] = 0;
        }
        else {
            yearObj[year[i]]++;
        }
    }
    for (var i in yearObj) {
        console.log(`${i} ${yearObj[i]}`);
    }
}

function findmatchesWonPerTeam() {
    console.clear();
    winner.sort();
    console.log("Matches Won per Team");
    for (var i = 0; i < winner.length; i++) {
        if (winnerObj[winner[i]] === undefined) {
            winnerObj[winner[i]] = 0;
        }
        else {
            winnerObj[winner[i]]++;
        }
    }
    for (var i in winnerObj) {
        console.log(`${i} ${winnerObj[i]}`);
    }
}

function findextraRunsConcededPerTeam() {
    console.clear();
    console.log("Extra Runs Conceded per Team in 2016");
    for (var i in extraRunsObj) {
        console.log(`${i} ${extraRunsObj[i]}`);
    }

}

function findbowlerEconomy() {
    console.clear();
    console.log('Bowler Economy for 2017');
    for (var i = 0; i < bowler.length; i++) {
        if (bowlerAndBalls[bowler[i]] === undefined) {
            bowlerAndBalls[bowler[i]] = 0;
        }
        else {
            bowlerAndBalls[bowler[i]]++;
        }

        if (bowlerRunsGiven[bowler[i]] === undefined) {
            bowlerRunsGiven[bowler[i]] = totalRunsGiven[i];
        }
        else {
            var currentRun = Number(bowlerRunsGiven[bowler[i]]);
            bowlerRunsGiven[bowler[i]] = Number(totalRunsGiven[i]) + currentRun;
        }
    }

    for (var i in bowlerRunsGiven) {
        var runs = bowlerRunsGiven[i];
        var balls = bowlerAndBalls[i];
        var economy = (runs * 1.0) / (balls / 6 + (balls % 6) / 10.0);
        economy = economy.toFixed(2);
        console.log(i + " " + economy);
    }

}