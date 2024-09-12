package ddwu.com.mobile.naverretrofittest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.naverretrofittest.data.MemoDao
import ddwu.com.mobile.naverretrofittest.data.MemoDatabase
import ddwu.com.mobile.naverretrofittest.databinding.ActivityShowMemoBinding
import ddwu.com.mobile.naverretrofittest.databinding.ActivityShowMemoListBinding
import ddwu.com.mobile.naverretrofittest.ui.MemoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowMemoListActivity: AppCompatActivity() {

    val showMemoListBinding by lazy {
        ActivityShowMemoListBinding.inflate(layoutInflater)
    }

    val memoDB : MemoDatabase by lazy {
        MemoDatabase.getDatabase(this)
    }

    val memoDao : MemoDao by lazy {
        memoDB.memoDao()
    }

    val adapter : MemoAdapter by lazy {
        MemoAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(showMemoListBinding.root)

        showMemoListBinding.rvMemo.adapter = adapter
        showMemoListBinding.rvMemo.layoutManager = GridLayoutManager(this, 3)

        showMemoListBinding.btnAdd.setOnClickListener {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

        adapter.setOnItemClickListener(object: MemoAdapter.OnMemoItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent (this@ShowMemoListActivity, ShowMemoActivity::class.java )
                intent.putExtra("memoDto", adapter.memoList?.get(position))
                startActivity(intent)
            }
        })

        showAllMemo()
    }

    fun showAllMemo() {
        CoroutineScope(Dispatchers.Main).launch {
            memoDao.getAllMemos().collect { memos ->
                adapter.memoList = memos
                adapter.notifyDataSetChanged()
            }
        }
    }
}
