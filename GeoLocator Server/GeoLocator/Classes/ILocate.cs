using GeoLocator.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace GeoLocator.Classes
{
    public interface ILocate
    {
        LocateData AddLocation(LocateData data);
        Dictionary<string, LocateData> GetLocation();
        string getBetween(string strSource, string strStart, string strEnd);
    }
}
