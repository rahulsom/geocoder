Geocoder
====

Geocoder Client that connects to several Geocoders. Written in groovy.

Usage
----

To get it into your groovy script:

```groovy
@Grab('com.github.rahulsom:geocoder:1.0')
```

To geocode something with google:

```groovy
def g = new GoogleCoder()
latlng = g.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043")
println "Center: $latlng"
```

This prints

```
37.4219998,-122.0839596
```

To reverse code a latlng, this time with Geonames:

```groovy
def latlng2 = new LatLng(lat: 37.4219998,lng: -122.0839596)
def address = new GeonamesCoder('demo').decode(latlng2) //?.normalize()
println address
```

This prints

```
com.github.rahulsom.geocoder.domain.Address(1646, Charleston Rd, Mountain View, CA, US, 94043)
```

You need to sign up for an account with Geonames to make this work.
