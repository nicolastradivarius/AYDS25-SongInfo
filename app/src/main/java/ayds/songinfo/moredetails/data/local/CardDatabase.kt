package ayds.songinfo.moredetails.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(entities = [CardEntity::class], version = 1)
abstract class CardDatabase : RoomDatabase() {
    abstract fun CardDao(): CardDao
}

@Entity(primaryKeys = ["name", "source"])
data class CardEntity(
    val name: String,
    val content: String,
    val url: String,
    val source: Int,
    val logoUrl: String
)

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardEntity)

    @Query("SELECT * FROM CardEntity WHERE name LIKE :card")
    fun getCardByArtistName(card: String): List<CardEntity>

}