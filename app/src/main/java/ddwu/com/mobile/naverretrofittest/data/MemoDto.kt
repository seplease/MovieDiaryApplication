package ddwu.com.mobile.naverretrofittest.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "memo_table")
data class MemoDto(
    @PrimaryKey (autoGenerate = true)
    val id: Long,
    var poster_path: String,
    var title: String,
    var date: String,
    var theater: String,
    var memo: String) : Serializable {
    override fun toString(): String {
        return "${id} / ${poster_path} / $title / $date / $theater / $memo"
    }
}
