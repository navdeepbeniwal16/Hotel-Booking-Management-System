package lans.hotels.domain.utils;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import java.util.HashMap;
import lans.hotels.domain.utils.District;

import static org.postgresql.core.JavaVersion.other;

public class Address {
    String line_1 = "";
    String line_2 = "";
    District district;
    String city;
    int postcode;

    public Address (String line_1, String line_2, District district, String city, int postcode) {
        this.line_1 = line_1;
        this.line_2 = line_2;
        this.district = district;
        this.city = city;
        this.postcode = postcode;
    }
    public String toString() {
        if(line_2!=null)
            return ""+ line_1 + "," +line_2+","+district+","+city+"-"+postcode;
        else
            return ""+ line_1 + "," +district+","+city+"-"+postcode;
    }

    public String getLine1 () {
        return this.line_1;
    }
    public String getLine2 () {
        return this.line_2;
    }

    public District getDistrict () {
        return this.district;
    }

    public String getCity () {
        return this.city;
    }
    public Integer getPostCode () {
        return this.postcode;
    }


    @Override
    public boolean equals(Object other) {
        if (other.getClass() != Address.class) return false;
        boolean sameline1 = this.line_1 == ((Address) other).line_1;
        boolean sameline2 = this.line_2 == ((Address) other).line_2;
        boolean sameDistrict = this.district.equals(((Address) other).district);
        boolean sameCity = this.city == ((Address) other).city;
        boolean samePostcode = this.postcode ==((Address) other).postcode;
        return sameline1 && sameline2 && sameDistrict && sameCity && samePostcode;
    }
}
