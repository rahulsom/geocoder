package com.github.rahulsom.geocoder.domain

/**
 * Created by rahulsomasunderam on 5/5/14.
 */
@groovy.transform.Immutable
class LatLng {
	double lat, lng

	@Override
	String toString() {
		"$lat,$lng"
	}
}

