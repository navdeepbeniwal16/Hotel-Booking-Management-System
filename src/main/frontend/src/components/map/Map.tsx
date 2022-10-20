import React from 'react';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { Spinner } from 'react-bootstrap';
import Hotel from '../../types/HotelType';


const containerStyle = {
  width: '100%',
  height: '100%',
};

const center = {
  lat: -37.840935,
  lng: 144.9623,
};

interface MapProps {
  hotels: Array<Hotel>;
}

function Map({ hotels }: MapProps) {
  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: process.env.REACT_APP_MAPS || "",
  });

  const [map, setMap] = React.useState<GoogleMap>();
  const onLoad = React.useCallback(function callback(map: any) {
    const bounds = new window.google.maps.LatLngBounds(center);
    if (map) {
      map.fitBounds(bounds);
      setMap(map);
    }
  }, []);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      center={center}
      zoom={10}
      onLoad={onLoad}
    >
      {/* Child components, such as markers, info windows, etc. */}
      <></>
    </GoogleMap>
  ) : (
      <div>
        <h2>
          Loading map
        </h2>
        <Spinner animation="border" />
    </div>
  );
}

export default React.memo(Map);
