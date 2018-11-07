package info.adavis.ufosightings.addsighting

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter.createFromResource
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import info.adavis.ufosightings.R
import info.adavis.ufosightings.home.MainActivity
import info.adavis.ufosightings.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_add_sighting.*
import org.jetbrains.anko.toast

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

            it.getErrorMessage().observe(this, Observer { message ->
                displayMessage(message)
            })

            it.getSuccessMessage().observe(this, Observer { message ->
                displayMessage(message)
                startActivity(Intent(this, MainActivity::class.java))
            })
        }
    }

    private fun displayMessage(message: String?) {
        message?.let { toast(it) }
    }

    private fun obtainViewModel(): AddSightingViewModel = obtainViewModel(AddSightingViewModel::class.java)
}
