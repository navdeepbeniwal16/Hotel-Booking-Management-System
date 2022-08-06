```mermaid
classDiagram
  Hotelier -- HotelGroup: owns
  HotelGroup -- Staff: employs
  Staff -- Hotel: works at
  HotelGroup -- Hotel: owns
  Hotel -- Room: has
  Hotel -- Amenity: has
  Room -- Feature: has

  Booking -- Room: for

  Customer -- Booking: makes
  Booking -- DateRange: over

  Customer -- Documentation: has

  class Hotelier {
    name
  }
  class HotelGroup {
    businessDetails
  }
  class Staff {
    name
  }
  class Hotel {
    address
  }
  class Room {
    descriptions
    type
  }
  class Amenity {
    description
  }
  class Feature {
    description
  }

  class Booking {
    type
  }

  class DateRange {
    check-in
    check-out
  }

  class Customer {

  }

  class Documentation {

  }
```
