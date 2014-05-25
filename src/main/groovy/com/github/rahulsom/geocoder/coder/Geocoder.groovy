package com.github.rahulsom.geocoder.coder

import com.github.rahulsom.geocoder.domain.Address
import com.github.rahulsom.geocoder.domain.LatLng

/**
 * Created by rahulsomasunderam on 5/23/14.
 */
public interface Geocoder {
  Address decode(LatLng latLng)
  LatLng encode(String address)
  boolean isValid()
}