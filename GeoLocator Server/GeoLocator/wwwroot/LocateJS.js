 function locate() {
      navigator.geolocation.getCurrentPosition(success, error);
      function success(pos) {
            var crd = pos.coords;
            r = new XMLHttpRequest();
            r.open('POST', 'http://localhost:5000/v1/Locate', true);
            r.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            var IMEI = prompt("Please enter your IMEI, if no IMEI enter IP Adress.");
            r.send('longetude='+crd.longitude+'&latetude='+crd.latitude+'&IMEI='+IMEI);
      };
         function error(error) {
            switch(error.code) {
              case error.PERMISSION_DENIED:
                  alert("Benutzer erlaubt keinen Zugriff auf Geolocation.")
                  break;
              case error.POSITION_UNAVAILABLE:
                  alert("Ortsinformationen nicht verfügbar.")
                  break;
              case error.TIMEOUT:
                  alert("Timeout.")
                  break;
              case error.UNKNOWN_ERROR:
                  alert("Unbekannter Fehler.")
                  break;
              default:
           }
     } 
}

function getLocation() {
    var url = "http://localhost:5000/v1/Locate";
    var IMEI2 = prompt("Please enter your IMEI, if no IMEI enter IP Adress.");
    var newURL = url + "/" + IMEI2;
  

    r = new XMLHttpRequest();

    r.open('GET', newURL, true);
    r.send();
    r.onreadystatechange = processRequest;

    function processRequest() {
        if (r.readyState == 4 && r.status == 200) {
            var pars = JSON.parse(r.response);
            var long = pars[IMEI2]["longetude"]
            var lat = pars[IMEI2]["latetude"]
            alert("Longitude: " + long + " " + "Latetude: " + lat);
            lat1 = lat;
            long1 = long;
        }
    }; 
}
var lat1;
var long1;
function newWindowMap() {
    var out = "https://www.google.de/maps/@" + lat1 + "," + long1 + "," + "15z";
    return out;
}

var arrayout = [];
var finalcount;
function getHistory() {
    var url = "http://localhost:5000/v1/Locate";
    var IMEI3 = prompt("Please enter your IMEI, if no IMEI enter IP Adress.");
    var newURL = url + "/" + IMEI3 + "/History";
    r = new XMLHttpRequest();

    r.open('GET', newURL, true);
    r.send();
    r.onreadystatechange = processRequest;

    function processRequest() {
        if (r.readyState == 4 && r.status == 200) {
            var pars = JSON.parse(r.response);
            var ent = 0;
            var table = "";
            var count = 0;
            for (var c in pars) {
                count++;
            }
            
            count = count / 3;
            finalcount = count;
            var array = [];
            
            for (var i = 0; i < count; i = 0) {
                var temp = parseInt(parseInt(i) + 1);
                var temp2 = parseInt(parseInt(i) + 2);
                var long = pars[temp];              
                var lat = pars[temp2];
                var time = pars[i];
                array.push(long);
                array.push(lat);
                array.push(time)
                //alert("Entry: " + ent + " at Time: " + time +  " : " + "Longitude: " + long + " Latetude: " + lat);
                ent++;
                pars = pars.slice(3);
                if (ent == count) {
                    break;
                }
                lat1 = lat;
                long1 = long;
            }
            arrayout = array;
            show();
        }
    };
   
}

function show() {
    var locations = [];
    for (var i = 0; i < finalcount; i++) {
        locations.push([arrayout.pop(), arrayout.pop(), arrayout.pop()])
        
    }

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 10,
        center: new google.maps.LatLng(52.4396086, 13.2749335),
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    var infowindow = new google.maps.InfoWindow();

    var marker, i;

    for (i = 0; i < locations.length; i++) {
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(locations[i][1], locations[i][2]),
            map: map
        });

        google.maps.event.addListener(marker, 'click', (function (marker, i) {
            return function () {
                infowindow.setContent("Entry: " + parseInt(locations.length - parseInt(i)) + " at Time: " + locations[i][0]);
                infowindow.open(map, marker);
            }
        })(marker, i));
    }

    var flightpath = [];
    for (i = 0; i < locations.length; i++) {
        flightpath[i] = new google.maps.LatLng(locations[i][1], locations[i][2]);
    }
    var tourpln = new google.maps.Polyline({
        path: flightpath,
        strokeColor: "#FF0000",
        strokeOpacity: 1.0,
        strokeWeight: 2
    });
    tourpln.setMap(map);
}




