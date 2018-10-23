using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using GeoLocator.Models;
using GeoLocator.Classes;
using Microsoft.AspNetCore.Cors;
using System.IO;
using System.Runtime.InteropServices.ComTypes;
using System.Text;

namespace GeoLocator.Controllers
{
    [Route("v1/Locate")]
    public class LocationController : Controller
    {
        private readonly ILocate _location;

        public LocationController(ILocate locaton)
        {
            _location = locaton; 
        }
        /// <summary>
        /// POST Request für die LocateData IMEI , Longetude und Latetude
        /// </summary>
        /// <param name="data">
        /// Objekt der Klasse LocateData mit:
        /// string IMEI
        /// string Longetude 
        /// string Latetude 
        /// </param>
        /// <returns>
        /// ModelState bzw NotFound()
        /// </returns>
        [HttpPost] 
        public ActionResult<LocateData> Location(LocateData data)
        {
            var outlocation = _location.AddLocation(data); //ruft die Funktion der klasse Locator auf um die daten in ein Dictionary mit IMEI als key zu speichern

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState); //Gibt Modelstate zurück 
            }

            if (outlocation == null)
            {
                return NotFound();
            }
            string path = @"C:\Users\Dan-PC\source\repos\GeoLocator Server\GeoLocator\LocationSaves.txt";
;

            string text = "IMEI= " + data.IMEI +  "  " + "Longitude= " + data.Longetude + "  " + "Latitude= " + data.Latetude + "  " + "Time= " + DateTime.Now.ToString("HH:mm:ss") + "|";
            using (var tw = new StreamWriter(path, true))
            {
                tw.WriteLine(text);
                tw.Close();
            }
            return null;
        }
        /// <summary>
        /// GET request für die Datein eines bestimmten Gerätes mit IMEI als key bzw id
        /// </summary>
        /// <param name="id">
        /// id ist die IMEI oder die IP Adresse des Gerätes
        /// </param>
        /// <returns>
        /// Das gesuchte Objekt(mittels id bestimmt) mit den Koordinaten oder Modelstate als return Wert 
        /// </returns>
        [HttpGet]
        [Route("{id}")]
        public ActionResult<Dictionary<string, LocateData>> GetLocation(String id)
        {
            Dictionary<string, LocateData> output = new Dictionary<string, LocateData>();
            string imei= "";
            string lng= "";
            string lat= "";

            string filePath = @"C:\Users\Dan-PC\source\repos\GeoLocator Server\GeoLocator\LocationSaves.txt"
            ;
            var filestream = new FileStream(filePath,FileMode.Open,FileAccess.Read,FileShare.ReadWrite);
            const Int32 BufferSize = 128;
            using (var fileStream = System.IO.File.OpenRead("LocationSaves.txt"))
            using (var streamReader = new StreamReader(fileStream, Encoding.UTF8, true, BufferSize))
            {
                string line;
                while ((line = streamReader.ReadLine()) != null)
                {
                    if (_location.getBetween(line, "IMEI= ", " ") == id)
                    {
                        imei = _location.getBetween(line, "IMEI= ", " ");
                        lng = _location.getBetween(line, "Longitude= ", "  ");
                        lat = _location.getBetween(line, "Latitude= ", "  ");

                    }
                   
                }
               }
            LocateData temp = new LocateData();
            temp.IMEI = imei;
            temp.Latetude = lat;
            temp.Longetude = lng;
            
            string key = id;

            var locationdata = _location.GetLocation();
            output.Add(temp.IMEI, temp);
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState); //Gibt Modelstate zurück 
            }
            else
            {
                return output;
            }
        }
        /// <summary>
        /// Stuff
        /// </summary>
        /// <param name="id"></param>
        /// <returns>
        /// Stuff
        /// </returns>
        [HttpGet]
        [Route("{id}/History")]
        public ActionResult<List<string>> GetHistory(String id)
        {
            List<string> output = new List<string>(); 
            string lng = "";
            string lat = "";
            string time ="";

            string filePath = @"C:\Users\Dan-PC\source\repos\GeoLocator Server\GeoLocator\LocationSaves.txt"
;
            var filestream = new FileStream(filePath, FileMode.Open, FileAccess.Read, FileShare.ReadWrite);
            const Int32 BufferSize = 128;
            using (var fileStream = System.IO.File.OpenRead("LocationSaves.txt"))
            using (var streamReader = new StreamReader(fileStream, Encoding.UTF8, true, BufferSize))
            {
                string line;
                while ((line = streamReader.ReadLine()) != null)
                {
                    if (_location.getBetween(line, "IMEI= ", " ") == id)
                    {
                        time = _location.getBetween(line, "Time= ", "|");
                        lng = _location.getBetween(line, "Longitude= ", "  ");
                        lat = _location.getBetween(line, "Latitude= ", "  ");
                        output.Add(time);
                        output.Add(lng);
                        output.Add(lat);
                    }
                }
            }
            return output;
        }
    }
}