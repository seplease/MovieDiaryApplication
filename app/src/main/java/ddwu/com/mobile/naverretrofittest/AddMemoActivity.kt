package ddwu.com.mobile.naverretrofittest

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import ddwu.com.mobile.naverretrofittest.data.MemoDao
import ddwu.com.mobile.naverretrofittest.data.MemoDatabase
import ddwu.com.mobile.naverretrofittest.data.MemoDto
import ddwu.com.mobile.naverretrofittest.databinding.ActivityAddMemoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddMemoActivity : AppCompatActivity() {

    val addMemoBinding by lazy {
        ActivityAddMemoBinding.inflate(layoutInflater)
    }

    val memoDB : MemoDatabase by lazy {
        MemoDatabase.getDatabase(this)
    }

    val memoDao : MemoDao by lazy {
        memoDB.memoDao()
    }

    companion object {
        const val REQUEST_SELECT_LOCATION = 123
        const val KEY_SELECTED_LOCATION = "selected_location"
    }
    private var imageUrl: String? = null
    private var selectedLocation: LatLng? = null
    private lateinit var geocoder: Geocoder
    private var theater: String = "영화관 정보"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(addMemoBinding.root)

        imageUrl = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        geocoder = Geocoder(this, Locale.getDefault())

        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .into(addMemoBinding.ivAddPhoto)
        }

        addMemoBinding.tvTitle.text = title

        addMemoBinding.btnAdd.setOnClickListener {
            val date = addMemoBinding.tvDate.text.toString()
            val memo = addMemoBinding.tvAddMemo.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val memoDto = MemoDto(0, imageUrl!!, title!!, date, theater, memo)
                Log.d("MemoInsert", "Inserting Memo: $memoDto")
                memoDao.insertMemo(memoDto)

                runOnUiThread {
                    Toast.makeText(
                        this@AddMemoActivity,
                        "새 메모가 추가되었습니다!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@AddMemoActivity, ShowMemoListActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }

        addMemoBinding.btnSelectLocation.setOnClickListener {
            val intent = Intent(this@AddMemoActivity, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_SELECT_LOCATION)
        }

        addMemoBinding.tvTheater.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("위치 정보")
                .setPositiveButton("확인", null)
                .create()

            CoroutineScope(Dispatchers.Main).launch {
                val address = getAddressFromLocation(selectedLocation?.latitude ?: 0.0, selectedLocation?.longitude ?: 0.0)
                dialog.setMessage("주소: ${address ?: "주소를 찾을 수 없습니다."}")
                dialog.show()
            }
        }

        addMemoBinding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String? {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == Activity.RESULT_OK) {
            val selectedLocation: LatLng? = data?.getParcelableExtra(KEY_SELECTED_LOCATION)

            this.selectedLocation = selectedLocation

            CoroutineScope(Dispatchers.Main).launch {
                val address = getAddressFromLocation(selectedLocation?.latitude ?: 0.0, selectedLocation?.longitude ?: 0.0)
                addMemoBinding.tvTheater.text = address ?: "주소를 찾을 수 없습니다."

                theater = address ?: "주소를 찾을 수 없습니다."
            }

            Log.d("TAG", "위도: ${selectedLocation?.latitude}\n경도: ${selectedLocation?.longitude}")
        }
    }
}
