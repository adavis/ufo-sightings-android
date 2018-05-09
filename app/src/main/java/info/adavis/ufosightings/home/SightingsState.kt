package info.adavis.ufosightings.home

import info.adavis.ufosightings.SightingsQuery

data class SightingsState(val data: List<SightingsQuery.Sighting>? = null)