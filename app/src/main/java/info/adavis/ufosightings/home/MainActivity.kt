package info.adavis.ufosightings.home

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.adavis.ufosightings.R
import info.adavis.ufosightings.SightingsQuery
import info.adavis.ufosightings.addsighting.AddSightingActivity
import info.adavis.ufosightings.util.obtainViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sighting_list_item.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.Locale

private const val DATE_FORMAT = "yyyy-MM-dd"

class MainActivity : AppCompatActivity() {

    private var adapter: SightingsAdapter? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = SightingsAdapter(emptyList())
        sightings_list.adapter = adapter

        fab.setOnClickListener {
            viewModel.handleAddSightingClick()
        }

        viewModel = obtainViewModel().also {
            it.getSightings().observe(this, Observer { sightingsState ->
                sightingsState?.let { state ->
                    state.data?.let { data -> displaySightings(data) }
                }
            })

            it.getErrorMessage().observe(this, Observer { message ->
                displayError(message)
            })

            it.navigateToAddSighting.observe(this, Observer {
                startActivity(Intent(this, AddSightingActivity::class.java))
            })
        }
    }

    private fun displaySightings(sightings: List<SightingsQuery.Sighting>) {
        adapter?.let {
            it.values = sightings
            it.notifyDataSetChanged()
        }
    }

    private fun displayError(message: String?) {
        message?.let { toast(it) }
    }

    // region adapter
    inner class SightingsAdapter constructor(
            var values: List<SightingsQuery.Sighting>
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<SightingsAdapter.ViewHolder>() {

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
        inner class ViewHolder(override val containerView: View)
            : RecyclerView.ViewHolder(containerView), LayoutContainer
    }
    //endregion

    private fun obtainViewModel(): MainViewModel = obtainViewModel(MainViewModel::class.java)
}
