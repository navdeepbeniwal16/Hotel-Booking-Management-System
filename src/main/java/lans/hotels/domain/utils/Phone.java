package lans.hotels.domain.utils;

import lans.hotels.domain.IValueObject;

public class Phone implements IValueObject {
    int countryCode;
    int areaCode;
    int number;

    public Phone(int countryCode, int areaCode, int number) {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.number = number;
    }

    public String toString() {
        return "+" + "(" + countryCode + areaCode + ")" + " " + number;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != Phone.class) return false;
        boolean sameCountry = this.countryCode == ((Phone) other).countryCode;
        boolean sameArea = this.areaCode == ((Phone) other).areaCode;
        boolean sameNumber = this.number == ((Phone) other).number;
        return sameCountry && sameArea && sameNumber;
    }
}
