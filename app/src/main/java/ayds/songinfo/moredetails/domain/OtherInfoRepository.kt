package ayds.songinfo.moredetails.domain

interface OtherInfoRepository {
    fun getArtistBiography(artistName: String): ArtistBiography
}