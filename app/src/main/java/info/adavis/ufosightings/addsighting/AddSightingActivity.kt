package info.adavis.ufosightings.addsighting

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter.createFromResource
import androidx.core.widget.toast
import info.adavis.ufosightings.R
import info.adavis.ufosightings.home.MainActivity
import info.adavis.ufosightings.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_add_sighting.*

class AddSightingActivity : AppCompatActivity() {

    private lateinit var viewModel: AddSightingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sighting)

        val adapter = createFromResource(this,
                                             R.array.shapes_array,
                                             R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        spinner.adapter = adapter

        fab.setOnClickListener {
            viewModel.handleSaveSighting(
                    city_edit_text.text.toString(),
                    state_edit_text.text.toString().toLowerCase(),
                    country_edit_text.text.toString().toLowerCase(),
                    spinner.selectedItem.toString().toLowerCase(),
                    comments_edit_text.text.toString()
            )
        }

        viewModel = obtainViewModel().also {

            it.getErrorMessage().observe(this, Observer {
                displayMessage(it)
            })

            it.getSuccessMessage().observe(this, Observer {
                displayMessage(it)
                startActivity(Intent(this, MainActivity::class.java))
            })
        }
    }

    private fun displayMessage(message: String?) {
        message?.let { toast(it) }
    }

    private fun obtainViewModel(): AddSightingViewModel = obtainViewModel(AddSightingViewModel::class.java)
}
