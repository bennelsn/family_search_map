package model;

public class CustomLatLng implements Comparable<CustomLatLng>
{
	public Double lat;
	public Double lng;
	
	public CustomLatLng()
	{}
	
	public CustomLatLng(Double lat, Double lng)
	{
		this.lat = lat;
		this.lng = lng;
	}
	@Override
	public int compareTo(CustomLatLng rhs) 
	{
		if(rhs == null)
            return -1;


        if(this.lat == rhs.lat)
        {
        	return this.lng.compareTo(rhs.lng);
        }
        else
        	return this.lat.compareTo(rhs.lat);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lng == null) ? 0 : lng.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomLatLng other = (CustomLatLng) obj;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lng == null) {
			if (other.lng != null)
				return false;
		} else if (!lng.equals(other.lng))
			return false;
		return true;
	}
}
