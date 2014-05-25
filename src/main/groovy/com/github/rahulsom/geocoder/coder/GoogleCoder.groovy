package com.github.rahulsom.geocoder.coder

import com.github.rahulsom.geocoder.domain.Address
import com.github.rahulsom.geocoder.domain.LatLng
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovyx.net.http.HTTPBuilder

/**
 * Created by rahulsomasunderam on 5/5/14.
 */
class GoogleCoder implements Geocoder {
  private Date invalidationTime = null

  @Override
  LatLng encode(String address) {
    def http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/')
    def resp = http.get(
        path: 'json',
        query: [address: address, sensor: false]
    )

    switch (resp.status) {
      case 'OK':
        def lat = resp.results.geometry.location.lat.find { it } as double
        def lng = resp.results.geometry.location.lng.find { it } as double
        if (lat && lng)
          return new LatLng(lat: lat, lng: lng)
        else
          return null
        break
      case 'OVER_QUERY_LIMIT':
        invalidationTime = new Date()
        return null
        break
      default:
        invalidationTime = null
        return null;
    }
  }

  @Override
  Address decode(LatLng latLng) {
    def http = new HTTPBuilder('https://maps.googleapis.com/maps/api/geocode/')
    def resp = http.get(
        path: 'json',
        query: [latlng: latLng.toString(), sensor: false,]
    )
    switch (resp.status) {
      case 'OK':
        def components = resp.results[0].address_components
        if (!getComponent(components, 'country')) {
          return null
        }
        if (!getComponent(components, 'postal_code')) {
          return null
        }
        if (!getComponent(components, 'locality')) {
          return null
        }
        invalidationTime = null
        return new Address(
            streetNumber: getComponent(components, 'street_number'),
            streetName: getComponent(components, 'route'),
            city: getComponent(components, 'locality'),
            state: getComponent(components, 'administrative_area_level_1'),
            country: getComponent(components, 'country'),
            zip: getComponent(components, 'postal_code'),
        )
      break
      case 'OVER_QUERY_LIMIT':
        invalidationTime = new Date()
        return null
      break
      default:
        invalidationTime = null
        return null;
    }


  }

  private String getComponent(json, String type) {
    json.find {
      it.types.contains(type)
    }?.short_name
  }

  @Override
  boolean isValid() {
    if (invalidationTime) {
      TimeDuration duration = TimeCategory.minus(new Date(), invalidationTime)
      TimeDuration reference = new TimeDuration(1, 0, 0, 0)

      if (duration > reference) {
        invalidationTime = null
      }

      return !invalidationTime
    }
    return true
  }
}
