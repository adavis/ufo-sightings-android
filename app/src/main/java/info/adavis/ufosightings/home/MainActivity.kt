package info.adavis.ufosightings.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import info.adavis.ufosightings.R
import info.adavis.ufosightings.SightingsQuery
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sighting_list_item.*
import java.text.SimpleDateFormat
import java.util.Locale

private const val DATE_FORMAT = "yyyy-MM-dd"

class MainActivity : AppCompatActivity(), MainView {

    private val presenter: MainPresenter by lazy { MainPresenter(this) }
    private var adapter: SightingsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = SightingsAdapter(emptyList())
        sightings_list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter.getTopSightings()
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun displaySightings(sightings: List<SightingsQuery.Sighting>) {
        runOnUiThread {
            adapter?.values = sightings
            adapter?.notifyDataSetChanged()
        }
    }

    override fun displayError(message: String?) {
        message?.let { runOnUiThread { toast(it) } }
    }

    inner class SightingsAdapter constructor(
            var values: List<SightingsQuery.Sighting>
    ) : RecyclerView.Adapter<SightingsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sighting_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val sighting = values[position]
            holder.comment_text.text = sighting.comments()
            holder.date_text.text = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(sighting.date())

            val imageResId = when(sighting.shape()) {
                "circle", "oval" -> R.drawable.ufo_circle_oval

                "disk" -> R.drawable.ufo_disk

                "light" -> R.drawable.ufo_light

                "sphere" -> R.drawable.ufo_sphere

                "triangle" -> R.drawable.ufo_triangle

                else -> 0
            }
            holder.icon_image.setImageResource(imageResId)
        }

        override fun getItemCount(): Int = values.size

        // this requires the experimental flag for now, it's in the build.gradle file
        inner class ViewHolder(override val containerView: View?)
            : RecyclerView.ViewHolder(containerView), LayoutContainer
    }
}
