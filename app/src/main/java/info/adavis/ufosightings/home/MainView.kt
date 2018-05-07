package info.adavis.ufosightings.home

import info.adavis.ufosightings.SightingsQuery

interface MainView {

    fun displaySightings(sightings: List<SightingsQuery.Sighting>)

    fun displayError(message: String?)

}