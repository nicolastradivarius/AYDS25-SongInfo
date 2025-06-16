package ayds.songinfo.moredetails.domain

interface OtherInfoRepository {
    fun getCards(artistName: String): List<Card>
}