package com.github.rahulsom.geocoder.domain

import java.security.SecureRandom

/**
 * Created by rahulsomasunderam on 5/5/14.
 */
@groovy.transform.ToString
@groovy.transform.Immutable
class Address {
	String streetNumber, streetName, city, state, country, zip

	String getStreet() {
		"$streetNumber $streetName"
	}

	Address normalize() {
		if (streetNumber == null) {
			new Address(this.properties + [streetNumber: new SecureRandom().nextInt(200).toString()])
		} else if (streetNumber.contains('-')) {
			new Address(this.properties + [streetNumber: streetNumber.split('-')[0]])
		} else {
			this
		}
	}
}