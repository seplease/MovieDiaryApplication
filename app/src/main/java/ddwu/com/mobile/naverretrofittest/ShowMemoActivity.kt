package ddwu.com.mobile.naverretrofittest

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import ddwu.com.mobile.naverretrofittest.data.MemoDao
import ddwu.com.mobile.naverretrofittest.data.MemoDatabase
import ddwu.com.mobile.naverretrofittest.data.MemoDto
import ddwu.com.mobile.naverretrofittest.databinding.ActivityShowMemoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ShowMemoActivity : AppCompatActivity() {

    val showMemoBinding by lazy {
        ActivityShowMemoBinding.inflate(layoutInflater)
    }

    val memoDB : MemoDatabase by lazy {
        MemoDatabase.getDatabase(this)
    }

    val memoDao : MemoDao by lazy {
        memoDB.memoDao()
    }

    lateinit var memoDto : MemoDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(showMemoBinding.root)

        showMemoBinding.btnModify.setOnClickListener {
            val updatedMemo = showMemoBinding.tvMemo.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                memoDto.memo = updatedMemo
                memoDao.updateMemo(memoDto)

                runOnUiThread {
                    Toast.makeText(
                        this@ShowMemoActivity,
                        "메모를 수정하였습니다!",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
            }
        }

        showMemoBinding.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                memoDao.deleteMemo(memoDto)

                runOnUiThread {
                    Toast.makeText(
                        this@ShowMemoActivity,
                        "메모를 삭제하였습니다!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@ShowMemoActivity, ShowMemoListActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }

        showMemoBinding.btnClose.setOnClickListener {
            finish()
        }

        memoDto = intent.getSerializableExtra("memoDto") as MemoDto

        Glide.with(this)
            .load(memoDto.poster_path)
            .into(showMemoBinding.ivPhoto)
        showMemoBinding.tvTitle.setText(memoDto.title)
        showMemoBinding.tvDate.setText(memoDto.date)
        showMemoBinding.tvTheater.setText(memoDto.theater)
        showMemoBinding.tvMemo.setText(memoDto.memo)
    }
}
