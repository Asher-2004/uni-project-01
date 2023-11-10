// Slide Show

var slideIndex = 1;
showDivs(slideIndex);

function plusDivs(n) {
  showDivs(slideIndex += n);
}

function currentDiv(n) {
  showDivs(slideIndex = n);
}

function showDivs(n) {
  var i;
  var x = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("img-click-button");
  if (n > x.length) { slideIndex = 1 }
  if (n < 1) { slideIndex = x.length }
  for (i = 0; i < x.length; i++) {
      x[i].style.display = "none";
  }
  for (i = 0; i < dots.length; i++) {
      dots[i].style.color = "#000";
      dots[i].style.textDecoration = "none";
      dots[i].style.fontSize = "1em";
  }
  x[slideIndex - 1].style.display = "block";
  dots[slideIndex - 1].style.color = "#ED2724";
  dots[slideIndex - 1].style.fontSize = "1.5em";
}


// const data = [
//   ['State', 'Population'],
//   ['New South Wales', 8000000],
//   ['Queensland', 5000000],
//   ['South Australia', 1700000],
//   ['Tasmania', 540000],
//   ['Victoria', 6600000],
//   ['Western Australia', 2600000],
//   ['Northern Territory', 245000],
//   ['ACT', 430000]
// ];
// google.charts.load('current', {
//   'packages':['geochart']
// });
// google.charts.setOnLoadCallback(drawRegionsMap);

// function drawRegionsMap() {
//   var chartData = google.visualization.arrayToDataTable(data);

//   var options = {
//     region: 'AU',
//     resolution: 'provinces',
//     colorAxis: { colors: ['#f4EC2E', '#ED2724'] },
//     backgroundColor: '#040806',
//     datalessRegionColor: '#EFEFEF',
//     defaultColor: '#EFEFEF',
// };

//   var chart = new google.visualization.GeoChart(document.getElementById('regions_div'));
//   chart.draw(chartData, options);
// }