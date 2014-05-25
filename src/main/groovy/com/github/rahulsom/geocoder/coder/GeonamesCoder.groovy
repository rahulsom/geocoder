package com.github.rahulsom.geocoder.coder

import com.github.rahulsom.geocoder.domain.Address
import com.github.rahulsom.geocoder.domain.LatLng
import groovy.transform.TupleConstructor
import groovy.transform.TypeChecked
import groovyx.net.http.HTTPBuilder

@TupleConstructor
@TypeChecked
class GeonamesCoder implements Geocoder{
  private Date invalidationTime = null
	String username = 'demo'

	Address decode(LatLng latLng) {
		def http = new HTTPBuilder('http://api.geonames.org/')

		def resp = http.get(
				path: 'findNearestAddressJSON',
				query: [lat: latLng.lat, lng: latLng.lng, username: username, style: 'full', formatted: 'true']
		) as Map<String, String>
		def address = resp.address as Map<String, String>
		if (!address) {
			return null
		}

		if (!address.countryCode) {
			return null
		}
		if (!address.postalcode) {
			return null
		}
		if (!address.placename) {
			return null
		}
		new Address(
				streetNumber: address.streetNumber,
				streetName: address.street,
				city: address.placename,
				state: address.adminCode1,
				country: address.countryCode,
				zip: address.postalcode,
		)
	}

  @Override
  LatLng encode(String address) {
    throw new Exception("Method not implemented")
  }

  @Override
  boolean isValid() {
    !invalidationTime
  }
}