package com.ustadmobile.ustadlocation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener, DownloadProgressView.OnStopDownloadListener {


    val locationList = arrayOf(
        Locations("United Arab Emirates","This will show how long it takes to download 4 hours of video content using Mobile Internet in United Arab Emirates", R.drawable.ic_flag_of_the_united_arab_emirates, 125000),
        Locations("United Kingdom","This will show how long it takes to download 4 hours of video content using Mobile Internet in United Kingdom", R.drawable.ic_flag_of_the_united_kingdom, 253000),
        Locations("Jordan","This will show how long it takes to download 4 hours of video content using Mobile Internet in Jordan", R.drawable.ic_flag_of_jordan, 526000),
        Locations("Kenya","This will show how long it takes to download 4 hours of video content using Mobile Internet in Kenya", R.drawable.ic_flag_of_kenya, 513000),
        Locations("Afghanistan","This will show how long it takes to download 4 hours of video content using Mobile Internet in Afghanistan", R.drawable.afg, 1072000),
        Locations("Peer to Peer","This will show how long it takes to download 4 hours of video content using Peer to Peer offline sharing", R.drawable.sharing_data, 120000)
    )


    private var mCountDownTimer: CountDownTimer? = null
    var selected: Locations? = null

    lateinit var title: TextView
    lateinit var downloadButton: Button
    lateinit var downloadProgress: DownloadProgressView
    lateinit var toolbar: Toolbar
    lateinit var thumbnail: ImageView
    lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = findViewById(R.id.entry_detail_title)
        downloadButton = findViewById(R.id.entry_download_open_button)
        downloadProgress = findViewById(R.id.entry_detail_progress)
        thumbnail = findViewById(R.id.entry_detail_thumbnail)
        description = findViewById(R.id.entry_detail_description)
        toolbar = findViewById(R.id.um_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        downloadButton.setOnClickListener(this)
        downloadProgress.setOnStopDownloadListener(this)
        setButtonTextLabel("Download")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.location_menu, menu)

        val item = menu!!.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter<Locations>(this, R.layout.spinner_text_view, locationList)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        return true
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {setDownloadProgressVisible(false)
        setDownloadButtonVisible(true)
        setDownloadProgressVisible(false)
        setButtonTextLabel("Download")
        mCountDownTimer?.cancel()
        selected = parent?.getItemAtPosition(position) as Locations
        thumbnail.setImageResource(selected!!.thumbnail)
        title.text = selected!!.title
        description.text = selected!!.description
    }


    fun setDownloadButtonVisible(visible: Boolean) {
        downloadButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setButtonTextLabel(textLabel: String) {
        downloadButton.text = textLabel
    }

    fun setDownloadProgressVisible(visible: Boolean) {
        downloadProgress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setDownloadProgressLabel(progressLabel: String) {
        downloadProgress.statusText = progressLabel
    }


    override fun onClick(v: View?) {
        setButtonTextLabel("Download")
        setDownloadButtonVisible(false)
        setDownloadProgressVisible(true)
        var i = 0
        downloadProgress.progress = 0.toFloat()
        mCountDownTimer = object : CountDownTimer(selected!!.totalTime, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                i++
                val data =  (i * 100/ (selected!!.totalTime / 1000)).toInt()
                downloadProgress.progress = data.toFloat() / 100
                setDownloadProgressLabel("Downloading")
            }

            override fun onFinish() { setDownloadProgressVisible(true)
                //Do what you want
                i++
                downloadProgress.progress = 100.toFloat()
                setDownloadProgressLabel("100")
                setDownloadProgressVisible(false)
                setButtonTextLabel("Completed")
                setDownloadButtonVisible(true)

            }
        }
        mCountDownTimer?.start()
    }

    override fun onClickStopDownload(view: DownloadProgressView) {
        setDownloadProgressVisible(false)
        setDownloadButtonVisible(true)
        setButtonTextLabel("Download")
        mCountDownTimer?.cancel()
    }
}

data class Locations(val title: String, val description: String, val thumbnail: Int, val totalTime: Long){

    override fun toString(): String {
        return title
    }

}
