using GeoLocator.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using System.IO;

namespace GeoLocator.Classes
{
    public class Locator : ILocate
    {
        private readonly Dictionary<string, LocateData> _location;

        public Locator()
        {
            _location = new Dictionary<string, LocateData>();
        }

        /// <summary>
        /// Funktion zum Hinzufügen von Datein (IMEI, Longitude, Latetude) als Objekt in ein Dictionary
        /// </summary>
        /// <param name="data">
        /// Objekt der Klasse LocateData mit:
        /// string IMEI
        /// string Longetude 
        /// string Latetude 
        /// </param>
        /// <returns>
        /// Das Objekt data mit den Elementen  
        /// </returns>
        public LocateData AddLocation(LocateData data)
        {
            try
            {
                if (_location.ContainsKey(data.IMEI)) //falls Key bze IMEI oder IP schon Vorhanden ist, wird der Standort des Gerätes geupdatet 
                {
                    _location[data.IMEI] = data;
                }
                else
                {
                    _location.Add(data.IMEI, data); //falls Key bzw IMEI oder IP nicht voranden ist, wird es hinzugefügt
                }
#pragma warning disable CS0168 // Variable is declared but never used
            }
            catch (ArgumentNullException ignore){ }
#pragma warning restore CS0168 // Variable is declared but never used
            return data;
        }
        /// <summary>
        /// Gibt das Dictionary zurück
        /// </summary>
        /// <returns>
        /// das im Konstruktor erstellte Dictionary
        /// </returns>
        public Dictionary<string, LocateData> GetLocation()
        { 
            return _location;
        }

        public string getBetween(string strSource, string strStart, string strEnd)
        {
            int Start, End;
            if (strSource.Contains(strStart) && strSource.Contains(strEnd))
            {
                Start = strSource.IndexOf(strStart, 0) + strStart.Length;
                End = strSource.IndexOf(strEnd, Start);
                return strSource.Substring(Start, End - Start);
            }
            else
            {
                return "";
            }
        }

    }
}
